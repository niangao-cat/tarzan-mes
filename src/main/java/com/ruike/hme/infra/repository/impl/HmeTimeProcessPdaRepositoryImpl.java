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
        //根据设备编码获取设备ID
        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(dto.getEquipmentCode());
        }});
        if (hmeEquipment == null) {
            throw new MtException("HME_TIME_PROCESS_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0001", "HME"));
        }
        //校验设备是否绑定在当前工位
        List<HmeEquipmentWkcRel> hmeEquipmentWkcRels = hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
            setTenantId(tenantId);
            setStationId(dto.getWorkcellId());
            setEquipmentId(hmeEquipment.getEquipmentId());
        }});
        if (CollectionUtils.isEmpty(hmeEquipmentWkcRels)) {
            throw new MtException("HME_TIME_PROCESS_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_PROCESS_0002", "HME"));
        }
        //返回结果
        HmeTimeProcessPdaVO result = new HmeTimeProcessPdaVO();
        result.setEquipmentId(hmeEquipment.getEquipmentId());
        result.setEquipmentCode(hmeEquipment.getAssetEncoding());
        result.setEquipmentName(hmeEquipment.getAssetName());
        result.setEquipmentStatus("BLUE");
        //判断设备是否已点检完成，如果已完成，则更新颜色为绿色
        //设备当天所有点检任务是否完成标识
        Boolean completeFlag = false;
        //获取当前班次
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
            //获取设备当天所有点检任务
            List<HmeEqManageTaskDoc> hmeEqManageTaskDocs = new ArrayList<>();
            //第一种情况 按班
            HmeEqManageTaskDoc hmeEqManageTaskDoc = new HmeEqManageTaskDoc();
            hmeEqManageTaskDoc.setTenantId(tenantId);
            hmeEqManageTaskDoc.setEquipmentId(hmeEquipment.getEquipmentId());
            hmeEqManageTaskDoc.setDocType("C");
            hmeEqManageTaskDoc.setTaskCycle("0.5");
            hmeEqManageTaskDoc.setShiftDate(shiftDate);
            hmeEqManageTaskDoc.setShiftCode(shiftCode);
            hmeEqManageTaskDocs.addAll(hmeEqManageTaskDocRepository.select(hmeEqManageTaskDoc));
            //第二种情况 非按班
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
        //先将扫描条码当做实物条码处理
        MtMaterialLot mtMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, dto.getScanBarcode());
        if (mtMaterialLot != null) {
            //2020-08-27 10:07 add by chaonan.hu for zhenyong.ban
            //根据条码在hme_eo_job_sn表中查询，校验是否存在工艺+加工类型JOB_TYPE与当前不一致且出站时间为空的数据
            List<HmeEoJobSn> hmeEoJobSnList = hmeTimeProcessPdaMapper.eoJobSnQuery(tenantId, mtMaterialLot.getMaterialLotId());
            for (HmeEoJobSn hmeEoJobSn:hmeEoJobSnList) {
                if(!operationId.equals(hmeEoJobSn.getOperationId()) || !"TIME_PROCESS_PDA".equals(hmeEoJobSn.getJobType())){
                    throw new MtException("HME_TIME_PROCESS_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TIME_PROCESS_0010", "HME"));
                }
            }

            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
            //数据校验
            check(tenantId, mtMaterialLot, materialId);

            //2020-12-01 add by chaonan.hu for zhenyong.ban 增加对上一步骤的校验
            this.operationVerify(tenantId, mtMaterialLot.getMaterialLotId(), operationId);

            //封装返回对象
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
            //查询这条数据是否已进站，若已进站，则显示进站日期
            List<Date> siteIndDateList = hmeTimeProcessPdaMapper.siteInDateQuery(tenantId, mtMaterialLot.getMaterialLotId(), operationId);
            if (CollectionUtils.isNotEmpty(siteIndDateList)) {
                hmeTimeProcessPdaVO3.setSiteInDate(siteIndDateList.get(0));
            }
            materialDataList.add(hmeTimeProcessPdaVO3);
            result.setMaterialDataList(materialDataList);
            //进站点击标识
            List<String> jobIdList = hmeTimeProcessPdaMapper.siteOutDateQuery(tenantId, mtMaterialLot.getMaterialLotId(), operationId);
            if (CollectionUtils.isNotEmpty(jobIdList)) {
                int currentFlag = 2;
                //当此次扫描条码进出站状态与上次状态不一致时，则报错
                if (flag != 0 && flag != currentFlag) {
                    throw new MtException("HME_TIME_PROCESS_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_TIME_PROCESS_0008", "HME", "出站", "进站"));
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
                //当此次扫描条码进出站状态与上次状态不一致时，则报错
                if (flag != 0) {
                    if (flag != currentFlag) {
                        throw new MtException("HME_TIME_PROCESS_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_TIME_PROCESS_0008", "HME", "进站", "出站"));
                    }else{
                        result.setFlag(currentFlag);
                    }
                } else {
                    result.setFlag(currentFlag);
                }
            }
        } else {
            //否则当做容器条码处理
            MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
                setTenantId(tenantId);
                setContainerCode(dto.getScanBarcode());
            }});
            if (mtContainer != null) {
                //调用API{containerLimitObjectQuery} 获取指定容器下的装载对象
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
                        //根据条码在hme_eo_job_sn表中查询，校验是否存在工艺+加工类型JOB_TYPE与当前不一致且出站时间为空的数据
                        List<HmeEoJobSn> hmeEoJobSnList = hmeTimeProcessPdaMapper.eoJobSnQuery(tenantId, mtMaterialLot1.getMaterialLotId());
                        for (HmeEoJobSn hmeEoJobSn:hmeEoJobSnList) {
                            if(!operationId.equals(hmeEoJobSn.getOperationId()) || !"TIME_PROCESS_PDA".equals(hmeEoJobSn.getJobType())){
                                throw new MtException("HME_TIME_PROCESS_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_TIME_PROCESS_0010", "HME"));
                            }
                        }
                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot1.getMaterialId());
                        //数据校验
                        check(tenantId, mtMaterialLot1, materialId);
                        //2020-12-01 add by chaonan.hu for zhenyong.ban 增加对上一步骤的校验
                        this.operationVerify(tenantId, mtMaterialLot1.getMaterialLotId(), operationId);
                        //封装返回对象
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
                        //查询这条数据是否已进站，若已进站，则显示进站日期
                        List<Date> siteIndDateList = hmeTimeProcessPdaMapper.siteInDateQuery(tenantId, mtMaterialLot1.getMaterialLotId(), operationId);
                        if (CollectionUtils.isNotEmpty(siteIndDateList)) {
                            hmeTimeProcessPdaVO3.setSiteInDate(siteIndDateList.get(0));
                        }
                        materialDataList.add(hmeTimeProcessPdaVO3);
                        result.setMaterialDataList(materialDataList);
                        //进站点击标识
                        List<String> jobIdList = hmeTimeProcessPdaMapper.siteOutDateQuery(tenantId, mtMaterialLot1.getMaterialLotId(), operationId);
                        if (CollectionUtils.isNotEmpty(jobIdList)) {
                            int currentFlag = 2;
                            //当此次扫描条码进出站状态与上次状态不一致时，则报错
                            if (flag != 0 && flag != currentFlag) {
                                throw new MtException("HME_TIME_PROCESS_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_TIME_PROCESS_0008", "HME", "出站", "进站"));
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
                            //当此次扫描条码进出站状态与上次状态不一致时，则报错
                            if (flag != 0) {
                                if (flag != currentFlag) {
                                    throw new MtException("HME_TIME_PROCESS_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_TIME_PROCESS_0008", "HME", "进站", "出站"));
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
            //往hme_eo_job_sn表中插入数据
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
            //2020/10/02 add by sanfeng.zhang for zhenyong 工单号
            MtExtendSettings reworkAttr = new MtExtendSettings();
            reworkAttr.setAttrName("WORK_ORDER_ID");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", hmeTimeProcessPdaVO3.getMaterialLotId(), Collections.singletonList(reworkAttr));
            if(CollectionUtils.isNotEmpty(mtExtendAttrVOList)){
                hmeEoJobSn.setWorkOrderId(mtExtendAttrVOList.get(0).getAttrValue());
            }
            hmeEoJobSn.setMaterialLotId(hmeTimeProcessPdaVO3.getMaterialLotId());
//            // 2021-03-08 add by sanfeng.zhang for ban.zhenyong Attribute3写入条码历史id
//            String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, hmeTimeProcessPdaVO3.getMaterialLotId());
//            hmeEoJobSn.setAttribute3(recentHisId);
            HmeVisualInspectionVO3 hmeVisualInspectionVO3 = hmeVisualInspectionMapper.cosTypeWaferAttrQuery(tenantId, hmeTimeProcessPdaVO3.getMaterialLotId());
            if(Objects.nonNull(hmeVisualInspectionVO3)){
                hmeEoJobSn.setAttribute3(hmeVisualInspectionVO3.getCosType());
                hmeEoJobSn.setAttribute5(hmeVisualInspectionVO3.getWafer());
            }
            hmeEoJobSn.setReworkFlag("N");
            hmeEoJobSn.setJobType("TIME_PROCESS_PDA");
            // 20210310 add by sanfeng.zhang for zhenyong.ban 工位、eo、条码、返修标识、job_type取出最大的eo_step_num + 1
            Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, dto.getWorkcellId(), null, hmeTimeProcessPdaVO3.getMaterialLotId(), HmeConstants.ConstantValue.NO, "TIME_PROCESS_PDA", dto.getOperationIdList().get(0));
            hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
            hmeEoJobSn.setSourceJobId(hmeTimeProcessPdaVO3.getSourceJobId());
            hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
            //往hme_eo_job_equipment表插入数据
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
            //2020-12-21 add by chaonan.hu for zhenyong.ban 更新条码扩展属性CURRENT_ROUTER_STEP
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(hmeTimeProcessPdaVO3.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("WORK_ORDER_ID");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotBlank(mtExtendAttrVOS.get(0).getAttrValue())){
                //根据工单查询到工步及工艺之间的关联关系
                List<HmeCosGetChipPlatformVO> hmeCosGetChipPlatformVOS = hmeCosGetChipPlatformMapper.routerStepAndOperationQuery(tenantId, mtExtendAttrVOS.get(0).getAttrValue());
                if (CollectionUtils.isEmpty(hmeCosGetChipPlatformVOS)) {
                    throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_028", "HME"));
                }
                //根据当前登录工位对应的工艺在关联关系中查找到对应工步
                List<HmeCosGetChipPlatformVO> afterFilterList = hmeCosGetChipPlatformVOS.stream().filter(item -> dto.getOperationIdList().get(0).equals(item.getOperationId())).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(afterFilterList)){
                    throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_028", "HME"));
                }
                String currentRouterStep = afterFilterList.get(0).getRouterStepId();
                //2021-04-22 19:34 edit by chaonan.hu for kang.wang 物料批更新改为批量 只为了新增一条历史
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(hmeTimeProcessPdaVO3.getMaterialLotId());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);
//                //更新物料批主表 只为了新增一条历史
//                MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
//                mtMaterialLotVO2.setEventId(eventId);
//                mtMaterialLotVO2.setMaterialLotId(hmeTimeProcessPdaVO3.getMaterialLotId());
//                mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                ////2021-04-22 19:34 edit by chaonan.hu for kang.wang 物料批扩展属性更新改为批量
                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(hmeTimeProcessPdaVO3.getMaterialLotId());
                List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
                MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                mtCommonExtendVO4.setAttrName("CURRENT_ROUTER_STEP");
                mtCommonExtendVO4.setAttrValue(currentRouterStep);
                mtCommonExtendVO4List.add(mtCommonExtendVO4);
                mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
                mtCommonExtendVO7List.add(mtCommonExtendVO7);
