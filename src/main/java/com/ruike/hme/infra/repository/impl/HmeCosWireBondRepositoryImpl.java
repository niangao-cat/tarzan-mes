package com.ruike.hme.infra.repository.impl;


import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeEoJobEquipmentService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmeCosWireBondVO;
import com.ruike.hme.domain.vo.HmeCosWireBondVO1;
import com.ruike.hme.domain.vo.HmeCosWireBondVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.actual.domain.vo.MtWoComponentActualVO12;
import tarzan.actual.domain.vo.MtWoComponentActualVO8;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.repository.MtRouterNextStepRepository;
import tarzan.method.infra.mapper.MtBomComponentMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;
import static com.ruike.wms.infra.constant.WmsConstant.BLANK;

@Component
public class HmeCosWireBondRepositoryImpl implements HmeCosWireBondRepository {

    private final HmeMaterialTransferRepository materialTransferRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final HmeEquipmentRepository hmeEquipmentRepository;
    private final HmeCosOperationRecordRepository cosOperationRecordRepository;
    private final MtWorkOrderRepository mtWorkOrderRepository;
    private final HmeTimeProcessPdaMapper hmeTimeProcessPdaMapper;
    private final HmeEoJobSnRepository hmeEoJobSnRepository;
    private final HmeWoJobSnRepository hmeWoJobSnRepository;
    private final HmeWoJobSnMapper hmeWoJobSnMapper;
    private final HmeCosOperationRecordMapper hmeCosOperationRecordMapper;
    private final MtMaterialRepository mtMaterialRepository;
    private final MtEventRepository mtEventRepository;
    private final MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final MtEventRequestRepository mtEventRequestRepository;
    private final MtWkcShiftRepository mtWkcShiftRepository;
    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;
    private final HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    private final MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    private final HmeCosWireBondMapper hmeCosWireBondMapper;
    private final HmeCosPatchPdaMapper hmeCosPatchPdaMapper;
    private final LovAdapter lovAdapter;
    private final HmeCosCommonService hmeCosCommonService;
    private final MtBomComponentMapper mtBomComponentMapper;
    private final HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    private final MtUomRepository mtUomRepository;
    private final MtModLocatorRepository mtModLocatorRepository;
    private final MtExtendSettingsRepository mtExtendSettingsRepository;
    private final HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;
    private final HmeEoJobEquipmentService hmeEoJobEquipmentService;
    private final MtRouterNextStepRepository mtRouterNextStepRepository;
    private final HmeMaterialLotLoadRepository materialLotLoadRepository;
    private final HmeEoJobEquipmentMapper hmeEoJobEquipmentMapper;
    private final HmeLoadJobRepository hmeLoadJobRepository;
    private final HmeLoadJobObjectRepository hmeLoadJobObjectRepository;
    private final HmeLoadJobMapper hmeLoadJobMapper;
    private final HmeLoadJobObjectMapper hmeLoadJobObjectMapper;
    private final HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;
    private final MtContainerTypeRepository mtContainerTypeRepository;
    private final MtCustomDbRepository customDbRepository;
    private final HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;

