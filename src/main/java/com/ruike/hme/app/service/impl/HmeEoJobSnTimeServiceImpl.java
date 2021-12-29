package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.app.service.HmeEoJobSnTimeService;
import com.ruike.hme.domain.entity.HmeEoJobContainer;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeMaterialLotLabCode;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobContainerMapper;
import com.ruike.hme.infra.mapper.HmeEoJobDataRecordMapper;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.hme.infra.mapper.HmeMaterialLotLabCodeMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.HmeEoJobSnUtils;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.repository.MtEoRouterActualRepository;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.repository.MtEoStepWipRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoRouterActualMapper;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContainerVO25;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.*;
import java.util.*;
import java.util.stream.*;

/**
 * 时效作业平台-SN作业应用服务默认实现
 *
 * @author yuchao.wang@hand-china.com 2020-11-03 00:04:39
 */
@Slf4j
@Service
public class HmeEoJobSnTimeServiceImpl implements HmeEoJobSnTimeService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeEoJobContainerRepository hmeEoJobContainerRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private HmeEoJobContainerMapper hmeEoJobContainerMapper;

    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;

    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;

    @Autowired
    private HmeOperationTimeObjectRepository hmeOperationTimeObjectRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtEoRouterActualMapper mtEoRouterActualMapper;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private HmeOpTagRelRepository hmeOpTagRelRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeEoRouterBomRelRepository hmeEoRouterBomRelRepository;

    @Autowired
    private HmeMaterialLotLabCodeMapper hmeMaterialLotLabCodeMapper;

    @Autowired
    private HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HmeProcessNcHeaderRepository hmeProcessNcHeaderRepository;

    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    /**
     *
     * @Description 时效作业-入炉
     *
     * @author yuchao.wang
     * @date 2020/11/3 9:47
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeServiceImpl.inSiteScan.Start tenantId={},dto={}", tenantId, dto);
        // 进站条码不能为空
        if (StringUtils.isBlank(dto.getSnNum())) {
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        defaultInputVerification(tenantId, dto);

        //获取所有eoId 条码Id
        List<String> eoIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getEoId).distinct().collect(Collectors.toList());
        List<String> materialLotIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getMaterialLotId).distinct().collect(Collectors.toList());

        //批量查询条码信息并校验状态
        HmeEoJobSnVO16 hmeEoJobSnVO16 = hmeEoJobSnCommonService.batchQueryAndCheckMaterialLotByIdsForTime(tenantId, materialLotIdList);

        //进站校验
        boolean isRework = inSiteScanValidate(tenantId, dto, hmeEoJobSnVO16, eoIdList, materialLotIdList);

        //判断是否为指定工艺路线返修 add by yuchao.wang for tianyang.xie at 2021.1.7
        boolean isDesignedRework = false;
        if (isRework) {
            HmeEoJobSnVO5 snVO5 = hmeEoJobSnVO16.getEoJobSnMap().get(materialLotIdList.get(0));
            isDesignedRework = HmeConstants.ConstantValue.YES.equals(snVO5.getDesignedReworkFlag());

            //如果是指定工艺路线返修 先查询指定的关系数据
            if (isDesignedRework) {
                HmeEoRouterBomRelVO eoRouterBomRel = hmeEoRouterBomRelRepository.queryLastRelByEoId(tenantId, eoIdList.get(0));
                if (Objects.isNull(eoRouterBomRel) || StringUtils.isBlank(eoRouterBomRel.getEoRouterBomRelId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "EO 工艺路线 装配清单关系"));
                }
                hmeEoJobSnVO16.setEoRouterBomRel(eoRouterBomRel);
            }
        }

        log.info("HmeEoJobSnTimeServiceImpl.inSiteScan snNum={},snType={},isRework={},isDesignedRework={},dto.getSnLineList().size()={}",
                dto.getSnNum(), dto.getSnType(), isRework, isDesignedRework, dto.getSnLineList().size());

        //如果是物料批并且有容器则先卸载
        if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(dto.getSnType())) {
            HmeEoJobSnVO5 snVO = hmeEoJobSnVO16.getEoJobSnMap().get(materialLotIdList.get(0));
            if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
                MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
                mtContainerVO25.setContainerId(snVO.getCurrentContainerId());
                mtContainerVO25.setLoadObjectId(snVO.getMaterialLotId());
                mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
            }
        }

        //根据是否返修走不同进站逻辑 modify by yuchao.wang for tianyang.xie at 2020.12.21
        if (isRework) {
            //根据是否指定工艺路线返修走不同进站逻辑 modify by yuchao.wang for tianyang.xie at 2021.1.7
            if (isDesignedRework) {
                inSiteScanForDesignedRework(tenantId, dto, hmeEoJobSnVO16);
            } else {
                inSiteScanForRework(tenantId, dto, hmeEoJobSnVO16, eoIdList);
            }
        } else {
            inSiteScanForNormal(tenantId, dto, hmeEoJobSnVO16, eoIdList);
        }

        log.info("<====== HmeEoJobSnTimeServiceImpl.inSiteScan.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
    }

    /**
     *
     * @Description 进站校验
     *
     * @author yuchao.wang
     * @date 2020/12/23 15:22
     * @return void
     *
     */
    private boolean inSiteScanValidate(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16, List<String> eoIdList, List<String> materialLotIdList) {
        Map<String, HmeEoJobSnVO5> snVoMap = hmeEoJobSnVO16.getEoJobSnMap();

        //校验同批次是否有返修标识不一致的条码 时效增加返修逻辑 modify by yuchao.wang for tianyang.xie at 2020.12.21
        List<String> reworkFlagList = snVoMap.values().stream().map(HmeEoJobSnVO5::getReworkFlag).distinct().collect(Collectors.toList());
        if (reworkFlagList.size() > 1) {
            // 正常加工sn与返修sn不允许同时进/出站!
            throw new MtException("HME_EO_JOB_SN_152", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_152", "HME"));
        }

        //V20210525 modify by penglin.sui for peng.zhao 进站校验工位绑定设备
        hmeEoJobSnCommonService.workcellBindEquipmentValidate(tenantId, dto.getOperationId(), dto.getWorkcellId());

        // 20210908 add by sanfeng.zhang for hui.gu 判断SN在该工序是否已拦截
        if (CollectionUtils.isNotEmpty(materialLotIdList)) {
            List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                    .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
            hmeEoJobSnCommonService.interceptValidate(tenantId, dto.getWorkcellId(), mtMaterialLotList);
        }

        boolean isRework = HmeConstants.ConstantValue.YES.equals(reworkFlagList.get(0));

        //返修相关校验
        if (isRework) {
            //当前容器下SN${1}已判定不良,请按照SN编码单独入炉!
            if (HmeConstants.LoadTypeCode.CONTAINER.equals(dto.getSnType()) || dto.getSnLineList().size() > 1) {
                String materialLotCode = "";
                Optional<HmeEoJobSnVO5> reworkMaterialLot = snVoMap.values().stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())).findAny();
                if (reworkMaterialLot.isPresent()) {
                    materialLotCode = reworkMaterialLot.get().getMaterialLotCode();
                }
                throw new MtException("HME_EO_JOB_SN_154", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_154", "HME", materialLotCode));
            }

            //判断是否为指定工艺路线返修 modify by yuchao.wang for tianyang.xie at 2021.1.7
            HmeEoJobSnVO5 snVO5 = snVoMap.get(materialLotIdList.get(0));
            boolean isDesignedRework = HmeConstants.ConstantValue.YES.equals(snVO5.getDesignedReworkFlag());

            //不是是指定工艺路线返修 校验ncRecord是否存在
            if (!isDesignedRework) {
                HmeNcRecordVO hmeNcRecordVO = hmeEoJobSnMapper.selectLastestNcRecordProcess(tenantId, materialLotIdList.get(0), dto.getWorkcellId());
                if (Objects.isNull(hmeNcRecordVO) || StringUtils.isBlank(hmeNcRecordVO.getNcRecordId())) {
                    //${1}不存在 请确认${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "ncRecord", dto.getSnNum()));
                }
            }
        }

        //校验是否重复进站
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnCommonService.queryEoJobSnForTimeInSite(tenantId, isRework, dto.getOperationId(), eoIdList);
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {

            long count = 0;

            if (isRework) {
                count = hmeEoJobSnList.stream().filter(item -> !dto.getOperationId().equals(item.getOperationId())).count();
                if (count > 0) {
                    List<HmeEoJobSn> exitEoJobSn = hmeEoJobSnList.stream()
                            .filter(item -> !dto.getOperationId().equals(item.getOperationId())).collect(Collectors.toList());
                    MtRouterStep routerStep = mtRouterStepRepository.routerStepGet(tenantId, exitEoJobSn.get(0).getEoStepId());
                    //	当前SN已在${1}工序下返修进站且未出站,不允许重复进站!
                    throw new MtException("HME_EO_JOB_SN_151", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_151", "HME", routerStep.getStepName()));
                }
            }

            //校验工位是否一致
            count = hmeEoJobSnList.stream().filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).count();
            if (count > 0) {
                List<HmeEoJobSn> exitEoJobSn = hmeEoJobSnList.stream()
                        .filter(item -> !dto.getWorkcellId().equals(item.getWorkcellId())).collect(Collectors.toList());
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, exitEoJobSn.get(0).getWorkcellId());
                //当前SN已在${1}进站,不允许在此工位重复进站
                throw new MtException("HME_EO_JOB_SN_144", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_144", "HME", mtModWorkcell.getWorkcellName()));
            }

            //校验平台是否一致
            count = hmeEoJobSnList.stream().filter(item -> !dto.getJobType().equals(item.getJobType())).count();
            if (count > 0) {
                String jobTypeDesc = "";
                List<HmeEoJobSn> exitEoJobSn = hmeEoJobSnList.stream()
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

            //判断报哪种错 目前只报错SN已入炉
            throw new MtException("HME_EO_JOB_TIME_SN_002", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_002", "HME"));
            /*List<String> wkcList = hmeEoJobSnList.stream().map(HmeEoJobSn::getWorkcellId)
                    .filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
            if (wkcList.contains(dto.getWorkcellId())) {
                // 报错：该SN已入炉
                throw new MtException("HME_EO_JOB_TIME_SN_002", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_002", "HME"));
            } else {
                // 同一工序不允许多个工位进站
                throw new MtException("HME_EO_JOB_SN_054", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_054", "HME"));
            }*/
        }

        return isRework;
    }

    /**
     *
     * @Description 时效进站-正常加工进站
     *
     * @author yuchao.wang
     * @date 2020/12/21 15:14
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @param eoIdList eoIdList
     * @return void
     *
     */
    private void inSiteScanForNormal(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16, List<String> eoIdList) {
        //取条码信息及上一步工艺
        Map<String, HmeEoJobSnVO5> snVoMap = hmeEoJobSnVO16.getEoJobSnMap();
        Map<String, HmeRouterStepVO2> nearStepMap = hmeEoJobSnVO16.getNearStepMap();
        Map<String, HmeRouterStepVO2> normalStepMap = hmeEoJobSnVO16.getNormalStepMap();

        //取当前步骤信息
        Map<String, HmeRouterStepVO3> currentStepMap = hmeEoJobSnCommonService
                .batchQueryCurrentRouterStepForTime(tenantId, eoIdList, dto.getOperationId());
        List<String> routerStepIdList = new ArrayList<>();
        for(Map.Entry<String, HmeRouterStepVO3> entry : currentStepMap.entrySet()){
            if(!routerStepIdList.contains(entry.getValue().getRouterStepId())) {
                routerStepIdList.add(entry.getValue().getRouterStepId());
            }
        }

        //批量查询所有正常步骤的下一步骤
        List<String> normalStepIdList = normalStepMap.values().stream()
                .map(HmeRouterStepVO2::getRouterStepId).collect(Collectors.toList());
        Map<String, String> nextStepMap = hmeEoJobSnCommonService.batchQueryNextStep(tenantId, normalStepIdList);

        //批量查询实验代码
        List<String> materialLotIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getMaterialLotId).distinct().collect(Collectors.toList());
        List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = hmeMaterialLotLabCodeMapper.batchSelectLabCode2(tenantId,materialLotIdList,routerStepIdList,null);

        //批量获取EoJobSn Id/Cid
        List<String> eoJobSnIdList = customSequence.getNextKeys("hme_eo_job_sn_s", dto.getSnLineList().size());
        List<String> eoJobSnCidList = customSequence.getNextKeys("hme_eo_job_sn_cid_s", dto.getSnLineList().size());

        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        // 获取当前时间
        final Date currentDate = new Date();

        //是否需要调用MoveIn API
        boolean isBatchMoveInProcess = false;
        int index = 0;
        List<HmeEoJobSnVO15> hmeEoJobSnVO15List = new ArrayList<>();
        List<MtEoRouterActualVO42> mtEoRouterActualVO42List = new ArrayList<>();
        List<HmeEoJobSn> insertList = new ArrayList<>();
        List<HmeEoJobSnVO2> dataRecordInitParamList = new ArrayList<>();
        List<HmeMaterialLotLabCode> insertMaterialLotLabCodeList = new ArrayList<>();
        List<HmeMaterialLotLabCode> updateMaterialLotLabCodeList = new ArrayList<>();

        for (HmeEoJobSnVO3 jobSn : dto.getSnLineList()) {
            //取到当前条码的数据
            HmeEoJobSnVO5 snVO = snVoMap.get(jobSn.getMaterialLotId());
            HmeRouterStepVO2 normalRouterStep = normalStepMap.get(jobSn.getEoId());
            HmeRouterStepVO2 nearRouterStep = nearStepMap.get(jobSn.getEoId());
            HmeRouterStepVO3 currentRouterStep = currentStepMap.get(jobSn.getEoId());
            if (Objects.isNull(currentRouterStep) || StringUtils.isBlank(currentRouterStep.getRouterStepId())) {
                // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
                throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
            }

            //构造返回数据
            jobSn.setWorkcellId(dto.getWorkcellId());
            jobSn.setJobType(dto.getJobType());
            jobSn.setOperationId(dto.getOperationId());
            jobSn.setOperationIdList(dto.getOperationIdList());
            jobSn.setReworkFlag(snVO.getReworkFlag());
            jobSn.setEoStepId(currentRouterStep.getRouterStepId());
            jobSn.setSiteId(dto.getSiteId());
            jobSn.setWorkOrderId(snVO.getWorkOrderId());
            jobSn.setMaterialId(snVO.getMaterialId());
            jobSn.setEoStepNum(HmeConstants.ConstantValue.ONE);

            log.info("当前jobSn对应eoId={},入口步骤标识:{}", jobSn.getEoId(), currentRouterStep.getEntryStepFlag());

            // 判断当前步骤是否为入口步骤
            if (HmeConstants.ConstantValue.YES.equals(currentRouterStep.getEntryStepFlag())) {
                // 构造API eoBatchWorking参数
                HmeEoJobSnVO15 hmeEoJobSnVO15 = new HmeEoJobSnVO15();
                hmeEoJobSnVO15.setEoId(jobSn.getEoId());
                hmeEoJobSnVO15.setEoQty(snVO.getEoQty());
                hmeEoJobSnVO15List.add(hmeEoJobSnVO15);
            } else {
                if (Objects.isNull(nearRouterStep) || StringUtils.isBlank(nearRouterStep.getRouterStepId())
                        || Objects.isNull(normalRouterStep) || StringUtils.isBlank(normalRouterStep.getRouterStepId())) {
                    // 当前为非入口步骤, 且根据EO没有找到最近加工步骤
                    throw new MtException("HME_EO_JOB_SN_046",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_046", "HME"));
                }

                log.info("当前步骤:" + currentRouterStep.getRouterStepId() + ",最近一步骤:" + nearRouterStep.getRouterStepId()
                        + ",最近一正常步骤:" + normalRouterStep.getRouterStepId() + ",最近一正常步骤的下一步骤:" +
                        (nextStepMap.containsKey(normalRouterStep.getRouterStepId()) ? "" : nextStepMap.get(normalRouterStep.getRouterStepId())));

                //查询当前EO关联的工单类型 是否为售后,如果是售后不做防跳站校验 modify by yuchao.wang for jiao.chen at 2020.9.28
                if (nearRouterStep.getCompletedQty().compareTo(BigDecimal.valueOf(0)) == 0
                        && !hmeEoJobSnCommonService.isAfterSalesWorkOrder(tenantId, jobSn.getEoId())) {
                    MtRouterStep routerStep = mtRouterStepRepository.routerStepGet(tenantId, nearRouterStep.getRouterStepId());
                    // 请先完成工序步骤{最近加工步骤描述}的出站操作，再执行本道工序步骤进站
                    throw new MtException("HME_EO_JOB_SN_031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_031", "HME", routerStep.getStepName()));
                }

                //比较当前步骤和最近加工步骤的下一步骤
                if (!nextStepMap.containsKey(normalRouterStep.getRouterStepId())
                        || !jobSn.getEoStepId().equals(nextStepMap.get(normalRouterStep.getRouterStepId()))) {
                    MtRouterStep routerStep = mtRouterStepRepository.routerStepGet(tenantId, normalRouterStep.getRouterStepId());
                    // 请先完成工序步骤{最近加工步骤描述}的出站操作，再执行本道工序步骤进站
                    throw new MtException("HME_EO_JOB_SN_032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_032", "HME", routerStep.getStepName()));
                }

                // 构造API eoNextStepMoveInBatchProcess2参数
                MtEoRouterActualVO51 queueProcessInfo = new MtEoRouterActualVO51();
                queueProcessInfo.setEoId(jobSn.getEoId());
                queueProcessInfo.setPreviousStepId(nearRouterStep.getEoStepActualId());
                queueProcessInfo.setQty(snVO.getEoQty().doubleValue());
                queueProcessInfo.setWorkcellId(nearRouterStep.getWipWorkcellId());
                queueProcessInfo.setRouterStepId(jobSn.getEoStepId());

                MtEoRouterActualVO42 mtEoRouterActualVO42 = new MtEoRouterActualVO42();
                mtEoRouterActualVO42.setQueueMessageList(Collections.singletonList(queueProcessInfo));

                HmeRouterStepVO nearStepVO = new HmeRouterStepVO();
                BeanUtils.copyProperties(nearRouterStep, nearStepVO);
                mtEoRouterActualVO42.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
                mtEoRouterActualVO42List.add(mtEoRouterActualVO42);
                isBatchMoveInProcess = true;

                // 构造API eoBatchWorking参数
                HmeEoJobSnVO15 hmeEoJobSnVO15 = new HmeEoJobSnVO15();
                hmeEoJobSnVO15.setEoId(jobSn.getEoId());
                hmeEoJobSnVO15.setEoQty(snVO.getEoQty());
                hmeEoJobSnVO15List.add(hmeEoJobSnVO15);
            }

            // 插入工序作业平台表
            HmeEoJobSn snJobEntity = getSnJobEntity(tenantId, userId, currentDate, eoJobSnIdList.get(index), eoJobSnCidList.get(index), jobSn);
            insertList.add(snJobEntity);

            // 构造采集数据和自检数据插入参数
            HmeEoJobSnVO2 dataRecordInitParam = new HmeEoJobSnVO2();
            BeanUtils.copyProperties(snJobEntity, dataRecordInitParam);
            dataRecordInitParam.setProductionVersion(snVO.getWoProductionVersion());
            dataRecordInitParam.setItemType(snVO.getItemType());
            dataRecordInitParamList.add(dataRecordInitParam);

            if(StringUtils.isNotBlank(currentRouterStep.getLabCode())) {
                List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList2 = hmeMaterialLotLabCodeList.stream().filter(item -> item.getMaterialLotId().equals(jobSn.getMaterialLotId())
                        && item.getRouterStepId().equals(currentRouterStep.getRouterStepId()) && item.getLabCode().equals(currentRouterStep.getLabCode()))
                        .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(hmeMaterialLotLabCodeList2)) {
                    HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    insertHmeMaterialLotLabCode.setTenantId(tenantId);
                    insertHmeMaterialLotLabCode.setMaterialLotId(jobSn.getMaterialLotId());
                    insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                    insertHmeMaterialLotLabCode.setLabCode(currentRouterStep.getLabCode());
                    insertHmeMaterialLotLabCode.setJobId(eoJobSnIdList.get(index));
                    insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                    insertHmeMaterialLotLabCode.setWorkOrderId(snVO.getWorkOrderId());
                    insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                    insertHmeMaterialLotLabCode.setRouterStepId(currentRouterStep.getRouterStepId());
                    insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    insertMaterialLotLabCodeList.add(insertHmeMaterialLotLabCode);
                } else {
                    if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCodeList2.get(0).getEnableFlag())) {
                        HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                        updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCodeList2.get(0).getLabCodeId());
                        updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                        updateMaterialLotLabCodeList.add(updateHmeMaterialLotLabCode);
                    }
                }
            }

            //20210727 add by sanfeng.zhang for wenxin.zhang 创建设备数据
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(snJobEntity.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
            index++;
        }

        //V20201028 modify by penglin.sui 修改为调用批量API
        if(isBatchMoveInProcess){
            //V20201029 modify by penglin.sui for tianyang.xie 提前区分 当前步骤 + 工位 + 返修步骤标识，循环调用API
            Map<String, List<MtEoRouterActualVO42>> mtEoRouterActualVO42Map = mtEoRouterActualVO42List.stream().collect(Collectors.groupingBy(HmeEoJobSnUtils::fetchGroupKey));
            log.info("<====== HmeEoJobSnTimeServiceImpl.inSiteScanForNormal mtEoRouterActualVO42List.size={}, mtEoRouterActualVO42Map.size={}",
                    mtEoRouterActualVO42List.size(), mtEoRouterActualVO42Map.size());
            for (String key : mtEoRouterActualVO42Map.keySet()) {
                boolean firstFlag = true;
                List<MtEoRouterActualVO51> queueMessageList = new ArrayList<>();
                MtEoRouterActualVO42 mtEoRouterActualVO42 = new MtEoRouterActualVO42();
                for (MtEoRouterActualVO42 value : mtEoRouterActualVO42Map.get(key)) {
                    if(firstFlag){
                        mtEoRouterActualVO42.setReworkStepFlag(value.getReworkStepFlag());
                        mtEoRouterActualVO42.setSourceStatus(value.getSourceStatus());
                        firstFlag = false;
                    }
                    queueMessageList.addAll(value.getQueueMessageList());
                }
                mtEoRouterActualVO42.setQueueMessageList(queueMessageList);
                mtEoStepActualRepository.eoNextStepMoveInBatchProcess(tenantId, mtEoRouterActualVO42);
            }
        }

        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_IN");

        HmeEoJobSnVO14 hmeEoJobSnVO14 = new HmeEoJobSnVO14();
        hmeEoJobSnVO14.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSnVO14.setHmeEoJobSnVO15List(hmeEoJobSnVO15List);
        hmeEoJobSnRepository.eoBatchWorking(tenantId, hmeEoJobSnVO14, eventRequestId);

        //判断是容器还是条码,容器条码做容器相关操作
        if (HmeConstants.LoadTypeCode.CONTAINER.equals(dto.getSnType())) {
            //先尝试删除非当前工位的数据 add by yuchao.wang for tianyang.xie at 2020.9.19
            hmeEoJobContainerRepository.deleteNotCurrentWkcData(tenantId, dto.getContainerId(), dto.getWorkcellId());

            // 容器作业记录新增
            HmeEoJobContainer hmeEoJobContainer = new HmeEoJobContainer();
            hmeEoJobContainer.setTenantId(tenantId);
            hmeEoJobContainer.setWorkcellId(dto.getWorkcellId());
            hmeEoJobContainer.setJobContainerId(customSequence.getNextKey("hme_eo_job_container_s"));
            hmeEoJobContainer.setCid(Long.valueOf(customSequence.getNextKey("hme_eo_job_container_cid_s")));
            hmeEoJobContainer.setContainerId(dto.getContainerId());
            hmeEoJobContainer.setContainerCode(dto.getSnNum());
            hmeEoJobContainer.setSiteInBy(userId);
            hmeEoJobContainer.setSiteInDate(currentDate);
            hmeEoJobContainerRepository.insertSelective(hmeEoJobContainer);
            dto.setJobContainerId(hmeEoJobContainer.getJobContainerId());

            //如果容器需要在EoJobSn传容器ID
            if (CollectionUtils.isNotEmpty(insertList)) {
                for (HmeEoJobSn jobSn : insertList) {
                    jobSn.setJobContainerId(hmeEoJobContainer.getJobContainerId());
                }
            }
        }

        //批量新增EoJobSn
        hmeEoJobSnRepository.myBatchInsert(insertList);

        //批量加载数据采集项
        hmeEoJobDataRecordRepository.batchInitEoJobDataRecord(tenantId, dataRecordInitParamList);

        //V20210310 modify by penglin.sui for hui.am 记录实验代码
        if(CollectionUtils.isNotEmpty(insertMaterialLotLabCodeList)){
            int count = 0;
            List<String> labCodeIdS = customDbRepository.getNextKeys("hme_material_lot_lab_code_s", insertMaterialLotLabCodeList.size());
            List<String> labCodeCidS = customDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", insertMaterialLotLabCodeList.size());
            List<String> insertMaterialLotLabCodeSqlList = new ArrayList<>();
            for (HmeMaterialLotLabCode insertData : insertMaterialLotLabCodeList) {
                insertData.setLabCodeId(labCodeIdS.get(count));
                insertData.setCid(Long.valueOf(labCodeCidS.get(count)));
                insertData.setObjectVersionNumber(1L);
                insertData.setCreatedBy(userId);
                insertData.setLastUpdatedBy(userId);
                Date date = CommonUtils.currentTimeGet();
                insertData.setCreationDate(date);
                insertData.setLastUpdateDate(date);
                insertMaterialLotLabCodeSqlList.addAll(customDbRepository.getInsertSql(insertData));
                count++;
            }
            jdbcTemplate.batchUpdate(insertMaterialLotLabCodeSqlList.toArray(new String[insertMaterialLotLabCodeSqlList.size()]));
        }

        //更新实验代码
        if(CollectionUtils.isNotEmpty(updateMaterialLotLabCodeList)){
            List<String> labCodeCidS = customDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", updateMaterialLotLabCodeList.size());
            int count = 0;
            List<String> updateMaterialLotLabCodeSqlList = new ArrayList<>();
            for (HmeMaterialLotLabCode updateData : updateMaterialLotLabCodeList
            ) {
                updateData.setLastUpdatedBy(userId);
                updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                updateData.setCid(Long.valueOf(labCodeCidS.get(count)));
                updateMaterialLotLabCodeSqlList.addAll(customDbRepository.getUpdateSql(updateData));
                count++;
            }
            if (CollectionUtils.isNotEmpty(updateMaterialLotLabCodeSqlList)) {
                jdbcTemplate.batchUpdate(updateMaterialLotLabCodeSqlList.toArray(new String[updateMaterialLotLabCodeSqlList.size()]));
            }
        }
    }

    /**
     *
     * @Description 时效进站-返修进站
     *
     * @author yuchao.wang
     * @date 2020/12/21 15:14
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @param eoIdList eoIdList
     * @return void
     *
     */
    private void inSiteScanForRework(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16, List<String> eoIdList) {
        //取条码信息及上一步工艺
        Map<String, HmeEoJobSnVO5> snVoMap = hmeEoJobSnVO16.getEoJobSnMap();
        Map<String, HmeRouterStepVO2> nearStepMap = hmeEoJobSnVO16.getNearStepMap();
        Map<String, HmeRouterStepVO2> normalStepMap = hmeEoJobSnVO16.getNormalStepMap();
        Map<String, List<HmeRouterStepVO2>> allNormalStepMap = hmeEoJobSnVO16.getAllNormalStepMap();

        //取当前步骤信息
        Map<String, HmeRouterStepVO3> currentStepMap = hmeEoJobSnCommonService
                .batchQueryCurrentRouterStepForTime(tenantId, eoIdList, dto.getOperationId());

        //批量获取EoJobSn Id/Cid
        String eoJobSnId = customSequence.getNextKey("hme_eo_job_sn_s");
        String eoJobSnCid = customSequence.getNextKey("hme_eo_job_sn_cid_s");

        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        // 获取当前时间
        final Date currentDate = new Date();

        //取到当前条码的数据
        HmeEoJobSnVO3 jobSn = dto.getSnLineList().get(0);
        HmeEoJobSnVO5 snVO = snVoMap.get(jobSn.getMaterialLotId());
        HmeRouterStepVO2 normalRouterStep = normalStepMap.get(jobSn.getEoId());
        HmeRouterStepVO2 nearRouterStep = nearStepMap.get(jobSn.getEoId());
        HmeRouterStepVO3 currentRouterStep = currentStepMap.get(jobSn.getEoId());
        if (Objects.isNull(currentRouterStep) || StringUtils.isBlank(currentRouterStep.getRouterStepId())) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        //如果是返修则校验当前步骤是否在正常步骤内
        List<String> allNormalStepIdList = allNormalStepMap.get(jobSn.getEoId()).stream()
                .map(HmeRouterStepVO2::getRouterStepId).collect(Collectors.toList());
        if (!allNormalStepIdList.contains(currentRouterStep.getRouterStepId())) {
            // 请在正常加工工序步骤{正常加工步骤}前进行返修加工
            MtRouterStep routerStep = mtRouterStepRepository.routerStepGet(tenantId, normalRouterStep.getRouterStepId());
            throw new MtException("HME_EO_JOB_SN_036", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_036", "HME", routerStep.getStepName()));
        }

        //构造返回数据
        jobSn.setWorkcellId(dto.getWorkcellId());
        jobSn.setJobType(dto.getJobType());
        jobSn.setOperationId(dto.getOperationId());
        jobSn.setOperationIdList(dto.getOperationIdList());
        jobSn.setReworkFlag(snVO.getReworkFlag());
        jobSn.setEoStepId(currentRouterStep.getRouterStepId());
        jobSn.setSiteId(dto.getSiteId());
        jobSn.setWorkOrderId(snVO.getWorkOrderId());
        jobSn.setMaterialId(snVO.getMaterialId());

        log.info("当前jobSn对应eoId={},入口步骤标识:{}", jobSn.getEoId(), currentRouterStep.getEntryStepFlag());

        if (Objects.isNull(nearRouterStep) || StringUtils.isBlank(nearRouterStep.getRouterStepId())
                || Objects.isNull(normalRouterStep) || StringUtils.isBlank(normalRouterStep.getRouterStepId())) {
            // 当前为非入口步骤, 且根据EO没有找到最近加工步骤
            throw new MtException("HME_EO_JOB_SN_046",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_046", "HME"));
        }

        log.info("当前步骤:" + currentRouterStep.getRouterStepId() + ",最近一步骤:" + nearRouterStep.getRouterStepId()
                + ",最近一正常步骤:" + normalRouterStep.getRouterStepId());

        //执行工艺步骤移入
        MtEoRouterActualVO15 eoRouterActual = new MtEoRouterActualVO15();
        eoRouterActual.setRouterStepId(jobSn.getEoStepId());
        eoRouterActual.setEoId(jobSn.getEoId());
        eoRouterActual.setQty(snVO.getEoQty().doubleValue());
        eoRouterActual.setPreviousStepId(nearRouterStep.getEoStepActualId());
        eoRouterActual.setWorkcellId(nearRouterStep.getWipWorkcellId());
        eoRouterActual.setReworkStepFlag(HmeConstants.ConstantValue.YES);
        eoRouterActual.setSourceEoStepActualId(normalRouterStep.getEoStepActualId());
        HmeRouterStepVO nearStepVO = new HmeRouterStepVO();
        BeanUtils.copyProperties(nearRouterStep, nearStepVO);
        eoRouterActual.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
        mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, eoRouterActual);

        //工序排队加工
        eoWorking(tenantId, snVO.getEoQty(), jobSn);

        // 插入工序作业平台表
        List<HmeEoJobSn> insertList = new ArrayList<>();
        HmeEoJobSn snJobEntity = getSnJobEntity(tenantId, userId, currentDate, eoJobSnId, eoJobSnCid, jobSn);
        insertList.add(snJobEntity);
        hmeEoJobSnRepository.myBatchInsert(insertList);

        // 构造采集数据和自检数据插入参数，批量加载数据采集项
        List<HmeEoJobSnVO2> dataRecordInitParamList = new ArrayList<>();
        HmeEoJobSnVO2 dataRecordInitParam = new HmeEoJobSnVO2();
        BeanUtils.copyProperties(snJobEntity, dataRecordInitParam);
        dataRecordInitParam.setProductionVersion(snVO.getWoProductionVersion());
        dataRecordInitParam.setItemType(snVO.getItemType());
        dataRecordInitParamList.add(dataRecordInitParam);
        hmeEoJobDataRecordRepository.batchInitEoJobDataRecord(tenantId, dataRecordInitParamList);

        //20210727 add by sanfeng.zhang for wenxin.zhang 创建设备数据
        HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        equSwitchParams.setJobId(snJobEntity.getJobId());
        equSwitchParams.setWorkcellId(dto.getWorkcellId());
        equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
    }

    /**
     *
     * @Description 时效进站-指定工艺路线返修进站
     *
     * @author yuchao.wang
     * @date 2021/1/7 15:53
     * @param tenantId 租户ID
     * @param dto 参数
     * @param hmeEoJobSnVO16 参数
     * @return void
     *
     */
    private void inSiteScanForDesignedRework(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO16 hmeEoJobSnVO16) {
        //取条码信息及上一步工艺
        HmeEoRouterBomRelVO eoRouterBomRel = hmeEoJobSnVO16.getEoRouterBomRel();
        Map<String, HmeEoJobSnVO5> snVoMap = hmeEoJobSnVO16.getEoJobSnMap();
        Map<String, HmeRouterStepVO2> nearStepMap = hmeEoJobSnVO16.getNearStepMap();
        Map<String, HmeRouterStepVO2> normalStepMap = hmeEoJobSnVO16.getNormalStepMap();

        //批量获取EoJobSn Id/Cid
        String eoJobSnId = customSequence.getNextKey("hme_eo_job_sn_s");
        String eoJobSnCid = customSequence.getNextKey("hme_eo_job_sn_cid_s");

        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        // 获取当前时间
        final Date currentDate = new Date();

        //取到当前条码的数据
        HmeEoJobSnVO3 jobSn = dto.getSnLineList().get(0);
        HmeEoJobSnVO5 snVO = snVoMap.get(jobSn.getMaterialLotId());
        HmeRouterStepVO2 normalRouterStep = normalStepMap.get(jobSn.getEoId());
        HmeRouterStepVO2 nearRouterStep = nearStepMap.get(jobSn.getEoId());

        //取当前步骤信息
        HmeRouterStepVO5 currentRouterStep = hmeEoJobSnCommonService.queryCurrentAndNextStepForDesignedRework(tenantId, dto.getOperationId(), eoRouterBomRel.getRouterId(), jobSn.getEoId());
        if (Objects.isNull(currentRouterStep) || StringUtils.isBlank(currentRouterStep.getRouterStepId())) {
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        //防跳站校验
        HmeRouterStepVO5 previousStep = hmeEoJobSnCommonService.queryPreviousStepForDesignedRework(tenantId, jobSn.getEoId(), eoRouterBomRel.getRouterId(), eoRouterBomRel.getCreationDate());

        //找到上一步骤才进行校验，没找到说明是首次进站
        if (Objects.nonNull(previousStep) && StringUtils.isNotBlank(previousStep.getRouterStepId())) {
            log.info("当前步骤:" + currentRouterStep.getRouterStepId() + ",上一步骤:" + previousStep.getRouterStepId());
            if (StringUtils.isBlank(previousStep.getNextStepId())) {
                //找不到步骤${1}的下一步骤,请检查!
                throw new MtException("HME_EO_JOB_SN_181", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_181", "HME", previousStep.getStepName()));
            }
            if (!currentRouterStep.getRouterStepId().equals(previousStep.getNextStepId())) {
                //本道工序步骤不为工序步骤【${1}】的下一步骤
                throw new MtException("HME_EO_JOB_SN_032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_032", "HME", previousStep.getStepName()));
            }
        }

        //构造返回数据
        jobSn.setWorkcellId(dto.getWorkcellId());
        jobSn.setJobType(dto.getJobType());
        jobSn.setOperationId(dto.getOperationId());
        jobSn.setOperationIdList(dto.getOperationIdList());
        jobSn.setReworkFlag(snVO.getReworkFlag());
        jobSn.setEoStepId(currentRouterStep.getRouterStepId());
        jobSn.setSiteId(dto.getSiteId());
        jobSn.setWorkOrderId(snVO.getWorkOrderId());
        jobSn.setMaterialId(snVO.getMaterialId());

        log.info("当前jobSn对应eoId={},入口步骤标识:{}", jobSn.getEoId(), currentRouterStep.getEntryStepFlag());

        if (Objects.isNull(nearRouterStep) || StringUtils.isBlank(nearRouterStep.getRouterStepId())
                || Objects.isNull(normalRouterStep) || StringUtils.isBlank(normalRouterStep.getRouterStepId())) {
            // 当前为非入口步骤, 且根据EO没有找到最近加工步骤
            throw new MtException("HME_EO_JOB_SN_046",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_046", "HME"));
        }

        log.info("当前步骤:" + currentRouterStep.getRouterStepId() + ",最近一步骤:" + nearRouterStep.getRouterStepId()
                + ",最近一正常步骤:" + normalRouterStep.getRouterStepId());

        //执行工艺步骤移入
        MtEoRouterActualVO15 eoRouterActual = new MtEoRouterActualVO15();
        eoRouterActual.setRouterStepId(jobSn.getEoStepId());
        eoRouterActual.setEoId(jobSn.getEoId());
        eoRouterActual.setQty(snVO.getEoQty().doubleValue());
        eoRouterActual.setPreviousStepId(nearRouterStep.getEoStepActualId());
        eoRouterActual.setWorkcellId(nearRouterStep.getWipWorkcellId());
        eoRouterActual.setReworkStepFlag(HmeConstants.ConstantValue.YES);
        eoRouterActual.setSourceEoStepActualId(normalRouterStep.getEoStepActualId());
        HmeRouterStepVO nearStepVO = new HmeRouterStepVO();
        BeanUtils.copyProperties(nearRouterStep, nearStepVO);
        eoRouterActual.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
        mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, eoRouterActual);

        //工序排队加工
        eoWorking(tenantId, snVO.getEoQty(), jobSn);

        // 插入工序作业平台表
        List<HmeEoJobSn> insertList = new ArrayList<>();
        HmeEoJobSn snJobEntity = getSnJobEntity(tenantId, userId, currentDate, eoJobSnId, eoJobSnCid, jobSn);
        insertList.add(snJobEntity);
        hmeEoJobSnRepository.myBatchInsert(insertList);

        // 构造采集数据和自检数据插入参数，批量加载数据采集项
        List<HmeEoJobSnVO2> dataRecordInitParamList = new ArrayList<>();
        HmeEoJobSnVO2 dataRecordInitParam = new HmeEoJobSnVO2();
        BeanUtils.copyProperties(snJobEntity, dataRecordInitParam);
        dataRecordInitParam.setProductionVersion(snVO.getWoProductionVersion());
        dataRecordInitParam.setItemType(snVO.getItemType());
        dataRecordInitParamList.add(dataRecordInitParam);
        hmeEoJobDataRecordRepository.batchInitEoJobDataRecord(tenantId, dataRecordInitParamList);

        //20210727 add by sanfeng.zhang for wenxin.zhang 创建设备数据
        HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        equSwitchParams.setJobId(snJobEntity.getJobId());
        equSwitchParams.setWorkcellId(dto.getWorkcellId());
        equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
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
        List<MtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                mtEoRouterActualMapper.eoOperationLimitCurrentRouterStepGet(tenantId, routerActualParam);
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
        mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);

        // 工序在制
        MtEoRouterActualVO19 workingParam = new MtEoRouterActualVO19();
        workingParam.setEventRequestId(eventRequestId);
        workingParam.setEoStepActualId(eoStepActualId);
        workingParam.setWorkcellId(snLine.getWorkcellId());
        workingParam.setQty(new Double(String.valueOf(eoQty)));
        workingParam.setSourceStatus("QUEUE");
        workingParam.setLastWorkcellId(snLine.getWorkcellId());
        mtEoRouterActualRepository.eoWkcAndStepWorking(tenantId, workingParam);
    }

    /**
     *
     * @Description 默认输入校验
     *
     * @author yuchao.wang
     * @date 2020/11/5 14:11
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    private void defaultInputVerification(Long tenantId, HmeEoJobSnVO3 dto) {
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位ID"));
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺ID"));
        }
        if (StringUtils.isBlank(dto.getSiteId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "站点ID"));
        }
        if (StringUtils.isBlank(dto.getJobType())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "作业类型"));
        }
        if (StringUtils.isBlank(dto.getSnType())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "条码类型"));
        }
        if (CollectionUtils.isEmpty(dto.getSnLineList())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "条码列表"));
        }
    }

    /**
     *
     * @Description 时效作业-出炉
     *
     * @author yuchao.wang
     * @date 2020/11/5 13:59
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnTimeDTO outSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeServiceImpl.outSiteScan.Start tenantId={},dto={}", tenantId, dto);
        // 非空校验
        defaultInputVerification(tenantId, dto);
        dto.setOutSiteAction(HmeConstants.OutSiteAction.COMPLETE);

        //获取所有eoId jobId
        List<String> eoIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getEoId).distinct().collect(Collectors.toList());
        List<String> jobIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getJobId).distinct().collect(Collectors.toList());

        HmeEoJobSnTimeDTO validateResult = outSiteScanValidate(tenantId, dto, jobIdList);
        if (StringUtils.isNotBlank(validateResult.getErrorCode())) {
            return validateResult;
        }

        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        // 获取当前时间
        final Date currentDate = new Date();

        //判断是否容器出站
        if (HmeConstants.SnType.CONTAINER.equals(dto.getSnType())) {
            //如果是容器则对容器出站
            if (StringUtils.isBlank(dto.getJobContainerId())) {
                throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_INSPECT_PLATFORM_001", "HME", "JobContainerId"));
            }

            HmeEoJobContainer jobContainer = new HmeEoJobContainer();
            jobContainer.setJobContainerId(dto.getJobContainerId());
            jobContainer.setSiteOutBy(userId);
            jobContainer.setSiteOutDate(currentDate);
            hmeEoJobContainerMapper.updateByPrimaryKeySelective(jobContainer);
        } else if (HmeConstants.SnType.MATERIAL_LOT.equals(dto.getSnType())) {
            //如果是物料批并且有容器则先卸载
            HmeMaterialLotVO3 hmeMaterialLot = validateResult.getMaterialLotList().get(0);
            if (StringUtils.isNotBlank(hmeMaterialLot.getCurrentContainerId())) {
                MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
                mtContainerVO25.setContainerId(hmeMaterialLot.getCurrentContainerId());
                mtContainerVO25.setLoadObjectId(hmeMaterialLot.getMaterialLotId());
                mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
            }
        } else if (HmeConstants.SnType.EQUIPMENT.equals(dto.getSnType())) {
            //如果是设备则分别对容器和条码做操作,先筛选容器条码做容器出站
            List<String> jobContainerIdList = validateResult.getHmeEoJobSnEntityList().stream()
                    .filter(item -> StringUtils.isNotBlank(item.getJobContainerId())).map(HmeEoJobSn::getJobContainerId).distinct().collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(jobContainerIdList)) {
                hmeEoJobContainerRepository.batchOutSite(tenantId, userId, jobContainerIdList);
            }

            //再筛选条码做条码卸载
            List<String> materialLotIdList = validateResult.getHmeEoJobSnEntityList().stream()
                    .filter(item -> StringUtils.isBlank(item.getJobContainerId())).map(HmeEoJobSn::getMaterialLotId).distinct().collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(materialLotIdList)) {
                List<HmeMaterialLotVO3> materialLotList = validateResult.getMaterialLotList().stream().filter(item ->
                        materialLotIdList.contains(item.getMaterialLotId()) && StringUtils.isNotBlank(item.getCurrentContainerId()))
                        .collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(materialLotList)) {
                    for (HmeMaterialLotVO3 materialLot : materialLotList) {
                        MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
                        mtContainerVO25.setContainerId(materialLot.getCurrentContainerId());
                        mtContainerVO25.setLoadObjectId(materialLot.getMaterialLotId());
                        mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                        mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
                    }
                }
            }
        }

        Map<String,HmeEoJobDataRecordVO2> hmeEoJobDataRecordVO2Map = hmeEoJobDataRecordRepository.queryNcRecord(tenantId,jobIdList);
        //20210727 modify by sanfeng.zhang for wenxin.zhang 保存设备数据
        hmeEoJobEquipmentRepository.batchSaveEquipmentRecordForOutSite(tenantId, dto);
        //判断是否为指定工艺路线返修 加工完成
        if (HmeConstants.ConstantValue.YES.equals(validateResult.getDesignedReworkFlag())) {
            //调用指定工艺路线返修加工完成通用主方法,返修只能单个条码
            HmeEoJobSnVO24 hmeEoJobSnVO24 = new HmeEoJobSnVO24();
            hmeEoJobSnVO24.setUserId(userId);
            hmeEoJobSnVO24.setReworkEo(validateResult.getReworkEo());
            HmeEoJobSnVO16 hmeEoJobSnVO16 = hmeEoJobSnCommonService.batchQueryEoAndWoInfoById(tenantId, Collections.singletonList(validateResult.getReworkEo().getEoId()));
            hmeEoJobSnVO24.setWoMap(hmeEoJobSnVO16.getWoMap());
            hmeEoJobSnVO24.setReworkMaterialLot(validateResult.getMaterialLotList().get(0));
            hmeEoJobSnVO24.setHmeEoJobSnEntity(validateResult.getHmeEoJobSnEntityList().get(0));
            hmeEoJobSnVO24.setNcJobDataRecordMap(hmeEoJobDataRecordVO2Map);
            hmeEoJobSnCommonService.batchMainOutSiteForDesignedReworkComplete(tenantId, dto, hmeEoJobSnVO24);
        } else {
            //批量查询EO、WO相关信息
            HmeEoJobSnVO16 hmeEoJobSnVO16 = hmeEoJobSnCommonService.batchQueryEoAndWoInfoById(tenantId, eoIdList);
            Map<String, HmeEoVO4> eoMap = hmeEoJobSnVO16.getEoMap();
            hmeEoJobSnVO16.setUserId(userId);
            hmeEoJobSnVO16.setCurrentDate(currentDate);
            hmeEoJobSnVO16.setHmeEoJobSnEntityList(validateResult.getHmeEoJobSnEntityList());
            hmeEoJobSnVO16.setNcJobDataRecordMap(hmeEoJobDataRecordVO2Map);
            //批量执行订单完成
            hmeEoJobSnCommonService.siteOutBatchComplete(tenantId, dto.getWorkcellId(), eoIdList, eoMap);

            //获取条码返修标识
            Map<String, String> reworkFlagMap = new HashMap<>();
            Map<String, HmeMaterialLotVO3> materialLotMap = new HashMap<>();
            validateResult.getMaterialLotList().forEach(item -> {
                reworkFlagMap.put(item.getMaterialLotId(), item.getReworkFlag());
                materialLotMap.put(item.getMaterialLotId() , item);
            });
            hmeEoJobSnVO16.setReworkFlagMap(reworkFlagMap);
            hmeEoJobSnVO16.setMaterialLotMap(materialLotMap);

            //调用出站通用主方法
            List<WmsObjectTransactionResponseVO> transactionResponseList = hmeEoJobSnCommonService.batchMainOutSite(tenantId, dto, hmeEoJobSnVO16);

            //批量更新出站
            hmeEoJobSnRepository.batchOutSite2(tenantId, userId, jobIdList, dto.getRemark());

            //完工数据统计
            hmeEoJobSnCommonService.batchWkcCompleteOutputRecord(tenantId, dto, hmeEoJobSnVO16);

            //发送实时接口
            hmeEoJobSnCommonService.sendTransactionInterface(tenantId, transactionResponseList);
        }
        log.info("<====== HmeEoJobSnTimeServiceImpl.outSiteScan.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
        return new HmeEoJobSnTimeDTO();
    }

    /**
     *
     * @Description 出站(加工完成/继续返修)校验
     *
     * @author yuchao.wang
     * @date 2020/12/24 10:15
     * @return com.ruike.hme.api.dto.HmeEoJobSnTimeDTO
     *
     */
    private HmeEoJobSnTimeDTO outSiteScanValidate(Long tenantId, HmeEoJobSnVO3 dto, List<String> jobIdList) {
        HmeEoJobSnTimeDTO result = new HmeEoJobSnTimeDTO();

        //获取所有条码Id
        List<String> materialLotIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getMaterialLotId).distinct().collect(Collectors.toList());

        //根据JobId查询EoJobSn数据
        List<HmeEoJobSn> hmeEoJobSnEntityList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeEoJobSn.FIELD_JOB_ID, jobIdList)).build());
        if (CollectionUtils.isEmpty(hmeEoJobSnEntityList) || hmeEoJobSnEntityList.size() != jobIdList.size()) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "作业信息"));
        }

        //校验同批次是否有返修标识不一致的条码 时效增加返修逻辑 modify by yuchao.wang for tianyang.xie at 2020.12.21
        List<String> reworkFlagList = hmeEoJobSnEntityList.stream().map(item -> HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())
                ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO).distinct().collect(Collectors.toList());
        if (reworkFlagList.size() > 1) {
            // 正常加工sn与返修sn不允许同时进/出站!
            throw new MtException("HME_EO_JOB_SN_152", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_152", "HME"));
        }

        //查询条码信息
        List<HmeMaterialLotVO3> materialLotList = hmeEoJobSnCommonService.batchQueryMaterialLotReworkFlagForTime(tenantId, materialLotIdList);

        //校验同批次是否有返修标识不一致的条码
        reworkFlagList = materialLotList.stream().map(item -> HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())
                ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO).distinct().collect(Collectors.toList());
        if (reworkFlagList.size() > 1) {
            // 正常加工sn与返修sn不允许同时进/出站!
            throw new MtException("HME_EO_JOB_SN_152", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_152", "HME"));
        }
        String reworkFlag = reworkFlagList.get(0);
        String designedReworkFlag = HmeConstants.ConstantValue.NO;

        //V20210630 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可出站
        StringBuilder materialLots = new StringBuilder();
        for(HmeMaterialLotVO3 item : materialLotList){
            if(StringUtils.isNotBlank(item.getStocktakeFlag()) &&
                    HmeConstants.ConstantValue.YES.equals(item.getStocktakeFlag())){
                if(materialLots.length() == 0){
                    materialLots.append(item.getMaterialLotCode());
                }else{
                    materialLots.append("," + item.getMaterialLotCode());
                }
            }
        }
        if(materialLots.length() > 0){
            //此SN【${1}】正在盘点,不可出站
            throw new MtException("HME_EO_JOB_SN_204", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_204", "HME", materialLots.toString()));
        }

        //如果是返修则只能返修一个条码 不支持批量返修
        if (HmeConstants.ConstantValue.YES.equals(reworkFlag)) {
            if (HmeConstants.SnType.CONTAINER.equals(dto.getSnType())
                    || HmeConstants.SnType.EQUIPMENT.equals(dto.getSnType()) || dto.getSnLineList().size() > 1) {
                String materialLotCode = "";
                Optional<HmeMaterialLotVO3> reworkMaterialLot = materialLotList.stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getReworkFlag())).findAny();
                if (reworkMaterialLot.isPresent()) {
                    materialLotCode = reworkMaterialLot.get().getMaterialLotCode();
                }
                //当前容器下SN${1}已判定不良,请按照SN编码单独入炉!
                throw new MtException("HME_EO_JOB_SN_154", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_154", "HME", materialLotCode));
            }

            //判断是否为指定工艺路线返修 add by yuchao.wang for tianyang.xie at 2021.1.8
            if (HmeConstants.ConstantValue.YES.equals(materialLotList.get(0).getDesignedReworkFlag())) {
                designedReworkFlag = HmeConstants.ConstantValue.YES;
            }
        }

        //校验质量状态为NG的条码是否返修
        List<HmeMaterialLotVO3> ngMaterialLotList = materialLotList.stream()
                .filter(item -> HmeConstants.ConstantValue.NG.equals(item.getQualityStatus())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(ngMaterialLotList)) {
            ngMaterialLotList.forEach(ngMaterialLot -> {
                //获取条码对应的作业的返修状态
                Optional<HmeEoJobSn> currJobSn = hmeEoJobSnEntityList.stream().filter(jobSn -> ngMaterialLot.getMaterialLotId().equals(jobSn.getMaterialLotId())).findFirst();
                String jobSnReworkFlag = currJobSn.isPresent() ? currJobSn.get().getReworkFlag() : HmeConstants.ConstantValue.NO;

                if (!HmeConstants.ConstantValue.YES.equals(ngMaterialLot.getReworkFlag())
                        && !HmeConstants.ConstantValue.YES.equals(jobSnReworkFlag)) {
                    //正常加工过程中不允许不良产品出站
                    throw new MtException("HME_EO_JOB_SN_060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_060", "HME"));
                }

                if (!HmeConstants.ConstantValue.YES.equals(jobSnReworkFlag)) {
                    //请先返修进站
                    throw new MtException("HME_EO_JOB_SN_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_061", "HME"));
                }
            });
        }

        //校验EoJobSn是否已经出站，有则报错
        List<HmeEoJobSn> siteOutList = hmeEoJobSnEntityList.stream().filter(item -> Objects.nonNull(item.getSiteOutDate()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(siteOutList)) {
            //已完成SN号不允许重复完成
            throw new MtException("HME_EO_JOB_SN_056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_056", "HME"));
        }

        //返修相关校验
        if (HmeConstants.ConstantValue.YES.equals(reworkFlag)) {
            //如果是返修，并且是点的加工完成，则需要给用户确认框
            if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                if (!HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag())) {
                    //指定工艺路线返修与普通返修 加工完成报错不同
                    if (HmeConstants.ConstantValue.YES.equals(designedReworkFlag)) {
                        //产品将结束指定工艺路线返修,是否确认
                        result.setErrorCode("HME_EO_JOB_SN_164");
                        result.setErrorMessage(mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_164", "HME"));
                    } else {
                        //产品将返修完成,后续以正常加工的方式继续生产, 是否确认
                        result.setErrorCode("HME_EO_JOB_SN_038");
                        result.setErrorMessage(mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_038", "HME"));
                    }
                    return result;
                }
            }

            //点继续返修 或者 指定工艺路线返修加工完成
            if (!HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction()) || HmeConstants.ConstantValue.YES.equals(designedReworkFlag)) {
                //由于不需要用eo/wo信息，只需要校验是否存在，不需要查询
                String eoId = dto.getSnLineList().get(0).getEoId();
                SecurityTokenHelper.close();
                List<MtEo> eoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class)
                        .select(MtEo.FIELD_EO_ID, MtEo.FIELD_WORK_ORDER_ID, MtEo.FIELD_QTY)
                        .andWhere(Sqls.custom().andEqualTo(MtEo.FIELD_EO_ID, eoId)).build());
                if (CollectionUtils.isEmpty(eoList) || Objects.isNull(eoList.get(0)) || StringUtils.isBlank(eoList.get(0).getEoId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "EO信息"));
                }
                result.setReworkEo(eoList.get(0));

                String workOrderId = eoList.get(0).getWorkOrderId();
                SecurityTokenHelper.close();
                int recordCount = mtWorkOrderRepository.selectCountByCondition(Condition.builder(MtWorkOrder.class)
                        .andWhere(Sqls.custom().andEqualTo(MtWorkOrder.FIELD_WORK_ORDER_ID, workOrderId)).build());
                if (recordCount < 1) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "工单信息"));
                }
            }
        }

        // 判断是否需要校验数据采集结果 add by yuchao.wang for peng.zhao at 2021.1.26
        boolean dataRecordValidateFlag = true;
        if (HmeConstants.OutSiteAction.REWORK.equals(dto.getOutSiteAction())
                || (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction()) && HmeConstants.ConstantValue.YES.equals(reworkFlag))) {
            dataRecordValidateFlag = false;
        }

        //校验数据采集项 add by yuchao.wang for peng.zhao at 2021.1.26
        if (dataRecordValidateFlag && hmeEoJobSnCommonService.hasMissingValue(tenantId, dto.getWorkcellId(), jobIdList)) {
            // 出站失败,存在未记录结果的数据采集记录
            throw new MtException("HME_EO_JOB_SN_009", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_009", "HME"));
        }

//        if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
            //校验数据采集项不良判定 add by yuchao.wang for peng.zhao at 2021.1.26
            if (!HmeConstants.ConstantValue.YES.equals(dto.getContinueFlag()) || !"HME_EO_JOB_SN_172".equals(dto.getErrorCode())) {
                //查询所有有结果的数据采集项
                List<HmeEoJobDataRecord> allEoJobDataRecordList = hmeEoJobDataRecordRepository
                        .batchQueryForNcRecordValidate(tenantId, dto.getWorkcellId(), jobIdList);
                if (CollectionUtils.isNotEmpty(allEoJobDataRecordList)) {
                    StringBuilder errorMessages = new StringBuilder();
                    List<HmeProcessNcDetailVO2> processNcDetailList = new ArrayList<>();
                    //根据工艺判断是器件不良还是老化不良
                    if (hmeEoJobSnCommonService.isAgeingNc(tenantId, dto.getOperationId())) {
                        //老化不良
                        //查询工序信息
                        String stationId = hmeEoJobSnMapper.queryWkcStation(tenantId, dto.getSiteId(), dto.getWorkcellId());

                        Map<String, HmeMaterialLotVO3> materialLotMap = new HashMap<String, HmeMaterialLotVO3>();
                        List<String> materialIdList = new ArrayList<>();
                        for (HmeMaterialLotVO3 item : materialLotList
                             ) {
                            materialLotMap.put(item.getMaterialLotId(), item);
                            materialIdList.add(item.getMaterialId());
                        }

                        //查询所有工序不良信息
                        List<HmeProcessNcHeaderVO2> processNcInfoList = hmeProcessNcHeaderRepository
                                .batchQueryProcessNcInfoForAgeingNcRecordValidate(tenantId, dto.getOperationId(),materialIdList);

                        //对所有的数据采集项按照jobId分组
                        Map<String, List<HmeEoJobDataRecord>> eoJobDataRecordMap = allEoJobDataRecordList
                                .stream().collect(Collectors.groupingBy(HmeEoJobDataRecord::getJobId));

                        //遍历所有作业校验采集项
                        for (HmeEoJobSn eoJobSn : hmeEoJobSnEntityList) {
                            if (!eoJobDataRecordMap.containsKey(eoJobSn.getJobId())) {
                                continue;
                            }

                            //获取当前作业的条码及数据采集项
                            HmeMaterialLotVO3 materialLot = materialLotMap.get(eoJobSn.getMaterialLotId());
                            String materialLotCode = materialLot.getMaterialLotCode();
                            List<HmeEoJobDataRecord> currEoJobDataRecordList = eoJobDataRecordMap.get(eoJobSn.getJobId());

                            //V20210607 modify by penglin.sui for peng.zhao 先根据【material_id】查，若有多笔：则根据【material_id】+【cos_model】，若唯一则取此数据；若有多笔，继续根据【material_id】+【cos_model】+【workcell_id】查，若唯一则取此数据，若多比，则报错HME_EO_JOB_SN_202
                            List<HmeProcessNcHeaderVO2> subProcessNcInfoList = processNcInfoList.stream().filter(item -> item.getMaterialId().equals(materialLot.getMaterialId())).collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(subProcessNcInfoList)){

                                List<String> headerIdList = subProcessNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());

                                if(headerIdList.size() > 1) {
                                    String cosModel = materialLotCode.substring(4, 5);
                                    subProcessNcInfoList = subProcessNcInfoList.stream().filter(item -> StringUtils.isNotBlank(item.getCosModel()) && item.getCosModel().equals(cosModel)).collect(Collectors.toList());

                                    if (CollectionUtils.isEmpty(subProcessNcInfoList)) {
                                        // 	此SN【${1}】查询到多条判定标准,请检查!
                                        throw new MtException("HME_EO_JOB_SN_202", mtErrorMessageRepository
                                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_202", "HME",
                                                        materialLot.getMaterialCode()));
                                    }

                                    headerIdList = subProcessNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());

                                    if (headerIdList.size() > 1) {
                                        subProcessNcInfoList = subProcessNcInfoList.stream().filter(item -> StringUtils.isNotBlank(item.getStationId()) && item.getStationId().equals(stationId)).collect(Collectors.toList());

                                        if(CollectionUtils.isNotEmpty(subProcessNcInfoList)){
                                            headerIdList = subProcessNcInfoList.stream().map(HmeProcessNcHeaderVO2::getHeaderId).distinct().collect(Collectors.toList());
                                        }

                                        if (CollectionUtils.isEmpty(subProcessNcInfoList) || headerIdList.size() > 1) {
                                            // 	此SN【${1}】查询到多条判定标准,请检查!
                                            throw new MtException("HME_EO_JOB_SN_202", mtErrorMessageRepository
                                                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_202", "HME",
                                                            materialLot.getMaterialCode()));
                                        }
                                    }
                                }
                            }

                            HmeProcessNcHeaderVO2 processNcInfo = null;
                            if(CollectionUtils.isNotEmpty(subProcessNcInfoList)){
                                processNcInfo = subProcessNcInfoList.get(0);
                            }

                            //当前作业的数据采集项进行校验
                            HmeEoJobSn dataRecordValidateResult = hmeEoJobSnCommonService
                                    .dataRecordAgeingProcessNcValidate(tenantId, materialLotCode, currEoJobDataRecordList, processNcInfo);
                            if (Objects.nonNull(dataRecordValidateResult) && StringUtils.isNotBlank(dataRecordValidateResult.getErrorCode())) {
                                result.setErrorCode(dataRecordValidateResult.getErrorCode());
//                                result.setErrorMessage(dataRecordValidateResult.getErrorMessage());

                                //V20210408 modify by penglin.sui for peng.zhao 新增工序不良参数返回，根据工序判定结果去自动发起不良
                                dataRecordValidateResult.getHmeNcDisposePlatformDTO().setWorkcellId(dto.getWorkcellId());
                                dataRecordValidateResult.getHmeNcDisposePlatformDTO().setCurrentwWorkcellId(dto.getWorkcellId());
                                dataRecordValidateResult.getHmeNcDisposePlatformDTO().setFlag("2");

                                result.setHmeNcDisposePlatformDTO(dataRecordValidateResult.getHmeNcDisposePlatformDTO());

                                if(errorMessages.length() == 0){
                                    errorMessages.append(dataRecordValidateResult.getErrorMessage());
                                }else{
                                    errorMessages.append("<br>" +  dataRecordValidateResult.getErrorMessage());
                                }

                                dataRecordValidateResult.getProcessNcDetailList().forEach(item -> {
                                    HmeProcessNcDetailVO2 processNcDetail = new HmeProcessNcDetailVO2();
                                    BeanUtils.copyProperties(item , processNcDetail);
                                    processNcDetail.setJobId(eoJobSn.getJobId());
                                    processNcDetailList.add(processNcDetail);
                                });

//                                return result;
                            }
                        }

                        //V20210521 modify by penglin.sui for peng.zhao 将全部的不良返回给前台，提示用户
                        if(errorMessages.length() > 0) {
                            result.setErrorMessage(errorMessages.toString());
                            result.setProcessNcDetailList(processNcDetailList);
                            return result;
                        }

                    } else {
                        //器件不良
                        //查询所有工序不良信息
                        List<String> materialIdList = materialLotList.stream()
                                .map(MtMaterialLot::getMaterialId).distinct().collect(Collectors.toList());
                        Map<String, HmeProcessNcHeaderVO2> processNcInfoMap = hmeProcessNcHeaderRepository
                                .batchQueryProcessNcInfoForNcRecordValidate(tenantId, dto.getOperationId(), materialIdList);

                        //对所有的数据采集项按照jobId分组
                        Map<String, List<HmeEoJobDataRecord>> eoJobDataRecordMap = allEoJobDataRecordList
                                .stream().collect(Collectors.groupingBy(HmeEoJobDataRecord::getJobId));

                        Map<String, HmeMaterialLotVO3> materialLotMap = new HashMap<String, HmeMaterialLotVO3>();
                        materialLotList.forEach(item -> materialLotMap.put(item.getMaterialLotId(), item));

                        //遍历所有作业校验采集项
                        for (HmeEoJobSn eoJobSn : hmeEoJobSnEntityList) {
                            if (!eoJobDataRecordMap.containsKey(eoJobSn.getJobId())) {
                                continue;
                            }

                            //获取当前产品编码和cos类型
                            HmeMaterialLotVO3 materialLot = materialLotMap.get(eoJobSn.getMaterialLotId());
                            String materialLotCode = materialLot.getMaterialLotCode();
                            String key = materialLot.getMaterialId() + "," + materialLotCode.substring(2, 4) + "," + materialLotCode.substring(4, 5) + "," + materialLotCode.substring(5, 6);
                            HmeProcessNcHeaderVO2 processNcInfo = processNcInfoMap.getOrDefault(key , null);
                            if(Objects.isNull(processNcInfo)){
                                key = materialLot.getMaterialId() + "," + materialLotCode.substring(2, 4) + "," + materialLotCode.substring(4, 5) + ",";
                                processNcInfo = processNcInfoMap.get(key);
                            }

                            //获取当前作业的数据采集项进行校验
                            List<HmeEoJobDataRecord> currEoJobDataRecordList = eoJobDataRecordMap.get(eoJobSn.getJobId());
                            HmeEoJobSn dataRecordValidateResult = hmeEoJobSnCommonService
                                    .dataRecordProcessNcValidate(tenantId, materialLotCode, currEoJobDataRecordList, processNcInfo);
                            if (Objects.nonNull(dataRecordValidateResult) && StringUtils.isNotBlank(dataRecordValidateResult.getErrorCode())) {
                                result.setErrorCode(dataRecordValidateResult.getErrorCode());
//                                result.setErrorMessage(dataRecordValidateResult.getErrorMessage());

                                //V20210408 modify by penglin.sui for peng.zhao 新增工序不良参数返回，根据工序判定结果去自动发起不良
                                dataRecordValidateResult.getHmeNcDisposePlatformDTO().setWorkcellId(dto.getWorkcellId());
                                dataRecordValidateResult.getHmeNcDisposePlatformDTO().setCurrentwWorkcellId(dto.getWorkcellId());
                                dataRecordValidateResult.getHmeNcDisposePlatformDTO().setFlag("2");

                                result.setHmeNcDisposePlatformDTO(dataRecordValidateResult.getHmeNcDisposePlatformDTO());

                                if(errorMessages.length() == 0){
                                    errorMessages.append(dataRecordValidateResult.getErrorMessage());
                                }else{
                                    errorMessages.append("<br>" +  dataRecordValidateResult.getErrorMessage());
                                }
                                dataRecordValidateResult.getProcessNcDetailList().forEach(item -> {
                                    HmeProcessNcDetailVO2 processNcDetail = new HmeProcessNcDetailVO2();
                                    BeanUtils.copyProperties(item , processNcDetail);
                                    processNcDetail.setJobId(eoJobSn.getJobId());
                                    processNcDetailList.add(processNcDetail);
                                });

//                                return result;
                            }
                        }
                        //V20210521 modify by penglin.sui for peng.zhao 将全部的不良返回给前台，提示用户
                        if(errorMessages.length() > 0) {
                            result.setErrorMessage(errorMessages.toString());
                            result.setProcessNcDetailList(processNcDetailList);
                            return result;
                        }
                    }
                }
            }
