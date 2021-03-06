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
 * @Description COS????????????
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
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return java.lang.Object
     * @Description ?????????????????????????????????
     * @author yuchao.wang
     * @date 2020/8/18 10:35
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosGetChipScanBarcodeResponseDTO scanBarcode(Long tenantId, HmeCosGetChipScanBarcodeDTO dto) {
        //????????????
        if (StringUtils.isEmpty(dto.getBarcode())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getWkcShiftId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }

        //????????????????????????????????????ID
        MtMaterialLotVO3 param = new MtMaterialLotVO3();
        param.setMaterialLotCode(dto.getBarcode());
        List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, param);
        if (CollectionUtils.isEmpty(materialLotIds) || StringUtils.isEmpty(materialLotIds.get(0))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }
        String materialLotId = materialLotIds.get(0);

        //??????API???????????????????????????
        HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, materialLotId, true, dto.getBarcode());
        Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();

        //????????????/????????????
        if (YES.equals(hmeCosMaterialLotVO.getFreezeFlag()) || YES.equals(hmeCosMaterialLotVO.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", hmeCosMaterialLotVO.getMaterialLotCode()));
        }

        //???????????????????????????
        if (StringUtils.isBlank(materialLotAttrMap.get("COS_RECORD"))) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????[COS_RECORD]"));
        }

        //??????????????????????????????????????????
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
                    "HME_CHIP_TRANSFER_013", "HME", "????????????????????????????????????"));
        }
        if (StringUtils.isBlank(cosOperationRecord.getWorkOrderId()) || StringUtils.isBlank(cosOperationRecord.getWafer())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "?????????????????????Wafer"));
        }
        // 20211126 add by sanfeng.zhang for peng.zhao ?????????????????????????????????
        String objectRecordCode = this.spliceObjectRecordCode(tenantId, cosOperationRecord.getWorkOrderId(), dto.getOperationId(), dto.getWorkcellId(), materialLotAttrMap.get("WAFER_NUM"), dto.getEquipmentId());
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("????????????");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.RECORD);
        hmeObjectRecordLockDTO.setObjectRecordId("");
        hmeObjectRecordLockDTO.setObjectRecordCode(objectRecordCode);
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //??????
        hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, Collections.singletonList(hmeObjectRecordLock));

        HmeCosGetChipScanBarcodeResponseDTO responseDTO = null;
        try {
            //??????WKC_ID+??????_ID+??????_ID??????????????????????????????????????????
            HmeCosOperationRecord lastCosOperationRecord = hmeCosOperationRecordRepository.queryLastRecord(tenantId, dto.getWorkcellId(), dto.getOperationId(), dto.getEquipmentId(), "N");
            if (!Objects.isNull(lastCosOperationRecord) && StringUtils.isNotEmpty(lastCosOperationRecord.getOperationRecordId())
                    && !Objects.isNull(lastCosOperationRecord.getSurplusCosNum()) && lastCosOperationRecord.getSurplusCosNum() > 0) {
                //???????????????????????? ??????/Wafer ????????????
                if (!cosOperationRecord.getWorkOrderId().equals(lastCosOperationRecord.getWorkOrderId())
                        || !cosOperationRecord.getWafer().equals(lastCosOperationRecord.getWafer())) {
                    throw new MtException("HME_CHIP_TRANSFER_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_012", "HME", lastCosOperationRecord.getWorkOrderId(), lastCosOperationRecord.getWafer()));
                }
                // ?????????????????? By ?????? 20210916 ???????????????
//            //????????????????????????
//            if (!StringUtils.trimToEmpty(lastCosOperationRecord.getAttribute1())
//                    .equals(StringUtils.trimToEmpty(hmeCosMaterialLotVO.getLot()))) {
//                throw new MtException("HME_COS_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_COS_005", "HME"));
//            }
            }

            //2020-12-01 add by chaonan.hu for zhenyong.ban ??????????????????????????????
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
            //2021-11-08 10:46 add by chaonan.hu for yiming.zhang ?????????????????????????????????????????????????????????????????????
            List<String> chipLabCodeList = hmeCosGetChipPlatformMapper.chipLabCodeQuery(tenantId, materialLotId);
            if(CollectionUtils.isEmpty(chipLabCodeList)){
                //??????????????????${1}??????????????????,?????????!
                throw new MtException("HME_LAB_CODE_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LAB_CODE_009", "HME", hmeCosMaterialLotVO.getMaterialLotCode()));
            }
            if(StringUtils.isBlank(sourceMaterialLotLabCode)){
                //???????????????????????????????????????????????????????????????????????????????????????
                long chipLabCodeNotNullCount = chipLabCodeList.stream().filter(item -> StringUtils.isNotBlank(item)).count();
                if(chipLabCodeNotNullCount != 0){
                    //???????????????????????????????????????????????????????????????,?????????!
                    throw new MtException("HME_LAB_CODE_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LAB_CODE_008", "HME"));
                }
            }else {
                //????????????????????????????????????????????????????????????????????????????????????????????????????????????
                String finalSourceMaterialLotLabCode = sourceMaterialLotLabCode;
                long chipLabCodeNotEqualsCount = chipLabCodeList.stream().filter(item -> !finalSourceMaterialLotLabCode.equals(item)).count();
                if(chipLabCodeNotEqualsCount != 0){
                    //???????????????????????????????????????????????????????????????,?????????!
                    throw new MtException("HME_LAB_CODE_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LAB_CODE_008", "HME"));
                }
            }

            //2021-10-27 17:01 add by chaonan.hu for yiming.zhang ???????????????????????????????????????????????????
            if(Objects.nonNull(dto.getMaterialLotInfo()) && StringUtils.isNotBlank(dto.getMaterialLotInfo().getMaterialLotId())){
                if(!dto.getMaterialLotInfo().getLabCode().equals(sourceMaterialLotLabCode) || !dto.getMaterialLotInfo().getLabRemark().equals(sourceMaterialLotLabRemark)){
                    throw new MtException("HME_LAB_CODE_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LAB_CODE_003", "HME"));
                }
            }

            //???????????????????????????
            responseDTO = hmeCosCommonService.getBaseScanBarcodeResponseDTO(tenantId, hmeCosMaterialLotVO, cosOperationRecord);
            responseDTO.setLabCode(sourceMaterialLotLabCode);
            responseDTO.setLabRemark(sourceMaterialLotLabRemark);

            //??????????????????
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

            //???????????????
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, responseDTO.getWorkOrderId());
            if (Objects.nonNull(mtWorkOrder)) {
                responseDTO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
            }

            //??????/??????????????????????????????
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
                //2020-11-03 edit by chaonan.hu for zhenyong.ban ???site_in_num????????????
                hmeWoJobSn.setSiteInNum(hmeWoJobSn.getSiteInNum() + responseDTO.getPrimaryUomQty());
                hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
            }

            //??????/????????????????????????????????????
            HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
            hmeCosOperationRecord.setTenantId(tenantId);
            hmeCosOperationRecord.setWorkOrderId(responseDTO.getWorkOrderId());
            hmeCosOperationRecord.setOperationId(dto.getOperationId());
            hmeCosOperationRecord.setWorkcellId(dto.getWorkcellId());
            hmeCosOperationRecord.setWafer(responseDTO.getWafer());
            hmeCosOperationRecord.setEquipmentId(dto.getEquipmentId());
