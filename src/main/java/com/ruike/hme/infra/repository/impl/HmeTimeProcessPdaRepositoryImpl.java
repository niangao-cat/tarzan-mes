package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeEoJobEquipmentService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.repository.MtRouterNextStepRepository;
import tarzan.method.domain.repository.MtRouterOperationRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.OK;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * HmeTimeProcessPdaRepositoryImpl
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 13:54:19
 **/
@Component
public class HmeTimeProcessPdaRepositoryImpl implements HmeTimeProcessPdaRepository {

    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeTimeProcessPdaMapper hmeTimeProcessPdaMapper;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private HmeEqManageTaskDocRepository hmeEqManageTaskDocRepository;
    @Autowired
    private HmeEqManageTaskDocMapper hmeEqManageTaskDocMapper;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private HmeCosGetChipPlatformMapper hmeCosGetChipPlatformMapper;
    @Autowired
    private MtRouterNextStepRepository mtRouterNextStepRepository;
    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeEoJobEquipmentService hmeEoJobEquipmentService;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeVisualInspectionMapper hmeVisualInspectionMapper;

    @Override
    public List<HmeTimeProcessPdaVO4> equipmentQuery(Long tenantId, HmeTimeProcessPdaDTO5 dto) {
        List<HmeTimeProcessPdaVO4> result = hmeTimeProcessPdaMapper.equipmentQuery(tenantId, dto);
        for (HmeTimeProcessPdaVO4 hmeTimeProcessPdaVO4 : result) {
            hmeTimeProcessPdaVO4.setHide(Boolean.FALSE);
        }
        return result;
    }