    public HmeCosWireBondRepositoryImpl(HmeTimeProcessPdaMapper hmeTimeProcessPdaMapper, HmeMaterialTransferRepository materialTransferRepository, MtErrorMessageRepository mtErrorMessageRepository, MtMaterialLotRepository mtMaterialLotRepository, HmeCosCommonService hmeCosCommonService, MtMaterialRepository mtMaterialRepository, HmeEquipmentRepository hmeEquipmentRepository, HmeCosOperationRecordRepository cosOperationRecordRepository, HmeEoJobEquipmentService hmeEoJobEquipmentService, HmeCosPatchPdaMapper hmeCosPatchPdaMapper, MtUomRepository mtUomRepository, MtModLocatorRepository mtModLocatorRepository, MtWorkOrderRepository mtWorkOrderRepository, MtWkcShiftRepository mtWkcShiftRepository, HmeCosWireBondMapper hmeCosWireBondMapper, HmeEoJobSnRepository hmeEoJobSnRepository, HmeWoJobSnRepository hmeWoJobSnRepository, MtBomComponentMapper mtBomComponentMapper, HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository, MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository, MtEventRequestRepository mtEventRequestRepository, LovAdapter lovAdapter, HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository, HmeWoJobSnMapper hmeWoJobSnMapper, MtExtendSettingsRepository mtExtendSettingsRepository, MtRouterNextStepRepository mtRouterNextStepRepository, MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository, HmeCosOperationRecordMapper hmeCosOperationRecordMapper, MtEventRepository mtEventRepository, WmsObjectTransactionRepository wmsObjectTransactionRepository, HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper, WmsTransactionTypeRepository wmsTransactionTypeRepository, HmeMaterialLotLoadRepository materialLotLoadRepository, HmeEoJobEquipmentMapper hmeEoJobEquipmentMapper, HmeLoadJobRepository hmeLoadJobRepository, HmeLoadJobObjectRepository hmeLoadJobObjectRepository, HmeLoadJobMapper hmeLoadJobMapper, HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository, MtContainerTypeRepository mtContainerTypeRepository, HmeLoadJobObjectMapper hmeLoadJobObjectMapper, MtCustomDbRepository customDbRepository, HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository) {
        this.hmeTimeProcessPdaMapper = hmeTimeProcessPdaMapper;
        this.materialTransferRepository = materialTransferRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.hmeCosCommonService = hmeCosCommonService;
        this.mtMaterialRepository = mtMaterialRepository;
        this.hmeEquipmentRepository = hmeEquipmentRepository;
        this.cosOperationRecordRepository = cosOperationRecordRepository;
        this.hmeEoJobEquipmentService = hmeEoJobEquipmentService;
        this.hmeCosPatchPdaMapper = hmeCosPatchPdaMapper;
        this.mtUomRepository = mtUomRepository;
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.mtWorkOrderRepository = mtWorkOrderRepository;
        this.mtWkcShiftRepository = mtWkcShiftRepository;
        this.hmeCosWireBondMapper = hmeCosWireBondMapper;
        this.hmeEoJobSnRepository = hmeEoJobSnRepository;
        this.hmeWoJobSnRepository = hmeWoJobSnRepository;
        this.mtBomComponentMapper = mtBomComponentMapper;
        this.hmeEoJobSnLotMaterialRepository = hmeEoJobSnLotMaterialRepository;
        this.mtInvOnhandQuantityRepository = mtInvOnhandQuantityRepository;
        this.mtEventRequestRepository = mtEventRequestRepository;
        this.lovAdapter = lovAdapter;
        this.hmeEoJobLotMaterialRepository = hmeEoJobLotMaterialRepository;
        this.hmeWoJobSnMapper = hmeWoJobSnMapper;
        this.mtExtendSettingsRepository = mtExtendSettingsRepository;
        this.mtRouterNextStepRepository = mtRouterNextStepRepository;
        this.mtWorkOrderComponentActualRepository = mtWorkOrderComponentActualRepository;
        this.hmeCosOperationRecordMapper = hmeCosOperationRecordMapper;
        this.mtEventRepository = mtEventRepository;
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.hmeEoJobSnLotMaterialMapper = hmeEoJobSnLotMaterialMapper;
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
        this.materialLotLoadRepository = materialLotLoadRepository;
        this.hmeEoJobEquipmentMapper = hmeEoJobEquipmentMapper;
        this.hmeLoadJobRepository = hmeLoadJobRepository;
        this.hmeLoadJobObjectRepository = hmeLoadJobObjectRepository;
        this.hmeLoadJobMapper = hmeLoadJobMapper;
        this.hmeMaterialLotLabCodeRepository = hmeMaterialLotLabCodeRepository;
        this.mtContainerTypeRepository = mtContainerTypeRepository;
        this.hmeLoadJobObjectMapper = hmeLoadJobObjectMapper;
        this.customDbRepository = customDbRepository;
        this.hmeCosOperationRecordHisRepository = hmeCosOperationRecordHisRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosWireBondVO1 siteOutDateNullQuery(Long tenantId, HmeCosWireBondDTO dto) {
        HmeCosWireBondVO1 result = new HmeCosWireBondVO1();
        List<HmeCosWireBondVO> hmeCosWireBondVOList = hmeCosWireBondMapper.siteOutDateNullQuery(tenantId, dto.getWorkcellId(), dto.getOperationId());
        result.setHmeCosWireBondVOList(hmeCosWireBondVOList);
        if (CollectionUtils.isNotEmpty(hmeCosWireBondVOList) && StringUtils.isNotEmpty(hmeCosWireBondVOList.get(0).getWorkOrderId())) {
            result.setWorkOrderId(hmeCosWireBondVOList.get(0).getWorkOrderId());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scanBarcode(Long tenantId, HmeCosWireBondDTO dto) {
        String operationId = dto.getOperationId();
        MtMaterialLot mtMaterialLot = materialTransferRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotCode());
        if (mtMaterialLot == null || !YES.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }
        if (!OK.equals(mtMaterialLot.getQualityStatus())) {
            throw new MtException("HME_TIME_PROCESS_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0005", "HME", mtMaterialLot.getQualityStatus()));
        }
        if (YES.equals(mtMaterialLot.getFreezeFlag()) || YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //??????????????????????????????
        String cosRecord;
        List<MtExtendAttrVO> mtExtendAttrVOS;
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("COS_RECORD");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isEmpty(mtExtendAttrVOS)) {
            throw new MtException("HME_TIME_PROCESS_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0009", "HME"));
        }
        cosRecord = mtExtendAttrVOS.get(0).getAttrValue();
        HmeCosOperationRecord cosOperationRecord = cosOperationRecordRepository.selectByPrimaryKey(cosRecord);
        String workOrderId = cosOperationRecord.getWorkOrderId();
        //Wafer??????
        String wafer;
        mtMaterialLotAttrVO2.setAttrName("WAFER_NUM");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isEmpty(mtExtendAttrVOS)) {
            throw new MtException("HME_COS_PATCH_PDA_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_PATCH_PDA_0001", "HME"));
        }
        wafer = mtExtendAttrVOS.get(0).getAttrValue();
        //????????????
        String containerTypeId = null;
        mtMaterialLotAttrVO2.setAttrName("CONTAINER_TYPE");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            /*MtContainerType mtContainerType = new MtContainerType();
            mtContainerType.setContainerTypeCode(mtExtendAttrVOS.get(0).getAttrValue());
            mtContainerType = mtContainerTypeRepository.selectOne(mtContainerType);*/

            containerTypeId = mtExtendAttrVOS.get(0).getAttrValue();
        }
        //COS??????
        String cosType = null;
        mtMaterialLotAttrVO2.setAttrName("COS_TYPE");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            cosType = mtExtendAttrVOS.get(0).getAttrValue();
        }
        //TYPE
        String type = null;
        mtMaterialLotAttrVO2.setAttrName("TYPE");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            type = mtExtendAttrVOS.get(0).getAttrValue();
        }
        //JOB_BATCH
        String jobBatch = null;
        mtMaterialLotAttrVO2.setAttrName("JOB_BATCH");/**/
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            jobBatch = mtExtendAttrVOS.get(0).getAttrValue();
        }
        //AVG_WAVE_LENGTH
        String avgWaveLength = null;
        mtMaterialLotAttrVO2.setAttrName("AVG_WAVE_LENGTH");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            avgWaveLength = mtExtendAttrVOS.get(0).getAttrValue();
        }
        //LOTNO
        String lotNo = null;
        mtMaterialLotAttrVO2.setAttrName("LOTNO");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            lotNo = mtExtendAttrVOS.get(0).getAttrValue();
        }
        //REMARK
        String remark = null;
        mtMaterialLotAttrVO2.setAttrName("REMARK");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            remark = mtExtendAttrVOS.get(0).getAttrValue();
        }

        //CURRENT_ROUTER_STEP
        String currentRouterStep = null;
        mtMaterialLotAttrVO2.setAttrName("CURRENT_ROUTER_STEP");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            currentRouterStep = mtExtendAttrVOS.get(0).getAttrValue();
        }

        //WORK_ORDER_ID
        String snWorkOrderId = null;
        mtMaterialLotAttrVO2.setAttrName("WORK_ORDER_ID");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            snWorkOrderId = mtExtendAttrVOS.get(0).getAttrValue();
        }

        // ????????????
        //????????????id
        List<String> routeStepIdList = hmeCosWireBondMapper.queryRouterStepIdByWorkOrderIdAndOperationId(tenantId, snWorkOrderId, dto.getOperationId());
        // ??????????????????ROUTER_STEP_ID
        if (CollectionUtils.isEmpty(routeStepIdList)) {
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", HME));
        }
        String routerStepId = routeStepIdList.get(0);
        List<MtRouterNextStep> preRouterStepList = mtRouterNextStepRepository.select(new MtRouterNextStep() {{
            setNextStepId(routerStepId);
        }});
        if (CollectionUtils.isEmpty(preRouterStepList)) {
            throw new MtException("HME_COS_030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_COS_030", HME));
        } else if (preRouterStepList.size() > 1) {
            throw new MtException("HME_COS_032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_COS_032", HME));
        }
        String preRouterStep = preRouterStepList.get(0).getRouterStepId();
        if (!(StringCommonUtils.equalsIgnoreBlank(currentRouterStep, preRouterStep) ||
                StringCommonUtils.equalsIgnoreBlank(routerStepId, currentRouterStep))) {
            throw new MtException("HME_COS_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_COS_033", HME));
        }

        //????????????????????????ID???Wafer??????????????????????????????ID???Wafer?????????
        String equipmentId = null;
        if (StringUtils.isNotEmpty(dto.getEquipmentCode())) {
            HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
                setTenantId(tenantId);
                setAssetEncoding(dto.getEquipmentCode());
            }});
            equipmentId = hmeEquipment.getEquipmentId();
        }

        //???????????????job_sn??????????????????????????????????????????
        hmeCosCommonService.verifyMaterialLotSiteOut(tenantId, mtMaterialLot.getMaterialLotId());

        //????????????????????????????????????
        HmeCosOperationRecord hmeCosOperationRecord = hmeCosWireBondMapper.recordQuery2(tenantId, dto.getWorkcellId(), operationId, workOrderId, wafer, equipmentId);
        if (hmeCosOperationRecord != null) {
            //2020-11-19 edit by chaonan.hu for zhenyong,ban ????????????
//            if (!hmeCosOperationRecord.getWorkOrderId().equals(workOrderId) || !hmeCosOperationRecord.getWafer().equals(wafer)) {
//                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeCosOperationRecord.getWorkOrderId());
//                throw new MtException("HME_COS_PATCH_PDA_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_COS_PATCH_PDA_0002", "HME", mtWorkOrder.getWorkOrderNum(), hmeCosOperationRecord.getWafer()));
//            }
            hmeCosOperationRecord.setCosNum(hmeCosOperationRecord.getCosNum() +
                    mtMaterialLot.getPrimaryUomQty().longValue());
            hmeCosOperationRecord.setSurplusCosNum(hmeCosOperationRecord.getSurplusCosNum() +
                    mtMaterialLot.getPrimaryUomQty().longValue());
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

            // ??????????????????
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHis.setTenantId(tenantId);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        } else {
            hmeCosOperationRecord = new HmeCosOperationRecord();
            hmeCosOperationRecord.setTenantId(tenantId);
            hmeCosOperationRecord.setSiteId(dto.getSiteId());
            hmeCosOperationRecord.setWorkOrderId(workOrderId);
            hmeCosOperationRecord.setWafer(wafer);
            hmeCosOperationRecord.setCosNum(mtMaterialLot.getPrimaryUomQty().longValue());
            hmeCosOperationRecord.setOperationId(operationId);
            hmeCosOperationRecord.setWorkcellId(dto.getWorkcellId());
            hmeCosOperationRecord.setEquipmentId(equipmentId);
            hmeCosOperationRecord.setSurplusCosNum(mtMaterialLot.getPrimaryUomQty().longValue());
            hmeCosOperationRecord.setMaterialId(mtMaterialLot.getMaterialId());

            hmeCosOperationRecord.setCosType(cosType);
            hmeCosOperationRecord.setType(type);
            if (StringUtils.isNotEmpty(avgWaveLength)) {
                hmeCosOperationRecord.setAverageWavelength(new BigDecimal(avgWaveLength));
            }
            hmeCosOperationRecord.setLotNo(lotNo);
            hmeCosOperationRecord.setJobBatch(jobBatch);
            hmeCosOperationRecord.setRemark(remark);
            if(StringUtils.isNotBlank(containerTypeId)){
                MtContainerType mtContainerType = new MtContainerType();
                mtContainerType.setTenantId(tenantId);
                mtContainerType.setContainerTypeCode(containerTypeId);
                mtContainerType = mtContainerTypeRepository.selectOne(mtContainerType);
                if(Objects.nonNull(mtContainerType)){
                    hmeCosOperationRecord.setContainerTypeId(mtContainerType.getContainerTypeId());
                }
            }
            cosOperationRecordRepository.insertSelective(hmeCosOperationRecord);

            // ??????????????????
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
        }

        //??????material_lot_id+??????ID+????????????????????????COS_WIRE_BOND?????????????????????????????????????????????
        List<String> jobIdList = hmeCosWireBondMapper.siteOutDateNullQuery1(tenantId, mtMaterialLot.getMaterialLotId(), operationId);
        if (CollectionUtils.isEmpty(jobIdList)) {
            //??????????????????????????????
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(new Date());
            hmeEoJobSn.setShiftId(dto.getWkcShiftId());
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? 1L : curUser.getUserId();
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(workOrderId);
            hmeEoJobSn.setOperationId(operationId);
            hmeEoJobSn.setSnMaterialId(mtMaterialLot.getMaterialId());
            hmeEoJobSn.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobSn.setReworkFlag("N");
            hmeEoJobSn.setJobType("COS_WIRE_BOND");
            // 20210310 add by sanfeng.zhang for zhenyong.ban ?????????eo???????????????????????????job_type???????????????eo_step_num + 1
            Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, dto.getWorkcellId(), null, mtMaterialLot.getMaterialLotId(), HmeConstants.ConstantValue.NO, "COS_WIRE_BOND", operationId);
            hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
            hmeEoJobSn.setSourceJobId(cosRecord);
            hmeEoJobSn.setSnQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