//        hmeCosOperationRecord.setAttribute1(hmeCosMaterialLotVO.getLot());  //??????????????????????????????????????????????????????????????? By ?????? 2021-10-12
            hmeCosOperationRecord = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecord);
            if (Objects.isNull(hmeCosOperationRecord) || StringUtils.isEmpty(hmeCosOperationRecord.getOperationRecordId())) {
                // ??????????????? ??????????????????????????? ??????????????????
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

                // ??????????????????
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

                // ??????????????????
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

            //??????EOJobSn
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
            // ????????????????????????????????????hme_eo_job_sn??????attribute3
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
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return void
     * @Description ????????????
     * @author yuchao.wang
     * @date 2020/8/18 19:50
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fetchInConfirm(Long tenantId, HmeCosGetChipSiteInConfirmDTO dto) {
        //????????????
        if (CollectionUtils.isEmpty(dto.getMaterialLotList())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "???????????????"));
        }
        for (HmeCosGetChipMaterialLotConfirmDTO materialLot : dto.getMaterialLotList()) {
            if (StringUtils.isEmpty(materialLot.getMaterialLotId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "?????????ID"));
            }
            if (Objects.isNull(materialLot.getPrimaryUomQty())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
            }
            if (StringUtils.isEmpty(materialLot.getEoJobSnId())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "eoSN????????????ID"));
            }
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        //????????????ID
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
            //2021-04-22 15:29 edit by chaonan.hu for kang.wang ???????????????????????????
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(materialLotDto.getMaterialLotId());
            mtMaterialLotVO20.setTrxPrimaryUomQty(-1 * materialLotDto.getPrimaryUomQty().doubleValue());
            mtMaterialLotVO20.setInLocatorTime(now);
            mtMaterialLotVO20.setEnableFlag("N");
            mtMaterialLotVO20List.add(mtMaterialLotVO20);