    @Override
    @ProcessLovValue
    public HmeTimeProcessPdaVO scanEquipment(Long tenantId, HmeTimeProcessPdaDTO dto) {
        //??????????????????????????????ID
        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(dto.getEquipmentCode());
        }});
        if (hmeEquipment == null) {
            throw new MtException("HME_TIME_PROCESS_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0001", "HME"));
        }
        //???????????????????????????????????????
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRels = hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
            setTenantId(tenantId);
            setStationId(dto.getWorkcellId());
            setEquipmentId(hmeEquipment.getEquipmentId());
        }});
        if (CollectionUtils.isEmpty(hmeEquipmentWkcRels)) {
            throw new MtException("HME_TIME_PROCESS_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0002", "HME"));
        }
        //????????????
        HmeTimeProcessPdaVO result = new HmeTimeProcessPdaVO();
        result.setEquipmentId(hmeEquipment.getEquipmentId());
        result.setEquipmentCode(hmeEquipment.getAssetEncoding());
        result.setEquipmentName(hmeEquipment.getAssetName());
        result.setEquipmentStatus("BLUE");
        //??????????????????????????????????????????????????????????????????????????????
        //????????????????????????????????????????????????
        Boolean completeFlag = false;
        //??????????????????
        Date shiftDate = null;
        String shiftCode = null;
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, new MtModOrganizationVO2() {{
            setTopSiteId(dto.getSiteId());
            setOrganizationType("WORKCELL");
            setOrganizationId(dto.getWorkcellId());
            setParentOrganizationType("WORKCELL");
            setQueryType("TOP");
        }});
        if(CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)){
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, mtModOrganizationItemVOS.get(0).getOrganizationId());
            if(mtWkcShiftVO3 != null){
                shiftCode = mtWkcShiftVO3.getShiftCode();
                shiftDate = mtWkcShiftVO3.getShiftDate();
            }
        }
        if (shiftDate != null && StringUtils.isNotBlank(shiftCode)) {
            //????????????????????????????????????
            List<HmeEqManageTaskDoc> hmeEqManageTaskDocs = new ArrayList<>();
            //??????????????? ??????
            HmeEqManageTaskDoc hmeEqManageTaskDoc = new HmeEqManageTaskDoc();
            hmeEqManageTaskDoc.setTenantId(tenantId);
            hmeEqManageTaskDoc.setEquipmentId(hmeEquipment.getEquipmentId());
            hmeEqManageTaskDoc.setDocType("C");
            hmeEqManageTaskDoc.setTaskCycle("0.5");
            hmeEqManageTaskDoc.setShiftDate(shiftDate);
            hmeEqManageTaskDoc.setShiftCode(shiftCode);
            hmeEqManageTaskDocs.addAll(hmeEqManageTaskDocRepository.select(hmeEqManageTaskDoc));
            //??????????????? ?????????
            hmeEqManageTaskDocs.addAll(hmeEqManageTaskDocMapper.queryTaskDocList2(tenantId, hmeEquipment.getEquipmentId()));
            if (CollectionUtils.isNotEmpty(hmeEqManageTaskDocs)) {
                completeFlag = true;
                for (HmeEqManageTaskDoc hmeEqManageTaskDocDb : hmeEqManageTaskDocs) {
                    if (!YES.equals(hmeEqManageTaskDocDb.getDocStatus())) {
                        completeFlag = false;
                        break;
                    }
                }
            }
        }
        if (completeFlag) {
            result.setEquipmentStatus("GREEN");
        }
        result.setWorkcellId(dto.getWorkcellId());
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(dto.getWorkcellId());
        result.setWorkcellName(mtModWorkcell.getWorkcellName());
        return result;
    }

    @Override
    public HmeTimeProcessPdaVO2 scanBarcode(Long tenantId, HmeTimeProcessPdaDTO2 dto) {
        HmeTimeProcessPdaVO2 result = new HmeTimeProcessPdaVO2();
        List<HmeTimeProcessPdaVO3> materialDataList = new ArrayList<>();
        Long barcodeCount = 0L;
        BigDecimal materialCount = BigDecimal.ZERO;
        String materialId = null;
        int flag = 0;
        HmeEquipment hmeEquipment1 = hmeEquipmentRepository.selectOne(new HmeEquipment(){{
            setTenantId(tenantId);
            setAssetEncoding(dto.getEquipmentCode());
        }});
        if (dto.getHmeTimeProcessPdaVO2() != null) {
            barcodeCount = dto.getHmeTimeProcessPdaVO2().getBarcodeCount();
            materialCount = dto.getHmeTimeProcessPdaVO2().getMaterialCount();
            materialId = dto.getHmeTimeProcessPdaVO2().getMaterialDataList().get(0).getMaterialId();
            materialDataList.addAll(dto.getHmeTimeProcessPdaVO2().getMaterialDataList());
            flag = dto.getHmeTimeProcessPdaVO2().getFlag();
        }
        String operationId = dto.getOperationIdList().get(0);
        //??????????????????????????????????????????
        MtMaterialLot mtMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getScanBarcode());
        if (mtMaterialLot != null) {
            //2020-08-27 10:07 add by chaonan.hu for zhenyong.ban
            //???????????????hme_eo_job_sn???????????????????????????????????????+????????????JOB_TYPE????????????????????????????????????????????????
            List<HmeEoJobSn> hmeEoJobSnList = hmeTimeProcessPdaMapper.eoJobSnQuery(tenantId, mtMaterialLot.getMaterialLotId());
            for (HmeEoJobSn hmeEoJobSn:hmeEoJobSnList) {
                if(!operationId.equals(hmeEoJobSn.getOperationId()) || !"TIME_PROCESS_PDA".equals(hmeEoJobSn.getJobType())){
                    throw new MtException("HME_TIME_PROCESS_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TIME_PROCESS_0010", "HME"));
                }
            }

            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
            //????????????
            check(tenantId, mtMaterialLot, materialId);

            //2020-12-01 add by chaonan.hu for zhenyong.ban ??????????????????????????????
            this.operationVerify(tenantId, mtMaterialLot.getMaterialLotId(), operationId);

            //??????????????????
            barcodeCount++;
            result.setBarcodeCount(barcodeCount);
            materialCount = materialCount.add(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
            result.setMaterialCount(materialCount);
            HmeTimeProcessPdaVO3 hmeTimeProcessPdaVO3 = new HmeTimeProcessPdaVO3();
            hmeTimeProcessPdaVO3.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeTimeProcessPdaVO3.setMaterialId(mtMaterialLot.getMaterialId());
            hmeTimeProcessPdaVO3.setMaterialCode(mtMaterial.getMaterialCode());
            hmeTimeProcessPdaVO3.setMaterialName(mtMaterial.getMaterialName());
            hmeTimeProcessPdaVO3.setBarcode(dto.getScanBarcode());
            hmeTimeProcessPdaVO3.setPrimaryUomQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
            hmeTimeProcessPdaVO3.setPrimaryUomId(mtMaterialLot.getPrimaryUomId());
            MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot.getPrimaryUomId());
            hmeTimeProcessPdaVO3.setUomCode(mtUom.getUomCode());
            List<MtExtendAttrVO> attrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                setMaterialLotId(mtMaterialLot.getMaterialLotId());
                setAttrName("COS_RECORD");
            }});
            if (CollectionUtils.isEmpty(attrVOS)) {
                throw new MtException("HME_TIME_PROCESS_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TIME_PROCESS_0009", "HME"));
            }
            hmeTimeProcessPdaVO3.setSourceJobId(attrVOS.get(0).getAttrValue());
            result.setUomCode(mtUom.getUomCode());
            //????????????????????????????????????????????????????????????????????????
            List<Date> siteIndDateList = hmeTimeProcessPdaMapper.siteInDateQuery(tenantId, mtMaterialLot.getMaterialLotId(), operationId);
            if (CollectionUtils.isNotEmpty(siteIndDateList)) {
                hmeTimeProcessPdaVO3.setSiteInDate(siteIndDateList.get(0));
            }
            materialDataList.add(hmeTimeProcessPdaVO3);
            result.setMaterialDataList(materialDataList);
            //??????????????????
            List<String> jobIdList = hmeTimeProcessPdaMapper.siteOutDateQuery(tenantId, mtMaterialLot.getMaterialLotId(), operationId);
            if (CollectionUtils.isNotEmpty(jobIdList)) {
                int currentFlag = 2;
                //???????????????????????????????????????????????????????????????????????????
                if (flag != 0 && flag != currentFlag) {
                    throw new MtException("HME_TIME_PROCESS_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TIME_PROCESS_0008", "HME", "??????", "??????"));
                } else {
                    result.setFlag(currentFlag);
                }
                List<HmeEoJobEquipment> hmeEoJobEquipments = hmeEoJobEquipmentRepository.select(new HmeEoJobEquipment() {{
                    setTenantId(tenantId);
                    setJobId(jobIdList.get(0));
                }});
                for (HmeEoJobEquipment hmeEoJobEquipment : hmeEoJobEquipments) {
                    if (!hmeEoJobEquipment.getEquipmentId().equals(hmeEquipment1.getEquipmentId())) {
                        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectByPrimaryKey(hmeEoJobEquipment.getEquipmentId());
                        throw new MtException("HME_TIME_PROCESS_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_TIME_PROCESS_0007", "HME", hmeEquipment.getAssetEncoding()));
                    }
                }
            } else {
                int currentFlag = 1;
                //???????????????????????????????????????????????????????????????????????????
                if (flag != 0) {
                    if (flag != currentFlag) {
                        throw new MtException("HME_TIME_PROCESS_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_TIME_PROCESS_0008", "HME", "??????", "??????"));
                    }else{
                        result.setFlag(currentFlag);
                    }
                } else {
                    result.setFlag(currentFlag);
                }
            }
        } else {
            //??????????????????????????????
            MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
                setTenantId(tenantId);
                setContainerCode(dto.getScanBarcode());
            }});
            if (mtContainer != null) {
                //??????API{containerLimitObjectQuery} ????????????????????????????????????
                List<MtContLoadDtlVO6> mtContLoadDtlVO6s = mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, new MtContLoadDtlVO() {{
                    setContainerId(mtContainer.getContainerId());
                }});
                if(CollectionUtils.isEmpty(mtContLoadDtlVO6s)){
                    throw new MtException("HME_TIME_PROCESS_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TIME_PROCESS_0011", "HME"));
                }
                for (MtContLoadDtlVO6 mtContLoadDtlVO6 : mtContLoadDtlVO6s) {
                    MtMaterialLot mtMaterialLot1 = mtMaterialLotRepository.selectByPrimaryKey(mtContLoadDtlVO6.getLoadObjectId());
                    if (mtMaterialLot1 != null) {
                        //2020-08-27 20:07 add by chaonan.hu for zhenyong.ban
                        //???????????????hme_eo_job_sn???????????????????????????????????????+????????????JOB_TYPE????????????????????????????????????????????????
                        List<HmeEoJobSn> hmeEoJobSnList = hmeTimeProcessPdaMapper.eoJobSnQuery(tenantId, mtMaterialLot1.getMaterialLotId());
                        for (HmeEoJobSn hmeEoJobSn:hmeEoJobSnList) {
                            if(!operationId.equals(hmeEoJobSn.getOperationId()) || !"TIME_PROCESS_PDA".equals(hmeEoJobSn.getJobType())){
                                throw new MtException("HME_TIME_PROCESS_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_TIME_PROCESS_0010", "HME"));
                            }
                        }
                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot1.getMaterialId());
                        //????????????
                        check(tenantId, mtMaterialLot1, materialId);
                        //2020-12-01 add by chaonan.hu for zhenyong.ban ??????????????????????????????
                        this.operationVerify(tenantId, mtMaterialLot1.getMaterialLotId(), operationId);
                        //??????????????????
                        barcodeCount++;
                        result.setBarcodeCount(barcodeCount);
                        materialCount = materialCount.add(new BigDecimal(mtMaterialLot1.getPrimaryUomQty()));
                        result.setMaterialCount(materialCount);
                        HmeTimeProcessPdaVO3 hmeTimeProcessPdaVO3 = new HmeTimeProcessPdaVO3();
                        hmeTimeProcessPdaVO3.setMaterialLotId(mtMaterialLot1.getMaterialLotId());
                        hmeTimeProcessPdaVO3.setMaterialId(mtMaterialLot1.getMaterialId());
                        hmeTimeProcessPdaVO3.setMaterialCode(mtMaterial.getMaterialCode());
                        hmeTimeProcessPdaVO3.setMaterialName(mtMaterial.getMaterialName());
                        hmeTimeProcessPdaVO3.setBarcode(dto.getScanBarcode());
                        hmeTimeProcessPdaVO3.setPrimaryUomQty(new BigDecimal(mtMaterialLot1.getPrimaryUomQty()));
                        hmeTimeProcessPdaVO3.setPrimaryUomId(mtMaterialLot1.getPrimaryUomId());
                        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialLot1.getPrimaryUomId());
                        hmeTimeProcessPdaVO3.setUomCode(mtUom.getUomCode());
                        List<MtExtendAttrVO> attrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                            setMaterialLotId(mtMaterialLot1.getMaterialLotId());
                            setAttrName("COS_RECORD");
                        }});
                        hmeTimeProcessPdaVO3.setSourceJobId(attrVOS.get(0).getAttrValue());
                        result.setUomCode(mtUom.getUomCode());
                        //????????????????????????????????????????????????????????????????????????
                        List<Date> siteIndDateList = hmeTimeProcessPdaMapper.siteInDateQuery(tenantId, mtMaterialLot1.getMaterialLotId(), operationId);
                        if (CollectionUtils.isNotEmpty(siteIndDateList)) {
                            hmeTimeProcessPdaVO3.setSiteInDate(siteIndDateList.get(0));
                        }
                        materialDataList.add(hmeTimeProcessPdaVO3);
                        result.setMaterialDataList(materialDataList);
                        //??????????????????
                        List<String> jobIdList = hmeTimeProcessPdaMapper.siteOutDateQuery(tenantId, mtMaterialLot1.getMaterialLotId(), operationId);
                        if (CollectionUtils.isNotEmpty(jobIdList)) {
                            int currentFlag = 2;
                            //???????????????????????????????????????????????????????????????????????????
                            if (flag != 0 && flag != currentFlag) {
                                throw new MtException("HME_TIME_PROCESS_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_TIME_PROCESS_0008", "HME", "??????", "??????"));
                            } else {
                                result.setFlag(currentFlag);
                            }
                            List<HmeEoJobEquipment> hmeEoJobEquipments = hmeEoJobEquipmentRepository.select(new HmeEoJobEquipment() {{
                                setTenantId(tenantId);
                                setJobId(jobIdList.get(0));
                            }});
                            for (HmeEoJobEquipment hmeEoJobEquipment : hmeEoJobEquipments) {
                                if (!hmeEoJobEquipment.getEquipmentId().equals(hmeEquipment1.getEquipmentId())) {
                                    HmeEquipment hmeEquipment = hmeEquipmentRepository.selectByPrimaryKey(hmeEoJobEquipment.getEquipmentId());
                                    throw new MtException("HME_TIME_PROCESS_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_TIME_PROCESS_0007", "HME", hmeEquipment.getAssetEncoding()));
                                }
                            }
                        } else {
                            int currentFlag = 1;
                            //???????????????????????????????????????????????????????????????????????????
                            if (flag != 0) {
                                if (flag != currentFlag) {
                                    throw new MtException("HME_TIME_PROCESS_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_TIME_PROCESS_0008", "HME", "??????", "??????"));
                                }else{
                                    result.setFlag(currentFlag);
                                }
                            } else {
                                flag = currentFlag;
                                result.setFlag(currentFlag);
                            }
                        }
                    }else{
                        throw new MtException("HME_TIME_PROCESS_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_TIME_PROCESS_0012", "HME"));
                    }
                }
            } else {
                throw new MtException("HME_TIME_PROCESS_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TIME_PROCESS_0003", "HME"));
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeTimeProcessPdaDTO3 siteIn(Long tenantId, HmeTimeProcessPdaDTO3 dto) {
        List<HmeTimeProcessPdaVO3> materialDataList = dto.getMaterialDataList();
        HmeEoJobSn hmeEoJobSn = null;
        HmeEoJobEquipment hmeEoJobEquipment = null;
        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment(){{
            setTenantId(tenantId);
            setAssetEncoding(dto.getEquipmentCode());
        }});
        List<String> eoJobSnList=new ArrayList<>();
        for (HmeTimeProcessPdaVO3 hmeTimeProcessPdaVO3 : materialDataList) {
            //???hme_eo_job_sn??????????????????
            hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(new Date());
            hmeEoJobSn.setShiftId(dto.getWkcShiftId());
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? 1L : curUser.getUserId();
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSn.setOperationId(dto.getOperationIdList().get(0));
            hmeEoJobSn.setSnMaterialId(hmeTimeProcessPdaVO3.getMaterialId());
            hmeEoJobSn.setSnQty(hmeTimeProcessPdaVO3.getPrimaryUomQty());
            //2020/10/02 add by sanfeng.zhang for zhenyong ?????????
            MtExtendSettings reworkAttr = new MtExtendSettings();
            reworkAttr.setAttrName("WORK_ORDER_ID");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", hmeTimeProcessPdaVO3.getMaterialLotId(), Collections.singletonList(reworkAttr));
            if(CollectionUtils.isNotEmpty(mtExtendAttrVOList)){
                hmeEoJobSn.setWorkOrderId(mtExtendAttrVOList.get(0).getAttrValue());
            }
            hmeEoJobSn.setMaterialLotId(hmeTimeProcessPdaVO3.getMaterialLotId());
//            // 2021-03-08 add by sanfeng.zhang for ban.zhenyong Attribute3??????????????????id
//            String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, hmeTimeProcessPdaVO3.getMaterialLotId());
//            hmeEoJobSn.setAttribute3(recentHisId);
            HmeVisualInspectionVO3 hmeVisualInspectionVO3 = hmeVisualInspectionMapper.cosTypeWaferAttrQuery(tenantId, hmeTimeProcessPdaVO3.getMaterialLotId());
            if(Objects.nonNull(hmeVisualInspectionVO3)){
                hmeEoJobSn.setAttribute3(hmeVisualInspectionVO3.getCosType());
                hmeEoJobSn.setAttribute5(hmeVisualInspectionVO3.getWafer());
            }
            hmeEoJobSn.setReworkFlag("N");
            hmeEoJobSn.setJobType("TIME_PROCESS_PDA");
            // 20210310 add by sanfeng.zhang for zhenyong.ban ?????????eo???????????????????????????job_type???????????????eo_step_num + 1
            Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, dto.getWorkcellId(), null, hmeTimeProcessPdaVO3.getMaterialLotId(), HmeConstants.ConstantValue.NO, "TIME_PROCESS_PDA", dto.getOperationIdList().get(0));
            hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
            hmeEoJobSn.setSourceJobId(hmeTimeProcessPdaVO3.getSourceJobId());
            hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
            //???hme_eo_job_equipment???????????????
            //hmeEoJobEquipment = new HmeEoJobEquipment();
            //hmeEoJobEquipment.setTenantId(tenantId);
            //hmeEoJobEquipment.setJobId(hmeEoJobSn.getJobId());
            //hmeEoJobEquipment.setWorkcellId(dto.getWorkcellId());
            //hmeEoJobEquipment.setEquipmentId(hmeEquipment.getEquipmentId());
            //hmeEoJobEquipment.setEquipmentStatus("BLUE");
            //hmeEoJobEquipmentRepository.insertSelective(hmeEoJobEquipment);
            eoJobSnList.add(hmeEoJobSn.getJobId());
        }
        hmeEoJobEquipmentService.bindHmeEoJobEquipmentOfTimeProcess(tenantId,eoJobSnList,dto.getWorkcellId(), hmeEquipment.getEquipmentId());
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeTimeProcessPdaDTO4 siteOut(Long tenantId, HmeTimeProcessPdaDTO4 dto) {
        List<HmeTimeProcessPdaVO3> materialDataList = dto.getMaterialDataList();
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COS_HLKP");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        HmeEoJobSn hmeEoJobSn = null;
        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtCommonExtendVO7> mtCommonExtendVO7List = new ArrayList<>();
        for (HmeTimeProcessPdaVO3 hmeTimeProcessPdaVO3 : materialDataList) {
            List<String> jobIdList = hmeTimeProcessPdaMapper.siteOutDateQuery(tenantId, hmeTimeProcessPdaVO3.getMaterialLotId(), dto.getOperationIdList().get(0));
            for (String jobId : jobIdList) {
                hmeEoJobSn = new HmeEoJobSn();
                hmeEoJobSn.setJobId(jobId);
                CustomUserDetails curUser = DetailsHelper.getUserDetails();
                Long userId = curUser == null ? 1L : curUser.getUserId();
                hmeEoJobSn.setSiteOutBy(userId);
                hmeEoJobSn.setSiteOutDate(new Date());
                hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);
            }
            //2020-12-21 add by chaonan.hu for zhenyong.ban ????????????????????????CURRENT_ROUTER_STEP
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(hmeTimeProcessPdaVO3.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("WORK_ORDER_ID");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotBlank(mtExtendAttrVOS.get(0).getAttrValue())){
                //?????????????????????????????????????????????????????????
                List<HmeCosGetChipPlatformVO> hmeCosGetChipPlatformVOS = hmeCosGetChipPlatformMapper.routerStepAndOperationQuery(tenantId, mtExtendAttrVOS.get(0).getAttrValue());
                if (CollectionUtils.isEmpty(hmeCosGetChipPlatformVOS)) {
                    throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_028", "HME"));
                }
                //??????????????????????????????????????????????????????????????????????????????
                List<HmeCosGetChipPlatformVO> afterFilterList = hmeCosGetChipPlatformVOS.stream().filter(item -> dto.getOperationIdList().get(0).equals(item.getOperationId())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(afterFilterList)){
                    throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_028", "HME"));
                }
                String currentRouterStep = afterFilterList.get(0).getRouterStepId();
                //2021-04-22 19:34 edit by chaonan.hu for kang.wang ??????????????????????????? ???????????????????????????
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(hmeTimeProcessPdaVO3.getMaterialLotId());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);
//                //????????????????????? ???????????????????????????
//                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//                mtMaterialLotVO2.setEventId(eventId);
//                mtMaterialLotVO2.setMaterialLotId(hmeTimeProcessPdaVO3.getMaterialLotId());
//                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                ////2021-04-22 19:34 edit by chaonan.hu for kang.wang ???????????????????????????????????????
                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(hmeTimeProcessPdaVO3.getMaterialLotId());
                List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
                MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                mtCommonExtendVO4.setAttrName("CURRENT_ROUTER_STEP");
                mtCommonExtendVO4.setAttrValue(currentRouterStep);
                mtCommonExtendVO4List.add(mtCommonExtendVO4);
                mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
                mtCommonExtendVO7List.add(mtCommonExtendVO7);
//                //??????????????????
//                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
//                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//                mtExtendVO5.setAttrName("CURRENT_ROUTER_STEP");
//                mtExtendVO5.setAttrValue(currentRouterStep);
//                mtExtendVO5List.add(mtExtendVO5);
//                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
//                    setEventId(eventId);
//                    setKeyId(hmeTimeProcessPdaVO3.getMaterialLotId());
//                    setAttrs(mtExtendVO5List);
//                }});
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
        return dto;
    }

    @Override
    public HmeTimeProcessPdaVO4 defectEquipmentQuery(Long tenantId, HmeTimeProcessPdaDTO5 dto) {
        return hmeTimeProcessPdaMapper.defectEquipmentQuery(tenantId, dto);
    }

    @Override
    public void operationVerify(Long tenantId, String materialLotId, String operationId) {
        //?????????????????????????????????Id
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(materialLotId);
        mtMaterialLotAttrVO2.setAttrName("WORK_ORDER_ID");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())){
            //?????????????????????????????????????????????????????????
            List<HmeCosGetChipPlatformVO> hmeCosGetChipPlatformVOS = hmeCosGetChipPlatformMapper.routerStepAndOperationQuery(tenantId, mtExtendAttrVOS.get(0).getAttrValue());
            if (CollectionUtils.isEmpty(hmeCosGetChipPlatformVOS)) {
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_028", "HME"));
            }
            //??????????????????????????????????????????????????????????????????????????????
            List<HmeCosGetChipPlatformVO> afterFilterList = hmeCosGetChipPlatformVOS.stream().filter(item -> operationId.equals(item.getOperationId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(afterFilterList)){
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_028", "HME"));
            }
            //???????????????????????????????????????????????????
            List<MtRouterNextStep> mtRouterNextStepList = mtRouterNextStepRepository.select(new MtRouterNextStep() {{
                setTenantId(tenantId);
                setNextStepId(afterFilterList.get(0).getRouterStepId());
            }});
            if(CollectionUtils.isEmpty(mtRouterNextStepList)){
                //???????????????????????????????????????{?????????????????????????????????????????????}
                throw new MtException("HME_COS_030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_030", "HME"));
            }else if(mtRouterNextStepList.size() > 1){
                //??????????????????????????????????????????{??????????????????????????????????????????,?????????}
                throw new MtException("HME_COS_032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_032", "HME"));
            }
            //???????????????????????????CURRENT_ROUTER_STEP
            mtMaterialLotAttrVO2.setAttrName("CURRENT_ROUTER_STEP");
            mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if(CollectionUtils.isEmpty(mtExtendAttrVOS) || StringUtils.isEmpty(mtExtendAttrVOS.get(0).getAttrValue())){
                //????????????????????????{??????????????????????????????????????????????????????}
                throw new MtException("HME_COS_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_033", "HME"));
            }
            if(!mtExtendAttrVOS.get(0).getAttrValue().equals(afterFilterList.get(0).getRouterStepId()) &&
                    !mtExtendAttrVOS.get(0).getAttrValue().equals(mtRouterNextStepList.get(0).getRouterStepId())){
                //??????????????????CURRENT_ROUTER_STEP?????????????????????????????????????????????????????????????????????????????????{??????????????????????????????????????????????????????}
                throw new MtException("HME_COS_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_033", "HME"));
            }
        }
    }

    void check(Long tenantId, MtMaterialLot mtMaterialLot, String materialId) {
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
        //????????????
        if (!YES.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }
        if (!OK.equals(mtMaterialLot.getQualityStatus())) {
            throw new MtException("HME_TIME_PROCESS_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0005", "HME", mtMaterialLot.getQualityStatus()));
        }
        if (StringUtils.isNotEmpty(mtMaterialLot.getCurrentContainerId())) {
            MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(mtMaterialLot.getCurrentContainerId());
            throw new MtException("HME_TIME_PROCESS_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0006", "HME", mtContainer.getContainerCode()));
        }
        if (StringUtils.isNotEmpty(materialId) && !mtMaterialLot.getMaterialId().equals(materialId)) {
            throw new MtException("HME_TIME_PROCESS_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0004", "HME", mtMaterial.getMaterialCode()));
        }
        // 2021-03-08 add by sanfeng.zhang ??????/?????? ??????
        if (YES.equals(mtMaterialLot.getFreezeFlag()) || YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", mtMaterial.getMaterialCode()));
        }
    }
}