//        }

        result.setHmeEoJobSnEntityList(hmeEoJobSnEntityList);
        result.setMaterialLotList(materialLotList);
        result.setReworkFlag(reworkFlag);
        result.setDesignedReworkFlag(designedReworkFlag);
        return result;
    }

    /**
     *
     * @Description 分页查询数据采集项
     *
     * @author yuchao.wang
     * @date 2020/11/23 18:59
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 翻页参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2>
     *
     */
    @Override
    public Page<HmeEoJobDataRecordVO2> queryDataRecord(Long tenantId, HmeEoJobDataRecordQueryDTO dto, PageRequest pageRequest) {
        log.info("<====== HmeEoJobSnTimeServiceImpl.queryDataRecord.Start tenantId={},dto={}", tenantId, dto);
        //非空校验
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工位ID"));
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺ID"));
        }
        if (StringUtils.isBlank(dto.getMaterialLotCode()) && StringUtils.isBlank(dto.getContainerCode()) && StringUtils.isBlank(dto.getEquipmentCode())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "器件编码或容器编码或设备编码"));
        }
        if (CollectionUtils.isEmpty(dto.getTagIdList())) {
            dto.setTagIdList(null);
        }
        /*List<HmeEoJobDataRecordVO3> jobSnList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getJobContainerId())) {
            //如果jobContainerId不为空，则为点击容器卡片查询
            jobSnList = hmeEoJobDataRecordMapper.queryAllEoJobSnByJobContainerId(tenantId, dto.getWorkcellId(), dto.getJobContainerId());
        } else if (StringUtils.isNotBlank(dto.getJobId())) {
            //如果jobId不为空，则为点击条码卡片查询
            jobSnList = hmeEoJobDataRecordMapper.queryAllEoJobSnByJobId(tenantId, dto.getJobId());
        } else if (StringUtils.isNotBlank(dto.getContainerCode()) || StringUtils.isNotBlank(dto.getMaterialLotCode())){
            //否则按照界面查询条件查询
            jobSnList = hmeEoJobDataRecordMapper.queryAllEoJobSnByCondition(tenantId, dto);
        } else {
            //报错条件必输
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "器件SN或容器SN"));
        }*/

        //如果输入了设备编码
        List<String> snList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getEquipmentCode())) {
            //查询值集HME.OPERATION_ITF_TABLE,查询工艺对应的接口表表名
            List<String> operationItfTableList = new ArrayList<>();
            List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.OPERATION_ITF_TABLE", tenantId);
            if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
                operationItfTableList = lovValueDTOList.stream()
                        .filter(item -> (dto.getOperationId().equals(item.getValue()) && StringUtils.isNotBlank(item.getMeaning())))
                        .map(LovValueDTO::getMeaning).distinct().collect(Collectors.toList());
            }

            //查询工艺下的接口表对应的条码
            if (CollectionUtils.isNotEmpty(operationItfTableList)) {
                //正常情况下一个工艺只会维护一个表，先采用循环try catch保证只跳过有问题的表，后续维护多个需要把try catch提出来
                for (String tableName : operationItfTableList) {
                    try {
                        List<String> sns = hmeEoJobDataRecordMapper.querySnFromItfTableByAssetEncoding(tenantId, tableName, dto.getEquipmentCode());
                        if (CollectionUtils.isNotEmpty(sns)) {
                            snList.addAll(sns);
                        }
                    } catch (Exception e) {
                        log.error("<==== HmeEoJobSnTimeServiceImpl.queryDataRecord.selectTable Error", e);
                    }
                }

                //条码去重
                if (CollectionUtils.isNotEmpty(snList)) {
                    snList = snList.stream().distinct().collect(Collectors.toList());
                }
            }

            //设备下没有条码直接返回
            if (CollectionUtils.isEmpty(snList)) {
                return new Page<HmeEoJobDataRecordVO2>();
            }
        }

        //根据不同条件查询作业信息
        List<HmeEoJobDataRecordVO3> jobSnList = hmeEoJobDataRecordMapper.queryEoJobSnByCondition(tenantId, dto, snList);
        if(CollectionUtils.isEmpty(jobSnList)) {
            return new Page<HmeEoJobDataRecordVO2>();
        }

        /*Map<String, BigDecimal> containerStandardReqdTimeMap = new HashMap<>();
        for (HmeEoJobDataRecordVO3 jobSn : jobSnList) {
            //如果是容器则先查询缓存中是否有当前容器的标准时长
            if (StringUtils.isNotBlank(jobSn.getContainerId()) && containerStandardReqdTimeMap.containsKey(jobSn.getContainerId())) {
                jobSn.setStandardReqdTimeInProcess(containerStandardReqdTimeMap.get(jobSn.getContainerId()));
            } else {
                //查询标准时长
                HmeEoJobSnVO3 hmeEoJobSnVO3Para = new HmeEoJobSnVO3();
                hmeEoJobSnVO3Para.setWorkcellId(dto.getWorkcellId());
                hmeEoJobSnVO3Para.setOperationId(dto.getOperationId());
                hmeEoJobSnVO3Para.setEoId(jobSn.getEoId());
                hmeEoJobSnVO3Para.setWorkOrderId(jobSn.getWorkOrderId());
                hmeEoJobSnVO3Para.setMaterialId(jobSn.getSnMaterialId());
                BigDecimal preStandardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);
                jobSn.setStandardReqdTimeInProcess(preStandardReqdTimeInProcess);

                //如果是容器则把当前容器的标准时长缓存
                if (StringUtils.isNotBlank(jobSn.getContainerId())) {
                    containerStandardReqdTimeMap.put(jobSn.getContainerId(), preStandardReqdTimeInProcess);
                }
            }

            //计算当前入炉时长（分钟）
            long inSiteTime = (new Date().getTime() - jobSn.getSiteInDate().getTime()) / (1000 * 60);
            jobSn.setInSiteTime(inSiteTime);

            //校验标准时长是否达标
            jobSn.setTimeStandardFlag((BigDecimal.valueOf(inSiteTime).compareTo(jobSn.getStandardReqdTimeInProcess()) >= 0) ? "Y" : "N");
        }

        //是否要求时效
        if (StringUtils.isNotBlank(dto.getTimeStandardFlag())) {
            jobSnList = jobSnList.stream()
                    .filter(item -> dto.getTimeStandardFlag().equals(item.getTimeStandardFlag())).collect(Collectors.toList());
        }*/

        //查询数据采集项
        List<String> jobIdList = jobSnList.stream().map(HmeEoJobDataRecordVO3::getJobId).distinct().collect(Collectors.toList());
        List<HmeEoJobDataRecordVO2> eoJobDataRecordVO2List = hmeEoJobDataRecordMapper.queryEoJobDataRecordByJobId(tenantId, jobIdList, dto.getTagIdList());
        if (CollectionUtils.isEmpty(eoJobDataRecordVO2List)) {
            return new Page<HmeEoJobDataRecordVO2>();
        }

        //数据采集项通过jobId分组
        Map<String, List<HmeEoJobDataRecordVO2>> eoJobDataRecordGroup = eoJobDataRecordVO2List.stream()
                .filter(item -> StringUtils.isNotBlank(item.getTagGroupAssignId())).collect(Collectors.groupingBy(HmeEoJobDataRecordVO2::getJobId));

        //先把作业排序 容器>条码
        List<HmeEoJobDataRecordVO3> allJobSnList = new LinkedList<>();
        //排序容器 容器编码从小到大-条码编码从小到大
        List<HmeEoJobDataRecordVO3> containerList = jobSnList.stream().filter(item -> StringUtils.isNotBlank(item.getContainerId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(containerList)) {
            List<HmeEoJobDataRecordVO3> sortedContainerList = containerList.stream()
                    .sorted(Comparator.comparing(HmeEoJobDataRecordVO3::getContainerCode)
                            .thenComparing(HmeEoJobDataRecordVO3::getMaterialLotCode))
                    .collect(Collectors.toList());
            allJobSnList.addAll(sortedContainerList);
        }
        //排序条码 入炉时间早的在前
        List<HmeEoJobDataRecordVO3> materialLotList = jobSnList.stream().filter(item -> StringUtils.isBlank(item.getContainerId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialLotList)) {
            List<HmeEoJobDataRecordVO3> sortedMaterialLotList = materialLotList.stream().sorted(Comparator.comparing(HmeEoJobDataRecordVO3::getSiteInDate)).collect(Collectors.toList());
            allJobSnList.addAll(sortedMaterialLotList);
        }

        //构造返回数据
        List<HmeEoJobDataRecordVO2> allResultList = new LinkedList<>();
        for (HmeEoJobDataRecordVO3 jobSn : allJobSnList) {
            List<HmeEoJobDataRecordVO2> currEoJobDataRecordList = eoJobDataRecordGroup.get(jobSn.getJobId());
            if (CollectionUtils.isEmpty(currEoJobDataRecordList)) {
                continue;
            }

            //对数据采集项排序
            List<HmeEoJobDataRecordVO2> dataRecords = currEoJobDataRecordList.stream()
                    .sorted(Comparator.comparing(HmeEoJobDataRecordVO2::getBusinessType)
                    .thenComparing(HmeEoJobDataRecordVO2::getSerialNumber)).collect(Collectors.toList());

            //放入上层字段
            long orderNumber = 1;
            for (HmeEoJobDataRecordVO2 dataRecord : dataRecords) {
                dataRecord.setJobContainerId(jobSn.getJobContainerId());
                dataRecord.setContainerId(jobSn.getContainerId());
                dataRecord.setContainerCode(jobSn.getContainerCode());
                dataRecord.setMaterialLotId(jobSn.getMaterialLotId());
                dataRecord.setMaterialLotCode(jobSn.getMaterialLotCode());
                dataRecord.setSiteInDate(jobSn.getSiteInDate());
                dataRecord.setStandardReqdTimeInProcess(jobSn.getStandardReqdTimeInProcess());
                dataRecord.setTimeStandardFlag(jobSn.getTimeStandardFlag());
                dataRecord.setEoId(jobSn.getEoId());
                dataRecord.setWorkOrderId(jobSn.getWorkOrderId());
                dataRecord.setSnMaterialId(jobSn.getSnMaterialId());
                dataRecord.setOrderNumber(orderNumber++);
            }
            allResultList.addAll(dataRecords);
        }

        //计算获取子串开始结束位置
        int fromIndex = pageRequest.getPage() * pageRequest.getSize();
        int toIndex = Math.min(fromIndex + pageRequest.getSize(), allResultList.size());
        Page<HmeEoJobDataRecordVO2> resultPage = new Page<>(allResultList.subList(fromIndex, toIndex),
                new PageInfo(pageRequest.getPage(), pageRequest.getSize()),
                allResultList.size());

        //查询时效时长
        Map<String, BigDecimal> containerStandardReqdTimeMap = new HashMap<>();
        Map<String, BigDecimal> jobStandardReqdTimeMap = new HashMap<>();
        for (HmeEoJobDataRecordVO2 result : resultPage) {
            //如果是容器则先查询缓存中是否有当前容器的标准时长
            if (StringUtils.isNotBlank(result.getContainerId()) && containerStandardReqdTimeMap.containsKey(result.getContainerId())) {
                result.setStandardReqdTimeInProcess(containerStandardReqdTimeMap.get(result.getContainerId()));
            } else if (jobStandardReqdTimeMap.containsKey(result.getJobId())) {
                result.setStandardReqdTimeInProcess(jobStandardReqdTimeMap.get(result.getJobId()));
            } else {
                //查询标准时长
                HmeEoJobSnVO3 hmeEoJobSnVO3Para = new HmeEoJobSnVO3();
                hmeEoJobSnVO3Para.setWorkcellId(dto.getWorkcellId());
                hmeEoJobSnVO3Para.setOperationId(dto.getOperationId());
                hmeEoJobSnVO3Para.setEoId(result.getEoId());
                hmeEoJobSnVO3Para.setWorkOrderId(result.getWorkOrderId());
                hmeEoJobSnVO3Para.setMaterialId(result.getSnMaterialId());
                BigDecimal preStandardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);
                result.setStandardReqdTimeInProcess(preStandardReqdTimeInProcess);

                //如果是容器则把当前容器的标准时长缓存
                if (StringUtils.isNotBlank(result.getContainerId())) {
                    containerStandardReqdTimeMap.put(result.getContainerId(), preStandardReqdTimeInProcess);
                } else {
                    jobStandardReqdTimeMap.put(result.getJobId(), preStandardReqdTimeInProcess);
                }
            }

            //计算当前入炉时长（分钟）
            long inSiteTime = (new Date().getTime() - result.getSiteInDate().getTime()) / (1000 * 60);

            //校验标准时长是否达标
            result.setTimeStandardFlag((BigDecimal.valueOf(inSiteTime).compareTo(result.getStandardReqdTimeInProcess()) >= 0) ? "Y" : "N");
        }

        log.info("<====== HmeEoJobSnTimeServiceImpl.queryDataRecord.End tenantId={},dto={}", tenantId, dto);
        return resultPage;
    }

    /**
     *
     * @Description 保存数据采集项结果
     *
     * @author yuchao.wang
     * @date 2020/11/24 17:33
     * @param tenantId 租户ID
     * @param dataRecordList 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDataRecord(Long tenantId, List<HmeEoJobDataRecord> dataRecordList) {
        log.info("<====== HmeEoJobSnTimeServiceImpl.saveDataRecord.Start tenantId={},dataRecordList={}", tenantId, dataRecordList);
        hmeEoJobDataRecordRepository.batchUpdateResult(tenantId, dataRecordList);
    }

    /**
     *
     * @Description 时效作业平台-继续返修
     *
     * @author yuchao.wang
     * @date 2020/12/24 10:36
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    public HmeEoJobSnTimeDTO continueRework(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnTimeServiceImpl.continueRework.Start tenantId={},dto={}", tenantId, dto);
        // 非空校验
        defaultInputVerification(tenantId, dto);
        dto.setOutSiteAction(HmeConstants.OutSiteAction.REWORK);

        //出站校验
        List<String> jobIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getJobId).distinct().collect(Collectors.toList());
        HmeEoJobSnTimeDTO validateResult = outSiteScanValidate(tenantId, dto, jobIdList);
        if (StringUtils.isNotBlank(validateResult.getErrorCode())) {
            return validateResult;
        }
        // 获取当前用户
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        //调用继续返修通用主方法,继续返修只能单个条码
        HmeEoJobSnVO24 hmeEoJobSnVO24 = new HmeEoJobSnVO24();
        hmeEoJobSnVO24.setUserId(userId);
        hmeEoJobSnVO24.setReworkEo(validateResult.getReworkEo());
        hmeEoJobSnVO24.setReworkMaterialLot(validateResult.getMaterialLotList().get(0));
        hmeEoJobSnVO24.setHmeEoJobSnEntity(validateResult.getHmeEoJobSnEntityList().get(0));
        hmeEoJobSnCommonService.batchMainOutSiteForRework(tenantId, dto, hmeEoJobSnVO24);
        log.info("<====== HmeEoJobSnTimeServiceImpl.continueRework.End tenantId={},dto.SnNum={}", tenantId, dto.getSnNum());
        return validateResult;
    }

    /**
     *
     * @Description 时效作业-查询工艺默认数据项
     *
     * @author yuchao.wang
     * @date 2020/12/24 14:34
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOpTagRelVO>
     *
     */
    @Override
    public List<HmeOpTagRelVO> queryDefaultDataTag(Long tenantId, HmeEoJobDataDefaultTagQueryDTO dto) {
        log.info("<====== HmeEoJobSnTimeServiceImpl.queryDefaultDataTag.Start tenantId={},dto={}", tenantId, dto);
        //非空校验
        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "工艺ID"));
        }

        //查询工艺下所有的采集项
        return hmeOpTagRelRepository.queryTagInfoByOperationId(tenantId, dto.getOperationId());
    }

    /**
     *
     * @Description 构造插入数据
     *
     * @author yuchao.wang
     * @date 2020/11/4 11:15
     * @param tenantId 租户ID
     * @param userId 用户ID
     * @param currentDate 当前时间
     * @param dto 参数
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     *
     */
    private HmeEoJobSn getSnJobEntity(Long tenantId, Long userId, Date currentDate, String Id, String Cid, HmeEoJobSnVO3 dto) {
        HmeEoJobSn snJob = new HmeEoJobSn();
        snJob.setJobId(Id);
        snJob.setCid(Long.parseLong(Cid));
        snJob.setTenantId(tenantId);
        snJob.setEoId(dto.getEoId());
        snJob.setSiteInDate(currentDate);
        snJob.setShiftId(dto.getWkcShiftId());
        snJob.setSiteInBy(userId);
        snJob.setWorkcellId(dto.getWorkcellId());
        snJob.setWorkOrderId(dto.getWorkOrderId());
        snJob.setOperationId(dto.getOperationId());
        snJob.setMaterialLotId(dto.getMaterialLotId());
        snJob.setSnMaterialId(dto.getMaterialId());
        snJob.setJobType(dto.getJobType());
        snJob.setEoStepId(dto.getEoStepId());
        snJob.setReworkFlag(dto.getReworkFlag());

        //V20210312 modify by penglin.sui for tianyang.xie 返修进站时，按tenant_id+eo_id+workcell_id+operation_id+rework_flag+job_type+material_lot_id取最大的eo_step_num+1没有为0
        if(HmeConstants.ConstantValue.YES.equals(dto.getReworkFlag())){
            int maxEoStepNum = hmeEoJobSnMapper.queryReworkMaxEoStepNum(tenantId,snJob);
            snJob.setEoStepNum(maxEoStepNum + 1);
        }else{
            snJob.setEoStepNum(dto.getEoStepNum());
        }

        snJob.setSourceContainerId(dto.getSourceContainerId());
        snJob.setJobContainerId(dto.getJobContainerId());
        snJob.setObjectVersionNumber(1L);
        snJob.setCreatedBy(userId);
        snJob.setCreationDate(currentDate);
        snJob.setLastUpdatedBy(userId);
        snJob.setLastUpdateDate(currentDate);
        return snJob;
    }

}
