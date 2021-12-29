package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO4;
import com.ruike.hme.app.service.HmeServiceSplitRecordService;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.repository.HmeEoJobSnLotMaterialRepository;
import com.ruike.hme.domain.repository.HmeServiceSplitRecordRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnReWorkMapper;
import com.ruike.hme.infra.mapper.HmeServiceSplitRecordMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.app.service.ItfServiceTransferIfaceService;
import com.ruike.itf.domain.vo.ServiceTransferIfaceInvokeVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 售后返品拆机表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 14:21:01
 */
@Service
@Slf4j
public class HmeServiceSplitRecordServiceImpl implements HmeServiceSplitRecordService {

    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final HmeServiceSplitRecordRepository hmeServiceSplitRecordRepository;
    private final HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    private final HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper;
    private final MtModLocatorRepository mtModLocatorRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final MtMaterialRepository mtMaterialRepository;
    private final ItfServiceTransferIfaceService itfServiceTransferIfaceService;
    private final LovAdapter lovAdapter;
    private final HmeEoJobSnReWorkMapper hmeEoJobSnReWorkMapper;
    private final MtModWorkcellRepository mtModWorkcellRepository;

    public HmeServiceSplitRecordServiceImpl(MtErrorMessageRepository mtErrorMessageRepository, HmeServiceSplitRecordRepository hmeServiceSplitRecordRepository, HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository, HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper, MtModLocatorRepository mtModLocatorRepository, MtMaterialLotRepository mtMaterialLotRepository, MtMaterialRepository mtMaterialRepository, ItfServiceTransferIfaceService itfServiceTransferIfaceService, LovAdapter lovAdapter, HmeEoJobSnReWorkMapper hmeEoJobSnReWorkMapper, MtModWorkcellRepository mtModWorkcellRepository) {
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.hmeServiceSplitRecordRepository = hmeServiceSplitRecordRepository;
        this.hmeEoJobSnLotMaterialRepository = hmeEoJobSnLotMaterialRepository;
        this.hmeServiceSplitRecordMapper = hmeServiceSplitRecordMapper;
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.mtMaterialRepository = mtMaterialRepository;
        this.itfServiceTransferIfaceService = itfServiceTransferIfaceService;
        this.lovAdapter = lovAdapter;
        this.hmeEoJobSnReWorkMapper = hmeEoJobSnReWorkMapper;
        this.mtModWorkcellRepository = mtModWorkcellRepository;
    }

    @Override
    @ProcessLovValue(targetField = {"", "recordLineList", ""})
    @Transactional(rollbackFor = Exception.class)
    public HmeServiceSplitRecordDTO snNumScan(Long tenantId, HmeServiceSplitRecordVO vo) {
        // 参数校验
        paramValidation(tenantId, vo);

        List<String> splitStatus = new ArrayList<>();
        splitStatus.add("REPAIR_COMPLETE");
        splitStatus.add("CANCEL");

        // 校验当前SN是否已经存在
        Condition condition = Condition.builder(HmeServiceSplitRecord.class).andWhere(Sqls.custom()
                .andEqualTo(HmeServiceSplitRecord.FIELD_TENANT_ID, tenantId)
                .andEqualTo(HmeServiceSplitRecord.FIELD_SN_NUM, vo.getSnNum())
                .andEqualTo(HmeServiceSplitRecord.FIELD_SITE_ID, vo.getSiteId())
                .andNotIn(HmeServiceSplitRecord.FIELD_SPLIT_STATUS, splitStatus)).build();
        List<HmeServiceSplitRecord> splitRecord = hmeServiceSplitRecordRepository.selectByCondition(condition);
        //如果不存在则新建
        if (CollectionUtils.isEmpty(splitRecord)) {
            // 判断条码是否在条码表中存在，若不存在执行以下逻辑创建条码
            HmeServiceSplitRecordDTO3 dto = hmeServiceSplitRecordRepository.splitRecordInsert(tenantId, vo);
            dto.setRepairSnFlag(true);
            //库存变更
            hmeServiceSplitRecordRepository.onhandQtyUpdateProcess(tenantId, dto, dto.getNewMaterialLot());
            if (StringUtils.isNotBlank(dto.getInternalOrderNum())) {
                // 内部订单接口回传
                this.internalOrderGoBack(tenantId, dto);
            }
        }

        // 查询拆机信息
        return hmeServiceSplitRecordRepository.splitRecordBySnQuery(tenantId, vo.getSnNum());
    }

