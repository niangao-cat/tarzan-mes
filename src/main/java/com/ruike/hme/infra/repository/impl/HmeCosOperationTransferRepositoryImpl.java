package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeWoJobSnService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmeCosOperationTransferVO;
import com.ruike.hme.domain.vo.HmeCosPatchPdaVO9;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.wms.app.service.impl.WmsCommonServiceComponent;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContainerVO25;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO3;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.repository.MtRouterNextStepRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static io.tarzan.common.domain.util.MtBaseConstants.QUALITY_STATUS.OK;

/**
 * 来料转移
 *
 * @author sanfeng.zhang@hand-china.com 2020/12/28 17:38
 */
@Component
public class HmeCosOperationTransferRepositoryImpl implements HmeCosOperationTransferRepository {

    @Autowired
    private HmeCosOperationTransferMapper hmeCosOperationTransferMapper;

    @Autowired
    private HmeContainerCapacityMapper hmeContainerCapacityMapper;

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtRouterNextStepRepository mtRouterNextStepRepository;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;

    @Autowired
    private WmsCommonServiceComponent commonServiceComponent;

    @Autowired
    private MtContainerRepository containerRepository;

    @Autowired
    private MtExtendSettingsRepository extendSettingsRepository;

    @Autowired
    private HmeWoJobSnService hmeWoJobSnService;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private HmeCosOperationRecordMapper hmeCosOperationRecordMapper;

    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;

    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private MtMaterialLotHisRepository mtMaterialLotHisRepository;

    @Autowired
    private HmeCosCommonService hmeCosCommonService;

    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;

    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;


    private static final String[] IGNORE_TABLE_FIELDS = new String[]{MtMaterialLot.FIELD_IDENTIFICATION,
            AuditDomain.FIELD_OBJECT_VERSION_NUMBER, AuditDomain.FIELD_LAST_UPDATED_BY, AuditDomain.FIELD_CREATED_BY,
            AuditDomain.FIELD_CREATION_DATE, AuditDomain.FIELD_LAST_UPDATE_DATE};
    private final String ATTR_TABLE = "mt_material_lot_attr";

    @Override
    @ProcessLovValue
    public HmeCosOperationTransferVO scanSourceBarcode(Long tenantId, String barcode, String operationId) {
        HmeCosOperationTransferVO transferVO = new HmeCosOperationTransferVO();
        // 校验参数
        if (StringUtils.isBlank(barcode)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "barcode"));
        }
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(barcode);
        }});

        // 校验条码有效性
        if (mtMaterialLot == null || !StringUtils.equals(mtMaterialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
            throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_002", "HME", barcode));
        }
        if (HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())||HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", barcode));
        }
        transferVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        transferVO.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        transferVO.setPrimaryUomQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));

        // 查询条码扩展字段
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_material_lot_attr");
        mtExtendVO1.setKeyIdList(Collections.singletonList(mtMaterialLot.getMaterialLotId()));
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        extendAttrVO1List.forEach(attr -> {
            switch (attr.getAttrName()) {
                case "WORK_ORDER_ID":
                    transferVO.setWorkOrderId(attr.getAttrValue());
                    break;
                case "COS_TYPE":
                    transferVO.setCosType(attr.getAttrValue());
                    break;
                case "WAFER_NUM":
                    transferVO.setWaferNum(attr.getAttrValue());
                    break;
                case "WORKING_LOT":
                    transferVO.setWorkingLot(attr.getAttrValue());
                    break;
                case "TYPE":
                    transferVO.setType(attr.getAttrValue());
                    break;
                case "REMARK":
                    transferVO.setRemark(attr.getAttrValue());
                    break;
                case "LOTNO":
                    transferVO.setLotNo(attr.getAttrValue());
                    break;
                case "AVG_WAVE_LENGTH":
                    transferVO.setAvgWaveLength(attr.getAttrValue());
                    break;
                case "CURRENT_ROUTER_STEP":
                    transferVO.setCurrentRouterStep(attr.getAttrValue());
                    break;
            }
        });
        // 校验芯片类型
        if (StringUtils.isBlank(transferVO.getCosType())) {
            throw new MtException("HME_CHIP_TRANSFER_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_004", "HME", barcode));
        }
        // 校验工艺 工序关系
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(transferVO.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("HME_CHIP_TRANSFER_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_004", "HME", barcode));
        }
        transferVO.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        transferVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        // 根据工艺和工单的步骤 获取当前工艺步骤
        List<String> routeStepIdList = hmeCosOperationTransferMapper.queryRouterStepIdByRouteAndOperation(tenantId, mtWorkOrder.getRouterId(), operationId);
        if (CollectionUtils.isEmpty(routeStepIdList)) {
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_028", "HME"));
        }
        // 根据当前步骤在mt_router_next_step获取上个工艺步骤
        List<MtRouterNextStep> routerNextStepList = mtRouterNextStepRepository.select(new MtRouterNextStep() {{
            setTenantId(tenantId);
            setNextStepId(routeStepIdList.get(0));
        }});
        if (CollectionUtils.isEmpty(routerNextStepList)) {
            throw new MtException("HME_COS_030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_030", "HME"));
        }
        // 找到多个 则报错
        if (routerNextStepList.size() > 1) {
            throw new MtException("HME_COS_032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_032", "HME"));
        }
        // 取出条码的扩展CURRENT_ROUTER_STEP 匹配当前工艺步骤和上个工艺
        if (!StringUtils.equals(transferVO.getCurrentRouterStep(), routeStepIdList.get(0)) && !StringUtils.equals(transferVO.getCurrentRouterStep(), routerNextStepList.get(0).getRouterStepId())) {
            throw new MtException("HME_COS_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_033", "HME"));
        }
