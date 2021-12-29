package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO5;
import com.ruike.hme.app.service.HmeEoJobSnBatchService;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.app.service.HmeEoJobSnSingleInService;
import com.ruike.hme.app.service.HmeWkcEoRelService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.HmeEoJobSnUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.plugin.hr.EmployeeHelper;
import org.hzero.boot.platform.plugin.hr.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtEoRouterActualRepository;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.repository.MtEoStepWipRepository;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoRouterActualMapper;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContainerVO25;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.repository.MtRouterDoneStepRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HmeEoJobSnSingleInServiceImpl implements HmeEoJobSnSingleInService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtUserClient userClient;
    @Autowired
    private HmeEoJobSnBatchService batchSnService;
    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;
    @Autowired
    private MtEoRouterActualMapper mtEoRouterActualMapper;
    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;
    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;
    @Autowired
    private HmeMaterialLotLabCodeMapper hmeMaterialLotLabCodeMapper;
    @Autowired
    private HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private HmeEoRouterBomRelRepository hmeEoRouterBomRelRepository;
    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;

    @Autowired
    private HmeFacYkMapper hmeFacYkMapper;

    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HmeWkcEoRelService hmeWkcEoRelService;

    @Autowired
    private HmeSnLabCodeMapper hmeSnLabCodeMapper;

    @Autowired
    private HmeOperationAssignMapper hmeOperationAssignMapper;

    @Autowired
    private HmeEmployeeAssignMapper hmeEmployeeAssignMapper;

    @Autowired
    private HmeQualificationRepository hmeQualificationRepository;
    @Autowired
    private HmeTagFormulaHeadMapper hmeTagFormulaHeadMapper;
    @Autowired
    private HmeDataRecordExtendRepository hmeDataRecordExtendRepository;
    @Autowired
    private HmeDataRecordExtendMapper hmeDataRecordExtendMapper;
    @Autowired
    private HmeTagCheckRepository hmeTagCheckRepository;

    @Autowired
    private HmeRepairLimitCountRepository repairLimitCountRepository;

    @Autowired
    private HmeRepairRecordRepository repairRecordRepository;

    @Autowired
    private HmeRepairRecordMapper repairRecordMapper;


    private HmeEoJobSnSingleVO inSiteScanValidate(Long tenantId, HmeEoJobSnVO3 dto) {
        // 进站条码不能为空
        if (StringUtils.isBlank(dto.getSnNum())) {
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (CollectionUtils.isEmpty(dto.getOperationIdList())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺ID"));
        } else if (dto.getOperationIdList().size() > 1){
            //当前工位对应多个工艺,请检查
            throw new MtException("HME_EO_JOB_SN_150", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_150", "HME"));
        }
        dto.setOperationId(dto.getOperationIdList().get(0));

        HmeEoJobSnSingleVO resultVO=new HmeEoJobSnSingleVO();
        resultVO.setIsClickInspectionBtn(HmeConstants.ConstantValue.NO);

        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getSnNum());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        if(Objects.isNull(mtMaterialLot) || StringUtils.isBlank(mtMaterialLot.getMaterialLotId())){
            //扫描条码为空,请确认
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        if(!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
            //当前条码无效, 请确认
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }
        //V20210703 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可进站
        if(StringUtils.isNotBlank(mtMaterialLot.getStocktakeFlag()) &&
                HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
            //此SN【${1}】正在盘点,不可进站
            throw new MtException("HME_EO_JOB_SN_205", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_205", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // 20210721 modify by sanfeng.zhang for xenxin.zhang 出站校验冻结标识为Y的SN不可进站
        if (HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())) {
            //【此SN【${1}】正在冻结,不可进站】
            throw new MtException("HME_EO_JOB_SN_208", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_208", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        //V20211013 modify by penglin.sui for 增加SN必须是在制品的校验
        //查询条码扩展属性
        List<String> materialLotIdList = new ArrayList<>();
        materialLotIdList.add(mtMaterialLot.getMaterialLotId());
        List<String> materialLotAttrNameList = new ArrayList<>();
        materialLotAttrNameList.add("MF_FLAG");
        List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_material_lot_attr","MATERIAL_LOT_ID"
                ,materialLotIdList,materialLotAttrNameList);
        Map<String,String> materialLotExtendAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)){
            materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName,MtExtendAttrVO1::getAttrValue));
        }
        //在制品校验
        if(!HmeConstants.ConstantValue.YES.equals(materialLotExtendAttrMap.getOrDefault("MF_FLAG",""))){
            //条码[${1}]不为在制品,不允许进站!
            throw new MtException("HME_EO_JOB_SN_253", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_253", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        dto.setMaterialLotId(mtMaterialLot.getMaterialLotId());

        // 20210908 add by sanfeng.zhang for hui.gu 判断SN在该工序是否已拦截
        hmeEoJobSnCommonService.interceptValidate(tenantId, dto.getWorkcellId(), Collections.singletonList(mtMaterialLot));

        //校验员工能否操作该物料
        //工艺需要的资质
        if(CollectionUtils.isNotEmpty(dto.getOperationIdList())) {
            List<HmeQualification> hmeOperationAssignList =
                    hmeOperationAssignMapper.batchQueryData(tenantId, dto.getOperationIdList());
            List<String> oprQualityIdList = hmeOperationAssignList.stream().map(HmeQualification::getQualityId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(oprQualityIdList)) {
                Map<String, String> hmeOperationAssignMap = hmeOperationAssignList.stream().collect(Collectors.toMap(HmeQualification::getQualityId, HmeQualification::getQualityName));
                // 当前用户的资质
                CustomUserDetails curUser = DetailsHelper.getUserDetails();
                Long userId = curUser == null ? -1L : curUser.getUserId();
                Employee employee = EmployeeHelper.getEmployee(userId, tenantId);
                List<HmeEmployeeAssign> hmeEmployeeAssignList = hmeEmployeeAssignMapper.queryDataOptional(tenantId,
                        String.valueOf(employee.getEmployeeId()), oprQualityIdList, mtMaterialLot.getMaterialId());
                List<String> empQualityIdList = hmeEmployeeAssignList.stream().map(HmeEmployeeAssign::getQualityId).collect(Collectors.toList());
                if (empQualityIdList.size() != oprQualityIdList.size()
                        || !empQualityIdList.containsAll(oprQualityIdList)) {
                    oprQualityIdList.removeAll(empQualityIdList);
                    if (CollectionUtils.isNotEmpty(oprQualityIdList)) {
                        //**资质缺少操作该物料的权限
                        throw new MtException("HME_EO_JOB_SN_203", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "HME_EO_JOB_SN_203", "HME", hmeOperationAssignMap.get(oprQualityIdList.get(0))));
                    }
                }
            }
        }

        //V20210525 modify by penglin.sui for peng.zhao 进站校验工位绑定设备
        hmeEoJobSnCommonService.workcellBindEquipmentValidate(tenantId, dto.getOperationId() , dto.getWorkcellId());

        //查询条码相关信息
        HmeEoJobSnVO5 snVO = hmeEoJobSnMapper.queryMaterialLotInfoForSingle(tenantId, mtMaterialLot.getMaterialLotId(), dto.getSiteId());
        if(Objects.isNull(snVO) || StringUtils.isBlank(snVO.getMaterialLotId())){
            //所扫描条码不存在或不为有效状态,请核实!
            throw new MtException("HME_NC_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0009", "HME"));
        }
        String eoId = snVO.getEoId();

        // 20210818 add by sanfeng.zhang for peng.zhao 查询器件测试标识
        String operationDeviceFlag = hmeEoJobSnMapper.queryOperationDeviceFlag(tenantId,  dto.getOperationId());
        snVO.setTestFlag(operationDeviceFlag);

        // 20210825 add by sanfeng.zhang for peng.zhao 交叉复测按钮显示 器件测试 返修 判定交叉复测为空 则显示
        snVO.setIsShowCrossRetestBtn(HmeConstants.ConstantValue.NO);
        if (HmeConstants.ConstantValue.YES.equals(operationDeviceFlag) &&
            HmeConstants.ConstantValue.YES.equals(snVO.getReworkFlag()) &&
            "".equals(snVO.getCrossRetestFlag())) {
            snVO.setIsShowCrossRetestBtn(HmeConstants.ConstantValue.YES);
        }

        List<String> eoIdList = new ArrayList<>();
        eoIdList.add(eoId);

        //批量查询工单类型
        Map<String, HmeEoJobSnBatchVO3> workOrderTypeMap = hmeEoJobSnCommonService.batchQtyAfterSalesWorkOrder(tenantId, eoIdList);

        HmeEoJobSnBatchVO3 workOrderType = workOrderTypeMap.getOrDefault(eoId, null);

        //V20210315 modify by penglin.sui for tianyang.xie 返修工单、翻新改造工单不能进单件工序作业平台
        if(Objects.nonNull(workOrderType)) {
            List<LovValueDTO> notInSingleWoTypeLovValueDTOList = lovAdapter.queryLovValue("HME.NOT_IN_SINGLE_WO_TYPE", tenantId);
            if (CollectionUtils.isNotEmpty(notInSingleWoTypeLovValueDTOList)) {
                List<LovValueDTO> woTypeLovs2 = notInSingleWoTypeLovValueDTOList.stream().filter(item -> item.getValue().equals(workOrderType.getWorkOrderType()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(woTypeLovs2)) {
                    // 工单类型为【${1}】,不允许在单件工序作业平台进站,请检查!
                    throw new MtException("HME_EO_JOB_SN_190", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_190", "HME", workOrderType.getWorkOrderType()));
                }
            }
        }

        //V20210226 modify by penglin.sui for jian.zhang 新增全检/抽检逻辑
        MtOperation mtOperation = mtOperationRepository.operationGet(tenantId, dto.getOperationIdList().get(0));
        List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("QMS.OQC_0002", tenantId);
        if(CollectionUtils.isNotEmpty(lovValueDTOList)){
            List<LovValueDTO> woTypeLovs2 = lovValueDTOList.stream().filter(item -> item.getValue().equals(mtOperation.getOperationName()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(woTypeLovs2)){
                resultVO.setIsClickInspectionBtn(HmeConstants.ConstantValue.YES);
            }
        }

        resultVO.setIsContainer(false);
        resultVO.setIsQuery(HmeConstants.ConstantValue.NO);
        resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.YES);
        resultVO.setProhibitClickContinueReworkFlag(HmeConstants.ConstantValue.NO);

        //是否返修进站
        boolean isRework = HmeConstants.ConstantValue.YES.equals(snVO.getReworkFlag());

        //是否指定工艺路线返修
        boolean isDesignedRework = isRework && HmeConstants.ConstantValue.YES.equals(snVO.getDesignedReworkFlag());
        resultVO.setIsDesignedRework(isDesignedRework);

        //如果是指定工艺路线返修 先查询指定的关系数据
        if (isDesignedRework) {
            HmeEoRouterBomRelVO eoRouterBomRel = hmeEoRouterBomRelRepository.queryLastRelByEoId(tenantId, eoId);
            if (Objects.isNull(eoRouterBomRel) || StringUtils.isBlank(eoRouterBomRel.getEoRouterBomRelId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "EO 工艺路线 装配清单关系"));
            }
            resultVO.setEoRouterBomRel(eoRouterBomRel);
        }

        //重复进站校验
        List<HmeEoJobSnVO23> hmeEoJobSns = hmeEoJobSnMapper.selectEoJobSnForInSite(tenantId, dto.getOperationId(), eoId, isRework);

        //如果不为空校验是否当前工位平台进站
        if (CollectionUtils.isNotEmpty(hmeEoJobSns)) {
            long count = 0;
            //返修进站时不允许多工艺同时进站
            if (isRework) {
                count = hmeEoJobSns.stream().filter(item -> !dto.getOperationId().equals(item.getOperationId())).count();
                if (count > 0) {
                    List<HmeEoJobSnVO23> exitEoJobSn = hmeEoJobSns.stream()
                            .filter(item -> !dto.getOperationId().equals(item.getOperationId())).collect(Collectors.toList());
                    MtRouterStep routerStep = mtRouterStepRepository.routerStepGet(tenantId, exitEoJobSn.get(0).getEoStepId());
                    //	当前SN已在${1}工序下返修进站且未出站,不允许重复进站!
                    throw new MtException("HME_EO_JOB_SN_151", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_151", "HME", routerStep.getStepName()));
                }
            }

            //校验工位是否一致
            count = hmeEoJobSns.stream().filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).count();
            if (count > 0) {
                List<HmeEoJobSnVO23> exitEoJobSn = hmeEoJobSns.stream()
                        .filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, exitEoJobSn.get(0).getWorkcellId());
                //当前SN已在${1}进站,不允许在此工位重复进站
                throw new MtException("HME_EO_JOB_SN_144", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_144", "HME", mtModWorkcell.getWorkcellName()));
            }

            //校验平台是否一致
            count = hmeEoJobSns.stream().filter(item -> !dto.getJobType().equals(item.getJobType())).count();
            if (count > 0) {
                String jobTypeDesc = "";
                List<HmeEoJobSnVO23> exitEoJobSn = hmeEoJobSns.stream()
                        .filter(item -> !dto.getJobType().equals(item.getJobType())).collect(Collectors.toList());
                switch(exitEoJobSn.get(0).getJobType()){
                    case HmeConstants.JobType.BATCH_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.BATCH_PROCESS;
                        break;
                    case HmeConstants.JobType.TIME_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.TIME_PROCESS;
                        break;
                    case HmeConstants.JobType.PREPARE_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.PREPARE_PROCESS;
                        break;
                    case HmeConstants.JobType.COS_COMPLETED:
                        jobTypeDesc = HmeConstants.JobTypeDesc.COS_COMPLETED;
                        break;
                    case HmeConstants.JobType.PACKAGE_PROCESS_PDA:
                        jobTypeDesc = HmeConstants.JobTypeDesc.PACKAGE_PROCESS_PDA;
                        break;
                    case HmeConstants.JobType.REPAIR_PROCESS:
                        jobTypeDesc = HmeConstants.JobTypeDesc.REPAIR_PROCESS;
                        break;
                    default:
                        break;
                }

                //当前SN已在${1}进站,不允许在此平台重复进站
                throw new MtException("HME_EO_JOB_SN_143", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_143", "HME", jobTypeDesc));
            }

            //如果是返修则要校验不良记录是否存在
            if (isRework) {
                HmeNcRecordVO hmeNcRecordVO = hmeEoJobSnMapper.selectLastNcRecordProcessForSingle(tenantId, eoId, dto.getWorkcellId());
                if (isDesignedRework) {
                    if (Objects.nonNull(hmeNcRecordVO)) {
                        resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
                        resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
                    }
                } else {
                    if (Objects.isNull(hmeNcRecordVO) || StringUtils.isBlank(hmeNcRecordVO.getNcRecordId())) {
                        //${1}不存在 请确认${2}
                        throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "ncRecord", dto.getSnNum()));
                    }
                    resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
                    resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
                    if (!StringUtils.equals(hmeNcRecordVO.getRootCauseProcessId(), hmeNcRecordVO.getCurrentProcessId())) {
                        resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.NO);
                    }
                }

                //如果是指定工艺路线末道序不允许点继续返修 add by yuchao.wang for tianyang.xie at 2021.2.4
                if (isDesignedRework){
                    String doneStepFlag = mtRouterDoneStepRepository.doneStepValidate(tenantId, hmeEoJobSns.get(0).getEoStepId());
                    if (HmeConstants.ConstantValue.YES.equals(doneStepFlag)) {
                        resultVO.setProhibitClickContinueReworkFlag(HmeConstants.ConstantValue.YES);
                    }
                }
            }

            //SN已经进站，走查询逻辑
            resultVO.setSnVO(snVO);
            resultVO.setIsQuery(HmeConstants.ConstantValue.YES);
            resultVO.setExitEoJobSn(hmeEoJobSns.get(0));
            resultVO.setIsContainer(false);
            resultVO.setIsRework(isRework);
            return resultVO;
        }

        // 验证EO状态
        if (!HmeConstants.EoStatus.WORKING.equals(snVO.getStatus())
                || (!HmeConstants.EoStatus.RELEASED.equals(snVO.getLastEoStatus())
                && !HmeConstants.EoStatus.HOLD.equals(snVO.getLastEoStatus()))) {
            //EO状态必须为运行状态
            throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
        }

        //验证质量状态
        if (!isRework && !HmeConstants.ConstantValue.OK.equals(snVO.getQualityStatus())) {
            // SN已判定不良，请判定处置方法后执行工序作业
            throw new MtException("HME_EO_JOB_SN_029", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_029", "HME"));
        }

        //获取EO下所有的正常加工工艺步骤
        List<HmeRouterStepVO> normalStepList = hmeEoJobSnMapper.batchSelectNormalStepByEoIds(tenantId, eoIdList);
        if (CollectionUtils.isEmpty(normalStepList)) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "最近正常加工步骤"));
        }

        //获取当前步骤
        HmeRouterStepVO5 currentStep = new HmeRouterStepVO5();
        if (isDesignedRework) {
            HmeEoRouterBomRelVO eoRouterBomRel = resultVO.getEoRouterBomRel();
            currentStep = hmeEoJobSnCommonService.queryCurrentAndNextStepForDesignedRework(tenantId, dto.getOperationId(), eoRouterBomRel.getRouterId(), eoId);
            currentStep.setEoId(eoId);

            //防跳站校验
            HmeRouterStepVO5 previousStep = hmeEoJobSnCommonService.queryPreviousStepForDesignedRework(tenantId, eoId, eoRouterBomRel.getRouterId(), eoRouterBomRel.getCreationDate());

            //找到上一步骤才进行校验，没找到说明是首次进站
            if (Objects.nonNull(previousStep) && StringUtils.isNotBlank(previousStep.getRouterStepId())) {
                if (StringUtils.isBlank(previousStep.getNextStepId())) {
                    //找不到步骤${1}的下一步骤,请检查!
                    throw new MtException("HME_EO_JOB_SN_181", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_181", "HME", previousStep.getStepName()));
                }
                if (!currentStep.getRouterStepId().equals(previousStep.getNextStepId())) {
                    //本道工序步骤不为工序步骤【${1}】的下一步骤
                    throw new MtException("HME_EO_JOB_SN_032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_032", "HME", previousStep.getStepName()));
                }
            }

            //如果是指定工艺路线末道序不允许点继续返修 add by yuchao.wang for tianyang.xie at 2021.2.4
            String doneStepFlag = mtRouterDoneStepRepository.doneStepValidate(tenantId, currentStep.getRouterStepId());
            if (HmeConstants.ConstantValue.YES.equals(doneStepFlag)) {
                resultVO.setProhibitClickContinueReworkFlag(HmeConstants.ConstantValue.YES);
            }
        } else {
            currentStep = hmeEoJobSnCommonService.queryCurrentAndNextStepByEoAndOperation(tenantId, eoId, dto.getOperationId());

            //V20210320 modify by penglin.sui for hui.ma 若当前步骤未找到实验代码，则查询SN工艺试验代码表
            if(StringUtils.isBlank(currentStep.getLabCode()) || StringUtils.isBlank(currentStep.getRouterStepRemark())){
                HmeSnLabCode hmeSnLabCodePara = new HmeSnLabCode();
                hmeSnLabCodePara.setTenantId(tenantId);
                hmeSnLabCodePara.setOperationId(dto.getOperationId());
                hmeSnLabCodePara.setMaterialLotId(dto.getMaterialLotId());
                hmeSnLabCodePara.setEnabledFlag(HmeConstants.ConstantValue.YES);
                HmeSnLabCode hmeSnLabCode = hmeSnLabCodeMapper.selectOne(hmeSnLabCodePara);
                if(Objects.nonNull(hmeSnLabCode)){
                    currentStep.setLabCode(StringUtils.isBlank(currentStep.getLabCode()) ? hmeSnLabCode.getLabCode() : currentStep.getLabCode());
                    currentStep.setRouterStepRemark(StringUtils.isBlank(currentStep.getRouterStepRemark()) ? hmeSnLabCode.getRemark() : currentStep.getRouterStepRemark());
                }
            }
        }

        //如果是返修则要校验不良记录是否存在
        if (isRework) {
            HmeNcRecordVO hmeNcRecordVO = hmeEoJobSnMapper.selectLastNcRecordProcessForSingle(tenantId, eoId, dto.getWorkcellId());
            if (isDesignedRework) {
                if (Objects.nonNull(hmeNcRecordVO)) {
                    resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
                    resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
                }
            } else {
                if (Objects.isNull(hmeNcRecordVO) || StringUtils.isBlank(hmeNcRecordVO.getNcRecordId())) {
                    //${1}不存在 请确认${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "ncRecord", dto.getSnNum()));
                }
                resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
                resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
                if (!StringUtils.equals(hmeNcRecordVO.getRootCauseProcessId(), hmeNcRecordVO.getCurrentProcessId())) {
                    resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.NO);
                }
            }

            //非指定工艺路线返修 判断当前步骤是否在正常步骤内
            if (!isDesignedRework) {
                String currentStepRouterStepId = currentStep.getRouterStepId();
                Optional<HmeRouterStepVO> routerStepVOOptional = normalStepList.stream()
                        .filter(item -> item.getRouterStepId().equals(currentStepRouterStepId)).findFirst();
                if (!routerStepVOOptional.isPresent()) {
                    // 请在正常加工工序步骤{正常加工步骤}前进行返修加工
                    String stepName = hmeEoJobSnMapper.queryNormalStepNameForRework(tenantId, eoId);
                    throw new MtException("HME_EO_JOB_SN_036", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_036", "HME", stepName));
                }
            }
        }

        // 如果是返修则要校验是否限制 SN 在工序、物料下的返修次数 若限制 则校验是否达到返修限制次数
        if (isRework) {
            HmeRepairLimitCount queryLimit = new HmeRepairLimitCount();
            queryLimit.setTenantId(tenantId);
            queryLimit.setMaterialId(mtMaterialLot.getMaterialId());
            queryLimit.setWorkcellId(dto.getProcessId());
            queryLimit.setEnableFlag(HmeConstants.ConstantValue.YES);
            HmeRepairLimitCount repairLimit = repairLimitCountRepository.selectOne(queryLimit);
            resultVO.setRepairLimitCount(repairLimit);
            if (Objects.nonNull(repairLimit)) {
                if (Objects.isNull(repairLimit.getLimitCount()) || repairLimit.getLimitCount() <=0L ) {
                    throw new MtException("HME_EO_JOB_SN_247", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_247", "HME", dto.getProcessId(), mtMaterialLot.getMaterialId() ));
                }
            }
            //查询返修记录
            HmeRepairRecord repairRecordParam = new HmeRepairRecord();
            repairRecordParam.setTenantId(tenantId);
            repairRecordParam.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            repairRecordParam.setMaterialId(mtMaterialLot.getMaterialId());
            repairRecordParam.setWorkcellId(dto.getProcessId());
            HmeRepairRecord repairRecord = repairRecordRepository.selectOne(repairRecordParam);
            resultVO.setRepairRecord(repairRecord);
            if (Objects.nonNull(repairRecord)) {
                // SN 达到返修次数后被 禁止返修
                if ("STOP".equals(repairRecord.getStatus()) ){
                    throw new MtException("HME_EO_JOB_SN_246", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_246", "HME"));
                }
                // SN 达到返修次数后 不允许进站 等待工艺判定
                if ( "WAITING".equals(repairRecord.getStatus())) {
                    throw new MtException("HME_EO_JOB_SN_245", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_245", "HME"));
                }
            }

        }

        //批量查询最近一步工艺步骤
        Map<String, HmeRouterStepVO> nearStepMap = hmeEoJobSnCommonService.batchQueryRouterStepByEoIds(tenantId, HmeConstants.StepType.NEAR_STEP, eoIdList);

        //按照顺序倒序排序
        normalStepList = normalStepList.stream()
                .sorted(Comparator.comparing(HmeRouterStepVO::getSequence, Comparator.reverseOrder())
                        .thenComparing(HmeRouterStepVO::getCreationDate, Comparator.reverseOrder())).collect(Collectors.toList());
        HmeRouterStepVO normalStep = normalStepList.get(0);
        //批量查询下一步骤
        String nextStepId = hmeEoJobSnCommonService.queryNextStep(tenantId, normalStep.getRouterStepId());

        //查询售后工单类型LOV
        List<LovValueDTO> woTypeLovs = lovAdapter.queryLovValue("HME.NOT_CHECK_JUMP_WOTYPE", tenantId);
        List<String> lovValueList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(woTypeLovs)) {
            lovValueList = woTypeLovs.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }

        //非入口步骤 并且是非返修的
        if(!HmeConstants.ConstantValue.YES.equals(currentStep.getEntryStepFlag()) && !isRework) {
            //判断是否有最近最近步骤
            HmeRouterStepVO nearStepVO = nearStepMap.get(eoId);
            if(Objects.isNull(nearStepVO)){
                // 当前为非入口步骤, 且根据EO没有找到最近加工步骤
                throw new MtException("HME_EO_JOB_SN_046",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_046", "HME"));
            }

            boolean checkFlag = true;
            if(Objects.nonNull(workOrderType) && CollectionUtils.isNotEmpty(lovValueList)){
                if(lovValueList.contains(workOrderType.getWorkOrderType())){
                    checkFlag = false;
                }
            }
            if(checkFlag && nearStepVO.getCompletedQty().compareTo(BigDecimal.ZERO) == 0){
                // 请先完成工序步骤【${1}】的出站操作, 再执行本道工序步骤进站
                throw new MtException("HME_EO_JOB_SN_031",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_031", "HME",
                                nearStepVO.getStepName()));
            }

            if (!currentStep.getRouterStepId().equals(nextStepId)) {
                // 本道工序步骤不为工序步骤{最近加工步骤描述}的下一步骤
                throw new MtException("HME_EO_JOB_SN_032",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_032", "HME",
                                normalStep.getStepName()));
            }
        }

        //构造返回数据
        dto.setEoId(eoId);
        HmeEoJobSnVO3 snLine = new HmeEoJobSnVO3();
        BeanUtils.copyProperties(dto, snLine);
        snLine.setEoStepId(currentStep.getRouterStepId());
        snLine.setOperationId(currentStep.getOperationId());
        snLine.setReworkFlag(isRework ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO);
        resultVO.setSnLine(snLine);
        resultVO.setSnVO(snVO);
        resultVO.setIsContainer(false);
        resultVO.setNearStep(nearStepMap.getOrDefault(eoId, new HmeRouterStepVO()));
        resultVO.setCurrentStep(currentStep);
        resultVO.setNormalStepList(normalStepList);
        resultVO.setIsRework(isRework);
        return resultVO;
    }


    /**
     *
     * @Description 工序作业-单个进站扫描-非返修
     *
     * @author jianmin.hong
     * @date 2020/11/23 16:32
     * @param tenantId 租户ID
     * @param dto 参数
     * @param singleVO 参数
     * @return HmeEoJobSnVO
     *
     */
    private HmeEoJobSnVO normalInSiteScan(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO singleVO) {
        //构造对象
        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        resultVO.setIsClickInspectionBtn(singleVO.getIsClickInspectionBtn());
        HmeEoJobSnVO5 snVO = singleVO.getSnVO();
        HmeRouterStepVO5 currentStep = singleVO.getCurrentStep();
        HmeEoJobSnVO3 snLine = singleVO.getSnLine();

        snLine.setSiteId(snVO.getSiteId());
        snLine.setWorkOrderId(snVO.getWorkOrderId());
        snLine.setMaterialId(snVO.getMaterialId());
        snLine.setEoStepNum(HmeConstants.ConstantValue.ONE);
        Long startDate = System.currentTimeMillis();
        //非首道序执行工艺步骤移入
        if(!HmeConstants.ConstantValue.YES.equals(currentStep.getEntryStepFlag())) {
            HmeRouterStepVO nearStepVO = singleVO.getNearStep();
            MtEoRouterActualVO15 eoRouterActual = new MtEoRouterActualVO15();
            eoRouterActual.setRouterStepId(snLine.getEoStepId());
            eoRouterActual.setEoId(snLine.getEoId());
            eoRouterActual.setQty(snVO.getEoQty().doubleValue());
            eoRouterActual.setPreviousStepId(nearStepVO.getEoStepActualId());
            eoRouterActual.setWorkcellId(nearStepVO.getWorkcellId());
            eoRouterActual.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
            startDate = System.currentTimeMillis();
            mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, eoRouterActual);
            log.info("=================================>单件作业-进站非返修正常进站非首道序执行工艺步骤移入总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        //工序排队加工
        eoWorking(tenantId, snVO.getEoQty(), snLine);

        //容器卸载
        if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
            snLine.setSourceContainerId(snVO.getCurrentContainerId());
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(snVO.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(snVO.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            startDate = System.currentTimeMillis();
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
            log.info("=================================>单件作业-进站非返修正常进站非首道序执行容器卸载总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        snLine.setLabCode(currentStep.getLabCode());
        startDate = System.currentTimeMillis();
        HmeEoJobSnVO2 currentJobSnVO = batchSnService.createSnJob(tenantId, snLine);
        log.info("=================================>单件作业-进站非返修正常进站createSnJob总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");

        // 创建设备数据
        HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        equSwitchParams.setJobId(currentJobSnVO.getJobId());
        equSwitchParams.setWorkcellId(dto.getWorkcellId());
        equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
        startDate = System.currentTimeMillis();
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        log.info("=================================>单件作业-进站非返修正常进站创建设备数据总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //V20210220 modify by penglin.sui for tianyang.xie 保存工位EO关系
        HmeWkcEoRel hmeWkcEoRel = new HmeWkcEoRel();
        hmeWkcEoRel.setWkcId(dto.getWorkcellId());
        hmeWkcEoRel.setOperationId(dto.getOperationId());
        hmeWkcEoRel.setEoId(currentJobSnVO.getEoId());
        hmeWkcEoRel.setJobId(currentJobSnVO.getJobId());
        startDate = System.currentTimeMillis();
        hmeWkcEoRelService.saveWkcEoRel(tenantId , hmeWkcEoRel);
        log.info("=================================>单件作业-进站非返修正常进站保存工位EO关系总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        // 如存在工艺步骤，需获取步骤名称
        resultVO.setCurrentStepName(currentStep.getStepName());
        resultVO.setCurrentStepDescription(currentStep.getDescription());
        resultVO.setCurrentStepSequence(currentStep.getSequence());
        resultVO.setNextStepName(currentStep.getNextStepName());
        resultVO.setNextStepDescription(currentStep.getNextDescription());

        resultVO.setLabCode(currentStep.getLabCode());
        resultVO.setRouterStepRemark(currentStep.getRouterStepRemark());

        //V20210125 modify by penglin.sui for hui.ma 新增实验代码记录
        if(StringUtils.isNotBlank(resultVO.getLabCode())) {
            startDate = System.currentTimeMillis();
            HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
            hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
            hmeMaterialLotLabCodePara.setRouterStepId(currentJobSnVO.getEoStepId());
            hmeMaterialLotLabCodePara.setLabCode(resultVO.getLabCode());
            HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
            if (Objects.isNull(hmeMaterialLotLabCode)) {
                HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                insertHmeMaterialLotLabCode.setTenantId(tenantId);
                insertHmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                insertHmeMaterialLotLabCode.setLabCode(currentStep.getLabCode());
                insertHmeMaterialLotLabCode.setJobId(currentJobSnVO.getJobId());
                insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                insertHmeMaterialLotLabCode.setWorkOrderId(snVO.getWorkOrderId());
                insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                insertHmeMaterialLotLabCode.setRouterStepId(currentJobSnVO.getEoStepId());
                insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                hmeMaterialLotLabCodeRepository.insertSelective(insertHmeMaterialLotLabCode);
            } else {
                if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                    HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                    updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    hmeMaterialLotLabCodeMapper.updateByPrimaryKeySelective(updateHmeMaterialLotLabCode);
                }
            }
            log.info("=================================>单件作业-进站非返修正常进站新增实验代码记录总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        //数据采集项
        if (CollectionUtils.isNotEmpty(currentJobSnVO.getDataRecordVOList())) {
            resultVO.setDataRecordVOList(currentJobSnVO.getDataRecordVOList());
        }

        // 班次
        if (currentJobSnVO.getShiftId() != null) {
            MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, currentJobSnVO.getShiftId());
            resultVO.setShiftId(currentJobSnVO.getShiftId());
            resultVO.setShiftCode(mtWkcShift.getShiftCode());
        }

        //工位信息
        if (StringUtils.isBlank(dto.getWorkcellCode())) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
            resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
        } else {
            resultVO.setWorkcellCode(dto.getWorkcellCode());
            resultVO.setWorkcellName(dto.getWorkcellName());
        }

        // 进出站相关
        resultVO.setOperationId(currentJobSnVO.getOperationId());
        resultVO.setEoStepId(currentJobSnVO.getEoStepId());
        resultVO.setEoStepNum(currentJobSnVO.getEoStepNum());
        resultVO.setJobId(currentJobSnVO.getJobId());
        resultVO.setEoId(currentJobSnVO.getEoId());

        resultVO.setSiteInBy(currentJobSnVO.getSiteInBy());
        // 操作员
        String userRealName = userClient.userInfoGet(tenantId, currentJobSnVO.getSiteInBy()).getRealName();
        resultVO.setSiteInByName(userRealName);
        resultVO.setSiteInDate(currentJobSnVO.getSiteInDate());
        resultVO.setSiteOutDate(currentJobSnVO.getSiteOutDate());
        resultVO.setSourceContainerId(currentJobSnVO.getSourceContainerId());

        //V20201208 modify by penglin.sui 返回BOM信息
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(currentJobSnVO.getEoId());
        List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId,eoIdList);
        if(CollectionUtils.isNotEmpty(hmeEoBomVOList)){
            resultVO.setBomId(hmeEoBomVOList.get(0).getBomId());
            resultVO.setBomName(hmeEoBomVOList.get(0).getBomName());
        }

        // 执行作业相关
        resultVO.setWorkcellId(dto.getWorkcellId());
        resultVO.setMaterialLotId(dto.getMaterialLotId());
        resultVO.setWorkOrderId(snVO.getWorkOrderId());
        resultVO.setWorkOrderNum(snVO.getWorkOrderNum());
        resultVO.setSnMaterialId(snVO.getMaterialId());

        //V20210217 modify by penglin.sui for tianyang.xie 更新Y宽数据项的上下限
        updateDataRecordYK(tenantId,dto , resultVO);

        resultVO.setSnMaterialCode(snVO.getMaterialCode());
        resultVO.setSnMaterialName(snVO.getMaterialName());
        resultVO.setWoQuantity(snVO.getWoQty());
        resultVO.setWoQuantityOut(snVO.getCompletedQty());
        resultVO.setQualityStatus(snVO.getQualityStatus());
        resultVO.setQualityStatusMeaning(snVO.getQualityStatusMeaning());
        resultVO.setRemark(snVO.getRemark());
        resultVO.setProductionVersion(snVO.getWoProductionVersion());
        resultVO.setIsContainer(singleVO.getIsContainer());
        resultVO.setReworkFlag(HmeConstants.ConstantValue.NO);
        resultVO.setDesignedReworkFlag(HmeConstants.ConstantValue.NO);
        resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.YES);
        resultVO.setProhibitClickContinueReworkFlag(singleVO.getProhibitClickContinueReworkFlag());
        resultVO.setNcRecordWorkcellCode(singleVO.getNcRecordWorkcellCode());
        resultVO.setNcRecordWorkcellName(singleVO.getNcRecordWorkcellName());
        resultVO.setSnNum(dto.getSnNum());
        resultVO.setProdLineCode(dto.getProdLineCode());
        resultVO.setTestFlag(snVO.getTestFlag());
        resultVO.setIsShowCrossRetestBtn(snVO.getIsShowCrossRetestBtn());
        return resultVO;
    }

    /**
     *
     * @Description 工序作业-单个进站扫描-返修
     *
     * @author jianmin.hong
     * @date 2020/11/23 16:32
     * @param tenantId 租户ID
     * @param dto 参数
     * @param singleVO 参数
     * @return HmeEoJobSnVO
     *
     */
    private HmeEoJobSnVO reworkInSiteScan(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO singleVO) {
        //构造对象
        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        resultVO.setIsClickInspectionBtn(singleVO.getIsClickInspectionBtn());
        HmeEoJobSnVO3 snLine = singleVO.getSnLine();
        HmeRouterStepVO normalNearStep = singleVO.getNormalStepList().get(0);
        HmeRouterStepVO nearStepVO = singleVO.getNearStep();
        HmeEoJobSnVO5 snVO = singleVO.getSnVO();

        snLine.setSiteId(snVO.getSiteId());
        snLine.setWorkOrderId(snVO.getWorkOrderId());
        snLine.setMaterialId(snVO.getMaterialId());
        snLine.setEoStepNum(HmeConstants.ConstantValue.ONE);

        //执行工艺步骤移入
        MtEoRouterActualVO15 eoRouterActual = new MtEoRouterActualVO15();
        eoRouterActual.setRouterStepId(snLine.getEoStepId());
        eoRouterActual.setEoId(snLine.getEoId());
        eoRouterActual.setQty(snVO.getEoQty().doubleValue());
        eoRouterActual.setPreviousStepId(nearStepVO.getEoStepActualId());
        eoRouterActual.setWorkcellId(nearStepVO.getWorkcellId());
        eoRouterActual.setReworkStepFlag(HmeConstants.ConstantValue.YES);
        eoRouterActual.setSourceEoStepActualId(normalNearStep.getEoStepActualId());
        eoRouterActual.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
        long startDate = System.currentTimeMillis();
        mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, eoRouterActual);
        log.info("=================================>单件作业-进站返修进站eoNextStepMoveInProcess总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //工序排队加工
        startDate = System.currentTimeMillis();
        eoWorking(tenantId, snVO.getEoQty(), snLine);
        log.info("=================================>单件作业-进站返修进站工序排队加工总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //容器卸载
        if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
            snLine.setSourceContainerId(snVO.getCurrentContainerId());
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(snVO.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(snVO.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            startDate = System.currentTimeMillis();
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
            log.info("=================================>单件作业-进站返修进站容器卸载总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        //创建作业
        startDate = System.currentTimeMillis();
        HmeEoJobSnVO2 currentJobSnVO = batchSnService.createSnJob(tenantId, snLine);
        log.info("=================================>单件作业-进站返修进站createSnJob总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        // 创建设备数据
        HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        equSwitchParams.setJobId(currentJobSnVO.getJobId());
        equSwitchParams.setWorkcellId(dto.getWorkcellId());
        equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
        startDate = System.currentTimeMillis();
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
        log.info("=================================>单件作业-进站返修进站创建设备数据总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //V20210220 modify by penglin.sui for tianyang.xie 保存工位EO关系
        HmeWkcEoRel hmeWkcEoRel = new HmeWkcEoRel();
        hmeWkcEoRel.setWkcId(dto.getWorkcellId());
        hmeWkcEoRel.setOperationId(dto.getOperationId());
        hmeWkcEoRel.setEoId(currentJobSnVO.getEoId());
        hmeWkcEoRel.setJobId(currentJobSnVO.getJobId());
        startDate = System.currentTimeMillis();
        hmeWkcEoRelService.saveWkcEoRel(tenantId , hmeWkcEoRel);
        log.info("=================================>单件作业-进站返修进站保存工位EO关系总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");

        //返修 更新 SN 在工序、 物料 下的返修次数
        if(Objects.isNull(singleVO.getRepairRecord())) {
            //新增记录
            HmeRepairRecord repairRecord = new HmeRepairRecord();
            repairRecord.setTenantId(tenantId);
            repairRecord.setMaterialLotId(snLine.getMaterialLotId());
            repairRecord.setMaterialId(snLine.getMaterialId());
            repairRecord.setWorkcellId(dto.getProcessId());
            repairRecord.setRepairCount(1L);
            repairRecord.setStatus("UNDEFINED");
            HmeRepairLimitCount repairLimit = singleVO.getRepairLimitCount();
            if (!Objects.isNull(repairLimit)) {
                //限制次数
                repairRecord.setLimitCount(repairLimit.getLimitCount());
                // 放行次数 初始值为 0
                repairRecord.setPassCount(0L);
                repairRecord.setPermitCount(repairLimit.getLimitCount());
                //允许返修的总次数 初始值为 返修限制次数
                repairRecord.setPermitRepairCount(repairLimit.getLimitCount());
                if (repairRecord.getRepairCount() >= repairLimit.getLimitCount()) {
                    repairRecord.setStatus("WAITING");
                }
            }
            repairRecordRepository.insertSelective(repairRecord);
        } else {
            //更新
            HmeRepairRecord repairRecord = singleVO.getRepairRecord();
            HmeRepairLimitCount repairLimit = singleVO.getRepairLimitCount();
            Long repairCount = 1L;
            if (!Objects.isNull(repairRecord.getRepairCount())) {
                repairCount = repairRecord.getRepairCount()+1;
            }
            repairRecord.setRepairCount(repairCount);
            // 判断进站次数是否达到返修 限制的进站次数 若是，更新状态
            if (!Objects.isNull(repairRecord.getPermitRepairCount()) && !Objects.isNull(repairLimit)) {
                if (repairCount >= repairRecord.getPermitRepairCount()) {
                    repairRecord.setStatus("WAITING");
                }
            }
            repairRecordMapper.updateByPrimaryKeySelective(repairRecord);
        }

        // 如存在工艺步骤，需获取步骤名称
        HmeRouterStepVO5 currentStep = singleVO.getCurrentStep();
        if(Objects.nonNull(currentStep)) {
            resultVO.setCurrentStepName(currentStep.getStepName());
            resultVO.setCurrentStepDescription(currentStep.getDescription());
            resultVO.setCurrentStepSequence(currentStep.getSequence());
            resultVO.setNextStepName(currentStep.getNextStepName());
            resultVO.setNextStepDescription(currentStep.getNextDescription());
            resultVO.setLabCode(currentStep.getLabCode());
            resultVO.setRouterStepRemark(currentStep.getRouterStepRemark());

            //V20210125 modify by penglin.sui for hui.ma 新增实验代码记录
            if(StringUtils.isNotBlank(resultVO.getLabCode())) {
                HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
                hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
                hmeMaterialLotLabCodePara.setRouterStepId(currentJobSnVO.getEoStepId());
                hmeMaterialLotLabCodePara.setLabCode(resultVO.getLabCode());
                startDate = System.currentTimeMillis();
                HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
                log.info("=================================>单件作业-进站返修进站selectLabCode2总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
                if (Objects.isNull(hmeMaterialLotLabCode)) {
                    HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    insertHmeMaterialLotLabCode.setTenantId(tenantId);
                    insertHmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                    insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                    insertHmeMaterialLotLabCode.setLabCode(currentStep.getLabCode());
                    insertHmeMaterialLotLabCode.setJobId(currentJobSnVO.getJobId());
                    insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                    insertHmeMaterialLotLabCode.setWorkOrderId(snVO.getWorkOrderId());
                    insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                    insertHmeMaterialLotLabCode.setRouterStepId(currentJobSnVO.getEoStepId());
                    insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    hmeMaterialLotLabCodeRepository.insertSelective(insertHmeMaterialLotLabCode);
                } else {
                    if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                        HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                        updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                        updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                        hmeMaterialLotLabCodeMapper.updateByPrimaryKeySelective(updateHmeMaterialLotLabCode);
                    }
                }
            }
        }

        //数据采集项
        if (CollectionUtils.isNotEmpty(currentJobSnVO.getDataRecordVOList())) {
            resultVO.setDataRecordVOList(currentJobSnVO.getDataRecordVOList());
        }

        // 班次
        if (currentJobSnVO.getShiftId() != null) {
            MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, currentJobSnVO.getShiftId());
            resultVO.setShiftId(currentJobSnVO.getShiftId());
            resultVO.setShiftCode(mtWkcShift.getShiftCode());
        }

        //工位信息
        if (StringUtils.isBlank(dto.getWorkcellCode())) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
            resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
        } else {
            resultVO.setWorkcellCode(dto.getWorkcellCode());
            resultVO.setWorkcellName(dto.getWorkcellName());
        }

        // 进出站相关
        resultVO.setOperationId(currentJobSnVO.getOperationId());
        resultVO.setEoStepId(currentJobSnVO.getEoStepId());
        resultVO.setEoStepNum(currentJobSnVO.getEoStepNum());
        resultVO.setJobId(currentJobSnVO.getJobId());
        resultVO.setEoId(currentJobSnVO.getEoId());
        resultVO.setSiteInBy(currentJobSnVO.getSiteInBy());
        // 操作员
        String userRealName = userClient.userInfoGet(tenantId, currentJobSnVO.getSiteInBy()).getRealName();
        resultVO.setSiteInByName(userRealName);
        resultVO.setSiteInDate(currentJobSnVO.getSiteInDate());
        resultVO.setSiteOutDate(currentJobSnVO.getSiteOutDate());
        resultVO.setSourceContainerId(currentJobSnVO.getSourceContainerId());

        //V20201208 modify by penglin.sui 返回BOM信息
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(currentJobSnVO.getEoId());
        List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId,eoIdList);
        if(CollectionUtils.isNotEmpty(hmeEoBomVOList)){
            resultVO.setBomId(hmeEoBomVOList.get(0).getBomId());
            resultVO.setBomName(hmeEoBomVOList.get(0).getBomName());
        }

        // 执行作业相关
        resultVO.setWorkcellId(dto.getWorkcellId());
        resultVO.setMaterialLotId(dto.getMaterialLotId());
        resultVO.setWorkOrderId(snVO.getWorkOrderId());
        resultVO.setWorkOrderNum(snVO.getWorkOrderNum());
        resultVO.setSnMaterialId(snVO.getMaterialId());

        //V20210217 modify by penglin.sui for tianyang.xie 更新Y宽数据项的上下限
        updateDataRecordYK(tenantId,dto,resultVO);

        resultVO.setSnMaterialCode(snVO.getMaterialCode());
        resultVO.setSnMaterialName(snVO.getMaterialName());
        resultVO.setWoQuantity(snVO.getWoQty());
        resultVO.setWoQuantityOut(snVO.getCompletedQty());
        resultVO.setQualityStatus(snVO.getQualityStatus());
        resultVO.setQualityStatusMeaning(snVO.getQualityStatusMeaning());
        resultVO.setRemark(snVO.getRemark());
        resultVO.setProductionVersion(snVO.getWoProductionVersion());
        resultVO.setIsContainer(singleVO.getIsContainer());
        resultVO.setReworkFlag(HmeConstants.ConstantValue.YES);
        resultVO.setDesignedReworkFlag(HmeConstants.ConstantValue.NO);
        resultVO.setIsClickProcessComplete(singleVO.getIsClickProcessComplete());
        resultVO.setProhibitClickContinueReworkFlag(singleVO.getProhibitClickContinueReworkFlag());
        resultVO.setNcRecordWorkcellCode(singleVO.getNcRecordWorkcellCode());
        resultVO.setNcRecordWorkcellName(singleVO.getNcRecordWorkcellName());
        resultVO.setSnNum(dto.getSnNum());
        resultVO.setProdLineCode(dto.getProdLineCode());
        resultVO.setTestFlag(snVO.getTestFlag());
        resultVO.setIsShowCrossRetestBtn(snVO.getIsShowCrossRetestBtn());
        return  resultVO;
    }

    /**
     *
     * @Description 工序作业-单个进站扫描-指定工艺路线返修
     *
     * @author yuchao.wang
     * @date 2020/12/31 14:00
     * @param tenantId 租户ID
     * @param dto 参数
     * @param singleVO 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO
     *
     */
    private HmeEoJobSnVO designedReworkInSiteScan(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO singleVO) {
        //构造对象
        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        HmeEoJobSnVO3 snLine = singleVO.getSnLine();
        HmeRouterStepVO normalNearStep = singleVO.getNormalStepList().get(0);
        HmeRouterStepVO nearStepVO = singleVO.getNearStep();
        HmeEoJobSnVO5 snVO = singleVO.getSnVO();
        HmeEoRouterBomRelVO eoRouterBomRel = singleVO.getEoRouterBomRel();

        snLine.setSiteId(snVO.getSiteId());
        snLine.setWorkOrderId(snVO.getWorkOrderId());
        snLine.setMaterialId(snVO.getMaterialId());
        snLine.setEoStepNum(HmeConstants.ConstantValue.ONE);
        snLine.setWoProductionVersion(snVO.getWoProductionVersion());
        snLine.setItemType(snVO.getItemType());

        //执行工艺步骤移入
        MtEoRouterActualVO15 eoRouterActual = new MtEoRouterActualVO15();
        eoRouterActual.setRouterStepId(snLine.getEoStepId());
        eoRouterActual.setEoId(snLine.getEoId());
        eoRouterActual.setQty(snVO.getEoQty().doubleValue());
        eoRouterActual.setPreviousStepId(nearStepVO.getEoStepActualId());
        eoRouterActual.setWorkcellId(nearStepVO.getWorkcellId());
        eoRouterActual.setReworkStepFlag(HmeConstants.ConstantValue.YES);
        eoRouterActual.setSourceEoStepActualId(normalNearStep.getEoStepActualId());
        eoRouterActual.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
        mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, eoRouterActual);

        //工序排队加工
        eoWorking(tenantId, snVO.getEoQty(), snLine);

        //容器卸载
        if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
            snLine.setSourceContainerId(snVO.getCurrentContainerId());
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(snVO.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(snVO.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
        }

        //创建作业
        HmeEoJobSnVO2 currentJobSnVO = hmeEoJobSnCommonService.createSnJobForSingle(tenantId, eoRouterBomRel.getBomId(), snLine);

        //更新 SN 在工序、 物料 下的返修次数
        if (Objects.isNull(singleVO.getRepairRecord())) {
            //新增记录
            HmeRepairRecord repairRecord = new HmeRepairRecord();
            repairRecord.setTenantId(tenantId);
            repairRecord.setMaterialLotId(snLine.getMaterialLotId());
            repairRecord.setMaterialId(snLine.getMaterialId());
            repairRecord.setWorkcellId(dto.getProcessId());
            repairRecord.setRepairCount(1L);
            repairRecord.setStatus("UNDEFINED");
            HmeRepairLimitCount repairLimit = singleVO.getRepairLimitCount();
            if (!Objects.isNull(repairLimit)) {
                //限制次数
                repairRecord.setLimitCount(repairLimit.getLimitCount());
                // 放行次数 初始值为 0
                repairRecord.setPassCount(0L);
                repairRecord.setPermitCount(repairLimit.getLimitCount());
                //允许返修的总次数 初始值为 返修限制次数
                repairRecord.setPermitRepairCount(repairLimit.getLimitCount());
                if (repairRecord.getRepairCount() >= repairLimit.getLimitCount()) {
                    repairRecord.setStatus("WAITING");
                }
            }
            repairRecordRepository.insertSelective(repairRecord);
        } else {
            //更新
            HmeRepairRecord repairRecord = singleVO.getRepairRecord();
            HmeRepairLimitCount repairLimit = singleVO.getRepairLimitCount();
            Long repairCount = 1L;
            if (!Objects.isNull(repairRecord.getRepairCount())) {
                repairCount = repairRecord.getRepairCount()+1;
            }
            repairRecord.setRepairCount(repairCount);
            // 判断进站次数是否达到返修 限制的进站次数 若是，更新状态
            if (!Objects.isNull(repairRecord.getPermitRepairCount()) && !Objects.isNull(repairLimit)) {
                if (repairCount >= repairRecord.getPermitRepairCount()) {
                    repairRecord.setStatus("WAITING");
                }
            }
            repairRecordMapper.updateByPrimaryKeySelective(repairRecord);
        }

        // 创建设备数据
        HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        equSwitchParams.setJobId(currentJobSnVO.getJobId());
        equSwitchParams.setWorkcellId(dto.getWorkcellId());
        equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);

        // 如存在工艺步骤，需获取步骤名称
        HmeRouterStepVO5 currentStep = singleVO.getCurrentStep();
        if(Objects.nonNull(currentStep)) {
            resultVO.setCurrentStepName(currentStep.getStepName());
            resultVO.setCurrentStepDescription(currentStep.getDescription());
            resultVO.setCurrentStepSequence(currentStep.getSequence());
            resultVO.setNextStepName(currentStep.getNextStepName());
            resultVO.setNextStepDescription(currentStep.getNextDescription());
        }

        //数据采集项
        if (CollectionUtils.isNotEmpty(currentJobSnVO.getDataRecordVOList())) {
            resultVO.setDataRecordVOList(currentJobSnVO.getDataRecordVOList());
        }

        // 班次
        if (snLine.getWkcShiftId() != null) {
            MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, snLine.getWkcShiftId());
            resultVO.setShiftId(snLine.getWkcShiftId());
            resultVO.setShiftCode(mtWkcShift.getShiftCode());
        }

        //工位信息
        if (StringUtils.isBlank(dto.getWorkcellCode())) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
            resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
        } else {
            resultVO.setWorkcellCode(dto.getWorkcellCode());
            resultVO.setWorkcellName(dto.getWorkcellName());
        }

        // 进出站相关
        resultVO.setOperationId(snLine.getOperationId());
        resultVO.setEoStepId(snLine.getEoStepId());
        resultVO.setEoStepNum(snLine.getEoStepNum());
        resultVO.setSourceContainerId(snLine.getSourceContainerId());
        resultVO.setJobId(currentJobSnVO.getJobId());
        resultVO.setEoId(currentJobSnVO.getEoId());
        resultVO.setSiteInBy(currentJobSnVO.getSiteInBy());
        // 操作员
        String userRealName = userClient.userInfoGet(tenantId, currentJobSnVO.getSiteInBy()).getRealName();
        resultVO.setSiteInByName(userRealName);
        resultVO.setSiteInDate(currentJobSnVO.getSiteInDate());
        resultVO.setSiteOutDate(currentJobSnVO.getSiteOutDate());

        //返回BOM信息
        resultVO.setBomId(eoRouterBomRel.getBomId());
        resultVO.setBomName(eoRouterBomRel.getBomName());

        // 执行作业相关
        resultVO.setWorkcellId(dto.getWorkcellId());
        resultVO.setMaterialLotId(dto.getMaterialLotId());
        resultVO.setWorkOrderId(snVO.getWorkOrderId());
        resultVO.setWorkOrderNum(snVO.getWorkOrderNum());
        resultVO.setSnMaterialId(snVO.getMaterialId());
        resultVO.setSnMaterialCode(snVO.getMaterialCode());
        resultVO.setSnMaterialName(snVO.getMaterialName());
        resultVO.setWoQuantity(snVO.getWoQty());
        resultVO.setWoQuantityOut(snVO.getCompletedQty());
        resultVO.setQualityStatus(snVO.getQualityStatus());
        resultVO.setQualityStatusMeaning(snVO.getQualityStatusMeaning());
        resultVO.setRemark(snVO.getRemark());
        resultVO.setProductionVersion(snVO.getWoProductionVersion());
        resultVO.setIsContainer(singleVO.getIsContainer());
        resultVO.setReworkFlag(HmeConstants.ConstantValue.YES);
        resultVO.setDesignedReworkFlag(HmeConstants.ConstantValue.YES);
        resultVO.setIsClickProcessComplete(singleVO.getIsClickProcessComplete());
        resultVO.setProhibitClickContinueReworkFlag(singleVO.getProhibitClickContinueReworkFlag());
        resultVO.setNcRecordWorkcellCode(singleVO.getNcRecordWorkcellCode());
        resultVO.setNcRecordWorkcellName(singleVO.getNcRecordWorkcellName());
        resultVO.setSnNum(dto.getSnNum());
        resultVO.setNcRecordWorkcellCode(singleVO.getNcRecordWorkcellCode());
        resultVO.setNcRecordWorkcellName(singleVO.getNcRecordWorkcellName());
        resultVO.setProdLineCode(dto.getProdLineCode());
        resultVO.setTestFlag(snVO.getTestFlag());
        resultVO.setIsShowCrossRetestBtn(snVO.getIsShowCrossRetestBtn());
        return  resultVO;
    }

    /**
     *
     * @Description 工序排队加工
     *
     * @author yuchao.wang
     * @date 2020/12/17 10:53
     * @return void
     *
     */
    private void eoWorking(Long tenantId, BigDecimal eoQty, HmeEoJobSnVO3 snLine) {
        //创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_IN");

        // 取当前步骤的步骤实绩
        MtEoRouterActualVO2 routerActualParam = new MtEoRouterActualVO2();
        routerActualParam.setEoId(snLine.getEoId());
        Long startDate = System.currentTimeMillis();
        List<MtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                mtEoRouterActualMapper.eoOperationLimitCurrentRouterStepGet(tenantId, routerActualParam);
        log.info("=================================>单件作业-进站取当前步骤的步骤实绩总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        if (CollectionUtils.isEmpty(eoOperationLimitCurrentRouterList)) {
            throw new MtException("HME_EO_JOB_SN_039",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
        }

        // 多条步骤实绩时，取最新的
        String eoStepActualId;
        if (eoOperationLimitCurrentRouterList.size() > 1) {
            eoStepActualId = eoOperationLimitCurrentRouterList.stream()
                    .max(Comparator.comparing(MtEoRouterActualVO5::getLastUpdateDate)).get()
                    .getEoStepActualId();
        } else {
            eoStepActualId = eoOperationLimitCurrentRouterList.get(0).getEoStepActualId();
        }
        log.debug("当前步骤的步骤实绩:" + eoStepActualId);

        // 工序排队
        MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
        mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO4.setQueueQty(new Double(String.valueOf(eoQty)));
        mtEoStepWipVO4.setWorkcellId(snLine.getWorkcellId());
        startDate = System.currentTimeMillis();
        mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);
        log.info("=================================>单件作业-进站工序排队总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");

        // 工序在制
        MtEoRouterActualVO19 workingParam = new MtEoRouterActualVO19();
        workingParam.setEventRequestId(eventRequestId);
        workingParam.setEoStepActualId(eoStepActualId);
        workingParam.setWorkcellId(snLine.getWorkcellId());
        workingParam.setQty(new Double(String.valueOf(eoQty)));
        workingParam.setSourceStatus("QUEUE");
        workingParam.setLastWorkcellId(snLine.getWorkcellId());
        startDate = System.currentTimeMillis();
        mtEoRouterActualRepository.eoWkcAndStepWorking(tenantId, workingParam);
        log.info("=================================>单件作业-进站工序在制总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
    }

    /**
     *
     * @Description 更新Y宽数据项的上下限
     *
     * @author penglin.sui
     * @date 2021/2/17 10:38
     * @return void
     *
     */
    private void updateDataRecordYK(Long tenantId,HmeEoJobSnVO3 dto , HmeEoJobSnVO hmeEoJobSnVO){
        //根据工艺ID查询工艺名称
        MtOperation mtOperation = mtOperationRepository.selectByPrimaryKey(dto.getOperationId());
        if(Objects.isNull(mtOperation)){
            return;
        }

        //判断当前工艺是否在值集中
        List<LovValueDTO> facOperations = lovAdapter.queryLovValue("HME.FAC_OPERATION", tenantId);
        if(CollectionUtils.isEmpty(facOperations)){
            return;
        }
        List<LovValueDTO> facOperations2 = facOperations.stream().filter(item -> item.getValue().equals(mtOperation.getOperationName()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(facOperations2)){
            return;
        }
        String targetOperationName = facOperations2.get(0).getMeaning();
        if(StringUtils.isBlank(targetOperationName)){
            return;
        }

        //查询FAC投料物料ID
        String facReleaseMaterialId = hmeEoJobSnMapper.queryFacMaterialId(tenantId,dto.getSiteId(),hmeEoJobSnVO.getEoId(),targetOperationName);
        if(StringUtils.isBlank(facReleaseMaterialId)){
            return;
        }

        //查询FAC-Y宽判定标准
        HmeFacYk hmeFacYkPara = new HmeFacYk();
        hmeFacYkPara.setTenantId(tenantId);
        hmeFacYkPara.setMaterialId(hmeEoJobSnVO.getSnMaterialId());
        hmeFacYkPara.setFacMaterialId(facReleaseMaterialId);
        hmeFacYkPara.setCosType(dto.getSnNum().substring(4,5));
        hmeFacYkPara.setWorkcellId(dto.getWorkcellId());
        HmeFacYk hmeFacYk = hmeFacYkMapper.selectMaxMinValue(tenantId,hmeFacYkPara);
        if(Objects.isNull(hmeFacYk)){
            return;
        }

        //更新当前工序作业下的数据采集
        if(Objects.nonNull(hmeFacYk.getStandardValue())){
            BigDecimal maxValue = hmeFacYk.getStandardValue();
            BigDecimal minValue = hmeFacYk.getStandardValue();
            if(Objects.nonNull(hmeFacYk.getAllowDiffer())){
                maxValue = maxValue.add(hmeFacYk.getAllowDiffer());
                minValue = minValue.subtract(hmeFacYk.getAllowDiffer());
                List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobDataRecordMapper.queryEoJobDataRecordOfJobId(tenantId,hmeEoJobSnVO.getJobId());
                if(CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)){
                    List<String> updateEoJobDataRecordSqlList = new ArrayList<>();
                    List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_data_record_cid_s", hmeEoJobDataRecordList.size());
                    int count = 0;
                    // 获取当前用户
                    CustomUserDetails curUser = DetailsHelper.getUserDetails();
                    Long userId = curUser == null ? -1L : curUser.getUserId();
                    for (HmeEoJobDataRecord hmeEoJobDataRecord : hmeEoJobDataRecordList
                    ) {
                        hmeEoJobDataRecord.setMaximalValue(maxValue);
                        hmeEoJobDataRecord.setMinimumValue(minValue);
                        hmeEoJobDataRecord.setLastUpdatedBy(userId);
                        hmeEoJobDataRecord.setLastUpdateDate(CommonUtils.currentTimeGet());
                        hmeEoJobDataRecord.setCid(Long.valueOf(cidS.get(count)));
                        updateEoJobDataRecordSqlList.addAll(customDbRepository.getUpdateSql(hmeEoJobDataRecord));
                        count++;
                    }
                    if (CollectionUtils.isNotEmpty(updateEoJobDataRecordSqlList)) {
                        jdbcTemplate.batchUpdate(updateEoJobDataRecordSqlList.toArray(new String[updateEoJobDataRecordSqlList.size()]));
                    }
                }
            }
        }
    }

    /**
     *
     * @Description 工序作业-单个进站扫描
     *
     * @author jianmin.hong
     * @date 2020/11/23 16:32
     * @param tenantId 租户ID
     * @param dto 参数
     * @return HmeEoJobSnVO
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        long startDate = System.currentTimeMillis();
        //校验
        HmeEoJobSnSingleVO hmeEoJobSnSingleVO = inSiteScanValidate(tenantId, dto);
        log.info("=================================>单件作业-进站校验总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //返回前台参数
        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        String showTagModelFlag = hmeTagCheckRepository.getShowTagModelFlag(tenantId, dto.getWorkcellId());
        if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnSingleVO.getIsQuery())){
            startDate = System.currentTimeMillis();
            resultVO = inSiteQuery(tenantId, dto, hmeEoJobSnSingleVO);
            resultVO.setShowTagModelFlag(showTagModelFlag);
            log.info("=================================>单件作业-进站走查询逻辑总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            return resultVO;
        }
        startDate = System.currentTimeMillis();
        if(hmeEoJobSnSingleVO.getIsRework()) {
            if (hmeEoJobSnSingleVO.getIsDesignedRework()) {
                //指定工艺路线返修
                resultVO = designedReworkInSiteScan(tenantId, dto, hmeEoJobSnSingleVO);
                log.info("=================================>单件作业-进站指定工艺路线返修总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            } else {
                //返修进站逻辑
                startDate = System.currentTimeMillis();
                resultVO = reworkInSiteScan(tenantId, dto, hmeEoJobSnSingleVO);
                log.info("=================================>单件作业-进站返修进站逻辑总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            }
        } else {
            //非返修正常进站逻辑
            resultVO = normalInSiteScan(tenantId, dto, hmeEoJobSnSingleVO);
            log.info("=================================>单件作业-进站非返修正常进站逻辑总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }
        // 20210823 add by sanfeng.zhang for peng.zhao 反射镜进站时 计算耦合标准和下限
        this.calculationReflectorRecord(tenantId, dto, resultVO);
        // 是否展示数据项弹框
        resultVO.setShowTagModelFlag(showTagModelFlag);
        return resultVO;
    }

    @Override
    public HmeEoJobSnVO inSiteQuery(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO hmeEoJobSnSingleVO) {
        HmeEoJobSnVO23 exitEoJobSn = hmeEoJobSnSingleVO.getExitEoJobSn();
        if(Objects.isNull(exitEoJobSn) || StringUtils.isBlank(exitEoJobSn.getJobId())){
            //该条码【${1}】在该工位未进站作业,请检查!
            throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_010", "HME",dto.getSnNum()));
        }

        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        BeanUtils.copyProperties(exitEoJobSn, resultVO);
        resultVO.setInspection(exitEoJobSn.getAttribute2());
        resultVO.setIsClickInspectionBtn(hmeEoJobSnSingleVO.getIsClickInspectionBtn());

        //V20210220 modify by penglin.sui for tianyang.xie 保存工位EO关系
        HmeWkcEoRel hmeWkcEoRel = new HmeWkcEoRel();
        hmeWkcEoRel.setWkcId(dto.getWorkcellId());
        hmeWkcEoRel.setOperationId(dto.getOperationId());
        hmeWkcEoRel.setEoId(resultVO.getEoId());
        hmeWkcEoRel.setJobId(resultVO.getJobId());
        hmeWkcEoRelService.saveWkcEoRel(tenantId , hmeWkcEoRel);

        // 获取当前作业数据采集
        HmeEoJobMaterialVO2 jobDataRecordCondition = new HmeEoJobMaterialVO2();
        jobDataRecordCondition.setWorkcellId(dto.getWorkcellId());
        jobDataRecordCondition.setJobId(resultVO.getJobId());
        jobDataRecordCondition.setJobType(dto.getJobType());
        long startDate = System.currentTimeMillis();
        List<HmeEoJobDataRecordVO> eoJobDataRecordVOList = hmeEoJobDataRecordRepository.eoJobDataRecordQuery(tenantId,jobDataRecordCondition);
        log.info("=================================>单件作业-进站查询逻辑获取当前作业数据采集总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        if(CollectionUtils.isNotEmpty(eoJobDataRecordVOList)){
            if (hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId())) {
                // 标准值取记录里面的
                eoJobDataRecordVOList.forEach(jobRecord -> {
                    if (jobRecord.getRecordStandardValue() != null) {
                        jobRecord.setStandardValue(String.valueOf(jobRecord.getRecordStandardValue()));
                    }
                });
            }
            resultVO.setDataRecordVOList(eoJobDataRecordVOList);
        }

        // 如存在工艺步骤，需获取步骤信息
        if (StringUtils.isNotBlank(exitEoJobSn.getEoStepId())) {
            startDate = System.currentTimeMillis();
            HmeRouterStepVO5 currentStep = hmeEoJobSnCommonService.queryCurrentAndNextStepByCurrentId(tenantId, exitEoJobSn.getEoStepId());
            log.info("=================================>单件作业-进站查询逻辑获取步骤总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            if(Objects.nonNull(currentStep)){
                resultVO.setCurrentStepName(currentStep.getStepName());
                resultVO.setCurrentStepDescription(currentStep.getDescription());
                resultVO.setCurrentStepSequence(currentStep.getSequence());
                resultVO.setNextStepName(currentStep.getNextStepName());
                resultVO.setNextStepDescription(currentStep.getNextDescription());
                resultVO.setLabCode(currentStep.getLabCode());
                resultVO.setRouterStepRemark(currentStep.getRouterStepRemark());
            }
        }

        // 节拍
        if (resultVO.getSiteOutDate() != null) {
            String meterTimeStr = DateUtil.getDistanceTime(resultVO.getSiteInDate(),
                    resultVO.getSiteOutDate());
            resultVO.setMeterTimeStr(meterTimeStr);
        }

        // 操作员
        if (Objects.nonNull(resultVO.getSiteInBy())) {
            String userRealName = userClient.userInfoGet(tenantId, resultVO.getSiteInBy()).getRealName();
            resultVO.setSiteInByName(userRealName);
        }

        //工位信息
        if (StringUtils.isBlank(dto.getWorkcellCode())) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
            resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
            resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
        } else {
            resultVO.setWorkcellCode(dto.getWorkcellCode());
            resultVO.setWorkcellName(dto.getWorkcellName());
        }

        //查询班次、生产版本
        HmeEoJobSnVO jobSnInfo = hmeEoJobSnMapper.queryEoJobSnInfoForInSiteQuery(tenantId, resultVO.getJobId());
        if (Objects.nonNull(jobSnInfo)) {
            resultVO.setShiftCode(jobSnInfo.getShiftCode());
            resultVO.setProductionVersion(jobSnInfo.getProductionVersion());
        }

        //V20201208 modify by penglin.sui 返回BOM信息
        if (hmeEoJobSnSingleVO.getIsDesignedRework()) {
            HmeEoRouterBomRelVO eoRouterBomRel = hmeEoJobSnSingleVO.getEoRouterBomRel();
            resultVO.setBomId(eoRouterBomRel.getBomId());
            resultVO.setBomName(eoRouterBomRel.getBomName());
        } else {
            List<String> eoIdList = new ArrayList<>(1);
            eoIdList.add(resultVO.getEoId());
            List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId, eoIdList);
            if (CollectionUtils.isNotEmpty(hmeEoBomVOList)) {
                resultVO.setBomId(hmeEoBomVOList.get(0).getBomId());
                resultVO.setBomName(hmeEoBomVOList.get(0).getBomName());
            }
        }

        // 执行作业相关
        HmeEoJobSnVO5 snVO = hmeEoJobSnSingleVO.getSnVO();
        resultVO.setWorkcellId(dto.getWorkcellId());
        resultVO.setMaterialLotId(dto.getMaterialLotId());
        resultVO.setSnNum(dto.getSnNum());
        resultVO.setWorkOrderId(snVO.getWorkOrderId());
        resultVO.setWorkOrderNum(snVO.getWorkOrderNum());
        resultVO.setSnMaterialId(snVO.getMaterialId());
        resultVO.setSnMaterialCode(snVO.getMaterialCode());
        resultVO.setSnMaterialName(snVO.getMaterialName());
        resultVO.setWoQuantity(snVO.getWoQty());
        resultVO.setWoQuantityOut(snVO.getCompletedQty());
        resultVO.setQualityStatus(snVO.getQualityStatus());
        resultVO.setQualityStatusMeaning(snVO.getQualityStatusMeaning());
        resultVO.setRemark(snVO.getRemark());
        resultVO.setIsContainer(hmeEoJobSnSingleVO.getIsContainer());
        resultVO.setReworkFlag(hmeEoJobSnSingleVO.getIsRework()? HmeConstants.ConstantValue.YES: HmeConstants.ConstantValue.NO);
        resultVO.setDesignedReworkFlag(hmeEoJobSnSingleVO.getIsDesignedRework()? HmeConstants.ConstantValue.YES: HmeConstants.ConstantValue.NO);
        resultVO.setIsClickProcessComplete(hmeEoJobSnSingleVO.getIsClickProcessComplete());
        resultVO.setProhibitClickContinueReworkFlag(hmeEoJobSnSingleVO.getProhibitClickContinueReworkFlag());
        resultVO.setNcRecordWorkcellCode(hmeEoJobSnSingleVO.getNcRecordWorkcellCode());
        resultVO.setNcRecordWorkcellName(hmeEoJobSnSingleVO.getNcRecordWorkcellName());
        resultVO.setSnNum(dto.getSnNum());
        resultVO.setProdLineCode(dto.getProdLineCode());
        resultVO.setTestFlag(snVO.getTestFlag());
        resultVO.setIsShowCrossRetestBtn(snVO.getIsShowCrossRetestBtn());
        return resultVO;
    }

    @Override
    public void calculationReflectorRecord(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO resultVO) {
        // 判断是否是反射镜进站 有采集项数据则进行计算
        if (hmeEoJobSnCommonService.isReflectorNc(tenantId, dto.getOperationId()) && CollectionUtils.isNotEmpty(resultVO.getDataRecordVOList())) {
            // 获取芯片筛选功率数据
            // 获取首序jobId (取SN扩展表SX_PROCESS值)
            List<MtExtendAttrVO> firstProcessAttrList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                setMaterialLotId(dto.getMaterialLotId());
                setAttrName("SX_PROCESS");
            }});
            if (CollectionUtils.isEmpty(firstProcessAttrList) || StringUtils.isBlank(firstProcessAttrList.get(0).getAttrValue())) {
                // 该器件【${1}】找不到筛选工序,请检查
                throw new MtException("HME_EO_JOB_SN_222", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_222", "HME", dto.getSnNum()));
            }
            String firstProcessJobId = firstProcessAttrList.get(0).getAttrValue();

            // 获取芯片耦合效率数据  即为工序不良数据项明细最小值
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
            String snNum = dto.getSnNum();
            String cosModel = snNum.substring(4, 5);
            String chipCombination = snNum.substring(5, 6);
            String processNcHeaderId = queryProcessNcHeader(tenantId, cosModel, chipCombination, mtMaterialLot.getMaterialId(), dto.getOperationId(), snNum);
            List<HmeProcessNcVO> processNcVOList = hmeEoJobSnMapper.queryProcessNcTagList(tenantId, processNcHeaderId);
            if (CollectionUtils.isEmpty(processNcVOList)) {
                throw new MtException("HME_EO_JOB_SN_236", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_236", "HME", snNum));
            }
            Set<String> headTagList = new HashSet<>();
            processNcVOList.forEach(vo -> {
                if (StringUtils.isBlank(vo.getDetailId())) {
                    // 数据项【${1}】查询不到判定标准,请检查!
                    throw new MtException("HME_EO_JOB_SN_170", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_170", "HME", vo.getTagCode()));
                }
                headTagList.add(vo.getTagId());
            });
            // 如果一个行对应多个明细 则报错
            Map<String, List<HmeProcessNcVO>> processNcMap = processNcVOList.stream().collect(Collectors.groupingBy(HmeProcessNcVO::getLineId));
            Map<String, HmeTagFormulaLineVO2> calculationValueMap = new HashMap<>();

            // 取本次job的采集数据的收集组
            List<HmeEoJobDataRecordVO> dataRecordVOList = resultVO.getDataRecordVOList();
            String tagGroupId = dataRecordVOList.get(0).getTagGroupId();
            // 根据数据项和数据收集组查询对应计算公式
            List<HmeTagFormulaLineVO2> tagFormulaLineList = hmeTagFormulaHeadMapper.queryTagFormulaLineList(tenantId, headTagList, tagGroupId);
            Map<String, List<HmeTagFormulaLineVO2>> groupTagMap = tagFormulaLineList.stream().collect(Collectors.groupingBy(tag -> tag.getHeaderTagId()));
            // 获取参数代码b对应数据项的采集结果
            List<String> tagIdList = tagFormulaLineList.stream().filter(vo -> StringUtils.equalsIgnoreCase(vo.getParameterCode(), "b")).map(HmeTagFormulaLineVO2::getTagId).collect(Collectors.toList());
            Map<String, String> filterPowerRecordMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(tagIdList)) {
                List<HmeEoJobDataRecord> filterPowerRecordList = hmeEoJobDataRecordMapper.queryFilterPower(tenantId, tagIdList, firstProcessJobId);
                filterPowerRecordMap = filterPowerRecordList.stream().collect(Collectors.toMap(HmeEoJobDataRecord::getTagId, HmeEoJobDataRecord::getResult));
            }
            Map<String, String> finalFilterPowerRecordMap = filterPowerRecordMap;
            // 获取数据项计算公式行为参数代码为a的数据项 取参数代码b 的result * 头的min_value 计算耦合标准或下限
            groupTagMap.entrySet().forEach(vo -> {
                // 参数代码a 数据项存在多条则报错
                Optional<HmeTagFormulaLineVO2> tagOpt = vo.getValue().stream().filter(tag -> StringUtils.equalsIgnoreCase(tag.getParameterCode(), "a")).findFirst();
                if (tagOpt.isPresent()) {
                    Optional<Map.Entry<String, List<HmeProcessNcVO>>> firstOpt = processNcMap.entrySet().stream().filter(pnc -> {
                        List<HmeProcessNcVO> valueList = pnc.getValue();
                        // 取到多个则 取第一个
                        if (StringUtils.equals(valueList.get(0).getTagId(), vo.getKey())) {
                            return true;
                        }
                        return false;
                    }).findFirst();
                    if (!firstOpt.isPresent()) {
                        throw new MtException("HME_EO_JOB_SN_224", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_224", "HME", vo.getKey()));
                    }
                    // 耦合效率
                    String minValue = firstOpt.get().getValue().get(0).getMinValue();
                    // 筛选功率
                    Optional<HmeTagFormulaLineVO2> selectTagOpt = vo.getValue().stream().filter(tag -> StringUtils.equalsIgnoreCase(tag.getParameterCode(), "b")).findFirst();
                    if (!selectTagOpt.isPresent()) {
                        throw new MtException("HME_EO_JOB_SN_237", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_237", "HME", vo.getValue().get(0).getHeaderTagCode()));
                    }
                    String result = finalFilterPowerRecordMap.get(selectTagOpt.get().getTagId());
                    BigDecimal resultValue = null;
                    try {
                        resultValue = BigDecimal.valueOf(Double.valueOf(result));
                    } catch (Exception e) {
                        throw new MtException("HME_EO_JOB_SN_168", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_168", "HME", selectTagOpt.get().getTagCode()));
                    }
                    BigDecimal multiplyQty = resultValue.multiply(BigDecimal.valueOf(Double.valueOf(minValue)));
                    HmeTagFormulaLineVO2 vo2 = new HmeTagFormulaLineVO2();
                    vo2.setHeaderTagId(firstOpt.get().getValue().get(0).getTagId());
                    vo2.setHeaderTagCode(firstOpt.get().getValue().get(0).getTagCode());
                    vo2.setTagId(tagOpt.get().getTagId());

                    vo2.setResultQty(multiplyQty);
                    calculationValueMap.put(tagOpt.get().getTagId(), vo2);
                }
            });
            List<HmeEoJobDataRecord> updateEoJobDataRecordList = new ArrayList<>();
            // 查询本job的记录 更新最小值或标准值 包含COS 则更新最小值 否则标准值
            List<HmeEoJobDataRecordVO2> jobDataRecordVO2List = hmeEoJobDataRecordMapper.queryEoJobDataRecordList(tenantId, resultVO.getJobId());
            // 取出平均功率的值
            Optional<HmeTagFormulaLineVO2> aceOpt = calculationValueMap.values().stream().filter(vo -> vo.getHeaderTagCode().endsWith("ACE")).findFirst();
            // 标准值 = 平均功率/CE数量
            List<HmeTagFormulaLineVO2> ceTagList = calculationValueMap.values().stream().filter(vo -> !vo.getHeaderTagCode().endsWith("ACE") && vo.getHeaderTagCode().endsWith("CE")).collect(Collectors.toList());
            BigDecimal standardValue = null;
            if (CollectionUtils.isNotEmpty(ceTagList) && aceOpt.isPresent()) {
                standardValue = aceOpt.get().getResultQty().divide(new BigDecimal(ceTagList.size()), 6);
            }
            List<HmeDataRecordExtend> updateDataRecordExtendList = new ArrayList<>();
            List<HmeDataRecordExtend> insertDataRecordExtendList = new ArrayList<>();
            BigDecimal finalStandardValue = standardValue;
            jobDataRecordVO2List.forEach(vo -> {
                Optional<Map.Entry<String, HmeTagFormulaLineVO2>> firstOpt = calculationValueMap.entrySet().stream().filter(cvm -> StringUtils.equals(cvm.getKey(), vo.getTagId())).findFirst();
                if (firstOpt.isPresent()) {
                    HmeEoJobDataRecord hmeEoJobDataRecord = new HmeEoJobDataRecord();
                    hmeEoJobDataRecord.setJobRecordId(vo.getJobRecordId());
                    hmeEoJobDataRecord.setTagId(vo.getTagId());
                    if (!firstOpt.get().getValue().getHeaderTagCode().endsWith("ACE")) {
                        hmeEoJobDataRecord.setMinimumValue(firstOpt.get().getValue().getResultQty());
                        updateEoJobDataRecordList.add(hmeEoJobDataRecord);
                        HmeDataRecordExtend hmeDataRecordExtend = new HmeDataRecordExtend();
                        hmeDataRecordExtend.setJobId(vo.getJobId());
                        hmeDataRecordExtend.setJobRecordId(vo.getJobRecordId());
                        hmeDataRecordExtend.setStandardValue(finalStandardValue);
                        hmeDataRecordExtend.setAttribute1(HmeConstants.ConstantValue.YES);
                        if (StringUtils.isNotBlank(vo.getDataRecordExtendId())) {
                            hmeDataRecordExtend.setDataRecordExtendId(vo.getDataRecordExtendId());
                            updateDataRecordExtendList.add(hmeDataRecordExtend);
                        } else {
                            insertDataRecordExtendList.add(hmeDataRecordExtend);
                        }
                    }
                }
            });
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            if (CollectionUtils.isNotEmpty(insertDataRecordExtendList)) {
                hmeDataRecordExtendRepository.batchInsertSelective(insertDataRecordExtendList);
            }
            if (CollectionUtils.isNotEmpty(updateDataRecordExtendList)) {
                hmeDataRecordExtendMapper.batchUpdateStandardValue(tenantId, userId, updateDataRecordExtendList);
            }
            if (CollectionUtils.isNotEmpty(updateEoJobDataRecordList)) {
               hmeEoJobDataRecordMapper.batchUpdateStandardOrMinimumValue(tenantId, userId, updateEoJobDataRecordList);

                // 反射镜的 采集项数据更新标准值后在返回
                for (HmeEoJobDataRecordVO hmeEoJobDataRecordVO : dataRecordVOList) {
                    Optional<HmeEoJobDataRecord> firstOpt = updateEoJobDataRecordList.stream().filter(dataRecord -> StringUtils.equals(dataRecord.getTagId(), hmeEoJobDataRecordVO.getTagId())).findFirst();
                    if (firstOpt.isPresent()) {
                        hmeEoJobDataRecordVO.setStandardValue(standardValue != null ? standardValue.toString() : null);
                        hmeEoJobDataRecordVO.setMinimumValue(firstOpt.get().getMinimumValue());
                    }
                }
                resultVO.setDataRecordVOList(dataRecordVOList);
            }
        }
    }


    private String queryProcessNcHeader(Long tenantId, String cosModel, String chipCombination, String materialId, String operationId, String snNum) {
        List<HmeProcessNcHeader> hmeProcessNcHeaderList = hmeEoJobSnMapper.queryProcessNcHeader(tenantId, cosModel, materialId, operationId);
        Optional<HmeProcessNcHeader> firstOpt = hmeProcessNcHeaderList.stream().filter(vo -> StringUtils.equals(chipCombination, vo.getChipCombination()) || StringUtils.isBlank(vo.getChipCombination())).findFirst();
        if (!firstOpt.isPresent()) {
            // 找不到头则报错
            throw new MtException("HME_EO_JOB_SN_169", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_169", "HME", snNum));
        }
        return firstOpt.get().getHeaderId();
    }
}
