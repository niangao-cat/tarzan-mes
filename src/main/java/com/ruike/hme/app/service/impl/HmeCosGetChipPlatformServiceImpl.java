package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeCosGetChipPlatformService;
import com.ruike.hme.app.service.HmeEoJobEquipmentService;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.barcode.CommonBarcodeUtil;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.repository.MtRouterNextStepRepository;
import tarzan.method.domain.repository.MtRouterOperationRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import javax.servlet.http.*;
import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.stream.*;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * @Classname HmeCosGetChipPlatformServiceImpl
 * @Description COS取片平台
 * @Date 2020/8/18 10:27
 * @Author yuchao.wang
 */
@Slf4j
@Service
public class HmeCosGetChipPlatformServiceImpl implements HmeCosGetChipPlatformService {

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private HmeCosCommonService hmeCosCommonService;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeMaterialLotNcLoadRepository hmeMaterialLotNcLoadRepository;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private HmeMaterialLotNcRecordRepository hmeMaterialLotNcRecordRepository;

    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;

    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private HmeCosOperationRecordMapper hmeCosOperationRecordMapper;

    @Autowired
    private HmeContainerCapacityMapper hmeContainerCapacityMapper;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private HmeCosPatchPdaRepository hmeCosPatchPdaRepository;

    @Autowired
    private HmeCosGetChipPlatformMapper hmeCosGetChipPlatformMapper;

    @Autowired
    private HmeTimeProcessPdaRepository hmeTimeProcessPdaRepository;

    @Autowired
    private HmeEoJobEquipmentService hmeEoJobEquipmentService;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private HmeCosNcRecordRepository hmeCosNcRecordRepository;

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private HmeMaterialLotNcRecordMapper hmeMaterialLotNcRecordMapper;

    @Autowired
    private HmeMaterialLotNcLoadMapper hmeMaterialLotNcLoadMapper;

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;