//            //??????API??????????????????
//            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
//                setEventId(eventId);
//                setMaterialLotId(materialLotDto.getMaterialLotId());
//                setTrxPrimaryUomQty(-1 * materialLotDto.getPrimaryUomQty().doubleValue());
//                setInLocatorTime(new Date());
//                setEnableFlag("N");
//            }}, "N");
            //2021-04-22 15:29 edit by chaonan.hu for kang.wang ???????????????????????????????????????
            MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
            mtCommonExtendVO7.setKeyId(materialLotDto.getMaterialLotId());
            List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
            MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
            mtCommonExtendVO4.setAttrName("CURRENT_ROUTER_STEP");
            mtCommonExtendVO4.setAttrValue(null);
            mtCommonExtendVO4List.add(mtCommonExtendVO4);
            mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
            mtCommonExtendVO7List.add(mtCommonExtendVO7);
            //2020-12-18 add by chaonan.hu for zhenyong.ban ???????????????????????????CURRENT_ROUTER_STEP
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

            //add by wenzhang.yu for zhenyong.ban   2020.11.23 ?????????????????????
            //add by wenzhnag.yu for zhenyong.ban   2020.10.04 ???????????????
            //MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            //mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
            //mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
            //mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
            //mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
            //mtInvOnhandQuantityVO9.setChangeQuantity(mtMaterialLot.getPrimaryUomQty());
            //mtInvOnhandQuantityVO9.setEventId(eventId);
            //mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

            //EoJobSn??????
            HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO = new HmeCosEoJobSnSiteOutVO();
            hmeCosEoJobSnSiteOutVO.setEoJobSnId(materialLotDto.getEoJobSnId());
            hmeCosEoJobSnSiteOutVO.setEquipmentList(dto.getEquipmentList());
            hmeCosEoJobSnSiteOutVO.setWorkcellId(dto.getWorkcellId());
            hmeCosCommonService.eoJobSnSiteOut(tenantId, hmeCosEoJobSnSiteOutVO);

            //??????????????????????????????????????? 2021-04-21 15:00 edit by chaonan.hu ????????????????????????????????????
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
            //2021-11-09 add by chaonan.hu for yiming.zhang ?????????????????????????????????
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

        //?????????????????????
        if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, HmeConstants.ConstantValue.NO);
        }
        //?????????????????????????????????
        if(CollectionUtils.isNotEmpty(mtCommonExtendVO7List)){
            mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId, mtCommonExtendVO7List);
        }
        //??????????????????????????????
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
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return void
     * @Description ????????????
     * @author yuchao.wang
     * @date 2020/8/18 20:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosGetChipSiteOutConfirmResponseDTO> fetchOutCreated(Long tenantId, HmeCosGetChipSiteOutConfirmDTO dto) {
        // 20210913 add by sanfeng.zhang for peng.zhao ?????????
        //??????
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("????????????");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.RECORD);
        hmeObjectRecordLockDTO.setObjectRecordId("");
        hmeObjectRecordLockDTO.setObjectRecordCode(dto.getCosRecord());
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //??????
        hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, Collections.singletonList(hmeObjectRecordLock));
        List<HmeCosGetChipSiteOutConfirmResponseDTO> responseDTOList = new ArrayList<>();
        try {
            //????????????
            checkParamsForSiteOutCreated(tenantId, dto);

            //??????????????????ID
            String sourceMaterialLotId = hmeEoJobSnMapper.querySourceMaterialLotIdForSiteOut(tenantId, dto.getWorkOrderId(), dto.getWorkcellId(), dto.getOperationId());

            //??????????????????????????????????????????
            HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
            hmeCosOperationRecord.setTenantId(tenantId);
            hmeCosOperationRecord.setOperationRecordId(dto.getCosRecord());
            hmeCosOperationRecord = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecord);
            if (Objects.isNull(hmeCosOperationRecord) || StringUtils.isEmpty(hmeCosOperationRecord.getOperationRecordId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "????????????????????????????????????"));
            }
            //??????API???????????????????????????
            HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, sourceMaterialLotId, false);
            Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();

            //????????????????????????
            HmeContainerCapacity hmeContainerCapacity = hmeContainerCapacityMapper.queryContainerCapacityForGetChip(tenantId,
                    dto.getContainerType(), hmeCosOperationRecord.getCosType(), dto.getOperationId());
            if (Objects.isNull(hmeContainerCapacity) || StringUtils.isEmpty(hmeContainerCapacity.getContainerCapacityId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "????????????"));
            }
            if (Objects.isNull(hmeContainerCapacity.getColumnNum()) || Objects.isNull(hmeContainerCapacity.getLineNum())
                    || Objects.isNull(hmeContainerCapacity.getCapacity())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "???????????????" + hmeContainerCapacity.getContainerCapacityId() + "????????????????????????"));
            }

            //???????????????????????????????????????
            if (!hmeContainerCapacity.getCapacity().equals(1L)) {
                throw new MtException("HME_COS_PATCH_PDA_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0008", "HME"));
            }

            //??????????????????????????????
            long containerShipNum = hmeContainerCapacity.getColumnNum() * hmeContainerCapacity.getLineNum() * hmeContainerCapacity.getCapacity();

            //???????????????????????????????????????/????????????????????????????????????????????? ???????????????????????????????????????????????????????????????????????????
            long lineCount = hmeContainerCapacity.getLineNum();
            long columnCount = hmeContainerCapacity.getColumnNum();
            long lastLineCount = hmeContainerCapacity.getColumnNum();
            if (dto.getActualChipNum() > containerShipNum) {
                throw new MtException("Exception", "???????????????????????????????????????????????????");
            } else if (dto.getActualChipNum() < containerShipNum) {
                lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getColumnNum();
                lineCount = dto.getActualChipNum() / hmeContainerCapacity.getColumnNum() + (lastLineCount == 0 ? 0 : 1);
            }

            //???????????????????????? ???????????????
            String direction = StringUtils.trimToEmpty(hmeContainerCapacity.getAttribute1());

            //??????/?????? ??????????????? ??????????????? ???????????????
            long outerStart = 1;
            long outerEnd = lineCount;
            int outerStep = 1;
            long innerStart = 1;
            long innerEnd = hmeContainerCapacity.getColumnNum();
            int innerStep = 1;

            //????????????????????????????????????
            switch (direction) {
                case "B":
                    //????????????,????????????
                    //?????????????????????????????????????????????????????????
                    lineCount = hmeContainerCapacity.getColumnNum();
                    columnCount = hmeContainerCapacity.getLineNum();
                    lastLineCount = hmeContainerCapacity.getLineNum();
                    if (dto.getActualChipNum() < containerShipNum) {
                        lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getLineNum();
                        lineCount = dto.getActualChipNum() / hmeContainerCapacity.getLineNum() + (lastLineCount == 0 ? 0 : 1);
                    }

                    //??????????????????
                    outerEnd = lineCount;
                    innerEnd = hmeContainerCapacity.getLineNum();
                    break;
                case "C":
                    //????????????,????????????
                    //?????????????????????????????????????????????????????????
                    lineCount = hmeContainerCapacity.getColumnNum();
                    columnCount = hmeContainerCapacity.getLineNum();
                    lastLineCount = hmeContainerCapacity.getLineNum();
                    if (dto.getActualChipNum() < containerShipNum) {
                        lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getLineNum();
                        lineCount = dto.getActualChipNum() / hmeContainerCapacity.getLineNum() + (lastLineCount == 0 ? 0 : 1);
                    }

                    //??????????????????
                    outerStart = hmeContainerCapacity.getColumnNum();
                    outerEnd = hmeContainerCapacity.getColumnNum() - lineCount + 1;
                    innerEnd = hmeContainerCapacity.getLineNum();
                    outerStep = -1;
                    break;
                case "D":
                    //????????????,????????????
                    //?????????????????????????????????????????????????????????
                    lineCount = hmeContainerCapacity.getColumnNum();
                    columnCount = hmeContainerCapacity.getLineNum();
                    lastLineCount = hmeContainerCapacity.getLineNum();
                    if (dto.getActualChipNum() < containerShipNum) {
                        lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getLineNum();
                        lineCount = dto.getActualChipNum() / hmeContainerCapacity.getLineNum() + (lastLineCount == 0 ? 0 : 1);
                    }

                    //??????????????????
                    outerStart = hmeContainerCapacity.getColumnNum();
                    outerEnd = hmeContainerCapacity.getColumnNum() - lineCount + 1;
                    innerStart = hmeContainerCapacity.getLineNum();
                    innerEnd = 1;
                    outerStep = -1;
                    innerStep = -1;
                    break;
                case "E":
                    //????????????,????????????
                    //???????????????????????????????????????
                    lineCount = hmeContainerCapacity.getColumnNum();
                    lastLineCount = hmeContainerCapacity.getLineNum();
                    if (dto.getActualChipNum() < containerShipNum) {
                        lastLineCount = dto.getActualChipNum() % hmeContainerCapacity.getLineNum();
                        lineCount = dto.getActualChipNum() / hmeContainerCapacity.getLineNum() + (lastLineCount == 0 ? 0 : 1);
                    }

                    //??????????????????
                    outerEnd = lineCount;
                    innerStart = hmeContainerCapacity.getLineNum();
                    innerEnd = 1;
                    innerStep = -1;
                    break;
                case "A":
                    //????????????,????????????
                default:
                    //????????????,????????????  A??????????????????????????????,????????????????????? ??????????????????
                    break;
            }

            //2020-12-18 add by chaonan.hu for zhenyong.ban ?????????????????????????????????????????????????????????
            List<HmeCosGetChipPlatformVO> hmeCosGetChipPlatformVOS = hmeCosGetChipPlatformMapper.routerStepAndOperationQuery(tenantId, hmeCosOperationRecord.getWorkOrderId());
            if (CollectionUtils.isEmpty(hmeCosGetChipPlatformVOS)) {
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_028", "HME"));
            }
            //??????????????????????????????????????????????????????????????????????????????
            List<HmeCosGetChipPlatformVO> afterFilterList = hmeCosGetChipPlatformVOS.stream().filter(item -> dto.getOperationId().equals(item.getOperationId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(afterFilterList)) {
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_028", "HME"));
            }
            String currentRouterStep = afterFilterList.get(0).getRouterStepId();

            //?????????????????????
            long notPrintQty = hmeEoJobSnRepository.queryNotPrintQtyBySourceJobId(tenantId, dto.getCosRecord());

            //?????????????????????
            if (Objects.isNull(hmeCosOperationRecord.getSurplusCosNum()) || hmeCosOperationRecord.getSurplusCosNum() < (dto.getActualChipNum() * dto.getMaterialLotCount() + notPrintQty)) {
                throw new MtException("Exception", "???????????????????????????????????????????????????");
            }

            //????????????ID
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("COS_FETCH_OUT");
            }});

            //??????????????????
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

            //???????????????????????????????????????
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

            //??????????????????Code
            HmeCosPatchPdaDTO4 cosPatchPdaDTO4 = new HmeCosPatchPdaDTO4();
            cosPatchPdaDTO4.setSiteId(dto.getSiteId());
            cosPatchPdaDTO4.setLot(dto.getLot());
            cosPatchPdaDTO4.setProdLineId(dto.getProdLineId());
            MtNumrangeVO8 mtNumrangeVO8 = hmeCosPatchPdaRepository.createIncomingValueList(tenantId, cosPatchPdaDTO4, dto.getMaterialLotCount().longValue(), dto.getCosType());
            if (Objects.isNull(mtNumrangeVO8) || CollectionUtils.isEmpty(mtNumrangeVO8.getNumberList())
                    || mtNumrangeVO8.getNumberList().size() != dto.getMaterialLotCount()) {
                throw new MtException("Exception", "????????????CODE??????");
            }
            List<String> numberList = mtNumrangeVO8.getNumberList();

            //??????????????????????????? ????????????
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
            String lot = hmeCosOperationRecord.getAttribute1();
            int loadIndex = 0;
            int ncLoadIndex = 0;
            int cosNcRecordIndex = 0;
            // ????????????
            String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
            // ????????????
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

                //??????????????????
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

                //add by wenzhnag.yu for zhenyong.ban   2020.12.23 ?????????????????????
                //add by wenzhnag.yu for zhenyong.ban   2020.10.04 ???????????????
                //MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLotVO13.getMaterialLotId());
                //MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                //mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
                //mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
                //mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
                //mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                //mtInvOnhandQuantityVO9.setChangeQuantity(mtMaterialLot.getPrimaryUomQty());
                //mtInvOnhandQuantityVO9.setEventId(eventId);
                //mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);


                //??????API???????????????????????????
                MtMaterialLot outMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, sourceMaterialLotId);
                if (Objects.isNull(outMaterialLot) || StringUtils.isEmpty(outMaterialLot.getMaterialLotId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "?????????????????????"));
                }
                responseDTO.setMaterialLotCode(outMaterialLot.getMaterialLotCode());
                responseDTO.setContainerShipNum(dto.getActualChipNum());

                //??????SN???????????????????????????/??????
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

                //??????EOJobSn
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
//            // ????????????????????????????????????hme_eo_job_sn??????attribute3
//            String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, hmeEoJobSn.getMaterialLotId());
                hmeEoJobSn.setAttribute3(hmeCosOperationRecord.getCosType());
                hmeEoJobSn.setAttribute5(hmeCosOperationRecord.getWafer());
                insertEoJobSnList.add(hmeEoJobSn);
                responseDTO.setEoJobSnId(hmeEoJobSn.getJobId());

                //??????????????????????????????
                long row = 0;
                long column = 0;

                //??????????????????????????????????????????????????????????????? ??????????????????????????????????????????????????????????????????
                List<String> materialLotLoadList = new ArrayList<>();
                for (long i = outerStart; verifyLoopEnd(direction, i, outerEnd, true); i = i + outerStep) {
                    for (long j = innerStart; verifyLoopEnd(direction, j, innerEnd, false); j = j + innerStep) {
                        //????????????????????????????????????????????????????????????????????????
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
                            //????????????????????????????????????????????????????????????LoadNum??????1
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

                            // 2021-01-08 add by sanfeng.zhang ??????????????????
                            // ??????????????????????????????
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

                        //???????????????????????????????????????????????????????????????????????????????????????
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
            //????????????
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
     * @param direction ????????????
     * @param index     ????????????index
     * @param loopEnd   ???????????????
     * @param outerFlag ?????????????????? true:??? false:???
     * @return boolean
     * @Description ?????? ???????????? ??? ???/???????????? ????????????for????????????
     * @author yuchao.wang
     * @date 2020/9/28 9:37
     */
    private boolean verifyLoopEnd(String direction, long index, long loopEnd, boolean outerFlag) {
        if (outerFlag) {
            //C D???????????????????????????????????????????????????????????????
            if ("C".equals(direction) || "D".equals(direction)) {
                return index >= loopEnd;
            } else {
                return index <= loopEnd;
            }
        } else {
            //D E???????????????????????????????????????????????????????????????
            if ("D".equals(direction) || "E".equals(direction)) {
                return index >= loopEnd;
            } else {
                return index <= loopEnd;
            }
        }
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosGetChipSiteOutConfirmResponseDTO>
     * @Description ????????????
     * @author yuchao.wang
     * @date 2020/8/19 16:01
     */
    @Override
    public HmeCosGetChipProcessingResponseDTO queryProcessing(Long tenantId, HmeCosGetChipSiteOutQueryDTO dto) {
        //????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }

        //??????WKC_ID+??????_ID+??????_ID??????????????????????????????????????????
        HmeCosOperationRecord lastCosOperationRecord = hmeCosOperationRecordRepository.queryLastRecord(tenantId, dto.getWorkcellId(), dto.getOperationId(), dto.getEquipmentId(), "Y");
        if (Objects.isNull(lastCosOperationRecord) || StringUtils.isEmpty(lastCosOperationRecord.getOperationRecordId())) {
            return new HmeCosGetChipProcessingResponseDTO();
        }

        HmeCosGetChipProcessingResponseDTO responseDTO = new HmeCosGetChipProcessingResponseDTO();

        //???????????????????????????????????? ??????????????????
        List<HmeEoJobSn> hmeEoJobSnInList = hmeEoJobSnMapper.queryEoJobSnBySourceJobId(tenantId, lastCosOperationRecord.getOperationRecordId(), "COS_FETCH_IN");
        if (CollectionUtils.isNotEmpty(hmeEoJobSnInList) && !Objects.isNull(hmeEoJobSnInList.get(0))) {
            //??????API???????????????????????????
            HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, hmeEoJobSnInList.get(0).getMaterialLotId(), false);

            //???????????????????????????
            HmeCosGetChipScanBarcodeResponseDTO baseResponseDTO = hmeCosCommonService.getBaseScanBarcodeResponseDTO(tenantId, hmeCosMaterialLotVO, lastCosOperationRecord);
            BeanUtils.copyProperties(baseResponseDTO, responseDTO);

            //??????????????????
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
            //??????????????????
            MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, lastCosOperationRecord.getMaterialId());
            if (Objects.isNull(mtMaterialVO) || StringUtils.isEmpty(mtMaterialVO.getMaterialId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "????????????"));
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

        //???????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, responseDTO.getWorkOrderId());
        if (Objects.nonNull(mtWorkOrder)) {
            responseDTO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        }

        //?????????????????????????????????
        List<HmeMaterialLotVO> hmeMaterialLotVOList = hmeEoJobSnMapper.queryMaterialLotByJobId(tenantId, responseDTO.getCosRecord());
        if (CollectionUtils.isNotEmpty(hmeMaterialLotVOList)) {
            //??????????????????????????????
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
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return java.lang.String
     * @Description ????????????
     * @author yuchao.wang
     * @date 2020/8/19 19:16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> fetchOut(Long tenantId, HmeCosGetChipSiteOutPrintDTO dto, HttpServletResponse response) {
        //????????????
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getOperationRecordId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????????????????????????????ID"));
        }
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }
        if (CollectionUtils.isEmpty(dto.getSiteOutList())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }

        // 20211202 add by sanfeng.zhang for yiming.zhang ??????????????????
        List<HmeObjectRecordLock> recordLockList = new ArrayList<>();
        for (HmeMaterialLotVO hmeMaterialLotVO : dto.getSiteOutList()) {
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("??????????????????");
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
            //2021-07-12 11:29 add by chaonan.hu for zhenyong.ban ?????????,?????????????????????(???????????????-qty)??????0,??????????????????????????????0???????????????????????????
            HmeCosOperationRecord hmeCosOperationRecordDb = new HmeCosOperationRecord();
            hmeCosOperationRecordDb.setOperationRecordId(dto.getOperationRecordId());
            hmeCosOperationRecordDb = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecordDb);
            if (Objects.isNull(hmeCosOperationRecordDb)) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "????????????????????????????????????"));
            }
            if (Objects.isNull(hmeCosOperationRecordDb.getSurplusCosNum())) {
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "?????????????????????????????????????????????"));
            }
            BigDecimal qty = hmeCosPatchPdaMapper.getAddQty(tenantId, dto.getWorkcellId(), dto.getOperationId(), hmeCosOperationRecordDb.getWafer(), "COS_FETCH_OUT");
            BigDecimal addQty = BigDecimal.valueOf(hmeCosOperationRecordDb.getSurplusCosNum()).subtract(qty);
            if(addQty.compareTo(BigDecimal.ZERO) == -1){
                throw new MtException("HME_COS_PATCH_PDA_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0016", "HME"));
            }

            //????????????
            Long processedNum = 0L;
            Long unqualifiedNum = 0L;
            for (HmeMaterialLotVO materialLotVO : dto.getSiteOutList()) {
                if (StringUtils.isEmpty(materialLotVO.getMaterialLotId())) {
                    throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_MATERIAL_INSP_0020", "QMS", "?????????ID"));
                }
                if (Objects.isNull(materialLotVO.getPrimaryUomQty())) {
                    throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
                }
                if (StringUtils.isEmpty(materialLotVO.getEoJobSnId())) {
                    throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_MATERIAL_INSP_0020", "QMS", "eoSN????????????ID"));
                }

                materialLotIdList.add(materialLotVO.getMaterialLotId());

                //?????????????????????????????????
                if (hmeEoJobSnRepository.checkSiteOutById(tenantId, materialLotVO.getEoJobSnId())) {
                    continue;
                }

                //EoJobSn??????
                HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO = new HmeCosEoJobSnSiteOutVO();
                BeanUtils.copyProperties(dto, hmeCosEoJobSnSiteOutVO);
                hmeCosEoJobSnSiteOutVO.setEoJobSnId(materialLotVO.getEoJobSnId());
                hmeCosCommonService.eoJobSnSiteOut(tenantId, hmeCosEoJobSnSiteOutVO);

                //????????????
                processedNum += materialLotVO.getPrimaryUomQty();

                //???????????????????????????
                if (hmeMaterialLotLoadRepository.checkHasNcLoadFlag(tenantId, materialLotVO.getMaterialLotId())) {
                    unqualifiedNum += materialLotVO.getPrimaryUomQty();
                }
            }

            //??????????????????0 ?????????????????????????????????
            if (processedNum != 0 || unqualifiedNum != 0) {
                //??????????????????????????????
                HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
                hmeWoJobSn.setTenantId(tenantId);
                hmeWoJobSn.setWorkOrderId(dto.getWorkOrderId());
                hmeWoJobSn.setOperationId(dto.getOperationId());
                hmeWoJobSn = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
                if (Objects.isNull(hmeWoJobSn) || StringUtils.isEmpty(hmeWoJobSn.getWoJobSnId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "??????????????????????????????"));
                }

                hmeWoJobSn.setProcessedNum(Objects.isNull(hmeWoJobSn.getProcessedNum()) ? processedNum :
                        hmeWoJobSn.getProcessedNum() + processedNum);
                hmeWoJobSn.setUnqualifiedNum(Objects.isNull(hmeWoJobSn.getUnqualifiedNum()) ? unqualifiedNum :
                        hmeWoJobSn.getUnqualifiedNum() + unqualifiedNum);
                hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);

                //???????????????????????????????????????????????????
                HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
                hmeCosOperationRecord.setOperationRecordId(dto.getOperationRecordId());
                hmeCosOperationRecord = hmeCosOperationRecordRepository.selectOne(hmeCosOperationRecord);
                if (Objects.isNull(hmeCosOperationRecord) || StringUtils.isEmpty(hmeCosOperationRecord.getOperationRecordId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "????????????????????????????????????"));
                }
                if (Objects.isNull(hmeCosOperationRecord.getSurplusCosNum())) {
                    throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "QMS_MATERIAL_INSP_0020", "QMS", "?????????????????????????????????????????????"));
                }

                long surplusCosNum = hmeCosOperationRecord.getSurplusCosNum() - processedNum;

                //????????????????????????0??????????????????????????????????????????
                if (surplusCosNum <= 0
                        && !hmeEoJobSnRepository.checkNotSiteOutByWkcId(tenantId, dto.getWorkcellId(), "COS_FETCH_IN")) {
                    throw new MtException("HME_COS_PATCH_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_0007", "HME"));
                }

                hmeCosOperationRecord.setSurplusCosNum(surplusCosNum);
                hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

                // ??????????????????
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
     * @param tenantId          ??????ID
     * @param materialLotIdList ?????????ID
     * @return java.lang.String
     * @Description ??????PDF?????????, ????????????????????????
     * @author yuchao.wang
     * @date 2020/8/19 20:22
     */
    @Override
    public void printPdf(Long tenantId, List<String> materialLotIdList, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(materialLotIdList)) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }

        //???????????????
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("make-dir-failed", "???????????????????????????!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }

        //??????API???????????????????????????
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        if (CollectionUtils.isEmpty(mtMaterialLotList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "???????????????"));
        }
        String uuid = UUID.randomUUID().toString();

        //????????????????????????
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<File> imageFileList = new ArrayList<>();
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        for (int i = 0; i < mtMaterialLotList.size(); i++) {
            MtMaterialLot mtMaterialLot = mtMaterialLotList.get(i);
            //??????API???????????????????????????
            HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, mtMaterialLot.getMaterialLotId(), true, mtMaterialLot.getMaterialLotCode());
            Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();
            String workOrderNum = null;
            if (StringUtils.isNotEmpty(materialLotAttrMap.get("WORK_ORDER_ID"))) {
                MtWorkOrder mtWorkOrder = new MtWorkOrder();
                mtWorkOrder.setWorkOrderId(materialLotAttrMap.get("WORK_ORDER_ID"));
                mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtWorkOrder);
                workOrderNum = mtWorkOrder.getWorkOrderNum();
            }
            //???????????????
            String barcodePath = basePath + "/" + uuid + "_" + mtMaterialLot.getMaterialLotId() + ".png";
            File imageFile = new File(barcodePath);
            try {
                CommonBarcodeUtil.generateCode128ToFile(mtMaterialLot.getMaterialLotCode(), CommonBarcodeUtil.IMG_TYPE_PNG, imageFile, 10);
                log.info("<====????????????????????????{}", barcodePath);

                //??????????????????
                //CommonBarcodeUtil.repaintPictureToCenter(imageFile, 385, 66);
            } catch (Exception e) {
                log.error("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.generateImageFile Error", e);
                throw new MtException("Exception", e.getMessage());
            }
            int num = i % 2;
            //????????????
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

        //??????PDF
        String pdfPath = basePath + "/" + uuid + ".pdf";
        try {
            log.info("<==== ??????PDF????????????:{}:{}", pdfPath, dataList);
            CommonPdfTemplateUtil.multiplePage(basePath + "/cos_qp_print_template.pdf", pdfPath, dataList);
            log.info("<==== ??????PDF?????????{}", pdfPath);
        } catch (Exception e) {
            log.error("<==== HmeCosGetChipPlatformServiceImpl.getPrintPdfUrl.generatePDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        }

        //?????????????????????????????????
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //??????????????????
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //?????????????????????????????????
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

        //??????????????????
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
     * @param tenantId ??????ID
     * @param dto      ??????
     * @Description ????????????????????????????????????
     * @author yuchao.wang
     * @date 2020/9/8 10:29
     */
    @Override
    public HmeCosGetChipMaxLoadDTO queryMaxLoadNumber(Long tenantId, HmeCosGetChipMaxLoadDTO dto) {
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }
        if (StringUtils.isEmpty(dto.getCosType())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????????????????COS??????"));
        }
        if (StringUtils.isEmpty(dto.getContainerType())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }

        //????????????????????????
        HmeContainerCapacity hmeContainerCapacity = hmeContainerCapacityMapper.queryContainerCapacityForGetChip(tenantId,
                dto.getContainerType(), dto.getCosType(), dto.getOperationId());
        if (Objects.isNull(hmeContainerCapacity) || StringUtils.isEmpty(hmeContainerCapacity.getContainerCapacityId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }
        if (Objects.isNull(hmeContainerCapacity.getColumnNum()) || Objects.isNull(hmeContainerCapacity.getLineNum())
                || Objects.isNull(hmeContainerCapacity.getCapacity())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "???????????????" + hmeContainerCapacity.getContainerCapacityId() + "????????????????????????"));
        }

        //???????????????????????????????????????
        if (!hmeContainerCapacity.getCapacity().equals(1L)) {
            throw new MtException("HME_COS_PATCH_PDA_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0008", "HME"));
        }

        //?????????????????????????????????
        dto.setMaxLoadNumber(hmeContainerCapacity.getColumnNum() * hmeContainerCapacity.getLineNum() * hmeContainerCapacity.getCapacity());
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosGetChipDeleteDTO batchDelete(Long tenantId, HmeCosGetChipDeleteDTO dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (CollectionUtils.isEmpty(dto.getMaterialLotIdList())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "?????????"));
        }
        for (String materialLotId : dto.getMaterialLotIdList()) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
            //??????material_lot_id+operation_id+workcell_id+job_type???COS_FETCH_IN???????????????????????????????????????????????????
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
            //?????????????????????
            //??????WKC_ID+??????_ID+??????_ID??????????????????????????????????????????
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

            // ??????????????????
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(lastCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            //??????????????????
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
            //2021-11-09 16:18 edit by chaonan.hu for yiming.zhang ????????????????????????????????????????????????jobId
            List<HmeCosGetChipMaterialLotListResponseDTO> siteOutDateNonNullList = resultList.stream()
                    .filter(item -> Objects.nonNull(item.getSiteOutDate()))
                    .sorted(Comparator.comparing(HmeCosGetChipMaterialLotListResponseDTO::getSiteOutDate).reversed())
                    .collect(Collectors.toList());
            String labCode = "";
            String labRemark = "";
            if(CollectionUtils.isNotEmpty(siteOutDateNonNullList)){
                //??????jobId??????????????????????????????,???????????????????????????????????????????????????
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
                    result.setStatus("?????????");
                } else {
                    result.setStatusFlag("Y");
                    result.setStatus("?????????");
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
     * @Description ????????????????????????
     * @author yuchao.wang
     * @date 2020/8/19 14:28
     */
    private void checkParamsForSiteOutCreated(Long tenantId, HmeCosGetChipSiteOutConfirmDTO dto) {
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getWkcShiftId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (StringUtils.isEmpty(dto.getCosType())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????????????????COS??????"));
        }
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }
        if (StringUtils.isEmpty(dto.getContainerType())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }
        if (StringUtils.isEmpty(dto.getCosRecord())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????????????????????????????ID"));
        }
        if (Objects.isNull(dto.getMaterialLotCount())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????????????????"));
        }
        if (Objects.isNull(dto.getActualChipNum())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????????????????"));
        }
        if (dto.getMaterialLotCount() < 1) {
            throw new MtException("Exception", "?????????????????????????????????");
        }
        if (dto.getActualChipNum() < 1) {
            throw new MtException("Exception", "?????????????????????????????????");
        }
    }

    /**
     * @param dto
     * @param hmeContainerCapacity
     * @param materialLotAttrMap
     * @return java.util.List<io.tarzan.common.domain.vo.MtExtendVO5>
     * @Description ????????????????????????
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
     * @param baseResponseDTO ??????
     * @return com.ruike.hme.api.dto.HmeCosGetChipScanBarcodeResponseDTO
     * @Description ??????????????????????????????????????????
     * @author wenzhang.yu
     * @date 2020/9/28 9:51
     */
    private HmeCosGetChipScanBarcodeResponseDTO getCosGetChipScanBarcodeResponse(HmeCosGetChipScanBarcodeResponseDTO baseResponseDTO) {
        //????????????
        HmeCosGetChipScanBarcodeResponseDTO responseDTO = new HmeCosGetChipScanBarcodeResponseDTO();
        BeanUtils.copyProperties(baseResponseDTO, responseDTO);

        //??????????????????-????????????
        List<HmeMaterialLotLoadVO2> materialLotLoadList = (List<HmeMaterialLotLoadVO2>) responseDTO.getMaterialLotLoadList();
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            List<HmeMaterialLotNcRecordVO2> materialLotNcList = new ArrayList<>();
            materialLotLoadList.forEach(load -> {
                //???????????????????????????????????????
                if (CollectionUtils.isNotEmpty(load.getDocList())) {
                    load.getDocList().forEach(ncLoad -> {
                        //?????????????????????????????????
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
     * @param baseResponseDTO ??????
     * @return com.ruike.hme.api.dto.HmeCosGetChipProcessingResponseDTO
     * @Description ??????????????????????????????????????????
     * @author wenzhang.yu
     * @date 2020/9/28 9:51
     */
    private HmeCosGetChipProcessingResponseDTO getCosGetChipProcessingResponse(HmeCosGetChipProcessingResponseDTO baseResponseDTO) {
        //????????????
        HmeCosGetChipProcessingResponseDTO responseDTO = new HmeCosGetChipProcessingResponseDTO();
        BeanUtils.copyProperties(baseResponseDTO, responseDTO);

        //??????????????????-????????????
        List<HmeMaterialLotLoadVO2> materialLotLoadList = (List<HmeMaterialLotLoadVO2>) responseDTO.getMaterialLotLoadList();
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            List<HmeMaterialLotNcRecordVO2> materialLotNcList = new ArrayList<>();
            materialLotLoadList.forEach(load -> {
                //???????????????????????????????????????
                if (CollectionUtils.isNotEmpty(load.getDocList())) {
                    load.getDocList().forEach(ncLoad -> {
                        //?????????????????????????????????
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