    private void internalOrderGoBack(Long tenantId, HmeServiceSplitRecordDTO3 dto) {
        // 获取客户已修仓货位、在线仓货位 都不存在 则报错
        List<LovValueDTO> locatorLovList = lovAdapter.queryLovValue("HME.CUSTMER_LOCATOR", tenantId);
        Optional<LovValueDTO> repairLocatorOpt = locatorLovList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "REPAIR_COMPLETE")).findFirst();
        if (!repairLocatorOpt.isPresent()) {
            throw new MtException("HME_REPAIR_SN_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0004", "HME"));
        }
        Optional<LovValueDTO> repairingOpt = locatorLovList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "REPAIRING")).findFirst();
        if (!repairingOpt.isPresent()) {
            throw new MtException("HME_REPAIR_SN_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0004", "HME"));
        }
        // 在线仓货位
        String repairingCode = repairingOpt.get().getMeaning();
        MtModLocator repairingLocator = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setTenantId(tenantId);
            setLocatorCode(repairingCode);
        }});
        if (Objects.isNull(repairingLocator)) {
            throw new MtException("HME_REPAIR_SN_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0005", "HME", repairingCode));
        }
        // 在线仓货位对应的仓库
        MtModLocator repairingWareHouse = mtModLocatorRepository.selectByPrimaryKey(repairingLocator.getParentLocatorId());
        if (Objects.isNull(repairingWareHouse)) {
            throw new MtException("HME_REPAIR_SN_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0006", "HME", repairingCode));
        }

        // 入库仓库
        MtModLocator wareHouse = mtModLocatorRepository.selectByPrimaryKey(dto.getWarehouseId());
        if (Objects.isNull(wareHouse)) {
            throw new MtException("HME_REPAIR_SN_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_REPAIR_SN_0007", "HME"));
        }

        // 入库仓库与在线仓库是否一致 不一致时售后大仓调拨回传接口 一致时则不处理
        if (!StringUtils.equals(wareHouse.getLocatorId(), repairingWareHouse.getLocatorId())) {
            HmeServiceSplitRecordVO3 splitRecordVO3 = hmeServiceSplitRecordMapper.querySplitRecord(tenantId, dto.getSplitRecordId());
            //移动类型 固定为311
            String moveType = "311";
            MtMaterialLot mtMaterialLot = dto.getNewMaterialLot();
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
            String realName = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getRealName() : "";
            //待业务确定
            ServiceTransferIfaceInvokeVO transferIfaceInvokeVO = new ServiceTransferIfaceInvokeVO(CommonUtils.currentTimeGet(), CommonUtils.currentTimeGet(), splitRecordVO3.getSnNum(), "04", mtMaterial != null ? mtMaterial.getMaterialCode() : "", splitRecordVO3.getPlantCode(), wareHouse.getLocatorCode(), moveType, BigDecimal.ONE, "ST", splitRecordVO3.getPlantCode(), repairingWareHouse.getLocatorCode(), splitRecordVO3.getSnNum(), splitRecordVO3.getAreaCode(), splitRecordVO3.getReceiveDate(), realName, splitRecordVO3.getBackType(), mtMaterialLot.getLot(), tenantId, null);
            try {
                itfServiceTransferIfaceService.invoke(Collections.singletonList(transferIfaceInvokeVO));
            } catch (Exception e) {
                log.error("售后大仓回调应用服务接口调用失败！");
            }

        }
    }

    private void paramValidation(Long tenantId, HmeServiceSplitRecordVO vo) {
        if (StringUtils.isBlank(vo.getSnNum())) {
            throw new MtException("HME_SPLIT_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0001", "HME", "SnNum"));
        }
        if (StringUtils.isBlank(vo.getSiteId())) {
            throw new MtException("HME_SPLIT_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0001", "HME", "SiteId"));
        }
        if (StringUtils.isBlank(vo.getOperationId())) {
            throw new MtException("HME_SPLIT_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0001", "HME", "OperationId"));
        }
        if (StringUtils.isBlank(vo.getWkcShiftId())) {
            throw new MtException("HME_SPLIT_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0001", "HME", "WkcShiftId"));
        }
        if (StringUtils.isBlank(vo.getWorkcellId())) {
            throw new MtException("HME_SPLIT_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0001", "HME", "WorkcellId"));
        }
    }

    @Override
    public HmeServiceSplitRecordDTO3 materialLotScan(Long tenantId, HmeServiceSplitMaterialLotVO vo) {
        // 获取当前整机已拆机的行
        List<HmeServiceSplitRecord> recordLines = hmeServiceSplitRecordRepository.select(new HmeServiceSplitRecord() {{
            setTenantId(tenantId);
            setTopSplitRecordId(vo.getTopSplitRecordId());
        }});
        // 校验当前组件是否已经存在拆机记录
        if (CollectionUtils.isNotEmpty(recordLines)) {
            Long count = recordLines.stream().filter(item -> StringUtils.equals(item.getTopSplitRecordId(), vo.getTopSplitRecordId()) &&
                    StringUtils.equals(item.getSnNum(), vo.getMaterialLotCode())).count();
            if (count > 0) {
                throw new MtException("HME_SPLIT_RECORD_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SPLIT_RECORD_0004", "HME", vo.getMaterialLotCode()));
            }
        }
        // 获取组件信息
        return hmeServiceSplitRecordRepository.materialLotGet(tenantId, vo.getMaterialLotCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeServiceSplitRecordDTO3 split(Long tenantId, HmeServiceSplitRecordDTO3 dto) {
        //判断当前选择的拆分物料，是否为半成品
        String workOrderNum = null;
        MtMaterialLot newSn = new MtMaterialLot();
        //        MtModLocator locator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());
        MtModLocator locator = null;
        //判断当前工位所在工段是否维护了退料货位，若维护了退料货位，则退料到退料货位， 若没有维护，则退料到当前工位所在工段下的默认存储货位
        List<MtModLocator> relBackLocatorList = hmeEoJobSnReWorkMapper.queryReleaseBackLocator(tenantId, dto.getWorkcellId(), dto.getSiteId());
        //没有维护 退料货位
        if (CollectionUtils.isEmpty(relBackLocatorList)) {
            locator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());
        } else {
            // 当前工位【${1}】所在工段下维护了多个退料货位,请检查!
            if (relBackLocatorList.size() > 1) {
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(dto.getWorkcellId());
                throw new MtException("HME_EO_JOB_SN_248", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_248", HmeConstants.ConstantValue.HME, mtModWorkcell.getWorkcellCode() ));
            }
            // 货位${1}未维护上层仓库,请检查仓库货位基础数据!
            if (StringUtils.isEmpty(relBackLocatorList.get(0).getParentLocatorId())) {
                throw new MtException("HME_SPLIT_RECORD_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SPLIT_RECORD_0019", HmeConstants.ConstantValue.HME, relBackLocatorList.get(0).getLocatorCode()));
            }
            // 退料货位
            locator = relBackLocatorList.get(0);
        }
        if (StringUtils.equals(HmeConstants.ItemGroup.RK06, dto.getItemType())) {
            throw new MtException("HME_SPLIT_RECORD_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0005", "HME", dto.getMaterialCode()));
        } else if (StringUtils.equals(HmeConstants.ConstantValue.YES, dto.getIsRepair()) &&
                StringUtils.equals(HmeConstants.ItemGroup.RK05, dto.getItemType())) {
            // 校验当前组件是否已经存在拆机记录
            HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordRepository.selectByPrimaryKey(dto.getSplitRecordId());
            this.materialLotScan(tenantId, new HmeServiceSplitMaterialLotVO(dto.getSplitRecordId(), hmeServiceSplitRecord.getServiceReceiveId(), dto.getMaterialLotCode(), hmeServiceSplitRecord.getTopSplitRecordId()));
            // 创建物料批
            HmeServiceSplitRecordVO vo = dto.toCreateCommand();
            newSn = hmeServiceSplitRecordRepository.splitMaterialLotCreate(tenantId, dto.getMaterialId(), vo, locator.getLocatorId());
            dto.setMaterialLotId(newSn.getMaterialLotId());
            dto.setMaterialLotCode(newSn.getMaterialLotCode());
            dto.setLotCode(newSn.getLot());
            // 创建工单
            workOrderNum = hmeServiceSplitRecordRepository.createWorkOrder(tenantId, dto.getMaterialLotCode(), dto.getMaterialId(), dto.getSiteId(), locator.getLocatorId());
            if (StringUtils.isBlank(workOrderNum)) {
                throw new MtException("HME_SPLIT_RECORD_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_SPLIT_RECORD_0021", "HME"));
            }
        }
        dto.setWorkOrderNum(workOrderNum);
        dto.setIssuedLocatorId(locator.getLocatorId());
        // 插入拆机记录
        hmeServiceSplitRecordRepository.splitRecordLineInsert(tenantId, dto);
        if (StringUtils.equals(dto.getIsOnhand(), HmeConstants.ConstantValue.YES)) {
            //库存变更
            dto.setRepairSnFlag(false);
            hmeServiceSplitRecordRepository.onhandQtyUpdateProcess(tenantId, dto, newSn);
        }
        return dto;
    }

    @Override
    public HmeServiceSplitBomHeaderVO bomGet(Long tenantId, String siteId, String materialId) {
        HmeServiceSplitBomHeaderVO result = hmeServiceSplitRecordRepository.selectLatestBomByMaterial(tenantId, siteId, materialId);
        if (Objects.isNull(result)) {
            return new HmeServiceSplitBomHeaderVO();
        }
        List<HmeServiceSplitBomLineVO> lineList = hmeServiceSplitRecordRepository.selectBomLineList(tenantId, siteId, result.getBomId());
        result.setLineList(lineList);
        return result;
    }

    @Override
    public HmeServiceSplitRecordDTO4 registerCancel(Long tenantId, HmeServiceSplitRecordDTO4 dto) {
        if(StringUtils.isEmpty(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(StringUtils.isEmpty(dto.getSnNum())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "撤销SN"));
        }
        //判断是否存在可撤销登记信息
        String serviceReceiveId = hmeServiceSplitRecordMapper.getServiceReceiveIdBySiteAndSn(tenantId, dto.getSiteId(), dto.getSnNum());
        if(StringUtils.isEmpty(serviceReceiveId)){
            throw new MtException("HME_SPLIT_RECORD_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0027", "HME"));
        }
        //判断是否存在已拆机记录
        int count = hmeServiceSplitRecordRepository.selectCount(new HmeServiceSplitRecord() {{
            setTenantId(tenantId);
            setServiceReceiveId(serviceReceiveId);
            setSnNum(dto.getSnNum());
        }});
        if(count > 0){
            throw new MtException("HME_SPLIT_RECORD_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0028", "HME"));
        }
        hmeServiceSplitRecordRepository.registerCancel(tenantId, serviceReceiveId);
        return dto;
    }
}