    /**
     * @param tenantId 租户ID
     * @param dto      参数
     * @return java.lang.Object
     * @Description 待取片容器进站条码扫描
     * @author yuchao.wang
     * @date 2020/8/18 10:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosGetChipScanBarcodeResponseDTO scanBarcode(Long tenantId, HmeCosGetChipScanBarcodeDTO dto) {
        //非空校验
        if (StringUtils.isEmpty(dto.getBarcode())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "条码"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工艺路线"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位"));
        }
        if (StringUtils.isEmpty(dto.getWkcShiftId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "班组"));
        }

        //根据物料批条码获取物料批ID
        MtMaterialLotVO3 param = new MtMaterialLotVO3();
        param.setMaterialLotCode(dto.getBarcode());
        List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, param);
        if (CollectionUtils.isEmpty(materialLotIds) || StringUtils.isEmpty(materialLotIds.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码信息"));
        }
        String materialLotId = materialLotIds.get(0);

        //调用API获取物料批相关信息
        HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, materialLotId, true, dto.getBarcode());
        Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();

        //校验盘点/冻结标识
        if (YES.equals(hmeCosMaterialLotVO.getFreezeFlag()) || YES.equals(hmeCosMaterialLotVO.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", hmeCosMaterialLotVO.getMaterialLotCode()));
        }

        //校验物料批扩展属性
        if (StringUtils.isBlank(materialLotAttrMap.get("COS_RECORD"))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码扩展属性[COS_RECORD]"));
        }

        //校验条码是否存在未出站的数据
        hmeCosCommonService.verifyMaterialLotSiteOut(tenantId, materialLotId);

        if (hmeEoJobSnRepository.checkGettingChipFlag(tenantId, materialLotId, dto.getOperationId())) {
            throw new MtException("HME_CHIP_TRANSFER_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_011", "HME", dto.getBarcode()));
        }

        HmeCosOperationRecord cosOperationRecord = new HmeCosOperationRecord();
        cosOperationRecord.setOperationRecordId(materialLotAttrMap.get("COS_RECORD"));
        cosOperationRecord = hmeCosOperationRecordRepository.selectOne(cosOperationRecord);
        if (Objects.isNull(cosOperationRecord) || StringUtils.isEmpty(cosOperationRecord.getOperationRecordId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "工单工艺工位在制记录信息"));
        }
        if (StringUtils.isBlank(cosOperationRecord.getWorkOrderId()) || StringUtils.isBlank(cosOperationRecord.getWafer())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码的工单号及Wafer"));
        }
        // 20211126 add by sanfeng.zhang for peng.zhao 对查询来料记录信息加锁
        String objectRecordCode = this.spliceObjectRecordCode(tenantId, cosOperationRecord.getWorkOrderId(), dto.getOperationId(), dto.getWorkcellId(), materialLotAttrMap.get("WAFER_NUM"), dto.getEquipmentId());
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("取片平台");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.RECORD);
        hmeObjectRecordLockDTO.setObjectRecordId("");
        hmeObjectRecordLockDTO.setObjectRecordCode(objectRecordCode);
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //加锁
        hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, Collections.singletonList(hmeObjectRecordLock));

        HmeCosGetChipScanBarcodeResponseDTO responseDTO = null;
        try {
            //根据WKC_ID+工艺_ID+设备_ID（可为空），查询最近一条数据
            HmeCosOperationRecord lastCosOperationRecord = hmeCosOperationRecordRepository.queryLastRecord(tenantId, dto.getWorkcellId(), dto.getOperationId(), dto.getEquipmentId(), "N");
            if (!Objects.isNull(lastCosOperationRecord) && StringUtils.isNotEmpty(lastCosOperationRecord.getOperationRecordId())
                    && !Objects.isNull(lastCosOperationRecord.getSurplusCosNum()) && lastCosOperationRecord.getSurplusCosNum() > 0) {
                //校验上两步查出的 工单/Wafer 是否一致
                if (!cosOperationRecord.getWorkOrderId().equals(lastCosOperationRecord.getWorkOrderId())
                        || !cosOperationRecord.getWafer().equals(lastCosOperationRecord.getWafer())) {
                    throw new MtException("HME_CHIP_TRANSFER_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_012", "HME", lastCosOperationRecord.getWorkOrderId(), lastCosOperationRecord.getWafer()));
                }
                // 取消批次校验 By 田欣 20210916 王利娟提出
//            //校验批次是否一致
//            if (!StringUtils.trimToEmpty(lastCosOperationRecord.getAttribute1())
//                    .equals(StringUtils.trimToEmpty(hmeCosMaterialLotVO.getLot()))) {
//                throw new MtException("HME_COS_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_COS_005", "HME"));
//            }
            }

            //2020-12-01 add by chaonan.hu for zhenyong.ban 增加对上一步骤的校验
            hmeTimeProcessPdaRepository.operationVerify(tenantId, materialLotId, dto.getOperationId());

            List<String> materialLotIdList = new ArrayList<>();
            materialLotIdList.add(materialLotId);
            List<HmeCosPatchPdaVO9> labCodeAndRemarkAttrList = hmeCosPatchPdaMapper.labCodeAndRemarkAttrQuery(tenantId, materialLotIdList);
            String sourceMaterialLotLabCode = null;
            String sourceMaterialLotLabRemark = null;
            if(CollectionUtils.isNotEmpty(labCodeAndRemarkAttrList)){
                sourceMaterialLotLabCode = labCodeAndRemarkAttrList.get(0).getLabCode();
                sourceMaterialLotLabRemark = labCodeAndRemarkAttrList.get(0).getRemark();
            }
            //2021-11-08 10:46 add by chaonan.hu for yiming.zhang 增加对条码实验代码与芯片实验代码是否一致的校验
            List<String> chipLabCodeList = hmeCosGetChipPlatformMapper.chipLabCodeQuery(tenantId, materialLotId);
            if(CollectionUtils.isEmpty(chipLabCodeList)){
                //未查询到条码${1}上的芯片信息,请检查!
                throw new MtException("HME_LAB_CODE_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LAB_CODE_009", "HME", hmeCosMaterialLotVO.getMaterialLotCode()));
            }
            if(StringUtils.isBlank(sourceMaterialLotLabCode)){
                //当条码实验代码为空时，如果有芯片的实验代码不为空，则不一致
                long chipLabCodeNotNullCount = chipLabCodeList.stream().filter(item -> StringUtils.isNotBlank(item)).count();
                if(chipLabCodeNotNullCount != 0){
                    //扫描条码的实验代码与芯片上的实验代码不一致,请检查!
                    throw new MtException("HME_LAB_CODE_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LAB_CODE_008", "HME"));
                }
            }else {
                //当条码实验代码不为空时，如果有芯片的实验代码不等于条码实验代码，则不一致
                String finalSourceMaterialLotLabCode = sourceMaterialLotLabCode;
                long chipLabCodeNotEqualsCount = chipLabCodeList.stream().filter(item -> !finalSourceMaterialLotLabCode.equals(item)).count();
                if(chipLabCodeNotEqualsCount != 0){
                    //扫描条码的实验代码与芯片上的实验代码不一致,请检查!
                    throw new MtException("HME_LAB_CODE_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LAB_CODE_008", "HME"));
                }
            }

            //2021-10-27 17:01 add by chaonan.hu for yiming.zhang 增加对条码实验代码和实验备注的校验
            if(Objects.nonNull(dto.getMaterialLotInfo()) && StringUtils.isNotBlank(dto.getMaterialLotInfo().getMaterialLotId())){
                if(!dto.getMaterialLotInfo().getLabCode().equals(sourceMaterialLotLabCode) || !dto.getMaterialLotInfo().getLabRemark().equals(sourceMaterialLotLabRemark)){
                    throw new MtException("HME_LAB_CODE_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LAB_CODE_003", "HME"));
                }
            }

            //查询基础返回的信息
            responseDTO = hmeCosCommonService.getBaseScanBarcodeResponseDTO(tenantId, hmeCosMaterialLotVO, cosOperationRecord);
            responseDTO.setLabCode(sourceMaterialLotLabCode);
            responseDTO.setLabRemark(sourceMaterialLotLabRemark);

            //查询装载信息
            //List<HmeMaterialLotLoadVO> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.queryLoadDatasByMaterialLotId(tenantId, hmeCosMaterialLotVO.getMaterialLotId());
            List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.queryLoadDetailByMaterialLotId(tenantId, hmeCosMaterialLotVO.getMaterialLotId());
            List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList1 = new ArrayList<>();
            for (int i = 1; i <= Integer.parseInt(responseDTO.getLocationRow()); i++) {
                for (int j = 1; j <= Integer.parseInt(responseDTO.getLocationColumn()); j++) {
                    Long loadRow = (long) i;
                    Long loadColumn = (long) j;
                    List<HmeMaterialLotLoadVO2> collect = hmeMaterialLotLoadList.stream()
                            .filter(t -> t.getLoadRow().equals(loadRow) && t.getLoadColumn().equals(loadColumn))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(collect)) {
                        hmeMaterialLotLoadList1.add(collect.get(0));
                    } else {
                        hmeMaterialLotLoadList1.add(new HmeMaterialLotLoadVO2());
                    }
                }
            }
            responseDTO.setMaterialLotLoadList(hmeMaterialLotLoadList1);

            //查询工单号
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, responseDTO.getWorkOrderId());
            if (Objects.nonNull(mtWorkOrder)) {
                responseDTO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
            }

            //创建/更新工单工艺在制记录
            HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
            hmeWoJobSn.setTenantId(tenantId);
            hmeWoJobSn.setWorkOrderId(responseDTO.getWorkOrderId());
            hmeWoJobSn.setOperationId(dto.getOperationId());
            hmeWoJobSn = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
            if (Objects.isNull(hmeWoJobSn) || StringUtils.isEmpty(hmeWoJobSn.getWoJobSnId())) {
                hmeWoJobSn = new HmeWoJobSn();
                hmeWoJobSn.setTenantId(tenantId);
                hmeWoJobSn.setWorkOrderId(responseDTO.getWorkOrderId());
                hmeWoJobSn.setOperationId(dto.getOperationId());
                hmeWoJobSn.setSiteId(hmeCosMaterialLotVO.getSiteId());
                hmeWoJobSn.setSiteInNum(responseDTO.getPrimaryUomQty());
                hmeWoJobSnRepository.insertSelective(hmeWoJobSn);
            } else {
                hmeWoJobSn.setSiteId(hmeCosMaterialLotVO.getSiteId());
                //2020-11-03 edit by chaonan.hu for zhenyong.ban 对site_in_num进行累加
                hmeWoJobSn.setSiteInNum(hmeWoJobSn.getSiteInNum() + responseDTO.getPrimaryUomQty());
                hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
            }

            //创建/更新工单工艺工位在制记录
            HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
            hmeCosOperationRecord.setTenantId(tenantId);
            hmeCosOperationRecord.setWorkOrderId(responseDTO.getWorkOrderId());
            hmeCosOperationRecord.setOperationId(dto.getOperationId());
            hmeCosOperationRecord.setWorkcellId(dto.getWorkcellId());
            hmeCosOperationRecord.setWafer(responseDTO.getWafer());
            hmeCosOperationRecord.setEquipmentId(dto.getEquipmentId());
//        hmeCosOperationRecord.setAttribute1(hmeCosMaterialLotVO.getLot());  //取消来源批次号后，来料信息不再记录来源批次 By 田欣 2021-10-12
            hmeCosOperationRecord = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecord);
            if (Objects.isNull(hmeCosOperationRecord) || StringUtils.isEmpty(hmeCosOperationRecord.getOperationRecordId())) {
                // 新增记录时 如果存在未出站记录 则不允许新增
                if (!hmeEoJobSnRepository.checkNotSiteOutByWkcId(tenantId, dto.getWorkcellId(), "COS_FETCH_IN")) {
                    throw new MtException("HME_COS_PATCH_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_0007", "HME"));
                }
                hmeCosOperationRecord = new HmeCosOperationRecord();
                hmeCosOperationRecord.setTenantId(tenantId);
                hmeCosOperationRecord.setWorkOrderId(responseDTO.getWorkOrderId());
                hmeCosOperationRecord.setOperationId(dto.getOperationId());
                hmeCosOperationRecord.setWorkcellId(dto.getWorkcellId());
                hmeCosOperationRecord.setWafer(responseDTO.getWafer());
                hmeCosOperationRecord.setEquipmentId(dto.getEquipmentId());
                hmeCosOperationRecord.setSiteId(hmeCosMaterialLotVO.getSiteId());
                hmeCosOperationRecord.setCosNum(responseDTO.getPrimaryUomQty());
                hmeCosOperationRecord.setSurplusCosNum(responseDTO.getPrimaryUomQty());
                hmeCosOperationRecord.setMaterialId(responseDTO.getMaterialId());
                hmeCosOperationRecord.setCosType(responseDTO.getCosType());
                hmeCosOperationRecord.setAverageWavelength(responseDTO.getAverageWavelength());
                hmeCosOperationRecord.setType(responseDTO.getType());
                hmeCosOperationRecord.setLotNo(responseDTO.getLotNo());
                hmeCosOperationRecord.setRemark(responseDTO.getRemark());
                if(StringUtils.isNotBlank(responseDTO.getContainerType())){
                    String containerType = responseDTO.getContainerType();
                    MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
                        setTenantId(tenantId);
                        setContainerTypeCode(containerType);
                    }});
                    if(Objects.nonNull(mtContainerType)){
                        hmeCosOperationRecord.setContainerTypeId(mtContainerType.getContainerTypeId());
                    }
                }
//            hmeCosOperationRecord.setAttribute1(hmeCosMaterialLotVO.getLot());
                hmeCosOperationRecord.setJobBatch(responseDTO.getWorkingLot());
                hmeCosOperationRecord.setAttribute2(hmeCosMaterialLotVO.getSupplierId());
                hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);

                // 保存历史记录
                HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
                BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
                hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            } else {
                Long cosNum = Objects.isNull(hmeCosOperationRecord.getCosNum()) ? responseDTO.getPrimaryUomQty() :
                        hmeCosOperationRecord.getCosNum() + responseDTO.getPrimaryUomQty();
                Long surplusCosNum = Objects.isNull(hmeCosOperationRecord.getSurplusCosNum()) ? responseDTO.getPrimaryUomQty() :
                        hmeCosOperationRecord.getSurplusCosNum() + responseDTO.getPrimaryUomQty();
                hmeCosOperationRecord.setCosNum(cosNum);
                hmeCosOperationRecord.setSurplusCosNum(surplusCosNum);
                hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

                // 保存历史记录
                HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
                BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
                hmeCosOperationRecordHis.setTenantId(tenantId);
                hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            }
            responseDTO.setCosRecord(hmeCosOperationRecord.getOperationRecordId());
            responseDTO.setSurplusCosNum(hmeCosOperationRecord.getSurplusCosNum());
            List<HmeEoJobSn> cosFetchOut = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                            .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, dto.getOperationId())
                            .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "COS_FETCH_OUT")
                            .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                    .build());
            if (CollectionUtils.isNotEmpty(cosFetchOut)) {
                List<String> materialLotIds1 = cosFetchOut.stream().map(HmeEoJobSn::getMaterialLotId).collect(Collectors.toList());
                List<MtMaterialLot> mtMaterialLot = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds1);
                if (CollectionUtils.isNotEmpty(mtMaterialLot)) {
                    double sum = mtMaterialLot.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
                    responseDTO.setAddNum(new BigDecimal(responseDTO.getSurplusCosNum()).subtract(new BigDecimal(sum)).doubleValue());
                } else {
                    responseDTO.setAddNum(responseDTO.getSurplusCosNum().doubleValue());
                }
            } else {
                responseDTO.setAddNum(responseDTO.getSurplusCosNum().doubleValue());
            }

            //新增EOJobSn
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setShiftId(dto.getWkcShiftId());
            hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(cosOperationRecord.getWorkOrderId());
            hmeEoJobSn.setOperationId(dto.getOperationId());
            hmeEoJobSn.setSnMaterialId(hmeCosMaterialLotVO.getMaterialId());
            hmeEoJobSn.setMaterialLotId(materialLotId);
            hmeEoJobSn.setJobType("COS_FETCH_IN");
            hmeEoJobSn.setSourceJobId(responseDTO.getCosRecord());
            hmeEoJobSn.setSnQty(new BigDecimal(hmeCosMaterialLotVO.getPrimaryUomQty()));
            // 查询最近的条码历史记录到hme_eo_job_sn表中attribute3
            // String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, hmeEoJobSn.getMaterialLotId());
            hmeEoJobSn.setAttribute3(materialLotAttrMap.get("COS_TYPE"));
            hmeEoJobSn.setAttribute5(materialLotAttrMap.get("WAFER_NUM"));
            hmeCosCommonService.eoJobSnSiteIn(tenantId, hmeEoJobSn);
            hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId, Collections.singletonList(hmeEoJobSn.getJobId()), dto.getWorkcellId());
            responseDTO.setEoJobSnId(hmeEoJobSn.getJobId());
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , Collections.singletonList(hmeObjectRecordLock) , HmeConstants.ConstantValue.YES);
        }
        return getCosGetChipScanBarcodeResponse(responseDTO);
    }

    public String spliceObjectRecordCode(Long tenantId, String workOrderId, String operationId, String workcellId, String waferNum, String equipmentId) {
        StringBuffer sb = new StringBuffer();
        sb.append(tenantId);
        sb.append(workOrderId);
        sb.append(operationId);
        sb.append(workcellId);
        sb.append(waferNum);
        sb.append(equipmentId);
        return sb.toString();
    }

    /**
     * @param tenantId 租户ID
     * @param dto      参数
     * @return void
     * @Description 进站确认
     * @author yuchao.wang
     * @date 2020/8/18 19:50
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fetchInConfirm(Long tenantId, HmeCosGetChipSiteInConfirmDTO dto) {
        //非空校验
        if (CollectionUtils.isEmpty(dto.getMaterialLotList())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "物料批信息"));
        }
        for (HmeCosGetChipMaterialLotConfirmDTO materialLot : dto.getMaterialLotList()) {
            if (StringUtils.isEmpty(materialLot.getMaterialLotId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "物料批ID"));
            }
            if (Objects.isNull(materialLot.getPrimaryUomQty())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "数量"));
            }
            if (StringUtils.isEmpty(materialLot.getEoJobSnId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "eoSN作业记录ID"));
            }
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位"));
        }
        //获取事件ID
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("COS_FETCH_IN");
        }});
        List<String> ncRecordIdList = new ArrayList<>();
        List<String> ncLoadIdList = new ArrayList<>();
        List<String> loadIdList = new ArrayList<>();
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtCommonExtendVO7> mtCommonExtendVO7List = new ArrayList<>();
        Date now = new Date();

        List<String> materialLotIdList = dto.getMaterialLotList().stream().map(HmeCosGetChipMaterialLotConfirmDTO::getMaterialLotId).collect(Collectors.toList());
        List<HmeCosPatchPdaVO9> materialLotAttrList = hmeCosPatchPdaMapper.labCodeRemarkAndWoAttrQuery(tenantId, materialLotIdList);
        List<HmeMaterialLotLoad> loadSequenceList = hmeCosPatchPdaMapper.loadSequenceQueryByMaterialLotId(tenantId, materialLotIdList);

        List<HmeMaterialLotLabCode> insertMaterialLotLabCodeList = new ArrayList<>();
        for (HmeCosGetChipMaterialLotConfirmDTO materialLotDto : dto.getMaterialLotList()) {
            //2021-04-22 15:29 edit by chaonan.hu for kang.wang 物料批更新改为批量
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(materialLotDto.getMaterialLotId());
            mtMaterialLotVO20.setTrxPrimaryUomQty(-1 * materialLotDto.getPrimaryUomQty().doubleValue());
            mtMaterialLotVO20.setInLocatorTime(now);
            mtMaterialLotVO20.setEnableFlag("N");
            mtMaterialLotVO20List.add(mtMaterialLotVO20);
//            //调用API更新来源条码
//            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
//                setEventId(eventId);
//                setMaterialLotId(materialLotDto.getMaterialLotId());
//                setTrxPrimaryUomQty(-1 * materialLotDto.getPrimaryUomQty().doubleValue());
//                setInLocatorTime(new Date());
//                setEnableFlag("N");
//            }}, "N");
            //2021-04-22 15:29 edit by chaonan.hu for kang.wang 物料批扩展属性更新改为批量
            MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
            mtCommonExtendVO7.setKeyId(materialLotDto.getMaterialLotId());
            List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
            MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
            mtCommonExtendVO4.setAttrName("CURRENT_ROUTER_STEP");
            mtCommonExtendVO4.setAttrValue(null);
            mtCommonExtendVO4List.add(mtCommonExtendVO4);
            mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
            mtCommonExtendVO7List.add(mtCommonExtendVO7);
            //2020-12-18 add by chaonan.hu for zhenyong.ban 删除条码的扩展属性CURRENT_ROUTER_STEP
//            MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
//            mtMaterialLotAttrVO3.setEventId(eventId);
//            mtMaterialLotAttrVO3.setMaterialLotId(materialLotDto.getMaterialLotId());
//            List<MtExtendVO5> attrList = new ArrayList<>();
//            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//            mtExtendVO5.setAttrName("CURRENT_ROUTER_STEP");
//            mtExtendVO5.setAttrValue(null);
//            attrList.add(mtExtendVO5);
//            mtMaterialLotAttrVO3.setAttr(attrList);
//            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);

            //add by wenzhang.yu for zhenyong.ban   2020.11.23 删除扣减现有量
            //add by wenzhnag.yu for zhenyong.ban   2020.10.04 扣减现有量
            //MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            //mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
            //mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
            //mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
            //mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
            //mtInvOnhandQuantityVO9.setChangeQuantity(mtMaterialLot.getPrimaryUomQty());
            //mtInvOnhandQuantityVO9.setEventId(eventId);
            //mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

            //EoJobSn出站
            HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO = new HmeCosEoJobSnSiteOutVO();
            hmeCosEoJobSnSiteOutVO.setEoJobSnId(materialLotDto.getEoJobSnId());
            hmeCosEoJobSnSiteOutVO.setEquipmentList(dto.getEquipmentList());
            hmeCosEoJobSnSiteOutVO.setWorkcellId(dto.getWorkcellId());
            hmeCosCommonService.eoJobSnSiteOut(tenantId, hmeCosEoJobSnSiteOutVO);

            //删除装载表及相关不良表信息 2021-04-21 15:00 edit by chaonan.hu 改为根据主键批量删除数据
            List<String> ncRecords = hmeMaterialLotNcRecordMapper.getNcRecordByMaterialLotId(tenantId, materialLotDto.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(ncRecords)){
                ncRecordIdList.addAll(ncRecords);
            }
            List<String> ncLoads = hmeMaterialLotNcLoadMapper.getNcLoadByMaterialLotId(tenantId, materialLotDto.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(ncLoads)){
                ncLoadIdList.addAll(ncLoads);
            }
            List<String> loadIds = hmeMaterialLotLoadMapper.getLoadByMaterialLotId(tenantId, materialLotDto.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(loadIds)){
                loadIdList.addAll(loadIds);
            }
            //2021-11-09 add by chaonan.hu for yiming.zhang 记录数据到实验代码表中
            List<HmeCosPatchPdaVO9> singleMaterialLotAttrList = materialLotAttrList.stream().filter(item -> materialLotDto.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
            HmeCosPatchPdaVO9 singleMaterialLotAttr = singleMaterialLotAttrList.get(0);
            if(StringUtils.isNotBlank(singleMaterialLotAttr.getLabCode())){
                List<HmeMaterialLotLoad> singleLoadSequenceList = loadSequenceList.stream().filter(item -> materialLotDto.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
                for (HmeMaterialLotLoad hmeMaterialLotLoad:singleLoadSequenceList) {
                    HmeMaterialLotLabCode hmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    hmeMaterialLotLabCode.setTenantId(tenantId);
                    hmeMaterialLotLabCode.setMaterialLotId(materialLotDto.getMaterialLotId());
                    hmeMaterialLotLabCode.setObject("COS");
                    hmeMaterialLotLabCode.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                    hmeMaterialLotLabCode.setLabCode(singleMaterialLotAttr.getLabCode());
                    hmeMaterialLotLabCode.setJobId(materialLotDto.getEoJobSnId());
                    hmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                    if(StringUtils.isNotBlank(singleMaterialLotAttr.getWorkOrderId())){
                        hmeMaterialLotLabCode.setWorkOrderId(singleMaterialLotAttr.getWorkOrderId());
                    }
                    hmeMaterialLotLabCode.setSourceObject("MA");
                    hmeMaterialLotLabCode.setEnableFlag("Y");
                    if(StringUtils.isNotBlank(singleMaterialLotAttr.getRemark())){
                        hmeMaterialLotLabCode.setAttribute1(singleMaterialLotAttr.getRemark());
                    }
                    insertMaterialLotLabCodeList.add(hmeMaterialLotLabCode);
                }
            }
        }

        //批量更新物料批
        if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, HmeConstants.ConstantValue.NO);
        }
        //批量更新物料批扩展属性
        if(CollectionUtils.isNotEmpty(mtCommonExtendVO7List)){
            mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId, mtCommonExtendVO7List);
        }
        //批量根据主键删除数据
        if(CollectionUtils.isNotEmpty(ncRecordIdList)){
            List<List<String>> list = WmsCommonUtils.splitSqlList(ncRecordIdList, 500);
            list.forEach(deleteList -> hmeMaterialLotNcRecordMapper.deleteNcRecordByPrimarykey(tenantId, deleteList));
        }
        if(CollectionUtils.isNotEmpty(ncLoadIdList)){
            List<List<String>> list = WmsCommonUtils.splitSqlList(ncLoadIdList, 500);
            list.forEach(deleteList -> hmeMaterialLotNcLoadMapper.deleteNcLoadByPrimarykey(tenantId, deleteList));
        }
        if(CollectionUtils.isNotEmpty(loadIdList)){
            List<List<String>> list = WmsCommonUtils.splitSqlList(loadIdList, 500);
            list.forEach(deleteList -> hmeMaterialLotLoadMapper.deleteLoadByPrimarykey(tenantId, deleteList));
        }
        if(CollectionUtils.isNotEmpty(insertMaterialLotLabCodeList)){
            List<String> sqlList = new ArrayList<>();
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            Date nowDate = new Date();
            List<String> ids = mtCustomDbRepository.getNextKeys("hme_material_lot_lab_code_s", insertMaterialLotLabCodeList.size());
            List<String> cids = mtCustomDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", insertMaterialLotLabCodeList.size());
            int i = 0;
            for (HmeMaterialLotLabCode hmeMaterialLotLabCode:insertMaterialLotLabCodeList) {
                hmeMaterialLotLabCode.setCreatedBy(userId);
                hmeMaterialLotLabCode.setCreationDate(nowDate);
                hmeMaterialLotLabCode.setLastUpdatedBy(userId);
                hmeMaterialLotLabCode.setLastUpdateDate(nowDate);
                hmeMaterialLotLabCode.setLabCodeId(ids.get(i));
                hmeMaterialLotLabCode.setCid(Long.valueOf(cids.get(i)));
                hmeMaterialLotLabCode.setObjectVersionNumber(1L);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeMaterialLotLabCode));
                i++;
            }
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    /**
     * @param tenantId 租户ID
     * @param dto      参数
     * @return void
     * @Description 出站确认
     * @author yuchao.wang
     * @date 2020/8/18 20:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosGetChipSiteOutConfirmResponseDTO> fetchOutCreated(Long tenantId, HmeCosGetChipSiteOutConfirmDTO dto) {
        // 20210913 add by sanfeng.zhang for peng.zhao 对加锁
        //加锁
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("取片平台");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.RECORD);
        hmeObjectRecordLockDTO.setObjectRecordId("");
        hmeObjectRecordLockDTO.setObjectRecordCode(dto.getCosRecord());
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //加锁
        hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, Collections.singletonList(hmeObjectRecordLock));
        List<HmeCosGetChipSiteOutConfirmResponseDTO> responseDTOList = new ArrayList<>();
        try {
            //非空校验
            checkParamsForSiteOutCreated(tenantId, dto);

            //查询来源物料ID
            String sourceMaterialLotId = hmeEoJobSnMapper.querySourceMaterialLotIdForSiteOut(tenantId, dto.getWorkOrderId(), dto.getWorkcellId(), dto.getOperationId());

            //查询工单工艺工位在制记录信息
            HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
            hmeCosOperationRecord.setTenantId(tenantId);
            hmeCosOperationRecord.setOperationRecordId(dto.getCosRecord());
            hmeCosOperationRecord = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecord);
            if (Objects.isNull(hmeCosOperationRecord) || StringUtils.isEmpty(hmeCosOperationRecord.getOperationRecordId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "工单工艺工位在制记录信息"));
            }
            //调用API获取物料批相关信息
            HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, sourceMaterialLotId, false);
            Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();

            //查询容器相关信息
            HmeContainerCapacity hmeContainerCapacity = hmeContainerCapacityMapper.queryContainerCapacityForGetChip(tenantId,
                    dto.getContainerType(), hmeCosOperationRecord.getCosType(), dto.getOperationId());
            if (Objects.isNull(hmeContainerCapacity) || StringUtils.isEmpty(hmeContainerCapacity.getContainerCapacityId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "容器信息"));
            }
            if (Objects.isNull(hmeContainerCapacity.getColumnNum()) || Objects.isNull(hmeContainerCapacity.getLineNum())
                    || Objects.isNull(hmeContainerCapacity.getCapacity())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "容器容量【" + hmeContainerCapacity.getContainerCapacityId() + "】行、列、芯片数"));
            }

            //取片限制每个格子只能装一片
            if (!hmeContainerCapacity.getCapacity().equals(1L)) {
                throw new MtException("HME_COS_PATCH_PDA_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0008", "HME"));
            }

            //计算单盒待转移芯片数
            long containerShipNum = hmeContainerCapacity.getColumnNum() * hmeContainerCapacity.getLineNum() * hmeContainerCapacity.getCapacity();

            //计算默认容器实际装载的行数/列数，最后一行的列数（这里的行 针对于填充方向可能为横向可能为纵向，先填充的是行）
            long lineCount = hmeContainerCapacity.getLineNum();
            long columnCount = hmeContainerCapacity.getColumnNum();
            long lastLineCount = hmeContainerCapacity.getColumnNum();
            if (dto.getActualChipNum() > containerShipNum) {
                throw new MtException("Exception", "条码芯片数量大于容器最大装载数量！");
            } else if (dto.getActualChipNum() < containerShipNum) {
                lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getColumnNum();
                lineCount = dto.getActualChipNum() / hmeContainerCapacity.getColumnNum() + (lastLineCount == 0 ? 0 : 1);
            }

            //获取默认装载方向 及循环条件
            String direction = StringUtils.trimToEmpty(hmeContainerCapacity.getAttribute1());

            //外层/内层 循环开始值 循环结束值 每次累加值
            long outerStart = 1;
            long outerEnd = lineCount;
            int outerStep = 1;
            long innerStart = 1;
            long innerEnd = hmeContainerCapacity.getColumnNum();
            int innerStep = 1;

            //根据不同方向确定装载模式
            switch (direction) {
                case "B":
                    //从上到下,从左到右
                    //计算容器实际装载的行数，最后一行的列数
                    lineCount = hmeContainerCapacity.getColumnNum();
                    columnCount = hmeContainerCapacity.getLineNum();
                    lastLineCount = hmeContainerCapacity.getLineNum();
                    if (dto.getActualChipNum() < containerShipNum) {
                        lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getLineNum();
                        lineCount = dto.getActualChipNum() / hmeContainerCapacity.getLineNum() + (lastLineCount == 0 ? 0 : 1);
                    }

                    //计算装载方向
                    outerEnd = lineCount;
                    innerEnd = hmeContainerCapacity.getLineNum();
                    break;
                case "C":
                    //从上到下,从右到左
                    //计算容器实际装载的行数，最后一行的列数
                    lineCount = hmeContainerCapacity.getColumnNum();
                    columnCount = hmeContainerCapacity.getLineNum();
                    lastLineCount = hmeContainerCapacity.getLineNum();
                    if (dto.getActualChipNum() < containerShipNum) {
                        lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getLineNum();
                        lineCount = dto.getActualChipNum() / hmeContainerCapacity.getLineNum() + (lastLineCount == 0 ? 0 : 1);
                    }

                    //计算装载方向
                    outerStart = hmeContainerCapacity.getColumnNum();
                    outerEnd = hmeContainerCapacity.getColumnNum() - lineCount + 1;
                    innerEnd = hmeContainerCapacity.getLineNum();
                    outerStep = -1;
                    break;
                case "D":
                    //从下到上,从右到左
                    //计算容器实际装载的行数，最后一行的列数
                    lineCount = hmeContainerCapacity.getColumnNum();
                    columnCount = hmeContainerCapacity.getLineNum();
                    lastLineCount = hmeContainerCapacity.getLineNum();
                    if (dto.getActualChipNum() < containerShipNum) {
                        lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getLineNum();
                        lineCount = dto.getActualChipNum() / hmeContainerCapacity.getLineNum() + (lastLineCount == 0 ? 0 : 1);
                    }

                    //计算装载方向
                    outerStart = hmeContainerCapacity.getColumnNum();
                    outerEnd = hmeContainerCapacity.getColumnNum() - lineCount + 1;
                    innerStart = hmeContainerCapacity.getLineNum();
                    innerEnd = 1;
                    outerStep = -1;
                    innerStep = -1;
                    break;
                case "E":
                    //从下到上,从左到右
                    //计算默认容器实际装载的行列
                    lineCount = hmeContainerCapacity.getColumnNum();
                    lastLineCount = hmeContainerCapacity.getLineNum();
                    if (dto.getActualChipNum() < containerShipNum) {
                        lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getLineNum();
                        lineCount = dto.getActualChipNum() / hmeContainerCapacity.getLineNum() + (lastLineCount == 0 ? 0 : 1);
                    }

                    //计算装载方向
                    outerEnd = lineCount;
                    innerStart = hmeContainerCapacity.getLineNum();
                    innerEnd = 1;
                    innerStep = -1;
                    break;
                case "A":
                    //从左到右,从上到下
                default:
                    //从左到右,从上到下  A或者默认都是这种方式,上面已经计算过 不再重复计算
                    break;
            }

            //2020-12-18 add by chaonan.hu for zhenyong.ban 根据工单查询到工步及工艺之间的关联关系
            List<HmeCosGetChipPlatformVO> hmeCosGetChipPlatformVOS = hmeCosGetChipPlatformMapper.routerStepAndOperationQuery(tenantId, hmeCosOperationRecord.getWorkOrderId());
            if (CollectionUtils.isEmpty(hmeCosGetChipPlatformVOS)) {
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_028", "HME"));
            }
            //根据当前登录工位对应的工艺在关联关系中查找到对应工步
            List<HmeCosGetChipPlatformVO> afterFilterList = hmeCosGetChipPlatformVOS.stream().filter(item -> dto.getOperationId().equals(item.getOperationId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(afterFilterList)) {
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_028", "HME"));
            }
            String currentRouterStep = afterFilterList.get(0).getRouterStepId();

            //查询未出站数量
            long notPrintQty = hmeEoJobSnRepository.queryNotPrintQtyBySourceJobId(tenantId, dto.getCosRecord());

            //校验剩余芯片数
            if (Objects.isNull(hmeCosOperationRecord.getSurplusCosNum()) || hmeCosOperationRecord.getSurplusCosNum() < (dto.getActualChipNum() * dto.getMaterialLotCount() + notPrintQty)) {
                throw new MtException("Exception", "总待转移芯片数需大于芯片剩余总数！");
            }

            //获取事件ID
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("COS_FETCH_OUT");
            }});

            //批量获取序列
            int loadCount = (int) (dto.getMaterialLotCount() * lineCount * columnCount);
            List<String> eoJobSnIdList = customSequence.getNextKeys("hme_eo_job_sn_s", dto.getMaterialLotCount());
            List<String> eoJobSnCidList = customSequence.getNextKeys("hme_eo_job_sn_cid_s", dto.getMaterialLotCount());
            List<String> loadIdList = customSequence.getNextKeys("hme_material_lot_load_s", loadCount);
            List<String> loadCidList = customSequence.getNextKeys("hme_material_lot_load_cid_s", loadCount);
            List<String> ncLoadIdList = new ArrayList<>();
            List<String> ncLoadCidList = new ArrayList<>();
            List<String> ncRecordIdList = new ArrayList<>();
            List<String> ncRecordCidList = new ArrayList<>();
            List<String> cosNcRecordIdList = new ArrayList<>();
            List<String> cosNcRecordCidList = new ArrayList<>();

            //如果不良则获取不良相关序列
            if (StringUtils.isNotEmpty(dto.getNcCode())) {
                ncLoadIdList = customSequence.getNextKeys("hme_material_lot_nc_load_s", loadCount);
                ncLoadCidList = customSequence.getNextKeys("hme_material_lot_nc_load_cid_s", loadCount);
                ncRecordIdList = customSequence.getNextKeys("hme_material_lot_nc_record_s", loadCount);
                ncRecordCidList = customSequence.getNextKeys("hme_material_lot_nc_record_cid_s", loadCount);
                cosNcRecordIdList = customSequence.getNextKeys("hme_cos_nc_record_s", loadCount);
                cosNcRecordCidList = customSequence.getNextKeys("hme_cos_nc_record_cid_s", loadCount);
            }

            Long userId = -1L;
            if (!Objects.isNull(DetailsHelper.getUserDetails())
                    && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
                userId = DetailsHelper.getUserDetails().getUserId();
            }

            //批量获取条码Code
            HmeCosPatchPdaDTO4 cosPatchPdaDTO4 = new HmeCosPatchPdaDTO4();
            cosPatchPdaDTO4.setSiteId(dto.getSiteId());
            cosPatchPdaDTO4.setLot(dto.getLot());
            cosPatchPdaDTO4.setProdLineId(dto.getProdLineId());
            MtNumrangeVO8 mtNumrangeVO8 = hmeCosPatchPdaRepository.createIncomingValueList(tenantId, cosPatchPdaDTO4, dto.getMaterialLotCount().longValue(), dto.getCosType());
            if (Objects.isNull(mtNumrangeVO8) || CollectionUtils.isEmpty(mtNumrangeVO8.getNumberList())
                    || mtNumrangeVO8.getNumberList().size() != dto.getMaterialLotCount()) {
                throw new MtException("Exception", "生成条码CODE失败");
            }
            List<String> numberList = mtNumrangeVO8.getNumberList();

            //根据选择的条码数量 循环生成
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
            String lot = hmeCosOperationRecord.getAttribute1();
            int loadIndex = 0;
            int ncLoadIndex = 0;
            int cosNcRecordIndex = 0;
            // 默认站点
            String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
            // 不良代码
            MtNcCode mtNcCode = mtNcCodeRepository.selectOne(new MtNcCode() {{
                setTenantId(tenantId);
                setNcCode(dto.getNcCode());
            }});
            List<HmeEoJobSn> insertEoJobSnList = new ArrayList<>();
            List<HmeMaterialLotLoad> insertMaterialLotLoadList = new ArrayList<>();
            List<HmeMaterialLotNcLoad> insertMaterialLotNcLoadList = new ArrayList<>();
            List<HmeMaterialLotNcRecord> insertMaterialLotNcRecordList = new ArrayList<>();
            List<HmeCosNcRecord> insertCosNcRecordList = new ArrayList<>();
            String supplierId = hmeCosOperationRecord.getAttribute2();
            for (int materialLotCount = 0; materialLotCount < dto.getMaterialLotCount(); materialLotCount++) {
                HmeCosGetChipSiteOutConfirmResponseDTO responseDTO = new HmeCosGetChipSiteOutConfirmResponseDTO();
                responseDTO.setOperationRecordId(hmeCosOperationRecord.getOperationRecordId());

                //创建出站条码
                String materialLotCode = numberList.get(materialLotCount);
                Date now = new Date();
                String nowStr = sdf.format(now);
                MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setEventId(eventId);
                    setMaterialLotCode(materialLotCode);
                    setSiteId(hmeCosMaterialLotVO.getSiteId());
                    setEnableFlag("Y");
                    setQualityStatus(hmeCosMaterialLotVO.getQualityStatus());
                    setMaterialId(hmeCosMaterialLotVO.getMaterialId());
                    setPrimaryUomId(hmeCosMaterialLotVO.getPrimaryUomId());
                    setTrxPrimaryUomQty((double) dto.getActualChipNum());
                    setLocatorId(hmeCosMaterialLotVO.getLocatorId());
                    setLot(lot);
                    setLoadTime(now);
                    setCreateReason("INITIALIZE");
                    setEoId(hmeCosMaterialLotVO.getEoId());
                    setOnlyInsert(true);
                    setSupplierId(supplierId);
                }}, "N");
                responseDTO.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());

                //add by wenzhnag.yu for zhenyong.ban   2020.12.23 删除增加现有量
                //add by wenzhnag.yu for zhenyong.ban   2020.10.04 增加现有量
                //MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLotVO13.getMaterialLotId());
                //MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                //mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
                //mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
                //mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
                //mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                //mtInvOnhandQuantityVO9.setChangeQuantity(mtMaterialLot.getPrimaryUomQty());
                //mtInvOnhandQuantityVO9.setEventId(eventId);
                //mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);


                //调用API获取物料批相关信息
                MtMaterialLot outMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, sourceMaterialLotId);
                if (Objects.isNull(outMaterialLot) || StringUtils.isEmpty(outMaterialLot.getMaterialLotId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "出站物料批信息"));
                }
                responseDTO.setMaterialLotCode(outMaterialLot.getMaterialLotCode());
                responseDTO.setContainerShipNum(dto.getActualChipNum());

                //目标SN物料批扩展属性新增/更新
                List<MtExtendVO5> mtExtendVO5List = getMtExtendVO5List(dto, hmeContainerCapacity, materialLotAttrMap, hmeCosOperationRecord);
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName("CURRENT_ROUTER_STEP");
                mtExtendVO5.setAttrValue(currentRouterStep);
                mtExtendVO5List.add(mtExtendVO5);
                if(StringUtils.isNotBlank(dto.getLabCode())){
                    MtExtendVO5 labCodeAttr = new MtExtendVO5();
                    labCodeAttr.setAttrName("LAB_CODE");
                    labCodeAttr.setAttrValue(dto.getLabCode());
                    mtExtendVO5List.add(labCodeAttr);
                }
                if(StringUtils.isNotBlank(dto.getLabRemark())){
                    MtExtendVO5 labRemarkAttr = new MtExtendVO5();
                    labRemarkAttr.setAttrName("LAB_REMARK");
                    labRemarkAttr.setAttrValue(dto.getLabRemark());
                    mtExtendVO5List.add(labRemarkAttr);
                }
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                    setEventId(eventId);
                    setKeyId(mtMaterialLotVO13.getMaterialLotId());
                    setAttrs(mtExtendVO5List);
                }});

                //新增EOJobSn
                HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
                hmeEoJobSn.setTenantId(tenantId);
                hmeEoJobSn.setSiteInDate(now);
                hmeEoJobSn.setSiteInBy(userId);
                hmeEoJobSn.setShiftId(dto.getWkcShiftId());
                hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
                hmeEoJobSn.setWorkOrderId(dto.getWorkOrderId());
                hmeEoJobSn.setOperationId(dto.getOperationId());
                hmeEoJobSn.setSnMaterialId(hmeCosMaterialLotVO.getMaterialId());
                hmeEoJobSn.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
                hmeEoJobSn.setEoStepNum(1);
                hmeEoJobSn.setReworkFlag("N");
                hmeEoJobSn.setJobType("COS_FETCH_OUT");
                hmeEoJobSn.setSourceJobId(dto.getCosRecord());
                hmeEoJobSn.setJobId(eoJobSnIdList.get(materialLotCount));
                hmeEoJobSn.setCid(Long.valueOf(eoJobSnCidList.get(materialLotCount)));
                hmeEoJobSn.setObjectVersionNumber(1L);
                hmeEoJobSn.setCreatedBy(userId);
                hmeEoJobSn.setCreationDate(now);
                hmeEoJobSn.setLastUpdatedBy(userId);
                hmeEoJobSn.setLastUpdateDate(now);
                hmeEoJobSn.setSnQty(new BigDecimal(dto.getActualChipNum()));
//            // 查询最近的条码历史记录到hme_eo_job_sn表中attribute3
//            String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, hmeEoJobSn.getMaterialLotId());
                hmeEoJobSn.setAttribute3(hmeCosOperationRecord.getCosType());
                hmeEoJobSn.setAttribute5(hmeCosOperationRecord.getWafer());
                insertEoJobSnList.add(hmeEoJobSn);
                responseDTO.setEoJobSnId(hmeEoJobSn.getJobId());

                //装载表行序号、列序号
                long row = 0;
                long column = 0;

                //新增装载表数据，横向优先装载时外层为行序号 内层为列序号，竖向优先装载时内层循环为行序号
                List<String> materialLotLoadList = new ArrayList<>();
                for (long i = outerStart; verifyLoopEnd(direction, i, outerEnd, true); i = i + outerStep) {
                    for (long j = innerStart; verifyLoopEnd(direction, j, innerEnd, false); j = j + innerStep) {
                        //根据装载方式判断行列，竖向优先时内层循环为行序号
                        if ("B".equals(direction) || "C".equals(direction) || "D".equals(direction) || "E".equals(direction)) {
                            row = j;
                            column = i;
                        } else {
                            row = i;
                            column = j;
                        }

                        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                        hmeMaterialLotLoad.setTenantId(tenantId);
                        hmeMaterialLotLoad.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
                        hmeMaterialLotLoad.setLoadSequence(mtMaterialLotVO13.getMaterialLotId()
                                .substring(0, mtMaterialLotVO13.getMaterialLotId().length() - 2) + row + column + nowStr);
                        hmeMaterialLotLoad.setLoadRow(row);
                        hmeMaterialLotLoad.setLoadColumn(column);
                        hmeMaterialLotLoad.setCosNum(hmeContainerCapacity.getCapacity());
                        hmeMaterialLotLoad.setMaterialLotLoadId(loadIdList.get(loadIndex));
                        hmeMaterialLotLoad.setCid(Long.valueOf(loadCidList.get(loadIndex++)));
                        hmeMaterialLotLoad.setObjectVersionNumber(1L);
                        hmeMaterialLotLoad.setCreatedBy(userId);
                        hmeMaterialLotLoad.setCreationDate(now);
                        hmeMaterialLotLoad.setLastUpdatedBy(userId);
                        hmeMaterialLotLoad.setLastUpdateDate(now);
                        hmeMaterialLotLoad.setAttribute1(dto.getCosType());
                        hmeMaterialLotLoad.setAttribute2(hmeCosOperationRecord.getWafer());
                        hmeMaterialLotLoad.setAttribute3(dto.getWorkOrderId());
                        if(StringUtils.isNotBlank(dto.getLabCode())){
                            hmeMaterialLotLoad.setAttribute19(dto.getLabCode());
                        }
                        if(StringUtils.isNotBlank(dto.getLabRemark())){
                            hmeMaterialLotLoad.setAttribute20(dto.getLabRemark());
                        }
                        insertMaterialLotLoadList.add(hmeMaterialLotLoad);
                        materialLotLoadList.add(hmeMaterialLotLoad.getMaterialLotLoadId());

                        if (StringUtils.isNotEmpty(dto.getNcCode())) {
                            //取片每个格子只能装一片，这里不使用循环，LoadNum均为1
                            HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
                            hmeMaterialLotNcLoad.setTenantId(tenantId);
                            hmeMaterialLotNcLoad.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                            hmeMaterialLotNcLoad.setLoadNum("1");
                            hmeMaterialLotNcLoad.setNcLoadId(ncLoadIdList.get(ncLoadIndex));
                            hmeMaterialLotNcLoad.setCid(Long.valueOf(ncLoadCidList.get(ncLoadIndex)));
                            hmeMaterialLotNcLoad.setObjectVersionNumber(1L);
                            hmeMaterialLotNcLoad.setCreatedBy(userId);
                            hmeMaterialLotNcLoad.setCreationDate(now);
                            hmeMaterialLotNcLoad.setLastUpdatedBy(userId);
                            hmeMaterialLotNcLoad.setLastUpdateDate(now);
                            insertMaterialLotNcLoadList.add(hmeMaterialLotNcLoad);

                            HmeMaterialLotNcRecord hmeMaterialLotNcRecord = new HmeMaterialLotNcRecord();
                            hmeMaterialLotNcRecord.setTenantId(tenantId);
                            hmeMaterialLotNcRecord.setNcLoadId(hmeMaterialLotNcLoad.getNcLoadId());
                            hmeMaterialLotNcRecord.setNcCode(dto.getNcCode());
                            hmeMaterialLotNcRecord.setNcRecordId(ncRecordIdList.get(ncLoadIndex));
                            hmeMaterialLotNcRecord.setCid(Long.valueOf(ncRecordCidList.get(ncLoadIndex++)));
                            hmeMaterialLotNcRecord.setObjectVersionNumber(1L);
                            hmeMaterialLotNcRecord.setCreatedBy(userId);
                            hmeMaterialLotNcRecord.setCreationDate(now);
                            hmeMaterialLotNcRecord.setLastUpdatedBy(userId);
                            hmeMaterialLotNcRecord.setLastUpdateDate(now);
                            insertMaterialLotNcRecordList.add(hmeMaterialLotNcRecord);

                            // 2021-01-08 add by sanfeng.zhang 记录不良历史
                            // 保存芯片不良记录历史
                            HmeCosNcRecord hmeCosNcRecord = new HmeCosNcRecord();
                            hmeCosNcRecord.setTenantId(tenantId);
                            hmeCosNcRecord.setSiteId(defaultSiteId);
                            hmeCosNcRecord.setUserId(userId);
                            hmeCosNcRecord.setDefectCount(BigDecimal.valueOf(1));
                            hmeCosNcRecord.setJobId(hmeEoJobSn.getJobId());
                            hmeCosNcRecord.setMaterialLotId(hmeEoJobSn.getMaterialLotId());
                            hmeCosNcRecord.setComponentMaterialId(hmeEoJobSn.getSnMaterialId());
                            if (mtNcCode != null) {
                                hmeCosNcRecord.setNcCodeId(mtNcCode.getNcCodeId());
                                hmeCosNcRecord.setNcType(mtNcCode.getNcType());
                            }
                            hmeCosNcRecord.setLoadNum("1");
                            hmeCosNcRecord.setOperationId(dto.getOperationId());
                            hmeCosNcRecord.setWorkcellId(dto.getWorkcellId());
                            hmeCosNcRecord.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                            hmeCosNcRecord.setHotSinkCode(hmeMaterialLotLoad.getHotSinkCode());
                            hmeCosNcRecord.setWorkOrderId(hmeMaterialLotLoad.getAttribute3());
                            hmeCosNcRecord.setWaferNum(hmeMaterialLotLoad.getAttribute2());
                            hmeCosNcRecord.setCosType(hmeMaterialLotLoad.getAttribute1());
                            hmeCosNcRecord.setNcLoadRow(hmeMaterialLotLoad.getLoadRow());
                            hmeCosNcRecord.setNcLoadColumn(hmeMaterialLotLoad.getLoadColumn());
                            hmeCosNcRecord.setStatus(YES);
                            hmeCosNcRecord.setCosNcRecordId(cosNcRecordIdList.get(cosNcRecordIndex));
                            hmeCosNcRecord.setCid(Long.valueOf(cosNcRecordCidList.get(cosNcRecordIndex++)));
                            hmeCosNcRecord.setObjectVersionNumber(1L);
                            hmeCosNcRecord.setCreatedBy(userId);
                            hmeCosNcRecord.setCreationDate(now);
                            hmeCosNcRecord.setLastUpdatedBy(userId);
                            hmeCosNcRecord.setLastUpdateDate(now);
                            insertCosNcRecordList.add(hmeCosNcRecord);
                        }

                        //因为存在装不满的情况，如果是最后一行，要判断是否为最后一列
                        if ("D".equals(direction) || "E".equals(direction)) {
                            if (i == outerEnd && hmeContainerCapacity.getLineNum() - j + 1 == lastLineCount) {
                                break;
                            }
                        } else {
                            if (i == outerEnd && j == lastLineCount) {
                                break;
                            }
                        }
                    }
                }
                responseDTO.setMaterialLotLoadList(materialLotLoadList);
                responseDTOList.add(responseDTO);
            }
            hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId, eoJobSnIdList, dto.getWorkcellId());
            //新增数据
            hmeEoJobSnRepository.myBatchInsert(insertEoJobSnList);
            hmeMaterialLotLoadRepository.myBatchInsert(insertMaterialLotLoadList);
            if (CollectionUtils.isNotEmpty(insertMaterialLotNcLoadList)) {
                hmeMaterialLotNcLoadRepository.myBatchInsert(insertMaterialLotNcLoadList);
            }
            if (CollectionUtils.isNotEmpty(insertMaterialLotNcRecordList)) {
                hmeMaterialLotNcRecordRepository.myBatchInsert(insertMaterialLotNcRecordList);
            }
            if (CollectionUtils.isNotEmpty(insertCosNcRecordList)) {
                List<List<HmeCosNcRecord>> splitSqlList = InterfaceUtils.splitSqlList(insertCosNcRecordList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeCosNcRecord> domains : splitSqlList) {
                    hmeCosNcRecordRepository.batchInsert(domains);
                }
            }

        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , Collections.singletonList(hmeObjectRecordLock) , HmeConstants.ConstantValue.YES);
        }

        return responseDTOList;
    }

    /**
     * @param direction 装载模式
     * @param index     当前循环index
     * @param loopEnd   循环结束值
     * @param outerFlag 是否外层循环 true:是 false:否
     * @return boolean
     * @Description 根据 装载模式 及 内/外层循环 判断当前for循环条件
     * @author yuchao.wang
     * @date 2020/9/28 9:37
     */
    private boolean verifyLoopEnd(String direction, long index, long loopEnd, boolean outerFlag) {
        if (outerFlag) {
            //C D装载方向时，外层是由大到小循环，要改变条件
            if ("C".equals(direction) || "D".equals(direction)) {
                return index >= loopEnd;
            } else {
                return index <= loopEnd;
            }
        } else {
            //D E装载方向时，内层是由大到小循环，要改变条件
            if ("D".equals(direction) || "E".equals(direction)) {
                return index >= loopEnd;
            } else {
                return index <= loopEnd;
            }
        }
    }