//        // 根据ORIGINAL_ID查询条码列表
//        hmeCosOperationTransferMapper.querySplitCodeList(tenantId, );
        return transferVO;
    }

    @Override
    public HmeContainerCapacity containerCosNumQuery(Long tenantId, HmeWoJobSnDTO2 hmeWoJobSnDTO2) {
        // 校验参数
        // 容器类型
        if (StringUtils.isBlank(hmeWoJobSnDTO2.getContainerTypeCode())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "containerTypeCode"));
        }
        // 工艺
        if (StringUtils.isBlank(hmeWoJobSnDTO2.getOperationId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "operationId"));
        }
        // 芯片类型
        if (StringUtils.isBlank(hmeWoJobSnDTO2.getCosType())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "cosType"));
        }
        // 根据容器类型找到对应的容器
        MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
            setTenantId(tenantId);
            setContainerTypeCode(hmeWoJobSnDTO2.getContainerTypeCode());
            setEnableFlag(HmeConstants.ConstantValue.YES);
        }});
        if (mtContainerType == null) {
            throw new MtException("HME_COS_CHIP_IMP_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_CHIP_IMP_0011", "HME", hmeWoJobSnDTO2.getContainerTypeCode()));
        }
        HmeContainerCapacity containerCapacity = hmeContainerCapacityMapper.selectOne(new HmeContainerCapacity() {{
            setTenantId(tenantId);
            setCosType(hmeWoJobSnDTO2.getCosType());
            setOperationId(hmeWoJobSnDTO2.getOperationId());
            setContainerTypeId(mtContainerType.getContainerTypeId());
        }});
        if (containerCapacity == null) {
            throw new MtException("HME_CHIP_TRANSFER_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_015", "HME"));
        }
        return containerCapacity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialLotSplit(Long tenantId, HmeWoJobSnDTO6 dto) {
        List<HmeWoJobSnDTO7> resultList = new ArrayList<>();
        List<HmeWoJobSnDTO7> targetList = dto.getTargetList();
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("HME_COS_026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_026", "HME"));
        }
        // 根据工艺和工单的步骤 获取当前工艺步骤 插入条码扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());
        List<String> routeStepIdList = hmeCosOperationTransferMapper.queryRouterStepIdByRouteAndOperation(tenantId, mtWorkOrder.getRouterId(), dto.getOperationId());
        if (CollectionUtils.isEmpty(routeStepIdList)) {
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_028", "HME"));
        }
        MtMaterialLot sourceMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        //1.生成来料记录表
        // 获取当前用户站点信息
        Long userId = DetailsHelper.getUserDetails().getUserId();
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        //获取容器类型Id
        MtContainerType mtContainerType = new MtContainerType();
        mtContainerType.setContainerTypeCode(dto.getContainerTypeCode());
        MtContainerType mtContainerType1 = mtContainerTypeRepository.selectOne(mtContainerType);
        //获取数量
        Double totalTransferQty = targetList.stream().map(HmeWoJobSnDTO7::getTransferQuantity)
                .filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue)
                .summaryStatistics().getSum();
        Double totalBarNum = targetList.stream().map(HmeWoJobSnDTO7::getTargetBarNum)
                .filter(Objects::nonNull).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue)
                .summaryStatistics().getSum();
        HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
        BeanUtils.copyProperties(dto, hmeCosOperationRecord);
        hmeCosOperationRecord.setContainerTypeId(mtContainerType1.getContainerTypeId());
        hmeCosOperationRecord.setTenantId(tenantId);
        hmeCosOperationRecord.setSiteId(siteId);
        hmeCosOperationRecord.setCosNum(totalTransferQty.longValue());
        hmeCosOperationRecord.setSurplusCosNum(totalTransferQty.longValue());
        hmeCosOperationRecord.setBarNum(totalBarNum.longValue());
        //2021-01-28 add by sanfeng.zhang for ban.zhenyong 存储条码供应商到ATTRIBUTE2
        hmeCosOperationRecord.setAttribute2(sourceMaterialLot.getSupplierId());
        hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

        //获取来源条码的实验代码与实验备注
        String labCode = null;
        String labRemark = null;
        List<HmeCosPatchPdaVO9> sourceLabCodeAndLabRemark = hmeCosPatchPdaMapper.labCodeAndRemarkAttrQuery(tenantId, Collections.singletonList(dto.getMaterialLotId()));
        if(CollectionUtils.isNotEmpty(sourceLabCodeAndLabRemark)){
            if(StringUtils.isNotBlank(sourceLabCodeAndLabRemark.get(0).getLabCode())){
                labCode = sourceLabCodeAndLabRemark.get(0).getLabCode();
            }
            if(StringUtils.isNotBlank(sourceLabCodeAndLabRemark.get(0).getRemark())){
                labRemark = sourceLabCodeAndLabRemark.get(0).getRemark();
            }
        }

        //2.拆分条码
        resultList = materialLotSplitEnd(tenantId, dto, totalTransferQty);

        //3.条码进出站
        for (HmeWoJobSnDTO7 temp : resultList) {
            //条码进站
            HmeWoJobSnDTO3 hmeWoJobSnDTO3 = new HmeWoJobSnDTO3();
            hmeWoJobSnDTO3.setMaterialLotCode(temp.getMaterialLotCode());
            hmeWoJobSnDTO3.setOperationId(dto.getOperationId());
            hmeWoJobSnDTO3.setOperationRecordId(hmeCosOperationRecord.getOperationRecordId());
            hmeWoJobSnDTO3.setShiftId(dto.getWkcShiftId());
            hmeWoJobSnDTO3.setSiteId(dto.getSiteId());
            hmeWoJobSnDTO3.setWkcLinetId(dto.getWkcLinetId());
            hmeWoJobSnDTO3.setWorkcellId(dto.getWorkcellId());
            hmeWoJobSnDTO3.setRequestId(temp.getRequestId());
            hmeWoJobSnDTO3.setParentEventId(temp.getParentEventId());
            hmeWoJobSnDTO3.setCosType(dto.getCosType());
            hmeWoJobSnDTO3.setWafer(dto.getWafer());
            // 将RouterStepId 插入目标物料批的扩展表
            // 插入条码扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP
            HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = incomeScanMaterialLot(tenantId, hmeWoJobSnDTO3, routeStepIdList.get(0), labCode, labRemark);
            //条码出站
            HmeWojobSnDTO4 hmeWojobSnDTO4 = new HmeWojobSnDTO4();
            hmeWojobSnDTO4.setEoJobSnId(hmeWoJobSnReturnDTO3.getEoJobSnId());
            hmeWojobSnDTO4.setWoJobSnId(hmeWoJobSnReturnDTO3.getWoJobSnId());
            hmeWojobSnDTO4.setMaterialLotId(hmeWoJobSnReturnDTO3.getMaterialLotId());
            hmeWojobSnDTO4.setOperationRecordId(hmeCosOperationRecord.getOperationRecordId());
            hmeWojobSnDTO4.setProcessedNum(temp.getTransferQuantity().toString());
            hmeWojobSnDTO4.setOperationId(dto.getOperationId());
            hmeWojobSnDTO4.setWorkcellId(dto.getWorkcellId());
            hmeWoJobSnService.siteOut(tenantId, hmeWojobSnDTO4);
        }
    }

    private List<HmeWoJobSnDTO7> materialLotSplitEnd(Long tenantId, HmeWoJobSnDTO6 dto, Double totalTransferQty) {
        List<HmeWoJobSnDTO7> resultList = new ArrayList<>();

        // 创建请求事件
        String requestId = commonServiceComponent.generateEventRequest(tenantId,
                "COS_MOVE_SPLIT");
        // 创建物料转移扣减事件
        String outEventId = commonServiceComponent.generateEvent(tenantId, "COS_MOVE_SPLIT_OUT",
                requestId);

        //校验数量
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        if (mtMaterialLot.getPrimaryUomQty() < totalTransferQty) {
            throw new MtException("HME_COS_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_021", "HME"));
        }
        // 扣减转移物料批数量
        Double primaryUomQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(totalTransferQty)).doubleValue();
        MtMaterialLotVO2 mtMaterialLotVo2 = new MtMaterialLotVO2();
        mtMaterialLotVo2.setEventId(outEventId);
        mtMaterialLotVo2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVo2.setPrimaryUomQty(primaryUomQty);
        mtMaterialLotVo2.setEnableFlag(primaryUomQty.compareTo(0.0) == 0 ? HmeConstants.ConstantValue.NO
                : HmeConstants.ConstantValue.YES);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVo2, HmeConstants.ConstantValue.NO);

        // 卸载容器
        if (HmeConstants.ConstantValue.NO.equals(mtMaterialLotVo2.getEnableFlag())
                && org.apache.commons.lang.StringUtils.isNotBlank(mtMaterialLot.getCurrentContainerId())) {
            MtContainerVO25 cnt = new MtContainerVO25();
            cnt.setContainerId(mtMaterialLot.getCurrentContainerId());
            cnt.setLoadObjectId(mtMaterialLot.getMaterialLotId());
            cnt.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            cnt.setEventRequestId(requestId);
            containerRepository.containerUnload(tenantId, cnt);
        }
        // 创建物料转移新增事件
        String inEventId = commonServiceComponent.generateEvent(tenantId, "COS_MOVE_SPLIT_IN",
                requestId);
        MtMaterialLotVO2 targetMaterialLotVo = new MtMaterialLotVO2();

        //循环拆分物料
        for (HmeWoJobSnDTO7 temp : dto.getTargetList()) {
            //获取目标条码
            MtMaterialLot transferMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(temp.getMaterialLotCode());
            }});
            // 校验目标条码状态 不存在、有效则报错
            if (transferMaterialLot == null || HmeConstants.ConstantValue.YES.equals(transferMaterialLot.getEnableFlag())) {
                throw new MtException("HME_COS_036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_036", "HME"));
            }
            //无效条码 数量置为0 2020/10/15 add by sanfeng.zhang for banzhenyong
            transferMaterialLot.setPrimaryUomQty(0D);
            mtMaterialLotMapper.updateByPrimaryKeySelective(transferMaterialLot);
            //校验条码进站
            copyPropertiesIgnoreNullAndTableFields(mtMaterialLot, targetMaterialLotVo);
            targetMaterialLotVo.setEventId(inEventId);
            targetMaterialLotVo.setMaterialLotId(transferMaterialLot.getMaterialLotId());
            targetMaterialLotVo.setMaterialLotCode(transferMaterialLot.getMaterialLotCode());
            targetMaterialLotVo.setEnableFlag(HmeConstants.ConstantValue.YES);
            targetMaterialLotVo.setPrimaryUomQty(temp.getTransferQuantity());

            // 创建后目标条码
            MtMaterialLotVO13 vo = mtMaterialLotRepository.materialLotUpdate(tenantId, targetMaterialLotVo,
                    HmeConstants.ConstantValue.NO);
            // 更新目标条码拓展字段
            this.updateMaterialLotExtendAttr(tenantId, mtMaterialLot.getMaterialLotId(), vo.getMaterialLotId(), inEventId);

            MtMaterialLot mtMaterialLotResult = mtMaterialLotRepository.materialLotPropertyGet(tenantId, vo.getMaterialLotId());
            HmeWoJobSnDTO7 hmeWoJobSnDTO7 = new HmeWoJobSnDTO7();
            hmeWoJobSnDTO7.setMaterialLotCode(mtMaterialLotResult.getMaterialLotCode());
            hmeWoJobSnDTO7.setTransferQuantity(temp.getTransferQuantity());
            hmeWoJobSnDTO7.setTargetBarNum(temp.getTargetBarNum());
            hmeWoJobSnDTO7.setMaterialLotId(mtMaterialLotResult.getMaterialLotId());
            hmeWoJobSnDTO7.setRequestId(requestId);
            hmeWoJobSnDTO7.setParentEventId(inEventId);
            resultList.add(hmeWoJobSnDTO7);
        }

        //2021-10-29 13:54 add by chaonan.hu for yiming.zhang 如果拆分后原条码数量为0时清空来料条码的实验代码与备注
        MtMaterialLot finalSourceMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
        if(finalSourceMaterialLot.getPrimaryUomQty() == 0){
            MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
            mtMaterialLotAttrVO3.setMaterialLotId(dto.getMaterialLotId());
            //创建事件
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("EMPTY_LAB_CODE");
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            mtMaterialLotAttrVO3.setEventId(eventId);
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 labCodeAttr = new MtExtendVO5();
            labCodeAttr.setAttrName("LAB_CODE");
            labCodeAttr.setAttrValue("");
            attrList.add(labCodeAttr);
            MtExtendVO5 labRemarkAttr = new MtExtendVO5();
            labRemarkAttr.setAttrName("LAB_REMARK");
            labRemarkAttr.setAttrValue("");
            attrList.add(labRemarkAttr);
            mtMaterialLotAttrVO3.setAttr(attrList);
            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
        }
        return resultList;
    }

    public static <E, T> void copyPropertiesIgnoreNullAndTableFields(E src, T target) {
        // 对象值转换时屏蔽表字段：ID，创建更新人等信息
        Set<String> hashSet = new HashSet<>();
        hashSet.addAll(Arrays.asList(WmsCommonUtils.getNullPropertyNames(src)));
        hashSet.addAll(Arrays.asList(IGNORE_TABLE_FIELDS));
        String[] result = new String[hashSet.size()];
        BeanUtils.copyProperties(src, target, hashSet.toArray(result));
    }

    /**
     * 更新拓展字段
     *
     * @param tenantId            租户
     * @param sourceMaterialLotId 来源物料批ID
     * @param materialLotId       生成物料批ID
     * @param eventId             事件ID
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/20 10:32:56
     */
    private void updateMaterialLotExtendAttr(Long tenantId, String sourceMaterialLotId, String materialLotId,
                                             String eventId) {
        // 查询原属性
        MtExtendVO extendAttrQuery = new MtExtendVO();
        extendAttrQuery.setKeyId(sourceMaterialLotId);
        extendAttrQuery.setTableName(ATTR_TABLE);
        List<MtExtendAttrVO> extendAttrList = extendSettingsRepository.attrPropertyQuery(tenantId, extendAttrQuery);

        // 覆盖目标属性
        List<MtExtendVO5> targetExtendAttrList = new ArrayList<>();
        MtExtendVO5 newAttr = null;
        Boolean mfFlag = false;
        List<String> attrNameList = Arrays.asList(new String[]{"ORIGINAL_ID", "COS_RECORD", "LOCATION_ROW", "LOCATION_COLUMN", "CHIP_NUM", "CONTAINER_TYPE", "CURRENT_ROUTER_STEP"});
        for (MtExtendAttrVO extendAttrVO : extendAttrList) {
            if (attrNameList.contains(extendAttrVO.getAttrName())) {
                continue;
            }

            if (StringUtils.equals(extendAttrVO.getAttrName(), "MF_FLAG")) {
                mfFlag = true;
            }
            newAttr = new MtExtendVO5();
            newAttr.setAttrName(extendAttrVO.getAttrName());
            newAttr.setAttrValue(extendAttrVO.getAttrValue());
            targetExtendAttrList.add(newAttr);
        }

        if (!mfFlag) {
            newAttr = new MtExtendVO5();
            newAttr.setAttrName("MF_FLAG");
            newAttr.setAttrValue("");
            targetExtendAttrList.add(newAttr);
        }

        newAttr = new MtExtendVO5();
        newAttr.setAttrName("ORIGINAL_ID");
        newAttr.setAttrValue(sourceMaterialLotId);
        targetExtendAttrList.add(newAttr);

        extendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE, materialLotId, eventId, targetExtendAttrList);
    }

    // modif liukejin 2020年12月18日13:53:57
    // 将RouterStepId 插入目标物料批的扩展表
    // 插入条码扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP
    private HmeWoJobSnReturnDTO3 incomeScanMaterialLot(Long tenantId, HmeWoJobSnDTO3 dto, String routeStepId, String labCode, String labRemark) {
        HmeWoJobSnReturnDTO3 hmeWoJobSnReturnDTO3 = new HmeWoJobSnReturnDTO3();
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        // 获取当前时间
        final Date currentDate = new Date(System.currentTimeMillis());

        // 进站条码不能为空
        if (StringUtils.isBlank(dto.getMaterialLotCode())) {
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (StringUtils.isBlank(dto.getOperationRecordId())) {
            throw new MtException("HME_COS_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_007", "HME"));
        }

        //校验条码是存在 Y OK
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLot.setSiteId(dto.getSiteId());
        mtMaterialLot.setMaterialLotCode(dto.getMaterialLotCode());
        mtMaterialLot.setEnableFlag("Y");
        MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectOne(mtMaterialLot);
        if (ObjectUtil.isNotEmpty(mtMaterialLot1)) {
            if (!OK.equals(mtMaterialLot1.getQualityStatus())) {
                throw new MtException("HME_WO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_WO_JOB_SN_002", "HME", dto.getMaterialLotCode()));
            }
        } else {
            throw new MtException("HME_WO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_001", "HME", dto.getMaterialLotCode()));
        }

        //校验条码数量（条码号从MT_MATERIAL_LOT表取出PRIMARY_UOM_QTY）是否等于器具容量（从器具拓展表HME_CONTAINER_CAPACITY取出LINE_NUM* COLUMN_NUM* CAPACITY），如果不等于则报错“该条码数量与容器容量不一致”
        HmeCosOperationRecord hmeCosOperationRecord = hmeCosOperationRecordRepository.selectByPrimaryKey(dto.getOperationRecordId());
        if (hmeCosOperationRecord.getSurplusCosNum() < mtMaterialLot1.getPrimaryUomQty()) {
            throw new MtException("HME_WO_JOB_SN_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_009", "HME"));
        }

        MtContainerType mtContainerType = mtContainerTypeRepository.selectByPrimaryKey(hmeCosOperationRecord.getContainerTypeId());
        HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
        hmeContainerCapacity.setContainerTypeId(mtContainerType.getContainerTypeId());
        hmeContainerCapacity.setOperationId(dto.getOperationId());
        hmeContainerCapacity.setCosType(hmeCosOperationRecord.getCosType());
        HmeContainerCapacity hmeContainerCapacity1 = hmeContainerCapacityRepository.selectOne(hmeContainerCapacity);
        Long a = hmeContainerCapacity1.getColumnNum() * hmeContainerCapacity1.getLineNum() * hmeContainerCapacity1.getCapacity();
        if (a.longValue() < mtMaterialLot1.getPrimaryUomQty().doubleValue()) {
            throw new MtException("HME_WO_JOB_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_004", "HME"));
        }
        if (mtMaterialLot1.getPrimaryUomQty() % hmeContainerCapacity1.getCapacity() != 0) {
            throw new MtException("HME_WO_JOB_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_JOB_SN_013", "HME"));
        }

        //校验是否存在未出站物料批
        //获取来料信息
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setSiteInDate(currentDate);
        hmeEoJobSn.setShiftId(dto.getShiftId());
        hmeEoJobSn.setSiteInBy(userId);
        hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSn.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeEoJobSn.setOperationId(dto.getOperationId());
        hmeEoJobSn.setSnMaterialId(mtMaterialLot1.getMaterialId());
        hmeEoJobSn.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        //hmeEoJobSn.setEoStepNum(1);
        hmeEoJobSn.setReworkFlag(HmeConstants.ConstantValue.NO);
        hmeEoJobSn.setJobType("IO_TRANSFER");
        hmeEoJobSn.setSourceJobId(dto.getOperationRecordId());
        hmeEoJobSn.setSnQty(new BigDecimal(mtMaterialLot1.getPrimaryUomQty()));