//                //更新扩展属性
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

        //批量更新物料批
        if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, mtMaterialLotVO20List, eventId, HmeConstants.ConstantValue.NO);
        }
        //批量更新物料批扩展属性
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
        //获取条码的扩展属性工单Id
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(materialLotId);
        mtMaterialLotAttrVO2.setAttrName("WORK_ORDER_ID");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && StringUtils.isNotEmpty(mtExtendAttrVOS.get(0).getAttrValue())){
            //根据工单查询到工步及工艺之间的关联关系
            List<HmeCosGetChipPlatformVO> hmeCosGetChipPlatformVOS = hmeCosGetChipPlatformMapper.routerStepAndOperationQuery(tenantId, mtExtendAttrVOS.get(0).getAttrValue());
            if (CollectionUtils.isEmpty(hmeCosGetChipPlatformVOS)) {
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_028", "HME"));
            }
            //根据当前登录工位对应的工艺在关联关系中查找到对应工步
            List<HmeCosGetChipPlatformVO> afterFilterList = hmeCosGetChipPlatformVOS.stream().filter(item -> operationId.equals(item.getOperationId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(afterFilterList)){
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_028", "HME"));
            }
            //根据查找到的对应工步查询其上一步骤
            List<MtRouterNextStep> mtRouterNextStepList = mtRouterNextStepRepository.select(new MtRouterNextStep() {{
                setTenantId(tenantId);
                setNextStepId(afterFilterList.get(0).getRouterStepId());
            }});
            if(CollectionUtils.isEmpty(mtRouterNextStepList)){
                //如果找不到上一步骤，则报错{当前工位工艺工序未维护上一工序}
                throw new MtException("HME_COS_030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_030", "HME"));
            }else if(mtRouterNextStepList.size() > 1){
                //如果找到多个上一步骤，则报错{该工单工艺获取到多个上一步骤,请确认}
                throw new MtException("HME_COS_032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_032", "HME"));
            }
            //获取条码的扩展属性CURRENT_ROUTER_STEP
            mtMaterialLotAttrVO2.setAttrName("CURRENT_ROUTER_STEP");
            mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if(CollectionUtils.isEmpty(mtExtendAttrVOS) || StringUtils.isEmpty(mtExtendAttrVOS.get(0).getAttrValue())){
                //如果找不到，报错{当前工序的上一工序未完工，不允许进站}
                throw new MtException("HME_COS_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_033", "HME"));
            }
            if(!mtExtendAttrVOS.get(0).getAttrValue().equals(afterFilterList.get(0).getRouterStepId()) &&
                    !mtExtendAttrVOS.get(0).getAttrValue().equals(mtRouterNextStepList.get(0).getRouterStepId())){
                //如果扩展属性CURRENT_ROUTER_STEP即不等于登录工位工艺对应的工步，又不等于上一步骤，报错{当前工序的上一工序未完工，不允许进站}
                throw new MtException("HME_COS_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_033", "HME"));
            }
        }
    }

    void check(Long tenantId, MtMaterialLot mtMaterialLot, String materialId) {
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
        //数据校验
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
        // 2021-03-08 add by sanfeng.zhang 盘点/冻结 校验
        if (YES.equals(mtMaterialLot.getFreezeFlag()) || YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", mtMaterial.getMaterialCode()));
        }
    }
}