    /**
     * @param tenantId 租户ID
     * @param dto      参数
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosGetChipSiteOutConfirmResponseDTO>
     * @Description 出站查询
     * @author yuchao.wang
     * @date 2020/8/19 16:01
     */
    @Override
    public HmeCosGetChipProcessingResponseDTO queryProcessing(Long tenantId, HmeCosGetChipSiteOutQueryDTO dto) {
        //校验非空
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工艺路线"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位"));
        }

        //根据WKC_ID+工艺_ID+设备_ID（可为空），查询最近一条数据
        HmeCosOperationRecord lastCosOperationRecord = hmeCosOperationRecordRepository.queryLastRecord(tenantId, dto.getWorkcellId(), dto.getOperationId(), dto.getEquipmentId(), "Y");
        if (Objects.isNull(lastCosOperationRecord) || StringUtils.isEmpty(lastCosOperationRecord.getOperationRecordId())) {
            return new HmeCosGetChipProcessingResponseDTO();
        }

        HmeCosGetChipProcessingResponseDTO responseDTO = new HmeCosGetChipProcessingResponseDTO();

        //查询是否有进站未完成数据 以及基础数据
        List<HmeEoJobSn> hmeEoJobSnInList = hmeEoJobSnMapper.queryEoJobSnBySourceJobId(tenantId, lastCosOperationRecord.getOperationRecordId(), "COS_FETCH_IN");
        if (CollectionUtils.isNotEmpty(hmeEoJobSnInList) && !Objects.isNull(hmeEoJobSnInList.get(0))) {
            //调用API获取物料批相关信息
            HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, hmeEoJobSnInList.get(0).getMaterialLotId(), false);

            //查询基础返回的信息
            HmeCosGetChipScanBarcodeResponseDTO baseResponseDTO = hmeCosCommonService.getBaseScanBarcodeResponseDTO(tenantId, hmeCosMaterialLotVO, lastCosOperationRecord);
            BeanUtils.copyProperties(baseResponseDTO, responseDTO);

            //查询装载信息
            List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.queryLoadDetailByMaterialLotId(tenantId, hmeCosMaterialLotVO.getMaterialLotId());
            List<HmeMaterialLotLoadVO2> hmeMaterialLotLoadList1 = new ArrayList<>();
            for (int i = 1; i <= Integer.parseInt(responseDTO.getLocationRow()); i++) {
                for (int j = 1; j <= Integer.parseInt(responseDTO.getLocationColumn()); j++) {
                    Long loadRow = (long) i;
                    Long loadColumn = (long) j;
                    List<HmeMaterialLotLoadVO2> collect = hmeMaterialLotLoadList.stream()
                            .filter(t -> t.getLoadRow().equals(loadRow) && t.getLoadColumn().equals(loadColumn))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(collect)) {
                        hmeMaterialLotLoadList1.add(collect.get(0));
                    } else {
                        hmeMaterialLotLoadList1.add(new HmeMaterialLotLoadVO2());
                    }
                }
            }
            responseDTO.setMaterialLotLoadList(hmeMaterialLotLoadList1);
            responseDTO.setEoJobSnId(hmeEoJobSnInList.get(0).getJobId());
            responseDTO = getCosGetChipProcessingResponse(responseDTO);
        } else {
            //查询物料数据
            MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, lastCosOperationRecord.getMaterialId());
            if (Objects.isNull(mtMaterialVO) || StringUtils.isEmpty(mtMaterialVO.getMaterialId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "物料信息"));
            }
            responseDTO.setMaterialName(mtMaterialVO.getMaterialName());
        }
        responseDTO.setCosRecord(lastCosOperationRecord.getOperationRecordId());
        responseDTO.setWorkOrderId(lastCosOperationRecord.getWorkOrderId());
        responseDTO.setAverageWavelength(lastCosOperationRecord.getAverageWavelength());
        responseDTO.setType(lastCosOperationRecord.getType());
        responseDTO.setLotNo(lastCosOperationRecord.getLotNo());
        responseDTO.setRemark(lastCosOperationRecord.getRemark());
        responseDTO.setSurplusCosNum(lastCosOperationRecord.getSurplusCosNum());
        responseDTO.setMaterialId(lastCosOperationRecord.getMaterialId());
        responseDTO.setCosType(lastCosOperationRecord.getCosType());
        responseDTO.setWafer(lastCosOperationRecord.getWafer());
        responseDTO.setLot(lastCosOperationRecord.getAttribute1());
        responseDTO.setWorkingLot(lastCosOperationRecord.getJobBatch());
        List<HmeEoJobSn> cosFetchOut = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                        .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, dto.getOperationId())
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "COS_FETCH_OUT")
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                .build());
        if (CollectionUtils.isNotEmpty(cosFetchOut)) {
            List<String> materialLotIds1 = cosFetchOut.stream().map(HmeEoJobSn::getMaterialLotId).collect(Collectors.toList());
            List<MtMaterialLot> mtMaterialLot = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds1);
            if (CollectionUtils.isNotEmpty(mtMaterialLot)) {
                double sum = mtMaterialLot.stream().mapToDouble(MtMaterialLot::getPrimaryUomQty).sum();
                responseDTO.setAddNum(new BigDecimal(responseDTO.getSurplusCosNum()).subtract(new BigDecimal(sum)).doubleValue());
            } else {
                responseDTO.setAddNum(responseDTO.getSurplusCosNum().doubleValue());
            }
        } else {
            responseDTO.setAddNum(responseDTO.getSurplusCosNum().doubleValue());
        }

        //查询工单号
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, responseDTO.getWorkOrderId());
        if (Objects.nonNull(mtWorkOrder)) {
            responseDTO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        }

        //查询所有出站物料批数据
        List<HmeMaterialLotVO> hmeMaterialLotVOList = hmeEoJobSnMapper.queryMaterialLotByJobId(tenantId, responseDTO.getCosRecord());
        if (CollectionUtils.isNotEmpty(hmeMaterialLotVOList)) {
            //分别将不良和良品返回
            List<HmeMaterialLotVO> siteOutOkList = new ArrayList<>();
            List<HmeMaterialLotVO> siteOutNgList = new ArrayList<>();
            hmeMaterialLotVOList.forEach(item -> {
                if (HmeConstants.ApiConstantValue.Y.equals(item.getNcFlag())) {
                    siteOutNgList.add(item);
                } else {
                    siteOutOkList.add(item);
                }
            });
            responseDTO.setSiteOutNgList(siteOutNgList);
            responseDTO.setSiteOutOkList(siteOutOkList);
        }

        return responseDTO;
    }

    /**
     * @param tenantId 租户ID
     * @param dto      参数
     * @return java.lang.String
     * @Description 出站打印
     * @author yuchao.wang
     * @date 2020/8/19 19:16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> fetchOut(Long tenantId, HmeCosGetChipSiteOutPrintDTO dto, HttpServletResponse response) {
        //非空校验
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位"));
        }
        if (StringUtils.isEmpty(dto.getOperationRecordId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单工艺工位在制记录ID"));
        }
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单ID"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工艺ID"));
        }
        if (CollectionUtils.isEmpty(dto.getSiteOutList())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "打印数据"));
        }

        // 20211202 add by sanfeng.zhang for yiming.zhang 打印出站加锁
        List<HmeObjectRecordLock> recordLockList = new ArrayList<>();
        for (HmeMaterialLotVO hmeMaterialLotVO : dto.getSiteOutList()) {
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("取片打印出站");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            hmeObjectRecordLockDTO.setObjectRecordId(hmeMaterialLotVO.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(hmeMaterialLotVO.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            recordLockList.add(hmeObjectRecordLock);
        }
        if (CollectionUtils.isNotEmpty(recordLockList)) {
            hmeObjectRecordLockRepository.batchCommonLockObject(tenantId ,recordLockList);
        }
        List<String> materialLotIdList = new ArrayList<>();
        try {
            //2021-07-12 11:29 add by chaonan.hu for zhenyong.ban 打印时,如果可新增数量(剩余芯片数-qty)小于0,则报错可新增数量小于0，请先撤回后再打印
            HmeCosOperationRecord hmeCosOperationRecordDb = new HmeCosOperationRecord();
            hmeCosOperationRecordDb.setOperationRecordId(dto.getOperationRecordId());
            hmeCosOperationRecordDb = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecordDb);
            if (Objects.isNull(hmeCosOperationRecordDb)) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "工单工艺工位在制记录数据"));
            }
            if (Objects.isNull(hmeCosOperationRecordDb.getSurplusCosNum())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "工单工艺工位在制记录剩余芯片数"));
            }
            BigDecimal qty = hmeCosPatchPdaMapper.getAddQty(tenantId, dto.getWorkcellId(), dto.getOperationId(), hmeCosOperationRecordDb.getWafer(), "COS_FETCH_OUT");
            BigDecimal addQty = BigDecimal.valueOf(hmeCosOperationRecordDb.getSurplusCosNum()).subtract(qty);
            if(addQty.compareTo(BigDecimal.ZERO) == -1){
                throw new MtException("HME_COS_PATCH_PDA_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0016", "HME"));
            }

            //循环出站
            Long processedNum = 0L;
            Long unqualifiedNum = 0L;
            for (HmeMaterialLotVO materialLotVO : dto.getSiteOutList()) {
                if (StringUtils.isEmpty(materialLotVO.getMaterialLotId())) {
                    throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_MATERIAL_INSP_0020", "QMS", "物料批ID"));
                }
                if (Objects.isNull(materialLotVO.getPrimaryUomQty())) {
                    throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_MATERIAL_INSP_0020", "QMS", "数量"));
                }
                if (StringUtils.isEmpty(materialLotVO.getEoJobSnId())) {
                    throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_MATERIAL_INSP_0020", "QMS", "eoSN作业记录ID"));
                }

                materialLotIdList.add(materialLotVO.getMaterialLotId());

                //如果已经出站就不再操作
                if (hmeEoJobSnRepository.checkSiteOutById(tenantId, materialLotVO.getEoJobSnId())) {
                    continue;
                }

                //EoJobSn出站
                HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO = new HmeCosEoJobSnSiteOutVO();
                BeanUtils.copyProperties(dto, hmeCosEoJobSnSiteOutVO);
                hmeCosEoJobSnSiteOutVO.setEoJobSnId(materialLotVO.getEoJobSnId());
                hmeCosCommonService.eoJobSnSiteOut(tenantId, hmeCosEoJobSnSiteOutVO);

                //已加工数
                processedNum += materialLotVO.getPrimaryUomQty();

                //判断是否有不良记录
                if (hmeMaterialLotLoadRepository.checkHasNcLoadFlag(tenantId, materialLotVO.getMaterialLotId())) {
                    unqualifiedNum += materialLotVO.getPrimaryUomQty();
                }
            }

            //如果数量不为0 则更新工单工艺在制记录
            if (processedNum != 0 || unqualifiedNum != 0) {
                //更新工单工艺在制记录
                HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
                hmeWoJobSn.setTenantId(tenantId);
                hmeWoJobSn.setWorkOrderId(dto.getWorkOrderId());
                hmeWoJobSn.setOperationId(dto.getOperationId());
                hmeWoJobSn = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
                if (Objects.isNull(hmeWoJobSn) || StringUtils.isEmpty(hmeWoJobSn.getWoJobSnId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "工单工艺在制记录数据"));
                }

                hmeWoJobSn.setProcessedNum(Objects.isNull(hmeWoJobSn.getProcessedNum()) ? processedNum :
                        hmeWoJobSn.getProcessedNum() + processedNum);
                hmeWoJobSn.setUnqualifiedNum(Objects.isNull(hmeWoJobSn.getUnqualifiedNum()) ? unqualifiedNum :
                        hmeWoJobSn.getUnqualifiedNum() + unqualifiedNum);
                hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);

                //更新工单工艺工位在制记录剩余芯片数
                HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
                hmeCosOperationRecord.setOperationRecordId(dto.getOperationRecordId());
                hmeCosOperationRecord = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecord);
                if (Objects.isNull(hmeCosOperationRecord) || StringUtils.isEmpty(hmeCosOperationRecord.getOperationRecordId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "工单工艺工位在制记录数据"));
                }
                if (Objects.isNull(hmeCosOperationRecord.getSurplusCosNum())) {
                    throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_MATERIAL_INSP_0020", "QMS", "工单工艺工位在制记录剩余芯片数"));
                }

                long surplusCosNum = hmeCosOperationRecord.getSurplusCosNum() - processedNum;

                //如果剩余芯片数为0，校验是否存在未出站投入条码
                if (surplusCosNum <= 0
                        && !hmeEoJobSnRepository.checkNotSiteOutByWkcId(tenantId, dto.getWorkcellId(), "COS_FETCH_IN")) {
                    throw new MtException("HME_COS_PATCH_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_0007", "HME"));
                }

                hmeCosOperationRecord.setSurplusCosNum(surplusCosNum);
                hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

                // 保存历史记录
                HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
                BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
                hmeCosOperationRecordHis.setTenantId(tenantId);
                hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            }
        } catch (Exception e) {
            throw new MtException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId, recordLockList, YES);
        }
        return materialLotIdList;
    }

    /**
     * @param tenantId          租户ID
     * @param materialLotIdList 物料批ID
     * @return java.lang.String
     * @Description 输出PDF文件流, 每个物料批为一页
     * @author yuchao.wang
     * @date 2020/8/19 20:22
     */
    @Override
    public void printPdf(Long tenantId, List<String> materialLotIdList, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(materialLotIdList)) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "条码ID"));
        }

        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("make-dir-failed", "创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }

        //调用API获取物料批相关信息
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        if (CollectionUtils.isEmpty(mtMaterialLotList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "物料批信息"));
        }
        String uuid = UUID.randomUUID().toString();

        //循环物料进行打印
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<File> imageFileList = new ArrayList<>();
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        for (int i = 0; i < mtMaterialLotList.size(); i++) {
            MtMaterialLot mtMaterialLot = mtMaterialLotList.get(i);
            //调用API获取物料批相关信息
            HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, mtMaterialLot.getMaterialLotId(), true, mtMaterialLot.getMaterialLotCode());
            Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();
            String workOrderNum = null;
            if (StringUtils.isNotEmpty(materialLotAttrMap.get("WORK_ORDER_ID"))) {
                MtWorkOrder mtWorkOrder = new MtWorkOrder();
                mtWorkOrder.setWorkOrderId(materialLotAttrMap.get("WORK_ORDER_ID"));
                mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtWorkOrder);
                workOrderNum = mtWorkOrder.getWorkOrderNum();
            }
            //生成条形码
            String barcodePath = basePath + "/" + uuid + "_" + mtMaterialLot.getMaterialLotId() + ".png";
            File imageFile = new File(barcodePath);
            try {
                CommonBarcodeUtil.generateCode128ToFile(mtMaterialLot.getMaterialLotCode(), CommonBarcodeUtil.IMG_TYPE_PNG, imageFile, 10);
                log.info("<====生成条形码完成！{}", barcodePath);

                //设置条码居中
                //CommonBarcodeUtil.repaintPictureToCenter(imageFile, 385, 66);
            } catch (Exception e) {
                log.error("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.generateImageFile Error", e);
                throw new MtException("Exception", e.getMessage());
            }
            int num = i % 2;
            //组装参数
            imgMap.put("barcodeImage" + num, barcodePath);
            formMap.put("barcode" + num, mtMaterialLot.getMaterialLotCode());
            formMap.put("waferNum" + num, materialLotAttrMap.get("WAFER_NUM"));
            formMap.put("chipQty" + num, mtMaterialLot.getPrimaryUomQty());
            formMap.put("workOrderNum" + num, workOrderNum);
            if (num == 1 || i == mtMaterialLotList.size() - 1) {
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            }
            imageFileList.add(imageFile);
        }

        //生成PDF
        String pdfPath = basePath + "/" + uuid + ".pdf";
        try {
            log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList);
            CommonPdfTemplateUtil.multiplePage(basePath + "/cos_qp_print_template.pdf", pdfPath, dataList);
            log.info("<==== 生成PDF完成！{}", pdfPath);
        } catch (Exception e) {
            log.error("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.generatePDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        }

        //将文件转化成流进行输出
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //设置相应参数
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //将文件转化成流进行输出
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.outputPDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.closeIO Error", e);
            }
        }

        //删除临时文件
        for (File imageFile : imageFileList) {
            if (!imageFile.delete()) {
                log.info("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.deleteImageFile Failed: {}", imageFile);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.deletePDFFile Failed: {}", pdfPath);
        }
    }

    /**
     * @param tenantId 租户ID
     * @param dto      参数
     * @Description 查询容器对应最大装载数量
     * @author yuchao.wang
     * @date 2020/9/8 10:29
     */
    @Override
    public HmeCosGetChipMaxLoadDTO queryMaxLoadNumber(Long tenantId, HmeCosGetChipMaxLoadDTO dto) {
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工艺路线"));
        }
        if (StringUtils.isEmpty(dto.getCosType())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单工艺工位在制COS类型"));
        }
        if (StringUtils.isEmpty(dto.getContainerType())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "容器类型"));
        }

        //查询容器相关信息
        HmeContainerCapacity hmeContainerCapacity = hmeContainerCapacityMapper.queryContainerCapacityForGetChip(tenantId,
                dto.getContainerType(), dto.getCosType(), dto.getOperationId());
        if (Objects.isNull(hmeContainerCapacity) || StringUtils.isEmpty(hmeContainerCapacity.getContainerCapacityId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "容器信息"));
        }
        if (Objects.isNull(hmeContainerCapacity.getColumnNum()) || Objects.isNull(hmeContainerCapacity.getLineNum())
                || Objects.isNull(hmeContainerCapacity.getCapacity())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "容器容量【" + hmeContainerCapacity.getContainerCapacityId() + "】行、列、芯片数"));
        }

        //取片限制每个格子只能装一片
        if (!hmeContainerCapacity.getCapacity().equals(1L)) {
            throw new MtException("HME_COS_PATCH_PDA_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0008", "HME"));
        }

        //计算单盒最大装载芯片数
        dto.setMaxLoadNumber(hmeContainerCapacity.getColumnNum() * hmeContainerCapacity.getLineNum() * hmeContainerCapacity.getCapacity());
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosGetChipDeleteDTO batchDelete(Long tenantId, HmeCosGetChipDeleteDTO dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "站点"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工艺路线"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位"));
        }
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单"));
        }
        if (CollectionUtils.isEmpty(dto.getMaterialLotIdList())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "物料批"));
        }
        for (String materialLotId : dto.getMaterialLotIdList()) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
            //根据material_lot_id+operation_id+workcell_id+job_type（COS_FETCH_IN）查询出站时间为空的数据，将其删除
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class).andWhere(Sqls.custom()
                    .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                    .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, dto.getOperationId())
                    .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLotId)
                    .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "COS_FETCH_IN")
                    .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).build());
            if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
                throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_010", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            hmeEoJobSnRepository.batchDeleteByPrimaryKey(hmeEoJobSnList);
            //扣减剩余芯片数
            //根据WKC_ID+工艺_ID+设备_ID（可为空），查询最近一条数据
            HmeCosOperationRecord lastCosOperationRecord = hmeCosOperationRecordRepository.queryLastRecord(tenantId, dto.getWorkcellId(), dto.getOperationId(), null, "Y");
            if (Objects.isNull(lastCosOperationRecord) || StringUtils.isEmpty(lastCosOperationRecord.getOperationRecordId())) {
                throw new MtException("HME_COS_PATCH_PDA_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0006", "HME"));
            }
            if (lastCosOperationRecord.getSurplusCosNum() < mtMaterialLot.getPrimaryUomQty()) {
                throw new MtException("HME_COS_PATCH_PDA_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0009", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            BigDecimal surplusCosNum = BigDecimal.valueOf(lastCosOperationRecord.getSurplusCosNum()).subtract(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
            lastCosOperationRecord.setSurplusCosNum(surplusCosNum.longValue());
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(lastCosOperationRecord);

            // 保存历史记录
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(lastCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            //扣减进站总数
            HmeWoJobSn hmeWoJobSn = hmeWoJobSnRepository.selectOne(new HmeWoJobSn() {{
                setTenantId(tenantId);
                setSiteId(dto.getSiteId());
                setWorkOrderId(dto.getWorkOrderId());
                setOperationId(dto.getOperationId());
            }});
            if (Objects.isNull(hmeWoJobSn)) {
                throw new MtException("HME_COS_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_023", "HME"));
            }
            if (hmeWoJobSn.getSiteInNum() < mtMaterialLot.getPrimaryUomQty()) {
                throw new MtException("HME_COS_024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_024", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            BigDecimal siteInNum = BigDecimal.valueOf(hmeWoJobSn.getSiteInNum()).subtract(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
            hmeWoJobSn.setSiteInNum(siteInNum.longValue());
            hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
        }
        return dto;
    }

    @Override
    public List<HmeCosGetChipMaterialLotListResponseDTO> queryInputMaterialLotList(Long tenantId, HmeCosGetChipMaterialLotListDTO dto) {
        List<HmeCosGetChipMaterialLotListResponseDTO> resultList = hmeCosGetChipPlatformMapper.queryInputMaterialLotList(tenantId, dto);
        if(CollectionUtils.isNotEmpty(resultList)){
            //2021-11-09 16:18 edit by chaonan.hu for yiming.zhang 取出查询列表中出站时间最近的那笔jobId
            List<HmeCosGetChipMaterialLotListResponseDTO> siteOutDateNonNullList = resultList.stream()
                    .filter(item -> Objects.nonNull(item.getSiteOutDate()))
                    .sorted(Comparator.comparing(HmeCosGetChipMaterialLotListResponseDTO::getSiteOutDate).reversed())
                    .collect(Collectors.toList());
            String labCode = "";
            String labRemark = "";
            if(CollectionUtils.isNotEmpty(siteOutDateNonNullList)){
                //根据jobId查询实验代码以及备注,返回至前台，用于新增界面默认带出来
                String jobId = siteOutDateNonNullList.get(0).getEoJobSnId();
                HmeCosPatchPdaVO9 labCodeAndRemark = hmeCosPatchPdaMapper.labCodeAndRemarkQueryByJobId(tenantId, jobId);
                if(Objects.nonNull(labCodeAndRemark)){
                    if(StringUtils.isNotBlank(labCodeAndRemark.getLabCode())){
                        labCode = labCodeAndRemark.getLabCode();
                    }
                    if(StringUtils.isNotBlank(labCodeAndRemark.getRemark())){
                        labRemark = labCodeAndRemark.getRemark();
                    }
                }
            }

            for (HmeCosGetChipMaterialLotListResponseDTO result : resultList) {
                result.setLabCode(labCode);
                result.setLabRemark(labRemark);
                if (Objects.isNull(result.getSiteOutDate())) {
                    result.setStatusFlag("N");
                    result.setStatus("未确认");
                } else {
                    result.setStatusFlag("Y");
                    result.setStatus("已确认");
                }
            }
        }
        return resultList;
    }

    @Override
    public List<HmeCosGetChipNcListDTO> queryNcList(Long tenantId, String materialLotId) {
        List<HmeCosGetChipNcListDTO> resultList = hmeCosGetChipPlatformMapper.queryNcList(tenantId, materialLotId);
        for (HmeCosGetChipNcListDTO result : resultList) {
            String position = String.valueOf((char) (64 + Long.valueOf(result.getLoadRow()))) +
                    result.getLoadColumn() + '-' + result.getLoadNum();
            result.setPosition(position);
        }
        return resultList;
    }

    /**
     * @param dto
     * @return void
     * @Description 出站新增校验入参
     * @author yuchao.wang
     * @date 2020/8/19 14:28
     */
    private void checkParamsForSiteOutCreated(Long tenantId, HmeCosGetChipSiteOutConfirmDTO dto) {
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工艺路线"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位"));
        }
        if (StringUtils.isEmpty(dto.getWkcShiftId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "班组"));
        }
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "站点"));
        }
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "产线"));
        }
        if (StringUtils.isEmpty(dto.getCosType())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单工艺工位在制COS类型"));
        }
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单ID"));
        }
        if (StringUtils.isEmpty(dto.getContainerType())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "容器类型"));
        }
        if (StringUtils.isEmpty(dto.getCosRecord())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单工艺工位在制记录ID"));
        }
        if (Objects.isNull(dto.getMaterialLotCount())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "生成条码数量"));
        }
        if (Objects.isNull(dto.getActualChipNum())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "条码芯片数量"));
        }
        if (dto.getMaterialLotCount() < 1) {
            throw new MtException("Exception", "生成条码数量应为正整数");
        }
        if (dto.getActualChipNum() < 1) {
            throw new MtException("Exception", "条码芯片数量应为正整数");
        }
    }

    /**
     * @param dto
     * @param hmeContainerCapacity
     * @param materialLotAttrMap
     * @return java.util.List<io.tarzan.common.domain.vo.MtExtendVO5>
     * @Description 构造扩展属性列表
     * @author yuchao.wang
     * @date 2020/8/19 9:45
     */
    private List<MtExtendVO5> getMtExtendVO5List(HmeCosGetChipSiteOutConfirmDTO dto,
                                                 HmeContainerCapacity hmeContainerCapacity,
                                                 Map<String, String> materialLotAttrMap,
                                                 HmeCosOperationRecord hmeCosOperationRecord) {
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
        MtExtendVO5 statusAttr1 = new MtExtendVO5();
        statusAttr1.setAttrName("MF_FLAG");
        statusAttr1.setAttrValue(HmeConstants.ConstantValue.YES);
        mtExtendVO5List.add(statusAttr1);
        MtExtendVO5 statusAttr2 = new MtExtendVO5();
        statusAttr2.setAttrName("COS_RECORD");
        statusAttr2.setAttrValue(dto.getCosRecord());
        mtExtendVO5List.add(statusAttr2);
        MtExtendVO5 statusAttr3 = new MtExtendVO5();
        statusAttr3.setAttrName("WAFER_NUM");
        statusAttr3.setAttrValue(hmeCosOperationRecord.getWafer());
        mtExtendVO5List.add(statusAttr3);
        MtExtendVO5 statusAttr4 = new MtExtendVO5();
        statusAttr4.setAttrName("CONTAINER_TYPE");
        statusAttr4.setAttrValue(dto.getContainerType());
        mtExtendVO5List.add(statusAttr4);
        MtExtendVO5 statusAttr5 = new MtExtendVO5();
        statusAttr5.setAttrName("COS_TYPE");
        statusAttr5.setAttrValue(hmeCosOperationRecord.getCosType());
        mtExtendVO5List.add(statusAttr5);
        MtExtendVO5 statusAttr6 = new MtExtendVO5();
        statusAttr6.setAttrName("LOCATION_ROW");
        statusAttr6.setAttrValue(Objects.isNull(hmeContainerCapacity.getLineNum()) ? "" : String.valueOf(hmeContainerCapacity.getLineNum()));
        mtExtendVO5List.add(statusAttr6);
        MtExtendVO5 statusAttr7 = new MtExtendVO5();
        statusAttr7.setAttrName("LOCATION_COLUMN");
        statusAttr7.setAttrValue(Objects.isNull(hmeContainerCapacity.getColumnNum()) ? "" : String.valueOf(hmeContainerCapacity.getColumnNum()));
        mtExtendVO5List.add(statusAttr7);
        MtExtendVO5 statusAttr8 = new MtExtendVO5();
        statusAttr8.setAttrName("CHIP_NUM");
        statusAttr8.setAttrValue(Objects.isNull(hmeContainerCapacity.getCapacity()) ? "" : String.valueOf(hmeContainerCapacity.getCapacity()));
        mtExtendVO5List.add(statusAttr8);
        MtExtendVO5 statusAttr9 = new MtExtendVO5();
        statusAttr9.setAttrName("REWORK_FLAG");
        statusAttr9.setAttrValue(materialLotAttrMap.get("REWORK_FLAG"));
        mtExtendVO5List.add(statusAttr9);
        MtExtendVO5 statusAttr10 = new MtExtendVO5();
        statusAttr10.setAttrName("PERFORMANCE_LEVEL");
        statusAttr10.setAttrValue(materialLotAttrMap.get("PERFORMANCE_LEVEL"));
        mtExtendVO5List.add(statusAttr10);
        MtExtendVO5 statusAttr11 = new MtExtendVO5();
        statusAttr11.setAttrName("PRODUCT_DATE");
        statusAttr11.setAttrValue(fm.format(new Date()));
        mtExtendVO5List.add(statusAttr11);
        MtExtendVO5 statusAttr12 = new MtExtendVO5();
        statusAttr12.setAttrName("STATUS");
        statusAttr12.setAttrValue("NEW");
        mtExtendVO5List.add(statusAttr12);
        MtExtendVO5 statusAttr13 = new MtExtendVO5();
        statusAttr13.setAttrName("AVG_WAVE_LENGTH");
        statusAttr13.setAttrValue(Objects.isNull(hmeCosOperationRecord.getAverageWavelength()) ? "" : String.valueOf(hmeCosOperationRecord.getAverageWavelength()));
        mtExtendVO5List.add(statusAttr13);
        MtExtendVO5 statusAttr14 = new MtExtendVO5();
        statusAttr14.setAttrName("TYPE");
        statusAttr14.setAttrValue(hmeCosOperationRecord.getType());
        mtExtendVO5List.add(statusAttr14);
        MtExtendVO5 statusAttr15 = new MtExtendVO5();
        statusAttr15.setAttrName("LOTNO");
        statusAttr15.setAttrValue(hmeCosOperationRecord.getLotNo());
        mtExtendVO5List.add(statusAttr15);
        MtExtendVO5 statusAttr16 = new MtExtendVO5();
        statusAttr16.setAttrName("WORKING_LOT");
        statusAttr16.setAttrValue(hmeCosOperationRecord.getJobBatch());
        mtExtendVO5List.add(statusAttr16);
        MtExtendVO5 statusAttr17 = new MtExtendVO5();
        statusAttr17.setAttrName("REMARK");
        statusAttr17.setAttrValue(hmeCosOperationRecord.getRemark());
        mtExtendVO5List.add(statusAttr17);
        MtExtendVO5 statusAttr18 = new MtExtendVO5();
        statusAttr18.setAttrName("WORK_ORDER_ID");
        statusAttr18.setAttrValue(hmeCosOperationRecord.getWorkOrderId());
        mtExtendVO5List.add(statusAttr18);
        return mtExtendVO5List;
    }

    /**
     * @param baseResponseDTO 参数
     * @return com.ruike.hme.api.dto.HmeCosGetChipScanBarcodeResponseDTO
     * @Description 构造返回参数不良位置描述列表
     * @author wenzhang.yu
     * @date 2020/9/28 9:51
     */
    private HmeCosGetChipScanBarcodeResponseDTO getCosGetChipScanBarcodeResponse(HmeCosGetChipScanBarcodeResponseDTO baseResponseDTO) {
        //拷贝属性
        HmeCosGetChipScanBarcodeResponseDTO responseDTO = new HmeCosGetChipScanBarcodeResponseDTO();
        BeanUtils.copyProperties(baseResponseDTO, responseDTO);

        //构造不良位置-描述列表
        List<HmeMaterialLotLoadVO2> materialLotLoadList = (List<HmeMaterialLotLoadVO2>) responseDTO.getMaterialLotLoadList();
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            List<HmeMaterialLotNcRecordVO2> materialLotNcList = new ArrayList<>();
            materialLotLoadList.forEach(load -> {
                //判断这个格子是否有不良芯片
                if (CollectionUtils.isNotEmpty(load.getDocList())) {
                    load.getDocList().forEach(ncLoad -> {
                        //遍历这个芯片的不良代码
                        ncLoad.getNcRecordList().forEach(ncRecord -> {
                            String position = String.valueOf((char) (64 + load.getLoadRow())) +
                                    '-' + load.getLoadColumn() + '-' + ncLoad.getLoadNum();
                            HmeMaterialLotNcRecordVO2 ncRecordVO2 = new HmeMaterialLotNcRecordVO2(ncLoad.getNcLoadId(),
                                    ncRecord.getNcRecordId(), position, ncRecord.getNcCode(), ncRecord.getNcDesc(), null, null);
                            materialLotNcList.add(ncRecordVO2);
                        });
                    });
                }
            });

            responseDTO.setMaterialLotNcList(materialLotNcList);
        }

        return responseDTO;
    }

    /**
     * @param baseResponseDTO 参数
     * @return com.ruike.hme.api.dto.HmeCosGetChipProcessingResponseDTO
     * @Description 构造返回参数不良位置描述列表
     * @author wenzhang.yu
     * @date 2020/9/28 9:51
     */
    private HmeCosGetChipProcessingResponseDTO getCosGetChipProcessingResponse(HmeCosGetChipProcessingResponseDTO baseResponseDTO) {
        //拷贝属性
        HmeCosGetChipProcessingResponseDTO responseDTO = new HmeCosGetChipProcessingResponseDTO();
        BeanUtils.copyProperties(baseResponseDTO, responseDTO);

        //构造不良位置-描述列表
        List<HmeMaterialLotLoadVO2> materialLotLoadList = (List<HmeMaterialLotLoadVO2>) responseDTO.getMaterialLotLoadList();
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            List<HmeMaterialLotNcRecordVO2> materialLotNcList = new ArrayList<>();
            materialLotLoadList.forEach(load -> {
                //判断这个格子是否有不良芯片
                if (CollectionUtils.isNotEmpty(load.getDocList())) {
                    load.getDocList().forEach(ncLoad -> {
                        //遍历这个芯片的不良代码
                        ncLoad.getNcRecordList().forEach(ncRecord -> {
                            String position = String.valueOf((char) (64 + load.getLoadRow())) +
                                    '-' + load.getLoadColumn() + '-' + ncLoad.getLoadNum();
                            HmeMaterialLotNcRecordVO2 ncRecordVO2 = new HmeMaterialLotNcRecordVO2(ncLoad.getNcLoadId(),
                                    ncRecord.getNcRecordId(), position, ncRecord.getNcCode(), ncRecord.getNcDesc(), null, null);
                            materialLotNcList.add(ncRecordVO2);
                        });
                    });
                }
            });

            responseDTO.setMaterialLotNcList(materialLotNcList);
        }

        return responseDTO;
    }
}