//        List<MtMaterialLotHis> mtMaterialLotHis = mtMaterialLotHisRepository.selectByCondition(Condition.builder(MtMaterialLotHis.class)
//                .andWhere(Sqls.custom()
//                        .andEqualTo(MtMaterialLotHis.FIELD_TENANT_ID, tenantId)
//                        .andEqualTo(MtMaterialLotHis.FIELD_MATERIAL_LOT_ID, mtMaterialLot1.getMaterialLotId()))
//                .orderByDesc(MtMaterialLotHis.FIELD_CREATION_DATE)
//                .build());
//        if(CollectionUtils.isNotEmpty(mtMaterialLotHis))
//        {
//            hmeEoJobSn.setAttribute3(mtMaterialLotHis.get(0).getMaterialLotHisId());
//
//        }
        hmeEoJobSn.setAttribute3(dto.getCosType());
        hmeEoJobSn.setAttribute5(dto.getWafer());
        // 20210311 eidt by wenzhang.yu for zhenyong.ban 工位、eo、条码、返修标识、job_type取出最大的eo_step_num + 1
        Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, hmeEoJobSn.getWorkcellId(),
                hmeEoJobSn.getEoId(), hmeEoJobSn.getMaterialLotId(), HmeConstants.ConstantValue.NO, hmeEoJobSn.getJobType(), hmeEoJobSn.getOperationId());
        hmeEoJobSn.setEoStepNum(maxEoStepNum+1);
        hmeEoJobSnRepository.insertSelective(hmeEoJobSn);

        hmeWoJobSnReturnDTO3.setEoJobSnId(hmeEoJobSn.getJobId());
        HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
        hmeWoJobSn.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeWoJobSn.setSiteId(dto.getSiteId());
        hmeWoJobSn.setOperationId(dto.getOperationId());
        HmeWoJobSn hmeWoJobSn1 = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
        if (ObjectUtil.isNotEmpty(hmeWoJobSn1)) {
            hmeWoJobSn1.setSiteInNum(hmeWoJobSn1.getSiteInNum() + mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeWoJobSnMapper.updateByPrimaryKey(hmeWoJobSn1);
            hmeWoJobSnReturnDTO3.setWoJobSnId(hmeWoJobSn1.getWoJobSnId());

        } else {
            hmeWoJobSn.setSiteInNum(mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeWoJobSnRepository.insertSelective(hmeWoJobSn);
            hmeWoJobSnReturnDTO3.setWoJobSnId(hmeWoJobSn.getWoJobSnId());
        }
        //生成右侧位置图的数据
        this.setMaterialLotLoad(tenantId, hmeContainerCapacity1, mtMaterialLot1, hmeCosOperationRecord, labCode, labRemark);

        MtExtendVO10 materialLotExtend = new MtExtendVO10();
        materialLotExtend.setKeyId(mtMaterialLot1.getMaterialLotId());
        List<MtExtendVO5> mtExtendVO5s = new ArrayList<>();
        MtExtendVO5 statusAttr2 = new MtExtendVO5();
        statusAttr2.setAttrName("COS_RECORD");
        statusAttr2.setAttrValue(hmeCosOperationRecord.getOperationRecordId());
        mtExtendVO5s.add(statusAttr2);
        MtExtendVO5 statusAttr3 = new MtExtendVO5();
        statusAttr3.setAttrName("LOCATION_ROW");
        statusAttr3.setAttrValue(hmeContainerCapacity1.getLineNum().toString());
        mtExtendVO5s.add(statusAttr3);
        MtExtendVO5 statusAttr4 = new MtExtendVO5();
        statusAttr4.setAttrName("LOCATION_COLUMN");
        statusAttr4.setAttrValue(hmeContainerCapacity1.getColumnNum().toString());
        mtExtendVO5s.add(statusAttr4);
        MtExtendVO5 statusAttr5 = new MtExtendVO5();
        statusAttr5.setAttrName("CHIP_NUM");
        statusAttr5.setAttrValue(hmeContainerCapacity1.getCapacity().toString());
        mtExtendVO5s.add(statusAttr5);
        MtExtendVO5 statusAttr6 = new MtExtendVO5();
        statusAttr6.setAttrName("CONTAINER_TYPE");
        statusAttr6.setAttrValue(mtContainerType.getContainerTypeCode());
        mtExtendVO5s.add(statusAttr6);
        // modif liukejin 2020年12月18日13:53:57
        // 将RouterStepId 插入目标物料批的扩展表
        // 插入条码扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP
        MtExtendVO5 statusAttr14 = new MtExtendVO5();
        statusAttr14.setAttrName("CURRENT_ROUTER_STEP");
        statusAttr14.setAttrValue(routeStepId);
        mtExtendVO5s.add(statusAttr14);

        materialLotExtend.setAttrs(mtExtendVO5s);
        materialLotExtend.setEventId(dto.getParentEventId());
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotExtend);
        hmeWoJobSnReturnDTO3.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
        return hmeWoJobSnReturnDTO3;
    }

    /**
     * 保存装载信息
     *
     * @param tenantId
     * @param hmeContainerCapacity
     * @param mtMaterialLot
     * @param hmeCosOperationRecord
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/4 9:16
     */
    private void setMaterialLotLoad(Long tenantId, HmeContainerCapacity hmeContainerCapacity, MtMaterialLot mtMaterialLot,
                                    HmeCosOperationRecord hmeCosOperationRecord, String labCode, String labRemark) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
        String data = sdf.format(new Date());
        int num = (int) Math.ceil(mtMaterialLot.getPrimaryUomQty() / hmeContainerCapacity.getCapacity());
        if (StringUtils.isEmpty(hmeContainerCapacity.getAttribute1()) || "A".equals(hmeContainerCapacity.getAttribute1())) {
            for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
                for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        } else if ("B".equals(hmeContainerCapacity.getAttribute1())) {
            for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        } else if ("C".equals(hmeContainerCapacity.getAttribute1())) {
            for (int j = hmeContainerCapacity.getColumnNum().intValue(); j > 0; j--) {
                for (int i = 1; i <= hmeContainerCapacity.getLineNum(); i++) {
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        } else if ("D".equals(hmeContainerCapacity.getAttribute1())) {
            for (int j = hmeContainerCapacity.getColumnNum().intValue(); j > 0; j--) {
                for (int i = hmeContainerCapacity.getLineNum().intValue(); i > 0; i--) {
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        } else {
            for (int j = 1; j <= hmeContainerCapacity.getColumnNum(); j++) {
                for (int i = hmeContainerCapacity.getLineNum().intValue(); i > 0; i--) {
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeMaterialLotLoad.setLoadRow((long) i);
                    hmeMaterialLotLoad.setLoadColumn((long) j);
                    hmeMaterialLotLoad.setAttribute1(hmeContainerCapacity.getCosType());
                    hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                    hmeMaterialLotLoad.setAttribute3(hmeCosOperationRecord.getWorkOrderId());
                    hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + i + j + data);
                    hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                    hmeMaterialLotLoad.setTenantId(tenantId);
                    if(StringUtils.isNotBlank(labCode)){
                        hmeMaterialLotLoad.setAttribute19(labCode);
                    }
                    if(StringUtils.isNotBlank(labRemark)){
                        hmeMaterialLotLoad.setAttribute20(labRemark);
                    }
                    hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
                    num--;
                    if (num == 0) {
                        break;
                    }
                }
                if (num == 0) {
                    break;
                }
            }
        }
    }
}
