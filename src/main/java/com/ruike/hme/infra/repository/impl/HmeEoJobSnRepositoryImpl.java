package com.ruike.hme.infra.repository.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.HmeEoJobSnUtils;
import com.ruike.itf.api.dto.ErpReducedSettleRadioUpdateDTO;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.itf.app.service.ItfWorkOrderIfaceService;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.plugin.hr.EmployeeHelper;
import org.hzero.boot.platform.plugin.hr.entity.Employee;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtEoStepWip;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoRouterActualMapper;
import tarzan.actual.infra.mapper.MtEoStepActualMapper;
import tarzan.actual.infra.repository.impl.MtAssembleProcessActualRepositoryImpl;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO6;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO7;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.*;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialSiteVO3;
import tarzan.material.domain.vo.MtMaterialSiteVO4;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.MtRouterStepVO5;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;
import tarzan.modeling.domain.vo.MtModWorkcellVO2;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;
import static com.ruike.hme.infra.constant.HmeConstants.EoStatus.COMPLETED;
import static com.ruike.hme.infra.constant.HmeConstants.OutSiteAction.REWORK;

/**
 * ??????????????????-SN?????? ???????????????
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 00:04:39
 */
@Component
@Slf4j
public class HmeEoJobSnRepositoryImpl extends BaseRepositoryImpl<HmeEoJobSn> implements HmeEoJobSnRepository {

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;
    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;
    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;
    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private MtRouterNextStepRepository mtRouterNextStepRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;
    @Autowired
    private MtEoRouterActualMapper mtEoRouterActualMapper;
    @Autowired
    private HmeEoJobMaterialRepository hmeEoJobMaterialRepository;
    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private HmeEoJobTimeMaterialRepository hmeEoJobTimeMaterialRepository;
    @Autowired
    private HmeEoJobContainerRepository hmeEoJobContainerRepository;
    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;
    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;
    @Autowired
    private HmeEoJobBeyondMaterialRepository hmeEoJobBeyondMaterialRepository;
    @Autowired
    private HmeQualificationRepository hmeQualificationRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeEoJobContainerMapper hmeEoJobContainerMapper;
    @Autowired
    private HmeOperationAssignMapper hmeOperationAssignMapper;
    @Autowired
    private HmeEmployeeAssignMapper hmeEmployeeAssignMapper;
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private MtUserClient userClient;
    @Autowired
    private MtMaterialBasisRepository mtMaterialBasisRepository;
    @Autowired
    private HmeWoSnRelRepository hmeWoSnRelRepository;
    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepository;
    @Autowired
    private HmeOperationTimeObjectRepository hmeOperationTimeObjectRepository;
    @Autowired
    private ItfWorkOrderIfaceService itfWorkOrderIfaceService;
    @Autowired
    private HmeEoJobMaterialMapper hmeEoJobMaterialMapper;
    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtAssembleProcessActualRepositoryImpl mtAssembleProcessActualRepositoryImpl;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeTagNcRepository hmeTagNcRepository;
    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;
    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private WmsTransactionTypeRepository transactionTypeRepository;
    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;
    @Autowired
    private HmeWkcCompleteOutputRecordRepository hmeWkcCompleteOutputRecordRepository;
    @Autowired
    private HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper;
    @Autowired
    private HmeEoJobLotMaterialMapper hmeEoJobLotMaterialMapper;
    @Autowired
    private HmeEoJobTimeMaterialMapper hmeEoJobTimeMaterialMapper;
    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;
    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HmeMtEoRouterActualMapper hmeMtEoRouterActualMapper;
    @Autowired
    private MtEoStepActualMapper mtEoStepActualMapper;
    @Autowired
    private MtEoRepository eoRepository;
    @Autowired
    private HmeWoSnRelMapper hmeWoSnRelMapper;
    @Autowired
    private HmeEoJobDataRecordMapper hmeEoJobDataRecordMapper;
    @Autowired
    private ThreadPoolTaskExecutor poolExecutor;

    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;

    @Autowired
    private HmeMaterialLotLabCodeMapper hmeMaterialLotLabCodeMapper;

    @Autowired
    private HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;

    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;

    @Autowired
    private HmeSnLabCodeMapper hmeSnLabCodeMapper;

    @Autowired
    private MtExtendSettingsRepository extendSettingsRepository;

    @Autowired
    private HmeCommonReportMapper hmeCommonReportMapperapper;

    private static final String SYMBOL = "#";

    private static String fetchGroupKey2(String str1, String str2) {
        return str1 + SYMBOL + str2;
    }

    @Override
    public HmeEoJobSnVO4 workcellScan(Long tenantId, HmeEoJobSnDTO dto) {
        //
        MtModWorkcellVO1 mtModWorkcellVO1 = new MtModWorkcellVO1();
        mtModWorkcellVO1.setWorkcellCode(dto.getWorkcellCode());
        mtModWorkcellVO1.setWorkcellType("STATION");
        mtModWorkcellVO1.setEnableFlag(YES);
        List<String> workcellIds = mtModWorkcellRepository.propertyLimitWorkcellQuery(tenantId, mtModWorkcellVO1);

        if (CollectionUtils.isEmpty(workcellIds)) {
            // ??????${1}?????????,?????????!
//            throw new MtException("HME_CHIP_DATA_0003",
//                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_CHIP_DATA_0003", "HME"
//                    ,dto.getWorkcellCode()));

            // ??????????????????,?????????????????????
            throw new MtException("HME_EO_JOB_SN_001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_001", "HME"));
        }

        MtModWorkcellVO2 workcellVO = mtModWorkcellMapper.selectWorkcellById(tenantId, workcellIds.get(0));

        //V20210624 modify by penglin.sui for peng.zhao ???????????????????????????
        if(!YES.equals(workcellVO.getEnableFlag())){
            // ??????????????????,?????????????????????
            throw new MtException("HME_EO_JOB_SN_001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_001", "HME"));
        }

        HmeEoJobSnVO4 hmeEoJobSnVO4 = new HmeEoJobSnVO4();
        hmeEoJobSnVO4.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnVO4.setWorkcellId(workcellVO.getWorkcellId());
        hmeEoJobSnVO4.setWorkcellName(workcellVO.getWorkcellName());

        // ????????????
        MtModOrganizationVO2 processParam = new MtModOrganizationVO2();
        processParam.setTopSiteId(dto.getSiteId());
        processParam.setOrganizationId(workcellVO.getWorkcellId());
        processParam.setOrganizationType("WORKCELL");
        processParam.setParentOrganizationType("WORKCELL");
        processParam.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> processList =
                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, processParam);
        if (CollectionUtils.isEmpty(processList)) {
            // ????????????Wkc??????????????????
            throw new MtException("HME_EO_JOB_SN_002",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_002", "HME"));
        } else {
            // ????????????
            hmeEoJobSnVO4.setProcessId(processList.get(0).getOrganizationId());
            MtModWorkcell process =
                    mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, hmeEoJobSnVO4.getProcessId());
            hmeEoJobSnVO4.setProcessName(process.getWorkcellName());
            hmeEoJobSnVO4.setProcessCode(process.getWorkcellCode());
            // ??????????????????
            MtOpWkcDispatchRelVO7 wkcDispatchParam = new MtOpWkcDispatchRelVO7();
            wkcDispatchParam.setWorkcellId(hmeEoJobSnVO4.getProcessId());
            List<MtOpWkcDispatchRelVO6> opWkcDispatchRelVO6List = mtOperationWkcDispatchRelRepository
                    .wkcLimitAvailableOperationQuery(tenantId, wkcDispatchParam);
            if (CollectionUtils.isNotEmpty(opWkcDispatchRelVO6List)) {
                // ???????????????????????????????????????
                List<String> operationIdList = opWkcDispatchRelVO6List.stream()
                        .map(MtOpWkcDispatchRelVO6::getOperationId).collect(Collectors.toList());
                hmeEoJobSnVO4.setOperationIdList(operationIdList);
                hmeEoJobSnVO4.setOperationId(operationIdList.get(0));

                //??????/??????/??????/?????? ????????????????????????
                if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType()) || HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())
                        || HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType()) || HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                    if (operationIdList.size() > 1) {
                        // ??????????????????????????????,?????????
                        throw new MtException("HME_EO_JOB_SN_150", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_150", "HME"));
                    }

                    //???????????????????????? modify by yuchao.wang for tianyang.xie at 2021.1.5
                    MtOperation mtOperation = mtOperationRepository.operationGet(tenantId, hmeEoJobSnVO4.getOperationId());
                    boolean isTimeOperation = Objects.nonNull(mtOperation) && "TIME".equals(mtOperation.getOperationType());

                    if (HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                        if (!isTimeOperation) {
                            // ?????????????????????????????????????????????????????????/??????
                            throw new MtException("HME_EO_JOB_SN_052",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_052", "HME"));
                        }
                    } else if (isTimeOperation) {
                        // ???????????????????????????????????????????????????/??????
                        throw new MtException("HME_EO_JOB_SN_158",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_158", "HME"));
                    }
                }

                List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, new MtExtendVO1("mt_operation_attr", operationIdList, "AUTO_NUMBER", "DIGIT_LIMIT", "DIGIT_LIMIT_UP"));

                //???????????????????????????????????????
                //????????????????????????
                if (CollectionUtils.isNotEmpty(extendAttrList)) {
                    for (MtExtendAttrVO1 extendAttr : extendAttrList) {
                        if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType()))
                        {
                            // ??????????????????
                            if ("AUTO_NUMBER".equals(extendAttr.getAttrName())) {
                                hmeEoJobSnVO4.setAutoNumber(extendAttr.getAttrValue());
                            }
                            // ????????????
                            if ("DIGIT_LIMIT".equals(extendAttr.getAttrName())) {
                                hmeEoJobSnVO4.setDigitLimit(extendAttr.getAttrValue());
                            }
                        }
                        //V20210625 modify by penglin.sui for hui.ma ?????????????????????????????????????????????????????????DIGIT_LIMIT_UP??????
                        if (hmeEoJobSnVO4.getOperationId().equals(extendAttr.getKeyId())
                            && "DIGIT_LIMIT_UP".equals(extendAttr.getAttrName())) {
                            hmeEoJobSnVO4.setDigitLimitUp(extendAttr.getAttrValue());
                        }
                    }
                }
            } else {
                // ????????????Wkc??????????????????
                throw new MtException("HME_EO_JOB_SN_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_021", "HME"));
            }

            // ????????????
            MtModOrganizationVO2 lineParam = new MtModOrganizationVO2();
            lineParam.setTopSiteId(dto.getSiteId());
            lineParam.setOrganizationId(hmeEoJobSnVO4.getProcessId());
            lineParam.setOrganizationType("WORKCELL");
            lineParam.setParentOrganizationType("WORKCELL");
            lineParam.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> lineVOList =
                    mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, lineParam);
            if (CollectionUtils.isEmpty(lineVOList)) {
                // ????????????Wkc??????????????????
                throw new MtException("HME_EO_JOB_SN_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_023", "HME"));
            } else {
                String wkcLineId = lineVOList.get(0).getOrganizationId();
                hmeEoJobSnVO4.setWkcLineId(wkcLineId);
                MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, wkcLineId);
                if (Objects.isNull(mtWkcShiftVO3)) {
                    throw new MtException("HME_EO_JOB_SN_026", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_026", "HME"));
                }
                hmeEoJobSnVO4.setWkcShiftId(mtWkcShiftVO3.getWkcShiftId());
            }

            // ?????????????????????
            MtModOrganizationVO2 prodLineParam = new MtModOrganizationVO2();
            prodLineParam.setTopSiteId(dto.getSiteId());
            prodLineParam.setOrganizationId(lineVOList.get(0).getOrganizationId());
            prodLineParam.setOrganizationType("WORKCELL");
            prodLineParam.setParentOrganizationType("PROD_LINE");
            prodLineParam.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> prodLineVOList =
                    mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, prodLineParam);

            if (CollectionUtils.isEmpty(prodLineVOList)) {
                // ????????????Wkc??????????????????
                throw new MtException("HME_EO_JOB_SN_040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_040", "HME"));
            } else {
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository
                        .prodLineBasicPropertyGet(tenantId, prodLineVOList.get(0).getOrganizationId());
                hmeEoJobSnVO4.setProdLineId(mtModProductionLine.getProdLineId());
                hmeEoJobSnVO4.setProdLineCode(mtModProductionLine.getProdLineCode());
                hmeEoJobSnVO4.setProdLineName(mtModProductionLine.getProdLineName());
            }

        }

        // ????????????
        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // ??????????????????
        final Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat(BaseConstants.Pattern.DATE);
        Employee employee = EmployeeHelper.getEmployee(userId, tenantId);
        if (Objects.isNull(employee)) {
            throw new MtException("HME_EO_JOB_SN_025",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_025", "HME"));
        }

        List<HmeEmployeeAssign> hmeEmployeeAssignList = hmeEmployeeAssignMapper.queryData(tenantId,
                String.valueOf(employee.getEmployeeId()), format.format(currentDate));
        List<String> empQualityIdList = hmeEmployeeAssignList.stream().map(HmeEmployeeAssign::getQualityId)
                .collect(Collectors.toList());

        // ??????????????????????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(hmeEoJobSnVO4.getOperationIdList())) {
            for (String operationId : hmeEoJobSnVO4.getOperationIdList()) {
                List<HmeOperationAssign> hmeOperationAssignList =
                        hmeOperationAssignMapper.queryData(tenantId, operationId);

                List<String> oprQualityIdList = hmeOperationAssignList.stream().map(HmeOperationAssign::getQualityId)
                        .collect(Collectors.toList());

                if (empQualityIdList.size() != oprQualityIdList.size()
                        || !empQualityIdList.containsAll(oprQualityIdList)) {
                    oprQualityIdList.removeAll(empQualityIdList);
                    if (CollectionUtils.isNotEmpty(oprQualityIdList)) {
                        HmeQualification hmeQualification =
                                hmeQualificationRepository.selectByPrimaryKey(oprQualityIdList.get(0));
                        // ??????????????????????????????????????????????????????: ?????????????????????????????????${qualityName}?????????,?????????????????????
                        throw new MtException("HME_EO_JOB_SN_024", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "HME_EO_JOB_SN_024", "HME", hmeQualification.getQualityName()));
                    }
                }
            }
        }
        // 20210917 add by sanfeng.zhang for wenxin.zhang ????????????????????? ???????????????????????????????????????????????????
        List<LovValueDTO> manageWkcList = lovAdapter.queryLovValue("HME.ELE_MANAGE_WKC", tenantId);
        Optional<LovValueDTO> firstOpt = manageWkcList.stream().filter(wkc -> StringUtils.equals(hmeEoJobSnVO4.getProcessCode(), wkc.getValue())).findFirst();
        if (firstOpt.isPresent()) {
            // ????????????????????????
            String areaId = hmeCommonReportMapperapper.queryAreaIdByProdLine(tenantId, hmeEoJobSnVO4.getProdLineId());
            // ????????????????????? ????????????????????? ???Y ????????????
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setKeyId(areaId);
                setAttrName("ELE_CHECK");
                setTableName("mt_mod_area_attr");
            }});
            String eleCheckFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            if (YES.equals(eleCheckFlag)) {
                // ???????????? 12 ?????????12??????
                List<LovValueDTO> manageHourList = lovAdapter.queryLovValue("HME.ELE_MANAGE_HOUR", tenantId);
                if (CollectionUtils.isEmpty(manageHourList) || manageHourList.size() > 1) {
                    throw new MtException("HME_EO_JOB_SN_238", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_238", "HME"));
                }
                String manageHour = manageHourList.get(0).getValue();
                List<HmeManageReturnVO> hmeManageReturnVOList = new ArrayList<>();
                // ???????????????
                try {
                    hmeManageReturnVOList = this.connectDatabase(tenantId, manageHour, userId);
                } catch (Exception e) {
                    log.info("????????????????????????");
                }
                if (CollectionUtils.isEmpty(hmeManageReturnVOList)) {
                    throw new MtException("HME_EO_JOB_SN_239", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_239", "HME"));
                }
                // ????????? ???errcode???=1??????????????????
                List<HmeManageReturnVO> sortList = hmeManageReturnVOList.stream().sorted(Comparator.comparing(HmeManageReturnVO::getDt).reversed()).collect(Collectors.toList());
                log.info("???????????????????????????{}", JSONArray.toJSONString(sortList));
                if (StringUtils.equals(sortList.get(0).getErrCode(), STRING_ONE)) {
                    throw new MtException("HME_EO_JOB_SN_239", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_239", "HME"));
                }
            }
        }

        if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType())
                || HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())
                || HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            // ???????????????????????????????????????????????????????????????????????????????????????
            if (isContainerControl(tenantId, hmeEoJobSnVO4.getWorkcellId())) {
                // ??????????????????????????????
                hmeEoJobSnVO4.setIsContainerOut(1);
                HmeEoJobContainer eoJobContainer = hmeEoJobContainerRepository.wkcLimitJobContainerGet(tenantId,
                        hmeEoJobSnVO4.getWorkcellId());
                if (Objects.nonNull(eoJobContainer)) {
                    hmeEoJobSnVO4.setJobContainerId(eoJobContainer.getJobContainerId());
                    hmeEoJobSnVO4.setContainerId(eoJobContainer.getContainerId());
                    hmeEoJobSnVO4.setContainerCode(eoJobContainer.getContainerCode());

                    // ?????????????????????????????????
                    HmeEoJobContainerVO2 eoJobContainerVO2 = hmeEoJobContainerRepository
                            .eoJobContainerPropertyGet(tenantId, eoJobContainer.getJobContainerId());
                    /*
                     * if (Objects.isNull(eoJobContainerVO2)) { HmeEoJobContainerVO eoJobContainerVO = new
                     * HmeEoJobContainerVO(); eoJobContainerVO.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
                     * eoJobContainerVO.setContainerCode(eoJobContainer.getContainerCode()); eoJobContainerVO2 =
                     * hmeEoJobContainerRepository.updateEoJobContainer(tenantId, eoJobContainerVO); }
                     */
                    hmeEoJobSnVO4.setHmeEoJobContainerVO2(eoJobContainerVO2);
                }

            } else {
                hmeEoJobSnVO4.setIsContainerOut(0);
            }

            // ??????????????????????????????
            if (workcellVO.getRate() == null || workcellVO.getActivity() == null) {
                MtModWorkcellVO2 workcellStepVO =
                        mtModWorkcellMapper.selectWorkcellById(tenantId, hmeEoJobSnVO4.getProcessId());

                if (workcellStepVO.getRate() == null || workcellStepVO.getActivity() == null) {
                    MtModOrganizationVO2 workcellRangeItemVO = new MtModOrganizationVO2();
                    workcellRangeItemVO.setTopSiteId(dto.getSiteId());
                    workcellRangeItemVO.setOrganizationId(workcellStepVO.getWorkcellId());
                    workcellRangeItemVO.setOrganizationType("WORKCELL");
                    workcellRangeItemVO.setParentOrganizationType("WORKCELL");
                    workcellRangeItemVO.setQueryType("BOTTOM");
                    List<MtModOrganizationItemVO> workcellRangeVOList = mtModOrganizationRelRepository
                            .parentOrganizationRelQuery(tenantId, workcellRangeItemVO);

                    if (CollectionUtils.isEmpty(workcellRangeVOList)) {
                        throw new MtException("HME_EO_JOB_SN_044", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_044", "HME"));
                    } else {
                        MtModWorkcellVO2 workcellRangeVO = mtModWorkcellMapper.selectWorkcellById(tenantId,
                                workcellRangeVOList.get(0).getOrganizationId());

                        if (workcellRangeVO.getRate() != null && workcellRangeVO.getActivity() != null) {
                            hmeEoJobSnVO4.setRate(workcellRangeVO.getRate());
                            hmeEoJobSnVO4.setActivity(workcellRangeVO.getActivity());
                        }
                    }
                } else {
                    hmeEoJobSnVO4.setRate(workcellStepVO.getRate());
                    hmeEoJobSnVO4.setActivity(workcellStepVO.getActivity());
                }
            } else {
                hmeEoJobSnVO4.setRate(workcellVO.getRate());
                hmeEoJobSnVO4.setActivity(workcellVO.getActivity());
            }

            // ???????????????
            HmeEoJobBeyondMaterialVO beyondParam = new HmeEoJobBeyondMaterialVO();
            beyondParam.setWorkcellId(workcellVO.getWorkcellId());
            hmeEoJobSnVO4.setHmeEoJobBeyondMaterialList(hmeEoJobBeyondMaterialRepository.list(tenantId, beyondParam));

            //V20200920 modify by penglin.sui for lu.bai ???????????????????????????????????????
            // SN????????????????????????
            HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
            hmeEoJobMaterialVO.setWorkcellId(workcellVO.getWorkcellId());
            hmeEoJobMaterialVO.setJobType(dto.getJobType());
            hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
            hmeEoJobMaterialVO.setIsWorkcellQuery(YES);
            hmeEoJobMaterialVO.setSiteId(dto.getSiteId());
            List<HmeEoJobLotMaterialVO> lotMaterialVOList = hmeEoJobLotMaterialRepository.matchedJobLotMaterialQuery(tenantId, hmeEoJobMaterialVO, null);
            if (CollectionUtils.isNotEmpty(lotMaterialVOList)) {
                hmeEoJobSnVO4.setLotMaterialVOList(lotMaterialVOList);
            }

            if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                // ??????????????????
                List<HmeEoJobTimeMaterialVO> timeMaterialVOList = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, hmeEoJobMaterialVO, null);
                if (CollectionUtils.isNotEmpty(timeMaterialVOList)) {
                    hmeEoJobSnVO4.setTimeMaterialVOList(timeMaterialVOList);
                }
            }
        } else {
            if (HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                // ?????????????????????????????????????????????????????????
                MtModOrganizationItemVO organizationItemVO = processList.get(0);
                MtModWorkcellVO2 wkcStepVO = mtModWorkcellMapper.selectWorkcellById(tenantId,
                        organizationItemVO.getOrganizationId());
                hmeEoJobSnVO4.setWkcStepName(wkcStepVO.getWorkcellName());
                List<HmeEquipmentVO2> hmeEquipmentVO2List =
                        hmeEoJobSnMapper.selectEquipmentByWorkcellId(tenantId, workcellVO.getWorkcellId());
                if (CollectionUtils.isNotEmpty(hmeEquipmentVO2List)) {
                    if (hmeEquipmentVO2List.size() > 1) {
                        List<String> assetEncodingsStr = hmeEquipmentVO2List.stream()
                                .map(HmeEquipmentVO2::getAssetEncoding).collect(Collectors.toList());
                        hmeEoJobSnVO4.setEquipmentCode(StringUtils.join(assetEncodingsStr.toArray(), "/"));

                        List<String> descriptionsStr = hmeEquipmentVO2List.stream()
                                .map(HmeEquipmentVO2::getDescriptions).collect(Collectors.toList());
                        hmeEoJobSnVO4.setEquipmentName(StringUtils.join(descriptionsStr.toArray(), "/"));
                    } else {
                        hmeEoJobSnVO4.setEquipmentCode(hmeEquipmentVO2List.get(0).getAssetEncoding());
                        hmeEoJobSnVO4.setEquipmentName(hmeEquipmentVO2List.get(0).getDescriptions());
                    }
                }
            }
        }

        return hmeEoJobSnVO4;
    }

    private List<HmeManageReturnVO> connectDatabase (Long tenantId, String manageHour, Long userId) {
        Date startTime = DateUtil.hourAddOrSub(new Date(), -1 * Integer.valueOf(manageHour));
        MtUserInfo userInfo = userClient.userInfoGet(tenantId, userId);
        log.info("=================================> ?????????????????????????????????:{}", DateUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
        long topTime = System.currentTimeMillis();
        if (userInfo != null) {
           return hmeCommonReportMapperapper.queryManageDoorRecordList(tenantId, userInfo.getLoginName(), startTime);
        }
        long endTime = System.currentTimeMillis();
        log.info("=================================> ??????????????????????????? ?????????:{}??????", (endTime - topTime));
        return Collections.emptyList();
    }

    /**
     * ???????????????????????????SN??????
     *
     * @param tenantId   ??????Id
     * @param workcellId ??????Id
     * @return
     */
    @Override
    public List<HmeEoJobSnVO> querySnByWorkcell(Long tenantId, String workcellId) {
        //???????????????????????????????????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.9.12
        // ??????????????????????????????????????????????????????????????????????????????
        //Set<String> criteriaJobTypes = new HashSet<>();
        //criteriaJobTypes.add(HmeConstants.JobType.SINGLE_PROCESS);
        //criteriaJobTypes.add(HmeConstants.JobType.BATCH_PROCESS);

        long startDate0 = System.currentTimeMillis();
        SecurityTokenHelper.close();
        List<HmeEoJobSn> hmeEoJobSnList = this.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        //.andIn(HmeEoJobSn.FIELD_JOB_TYPE, criteriaJobTypes)
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.BATCH_PROCESS)
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, workcellId)
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());

        long endDate11 = System.currentTimeMillis();
        log.info("=================================>querySnByWorkcell ????????????SN????????????"+(endDate11 - startDate0) + "??????");
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            List<HmeEoJobSnVO> resultList = new ArrayList<>();

            //????????????EO
            List<String> eoIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getEoId).distinct().collect(Collectors.toList());
            Future<List<MtEo>> MtEoone = poolExecutor.submit(() -> {
                SecurityTokenHelper.close();
                    List<MtEo> mtEoList1 = mtEoRepository.eoPropertyBatchGet(tenantId, eoIdList);
                    return mtEoList1;
            });

            long endDate1 = System.currentTimeMillis();
            log.info("=================================>querySnByWorkcell ????????????EO???WO????????????"+(endDate1 - startDate0) + "??????");
            //??????????????????
            List<String> materialLotIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getMaterialLotId).distinct().collect(Collectors.toList());
            List<MtMaterialLot> mtMaterialLotList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(materialLotIdList)) {
                SecurityTokenHelper.close();
                mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
                if(CollectionUtils.isEmpty(mtMaterialLotList) || mtMaterialLotList.size() != materialLotIdList.size()){
                    //??????????????????, ?????????
                    throw new MtException("HME_EO_JOB_SN_050",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_050", "HME"));
                }
            }

            long endDate2 = System.currentTimeMillis();
            log.info("=================================>querySnByWorkcell ??????????????????????????????"+(endDate2 - endDate1) + "??????");

            //????????????????????????
            List<String> jobIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getJobId).distinct().collect(Collectors.toList());
            List<String> workcellIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getWorkcellId).distinct().collect(Collectors.toList());
            List<HmeEoJobMaterial> hmeEoJobMaterialList2 = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(jobIdList) && CollectionUtils.isNotEmpty(workcellIdList)) {
                SecurityTokenHelper.close();
                hmeEoJobMaterialList2 = hmeEoJobMaterialRepository.selectByCondition(Condition.builder(HmeEoJobMaterial.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobMaterial.FIELD_TENANT_ID, tenantId)
                                .andIn(HmeEoJobMaterial.FIELD_JOB_ID, jobIdList)
                                .andIn(HmeEoJobMaterial.FIELD_WORKCELL_ID, workcellIdList)).build());
            }

            long endDate3 = System.currentTimeMillis();
            log.info("=================================>querySnByWorkcell ????????????????????????????????????"+(endDate3 - endDate2) + "??????");

//            //????????????????????????
//            Future<List<HmeEoJobDataRecordVO>> HmeEoJobDataRecordone = poolExecutor.submit(() -> {
//            List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList1 = new ArrayList<>();
//            if(CollectionUtils.isNotEmpty(jobIdList) && CollectionUtils.isNotEmpty(workcellIdList)) {
//                SecurityTokenHelper.close();
//                hmeEoJobDataRecordVOList1 = hmeEoJobDataRecordMapper.queryEoJobDataRecords2(tenantId,workcellIdList.get(0),jobIdList);
//            }
//            return hmeEoJobDataRecordVOList1;
//            });
//            //log.info("<======================querySnByWorkcell hmeEoJobDataRecordVOList2.size=====================>:" + hmeEoJobDataRecordVOList2.size());
//
            long endDate31 = System.currentTimeMillis();
//            log.info("=================================>querySnByWorkcell ????????????????????????????????????"+(endDate31 - endDate3) + "??????");
            //V20201102 modify by penglin.sui for zhenyong.ban ????????????????????????

            Future<List<MtContainer>> MtContainerone = poolExecutor.submit(() -> {
            List<String> containerIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getSourceContainerId).distinct().collect(Collectors.toList());
            List<MtContainer> mtContainerList1 = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(containerIdList)) {
                SecurityTokenHelper.close();
                mtContainerList1 = mtContainerRepository.containerPropertyBatchGet(tenantId, containerIdList);
            }
            return mtContainerList1;
            });

            long endDate4 = System.currentTimeMillis();
            log.info("=================================>querySnByWorkcell ????????????????????????????????????"+(endDate4 - endDate31) + "??????");

            //????????????????????????
            List<String> eoStepIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getEoStepId).distinct().collect(Collectors.toList());
            Future<List<MtRouterStep>> eoStepIdone = poolExecutor.submit(() -> {
            List<MtRouterStep> currentRouterStepList1 = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(eoStepIdList)) {
                SecurityTokenHelper.close();
                currentRouterStepList1 = mtRouterStepRepository.routerStepBatchGet(tenantId, eoStepIdList);
            }
                return currentRouterStepList1;
            });

            long endDate5 = System.currentTimeMillis();
            log.info("=================================>querySnByWorkcell ??????????????????????????????"+(endDate5 - endDate4) + "??????");

            //????????????????????????
            List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(eoStepIdList) && CollectionUtils.isNotEmpty(materialLotIdList)) {
                SecurityTokenHelper.close();
                hmeMaterialLotLabCodeList = hmeMaterialLotLabCodeMapper.selectByCondition(Condition.builder(HmeMaterialLotLabCode.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLabCode.FIELD_TENANT_ID, tenantId)
                                .andIn(HmeMaterialLotLabCode.FIELD_MATERIAL_LOT_ID, materialLotIdList)
                                .andIn(HmeMaterialLotLabCode.FIELD_ROUTER_STEP_ID,eoStepIdList)
                                .andEqualTo(HmeMaterialLotLabCode.FIELD_ENABLE_FLAG , YES)).build());
            }

            List<MtContainer> mtContainerList2 = new ArrayList<>();
            //??????????????????????????????????????????
            try {
//                while (!MtContainerone.isDone())
//                {
//                    log.info("MtContainerone?????????????????????????????????100ms");
//                    Thread.sleep(100);
//                }
                mtContainerList2.addAll(MtContainerone.get());
            } catch (InterruptedException | ExecutionException e) {
                MtContainerone.cancel(true);
                Thread.currentThread().interrupt();
            }

            //????????????WO
            List<MtEo> mtEoList = new ArrayList<>();
            //??????????????????????????????????????????
            try {
                mtEoList.addAll(MtEoone.get());
            } catch (InterruptedException | ExecutionException e) {
                MtEoone.cancel(true);
                Thread.currentThread().interrupt();
            }
            List<String> workOrderIdList = new ArrayList<>();
            Map<String,HmeEoJobSnVO19> workOrderMap = new HashMap<>();
            Map<String,String> eoMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(mtEoList)) {
                eoMap = mtEoList.stream()
                        .collect(Collectors.toMap(MtEo::getEoId, MtEo::getWorkOrderId));
                workOrderIdList = mtEoList.stream().map(MtEo::getWorkOrderId).distinct().collect(Collectors.toList());
                List<HmeEoJobSnVO19> mtWorkOrderList = hmeEoJobSnMapper.selectWoProdLine(tenantId,workOrderIdList);
                if(CollectionUtils.isEmpty(mtWorkOrderList) || mtWorkOrderList.size() != workOrderIdList.size()){
                    //??????${1}?????????,?????????!
                    throw new MtException("HME_COS_CHIP_IMP_0020",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_COS_CHIP_IMP_0020", "HME"));
                }
                if(CollectionUtils.isNotEmpty(mtWorkOrderList)) {
                    workOrderMap = mtWorkOrderList.stream()
                            .collect(Collectors.toMap(HmeEoJobSnVO19::getWorkOrderId, t -> t));
                }
            }

            //V20201117 modify by penglin.sui for hui.ma ??????EO BOM
            List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId,eoIdList);
            Map<String,HmeEoBomVO> eoBomMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(hmeEoBomVOList)){
                eoBomMap = hmeEoBomVOList.stream().collect(Collectors.toMap(HmeEoBomVO::getEoId, t -> t));;
            }

            List<MtRouterStep> currentRouterStepList = new ArrayList<>();
            try {
                currentRouterStepList.addAll(eoStepIdone.get());
            } catch (InterruptedException | ExecutionException e) {
                eoStepIdone.cancel(true);
                Thread.currentThread().interrupt();
            }

//            List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList2 = new ArrayList<>();
//            try {
//                hmeEoJobDataRecordVOList2.addAll(HmeEoJobDataRecordone.get());
//            } catch (InterruptedException | ExecutionException e) {
//                HmeEoJobDataRecordone.cancel(true);
//                Thread.currentThread().interrupt();
//            }

            long endDate0 = System.currentTimeMillis();
            log.info("=================================>querySnByWorkcell ????????????????????????"+(endDate0 - startDate0) + "??????");

            long startDate = System.currentTimeMillis();
            for (HmeEoJobSn jobSn : hmeEoJobSnList) {

                Map<String, String> finalEoMap = eoMap;
                Map<String, HmeEoJobSnVO19> finalWorkOrderMap = workOrderMap;
                List<MtRouterStep> finalCurrentRouterStepList = currentRouterStepList;
                List<HmeEoJobMaterial> finalHmeEoJobMaterialList = hmeEoJobMaterialList2;
                List<MtMaterialLot> finalMtMaterialLotList = mtMaterialLotList;
//                List<HmeEoJobDataRecordVO> finalHmeEoJobDataRecordVOList = hmeEoJobDataRecordVOList2;
                List<MtContainer> finalMtContainerList = mtContainerList2;
                List<HmeMaterialLotLabCode> finalHmeMaterialLotLabCodeList = hmeMaterialLotLabCodeList;

                Map<String, HmeEoBomVO> finalEoBomMap = eoBomMap;

                String workOrderId = "";

                //????????????
//                List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = new ArrayList<>();
                List<HmeEoJobMaterial> hmeEoJobMaterialList = new ArrayList<>();
                List<MtContainer> mtContainerList = new ArrayList<>();
                List<MtRouterStep> currentRouterStepList2 = new ArrayList<>();
                List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList2 = new ArrayList<>();

                workOrderId = finalEoMap.getOrDefault(jobSn.getEoId(),"");
                MtMaterialLot mtMaterialLot = finalMtMaterialLotList.stream().filter(x -> x.getMaterialLotId().equals(jobSn.getMaterialLotId())).collect(Collectors.toList()).get(0);
                HmeEoJobSnVO hmeEoJobSnVO = new HmeEoJobSnVO();
                hmeEoJobSnVO.setSnNum(mtMaterialLot.getMaterialLotCode());
                hmeEoJobSnVO.setQualityStatus(mtMaterialLot.getQualityStatus());
                if(CollectionUtils.isNotEmpty(finalHmeEoJobMaterialList)) {
                    // ??????????????????????????????
                    hmeEoJobMaterialList = finalHmeEoJobMaterialList.stream().filter(x -> x.getJobId().equals(jobSn.getJobId()) && x.getWorkcellId().equals(jobSn.getWorkcellId())).collect(Collectors.toList());
                }
                if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
                    Long materialScanCount = hmeEoJobMaterialList.stream()
                            .filter(material -> StringUtils.isNotBlank(material.getMaterialLotCode())).count();

                    hmeEoJobSnVO.setMaterialScanCount(materialScanCount.intValue());
                    hmeEoJobSnVO.setMaterialScanAllCount(hmeEoJobMaterialList.size());
                } else {
                    hmeEoJobSnVO.setMaterialScanCount(0);
                    hmeEoJobSnVO.setMaterialScanAllCount(0);
                }

                // ??????????????????????????????
//                if(CollectionUtils.isNotEmpty(finalHmeEoJobDataRecordVOList)) {
//                    hmeEoJobDataRecordVOList = finalHmeEoJobDataRecordVOList.stream().filter(x -> x.getJobId().equals(jobSn.getJobId())
//                            && x.getWorkcellId().equals(jobSn.getWorkcellId())).collect(Collectors.toList());
//                    if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordVOList)) {
//                        List<HmeEoJobDataRecordVO> dataList = hmeEoJobDataRecordVOList.stream()
//                                .filter(data -> "DATA".equals(data.getGroupPurpose())).collect(Collectors.toList());
//                        List<HmeEoJobDataRecordVO> generalList = hmeEoJobDataRecordVOList.stream()
//                                .filter(data -> "GENERAL".equals(data.getGroupPurpose()))
//                                .collect(Collectors.toList());
//
//                        if (CollectionUtils.isNotEmpty(dataList)) {
//                            Long dataScanCount = dataList.stream().filter(data -> StringUtils.isNotBlank(data.getResult()))
//                                    .count();
//                            hmeEoJobSnVO.setDataScanCount(dataScanCount.intValue());
//                            hmeEoJobSnVO.setDataScanAllCount(dataList.size());
//                        } else {
//                            hmeEoJobSnVO.setDataScanCount(0);
//                            hmeEoJobSnVO.setDataScanAllCount(0);
//                        }
//
//                        if (CollectionUtils.isNotEmpty(generalList)) {
//                            Long generalScanCount = generalList.stream()
//                                    .filter(general -> StringUtils.isNotBlank(general.getResult())).count();
//                            hmeEoJobSnVO.setGeneralScanCount(generalScanCount.intValue());
//                            hmeEoJobSnVO.setGeneralScanAllCount(generalList.size());
//                        } else {
//                            hmeEoJobSnVO.setGeneralScanCount(0);
//                            hmeEoJobSnVO.setGeneralScanAllCount(0);
//                        }
//                    }
//                }

                BeanUtils.copyProperties(jobSn, hmeEoJobSnVO);
//                hmeEoJobSnVO.setDataRecordVOList(hmeEoJobDataRecordVOList);
                HmeEoJobSnVO19 woProdLine = finalWorkOrderMap.getOrDefault(workOrderId,null);
                if(Objects.nonNull(woProdLine)) {
                    hmeEoJobSnVO.setWorkOrderNum(woProdLine.getWorkOrderNum());
                    hmeEoJobSnVO.setProdLineCode(woProdLine.getProdLineCode());
                }
                if(CollectionUtils.isNotEmpty(finalMtContainerList)) {
                    mtContainerList = finalMtContainerList.stream().filter(x -> x.getContainerId().equals(jobSn.getSourceContainerId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(mtContainerList)) {
                        hmeEoJobSnVO.setSourceContainerCode(mtContainerList.get(0).getContainerCode());
                    }
                }

                //V20200917 modify by penglin.sui for tiaoyan.xie ????????????????????????
                if (StringUtils.isNotBlank(hmeEoJobSnVO.getEoStepId())) {
                    if(CollectionUtils.isNotEmpty(finalCurrentRouterStepList)) {
                        currentRouterStepList2 = finalCurrentRouterStepList.stream().filter(x -> x.getRouterStepId().equals(hmeEoJobSnVO.getEoStepId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(currentRouterStepList2)) {
                            hmeEoJobSnVO.setCurrentStepName(currentRouterStepList2.get(0).getStepName());
                            hmeEoJobSnVO.setCurrentStepDescription(currentRouterStepList2.get(0).getDescription());
                            hmeEoJobSnVO.setCurrentStepSequence(currentRouterStepList2.get(0).getSequence());
                        }
                    }
                }
                //V20201117 modify by penglin.sui for hui.ma ??????BOM????????????
                HmeEoBomVO hmeEoBomVO = finalEoBomMap.get(jobSn.getEoId());
                if(Objects.nonNull(hmeEoBomVO)){
                    hmeEoJobSnVO.setBomId(hmeEoBomVO.getBomId());
                    hmeEoJobSnVO.setBomName(hmeEoBomVO.getBomName());
                }

                //V20210401 modify by penglin.sui for hui.ma ?????????????????????????????????
                hmeMaterialLotLabCodeList2 = finalHmeMaterialLotLabCodeList.stream().filter(item -> item.getTenantId().equals(tenantId)
                        && item.getMaterialLotId().equals(jobSn.getMaterialLotId())
                        && item.getRouterStepId().equals(jobSn.getEoStepId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList2)){
                    hmeEoJobSnVO.setLabCode(hmeMaterialLotLabCodeList2.get(0).getLabCode());
                }

                resultList.add(hmeEoJobSnVO);
            }
            //V20201123 modify by penglin.sui for hui.ma ???BomName??????
            //V20201207 modify by penglin.sui for tianyang.xie ???????????????????????????????????????BomName??????
            if(CollectionUtils.isNotEmpty(resultList)) {
                resultList = resultList.stream().sorted(Comparator
                        .comparing(HmeEoJobSnVO::getQualityStatus).thenComparing(HmeEoJobSnVO::getBomName))
                        .collect(Collectors.toList());
            }
            long endDate = System.currentTimeMillis();
            log.info("=================================>querySnByWorkcell ??????????????????"+(endDate - startDate) + "??????");
            return resultList;
        }
        return null;
    }

    @Override
    public HmeEoJobTimeSnVO4 queryTimeSnByWorkcell(Long tenantId, String workcellId, String operationId) {
        HmeEoJobTimeSnVO4 resultVO = new HmeEoJobTimeSnVO4();
        List<HmeEoJobTimeSnVO3> lineList = new ArrayList<>();
        // ???????????????????????????
//        MtOperation mtOperation = mtOperationRepository.operationGet(tenantId, operationId);
//        resultVO.setStandardReqdTimeInProcess(BigDecimal.valueOf(mtOperation.getStandardReqdTimeInProcess()));

        List<HmeEoJobContainer> hmeEoJobContainerList =
                hmeEoJobContainerRepository.selectByCondition(Condition.builder(HmeEoJobContainer.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobContainer.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(HmeEoJobContainer.FIELD_WORKCELL_ID, workcellId)
                                .andIsNull(HmeEoJobContainer.FIELD_SITE_OUT_DATE))
                        .orderByAsc(HmeEoJobContainer.FIELD_SITE_IN_DATE).build());
        List<HmeEoJobSn> hmeEoJobSnList = this.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.TIME_PROCESS)
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, workcellId)
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());
        resultVO.setSiteContainerCount(hmeEoJobContainerList.size());
        resultVO.setSiteSnMaterialCount(hmeEoJobSnList.size());
        List<HmeEoJobTimeSnVO3> standardReqdTimeInProcessList;

        if (CollectionUtils.isNotEmpty(hmeEoJobContainerList)) {
            // ??????????????????
            hmeEoJobContainerList.forEach(jobContainer -> {
                String eoId = "";
                String workOrderId = "";
                String materialId = "";
                String materialLotId = "";
                String materialLotCode = "";
                HmeEoJobTimeSnVO3 containerVO = new HmeEoJobTimeSnVO3();
                containerVO.setSnType("CONTAINER");
                containerVO.setMaterialLotCode(jobContainer.getContainerCode());
                containerVO.setSiteInDate(jobContainer.getSiteInDate());
                containerVO.setSiteInBy(jobContainer.getSiteInBy());
                containerVO.setSiteInByName(userClient.userInfoGet(tenantId, jobContainer.getSiteInBy()).getRealName());
                containerVO.setSumEoQty(new BigDecimal(0));
                containerVO.setJobContainerId(jobContainer.getJobContainerId());

                List<HmeEoJobSn> newHmeEoJobSnList =
                        hmeEoJobSnList.stream()
                                .filter(hmeEoJobSn -> Objects.nonNull(hmeEoJobSn.getJobContainerId()))
                                .filter(hmeEoJobSn -> hmeEoJobSn.getJobContainerId()
                                        .equals(jobContainer.getJobContainerId()))
                                .collect(Collectors.toList());

                for (HmeEoJobSn newJobSn : newHmeEoJobSnList
                ) {
                    MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, newJobSn.getEoId());
                    containerVO.setSumEoQty(containerVO.getSumEoQty().add(BigDecimal.valueOf(mtEo.getQty())));

                    MtMaterial mtMaterial =
                            mtMaterialRepository.materialPropertyGet(tenantId, newJobSn.getSnMaterialId());
                    if (StringUtils.isNotBlank(containerVO.getMaterialCode())
                            && !containerVO.getMaterialCode().equals(mtMaterial.getMaterialCode())) {
                        // ??????SN????????????...
                        containerVO.setMaterialCode("...");
                        containerVO.setMaterialName("...");
                    } else {
                        containerVO.setMaterialCode(mtMaterial.getMaterialCode());
                        containerVO.setMaterialName(mtMaterial.getMaterialName());
                    }
                    eoId = newJobSn.getEoId();
                    workOrderId = mtEo.getWorkOrderId();
                    materialId = mtMaterial.getMaterialId();
                    materialLotId = newJobSn.getMaterialLotId();
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(newJobSn.getMaterialLotId());
                    materialLotCode = mtMaterialLot.getMaterialLotCode();
                }

                //V20200825 modify by penglin.sui for fang.pan ????????????????????????
                HmeEoJobSnVO3 hmeEoJobSnVO3Para = new HmeEoJobSnVO3();
                hmeEoJobSnVO3Para.setWorkcellId(jobContainer.getWorkcellId());
                hmeEoJobSnVO3Para.setEoId(eoId);
                hmeEoJobSnVO3Para.setOperationId(operationId);
                hmeEoJobSnVO3Para.setWorkOrderId(workOrderId);
                hmeEoJobSnVO3Para.setMaterialId(materialId);
                hmeEoJobSnVO3Para.setMaterialLotId(materialLotId);
                hmeEoJobSnVO3Para.setSnNum(materialLotCode);
                BigDecimal standardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);
                if (Objects.nonNull(standardReqdTimeInProcess)) {
                    containerVO.setStandardReqdTimeInProcess(standardReqdTimeInProcess);
                }
                lineList.add(containerVO);
            });
        }

        // ?????????????????????????????????
        List<HmeEoJobSn> newHmeEoJobSnList =
                hmeEoJobSnList.stream().filter(hmeEoJobSn -> Objects.isNull(hmeEoJobSn.getJobContainerId()))
                        .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(newHmeEoJobSnList)) {
            //
            newHmeEoJobSnList.forEach(jobSn -> {
                HmeEoJobTimeSnVO3 jobSnVO = new HmeEoJobTimeSnVO3();
                jobSnVO.setSnType("MATERIAL_LOT");
                jobSnVO.setMaterialLotId(jobSn.getMaterialLotId());
                MtMaterialLot mtMaterialLot =
                        mtMaterialLotRepository.materialLotPropertyGet(tenantId, jobSn.getMaterialLotId());
                jobSnVO.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                jobSnVO.setSiteInDate(jobSn.getSiteInDate());
                jobSnVO.setSiteInBy(jobSn.getSiteInBy());
                jobSnVO.setSiteInByName(userClient.userInfoGet(tenantId, jobSn.getSiteInBy()).getRealName());
                jobSnVO.setJobId(jobSn.getJobId());
                jobSnVO.setJobContainerId(jobSn.getJobContainerId());

                MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, jobSn.getEoId());
                jobSnVO.setSumEoQty(BigDecimal.valueOf(mtEo.getQty()));

                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, jobSn.getSnMaterialId());
                jobSnVO.setMaterialCode(mtMaterial.getMaterialCode());
                jobSnVO.setMaterialName(mtMaterial.getMaterialName());

                //V20200825 modify by penglin.sui for fang.pan ????????????????????????
                HmeEoJobSnVO3 hmeEoJobSnVO3Para = new HmeEoJobSnVO3();
                hmeEoJobSnVO3Para.setWorkcellId(jobSn.getWorkcellId());
                hmeEoJobSnVO3Para.setEoId(jobSn.getEoId());
                hmeEoJobSnVO3Para.setOperationId(jobSn.getOperationId());
                hmeEoJobSnVO3Para.setWorkOrderId(mtEo.getWorkOrderId());
                hmeEoJobSnVO3Para.setMaterialId(mtMaterial.getMaterialId());
                hmeEoJobSnVO3Para.setMaterialLotId(jobSn.getMaterialLotId());
                hmeEoJobSnVO3Para.setSnNum(mtMaterialLot.getMaterialLotCode());
                BigDecimal standardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);
                if (Objects.nonNull(standardReqdTimeInProcess)) {
                    jobSnVO.setStandardReqdTimeInProcess(standardReqdTimeInProcess);
                }
                lineList.add(jobSnVO);
            });
        }

        resultVO.setLineList(lineList);
        return resultVO;
    }

    @Override
    public HmeEoJobTimeSnVO2 timeSnScan(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnRepositoryImpl.timeSnScan.Start tenantId={},dto={}", tenantId, dto);
        HmeEoJobTimeSnVO2 resultVO = new HmeEoJobTimeSnVO2();
        // ??????????????????ID
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        //????????????????????? ???????????????????????????????????? ????????????????????????????????? add by yuchao.wang for tianyang.xie at 20221.1.15
        List<String> materialLotIds = new ArrayList<>();
        if (HmeConstants.InOutType.OUT.equals(dto.getInOutType())
                && !hmeEoJobSnCommonService.codeIsMaterialLotOrContainer(tenantId, dto.getSnNum())) {
            //????????????????????????
            HmeEquipment equipmentPara = new HmeEquipment();
            equipmentPara.setTenantId(tenantId);
            equipmentPara.setAssetEncoding(dto.getSnNum());
            SecurityTokenHelper.close();
            int count = hmeEquipmentRepository.selectCount(equipmentPara);
            if (count < 1) {
                //?????????????????????
                throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0003", "HME"));
            }

            //???????????????????????????
            List<String> idList = new ArrayList<>();
            //????????????HME.OPERATION_ITF_TABLE,????????????????????????????????????
            List<String> operationItfTableList = new ArrayList<>();
            List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.OPERATION_ITF_TABLE", tenantId);
            if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
                operationItfTableList = lovValueDTOList.stream()
                        .filter(item -> (dto.getOperationId().equals(item.getValue()) && StringUtils.isNotBlank(item.getMeaning())))
                        .map(LovValueDTO::getMeaning).distinct().collect(Collectors.toList());
            }

            //??????????????????????????????????????????
            if (CollectionUtils.isNotEmpty(operationItfTableList)) {
                //??????????????????????????????????????????????????????????????????try catch????????????????????????????????????????????????????????????try catch?????????
                for (String tableName : operationItfTableList) {
                    try {
                        List<String> ids = hmeEoJobDataRecordMapper.queryMaterialLotIdFromItfTableByAssetEncoding(tenantId, tableName, dto.getSnNum());
                        if (CollectionUtils.isNotEmpty(ids)) {
                            idList.addAll(ids);
                        }
                    } catch (Exception e) {
                        log.error("<==== HmeEoJobSnRepositoryImpl.timeSnScan.selectTable Error", e);
                    }
                }

                if (CollectionUtils.isNotEmpty(idList)) {
                    //??????????????????????????????????????????????????????
                    idList = hmeEoJobDataRecordMapper.queryAllMaterialLotIdInSameContainer(tenantId, dto.getWorkcellId(), dto.getJobType(), idList.stream().distinct().collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(idList)) {
                        materialLotIds = idList.stream().distinct().collect(Collectors.toList());
                        resultVO.setSnType(HmeConstants.SnType.EQUIPMENT);
                    }
                }
            }
        } else {
            MtMaterialLotVO30 materialLotVo30 = new MtMaterialLotVO30();
            materialLotVo30.setCode(dto.getSnNum());
            materialLotVo30.setAllLevelFlag(YES);
            log.info("<====== HmeEoJobSnRepositoryImpl.timeSnScan.codeOrIdentificationLimitObjectGet materialLotVo30={}", materialLotVo30);
            MtMaterialLotVO29 isContainerVO =
                    mtMaterialLotRepository.codeOrIdentificationLimitObjectGet(tenantId, materialLotVo30);
            log.info("<====== HmeEoJobSnRepositoryImpl.timeSnScan.codeOrIdentificationLimitObjectGet CodeType={}", isContainerVO.getCodeType());
            if (HmeConstants.LoadTypeCode.CONTAINER.equals(isContainerVO.getCodeType())) {
                //V20210524 modify by penglin.sui for peng.zhao ??????????????????????????????????????????
                if(CollectionUtils.isEmpty(isContainerVO.getLoadingObjectlList())){
                    //?????????${1}?????????,???????????????
                    throw new MtException("HME_EO_JOB_SN_200", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_200", "HME", dto.getSnNum()));
                }
                List<MtMaterialLotVO28> materialLotVOList = isContainerVO.getLoadingObjectlList();
                //V20200827 modify by penglin.sui for tianyang.xie ?????????MATERIAL_LOT???????????????
                materialLotVOList = materialLotVOList.stream()
                        .filter((MtMaterialLotVO28 s) -> HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(s.getLoadObjectType()))
                        .collect(Collectors.toList());
                materialLotIds = materialLotVOList.stream().map(MtMaterialLotVO28::getLoadObjectId).distinct()
                        .collect(Collectors.toList());
                resultVO.setContainerId(isContainerVO.getCodeId());
                resultVO.setSnType(HmeConstants.SnType.CONTAINER);
            } else if (HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(isContainerVO.getCodeType())) {
                materialLotIds.add(isContainerVO.getCodeId());
                resultVO.setSnType(HmeConstants.SnType.MATERIAL_LOT);
            }
        }

        // ???SN???????????????????????????????????????????????????????????????
        if (CollectionUtils.isEmpty(materialLotIds)) {
            throw new MtException("HME_EO_JOB_TIME_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_001", "HME"));
        }

        //???????????????????????????
        SecurityTokenHelper.close();
        List<HmeEoJobSn> allEoJobSnList = this.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, dto.getJobType())
                        .andIn(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLotIds))
                .build());

        Optional<HmeEoJobSn> notOutSiteOptional = allEoJobSnList.stream().filter(item -> Objects.isNull(item.getSiteOutDate())).findAny();
        if (HmeConstants.InOutType.IN.equals(dto.getInOutType())) {
            // ?????????????????????????????????
            if (notOutSiteOptional.isPresent()) {
                // ????????????SN?????????
                throw new MtException("HME_EO_JOB_TIME_SN_002", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_002", "HME"));
            }
        } else {
            // ?????????????????????????????????
            if (!notOutSiteOptional.isPresent()) {
                // ????????????SN?????????
                throw new MtException("HME_EO_JOB_TIME_SN_003", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_003", "HME"));
            }

            if (HmeConstants.SnType.CONTAINER.equals(resultVO.getSnType())) {
                //V20200828 modify by penglin.sui for tianyang.xie ??????????????????????????????
                List<HmeEoJobContainer> hmeEoJobContainerList = hmeEoJobContainerRepository.selectByCondition(Condition.builder(HmeEoJobContainer.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobContainer.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(HmeEoJobContainer.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                .andEqualTo(HmeEoJobContainer.FIELD_CONTAINER_ID, resultVO.getContainerId())
                                .andIsNull(HmeEoJobContainer.FIELD_SITE_OUT_DATE)).build());

                resultVO.setSiteInDate(hmeEoJobContainerList.get(0).getSiteInDate());
                resultVO.setJobContainerId(hmeEoJobContainerList.get(0).getJobContainerId());
            }
        }

        resultVO.setSiteInBy(userId);
        resultVO.setSiteInByName(userClient.userInfoGet(tenantId, userId).getRealName());
        resultVO.setSumEoCount(materialLotIds.size());

        // ???????????????Id??????????????????
        List<HmeEoJobTimeSnVO3> snLineList = hmeEoJobSnMapper.selectSnLineByMaterialLotIds(tenantId, materialLotIds);
        if (CollectionUtils.isEmpty(snLineList) || snLineList.size() != materialLotIds.size()) {
            // ???????????????????????????????????????
            throw new MtException("HME_EO_JOB_TIME_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_001", "HME"));
        }

        //?????????????????????????????? ???????????????????????? modify by yuchao.wang for tianyang.xie at 2020.12.21
        List<String> reworkFlagList = snLineList.stream().map(item -> YES.equals(item.getReworkFlag()) ? YES : NO).distinct().collect(Collectors.toList());
        if (reworkFlagList.size() > 1) {
            // ????????????sn?????????sn??????????????????/??????!
            throw new MtException("HME_EO_JOB_SN_152", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_152", "HME"));
        }

        //??????????????????
        resultVO.setReworkFlag(reworkFlagList.get(0));
        resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.YES);
        resultVO.setProhibitClickContinueReworkFlag(HmeConstants.ConstantValue.NO);
        if (YES.equals(resultVO.getReworkFlag())) {
            //???????????????SN${1}???????????????,?????????SN??????????????????!
            if (HmeConstants.SnType.CONTAINER.equals(resultVO.getSnType())
                    || HmeConstants.SnType.EQUIPMENT.equals(resultVO.getSnType()) || snLineList.size() > 1){
                String materialLotCode = "";
                Optional<HmeEoJobTimeSnVO3> reworkMaterialLot = snLineList.stream().filter(item -> YES.equals(item.getReworkFlag())).findAny();
                if (reworkMaterialLot.isPresent()) {
                    materialLotCode = reworkMaterialLot.get().getMaterialLotCode();
                }
                throw new MtException("HME_EO_JOB_SN_154", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_154", "HME", materialLotCode));
            }

            //??????????????????????????????
            String designedReworkFlag = YES.equals(snLineList.get(0).getDesignedReworkFlag()) ? YES : NO;
            resultVO.setDesignedReworkFlag(designedReworkFlag);

            //????????????????????????
            String eoId = snLineList.get(0).getEoId();
            HmeNcRecordVO hmeNcRecordVO = hmeEoJobSnMapper.selectLastNcRecordProcessForSingle(tenantId, eoId, dto.getWorkcellId());
            if (YES.equals(designedReworkFlag)) {
                if (Objects.nonNull(hmeNcRecordVO)) {
                    resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
                    resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
                }
            } else {
                if (Objects.isNull(hmeNcRecordVO) || StringUtils.isBlank(hmeNcRecordVO.getNcRecordId())) {
                    //${1}????????? ?????????${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "ncRecord", dto.getSnNum()));
                }
                resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
                resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
                if (!StringUtils.equals(hmeNcRecordVO.getRootCauseProcessId(), hmeNcRecordVO.getCurrentProcessId())) {
                    resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.NO);
                }
            }
        }

        //????????????EO
        Map<String, MtEo> eoMap = new HashMap<>();
        List<String> eoIdList = snLineList.stream().map(HmeEoJobTimeSnVO3::getEoId).distinct().collect(Collectors.toList());
        List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(mtEos)) {
            mtEos.forEach(item -> eoMap.put(item.getEoId(), item));
        }

        //?????????????????????????????????????????? key:materialLotId value:????????????????????????
        Map<String, List<HmeEoJobSn>> existsSnLineMap = new HashMap<>();
        if (HmeConstants.InOutType.OUT.equals(dto.getInOutType()) && CollectionUtils.isNotEmpty(allEoJobSnList)) {
            existsSnLineMap = allEoJobSnList.stream().collect(Collectors.groupingBy(HmeEoJobSn::getMaterialLotId));
        }

        BigDecimal preStandardReqdTimeInProcess = null;
        for (HmeEoJobTimeSnVO3 snLine : snLineList) {
            snLine.setSiteInBy(userId);
            snLine.setSiteInByName(userClient.userInfoGet(tenantId, userId).getRealName());

            MtEo mtEo = eoMap.get(snLine.getEoId());
            if (Objects.isNull(mtEo) || StringUtils.isBlank(mtEo.getEoId())) {
                // ????????????SN????????????EO
                throw new MtException("HME_EO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_005", "HME"));
            }

            //V20200821 modify by penglin.sui for fang.pan ??????????????????????????????-????????????????????????
            HmeEoJobSnVO3 hmeEoJobSnVO3Para = new HmeEoJobSnVO3();
            hmeEoJobSnVO3Para.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSnVO3Para.setEoId(snLine.getEoId());
            hmeEoJobSnVO3Para.setOperationId(dto.getOperationId());
            hmeEoJobSnVO3Para.setWorkOrderId(mtEo.getWorkOrderId());
            hmeEoJobSnVO3Para.setMaterialId(snLine.getMaterialId());
            hmeEoJobSnVO3Para.setMaterialCode(snLine.getMaterialCode());
            hmeEoJobSnVO3Para.setSnNum(snLine.getMaterialLotCode());

            if (HmeConstants.InOutType.OUT.equals(dto.getInOutType())) {
                //???????????????????????????????????????
                List<HmeEoJobSn> existsSnLineList = existsSnLineMap.get(snLine.getMaterialLotId());
                if (CollectionUtils.isEmpty(existsSnLineList)) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "????????????"));
                }

                //???????????????????????????????????????
                Optional<HmeEoJobSn> notOutSiteSn = existsSnLineList.stream().filter(item -> Objects.isNull(item.getSiteOutDate())).findAny();
                if (notOutSiteSn.isPresent()) {
                    // ?????????????????????????????????ID
                    HmeEoJobSn existsSnLine = notOutSiteSn.get();
                    snLine.setJobId(existsSnLine.getJobId());
                    snLine.setOperationId(existsSnLine.getOperationId());
                    snLine.setEoStepId(existsSnLine.getEoStepId());
                    snLine.setSiteInDate(existsSnLine.getSiteInDate());

                    //???????????????????????????????????????????????????????????? add by yuchao.wang for tianyang.xie at 2021.2.4
                    if (YES.equals(resultVO.getDesignedReworkFlag())) {
                        String doneStepFlag = mtRouterDoneStepRepository.doneStepValidate(tenantId, snLine.getEoStepId());
                        if (HmeConstants.ConstantValue.YES.equals(doneStepFlag)) {
                            resultVO.setProhibitClickContinueReworkFlag(HmeConstants.ConstantValue.YES);
                        }
                    }
                }
            }

            //?????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.10.20
            //????????????????????????????????????????????? modify by yuchao.wang for tianyang.xie at 2021.1.20
            if (HmeConstants.InOutType.OUT.equals(dto.getInOutType()) && HmeConstants.SnType.EQUIPMENT.equals(resultVO.getSnType())) {
                //??????????????????????????????
                preStandardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);

                //????????????????????????????????????
                long inSiteTime = (new Date().getTime() - snLine.getSiteInDate().getTime()) / (1000 * 60);

                //??????????????????????????????
                snLine.setStandardReqdTimeInProcessFlag(BigDecimal.valueOf(inSiteTime).compareTo(preStandardReqdTimeInProcess) >= 0);
            } else if (preStandardReqdTimeInProcess == null) {
                preStandardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);
            } else if (HmeConstants.InOutType.IN.equals(dto.getInOutType())) {
                if (preStandardReqdTimeInProcess.compareTo(hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para)) != 0) {
                    //????????????????????????????????????,???????????????
                    throw new MtException("HME_EO_JOB_SN_059", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_059", "HME"));
                }
            }
            snLine.setStandardReqdTimeInProcess(preStandardReqdTimeInProcess);

            //???????????????,????????????????????????
            if (YES.equals(snLine.getReworkFlag()) && StringUtils.isBlank(resultVO.getNcRecordWorkcellCode())) {
                HmeNcRecordVO hmeNcRecordVO = hmeEoJobSnMapper.selectLastestNcRecordProcess(tenantId, snLine.getMaterialLotId(), dto.getWorkcellId());
                if (Objects.nonNull(hmeNcRecordVO)) {
                    resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
                    resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
                }
            }
        }

        //??????????????????,?????????????????????????????????????????????????????????????????????
        /*if (HmeConstants.InOutType.IN.equals(dto.getInOutType())) {
            //??????????????????????????????????????????????????????
            Map<String, BigDecimal> preStandardReqdTimeInProcessMap = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessBatchGet(tenantId, hmeEoJobSnVO3ParaList);
        } else {
            //?????????????????????????????????????????????????????????????????????
            BigDecimal preStandardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3ParaList.get(0));
            snLineList.forEach(item -> item.setStandardReqdTimeInProcess(preStandardReqdTimeInProcess));
        }*/

        //????????????????????????????????????????????????????????????????????????
        if (HmeConstants.InOutType.OUT.equals(dto.getInOutType())) {
            //?????????????????????????????????????????????????????????
            if (HmeConstants.SnType.EQUIPMENT.equals(resultVO.getSnType())) {
                snLineList = snLineList.stream().filter(item -> (StringUtils.isNotBlank(item.getJobId()) && item.isStandardReqdTimeInProcessFlag()))
                        .sorted(Comparator.comparing(HmeEoJobTimeSnVO3::getEoId)).collect(Collectors.toList());
            } else {
                snLineList = snLineList.stream().filter(item -> StringUtils.isNotBlank(item.getJobId()))
                        .sorted(Comparator.comparing(HmeEoJobTimeSnVO3::getEoId)).collect(Collectors.toList());
            }
            resultVO.setSumEoCount(snLineList.size());
        } else {
            snLineList = snLineList.stream().sorted(Comparator.comparing(HmeEoJobTimeSnVO3::getEoId))
                    .collect(Collectors.toList());
        }
        resultVO.setLineList(snLineList);

        //V20210310 modify by penglin.sui for hui.ma ???????????????????????????
        eoIdList = snLineList.stream().map(HmeEoJobTimeSnVO3::getEoId).distinct().collect(Collectors.toList());
        List<String> materialLotIdList = snLineList.stream().map(HmeEoJobTimeSnVO3::getMaterialLotId).distinct().collect(Collectors.toList());
        //EOID????????????ID??????
        Map<String,String> eoIdAndMaterialLotIdRelMap = snLineList.stream().collect(Collectors.toMap(HmeEoJobTimeSnVO3::getEoId,HmeEoJobTimeSnVO3::getMaterialLotId));
        List<HmeRouterStepVO3> hmeRouterStepVO3s = hmeEoJobSnMapper.batchQueryCurrentRouterStepForTime(tenantId, eoIdList, dto.getOperationId());
        List<HmeEoJobTimeSnVO5> labCodeAndRemarkList = new ArrayList<>();

        //V20210320 modify by penglin.sui for hui.ma ???????????????????????????????????????????????????????????????SN?????????????????????
        List<HmeRouterStepVO3> labCodeOrRemarkIsNullStepList = new ArrayList<>();
        Map<String,HmeSnLabCode> snLabCodeMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(hmeRouterStepVO3s)){
            labCodeOrRemarkIsNullStepList = hmeRouterStepVO3s.stream().filter(item -> StringUtils.isBlank(item.getLabCode()) || StringUtils.isBlank(item.getRouterStepRemark()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(labCodeOrRemarkIsNullStepList)){
                List<String> labCodeOrRemarkIsNullOperationIdList = labCodeOrRemarkIsNullStepList.stream().map(HmeRouterStepVO3::getOperationId)
                        .collect(Collectors.toList());
                List<HmeSnLabCode> hmeSnLabCodeList = hmeSnLabCodeMapper.selectByCondition(Condition.builder(HmeSnLabCode.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeSnLabCode.FIELD_TENANT_ID, tenantId)
                                .andIn(HmeSnLabCode.FIELD_OPERATION_ID,labCodeOrRemarkIsNullOperationIdList)
                                .andIn(HmeSnLabCode.FIELD_MATERIAL_LOT_ID,materialLotIdList)
                                .andEqualTo(HmeSnLabCode.FIELD_ENABLED_FLAG,HmeConstants.ConstantValue.YES))
                        .build());
                if(CollectionUtils.isNotEmpty(hmeSnLabCodeList)) {
                    snLabCodeMap = hmeSnLabCodeList.stream().collect(Collectors.toMap(item -> item.getTenantId() + "#" + item.getOperationId() + "#" + item.getMaterialLotId(), t -> t));
                }
            }
        }
        for (HmeRouterStepVO3 hmeRouterStepVO3:hmeRouterStepVO3s
             ) {

            if(StringUtils.isBlank(hmeRouterStepVO3.getLabCode()) || StringUtils.isBlank(hmeRouterStepVO3.getRouterStepRemark())) {
                HmeSnLabCode hmeSnLabCode = snLabCodeMap.getOrDefault(tenantId + "#" + hmeRouterStepVO3.getOperationId() + "#" + eoIdAndMaterialLotIdRelMap.getOrDefault(hmeRouterStepVO3.getEoId(),"") , null);
                if(Objects.nonNull(hmeSnLabCode)) {
                    if(StringUtils.isBlank(hmeRouterStepVO3.getLabCode())) {
                        hmeRouterStepVO3.setLabCode(hmeSnLabCode.getLabCode());
                    }
                    if(StringUtils.isBlank(hmeRouterStepVO3.getRouterStepRemark())) {
                        hmeRouterStepVO3.setRouterStepRemark(hmeSnLabCode.getRemark());
                    }
                }
            }

            if(StringUtils.isNotBlank(hmeRouterStepVO3.getLabCode()) || StringUtils.isNotBlank(hmeRouterStepVO3.getRouterStepRemark())) {
                HmeEoJobTimeSnVO5 hmeEoJobTimeSnVO5 = new HmeEoJobTimeSnVO5();
                hmeEoJobTimeSnVO5.setLabCode(hmeRouterStepVO3.getLabCode());
                hmeEoJobTimeSnVO5.setRouterStepRemark(hmeRouterStepVO3.getRouterStepRemark());
                labCodeAndRemarkList.add(hmeEoJobTimeSnVO5);
            }
        }
        if(CollectionUtils.isNotEmpty(labCodeAndRemarkList)) {
            resultVO.setLabCodeAndRemarkList(labCodeAndRemarkList);
        }

        log.info("<====== HmeEoJobSnRepositoryImpl.timeSnScan.End resultVO={}", resultVO);
        return resultVO;
    }

    private static String fetchGroupKey(MtEoRouterActualVO42 mtEoRouterActualVO42) {
        return mtEoRouterActualVO42.getReworkStepFlag() + "#" + mtEoRouterActualVO42.getSourceStatus();
    }

    private void reworkInSiteValidation(Long tenantId, String snId, String workcellId) {
        MtExtendVO vo = new MtExtendVO() {{
            setAttrName("REWORK_FLAG");
            setKeyId(snId);
            setTableName("mt_material_lot_attr");
        }};
        List<MtExtendAttrVO> attrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, vo);
        if (CollectionUtils.isNotEmpty(attrList) && YES.equals(attrList.get(0).getAttrValue())) {
            List<HmeEoJobSn> snList = hmeEoJobSnRepository.select(new HmeEoJobSn() {{
                setMaterialLotId(snId);
                setReworkFlag(YES);
            }});
            snList.forEach(rec -> {
                if (!workcellId.equals(rec.getWorkcellId()) && Objects.isNull(rec.getSiteOutDate())) {
                    MtModWorkcell workcell = mtModWorkcellRepository.selectByPrimaryKey(rec.getWorkcellId());
                    throw new MtException("HME_EO_JOB_SN_137",mtErrorMessageRepository.getErrorMessageWithModule(tenantId,"HME_EO_JOB_SN_137", HME, workcell.getWorkcellCode()));
                } });
        }
    }

    /**
     * ????????????-????????????
     *
     * @param tenantId ??????
     * @param dto      ??????????????????
     * @return ??????????????????
     */
    @Override
    public HmeEoJobSnVO3 inSiteScanCheck(Long tenantId, HmeEoJobSnVO3 dto) {
        // ????????????????????????
        if (StringUtils.isBlank(dto.getSnNum())) {
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        MtMaterialLotVO3 materialLotParam = new MtMaterialLotVO3();
        materialLotParam.setTenantId(tenantId);
        materialLotParam.setSiteId(dto.getSiteId());
        materialLotParam.setMaterialLotCode(dto.getSnNum());
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            materialLotParam.setEnableFlag(YES);
        }
        List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotParam);
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            // ??????????????????????????????
            if (StringUtils.isBlank(dto.getProdLineId())){
                throw new MtException("HME_EO_JOB_SN_140", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_140", "HME"));
            }
            MtModProductionLine productionLine = mtModProductionLineRepository.selectByPrimaryKey(dto.getProdLineId());
            if (!Objects.isNull(productionLine)) {
                // ??????SN ??????Eo?????????
                String eoProdLineCode = hmeEoJobSnMapper.selectEoProdLine(tenantId, materialLotIds.get(0));
                if (!StringUtils.equals(productionLine.getProdLineCode(), eoProdLineCode)) {
                    throw new MtException("HME_EO_JOB_SN_141", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_141", "HME",
                                    productionLine.getProdLineCode(), eoProdLineCode));
                }
            }
        }
        return dto;
    }

    /**
     * ????????????
     *
     * @param tenantId ??????
     * @param dto      ??????????????????
     * @return ??????????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        // ????????????????????????
        if (StringUtils.isBlank(dto.getSnNum())) {
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }

        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        HmeEoJobSnVO2 currentJobSnVO = new HmeEoJobSnVO2();
        HmeEoJobSnVO5 snVO = new HmeEoJobSnVO5();
        // ???????????????????????????????????????
        boolean isSnQuery = true;
        // ???????????????JobSn??????
        boolean isJobSnLoad = false;
        // ???????????????????????????????????????
        boolean isContainerInSite = false;
        // ?????????????????????????????????????????????
        boolean isReworkJobSnLoad = false;
        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // ??????????????????
        final Date currentDate = new Date(System.currentTimeMillis());

        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
        MtMaterialLotVO3 materialLotParam = new MtMaterialLotVO3();
        materialLotParam.setTenantId(tenantId);
        materialLotParam.setSiteId(dto.getSiteId());
        materialLotParam.setMaterialLotCode(dto.getSnNum());
        //???????????????????????? add by yuchao.wang for lu.bai at 2020.10.19
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            materialLotParam.setEnableFlag(YES);
        }
        List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotParam);
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            this.reworkInSiteValidation(tenantId, materialLotIds.get(0), dto.getWorkcellId());
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                MtMaterialLot mtMaterialLot =
                        mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotIds.get(0));
                if (!dto.getMaterialId().equals(mtMaterialLot.getMaterialId())) {
                    // ??????????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_022", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_022", "HME"));
                }

                //??????????????????????????????????????????????????????????????? add by yuchao.wang for lu.bai at 2020.10.20
                //V20201002 modify by penglin.sui for lu.bai ?????????????????????????????????????????????????????????
                //if (dto.getPrepareQty().compareTo(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())) != 0) {
                //    String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_PREPARE");
                //    String materialPrepareEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                //        {
                //            setEventRequestId(eventRequestId);
                //            setWorkcellId(dto.getWorkcellId());
                //            setLocatorId(mtMaterialLot.getLocatorId());
                //            setEventTypeCode("MATERIAL_PREPARE");
                //        }
                //    });
                //    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                //    mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                //    mtMaterialLotVO2.setEventId(materialPrepareEventId);
                //    mtMaterialLotVO2.setPrimaryUomQty(dto.getPrepareQty().doubleValue());
                //    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);
                //}
            } else {
                snVO = hmeEoJobSnMapper.queryMaterialByLotId(tenantId, materialLotIds.get(0));
            }
            dto.setMaterialLotId(materialLotIds.get(0));
        } else {
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                // ????????????????????????
                //MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());

                //?????????????????? add by yuchao.wang for lu.bai at 2020.9.28
                MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId,
                        HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());
                if (Objects.isNull(mtModLocator)) {
                    //???????????????????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_090", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_090", "HME"));
                }

                String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_PREPARE");
                String materialPrepareEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                    {
                        setEventRequestId(eventRequestId);
                        setWorkcellId(dto.getWorkcellId());
                        setLocatorId(mtModLocator.getLocatorId());
                        setEventTypeCode("MATERIAL_PREPARE");
                    }
                });

                MtMaterialLotVO2 materialLotVO = new MtMaterialLotVO2();
                materialLotVO.setTenantId(tenantId);
                materialLotVO.setEventId(materialPrepareEventId);
                materialLotVO.setSiteId(dto.getSiteId());
                materialLotVO.setEnableFlag(HmeConstants.ConstantValue.NO);
                materialLotVO.setQualityStatus("OK");
                materialLotVO.setMaterialLotCode(dto.getSnNum());
                materialLotVO.setMaterialId(dto.getMaterialId());
                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
                materialLotVO.setPrimaryUomId(mtMaterial.getPrimaryUomId());
                materialLotVO.setPrimaryUomQty(new Double(String.valueOf(dto.getPrepareQty())));
                materialLotVO.setLocatorId(mtModLocator.getLocatorId());
                // materialLotVO.setEoId(dto.getEoId());
                materialLotVO.setLoadTime(currentDate);
                materialLotVO.setCreateReason("INITIALIZE");
                materialLotVO.setIdentification(dto.getSnNum());
                materialLotVO.setCid(Long.valueOf(customSequence.getNextKey("mt_material_lot_cid_s")));
                MtMaterialLotVO13 materialLotResult = mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotVO,
                        HmeConstants.ConstantValue.NO);

                // ??????????????????-???????????????NEW
                MtExtendVO10 materialLotExtend = new MtExtendVO10();
                materialLotExtend.setKeyId(materialLotResult.getMaterialLotId());
                List<MtExtendVO5> attrs = new ArrayList<>();
                MtExtendVO5 statusAttr = new MtExtendVO5();
                statusAttr.setAttrName(HmeConstants.ExtendAttr.STATUS);
                statusAttr.setAttrValue(HmeConstants.StatusCode.NEW);
                attrs.add(statusAttr);
                MtExtendVO5 statusAttr2 = new MtExtendVO5();
                statusAttr2.setAttrName("VIRTUAL_FLAG");
                statusAttr2.setAttrValue(YES);
                attrs.add(statusAttr2);
                materialLotExtend.setAttrs(attrs);
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotExtend);
                // ?????????dto??????ID??????
                dto.setMaterialLotId(materialLotResult.getMaterialLotId());
            } else {
                if (!HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType()) && !HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                    // ????????????SN????????????EO
                    throw new MtException("HME_EO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_005", "HME"));
                }
            }
        }

        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        // ????????????-??????
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            HmeEoJobSn eoJobSn = new HmeEoJobSn();
            eoJobSn.setTenantId(tenantId);
            eoJobSn.setWorkcellId(dto.getWorkcellId());
            eoJobSn.setMaterialLotId(dto.getMaterialLotId());
            hmeEoJobSn = hmeEoJobSnMapper.selectOne(eoJobSn);
            if (Objects.isNull(hmeEoJobSn)) {
                // ???????????????????????????
                dto.setReworkFlag(HmeConstants.ConstantValue.NO);
                currentJobSnVO = createSnJob(tenantId, dto);
                isSnQuery = false;

                dto.setJobId(currentJobSnVO.getJobId());
                dto.setEoId(currentJobSnVO.getEoId());
                dto.setEoStepId(currentJobSnVO.getEoStepId());
            } else {
                //??????????????? ?????????????????????????????? add by yuchao.wang for lu.bai at 2020.10.4
                MtMaterialLot mtMaterialLot =
                        mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
                if (!mtMaterialLot.getPrimaryUomQty().equals(dto.getPrepareQty().doubleValue())) {
                    //???????????????????????????????????????????????? ????????????????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_100", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_100", "HME"));
                }

                dto.setJobId(hmeEoJobSn.getJobId());
                dto.setEoId(hmeEoJobSn.getEoId());
                dto.setEoStepId(hmeEoJobSn.getEoStepId());
                BeanUtils.copyProperties(hmeEoJobSn, currentJobSnVO);
            }
        } else {
            // ?????????????????????-??????
            // ?????????????????????
            if (!HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                // ??????????????????????????????SN??????
                if (StringUtils.isNotBlank(dto.getEoStepId()) && dto.getEoStepNum() != null) {
                    String reworkFlag = HmeConstants.ConstantValue.NO;
                    MtExtendSettings reworkAttr = new MtExtendSettings();
                    reworkAttr.setAttrName("REWORK_FLAG");
                    List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_material_lot_attr", "MATERIAL_LOT_ID", dto.getMaterialLotId(),
                            Collections.singletonList(reworkAttr));
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                        if (StringUtils.isNotBlank(mtExtendAttrVOList.get(0).getAttrValue())) {
                            reworkFlag = mtExtendAttrVOList.get(0).getAttrValue();
                        }
                    }
                    HmeEoJobSn eoJobSn = new HmeEoJobSn();
                    eoJobSn.setWorkcellId(dto.getWorkcellId());
                    eoJobSn.setMaterialLotId(dto.getMaterialLotId());
                    eoJobSn.setEoStepId(dto.getEoStepId());
                    eoJobSn.setEoStepNum(dto.getEoStepNum());
                    hmeEoJobSn = hmeEoJobSnMapper.queryEoJobSn(tenantId, eoJobSn);
                    if (Objects.nonNull(hmeEoJobSn)) {
                        dto.setJobId(hmeEoJobSn.getJobId());
                        dto.setEoId(hmeEoJobSn.getEoId());
                        dto.setEoStepId(hmeEoJobSn.getEoStepId());
                        BeanUtils.copyProperties(hmeEoJobSn, currentJobSnVO);
                    }
                }
            } else {
                // ??????????????????-??????
                // ??????????????????????????????
                if (HmeConstants.LoadTypeCode.CONTAINER.equals(dto.getSnType())) {
                    //??????????????????????????????????????? add by yuchao.wang for tianyang.xie at 2020.9.19
                    hmeEoJobContainerRepository.deleteNotCurrentWkcData(tenantId, dto.getContainerId(), dto.getWorkcellId());

                    HmeEoJobContainer hmeEoJobContainer = new HmeEoJobContainer();
                    // ????????????????????????
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
                }
            }
        }

        // ????????????????????????
        boolean isReworkLoad = false;
        BigDecimal preStandardRepqTimeInProcess = null;
        // ?????????????????????????????????????????????????????????????????????????????????????????????
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType()) && Objects.isNull(hmeEoJobSn.getJobId())) {
            List<HmeEoJobSnVO3> snLineList = new ArrayList<>(16);
            if (HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                snLineList = dto.getSnLineList();
                snLineList.forEach(line -> {
                    line.setWorkcellId(dto.getWorkcellId());
                    line.setJobType(dto.getJobType());
                    line.setJobContainerId(dto.getJobContainerId());
                    line.setOperationId(dto.getOperationId());
                    line.setOperationIdList(dto.getOperationIdList());
                });
            } else {
                dto.setEoId(snVO.getEoId());
                dto.setMaterialLotId(snVO.getMaterialLotId());

                if (HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())) {
                    MtMaterialLotVO30 materialLotVo30 = new MtMaterialLotVO30();
                    materialLotVo30.setCode(dto.getSnNum());
                    materialLotVo30.setAllLevelFlag(YES);
                    MtMaterialLotVO29 isContainerVO =
                            mtMaterialLotRepository.codeOrIdentificationLimitObjectGet(tenantId, materialLotVo30);

                    if (HmeConstants.LoadTypeCode.CONTAINER.equals(isContainerVO.getCodeType())) {
                        isContainerInSite = true;

                        List<MtMaterialLotVO28> materialLotVOList = isContainerVO.getLoadingObjectlList();
                        List<String> eoMaterialLotIds = materialLotVOList.stream().map(MtMaterialLotVO28::getLoadObjectId)
                                .collect(Collectors.toList());

                        for (String materialLotId : eoMaterialLotIds) {
                            MtMaterialLot materialLot =
                                    mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotId);

                            HmeEoJobSnVO3 snLine = new HmeEoJobSnVO3();
                            BeanUtils.copyProperties(dto, snLine);
                            // ??????dto???????????????????????????????????????
                            snLine.setSnNum(materialLot.getMaterialLotCode());
                            snLine.setEoId(materialLot.getEoId());
                            snLine.setMaterialLotId(materialLotId);
                            snLineList.add(snLine);
                        }
                        resultVO.setIsContainer(true);
                    } else {
                        HmeEoJobSnVO3 snLine = new HmeEoJobSnVO3();
                        BeanUtils.copyProperties(dto, snLine);
                        snLineList.add(snLine);
                    }
                } else {
                    HmeEoJobSnVO3 snLine = new HmeEoJobSnVO3();
                    BeanUtils.copyProperties(dto, snLine);
                    snLineList.add(snLine);
                }
            }

            // ?????????????????????????????????SN?????????????????????????????????SN
            log.info("snLineList.size():" + snLineList.size());
            int loopCount = 0;
            // ??????????????????
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_IN");
            Boolean isBatchExecEoWorking = false;
            Boolean isBatchMoveInProcess = false;
            HmeEoJobSnVO14 hmeEoJobSnVO14 = new HmeEoJobSnVO14();
            List<HmeEoJobSnVO15> hmeEoJobSnVO15List = new ArrayList<>();
            List<MtEoRouterActualVO42> mtEoRouterActualVO42List = new ArrayList<>();
            //??????????????????????????????
            List<String> eoIdList = new ArrayList<>();
            Map<String, HmeRouterStepVO> nearStepMap = new HashMap<>();
            Map<String, HmeRouterStepVO> normalStepMap = new HashMap<>();
            List<MtEoStepWipVO1> mtEoStepWipVO1List = new ArrayList<>();
            Map<String, MtEoStepWip> mtEoStepWipMap = new HashMap<>();
            if (snLineList.size() > 0) {
                eoIdList = snLineList.stream().map(HmeEoJobSnVO3::getEoId).distinct().collect(Collectors.toList());
                nearStepMap = batchSelectNearStepByEoIds(tenantId, eoIdList);
                nearStepMap.forEach((k, v) -> {
                    MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                    mtEoStepWipVO1.setEoStepActualId(v.getEoStepActualId());
                    mtEoStepWipVO1List.add(mtEoStepWipVO1);
                });
                mtEoStepWipMap = batchSelectMtEoStepWipByEoIds(tenantId, mtEoStepWipVO1List);
                normalStepMap = batchSelectNormalStepByEoIds(tenantId, eoIdList);
            }
            Boolean currentJobSnVOReturnNullFlag = false;
            for (HmeEoJobSnVO3 jobSn : snLineList) {
                loopCount++;
                List<MtEoRouterActualVO51> queueMessageList = new ArrayList<>();
                MtEoRouterActualVO42 mtEoRouterActualVO42 = new MtEoRouterActualVO42();
                // ??????/??????????????????????????????????????????????????????????????????EO
                if ((HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType()) && isContainerInSite)) {
                    snVO = hmeEoJobSnMapper.queryMaterialByLotId(tenantId, jobSn.getMaterialLotId());
                }
                if (HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType()) && HmeConstants.LoadTypeCode.CONTAINER.equals(dto.getSnType())) {
                    //V20200821 modify by penglin.sui for fang.pan ??????????????????????????????????????????????????????????????????
                    log.info("???????????????????????????????????????????????? begin");
                    if (Objects.isNull(preStandardRepqTimeInProcess)) {
                        if (Objects.nonNull(jobSn.getStandardReqdTimeInProcess())) {
                            preStandardRepqTimeInProcess = jobSn.getStandardReqdTimeInProcess();
                        }
                    } else {
                        if (!preStandardRepqTimeInProcess.equals(jobSn.getStandardReqdTimeInProcess())) {
                            //????????????????????????????????????,???????????????
                            throw new MtException("HME_EO_JOB_SN_059", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_059", "HME"));
                        }
                    }
                    log.info("???????????????????????????????????????????????? end");
                    snVO = hmeEoJobSnMapper.queryMaterialByLotId(tenantId, jobSn.getMaterialLotId());
                }
                // ??????EO??????
                if (!HmeConstants.EoStatus.WORKING.equals(snVO.getStatus())
                        || (!HmeConstants.EoStatus.RELEASED.equals(snVO.getLastEoStatus())
                        && !HmeConstants.EoStatus.HOLD.equals(snVO.getLastEoStatus()))) {
                    throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
                }

                //V20200819 modify by penglin.sui for zhenyong.ban ??????SN????????????EO
                if (HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType()) || HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                    MtMaterialLotVO3 materialLotParam2 = new MtMaterialLotVO3();
                    materialLotParam2.setTenantId(tenantId);
                    materialLotParam2.setSiteId(jobSn.getSiteId());
                    materialLotParam2.setMaterialLotCode(jobSn.getSnNum());
                    materialLotParam2.setEnableFlag(YES);
                    List<String> materialLotIds2 = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotParam2);
                    if (CollectionUtils.isEmpty(materialLotIds2)) {
                        // ????????????SN????????????EO
                        throw new MtException("HME_EO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_005", "HME"));
                    }
                }

                // ???????????????????????????
                boolean isRouterStepEntry = false;

                String routerId = mtEoRouterRepository.eoRouterGet(tenantId, jobSn.getEoId());
                List<MtRouterStepVO5> routerStepList = mtRouterStepRepository.routerStepListQuery(tenantId, routerId);

                List<MtRouterStepVO5> resultStepList = new ArrayList<>(16);
                for (String operationId : jobSn.getOperationIdList()) {
                    List<MtRouterStepVO5> matchedStepList = routerStepList.stream()
                            .filter(routerStep -> routerStep.getOperationId().equals(operationId))
                            .collect(Collectors.toList());
                    resultStepList.addAll(matchedStepList);
                }

                if (CollectionUtils.isEmpty(resultStepList)) {
                    // ????????????????????????????????????,??????????????????????????????/????????????????????????
                    throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
                } else {
                    MtExtendSettings reworkAttr = new MtExtendSettings();
                    reworkAttr.setAttrName("REWORK_FLAG");

                    List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_material_lot_attr", "MATERIAL_LOT_ID", jobSn.getMaterialLotId(),
                            Collections.singletonList(reworkAttr));
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                        if (YES.equals(mtExtendAttrVOList.get(0).getAttrValue())) {
                            jobSn.setReworkFlag(YES);
                        } else {
                            jobSn.setReworkFlag(HmeConstants.ConstantValue.NO);
                        }
                    } else {
                        jobSn.setReworkFlag(HmeConstants.ConstantValue.NO);
                    }

                    if (HmeConstants.JobType.BATCH_PROCESS.equals(jobSn.getJobType()) &&
                            YES.equals(jobSn.getReworkFlag())) {
                        // SN?????????????????????????????????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_051",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_051", "HME"));
                    }

                    // ??????????????????
                    resultVO.setReworkFlag(jobSn.getReworkFlag());
                    //V20200924 modify by penglin.sui for tianyang.xie ??????/??????????????????????????????????????????????????????????????????
                    //V20201112 modify by yuchao.wang for tianyang.xie ??????????????????????????????
                    resultVO.setIsClickProcessComplete(YES);
                    if (YES.equals(jobSn.getReworkFlag())
                            && (HmeConstants.JobType.SINGLE_PROCESS.equals(jobSn.getJobType()) || HmeConstants.JobType.BATCH_PROCESS.equals(jobSn.getJobType()))) {
                        HmeNcRecordVO hmeNcRecordVO = hmeEoJobSnMapper.selectLastestNcRecordProcess(tenantId, jobSn.getMaterialLotId(), dto.getWorkcellId());
                        if (Objects.isNull(hmeNcRecordVO) || StringUtils.isBlank(hmeNcRecordVO.getNcRecordId())) {
                            //${1}????????? ?????????${2}
                            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0037", "GENERAL", "ncRecord", jobSn.getMaterialLotId()));
                        }
                        resultVO.setNcRecordWorkcellCode(hmeNcRecordVO.getRootCauseProcessCode());
                        resultVO.setNcRecordWorkcellName(hmeNcRecordVO.getRootCauseProcessName());
                        if (!StringUtils.equals(hmeNcRecordVO.getRootCauseProcessId(), hmeNcRecordVO.getCurrentProcessId())) {
                            resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.NO);
                        }
                    }

                    if (resultStepList.size() == 1) {
                        // ??????EO????????????????????????
                        log.info("??????EO????????????????????????");
                        jobSn.setEoStepId(resultStepList.get(0).getRouterStepId());
                        log.debug("??????????????????ID???routerStepId=" + jobSn.getEoStepId());
                        log.info("jobSn.getReworkFlag()???" + jobSn.getReworkFlag());
                        if (!YES.equals(jobSn.getReworkFlag())) {
                            // ???????????????OK
                            if (HmeConstants.ConstantValue.OK.equals(snVO.getQualityStatus())) {
                                // ???????????????????????????????????????
                                MtRouterOperation mtRouterOperation = mtRouterOperationRepository
                                        .routerOperationGet(tenantId, jobSn.getEoStepId());
                                if (Objects.nonNull(mtRouterOperation)) {
                                    jobSn.setOperationId(mtRouterOperation.getOperationId());
                                }

                                List<HmeEoJobSn> hmeEoJobSnList = selectByCondition(Condition.builder(HmeEoJobSn.class)
                                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                                                .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE,
                                                        jobSn.getJobType())
                                                .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID,
                                                        jobSn.getOperationId())
                                                .andEqualTo(HmeEoJobSn.FIELD_EO_ID, jobSn.getEoId())
                                                .andEqualTo(HmeEoJobSn.FIELD_EO_STEP_ID,
                                                        jobSn.getEoStepId())
                                                .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG,
                                                        HmeConstants.ConstantValue.NO)
                                                //V20200821 modify by penglin.sui for zhenyong.ban ??????/???????????????????????????????????????????????????????????????????????????eoSN????????????????????????
                                                .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, jobSn.getMaterialLotId()))
                                        .build());
                                log.info("jobSn.getMaterialLotId()--1???" + jobSn.getMaterialLotId());
                                log.info("hmeEoJobSnList--1???" + hmeEoJobSnList.size());

                                if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
                                    hmeEoJobSn = hmeEoJobSnList.get(0);
                                    isJobSnLoad = true;
                                } else {
                                    List<String> entryStepIdList =
                                            mtRouterStepRepository.routerEntryRouterStepGet(tenantId, routerId);
                                    if (CollectionUtils.isEmpty(entryStepIdList)) {
                                        throw new MtException("HME_EO_JOB_SN_030",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_030", "HME"));
                                    } else {
                                        if (jobSn.getEoStepId().equals(entryStepIdList.get(0))) {
                                            log.debug("??????????????????????????????????????????");
                                            isRouterStepEntry = true;
                                        }
                                    }

                                }
                            } else {
                                // ??????????????????OK, ??????:SN????????????????????????????????????????????????????????????
                                throw new MtException("HME_EO_JOB_SN_029", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_029", "HME"));
                            }
                        } else {
                            // ??????????????????, ???????????????Y
                            // ???????????????????????????????????????
                            MtRouterOperation mtRouterOperation = mtRouterOperationRepository
                                    .routerOperationGet(tenantId, jobSn.getEoStepId());
                            if (Objects.nonNull(mtRouterOperation)) {
                                jobSn.setOperationId(mtRouterOperation.getOperationId());
                            }
                            HmeEoJobSn reworkJobSnParam = new HmeEoJobSn();
                            reworkJobSnParam.setTenantId(tenantId);
                            reworkJobSnParam.setJobType(jobSn.getJobType());
                            reworkJobSnParam.setEoId(jobSn.getEoId());
                            reworkJobSnParam.setOperationId(jobSn.getOperationId());
                            reworkJobSnParam.setEoStepId(jobSn.getEoStepId());
                            reworkJobSnParam.setEoStepNum(jobSn.getEoStepNum());
                            reworkJobSnParam.setReworkFlag(jobSn.getReworkFlag());
                            //V20200821 modify by penglin.sui for zhenyong.ban ??????/???????????????????????????????????????????????????????????????????????????eoSN????????????????????????
                            reworkJobSnParam.setMaterialLotId(jobSn.getMaterialLotId());

                            List<HmeEoJobSn> reworkJobSnList = hmeEoJobSnMapper.select(reworkJobSnParam);
                            log.info("reworkJobSnList--1???" + reworkJobSnList.size());
                            if (CollectionUtils.isNotEmpty(reworkJobSnList)) {
                                List<HmeEoJobSn> currentStepJobSnList = reworkJobSnList.stream()
                                        .sorted(Comparator.comparing(HmeEoJobSn::getEoStepNum).reversed())
                                        .collect(Collectors.toList());
                                Optional<HmeEoJobSn> jobSnOptional = currentStepJobSnList.stream()
                                        .filter(sn -> sn.getSiteOutDate() == null).findFirst();
                                log.info("jobSnOptional.isPresent()--1???" + jobSnOptional.isPresent());
                                if (jobSnOptional.isPresent()) {
                                    hmeEoJobSn = jobSnOptional.get();
                                    isJobSnLoad = true;
                                } else {
                                    resultVO.setHmeEoJobSnList(currentStepJobSnList);
                                    // ????????????SN??????????????????
                                    resultVO.getHmeEoJobSnList()
                                            .forEach(step -> step.setCurrentStepName(mtRouterStepRepository
                                                    .routerStepGet(tenantId, step.getEoStepId())
                                                    .getStepName()));
                                    Integer currentEoStepNum = currentStepJobSnList.get(0).getEoStepNum();
                                    if (Objects.isNull(currentEoStepNum)) {
                                        jobSn.setEoStepNum(1);
                                    } else {
                                        jobSn.setEoStepNum(currentEoStepNum + 1);
                                    }
                                }
                            } else {
                                jobSn.setEoStepNum(1);
                            }
                        }
                    } else {
                        // ???SEQUENCE?????????
                        resultStepList = resultStepList.stream()
                                .sorted(Comparator.comparing(MtRouterStepVO5::getSequence))
                                .collect(Collectors.toList());
                        // ??????EO????????????????????????
                        log.debug("??????EO????????????????????????");
                        if (!YES.equals(jobSn.getReworkFlag())) {
                            log.info("jobSn.getReworkFlag()-2" + jobSn.getReworkFlag());
                            if (HmeConstants.ConstantValue.OK.equals(snVO.getQualityStatus())) {
                                List<HmeEoJobSn> jobSnList = new ArrayList<>(16);
                                for (MtRouterStepVO5 routerStep : resultStepList) {
                                    // ???????????????????????????????????????
                                    MtRouterOperation mtRouterOperation = mtRouterOperationRepository
                                            .routerOperationGet(tenantId, routerStep.getRouterStepId());
                                    if (Objects.nonNull(mtRouterOperation)) {
                                        jobSn.setOperationId(mtRouterOperation.getOperationId());
                                    }
                                    List<HmeEoJobSn> hmeEoJobSnList = selectByCondition(Condition
                                            .builder(HmeEoJobSn.class)
                                            .andWhere(Sqls.custom()
                                                    .andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                                                    .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE,
                                                            jobSn.getJobType())
                                                    .andEqualTo(HmeEoJobSn.FIELD_EO_ID, jobSn.getEoId())
                                                    .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID,
                                                            jobSn.getOperationId())
                                                    .andEqualTo(HmeEoJobSn.FIELD_EO_STEP_ID,
                                                            routerStep.getRouterStepId())
                                                    .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG,
                                                            HmeConstants.ConstantValue.NO)
                                                    //V20200821 modify by penglin.sui for zhenyong.ban ??????/???????????????????????????????????????????????????????????????????????????eoSN????????????????????????
                                                    .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID,
                                                            jobSn.getMaterialLotId()))
                                            .build());
                                    log.info("jobSn.getMaterialLotId()-2:" + jobSn.getMaterialLotId());
                                    log.info("hmeEoJobSnList.size()-2" + hmeEoJobSnList.size());
                                    if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
                                        Optional<HmeEoJobSn> jobSnOptional = hmeEoJobSnList.stream()
                                                .filter(sn -> sn.getSiteOutDate() == null).findFirst();
                                        log.info("jobSnOptional.isPresent()-2" + jobSnOptional.isPresent());
                                        if (jobSnOptional.isPresent()) {
                                            hmeEoJobSn = jobSnOptional.get();
                                            isJobSnLoad = true;
                                            break;
                                        } else {
                                            jobSnList.add(hmeEoJobSnList.get(0));
                                        }
                                    } else {
                                        jobSn.setEoStepId(routerStep.getRouterStepId());
                                        break;
                                    }
                                }

                                log.debug("????????????????????????=" + jobSn.getEoStepId());

                                if (CollectionUtils.isNotEmpty(jobSnList)) {
                                    log.debug("??????SN??????????????????????????????List???????????????????????????");
                                    resultVO.setHmeEoJobSnList(jobSnList);
                                    // ????????????SN??????????????????
                                    resultVO.getHmeEoJobSnList()
                                            .forEach(step -> step.setCurrentStepName(mtRouterStepRepository
                                                    .routerStepGet(tenantId, step.getEoStepId())
                                                    .getStepName()));
                                }

                                if (!isJobSnLoad) {
                                    // ???????????????????????????????????????
                                    if (StringUtils.isBlank(jobSn.getEoStepId())) {
                                        if (CollectionUtils.isNotEmpty(jobSnList)) {
                                            jobSn.setEoStepId(jobSnList.get(0).getEoStepId());
                                            hmeEoJobSn = jobSnList.get(0);
                                            isJobSnLoad = true;
                                            log.debug("??????????????????????????????????????????????????????=" + jobSn.getEoStepId());
                                        } else {
                                            // ????????????????????????????????????????????????, ?????????
                                            throw new MtException("HME_EO_JOB_SN_043",
                                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "HME_EO_JOB_SN_043", "HME"));
                                        }
                                    }
                                    // ????????????????????????
                                    List<String> entryStepIdList =
                                            mtRouterStepRepository.routerEntryRouterStepGet(tenantId, routerId);
                                    if (CollectionUtils.isEmpty(entryStepIdList)) {
                                        throw new MtException("HME_EO_JOB_SN_030",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_030", "HME"));
                                    } else {
                                        if (jobSn.getEoStepId().equals(entryStepIdList.get(0))) {
                                            log.debug("??????????????????????????????????????????");
                                            isRouterStepEntry = true;
                                        }
                                    }
                                }

                            } else {
                                // ??????????????????OK, ??????:SN????????????????????????????????????????????????????????????
                                throw new MtException("HME_EO_JOB_SN_029", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_029", "HME"));
                            }

                        } else {
                            List<HmeEoJobSn> jobSnList = new ArrayList<>(16);
                            List<HmeEoJobSn> eoStepList = new ArrayList<>(16);
                            if (StringUtils.isNotBlank(jobSn.getEoStepId())) {
                                log.info("jobSn.getEoStepId():" + jobSn.getEoStepId());
                                // ???????????????????????????????????????
                                HmeEoJobSn reworkJobSnParam = new HmeEoJobSn();
                                reworkJobSnParam.setTenantId(tenantId);
                                reworkJobSnParam.setJobType(jobSn.getJobType());
                                reworkJobSnParam.setEoId(jobSn.getEoId());
                                reworkJobSnParam.setOperationId(jobSn.getOperationId());
                                reworkJobSnParam.setEoStepId(jobSn.getEoStepId());
                                reworkJobSnParam.setEoStepNum(jobSn.getEoStepNum());
                                reworkJobSnParam.setReworkFlag(jobSn.getReworkFlag());
                                //V20200821 modify by penglin.sui for zhenyong.ban ??????/???????????????????????????????????????????????????????????????????????????eoSN????????????????????????
                                reworkJobSnParam.setMaterialLotId(jobSn.getMaterialLotId());

                                List<HmeEoJobSn> reworkJobSnList = hmeEoJobSnMapper.select(reworkJobSnParam);

                                if (CollectionUtils.isNotEmpty(reworkJobSnList)) {
                                    List<HmeEoJobSn> currentStepJobSnList = reworkJobSnList.stream()
                                            .sorted(Comparator.comparing(HmeEoJobSn::getEoStepNum).reversed())
                                            .collect(Collectors.toList());
                                    if (YES.equals(jobSn.getQueryReworkFlag())) {
                                        Optional<HmeEoJobSn> jobSnOptional = currentStepJobSnList.stream()
                                                .filter(sn -> sn.getSiteOutDate() == null).findFirst();

                                        if (jobSnOptional.isPresent()) {
                                            hmeEoJobSn = jobSnOptional.get();
                                            isJobSnLoad = true;
                                        } else {
                                            if (Objects.isNull(jobSn.getEoStepNum())) {
                                                // ????????????????????????????????????????????????????????????
                                                log.info("======================????????????????????????????????????????????????????????????==========================???" + isReworkLoad);
                                                isReworkLoad = true;
                                            }
                                        }
                                        jobSnList.addAll(currentStepJobSnList);

                                    } else {
                                        Integer newEoStepNum = currentStepJobSnList.get(0).getEoStepNum() + 1;
                                        jobSn.setEoStepNum(newEoStepNum);
                                    }
                                } else {
                                    if (YES.equals(jobSn.getQueryReworkFlag())) {
                                        //???????????????????????????
                                        throw new MtException("HME_EO_JOB_SN_049",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_049", "HME"));
                                    }
                                    jobSn.setEoStepNum(1);
                                }
                            } else {
                                // ???????????????????????????????????????????????????????????????????????????
                                for (MtRouterStepVO5 routerStep : resultStepList) {
                                    HmeEoJobSn eoStepJobSn = new HmeEoJobSn();
                                    eoStepJobSn.setJobType(jobSn.getJobType());
                                    eoStepJobSn.setEoId(jobSn.getEoId());
                                    eoStepJobSn.setOperationId(routerStep.getOperationId());
                                    eoStepJobSn.setEoStepId(routerStep.getRouterStepId());
                                    eoStepJobSn.setCurrentStepName(routerStep.getStepName());
                                    eoStepJobSn.setReworkFlag(YES);
                                    eoStepJobSn.setEoId(jobSn.getEoId());
                                    eoStepList.add(eoStepJobSn);
                                }
                                // ?????????????????????????????????????????????????????????????????????
                                log.debug("?????????????????????????????????");
                                resultVO.setHmeEoStepList(eoStepList);
                                isReworkLoad = true;
                            }
                            // ??????????????????????????????
                            if (CollectionUtils.isNotEmpty(jobSnList)) {
                                log.debug("???????????????SN??????????????????????????????List???????????????????????????");
                                resultVO.setHmeEoJobSnList(jobSnList);
                                // ????????????SN??????????????????
                                if (CollectionUtils.isNotEmpty(resultVO.getHmeEoJobSnList())) {
                                    resultVO.getHmeEoJobSnList()
                                            .forEach(step -> step.setCurrentStepName(mtRouterStepRepository
                                                    .routerStepGet(tenantId, step.getEoStepId())
                                                    .getStepName()));
                                }
                            }
                            // ??????????????????????????????
                            if (!isJobSnLoad) {
                                // ???????????????????????????????????????
                                if (StringUtils.isBlank(jobSn.getEoStepId())) {
                                    if (CollectionUtils.isEmpty(eoStepList)) {
                                        // ????????????????????????????????????????????????, ?????????
                                        throw new MtException("HME_EO_JOB_SN_043",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_043", "HME"));
                                    }
                                } else {
                                    // ???????????????????????????jobSN??????
                                    if (isReworkLoad && CollectionUtils.isNotEmpty(jobSnList)) {
                                        if (StringUtils.isNotBlank(jobSnList.get(0).getJobId())) {
                                            jobSn.setEoStepId(jobSnList.get(0).getEoStepId());
                                            hmeEoJobSn = jobSnList.get(0);
                                            isJobSnLoad = true;
                                            isReworkLoad = false;
                                            log.debug("?????????????????????????????????????????????????????????=" + hmeEoJobSn.getEoStepId());
                                        } else {
                                            log.debug("????????????????????????????????????");
                                        }
                                    }
                                }
                            }

                        }
                    }
                    // ??????????????????????????????
                    String operationId = "";
                    String eoId = "";
                    String eoStepId = "";
                    currentJobSnVOReturnNullFlag = false;
                    log.info("================================isJobSnLoad================================:" + isJobSnLoad);
                    if (isJobSnLoad) {
                        if (Objects.nonNull(hmeEoJobSn)) {
                            log.debug("??????????????????ID=" + hmeEoJobSn.getJobId());
                            dto.setJobId(hmeEoJobSn.getJobId());
                            dto.setEoId(hmeEoJobSn.getEoId());
                            dto.setOperationId(hmeEoJobSn.getOperationId());
                            dto.setEoStepId(hmeEoJobSn.getEoStepId());
                            BeanUtils.copyProperties(hmeEoJobSn, currentJobSnVO);

                            operationId = hmeEoJobSn.getOperationId();
                            eoId = hmeEoJobSn.getEoId();
                            eoStepId = hmeEoJobSn.getEoStepId();
                        }
                    } else {
                        log.debug("????????????OperationId=" + jobSn.getOperationId());

                        operationId = jobSn.getOperationId();
                        eoId = jobSn.getEoId();
                        eoStepId = jobSn.getEoStepId();

                        MtOperation mtOperation = mtOperationRepository.operationGet(tenantId, jobSn.getOperationId());
                        if ("TIME".equals(mtOperation.getOperationType()) && !HmeConstants.JobType.TIME_PROCESS.equals(jobSn.getJobType())) {
                            // ???????????????????????????????????????????????????
                            throw new MtException("HME_EO_JOB_SN_052",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EO_JOB_SN_052", "HME"));
                        }
                        log.info("======================isReworkLoad==========================???" + isReworkLoad);
                        if (!isReworkLoad) {
                            // ???EO??????????????????, ????????????????????????JobSN
                            // ??????????????????????????????
                            if (isRouterStepEntry) {
                                // ???SN????????????????????????
                                if (HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())
                                        || HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())) {
                                    if (loopCount == 1) {
                                        hmeEoJobSnVO14.setWorkcellId(jobSn.getWorkcellId());
                                    }
                                    HmeEoJobSnVO15 hmeEoJobSnVO15 = new HmeEoJobSnVO15();
                                    hmeEoJobSnVO15.setEoId(jobSn.getEoId());
                                    hmeEoJobSnVO15.setEoQty(snVO.getEoQty());
                                    hmeEoJobSnVO15List.add(hmeEoJobSnVO15);
                                    isBatchExecEoWorking = true;
                                } else {
                                    eoWorking(tenantId, snVO.getEoQty(), jobSn, eventRequestId);
                                }
                            } else {
                                if (!YES.equals(jobSn.getReworkFlag())) {
                                    log.debug("????????????????????????");
                                    // ?????????????????????????????????, ??????????????????
                                    HmeRouterStepVO nearStepVO = nearStepMap.get(jobSn.getEoId());
                                    if (Objects.isNull(nearStepVO)) {
                                        // ????????????????????????, ?????????EO??????????????????????????????
                                        throw new MtException("HME_EO_JOB_SN_046",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_046", "HME"));
                                    }

                                    //????????????EO????????????????????? ???????????????,???????????????????????????????????? modify by yuchao.wang for jiao.chen at 2020.9.28
                                    if (nearStepVO.getCompletedQty().compareTo(BigDecimal.valueOf(0)) == 0
                                            && !isAfterSalesWorkOrder(tenantId, jobSn.getEoId())) {
                                        MtRouterStep nearRouterStep = mtRouterStepRepository.routerStepGet(tenantId,
                                                nearStepVO.getRouterStepId());
                                        // ????????????????????????{????????????????????????}???????????????????????????????????????????????????
                                        throw new MtException("HME_EO_JOB_SN_031",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_031", "HME",
                                                        nearRouterStep.getStepName()));
                                    }

                                    HmeRouterStepVO nearNormalStepVO =
                                            hmeEoJobSnMapper.selectNormalStepByEoId(tenantId, jobSn.getEoId());
                                    List<MtRouterNextStep> nextStepList = mtRouterNextStepRepository
                                            .routerNextStepQuery(tenantId, nearNormalStepVO.getRouterStepId());
                                    log.debug("????????????????????????: RouterStepId = " + nearStepVO.getRouterStepId());
                                    log.debug("??????????????????????????????: RouterStepId = " + nearNormalStepVO.getRouterStepId());
                                    // ??????????????????????????????????????????????????????
                                    if (jobSn.getEoStepId().equals(nextStepList.get(0).getNextStepId())) {

                                        // ???SN????????????????????????
                                        if (HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())
                                                || HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())) {

                                            MtEoRouterActualVO51 queueProcessInfo = new MtEoRouterActualVO51();
                                            queueProcessInfo.setEoId(jobSn.getEoId());
                                            queueProcessInfo.setPreviousStepId(nearStepVO.getEoStepActualId());
                                            queueProcessInfo.setQty(snVO.getEoQty().doubleValue());
                                            queueProcessInfo.setRouterStepId(jobSn.getEoStepId());
                                            queueProcessInfo.setWorkcellId(nearStepVO.getWorkcellId());
                                            queueMessageList.add(queueProcessInfo);
                                            mtEoRouterActualVO42.setQueueMessageList(queueMessageList);
                                            mtEoRouterActualVO42.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
                                            mtEoRouterActualVO42List.add(mtEoRouterActualVO42);

                                            if (loopCount == 1) {
                                                hmeEoJobSnVO14.setWorkcellId(jobSn.getWorkcellId());
                                            }

                                            isBatchMoveInProcess = true;

                                            HmeEoJobSnVO15 hmeEoJobSnVO15 = new HmeEoJobSnVO15();
                                            hmeEoJobSnVO15.setEoId(jobSn.getEoId());
                                            hmeEoJobSnVO15.setEoQty(snVO.getEoQty());
                                            hmeEoJobSnVO15List.add(hmeEoJobSnVO15);
                                            isBatchExecEoWorking = true;
                                        } else {
                                            MtEoRouterActualVO15 eoRouterActual = new MtEoRouterActualVO15();
                                            eoRouterActual.setEoId(jobSn.getEoId());
                                            eoRouterActual.setQty(new Double(String.valueOf(snVO.getEoQty())));
                                            eoRouterActual.setPreviousStepId(nearStepVO.getEoStepActualId());
                                            eoRouterActual.setRouterStepId(jobSn.getEoStepId());
                                            // ???????????????????????????ID
                                            MtEoStepWipVO1 eoStepWipParam = new MtEoStepWipVO1();
                                            eoStepWipParam.setEoStepActualId(nearStepVO.getEoStepActualId());
                                            List<MtEoStepWip> mtEoStepWipList =
                                                    mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, eoStepWipParam);
                                            if (CollectionUtils.isEmpty(mtEoStepWipList)) {
                                                // ??????????????????????????????????????????????????????
                                                throw new MtException("HME_EO_JOB_SN_034",
                                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "HME_EO_JOB_SN_034", "HME"));
                                            } else {
                                                log.debug("????????????????????????ID = " + mtEoStepWipList.get(0).getWorkcellId());
                                                eoRouterActual.setWorkcellId(mtEoStepWipList.get(0).getWorkcellId());
                                            }
                                            //V20200826 modify by penglin.sui for tianyang.xie ??????????????????:SourceStatus
                                            eoRouterActual.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
                                            mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, eoRouterActual);

                                            eoWorking(tenantId, snVO.getEoQty(), jobSn, eventRequestId);
                                        }
                                    } else {
                                        MtRouterStep nearRouterStep = mtRouterStepRepository.routerStepGet(tenantId,
                                                nearStepVO.getRouterStepId());
                                        // ????????????????????????????????????{????????????????????????}???????????????
                                        throw new MtException("HME_EO_JOB_SN_032",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_032", "HME",
                                                        nearRouterStep.getStepName()));
                                    }
                                } else {
                                    // ????????????????????????
                                    HmeRouterStepVO reworkNearStepVO = nearStepMap.get(jobSn.getEoId());
                                    if (Objects.isNull(reworkNearStepVO)) {
                                        // ?????????????????????????????????
                                        throw new MtException("HME_EO_JOB_SN_045",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_045", "HME"));
                                    }
                                    //V20200827 modify by penglin.sui for jiao.chen ??????????????????????????????????????????????????????CompleteQty???0???????????????????????????
//                                    if (reworkNearStepVO.getCompletedQty().compareTo(BigDecimal.valueOf(0)) == 0) {
//                                        MtRouterStep nearRouterStep = mtRouterStepRepository.routerStepGet(tenantId,
//                                                        reworkNearStepVO.getRouterStepId());
//                                        // ????????????????????????{????????????????????????}???????????????????????????????????????????????????
//                                        throw new MtException("HME_EO_JOB_SN_035",
//                                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                                                        "HME_EO_JOB_SN_035", "HME",
//                                                                        nearRouterStep.getStepName()));
//                                    }

                                    // ????????????????????????
                                    HmeRouterStepVO normalNearStepVO = normalStepMap.get(jobSn.getEoId());
                                    if (Objects.isNull(normalNearStepVO)) {
                                        // ?????????????????????????????????
                                        throw new MtException("HME_EO_JOB_SN_045",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_045", "HME"));
                                    }
                                    log.debug("???????????????=" + resultStepList.get(0).getSequence());
                                    Optional<MtRouterStepVO5> currentStepOptional = resultStepList.stream()
                                            .filter(step -> step.getRouterStepId().equals(jobSn.getEoStepId()))
                                            .findFirst();
                                    Optional<MtRouterStepVO5> normalNewStepOptional = routerStepList.stream()
                                            .filter(step -> step.getRouterStepId()
                                                    .equals(normalNearStepVO.getRouterStepId()))
                                            .findFirst();
                                    if (normalNewStepOptional.isPresent() && currentStepOptional.isPresent()) {
                                        log.debug("????????????????????????=" + normalNewStepOptional.get().getSequence());
                                        if (currentStepOptional.get().getSequence()
                                                .compareTo(normalNewStepOptional.get().getSequence()) > 0) {
                                            MtRouterStep nearRouterStep = mtRouterStepRepository.routerStepGet(tenantId,
                                                    normalNearStepVO.getRouterStepId());
                                            // ??????????????????????????????{??????????????????}?????????????????????
                                            throw new MtException("HME_EO_JOB_SN_036",
                                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "HME_EO_JOB_SN_036", "HME",
                                                            nearRouterStep.getStepName()));
                                        }
                                    }

                                    // ???SN????????????????????????
                                    if (HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())
                                            || HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())) {

                                        MtEoRouterActualVO51 queueProcessInfo = new MtEoRouterActualVO51();
                                        queueProcessInfo.setEoId(jobSn.getEoId());
                                        queueProcessInfo.setPreviousStepId(reworkNearStepVO.getEoStepActualId());
                                        queueProcessInfo.setQty(snVO.getEoQty().doubleValue());
                                        queueProcessInfo.setRouterStepId(jobSn.getEoStepId());
                                        queueProcessInfo.setSourceEoStepActualId(normalNearStepVO.getEoStepActualId());
                                        MtEoStepWip mtEoStepWip = new MtEoStepWip();
                                        mtEoStepWip.setWorkcellId(jobSn.getWorkcellId());
                                        queueProcessInfo.setWorkcellId(mtEoStepWipMap.getOrDefault(reworkNearStepVO.getEoStepActualId(), mtEoStepWip).getWorkcellId());
                                        queueMessageList.add(queueProcessInfo);
                                        if (loopCount == 1) {
                                            hmeEoJobSnVO14.setWorkcellId(jobSn.getWorkcellId());
                                        }

                                        mtEoRouterActualVO42.setReworkStepFlag(jobSn.getReworkFlag());
                                        mtEoRouterActualVO42.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(reworkNearStepVO));
                                        mtEoRouterActualVO42.setQueueMessageList(queueMessageList);
                                        mtEoRouterActualVO42List.add(mtEoRouterActualVO42);

                                        isBatchMoveInProcess = true;

                                        HmeEoJobSnVO15 hmeEoJobSnVO15 = new HmeEoJobSnVO15();
                                        hmeEoJobSnVO15.setEoId(jobSn.getEoId());
                                        hmeEoJobSnVO15.setEoQty(snVO.getEoQty());
                                        hmeEoJobSnVO15List.add(hmeEoJobSnVO15);
                                        isBatchExecEoWorking = true;
                                    } else {
                                        MtEoRouterActualVO15 eoRouterActual = new MtEoRouterActualVO15();
                                        eoRouterActual.setRouterStepId(jobSn.getEoStepId());
                                        eoRouterActual.setEoId(snVO.getEoId());
                                        log.debug("??????EO??????????????????=" + String.valueOf(snVO.getEoQty()));
                                        eoRouterActual.setQty(new Double(String.valueOf(snVO.getEoQty())));
                                        // ????????????????????????????????????ID
                                        eoRouterActual.setPreviousStepId(reworkNearStepVO.getEoStepActualId());

                                        // ???????????????????????????ID
                                        MtEoStepWipVO1 eoStepWipParam = new MtEoStepWipVO1();
                                        eoStepWipParam.setEoStepActualId(reworkNearStepVO.getEoStepActualId());
                                        List<MtEoStepWip> mtEoStepWipList =
                                                mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, eoStepWipParam);
                                        if (CollectionUtils.isNotEmpty(mtEoStepWipList)) {
                                            eoRouterActual.setWorkcellId(mtEoStepWipList.get(0).getWorkcellId());
                                        } else {
                                            eoRouterActual.setWorkcellId(jobSn.getWorkcellId());
                                        }
                                        log.debug("???????????????????????????ID = " + eoRouterActual.getWorkcellId());

                                        eoRouterActual.setReworkStepFlag(jobSn.getReworkFlag());
                                        // ????????????????????????????????????ID
                                        log.debug("????????????????????????: SourceEoStepActualId = "
                                                + normalNearStepVO.getEoStepActualId());
                                        eoRouterActual.setSourceEoStepActualId(normalNearStepVO.getEoStepActualId());
                                        //V20200826 modify by penglin.sui for tianyang.xie ??????????????????:SourceStatus
                                        eoRouterActual.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(reworkNearStepVO));
                                        // ????????????????????????
                                        mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, eoRouterActual);

                                        eoWorking(tenantId, snVO.getEoQty(), jobSn, eventRequestId);
                                    }
                                }
                            }

                            if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType())
                                    || HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())) {
                                // ????????????SN?????????????????????????????????????????????????????????????????????????????????
                                if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
                                    log.debug("??????SN???????????????????????????");
                                    jobSn.setSourceContainerId(snVO.getCurrentContainerId());

                                    // ????????????
                                    MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
                                    mtContainerVO25.setContainerId(snVO.getCurrentContainerId());
                                    mtContainerVO25.setLoadObjectId(snVO.getMaterialLotId());
                                    mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                                    mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
                                }
                            }

                            // ???????????????????????????
                            jobSn.setSiteId(snVO.getSiteId());
                            jobSn.setWorkOrderId(snVO.getWorkOrderId());
                            jobSn.setMaterialId(snVO.getMaterialId());
                            if (!YES.equals(jobSn.getReworkFlag())) {
                                jobSn.setEoStepNum(HmeConstants.ConstantValue.ONE);
                            }
                            currentJobSnVO = createSnJob(tenantId, jobSn);
                            isSnQuery = false;
                            //??????????????????????????????????????????
                            if (YES.equals(jobSn.getReworkFlag())) {
                                isReworkJobSnLoad = true;
                            }
                            // ?????????????????????????????????????????????????????????
                            if (Objects.nonNull(currentJobSnVO)) {
                                dto.setJobId(currentJobSnVO.getJobId());
                                dto.setEoId(currentJobSnVO.getEoId());
                                dto.setOperationId(currentJobSnVO.getOperationId());
                                dto.setEoStepId(currentJobSnVO.getEoStepId());
                                dto.setSourceJobId(currentJobSnVO.getSourceJobId());
                            } else {
                                //return null;
                                currentJobSnVOReturnNullFlag = true;
                            }
                        }
                    }

                    //V20200815 modify by penglin.sui for zhenyong.ban
                    // 1.???????????????????????????????????????????????????????????????????????????????????????
                    // 2.?????????????????????????????????????????????????????????????????????????????????????????????
                    if (HmeConstants.JobType.SINGLE_PROCESS.equals(jobSn.getJobType()) || HmeConstants.JobType.BATCH_PROCESS.equals(jobSn.getJobType()) || HmeConstants.JobType.TIME_PROCESS.equals(jobSn.getJobType())) {

                        //V20210525 modify by penglin.sui for peng.zhao ??????????????????????????????
                        if(loopCount == 1) {
                            hmeEoJobSnCommonService.workcellBindEquipmentValidate(tenantId, operationId, dto.getWorkcellId());
                        }

                        if (!YES.equals(jobSn.getReworkFlag())) {
                            HmeEoJobSn snCondition = new HmeEoJobSn();
                            snCondition.setTenantId(tenantId);
                            snCondition.setEoId(eoId);
                            snCondition.setOperationId(operationId);
                            snCondition.setEoStepId(eoStepId);
                            snCondition.setReworkFlag(HmeConstants.ConstantValue.NO);
                            HmeEoJobSn hmeEoJobSn2 = this.selectOne(snCondition);
                            if (Objects.nonNull(hmeEoJobSn2)) {
                                if (!hmeEoJobSn2.getWorkcellId().equals(jobSn.getWorkcellId())) {
                                    // ???????????????????????????????????????
                                    throw new MtException("HME_EO_JOB_SN_054",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                    "HME_EO_JOB_SN_054", "HME"));
                                }

                                //????????????????????????????????????????????????
                                if (HmeConstants.JobType.BATCH_PROCESS.equals(jobSn.getJobType())) {
                                    if (hmeEoJobSn2.getSiteOutDate() != null) {
                                        // ?????????????????????????????????????????????????????????
                                        throw new MtException("HME_EO_JOB_SN_055",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                        "HME_EO_JOB_SN_055", "HME"));
                                    }
                                }
                            }
                        }

                        //????????????????????????NULL?????????/?????????????????????????????????????????????????????????????????????
                        if (currentJobSnVOReturnNullFlag) {
                            if (loopCount < snLineList.size()) {
                                continue;
                            } else {
                                break;
                            }
                        }
                    }
                }

                // ??????????????????????????????SN??????????????????
                if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())
                        && !HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                    // ??????????????????????????????????????????????????????????????????
                    if (!isSnQuery && !isJobSnLoad && !isReworkLoad) {
                        log.debug("??????????????????????????????????????????????????????????????????");
                        if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
                            // ??????????????????
                            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
                            equSwitchParams.setJobId(currentJobSnVO.getJobId());
                            equSwitchParams.setWorkcellId(dto.getWorkcellId());
                            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
                            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
                        }
                    }
                }
            }

            //V20201028 modify by penglin.sui ?????????????????????API
            if (isBatchMoveInProcess) {
                //V20201029 modify by penglin.sui for tianyang.xie ???????????? ???????????? + ?????? + ?????????????????????????????????API
                log.info("<=======================mtEoRouterActualVO42List.size========================>:" + mtEoRouterActualVO42List.size());
                Map<String, List<MtEoRouterActualVO42>> mtEoRouterActualVO42Map = mtEoRouterActualVO42List.stream().collect(Collectors.groupingBy(e -> fetchGroupKey(e)));
                log.info("<=======================mtEoRouterActualVO42Map.size========================>:" + mtEoRouterActualVO42Map.size());
                int loopCount2 = 0;
                for (String key : mtEoRouterActualVO42Map.keySet()) {
                    loopCount2 = 0;
                    List<MtEoRouterActualVO51> queueMessageList = new ArrayList<>();
                    MtEoRouterActualVO42 mtEoRouterActualVO42 = new MtEoRouterActualVO42();
                    for (MtEoRouterActualVO42 value : mtEoRouterActualVO42Map.get(key)) {
                        if (loopCount2 == 0) {
                            mtEoRouterActualVO42.setReworkStepFlag(value.getReworkStepFlag());
                            mtEoRouterActualVO42.setSourceStatus(value.getSourceStatus());
                        }
                        queueMessageList.addAll(value.getQueueMessageList());
                        loopCount2++;
                    }
                    mtEoRouterActualVO42.setQueueMessageList(queueMessageList);
                    mtEoStepActualRepository.eoNextStepMoveInBatchProcess(tenantId, mtEoRouterActualVO42);
                }
            }

            if (isBatchExecEoWorking) {
                hmeEoJobSnVO14.setHmeEoJobSnVO15List(hmeEoJobSnVO15List);
                eoBatchWorking(tenantId, hmeEoJobSnVO14, eventRequestId);
            }
            if (currentJobSnVOReturnNullFlag) {
                return null;
            }
        }

        if (!isReworkLoad) {
            // ???????????????????????????????????????SN??????????????????SN???????????????????????????????????????????????????????????????
            if (!HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                // ????????????????????????????????????????????????????????????
                List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOListAll = new ArrayList<HmeEoJobLotMaterialVO>();
                List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOListAll = new ArrayList<HmeEoJobTimeMaterialVO>();
                List<HmeEoJobMaterialVO> hmeEoJobMaterialVOAllList = new ArrayList<HmeEoJobMaterialVO>();

                //???????????????????????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.10.21
                if (!HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType()) || !YES.equals(dto.getBatchProcessSnScanFlag())) {
                    if (isSnQuery || isJobSnLoad) {
                        // ??????????????????????????????
                        HmeEoJobMaterialVO jobLotCondition = new HmeEoJobMaterialVO();
                        jobLotCondition.setWorkcellId(dto.getWorkcellId());
                        jobLotCondition.setJobId(hmeEoJobSn.getJobId());
                        jobLotCondition.setEoId(dto.getEoId());
                        jobLotCondition.setOperationId(dto.getOperationId());
                        jobLotCondition.setJobType(dto.getJobType());
                        jobLotCondition.setEoStepId(dto.getEoStepId());
                        jobLotCondition.setWorkOrderId(dto.getWorkOrderId());
                        jobLotCondition.setMaterialCode(dto.getMaterialCode());
                        jobLotCondition.setMaterialId(dto.getMaterialId());
                        jobLotCondition.setSiteId(dto.getSiteId());
                        jobLotCondition.setPrepareQty(dto.getPrepareQty());
                        jobLotCondition.setReworkFlag(currentJobSnVO.getReworkFlag());
                        hmeEoJobLotMaterialVOListAll = hmeEoJobLotMaterialRepository
                                .matchedJobLotMaterialQuery(tenantId, jobLotCondition, null);
                        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialVOListAll)) {
                            currentJobSnVO.setLotMaterialVOList(hmeEoJobLotMaterialVOListAll);
                        }
                        // ?????????????????????????????????, ???????????????
                        if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                            // ??????????????????????????????
                            HmeEoJobMaterialVO jobTimeCondition = new HmeEoJobMaterialVO();
                            jobTimeCondition.setWorkcellId(dto.getWorkcellId());
                            jobTimeCondition.setJobId(hmeEoJobSn.getJobId());
                            jobTimeCondition.setEoId(dto.getEoId());
                            jobTimeCondition.setOperationId(dto.getOperationId());
                            jobTimeCondition.setJobType(dto.getJobType());
                            jobTimeCondition.setEoStepId(dto.getEoStepId());
                            jobTimeCondition.setWorkOrderId(dto.getWorkOrderId());
                            jobTimeCondition.setMaterialCode(dto.getMaterialCode());
                            jobTimeCondition.setMaterialId(dto.getMaterialId());
                            jobTimeCondition.setSiteId(dto.getSiteId());
                            jobTimeCondition.setPrepareQty(dto.getPrepareQty());
                            jobTimeCondition.setReworkFlag(currentJobSnVO.getReworkFlag());
                            hmeEoJobTimeMaterialVOListAll = hmeEoJobTimeMaterialRepository
                                    .matchedJobTimeMaterialQuery(tenantId, jobTimeCondition, null);
                            if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialVOListAll)) {
                                currentJobSnVO.setTimeMaterialVOList(hmeEoJobTimeMaterialVOListAll);
                            }
                        }

                        // ??????????????????????????????
                        HmeEoJobMaterialVO2 jobMaterialCondition = new HmeEoJobMaterialVO2();
                        jobMaterialCondition.setJobId(hmeEoJobSn.getJobId());
                        jobMaterialCondition.setWorkcellId(dto.getWorkcellId());
                        jobMaterialCondition.setMaterialId(dto.getMaterialId());
                        jobMaterialCondition.setSiteId(dto.getSiteId());
                        jobMaterialCondition.setWorkOrderId(dto.getWorkOrderId());
                        jobMaterialCondition.setJobType(dto.getJobType());
                        jobMaterialCondition.setEoId(dto.getEoId());
                        jobMaterialCondition.setOperationId(dto.getOperationId());
                        hmeEoJobMaterialVOAllList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId,
                                jobMaterialCondition);
                        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialVOAllList)) {
                            currentJobSnVO.setMaterialVOList(hmeEoJobMaterialVOAllList);
                        }

                        // ??????????????????????????????
                        HmeEoJobMaterialVO2 jobDataRecordCondition = new HmeEoJobMaterialVO2();
                        jobDataRecordCondition.setWorkcellId(dto.getWorkcellId());
                        jobDataRecordCondition.setJobId(hmeEoJobSn.getJobId());
                        jobDataRecordCondition.setJobType(dto.getJobType());
                        if(Objects.isNull(hmeEoJobSn) || StringUtils.isBlank(hmeEoJobSn.getJobId())){
                            //????????????${1}??????????????????????????????,?????????!
                            throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_CHIP_TRANSFER_010", "HME",dto.getSnNum()));
                        }
                        currentJobSnVO.setDataRecordVOList(hmeEoJobDataRecordRepository.eoJobDataRecordQuery(tenantId,
                                jobDataRecordCondition));
                    }
                }

                // ?????????????????????????????????????????????
                if (StringUtils.isNotBlank(dto.getEoStepId())) {
                    HmeRouterStepVO5 currentStep = hmeEoJobSnCommonService.queryCurrentAndNextStepByCurrentId(tenantId, dto.getEoStepId());
                    if(Objects.nonNull(currentStep)){
                        resultVO.setCurrentStepName(currentStep.getStepName());
                        resultVO.setCurrentStepDescription(currentStep.getDescription());
                        resultVO.setCurrentStepSequence(currentStep.getSequence());
                        resultVO.setNextStepName(currentStep.getNextStepName());
                        resultVO.setNextStepDescription(currentStep.getNextDescription());

                        //V20210320 modify by penglin.sui for hui.ma ????????????????????????????????????????????????SN?????????????????????
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

                        resultVO.setLabCode(currentStep.getLabCode());
                        resultVO.setRouterStepRemark(currentStep.getRouterStepRemark());

                        //V20210125 modify by penglin.sui for hui.ma ????????????????????????
                        if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType())) {
                            if (StringUtils.isNotBlank(resultVO.getLabCode())) {
                                HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
                                hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
                                hmeMaterialLotLabCodePara.setRouterStepId(dto.getEoStepId());
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
                                    insertHmeMaterialLotLabCode.setWorkOrderId(dto.getWorkOrderId());
                                    insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                                    insertHmeMaterialLotLabCode.setRouterStepId(dto.getEoStepId());
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
                    }
                }

                //V20200828 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????????????????????????????
                if (CollectionUtils.isNotEmpty(currentJobSnVO.getMaterialVOList())) {
                    resultVO.setMaterialVOList(currentJobSnVO.getMaterialVOList().stream()
                            .sorted(Comparator.comparing(HmeEoJobMaterialVO::getLineNumber)).collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(currentJobSnVO.getLotMaterialVOList())) {
                    resultVO.setLotMaterialVOList(currentJobSnVO.getLotMaterialVOList().stream()
                            .sorted(Comparator.comparing(HmeEoJobLotMaterialVO::getLineNumber)).collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(currentJobSnVO.getTimeMaterialVOList())) {
                    resultVO.setTimeMaterialVOList(currentJobSnVO.getTimeMaterialVOList().stream()
                            .sorted(Comparator.comparing(HmeEoJobTimeMaterialVO::getLineNumber)).collect(Collectors.toList()));
                }

                //V20200824 modify by penglin.sui for zhenyong.ban ?????????????????????
                //V20200824 modify by yuchao.wang for tianyang.xie ?????????????????????????????????????????????????????????????????????????????????
                if (CollectionUtils.isNotEmpty(currentJobSnVO.getDataRecordVOList())) {
                    resultVO.setDataRecordVOList(currentJobSnVO.getDataRecordVOList());
                //    resultVO.setDataRecordVOList(currentJobSnVO.getDataRecordVOList().stream()
                //            .sorted(Comparator.comparing(HmeEoJobDataRecordVO::getIsSupplement)
                //                    //??????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.9.12
                //                    //.thenComparing(HmeEoJobDataRecordVO::getResultType)
                //                    .thenComparing(HmeEoJobDataRecordVO::getSerialNumber)).collect(Collectors.toList()));
                }

            }
            // ??????
            if (currentJobSnVO.getShiftId() != null) {
                MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, currentJobSnVO.getShiftId());
                resultVO.setShiftId(currentJobSnVO.getShiftId());
                resultVO.setShiftCode(mtWkcShift.getShiftCode());
            }
            // ??????
            if (currentJobSnVO.getSiteOutDate() != null) {
                String meterTimeStr = DateUtil.getDistanceTime(currentJobSnVO.getSiteInDate(),
                        currentJobSnVO.getSiteOutDate());
                resultVO.setMeterTimeStr(meterTimeStr);
            }

            // ???????????????
            resultVO.setOperationId(currentJobSnVO.getOperationId());
            resultVO.setEoStepId(currentJobSnVO.getEoStepId());
            resultVO.setEoStepNum(currentJobSnVO.getEoStepNum());
            resultVO.setReworkFlag(currentJobSnVO.getReworkFlag());
            resultVO.setJobId(currentJobSnVO.getJobId());
            resultVO.setEoId(currentJobSnVO.getEoId());
            resultVO.setSiteInBy(currentJobSnVO.getSiteInBy());
            // ?????????
            String userRealName = userClient.userInfoGet(tenantId, currentJobSnVO.getSiteInBy()).getRealName();
            resultVO.setSiteInByName(userRealName);
            resultVO.setSiteInDate(currentJobSnVO.getSiteInDate());
            resultVO.setSiteOutDate(currentJobSnVO.getSiteOutDate());
            resultVO.setSourceContainerId(currentJobSnVO.getSourceContainerId());
        }

        // ??????????????????
        resultVO.setWorkcellId(dto.getWorkcellId());
        resultVO.setMaterialLotId(dto.getMaterialLotId());
        resultVO.setSnNum(dto.getSnNum());

        //V20201230 modify by penglin.sui ??????BOM??????
        List<String> eoIdList = new ArrayList<>(1);
        eoIdList.add(currentJobSnVO.getEoId());
        List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId,eoIdList);
        if(CollectionUtils.isNotEmpty(hmeEoBomVOList)){
            resultVO.setBomId(hmeEoBomVOList.get(0).getBomId());
            resultVO.setBomName(hmeEoBomVOList.get(0).getBomName());
        }

        resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
        resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
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

        //?????????????????? add by yuchao.wang for can.wang at 2020.9.22
        if (StringUtils.isNotBlank(resultVO.getWorkOrderId())) {
            MtWorkOrder workOrder = mtWorkOrderRepository.woPropertyGet(tenantId, resultVO.getWorkOrderId());
            if (!Objects.isNull(workOrder)) {
                resultVO.setProductionVersion(workOrder.getProductionVersion());
            }
        }

        return resultVO;
    }

    /**
     * @param tenantId ??????ID
     * @param eoId     eoId
     * @return boolean
     * @Description ????????????EO????????????????????? ???????????????
     * @author yuchao.wang
     * @date 2020/9/28 14:47
     */
    private boolean isAfterSalesWorkOrder(Long tenantId, String eoId) {
        String workOrderType = hmeEoJobSnMapper.queryWorkOrderTypeByEoId(tenantId, eoId);

        if (StringUtils.isNotBlank(workOrderType)) {
            //????????????????????????LOV
            List<LovValueDTO> woTypeLovs = lovAdapter.queryLovValue("HME.NOT_CHECK_JUMP_WOTYPE", tenantId);
            if (CollectionUtils.isNotEmpty(woTypeLovs)) {
                for (LovValueDTO lov : woTypeLovs) {
                    if (workOrderType.equals(lov.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * ??????????????????-??????????????????
     *
     * @param tenantId ??????ID
     * @param qty      ????????????
     * @param dto      ????????????????????????
     */
    public void eoWorking(Long tenantId, BigDecimal qty, HmeEoJobSnVO3 dto) {
        // ??????????????????????????????
        MtEoRouterActualVO2 routerActualParam = new MtEoRouterActualVO2();
        routerActualParam.setEoId(dto.getEoId());
        List<MtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                mtEoRouterActualMapper.eoOperationLimitCurrentRouterStepGet(tenantId, routerActualParam);
        String eoStepActualId;
        if (CollectionUtils.isEmpty(eoOperationLimitCurrentRouterList)) {
            throw new MtException("HME_EO_JOB_SN_039",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
        } else {
            // ????????????????????????????????????
            if (eoOperationLimitCurrentRouterList.size() > 1) {
                eoStepActualId = eoOperationLimitCurrentRouterList.stream()
                        .max(Comparator.comparing(MtEoRouterActualVO5::getLastUpdateDate)).get()
                        .getEoStepActualId();
            } else {
                eoStepActualId = eoOperationLimitCurrentRouterList.get(0).getEoStepActualId();
            }
        }
        log.debug("???????????????????????????=" + eoStepActualId);

        // ??????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_IN");
        // ????????????
        MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
        mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO4.setQueueQty(new Double(String.valueOf(qty)));
        mtEoStepWipVO4.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);
        // ????????????
        MtEoRouterActualVO19 workingParam = new MtEoRouterActualVO19();
        workingParam.setEventRequestId(eventRequestId);
        workingParam.setEoStepActualId(eoStepActualId);
        workingParam.setWorkcellId(dto.getWorkcellId());
        workingParam.setQty(new Double(String.valueOf(qty)));
        workingParam.setSourceStatus("QUEUE");
        workingParam.setLastWorkcellId(dto.getWorkcellId());
        mtEoRouterActualRepository.eoWorkingProcess(tenantId, workingParam);
    }

    /**
     * ??????????????????-??????????????????-
     */
    public void eoWorking(Long tenantId, BigDecimal qty, HmeEoJobSnVO3 dto, String eventRequestId) {
        // ??????????????????????????????
        MtEoRouterActualVO2 routerActualParam = new MtEoRouterActualVO2();
        routerActualParam.setEoId(dto.getEoId());
        List<MtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                mtEoRouterActualMapper.eoOperationLimitCurrentRouterStepGet(tenantId, routerActualParam);
        String eoStepActualId;
        if (CollectionUtils.isEmpty(eoOperationLimitCurrentRouterList)) {
            throw new MtException("HME_EO_JOB_SN_039",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
        } else {
            // ????????????????????????????????????
            if (eoOperationLimitCurrentRouterList.size() > 1) {
                eoStepActualId = eoOperationLimitCurrentRouterList.stream()
                        .max(Comparator.comparing(MtEoRouterActualVO5::getLastUpdateDate)).get()
                        .getEoStepActualId();
            } else {
                eoStepActualId = eoOperationLimitCurrentRouterList.get(0).getEoStepActualId();
            }
        }
        log.debug("???????????????????????????=" + eoStepActualId);

        // // ??????????????????
        // String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId,
        // "EO_ROUTE_STEP_IN");

        // ????????????
        MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
        mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO4.setQueueQty(new Double(String.valueOf(qty)));
        mtEoStepWipVO4.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);

        // ????????????
        MtEoRouterActualVO19 workingParam = new MtEoRouterActualVO19();
        workingParam.setEventRequestId(eventRequestId);
        workingParam.setEoStepActualId(eoStepActualId);
        workingParam.setWorkcellId(dto.getWorkcellId());
        workingParam.setQty(new Double(String.valueOf(qty)));
        workingParam.setSourceStatus("QUEUE");
        workingParam.setLastWorkcellId(dto.getWorkcellId());
        // mtEoRouterActualRepository.eoWorkingProcess(tenantId, workingParam,mtEoRouterActual);
        mtEoRouterActualRepository.eoWkcAndStepWorking(tenantId, workingParam);
    }

    /**
     * ??????????????????-??????????????????-??????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoBatchWorking(Long tenantId, HmeEoJobSnVO14 dto, String eventRequestId) {
        // ??????????????????????????????
        HmeMtEoRouterActualVO2 routerActualParam = new HmeMtEoRouterActualVO2();
        List<String> eoIdList2 = dto.getHmeEoJobSnVO15List().stream().map(HmeEoJobSnVO15::getEoId).distinct().collect(Collectors.toList());
        routerActualParam.setEoIdList(eoIdList2);
        List<HmeMtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                hmeMtEoRouterActualMapper.eoBatchOperationLimitCurrentRouterStepGet(tenantId, routerActualParam);
        String eoStepActualId;
        Map<String, Double> eoStepActualIdMap = new HashMap<>();
        if (CollectionUtils.isEmpty(eoOperationLimitCurrentRouterList) || eoOperationLimitCurrentRouterList.size() < eoIdList2.size()) {
            //???????????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_039",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
        } else {
            //?????????????????????eoId
            List<String> eoIdList = eoOperationLimitCurrentRouterList.stream().map(HmeMtEoRouterActualVO5::getEoId).distinct().collect(Collectors.toList());
            if (eoIdList.size() != eoIdList2.size()) {
                //???????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_039",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
            }
            List<HmeMtEoRouterActualVO5> singleEoOperationLimitCurrentRouterList = new ArrayList<>();
            for (String eoId : eoIdList
            ) {
                singleEoOperationLimitCurrentRouterList = eoOperationLimitCurrentRouterList.stream()
                        .filter(t -> eoId.equals(t.getEoId())).collect(Collectors.toList());
                // ????????????????????????????????????
                if (singleEoOperationLimitCurrentRouterList.size() > 1) {
                    eoStepActualId = singleEoOperationLimitCurrentRouterList.stream()
                            .max(Comparator.comparing(HmeMtEoRouterActualVO5::getLastUpdateDate)).get()
                            .getEoStepActualId();
                } else {
                    eoStepActualId = singleEoOperationLimitCurrentRouterList.get(0).getEoStepActualId();
                }
                HmeEoJobSnVO15 hmeEoJobSnVO15 = dto.getHmeEoJobSnVO15List().stream().filter(x -> eoId.equals(x.getEoId())).collect(Collectors.toList()).get(0);
                eoStepActualIdMap.put(eoStepActualId, hmeEoJobSnVO15.getEoQty().doubleValue());
                log.debug("??????EO=" + eoId + "???????????????=" + eoStepActualId);
            }
        }

        // // ??????????????????
        // String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId,
        // "EO_ROUTE_STEP_IN");

        // ??????????????????
        List<MtEoStepWipVO16> mtEoStepWipVO16List = new ArrayList<>();
        MtEoStepWipVO17 mtEoStepWipVO17 = new MtEoStepWipVO17();
        eoStepActualIdMap.forEach((k, v) -> {
            MtEoStepWipVO16 mtEoStepWipVO16 = new MtEoStepWipVO16();
            mtEoStepWipVO16.setWorkcellId(dto.getWorkcellId());
            mtEoStepWipVO16.setEoStepActualId(k);
            mtEoStepWipVO16.setQueueQty(v);
            mtEoStepWipVO16List.add(mtEoStepWipVO16);
        });
        mtEoStepWipVO17.setEoStepWipList(mtEoStepWipVO16List);
        mtEoStepWipRepository.eoWkcBatchQueue(tenantId, mtEoStepWipVO17);

        // ??????????????????
        List<MtEoRouterActualVO53> eoRouterActualList = new ArrayList<>();
        MtEoRouterActualVO52 mtEoRouterActualVO52 = new MtEoRouterActualVO52();
        mtEoRouterActualVO52.setEventRequestId(eventRequestId);
        mtEoRouterActualVO52.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO52.setSourceStatus("QUEUE");
        eoStepActualIdMap.forEach((k, v) -> {
            MtEoRouterActualVO53 mtEoRouterActualVO53 = new MtEoRouterActualVO53();
            mtEoRouterActualVO53.setEoStepActualId(k);
            mtEoRouterActualVO53.setQty(v);
            eoRouterActualList.add(mtEoRouterActualVO53);
        });
        mtEoRouterActualVO52.setEoRouterActualList(eoRouterActualList);
        mtEoRouterActualRepository.eoWkcAndStepBatchWorking(tenantId, mtEoRouterActualVO52);
    }

    /**
     * @param tenantId  ??????
     * @param userId    ??????ID
     * @param jobIdList jobIdList
     * @return void
     * @Description ??????JobId????????????
     * @author yuchao.wang
     * @date 2020/11/5 19:43
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchOutSite(Long tenantId, Long userId, List<String> jobIdList) {
        if (CollectionUtils.isNotEmpty(jobIdList)) {
            List<List<String>> splitIdList = InterfaceUtils.splitSqlList(jobIdList, SQL_ITEM_COUNT_LIMIT);
            for (List<String> ids : splitIdList) {
                hmeEoJobSnMapper.batchOutSite(tenantId, userId, ids);
            }
        }
    }

    /**
     *
     * @param tenantId  ??????
     * @param userId    ??????ID
     * @param jobIdList jobIdList
     * @param remark    ??????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchOutSite2(Long tenantId, Long userId, List<String> jobIdList, String remark) {
        if (CollectionUtils.isNotEmpty(jobIdList)) {
            List<List<String>> splitIdList = InterfaceUtils.splitSqlList(jobIdList, SQL_ITEM_COUNT_LIMIT);
            for (List<String> ids : splitIdList) {
                hmeEoJobSnMapper.batchOutSite2(tenantId, userId, ids, remark);
            }
        }
    }

    /**
     *
     * @Description ????????????????????????
     *
     * @author yuchao.wang
     * @date 2020/11/17 10:10
     * @param tenantId ??????ID
     * @param dto ??????
     * @param pageRequest ????????????
     * @return com.ruike.hme.domain.vo.HmeEoJobTimeSnVO4
     * @Description ????????????????????????
     * @author yuchao.wang
     * @date 2020/11/17 10:10
     */
    @Override
    public HmeEoJobTimeSnVO4 queryPageTimeSnByWorkcell(Long tenantId, HmeEoJobSnDTO dto, PageRequest pageRequest) {
        //????????????
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????ID"));
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME", "??????ID"));
        }

        List<HmeEoJobTimeSnVO3> lineList = new ArrayList<>();

        //????????????SQL
        Sqls containerSql = Sqls.custom().andEqualTo(HmeEoJobContainer.FIELD_TENANT_ID, tenantId)
                .andEqualTo(HmeEoJobContainer.FIELD_WORKCELL_ID, dto.getWorkcellId())
                .andIsNull(HmeEoJobContainer.FIELD_SITE_OUT_DATE);
        //??????????????????????????? ??????????????????
        if (StringUtils.isNotBlank(dto.getBarcode())) {
            containerSql = containerSql.andEqualTo(HmeEoJobContainer.FIELD_CONTAINER_CODE, dto.getBarcode());
        }

        SecurityTokenHelper.close();
        List<HmeEoJobContainer> hmeEoJobContainerList =
                hmeEoJobContainerRepository.selectByCondition(Condition.builder(HmeEoJobContainer.class)
                        .andWhere(containerSql)
                        .orderByAsc(HmeEoJobContainer.FIELD_SITE_IN_DATE).build());

        //??????????????????SQL
        Sqls eoJobSnSql = Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.TIME_PROCESS)
                .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE);
        if (StringUtils.isNotBlank(dto.getBarcode())) {
            //???????????????????????????????????????????????????jobContainerId?????????????????????ID
            if (CollectionUtils.isNotEmpty(hmeEoJobContainerList)) {
                List<String> jobContainerIdList = hmeEoJobContainerList.stream()
                        .map(HmeEoJobContainer::getJobContainerId).collect(Collectors.toList());
                eoJobSnSql = eoJobSnSql.andIn(HmeEoJobSn.FIELD_JOB_CONTAINER_ID, jobContainerIdList);
            } else {
                //??????????????????ID?????????????????????
                List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
                        .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, dto.getBarcode())).build());

                String materialLotId = (CollectionUtils.isNotEmpty(mtMaterialLots) && Objects.nonNull(mtMaterialLots.get(0))) ? mtMaterialLots.get(0).getMaterialLotId() : "";
                eoJobSnSql = eoJobSnSql.andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, StringUtils.trimToEmpty(materialLotId));
            }
        }
        SecurityTokenHelper.close();
        List<HmeEoJobSn> hmeEoJobSnList = this.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(eoJobSnSql)
                .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());

        if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
            return new HmeEoJobTimeSnVO4(new ArrayList<HmeEoJobTimeSnVO3>(), pageRequest, 0, 0, 0);
        }

        //????????????????????????/EO/??????
        Map<String, String> materialLotCodeMap = new HashMap<>();
        Map<String, String> reworkFlagMap = new HashMap<>();
        Map<String, MtEo> eoMap = new HashMap<>();
        Map<String, MtMaterial> materialMap = new HashMap<>();
        List<String> materialLotIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getMaterialLotId).distinct().collect(Collectors.toList());
        List<String> eoIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getEoId).distinct().collect(Collectors.toList());
        List<String> materialIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getSnMaterialId).distinct().collect(Collectors.toList());

        List<HmeMaterialLotVO3> materialLotList = hmeEoJobSnCommonService.batchQueryMaterialLotReworkFlagForTime(tenantId, materialLotIdList);
        materialLotList.forEach(item -> {
            materialLotCodeMap.put(item.getMaterialLotId(), item.getMaterialLotCode());
            reworkFlagMap.put(item.getMaterialLotId(), (YES.equals(item.getReworkFlag()) ? YES : NO));
        });
        SecurityTokenHelper.close();
        List<MtEo> eoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class)
                .andWhere(Sqls.custom().andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                        .andIn(MtEo.FIELD_EO_ID, eoIdList)).build());
        eoList.forEach(item -> eoMap.put(item.getEoId(), item));
        SecurityTokenHelper.close();
        List<MtMaterial> materialList = mtMaterialRepository.selectByCondition(Condition.builder(MtMaterial.class)
                .andWhere(Sqls.custom().andEqualTo(MtMaterial.FIELD_TENANT_ID, tenantId)
                        .andIn(MtMaterial.FIELD_MATERIAL_ID, materialIdList)).build());
        materialList.forEach(item -> materialMap.put(item.getMaterialId(), item));

        // ?????????????????????
        hmeEoJobContainerList.forEach(jobContainer -> {
            String eoId = "";
            String workOrderId = "";
            String materialId = "";
            String materialLotId = "";
            String materialLotCode = "";
            HmeEoJobTimeSnVO3 containerVO = new HmeEoJobTimeSnVO3();
            containerVO.setSnType("CONTAINER");
            containerVO.setMaterialLotCode(jobContainer.getContainerCode());
            containerVO.setSiteInDate(jobContainer.getSiteInDate());
            containerVO.setSiteInBy(jobContainer.getSiteInBy());
            containerVO.setSiteInByName(userClient.userInfoGet(tenantId, jobContainer.getSiteInBy()).getRealName());
            containerVO.setSumEoQty(new BigDecimal(0));
            containerVO.setJobContainerId(jobContainer.getJobContainerId());
            containerVO.setReworkFlag(NO);

            //???????????????????????????
            List<HmeEoJobSn> eoJobSnInContainerList = hmeEoJobSnList.stream()
                            .filter(hmeEoJobSn -> Objects.nonNull(hmeEoJobSn.getJobContainerId())
                                    && hmeEoJobSn.getJobContainerId().equals(jobContainer.getJobContainerId()))
                            .collect(Collectors.toList());

            //??????????????????????????????
            if (CollectionUtils.isNotEmpty(eoJobSnInContainerList)) {
                for (HmeEoJobSn newJobSn : eoJobSnInContainerList) {
                    MtEo mtEo = eoMap.getOrDefault(newJobSn.getEoId(), new MtEo());
                    MtMaterial mtMaterial = materialMap.getOrDefault(newJobSn.getSnMaterialId(), new MtMaterial());

                    containerVO.setSumEoQty(containerVO.getSumEoQty().add(BigDecimal.valueOf(mtEo.getQty())));
                    if (YES.equals(reworkFlagMap.get(newJobSn.getMaterialLotId()))) {
                        containerVO.setReworkFlag(YES);
                    }
                    if (StringUtils.isNotBlank(containerVO.getMaterialCode())
                            && !containerVO.getMaterialCode().equals(mtMaterial.getMaterialCode())) {
                        // ??????SN????????????...
                        containerVO.setMaterialCode("...");
                        containerVO.setMaterialName("...");
                    } else {
                        containerVO.setMaterialCode(mtMaterial.getMaterialCode());
                        containerVO.setMaterialName(mtMaterial.getMaterialName());
                    }
                    eoId = newJobSn.getEoId();
                    workOrderId = mtEo.getWorkOrderId();
                    materialId = mtMaterial.getMaterialId();
                    materialLotId = newJobSn.getMaterialLotId();
                    materialLotCode = materialLotCodeMap.get(newJobSn.getMaterialLotId());
                }

                //V20200825 modify by penglin.sui for fang.pan ????????????????????????
                HmeEoJobSnVO3 hmeEoJobSnVO3Para = new HmeEoJobSnVO3();
                hmeEoJobSnVO3Para.setWorkcellId(jobContainer.getWorkcellId());
                hmeEoJobSnVO3Para.setEoId(eoId);
                hmeEoJobSnVO3Para.setOperationId(dto.getOperationId());
                hmeEoJobSnVO3Para.setWorkOrderId(workOrderId);
                hmeEoJobSnVO3Para.setMaterialId(materialId);
                hmeEoJobSnVO3Para.setMaterialLotId(materialLotId);
                hmeEoJobSnVO3Para.setSnNum(materialLotCode);
                BigDecimal standardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);
                if (Objects.nonNull(standardReqdTimeInProcess)) {
                    containerVO.setStandardReqdTimeInProcess(standardReqdTimeInProcess);
                }
                lineList.add(containerVO);
            }
        });

        // ?????????????????????????????????
        List<HmeEoJobSn> materialLotEoJobSnList = hmeEoJobSnList.stream()
                .filter(hmeEoJobSn -> Objects.isNull(hmeEoJobSn.getJobContainerId())).collect(Collectors.toList());

        //?????????????????????
        materialLotEoJobSnList.forEach(jobSn -> {
            MtEo mtEo = eoMap.getOrDefault(jobSn.getEoId(), new MtEo());
            MtMaterial mtMaterial = materialMap.getOrDefault(jobSn.getSnMaterialId(),new MtMaterial());

            HmeEoJobTimeSnVO3 jobSnVO = new HmeEoJobTimeSnVO3();
            jobSnVO.setSnType("MATERIAL_LOT");
            jobSnVO.setMaterialLotId(jobSn.getMaterialLotId());
            jobSnVO.setMaterialLotCode(materialLotCodeMap.get(jobSn.getMaterialLotId()));
            jobSnVO.setSiteInDate(jobSn.getSiteInDate());
            jobSnVO.setSiteInBy(jobSn.getSiteInBy());
            jobSnVO.setSiteInByName(userClient.userInfoGet(tenantId, jobSn.getSiteInBy()).getRealName());
            jobSnVO.setJobId(jobSn.getJobId());
            jobSnVO.setJobContainerId(jobSn.getJobContainerId());
            jobSnVO.setSumEoQty(BigDecimal.valueOf(mtEo.getQty()));
            jobSnVO.setMaterialCode(mtMaterial.getMaterialCode());
            jobSnVO.setMaterialName(mtMaterial.getMaterialName());
            jobSnVO.setReworkFlag(reworkFlagMap.get(jobSn.getMaterialLotId()));

            //V20200825 modify by penglin.sui for fang.pan ????????????????????????
            HmeEoJobSnVO3 hmeEoJobSnVO3Para = new HmeEoJobSnVO3();
            hmeEoJobSnVO3Para.setWorkcellId(jobSn.getWorkcellId());
            hmeEoJobSnVO3Para.setEoId(jobSn.getEoId());
            hmeEoJobSnVO3Para.setOperationId(jobSn.getOperationId());
            hmeEoJobSnVO3Para.setWorkOrderId(mtEo.getWorkOrderId());
            hmeEoJobSnVO3Para.setMaterialId(mtMaterial.getMaterialId());
            hmeEoJobSnVO3Para.setMaterialLotId(jobSn.getMaterialLotId());
            hmeEoJobSnVO3Para.setSnNum(materialLotCodeMap.get(jobSn.getMaterialLotId()));
            BigDecimal standardReqdTimeInProcess = hmeOperationTimeObjectRepository.StandardReqdTimeInProcessGet(tenantId, hmeEoJobSnVO3Para);
            if (Objects.nonNull(standardReqdTimeInProcess)) {
                jobSnVO.setStandardReqdTimeInProcess(standardReqdTimeInProcess);
            }
            lineList.add(jobSnVO);
        });

        //?????????????????????
        if (CollectionUtils.isEmpty(lineList)) {
            return new HmeEoJobTimeSnVO4(new ArrayList<HmeEoJobTimeSnVO3>(), pageRequest, 0, 0, 0);
        }
        List<HmeEoJobTimeSnVO3> filterLineList = lineList;
        // ???????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getMaterialName())) {
            filterLineList = lineList.stream().filter(line -> StringUtils.contains(line.getMaterialCode(), dto.getMaterialName()) || StringUtils.contains(line.getMaterialName(), dto.getMaterialName())).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(filterLineList)) {
            return new HmeEoJobTimeSnVO4(new ArrayList<HmeEoJobTimeSnVO3>(), pageRequest, 0, 0, 0);
        }
        //?????????snType????????????????????????????????????????????????????????????????????????
        List<HmeEoJobTimeSnVO3> pageList = filterLineList.stream().sorted(Comparator
                .comparing(HmeEoJobTimeSnVO3::getSnType).thenComparing(HmeEoJobTimeSnVO3::getSiteInDate))
                .collect(Collectors.toList());

        //????????????????????????????????????
        int fromIndex = pageRequest.getPage() * pageRequest.getSize();
        int toIndex = Math.min(fromIndex + pageRequest.getSize(), filterLineList.size());
        return new HmeEoJobTimeSnVO4(pageList.subList(fromIndex, toIndex), pageRequest, filterLineList.size(),
                hmeEoJobContainerList.size(), hmeEoJobSnList.size());
    }

    /**
     *
     * @Description ??????JobId????????????
     *
     * @author yuchao.wang
     * @date 2020/11/19 1:22
     * @param tenantId ??????
     * @param userId ??????ID
     * @param snLineList ????????????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchOutSiteWithMaterialLot(Long tenantId, Long userId, List<HmeEoJobSnVO3> snLineList) {
        hmeEoJobSnMapper.batchOutSiteWithMaterialLot(tenantId, userId, snLineList);
    }

    /**
     * ??????????????????
     *
     * @param tenantId ??????ID
     * @param dto      ????????????
     * @return ????????????
     */
    @Override
    public HmeEoJobSnVO2 prepareInSite(Long tenantId, HmeEoJobSnVO2 dto) {
        List<HmeEoJobMaterialVO> materialVOList = new ArrayList<>();
        List<HmeEoJobLotMaterialVO> lotMaterialVOList = new ArrayList<>();
        List<HmeEoJobTimeMaterialVO> timeMaterialVOList = new ArrayList<>();

        MtMaterial snMaterial = mtMaterialRepository.materialPropertyGet(tenantId, dto.getSnMaterialId());
        if (Objects.nonNull(snMaterial)) {
            List<HmePrepareMaterialVO> prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery(tenantId,
                    dto.getSiteId(), dto.getWorkOrderId(), snMaterial.getMaterialCode());
            if (CollectionUtils.isNotEmpty(prepareMaterialVOList)) {
                for (HmePrepareMaterialVO materialVO : prepareMaterialVOList) {

                    //V20201001 modify by penglin.sui for lu.bai ??????????????????
                    //???????????????????????????
                    String backFlushMaterialFlag = hmeEoJobSnLotMaterialMapper.selectIsBackFlashMaterial(tenantId, dto.getSiteId(), materialVO.getBomComponentId());
                    if (YES.equals(backFlushMaterialFlag)) {
                        continue;
                    }

                    hmeEoJobMaterialRepository.initJobMaterial(tenantId,
                            materialVO.getMaterialId(), true, materialVO.getComponentQty(), null, dto);
                }
            }

            List<HmePrepareMaterialVO> jobLotMaterialList = hmeEoJobSnMapper.prepareEoJobLotMaterialQuery(tenantId,
                    dto.getSiteId(), dto.getWorkOrderId(), snMaterial.getMaterialCode());
            // SN????????????????????????
            if (CollectionUtils.isNotEmpty(jobLotMaterialList)) {
                for (HmePrepareMaterialVO materialVO : jobLotMaterialList) {

                    //V20201001 modify by penglin.sui for lu.bai ??????????????????
                    //???????????????????????????
                    String backFlushMaterialFlag = hmeEoJobSnLotMaterialMapper.selectIsBackFlashMaterial(tenantId, dto.getSiteId(), materialVO.getBomComponentId());
                    if (YES.equals(backFlushMaterialFlag)) {
                        continue;
                    }

                    hmeEoJobLotMaterialRepository.initLotMaterialList(
                            tenantId, materialVO.getMaterialId(), materialVO.getComponentQty(), dto);
                }
            }

            List<HmePrepareMaterialVO> jobTimeMaterialList = hmeEoJobSnMapper.prepareEoJobTimeMaterialQuery(tenantId,
                    dto.getSiteId(), dto.getWorkOrderId(), snMaterial.getMaterialCode());
            if (CollectionUtils.isNotEmpty(jobTimeMaterialList)) {
                for (HmePrepareMaterialVO materialVO : jobTimeMaterialList) {
                    String availableTime = new BigDecimal(materialVO.getAvailableTime())
                            .multiply(BigDecimal.valueOf(60)).toString();

                    //V20200915 modify by penglin.sui for lu.bai ??????????????????????????????
                    if (StringUtils.isBlank(availableTime)) {
                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialVO.getMaterialId());
                        if (Objects.isNull(mtMaterial)) {
                            //${1}????????? ?????????${2}
                            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0037", "GENERAL", "??????", materialVO.getMaterialId()));
                        }
                        //???????????????${1}??????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_070", "HME", mtMaterial.getMaterialCode()));
                    }

                    //V20201001 modify by penglin.sui for lu.bai ??????????????????
                    //???????????????????????????
                    String backFlushMaterialFlag = hmeEoJobSnLotMaterialMapper.selectIsBackFlashMaterial(tenantId, dto.getSiteId(), materialVO.getBomComponentId());
                    if (YES.equals(backFlushMaterialFlag)) {
                        continue;
                    }

                    hmeEoJobTimeMaterialRepository.initTimeMaterialList(tenantId, materialVO.getMaterialId(),
                            availableTime, materialVO.getComponentQty(), dto);
                }
            }

            //??????????????????????????? add by yuchao.wang for lu.bai at 2020.9.23
            HmeEoJobMaterialVO jobLotCondition = new HmeEoJobMaterialVO();
            jobLotCondition.setWorkcellId(dto.getWorkcellId());
            jobLotCondition.setJobId(dto.getJobId());
            jobLotCondition.setOperationId(dto.getOperationId());
            jobLotCondition.setJobType(dto.getJobType());
            jobLotCondition.setWorkOrderId(dto.getWorkOrderId());
            jobLotCondition.setMaterialId(dto.getMaterialId());
            jobLotCondition.setSiteId(dto.getSiteId());
            jobLotCondition.setPrepareQty(dto.getPrepareQty());
            jobLotCondition.setReworkFlag(dto.getReworkFlag());
            lotMaterialVOList = hmeEoJobLotMaterialRepository.matchedJobLotMaterialQuery(tenantId, jobLotCondition, null);

            HmeEoJobMaterialVO jobTimeCondition = new HmeEoJobMaterialVO();
            jobTimeCondition.setWorkcellId(dto.getWorkcellId());
            jobTimeCondition.setJobId(dto.getJobId());
            jobTimeCondition.setOperationId(dto.getOperationId());
            jobTimeCondition.setJobType(dto.getJobType());
            jobTimeCondition.setWorkOrderId(dto.getWorkOrderId());
            jobTimeCondition.setMaterialId(dto.getMaterialId());
            jobTimeCondition.setSiteId(dto.getSiteId());
            jobTimeCondition.setPrepareQty(dto.getPrepareQty());
            jobTimeCondition.setReworkFlag(dto.getReworkFlag());
            timeMaterialVOList = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, jobTimeCondition, null);

            // ??????????????????????????????
            HmeEoJobMaterialVO2 jobMaterialCondition = new HmeEoJobMaterialVO2();
            jobMaterialCondition.setJobId(dto.getJobId());
            jobMaterialCondition.setWorkcellId(dto.getWorkcellId());
            jobMaterialCondition.setMaterialId(dto.getMaterialId());
            jobMaterialCondition.setSiteId(dto.getSiteId());
            jobMaterialCondition.setWorkOrderId(dto.getWorkOrderId());
            jobMaterialCondition.setJobType(dto.getJobType());
            jobMaterialCondition.setOperationId(dto.getOperationId());
            materialVOList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId,
                    jobMaterialCondition);

            //V20200828 modify by penglin.sui for tianyang.xie ??????????????????????????????????????????????????????
            if (CollectionUtils.isNotEmpty(materialVOList)) {
                dto.setMaterialVOList(materialVOList.stream()
                        .sorted(Comparator.comparing(HmeEoJobMaterialVO::getLineNumber)).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(lotMaterialVOList)) {
                dto.setLotMaterialVOList(lotMaterialVOList.stream()
                        .sorted(Comparator.comparing(HmeEoJobLotMaterialVO::getLineNumber)).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(timeMaterialVOList)) {
                dto.setTimeMaterialVOList(timeMaterialVOList.stream()
                        .sorted(Comparator.comparing(HmeEoJobTimeMaterialVO::getLineNumber)).collect(Collectors.toList()));
            }
        }

        // ???????????????????????????
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = hmeEoJobDataRecordRepository.inSiteScan(tenantId, dto);
        dto.setDataRecordVOList(hmeEoJobDataRecordVOList);

        return dto;
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ????????????
     * @author penglin.sui
     * @date 2020/10/4 16:08
     */
    public HmeEoJobSnVO2 prepareInSiteOfRefresh(Long tenantId, HmeEoJobSnVO2 dto) {
        List<HmeEoJobMaterialVO> materialVOList = new ArrayList<>();
        List<HmeEoJobLotMaterialVO> lotMaterialVOList = new ArrayList<>();
        List<HmeEoJobTimeMaterialVO> timeMaterialVOList = new ArrayList<>();

        MtMaterial snMaterial = mtMaterialRepository.materialPropertyGet(tenantId, dto.getSnMaterialId());
        if (Objects.nonNull(snMaterial)) {
            int count = 0;
            List<HmePrepareMaterialVO> prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery(tenantId,
                    dto.getSiteId(), dto.getWorkOrderId(), snMaterial.getMaterialCode());
            if (CollectionUtils.isNotEmpty(prepareMaterialVOList)) {
                for (HmePrepareMaterialVO materialVO : prepareMaterialVOList) {

                    //V20201001 modify by penglin.sui for lu.bai ??????????????????
                    //???????????????????????????
                    String backFlushMaterialFlag = hmeEoJobSnLotMaterialMapper.selectIsBackFlashMaterial(tenantId, dto.getSiteId(), materialVO.getBomComponentId());
                    if (YES.equals(backFlushMaterialFlag)) {
                        continue;
                    }

                    //?????? ?????? + ?????? ?????????????????????????????????
                    count = hmeEoJobMaterialRepository.selectCountByCondition(Condition.builder(HmeEoJobMaterial.class)
                            .andWhere(Sqls.custom().andEqualTo(HmeEoJobMaterial.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(HmeEoJobMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                    .andEqualTo(HmeEoJobMaterial.FIELD_MATERIAL_ID, materialVO.getMaterialId())).build());
                    if (count > 0) {
                        continue;
                    }

                    hmeEoJobMaterialRepository.initJobMaterial(tenantId,
                            materialVO.getMaterialId(), true, materialVO.getComponentQty(), null, dto);
                }
            }

            List<HmePrepareMaterialVO> jobLotMaterialList = hmeEoJobSnMapper.prepareEoJobLotMaterialQuery(tenantId,
                    dto.getSiteId(), dto.getWorkOrderId(), snMaterial.getMaterialCode());
            // SN????????????????????????
            if (CollectionUtils.isNotEmpty(jobLotMaterialList)) {
                for (HmePrepareMaterialVO materialVO : jobLotMaterialList) {

                    //V20201001 modify by penglin.sui for lu.bai ??????????????????
                    //???????????????????????????
                    String backFlushMaterialFlag = hmeEoJobSnLotMaterialMapper.selectIsBackFlashMaterial(tenantId, dto.getSiteId(), materialVO.getBomComponentId());
                    if (YES.equals(backFlushMaterialFlag)) {
                        continue;
                    }

                    //?????? ?????? + ?????? ?????????????????????????????????
                    count = hmeEoJobLotMaterialRepository.selectCountByCondition(Condition.builder(HmeEoJobLotMaterial.class)
                            .andWhere(Sqls.custom().andEqualTo(HmeEoJobLotMaterial.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_MATERIAL_ID, materialVO.getMaterialId())).build());
                    if (count > 0) {
                        continue;
                    }

                    hmeEoJobLotMaterialRepository.initLotMaterialList(
                            tenantId, materialVO.getMaterialId(), materialVO.getComponentQty(), dto);
                }
            }

            List<HmePrepareMaterialVO> jobTimeMaterialList = hmeEoJobSnMapper.prepareEoJobTimeMaterialQuery(tenantId,
                    dto.getSiteId(), dto.getWorkOrderId(), snMaterial.getMaterialCode());
            if (CollectionUtils.isNotEmpty(jobTimeMaterialList)) {
                for (HmePrepareMaterialVO materialVO : jobTimeMaterialList) {

                    //?????? ?????? + ?????? ?????????????????????????????????
                    count = hmeEoJobTimeMaterialRepository.selectCountByCondition(Condition.builder(HmeEoJobTimeMaterial.class)
                            .andWhere(Sqls.custom().andEqualTo(HmeEoJobTimeMaterial.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(HmeEoJobTimeMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                    .andEqualTo(HmeEoJobTimeMaterial.FIELD_MATERIAL_ID, materialVO.getMaterialId())).build());
                    if (count > 0) {
                        continue;
                    }

                    String availableTime = new BigDecimal(materialVO.getAvailableTime())
                            .multiply(BigDecimal.valueOf(60)).toString();

                    //V20200915 modify by penglin.sui for lu.bai ??????????????????????????????
                    if (StringUtils.isBlank(availableTime)) {
                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialVO.getMaterialId());
                        if (Objects.isNull(mtMaterial)) {
                            //${1}????????? ?????????${2}
                            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0037", "GENERAL", "??????", materialVO.getMaterialId()));
                        }
                        //???????????????${1}??????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_070", "HME", mtMaterial.getMaterialCode()));
                    }

                    //V20201001 modify by penglin.sui for lu.bai ??????????????????
                    //???????????????????????????
                    String backFlushMaterialFlag = hmeEoJobSnLotMaterialMapper.selectIsBackFlashMaterial(tenantId, dto.getSiteId(), materialVO.getBomComponentId());
                    if (YES.equals(backFlushMaterialFlag)) {
                        continue;
                    }

                    hmeEoJobTimeMaterialRepository.initTimeMaterialList(tenantId, materialVO.getMaterialId(),
                            availableTime, materialVO.getComponentQty(), dto);
                }
            }

            //??????????????????????????? add by yuchao.wang for lu.bai at 2020.9.23
            HmeEoJobMaterialVO jobLotCondition = new HmeEoJobMaterialVO();
            jobLotCondition.setWorkcellId(dto.getWorkcellId());
            jobLotCondition.setJobId(dto.getJobId());
            jobLotCondition.setOperationId(dto.getOperationId());
            jobLotCondition.setJobType(dto.getJobType());
            jobLotCondition.setWorkOrderId(dto.getWorkOrderId());
            jobLotCondition.setMaterialId(dto.getMaterialId());
            jobLotCondition.setSiteId(dto.getSiteId());
            jobLotCondition.setPrepareQty(dto.getPrepareQty());
            jobLotCondition.setReworkFlag(dto.getReworkFlag());
            lotMaterialVOList = hmeEoJobLotMaterialRepository.matchedJobLotMaterialQuery(tenantId, jobLotCondition, null);

            HmeEoJobMaterialVO jobTimeCondition = new HmeEoJobMaterialVO();
            jobTimeCondition.setWorkcellId(dto.getWorkcellId());
            jobTimeCondition.setJobId(dto.getJobId());
            jobTimeCondition.setOperationId(dto.getOperationId());
            jobTimeCondition.setJobType(dto.getJobType());
            jobTimeCondition.setWorkOrderId(dto.getWorkOrderId());
            jobTimeCondition.setMaterialId(dto.getMaterialId());
            jobTimeCondition.setSiteId(dto.getSiteId());
            jobTimeCondition.setPrepareQty(dto.getPrepareQty());
            jobTimeCondition.setReworkFlag(dto.getReworkFlag());
            timeMaterialVOList = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, jobTimeCondition, null);

            // ??????????????????????????????
            HmeEoJobMaterialVO2 jobMaterialCondition = new HmeEoJobMaterialVO2();
            jobMaterialCondition.setJobId(dto.getJobId());
            jobMaterialCondition.setWorkcellId(dto.getWorkcellId());
            jobMaterialCondition.setMaterialId(dto.getMaterialId());
            jobMaterialCondition.setSiteId(dto.getSiteId());
            jobMaterialCondition.setWorkOrderId(dto.getWorkOrderId());
            jobMaterialCondition.setJobType(dto.getJobType());
            jobMaterialCondition.setOperationId(dto.getOperationId());
            materialVOList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId,
                    jobMaterialCondition);

            //V20200828 modify by penglin.sui for tianyang.xie ??????????????????????????????????????????????????????
            if (CollectionUtils.isNotEmpty(materialVOList)) {
                dto.setMaterialVOList(materialVOList.stream()
                        .sorted(Comparator.comparing(HmeEoJobMaterialVO::getLineNumber)).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(lotMaterialVOList)) {
                dto.setLotMaterialVOList(lotMaterialVOList.stream()
                        .sorted(Comparator.comparing(HmeEoJobLotMaterialVO::getLineNumber)).collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(timeMaterialVOList)) {
                dto.setTimeMaterialVOList(timeMaterialVOList.stream()
                        .sorted(Comparator.comparing(HmeEoJobTimeMaterialVO::getLineNumber)).collect(Collectors.toList()));
            }
        }

        return dto;
    }

    /**
     * @param tenantId      ??????ID
     * @param materialLotId ?????????ID
     * @param operationId   ??????ID
     * @Description ??????material_lot_id+??????ID+'????????????????????????COS_ FETCH _IN'??????????????????????????????????????????????????????
     * @author yuchao.wang
     * @date 2020/8/18 11:19
     */
    @Override
    public boolean checkGettingChipFlag(Long tenantId, String materialLotId, String operationId) {
        Integer gettingChipFlag = hmeEoJobSnMapper.checkGettingChipFlag(tenantId, materialLotId, operationId);
        return !Objects.isNull(gettingChipFlag) && 1 == gettingChipFlag;
    }

    /**
     * @param tenantId ??????ID
     * @param jobId    ID
     * @return boolean
     * @Description ??????ID?????????????????? true:????????? false:?????????
     * @author yuchao.wang
     * @date 2020/8/31 18:32
     */
    @Override
    public boolean checkSiteOutById(Long tenantId, String jobId) {
        Integer gettingChipFlag = hmeEoJobSnMapper.checkSiteOutById(tenantId, jobId);
        return !Objects.isNull(gettingChipFlag) && 1 == gettingChipFlag;
    }

    /**
     * @param insertList ??????????????????
     * @return void
     * @Description ????????????
     * @author yuchao.wang
     * @date 2020/8/27 16:49
     */
    @Override
    public void myBatchInsert(List<HmeEoJobSn> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeEoJobSn>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);

            //V20210409 modify by penglin.sui for tianyang.xie ???????????????????????????????????????????????????????????????
            List<HmeEoJobSn> timeInsertList = insertList.stream().filter(item -> item.getJobType().equals(HmeConstants.JobType.TIME_PROCESS))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(timeInsertList)) {
                //??????????????????
                List<String> eoIdList = timeInsertList.stream().map(HmeEoJobSn::getEoId).collect(Collectors.toList());
                Integer count = hmeEoJobSnMapper.batchQueryHaveInSiteCount(timeInsertList.get(0).getTenantId(),eoIdList);
                if(count > 0){
                    //SN?????????????????????,??????????????????!
                    throw new MtException("HME_EO_JOB_SN_191", mtErrorMessageRepository.getErrorMessageWithModule(timeInsertList.get(0).getTenantId(),
                            "HME_EO_JOB_SN_191", "HME"));
                }
            }

            for (List<HmeEoJobSn> domains : splitSqlList) {
                hmeEoJobSnMapper.batchInsert(domains);
            }
        }
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkOrderVO>
     * @Description ??????????????????-???????????????LOV
     * @author yuchao.wang
     * @date 2020/9/1 13:54
     */
    @Override
    public List<HmeWorkOrderVO> workOrderQueryForFirst(Long tenantId, HmeWoLovQueryDTO dto) {
        return hmeEoJobSnMapper.workOrderQueryForFirst(tenantId, dto);
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     * @Description ??????????????????-EO??????LOV
     * @author yuchao.wang
     * @date 2020/9/1 16:30
     */
    @Override
    public List<HmeEoVO> eoQueryForFirst(Long tenantId, HmeEoLovQueryDTO dto) {
        return hmeEoJobSnMapper.eoQueryForFirst(tenantId, dto);
    }

    /**
     * @param tenantId       ??????ID
     * @param materialLotIds ?????????ID??????
     * @param dto            ??????
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMaterialLotVO2>
     * @Description ??????????????????-???????????????LOV
     * @author yuchao.wang
     * @date 2020/9/1 19:51
     */
    @Override
    public List<HmeMaterialLotVO2> materialLotLovQueryForFirst(Long tenantId, List<String> materialLotIds, HmeMaterialLotLovQueryDTO dto) {
        return hmeEoJobSnMapper.materialLotLovQueryForFirst(tenantId, materialLotIds, dto);
    }

    /**
     * @param tenantId ??????ID
     * @param eoId     eoId
     * @return com.ruike.hme.domain.vo.HmeEoVO2
     * @Description ??????EOID??????EO???????????????????????????
     * @author yuchao.wang
     * @date 2020/9/3 14:41
     */
    @Override
    public HmeEoVO2 queryEoActualByEoId(Long tenantId, String eoId) {
        return hmeEoJobSnMapper.queryEoActualByEoId(tenantId, eoId);
    }

    /**
     * @param tenantId     ??????ID
     * @param upgradeSnDTO ??????
     * @return java.lang.String
     * @Description ??????SN??????-??????????????????EoJobSn
     * @author yuchao.wang
     * @date 2020/9/3 18:46
     */
    @Override
    public String queryJobIdFirstProcessSnUpgrade(Long tenantId, HmeEoJobFirstProcessUpgradeSnDTO upgradeSnDTO) {
        return hmeEoJobSnMapper.queryJobIdFirstProcessSnUpgrade(tenantId, upgradeSnDTO);
    }

    /**
     * @param tenantId    ??????ID
     * @param sourceJobId ????????????ID
     * @return java.lang.Long
     * @Description ??????????????????????????????????????????
     * @author yuchao.wang
     * @date 2020/9/7 18:58
     */
    @Override
    public long queryNotPrintQtyBySourceJobId(Long tenantId, String sourceJobId) {
        Long notPrintQty = hmeEoJobSnMapper.queryNotPrintQtyBySourceJobId(tenantId, sourceJobId);
        return Objects.isNull(notPrintQty) ? 0L : notPrintQty;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO3 release(Long tenantId, HmeEoJobSnVO3 dto) {
        HmeEoJobSnVO3 hmeEoJobSnVO = new HmeEoJobSnVO3();
        HmeEoJobSnLotMaterialVO4 hmeEoJobSnLotMaterialVO4 = new HmeEoJobSnLotMaterialVO4();
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            //??????????????????????????????????????????sn???????????????EoId/EoStepId?????????sn?????? add by yuchao.wang for jiao.chen at 2020.9.28
            if (HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())) {
                if (CollectionUtils.isEmpty(dto.getSnLineList()) || dto.getSnLineList().size() > 1
                        || Objects.isNull(dto.getSnLineList().get(0))) {
                    //????????????????????????SN?????????,?????????????????????SN???????????????
                    throw new MtException("HME_EO_JOB_SN_086", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_086", "HME"));
                }
                dto.setEoId(dto.getSnLineList().get(0).getEoId());
                dto.setEoStepId(dto.getSnLineList().get(0).getEoStepId());
                dto.setJobId(dto.getSnLineList().get(0).getJobId());
                dto.setMaterialLotId(dto.getSnLineList().get(0).getMaterialLotId());
                dto.setReworkFlag(dto.getSnLineList().get(0).getReworkFlag());
                dto.setSnMaterialId(dto.getSnLineList().get(0).getSnMaterialId());
                dto.setWorkOrderId(dto.getSnLineList().get(0).getWorkOrderId());
                dto.setSnNum(dto.getSnLineList().get(0).getSnNum());
            }

            hmeEoJobSnVO = hmeEoJobSnLotMaterialRepository.lotMaterialOutSite2(tenantId, dto);
        } else {
            hmeEoJobSnVO = hmeEoJobSnLotMaterialRepository.prepareLotMaterialOutSite2(tenantId, dto);
        }

        return hmeEoJobSnVO;
    }

    @Override
    public List<HmeEoJobSnVO9> releaseBackQuery(Long tenantId, HmeEoJobSnVO3 dto) {

        if (StringUtils.isBlank(dto.getWorkcellId())) {
            //??????????????????!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if (StringUtils.isBlank(dto.getSnNum())) {
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        List<HmeEoJobSnVO9> allBackMaterialList = new ArrayList<>();
        if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            //???????????????
            List<HmeEoJobSnVO9> backMaterialList = hmeEoJobMaterialMapper.selectReleaseEoJobMaterial(tenantId, dto);
            if(CollectionUtils.isNotEmpty(backMaterialList)){
                allBackMaterialList.addAll(backMaterialList);
            }
        } else if (HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType()) || HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())) {
            //???????????????????????????
            List<HmeEoJobSnVO9> backMaterialList = hmeEoJobSnLotMaterialMapper.selectReleaseEoJobSnLotMaterial(tenantId, dto);
            if(CollectionUtils.isNotEmpty(backMaterialList)){
                allBackMaterialList.addAll(backMaterialList);
            }
        }
        return allBackMaterialList;
    }

    /**
     * ????????????
     *
     * @param tenantId ??????ID
     * @param dto      ????????????
     * @return
     */
    public void releaseBackValidate(Long tenantId, HmeEoJobSnVO9 dto) {
        if (StringUtils.isBlank(dto.getBackMaterialLotCode())) {
            //??????????????????,?????????
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (Objects.isNull(dto.getBackQty())) {
            //????????????????????????
            throw new MtException("HME_EO_JOB_SN_067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_067", "HME"));
        }

        //??????????????????SN??????
        int count = hmeEoJobSnMapper.selectCountByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                        .andIsNotNull(HmeEoJobSn.FIELD_SITE_IN_DATE)
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)
                        .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, dto.getSourceMaterialLotId())).build());
        if (count == 0) {
            //????????????SN?????????,?????????????????????
            throw new MtException("HME_EO_JOB_SN_071", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_071", "HME"));
        }

        //????????????????????????
        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getBackMaterialLotCode());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        if (Objects.isNull(mtMaterialLot)) {
            //?????????????????????
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }

        //V20201003 modify by penglin.sui for lu.bai ???????????????????????????????????????????????????????????????
        if (dto.getBackQty().compareTo(dto.getReleaseQty()) > 0) {
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
            if (Objects.isNull(mtMaterial)) {
                //???????????????${1}
                throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_0053", "HME", dto.getMaterialId()));
            }
            //?????????${1}?????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_069", "HME", mtMaterial.getMaterialCode()));
        }

        //????????????????????????????????????????????????
        if (!dto.getMaterialId().equals(mtMaterialLot.getMaterialId())) {
            //??????????????????????????????????????????,?????????
            throw new MtException("HME_EO_JOB_SN_102", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_102", "HME"));
        }

        //???????????????????????????
        MtExtendSettings productionVersionAttr = new MtExtendSettings();
        productionVersionAttr.setAttrName("MATERIAL_VERSION");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", mtMaterialLot.getMaterialLotId(),
                Collections.singletonList(productionVersionAttr));
        String productionVersion = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            productionVersion = mtExtendAttrVOList.get(0).getAttrValue();
        }
        //????????????????????????
        if (StringUtils.isNotBlank(dto.getProductionVersion()) || StringUtils.isNotBlank(productionVersion)) {
            if (!dto.getProductionVersion().equals(productionVersion)) {
                //??????????????????????????????????????????????????????,?????????
                throw new MtException("HME_EO_JOB_SN_103", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_103", "HME"));
            }
        }

        //??????????????????
        if (!dto.getLocatorId().equals(mtMaterialLot.getLocatorId())) {
            //????????????????????????????????????????????????????????????,?????????
            throw new MtException("HME_EO_JOB_SN_104", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_104", "HME"));
        }

        //??????????????????
        if (!dto.getSiteId().equals(mtMaterialLot.getSiteId())) {
            //??????????????????????????????????????????????????????,?????????
            throw new MtException("HME_EO_JOB_SN_106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_106", "HME"));
        }

        //??????????????????
        if (!dto.getLotCode().equals(mtMaterialLot.getLot())) {
            //??????????????????????????????????????????????????????,?????????
            throw new MtException("HME_EO_JOB_SN_118", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_118", "HME"));
        }

        //?????????????????????????????????????????????
        BigDecimal releaseQtySum = BigDecimal.ZERO;
        if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            //???????????????
            releaseQtySum = hmeEoJobMaterialMapper.selectReleaseQtySum(tenantId, dto);
        } else if (HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType()) || HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())) {
            //??????/????????????
            if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())) {
                //?????????
                releaseQtySum = hmeEoJobSnLotMaterialMapper.selectVirtualReleaseQtySum(tenantId, dto);
            } else {
                //????????????
                releaseQtySum = hmeEoJobSnLotMaterialMapper.selectReleaseQtySum(tenantId, dto);
            }
        }

        if (Objects.isNull(releaseQtySum)) {
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
            if (Objects.isNull(mtMaterial)) {
                //???????????????${1}
                throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_0053", "HME", dto.getMaterialId()));
            }
            //?????????${1}?????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_069", "HME", mtMaterial.getMaterialCode()));
        }
        if (dto.getBackQty().compareTo(releaseQtySum) > 0) {
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
            if (Objects.isNull(mtMaterial)) {
                //???????????????${1}
                throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MATERIAL_0053", "HME", dto.getMaterialId()));
            }
            //?????????${1}?????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_069", "HME", mtMaterial.getMaterialCode()));
        }
    }

    /**
     * ???????????????
     *
     * @param tenantId ??????ID
     * @param dto      ????????????
     * @return ????????????
     */
    public void virtualReleaseBack(Long tenantId, HmeEoJobSnVO9 dto) {
        // ????????????ID
//        String batchId = Utils.getBatchId();
        String batchId = Utils.getBatchId();
        //??????
        releaseBackValidate(tenantId, dto);

        //???????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WIP_RETURN");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_RETURN");

        //????????????
        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getBackMaterialLotCode());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        if (Objects.isNull(mtMaterialLot)) {
            //?????????????????????
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
        if (Objects.isNull(mtMaterial)) {
            //???????????????${1}
            throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_0053", "HME", dto.getMaterialId()));
        }
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (Objects.isNull(mtEo)) {
            //${1}????????? ?????????${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "EO", dto.getEoId()));
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, mtEo.getWorkOrderId());
        if (Objects.isNull(mtWorkOrder)) {
            //${1}????????? ?????????${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "??????", mtEo.getWorkOrderId()));
        }
        MtModProductionLine mtModProductionLine = mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId, mtWorkOrder.getProductionLineId());

        // ????????????wkc???????????????????????????
        MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
        mtWorkOrderVO7.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        mtWorkOrderVO7.setOperationId(dto.getOperationId());
        List<MtRouterStep> mtRouterStepList = hmeEoJobSnLotMaterialMapper.selectOperationStep(tenantId, mtWorkOrder.getRouterId(), dto.getOperationId());
        List<MtRouterStep> mtRouterStepList2 = new ArrayList<MtRouterStep>();
        if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
            MtRouterStep mtRouterStep = mtRouterStepRepository.selectByPrimaryKey(dto.getEoStepId());
            if (Objects.isNull(mtRouterStep)) {
                //${1}????????? ?????????${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "??????????????????", dto.getEoStepId()));
            }
            mtRouterStepList2 = mtRouterStepList.stream().filter(x -> x.getStepName().equals(mtRouterStep.getStepName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(mtRouterStepList2)) {
                mtWorkOrderVO7.setRouterStepId(mtRouterStepList2.get(0).getRouterStepId());
            }
        }
        List<MtWorkOrderVO8> woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
        List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(woComponentList)) {
            List<String> bomComponentIdList = woComponentList.stream().map(MtWorkOrderVO8::getBomComponentId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
                List<String> attrNameList = new ArrayList<>();
                attrNameList.add("lineAttribute5");
                mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentIdList, attrNameList);
            }
        }

        //????????????
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setTenantId(tenantId);
        mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setTrxPrimaryUomQty(dto.getBackQty().doubleValue());
        mtMaterialLotVO2.setEnableFlag(YES);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, HmeConstants.ConstantValue.NO);

        //??????????????????
        if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            //???????????????
            HmeEoJobMaterial existsHmeEoJobMaterial = hmeEoJobMaterialRepository.selectByPrimaryKey(dto.getJobMaterialId());

            HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
            hmeEoJobMaterial.setTenantId(tenantId);
            hmeEoJobMaterial.setJobId(existsHmeEoJobMaterial.getJobId());
            hmeEoJobMaterial.setWorkcellId(existsHmeEoJobMaterial.getWorkcellId());
            hmeEoJobMaterial.setSnMaterialId(existsHmeEoJobMaterial.getSnMaterialId());
            hmeEoJobMaterial.setMaterialId(existsHmeEoJobMaterial.getMaterialId());
            hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            hmeEoJobMaterial.setReleaseQty(dto.getBackQty().multiply(new BigDecimal(-1)));
            hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobMaterial.setEoId(existsHmeEoJobMaterial.getEoId());
            hmeEoJobMaterial.setBomComponentId(existsHmeEoJobMaterial.getBomComponentId());
            hmeEoJobMaterial.setBydMaterialId(existsHmeEoJobMaterial.getBydMaterialId());
            hmeEoJobMaterial.setLocatorId(existsHmeEoJobMaterial.getLocatorId());
            hmeEoJobMaterial.setLotCode(existsHmeEoJobMaterial.getLotCode());
            hmeEoJobMaterial.setProductionVersion(existsHmeEoJobMaterial.getProductionVersion());
            hmeEoJobMaterial.setVirtualFlag(existsHmeEoJobMaterial.getVirtualFlag());
            hmeEoJobMaterial.setParentMaterialLotId(existsHmeEoJobMaterial.getParentMaterialLotId());
            hmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty());
            hmeEoJobMaterialRepository.insert(hmeEoJobMaterial);

            //???????????????????????????is_issued
            existsHmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ZERO);
            hmeEoJobMaterialMapper.updateByPrimaryKey(existsHmeEoJobMaterial);
        } else {
            //??????/????????????
            HmeEoJobSnLotMaterial existsHmeEoJobSnLotMaterial = hmeEoJobSnLotMaterialRepository.selectByPrimaryKey(dto.getJobMaterialId());

            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
            hmeEoJobSnLotMaterial.setTenantId(tenantId);
            hmeEoJobSnLotMaterial.setLotMaterialId(existsHmeEoJobSnLotMaterial.getLotMaterialId());
            hmeEoJobSnLotMaterial.setTimeMaterialId(existsHmeEoJobSnLotMaterial.getTimeMaterialId());
            hmeEoJobSnLotMaterial.setMaterialType(existsHmeEoJobSnLotMaterial.getMaterialType());
            hmeEoJobSnLotMaterial.setWorkcellId(existsHmeEoJobSnLotMaterial.getWorkcellId());
            hmeEoJobSnLotMaterial.setJobId(existsHmeEoJobSnLotMaterial.getJobId());
            hmeEoJobSnLotMaterial.setMaterialId(existsHmeEoJobSnLotMaterial.getMaterialId());
            hmeEoJobSnLotMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobSnLotMaterial.setReleaseQty(dto.getReleaseQty().multiply(new BigDecimal(-1)));
            hmeEoJobSnLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobSnLotMaterial.setLocatorId(existsHmeEoJobSnLotMaterial.getLocatorId());
            hmeEoJobSnLotMaterial.setLotCode(existsHmeEoJobSnLotMaterial.getLotCode());
            hmeEoJobSnLotMaterial.setProductionVersion(existsHmeEoJobSnLotMaterial.getProductionVersion());
            hmeEoJobSnLotMaterial.setVirtualFlag(existsHmeEoJobSnLotMaterial.getVirtualFlag());
            hmeEoJobSnLotMaterial.setParentMaterialLotId(existsHmeEoJobSnLotMaterial.getParentMaterialLotId());
            hmeEoJobSnLotMaterial.setRemainQty(existsHmeEoJobSnLotMaterial.getRemainQty());
            hmeEoJobSnLotMaterialRepository.insert(hmeEoJobSnLotMaterial);

            //???????????????release_qty
//            if (HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType())) {
//                List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = hmeEoJobLotMaterialMapper.queryEoJobLotMaterial3(tenantId, existsHmeEoJobSnLotMaterial.getWorkcellId(), existsHmeEoJobSnLotMaterial.getMaterialId(), existsHmeEoJobSnLotMaterial.getProductionVersion());
//                if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList)) {
//                    for (HmeEoJobLotMaterial updateHmeEoJobLotMaterial : hmeEoJobLotMaterialList
//                    ) {
//                        updateHmeEoJobLotMaterial.setReleaseQty((Objects.isNull(updateHmeEoJobLotMaterial.getReleaseQty()) ? BigDecimal.ZERO : updateHmeEoJobLotMaterial.getReleaseQty()).add(dto.getBackQty()));
//                        hmeEoJobLotMaterialMapper.updateByPrimaryKey(updateHmeEoJobLotMaterial);
//                    }
//                }
//                HmeEoJobLotMaterial hmeEoJobLotMaterial = hmeEoJobLotMaterialMapper.selectByPrimaryKey(existsHmeEoJobSnLotMaterial.getLotMaterialId());
//                if(Objects.nonNull(hmeEoJobLotMaterial)){
//                    hmeEoJobLotMaterial.setCostQty(hmeEoJobLotMaterial.getCostQty().subtract(dto.getBackQty()));
//                    hmeEoJobLotMaterialMapper.updateByPrimaryKey(hmeEoJobLotMaterial);
//                }
//            } else if (HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())) {
//                List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList = hmeEoJobTimeMaterialMapper.queryEoJobTimeMaterial3(tenantId, existsHmeEoJobSnLotMaterial.getWorkcellId(), existsHmeEoJobSnLotMaterial.getMaterialId(), existsHmeEoJobSnLotMaterial.getProductionVersion());
//                if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
//                    for (HmeEoJobTimeMaterial updateHmeEoJobTimeMaterial : hmeEoJobTimeMaterialList
//                    ) {
//                        updateHmeEoJobTimeMaterial.setReleaseQty((Objects.isNull(updateHmeEoJobTimeMaterial.getReleaseQty()) ? BigDecimal.ZERO : updateHmeEoJobTimeMaterial.getReleaseQty()).add(dto.getBackQty()));
//                        hmeEoJobTimeMaterialMapper.updateByPrimaryKey(updateHmeEoJobTimeMaterial);
//                    }
//                }
//                HmeEoJobTimeMaterial hmeEoJobTimeMaterial = hmeEoJobTimeMaterialMapper.selectByPrimaryKey(existsHmeEoJobSnLotMaterial.getTimeMaterialId());
//                if(Objects.nonNull(hmeEoJobTimeMaterial)){
//                    hmeEoJobTimeMaterial.setCostQty(hmeEoJobTimeMaterial.getCostQty().subtract(dto.getBackQty()));
//                    hmeEoJobTimeMaterialMapper.updateByPrimaryKey(hmeEoJobTimeMaterial);
//                }
//            }
            //?????????????????????release_qty
            existsHmeEoJobSnLotMaterial.setReleaseQty((Objects.isNull(existsHmeEoJobSnLotMaterial.getReleaseQty()) ? BigDecimal.ZERO : existsHmeEoJobSnLotMaterial.getReleaseQty()).subtract(dto.getBackQty()));
            hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(existsHmeEoJobSnLotMaterial);
        }

        MtEoVO19 mtEoVO19 = new MtEoVO19();
        mtEoVO19.setEoId(dto.getEoId());
        mtEoVO19.setOperationId(dto.getOperationId());
        mtEoVO19.setRouterStepId(dto.getEoStepId());
        // ????????????wkc???????????????????????????
        List<MtEoVO20> eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
        //V20201221 modify by penglin.sui for lu.bai ??????BOM????????????
        Map<String,MtExtendAttrVO1> bomComponentExtendMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(eoComponentList)){
            List<String> bomComponentIdList = eoComponentList.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
            List<String> attrNameList = new ArrayList<>(4);
            attrNameList.add(HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM);
            attrNameList.add(HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG);
            attrNameList.add(HmeConstants.BomComponentExtendAttr.VIRTUAL_FLAG);
            List<MtExtendAttrVO1> bomExtendAttrList = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
            if(CollectionUtils.isNotEmpty(bomExtendAttrList)){
                bomComponentExtendMap = bomExtendAttrList.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getKeyId(),item.getAttrName()), t -> t));
            }
        }

        //??????????????????????????????
        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotComponentMaterialList = new ArrayList<HmeEoJobSnLotMaterial>();
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, mtMaterialLot.getMaterialLotId())
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.PREPARE_PROCESS)).build());
        BigDecimal virtualSnQtySum = hmeEoJobSnList.stream().map(HmeEoJobSn::getSnQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            List<String> jobIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getJobId).collect(Collectors.toList());
            List<HmeEoJobMaterial> hmeEoJobMaterialList2 = hmeEoJobMaterialMapper.selectVirtualComponent(tenantId, jobIdList);
            if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList2)) {
                // ??????????????????
                List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterialList3 = hmeEoJobMaterialList2.stream().map(material2 -> {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setIsReleased(material2.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(material2.getJobId());
                    hmeEoJobSnLotMaterial.setLotMaterialId(material2.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialId(material2.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material2.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.SN);
                    hmeEoJobSnLotMaterial.setReleaseQty(material2.getReleaseQty());
                    hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                    hmeEoJobSnLotMaterial.setLocatorId(material2.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material2.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(StringUtils.isBlank(material2.getVirtualFlag()) ? HmeConstants.ConstantValue.NO : material2.getVirtualFlag());
                    hmeEoJobSnLotMaterial.setProductionVersion(material2.getProductionVersion());
                    hmeEoJobSnLotMaterial.setRemainQty(Objects.isNull(material2.getRemainQty()) ? BigDecimal.ZERO : material2.getRemainQty());
                    return hmeEoJobSnLotMaterial;
                }).collect(Collectors.toList());
                hmeEoJobSnLotComponentMaterialList.addAll(hmeEoJobSnLotMaterialList3);
            }

            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotComponentMaterialList2 = hmeEoJobSnLotMaterialMapper.selectVirtualComponent(tenantId, jobIdList);
            if (CollectionUtils.isNotEmpty(hmeEoJobSnLotComponentMaterialList2)) {
                hmeEoJobSnLotComponentMaterialList.addAll(hmeEoJobSnLotComponentMaterialList2);
            }
        }

        //???????????????
        if (CollectionUtils.isNotEmpty(hmeEoJobSnLotComponentMaterialList)) {
            Boolean canOutFeedQtyFlag = false;
            String assembleExcessFlag = "";
            String transactionTypeCode = "";
            String inAssembleExcessFlag = "";
            String inTransactionTypeCode = "";
            //?????????????????????
            BigDecimal canOutFeedQty = BigDecimal.ZERO;
            BigDecimal haveOutFeedQty = BigDecimal.ZERO;
            MtBomComponent woBomComponent = null;
            //?????????????????????
            List<String> componentMaterialIdList = hmeEoJobSnLotComponentMaterialList.stream().map(HmeEoJobSnLotMaterial::getMaterialId).distinct().collect(Collectors.toList());
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotComponentMaterialList2 = new ArrayList<HmeEoJobSnLotMaterial>();
            BigDecimal componentReleaseBackQty = BigDecimal.ZERO;
            BigDecimal currComponentReleaseBackQty = BigDecimal.ZERO;
            BigDecimal remainQty = BigDecimal.ZERO;

            String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
            String routerStepId = "";
            if (CollectionUtils.isNotEmpty(mtRouterStepList2)) {
                routerStepId = mtRouterStepList2.get(0).getRouterStepId();
            }
            if ((StringUtils.isEmpty(flagGet) || NO.equals(flagGet))) {
                routerStepId = "";
            }

            for (String materialId : componentMaterialIdList
            ) {
                hmeEoJobSnLotComponentMaterialList2 = hmeEoJobSnLotComponentMaterialList.stream().filter(x -> x.getMaterialId().equals(materialId)).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(hmeEoJobSnLotComponentMaterialList2)) {
                    continue;
                }
                componentReleaseBackQty = dto.getBackQty().multiply(hmeEoJobSnLotComponentMaterialList2.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add)).divide(virtualSnQtySum, 2, BigDecimal.ROUND_HALF_EVEN);
                log.info("<====== HmeEoJobSnRepositoryImpl.virtualReleaseBack hmeEoJobSnLotComponentMaterialList2.sum=[{}]", hmeEoJobSnLotComponentMaterialList2.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                log.info("<====== HmeEoJobSnRepositoryImpl.virtualReleaseBack virtualSnQtySum=[{}]", virtualSnQtySum);
                log.info("<====== HmeEoJobSnRepositoryImpl.virtualReleaseBack componentReleaseBackQty=[{}]", componentReleaseBackQty);
                remainQty = componentReleaseBackQty;

                //?????????????????????
                canOutFeedQty = BigDecimal.ZERO;
                haveOutFeedQty = BigDecimal.ZERO;
                woBomComponent = null;
                inAssembleExcessFlag = YES;
                inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT;
                if (CollectionUtils.isNotEmpty(woComponentList)) {
                    // ?????????ID
                    Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(materialId)).findFirst();
                    if (componentOptional.isPresent()) {
                        MtWorkOrderVO8 component = componentOptional.get();
                        woBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, component.getBomComponentId());
                        inAssembleExcessFlag = HmeConstants.ConstantValue.NO;
                        inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R;
                    }
                }

                //modify by penglin.sui for jiao.chen
                //???????????????????????????????????????????????????????????? + ???????????? ??? ?????????????????????????????????????????????????????????????????????????????????
                canOutFeedQtyFlag = false;

                if (HmeConstants.ConstantValue.NO.equals(inAssembleExcessFlag)) {

                    String woBomComponentId = woBomComponent.getBomComponentId();
                    List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(woBomComponentId)).collect(Collectors.toList());
                    BigDecimal woSumQty = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                        if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                            woSumQty = new BigDecimal(mtExtendAttrVO1List2.get(0).getAttrValue());
                        }
                    }
                    if (woSumQty.compareTo(BigDecimal.ZERO) <= 0) {
                        //????????????????????????????????????
                        canOutFeedQty = dto.getBackQty();
                        canOutFeedQtyFlag = true;
                        inAssembleExcessFlag = YES;
                        inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT;
                    } else {
                        List<MtWorkOrderComponentActual> mtWorkOrderComponentActualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class)
                                .andWhere(Sqls.custom()
                                        .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                                        .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, woBomComponent.getBomComponentId())
                                        .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerStepId))
                                .build());
                        BigDecimal assembleQty = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(mtWorkOrderComponentActualList)) {
                            MtWorkOrderComponentActual mtWorkOrderComponentActual = mtWorkOrderComponentActualList.get(0);
                            assembleQty = BigDecimal.valueOf(mtWorkOrderComponentActual.getAssembleQty());
                        }
                        if (assembleQty.compareTo(woSumQty) > 0) {
                            //????????????????????????????????????
                            canOutFeedQty = assembleQty.subtract(woSumQty);
                            canOutFeedQty = canOutFeedQty.compareTo(dto.getBackQty()) > 0 ? dto.getBackQty() : canOutFeedQty;
                            canOutFeedQtyFlag = true;
                            inAssembleExcessFlag = YES;
                            inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT;
                        }
                    }
                }

                for (int i = 0; i <= hmeEoJobSnLotComponentMaterialList2.size(); i++
                ) {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotComponentMaterial = hmeEoJobSnLotComponentMaterialList2.get(i);
                    if (hmeEoJobSnLotComponentMaterial.getReleaseQty().compareTo(BigDecimal.ZERO) <= 0) {
                        continue;
                    }
                    currComponentReleaseBackQty = (remainQty.compareTo(hmeEoJobSnLotComponentMaterial.getReleaseQty().subtract(haveOutFeedQty))) > 0 ? hmeEoJobSnLotComponentMaterial.getReleaseQty() : remainQty;
                    if (currComponentReleaseBackQty.compareTo(BigDecimal.ZERO) <= 0) {
                        haveOutFeedQty = BigDecimal.ZERO;
                        continue;
                    }

                    MtMaterialLot mtMaterialLotComponent = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSnLotComponentMaterial.getMaterialLotId());
                    if (Objects.isNull(mtMaterialLotComponent)) {
                        //?????????????????????
                        throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_NC_0003", "HME"));
                    }

                    MtMaterial mtMaterialComponent = mtMaterialRepository.selectByPrimaryKey(hmeEoJobSnLotComponentMaterial.getMaterialId());
                    if (Objects.isNull(mtMaterialComponent)) {
                        //???????????????${1}
                        throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_0053", "HME", dto.getMaterialId()));
                    }

                    assembleExcessFlag = inAssembleExcessFlag;
                    transactionTypeCode = inTransactionTypeCode;
                    if (canOutFeedQtyFlag) {
                        if (canOutFeedQty.compareTo(BigDecimal.ZERO) > 0) {
                            if (canOutFeedQty.compareTo(currComponentReleaseBackQty) < 0) {
                                currComponentReleaseBackQty = canOutFeedQty;
                                //???????????????????????????????????????????????????????????????????????????????????????????????????
                                i--;
                                canOutFeedQty = BigDecimal.ZERO;
                                haveOutFeedQty = currComponentReleaseBackQty;
                            } else {
                                canOutFeedQty = canOutFeedQty.subtract(currComponentReleaseBackQty);
                                haveOutFeedQty = BigDecimal.ZERO;
                            }
                        } else {
                            haveOutFeedQty = BigDecimal.ZERO;
                            assembleExcessFlag = HmeConstants.ConstantValue.NO;
                            transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R;
                        }
                    }

                    //?????????????????????????????????
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial1 = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial1.setJobMaterialId(hmeEoJobSnLotComponentMaterial.getJobMaterialId());
                    hmeEoJobSnLotMaterial1.setRemainQty(hmeEoJobSnLotComponentMaterial.getRemainQty().add(currComponentReleaseBackQty));
                    hmeEoJobSnLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobSnLotMaterial1);

                    //??????????????????
                    List<MtModLocator> mtModLocators = hmeEoJobSnLotMaterialMapper.queryPreLoadLocator(tenantId,
                            mtMaterialLotComponent.getLocatorId(), dto.getWorkcellId());
                    if (CollectionUtils.isEmpty(mtModLocators) || Objects.isNull(mtModLocators.get(0))
                            || StringUtils.isBlank(mtModLocators.get(0).getLocatorId())) {
                        throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
                    }
                    if (mtModLocators.size() > 1) {
                        //?????????????????????${1}??????????????????????????????,?????????
                        throw new MtException("HME_EO_JOB_SN_101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_101", "HME", HmeConstants.LocaltorType.PRE_LOAD));
                    }
                    MtModLocator mtModLocator1 = mtModLocators.get(0);

                    //??????????????????????????????
                    MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                    mtInvOnhandQuantityVO9.setSiteId(mtEo.getSiteId());
                    mtInvOnhandQuantityVO9.setLocatorId(mtModLocator1.getLocatorId());
                    mtInvOnhandQuantityVO9.setMaterialId(hmeEoJobSnLotComponentMaterial.getMaterialId());
                    mtInvOnhandQuantityVO9.setLotCode(hmeEoJobSnLotComponentMaterial.getLotCode());
                    mtInvOnhandQuantityVO9.setChangeQuantity(currComponentReleaseBackQty.doubleValue());
                    mtInvOnhandQuantityVO9.setEventId(eventId);
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

                    //EO????????????
                    MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = new MtAssembleProcessActualVO5();
                    mtAssembleProcessActualVO5.setEoId(dto.getEoId());
                    mtAssembleProcessActualVO5.setMaterialId(hmeEoJobSnLotComponentMaterial.getMaterialId());
                    mtAssembleProcessActualVO5.setOperationId(dto.getOperationId());
                    mtAssembleProcessActualVO5.setRouterStepId(dto.getEoStepId());
                    mtAssembleProcessActualVO5.setTrxAssembleQty(currComponentReleaseBackQty.doubleValue());
                    mtAssembleProcessActualVO5.setLocatorId(mtModLocator1.getLocatorId());
                    Long userId = -1L;
                    if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
                        userId = DetailsHelper.getUserDetails().getUserId();
                    }
                    mtAssembleProcessActualVO5.setOperationBy(userId);
                    mtAssembleProcessActualVO5.setWorkcellId(hmeEoJobSnLotComponentMaterial.getWorkcellId());
                    mtAssembleProcessActualVO5.setParentEventId(eventId);
                    mtAssembleProcessActualVO5.setEventRequestId(eventRequestId);
                    if (StringUtils.isBlank(dto.getShiftId())) {
                        MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, dto.getShiftId());
                        mtAssembleProcessActualVO5.setShiftCode(mtWkcShift.getShiftCode());
                        mtAssembleProcessActualVO5.setShiftDate(mtWkcShift.getShiftDate());
                    }
                    mtAssembleProcessActualVO5.setMaterialLotId(hmeEoJobSnLotComponentMaterial.getMaterialLotId());

                    WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                    String bomComponentId = "";
                    if (CollectionUtils.isNotEmpty(eoComponentList)) {
                        // ?????????ID
                        Optional<MtEoVO20> componentOptional = eoComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(hmeEoJobSnLotComponentMaterial.getMaterialId())).findFirst();
                        if (componentOptional.isPresent()) {
                            MtEoVO20 component = componentOptional.get();
                            bomComponentId = component.getBomComponentId();
                            mtAssembleProcessActualVO5.setBomComponentId(component.getBomComponentId());
                            MtBomComponent mtBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, component.getBomComponentId());
                            if (Objects.isNull(mtBomComponent)) {
                                //${1}????????? ?????????${2}
                                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_GENERAL_0037", "GENERAL", "BOM", component.getBomComponentId()));
                            }
                            if(MapUtils.isNotEmpty(bomComponentExtendMap)){
                                MtExtendAttrVO1 bomMtExtendAttr = bomComponentExtendMap.getOrDefault(fetchGroupKey2(component.getBomComponentId(),HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM),null);
                                if(Objects.nonNull(bomMtExtendAttr)){
                                    objectTransactionVO.setBomReserveNum(bomMtExtendAttr.getAttrValue());
                                }
                            }
                            objectTransactionVO.setBomReserveLineNum(String.valueOf(mtBomComponent.getLineNumber()));
                        }
                    }
                    mtAssembleProcessActualVO5.setAssembleExcessFlag(assembleExcessFlag);
                    mtAssembleProcessActualRepositoryImpl.componentAssembleCancelProcess(tenantId, mtAssembleProcessActualVO5);

                    //????????????
                    List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
                    objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
                    objectTransactionVO.setEventId(eventId);
                    objectTransactionVO.setMaterialLotId(hmeEoJobSnLotComponentMaterial.getMaterialLotId());
                    objectTransactionVO.setMaterialId(mtMaterialComponent.getMaterialId());
                    objectTransactionVO.setMaterialCode(mtMaterialComponent.getMaterialCode());
                    objectTransactionVO.setTransactionQty(currComponentReleaseBackQty);
                    objectTransactionVO.setLotNumber(mtMaterialLotComponent.getLot());
                    if (!StringUtils.isBlank(mtMaterialComponent.getPrimaryUomId())) {
                        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialComponent.getPrimaryUomId());
                        if (Objects.nonNull(mtUom)) {
                            objectTransactionVO.setTransactionUom(mtUom.getUomCode());
                        }
                    }
                    objectTransactionVO.setTransactionTime(new Date());
                    objectTransactionVO.setPlantId(mtMaterialLotComponent.getSiteId());
                    MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(mtMaterialLotComponent.getSiteId());
                    if (Objects.nonNull(mtModSite)) {
                        objectTransactionVO.setPlantCode(mtModSite.getSiteCode());
                    }
                    if (!StringUtils.isBlank(mtModLocator1.getLocatorId())) {
                        objectTransactionVO.setLocatorId(mtModLocator1.getLocatorId());
                        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtModLocator1.getLocatorId());
                        if (Objects.nonNull(mtModLocator)) {
                            objectTransactionVO.setLocatorCode(mtModLocator.getLocatorCode());
                        }

                        MtModLocator mtModLocator2 = hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId, mtModLocator1.getLocatorId());
                        if (Objects.nonNull(mtModLocator2)) {
                            objectTransactionVO.setWarehouseId(mtModLocator2.getLocatorId());
                            objectTransactionVO.setWarehouseCode(mtModLocator2.getLocatorCode());
                        }
                    }
                    objectTransactionVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                    objectTransactionVO.setProdLineCode(mtModProductionLine.getProdLineCode());

                    WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
                    wmsTransactionTypePara.setTenantId(tenantId);
                    wmsTransactionTypePara.setTransactionTypeCode(transactionTypeCode);
                    WmsTransactionType wmsTransactionType = transactionTypeRepository.selectOne(wmsTransactionTypePara);
                    objectTransactionVO.setMoveType(Objects.isNull(wmsTransactionType) ? "262" : wmsTransactionType.getMoveType());
                    String specialInvFlag = "";
                    if(MapUtils.isNotEmpty(bomComponentExtendMap)){
                        MtExtendAttrVO1 bomMtExtendAttr = bomComponentExtendMap.getOrDefault(fetchGroupKey2(bomComponentId,HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG),null);
                        if(Objects.nonNull(bomMtExtendAttr)){
                            specialInvFlag = bomMtExtendAttr.getAttrValue();
                        }
                    }
                    if("E".equals(specialInvFlag)) {
                        //???????????????????????????
                        MtExtendSettings soNumAttr = new MtExtendSettings();
                        soNumAttr.setAttrName("SO_NUM");
                        List<MtExtendAttrVO> soNumAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                "mt_material_lot_attr", "MATERIAL_LOT_ID", hmeEoJobSnLotComponentMaterial.getMaterialLotId(),
                                Collections.singletonList(soNumAttr));
                        if (CollectionUtils.isNotEmpty(soNumAttrVOList)) {
                            objectTransactionVO.setSoNum(soNumAttrVOList.get(0).getAttrValue());
                        }

                        //???????????????????????????
                        MtExtendSettings soLineNumAttr = new MtExtendSettings();
                        soLineNumAttr.setAttrName("SO_LINE_NUM");
                        List<MtExtendAttrVO> soLineNumAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                "mt_material_lot_attr", "MATERIAL_LOT_ID", hmeEoJobSnLotComponentMaterial.getMaterialLotId(),
                                Collections.singletonList(soLineNumAttr));
                        if (CollectionUtils.isNotEmpty(soLineNumAttrVOList)) {
                            objectTransactionVO.setSoLineNum(soLineNumAttrVOList.get(0).getAttrValue());
                        }
                    }
                    objectTransactionVO.setAttribute16(batchId);
                    objectTransactionRequestList.add(objectTransactionVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

                    remainQty = remainQty.subtract(currComponentReleaseBackQty);
                    if (remainQty.compareTo(BigDecimal.ZERO) <= 0) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param tenantId ??????ID
     * @param dto      ????????????
     * @return ????????????
     */
    public void normalReleaseBack(Long tenantId, HmeEoJobSnVO9 dto) {
        String batchId = Utils.getBatchId();
        //??????
        releaseBackValidate(tenantId, dto);

        //???????????????????????????
        String eventId = "";
        String outEventId = "";
        String eventRequestId = "";
        String outEventRequestId = "";
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("WIP_RETURN");
            eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_RETURN");
        } else {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("MATERIAL_PREPARE_IN_R");
            eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            MtEventCreateVO outEventCreateVO = new MtEventCreateVO();
            outEventCreateVO.setEventTypeCode("MATERIAL_PREPARE_OUT_R");
            outEventId = mtEventRepository.eventCreate(tenantId, outEventCreateVO);
        }

        //????????????
        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getBackMaterialLotCode());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        if (Objects.isNull(mtMaterialLot)) {
            //?????????????????????
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
        if (Objects.isNull(mtMaterial)) {
            //???????????????${1}
            throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_0053", "HME", dto.getMaterialId()));
        }
        MtEo mtEo = null;
        String workOrderId = dto.getWorkOrderId();
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (Objects.isNull(mtEo)) {
                //${1}????????? ?????????${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "EO", dto.getEoId()));
            }
            workOrderId = mtEo.getWorkOrderId();
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
        if (Objects.isNull(mtWorkOrder)) {
            //${1}????????? ?????????${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "??????", workOrderId));
        }
        MtModProductionLine mtModProductionLine = mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId, mtWorkOrder.getProductionLineId());

        //????????????
        MtMaterialLotVO2 materialLotVO = new MtMaterialLotVO2();
        materialLotVO.setTenantId(tenantId);
        materialLotVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        materialLotVO.setEventId(eventId);
        materialLotVO.setQualityStatus(HmeConstants.ConstantValue.OK);
        materialLotVO.setTrxPrimaryUomQty(dto.getBackQty().doubleValue());
        materialLotVO.setEnableFlag(YES);
        MtMaterialLotVO13 materialLotResult = mtMaterialLotRepository.materialLotUpdate(tenantId,
                materialLotVO, HmeConstants.ConstantValue.NO);

        List<MtEoVO20> eoComponentList = new ArrayList<MtEoVO20>();
        List<MtWorkOrderVO8> woComponentList = new ArrayList<>();
        List<MtRouterStep> mtRouterStepList2 = new ArrayList<MtRouterStep>();
        List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
        Map<String,MtExtendAttrVO1> bomComponentExtendMap = new HashMap<>();
        //???????????????
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {

            MtEoVO19 mtEoVO19 = new MtEoVO19();
            mtEoVO19.setEoId(dto.getEoId());
            mtEoVO19.setOperationId(dto.getOperationId());
            mtEoVO19.setRouterStepId(dto.getEoStepId());
            // ????????????wkc???????????????????????????
            eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
            //V20201221 modify by penglin.sui for lu.bai ??????BOM????????????
            if(CollectionUtils.isNotEmpty(eoComponentList)){
                List<String> bomComponentIdList = eoComponentList.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
                List<String> attrNameList = new ArrayList<>(3);
                attrNameList.add(HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM);
                attrNameList.add(HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG);
                attrNameList.add(HmeConstants.BomComponentExtendAttr.VIRTUAL_FLAG);
                List<MtExtendAttrVO1> bomExtendAttrList = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
                if(CollectionUtils.isNotEmpty(bomExtendAttrList)){
                    bomComponentExtendMap = bomExtendAttrList.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getKeyId(),item.getAttrName()), t -> t));
                }
            }

            // ????????????wkc???????????????????????????
            MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
            mtWorkOrderVO7.setWorkOrderId(mtWorkOrder.getWorkOrderId());
            mtWorkOrderVO7.setOperationId(dto.getOperationId());
            List<MtRouterStep> mtRouterStepList = hmeEoJobSnLotMaterialMapper.selectOperationStep(tenantId, mtWorkOrder.getRouterId(), dto.getOperationId());
            if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
                MtRouterStep mtRouterStep = mtRouterStepRepository.selectByPrimaryKey(dto.getEoStepId());
                if (Objects.isNull(mtRouterStep)) {
                    //${1}????????? ?????????${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "??????????????????", dto.getEoStepId()));
                }
                mtRouterStepList2 = mtRouterStepList.stream().filter(x -> x.getStepName().equals(mtRouterStep.getStepName())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtRouterStepList2)) {
                    mtWorkOrderVO7.setRouterStepId(mtRouterStepList2.get(0).getRouterStepId());
                }
            }
            woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
            if (CollectionUtils.isNotEmpty(woComponentList)) {
                List<String> bomComponentIdList = woComponentList.stream().map(MtWorkOrderVO8::getBomComponentId).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
                    List<String> attrNameList = new ArrayList<>();
                    attrNameList.add("lineAttribute5");
                    mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentIdList, attrNameList);
                }
            }

            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            mtInvOnhandQuantityVO9.setSiteId(mtEo.getSiteId());
            mtInvOnhandQuantityVO9.setLocatorId(dto.getLocatorId());
            mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
            mtInvOnhandQuantityVO9.setChangeQuantity(dto.getBackQty().doubleValue());
            mtInvOnhandQuantityVO9.setEventId(eventId);
            mtInvOnhandQuantityVO9.setLotCode(dto.getLotCode());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
        } else {
            //???????????????????????????
            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
            mtInvOnhandQuantityVO9.setSiteId(mtWorkOrder.getSiteId());
            mtInvOnhandQuantityVO9.setLocatorId(dto.getLocatorId());
            mtInvOnhandQuantityVO9.setMaterialId(mtMaterialLot.getMaterialId());
            mtInvOnhandQuantityVO9.setChangeQuantity(dto.getBackQty().doubleValue());
            mtInvOnhandQuantityVO9.setEventId(eventId);
            mtInvOnhandQuantityVO9.setLotCode(dto.getLotCode());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

            //???????????????????????????
            List<MtModLocator> mtModLocators = hmeEoJobSnLotMaterialMapper.queryPreLoadLocator(tenantId,
                    dto.getLocatorId(), dto.getWorkcellId());
            if (CollectionUtils.isEmpty(mtModLocators) || Objects.isNull(mtModLocators.get(0))
                    || StringUtils.isBlank(mtModLocators.get(0).getLocatorId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
            }
            if (mtModLocators.size() > 1) {
                //?????????????????????${1}??????????????????????????????,?????????
                throw new MtException("HME_EO_JOB_SN_101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_101", "HME", HmeConstants.LocaltorType.PRE_LOAD));
            }
            MtModLocator mtModLocator1 = mtModLocators.get(0);
            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO92 = new MtInvOnhandQuantityVO9();
            mtInvOnhandQuantityVO92.setSiteId(mtWorkOrder.getSiteId());
            mtInvOnhandQuantityVO92.setLocatorId(mtModLocator1.getLocatorId());
            mtInvOnhandQuantityVO92.setMaterialId(mtMaterialLot.getMaterialId());
            mtInvOnhandQuantityVO92.setChangeQuantity(dto.getBackQty().doubleValue());
            mtInvOnhandQuantityVO92.setEventId(outEventId);
            mtInvOnhandQuantityVO92.setLotCode(dto.getLotCode());
            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO92);
        }

        //??????????????????
        if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            //???????????????
            HmeEoJobMaterial existsHmeEoJobMaterial = hmeEoJobMaterialRepository.selectByPrimaryKey(dto.getJobMaterialId());

            HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
            hmeEoJobMaterial.setTenantId(tenantId);
            hmeEoJobMaterial.setJobId(existsHmeEoJobMaterial.getJobId());
            hmeEoJobMaterial.setWorkcellId(existsHmeEoJobMaterial.getWorkcellId());
            hmeEoJobMaterial.setSnMaterialId(existsHmeEoJobMaterial.getSnMaterialId());
            hmeEoJobMaterial.setMaterialId(existsHmeEoJobMaterial.getMaterialId());
            hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            hmeEoJobMaterial.setReleaseQty(dto.getBackQty().multiply(new BigDecimal(-1)));
            hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobMaterial.setEoId(existsHmeEoJobMaterial.getEoId());
            hmeEoJobMaterial.setBomComponentId(existsHmeEoJobMaterial.getBomComponentId());
            hmeEoJobMaterial.setBydMaterialId(existsHmeEoJobMaterial.getBydMaterialId());
            hmeEoJobMaterial.setLocatorId(existsHmeEoJobMaterial.getLocatorId());
            hmeEoJobMaterial.setLotCode(existsHmeEoJobMaterial.getLotCode());
            hmeEoJobMaterial.setProductionVersion(existsHmeEoJobMaterial.getProductionVersion());
            hmeEoJobMaterial.setVirtualFlag(existsHmeEoJobMaterial.getVirtualFlag());
            hmeEoJobMaterial.setParentMaterialLotId(existsHmeEoJobMaterial.getParentMaterialLotId());
            hmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty());
            hmeEoJobMaterialRepository.insert(hmeEoJobMaterial);

            //???????????????????????????is_issued
            existsHmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ZERO);
            if (Objects.nonNull(existsHmeEoJobMaterial.getRemainQty())) {
                existsHmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty().subtract(dto.getBackQty()));
            }
            hmeEoJobMaterialMapper.updateByPrimaryKey(existsHmeEoJobMaterial);
        } else {
            //??????/????????????
            HmeEoJobSnLotMaterial existsHmeEoJobSnLotMaterial = hmeEoJobSnLotMaterialRepository.selectByPrimaryKey(dto.getJobMaterialId());

            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
            hmeEoJobSnLotMaterial.setTenantId(tenantId);
            hmeEoJobSnLotMaterial.setLotMaterialId(existsHmeEoJobSnLotMaterial.getLotMaterialId());
            hmeEoJobSnLotMaterial.setTimeMaterialId(existsHmeEoJobSnLotMaterial.getTimeMaterialId());
            hmeEoJobSnLotMaterial.setMaterialType(existsHmeEoJobSnLotMaterial.getMaterialType());
            hmeEoJobSnLotMaterial.setWorkcellId(existsHmeEoJobSnLotMaterial.getWorkcellId());
            hmeEoJobSnLotMaterial.setJobId(existsHmeEoJobSnLotMaterial.getJobId());
            hmeEoJobSnLotMaterial.setMaterialId(existsHmeEoJobSnLotMaterial.getMaterialId());
            hmeEoJobSnLotMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeEoJobSnLotMaterial.setReleaseQty(dto.getReleaseQty().multiply(new BigDecimal(-1)));
            hmeEoJobSnLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobSnLotMaterial.setLocatorId(existsHmeEoJobSnLotMaterial.getLocatorId());
            hmeEoJobSnLotMaterial.setLotCode(existsHmeEoJobSnLotMaterial.getLotCode());
            hmeEoJobSnLotMaterial.setProductionVersion(existsHmeEoJobSnLotMaterial.getProductionVersion());
            hmeEoJobSnLotMaterial.setVirtualFlag(existsHmeEoJobSnLotMaterial.getVirtualFlag());
            hmeEoJobSnLotMaterial.setParentMaterialLotId(existsHmeEoJobSnLotMaterial.getParentMaterialLotId());
            hmeEoJobSnLotMaterial.setRemainQty(existsHmeEoJobSnLotMaterial.getRemainQty());
            hmeEoJobSnLotMaterialRepository.insert(hmeEoJobSnLotMaterial);

            //???????????????cost_qty
//            if (HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType())) {
//                List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = hmeEoJobLotMaterialMapper.queryEoJobLotMaterial3(tenantId, existsHmeEoJobSnLotMaterial.getWorkcellId(), existsHmeEoJobSnLotMaterial.getMaterialId(), existsHmeEoJobSnLotMaterial.getProductionVersion());
//                if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList)) {
//                    for (HmeEoJobLotMaterial updateHmeEoJobLotMaterial : hmeEoJobLotMaterialList
//                    ) {
//                        updateHmeEoJobLotMaterial.setReleaseQty((Objects.isNull(updateHmeEoJobLotMaterial.getReleaseQty()) ? BigDecimal.ZERO : updateHmeEoJobLotMaterial.getReleaseQty()).add(dto.getBackQty()));
//                        hmeEoJobLotMaterialMapper.updateByPrimaryKey(updateHmeEoJobLotMaterial);
//                    }
//                }
//                HmeEoJobLotMaterial hmeEoJobLotMaterial = hmeEoJobLotMaterialMapper.selectByPrimaryKey(existsHmeEoJobSnLotMaterial.getLotMaterialId());
//                if(Objects.nonNull(hmeEoJobLotMaterial)){
//                    hmeEoJobLotMaterial.setCostQty(hmeEoJobLotMaterial.getCostQty().subtract(dto.getBackQty()));
//                    hmeEoJobLotMaterialMapper.updateByPrimaryKey(hmeEoJobLotMaterial);
//                }
//            } else if (HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())) {
//                List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList = hmeEoJobTimeMaterialMapper.queryEoJobTimeMaterial3(tenantId, existsHmeEoJobSnLotMaterial.getWorkcellId(), existsHmeEoJobSnLotMaterial.getMaterialId(), existsHmeEoJobSnLotMaterial.getProductionVersion());
//                if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
//                    for (HmeEoJobTimeMaterial updateHmeEoJobTimeMaterial : hmeEoJobTimeMaterialList
//                    ) {
//                        updateHmeEoJobTimeMaterial.setReleaseQty((Objects.isNull(updateHmeEoJobTimeMaterial.getReleaseQty()) ? BigDecimal.ZERO : updateHmeEoJobTimeMaterial.getReleaseQty()).add(dto.getBackQty()));
//                        hmeEoJobTimeMaterialMapper.updateByPrimaryKey(updateHmeEoJobTimeMaterial);
//                    }
//                }
//                HmeEoJobTimeMaterial hmeEoJobTimeMaterial = hmeEoJobTimeMaterialMapper.selectByPrimaryKey(existsHmeEoJobSnLotMaterial.getTimeMaterialId());
//                if(Objects.nonNull(hmeEoJobTimeMaterial)){
//                    hmeEoJobTimeMaterial.setCostQty(hmeEoJobTimeMaterial.getCostQty().subtract(dto.getBackQty()));
//                    hmeEoJobTimeMaterialMapper.updateByPrimaryKey(hmeEoJobTimeMaterial);
//                }
//            }
            //?????????????????????release_qty
            existsHmeEoJobSnLotMaterial.setReleaseQty((Objects.isNull(existsHmeEoJobSnLotMaterial.getReleaseQty()) ? BigDecimal.ZERO : existsHmeEoJobSnLotMaterial.getReleaseQty()).subtract(dto.getBackQty()));
            if (Objects.nonNull(existsHmeEoJobSnLotMaterial.getRemainQty())) {
                existsHmeEoJobSnLotMaterial.setRemainQty(existsHmeEoJobSnLotMaterial.getRemainQty().subtract(dto.getBackQty()));
            }
            hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(existsHmeEoJobSnLotMaterial);
        }

        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
        String assembleExcessFlag = YES;
        MtBomComponent woBomComponent = null;
        Long userId = -1L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        MtWkcShift mtWkcShift = null;
        if (StringUtils.isBlank(dto.getShiftId())) {
            mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, dto.getShiftId());
        }
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            //EO????????????
            MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = new MtAssembleProcessActualVO5();
            mtAssembleProcessActualVO5.setEoId(dto.getEoId());
            mtAssembleProcessActualVO5.setMaterialId(dto.getMaterialId());
            mtAssembleProcessActualVO5.setOperationId(dto.getOperationId());
            mtAssembleProcessActualVO5.setRouterStepId(dto.getEoStepId());
            mtAssembleProcessActualVO5.setLocatorId(mtMaterialLot.getLocatorId());
            mtAssembleProcessActualVO5.setOperationBy(userId);
            mtAssembleProcessActualVO5.setWorkcellId(dto.getWorkcellId());
            mtAssembleProcessActualVO5.setParentEventId(eventId);
            mtAssembleProcessActualVO5.setEventRequestId(eventRequestId);
            if (Objects.nonNull(mtWkcShift)) {
                mtAssembleProcessActualVO5.setShiftCode(mtWkcShift.getShiftCode());
                mtAssembleProcessActualVO5.setShiftDate(mtWkcShift.getShiftDate());
            }
            mtAssembleProcessActualVO5.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            String bomComponentId = "";
            if (CollectionUtils.isNotEmpty(eoComponentList)) {
                // ?????????ID
                Optional<MtEoVO20> componentOptional = eoComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(dto.getMaterialId())).findFirst();
                if (componentOptional.isPresent()) {
                    MtEoVO20 component = componentOptional.get();
                    bomComponentId = component.getBomComponentId();
                    mtAssembleProcessActualVO5.setBomComponentId(component.getBomComponentId());
                    MtBomComponent mtBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, component.getBomComponentId());
                    if (Objects.isNull(mtBomComponent)) {
                        //${1}????????? ?????????${2}
                        throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "BOM", component.getBomComponentId()));
                    }
                    objectTransactionVO.setBomReserveLineNum(String.valueOf(mtBomComponent.getLineNumber()));
                    if(MapUtils.isNotEmpty(bomComponentExtendMap)){
                        MtExtendAttrVO1 bomMtExtendAttr = bomComponentExtendMap.getOrDefault(fetchGroupKey2(component.getBomComponentId(),HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM),null);
                        if(Objects.nonNull(bomMtExtendAttr)){
                            objectTransactionVO.setBomReserveNum(bomMtExtendAttr.getAttrValue());
                        }
                    }
                }
            }

            //?????????????????????
            BigDecimal canOutFeedQty = BigDecimal.ZERO;
            //?????????????????????
            BigDecimal canInFeedQty = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(woComponentList)) {
                // ?????????ID
                Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(dto.getMaterialId())).findFirst();
                if (componentOptional.isPresent()) {
                    MtWorkOrderVO8 component = componentOptional.get();
                    woBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, component.getBomComponentId());
                    assembleExcessFlag = HmeConstants.ConstantValue.NO;
                }
            }

            if (HmeConstants.ConstantValue.NO.equals(assembleExcessFlag)) {
                canInFeedQty = dto.getBackQty();
            } else {
                canOutFeedQty = dto.getBackQty();
            }

            //modify by penglin.sui for lu.bai
            //???????????????????????????????????????????????????????????? + ???????????? ??? ?????????????????????????????????????????????????????????????????????????????????
            if (HmeConstants.ConstantValue.NO.equals(assembleExcessFlag)) {
                //V20201103 modify by penglin.sui for lu.bai ????????????????????????????????????ID
                String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
                String routerStepId = "";
                if (CollectionUtils.isNotEmpty(mtRouterStepList2)) {
                    routerStepId = mtRouterStepList2.get(0).getRouterStepId();
                }
                if ((StringUtils.isEmpty(flagGet) || NO.equals(flagGet))) {
                    routerStepId = "";
                }

                String woBomComponentId = woBomComponent.getBomComponentId();
                List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(woBomComponentId)).collect(Collectors.toList());
                BigDecimal woSumQty = BigDecimal.ZERO;
                if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                    if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                        woSumQty = new BigDecimal(mtExtendAttrVO1List2.get(0).getAttrValue());
                    }
                }
                if (woSumQty.compareTo(BigDecimal.ZERO) <= 0) {
                    //???????????????????????????
                    canOutFeedQty = dto.getBackQty();
                    canInFeedQty = BigDecimal.ZERO;
                } else {
                    List<MtWorkOrderComponentActual> mtWorkOrderComponentActualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class)
                            .andWhere(Sqls.custom()
                                    .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                                    .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, woBomComponent.getBomComponentId())
                                    .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerStepId))
                            .build());
                    BigDecimal assembleQty = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(mtWorkOrderComponentActualList)) {
                        MtWorkOrderComponentActual mtWorkOrderComponentActual = mtWorkOrderComponentActualList.get(0);
                        assembleQty = BigDecimal.valueOf(mtWorkOrderComponentActual.getAssembleQty());
                    }
                    if (assembleQty.compareTo(woSumQty) > 0) {
                        //???????????????????????????
                        canOutFeedQty = assembleQty.subtract(woSumQty);
                        canOutFeedQty = canOutFeedQty.compareTo(dto.getBackQty()) > 0 ? dto.getBackQty() : canOutFeedQty;
                        if (canOutFeedQty.compareTo(dto.getBackQty()) < 0) {
                            canInFeedQty = dto.getBackQty().subtract(canOutFeedQty);
                        } else {
                            canInFeedQty = BigDecimal.ZERO;
                        }
                    }
                }
            }

            //????????????
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
            objectTransactionVO.setEventId(eventId);
            objectTransactionVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            objectTransactionVO.setMaterialId(mtMaterial.getMaterialId());
            objectTransactionVO.setMaterialCode(mtMaterial.getMaterialCode());
            objectTransactionVO.setLotNumber(mtMaterialLot.getLot());
            if (!StringUtils.isBlank(mtMaterial.getPrimaryUomId())) {
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                if (Objects.nonNull(mtUom)) {
                    objectTransactionVO.setTransactionUom(mtUom.getUomCode());
                }
            }
            objectTransactionVO.setTransactionTime(new Date());
            objectTransactionVO.setPlantId(mtMaterialLot.getSiteId());
            MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(mtMaterialLot.getSiteId());
            if (Objects.nonNull(mtModSite)) {
                objectTransactionVO.setPlantCode(mtModSite.getSiteCode());
            }
            if (!StringUtils.isBlank(mtMaterialLot.getLocatorId())) {
                objectTransactionVO.setLocatorId(mtMaterialLot.getLocatorId());
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                if (Objects.nonNull(mtModLocator)) {
                    objectTransactionVO.setLocatorCode(mtModLocator.getLocatorCode());
                }

                MtModLocator mtModLocator1 = hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId, mtMaterialLot.getLocatorId());
                if (Objects.nonNull(mtModLocator1)) {
                    objectTransactionVO.setWarehouseId(mtModLocator1.getLocatorId());
                    objectTransactionVO.setWarehouseCode(mtModLocator1.getLocatorCode());
                }
            }
            objectTransactionVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
            objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
            objectTransactionVO.setProdLineCode(mtModProductionLine.getProdLineCode());

            if(StringUtils.isNotBlank(bomComponentId)) {
                String specialInvFlag = "";
                if (MapUtils.isNotEmpty(bomComponentExtendMap)) {
                    MtExtendAttrVO1 bomMtExtendAttr = bomComponentExtendMap.getOrDefault(fetchGroupKey2(bomComponentId, HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG), null);
                    if (Objects.nonNull(bomMtExtendAttr)) {
                        specialInvFlag = bomMtExtendAttr.getAttrValue();
                    }
                }
                if ("E".equals(specialInvFlag)) {
                    //???????????????????????????
                    MtExtendSettings soNumAttr = new MtExtendSettings();
                    soNumAttr.setAttrName("SO_NUM");
                    List<MtExtendAttrVO> soNumAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_material_lot_attr", "MATERIAL_LOT_ID", mtMaterialLot.getMaterialLotId(),
                            Collections.singletonList(soNumAttr));
                    if (CollectionUtils.isNotEmpty(soNumAttrVOList)) {
                        objectTransactionVO.setSoNum(soNumAttrVOList.get(0).getAttrValue());
                    }

                    //???????????????????????????
                    MtExtendSettings soLineNumAttr = new MtExtendSettings();
                    soLineNumAttr.setAttrName("SO_LINE_NUM");
                    List<MtExtendAttrVO> soLineNumAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_material_lot_attr", "MATERIAL_LOT_ID", mtMaterialLot.getMaterialLotId(),
                            Collections.singletonList(soLineNumAttr));
                    if (CollectionUtils.isNotEmpty(soLineNumAttrVOList)) {
                        objectTransactionVO.setSoLineNum(soLineNumAttrVOList.get(0).getAttrValue());
                    }
                }
            }
            //?????????
            if (canOutFeedQty.compareTo(BigDecimal.ZERO) > 0) {
                mtAssembleProcessActualVO5.setTrxAssembleQty(canOutFeedQty.doubleValue());
                mtAssembleProcessActualVO5.setAssembleExcessFlag(YES);
                mtAssembleProcessActualRepositoryImpl.componentAssembleCancelProcess(tenantId, mtAssembleProcessActualVO5);

                WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
                wmsTransactionTypePara.setTenantId(tenantId);
                wmsTransactionTypePara.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
                WmsTransactionType wmsTransactionType = transactionTypeRepository.selectOne(wmsTransactionTypePara);
                objectTransactionVO.setMoveType(Objects.isNull(wmsTransactionType) ? "262" : wmsTransactionType.getMoveType());

                objectTransactionVO.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
                objectTransactionVO.setTransactionQty(canOutFeedQty);
                objectTransactionVO.setAttribute16(batchId);
                objectTransactionRequestList.add(objectTransactionVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
            }
            //?????????
            if (canInFeedQty.compareTo(BigDecimal.ZERO) > 0) {
                mtAssembleProcessActualVO5.setTrxAssembleQty(canInFeedQty.doubleValue());
                mtAssembleProcessActualVO5.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                mtAssembleProcessActualRepositoryImpl.componentAssembleCancelProcess(tenantId, mtAssembleProcessActualVO5);

                objectTransactionRequestList.clear();

                WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
                wmsTransactionTypePara.setTenantId(tenantId);
                wmsTransactionTypePara.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R);
                WmsTransactionType wmsTransactionType = transactionTypeRepository.selectOne(wmsTransactionTypePara);
                objectTransactionVO.setMoveType(Objects.isNull(wmsTransactionType) ? "262" : wmsTransactionType.getMoveType());
                objectTransactionVO.setAttribute16(batchId);
                objectTransactionVO.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R);
                objectTransactionVO.setTransactionQty(canInFeedQty);
                objectTransactionRequestList.add(objectTransactionVO);
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
            }
            //???????????????,???????????? COS???????????????????????????????????? add by yuchao.wang for jiao.chen at 2020.10.5
            if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType())
                    && HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType())) {
                List<LovValueDTO> poTypeLov = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
                if (CollectionUtils.isNotEmpty(poTypeLov)) {
                    List<String> itemGroupList = poTypeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                    String itemGroup = hmeEoJobLotMaterialMapper.queryCosMaterialItemGroup(tenantId, dto.getMaterialId(), mtMaterialLot.getSiteId());
                    if (CollectionUtils.isNotEmpty(itemGroupList) && itemGroupList.contains(itemGroup)) {
                        hmeVirtualNumRepository.updateVirtualNumForReleaseBack(tenantId, dto.getEoId());
                    }
                }
            }
        }
    }

    /**
     * ?????????????????????
     *
     * @param tenantId ??????
     * @param dto      ??????
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/19 04:08:35
     */
    private void upgradeMaterialRevert(Long tenantId, HmeEoJobSnVO9 dto) {
        // ???????????????????????????????????????
        HmeUpgradeMaterialVO backMaterial = hmeEoJobSnMapper.selectMaterialUpgradeInfo(tenantId, dto.getMaterialCode());
        if (YES.equals(backMaterial.getUpgradeFlag())) {
            // ?????????????????????EO_ID?????????EO???identification????????????????????????
            MtEo eo = eoRepository.selectByPrimaryKey(dto.getEoId());
            MtMaterialLot backMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(dto.getBackMaterialLotCode());
            }});
            if (StringCommonUtils.equalsIgnoreBlank(eo.getIdentification(), backMaterialLot.getMaterialLotCode())) {
                throw new CommonException("??????EO ??? identification ???????????????????????????");
            }
            //????????????ID
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("EO_SN_REDUCTION");
            }});
            MtMaterial releaseMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
            // ???????????????????????????????????????????????????
            MtMaterialLotVO2 update = new MtMaterialLotVO2();
            update.setTrxPrimaryUomQty(backMaterialLot.getPrimaryUomQty() * (-1));
            update.setMaterialLotId(backMaterialLot.getMaterialLotId());
            update.setMaterialId(dto.getMaterialId());
            update.setPrimaryUomId(releaseMaterial.getPrimaryUomId());
            update.setEventId(eventId);
            update.setSiteId(dto.getSiteId());
            update.setLocatorId(dto.getLocatorId());
            update.setQualityStatus(OK);
            update.setLoadTime(null);
            update.setInSiteTime(null);
            update.setEoId("");
            mtMaterialLotRepository.materialLotUpdate(tenantId, update, NO);

            // ??????????????????
            MtExtendVO10 updateAttr = new MtExtendVO10();
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 mfFlag = new MtExtendVO5();
            mfFlag.setAttrName("MF_FLAG");
            mfFlag.setAttrValue("");
            attrs.add(mfFlag);
            MtExtendVO5 reworkFlag = new MtExtendVO5();
            reworkFlag.setAttrName("REWORK_FLAG");
            reworkFlag.setAttrValue("");
            attrs.add(reworkFlag);
            MtExtendVO5 performanceLevel = new MtExtendVO5();
            performanceLevel.setAttrName("PERFORMANCE_LEVEL");
            performanceLevel.setAttrValue("");
            attrs.add(performanceLevel);
            updateAttr.setEventId(eventId);
            updateAttr.setKeyId(backMaterialLot.getMaterialLotId());
            updateAttr.setAttrs(attrs);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, updateAttr);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobSnVO9> releaseBack(Long tenantId, HmeEoJobSnVO9 dto) {
        List<HmeEoJobSnVO9> hmeEoJobSnVO9List = new ArrayList<HmeEoJobSnVO9>();
        // ????????????????????????????????????
        this.upgradeMaterialRevert(tenantId, dto);

        if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())) {
            //?????????
            virtualReleaseBack(tenantId, dto);
        } else {
            //????????????
            normalReleaseBack(tenantId, dto);
        }

        //????????????????????????????????????
        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setWorkcellId(dto.getWorkcellId());
        hmeEoJobSnVO3.setMaterialLotId(dto.getSourceMaterialLotId());
        hmeEoJobSnVO3.setMaterialType(dto.getMaterialType());
        hmeEoJobSnVO3.setSnNum(dto.getSourceMaterialLotCode());
        hmeEoJobSnVO9List = self().releaseBackQuery(tenantId, hmeEoJobSnVO3);
        return hmeEoJobSnVO9List;
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @param eoJobSns eoJobSns
     * @return void
     * @Description ????????????????????????-??????????????????/????????????????????????/????????????????????????/PDA??????????????????
     * @author yuchao.wang
     * @date 2020/9/21 10:59
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wkcCompleteOutputRecord(Long tenantId, HmeEoJobSnVO3 dto, List<HmeEoJobSn> eoJobSns) {
        if(YES.equals(dto.getReworkFlag()))
        {
           return;
        }
        //???????????????????????????
        List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoJobSns)) {
            // UPDATE 20201021 YC ????????????
            // ???????????????????????????
            Map<String, HmeEoJobSnMaterialLotAttrVO> materialLotAttrVOMap = hmeEoJobSnLotMaterialRepository
                    .getMaterialLotAttrBatch(tenantId,
                            eoJobSns.stream().map(HmeEoJobSn::getMaterialLotId).distinct()
                                    .collect(Collectors.toList()))
                    .stream().collect(Collectors.toMap(HmeEoJobSnMaterialLotAttrVO::getMaterialLotId, t -> t));

            for (HmeEoJobSn eoJobSn : eoJobSns) {
                HmeEoJobSnMaterialLotAttrVO hmeEoJobSnMaterialLotAttrVO = materialLotAttrVOMap.get(eoJobSn.getMaterialLotId());
                if (hmeEoJobSnMaterialLotAttrVO != null
                        && YES.equals(hmeEoJobSnMaterialLotAttrVO.getReworkFlag())) {
                    continue;
                }
                /*MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(eoJobSn.getMaterialLotId());
                mtMaterialLotAttrVO2.setAttrName("REWORK_FLAG");
                List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && !Objects.isNull(mtExtendAttrVOS.get(0))
                        && HmeConstants.ConstantValue.YES.equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                    continue;
                }*/
                hmeEoJobSnList.add(eoJobSn);
            }
        }
        if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
            return;
        }

        //??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        //????????????????????????
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(userId);
        userOrganization.setOrganizationType("SITE");
        MtUserOrganization defaultSite = userOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
        if (Objects.isNull(defaultSite) || StringUtils.isBlank(defaultSite.getOrganizationId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
        }
        String defaultSiteId = defaultSite.getOrganizationId();

        // UPDATE 20201021 YC ????????????
        /*// ????????????
        MtModOrganizationVO2 processParam = new MtModOrganizationVO2();
        processParam.setTopSiteId(defaultSiteId);
        processParam.setOrganizationId(dto.getWorkcellId());
        processParam.setOrganizationType("WORKCELL");
        processParam.setParentOrganizationType("WORKCELL");
        processParam.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> processList = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, processParam);
        if (CollectionUtils.isEmpty(processList) || Objects.isNull(processList.get(0))) {
            //????????????Wkc??????????????????
            throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_002", "HME"));
        }*/

        //????????????
        MtModOrganizationVO2 lineParam = new MtModOrganizationVO2();
        lineParam.setTopSiteId(defaultSiteId);
        // lineParam.setOrganizationId(processList.get(0).getOrganizationId());
        lineParam.setOrganizationId(dto.getWorkcellId());
        lineParam.setOrganizationType("WORKCELL");
        lineParam.setParentOrganizationType("WORKCELL");
        lineParam.setQueryType("TOP");
        // lineParam.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> lineVOList = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, lineParam);
        if (CollectionUtils.isEmpty(lineVOList) || Objects.isNull(lineVOList.get(0))) {
            // ????????????Wkc??????????????????
            throw new MtException("HME_EO_JOB_SN_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_023", "HME"));
        }
        String workcellId = lineVOList.get(0).getOrganizationId();

        // UPDATE 20201021 YC ????????????
        List<String> eoIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getEoId)
                .distinct().collect(Collectors.toList());
        Map<String, MtEo> mtEoMap = mtEoRepository
                .eoPropertyBatchGet(tenantId, eoIdList)
                .stream().collect(Collectors.toMap(MtEo::getEoId, t -> t));

        Map<HmeEoJobSnRecordVO, HmeWkcCompleteOutputRecord> hmeWkcCompleteOutputRecordMap =
                hmeWkcCompleteOutputRecordRepository.selectByCondition(Condition
                        .builder(HmeWkcCompleteOutputRecord.class)
                        .andWhere(Sqls.custom()
                                .andEqualTo(HmeWkcCompleteOutputRecord.FIELD_TENANT_ID,
                                        tenantId)
                                .andIn(HmeWkcCompleteOutputRecord.FIELD_WKC_SHIFT_ID,
                                        hmeEoJobSnList.stream()
                                                .map(HmeEoJobSn::getShiftId)
                                                .distinct()
                                                .collect(Collectors.toList())))
                        .build()).stream()
                        .collect(Collectors.toMap(t -> new HmeEoJobSnRecordVO(t.getWorkOrderId(),
                                        t.getWkcShiftId(), t.getMaterialId(), t.getWorkcellId()),
                                t -> t));

        //????????????????????????????????????????????????????????? modify by yuchao.wang at 2020.10.23
        //??????/????????????????????????????????????????????????????????????????????? modify by yuchao.wang at 2020.11.30
        //Map<String, List<HmeEoJobSn>> workcellIdMap = new HashMap<String, List<HmeEoJobSn>>();
        //List<HmeEoJobSn> allWorkcellIdList = hmeEoJobSnMapper.batchQueryUnfinishedSection(tenantId, eoIdList, defaultSiteId);
        //if (CollectionUtils.isNotEmpty(allWorkcellIdList)) {
        //    workcellIdMap = allWorkcellIdList.stream().collect(Collectors.groupingBy(HmeEoJobSn::getEoId));
        //}

        List<HmeWkcCompleteOutputRecord> insertDataList = new ArrayList<>(hmeEoJobSnList.size());
        List<HmeWkcCompleteOutputRecord> updateDataList = new ArrayList<>(hmeEoJobSnList.size());
        Map<String, Long> updateQtyMap = new HashMap<String, Long>();
        for (HmeEoJobSn eoJobSn : hmeEoJobSnList) {
            //??????EO
            MtEo mtEo = mtEoMap.get(eoJobSn.getEoId());
            if (Objects.isNull(mtEo) || StringUtils.isBlank(mtEo.getEoId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "EO??????"));
            }

            //???????????????????????????????????????????????????
            List<HmeEoJobSnDTO3> hmeEoJobSnDTO3s = hmeEoJobSnMapper.queryUnfinishedSection(tenantId, mtEo.getEoId(), defaultSiteId);
            //List<String> workcellIdList = new ArrayList<>();
            //if (workcellIdMap.containsKey(mtEo.getEoId())
            //        && CollectionUtils.isNotEmpty(workcellIdMap.get(mtEo.getEoId()))) {
            //    workcellIdList = workcellIdMap.get(mtEo.getEoId()).stream()
            //            .map(HmeEoJobSn::getWorkcellId).distinct().collect(Collectors.toList());
            //}

            //?????????????????????????????????
            if(CollectionUtils.isNotEmpty(hmeEoJobSnDTO3s)) {
                List<String> workcellIdList = hmeEoJobSnDTO3s.stream()
                        .filter(t -> !YES.equals(t.getPrepareFlag()))
                        .map(HmeEoJobSnDTO3::getWorkcellId)
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(workcellIdList) && workcellIdList.contains(workcellId)) {
                    continue;
                }
            }

            HmeWkcCompleteOutputRecord wkcCompleteOutputRecord =
                    hmeWkcCompleteOutputRecordMap.get(new HmeEoJobSnRecordVO(mtEo.getWorkOrderId(),
                            eoJobSn.getShiftId(), mtEo.getMaterialId(), workcellId));
            if (Objects.isNull(wkcCompleteOutputRecord) || StringUtils.isBlank(wkcCompleteOutputRecord.getWkcOutputRecordId())) {
                wkcCompleteOutputRecord = new HmeWkcCompleteOutputRecord();
                wkcCompleteOutputRecord.setTenantId(tenantId);
                wkcCompleteOutputRecord.setSiteId(defaultSiteId);
                wkcCompleteOutputRecord.setWorkOrderId(mtEo.getWorkOrderId());
                wkcCompleteOutputRecord.setWkcShiftId(eoJobSn.getShiftId());
                wkcCompleteOutputRecord.setMaterialId(mtEo.getMaterialId());
                wkcCompleteOutputRecord.setWorkcellId(workcellId);
                wkcCompleteOutputRecord.setQty(Objects.isNull(mtEo.getQty()) ? 0L : mtEo.getQty().longValue());
                insertDataList.add(wkcCompleteOutputRecord);
                // hmeWkcCompleteOutputRecordRepository.insertSelective(queryParam);
            } else {
                //???????????????????????????,?????????????????????update??????
                if (updateQtyMap.containsKey(wkcCompleteOutputRecord.getWkcOutputRecordId())) {
                    Long qty = updateQtyMap.get(wkcCompleteOutputRecord.getWkcOutputRecordId())
                            + (Objects.isNull(mtEo.getQty()) ? 0L : mtEo.getQty().longValue());

                    updateQtyMap.put(wkcCompleteOutputRecord.getWkcOutputRecordId(), qty);
                } else {
                    updateQtyMap.put(wkcCompleteOutputRecord.getWkcOutputRecordId(),
                            Objects.isNull(mtEo.getQty()) ? 0L : mtEo.getQty().longValue());

                    HmeWkcCompleteOutputRecord update = new HmeWkcCompleteOutputRecord();
                    update.setTenantId(tenantId);
                    update.setWkcOutputRecordId(wkcCompleteOutputRecord.getWkcOutputRecordId());
                    update.setQty(Objects.isNull(wkcCompleteOutputRecord.getQty()) ? 0L : wkcCompleteOutputRecord.getQty());
                    updateDataList.add(update);
                    // hmeWkcCompleteOutputRecordMapper.updateByPrimaryKeySelective(update);
                }
            }
        }

        // ???????????????????????????
        List<String> sqlList = new ArrayList<>();
        Date date = new Date();
        if (CollectionUtils.isNotEmpty(insertDataList)) {
            //????????????????????????????????????
            Map<String, List<HmeWkcCompleteOutputRecord>> groupMap = insertDataList.stream()
                    .collect(Collectors.groupingBy(item -> item.getTenantId() + "-" + item.getWorkOrderId()
                            + "-" + item.getWorkcellId() + "-" + item.getWkcShiftId() + "-" + item.getMaterialId()));

            List<String> idS = customDbRepository.getNextKeys("hme_wkc_complete_output_record_s", groupMap.size());
            List<String> cidS = customDbRepository.getNextKeys("hme_wkc_complete_output_record_cid_s", groupMap.size());
            int count = 0;
            for (Map.Entry<String, List<HmeWkcCompleteOutputRecord>> entry : groupMap.entrySet()) {
                if (CollectionUtils.isEmpty(entry.getValue()) || Objects.isNull(entry.getValue().get(0))) {
                    continue;
                }

                HmeWkcCompleteOutputRecord insertData = entry.getValue().get(0);
                insertData.setQty(entry.getValue().stream().mapToLong(HmeWkcCompleteOutputRecord::getQty).sum());
                insertData.setWkcOutputRecordId(idS.get(count));
                insertData.setCid(Long.valueOf(cidS.get(count)));
                insertData.setCreatedBy(userId);
                insertData.setCreationDate(date);
                insertData.setLastUpdatedBy(userId);
                insertData.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getInsertSql(insertData));
                count++;
            }
        }

        if (CollectionUtils.isNotEmpty(updateDataList)) {
            List<String> cidS = customDbRepository.getNextKeys("hme_wkc_complete_output_record_cid_s",
                    updateDataList.size());
            int count = 0;
            for (HmeWkcCompleteOutputRecord updateData : updateDataList) {
                updateData.setQty(updateData.getQty() + updateQtyMap.get(updateData.getWkcOutputRecordId()));
                updateData.setCid(Long.valueOf(cidS.get(count)));
                updateData.setLastUpdatedBy(userId);
                updateData.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getUpdateSql(updateData));
                count++;
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    /**
     * @param tenantId      ??????ID
     * @param materialLotId ?????????ID
     * @return com.ruike.hme.domain.entity.HmeEoJobSn
     * @Description ???????????????ID??????????????????????????????EoJobSn???????????????NULL
     * @author yuchao.wang
     * @date 2020/9/27 14:06
     */
    @Override
    public HmeEoJobSn queryLastEoJobSnByMaterialLotId(Long tenantId, String materialLotId) {
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.queryEoJobSnByMaterialLotId(tenantId, materialLotId);
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList) && Objects.nonNull(hmeEoJobSnList.get(0))
                && StringUtils.isNotBlank(hmeEoJobSnList.get(0).getJobId())) {
            return hmeEoJobSnList.get(0);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void snUpgrade(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobMaterial hmeEoJobMaterial) {
        log.debug("????????????????????????");
        List<String> upgradeSnList = new ArrayList<>();
        if (StringUtils.isBlank(hmeEoJobMaterial.getMaterialLotCode())) {
            return;
        }
        MtMaterialSite mtMaterialSite = new MtMaterialSite();
        mtMaterialSite.setSiteId(dto.getSiteId());
        mtMaterialSite.setMaterialId(hmeEoJobMaterial.getMaterialId());
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId, mtMaterialSite);
        MtExtendSettings attribute17ExtendAttr = new MtExtendSettings();
        attribute17ExtendAttr.setAttrName("attribute17");
        List<MtExtendAttrVO> upgradeAttr = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_site_attr", "MATERIAL_SITE_ID", materialSiteId,
                Collections.singletonList(attribute17ExtendAttr));
        if (CollectionUtils.isNotEmpty(upgradeAttr)) {
            // SN????????????
            if (YES.equals(upgradeAttr.get(0).getAttrValue())) {
                if (CollectionUtils.isNotEmpty(upgradeSnList)) {
                    MtRouterStep currentRouterStep =
                            mtRouterStepRepository.routerStepGet(tenantId, dto.getEoStepId());
                    MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,
                            hmeEoJobMaterial.getMaterialId());
                    // ?????????????????????${1}???SN??????????????????????????????????????????1???,??????????????????${2}??????????????????
                    throw new MtException("HME_EO_JOB_SN_027",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_027", "HME", currentRouterStep.getStepName(),
                                    mtMaterial.getMaterialName()));
                }
                upgradeSnList.add(hmeEoJobMaterial.getJobMaterialId());
            }
        }

        if (CollectionUtils.isNotEmpty(upgradeSnList)) {
            log.debug("????????????????????????");
            MtMaterialLot currentSn =
                    mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
            final Date currentDate = new Date(System.currentTimeMillis());

            String upgradeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setLocatorId(currentSn.getLocatorId());
                    setEventTypeCode("EO_MATERIAL_LOT_UPGRADE");
                }
            });

            // ?????????????????????????????????N
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                {
                    setEventId(upgradeEventId);
                    setMaterialLotId(dto.getMaterialLotId());
                    setEnableFlag(HmeConstants.ConstantValue.NO);
                }
            }, "N");

            MtMaterialLotVO2 materialLotVO = new MtMaterialLotVO2();
            materialLotVO.setTenantId(tenantId);
            materialLotVO.setEventId(upgradeEventId);
            materialLotVO.setSiteId(dto.getSiteId());
            materialLotVO.setEnableFlag(YES);
            materialLotVO.setQualityStatus(HmeConstants.ConstantValue.OK);
            materialLotVO.setMaterialLotCode(hmeEoJobMaterial.getMaterialLotCode());
            //V20200826 modify by penglin.sui for tianyang.xie ?????????????????????????????????
            materialLotVO.setMaterialId(currentSn.getMaterialId());
            //materialLotVO.setMaterialId(dto.getSnMaterialId());
            materialLotVO.setPrimaryUomId(currentSn.getPrimaryUomId());
            materialLotVO.setPrimaryUomQty(new Double(String.valueOf(hmeEoJobMaterial.getReleaseQty())));
            materialLotVO.setLocatorId(currentSn.getLocatorId());
            materialLotVO.setEoId(dto.getEoId());
            materialLotVO.setLoadTime(currentDate);
            materialLotVO.setInSiteTime(currentDate);
            materialLotVO.setCreateReason("INITIALIZE");
            materialLotVO.setCid(Long.valueOf(customSequence.getNextKey("mt_material_lot_cid_s")));
            MtMaterialLotVO13 materialLotResult = mtMaterialLotRepository.materialLotUpdate(tenantId,
                    materialLotVO, HmeConstants.ConstantValue.NO);

            // ??????????????????-??????????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(materialLotResult.getMaterialLotId());
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName(HmeConstants.ExtendAttr.STATUS);
            MtExtendVO5 mfFlagAttr = new MtExtendVO5();
            mfFlagAttr.setAttrName("MF_FLAG");
            MtExtendVO5 reworkFlagAttr = new MtExtendVO5();
            reworkFlagAttr.setAttrName("REWORK_FLAG");
            MtExtendVO5 pfmLevelAttr = new MtExtendVO5();
            pfmLevelAttr.setAttrName("PERFORMANCE_LEVEL");

            List<MtExtendAttrVO1> mtExtendAttrVO1s = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_material_lot_attr", Collections.singletonList(dto.getMaterialLotId()), "MF_FLAG", "REWORK_FLAG", "PERFORMANCE_LEVEL"));
            for (MtExtendAttrVO1 extendAttr : mtExtendAttrVO1s) {
                // ??????
                if (HmeConstants.ExtendAttr.STATUS.equals(extendAttr.getAttrName())) {
                    statusAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ???????????????
                if ("MF_FLAG".equals(extendAttr.getAttrName())) {
                    mfFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ????????????
                if ("REWORK_FLAG".equals(extendAttr.getAttrName())) {
                    reworkFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ????????????
                if ("PERFORMANCE_LEVEL".equals(extendAttr.getAttrName())) {
                    pfmLevelAttr.setAttrValue(extendAttr.getAttrValue());
                }
            }

            List<MtExtendVO5> list = new ArrayList<>(16);
            list.add(statusAttr);
            list.add(mfFlagAttr);
            list.add(reworkFlagAttr);
            list.add(pfmLevelAttr);
            mtExtendVO10.setAttrs(list);
            // ???????????????????????????
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

            // eo??????
            String eoUpdateEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setLocatorId(currentSn.getLocatorId());
                    setEventTypeCode("EO_UPDATE");
                }
            });

            MtEoVO mtEoVO = new MtEoVO();

            mtEoVO.setEventId(eoUpdateEventId);
            mtEoVO.setTenantId(tenantId);
            mtEoVO.setEoId(dto.getEoId());
            mtEoVO.setWorkOrderId(dto.getWorkOrderId());
            mtEoVO.setIdentification(hmeEoJobMaterial.getMaterialLotCode());
            mtEoRepository.eoUpdate(tenantId, mtEoVO, HmeConstants.ConstantValue.NO);

            // ??????????????????????????????
            dto.setSnNum(hmeEoJobMaterial.getMaterialLotCode());
            // ????????????????????????????????????????????????
            dto.setMaterialLotId(materialLotResult.getMaterialLotId());

            //V20201005 modify by penglin.sui for for lu.bai ??????hme_eo_job_sn ???material_lot_id
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnMapper.selectByPrimaryKey(dto.getJobId());
            if (Objects.isNull(hmeEoJobSn)) {
                //${1}????????? ?????????${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "eoJobSn", dto.getJobId()));
            }
            hmeEoJobSn.setMaterialLotId(materialLotResult.getMaterialLotId());
            hmeEoJobSnMapper.updateByPrimaryKey(hmeEoJobSn);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void snBatchUpgrade(Long tenantId, HmeEoJobSnVO20 dto) {
        log.debug("<==============????????????begin==============>");

        if(Objects.isNull(dto)){
            return;
        }

        if(CollectionUtils.isEmpty(dto.getMaterialLotList())){
            return;
        }

        //????????????????????????
        List<MtMaterialSiteVO3> materialSiteIds = new ArrayList<>();
        dto.getMaterialLotList().forEach(item -> {
            MtMaterialSiteVO3 mtMaterialSiteVO3 = new MtMaterialSiteVO3();
            mtMaterialSiteVO3.setSiteId(dto.getSiteId());
            mtMaterialSiteVO3.setMaterialId(item.getMaterialId());
            materialSiteIds.add(mtMaterialSiteVO3);
        });
        List<MtMaterialSiteVO4> materialSiteList = mtMaterialSiteRepository.materialSiteLimitRelationBatchGet(tenantId,materialSiteIds);
        if(CollectionUtils.isEmpty(materialSiteList) ||
                (CollectionUtils.isNotEmpty(materialSiteList) && materialSiteList.size() != materialSiteIds.size())){
            //????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_033", "HME"));
        }
        Map<String,String> materialSiteMap = materialSiteList.stream().collect(Collectors.toMap(MtMaterialSiteVO4::getMaterialSiteId,MtMaterialSiteVO4::getMaterialId));
        List<String> materialSiteIdList = materialSiteList.stream().map(MtMaterialSiteVO4::getMaterialSiteId).collect(Collectors.toList());
        List<String> attrNamelist = new ArrayList<>();
        attrNamelist.add("attribute17");
        //????????????????????????
        List<MtExtendAttrVO1> suUpGradeFlagList = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_material_site_attr","MATERIAL_SITE_ID",materialSiteIdList,attrNamelist);
        boolean isUpGrade = false;
        HmeEoJobSnVO21 upGradeMaterialLot = new HmeEoJobSnVO21();
        if (CollectionUtils.isNotEmpty(suUpGradeFlagList)) {
            List<MtExtendAttrVO1> suUpGradeFlagList2 = suUpGradeFlagList.stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getAttrValue()))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(suUpGradeFlagList2)){
                if(suUpGradeFlagList2.size() > 1) {
                    MtRouterStep currentRouterStep =
                            mtRouterStepRepository.routerStepGet(tenantId, dto.getEoStepId());
                    MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,
                            materialSiteMap.get(suUpGradeFlagList2.get(1).getKeyId()));
                    // ?????????????????????${1}???SN??????????????????????????????????????????1???,??????????????????${2}??????????????????
                    throw new MtException("HME_EO_JOB_SN_027",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_027", "HME", currentRouterStep.getStepName(),
                                    mtMaterial.getMaterialName()));
                }
                List<HmeEoJobSnVO21> upGradeMaterialLotList = dto.getMaterialLotList().stream().filter(item -> materialSiteMap.get(suUpGradeFlagList2.get(0).getKeyId()).equals(item.getMaterialId()))
                        .collect(Collectors.toList());
                upGradeMaterialLot = upGradeMaterialLotList.get(0);
                isUpGrade = true;
            }
        }

        if (isUpGrade) {
            log.debug("????????????????????????");
            MtMaterialLot currentSn =
                    mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getSnMaterialLotId());
            final Date currentDate = CommonUtils.currentTimeGet();

            String upgradeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setLocatorId(currentSn.getLocatorId());
                    setEventTypeCode("EO_MATERIAL_LOT_UPGRADE");
                }
            });

            // ?????????????????????????????????N
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                {
                    setEventId(upgradeEventId);
                    setMaterialLotId(dto.getSnMaterialLotId());
                    setEnableFlag(HmeConstants.ConstantValue.NO);
                }
            }, "N");

            MtMaterialLotVO2 materialLotVO = new MtMaterialLotVO2();
            materialLotVO.setTenantId(tenantId);
            materialLotVO.setEventId(upgradeEventId);
            materialLotVO.setSiteId(dto.getSiteId());
            materialLotVO.setEnableFlag(HmeConstants.ConstantValue.YES);
            materialLotVO.setQualityStatus(HmeConstants.ConstantValue.OK);
            materialLotVO.setMaterialLotCode(upGradeMaterialLot.getMaterialLotCode());
            //V20200826 modify by penglin.sui for tianyang.xie ?????????????????????????????????
            materialLotVO.setMaterialId(currentSn.getMaterialId());
            //materialLotVO.setMaterialId(dto.getSnMaterialId());
            materialLotVO.setPrimaryUomId(currentSn.getPrimaryUomId());
            materialLotVO.setPrimaryUomQty(1D);
            materialLotVO.setLocatorId(currentSn.getLocatorId());
            materialLotVO.setEoId(dto.getEoId());
            materialLotVO.setLoadTime(currentDate);
            materialLotVO.setInSiteTime(currentDate);
            materialLotVO.setCreateReason("INITIALIZE");
            materialLotVO.setCid(Long.valueOf(customSequence.getNextKey("mt_material_lot_cid_s")));
            MtMaterialLotVO13 materialLotResult = mtMaterialLotRepository.materialLotUpdate(tenantId,
                    materialLotVO, HmeConstants.ConstantValue.NO);

            // ??????????????????-??????????????????
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(materialLotResult.getMaterialLotId());
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName(HmeConstants.ExtendAttr.STATUS);
            MtExtendVO5 mfFlagAttr = new MtExtendVO5();
            mfFlagAttr.setAttrName("MF_FLAG");
            MtExtendVO5 reworkFlagAttr = new MtExtendVO5();
            reworkFlagAttr.setAttrName("REWORK_FLAG");
            MtExtendVO5 pfmLevelAttr = new MtExtendVO5();
            pfmLevelAttr.setAttrName("PERFORMANCE_LEVEL");

            List<MtExtendAttrVO1> mtExtendAttrVO1s = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_material_lot_attr", Collections.singletonList(dto.getSnMaterialLotId()), "MF_FLAG", "REWORK_FLAG", "PERFORMANCE_LEVEL"));
            for (MtExtendAttrVO1 extendAttr : mtExtendAttrVO1s) {
                // ??????
                if (HmeConstants.ExtendAttr.STATUS.equals(extendAttr.getAttrName())) {
                    statusAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ???????????????
                if ("MF_FLAG".equals(extendAttr.getAttrName())) {
                    mfFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ????????????
                if ("REWORK_FLAG".equals(extendAttr.getAttrName())) {
                    reworkFlagAttr.setAttrValue(extendAttr.getAttrValue());
                }
                // ????????????
                if ("PERFORMANCE_LEVEL".equals(extendAttr.getAttrName())) {
                    pfmLevelAttr.setAttrValue(extendAttr.getAttrValue());
                }
            }

            List<MtExtendVO5> list = new ArrayList<>(16);
            list.add(statusAttr);
            list.add(mfFlagAttr);
            list.add(reworkFlagAttr);
            list.add(pfmLevelAttr);
            mtExtendVO10.setAttrs(list);
            // ???????????????????????????
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

            // eo??????
            String eoUpdateEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setLocatorId(currentSn.getLocatorId());
                    setEventTypeCode("EO_UPDATE");
                }
            });

            MtEoVO mtEoVO = new MtEoVO();

            mtEoVO.setEventId(eoUpdateEventId);
            mtEoVO.setTenantId(tenantId);
            mtEoVO.setEoId(dto.getEoId());
            mtEoVO.setWorkOrderId(dto.getWorkOrderId());
            mtEoVO.setIdentification(upGradeMaterialLot.getMaterialLotCode());
            mtEoRepository.eoUpdate(tenantId, mtEoVO, HmeConstants.ConstantValue.NO);

            //V20201005 modify by penglin.sui for for lu.bai ??????hme_eo_job_sn ???material_lot_id
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnMapper.selectByPrimaryKey(dto.getJobId());
            if (Objects.isNull(hmeEoJobSn)) {
                //${1}????????? ?????????${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "eoJobSn", dto.getJobId()));
            }
            hmeEoJobSn.setMaterialLotId(materialLotResult.getMaterialLotId());
            hmeEoJobSnMapper.updateByPrimaryKey(hmeEoJobSn);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO2 refresh(Long tenantId, HmeEoJobSnVO3 dto) {
        HmeEoJobSnVO2 resultVO = new HmeEoJobSnVO2();
        BeanUtils.copyProperties(dto, resultVO);
        if (StringUtils.isNotBlank(dto.getEoStepId())) {
            // ??????????????????
            MtRouterStep currentRouterStep = mtRouterStepRepository.routerStepGet(tenantId, dto.getEoStepId());
            resultVO.setCurrentStepName(currentRouterStep.getStepName());
            resultVO.setCurrentStepDescription(currentRouterStep.getDescription());
            resultVO.setCurrentStepSequence(currentRouterStep.getSequence());
            // ?????????????????????????????????
            List<MtRouterNextStep> mtRouterNextStepList =
                    mtRouterNextStepRepository.routerNextStepQuery(tenantId, dto.getEoStepId());
            if (CollectionUtils.isNotEmpty(mtRouterNextStepList)) {
                MtRouterNextStep mtRouterNextStep = mtRouterNextStepList.get(0);
                MtRouterStep nextRouterStep =
                        mtRouterStepRepository.routerStepGet(tenantId, mtRouterNextStep.getNextStepId());
                resultVO.setNextStepName(nextRouterStep.getStepName());
                resultVO.setNextStepDescription(nextRouterStep.getDescription());
            }
        }

        //????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        resultVO.setProductionVersion(mtWorkOrder.getProductionVersion());
        resultVO.setMaterialId(resultVO.getSnMaterialId());
        //???????????????
        MtMaterialBasic mtMaterialBasicPara = new MtMaterialBasic();
        mtMaterialBasicPara.setTenantId(tenantId);
        mtMaterialBasicPara.setMaterialId(resultVO.getSnMaterialId());
        MtMaterialBasic mtMaterialBasic = mtMaterialBasisRepository.selectOne(mtMaterialBasicPara);
        if (Objects.nonNull(mtMaterialBasic)) {
            resultVO.setItemType(mtMaterialBasic.getItemType());
        }
        log.info("<====== HmeEoJobSnRepositoryImpl.refresh resultVO={}", resultVO);
        if (!HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
            return materialInSiteOfRefresh(tenantId, resultVO);
        } else {
            return null;
        }
    }

    /**
     * @param tenantId ??????ID
     * @param dto      ??????
     * @return com.ruike.hme.domain.vo.HmeEoVO
     * @Description ??????????????????-???????????? ???????????????EO
     * @author yuchao.wang
     * @date 2020/10/9 23:05
     */
    @Override
    public HmeEoVO defaultEoQueryForFirst(Long tenantId, HmeEoJobSnVO3 dto) {
        return hmeEoJobSnMapper.defaultEoQueryForFirst(tenantId, dto);
    }

    /**
     * @param tenantId   ??????ID
     * @param siteId     ??????ID
     * @param materialId ??????ID
     * @return String
     * @Description ??????????????????
     * @author penglin.sui
     * @date 2020/10/14 21:19
     */
    @Override
    public String getMaterialType(Long tenantId, String siteId, String materialId) {
        String type = HmeConstants.MaterialTypeCode.LOT;
        MtMaterialSite materialSiteParam = new MtMaterialSite();
        materialSiteParam.setTenantId(tenantId);
        materialSiteParam.setSiteId(siteId);
        materialSiteParam.setMaterialId(materialId);
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId,
                materialSiteParam);
        if (StringUtils.isBlank(materialSiteId)) {
            // ????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
        }

        MtExtendSettings extendAttr = new MtExtendSettings();
        extendAttr.setAttrName("attribute14");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_site_attr", "MATERIAL_SITE_ID", materialSiteId,
                Collections.singletonList(extendAttr));
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            if (StringUtils.isNotBlank(mtExtendAttrVOList.get(0).getAttrValue())) {
                type = mtExtendAttrVOList.get(0).getAttrValue();
            }
        }

        return type;
    }

    /**
     * ????????????????????????
     *
     * @param tenantId
     * @param materialIds
     * @author chuang.yang
     * @date 2020/10/21
     */
    private List<HmeEoJobSnVO10> getMaterialAttrBatch(Long tenantId, String siteId, List<String> materialIds) {
        SecurityTokenHelper.close();
        List<MtMaterialSite> mtMaterialSiteList =
                mtMaterialSiteRepository.selectByCondition(Condition.builder(MtMaterialSite.class)
                        .andWhere(Sqls.custom().andEqualTo(MtMaterialSite.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtMaterialSite.FIELD_SITE_ID, siteId)
                                .andIn(MtMaterialSite.FIELD_MATERIAL_ID, materialIds))
                        .build());
        if (CollectionUtils.isEmpty(mtMaterialSiteList) || mtMaterialSiteList.size() != materialIds.size()) {
            // ????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_033",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
        }

        Map<String, String> materialSiteMap = mtMaterialSiteList.stream()
                .collect(Collectors.toMap(MtMaterialSite::getMaterialId, MtMaterialSite::getMaterialSiteId));

        List<String> materialSiteIds = mtMaterialSiteList.stream().map(MtMaterialSite::getMaterialSiteId).distinct()
                .collect(Collectors.toList());
        SecurityTokenHelper.close();
        List<MtExtendAttrVO1> mtExtendAttrVO1List = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_material_site_attr", materialSiteIds, "attribute14", "attribute1", "ISSUED_FLAG"));

        Map<String, String> materialTypeMap = new HashMap<>();
        Map<String, String> materialBackFlushMap = new HashMap<>();
        Map<String, String> materialIssuedFlagMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
            List<MtExtendAttrVO1> attrList = mtExtendAttrVO1List.stream()
                    .filter(t -> "attribute14".equals(t.getAttrName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(attrList)) {
                materialTypeMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }

            attrList = mtExtendAttrVO1List.stream()
                    .filter(t -> "attribute1".equals(t.getAttrName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(attrList)) {
                materialBackFlushMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }

            attrList = mtExtendAttrVO1List.stream()
                    .filter(t -> "ISSUED_FLAG".equals(t.getAttrName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(attrList)) {
                materialIssuedFlagMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }
        }

        List<HmeEoJobSnVO10> resultList = new ArrayList<>(materialIds.size());
        for (String materialId : materialIds) {
            HmeEoJobSnVO10 result = new HmeEoJobSnVO10();
            result.setMaterialId(materialId);

            String materialSiteId = materialSiteMap.get(materialId);

            //  ????????????
            if (StringUtils.isNotBlank(materialTypeMap.get(materialSiteId))) {
                result.setMaterialType(materialTypeMap.get(materialSiteId));
            } else {
                result.setMaterialType(HmeConstants.MaterialTypeCode.LOT);
            }

            // ?????????
            result.setBackFlush(StringUtils.isNotEmpty(materialBackFlushMap.get(materialSiteId))
                    && "2".equals(materialBackFlushMap.get(materialSiteId)));

            // ??????????????????,Y?????????
            result.setIssuedFlag(StringUtils.isNotEmpty(materialIssuedFlagMap.get(materialSiteId))
                    && YES.equals(materialIssuedFlagMap.get(materialSiteId)));

            resultList.add(result);
        }
        return resultList;
    }

    /**
     * @param tenantId   ??????ID
     * @param siteId     ??????ID
     * @param materialId ??????ID
     * @return Boolean
     * @Description ?????????????????????
     * @author penglin.sui
     * @date 2020/10/14 21:19
     */
    @Override
    public Boolean checkBackFlush(Long tenantId, String siteId, String materialId) {
        Boolean isBackFlush = false;
        MtMaterialSite materialSiteParam = new MtMaterialSite();
        materialSiteParam.setTenantId(tenantId);
        materialSiteParam.setSiteId(siteId);
        materialSiteParam.setMaterialId(materialId);
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId,
                materialSiteParam);
        if (StringUtils.isBlank(materialSiteId)) {
            // ????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
        }

        MtExtendSettings extendAttr = new MtExtendSettings();
        extendAttr.setAttrName("attribute1");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_site_attr", "MATERIAL_SITE_ID", materialSiteId,
                Collections.singletonList(extendAttr));
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            if (StringUtils.isNotBlank(mtExtendAttrVOList.get(0).getAttrValue())) {
                if ("2".equals(mtExtendAttrVOList.get(0).getAttrValue())) {
                    isBackFlush = true;
                }
            }
        }

        return isBackFlush;
    }

    @Override
    public Boolean checkIssuedFlag(Long tenantId, String siteId, String materialId) {
        Boolean isIssuedFlash = true;
        MtMaterialSite materialSiteParam = new MtMaterialSite();
        materialSiteParam.setTenantId(tenantId);
        materialSiteParam.setSiteId(siteId);
        materialSiteParam.setMaterialId(materialId);
        String materialSiteId = mtMaterialSiteRepository.materialSiteLimitRelationGet(tenantId,
                materialSiteParam);
        if (StringUtils.isBlank(materialSiteId)) {
            // ????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
        }

        MtExtendSettings extendAttr = new MtExtendSettings();
        extendAttr.setAttrName("ISSUED_FLAG");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_site_attr", "MATERIAL_SITE_ID", materialSiteId,
                Collections.singletonList(extendAttr));
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            if (StringUtils.isNotBlank(mtExtendAttrVOList.get(0).getAttrValue())) {
                if (YES.equals(mtExtendAttrVOList.get(0).getAttrValue())) {
                    isIssuedFlash = false;
                }
            }
        }

        return isIssuedFlash;
    }

    /**
     * @param tenantId       ??????ID
     * @param bomComponentId ??????ID
     * @return Boolean
     * @Description ???????????????????????????
     * @author penglin.sui
     * @date 2020/10/14 21:40
     */
    @Override
    public Boolean checkVirtualComponent(Long tenantId, String bomComponentId) {
        Boolean isVirtualComponent = false;
        MtExtendSettings extendAttr = new MtExtendSettings();
        extendAttr.setAttrName("lineAttribute9");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                Collections.singletonList(extendAttr));
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            if (StringUtils.isNotBlank(mtExtendAttrVOList.get(0).getAttrValue())) {
                if (HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(mtExtendAttrVOList.get(0).getAttrValue())) {
                    isVirtualComponent = true;
                }
            }
        }

        return isVirtualComponent;
    }

//    /**
//     * ??????????????????????????????
//     *
//     * @author chuang.yang
//     * @date 2020/10/21
//     * @param tenantId
//     * @param bomComponentIds
//     */
//    private List<HmeEoJobSnVO11> getVirtualComponentBatch(Long tenantId, List<String> bomComponentIds) {
//        SecurityTokenHelper.close();
//        List<MtExtendAttrVO1> bomComponentAttrList = mtExtendSettingsMapper.attrPropertyBatchQuery(tenantId,
//                        "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentIds);
//
//        Map<String, String> bomComponentVirtualMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(bomComponentAttrList)) {
//            List<MtExtendAttrVO1> attrList = bomComponentAttrList.stream()
//                            .filter(t -> "lineAttribute9".equals(t.getAttrName())).collect(Collectors.toList());
//
//            if (CollectionUtils.isNotEmpty(attrList)) {
//                bomComponentVirtualMap = attrList.stream()
//                                .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
//            }
//        }
//
//        List<HmeEoJobSnVO11> resultList = new ArrayList<>(bomComponentIds.size());
//        for (String bomComponentId : bomComponentIds) {
//            HmeEoJobSnVO11 result = new HmeEoJobSnVO11();
//            result.setBomComponentId(bomComponentId);
//
//            result.setVirtualComponent(StringUtils.isNotEmpty(bomComponentVirtualMap.get(bomComponentId))
//                            && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG
//                                            .equals(bomComponentVirtualMap.get(bomComponentId)));
//        }
//
//        return resultList;
//    }

    @Override
    public HmeEoJobSnVO2 materialInSite(Long tenantId, HmeEoJobSnVO2 dto) {
        List<HmeEoJobMaterialVO> materialVOList = new ArrayList<>();
        List<HmeEoJobLotMaterialVO> lotMaterialVOList = new ArrayList<>();
        List<HmeEoJobTimeMaterialVO> timeMaterialVOList = new ArrayList<>();
        Long startDate = 0L;
        Long endDate = 0L;
        // ??????????????????????????????????????????????????????????????????????????????
//        if (!HmeConstants.ConstantValue.YES.equals(dto.getReworkFlag())) {
        if (StringUtils.isNotBlank(dto.getOperationId())) {
            MtEoVO19 mtEoVO19 = new MtEoVO19();
            mtEoVO19.setEoId(dto.getEoId());
            mtEoVO19.setOperationId(dto.getOperationId());
            mtEoVO19.setRouterStepId(dto.getEoStepId());

            // ????????????wkc???????????????????????????
            startDate = System.currentTimeMillis();
            List<MtEoVO20> mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
            endDate = System.currentTimeMillis();
            log.info("=================================>??????-createSnJob-eoComponentQtyQuery????????????" + (endDate - startDate) + "ms");
            List<String> bomComponentIdList = new ArrayList<String>();
            List<String> materialIdList = new ArrayList<String>();
            List<HmeEoJobSnMaterialUomVO> mtMaterialUomList = new ArrayList<HmeEoJobSnMaterialUomVO>();
            if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                bomComponentIdList = mtEoVO20List.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
                materialIdList = mtEoVO20List.stream().map(MtEoVO20::getMaterialId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(materialIdList)) {
                    startDate = System.currentTimeMillis();
                    mtMaterialUomList = hmeEoJobSnMapper.queryMaterialUom(tenantId, materialIdList);
                    endDate = System.currentTimeMillis();
                    log.info("=================================>??????-createSnJob-queryMaterialUom????????????" + (endDate - startDate) + "ms");
                }
            }
            List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.EO_JOB_SN_UOM", tenantId);
            //V20201022 modify by penglin.sui ???????????????????????????
            startDate = System.currentTimeMillis();
            List<String> backFlushBomComponentIdList = hmeEoJobSnLotMaterialMapper.selectIsBackFlashMaterial2(tenantId, dto.getSiteId(), bomComponentIdList);
            endDate = System.currentTimeMillis();
            log.info("=================================>??????-createSnJob-selectIsBackFlashMaterial2????????????" + (endDate - startDate) + "ms");
            //V20201022 modify by penglin.sui ??????????????????
            startDate = System.currentTimeMillis();
            List<MtMaterialSite> mtMaterialSiteList = hmeEoJobSnMapper.queryMaterialSite(tenantId, dto.getSiteId(), materialIdList);
            endDate = System.currentTimeMillis();
            log.info("=================================>??????-createSnJob-queryMaterialSite????????????" + (endDate - startDate) + "ms");
            List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<MtExtendAttrVO1>();
            if (CollectionUtils.isNotEmpty(mtMaterialSiteList)) {
                List<String> materialSiteIdList = mtMaterialSiteList.stream().map(MtMaterialSite::getMaterialSiteId).collect(Collectors.toList());
                List<String> attrNameList = new ArrayList<String>();
                attrNameList.add("attribute9");
                attrNameList.add("attribute14");
                startDate = System.currentTimeMillis();
                mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_material_site_attr", "MATERIAL_SITE_ID", materialSiteIdList, attrNameList);
                endDate = System.currentTimeMillis();
                log.info("=================================>??????-createSnJob-queryExtendAttr????????????" + (endDate - startDate) + "ms");
            }
            //?????????????????????????????????
            startDate = System.currentTimeMillis();
            List<MtExtendAttrVO1> virtualComponentList = hmeEoJobSnRepository.getVirtualComponent(tenantId, bomComponentIdList);
            endDate = System.currentTimeMillis();
            log.info("=================================>??????-createSnJob-getVirtualComponent????????????" + (endDate - startDate) + "ms");
            startDate = System.currentTimeMillis();
            for (MtEoVO20 component : mtEoVO20List) {
                if (component.getComponentQty().compareTo(HmeConstants.ConstantValue.DOUBLE_ZERO) != 0) {

                    //V20201001 modify by penglin.sui for lu.bai ??????????????????
                    if (backFlushBomComponentIdList.contains(component.getBomComponentId())) {
                        continue;
                    }
                    List<MtMaterialSite> materialSiteIdList2 = mtMaterialSiteList.stream().filter(item -> item.getMaterialId().equals(component.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(materialSiteIdList2)) {
                        // ????????????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
                    }
                    String materialSiteId = materialSiteIdList2.get(0).getMaterialSiteId();

                    //?????????????????????
                    List<MtExtendAttrVO1> virtualComponentList2 = virtualComponentList.stream().filter(item -> item.getKeyId().equals(component.getBomComponentId()) && "lineAttribute9".equals(item.getAttrName())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(virtualComponentList2)) {
                        if (StringUtils.isNotBlank(virtualComponentList2.get(0).getAttrValue())) {
                            if (HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(virtualComponentList2.get(0).getAttrValue())) {
                                continue;
                            }
                        }
                    }

                    // ???????????????????????????????????????????????????,?????????????????????????????????????????????????????????
                    String lotType = HmeConstants.MaterialTypeCode.LOT;
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                        List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(materialSiteId) && "attribute14".equals(item.getAttrName())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                            if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                lotType = mtExtendAttrVO1List2.get(0).getAttrValue();
                            }
                        }
                    }

                    BigDecimal componentQty = new BigDecimal(String.valueOf(component.getComponentQty()));
                    List<HmeEoJobSnMaterialUomVO> mtMaterialUomList1 = mtMaterialUomList.stream().filter(item -> item.getMaterialId().equals(component.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(mtMaterialUomList1)) {
                        //${1}????????? ?????????${2}
                        throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "??????", component.getMaterialId()));
                    }
                    // SN????????????????????????
                    if (HmeConstants.MaterialTypeCode.SN.equals(lotType)) {
                        //V20200922 modify by penglin.sui for lu.bai ??????????????????????????????????????????????????????
                        boolean isSplitLine = false;
                        if (CollectionUtils.isNotEmpty(mtMaterialUomList1)) {
                            if (CollectionUtils.isNotEmpty(typeLov)) {
                                isSplitLine = typeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList()).contains(mtMaterialUomList1.get(0).getUomType());
                            }
                        }

                        List<HmeEoJobMaterialVO> jobMaterialVOList = hmeEoJobMaterialRepository.initJobMaterial(
                                tenantId, component.getMaterialId(), isSplitLine, componentQty,
                                component.getBomComponentId(), dto);
                        //materialVOList.addAll(jobMaterialVOList);
                    }

                    // SN????????????????????????
                    if (HmeConstants.MaterialTypeCode.LOT.equals(lotType)) {
//                            HmeEoJobLotMaterialVO hmeEoJobLotMaterialVO = hmeEoJobLotMaterialRepository
//                                            .initLotMaterial(tenantId, component.getMaterialId(), componentQty, dto);
//                            lotMaterialVOList.add(hmeEoJobLotMaterialVO);
                        List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = hmeEoJobLotMaterialRepository
                                .initLotMaterialList(tenantId, component.getMaterialId(), componentQty, dto);
                        //lotMaterialVOList.addAll(hmeEoJobLotMaterialVOList);
                    }

                    // ???????????????????????????????????????????????????????????????????????????
                    if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                        // ??????????????????
                        if (HmeConstants.MaterialTypeCode.TIME.equals(lotType)) {
                            String availableTime = "";
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                                List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(materialSiteId) && "attribute9".equals(item.getAttrName())).collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2) && StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                    availableTime = mtExtendAttrVO1List2.get(0).getAttrValue();
                                }
                            }

                            //V20200915 modify by penglin.sui for lu.bai ??????????????????????????????
                            if (StringUtils.isBlank(availableTime)) {
                                //???????????????${1}??????????????????????????????
                                throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_070", "HME", mtMaterialUomList1.get(0).getMaterialCode()));
                            }

//                                HmeEoJobTimeMaterialVO hmeEoJobTimeMaterialVO = hmeEoJobTimeMaterialRepository
//                                                .initTimeMaterial(tenantId, component.getMaterialId(), availableTime,
//                                                                componentQty, dto);
//                                timeMaterialVOList.add(hmeEoJobTimeMaterialVO);
                            List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOList = hmeEoJobTimeMaterialRepository
                                    .initTimeMaterialList(tenantId, component.getMaterialId(), availableTime,
                                            componentQty, dto);
                            //timeMaterialVOList.addAll(hmeEoJobTimeMaterialVOList);
                        }
                    }
                }
            }
            endDate = System.currentTimeMillis();
            log.info("=================================>??????-createSnJob-???????????????" + mtEoVO20List.size());
            log.info("=================================>??????-createSnJob-??????????????????" + (endDate - startDate) + "ms");

            //???????????????????????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.10.21
            if (!HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())
                    || !YES.equals(dto.getBatchProcessSnScanFlag())) {
                // SN????????????????????????
                HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
                hmeEoJobMaterialVO.setWorkcellId(dto.getWorkcellId());
                hmeEoJobMaterialVO.setJobType(dto.getJobType());
                hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
                hmeEoJobMaterialVO.setWorkOrderId(dto.getWorkOrderId());
                hmeEoJobMaterialVO.setEoStepId(dto.getEoStepId());
                hmeEoJobMaterialVO.setEoId(dto.getEoId());
                hmeEoJobMaterialVO.setJobId(dto.getJobId());
                hmeEoJobMaterialVO.setSiteId(dto.getSiteId());
                hmeEoJobMaterialVO.setReworkFlag(dto.getReworkFlag());
                startDate = System.currentTimeMillis();
                lotMaterialVOList = hmeEoJobLotMaterialRepository.matchedJobLotMaterialQuery(tenantId, hmeEoJobMaterialVO, null);
                endDate = System.currentTimeMillis();
                log.info("=================================>??????-createSnJob-matchedJobLotMaterialQuery????????????" + (endDate - startDate) + "ms");

                // ???????????????????????????????????????????????????????????????????????????
                if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                    // ??????????????????
                    startDate = System.currentTimeMillis();
                    timeMaterialVOList = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, hmeEoJobMaterialVO, null);
                    endDate = System.currentTimeMillis();
                    log.info("=================================>??????-createSnJob-matchedJobTimeMaterialQuery????????????" + (endDate - startDate) + "ms");
                }

                //SN????????????????????????
                HmeEoJobMaterialVO2 hmeEoJobMaterialVO2 = new HmeEoJobMaterialVO2();
                hmeEoJobMaterialVO2.setWorkcellId(dto.getWorkcellId());
                hmeEoJobMaterialVO2.setJobId(dto.getJobId());
                hmeEoJobMaterialVO2.setEoId(dto.getEoId());
                hmeEoJobMaterialVO2.setSiteId(dto.getSiteId());
                hmeEoJobMaterialVO2.setEoStepId(dto.getEoStepId());
                hmeEoJobMaterialVO2.setOperationId(dto.getOperationId());
                hmeEoJobMaterialVO2.setJobType(dto.getJobType());
                startDate = System.currentTimeMillis();
                materialVOList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId, hmeEoJobMaterialVO2);
                endDate = System.currentTimeMillis();
                log.info("=================================>??????-createSnJob-jobSnLimitJobMaterialQuery????????????" + (endDate - startDate) + "ms");
            }
        }
        //V20200828 modify by penglin.sui for tianyang.xie ??????????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(materialVOList)) {
            materialVOList = materialVOList.stream()
                    .sorted(Comparator.comparing(HmeEoJobMaterialVO::getLineNumber)).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(lotMaterialVOList)) {
            lotMaterialVOList = lotMaterialVOList.stream()
                    .sorted(Comparator.comparing(HmeEoJobLotMaterialVO::getLineNumber)).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(timeMaterialVOList)) {
            timeMaterialVOList = timeMaterialVOList.stream()
                    .sorted(Comparator.comparing(HmeEoJobTimeMaterialVO::getLineNumber)).collect(Collectors.toList());
        }
        dto.setMaterialVOList(materialVOList);
        dto.setLotMaterialVOList(lotMaterialVOList);
        dto.setTimeMaterialVOList(timeMaterialVOList);
//        }

        // ???????????????????????????
        startDate = System.currentTimeMillis();
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = hmeEoJobDataRecordRepository.inSiteScan(tenantId, dto);
        endDate = System.currentTimeMillis();
        log.info("=================================>??????-createSnJob-inSiteScan????????????" + (endDate - startDate) + "ms");
        dto.setDataRecordVOList(hmeEoJobDataRecordVOList);

        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO2 materialInSiteOfRefresh(Long tenantId, HmeEoJobSnVO2 dto) {
        List<HmeEoJobMaterialVO> materialVOList = new ArrayList<>();
        List<HmeEoJobLotMaterialVO> lotMaterialVOList = new ArrayList<>();
        List<HmeEoJobTimeMaterialVO> timeMaterialVOList = new ArrayList<>();

        // ??????????????????????????????????????????????????????????????????????????????
//        if (!HmeConstants.ConstantValue.YES.equals(dto.getReworkFlag())) {
        if (StringUtils.isNotBlank(dto.getOperationId())) {
            MtEoVO19 mtEoVO19 = new MtEoVO19();
            mtEoVO19.setEoId(dto.getEoId());
            mtEoVO19.setOperationId(dto.getOperationId());
            mtEoVO19.setRouterStepId(dto.getEoStepId());
            List<MtEoVO20> eoComponentList = new ArrayList<>();
            List<HmePrepareMaterialVO> prepareMaterialVOList = new ArrayList<>();

            HmeEoJobMaterialVO hmeEoJobMaterialVO3 = new HmeEoJobMaterialVO();
            hmeEoJobMaterialVO3.setJobType(dto.getJobType());
            hmeEoJobMaterialVO3.setEoId(dto.getEoId());
            hmeEoJobMaterialVO3.setOperationId(dto.getOperationId());
            hmeEoJobMaterialVO3.setRouterStepId(dto.getEoStepId());
            hmeEoJobMaterialVO3.setWorkOrderId(dto.getWorkOrderId());

            // Boolean queryComponent = false;
            initJobMaterialProcess(tenantId, dto);
            invalidBarcodeProcess(tenantId, dto);
            //???????????????????????????????????????????????????
            List<HmeEoJobMaterial> hmeEoJobMaterialList = hmeEoJobMaterialMapper.selectNotReleaseJobMaterial(tenantId, dto.getJobId(), dto.getSiteId());
            List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = hmeEoJobLotMaterialMapper.selectNotReleaseLotMaterial(tenantId, dto.getWorkcellId(), dto.getJobId());
            List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList = hmeEoJobTimeMaterialMapper.selectNotReleaseTimeMaterial(tenantId, dto.getWorkcellId(), dto.getJobId());
            List<String> materialAllList = new ArrayList<>();
            // modify by jiangling.zheng @20201114 ??????
            List<MtEoVO20> eoComponents = new ArrayList<>();
            List<HmePrepareMaterialVO> prepareMaterials = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList) || CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList) ||
                    CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
                List<String> jobMaterialAllList = hmeEoJobMaterialList.stream().map(HmeEoJobMaterial::getMaterialId).distinct().collect(Collectors.toList());
                List<String> lotMaterialAllList = hmeEoJobLotMaterialList.stream().map(HmeEoJobLotMaterial::getMaterialId).distinct().collect(Collectors.toList());
                List<String> timeMaterialAllList = hmeEoJobTimeMaterialList.stream().map(HmeEoJobTimeMaterial::getMaterialId).distinct().collect(Collectors.toList());
                if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                    //??????????????????
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getSnMaterialId());
                    if (Objects.isNull(mtMaterial)) {
                        //${1}????????? ?????????${2}
                        throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "??????", dto.getSnMaterialId()));
                    }
                    prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery2(tenantId, dto.getSiteId(), dto.getWorkOrderId(), mtMaterial.getMaterialCode());
                } else {
                    eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                }
                materialAllList.addAll(jobMaterialAllList);
                materialAllList.addAll(lotMaterialAllList);
                materialAllList.addAll(timeMaterialAllList);
            }
                /*if(CollectionUtils.isNotEmpty(hmeEoJobMaterialList)){
                    materialAllList = hmeEoJobMaterialList.stream().map(HmeEoJobMaterial::getMaterialId).distinct().collect(Collectors.toList());
                    if(HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())){
                        //??????????????????
                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getSnMaterialId());
                        if (Objects.isNull(mtMaterial)) {
                            //${1}????????? ?????????${2}
                            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0037", "GENERAL", "??????", dto.getSnMaterialId()));
                        }
                        prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery2(tenantId, dto.getSiteId(), dto.getWorkOrderId(), mtMaterial.getMaterialCode());
                    }else {
                        eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                    }
                    queryComponent = true;
                }

                if(CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList)){
                    List<String> lotMaterialAllList = hmeEoJobLotMaterialList.stream().map(HmeEoJobLotMaterial::getMaterialId).distinct().collect(Collectors.toList());
                    if(!queryComponent){
                        if(HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())){
                            //??????????????????
                            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getSnMaterialId());
                            if (Objects.isNull(mtMaterial)) {
                                //${1}????????? ?????????${2}
                                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_GENERAL_0037", "GENERAL", "??????", dto.getSnMaterialId()));
                            }
                            prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery2(tenantId, dto.getSiteId(), dto.getWorkOrderId(), mtMaterial.getMaterialCode());
                        }else {
                            eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                        }
                        queryComponent = true;
                    }
                    materialAllList.addAll(lotMaterialAllList);
                }

                if(CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)){
                    List<String> timeMaterialAllList = hmeEoJobTimeMaterialList.stream().map(HmeEoJobTimeMaterial::getMaterialId).distinct().collect(Collectors.toList());
                    if(!queryComponent){
                        if(HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())){
                            //??????????????????
                            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getSnMaterialId());
                            if (Objects.isNull(mtMaterial)) {
                                //${1}????????? ?????????${2}
                                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_GENERAL_0037", "GENERAL", "??????", dto.getSnMaterialId()));
                            }
                            prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery2(tenantId, dto.getSiteId(), dto.getWorkOrderId(), mtMaterial.getMaterialCode());
                        }else {
                            eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                        }
                        queryComponent = true;
                    }
                    materialAllList.addAll(timeMaterialAllList);
                }*/

            //????????????????????????????????????????????????????????????
            List<MtMaterialSite> mtMaterialSiteList = new ArrayList<>();
            List<HmeEoJobSnMaterialUomVO> mtMaterialUomList = new ArrayList<HmeEoJobSnMaterialUomVO>();
            if (CollectionUtils.isNotEmpty(materialAllList)) {
                mtMaterialSiteList = hmeEoJobSnMapper.queryMaterialSite(tenantId, dto.getSiteId(), materialAllList);
                mtMaterialUomList = hmeEoJobSnMapper.queryMaterialUom(tenantId, materialAllList);
            }
            List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(mtMaterialSiteList)) {
                mtExtendAttrVO1List = getMaterialSiteAttr(tenantId, mtMaterialSiteList);
            }
            List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.EO_JOB_SN_UOM", tenantId);
            List<String> snJobMaterialIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {

                Map<String, List<HmeEoJobMaterial>> hmeEoJobMaterialMap = hmeEoJobMaterialList.stream().collect(Collectors.groupingBy(HmeEoJobMaterial::getMaterialId));
                for (String materialId : hmeEoJobMaterialMap.keySet()
                ) {
                    List<MtMaterialSite> materialSiteIdList2 = mtMaterialSiteList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(materialSiteIdList2)) {
                        // ????????????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
                    }
                    // ???????????????????????????????????????????????????,?????????????????????????????????????????????????????????
                        /*String lotType = HmeConstants.MaterialTypeCode.LOT;
                        if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                            List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) && item.getAttrName().equals("attribute14")).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                                if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                    lotType = mtExtendAttrVO1List2.get(0).getAttrValue();
                                }
                            }
                        }*/
                    List<HmeEoJobMaterial> subHmeEoJobMaterial = hmeEoJobMaterialMap.get(materialId);
                    String lotType = StringUtils.isNotBlank(subHmeEoJobMaterial.get(0).getLotType()) ?
                            subHmeEoJobMaterial.get(0).getLotType() : HmeConstants.MaterialTypeCode.LOT;
                    BigDecimal componentQty = BigDecimal.ZERO;
                    if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                        if (CollectionUtils.isNotEmpty(prepareMaterialVOList)) {
                            List<HmePrepareMaterialVO> singlePrepareMaterialVOList = prepareMaterialVOList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(singlePrepareMaterialVOList)) {
                                componentQty = singlePrepareMaterialVOList.get(0).getComponentQty();
                            }
                        }
                    } else {
                        if (CollectionUtils.isNotEmpty(eoComponentList)) {
                            List<MtEoVO20> singleEoComponentList = eoComponentList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(singleEoComponentList)) {
                                componentQty = BigDecimal.valueOf(singleEoComponentList.get(0).getComponentQty());
                            }
                        }
                    }
                    // add by jiangling.zheng 20201114 ?????????????????????????????????????????????????????? ???????????????????????????0????????????????????????material_lot_id
                    if (componentQty.compareTo(BigDecimal.ZERO) <= 0) {
                        //List<HmeEoJobMaterial> subHmeEoJobMaterial = hmeEoJobMaterialMap.get(materialId);
                        snJobMaterialIdList.addAll(subHmeEoJobMaterial.stream().filter(c -> StringUtils.isBlank(c.getMaterialLotId()))
                                .map(HmeEoJobMaterial::getJobMaterialId).collect(Collectors.toList()));
                    }
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                        String isRecoilMaterial = "";
                        List<MtExtendAttrVO1> isRecoilMaterials = mtExtendAttrVO1List.stream().filter(item ->
                                item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) &&
                                        "attribute1".equals(item.getAttrName())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(isRecoilMaterials) && StringUtils.isNotBlank(isRecoilMaterials.get(0).getAttrValue())) {
                            isRecoilMaterial = isRecoilMaterials.get(0).getAttrValue();
                        }
                        if (HmeConstants.ConstantValue.STRING_TWO.equals(isRecoilMaterial)) {
                            snJobMaterialIdList.addAll(subHmeEoJobMaterial.stream().map(HmeEoJobMaterial::getJobMaterialId).collect(Collectors.toList()));
                            continue;
                        }
                    }
                    // end add
                    if (!HmeConstants.MaterialTypeCode.SN.equals(lotType)) {
                        //List<HmeEoJobMaterial> subHmeEoJobMaterial = hmeEoJobMaterialMap.get(materialId);
                        if (HmeConstants.MaterialTypeCode.LOT.equals(lotType)) {
                            List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = hmeEoJobLotMaterialRepository
                                    .initLotMaterialList(tenantId, materialId, componentQty, dto);
                            snJobMaterialIdList.addAll(subHmeEoJobMaterial.stream().map(HmeEoJobMaterial::getJobMaterialId).collect(Collectors.toList()));
                        } else if (HmeConstants.MaterialTypeCode.TIME.equals(lotType)) {
                            String availableTime = "";
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                                List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item ->
                                        item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) &&
                                                "attribute9".equals(item.getAttrName())).collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2) && StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                    availableTime = mtExtendAttrVO1List2.get(0).getAttrValue();
                                }
                            }

                            //V20200915 modify by penglin.sui for lu.bai ??????????????????????????????
                            if (StringUtils.isBlank(availableTime)) {
                                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialId);
                                //???????????????${1}??????????????????????????????
                                throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_070", "HME", mtMaterial.getMaterialCode()));
                            }
                            List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOList = hmeEoJobTimeMaterialRepository
                                    .initTimeMaterialList(tenantId, materialId, availableTime, componentQty, dto);
                            snJobMaterialIdList.addAll(subHmeEoJobMaterial.stream().map(HmeEoJobMaterial::getJobMaterialId).collect(Collectors.toList()));
                        }
                    }
                }
            }

            List<String> lotJobMaterialIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList) && ((!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType()) && CollectionUtils.isNotEmpty(eoComponentList)) || (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType()) && CollectionUtils.isNotEmpty(prepareMaterialVOList)))) {
                Map<String, List<HmeEoJobLotMaterial>> hmeEoJobLotMaterialMap = hmeEoJobLotMaterialList.stream().collect(Collectors.groupingBy(HmeEoJobLotMaterial::getMaterialId));
                for (String materialId : hmeEoJobLotMaterialMap.keySet()) {
                    List<MtMaterialSite> materialSiteIdList2 = mtMaterialSiteList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(materialSiteIdList2)) {
                        // ????????????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
                    }
                    // ???????????????????????????????????????????????????,?????????????????????????????????????????????????????????
                    String lotType = HmeConstants.MaterialTypeCode.LOT;
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                        List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) && "attribute14".equals(item.getAttrName())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                            if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                lotType = mtExtendAttrVO1List2.get(0).getAttrValue();
                            }
                        }
                    }
                    BigDecimal componentQty = BigDecimal.ZERO;
                    String bomComponentId = "";
                    if (CollectionUtils.isNotEmpty(eoComponentList)) {
                        List<MtEoVO20> singleEoComponentList = eoComponentList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(singleEoComponentList)) {
                            componentQty = BigDecimal.valueOf(singleEoComponentList.get(0).getComponentQty());
                            bomComponentId = singleEoComponentList.get(0).getBomComponentId();
                        }
                    }

                    if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                        if (CollectionUtils.isNotEmpty(prepareMaterialVOList)) {
                            List<HmePrepareMaterialVO> singlePrepareMaterialVOList = prepareMaterialVOList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(singlePrepareMaterialVOList)) {
                                componentQty = singlePrepareMaterialVOList.get(0).getComponentQty();
                                bomComponentId = singlePrepareMaterialVOList.get(0).getBomComponentId();
                            }
                        }
                    } else {
                        if (CollectionUtils.isNotEmpty(eoComponentList)) {
                            List<MtEoVO20> singleEoComponentList = eoComponentList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(singleEoComponentList)) {
                                componentQty = BigDecimal.valueOf(singleEoComponentList.get(0).getComponentQty());
                                bomComponentId = singleEoComponentList.get(0).getBomComponentId();
                            }
                        }
                    }
                    // add by jiangling.zheng 20201114 ?????????????????????????????????????????????????????? ???????????????????????????0????????????????????????material_lot_id
                    if (componentQty.compareTo(BigDecimal.ZERO) <= 0) {
                        List<HmeEoJobLotMaterial> subHmeEoJobLotMaterial = hmeEoJobLotMaterialMap.get(materialId);
                        lotJobMaterialIdList.addAll(subHmeEoJobLotMaterial.stream().filter(c -> StringUtils.isBlank(c.getMaterialLotId()))
                                .map(HmeEoJobLotMaterial::getJobMaterialId).collect(Collectors.toList()));
                    }
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                        String isRecoilMaterial = "";
                        List<MtExtendAttrVO1> isRecoilMaterials = mtExtendAttrVO1List.stream().filter(item ->
                                item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) &&
                                        "attribute1".equals(item.getAttrName())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(isRecoilMaterials) && StringUtils.isNotBlank(isRecoilMaterials.get(0).getAttrValue())) {
                            isRecoilMaterial = isRecoilMaterials.get(0).getAttrValue();
                        }
                        if (HmeConstants.ConstantValue.STRING_TWO.equals(isRecoilMaterial)) {
                            List<HmeEoJobLotMaterial> subHmeEoJobLotMaterial = hmeEoJobLotMaterialMap.get(materialId);
                            lotJobMaterialIdList.addAll(subHmeEoJobLotMaterial.stream().map(HmeEoJobLotMaterial::getJobMaterialId).collect(Collectors.toList()));
                            continue;
                        }
                    }
                    // end add
                    if (StringUtils.isBlank(bomComponentId)) {
                        List<String> substituteMaterialIdList = hmeEoJobSnLotMaterialRepository.querySubstituteMaterial(tenantId, materialId, hmeEoJobMaterialVO3, new ArrayList<>(), eoComponentList);
                        if (CollectionUtils.isEmpty(substituteMaterialIdList)) {
                            continue;
                        }
                        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                            List<HmePrepareMaterialVO> existsPrepareComponentList = prepareMaterialVOList.stream().filter(item -> substituteMaterialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(existsPrepareComponentList)) {
                                continue;
                            }
                        } else {
                            List<MtEoVO20> existsEoComponentList = eoComponentList.stream().filter(item -> substituteMaterialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(existsEoComponentList)) {
                                continue;
                            }
                        }
                    }

                    if (!HmeConstants.MaterialTypeCode.LOT.equals(lotType)) {
                        List<HmeEoJobLotMaterial> subHmeEoJobLotMaterial = hmeEoJobLotMaterialMap.get(materialId);
                        if (HmeConstants.MaterialTypeCode.SN.equals(lotType)) {
                            List<HmeEoJobSnMaterialUomVO> mtMaterialUomList1 = mtMaterialUomList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(mtMaterialUomList1)) {
                                //${1}????????? ?????????${2}
                                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_GENERAL_0037", "GENERAL", "??????", materialId));
                            }
                            boolean isSplitLine = false;
                            if (CollectionUtils.isNotEmpty(mtMaterialUomList1)) {
                                if (CollectionUtils.isNotEmpty(typeLov)) {
                                    isSplitLine = typeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList()).contains(mtMaterialUomList1.get(0).getUomType());
                                }
                            }

                            List<HmeEoJobMaterialVO> jobMaterialVOList = hmeEoJobMaterialRepository.initJobMaterial(
                                    tenantId, materialId, isSplitLine, componentQty, bomComponentId, dto);
                            lotJobMaterialIdList.addAll(subHmeEoJobLotMaterial.stream().map(HmeEoJobLotMaterial::getJobMaterialId).collect(Collectors.toList()));
                        } else if (HmeConstants.MaterialTypeCode.TIME.equals(lotType)) {
                            String availableTime = "";
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                                List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) && "attribute9".equals(item.getAttrName())).collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2) && StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                    availableTime = mtExtendAttrVO1List2.get(0).getAttrValue();
                                }
                            }

                            //V20200915 modify by penglin.sui for lu.bai ??????????????????????????????
                            if (StringUtils.isBlank(availableTime)) {
                                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialId);
                                //???????????????${1}??????????????????????????????
                                throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_070", "HME", mtMaterial.getMaterialCode()));
                            }
                            List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOList = hmeEoJobTimeMaterialRepository
                                    .initTimeMaterialList(tenantId, materialId, availableTime, componentQty, dto);
                            lotJobMaterialIdList.addAll(subHmeEoJobLotMaterial.stream().map(HmeEoJobLotMaterial::getJobMaterialId).collect(Collectors.toList()));
                        }
                    }
                }
            }

            List<String> timeJobMaterialIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList) && ((!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType()) && CollectionUtils.isNotEmpty(eoComponentList)) || (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType()) && CollectionUtils.isNotEmpty(prepareMaterialVOList)))) {
                Map<String, List<HmeEoJobTimeMaterial>> hmeEoJobTimeMaterialMap = hmeEoJobTimeMaterialList.stream().collect(Collectors.groupingBy(HmeEoJobTimeMaterial::getMaterialId));
                for (String materialId : hmeEoJobTimeMaterialMap.keySet()
                ) {
                    List<MtMaterialSite> materialSiteIdList2 = mtMaterialSiteList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(materialSiteIdList2)) {
                        // ????????????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
                    }
                    // ???????????????????????????????????????????????????,?????????????????????????????????????????????????????????
                    String lotType = HmeConstants.MaterialTypeCode.LOT;
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                        List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) && "attribute14".equals(item.getAttrName())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                            if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                lotType = mtExtendAttrVO1List2.get(0).getAttrValue();
                            }
                        }
                    }
                    BigDecimal componentQty = BigDecimal.ZERO;
                    String bomComponentId = "";
                    if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                        if (CollectionUtils.isNotEmpty(prepareMaterialVOList)) {
                            List<HmePrepareMaterialVO> singlePrepareMaterialVOList = prepareMaterialVOList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(singlePrepareMaterialVOList)) {
                                componentQty = singlePrepareMaterialVOList.get(0).getComponentQty();
                                bomComponentId = singlePrepareMaterialVOList.get(0).getBomComponentId();
                            }
                        }
                    } else {
                        if (CollectionUtils.isNotEmpty(eoComponentList)) {
                            List<MtEoVO20> singleEoComponentList = eoComponentList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(singleEoComponentList)) {
                                componentQty = BigDecimal.valueOf(singleEoComponentList.get(0).getComponentQty());
                                bomComponentId = singleEoComponentList.get(0).getBomComponentId();
                            }
                        }
                    }
                    // add by jiangling.zheng 20201114 ?????????????????????????????????????????????????????? ???????????????????????????0????????????????????????material_lot_id
                    if (componentQty.compareTo(BigDecimal.ZERO) <= 0) {
                        List<HmeEoJobTimeMaterial> subHmeEoJobTimeMaterial = hmeEoJobTimeMaterialMap.get(materialId);
                        timeJobMaterialIdList.addAll(subHmeEoJobTimeMaterial.stream().filter(c -> StringUtils.isBlank(c.getMaterialLotId()))
                                .map(HmeEoJobTimeMaterial::getJobMaterialId).collect(Collectors.toList()));
                    }
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                        String isRecoilMaterial = "";
                        List<MtExtendAttrVO1> isRecoilMaterials = mtExtendAttrVO1List.stream().filter(item ->
                                item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) &&
                                        "attribute1".equals(item.getAttrName())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(isRecoilMaterials) && StringUtils.isNotBlank(isRecoilMaterials.get(0).getAttrValue())) {
                            isRecoilMaterial = isRecoilMaterials.get(0).getAttrValue();
                        }
                        if (HmeConstants.ConstantValue.STRING_TWO.equals(isRecoilMaterial)) {
                            List<HmeEoJobTimeMaterial> subHmeEoJobTimeMaterial = hmeEoJobTimeMaterialMap.get(materialId);
                            timeJobMaterialIdList.addAll(subHmeEoJobTimeMaterial.stream().map(HmeEoJobTimeMaterial::getJobMaterialId).collect(Collectors.toList()));
                            continue;
                        }
                    }
                    // end add
                    if (StringUtils.isBlank(bomComponentId)) {
                        List<String> substituteMaterialIdList = hmeEoJobSnLotMaterialRepository.querySubstituteMaterial(tenantId, materialId, hmeEoJobMaterialVO3, new ArrayList<>(), eoComponentList);
                        if (CollectionUtils.isEmpty(substituteMaterialIdList)) {
                            continue;
                        }
                        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                            List<HmePrepareMaterialVO> existsPrepareComponentList = prepareMaterialVOList.stream().filter(item -> substituteMaterialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(existsPrepareComponentList)) {
                                continue;
                            }
                        } else {
                            List<MtEoVO20> existsEoComponentList = eoComponentList.stream().filter(item -> substituteMaterialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(existsEoComponentList)) {
                                continue;
                            }
                        }
                    }

                    if (!HmeConstants.MaterialTypeCode.TIME.equals(lotType)) {
                        List<HmeEoJobTimeMaterial> subHmeEoJobTimeMaterial = hmeEoJobTimeMaterialMap.get(materialId);
                        if (HmeConstants.MaterialTypeCode.SN.equals(lotType)) {
                            List<HmeEoJobSnMaterialUomVO> mtMaterialUomList1 = mtMaterialUomList.stream().filter(item -> item.getMaterialId().equals(materialId)).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(mtMaterialUomList1)) {
                                //${1}????????? ?????????${2}
                                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_GENERAL_0037", "GENERAL", "??????", materialId));
                            }
                            boolean isSplitLine = false;
                            if (CollectionUtils.isNotEmpty(mtMaterialUomList1)) {
                                if (CollectionUtils.isNotEmpty(typeLov)) {
                                    isSplitLine = typeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList()).contains(mtMaterialUomList1.get(0).getUomType());
                                }
                            }

                            List<HmeEoJobMaterialVO> jobMaterialVOList = hmeEoJobMaterialRepository.initJobMaterial(
                                    tenantId, materialId, isSplitLine, componentQty, bomComponentId, dto);
                            timeJobMaterialIdList.addAll(subHmeEoJobTimeMaterial.stream().map(HmeEoJobTimeMaterial::getJobMaterialId).collect(Collectors.toList()));
                        } else if (HmeConstants.MaterialTypeCode.LOT.equals(lotType)) {
                            List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = hmeEoJobLotMaterialRepository
                                    .initLotMaterialList(tenantId, materialId, componentQty, dto);
                            timeJobMaterialIdList.addAll(subHmeEoJobTimeMaterial.stream().map(HmeEoJobTimeMaterial::getJobMaterialId).collect(Collectors.toList()));
                        }
                    }
                }
            }

            //?????????????????????????????????
            if (CollectionUtils.isNotEmpty(snJobMaterialIdList)) {
                hmeEoJobMaterialMapper.batchDeleteByPrimary(snJobMaterialIdList.stream().distinct().collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(lotJobMaterialIdList)) {
                hmeEoJobLotMaterialMapper.batchDeleteByPrimary(lotJobMaterialIdList.stream().distinct().collect(Collectors.toList()));
            }
            if (CollectionUtils.isNotEmpty(timeJobMaterialIdList)) {
                hmeEoJobTimeMaterialMapper.batchDeleteByPrimary(timeJobMaterialIdList.stream().distinct().collect(Collectors.toList()));
            }

            // SN????????????????????????
            HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
            hmeEoJobMaterialVO.setWorkcellId(dto.getWorkcellId());
            hmeEoJobMaterialVO.setJobType(dto.getJobType());
            hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
            hmeEoJobMaterialVO.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobMaterialVO.setEoStepId(dto.getEoStepId());
            hmeEoJobMaterialVO.setEoId(dto.getEoId());
            hmeEoJobMaterialVO.setJobId(dto.getJobId());
            hmeEoJobMaterialVO.setSiteId(dto.getSiteId());
            hmeEoJobMaterialVO.setReworkFlag(dto.getReworkFlag());
            hmeEoJobMaterialVO.setPrepareQty(dto.getPrepareQty());
            hmeEoJobMaterialVO.setMaterialId(dto.getMaterialId());
            lotMaterialVOList = hmeEoJobLotMaterialRepository.matchedJobLotMaterialQuery(tenantId, hmeEoJobMaterialVO, null);

            // ???????????????????????????????????????????????????????????????????????????
            if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                // ??????????????????
                timeMaterialVOList = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, hmeEoJobMaterialVO, null);
            }

            //SN????????????????????????
            HmeEoJobMaterialVO2 hmeEoJobMaterialVO2 = new HmeEoJobMaterialVO2();
            hmeEoJobMaterialVO2.setWorkcellId(dto.getWorkcellId());
            hmeEoJobMaterialVO2.setJobId(dto.getJobId());
            hmeEoJobMaterialVO2.setEoId(dto.getEoId());
            hmeEoJobMaterialVO2.setMaterialId(dto.getMaterialId());
            hmeEoJobMaterialVO2.setJobType(dto.getJobType());
            hmeEoJobMaterialVO2.setSiteId(dto.getSiteId());
            hmeEoJobMaterialVO2.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobMaterialVO2.setOperationId(dto.getOperationId());
            materialVOList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId, hmeEoJobMaterialVO2);
        }
        //V20200828 modify by penglin.sui for tianyang.xie ??????????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(materialVOList)) {
            materialVOList = materialVOList.stream()
                    .sorted(Comparator.comparing(HmeEoJobMaterialVO::getLineNumber)).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(lotMaterialVOList)) {
            lotMaterialVOList = lotMaterialVOList.stream()
                    .sorted(Comparator.comparing(HmeEoJobLotMaterialVO::getLineNumber)).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(timeMaterialVOList)) {
            timeMaterialVOList = timeMaterialVOList.stream()
                    .sorted(Comparator.comparing(HmeEoJobTimeMaterialVO::getLineNumber)).collect(Collectors.toList());
        }
        dto.setMaterialVOList(materialVOList);
        dto.setLotMaterialVOList(lotMaterialVOList);
        dto.setTimeMaterialVOList(timeMaterialVOList);
//        }

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void outSiteValidate(Long tenantId, HmeEoJobSnVO3 dto) {
//        HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
//        hmeEoJobMaterial.setTenantId(tenantId);
//        hmeEoJobMaterial.setJobId(dto.getJobId());
//        hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
//        List<HmeEoJobMaterial> hmeEoJobMaterialList = hmeEoJobMaterialRepository.select(hmeEoJobMaterial);

        //V20200821 modify by penglin.sui for zhenyong.ban ??????????????????
//        List<HmeEoJobMaterial> unReleaseMaterial = hmeEoJobMaterialList.stream()
//                        .filter(material -> material.getIsReleased() == 0).collect(Collectors.toList());
//        if (CollectionUtils.isNotEmpty(unReleaseMaterial)) {
//            //????????????,??????????????????????????????
//            throw new MtException("HME_EO_JOB_SN_007",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_007", "HME"));
//        }
        //end

        // ???????????????????????????????????????????????????????????????
        if (dto.getIsDataRecordValidate()) {
            SecurityTokenHelper.close();
            Sqls listDataRecordSql = Sqls.custom().andEqualTo(HmeEoJobDataRecord.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(HmeEoJobDataRecord.FIELD_JOB_ID, dto.getJobId())
                    .andEqualTo(HmeEoJobDataRecord.FIELD_WORKCELL_ID, dto.getWorkcellId());

            List<HmeEoJobDataRecord> hmeEoJobDataRecordList = hmeEoJobDataRecordRepository.selectByCondition(
                    Condition.builder(HmeEoJobDataRecord.class).andWhere(listDataRecordSql)
                            .andWhere(Sqls.custom().andIsNull(HmeEoJobDataRecord.FIELD_RESULT)
                            .orEqualTo(HmeEoJobDataRecord.FIELD_RESULT,"")).build());

            // ??????????????????????????????
            if (CollectionUtils.isNotEmpty(hmeEoJobDataRecordList)) {

                //??????????????????????????????????????????????????????
                List<HmeEoJobDataRecord> hmeEoJobDataRecordList1 = hmeEoJobDataRecordList.stream().filter(c -> !"1".equals(c.getIsSupplement())).collect(Collectors.toList());

                boolean isValueAllowMissing = true;

                // UPDATE 20201021 YC ????????????
                List<String> tagIds = hmeEoJobDataRecordList1.stream().map(HmeEoJobDataRecord::getTagId).distinct()
                        .collect(Collectors.toList());

                SecurityTokenHelper.close();
                List<MtTagGroupAssign> mtTagGroupAssignList =
                        mtTagGroupAssignRepository.selectByCondition(Condition.builder(MtTagGroupAssign.class)
                                .andWhere(Sqls.custom()
                                        .andEqualTo(MtTagGroupAssign.FIELD_TENANT_ID, 0L)
                                        .andIn(MtTagGroupAssign.FIELD_TAG_ID, tagIds))
                                .build());

                Map<HmeEoJobSnVO12, String> tagGroupAssignMap = new HashMap<>(mtTagGroupAssignList.size());
                if (CollectionUtils.isNotEmpty(mtTagGroupAssignList)) {
                    tagGroupAssignMap = mtTagGroupAssignList.stream()
                            .collect(Collectors.toMap(t -> new HmeEoJobSnVO12(t.getTagId(), t.getTagGroupId()),
                                    MtTagGroupAssign::getValueAllowMissing));
                }

                String valueAllowMissing;
                for (HmeEoJobDataRecord hmeEoJobDataRecord : hmeEoJobDataRecordList1) {
                    valueAllowMissing = tagGroupAssignMap.get(new HmeEoJobSnVO12(hmeEoJobDataRecord.getTagId(),
                            hmeEoJobDataRecord.getTagGroupId()));
                    if (StringUtils.isBlank(valueAllowMissing) || "N".equals(valueAllowMissing)) {
                        isValueAllowMissing = false;
                        break;
                    }
                }
                if (!isValueAllowMissing) {
                    // ????????????,??????????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_009", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_009", "HME"));
                }

                /*// ??????????????????????????????????????????????????????
                List<HmeEoJobDataRecord> hmeEoJobDataRecordList1 = hmeEoJobDataRecordList.stream().filter(c -> !"1".equals(c.getIsSupplement())).collect(Collectors.toList());
                MtTagGroupAssign mtTagGroupAssignPara = new MtTagGroupAssign();
                mtTagGroupAssignPara.setTenantId(0L);
                Boolean isValueAllowMissing = true;
                for (HmeEoJobDataRecord hmeEoJobDataRecord : hmeEoJobDataRecordList1
                ) {
                    mtTagGroupAssignPara.setTagId(hmeEoJobDataRecord.getTagId());
                    mtTagGroupAssignPara.setTagGroupId(hmeEoJobDataRecord.getTagGroupId());
                    MtTagGroupAssign mtTagGroupAssign = mtTagGroupAssignRepository.selectOne(mtTagGroupAssignPara);
                    if (StringUtils.isBlank(mtTagGroupAssign.getValueAllowMissing()) || "N".equals(mtTagGroupAssign.getValueAllowMissing())) {
                        isValueAllowMissing = false;
                        break;
                    }
                }
                if (!isValueAllowMissing) {
                    //????????????,??????????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_009", "HME"));
                }*/
            }
        }

        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())
                && !HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
            //V20200821 modify by penglin.sui for zhenyong.ban ??????????????????????????????????????????
            log.debug("??????????????????????????????????????????");
            if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType()) || HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())) {
                //??????????????????????????????????????????????????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.9.12
                if (StringUtils.isNotBlank(dto.getContainerId())) {
                    MtContLoadDtlVO10 contLoadDtlParam = new MtContLoadDtlVO10();
                    contLoadDtlParam.setContainerId(dto.getContainerId());
                    contLoadDtlParam.setAllLevelFlag(HmeConstants.ConstantValue.NO);
                    List<MtContLoadDtlVO4> mtContLoadDtlVo1List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlParam);

                    if (CollectionUtils.isNotEmpty(mtContLoadDtlVo1List)) {
                        List<String> materialLotIds = mtContLoadDtlVo1List.stream().filter(t -> t.getMaterialLotId() != null)
                                .map(MtContLoadDtlVO4::getMaterialLotId).distinct().collect(Collectors.toList());
                        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);

                        // UPDATE 20201021 YC ????????????
                        if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
                            List<String> eoIds = mtMaterialLotList.stream().map(MtMaterialLot::getEoId).distinct()
                                    .collect(Collectors.toList());
                            List<HmeEoJobSnVO> hmeEoJobSnVOList =
                                    hmeEoJobSnMapper.selectLastestJobSnOfEoBatch(tenantId, eoIds);
                            if (CollectionUtils.isEmpty(hmeEoJobSnVOList) || hmeEoJobSnVOList.stream()
                                    .anyMatch(t -> !t.getOperationId().equals(dto.getOperationId()))) {
                                // ?????????????????????????????????????????????????????????
                                throw new MtException("HME_EO_JOB_SN_058", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_058", "HME"));
                            }
                        }

                        /*if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
                            for (MtMaterialLot mtMaterialLot : mtMaterialLotList) {
                                HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnMapper.selectLastestJobSnOfEo(tenantId, mtMaterialLot.getEoId());
                                if (Objects.nonNull(hmeEoJobSnVO)) {
                                    if (hmeEoJobSnVO.getOperationId().equals(dto.getOperationId())) {
                                        continue;
                                    }
                                }
                                //?????????????????????????????????????????????????????????
                                throw new MtException("HME_EO_JOB_SN_058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_058", "HME"));
                            }
                        }*/
                    }
                }
            }
        }

        //V20201014 modify by penglin.sui for lu.bai ????????????????????????,?????????????????????
        if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType()) || HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType()) || HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            if (!YES.equals(dto.getReworkFlag())) {
                //??????????????????
                List<MtEoVO20> eoComponentList = new ArrayList<MtEoVO20>();
                List<MtEoVO20> eoComponentList2 = new ArrayList<MtEoVO20>();
                List<HmePrepareMaterialVO> prepareMaterialVOList = new ArrayList<HmePrepareMaterialVO>();
                List<HmePrepareMaterialVO> prepareMaterialVOList2 = new ArrayList<HmePrepareMaterialVO>();
                if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                    //??????????????????
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getSnMaterialId());
                    if (Objects.isNull(mtMaterial)) {
                        //${1}????????? ?????????${2}
                        throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "??????", dto.getSnMaterialId()));
                    }
                    prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery2(tenantId, dto.getSiteId(), dto.getWorkOrderId(), mtMaterial.getMaterialCode());
                } else {
                    if (StringUtils.isNotBlank(dto.getOperationId())) {
                        MtEoVO19 mtEoVO19 = new MtEoVO19();
                        mtEoVO19.setEoId(dto.getEoId());
                        mtEoVO19.setOperationId(dto.getOperationId());
                        mtEoVO19.setRouterStepId(dto.getEoStepId());
                        eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                    }

                    MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
                    if (Objects.isNull(mtEo)) {
                        //${1}????????? ?????????${2}
                        throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "EO", dto.getEoId()));
                    }
                    dto.setWorkOrderId(mtEo.getWorkOrderId());
                }

                List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.INPUT_WO_TYPE", tenantId);
                if (CollectionUtils.isNotEmpty(lovValueDTOS)) {
                    List<String> valueList = lovValueDTOS.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                    MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
                    if (Objects.isNull(mtWorkOrder)) {
                        //${1}????????? ?????????${2}
                        throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "??????", dto.getWorkOrderId()));
                    }
                    if (valueList.contains(mtWorkOrder.getWorkOrderType())) {
                        //???????????????????????????
                        MtExtendSettings issuedFlagAttr = new MtExtendSettings();
                        issuedFlagAttr.setAttrName("ISSUED_FLAG");
                        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                "mt_mod_production_line_attr", "PROD_LINE_ID", mtWorkOrder.getProductionLineId(),
                                Collections.singletonList(issuedFlagAttr));
                        String issuedFlag = HmeConstants.ConstantValue.NO;
                        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                            if (StringUtils.isNotBlank(mtExtendAttrVOList.get(0).getAttrValue())) {
                                issuedFlag = mtExtendAttrVOList.get(0).getAttrValue();
                            }
                        }
                        if (!YES.equals(issuedFlag)) {
                            List<String> materialIdList = new ArrayList<String>();
                            Map<String, BigDecimal> bomComponentMap = new HashMap<>();
                            List<String> substituteMaterialIdList = new ArrayList<String>();
                            String type = "";
                            if (CollectionUtils.isNotEmpty(prepareMaterialVOList)) {
                                prepareMaterialVOList2 = prepareMaterialVOList.stream().filter(item -> item.getComponentQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(prepareMaterialVOList2)) {
                                    // UPDATE 20201021 YC ????????????
                                    List<String> materialIds = prepareMaterialVOList2.stream()
                                            .map(HmePrepareMaterialVO::getMaterialId).distinct()
                                            .collect(Collectors.toList());

                                    Map<String, HmeEoJobSnVO10> materialAttrMap =
                                            getMaterialAttrBatch(tenantId, dto.getSiteId(), materialIds)
                                                    .stream()
                                                    .collect(Collectors.toMap(
                                                            HmeEoJobSnVO10::getMaterialId,
                                                            t -> t));

                                    List<String> bomComponentIds = prepareMaterialVOList2.stream()
                                            .map(HmePrepareMaterialVO::getBomComponentId).distinct()
                                            .collect(Collectors.toList());

                                    Map<String, Boolean> bomComponentAttrMap = hmeEoJobSnLotMaterialRepository
                                            .getBomComponentAttrBatch(tenantId, bomComponentIds).stream()
                                            .collect(Collectors.toMap(HmeEoJobSnBomCompAttrVO::getBomComponentId,
                                                    HmeEoJobSnBomCompAttrVO::isVirtualComponentFlag));

                                    for (HmePrepareMaterialVO woComponent : prepareMaterialVOList2) {
                                        HmeEoJobSnVO10 materialAttr = materialAttrMap.get(woComponent.getMaterialId());
                                        type = materialAttr.getMaterialType();
                                        if (!HmeConstants.MaterialTypeCode.LOT.equals(type)
                                                && !HmeConstants.MaterialTypeCode.TIME.equals(type)) {
                                            continue;
                                        }
                                        // ???????????????
                                        if (materialAttr.isBackFlush()) {
                                            continue;
                                        }
                                        // ??????????????????????????? modify by yuchao.wang for lu.bai at 2021.1.21
                                        if (!bomComponentAttrMap.get(woComponent.getBomComponentId())) {
                                            continue;
                                        }
                                        // ????????????????????????
                                        if (materialAttr.isIssuedFlag()) {
                                            continue;
                                        }
                                        bomComponentMap.put(woComponent.getMaterialId(),
                                                woComponent.getComponentQty().multiply(dto.getPrepareQty()));

                                        /*type = getMaterialType(tenantId, dto.getSiteId(), woComponent.getMaterialId());
                                        if (!HmeConstants.MaterialTypeCode.LOT.equals(type) && !HmeConstants.MaterialTypeCode.TIME.equals(type)) {
                                            continue;
                                        }
                                        //???????????????
                                        if (checkBackFlush(tenantId, dto.getSiteId(), woComponent.getMaterialId())) {
                                            continue;
                                        }
                                        //?????????????????????
                                        if (checkVirtualComponent(tenantId, woComponent.getBomComponentId())) {
                                            continue;
                                        }
                                        //????????????????????????
                                        if (!checkIssuedFlag(tenantId, dto.getSiteId(), woComponent.getMaterialId())) {
                                            continue;
                                        }
                                        bomComponentMap.put(woComponent.getMaterialId(), woComponent.getComponentQty().multiply(dto.getPrepareQty()));*/
                                    }
                                }
                            }
                            if (CollectionUtils.isNotEmpty(eoComponentList)) {
                                eoComponentList2 = eoComponentList.stream().filter(item -> BigDecimal.valueOf(item.getComponentQty()).compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(eoComponentList2)) {
                                    // UPDATE 20201021 YC ????????????
                                    List<String> materialIds = eoComponentList2.stream()
                                            .map(MtEoVO20::getMaterialId).distinct()
                                            .collect(Collectors.toList());

                                    Map<String, HmeEoJobSnVO10> materialAttrMap =
                                            getMaterialAttrBatch(tenantId, dto.getSiteId(), materialIds)
                                                    .stream()
                                                    .collect(Collectors.toMap(
                                                            HmeEoJobSnVO10::getMaterialId,
                                                            t -> t));

                                    List<String> bomComponentIds = eoComponentList2.stream()
                                            .map(MtEoVO20::getBomComponentId).distinct()
                                            .collect(Collectors.toList());

                                    Map<String, Boolean> bomComponentAttrMap = hmeEoJobSnLotMaterialRepository
                                            .getBomComponentAttrBatch(tenantId, bomComponentIds).stream()
                                            .collect(Collectors.toMap(HmeEoJobSnBomCompAttrVO::getBomComponentId,
                                                    HmeEoJobSnBomCompAttrVO::isVirtualComponentFlag));
                                    log.info("<======================???????????? bomComponentAttrMap.size()====================>:" + bomComponentAttrMap.size());
                                    for (MtEoVO20 eoComponent : eoComponentList2) {
                                        log.info("<======================???????????? eoComponent.getBomComponentId()====================>:" + eoComponent.getBomComponentId());
                                        HmeEoJobSnVO10 materialAttr = materialAttrMap.get(eoComponent.getMaterialId());
                                        type = materialAttr.getMaterialType();

                                        log.info("<======================???????????? materialAttr.getMaterialType()====================>:" + materialAttr.getMaterialType());
                                        log.info("<======================???????????? materialAttr.isBackFlush()====================>:" + materialAttr.isBackFlush());
                                        log.info("<======================???????????? bomComponentAttrMap.get(eoComponent.getBomComponentId())====================>:" + bomComponentAttrMap.get(eoComponent.getBomComponentId()));
                                        log.info("<======================???????????? materialAttr.isIssuedFlag()====================>:" + materialAttr.isIssuedFlag());

                                        if (!HmeConstants.MaterialTypeCode.LOT.equals(type) && !HmeConstants.MaterialTypeCode.TIME.equals(type)) {
                                            continue;
                                        }
                                        //???????????????
                                        if (materialAttr.isBackFlush()) {
                                            continue;
                                        }
                                        //?????????????????????
                                        if (bomComponentAttrMap.get(eoComponent.getBomComponentId())) {
                                            continue;
                                        }
                                        //????????????????????????
                                        if (materialAttr.isIssuedFlag()) {
                                            continue;
                                        }
                                        bomComponentMap.put(eoComponent.getMaterialId(), BigDecimal.valueOf(eoComponent.getComponentQty()));

                                        /*type = getMaterialType(tenantId, dto.getSiteId(), eoComponent.getMaterialId());
                                        if (!HmeConstants.MaterialTypeCode.LOT.equals(type) && !HmeConstants.MaterialTypeCode.TIME.equals(type)) {
                                            continue;
                                        }
                                        //???????????????
                                        if (checkBackFlush(tenantId, dto.getSiteId(), eoComponent.getMaterialId())) {
                                            continue;
                                        }
                                        //?????????????????????
                                        if (checkVirtualComponent(tenantId, eoComponent.getBomComponentId())) {
                                            continue;
                                        }
                                        //????????????????????????
                                        if (!checkIssuedFlag(tenantId, dto.getSiteId(), eoComponent.getMaterialId())) {
                                            continue;
                                        }
                                        bomComponentMap.put(eoComponent.getMaterialId(), BigDecimal.valueOf(eoComponent.getComponentQty()));*/
                                    }
                                }
                            }
                            log.info("<======================???????????? bomComponentMap.size()====================>:" + bomComponentMap.size());
                            HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
                            hmeEoJobMaterialVO.setJobType(dto.getJobType());
                            hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
                            hmeEoJobMaterialVO.setEoStepId(dto.getEoStepId());
                            hmeEoJobMaterialVO.setEoId(dto.getEoId());
                            hmeEoJobMaterialVO.setWorkOrderId(dto.getWorkOrderId());

                            //??????????????????????????????????????????
                            List<HmeEoJobMaterial> hmeEoJobMaterials2 = new ArrayList<HmeEoJobMaterial>();
                            SecurityTokenHelper.close();
                            List<HmeEoJobMaterial> hmeEoJobMaterials = hmeEoJobMaterialMapper.selectNotIssuedJobMaterial(tenantId, dto.getJobId(), new ArrayList<String>());
                            if (CollectionUtils.isNotEmpty(hmeEoJobMaterials)) {
                                // UPDATE 20201021 YC ????????????
                                List<String> materialIds = hmeEoJobMaterials.stream()
                                        .map(HmeEoJobMaterial::getMaterialId).distinct()
                                        .collect(Collectors.toList());

                                Map<String, HmeEoJobSnVO10> materialAttrMap =
                                        getMaterialAttrBatch(tenantId, dto.getSiteId(), materialIds)
                                                .stream()
                                                .collect(Collectors.toMap(
                                                        HmeEoJobSnVO10::getMaterialId,
                                                        t -> t));

                                for (HmeEoJobMaterial hmeEoJobMaterial : hmeEoJobMaterials) {
                                    //????????????????????????
                                    log.info("<======================???????????? materialAttrMap.get(hmeEoJobMaterial.getMaterialId()).isIssuedFlag()====================>:" + materialAttrMap.get(hmeEoJobMaterial.getMaterialId()).isIssuedFlag());
                                    if (materialAttrMap.get(hmeEoJobMaterial.getMaterialId()).isIssuedFlag()) {
                                        continue;
                                    }

                                    /*if (!checkIssuedFlag(tenantId, dto.getSiteId(), hmeEoJobMaterial.getMaterialId())) {
                                        continue;
                                    }*/

                                    substituteMaterialIdList = hmeEoJobSnLotMaterialRepository.querySubstituteMaterial(tenantId, hmeEoJobMaterial.getMaterialId(), hmeEoJobMaterialVO, new ArrayList<MtWorkOrderVO8>(), eoComponentList);
                                    if (CollectionUtils.isEmpty(substituteMaterialIdList)) {
                                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeEoJobMaterial.getMaterialId());
                                        //?????????${1}???????????????,????????????
                                        throw new MtException("HME_EO_JOB_SN_115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "HME_EO_JOB_SN_115", "HME", mtMaterial.getMaterialCode()));
                                    }
                                    SecurityTokenHelper.close();
                                    hmeEoJobMaterials2 = hmeEoJobMaterialMapper.selectIssuedJobMaterial(tenantId, dto.getJobId(), substituteMaterialIdList);
                                    if (CollectionUtils.isEmpty(hmeEoJobMaterials2)) {
                                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeEoJobMaterial.getMaterialId());
                                        //?????????${1}???????????????,????????????
                                        throw new MtException("HME_EO_JOB_SN_115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "HME_EO_JOB_SN_115", "HME", mtMaterial.getMaterialCode()));
                                    }
                                    BigDecimal releaseQtySum = hmeEoJobMaterials2.stream().map(HmeEoJobMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                                    BigDecimal componentQtySum = BigDecimal.ZERO;
                                    for (HmeEoJobMaterial hmeEoJobMaterial2 : hmeEoJobMaterials2
                                    ) {
                                        if (bomComponentMap.containsKey(hmeEoJobMaterial2.getMaterialId())) {
                                            componentQtySum = componentQtySum.add(bomComponentMap.get(hmeEoJobMaterial2.getMaterialId()));
                                        }
                                    }
                                    if (releaseQtySum.compareTo(componentQtySum) < 0) {
                                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeEoJobMaterial.getMaterialId());
                                        //?????????${1}???????????????,????????????
                                        throw new MtException("HME_EO_JOB_SN_115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "HME_EO_JOB_SN_115", "HME", mtMaterial.getMaterialCode()));
                                    }
                                }
                            }

                            for (Map.Entry<String, BigDecimal> entry : bomComponentMap.entrySet()) {
                                log.info("<========================Map.Entry<String, BigDecimal> entry : bomComponentMap.entrySet()=======================>:" + entry.getKey() + "-" + entry.getValue());
                                List<String> materialIdList2 = new ArrayList<String>();
                                materialIdList2.add(entry.getKey());
                                BigDecimal releaseQtySum = hmeEoJobSnLotMaterialMapper.selectHaveReleaseSnLotSum(tenantId, dto.getJobId(), materialIdList2);
                                if (Objects.nonNull(releaseQtySum)) {
                                    if (releaseQtySum.compareTo(entry.getValue()) >= 0) {
                                        continue;
                                    }
                                }

                                substituteMaterialIdList = hmeEoJobSnLotMaterialRepository.querySubstituteMaterial(tenantId, entry.getKey(), hmeEoJobMaterialVO, new ArrayList<MtWorkOrderVO8>(), eoComponentList);
                                if (CollectionUtils.isEmpty(substituteMaterialIdList)) {
                                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(entry.getKey());
                                    //?????????${1}???????????????,????????????
                                    throw new MtException("HME_EO_JOB_SN_115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EO_JOB_SN_115", "HME", mtMaterial.getMaterialCode()));
                                }
                                releaseQtySum = hmeEoJobSnLotMaterialMapper.selectHaveReleaseSnLotSum(tenantId, dto.getJobId(), substituteMaterialIdList);
                                if (Objects.isNull(releaseQtySum)) {
                                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(entry.getKey());
                                    //?????????${1}???????????????,????????????
                                    throw new MtException("HME_EO_JOB_SN_115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EO_JOB_SN_115", "HME", mtMaterial.getMaterialCode()));
                                }
                                if (releaseQtySum.compareTo(entry.getValue()) < 0) {
                                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(entry.getKey());
                                    //?????????${1}???????????????,????????????
                                    throw new MtException("HME_EO_JOB_SN_115", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EO_JOB_SN_115", "HME", mtMaterial.getMaterialCode()));
                                }
                            }
                        }
                    }
                }
            }

            if(HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType())){
                //V20210129 modify by penglin.sui for hui.ma ??????????????????
                if(StringUtils.isNotBlank(dto.getLabCode())){
                    //??????????????????
                    List<String> snMaterialLotIdList = new ArrayList<>();
                    snMaterialLotIdList.add(dto.getMaterialLotId());
                    Map<String , List<HmeMaterialLotLabCode>> hmeMaterialLotLabCodeMap = new HashMap<>();
                    List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = hmeMaterialLotLabCodeMapper.batchSelectLabCode(tenantId,dto.getEoStepId(),snMaterialLotIdList);
                    if(CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList)){
                        hmeMaterialLotLabCodeMap = hmeMaterialLotLabCodeList.stream().collect(Collectors.groupingBy(e -> e.getMaterialLotId()));
                    }
                    List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList1 = hmeMaterialLotLabCodeMap.getOrDefault(dto.getMaterialLotId() , new ArrayList<>());
                    if(CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList1)){
                        List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList2 = hmeMaterialLotLabCodeList1.stream().filter(item -> dto.getLabCode().equals(item.getLabCode()))
                                .collect(Collectors.toList());
                        if(CollectionUtils.isEmpty(hmeMaterialLotLabCodeList2)){
                            //????????????????????????????????????${1}??????????????????????????????${2}????????????,?????????
                            throw new MtException("HME_EO_JOB_SN_180", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_180", "HME" , dto.getLabCode() , hmeMaterialLotLabCodeList1.get(0).getLabCode()));
                        }
                    }
                }
            }
        }
    }

    /**
     * tagNcValidate
     *
     * @author penglin.sui@hand-china.com 2020/09/24 11:44
     */
    private List<HmeTagNcVO> tagNcValidate(Long tenantId, List<HmeEoJobDataRecordVO> dataRecordVOList) {
        List<HmeTagNcVO> hmeTagNcVOList = new ArrayList<HmeTagNcVO>();
        if (CollectionUtils.isEmpty(dataRecordVOList)) {
            return hmeTagNcVOList;
        }

        List<HmeEoJobDataRecordVO> dataRecordVOList2 = dataRecordVOList.stream()
                .filter(dataRecord -> StringUtils.isNotBlank(dataRecord.getResult()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(dataRecordVOList2)) {
            return hmeTagNcVOList;
        }

        List<HmeTagNc> hmeTagNcList = new ArrayList<HmeTagNc>();
        List<HmeTagNc> ngHmeTagNcList = new ArrayList<HmeTagNc>();

        // UPDATE 20201022 YC ????????????
        SecurityTokenHelper.close();
        List<HmeTagNc> hmeTagNcAllList = hmeTagNcRepository.selectByCondition(Condition.builder(HmeTagNc.class)
                .andWhere(Sqls.custom().andEqualTo(HmeTagNc.FIELD_TENANT_ID, tenantId).andIn(
                        HmeTagNc.FIELD_TAG_GROUP_ID,
                        dataRecordVOList2.stream().map(HmeEoJobDataRecordVO::getTagGroupId).distinct()
                                .collect(Collectors.toList())))
                .orderByAsc(HmeTagNc.FIELD_PRIORITY).build());

        // ???tagId???tagGroupId??????
        Map<HmeTagNcVO1, List<HmeTagNc>> groupMap = hmeTagNcAllList.stream().collect(Collectors.groupingBy(
                t -> new HmeTagNcVO1(t.getTagId(), t.getTagGroupId()),
                Collectors.collectingAndThen(Collectors
                                .toCollection(() -> new TreeSet<>(Comparator.comparing(HmeTagNc::getPriority))),
                        ArrayList::new)));

        // ???tagType???tagGroupId??????
        Map<HmeTagNcVO2, List<HmeTagNc>> typeMap = hmeTagNcAllList.stream().collect(Collectors.groupingBy(
                t -> new HmeTagNcVO2(t.getTagGroupId(), t.getTagType()),
                Collectors.collectingAndThen(Collectors
                                .toCollection(() -> new TreeSet<>(Comparator.comparing(HmeTagNc::getPriority))),
                        ArrayList::new)));

        // ??????NcCode
        Map<String, MtNcCode> mtNcCodeMap = mtNcCodeRepository
                .ncCodeBatchGet(tenantId,
                        hmeTagNcAllList.stream().map(HmeTagNc::getNcCodeId).distinct()
                                .collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(MtNcCode::getNcCodeId, t -> t));

        for (HmeEoJobDataRecordVO hmeEoJobDataRecordVO : dataRecordVOList2) {
            //??????tag_group_id + tag_id???hme_tag_nc???????????????
            /*hmeTagNcList = hmeTagNcRepository.selectByCondition(Condition.builder(HmeTagNc.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeTagNc.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeTagNc.FIELD_TAG_GROUP_ID, hmeEoJobDataRecordVO.getTagGroupId())
                            .andEqualTo(HmeTagNc.FIELD_TAG_ID, hmeEoJobDataRecordVO.getTagId())
                    )
                    .orderByAsc(HmeTagNc.FIELD_PRIORITY).build());*/
            hmeTagNcList = groupMap.get(
                    new HmeTagNcVO1(hmeEoJobDataRecordVO.getTagId(), hmeEoJobDataRecordVO.getTagGroupId()));
            if (CollectionUtils.isEmpty(hmeTagNcList)) {
                //??????tag_group_id + tag_type???hme_tag_nc???????????????
                /*hmeTagNcList = hmeTagNcRepository.selectByCondition(Condition.builder(HmeTagNc.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeTagNc.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(HmeTagNc.FIELD_TAG_GROUP_ID, hmeEoJobDataRecordVO.getTagGroupId())
                                .andEqualTo(HmeTagNc.FIELD_TAG_TYPE, hmeEoJobDataRecordVO.getTagType())
                        )
                        .orderByAsc(HmeTagNc.FIELD_PRIORITY).build());*/
                hmeTagNcList = typeMap.get(new HmeTagNcVO2(hmeEoJobDataRecordVO.getTagGroupId(),
                        hmeEoJobDataRecordVO.getTagType()));
                if (CollectionUtils.isEmpty(hmeTagNcList)) {
                    continue;
                }
            }
            if (!hmeEoJobDataRecordVO.getResult().matches("\\d+")) {
                continue;
            }
            BigDecimal result = new BigDecimal(hmeEoJobDataRecordVO.getResult());

            ngHmeTagNcList = hmeTagNcList.stream()
                    .filter(tagNc -> tagNc.getMinValue().compareTo(result) > 0 ||
                            tagNc.getMaxValue().compareTo(result) < 0)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(ngHmeTagNcList)) {
                continue;
            }

            for (HmeTagNc hmeTagNc : ngHmeTagNcList) {
                HmeTagNcVO hmeTagNcVO = new HmeTagNcVO();
                BeanUtils.copyProperties(hmeTagNc, hmeTagNcVO);
                hmeTagNcVO.setTagCode(hmeEoJobDataRecordVO.getTagCode());
                hmeTagNcVO.setTagDescription(hmeEoJobDataRecordVO.getTagDescription());

                // MtNcCode mtNcCode = mtNcCodeRepository.ncCodePropertyGet(tenantId, hmeTagNc.getNcCodeId());
                MtNcCode mtNcCode = mtNcCodeMap.get(hmeTagNc.getNcCodeId());
                if (Objects.isNull(mtNcCode)) {
                    //${1}????????? ?????????${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "ncCode", hmeTagNc.getNcCodeId()));
                }
                hmeTagNcVO.setNcCode(mtNcCode.getNcCode());
                hmeTagNcVO.setNcCodeDescription(mtNcCode.getDescription());
                hmeTagNcVOList.add(hmeTagNcVO);
            }
        }
        return hmeTagNcVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSn outSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        //DetailsHelper.setCustomUserDetails(6L, "zh_CN");
        long start = System.currentTimeMillis();
        long before;

        // ??????????????????
        final Date currentDate = CommonUtils.currentTimeGet();
        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        if (!HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
            log.debug("TEST-!TIME_PROCESS");
            //??????????????????????????????????????? add by yuchao.wang for lu.bai at 2020.10.4
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("MATERIAL_PREPARE_COMPLETE");
                }});

                MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
                mtLotUpdate.setMaterialLotId(dto.getMaterialLotId());
                mtLotUpdate.setEnableFlag(YES);
                mtLotUpdate.setEventId(eventId);
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);
            }

            MtExtendSettings reworkAttr = new MtExtendSettings();
            reworkAttr.setAttrName("REWORK_FLAG");

            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", dto.getMaterialLotId(),
                    Collections.singletonList(reworkAttr));
            String reworkFlag;
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                if (YES.equals(mtExtendAttrVOList.get(0).getAttrValue())) {
                    reworkFlag = YES;
                } else {
                    reworkFlag = HmeConstants.ConstantValue.NO;
                }
            } else {
                reworkFlag = HmeConstants.ConstantValue.NO;
            }

            //V20200826 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????
            MtMaterialLot currentSn =
                    mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
            if (HmeConstants.ConstantValue.NG.equals(currentSn.getQualityStatus())) {

                //V20200826 modify by penglin.sui for tianyang.xie ??????????????????????????????

                HmeEoJobSn hmeEoJobSnPara = new HmeEoJobSn();
                hmeEoJobSnPara.setTenantId(tenantId);
                hmeEoJobSnPara.setEoId(dto.getEoId());
                hmeEoJobSnPara.setWorkcellId(dto.getWorkcellId());
                HmeEoJobSn hmeEoJobSn = hmeEoJobSnMapper.selectLastestJobSnOfEoWkc(tenantId, hmeEoJobSnPara);
                if (Objects.nonNull(hmeEoJobSn)) {
                    if (!YES.equals(hmeEoJobSn.getReworkFlag())) {
                        //????????????????????????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_060", "HME"));
                    }
                }

                SecurityTokenHelper.close();
                List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.selectByCondition(Condition.builder(HmeEoJobSn.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(HmeEoJobSn.FIELD_EO_ID, dto.getEoId())
                                .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE)
                                .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG, YES)).build());
                if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
                    //??????????????????
                    throw new MtException("HME_EO_JOB_SN_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_061", "HME"));
                }
            }

            // ????????????????????????????????????
            dto.setIsDataRecordValidate(true);
            if (dto.getOutSiteAction() != null) {
                if (REWORK.equals(dto.getOutSiteAction())) {
                    if (HmeConstants.ConstantValue.NO.equals(reworkFlag)) {
                        // ??????????????????SN??????????????????????????????????????????????????????????????????????????????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_037", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_037", "HME"));
                    }
                    dto.setIsDataRecordValidate(false);
                }

                if (YES.equals(reworkFlag)) {
                    if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                        if (StringUtils.isBlank(dto.getContinueFlag())) {
                            dto.setContinueFlag(HmeConstants.ConstantValue.NO);
                        }
                        if (YES.equals(dto.getContinueFlag())) {
                            dto.setIsDataRecordValidate(false);
                        } else {
                            // ?????????????????????????????????????????????????????????????????????????????????
                            HmeEoJobSn resultJobSn = new HmeEoJobSn();
                            resultJobSn.setErrorCode("HME_EO_JOB_SN_038");
                            resultJobSn.setErrorMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_038", "HME"));
                            return resultJobSn;
                        }
                    }
                }
            }
            // ??????SN????????????????????????
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnMapper.selectByPrimaryKey(dto.getJobId());
            dto.setJobId(hmeEoJobSn.getJobId());

            log.debug("TEST-??????????????????");
            before = System.currentTimeMillis();
            self().outSiteValidate(tenantId, dto);
            log.debug("TEST-??????????????????: " + (System.currentTimeMillis() - before));

            //V20200924 modify by penglin.sui for tianyang.xie ??????????????????
            if (YES.equals(StringUtils.isBlank(dto.getCheckTagNcFlag()) ? YES : dto.getCheckTagNcFlag())) {
                List<HmeTagNcVO> hmeTagNcVOList = tagNcValidate(tenantId, dto.getDataRecordVOList());
                if (CollectionUtils.isNotEmpty(hmeTagNcVOList)) {
                    hmeEoJobSn.setTagNcFlag(YES);
                    hmeEoJobSn.setHmeTagNcVOList(hmeTagNcVOList);
                    return hmeEoJobSn;
                }
            }

            MtEo mtEo = new MtEo();
            HmeEoJobSnVO5 snVO = hmeEoJobSnMapper.queryMaterialByLotId(tenantId, dto.getMaterialLotId());
            if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                // ???????????????????????????EO
                dto.setEoId(snVO.getEoId());
                mtEo = mtEoRepository.eoPropertyGet(tenantId, snVO.getEoId());
            }
            //V20201026 modify by penglin.sui for lu.bai ???????????????????????????
            if (!YES.equals(reworkFlag)) {
                if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
//                // ??????????????????????????????
//                HmeEoJobMaterial jobMaterialParam = new HmeEoJobMaterial();
//                jobMaterialParam.setTenantId(tenantId);
//                jobMaterialParam.setJobId(dto.getJobId());
//                jobMaterialParam.setWorkcellId(dto.getWorkcellId());
//                List<HmeEoJobMaterial> hmeEoJobMaterialList = hmeEoJobMaterialRepository.select(jobMaterialParam);
//                if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
//                    hmeEoJobMaterialRepository.materialOutSite(tenantId, hmeEoJobMaterialList);
//                }
//
//                // ??????????????????????????????
////                HmeEoJobMaterialVO jobLotMaterialCondition = new HmeEoJobMaterialVO();
////                jobLotMaterialCondition.setWorkcellId(dto.getWorkcellId());
////                jobLotMaterialCondition.setJobId(hmeEoJobSn.getJobId());
////                jobLotMaterialCondition.setEoStepId(hmeEoJobSn.getEoStepId());
////                List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = hmeEoJobLotMaterialRepository
////                                .matchedJobLotMaterialQuery(tenantId, jobLotMaterialCondition, null);
////                // ??????????????????????????????
////                HmeEoJobMaterialVO jobTimeMaterialCondition = new HmeEoJobMaterialVO();
////                jobTimeMaterialCondition.setWorkcellId(dto.getWorkcellId());
////                jobTimeMaterialCondition.setJobId(hmeEoJobSn.getJobId());
////                jobTimeMaterialCondition.setEoStepId(hmeEoJobSn.getEoStepId());
////                List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOList = hmeEoJobTimeMaterialRepository
////                                .matchedJobTimeMaterialQuery(tenantId, jobTimeMaterialCondition, null);
////
////                // ?????????????????????????????????????????????
////                if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialVOList)
////                                || CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialVOList)) {
////                   hmeEoJobSnLotMaterialRepository.lotMaterialOutSite(tenantId, dto, hmeEoJobLotMaterialVOList,
////                           hmeEoJobTimeMaterialVOList);
////                }
//                hmeEoJobSnLotMaterialRepository.lotMaterialOutSite2(tenantId, dto);
//            }else{
//                hmeEoJobSnLotMaterialRepository.prepareLotMaterialOutSite2(tenantId, dto);
                    before = System.currentTimeMillis();
                    hmeEoJobSnLotMaterialRepository.backFlushMaterialOutSite(tenantId, dto);
                    log.debug("TEST-backFlushMaterialOutSite: " + (System.currentTimeMillis() - before));
                } else {
                    before = System.currentTimeMillis();
                    hmeEoJobSnLotMaterialRepository.prepareBackFlushMaterialOutSite(tenantId, dto);
                    log.debug("TEST-backFlushMaterialOutSite: " + (System.currentTimeMillis() - before));
                }
            }

            // ??????????????????
            before = System.currentTimeMillis();
            HmeEoJobSn jobSnResult = jobSnOutSite(tenantId, dto);
            log.debug("TEST-jobSnOutSite: " + (System.currentTimeMillis() - before));

            // ??????????????????
            if (isContainerControl(tenantId, hmeEoJobSn.getWorkcellId())) {
                HmeEoJobContainer jobContainer = new HmeEoJobContainer();
                if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                    jobContainer = hmeEoJobContainerRepository.wkcLimitJobContainerGet(tenantId, dto.getWorkcellId());
                    if (Objects.isNull(jobContainer)) {
                        throw new MtException("HME_EO_JOB_SN_010", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_010", "HME"));
                    }
                } else {
                    log.debug("PDA???????????????");
                    // ????????????????????????????????????????????????
                    String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                        {
                            setEventTypeCode("PRODUCT_RECEIPT");
                        }
                    });
                    MtContainerType condition = new MtContainerType();
                    condition.setContainerTypeCode("PRODUCTION_CONTAINER");
                    condition.setEnableFlag(YES);
                    condition.setPackingLevel("MATERIAL_LOT");
                    List<String> containerTypeId = mtContainerTypeRepository.propertyLimitContainerTypeQuery(tenantId,
                            condition, HmeConstants.ConstantValue.NO);

                    if (CollectionUtils.isNotEmpty(containerTypeId)) {
                        MtContainerVO12 mtContainerVO12 = new MtContainerVO12();
                        mtContainerVO12.setSiteId(dto.getSiteId());
                        mtContainerVO12.setLocatorId(snVO.getLocatorId());
                        mtContainerVO12.setContainerCode(dto.getSnNum());
                        mtContainerVO12.setContainerTypeId(containerTypeId.get(0));
                        mtContainerVO12.setStatus("CANRELEASE");
                        mtContainerVO12.setEventId(eventId);

                        MtContainerVO26 mtContainerVO26 = mtContainerRepository.containerUpdate(tenantId,
                                mtContainerVO12, YES);
                        jobContainer.setContainerId(mtContainerVO26.getContainerId());
                    } else {
                        throw new MtException("HME_EO_JOB_SN_017", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "HME_EO_JOB_SN_017", "HME", condition.getContainerTypeCode()));
                    }
                }

                // ??????????????????
                String eventRequestId =
                        mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");

                // ??????{ containerLoadVerify }????????????????????????
                MtContainerVO9 mtContainerVo9 = new MtContainerVO9();
                mtContainerVo9.setContainerId(jobContainer.getContainerId());
                mtContainerVo9.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerVo9.setLoadObjectId(dto.getMaterialLotId());

                // ??????{ containerLoad }??????????????????
                MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
                mtContainerVO24.setContainerId(jobContainer.getContainerId());
                mtContainerVO24.setEventRequestId(eventRequestId);
                mtContainerVO24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerVO24.setLoadObjectId(dto.getMaterialLotId());

                if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                    mtContainerVo9.setTrxLoadQty(new Double(String.valueOf(dto.getPrepareQty())));
                    mtContainerVO24.setTrxLoadQty(new Double(String.valueOf(dto.getPrepareQty())));
                } else {
                    // ??????????????????????????????EO??????
                    mtContainerVo9.setTrxLoadQty(mtEo.getQty());
                    mtContainerVO24.setTrxLoadQty(mtEo.getQty());
                }
                // ??????????????????
                mtContainerRepository.containerLoadVerify(tenantId, mtContainerVo9);
                // ????????????
                mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
                // ??????????????????EO
                hmeEoJobSn.setJobContainerId(jobContainer.getJobContainerId());
            }

            // ??????????????????
            hmeEoJobSn.setSiteOutDate(currentDate);
            // ???????????????
            hmeEoJobSn.setSiteOutBy(userId);
            // ????????????????????????????????????????????????
            hmeEoJobSn.setMaterialLotId(dto.getMaterialLotId());
            hmeEoJobSnMapper.updateByPrimaryKey(hmeEoJobSn);

            // ????????????????????????
            if (StringUtils.isNotBlank(hmeEoJobSn.getEoStepId())) {
                MtRouterStep currentRouterStep =
                        mtRouterStepRepository.routerStepGet(tenantId, hmeEoJobSn.getEoStepId());
                hmeEoJobSn.setCurrentStepName(currentRouterStep.getStepName());
                // ???????????????????????????????????????
                List<MtRouterNextStep> mtRouterNextStepList =
                        mtRouterNextStepRepository.routerNextStepQuery(tenantId, hmeEoJobSn.getEoStepId());
                if (CollectionUtils.isNotEmpty(mtRouterNextStepList)) {
                    MtRouterNextStep mtRouterNextStep = mtRouterNextStepList.get(0);
                    MtRouterStep nextRouterStep =
                            mtRouterStepRepository.routerStepGet(tenantId, mtRouterNextStep.getNextStepId());
                    hmeEoJobSn.setNextStepName(nextRouterStep.getStepName());
                }
            }

            hmeEoJobSn.setSnMaterialCode(snVO.getMaterialCode());
            hmeEoJobSn.setSnMaterialName(snVO.getMaterialName());
            hmeEoJobSn.setWorkOrderNum(snVO.getWorkOrderNum());
            hmeEoJobSn.setRemark(snVO.getRemark());

            //???????????????/??????/PDA???????????? ?????????????????????????????? add by yuchao.wang for fang.pan at 2020.9.21
            if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType())
                    || HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())
                    || HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                HmeEoJobSn eoJobSn = new HmeEoJobSn();
                BeanUtils.copyProperties(hmeEoJobSn, eoJobSn);
                eoJobSn.setMaterialLotId(dto.getMaterialLotId());

                before = System.currentTimeMillis();
                self().wkcCompleteOutputRecord(tenantId, dto, Collections.singletonList(eoJobSn));
                log.debug("TEST-wkcCompleteOutputRecord: " + (System.currentTimeMillis() - before));
            }

            //V20210311 modify by penglin.sui for hui.ma ????????????????????????
            if(HmeConstants.ConstantValue.YES.equals(dto.getIsRecordLabCode()) && StringUtils.isNotBlank(dto.getLabCode())) {
                HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
                hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
                hmeMaterialLotLabCodePara.setRouterStepId(hmeEoJobSn.getEoStepId());
                hmeMaterialLotLabCodePara.setLabCode(dto.getLabCode());
                HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
                if (Objects.isNull(hmeMaterialLotLabCode)) {
                    HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    insertHmeMaterialLotLabCode.setTenantId(tenantId);
                    insertHmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                    insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                    insertHmeMaterialLotLabCode.setLabCode(dto.getLabCode());
                    insertHmeMaterialLotLabCode.setJobId(dto.getJobId());
                    insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                    insertHmeMaterialLotLabCode.setWorkOrderId(snVO.getWorkOrderId());
                    insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                    insertHmeMaterialLotLabCode.setRouterStepId(hmeEoJobSn.getEoStepId());
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

            //?????????????????? ????????????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.11.04
            if (!HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())) {
                sendTransactionInterface(tenantId, jobSnResult);
            } else {
                hmeEoJobSn.setWoCompleteTransactionResponseVOList(jobSnResult.getWoCompleteTransactionResponseVOList());
                hmeEoJobSn.setWoReportTransactionResponseVOList(jobSnResult.getWoReportTransactionResponseVOList());
            }
            log.debug("TEST-?????????????????????: " + (System.currentTimeMillis() - before));
            return hmeEoJobSn;
        } else {
            log.debug("TEST-TIME_PROCESS");
            if (StringUtils.isNotBlank(dto.getJobContainerId())) {
                HmeEoJobContainer jobContainer =
                        hmeEoJobContainerRepository.selectByPrimaryKey(dto.getJobContainerId());

                //V20200828 modify by penglin.sui for tianyang.xie ???????????????EO
                // ??????????????????
//                String eventRequestId =
//                                mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_CONTAINER_OUT_SITE");
//
//                dto.getSnLineList().forEach(jobSn -> {
//                    MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, jobSn.getEoId());
//                    // ??????{ containerLoadVerify }????????????????????????
//                    MtContainerVO9 mtContainerVo9 = new MtContainerVO9();
//                    mtContainerVo9.setContainerId(jobContainer.getContainerId());
//                    mtContainerVo9.setLoadObjectType("EO");
//                    mtContainerVo9.setLoadObjectId(jobSn.getEoId());
//                    mtContainerVo9.setTrxLoadQty(mtEo.getQty());
//                    mtContainerRepository.containerLoadVerify(tenantId, mtContainerVo9);
//
//                    // ??????{ containerLoad }??????????????????
//                    MtContainerVO24 mtContainerVO24 = new MtContainerVO24();
//                    mtContainerVO24.setContainerId(jobContainer.getContainerId());
//                    mtContainerVO24.setLoadObjectType("EO");
//                    mtContainerVO24.setLoadObjectId(jobSn.getEoId());
//                    mtContainerVO24.setEventRequestId(eventRequestId);
//                    mtContainerVO24.setTrxLoadQty(mtEo.getQty());
//                    mtContainerRepository.containerLoad(tenantId, mtContainerVO24);
//                });
                jobContainer.setSiteOutBy(userId);
                jobContainer.setSiteOutDate(currentDate);
                hmeEoJobContainerMapper.updateByPrimaryKey(jobContainer);
            }

            List<HmeEoJobSn> eoJobSnList = new ArrayList<>();

            // UPDATE 20201021 YC ????????????
            List<String> jobIds = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getJobId).distinct()
                    .collect(Collectors.toList());
            List<HmeEoJobSn> hmeEoJobSnList = selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andIn(HmeEoJobSn.FIELD_JOB_ID, jobIds))
                    .build());

            Map<String, HmeEoJobSn> hmeEoJobSnMap =
                    hmeEoJobSnList.stream().collect(Collectors.toMap(HmeEoJobSn::getJobId, t -> t));

            List<String> eoStepIds = hmeEoJobSnList.stream().map(HmeEoJobSn::getEoStepId).distinct()
                    .collect(Collectors.toList());

            Map<HmeEoJobSnVO13, HmeEoJobSn> hmeEoJobSnVO13Map = selectByCondition(Condition
                    .builder(HmeEoJobSn.class)
                    .select(HmeEoJobSn.FIELD_EO_ID, HmeEoJobSn.FIELD_OPERATION_ID, HmeEoJobSn.FIELD_EO_STEP_ID)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andIn(HmeEoJobSn.FIELD_JOB_ID, eoStepIds))
                    .build()).stream().collect(Collectors.groupingBy(
                    t -> new HmeEoJobSnVO13(t.getEoId(), t.getOperationId(), t.getEoStepId()),
                    Collectors.collectingAndThen(Collectors.reducing((c1,
                                                                      c2) -> c1.getCreationDate().getTime() > c2
                                    .getCreationDate().getTime() ? c1
                                    : c2),
                            Optional::get)));

            //?????????????????????????????????????????????completeProcess modify by yuchao.wang for lu.bai at 2020.10.28
            List<String> eoIdList = dto.getSnLineList().stream().map(HmeEoJobSnVO3::getEoId).distinct().collect(Collectors.toList());
            siteOutBatchComplete(tenantId, dto.getWorkcellId(), eoIdList);

            //??????/??????????????????
            List<WmsObjectTransactionResponseVO> woCompleteTransactionResponseVOList = new ArrayList<>();
            List<WmsObjectTransactionResponseVO> woReportTransactionResponseVOList = new ArrayList<>();
            List<String> sqlList = new ArrayList<>(dto.getSnLineList().size());
            for (HmeEoJobSnVO3 jobSn : dto.getSnLineList()) {
                // HmeEoJobSn hmeEoJobSn = selectByPrimaryKey(jobSn.getJobId());
                HmeEoJobSn hmeEoJobSn = hmeEoJobSnMap.get(jobSn.getJobId());
                jobSn.setEoStepId(hmeEoJobSn.getEoStepId());

                // ????????????????????????????????????????????????
                /*HmeEoJobSn snCondition2 = new HmeEoJobSn();
                snCondition2.setTenantId(tenantId);
                snCondition2.setEoId(jobSn.getEoId());
                snCondition2.setOperationId(jobSn.getOperationId());
                snCondition2.setEoStepId(jobSn.getEoStepId());
                HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnMapper.selectLastestJobSn(tenantId, snCondition2);
                if (Objects.nonNull(hmeEoJobSnVO)) {
                    if (hmeEoJobSnVO.getSiteOutDate() != null) {
                        // 	?????????SN????????????????????????
                        throw new MtException("HME_EO_JOB_SN_056",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_056", "HME"));
                    }
                }*/
                HmeEoJobSn eoJobSn = hmeEoJobSnVO13Map
                        .get(new HmeEoJobSnVO13(jobSn.getEoId(), jobSn.getOperationId(), jobSn.getEoStepId()));
                if (eoJobSn != null && eoJobSn.getSiteOutDate() != null) {
                    // ?????????SN????????????????????????
                    throw new MtException("HME_EO_JOB_SN_056", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_056", "HME"));
                }
                jobSn.setJobType(dto.getJobType());
                // ????????????
                HmeEoJobSn jobSnResult = jobSnOutSite(tenantId, jobSn);
                if (CollectionUtils.isNotEmpty(jobSnResult.getWoCompleteTransactionResponseVOList())) {
                    woCompleteTransactionResponseVOList.addAll(jobSnResult.getWoCompleteTransactionResponseVOList());
                }
                if (CollectionUtils.isNotEmpty(jobSnResult.getWoReportTransactionResponseVOList())) {
                    woReportTransactionResponseVOList.addAll(jobSnResult.getWoReportTransactionResponseVOList());
                }
                // ??????????????????
                hmeEoJobSn.setSiteOutDate(currentDate);
                // ???????????????
                hmeEoJobSn.setSiteOutBy(userId);

                hmeEoJobSn.setLastUpdateDate(currentDate);
                hmeEoJobSn.setLastUpdatedBy(userId);

                // hmeEoJobSnMapper.updateByPrimaryKey(hmeEoJobSn);
                sqlList.addAll(customDbRepository.getUpdateSql(hmeEoJobSn));

                eoJobSnList.add(hmeEoJobSn);
            }

            if (CollectionUtils.isNotEmpty(sqlList)) {
                jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }

            //??????????????????????????? ?????????????????????????????? add by yuchao.wang for fang.pan at 2020.9.21
            before = System.currentTimeMillis();
            self().wkcCompleteOutputRecord(tenantId, dto, eoJobSnList);
            log.debug("TEST-wkcCompleteOutputRecord: " + (System.currentTimeMillis() - before));

            //???????????????????????? modify by yuchao.wang for tianyang.xie at 2020.11.04
            HmeEoJobSn jobSnPara = new HmeEoJobSn();
            jobSnPara.setWoCompleteTransactionResponseVOList(woCompleteTransactionResponseVOList);
            jobSnPara.setWoReportTransactionResponseVOList(woReportTransactionResponseVOList);
            sendTransactionInterface(tenantId, jobSnPara);

            log.debug("TEST-?????????????????????: " + (System.currentTimeMillis() - before));
            return null;
        }
    }

    /**
     * @param tenantId   ??????ID
     * @param hmeEoJobSn ??????
     * @return void
     * @Description ????????????????????????
     * @author yuchao.wang
     * @date 2020/11/4 15:24
     */
    private void sendTransactionInterface(Long tenantId, HmeEoJobSn hmeEoJobSn) {
        if (Objects.isNull(hmeEoJobSn)) {
            return;
        }

        //????????????????????????,????????????????????????
        itfObjectTransactionIfaceService.sendSapProdMaterialMove(tenantId,hmeEoJobSn.getWoReportTransactionResponseVOList(),hmeEoJobSn.getWoCompleteTransactionResponseVOList());
    }

    /**
     * @param tenantId   ??????ID
     * @param workcellId ??????ID
     * @param eoIdList   eoIdList
     * @return void
     * @Description ????????????????????????
     * @author yuchao.wang
     * @date 2020/10/28 18:21
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void siteOutBatchComplete(Long tenantId, String workcellId, List<String> eoIdList) {
        long start = System.currentTimeMillis();
        //????????????EO
        List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIdList);
        if (CollectionUtils.isEmpty(mtEoList) || eoIdList.size() != mtEoList.size()) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO??????"));
        }
        Map<String, Double> eoQtyMap = new HashMap<String, Double>();
        Map<String, String> eoWoIdMap = new HashMap<String, String>();
        mtEoList.forEach(item -> {
            eoQtyMap.put(item.getEoId(), item.getQty());
            eoWoIdMap.put(item.getEoId(), item.getWorkOrderId());
        });

        //??????????????????????????????
        Map<String, HmeRouterStepVO> nearStepMap = batchSelectNearStepByEoIds(tenantId, eoIdList);

        // ??????????????????????????????
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

        //??????????????????
        List<MtEoRouterActualVO40> eoRouterActualList = new ArrayList<>();
        eoIdList.forEach(eoId -> {
            MtEoRouterActualVO40 eoRouterActualVO40 = new MtEoRouterActualVO40();
            eoRouterActualVO40.setQty(eoQtyMap.get(eoId));
            eoRouterActualVO40.setEoStepActualId(nearStepMap.get(eoId).getEoStepActualId());
            eoRouterActualVO40.setWorkcellId(workcellId);
            eoRouterActualList.add(eoRouterActualVO40);
        });
        MtEoRouterActualVO39 eoRouterActualCompleteParam = new MtEoRouterActualVO39();
        eoRouterActualCompleteParam.setEventRequestId(eventRequestId);
        eoRouterActualCompleteParam.setSourceStatus("WORKING");
        eoRouterActualCompleteParam.setEoRouterActualList(eoRouterActualList);

        long before = System.currentTimeMillis();
//        mtEoRouterActualRepository.completeBatchProcess(tenantId, eoRouterActualCompleteParam);
        //20201029 modify by penglin.sui for tianyang.xie ?????????????????????????????????API
        List<MtEoRouterActualVO55> completeResultList = mtEoRouterActualRepository.completeBatchProcess(tenantId, eoRouterActualCompleteParam);
        log.debug("TEST-completeBatchProcess2: " + (System.currentTimeMillis() - before));
        log.debug("TEST-siteOutBatchComplete: " + (System.currentTimeMillis() - start));
    }

    /**
     * @param tenantId ??????ID
     * @param eoIdList eoIdList
     * @return java.util.Map<java.lang.String, com.ruike.hme.domain.vo.HmeRouterStepVO>
     * @Description ??????EO??????????????????????????????
     * @author yuchao.wang
     * @date 2020/10/28 18:18
     */
    private Map<String, HmeRouterStepVO> batchSelectNearStepByEoIds(Long tenantId, List<String> eoIdList) {
        Map<String, HmeRouterStepVO> nearStepMap = new HashMap<String, HmeRouterStepVO>();

        //??????EO????????????????????????
        List<HmeRouterStepVO> routerStepVOList = hmeEoJobSnMapper.batchSelectStepByEoIds(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(routerStepVOList)) {
            Map<String, List<HmeRouterStepVO>> eoRouterStepMap = routerStepVOList
                    .stream().collect(Collectors.groupingBy(HmeRouterStepVO::getEoId));

            //???????????????????????????eoId????????????
            if (Objects.isNull(eoRouterStepMap) || eoRouterStepMap.size() != eoIdList.size()) {
                // ?????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_045", "HME"));
            }

            //??????????????????
            for (Map.Entry<String, List<HmeRouterStepVO>> entry : eoRouterStepMap.entrySet()) {
                if (CollectionUtils.isEmpty(entry.getValue())) {
                    throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_045", "HME"));
                }

                //????????????????????????????????????
                List<HmeRouterStepVO> routerSteps = entry.getValue().stream().sorted(Comparator
                        .comparing(HmeRouterStepVO::getLastUpdateDate, Comparator.reverseOrder())).collect(Collectors.toList());

                //????????????????????????????????????????????????ID
                nearStepMap.put(entry.getKey(), routerSteps.get(0));
            }
        }

        return nearStepMap;
    }

    /**
     * @param tenantId ??????ID
     * @param eoIdList eoIdList
     * @return java.util.Map<java.lang.String, com.ruike.hme.domain.vo.HmeRouterStepVO>
     * @Description ??????EO??????????????????????????????
     * @author yuchao.wang
     * @date 2020/10/28 18:18
     */
    private Map<String, HmeRouterStepVO> batchSelectNormalStepByEoIds(Long tenantId, List<String> eoIdList) {
        Map<String, HmeRouterStepVO> normalStepMap = new HashMap<String, HmeRouterStepVO>();

        //??????EO????????????????????????
        List<HmeRouterStepVO> routerStepVOList = hmeEoJobSnMapper.batchSelectNormalStepByEoIds(tenantId, eoIdList);
        if (CollectionUtils.isNotEmpty(routerStepVOList)) {
            Map<String, List<HmeRouterStepVO>> eoRouterStepMap = routerStepVOList
                    .stream().collect(Collectors.groupingBy(HmeRouterStepVO::getEoId));

            //???????????????????????????eoId????????????
            if (Objects.isNull(eoRouterStepMap) || eoRouterStepMap.size() != eoIdList.size()) {
                // ?????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_045", "HME"));
            }

            //??????????????????
            for (Map.Entry<String, List<HmeRouterStepVO>> entry : eoRouterStepMap.entrySet()) {
                if (CollectionUtils.isEmpty(entry.getValue())) {
                    // ?????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_045", "HME"));
                }

                //????????????????????????????????????
                List<HmeRouterStepVO> routerSteps = entry.getValue().stream().sorted(Comparator
                        .comparing(HmeRouterStepVO::getLastUpdateDate, Comparator.reverseOrder())).collect(Collectors.toList());

                //????????????????????????????????????????????????ID
                normalStepMap.put(entry.getKey(), routerSteps.get(0));
            }
        }

        return normalStepMap;
    }

    /**
     *
     * @Description ??????EO??????????????????????????????
     *
     * @author penglin.sui
     * @date 2020/10/28 20:10
     * @param tenantId ??????ID
     * @param mtEoStepWipVO1List mtEoStepWipVO1List
     * @return java.util.Map<java.lang.String,tarzan.actual.domain.entity.MtEoStepWip>
     *
     */
    private Map<String, MtEoStepWip> batchSelectMtEoStepWipByEoIds(Long tenantId, List<MtEoStepWipVO1> mtEoStepWipVO1List) {
        List<MtEoStepWip> mtEoStepWipList1 = new ArrayList<>();
        for (MtEoStepWipVO1 mtEoStepWipVO1 : mtEoStepWipVO1List
        ) {
            MtEoStepWip mtEoStepWip = new MtEoStepWip();
            mtEoStepWip.setEoStepActualId(mtEoStepWipVO1.getEoStepActualId());
            mtEoStepWipList1.add(mtEoStepWip);
        }

        Map<String, MtEoStepWip> mtEoStepWipMap = new HashMap<String, MtEoStepWip>();

        //??????EO????????????????????????
        List<MtEoStepWip> mtEoStepWipList = hmeEoJobSnMapper.selectForEoWkcAndStepWipQuery(tenantId, mtEoStepWipList1);
        if (CollectionUtils.isEmpty(mtEoStepWipList)) {
            // ??????????????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_034", "HME"));
        }
        Map<String, List<MtEoStepWip>> eoRouterStepMap = mtEoStepWipList
                .stream().collect(Collectors.groupingBy(MtEoStepWip::getEoStepActualId));

        if (Objects.isNull(eoRouterStepMap) || eoRouterStepMap.size() != mtEoStepWipList1.size()) {
            // ??????????????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_034", "HME"));
        }

        for (Map.Entry<String, List<MtEoStepWip>> entry : eoRouterStepMap.entrySet()) {
            if (CollectionUtils.isEmpty(entry.getValue())) {
                // ??????????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_034", "HME"));
            }

            //????????????????????????????????????????????????ID
            mtEoStepWipMap.put(entry.getKey(), entry.getValue().get(0));
        }

        return mtEoStepWipMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobSn> batchOutSiteScan(Long tenantId, HmeEoJobSnVO6 dto) {
        if (CollectionUtils.isEmpty(dto.getSnLineList())) {
            throw new MtException("HME_EO_JOB_SN_018",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_018", "HME"));
        }
        List<HmeEoJobSnVO3> hmeEoJobSnVO3List = dto.getSnLineList();
        List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
        //??????/??????????????????
        List<WmsObjectTransactionResponseVO> woCompleteTransactionResponseVOList = new ArrayList<>();
        List<WmsObjectTransactionResponseVO> woReportTransactionResponseVOList = new ArrayList<>();

        //???????????????????????????????????????????????????completeBatchProcess modify by yuchao.wang for lu.bai at 2020.10.28
        List<String> eoIdList = hmeEoJobSnVO3List.stream().map(HmeEoJobSnVO3::getEoId).distinct().collect(Collectors.toList());
        long startDate = System.currentTimeMillis();
        siteOutBatchComplete(tenantId, dto.getWorkcellId(), eoIdList);
        long endDate = System.currentTimeMillis();
        log.info("=================================>??????????????????-completeBatchProcess????????????" + (endDate - startDate) + "??????");
        startDate = System.currentTimeMillis();
        for (HmeEoJobSnVO3 eoJobSnItem : hmeEoJobSnVO3List) {
            eoJobSnItem.setJobType(dto.getJobType());
            eoJobSnItem.setSiteId(dto.getSiteId());
            eoJobSnItem.setWorkcellId(dto.getWorkcellId());
            eoJobSnItem.setOutSiteAction(dto.getOutSiteAction());
            eoJobSnItem.setContinueFlag(dto.getContinueFlag());

            //?????????????????? ???????????????????????? modify by yuchao.wang for tianyang.xie at 2020.11.04
            HmeEoJobSn jobSnResult = self().outSiteScan(tenantId, eoJobSnItem);
            if (CollectionUtils.isNotEmpty(jobSnResult.getWoCompleteTransactionResponseVOList())) {
                woCompleteTransactionResponseVOList.addAll(jobSnResult.getWoCompleteTransactionResponseVOList());
            }
            if (CollectionUtils.isNotEmpty(jobSnResult.getWoReportTransactionResponseVOList())) {
                woReportTransactionResponseVOList.addAll(jobSnResult.getWoReportTransactionResponseVOList());
            }
            hmeEoJobSnList.add(jobSnResult);
        }

        //???????????????????????? modify by yuchao.wang for tianyang.xie at 2020.11.04
        HmeEoJobSn jobSnPara = new HmeEoJobSn();
        jobSnPara.setWoCompleteTransactionResponseVOList(woCompleteTransactionResponseVOList);
        jobSnPara.setWoReportTransactionResponseVOList(woReportTransactionResponseVOList);
        sendTransactionInterface(tenantId, jobSnPara);

        endDate = System.currentTimeMillis();
        log.info("=================================>??????????????????-hmeEoJobSnVO3List???????????????" + hmeEoJobSnVO3List.size());
        log.info("=================================>??????????????????-hmeEoJobSnVO3List??????????????????" + (endDate - startDate) + "??????");
        return hmeEoJobSnList;
    }

    /**
     * ??????????????????
     *
     * @param tenantId ??????Id
     * @param dto      SN??????
     * @return ????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO2 createSnJob(Long tenantId, HmeEoJobSnVO3 dto) {
        long startDate = 0L;
        long endDate = 0L;
        HmeEoJobSnVO2 resultVO = new HmeEoJobSnVO2();
        BeanUtils.copyProperties(dto, resultVO);
        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // ??????????????????
        final Date currentDate = CommonUtils.currentTimeGet();

        HmeEoJobSn snJob = new HmeEoJobSn();
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
        snJob.setEoStepNum(dto.getEoStepNum());
        snJob.setReworkFlag(dto.getReworkFlag());
        snJob.setSourceContainerId(dto.getSourceContainerId());
        snJob.setJobContainerId(dto.getJobContainerId());

        //???????????????????????????ID
        if (YES.equals(dto.getReworkFlag())) {
            HmeEoJobSn snCondition = new HmeEoJobSn();
            snCondition.setTenantId(tenantId);
            snCondition.setEoId(dto.getEoId());
            snCondition.setOperationId(dto.getOperationId());
            snCondition.setEoStepId(dto.getEoStepId());
            snCondition.setReworkFlag(HmeConstants.ConstantValue.NO);
            HmeEoJobSn hmeEoJobSn = this.selectOne(snCondition);
            snJob.setSourceJobId(hmeEoJobSn.getJobId());
        }
        //V20201004 modify by penglin.sui for lu.bai ??????SN????????????
        if (Objects.nonNull(dto.getPrepareQty())) {
            snJob.setSnQty(dto.getPrepareQty());
        }

        self().insertSelective(snJob);

        BeanUtils.copyProperties(snJob, resultVO);
        resultVO.setMaterialId(dto.getMaterialId());
        resultVO.setPrepareQty(dto.getPrepareQty());
        resultVO.setBatchProcessSnScanFlag(dto.getBatchProcessSnScanFlag());

        HmeRouterStepVO5 currentRouterStep = null;
        if (StringUtils.isNotBlank(dto.getEoStepId())) {
            // ??????????????????
            currentRouterStep = hmeEoJobSnCommonService.queryCurrentAndNextStepByCurrentId(tenantId, dto.getEoStepId());
            resultVO.setCurrentStepName(currentRouterStep.getStepName());
            resultVO.setCurrentStepDescription(currentRouterStep.getDescription());
            resultVO.setCurrentStepSequence(currentRouterStep.getSequence());
            resultVO.setLabCode(currentRouterStep.getLabCode());

            // ?????????????????????????????????
            List<MtRouterNextStep> mtRouterNextStepList =
                    mtRouterNextStepRepository.routerNextStepQuery(tenantId, dto.getEoStepId());
            if (CollectionUtils.isNotEmpty(mtRouterNextStepList)) {
                MtRouterNextStep mtRouterNextStep = mtRouterNextStepList.get(0);
                MtRouterStep nextRouterStep =
                        mtRouterStepRepository.routerStepGet(tenantId, mtRouterNextStep.getNextStepId());
                resultVO.setNextStepName(nextRouterStep.getStepName());
                resultVO.setNextStepDescription(nextRouterStep.getDescription());
            }
        }

        //V20210320 modify by penglin.sui for hui.ma ????????????????????????????????????????????????SN?????????????????????
        if(Objects.isNull(currentRouterStep) ||
                (Objects.nonNull(currentRouterStep) && StringUtils.isBlank(currentRouterStep.getLabCode()) || StringUtils.isBlank(currentRouterStep.getRouterStepRemark()))) {
            HmeSnLabCode hmeSnLabCodePara = new HmeSnLabCode();
            hmeSnLabCodePara.setTenantId(tenantId);
            hmeSnLabCodePara.setOperationId(dto.getOperationId());
            hmeSnLabCodePara.setMaterialLotId(dto.getMaterialLotId());
            hmeSnLabCodePara.setEnabledFlag(HmeConstants.ConstantValue.YES);
            HmeSnLabCode hmeSnLabCode = hmeSnLabCodeMapper.selectOne(hmeSnLabCodePara);
            if (Objects.nonNull(hmeSnLabCode)) {
                resultVO.setLabCode(hmeSnLabCode.getLabCode());
            }
        }

        //????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        resultVO.setProductionVersion(mtWorkOrder.getProductionVersion());
        //???????????????
        MtMaterialBasic mtMaterialBasicPara = new MtMaterialBasic();
        mtMaterialBasicPara.setTenantId(tenantId);
        mtMaterialBasicPara.setMaterialId(resultVO.getSnMaterialId());
        MtMaterialBasic mtMaterialBasic = mtMaterialBasisRepository.selectOne(mtMaterialBasicPara);
        if (Objects.nonNull(mtMaterialBasic)) {
            resultVO.setItemType(mtMaterialBasic.getItemType());
        }

        if (!HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
            HmeEoJobSnVO2 hmeEoJobSnVO2 = new HmeEoJobSnVO2();
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                startDate = System.currentTimeMillis();
                hmeEoJobSnVO2 = prepareInSite(tenantId, resultVO);
                endDate = System.currentTimeMillis();
                log.info("=================================>??????-createSnJob-prepareInSite????????????" + (endDate - startDate) + "ms");
                return hmeEoJobSnVO2;
            } else {
                startDate = System.currentTimeMillis();
                hmeEoJobSnVO2 = materialInSite(tenantId, resultVO);
                endDate = System.currentTimeMillis();
                log.info("=================================>??????-createSnJob-materialInSite????????????" + (endDate - startDate) + "ms");
                return hmeEoJobSnVO2;
            }

        } else {
            return null;
        }
    }

    /**
     * ????????????????????????
     *
     * @param tenantId ??????ID
     * @param dto      ????????????
     * @author liyuan.lv@hand-china.com 20.7.20 11:22:02
     */
    private HmeEoJobSn jobSnOutSite(Long tenantId, HmeEoJobSnVO3 dto) {
        //??????/??????????????????
        List<WmsObjectTransactionResponseVO> woCompleteTransactionResponseVOList = new ArrayList<>();
        List<WmsObjectTransactionResponseVO> woReportTransactionResponseVOList = new ArrayList<>();
        String batchId = Utils.getBatchId();
        // ????????????????????????????????????
        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            // ?????????JobSN?????????EO
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());

            long before = 0;

            //???????????????????????????????????????????????????completeProcess?????????/???????????????????????????completeProcess modify by yuchao.wang for lu.bai at 2020.10.28
            if (!HmeConstants.JobType.BATCH_PROCESS.equals(dto.getJobType())
                    && !HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                // ??????????????????
                HmeRouterStepVO nearStepVO = hmeEoJobSnMapper.selectNearStepByEoId(tenantId, dto.getEoId());
                if (Objects.isNull(nearStepVO)) {
                    // ?????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_045", "HME"));
                }

                // ??????????????????
                // ??????????????????
                String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_COMPLETED");

                before = System.currentTimeMillis();
                if (!REWORK.equals(dto.getOutSiteAction()))  {
                    // UPDATE 20201022 YC ??????????????? ?????????+???????????????????????????
                    MtEoRouterActualVO19 eoRouterActualCompleteParam = new MtEoRouterActualVO19();
                    eoRouterActualCompleteParam.setWorkcellId(dto.getWorkcellId());
                    eoRouterActualCompleteParam.setQty(mtEo.getQty());
                    eoRouterActualCompleteParam.setEoStepActualId(nearStepVO.getEoStepActualId());
                    eoRouterActualCompleteParam.setEventRequestId(eventRequestId);
                    eoRouterActualCompleteParam.setSourceStatus("WORKING");
                    mtEoRouterActualRepository.completeProcess(tenantId, eoRouterActualCompleteParam);
                }
                log.debug("TEST-completeProcess: " + (System.currentTimeMillis() - before));
            }

            MtWorkOrder workOrder = mtWorkOrderRepository.woPropertyGet(tenantId, mtEo.getWorkOrderId());
            if (StringUtils.isBlank(workOrder.getLocatorId())) {
                throw new MtException("HME_EO_JOB_SN_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_011", "HME"));
            }
            String doneStepEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "INV_UPDATE");

            //V20200929 modify by penglin.sui for tianyang.xie ????????????SN??????????????????????????????WKC????????????WKC???????????????????????????
            MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, dto.getWorkcellId());

            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setEventRequestId(doneStepEventRequestId);
                    setLocatorId(mtModLocator.getLocatorId());
                    setEventTypeCode("INV_ONHAND_INCREASE");
                }
            });
            String completeEventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {
                {
                    setEventRequestId(doneStepEventRequestId);
                    setLocatorId(mtModLocator.getLocatorId());
                    setEventTypeCode("EO_WKC_STEP_COMPLETE");
                }
            });

            if (YES
                    .equals(mtRouterDoneStepRepository.doneStepValidate(tenantId, dto.getEoStepId()))) {
                boolean emptyFlag = false;
                List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.NOT_CREATE_BATCH_NUM", tenantId);
                if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
                    List<String> operationIdList = lovValueDTOList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                    MtMaterialBasic mtMaterialBasic = hmeEoJobSnMapper.queryMaterialBasic(tenantId, mtEo.getSiteId(), mtEo.getMaterialId());
                    if (Objects.nonNull(mtMaterialBasic)) {
                        if (StringUtils.isNotBlank(mtMaterialBasic.getItemType())) {
                            if (CollectionUtils.isNotEmpty(operationIdList)) {
                                if (operationIdList.contains(mtMaterialBasic.getItemType())) {
                                    emptyFlag = true;
                                }
                            }
                        }
                    }
                }
                String lotCode = "";
                if (!emptyFlag) {
                    //????????????HME_MATERIAL_BATCH_NUM???????????????????????????
                    lotCode = profileClient.getProfileValueByOptions("HME_MATERIAL_BATCH_NUM");
                    if (StringUtils.isBlank(lotCode)) {
                        //????????????????????????,?????????????????????????????????????????????????????????HME_MATERIAL_BATCH_NUM???
                        throw new MtException("HME_EO_JOB_SN_113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_113", "HME"));
                    }
                    if (lotCode.length() != 10) {
                        //?????????????????????10???,?????????????????????????????????????????????????????????HME_MATERIAL_BATCH_NUM???
                        throw new MtException("HME_EO_JOB_SN_114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_114", "HME"));
                    }
                }

                //???????????????????????????????????? modify by yuchao.wang for can.wang at 2021.1.7
                if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                    // ?????????????????????????????????
                    MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                    mtInvOnhandQuantityVO9.setSiteId(mtEo.getSiteId());
                    mtInvOnhandQuantityVO9.setLocatorId(mtModLocator.getLocatorId());
                    mtInvOnhandQuantityVO9.setMaterialId(mtEo.getMaterialId());
                    mtInvOnhandQuantityVO9.setChangeQuantity(mtEo.getQty());
                    mtInvOnhandQuantityVO9.setEventId(eventId);
                    mtInvOnhandQuantityVO9.setLotCode(lotCode);

                    before = System.currentTimeMillis();
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
                    log.debug("TEST-onhandQtyUpdateProcess: " + (System.currentTimeMillis() - before));
                }

                //??????????????????????????????
                String soNum = "";
                String soLineNum = "";
                MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                mtExtendVO1.setTableName("mt_work_order_attr");
                mtExtendVO1.setKeyIdList(Collections.singletonList(dto.getWorkOrderId()));
                List<MtExtendVO5> attrs = new ArrayList<>();
                MtExtendVO5 extend1 = new MtExtendVO5();
                extend1.setAttrName("attribute1");
                attrs.add(extend1);
                MtExtendVO5 extend2 = new MtExtendVO5();
                extend2.setAttrName("attribute7");
                attrs.add(extend2);
                mtExtendVO1.setAttrs(attrs);
                List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                if (CollectionUtils.isNotEmpty(extendAttrList)) {
                    for (MtExtendAttrVO1 item : extendAttrList) {
                        if ("attribute1".equals(item.getAttrName())) {
                            soNum = item.getAttrValue();
                        } else if ("attribute7".equals(item.getAttrName())) {
                            soLineNum = item.getAttrValue();
                        }
                    }
                }

                // ?????????????????????, ?????????????????????, ??????????????????
                List<MtExtendVO5> mtLotExtendList = new ArrayList<>();
                // 20201126 ????????????????????????MF_FLAG
                if (!REWORK.equals(dto.getOutSiteAction())) {
                    MtExtendVO5 mfFlagExtend = new MtExtendVO5();
                    mfFlagExtend.setAttrName("MF_FLAG");
                    mfFlagExtend.setAttrValue("");
                    mtLotExtendList.add(mfFlagExtend);
                }

                //????????????????????????????????? modify by yuchao.wang for can.wang at 2021.1.15
                if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                    //V20200908 modify by penglin.sui for fang.pan  ??????mt_material_lot_attr???????????????MATERIAL_VERSION
                    MtExtendVO5 mfFlagExtend2 = new MtExtendVO5();
                    mfFlagExtend2.setAttrName("MATERIAL_VERSION");
                    mfFlagExtend2.setAttrValue(workOrder.getProductionVersion());
                    mtLotExtendList.add(mfFlagExtend2);

                    mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                        {
                            setKeyId(dto.getMaterialLotId());
                            setEventId(eventId);
                            setAttrs(mtLotExtendList);
                        }
                    });

                    MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
                    mtLotUpdate.setMaterialLotId(dto.getMaterialLotId());
                    mtLotUpdate.setEventId(eventId);
                    mtLotUpdate.setLot(lotCode);
                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);
                }

                mtLotExtendList.clear();
                //????????????????????????????????? modify by yuchao.wang for can.wang at 2021.1.15
                if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                    //V20200908 modify by penglin.sui for fang.pan ??????????????????-???????????????status???-????????????????????????COMPLETED???????????????
                    MtExtendVO5 mfFlagExtend3 = new MtExtendVO5();
                    mfFlagExtend3.setAttrName("STATUS");
                    mfFlagExtend3.setAttrValue("COMPLETED");
                    mtLotExtendList.add(mfFlagExtend3);
                }

                //?????????????????????????????? add by yuchao.wang for tianyang.xie at 2020.9.27
                MtExtendVO5 soNumExtend = new MtExtendVO5();
                soNumExtend.setAttrName("SO_NUM");
                soNumExtend.setAttrValue(soNum);
                mtLotExtendList.add(soNumExtend);
                MtExtendVO5 soLineNumExtend = new MtExtendVO5();
                soLineNumExtend.setAttrName("SO_LINE_NUM");
                soLineNumExtend.setAttrValue(soLineNum);
                mtLotExtendList.add(soLineNumExtend);

                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                    {
                        setKeyId(dto.getMaterialLotId());
                        setEventId(completeEventId);
                        setAttrs(mtLotExtendList);
                    }
                });

                //V20200814 modify by penglin.sui for fang.pan ??????hme_wo_sn_rel???
                HmeWoSnRel hmeWoSnRel = new HmeWoSnRel();
                hmeWoSnRel.setTenantId(tenantId);
                hmeWoSnRel.setSiteId(mtEo.getSiteId());
                hmeWoSnRel.setWorkOrderNum(workOrder.getWorkOrderNum());
                hmeWoSnRel.setSnNum(mtEo.getIdentification());
                List<HmeWoSnRel>relList = hmeWoSnRelRepository.select(hmeWoSnRel);
                if (CollectionUtils.isEmpty(relList)){
                    hmeWoSnRelRepository.insert(hmeWoSnRel);
                } else {
                    hmeWoSnRelMapper.updateByPrimaryKeySelective(hmeWoSnRel);
                }

                //?????????????????? add by yuchao.wang for tianyang.xie at 2020.9.14
                MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
                mtEventCreateVO.setWorkcellId(dto.getWorkcellId());
                mtEventCreateVO.setEventTypeCode("HME_EO_COMPLETE");
                String endEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

                //?????????????????????????????? modify by yuchao.wang for can.wang at 2021.1.15
                if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                    //??????????????????
                    List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
                    WmsObjectTransactionRequestVO woCompleteTransactionRequestVO = new WmsObjectTransactionRequestVO();
                    woCompleteTransactionRequestVO.setTransactionTypeCode("HME_WO_COMPLETE");
                    woCompleteTransactionRequestVO.setAttribute16(batchId);
                    woCompleteTransactionRequestVO.setEventId(endEventId);
                    woCompleteTransactionRequestVO.setMaterialLotId(dto.getMaterialLotId());
                    woCompleteTransactionRequestVO.setMaterialId(mtEo.getMaterialId());
                    woCompleteTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(mtEo.getQty()));
                    MtMaterialLot currentSn = mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());
                    woCompleteTransactionRequestVO.setLotNumber(lotCode);
                    MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtEo.getUomId());
                    if (Objects.nonNull(mtUom)) {
                        woCompleteTransactionRequestVO.setTransactionUom(mtUom.getUomCode());
                    }
                    woCompleteTransactionRequestVO.setTransactionTime(new Date());
                    woCompleteTransactionRequestVO.setPlantId(mtEo.getSiteId());
                    MtModLocator mtModLocator1 = hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId, mtModLocator.getLocatorId());
                    if (Objects.nonNull(mtModLocator1)) {
                        woCompleteTransactionRequestVO.setWarehouseId(mtModLocator1.getLocatorId());
                        woCompleteTransactionRequestVO.setWarehouseCode(mtModLocator1.getLocatorCode());
                    }
                    woCompleteTransactionRequestVO.setLocatorId(mtModLocator.getLocatorId());
                    woCompleteTransactionRequestVO.setLocatorCode(mtModLocator.getLocatorCode());
                    woCompleteTransactionRequestVO.setWorkOrderNum(workOrder.getWorkOrderNum());
                    log.info("<=========================jobSnOutSite dto.getJobType()================================>:" + dto.getJobType());
                    // ?????????????????????????????????????????????
                    if (StringUtils.isNotBlank(dto.getEoStepId())) {
                        MtRouterStep currentRouterStep = mtRouterStepRepository.routerStepGet(tenantId, dto.getEoStepId());
                        if (Objects.isNull(currentRouterStep)) {
                            //${1}????????? ?????????${2}
                            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0037", "GENERAL", "routerStep", dto.getEoStepId()));
                        }
                        woCompleteTransactionRequestVO.setOperationSequence(currentRouterStep.getStepName());
                    }
                    woCompleteTransactionRequestVO.setContainerId(currentSn.getCurrentContainerId());
                    MtModProductionLine mtModProductionLine = mtModProductionLineRepository
                            .prodLineBasicPropertyGet(tenantId, mtEo.getProductionLineId());
                    if (Objects.nonNull(mtModProductionLine)) {
                        woCompleteTransactionRequestVO.setProdLineCode(mtModProductionLine.getProdLineCode());
                    }
                    woCompleteTransactionRequestVO.setSoNum(soNum);
                    woCompleteTransactionRequestVO.setSoLineNum(soLineNum);
                    //???????????????????????????MES_RK06 ???????????????????????? modify by yuchao.wang for jiao.chen at 2020.9.28
                    if ("MES_RK06".equals(workOrder.getWorkOrderType())) {
                        //????????????????????????????????????????????????SAP??????
                        HmeServiceSplitRecord hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryWoNumBySnNumAndWoId(tenantId, workOrder.getWorkOrderId(), dto.getSnNum());
                        if (Objects.isNull(hmeServiceSplitRecord)) {
                            //??????????????????????????????
                            throw new MtException("HME_EO_JOB_SN_084", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_084", "HME"));
                        }

                        //???????????????????????????????????????????????????????????????????????????SAP?????????
                        String internalOrderNum = hmeServiceSplitRecord.getInternalOrderNum();
                        if (StringUtils.isBlank(internalOrderNum)) {
                            if (StringUtils.isNotBlank(hmeServiceSplitRecord.getTopSplitRecordId())) {
                                hmeServiceSplitRecord = hmeServiceSplitRecordMapper.queryWoNumBySplitRecordId(tenantId, hmeServiceSplitRecord.getTopSplitRecordId());
                                if (Objects.nonNull(hmeServiceSplitRecord)) {
                                    internalOrderNum = hmeServiceSplitRecord.getInternalOrderNum();
                                }
                            }

                            if (StringUtils.isBlank(internalOrderNum)) {
                                //???????????????????????????????????????????????????
                                throw new MtException("HME_EO_JOB_SN_085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_085", "HME"));
                            }
                        } else {
                            //????????????????????????????????????,??????????????????
                            woCompleteTransactionRequestVO.setTransactionTypeCode("WMS_INSDID_ORDER_S_R");
                        }
                        woCompleteTransactionRequestVO.setWorkOrderNum(internalOrderNum);

                        //??????????????????
                        WmsTransactionType transactionType = new WmsTransactionType();
                        transactionType.setTenantId(tenantId);
                        transactionType.setTransactionTypeCode(woCompleteTransactionRequestVO.getTransactionTypeCode());
                        transactionType = transactionTypeRepository.selectOne(transactionType);
                        if (Objects.nonNull(transactionType)) {
                            woCompleteTransactionRequestVO.setMoveType(transactionType.getMoveType());
                        }
                    } else {
                        //??????????????????
                        WmsObjectTransactionRequestVO woReportTransactionRequestVO = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(woCompleteTransactionRequestVO, woReportTransactionRequestVO);
                        woReportTransactionRequestVO.setTransactionTypeCode("HME_WORK_REPORT");

                        //??????????????????
                        List<String> transactionTypeCodeList = new ArrayList<>();
                        transactionTypeCodeList.add(woCompleteTransactionRequestVO.getTransactionTypeCode());
                        transactionTypeCodeList.add(woReportTransactionRequestVO.getTransactionTypeCode());
                        List<WmsTransactionType> transactionTypes = transactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class)
                                .andWhere(Sqls.custom().andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                                        .andIn(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, transactionTypeCodeList)
                                ).build());
                        if (CollectionUtils.isNotEmpty(transactionTypes)) {
                            transactionTypes.forEach(item -> {
                                if (woCompleteTransactionRequestVO.getTransactionTypeCode().equals(item.getTransactionTypeCode())) {
                                    woCompleteTransactionRequestVO.setMoveType(item.getMoveType());
                                } else if (woReportTransactionRequestVO.getTransactionTypeCode().equals(item.getTransactionTypeCode())) {
                                    woReportTransactionRequestVO.setMoveType(item.getMoveType());
                                }
                            });
                        }
                        List<WmsObjectTransactionRequestVO> objectTransactionRequestList1 = new ArrayList<>();
                        objectTransactionRequestList1.add(woReportTransactionRequestVO);
                        //??????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.11.04
                        woReportTransactionResponseVOList = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList1);

                    }

                    //101????????????,EO???WO?????????????????? ?????????????????? add by yuchao.wang for tianyang.xie at 2020.11.04
                    if ("101".equals(woCompleteTransactionRequestVO.getMoveType())
                            && !mtEo.getMaterialId().equals(workOrder.getMaterialId())) {
                        String attrName = hmeEoJobSnMapper.queryBomComponentAttrByEoId(tenantId, mtEo.getEoId(), mtEo.getMaterialId(), "lineAttribute24");
                        woCompleteTransactionRequestVO.setAttribute30(attrName);
                    }
                    objectTransactionRequestList.add(woCompleteTransactionRequestVO);
                    //??????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.11.04
                    woCompleteTransactionResponseVOList = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                }

                //V20200909 modify by penglin.sui for tianyang.xie ????????????????????????????????????????????????????????????????????????????????????
                if (HmeConstants.WorkOrderStatus.COMPLETED.equals(workOrder.getStatus())) {
                    List<MtEo> mtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class)
                            .andWhere(Sqls.custom().andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(MtEo.FIELD_WORK_ORDER_ID, mtEo.getWorkOrderId())
                                    .andEqualTo(MtEo.FIELD_STATUS, COMPLETED)
                                    .andNotEqualTo(MtEo.FIELD_MATERIAL_ID, workOrder.getMaterialId())).build());
                    if (mtEoList.size() > 0) {
                        //???????????????
                        List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateDTOList = new ArrayList<ErpReducedSettleRadioUpdateDTO>();
                        List<MtEo> completeMtEoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class)
                                .andWhere(Sqls.custom().andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtEo.FIELD_WORK_ORDER_ID, mtEo.getWorkOrderId())
                                        .andEqualTo(MtEo.FIELD_STATUS, COMPLETED)).build());

                        // UPDATE 20201021 YC ????????????
                        // ??????????????????
                        List<String> eoMaterialIds = mtEoList.stream().map(MtEo::getMaterialId).distinct().collect(Collectors.toList());
                        eoMaterialIds.add(workOrder.getMaterialId());

                        SecurityTokenHelper.close();
                        Map<String, String> materialMap = mtMaterialRepository.selectByCondition(Condition.builder(MtMaterial.class)
                                .select(MtMaterial.FIELD_MATERIAL_ID, MtMaterial.FIELD_MATERIAL_CODE)
                                .andWhere(Sqls.custom().andEqualTo(MtMaterial.FIELD_TENANT_ID, tenantId)
                                        .andIn(MtMaterial.FIELD_MATERIAL_ID, eoMaterialIds))
                                .build()).stream()
                                .collect(Collectors.toMap(MtMaterial::getMaterialId,
                                        MtMaterial::getMaterialCode));

                        BigDecimal mainMaterialQty = new BigDecimal(100);
                        BigDecimal eoSumQty = BigDecimal.valueOf(completeMtEoList.stream().mapToDouble(MtEo::getQty).sum());
                        String materialCode;

                        //????????????????????????????????????????????????????????????
                        Map<String, List<MtEo>> materialGroup = mtEoList.stream().collect(Collectors.groupingBy(MtEo::getMaterialId));
                        for (Map.Entry<String, List<MtEo>> materialEntry : materialGroup.entrySet()) {
                            materialCode = materialMap.get(materialEntry.getKey());
                            if (StringUtils.isEmpty(materialCode)) {
                                //???????????????${1}
                                throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_MATERIAL_0053", "HME", materialEntry.getKey()));
                            }

                            BigDecimal qty = BigDecimal.valueOf(materialEntry.getValue().stream().mapToDouble(MtEo::getQty).sum());
                            BigDecimal radio = qty.multiply(BigDecimal.valueOf(100)).divide(eoSumQty, 0, BigDecimal.ROUND_HALF_UP);
                            ErpReducedSettleRadioUpdateDTO erpReducedSettleRadioUpdateDTO = new ErpReducedSettleRadioUpdateDTO();
                            erpReducedSettleRadioUpdateDTO.setAUFNR(workOrder.getWorkOrderNum());
                            erpReducedSettleRadioUpdateDTO.setMATNR(materialCode);
                            erpReducedSettleRadioUpdateDTO.setPROZS(String.valueOf(radio));
                            erpReducedSettleRadioUpdateDTOList.add(erpReducedSettleRadioUpdateDTO);

                            //?????????????????????
                            mainMaterialQty = mainMaterialQty.subtract(radio);
                        }

                        //????????????????????? by yuchao.wang for tianyang.xie at 2020.11.16
                        materialCode = materialMap.get(workOrder.getMaterialId());
                        if (StringUtils.isEmpty(materialCode)) {
                            //???????????????${1}
                            throw new MtException("MT_MATERIAL_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MATERIAL_0053", "HME", workOrder.getMaterialId()));
                        }
                        ErpReducedSettleRadioUpdateDTO erpReducedSettleRadioUpdateDTO = new ErpReducedSettleRadioUpdateDTO();
                        erpReducedSettleRadioUpdateDTO.setAUFNR(workOrder.getWorkOrderNum());
                        erpReducedSettleRadioUpdateDTO.setMATNR(materialCode);
                        erpReducedSettleRadioUpdateDTO.setPROZS(String.valueOf(mainMaterialQty));
                        erpReducedSettleRadioUpdateDTOList.add(erpReducedSettleRadioUpdateDTO);

                        try {
                            itfWorkOrderIfaceService.erpReducedSettleRadioUpdateRestProxy(tenantId, erpReducedSettleRadioUpdateDTOList);
                        } catch (Exception e) {
                            log.error("=================?????????????????????????????????================={}:" + e.getMessage(), erpReducedSettleRadioUpdateDTOList);
                        }
                    }
                }
            }

            if (HmeConstants.OutSiteAction.COMPLETE.equals(dto.getOutSiteAction())) {
                // ????????????????????????????????????????????????OK, ???????????????????????????
                log.debug("?????????????????????");

                MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
                mtLotUpdate.setMaterialLotId(dto.getMaterialLotId());
                mtLotUpdate.setEventId(completeEventId);
                mtLotUpdate.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtLotUpdate.setLocatorId(mtModLocator.getLocatorId());
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);

                List<MtExtendVO5> mtLotExtendList = new ArrayList<>();
                MtExtendVO5 reworkFlagExtend = new MtExtendVO5();
                reworkFlagExtend.setAttrName("REWORK_FLAG");
                reworkFlagExtend.setAttrValue("");
                mtLotExtendList.add(reworkFlagExtend);

                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
                    {
                        setKeyId(dto.getMaterialLotId());
                        setEventId(completeEventId);
                        setAttrs(mtLotExtendList);
                    }
                });
            } else if (REWORK.equals(dto.getOutSiteAction())) {
                log.debug("??????????????????");
                MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
                mtLotUpdate.setMaterialLotId(dto.getMaterialLotId());
                mtLotUpdate.setEventId(completeEventId);
                mtLotUpdate.setLocatorId(mtModLocator.getLocatorId());
                mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);
            }

            //V20210126 modify by penglin.sui for hui.ma ??????????????????
            if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType())) {
                if (HmeConstants.ConstantValue.YES.equals(dto.getIsRecordLabCode()) && StringUtils.isNotBlank(dto.getLabCode())) {
                    HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
                    hmeMaterialLotLabCodePara.setMaterialLotId(dto.getMaterialLotId());
                    hmeMaterialLotLabCodePara.setRouterStepId(dto.getEoStepId());
                    hmeMaterialLotLabCodePara.setLabCode(dto.getLabCode());
                    HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
                    if (Objects.isNull(hmeMaterialLotLabCode)) {
                        HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                        insertHmeMaterialLotLabCode.setTenantId(tenantId);
                        insertHmeMaterialLotLabCode.setMaterialLotId(dto.getMaterialLotId());
                        insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                        insertHmeMaterialLotLabCode.setLabCode(dto.getLabCode());
                        insertHmeMaterialLotLabCode.setJobId(dto.getJobId());
                        insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                        insertHmeMaterialLotLabCode.setWorkOrderId(dto.getWorkOrderId());
                        insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                        insertHmeMaterialLotLabCode.setRouterStepId(dto.getEoStepId());
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
        }

        if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
            log.debug("????????????????????????=" + dto.getEquipmentList());
            // ?????????????????????
            if (CollectionUtils.isNotEmpty(dto.getEquipmentList())) {
                HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
                equSwitchParams.setJobId(dto.getJobId());
                equSwitchParams.setWorkcellId(dto.getWorkcellId());
                equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
                hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
            }
        }

        HmeEoJobSn result = new HmeEoJobSn();
        result.setWoCompleteTransactionResponseVOList(woCompleteTransactionResponseVOList);
        result.setWoReportTransactionResponseVOList(woReportTransactionResponseVOList);
        return result;
    }

    private boolean isContainerControl(Long tenantId, String workcellId) {
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName("mt_mod_workcell_attr");
        mtExtendVO.setKeyId(workcellId);
        mtExtendVO.setAttrName("CONTAINER_OUT");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        // ??????????????????
        return CollectionUtils.isNotEmpty(mtExtendAttrVOList)
                && YES.equals(mtExtendAttrVOList.get(0).getAttrValue());
    }

    @Override
    public List<HmeWorkOrderVO> workOrderQuery(Long tenantId, HmeWorkOrderVO dto) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        return hmeEoJobSnMapper.workOrderQuery(tenantId, userId, dto);
    }

    @Override
    public HmeEoJobSn materialLotLimitSnJobGet(Long tenantId, HmeEoJobSnVO3 dto) {
        HmeEoJobSn eoJobSn = new HmeEoJobSn();
        eoJobSn.setTenantId(tenantId);
        eoJobSn.setWorkcellId(dto.getWorkcellId());
        eoJobSn.setMaterialLotId(dto.getMaterialLotId());
        if (StringUtils.isNotBlank(dto.getEoStepId())) {
            eoJobSn.setEoStepId(dto.getEoStepId());
            eoJobSn.setEoStepNum(dto.getEoStepNum());
        }
        return hmeEoJobSnMapper.selectOne(eoJobSn);
    }

    @Override
    public List<HmePrepareMaterialVO> materialQuery(Long tenantId, HmeWorkOrderVO dto) {
        return hmeEoJobSnMapper.materialQuery(tenantId, dto.getSiteId(), dto);
    }

    @Override
    public HmePrepareMaterialVO materialPreparedQuery(Long tenantId, HmeWorkOrderVO dto) {
        return hmeEoJobSnMapper.selectPreparedQtyByMaterialId(tenantId, dto);
    }

    /**
     * @param tenantId      ??????ID
     * @param materialLotId ?????????ID
     * @param operationId   ??????ID
     * @Description ??????material_lot_id+??????ID+'????????????????????????CHIP_NUM_ENTERING'??????????????????????????????????????????????????????
     * @author yifan.xiong
     * @date 2020-8-28 17:48:24
     */
    @Override
    public boolean checkChipEnterFlag(Long tenantId, String materialLotId, String operationId) {
        Integer gettingChipFlag = hmeEoJobSnMapper.checkChipInputFlag(tenantId, materialLotId, operationId);
        return !Objects.isNull(gettingChipFlag) && 1 == gettingChipFlag;
    }

    @Override
    public Page<HmeLocatorOnhandQuantityVO> locatorOnhandQuantityQuery(Long tenantId, HmeLocatorOnhandQuantityDTO dto, PageRequest pageRequest) {
        if (StringUtils.isBlank(dto.getWorkcellId())) {
            //${1}????????????
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }
        if (CollectionUtils.isEmpty(dto.getLocatorTypeList())) {
            //${1}????????????
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }

        List<String> workcellLocatorTypeList = dto.getLocatorTypeList().stream().filter(material -> HmeConstants.LocaltorType.DEFAULT_STORAGE.equals(material)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(workcellLocatorTypeList)) {
            dto.setWorkcellLocatorTypeList(workcellLocatorTypeList);
        }
        List<String> prodLineLocatorTypeList = dto.getLocatorTypeList().stream().filter(material -> HmeConstants.LocaltorType.BACKFLUSH.equals(material) || HmeConstants.LocaltorType.PRE_LOAD.equals(material)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(prodLineLocatorTypeList)) {
            dto.setProdLineLocatorTypeList(prodLineLocatorTypeList);
        }

        return PageHelper.doPage(pageRequest, () -> hmeEoJobSnMapper.queryLocatorOnhandQuantity(tenantId, dto));
    }

    @Override
    public Page<HmeBackFlushVO> backFlushQuery(Long tenantId, HmeBackFlushDTO dto, PageRequest pageRequest) {
        //??????????????????
        MtModLocator backFlushLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId,HmeConstants.LocaltorType.BACKFLUSH,dto.getWorkcellId());
        dto.setLocatorId(backFlushLocator.getLocatorId());
        //??????????????????
        MtEoVO19 mtEoVO19 = new MtEoVO19();
        mtEoVO19.setEoId(dto.getEoId());
        mtEoVO19.setOperationId(dto.getOperationId());
        mtEoVO19.setRouterStepId(dto.getEoStepId());
        // ????????????wkc???????????????????????????
        List<MtEoVO20> eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
        List<String> bomComponentIdList = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(eoComponentList)){
            bomComponentIdList = eoComponentList.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
            dto.setBomComponentIdList(bomComponentIdList);
        }
        return PageHelper.doPage(pageRequest, () -> hmeEoJobSnMapper.queryBackFlush(tenantId,dto));
    }

    @Override
    public List<MtExtendAttrVO1> getVirtualComponent(Long tenantId, List<String> bomComponentIdList) {
        List<String> attrNameList = new ArrayList<String>();
        attrNameList.add("lineAttribute9");
        return hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentIdList, attrNameList);
    }

    /**
     * @param tenantId   ??????ID
     * @param workcellId ??????ID
     * @param jobType    ????????????
     * @return boolean
     * @Description ?????????????????????????????????????????????????????? true:????????????????????? false:???
     * @author yuchao.wang
     * @date 2020/10/26 15:07
     */
    @Override
    public boolean checkNotSiteOutByWkcId(Long tenantId, String workcellId, String jobType) {
        Integer notSiteOutFlag = hmeEoJobSnMapper.checkNotSiteOutByWkcId(tenantId, workcellId, jobType);
        return !(Objects.nonNull(notSiteOutFlag) && 1 == notSiteOutFlag);
    }

    /**
     * ?????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/11/14 13:27
     */
    private void invalidBarcodeProcess(Long tenantId, HmeEoJobSnVO2 dto) {
        // ???????????????
        List<HmeEoJobMaterial> hmeEoJobMaterialList = hmeEoJobMaterialMapper.selectNotReleaseEoJobMaterial(tenantId, dto.getJobId());
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = hmeEoJobLotMaterialMapper.selectNotReleaseLotMaterial(tenantId, dto.getWorkcellId(), dto.getJobId());
        List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList = hmeEoJobTimeMaterialMapper.selectNotReleaseTimeMaterial(tenantId, dto.getWorkcellId(), dto.getJobId());
        // ????????????????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
            List<String> jobMaterialIds = hmeEoJobMaterialList.stream().filter(c -> HmeConstants.ConstantValue.NO.equals(c.getEnableFlag()) ||
                    StringUtils.isBlank(c.getEnableFlag())).map(HmeEoJobMaterial::getJobMaterialId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(jobMaterialIds)) {
                hmeEoJobMaterialMapper.batchUpdateJobMaterial(jobMaterialIds);
            }
        }
        // ????????????????????????????????????
        List<String> jobLotMaterialIds = new ArrayList<>();
        List<String> delJobLotMaterialIds = new ArrayList<>();
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeEoJobLotMaterialList.stream().filter(c ->
                HmeConstants.ConstantValue.NO.equals(c.getEnableFlag()) || StringUtils.isBlank(c.getEnableFlag())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterials)) {
            for (HmeEoJobLotMaterial lotMaterial : hmeEoJobLotMaterials) {
                // ????????????+??????+??????????????????????????????????????????
                int lotMaterialCount = hmeEoJobLotMaterialMapper.getEoJobLotMaterialCount(tenantId, lotMaterial.getWorkcellId(),
                        lotMaterial.getMaterialId(), lotMaterial.getProductionVersion());
                // ????????????????????????????????????????????????
                if (lotMaterialCount > 1) {
                    delJobLotMaterialIds.add(lotMaterial.getJobMaterialId());
                } else {
                    jobLotMaterialIds.add(lotMaterial.getJobMaterialId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(jobLotMaterialIds)) {
            hmeEoJobLotMaterialMapper.batchUpdateJobLotMaterial(jobLotMaterialIds);
        }
        if (CollectionUtils.isNotEmpty(delJobLotMaterialIds)) {
            hmeEoJobLotMaterialMapper.batchDeleteByPrimary(delJobLotMaterialIds);
        }
        // ????????????????????????????????????
        List<String> jobTimeMaterialIds = new ArrayList<>();
        List<String> delJobTimeMaterialIds = new ArrayList<>();
        List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterials = hmeEoJobTimeMaterialList.stream().filter(c ->
                HmeConstants.ConstantValue.NO.equals(c.getEnableFlag()) || StringUtils.isBlank(c.getEnableFlag())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterials)) {
            for (HmeEoJobTimeMaterial timeMaterial : hmeEoJobTimeMaterials) {
                // ????????????+??????+??????????????????????????????????????????
                int timeMaterialCount = hmeEoJobTimeMaterialMapper.getEoJobTimeMaterialCount(tenantId, timeMaterial.getWorkcellId(),
                        timeMaterial.getMaterialId(), timeMaterial.getProductionVersion());
                // ????????????????????????????????????????????????
                if (timeMaterialCount > 1) {
                    delJobTimeMaterialIds.add(timeMaterial.getJobMaterialId());
                } else {
                    jobTimeMaterialIds.add(timeMaterial.getJobMaterialId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(jobTimeMaterialIds)) {
            hmeEoJobTimeMaterialMapper.batchUpdateJobTimeMaterial(jobTimeMaterialIds);
        }
        if (CollectionUtils.isNotEmpty(delJobTimeMaterialIds)) {
            hmeEoJobTimeMaterialMapper.batchDeleteByPrimary(delJobTimeMaterialIds);
        }
    }

    /**
     * ???????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/11/14 17:26
     */
    private void initJobMaterialProcess(Long tenantId, HmeEoJobSnVO2 dto) {

        //???????????????????????????????????????????????????
        List<HmeEoJobMaterial> hmeEoJobMaterialList = hmeEoJobMaterialMapper.selectNotReleaseEoJobMaterial(tenantId, dto.getJobId());
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = hmeEoJobLotMaterialMapper.selectNotReleaseLotMaterial(tenantId, dto.getWorkcellId(), dto.getJobId());
        List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList = hmeEoJobTimeMaterialMapper.selectNotReleaseTimeMaterial(tenantId, dto.getWorkcellId(), dto.getJobId());
        // ???????????????
        List<HmeEoJobMaterial> eoJobMaterialList = hmeEoJobMaterialMapper.selectReleaseJobMaterial(tenantId, dto.getJobId());
        List<HmeEoJobLotMaterial> eoJobLotMaterialList = hmeEoJobLotMaterialMapper.selectReleaseLotMaterial(tenantId, dto.getWorkcellId(), dto.getJobId());
        List<HmeEoJobTimeMaterial> eoJobTimeMaterialList = hmeEoJobTimeMaterialMapper.selectReleaseTimeMaterial(tenantId, dto.getWorkcellId(), dto.getJobId());

        MtEoVO19 mtEoVO19 = new MtEoVO19();
        mtEoVO19.setEoId(dto.getEoId());
        mtEoVO19.setOperationId(dto.getOperationId());
        mtEoVO19.setRouterStepId(dto.getEoStepId());
        List<MtEoVO20> eoComponentList = new ArrayList<>();
        List<HmePrepareMaterialVO> prepareMaterialVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList) || CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList) ||
                CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                //??????????????????
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getSnMaterialId());
                if (Objects.isNull(mtMaterial)) {
                    //${1}????????? ?????????${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "??????", dto.getSnMaterialId()));
                }
                prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery2(tenantId, dto.getSiteId(), dto.getWorkOrderId(), mtMaterial.getMaterialCode());
            } else {
                eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
            }
        }

        List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.EO_JOB_SN_UOM", tenantId);
        List<String> delSnJobMaterialIds = new ArrayList<>();
        List<String> delLotJobMaterialIds = new ArrayList<>();
        List<String> delTimeJobMaterialIds = new ArrayList<>();
        // ??????
        List<HmePrepareMaterialVO> prepareMaterials = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(prepareMaterialVOList)) {
            prepareMaterials.addAll(prepareMaterialVOList.stream().filter(c ->
                    c.getComponentQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList()));
            List<String> prepareMaterialIds = prepareMaterials.stream().map(HmePrepareMaterialVO::getMaterialId).distinct().collect(Collectors.toList());
            //????????????????????????????????????????????????????????????
            List<HmeEoJobSnMaterialUomVO> mtMaterialUomList = hmeEoJobSnMapper.queryMaterialUom(tenantId, prepareMaterialIds);
            List<MtMaterialSite> mtMaterialSiteList = hmeEoJobSnMapper.queryMaterialSite(tenantId, dto.getSiteId(), prepareMaterialIds);
            List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<MtExtendAttrVO1>();
            if (CollectionUtils.isNotEmpty(mtMaterialSiteList)) {
                mtExtendAttrVO1List = getMaterialSiteAttr(tenantId, mtMaterialSiteList);
            }
            for (HmePrepareMaterialVO vo : prepareMaterials) {
                List<MtMaterialSite> materialSiteIdList2 = mtMaterialSiteList.stream().filter(item ->
                        item.getMaterialId().equals(vo.getMaterialId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(materialSiteIdList2)) {
                    // ????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
                }
                // ???????????????????????????????????????????????????,?????????????????????????????????????????????????????????
                String lotType = HmeConstants.MaterialTypeCode.LOT;
                if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                    List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item ->
                            item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) &&
                                    "attribute14".equals(item.getAttrName())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                        if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                            lotType = mtExtendAttrVO1List2.get(0).getAttrValue();
                        }
                    }
                }
                List<HmeEoJobSnMaterialUomVO> mtMaterialUomList1 = mtMaterialUomList.stream().filter(item -> item.getMaterialId().equals(vo.getMaterialId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(mtMaterialUomList1)) {
                    //${1}????????? ?????????${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "??????", vo.getMaterialId()));
                }
                boolean isSplitLine = false;
                if (CollectionUtils.isNotEmpty(mtMaterialUomList1)) {
                    if (CollectionUtils.isNotEmpty(typeLov)) {
                        isSplitLine = typeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList()).contains(mtMaterialUomList1.get(0).getUomType());
                    }
                }
                BigDecimal prepareQty = dto.getPrepareQty() == null ? BigDecimal.ONE : dto.getPrepareQty();
                // ??????????????????????????????????????????
                boolean isCreateSnMaterial = false;
                if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
                    List<HmeEoJobMaterial> snJobMaterialList = hmeEoJobMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                            vo.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(snJobMaterialList)) {
                        BigDecimal sumReleaseQty = snJobMaterialList.stream().map(HmeEoJobMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal qty = sumReleaseQty.divide(prepareQty);
                        if (qty.compareTo(vo.getComponentQty()) != 0) {
                            delSnJobMaterialIds.addAll(snJobMaterialList.stream().map(HmeEoJobMaterial::getJobMaterialId).distinct().collect(Collectors.toList()));
                            isCreateSnMaterial = true;
                        }
                    } else {
                        isCreateSnMaterial = true;
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(eoJobMaterialList)) {
                        List<HmeEoJobMaterial> snJobMaterialList = eoJobMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                                vo.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(snJobMaterialList)) {
                            isCreateSnMaterial = true;
                        }
                    } else {
                        isCreateSnMaterial = true;
                    }
                }
                // ??????????????????????????????????????????
                boolean isCreateLotMaterial = false;
                if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList)) {
                    List<HmeEoJobLotMaterial> lotJobMaterialList = hmeEoJobLotMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                            vo.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(lotJobMaterialList)) {
                        BigDecimal sumReleaseQty = lotJobMaterialList.get(0).getReleaseQty() == null ? BigDecimal.ZERO :
                                lotJobMaterialList.get(0).getReleaseQty();
                        BigDecimal qty = sumReleaseQty.divide(prepareQty);
                        if (qty.compareTo(vo.getComponentQty()) != 0) {
                            // delLotJobMaterialIds.add(lotJobMaterialList.get(0).getJobMaterialId());
                            isCreateLotMaterial = true;
                        }
                    } else {
                        isCreateLotMaterial = true;
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(eoJobLotMaterialList)) {
                        List<HmeEoJobLotMaterial> lotJobMaterialList = eoJobLotMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                                vo.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(lotJobMaterialList)) {
                            isCreateLotMaterial = true;
                        }
                    } else {
                        isCreateLotMaterial = true;
                    }
                }
                // ??????????????????????????????????????????
                boolean isCreateTimeMaterial = false;
                if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
                    List<HmeEoJobTimeMaterial> timeJobMaterialList = hmeEoJobTimeMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                            vo.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(timeJobMaterialList)) {
                        BigDecimal sumReleaseQty = timeJobMaterialList.get(0).getReleaseQty() == null ? BigDecimal.ZERO :
                                timeJobMaterialList.get(0).getReleaseQty();
                        BigDecimal qty = sumReleaseQty.divide(prepareQty);
                        if (qty.compareTo(vo.getComponentQty()) != 0) {
                            // delTimeJobMaterialIds.add(timeJobMaterialList.get(0).getJobMaterialId());
                            isCreateTimeMaterial = true;
                        }
                    } else {
                        isCreateTimeMaterial = true;
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(eoJobTimeMaterialList)) {
                        List<HmeEoJobTimeMaterial> timeJobMaterialList = eoJobTimeMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                                vo.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(timeJobMaterialList)) {
                            isCreateTimeMaterial = true;
                        }
                    } else {
                        isCreateTimeMaterial = true;
                    }
                }
                if (HmeConstants.MaterialTypeCode.SN.equals(lotType) && isCreateSnMaterial) {
                    hmeEoJobMaterialRepository.initJobMaterial(tenantId, vo.getMaterialId(), isSplitLine, vo.getComponentQty(), vo.getBomComponentId(), dto);
                }
                if (HmeConstants.MaterialTypeCode.LOT.equals(lotType) && isCreateLotMaterial) {
                    hmeEoJobLotMaterialRepository.initLotMaterialList(tenantId, vo.getMaterialId(), vo.getComponentQty(), dto);
                }
                if (HmeConstants.MaterialTypeCode.TIME.equals(lotType) && isCreateTimeMaterial) {
                    String availableTime = "";
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                        List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item ->
                                item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) &&
                                        "attribute9".equals(item.getAttrName())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2) && StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                            availableTime = mtExtendAttrVO1List2.get(0).getAttrValue();
                        }
                    }

                    //??????????????????????????????
                    if (StringUtils.isBlank(availableTime)) {
                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(vo.getMaterialId());
                        //???????????????${1}??????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_070", "HME", mtMaterial.getMaterialCode()));
                    }
                    hmeEoJobTimeMaterialRepository.initTimeMaterialList(tenantId, vo.getMaterialId(), availableTime, vo.getComponentQty(), dto);
                }
            }
        }
        // ??????
        List<MtEoVO20> eoComponents = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoComponentList)) {
            eoComponents.addAll(eoComponentList.stream().filter(c ->
                    c.getComponentQty().compareTo(0D) > 0).collect(Collectors.toList()));
            List<String> eoCompMaterialIds = eoComponents.stream().map(MtEoVO20::getMaterialId).distinct().collect(Collectors.toList());
            //????????????????????????????????????????????????????????????
            List<HmeEoJobSnMaterialUomVO> mtMaterialUomList = hmeEoJobSnMapper.queryMaterialUom(tenantId, eoCompMaterialIds);
            List<MtMaterialSite> mtMaterialSiteList = hmeEoJobSnMapper.queryMaterialSite(tenantId, dto.getSiteId(), eoCompMaterialIds);
            List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<MtExtendAttrVO1>();
            if (CollectionUtils.isNotEmpty(mtMaterialSiteList)) {
                mtExtendAttrVO1List = getMaterialSiteAttr(tenantId, mtMaterialSiteList);
            }
            for (MtEoVO20 vo : eoComponents) {
                BigDecimal componentQty = BigDecimal.valueOf(vo.getComponentQty());
                List<MtMaterialSite> materialSiteIdList2 = mtMaterialSiteList.stream().filter(item ->
                        item.getMaterialId().equals(vo.getMaterialId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(materialSiteIdList2)) {
                    // ????????????????????????????????????
                    throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
                }
                // ???????????????????????????????????????????????????,?????????????????????????????????????????????????????????
                String lotType = HmeConstants.MaterialTypeCode.LOT;
                if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                    List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item ->
                            item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) &&
                                    "attribute14".equals(item.getAttrName())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                        if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                            lotType = mtExtendAttrVO1List2.get(0).getAttrValue();
                        }
                    }
                }
                List<HmeEoJobSnMaterialUomVO> mtMaterialUomList1 = mtMaterialUomList.stream().filter(item -> item.getMaterialId().equals(vo.getMaterialId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(mtMaterialUomList1)) {
                    //${1}????????? ?????????${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "??????", vo.getMaterialId()));
                }
                boolean isSplitLine = false;
                if (CollectionUtils.isNotEmpty(mtMaterialUomList1)) {
                    if (CollectionUtils.isNotEmpty(typeLov)) {
                        isSplitLine = typeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList()).contains(mtMaterialUomList1.get(0).getUomType());
                    }
                }
                BigDecimal prepareQty = dto.getPrepareQty() == null ? BigDecimal.ONE : dto.getPrepareQty();
                // ??????????????????????????????????????????
                boolean isCreateSnMaterial = false;
                if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
                    List<HmeEoJobMaterial> snJobMaterialList = hmeEoJobMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                            vo.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(snJobMaterialList)) {
                        BigDecimal sumReleaseQty = snJobMaterialList.stream().map(HmeEoJobMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal qty = sumReleaseQty.divide(prepareQty);
                        if (qty.compareTo(componentQty) != 0) {
                            delSnJobMaterialIds.addAll(snJobMaterialList.stream().map(HmeEoJobMaterial::getJobMaterialId).distinct().collect(Collectors.toList()));
                            isCreateSnMaterial = true;
                        }
                    } else {
                        isCreateSnMaterial = true;
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(eoJobMaterialList)) {
                        List<HmeEoJobMaterial> snJobMaterialList = eoJobMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                                vo.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(snJobMaterialList)) {
                            isCreateSnMaterial = true;
                        }
                    } else {
                        isCreateSnMaterial = true;
                    }
                }
                // ??????????????????????????????????????????
                boolean isCreateLotMaterial = false;
                if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList)) {
                    List<HmeEoJobLotMaterial> lotJobMaterialList = hmeEoJobLotMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                            vo.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(lotJobMaterialList)) {
                        BigDecimal sumReleaseQty = lotJobMaterialList.get(0).getReleaseQty() == null ? BigDecimal.ZERO :
                                lotJobMaterialList.get(0).getReleaseQty();
                        BigDecimal qty = sumReleaseQty.divide(prepareQty);
                        if (qty.compareTo(componentQty) != 0) {
                            // delLotJobMaterialIds.add(lotJobMaterialList.get(0).getJobMaterialId());
                            isCreateLotMaterial = true;
                        }
                    } else {
                        isCreateLotMaterial = true;
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(eoJobLotMaterialList)) {
                        List<HmeEoJobLotMaterial> lotJobMaterialList = eoJobLotMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                                vo.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(lotJobMaterialList)) {
                            isCreateLotMaterial = true;
                        }
                    } else {
                        isCreateLotMaterial = true;
                    }
                }
                // ??????????????????????????????????????????
                boolean isCreateTimeMaterial = false;
                if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
                    List<HmeEoJobTimeMaterial> timeJobMaterialList = hmeEoJobTimeMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                            vo.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(timeJobMaterialList)) {
                        BigDecimal sumReleaseQty = timeJobMaterialList.get(0).getReleaseQty() == null ? BigDecimal.ZERO :
                                timeJobMaterialList.get(0).getReleaseQty();
                        BigDecimal qty = sumReleaseQty.divide(prepareQty);
                        if (qty.compareTo(componentQty) != 0) {
                            // delTimeJobMaterialIds.add(timeJobMaterialList.get(0).getJobMaterialId());
                            isCreateTimeMaterial = true;
                        }
                    } else {
                        isCreateTimeMaterial = true;
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(eoJobTimeMaterialList)) {
                        List<HmeEoJobTimeMaterial> timeJobMaterialList = eoJobTimeMaterialList.stream().filter(c -> StringUtils.equals(c.getMaterialId(),
                                vo.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(timeJobMaterialList)) {
                            isCreateTimeMaterial = true;
                        }
                    } else {
                        isCreateTimeMaterial = true;
                    }
                }
                if (HmeConstants.MaterialTypeCode.SN.equals(lotType) && isCreateSnMaterial) {
                    hmeEoJobMaterialRepository.initJobMaterial(tenantId, vo.getMaterialId(), isSplitLine, componentQty, vo.getBomComponentId(), dto);
                }
                if (HmeConstants.MaterialTypeCode.LOT.equals(lotType) && isCreateLotMaterial) {
                    hmeEoJobLotMaterialRepository.initLotMaterialList(tenantId, vo.getMaterialId(), componentQty, dto);
                }
                if (HmeConstants.MaterialTypeCode.TIME.equals(lotType) && isCreateTimeMaterial) {
                    String availableTime = "";
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                        List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item ->
                                item.getKeyId().equals(materialSiteIdList2.get(0).getMaterialSiteId()) &&
                                        "attribute9".equals(item.getAttrName())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2) && StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                            availableTime = mtExtendAttrVO1List2.get(0).getAttrValue();
                        }
                    }

                    // ??????????????????????????????
                    if (StringUtils.isBlank(availableTime)) {
                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(vo.getMaterialId());
                        //???????????????${1}??????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_070", "HME", mtMaterial.getMaterialCode()));
                    }
                    hmeEoJobTimeMaterialRepository.initTimeMaterialList(tenantId, vo.getMaterialId(), availableTime, componentQty, dto);
                }
            }
        }
        //?????????????????????????????????
        if (CollectionUtils.isNotEmpty(delSnJobMaterialIds)) {
            hmeEoJobMaterialMapper.batchDeleteByPrimary(delSnJobMaterialIds);
        }
        if (CollectionUtils.isNotEmpty(delLotJobMaterialIds)) {
            hmeEoJobLotMaterialMapper.batchDeleteByPrimary(delLotJobMaterialIds);
        }
        if (CollectionUtils.isNotEmpty(delTimeJobMaterialIds)) {
            hmeEoJobTimeMaterialMapper.batchDeleteByPrimary(delTimeJobMaterialIds);
        }
    }

    private List<MtExtendAttrVO1> getMaterialSiteAttr(Long tenantId, List<MtMaterialSite> mtMaterialSiteList) {
        //????????????????????????????????????????????????????????????
        List<String> materialSiteIdList = mtMaterialSiteList.stream().map(MtMaterialSite::getMaterialSiteId).collect(Collectors.toList());
        List<String> attrNameList = new ArrayList<String>();
        attrNameList.add("attribute1");
        attrNameList.add("attribute9");
        attrNameList.add("attribute14");
        List<MtExtendAttrVO1> mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_material_site_attr", "MATERIAL_SITE_ID", materialSiteIdList, attrNameList);
        return mtExtendAttrVO1List;
    }

}