//            String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, mtMaterialLot.getMaterialLotId());
            hmeEoJobSn.setAttribute3(hmeCosOperationRecord.getCosType());
            hmeEoJobSn.setAttribute5(hmeCosOperationRecord.getWafer());
            hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
            hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId, Collections.singletonList(hmeEoJobSn.getJobId()), dto.getWorkcellId());
        }
        //??????????????????????????????????????????
        //????????????+???????????????hme_wo_job_sn???????????????????????????????????????????????????
        HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
        hmeWoJobSn.setTenantId(tenantId);
        hmeWoJobSn.setSiteId(dto.getSiteId());
        hmeWoJobSn.setWorkOrderId(workOrderId);
        hmeWoJobSn.setOperationId(operationId);
        HmeWoJobSn hmeWoJobSnDb = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
        if (hmeWoJobSnDb == null) {
            hmeWoJobSn.setSiteInNum(mtMaterialLot.getPrimaryUomQty().longValue());
            hmeWoJobSnRepository.insertSelective(hmeWoJobSn);
        } else {
            hmeWoJobSnDb.setSiteInNum(hmeWoJobSnDb.getSiteInNum() + mtMaterialLot.getPrimaryUomQty().longValue());
            hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSnDb);
        }
        //2021-02-04 add by chaonan.hu for zhenyong.ban ??????COS??????
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = materialLotLoadRepository.select(new HmeMaterialLotLoad() {{
            setTenantId(tenantId);
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
        }});
        if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
            HmeLoadJobDTO3 hmeLoadJobDTO3 = new HmeLoadJobDTO3();
            hmeLoadJobDTO3.setSiteId(dto.getSiteId());
            hmeLoadJobDTO3.setOperationId(dto.getOperationId());
            hmeLoadJobDTO3.setWorkcellId(dto.getWorkcellId());
            List<HmeEquipment> hmeEquipmentList = hmeEoJobEquipmentMapper.queryEquipmentListByWorkCellId(tenantId, dto.getWorkcellId());
            if (CollectionUtils.isNotEmpty(hmeEquipmentList)) {
                List<String> assetEncodingList = hmeEquipmentList.stream().map(HmeEquipment::getEquipmentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                hmeLoadJobDTO3.setAssetEncodingList(assetEncodingList);
            }
            // 20210928 modify by sanfeng.zhang for peng.zhao ??????????????????
            this.handleMaterialLotLoad(tenantId, hmeLoadJobDTO3, "COS_WIRE_BOND_IN", hmeCosOperationRecord, hmeMaterialLotLoadList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void barcodeSiteOut(Long tenantId, List<HmeCosWireBondDTO1> hmeCosWireBondDTO1List) {
        List<String> assetEncodingList = new ArrayList<>();
        List<HmeEquipment> hmeEquipmentList = hmeEoJobEquipmentMapper.queryEquipmentListByWorkCellId(tenantId, hmeCosWireBondDTO1List.get(0).getWorkcellId());
        if (CollectionUtils.isNotEmpty(hmeEquipmentList)) {
            assetEncodingList = hmeEquipmentList.stream().map(HmeEquipment::getEquipmentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        for (HmeCosWireBondDTO1 dto : hmeCosWireBondDTO1List) {
            String operationId = dto.getOperationId();
            MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());

            //??????????????????????????????
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? 1L : curUser.getUserId();
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSn.setSiteOutDate(new Date());
            hmeEoJobSnRepository.updateByPrimaryKeySelective(hmeEoJobSn);

            //2020-11-19 edit by chaonan.hu for zhenyong.ban
            String workOrderId = null;
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_material_lot_attr");
            mtExtendVO.setAttrName("WORK_ORDER_ID");
            mtExtendVO.setKeyId(dto.getMaterialLotId());
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                workOrderId = mtExtendAttrVOS.get(0).getAttrValue();
            }
            String waferNum = null;
            mtExtendVO.setAttrName("WAFER_NUM");
            mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                waferNum = mtExtendAttrVOS.get(0).getAttrValue();
            }

            // ??????????????????????????????
            List<String> routeStepIdList = hmeCosWireBondMapper.queryRouterStepIdByWorkOrderIdAndOperationId(tenantId, workOrderId, dto.getOperationId());
            // ??????????????????ROUTER_STEP_ID
            if (CollectionUtils.isEmpty(routeStepIdList)) {
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", HME));
            }
            String routeStepId = routeStepIdList.get(0);

            //????????????????????????
            HmeCosOperationRecord hmeCosOperationRecord = hmeCosWireBondMapper.recordQuery(tenantId, dto.getWorkcellId(), operationId, workOrderId, waferNum, null);
            hmeCosOperationRecord.setSurplusCosNum(hmeCosOperationRecord.getSurplusCosNum() -
                    mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

            // ??????????????????
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHis.setTenantId(tenantId);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

            //??????????????????????????????
            HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
            hmeWoJobSn.setTenantId(tenantId);
            hmeWoJobSn.setSiteId(dto.getSiteId());
            hmeWoJobSn.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
            hmeWoJobSn.setOperationId(operationId);
            HmeWoJobSn hmeWoJobSnDb = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
            hmeWoJobSnDb.setProcessedNum(hmeWoJobSnDb.getProcessedNum() + mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSnDb);

            //?????????????????????ID
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("MATERIAL_LOT_CONSUME");
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            //???????????????????????? ?????????ID
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "COS_WIRE_BOND");
            //???????????????????????? ?????????ID
            mtEventCreateVO.setEventTypeCode("COS_WIRE_BOND_FEEDING");
            String parentEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            //????????????????????????????????? ??????ID
            /*mtEventCreateVO.setEventTypeCode("MATERIAL_LOT_CONSUME");
            String eventId2 = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);*/

            //??????wkc_shift_id?????????????????????????????????
            MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());
            //???????????????????????????
            List<MtBomComponent> mtBomComponentList = new ArrayList<>();
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeCosOperationRecord.getWorkOrderId());
            String routerOperationId = null;
            String routerStepId = null;
            if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
                //??????ROUTER_ID?????????ID?????????????????????mt_router_step???
                //??????????????????????????????mt_router_operation?????????routerStepId??????????????????ID
                List<MtRouterOperation> mtRouterOperations = hmeCosPatchPdaMapper.routerOperationQuery(tenantId, mtWorkOrder.getRouterId());
                for (MtRouterOperation mtRouterOperation : mtRouterOperations) {
                    if (operationId.equals(mtRouterOperation.getOperationId())) {
                        routerStepId = mtRouterOperation.getRouterStepId();
                        routerOperationId = mtRouterOperation.getRouterOperationId();
                        break;
                    }
                }
                if (StringUtils.isNotEmpty(routerOperationId)) {
                    List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.CHIP_ITEM_GROUP", tenantId);
                    if (CollectionUtils.isEmpty(lovValueDTOS)) {
                        throw new MtException("HME_COS_PATCH_PDA_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_PATCH_PDA_0015", "HME"));
                    }
                    List<String> itemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                    //??????BOM_COMPONENT_ID
                    mtBomComponentList = hmeCosPatchPdaMapper.bomComponentQuery2(tenantId, routerOperationId, dto.getSiteId(), itemGroupList);
                }
            }

            // ???????????????????????????
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("CURRENT_ROUTER_STEP");
            mtExtendVO5.setAttrValue(routeStepId);
            attrList.add(mtExtendVO5);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
                setEventId(eventId);
                setKeyId(dto.getMaterialLotId());
                setAttrs(attrList);
            }});
            List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
            for (MtBomComponent mtBomComponent : mtBomComponentList) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtBomComponent.getMaterialId());
                //???????????????????????????
                BigDecimal totalQty = BigDecimal.valueOf(mtBomComponent.getQty() * mtMaterialLot1.getPrimaryUomQty().longValue());
                //??????????????????????????????????????????
                BigDecimal currentQty = BigDecimal.ZERO;
                //????????????????????????????????????????????????
                List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeEoJobLotMaterialRepository.select(new HmeEoJobLotMaterial() {{
                    setTenantId(tenantId);
                    setWorkcellId(dto.getWorkcellId());
                    setMaterialId(mtBomComponent.getMaterialId());
                }});
                if (CollectionUtils.isEmpty(hmeEoJobLotMaterials)) {
                    throw new MtException("HME_COS_PATCH_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_PATCH_0003", "HME", mtMaterial.getMaterialCode()));
                } else {
                    //?????????????????????????????????????????????
                    hmeEoJobLotMaterials = hmeEoJobLotMaterials.stream().sorted(Comparator.comparing(HmeEoJobLotMaterial::getCreationDate)).collect(Collectors.toList());
                    for (HmeEoJobLotMaterial hmeEoJobLotMaterial : hmeEoJobLotMaterials) {
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobLotMaterial.getMaterialLotId());
                        currentQty = currentQty.add(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
                    }
                    if (totalQty.compareTo(currentQty) > 0) {
                        //?????????>????????????????????????
                        throw new MtException("HME_COS_PATCH_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_PATCH_0004", "HME", mtMaterial.getMaterialCode()));
                    } else {
                        List<MtMaterialLot> distSnList = new ArrayList<>();
                        //????????????
                        boolean breakFlag = false;
                        for (HmeEoJobLotMaterial hmeEoJobLotMaterial : hmeEoJobLotMaterials) {
                            Double useQty;
                            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobLotMaterial.getMaterialLotId());
                            if (totalQty.compareTo(BigDecimal.ZERO) > 0) {
                                //?????????????????????????????????????????????trxPrimaryUomQty = -?????????
                                if (totalQty.compareTo(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())) < 0) {
                                    //2021-04-22 20:23 edit by chaonan.hu for kang.wang ???????????????????????????
                                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                                    mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                    mtMaterialLotVO20.setTrxPrimaryUomQty(totalQty.multiply(new BigDecimal(-1)).doubleValue());
                                    mtMaterialLotVO20List.add(mtMaterialLotVO20);
//                                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//                                    mtMaterialLotVO2.setEventId(eventId);
//                                    mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//                                    mtMaterialLotVO2.setTrxPrimaryUomQty(totalQty.multiply(new BigDecimal(-1)).doubleValue());
//                                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                                    useQty = totalQty.doubleValue();
                                    breakFlag = true;
                                } else {
                                    //2021-04-22 20:23 edit by chaonan.hu for kang.wang ???????????????????????????
                                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                                    mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                    mtMaterialLotVO20.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() * -1);
                                    mtMaterialLotVO20.setEnableFlag("N");
                                    mtMaterialLotVO20List.add(mtMaterialLotVO20);

                                    //???????????????????????????????????????????????????trxPrimaryUomQty = -????????????,??????????????????
//                                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//                                    mtMaterialLotVO2.setEventId(eventId);
//                                    mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//                                    mtMaterialLotVO2.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() * -1);
//                                    mtMaterialLotVO2.setEnableFlag("N");
//                                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                                    totalQty = totalQty.subtract(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
                                    useQty = mtMaterialLot.getPrimaryUomQty();
                                    //????????????ID??????hme_eo_job_lot_material??????????????????????????????
                                    List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = hmeEoJobLotMaterialRepository.select(new HmeEoJobLotMaterial() {{
                                        setTenantId(tenantId);
                                        setMaterialLotId(hmeEoJobLotMaterial.getMaterialLotId());
                                    }});
                                    hmeEoJobLotMaterialRepository.batchDeleteByPrimaryKey(hmeEoJobLotMaterialList);
                                }

                                //??????API{woComponentAssemble} ??????????????????????????????
                                MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
                                mtWoComponentActualVO1.setBomComponentId(mtBomComponent.getBomComponentId());
                                mtWoComponentActualVO1.setEventRequestId(eventRequestId);
                                mtWoComponentActualVO1.setLocatorId(mtMaterialLot.getLocatorId());
                                mtWoComponentActualVO1.setMaterialId(mtMaterialLot.getMaterialId());
                                mtWoComponentActualVO1.setOperationId(operationId);
                                mtWoComponentActualVO1.setParentEventId(parentEventId);
                                mtWoComponentActualVO1.setRouterStepId(routerStepId);
                                mtWoComponentActualVO1.setShiftDate(mtWkcShift.getShiftDate());
                                mtWoComponentActualVO1.setShiftCode(mtWkcShift.getShiftCode());
                                mtWoComponentActualVO1.setTrxAssembleQty(useQty);
                                mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
                                mtWoComponentActualVO1.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
                                mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);
                                //??????API{onhandQtyUpdateProcess} ???????????????????????????????????????
                                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                                mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                                mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
                                mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
                                mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                                mtInvOnhandQuantityVO9.setChangeQuantity(useQty);
                                mtInvOnhandQuantityVO9.setEventId(eventId);
                                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
                                //??????API{objectTransactionSync} ??????????????????????????????????????????????????????
                                WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                                objectTransactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE");
                                objectTransactionRequestVO.setEventId(eventId);
                                objectTransactionRequestVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                objectTransactionRequestVO.setMaterialId(mtMaterialLot.getMaterialId());
                                objectTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(useQty));
                                objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());

                                //??????
                                MtUom uom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                                objectTransactionRequestVO.setTransactionUom(uom.getUomCode());

                                //??????
                                MtModLocator loc = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                                objectTransactionRequestVO.setLocatorId(mtMaterialLot.getLocatorId());
                                objectTransactionRequestVO.setLocatorCode(loc.getLocatorCode());

                                //??????
                                List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, loc.getLocatorId(), "TOP");
                                if (!org.springframework.util.CollectionUtils.isEmpty(pLocatorIds)) {
                                    MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                                    if (ObjectUtils.anyNotNull(ploc)) {
                                        objectTransactionRequestVO.setWarehouseCode(ploc.getLocatorCode());
                                        objectTransactionRequestVO.setWarehouseId(ploc.getLocatorId());
                                    }
                                }

                                objectTransactionRequestVO.setTransactionTime(new Date());
                                objectTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
                                objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());

                                //??????
                                objectTransactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
                                //bomReserveNum
                                MtExtendVO extendVO = new MtExtendVO();
                                extendVO.setTableName("mt_bom_component_attr");
                                extendVO.setKeyId(mtBomComponent.getBomComponentId());
                                extendVO.setAttrName("lineAttribute10");
                                List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                                if (!Objects.isNull(extendAttrList)) {
                                    objectTransactionRequestVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                                }

                                objectTransactionRequestVO.setMergeFlag("N");
                                WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
                                    setTenantId(tenantId);
                                    setTransactionTypeCode("HME_WO_ISSUE");
                                }});
                                objectTransactionRequestVO.setMoveType(wmsTransactionType.getMoveType());
                                wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(objectTransactionRequestVO));
                                // ???????????????sn????????????
                                MtMaterialLot distSn = new MtMaterialLot();
                                distSn.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                distSn.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                                distSn.setSupplierId(mtMaterialLot.getSupplierId());
                                distSn.setPrimaryUomQty(useQty / mtBomComponent.getQty());
                                distSn.setMaterialId(mtMaterialLot.getMaterialId());
                                distSnList.add(distSn);
                                //2021-02-25 add by chaonan.hu for zhenyong.ban ??????????????????
                                List<MtExtendAttrVO> labCodeAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                                    setMaterialLotId(mtMaterialLot.getMaterialLotId());
                                    setAttrName("LAB_CODE");
                                }});
                                if(CollectionUtils.isNotEmpty(labCodeAttr) && StringUtils.isNotBlank(labCodeAttr.get(0).getAttrValue())){
                                    List<HmeMaterialLotLoad> materialLotLoadList = materialLotLoadRepository.select(new HmeMaterialLotLoad() {{
                                        setTenantId(tenantId);
                                        setMaterialLotId(dto.getMaterialLotId());
                                    }});
                                    for (HmeMaterialLotLoad hmeMaterialLotLoad:materialLotLoadList) {
                                        HmeMaterialLotLabCode hmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                                        hmeMaterialLotLabCode.setTenantId(tenantId);
                                        hmeMaterialLotLabCode.setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
                                        hmeMaterialLotLabCode.setObject("COS");
                                        hmeMaterialLotLabCode.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
                                        hmeMaterialLotLabCode.setLabCode(labCodeAttr.get(0).getAttrValue());
                                        hmeMaterialLotLabCode.setJobId(dto.getJobId());
                                        hmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                                        List<MtExtendAttrVO> workOrderIdAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                                            setMaterialLotId(dto.getMaterialLotId());
                                            setAttrName("WORK_ORDER_ID");
                                        }});
                                        if(CollectionUtils.isNotEmpty(workOrderIdAttr) && StringUtils.isNotBlank(workOrderIdAttr.get(0).getAttrValue())){
                                            hmeMaterialLotLabCode.setWorkOrderId(workOrderIdAttr.get(0).getAttrValue());
                                        }
                                        hmeMaterialLotLabCode.setSourceObject("MA");
                                        hmeMaterialLotLabCode.setSourceMaterialLotId(mtMaterialLot.getMaterialLotId());
                                        hmeMaterialLotLabCode.setSourceMaterialId(mtMaterialLot.getMaterialId());
                                        hmeMaterialLotLabCode.setEnableFlag("Y");
                                        hmeMaterialLotLabCodeRepository.insertSelective(hmeMaterialLotLabCode);
                                    }
                                }
                                if (breakFlag) {
                                    break;
                                }
                            }
                        }
                        // ??????????????????????????????
                        List<HmeMaterialLotLoad> materialLotLoadList = materialLotLoadRepository.select(new HmeMaterialLotLoad() {{
                            setMaterialLotId(dto.getMaterialLotId());
                        }});
                        // ?????????????????????????????????????????????
                        Map<String, MtMaterialLot> snMap = distSnList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotId, v -> v));
                        Map<String, Integer> lineMap = distSnList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotId, v -> (int) Math.ceil(v.getPrimaryUomQty())));
                        AtomicInteger startIndexGen = new AtomicInteger(0);
                        AtomicInteger totalCountGen = new AtomicInteger(0);
                        int snCount = snMap.keySet().size();
                        lineMap.forEach((materialLotId, lineNum) -> {
                            int startIndex = startIndexGen.get();
                            int totalCount = totalCountGen.incrementAndGet();
                            int endIndex = startIndex + lineNum;
                            if (totalCount == snCount) {
                                endIndex = materialLotLoadList.size();
                            }

                            MtExtendVO supplierLotAttr = new MtExtendVO();
                            supplierLotAttr.setTableName("mt_material_lot_attr");
                            supplierLotAttr.setAttrName("SUPPLIER_LOT");
                            supplierLotAttr.setKeyId(materialLotId);
                            List<MtExtendAttrVO> mtExtendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, supplierLotAttr);
                            String supplierLot = CollectionUtils.isNotEmpty(mtExtendAttrList) ? mtExtendAttrList.get(0).getAttrValue() : BLANK;
                            List<HmeMaterialLotLoad> subList = materialLotLoadList.subList(startIndex, endIndex);
                            MtMaterialLot sn = snMap.get(materialLotId);
                            // ??????????????????
                            subList.forEach(rec -> {
                                rec.setAttribute7(sn.getMaterialLotCode());
                                rec.setAttribute12(sn.getMaterialId());
                                rec.setAttribute8(sn.getSupplierId());
                                rec.setAttribute9(supplierLot);
                                materialLotLoadRepository.updateByPrimaryKeySelective(rec);
                            });
                            startIndexGen.set(endIndex);
                        });
                    }
                }
            }
            //?????????????????????
            if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
            }
            //2021-02-04 add by chaonan.hu for zhenyong.ban ??????COS??????
            List<HmeMaterialLotLoad> hmeMaterialLotLoadList = materialLotLoadRepository.select(new HmeMaterialLotLoad() {{
                setTenantId(tenantId);
                setMaterialLotId(mtMaterialLot1.getMaterialLotId());
            }});
            if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                HmeLoadJobDTO3 hmeLoadJobDTO3 = new HmeLoadJobDTO3();
                hmeLoadJobDTO3.setSiteId(dto.getSiteId());
                hmeLoadJobDTO3.setOperationId(dto.getOperationId());
                hmeLoadJobDTO3.setWorkcellId(dto.getWorkcellId());
                if (CollectionUtils.isNotEmpty(assetEncodingList)) {
                    hmeLoadJobDTO3.setAssetEncodingList(assetEncodingList);
                }
                // 20210928 modify by sanfeng.zhang for peng.zhao ??????????????????
                this.handleMaterialLotLoad(tenantId, hmeLoadJobDTO3, "COS_WIRE_BOND_OUT", hmeCosOperationRecord, hmeMaterialLotLoadList);
            }
        }
    }

    private void handleMaterialLotLoad (Long tenantId, HmeLoadJobDTO3 dto, String loadJobType, HmeCosOperationRecord hmeCosOperationRecord, List<HmeMaterialLotLoad> hmeMaterialLotLoadList) {
        List<HmeLoadJob> hmeLoadJobList = new ArrayList<>();
        List<HmeLoadJobObject> hmeLoadJobObjectList = new ArrayList<>();
        List<String> loadJobCids = customDbRepository.getNextKeys("hme_load_job_cid_s", hmeMaterialLotLoadList.size());
        List<String> loadJobIds = customDbRepository.getNextKeys("hme_load_job_s", hmeMaterialLotLoadList.size());
        Integer loadJobIndex = 0;
        Date now = CommonUtils.currentTimeGet();
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoadList) {
            HmeLoadJob hmeLoadJob = new HmeLoadJob();
            hmeLoadJob.setTenantId(tenantId);
            hmeLoadJob.setSiteId(dto.getSiteId());
            hmeLoadJob.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
            hmeLoadJob.setLoadJobType(loadJobType);
            hmeLoadJob.setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeMaterialLotLoad.getMaterialLotId());
            hmeLoadJob.setMaterialId(mtMaterialLot.getMaterialId());
            hmeLoadJob.setLoadRow(hmeMaterialLotLoad.getLoadRow());
            hmeLoadJob.setLoadColumn(hmeMaterialLotLoad.getLoadColumn());
            hmeLoadJob.setCosNum(1L);
            hmeLoadJob.setOperationId(dto.getOperationId());
            hmeLoadJob.setWorkcellId(dto.getWorkcellId());
            hmeLoadJob.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
            hmeLoadJob.setWaferNum(hmeCosOperationRecord.getWafer());
            hmeLoadJob.setCosType(hmeCosOperationRecord.getCosType());
            hmeLoadJob.setStatus("0");
            if("COS_WIRE_BOND_OUT".equals(loadJobType)){
                hmeLoadJob.setBomMaterialId(hmeMaterialLotLoad.getAttribute12());
                if(StringUtils.isNotBlank(hmeMaterialLotLoad.getAttribute7())){
                    MtMaterialLot bomMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                        setTenantId(tenantId);
                        setMaterialLotCode(hmeMaterialLotLoad.getAttribute7());
                    }});
                    if(Objects.nonNull(bomMaterialLot)){
                        hmeLoadJob.setBomMaterialLotId(bomMaterialLot.getMaterialLotId());
                    }
                }
                hmeLoadJob.setBomMaterialLotSupplier(hmeMaterialLotLoad.getAttribute8());
            }
            hmeLoadJob.setLoadJobId(loadJobIds.get(loadJobIndex));
            hmeLoadJob.setCid(Long.valueOf(loadJobCids.get(loadJobIndex++)));
            hmeLoadJob.setObjectVersionNumber(1L);
            hmeLoadJob.setCreatedBy(userId);
            hmeLoadJob.setCreationDate(now);
            hmeLoadJob.setLastUpdatedBy(userId);
            hmeLoadJob.setLastUpdateDate(now);
            hmeLoadJobList.add(hmeLoadJob);
            //??????
            if (CollectionUtils.isNotEmpty(dto.getAssetEncodingList())) {
                for (String assetEncoding : dto.getAssetEncodingList()) {
                    HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                    hmeLoadJobObject.setTenantId(tenantId);
                    hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                    hmeLoadJobObject.setObjectType("EQUIPMENT");
                    hmeLoadJobObject.setObjectId(assetEncoding);
                    hmeLoadJobObjectList.add(hmeLoadJobObject);
                }
            }
            //??????
            List<String> ncCodeList = hmeLoadJobMapper.ncCodeQuery(tenantId, hmeMaterialLotLoad.getLoadSequence());
            if(CollectionUtils.isNotEmpty(ncCodeList)){
                for (String ncCode:ncCodeList) {
                    HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                    hmeLoadJobObject.setTenantId(tenantId);
                    hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                    hmeLoadJobObject.setObjectType("NC");
                    hmeLoadJobObject.setObjectId(ncCode);
                    hmeLoadJobObjectList.add(hmeLoadJobObject);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(hmeLoadJobList)) {
            List<List<HmeLoadJob>> splitSqlList = InterfaceUtils.splitSqlList(hmeLoadJobList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeLoadJob> domains : splitSqlList) {
                hmeLoadJobMapper.batchInsert(domains);
            }
        }
        if (CollectionUtils.isNotEmpty(hmeLoadJobObjectList)) {
            List<String> loadJobObjectIdList = customDbRepository.getNextKeys("hme_load_job_object_s", hmeLoadJobObjectList.size());
            List<String> loadJobObjectCidList = customDbRepository.getNextKeys("hme_load_job_object_cid_s", hmeLoadJobObjectList.size());
            Integer objectIndex = 0;
            for (HmeLoadJobObject loadJobObject : hmeLoadJobObjectList) {
                loadJobObject.setLoadObjectId(loadJobObjectIdList.get(objectIndex));
                loadJobObject.setCid(Long.valueOf(loadJobObjectCidList.get(objectIndex++)));
                loadJobObject.setObjectVersionNumber(1L);
                loadJobObject.setCreatedBy(userId);
                loadJobObject.setCreationDate(now);
                loadJobObject.setLastUpdatedBy(userId);
                loadJobObject.setLastUpdateDate(now);
            }
            if (CollectionUtils.isNotEmpty(hmeLoadJobObjectList)) {
                List<List<HmeLoadJobObject>> splitSqlList = InterfaceUtils.splitSqlList(hmeLoadJobObjectList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeLoadJobObject> domains : splitSqlList) {
                    hmeLoadJobObjectMapper.batchInsert(domains);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void feedingSiteOut(Long tenantId, List<HmeCosWireBondDTO1> hmeCosWireBondDTO1List) {
        for (HmeCosWireBondDTO1 dto : hmeCosWireBondDTO1List) {
            String operationId = dto.getOperationId();
            MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());

            //??????????????????????????????
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? 1L : curUser.getUserId();
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSn.setSiteOutDate(new Date());
            hmeEoJobSnRepository.updateByPrimaryKeySelective(hmeEoJobSn);

            //????????????????????????
            HmeCosOperationRecord hmeCosOperationRecord = hmeCosPatchPdaMapper.recordQuery(tenantId, dto.getWorkcellId(), operationId, null);
            hmeCosOperationRecord.setSurplusCosNum(hmeCosOperationRecord.getSurplusCosNum() -
                    mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(hmeCosOperationRecord);

            // ??????????????????
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHis.setTenantId(tenantId);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

            //??????????????????????????????
            HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
            hmeWoJobSn.setTenantId(tenantId);
            hmeWoJobSn.setSiteId(dto.getSiteId());
            hmeWoJobSn.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
            hmeWoJobSn.setOperationId(operationId);
            HmeWoJobSn hmeWoJobSnDb = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
            hmeWoJobSnDb.setProcessedNum(hmeWoJobSnDb.getProcessedNum() + mtMaterialLot1.getPrimaryUomQty().longValue());
            hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSnDb);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void barcodeFeeding(Long tenantId, HmeCosWireBondDTO2 dto) {
        //?????????????????????ID
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("MATERIAL_LOT_CONSUME");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        //???????????????????????? ?????????ID
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "COS_WIRE_BOND");
        //???????????????????????? ?????????ID
        mtEventCreateVO.setEventTypeCode("COS_WIRE_BOND_FEEDING");
        String parentEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        //????????????????????????????????? ??????ID
        /*mtEventCreateVO.setEventTypeCode("MATERIAL_LOT_CONSUME");
        String eventId2 = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);*/

        //??????wkc_shift_id?????????????????????????????????
        MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(dto.getWkcShiftId());
        //???????????????????????????
        List<MtBomComponent> mtBomComponentList;
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(dto.getWorkOrderId());

        //??????bom??????
        List<String> bomIds = new ArrayList<>();
        bomIds.add(mtWorkOrder.getBomId());
        mtBomComponentList = mtBomComponentMapper.selectBomComponentByBomIdsAll(tenantId, bomIds);
        List<String> materialLists = mtBomComponentList.stream().map(MtBomComponent::getMaterialId).collect(Collectors.toList());
        Map<String, String> bomComponentMap = mtBomComponentList.stream().collect(Collectors.toMap(MtBomComponent::getMaterialId, MtBomComponent::getBomComponentId));
        Map<String, Long> bomLineNumMap = mtBomComponentList.stream().collect(Collectors.toMap(MtBomComponent::getMaterialId, MtBomComponent::getLineNumber));
        //?????????????????????????????????
        List<MtExtendAttrVO1> extendAttrVO1List = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_bom_component_attr");
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 attrOne = new MtExtendVO5();
            attrOne.setAttrName("lineAttribute5");
            attrList.add(attrOne);
            MtExtendVO5 attrTwo = new MtExtendVO5();
            attrTwo.setAttrName("lineAttribute4");
            attrList.add(attrTwo);
            mtExtendVO1.setAttrs(attrList);
            mtExtendVO1.setKeyIdList(mtBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()));
            extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        }
        Map<String, List<MtExtendAttrVO1>> attrMap = extendAttrVO1List.stream().collect(Collectors.groupingBy(attr -> attr.getKeyId() + "_" + attr.getAttrName()));

        //???????????????
        List<MtWorkOrderComponentActual> componentActualList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(materialLists)) {
            componentActualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class)
                    .andWhere(Sqls.custom().andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                            .andIn(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, materialLists)).build());
        }
        Map<String, List<MtWorkOrderComponentActual>> actualMap = componentActualList.stream().collect(Collectors.groupingBy(actual -> actual.getWorkOrderId() + "_" + actual.getMaterialId()));

        //????????????id
        List<String> routeStepIdList = hmeCosWireBondMapper.queryRouterStepIdByWorkOrderIdAndOperationId(tenantId, mtWorkOrder.getWorkOrderId(), dto.getOperationId());

        //??????
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        for (HmeCosWireBondDTO3 hmeCosWireBondDTO3 : dto.getMaterialLotList()) {
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeCosWireBondDTO3.getMaterialId());
            if (!materialLists.contains(hmeCosWireBondDTO3.getMaterialId())) {
                throw new MtException("HME_COS_PATCH_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_0002", "HME", hmeCosWireBondDTO3.getMaterialCode()));
            }

            //?????????????????????????????????????????????
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeCosWireBondDTO3.getMaterialLotId());
            if (hmeCosWireBondDTO3.getQty().compareTo(mtMaterialLot.getPrimaryUomQty()) > 0) {
                //?????????>????????????????????????
                throw new MtException("HME_COS_PATCH_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_0004", "HME", mtMaterial.getMaterialCode()));
            } else {
                Double useQty;
                //?????????????????????????????????????????????trxPrimaryUomQty = -?????????
                if (hmeCosWireBondDTO3.getQty().compareTo(mtMaterialLot.getPrimaryUomQty()) < 0) {
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    mtMaterialLotVO20.setTrxPrimaryUomQty(BigDecimal.valueOf(hmeCosWireBondDTO3.getQty()).multiply(new BigDecimal(-1)).doubleValue());
                    mtMaterialLotVO20List.add(mtMaterialLotVO20);
//                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//                    mtMaterialLotVO2.setEventId(eventId);
//                    mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//                    mtMaterialLotVO2.setTrxPrimaryUomQty(BigDecimal.valueOf(hmeCosWireBondDTO3.getQty()).multiply(new BigDecimal(-1)).doubleValue());
//                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                    useQty = hmeCosWireBondDTO3.getQty();
                } else {
                    //?????????????????????????????????????????????trxPrimaryUomQty = -????????????,??????????????????
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    mtMaterialLotVO20.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() * -1);
                    mtMaterialLotVO20.setEnableFlag("N");
                    mtMaterialLotVO20List.add(mtMaterialLotVO20);

//                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//                    mtMaterialLotVO2.setEventId(eventId);
//                    mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//                    mtMaterialLotVO2.setTrxPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() * -1);
//                    mtMaterialLotVO2.setEnableFlag("N");
//                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                    //totalQty = totalQty.subtract(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                    useQty = mtMaterialLot.getPrimaryUomQty();
                    //????????????ID??????hme_eo_job_lot_material??????????????????????????????
                    List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = hmeEoJobLotMaterialRepository.select(new HmeEoJobLotMaterial() {{
                        setTenantId(tenantId);
                        setMaterialLotId(hmeCosWireBondDTO3.getMaterialLotId());
                    }});
                    hmeEoJobLotMaterialRepository.batchDeleteByPrimaryKey(hmeEoJobLotMaterialList);
                }
                //?????????????????????hme_eo_job_sn_lot_material
                HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = hmeEoJobSnLotMaterialRepository.selectOne(new HmeEoJobSnLotMaterial() {{
                    setTenantId(tenantId);
                    setJobId(dto.getJobId());
                    setMaterialLotId(hmeCosWireBondDTO3.getMaterialLotId());
                }});
                if (Objects.isNull(hmeEoJobSnLotMaterial)) {
                    hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setTenantId(tenantId);
                    hmeEoJobSnLotMaterial.setMaterialId(mtMaterial.getMaterialId());
                    hmeEoJobSnLotMaterial.setLotMaterialId(mtMaterial.getMaterialId());
                    hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                    hmeEoJobSnLotMaterial.setJobId(dto.getJobId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setReleaseQty(BigDecimal.valueOf(useQty));
                    hmeEoJobSnLotMaterial.setIsReleased(1);
                    hmeEoJobSnLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                    hmeEoJobSnLotMaterialRepository.insert(hmeEoJobSnLotMaterial);
                } else {
                    hmeEoJobSnLotMaterial.setReleaseQty(hmeEoJobSnLotMaterial.getReleaseQty().add(BigDecimal.valueOf(useQty)));
                    hmeEoJobSnLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobSnLotMaterial);
                }

                //??????????????????????????????????????????????????????????????????/???
                String bomComponentId = bomComponentMap.get(mtMaterial.getMaterialId());
                List<MtExtendAttrVO1> totalDemandList = attrMap.get(bomComponentId + "_" + "lineAttribute5");
                BigDecimal totalDemand = CollectionUtils.isNotEmpty(totalDemandList) ? BigDecimal.valueOf(Double.parseDouble(totalDemandList.get(0).getAttrValue())) : BigDecimal.ZERO;
                List<MtExtendAttrVO1> realDemandList = attrMap.get(bomComponentId + "_" + "lineAttribute4");
                BigDecimal realDemand = CollectionUtils.isNotEmpty(realDemandList) ? BigDecimal.valueOf(Double.parseDouble(realDemandList.get(0).getAttrValue())) : BigDecimal.ZERO;
                //???????????????(???????????????-???????????????)
                BigDecimal lastScrappedQty = totalDemand.subtract(realDemand);
                //???????????????????????????????????????????????????????????????
                List<WmsObjectTransactionRequestVO> requestVOList = new ArrayList<>();
                //???????????????
                List<MtWorkOrderComponentActual> actualList = actualMap.get(mtWorkOrder.getWorkOrderId() + "_" + mtMaterial.getMaterialId());
                BigDecimal scrappedQty = CollectionUtils.isNotEmpty(actualList) ? BigDecimal.valueOf(actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).mapToDouble(Double::doubleValue).summaryStatistics().getSum()) : BigDecimal.ZERO;
                if (scrappedQty.add(BigDecimal.valueOf(hmeCosWireBondDTO3.getQty())).compareTo(lastScrappedQty) <= 0) {
                    //??????????????? + ???????????? <= ??????????????? ??????????????? ??????????????????????????? ??????bomReserveNum,bomReserveLineNum
                    WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                    requestVO.setTransactionTypeCode("HME_WO_ISSUE");
                    requestVO.setTransactionQty(BigDecimal.valueOf(hmeCosWireBondDTO3.getQty()));
                    //??????
                    requestVO.setBomReserveLineNum(bomLineNumMap.get(mtMaterial.getMaterialId()).toString());
                    //bomReserveNum
                    MtExtendVO extendVO = new MtExtendVO();
                    extendVO.setTableName("mt_bom_component_attr");
                    extendVO.setKeyId(bomComponentMap.get(mtMaterial.getMaterialId()));
                    extendVO.setAttrName("lineAttribute10");
                    List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                    if (!Objects.isNull(extendAttrList)) {
                        requestVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                    }
                    WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
                        setTenantId(tenantId);
                        setTransactionTypeCode("HME_WO_ISSUE");
                    }});
                    requestVO.setMoveType(wmsTransactionType.getMoveType());
                    requestVOList.add(requestVO);
                } else {
                    //??????????????? >= ??????????????? ??????????????? ???????????????????????????
                    if (scrappedQty.compareTo(lastScrappedQty) >= 0) {
                        WmsObjectTransactionRequestVO requestVO = new WmsObjectTransactionRequestVO();
                        requestVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        requestVO.setTransactionQty(BigDecimal.valueOf(hmeCosWireBondDTO3.getQty()));
                        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
                            setTenantId(tenantId);
                            setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        }});
                        requestVO.setMoveType(wmsTransactionType.getMoveType());
                        requestVOList.add(requestVO);
                    } else {
                        //??????????????? < ???????????????(???????????????/?????????)
                        //??????????????? ??????????????????????????????-???????????????
                        WmsObjectTransactionRequestVO requestInVO = new WmsObjectTransactionRequestVO();
                        requestInVO.setTransactionTypeCode("HME_WO_ISSUE");
                        requestInVO.setTransactionQty(lastScrappedQty.subtract(scrappedQty));
                        //??????
                        requestInVO.setBomReserveLineNum(bomLineNumMap.get(mtMaterial.getMaterialId()).toString());
                        //bomReserveNum
                        MtExtendVO extendVO = new MtExtendVO();
                        extendVO.setTableName("mt_bom_component_attr");
                        extendVO.setKeyId(bomComponentMap.get(mtMaterial.getMaterialId()));
                        extendVO.setAttrName("lineAttribute10");
                        List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
                        if (!Objects.isNull(extendAttrList)) {
                            requestInVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
                        }
                        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
                            setTenantId(tenantId);
                            setTransactionTypeCode("HME_WO_ISSUE");
                        }});
                        requestInVO.setMoveType(wmsTransactionType.getMoveType());
                        requestVOList.add(requestInVO);
                        //??????????????? ????????????????????????????????? + ???????????????- ???????????????
                        WmsObjectTransactionRequestVO requestOutVO = new WmsObjectTransactionRequestVO();
                        requestOutVO.setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        requestOutVO.setTransactionQty(scrappedQty.add(BigDecimal.valueOf(hmeCosWireBondDTO3.getQty())).subtract(lastScrappedQty));
                        WmsTransactionType transactionType = wmsTransactionTypeRepository.selectOne(new WmsTransactionType() {{
                            setTenantId(tenantId);
                            setTransactionTypeCode("HME_WO_ISSUE_EXT");
                        }});
                        requestOutVO.setMoveType(transactionType.getMoveType());
                        requestVOList.add(requestOutVO);
                    }
                }
                //??????API{woComponentAssemble} ??????????????????????????????
                MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
                if (materialLists.contains(hmeCosWireBondDTO3.getMaterialId())) {
                    mtWoComponentActualVO1.setAssembleExcessFlag("N");
                } else {
                    mtWoComponentActualVO1.setAssembleExcessFlag("Y");
                }
                mtWoComponentActualVO1.setBomComponentId(bomComponentId);
                mtWoComponentActualVO1.setEventRequestId(eventRequestId);
                mtWoComponentActualVO1.setLocatorId(mtMaterialLot.getLocatorId());
                mtWoComponentActualVO1.setMaterialId(mtMaterialLot.getMaterialId());
                mtWoComponentActualVO1.setOperationId(dto.getOperationId());
                mtWoComponentActualVO1.setParentEventId(parentEventId);
                if (CollectionUtils.isNotEmpty(routeStepIdList)) {
                    mtWoComponentActualVO1.setRouterStepId(routeStepIdList.get(0));
                }
                mtWoComponentActualVO1.setShiftDate(mtWkcShift.getShiftDate());
                mtWoComponentActualVO1.setShiftCode(mtWkcShift.getShiftCode());
                mtWoComponentActualVO1.setTrxAssembleQty(useQty);
                mtWoComponentActualVO1.setWorkcellId(dto.getWorkcellId());
                mtWoComponentActualVO1.setWorkOrderId(dto.getWorkOrderId());
                mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);
                //??????API{woComponentScrap} ??????????????????????????????
                MtWoComponentActualVO8 mtWoComponentActualVO8 = new MtWoComponentActualVO8();
                if (materialLists.contains(hmeCosWireBondDTO3.getMaterialId())) {
                    mtWoComponentActualVO8.setAssembleExcessFlag("N");
                } else {
                    mtWoComponentActualVO8.setAssembleExcessFlag("Y");
                }
                mtWoComponentActualVO8.setBomComponentId(bomComponentId);
                mtWoComponentActualVO8.setBomId(mtWorkOrder.getBomId());
                mtWoComponentActualVO8.setEventRequestId(eventRequestId);
                mtWoComponentActualVO8.setLocatorId(mtMaterialLot.getLocatorId());
                mtWoComponentActualVO8.setMaterialId(mtMaterialLot.getMaterialId());
                mtWoComponentActualVO8.setOperationId(dto.getOperationId());
                if (CollectionUtils.isNotEmpty(routeStepIdList)) {
                    mtWoComponentActualVO8.setRouterStepId(routeStepIdList.get(0));
                }
                mtWoComponentActualVO8.setShiftCode(mtWkcShift.getShiftCode());
                mtWoComponentActualVO8.setShiftDate(mtWkcShift.getShiftDate());
                mtWoComponentActualVO8.setTrxScrappedQty(hmeCosWireBondDTO3.getQty());
                mtWoComponentActualVO8.setWorkcellId(dto.getWorkcellId());
                mtWoComponentActualVO8.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                mtWorkOrderComponentActualRepository.woComponentScrap(tenantId, mtWoComponentActualVO8);

                //????????????
                MtWoComponentActualVO12 componentV12 = new MtWoComponentActualVO12();
                componentV12.setTrxAssembleQty(hmeCosWireBondDTO3.getQty());
                if (materialLists.contains(hmeCosWireBondDTO3.getMaterialId())) {
                    componentV12.setAssembleExcessFlag("N");
                } else {
                    componentV12.setAssembleExcessFlag("Y");
                }
                componentV12.setBomComponentId(bomComponentId);
                componentV12.setBomId(mtWorkOrder.getBomId());
                componentV12.setEventRequestId(eventRequestId);
                componentV12.setLocatorId(mtMaterialLot.getLocatorId());
                componentV12.setMaterialId(mtMaterialLot.getMaterialId());
                componentV12.setOperationId(dto.getOperationId());
                componentV12.setParentEventId(parentEventId);
                if (CollectionUtils.isNotEmpty(routeStepIdList)) {
                    componentV12.setRouterStepId(routeStepIdList.get(0));
                }
                componentV12.setShiftCode(mtWkcShift.getShiftCode());
                componentV12.setShiftDate(mtWkcShift.getShiftDate());
                componentV12.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                componentV12.setWorkcellId(dto.getWorkcellId());
                mtWorkOrderComponentActualRepository.woComponentAssembleCancel(tenantId, componentV12);

                //??????API{onhandQtyUpdateProcess} ???????????????????????????????????????
                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                mtInvOnhandQuantityVO9.setSiteId(mtMaterialLot.getSiteId());
                mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
                mtInvOnhandQuantityVO9.setLocatorId(mtMaterialLot.getLocatorId());
                mtInvOnhandQuantityVO9.setLotCode(mtMaterialLot.getLot());
                mtInvOnhandQuantityVO9.setChangeQuantity(useQty);
                mtInvOnhandQuantityVO9.setEventId(eventId);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
                //??????API{objectTransactionSync} ??????????????????????????????????????????????????????
                List<WmsObjectTransactionRequestVO> transactionRequestVOList = new ArrayList<>();
                for (WmsObjectTransactionRequestVO requestVO : requestVOList) {
                    WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                    objectTransactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
                    objectTransactionRequestVO.setEventId(eventId);
                    objectTransactionRequestVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    objectTransactionRequestVO.setMaterialId(mtMaterialLot.getMaterialId());
                    objectTransactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
                    objectTransactionRequestVO.setLotNumber(mtMaterialLot.getLot());
                    //??????
                    MtUom uom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                    objectTransactionRequestVO.setTransactionUom(uom.getUomCode());
                    //??????
                    MtModLocator loc = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                    objectTransactionRequestVO.setLocatorId(mtMaterialLot.getLocatorId());
                    objectTransactionRequestVO.setLocatorCode(loc.getLocatorCode());
                    //??????
                    List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, loc.getLocatorId(), "TOP");
                    if (!org.springframework.util.CollectionUtils.isEmpty(pLocatorIds)) {
                        MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                        if (ObjectUtils.anyNotNull(ploc)) {
                            objectTransactionRequestVO.setWarehouseCode(ploc.getLocatorCode());
                            objectTransactionRequestVO.setWarehouseId(ploc.getLocatorId());
                        }
                    }
                    objectTransactionRequestVO.setTransactionTime(new Date());
                    objectTransactionRequestVO.setPlantId(mtMaterialLot.getSiteId());
                    objectTransactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    objectTransactionRequestVO.setMergeFlag("N");
                    objectTransactionRequestVO.setMoveType(requestVO.getMoveType());
                    objectTransactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
                    objectTransactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
                    transactionRequestVOList.add(objectTransactionRequestVO);
                }
                if (CollectionUtils.isNotEmpty(transactionRequestVOList)) {
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionRequestVOList);
                }
            }
        }
        //?????????????????????
        if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, NO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosWireBondVO2> bandingMaterialQuery(Long tenantId, String workcellId, String jobId, Double qty, String materialLotId) {
        List<HmeCosWireBondVO2> resultList = new ArrayList<>();
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeEoJobLotMaterialRepository.select(new HmeEoJobLotMaterial() {{
            setTenantId(tenantId);
            setWorkcellId(workcellId);
        }});
        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterials)) {
            HmeCosWireBondVO2 hmeCosWireBondVO2;
            for (HmeEoJobLotMaterial hmeEoJobLotMaterial : hmeEoJobLotMaterials) {
                if (StringUtils.isEmpty(hmeEoJobLotMaterial.getMaterialLotId())) {
                    continue;
                }
                hmeCosWireBondVO2 = new HmeCosWireBondVO2();
                hmeCosWireBondVO2.setMaterialId(hmeEoJobLotMaterial.getMaterialId());
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeEoJobLotMaterial.getMaterialId());
                hmeCosWireBondVO2.setMaterialCode(mtMaterial.getMaterialCode());
                hmeCosWireBondVO2.setMaterialName(mtMaterial.getMaterialName());
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobLotMaterial.getMaterialLotId());
                hmeCosWireBondVO2.setMaterialLotQty(mtMaterialLot.getPrimaryUomQty());
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
                hmeCosWireBondVO2.setUomCode(mtUom.getUomCode());
                hmeCosWireBondVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeCosWireBondVO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                if (StringUtils.isNotEmpty(jobId)) {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = hmeEoJobSnLotMaterialRepository.selectOne(new HmeEoJobSnLotMaterial() {{
                        setTenantId(tenantId);
                        setJobId(jobId);
                        setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    }});
                    if (!Objects.isNull(hmeEoJobSnLotMaterial)) {
                        //???????????????
                        hmeCosWireBondVO2.setInvestedQty(hmeEoJobSnLotMaterial.getReleaseQty().doubleValue());

                    }
                    //????????????ID
                    List<MtExtendAttrVO> mtExtendAttrVOS;
                    MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                    mtMaterialLotAttrVO2.setMaterialLotId(materialLotId);
                    mtMaterialLotAttrVO2.setAttrName("WORK_ORDER_ID");
                    mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                    String workOrderId = mtExtendAttrVOS.get(0).getAttrValue();
                    MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);

                    //??????bom??????
                    List<String> bomIds = new ArrayList<>();
                    bomIds.add(mtWorkOrder.getBomId());
                    List<MtBomComponent> mtBomComponentList = mtBomComponentMapper.selectBomComponentByBomIdsAll(tenantId, bomIds);
                    List<String> materialIds = mtBomComponentList.stream().map(MtBomComponent::getMaterialId).collect(Collectors.toList());
                    Map<String, Double> bomComponentMap = mtBomComponentList.stream().collect(Collectors.toMap(MtBomComponent::getMaterialId, MtBomComponent::getQty));

                    //????????????
                    if (materialIds.contains(mtMaterial.getMaterialId())) {
                        hmeCosWireBondVO2.setDemandQty(BigDecimal.valueOf(qty).multiply(BigDecimal.valueOf(bomComponentMap.get(mtMaterial.getMaterialId()))).doubleValue());
                    }
                }

                resultList.add(hmeCosWireBondVO2);
            }
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosWireBondDTO4 batchDelete(Long tenantId, HmeCosWireBondDTO4 dto) {
        if (liquibase.util.StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
        if (liquibase.util.StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }
        if (liquibase.util.StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }
//        if (liquibase.util.StringUtils.isEmpty(dto.getWorkOrderId())) {
//            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
//        }
        if (org.apache.commons.collections.CollectionUtils.isEmpty(dto.getMaterialLotIdList())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "?????????"));
        }
        for (String materialLotId : dto.getMaterialLotIdList()) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
            //??????material_lot_id+operation_id+workcell_id+job_type???COS_WIRE_BOND???????????????????????????????????????????????????
            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class).andWhere(Sqls.custom()
                    .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                    .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, dto.getOperationId())
                    .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLotId)
                    .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "COS_WIRE_BOND")
                    .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)).build());
            if (org.apache.commons.collections.CollectionUtils.isEmpty(hmeEoJobSnList)) {
                throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_010", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            hmeEoJobSnRepository.batchDeleteByPrimaryKey(hmeEoJobSnList);
            //?????????????????????????????????
            //??????????????????????????????WORK_ORDER_ID???WAFER_NUM
            String workOrderId = null;
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_material_lot_attr");
            mtExtendVO.setAttrName("WORK_ORDER_ID");
            mtExtendVO.setKeyId(materialLotId);
            List<MtExtendAttrVO> mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                workOrderId = mtExtendAttrVOS.get(0).getAttrValue();
            }
            String waferNum = null;
            mtExtendVO.setAttrName("WAFER_NUM");
            mtExtendAttrVOS = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
                waferNum = mtExtendAttrVOS.get(0).getAttrValue();
            }
            //2020-11-20 edit by chaonan.hu for zhenyong.ban ??????WKC_ID+??????_ID+??????Id+wafer+??????_ID??????????????????????????????????????????
            HmeCosOperationRecord lastCosOperationRecord = hmeCosWireBondMapper.recordQuery(tenantId, dto.getWorkcellId(), dto.getOperationId(), workOrderId, waferNum, null);
            if (Objects.isNull(lastCosOperationRecord) || liquibase.util.StringUtils.isEmpty(lastCosOperationRecord.getOperationRecordId())) {
                throw new MtException("HME_COS_PATCH_PDA_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0006", "HME"));
            }
            if (lastCosOperationRecord.getSurplusCosNum() < mtMaterialLot.getPrimaryUomQty()) {
                throw new MtException("HME_COS_PATCH_PDA_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_PATCH_PDA_0009", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            if (lastCosOperationRecord.getCosNum() < mtMaterialLot.getPrimaryUomQty()) {
                throw new MtException("HME_COS_029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_029", "HME"));
            }
            BigDecimal surplusCosNum = BigDecimal.valueOf(lastCosOperationRecord.getSurplusCosNum()).subtract(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
            BigDecimal cosNum = BigDecimal.valueOf(lastCosOperationRecord.getCosNum()).subtract(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
            lastCosOperationRecord.setSurplusCosNum(surplusCosNum.longValue());
            lastCosOperationRecord.setCosNum(cosNum.longValue());
            hmeCosOperationRecordMapper.updateByPrimaryKeySelective(lastCosOperationRecord);

            // ??????????????????
            HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
            BeanUtils.copyProperties(lastCosOperationRecord, hmeCosOperationRecordHis);
            hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);
            //??????????????????
            HmeWoJobSn woJobSn = new HmeWoJobSn();
            woJobSn.setTenantId(tenantId);
            woJobSn.setSiteId(dto.getSiteId());
            woJobSn.setWorkOrderId(workOrderId);
            woJobSn.setOperationId(dto.getOperationId());
            HmeWoJobSn hmeWoJobSn = hmeWoJobSnRepository.selectOne(woJobSn);
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
    public void createLoadJob(Long tenantId, HmeMaterialLotLoad hmeMaterialLotLoad, HmeLoadJobDTO3 dto, String loadJobType, HmeCosOperationRecord hmeCosOperationRecord) {
        HmeLoadJob hmeLoadJob = new HmeLoadJob();
        hmeLoadJob.setTenantId(tenantId);
        hmeLoadJob.setSiteId(dto.getSiteId());
        hmeLoadJob.setLoadSequence(hmeMaterialLotLoad.getLoadSequence());
        hmeLoadJob.setLoadJobType(loadJobType);
        hmeLoadJob.setMaterialLotId(hmeMaterialLotLoad.getMaterialLotId());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeMaterialLotLoad.getMaterialLotId());
        hmeLoadJob.setMaterialId(mtMaterialLot.getMaterialId());
        hmeLoadJob.setLoadRow(hmeMaterialLotLoad.getLoadRow());
        hmeLoadJob.setLoadColumn(hmeMaterialLotLoad.getLoadColumn());
        hmeLoadJob.setCosNum(1L);
        hmeLoadJob.setOperationId(dto.getOperationId());
        hmeLoadJob.setWorkcellId(dto.getWorkcellId());
        hmeLoadJob.setWorkOrderId(hmeCosOperationRecord.getWorkOrderId());
        hmeLoadJob.setWaferNum(hmeCosOperationRecord.getWafer());
        hmeLoadJob.setCosType(hmeCosOperationRecord.getCosType());
        hmeLoadJob.setStatus("0");
        if("COS_WIRE_BOND_OUT".equals(loadJobType)){
            hmeLoadJob.setBomMaterialId(hmeMaterialLotLoad.getAttribute11());
            if(StringUtils.isNotBlank(hmeMaterialLotLoad.getAttribute7())){
                MtMaterialLot bomMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                    setTenantId(tenantId);
                    setMaterialLotCode(hmeMaterialLotLoad.getAttribute7());
                }});
                if(Objects.nonNull(bomMaterialLot)){
                    hmeLoadJob.setBomMaterialLotId(bomMaterialLot.getMaterialLotId());
                }
            }
            hmeLoadJob.setBomMaterialLotSupplier(hmeMaterialLotLoad.getAttribute8());
        }
        hmeLoadJobRepository.insertSelective(hmeLoadJob);
        //??????
        if (CollectionUtils.isNotEmpty(dto.getAssetEncodingList())) {
            for (String assetEncoding : dto.getAssetEncodingList()) {
                HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                hmeLoadJobObject.setTenantId(tenantId);
                hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                hmeLoadJobObject.setObjectType("EQUIPMENT");
                hmeLoadJobObject.setObjectId(assetEncoding);
                hmeLoadJobObjectRepository.insertSelective(hmeLoadJobObject);
            }
        }
        //??????
        List<String> ncCodeList = hmeLoadJobMapper.ncCodeQuery(tenantId, hmeMaterialLotLoad.getLoadSequence());
        if(CollectionUtils.isNotEmpty(ncCodeList)){
            for (String ncCode:ncCodeList) {
                HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                hmeLoadJobObject.setTenantId(tenantId);
                hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                hmeLoadJobObject.setObjectType("NC");
                hmeLoadJobObject.setObjectId(ncCode);
                hmeLoadJobObjectRepository.insertSelective(hmeLoadJobObject);
            }
        }
    }
}
