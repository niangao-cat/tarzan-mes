package tarzan.order.app.service.impl;

import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.repository.MtThreadPoolRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.CollectorsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.repository.MtEoRouterActualRepository;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.repository.MtWorkOrderActualRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.config.ExecutorConfig;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.MtRouterStepMapper;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtCustomerRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.app.service.MtWorkOrderService;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.*;
import tarzan.order.infra.mapper.MtEoMapper;
import tarzan.order.infra.mapper.MtWorkOrderMapper;
import tarzan.order.infra.mapper.MtWorkOrderRelMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * ????????????????????????????????????
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
@Service
@Slf4j
public class MtWorkOrderServiceImpl implements MtWorkOrderService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtWorkOrderMapper mtWorkOrderMapper;

    @Autowired
    private MtThreadPoolRepository mtThreadPoolRepository;

    @Autowired
    private ExecutorConfig executorConfig;

    @Autowired
    private MtWorkOrderActualRepository mtWorkOrderActualRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtCustomerRepository mtCustomerRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterStepMapper mtRouterStepMapper;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEoMapper mtEoMapper;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtRouterStepGroupStepRepository mtRouterStepGroupStepRepository;

    @Autowired
    private MtRouterLinkRepository mtRouterLinkRepository;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Autowired
    private MtBomSubstituteGroupRepository mtBomSubstituteGroupRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtWorkOrderRelMapper mtWorkOrderRelMapper;

    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;

    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtEoBomRepository mtEoBomRepository;

    @Autowired
    private HmeCosCommonService hmeCosCommonService;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Override
    public Page<MtWorkOrderVO39> woListForUi(Long tenantId, MtWorkOrderVO38 dto, PageRequest pageRequest) {
        Long userId = DetailsHelper.getUserDetails().getUserId();

        MtUserOrganization org = new MtUserOrganization();
        org.setUserId(userId);
        org.setOrganizationType("SITE");
        org.setEnableFlag("Y");
        List<MtUserOrganization> orgList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, org);
        List<String> siteIdList = orgList.stream().map(MtUserOrganization::getOrganizationId)
                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(siteIdList)) {
            return new Page<MtWorkOrderVO39>();
        }

        Page<MtWorkOrderVO39> base = PageHelper.doPageAndSort(pageRequest,
                () -> mtWorkOrderMapper.selectForUi(tenantId, dto, siteIdList));
        if (CollectionUtils.isNotEmpty(base)) {

            ThreadPoolTaskExecutor poolExecutor = executorConfig.asyncServiceExecutor();

            List<MtGenType> genTypeList = mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "WO_TYPE");
            List<MtGenStatus> genStatusList = mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "WO_STATUS");

            // ?????????????????????
            List<MtWorkOrderVO39> result = Collections.synchronizedList(new ArrayList<>());

            // ????????????????????????????????? siteId???????????????API{ siteBasicPropertyBatchGet }?????????????????????????????????
            List<String> siteIds;
            // ????????????????????????????????? materialId???????????????API{materialPropertyBatchGet }?????????????????????????????????
            List<String> materialIds;
            // ???????????????????????????????????? productionLineId???????????????API{ prodLineBasicPropertyBatchGet }???????????????????????????????????????
            List<String> productionLineIds;

            // ???????????????????????????id??????
            siteIds = base.parallelStream().map(MtWorkOrderVO39::getSiteId).collect(Collectors.toList());
            materialIds = base.parallelStream().map(MtWorkOrderVO39::getMaterialId).collect(Collectors.toList());
            productionLineIds = base.parallelStream().map(MtWorkOrderVO39::getProductionLineId)
                    .collect(Collectors.toList());

            List<MtMaterialVO> mtMaterialVOS = new ArrayList<>();
            List<MtModSite> sites = new ArrayList<>();
            List<MtModProductionLine> productionLines = new ArrayList<>();

            try {
                // ?????????????????????????????????
                Future<List<MtMaterialVO>> materialFuture =
                        mtThreadPoolRepository.getMaterialFuture(poolExecutor, tenantId, materialIds);
                Future<List<MtModSite>> sitesFuture =
                        mtThreadPoolRepository.getModSiteFuture(poolExecutor, tenantId, siteIds);

                Future<List<MtModProductionLine>> productionLinesFuture = mtThreadPoolRepository
                        .getModProductionLineFuture(poolExecutor, tenantId, productionLineIds);

                sites = sitesFuture.get();
                mtMaterialVOS = materialFuture.get();
                productionLines = productionLinesFuture.get();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // ???????????????map??????
            Map<String, MtModSite> siteMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(sites)) {
                siteMap = sites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
            }

            Map<String, MtMaterialVO> materialMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                materialMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t));
            }

            Map<String, MtModProductionLine> productionMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(productionLines)) {
                productionMap = productionLines.stream()
                        .collect(Collectors.toMap(MtModProductionLine::getProdLineId, t -> t));
            }

            Map<String, MtMaterialVO> finalMaterialMap = materialMap;
            Map<String, MtModSite> finalSiteMap = siteMap;
            Map<String, MtModProductionLine> finalProductionMap = productionMap;

            base.parallelStream().forEach(workOrder -> {
                MtWorkOrderVO39 vo = new MtWorkOrderVO39();
                vo.setWorkOrderNum(workOrder.getWorkOrderNum());
                vo.setWorkOrderType(workOrder.getWorkOrderType());
                vo.setWorkOrderId(workOrder.getWorkOrderId());
                vo.setSiteId(workOrder.getSiteId());
                vo.setProductionLineId(workOrder.getProductionLineId());
                vo.setMaterialId(workOrder.getMaterialId());
                vo.setQty(workOrder.getQty());
                vo.setStatus(workOrder.getStatus());
                vo.setPlanStartTime(workOrder.getPlanStartTime());
                vo.setPlanEndTime(workOrder.getPlanEndTime());
                vo.setSiteCode(null != finalSiteMap.get(workOrder.getSiteId())
                        ? finalSiteMap.get(workOrder.getSiteId()).getSiteCode()
                        : null);
                vo.setSiteName(null != finalSiteMap.get(workOrder.getSiteId())
                        ? finalSiteMap.get(workOrder.getSiteId()).getSiteName()
                        : null);
                vo.setMaterialCode(null != finalMaterialMap.get(workOrder.getMaterialId())
                        ? finalMaterialMap.get(workOrder.getMaterialId()).getMaterialCode()
                        : null);
                vo.setMaterialName(null != finalMaterialMap.get(workOrder.getMaterialId())
                        ? finalMaterialMap.get(workOrder.getMaterialId()).getMaterialName()
                        : null);
                vo.setProductionLineCode(null != finalProductionMap.get(workOrder.getProductionLineId())
                        ? finalProductionMap.get(workOrder.getProductionLineId()).getProdLineCode()
                        : null);
                vo.setProductionLineName(null != finalProductionMap.get(workOrder.getProductionLineId())
                        ? finalProductionMap.get(workOrder.getProductionLineId()).getProdLineName()
                        : null);
                MtWorkOrderActualVO mtWorkOrderActualVO = new MtWorkOrderActualVO();
                mtWorkOrderActualVO.setWorkOrderId(vo.getWorkOrderId());
                MtWorkOrderActual woActualGet = mtWorkOrderActualRepository.woActualGet(tenantId, mtWorkOrderActualVO);
                if (woActualGet != null) {
                    vo.setReleasedQty(woActualGet.getReleasedQty());
                    vo.setCompletedQty(woActualGet.getCompletedQty());
                    vo.setScrappedQty(woActualGet.getScrappedQty());
                }
                // ???????????????????????????????????????????????????
                MtWorkOrderVO45 kitQtyQuery = new MtWorkOrderVO45();
                kitQtyQuery.setWorkOrderId(workOrder.getWorkOrderId());
                kitQtyQuery.setOnlyIssueAssembleFlag("N");

                try {
                    Double kitQty = mtWorkOrderRepository.woKittingQtyCalculate(tenantId, kitQtyQuery);
                    vo.setKitQty(kitQty);
                } catch (Exception ex) {
                    vo.setKitQty(0.0D);
                }

                Optional<MtGenType> typeOp = genTypeList.stream()
                        .filter(t -> t.getTypeCode().equals(vo.getWorkOrderType())).findFirst();
                Optional<MtGenStatus> statusOp = genStatusList.stream()
                        .filter(t -> t.getStatusCode().equals(vo.getStatus())).findFirst();
                typeOp.ifPresent(mtGenType -> vo.setWorkOrderTypeDesc(mtGenType.getDescription()));
                statusOp.ifPresent(mtGenStatus -> vo.setStatusDesc(mtGenStatus.getDescription()));

                result.add(vo);
            });
            result.sort((w1, w2) -> {
                return w1.getWorkOrderNum().compareTo(w2.getWorkOrderNum());
            });
            base.setContent(result);
        }

        return base;
    }

    @Override
    public MtWorkOrderVO40 woDetailForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        // ??????WO????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
        MtWorkOrderVO40 vo = new MtWorkOrderVO40();
        BeanUtils.copyProperties(mtWorkOrder, vo);

        MtGenType workOrderType = mtGenTypeRepository.getGenType(tenantId, "ORDER", "WO_TYPE", vo.getWorkOrderType());

        // ???????????????????????????
        MtGenStatus workOrderStatus =
                mtGenStatusRepository.getGenStatus(tenantId, "ORDER", "WO_STATUS", vo.getStatus());
        MtGenType completeControlType = mtGenTypeRepository.getGenType(tenantId, "MATERIAL", "CONTROL_TYPE",
                vo.getCompleteControlType());

        // ??????????????????
        MtModSite mtModSite = StringUtils.isEmpty(vo.getSiteId()) ? null
                : mtModSiteRepository.siteBasicPropertyGet(tenantId, vo.getSiteId());
        // ??????????????????
        MtMaterialVO mtMaterialVO = StringUtils.isEmpty(vo.getMaterialId()) ? null
                : mtMaterialRepository.materialPropertyGet(tenantId, vo.getMaterialId());
        // ??????????????????
        MtUomVO mtUomVO = StringUtils.isEmpty(vo.getUomId()) ? null
                : mtUomRepository.uomPropertyGet(tenantId, vo.getUomId());

        // ??????????????????
        MtCustomer mtCustomer = new MtCustomer();
        mtCustomer.setTenantId(tenantId);
        mtCustomer.setCustomerId(vo.getCustomerId());
        mtCustomer = mtCustomerRepository.selectOne(mtCustomer);

        // ?????????????????????
        MtModProductionLine mtModProductionLine = StringUtils.isEmpty(vo.getProductionLineId()) ? null
                : mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId, vo.getProductionLineId());
        // ??????????????????
        MtModLocator mtModLocator = StringUtils.isEmpty(vo.getLocatorId()) ? null
                : mtModLocatorRepository.locatorBasicPropertyGet(tenantId, vo.getLocatorId());
        // ????????????????????????
        MtBomVO7 mtBomVO7 = StringUtils.isEmpty(vo.getBomId()) ? null
                : mtBomRepository.bomBasicGet(tenantId, vo.getBomId());
        // ??????????????????
        MtRouter mtRouter = StringUtils.isEmpty(vo.getRouterId()) ? null
                : mtRouterRepository.routerGet(tenantId, vo.getRouterId());

        // ??????????????????????????????
        MtWorkOrderActualVO mtWorkOrderActualVO = new MtWorkOrderActualVO();
        mtWorkOrderActualVO.setWorkOrderId(vo.getWorkOrderId());
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, mtWorkOrderActualVO);

        // ????????????
        vo.setWorkOrderTypeDesc(workOrderType == null ? "" : workOrderType.getDescription());
        vo.setStatusDesc(workOrderStatus == null ? "" : workOrderStatus.getDescription());
        vo.setCompleteControlTypeDesc(completeControlType == null ? "" : completeControlType.getDescription());
        vo.setSiteCode(mtModSite == null ? "" : mtModSite.getSiteCode());
        vo.setSiteName(mtModSite == null ? "" : mtModSite.getSiteName());
        vo.setMaterialCode(mtMaterialVO == null ? "" : mtMaterialVO.getMaterialCode());
        vo.setMaterialName(mtMaterialVO == null ? "" : mtMaterialVO.getMaterialName());
        vo.setUomCode(mtUomVO == null ? "" : mtUomVO.getUomCode());
        vo.setUomName(mtUomVO == null ? "" : mtUomVO.getUomName());
        vo.setCustomerCode(mtCustomer == null ? "" : mtCustomer.getCustomerCode());
        vo.setCustomerName(mtCustomer == null ? "" : mtCustomer.getCustomerName());
        vo.setProductionLineCode(mtModProductionLine == null ? "" : mtModProductionLine.getProdLineCode());
        vo.setProductionLineName(mtModProductionLine == null ? "" : mtModProductionLine.getProdLineName());
        vo.setLocatorCode(mtModLocator == null ? "" : mtModLocator.getLocatorCode());
        vo.setLocatorName(mtModLocator == null ? "" : mtModLocator.getLocatorName());
        vo.setBomName(mtBomVO7 == null ? "" : mtBomVO7.getBomName());
        vo.setRouterName(mtRouter == null ? "" : mtRouter.getRouterName());

        if (mtWorkOrderActual != null) {
            vo.setActualStartTime(mtWorkOrderActual.getActualStartDate());
            vo.setActualEndDate(mtWorkOrderActual.getActualEndDate());
            vo.setReleasedQty(mtWorkOrderActual.getReleasedQty());
            vo.setCompletedQty(mtWorkOrderActual.getCompletedQty());
            vo.setScrappedQty(mtWorkOrderActual.getScrappedQty());
            vo.setHoldQty(mtWorkOrderActual.getHoldQty());
        }
        return vo;
    }

    @Override
    public List<MtWorkOrderVO41> routerListForUi(Long tenantId, String workOrderId, String routerId,
                                                 PageRequest pageRequest) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }
        if (StringUtils.isEmpty(routerId)) {
            return Collections.emptyList();
        }


        // ????????????????????????
        //2020/7/23 update by sanfeng.zhang ???SEQUENCE??????
        Page<String> stepIdPage = PageHelper.doPage(pageRequest,
                () -> mtRouterStepMapper.selectByWoRouter(tenantId, routerId));
        if (CollectionUtils.isEmpty(stepIdPage)) {
            return Collections.emptyList();
        }

        List<MtRouterStep> routerStepList = mtRouterStepRepository.routerStepBatchGet(tenantId, stepIdPage);

        // ??????????????????
        List<MtWorkOrderVO41> resultList = getRouterStepList(tenantId, workOrderId, routerStepList, "N");

        Collections.sort(resultList, (t1, t2) -> {
            return t1.getSequence().compareTo(t2.getSequence());
        });
        // ????????????????????????
        Page<MtWorkOrderVO41> resultPage = new Page<MtWorkOrderVO41>();
        resultPage.setContent(resultList);
        resultPage.setNumber(stepIdPage.getNumber());
        resultPage.setNumberOfElements(stepIdPage.size());
        resultPage.setSize(stepIdPage.getSize());
        resultPage.setTotalElements(stepIdPage.getTotalElements());
        resultPage.setTotalPages(stepIdPage.getTotalPages());
        return resultPage;

    }


    @Override
    public List<MtWorkOrderVO41> subRouterStepListForUi(Long tenantId, String workOrderId, String routerStepId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "routerStepId", ""));
        }
        MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, routerStepId);

        // ????????????????????????????????????????????????
        if (mtRouterStep == null || "OPERATION".equals(mtRouterStep.getRouterStepType())) {
            return Collections.emptyList();
        }

        List<MtRouterStep> routerStepList = new ArrayList<MtRouterStep>();

        // ????????????????????????????????????
        if ("GROUP".equals(mtRouterStep.getRouterStepType())) {
            List<MtRouterStepGroupStep> childSteps = mtRouterStepGroupStepRepository.groupStepLimitStepQuery(tenantId,
                    mtRouterStep.getRouterStepId());
            List<String> childStepIds = childSteps.stream().map(MtRouterStepGroupStep::getRouterStepId)
                    .collect(Collectors.toList());
            // ??????routerStepId????????????API{routerStepBatchGet}???????????????step?????????????????????
            routerStepList = CollectionUtils.isEmpty(childStepIds) ? new ArrayList<MtRouterStep>()
                    : mtRouterStepRepository.routerStepBatchGet(tenantId, childStepIds);
        } else {
            // ??????????????????????????????????????????API{routerLinkBatchGet}???????????????routerStepId???????????????routerId
            MtRouterLink mtRouterLink = mtRouterLinkRepository.routerLinkGet(tenantId, mtRouterStep.getRouterStepId());
            if (mtRouterLink != null) {
                List<String> stepIds = mtRouterStepRepository.routerStepListQuery(tenantId, mtRouterLink.getRouterId())
                        .stream().map(MtRouterStepVO5::getRouterStepId).collect(Collectors.toList());
                routerStepList = CollectionUtils.isEmpty(stepIds) ? new ArrayList<MtRouterStep>()
                        : mtRouterStepRepository.routerStepBatchGet(tenantId, stepIds);
            }
        }

        return getRouterStepList(tenantId, workOrderId, routerStepList, "Y");
    }

    /**
     * ??????????????????
     *
     * @author xiao.tang02@hand-china.com 2019???12???24?????????10:47:55
     * @param tenantId
     * @param workOrderId
     * @param routerStepList
     * @param subFlag ?????????????????????
     * @return
     * @return List<MtWorkOrderVO41>
     */
    private List<MtWorkOrderVO41> getRouterStepList(Long tenantId, String workOrderId,
                                                    List<MtRouterStep> routerStepList, String subFlag) {
        if (CollectionUtils.isEmpty(routerStepList)) {
            return Collections.emptyList();
        }

        List<String> routerStepIdList =
                routerStepList.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList());

        // ?????????????????????
        List<MtWorkOrderVO41> resultList = new ArrayList<MtWorkOrderVO41>();

        List<MtGenType> stepTypeList = mtGenTypeRepository.getGenTypes(tenantId, "ROUTER", "ROUTER_STEP_TYPE");
        List<MtGenType> queueDecisionTypeList =
                mtGenTypeRepository.getGenTypes(tenantId, "ROUTER", "QUEUE_DECISION_TYPE");
        Map<String, String> stepTypeMap = stepTypeList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        Map<String, String> queueDecisionTypeMap = queueDecisionTypeList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        MtEoVO2 mtEoVO2 = new MtEoVO2();
        mtEoVO2.setWorkOrderId(workOrderId);
        List<String> eoIdList = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);

        Map<String, List<MtRouterStep>> routerStepGroup =
                routerStepList.stream().collect(Collectors.groupingBy(MtRouterStep::getRouterStepType));
        List<MtRouterStep> stepOperations = routerStepGroup.get("OPERATION");
        List<MtRouterStep> stepGroups = routerStepGroup.get("GROUP");
        List<MtRouterStep> stepRouters = routerStepGroup.get("ROUTER");
        List<MtRouterStep> noTypeRouters = routerStepGroup.get("");

        // ????????????????????????????????????(???????????????????????????????????????????????????????????????)
        if (CollectionUtils.isNotEmpty(noTypeRouters)) {
            for (MtRouterStep routerStep : noTypeRouters) {
                // ????????????
                MtWorkOrderVO41 resVO = new MtWorkOrderVO41();
                resVO.setRouterStepId(routerStep.getRouterStepId());
                resVO.setSequence(routerStep.getSequence());
                resVO.setStep(routerStep.getStepName());
                resVO.setDescription(routerStep.getDescription());
                resVO.setEntryStepFlag(routerStep.getEntryStepFlag());
                resVO.setKeyStepFlag(routerStep.getKeyStepFlag() == null ? "" : routerStep.getKeyStepFlag());
                resVO.setQueueDecisionType(routerStep.getQueueDecisionType());
                resVO.setQueueDecisionTypeDesc(queueDecisionTypeMap.get(routerStep.getQueueDecisionType()) == null ? ""
                        : queueDecisionTypeMap.get(routerStep.getQueueDecisionType()));
                resVO.setChildren(null);
                resultList.add(resVO);
            }
        }


        // ???????????????
        if (CollectionUtils.isNotEmpty(stepOperations)) {
            // 1?????????API{woLimitEoQuery}????????????workOrderId?????????EoId
            // 2?????????EO?????????API{eoLimitStepActualQuery}??????eoStepActualId
            // 3?????????eoStepActualId????????????API{eoStepPropertyGet}
//            List<MtEoStepActual> eoStepActualList = new ArrayList<MtEoStepActual>();

            //V20211202 modify by penglin.sui ?????????????????????API
//            List<MtEoStepActualVO34> eoStepActualVO34List = mtEoStepActualRepository.eoLimitStepActualBatchQuery(tenantId , eoIdList);
//            List<String> allEoStepActualIdList = new ArrayList<>();
//            for (MtEoStepActualVO34 eoStepActual : eoStepActualVO34List
//                 ) {
//                allEoStepActualIdList.addAll(eoStepActual.getEoStepActualId());
//            }
//            List<MtEoStepActual> eoStepPropertyList = new ArrayList<>(allEoStepActualIdList.size());
//            if(CollectionUtils.isNotEmpty(allEoStepActualIdList)){
//                allEoStepActualIdList = allEoStepActualIdList.stream().distinct().collect(Collectors.toList());
//                eoStepPropertyList = mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId , allEoStepActualIdList);
//            }
            //V20211203 modify by penglin.sui ???????????????eoStepActualList????????????????????????
//            for (String eoId : eoIdList) {
////                List<String> eoStepActualIdList = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, eoId);
//                List<MtEoStepActualVO34> subEoStepActualVO34List = eoStepActualVO34List.stream().filter(item -> item.getEoId().equals(eoId))
//                        .collect(Collectors.toList());
//                List<String> eoStepActualIdList = new ArrayList<>();
//                if(CollectionUtils.isNotEmpty(subEoStepActualVO34List)){
//                    eoStepActualIdList = subEoStepActualVO34List.get(0).getEoStepActualId();
//                }
//
////                for (String eoStepActualId : eoStepActualIdList) {
//////                    MtEoStepActual eoStepPropertyGet =
//////                            mtEoStepActualRepository.eoStepPropertyGet(tenantId, eoStepActualId);
////                    List<MtEoStepActual> subEoStepPropertyGet = eoStepPropertyList.stream().filter(item -> item.getEoStepActualId().equals(eoStepActualId))
////                            .collect(Collectors.toList());
////                    eoStepActualList.add(subEoStepPropertyGet.get(0));
////                }
//            }

            //???????????????????????? && ????????????
            int index = 0;
            List<HmeEoJobSn> eoJobSns = hmeEoJobSnMapper.selectByCondition(Condition.builder(HmeEoJobSn.class).select(
                    HmeEoJobSn.FIELD_WORK_ORDER_ID,HmeEoJobSn.FIELD_OPERATION_ID,HmeEoJobSn.FIELD_EO_ID,HmeEoJobSn.FIELD_SITE_IN_DATE,HmeEoJobSn.FIELD_SITE_OUT_DATE).andWhere(
                    Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_WORK_ORDER_ID,workOrderId)).build());
            Map<String,List<HmeEoJobSn>> eoJobMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(eoJobSns)){
                eoJobMap = eoJobSns.stream().collect(Collectors.groupingBy(HmeEoJobSn::getOperationId));
            }
            MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId,new MtWorkOrderActualVO(){{
                setWorkOrderId(workOrderId);
            }});
            //?????????????????????????????????
            BigDecimal lastCompletedQty = BigDecimal.ZERO;
            for (MtRouterStep routerStep : stepOperations) {
                index++;
                String doneStepValidate =
                        mtRouterDoneStepRepository.doneStepValidate(tenantId, routerStep.getRouterStepId());
                MtWorkOrderVO41 vo = new MtWorkOrderVO41();
                vo.setRouterStepId(routerStep.getRouterStepId());
                vo.setSequence(routerStep.getSequence());
                vo.setStep(routerStep.getStepName());
                vo.setDescription(routerStep.getDescription());
                vo.setEntryStepFlag(routerStep.getEntryStepFlag());
                vo.setKeyStepFlag(routerStep.getKeyStepFlag() == null ? "" : routerStep.getKeyStepFlag());
                vo.setDoneStepFlag(doneStepValidate);
                vo.setRouterStepType(routerStep.getRouterStepType());
                vo.setRouterStepTypeDesc(stepTypeMap.get(routerStep.getRouterStepType()) == null ? ""
                        : stepTypeMap.get(routerStep.getRouterStepType()));
                vo.setQueueDecisionType(routerStep.getQueueDecisionType());
                vo.setQueueDecisionTypeDesc(queueDecisionTypeMap.get(routerStep.getQueueDecisionType()) == null ? ""
                        : queueDecisionTypeMap.get(routerStep.getQueueDecisionType()));
                vo.setChildren(null);

                // 1???????????????????????????ID?????????API{routerOperationGet}??????????????????routerStepId?????????operationId
                MtRouterOperation operation = new MtRouterOperation();
                operation.setTenantId(tenantId);
                operation.setRouterStepId(routerStep.getRouterStepId());
                MtRouterOperation mtRouterOperation = mtRouterOperationRepository.selectOne(operation);
                if (mtRouterOperation != null) {
//                    // 2???????????????????????????ID=??????????????????ID+???????????????????????????=???????????????????????????????????????????????????????????????????????????4?????????????????????????????????????????????4??????????????????
//                    List<MtEoStepActual> actualList = eoStepActualList.stream()
//                            .filter(t -> t.getOperationId().equals(mtRouterOperation.getOperationId())
//                                    && t.getStepName().equals(routerStep.getStepName()))
//                            .collect(Collectors.toList());
//                    BigDecimal completedQty = actualList.stream().collect(
//                            CollectorsUtil.summingBigDecimal(t -> t.getCompletedQty() == null ? BigDecimal.ZERO
//                                    : BigDecimal.valueOf(t.getCompletedQty())));
//                    BigDecimal queueQty = actualList.stream().collect(
//                            CollectorsUtil.summingBigDecimal(t -> t.getQueueQty() == null ? BigDecimal.ZERO
//                                    : BigDecimal.valueOf(t.getQueueQty())));
//                    BigDecimal workingQty = actualList.stream().collect(
//                            CollectorsUtil.summingBigDecimal(t -> t.getWorkingQty() == null ? BigDecimal.ZERO
//                                    : BigDecimal.valueOf(t.getWorkingQty())));
//                    BigDecimal completePendingQty = actualList.stream()
//                            .collect(CollectorsUtil.summingBigDecimal(
//                                    t -> t.getCompletePendingQty() == null ? BigDecimal.ZERO
//                                            : BigDecimal.valueOf(t.getCompletePendingQty())));
//                    vo.setCompletedQty(completedQty == null ? null : completedQty.doubleValue());
//                    vo.setQueueQty(queueQty == null ? null : queueQty.doubleValue());
//                    vo.setWorkingQty(workingQty == null ? null : workingQty.doubleValue());
//                    vo.setCompletePendingQty(completePendingQty == null ? null : completePendingQty.doubleValue());

                    //???????????????????????????????????????????????????????????? -- By ?????? 2021-11-22 for ?????????
                    //?????????????????????
                    List<HmeEoJobSn> currEoJobSns = eoJobMap.get(mtRouterOperation.getOperationId());
                    if (CollectionUtils.isNotEmpty(currEoJobSns)) {
                        //???????????????????????????EO
                        List<MtEo> currEos = mtEoMapper.selectByCondition(Condition.builder(MtEo.class).select(MtEo.FIELD_EO_ID,MtEo.FIELD_STATUS,MtEo.FIELD_IDENTIFICATION).andWhere(
                                Sqls.custom().andIn(MtEo.FIELD_EO_ID,currEoJobSns.stream().map(HmeEoJobSn::getEoId).collect(Collectors.toList()))).build());
                        //????????????????????????
                        BigDecimal currSiteInQty = BigDecimal.valueOf(currEoJobSns.size());

                        //????????????????????????
                        //???????????????????????????????????????????????????
                        BigDecimal queueQty = BigDecimal.ZERO;
                        if (index == 1) {
                            queueQty = BigDecimal.valueOf(mtWorkOrderActual.getReleasedQty()).subtract(currSiteInQty);
                        }else {
                            //?????????????????????????????????????????????????????????????????????
                            queueQty = lastCompletedQty.subtract(currSiteInQty);
                        }
                        //???????????????????????????
                        BigDecimal workingQty = BigDecimal.valueOf(currEos.stream().filter(e->e.getStatus().equals("WORKING")).count());
                        //????????????????????????
                        BigDecimal completedQty = BigDecimal.valueOf(currEoJobSns.stream().filter(e -> StringUtils.isNotEmpty(e.getSiteInDate().toString()) && StringUtils.isNotEmpty(e.getSiteOutDate().toString())).count());
                        lastCompletedQty = completedQty;
                        //?????????????????????????????????
                        BigDecimal scrapHoldQty = BigDecimal.valueOf(currEos.stream().filter(e->e.getStatus().equals("HOLD")||e.getStatus().equals("ABANDON")).count());


                        vo.setCompletedQty(completedQty.doubleValue());
                        vo.setQueueQty(queueQty.doubleValue());
                        vo.setWorkingQty(workingQty.doubleValue());
                        vo.setScrapHoldQty(scrapHoldQty.doubleValue());

//                      vo.setCompletePendingQty(completePendingQty == null ? null : completePendingQty.doubleValue());
                    }
                }
                resultList.add(vo);
            }

        }

        //V20211203 modify by penglin.sui ???????????????API
        List<String> allRouterStepIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(stepGroups)) {
            List<String> stepGroupStepIdList =
                    stepGroups.stream().map(MtRouterStep::getRouterStepId)
                            .distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(stepGroupStepIdList)) {
                allRouterStepIdList.addAll(stepGroupStepIdList);
            }
        }
        if (CollectionUtils.isNotEmpty(stepRouters)) {
            List<String> stepRouterStepIdList =
                    stepRouters.stream().map(MtRouterStep::getRouterStepId)
                            .distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(stepRouterStepIdList)) {
                allRouterStepIdList.addAll(stepRouterStepIdList);
            }
        }

        if(CollectionUtils.isNotEmpty(allRouterStepIdList)){
            allRouterStepIdList = allRouterStepIdList.stream().distinct().collect(Collectors.toList());
        }
        Map<String , String> doneStepValidateMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(allRouterStepIdList)) {
            List<MtRouterDoneStepVO> doneStepValidateList =
                    mtRouterDoneStepRepository.doneStepBatchValidate(tenantId, allRouterStepIdList);
            if (CollectionUtils.isNotEmpty(doneStepValidateList)) {
                doneStepValidateMap = doneStepValidateList.stream()
                        .collect(Collectors.toMap(MtRouterDoneStepVO::getRouterStepId, MtRouterDoneStepVO::getDoneStepFlag));
            }
        }

        // ??????????????????
        if (CollectionUtils.isNotEmpty(stepGroups)) {
            // 1?????????API{woLimitEoQuery}????????????workOrderId?????????EoId
            // 2?????????EO?????????API{eoLimitStepActualQuery}??????eoStepActualId
            // 3?????????eoStepActualId????????????API{eoStepPropertyGet} ??????eoId??????
            // Map<String, List<MtEoStepActual>> eoStepActualMap = new HashMap<String, List<MtEoStepActual>>();
            // for (String eoId : eoIdList) {
            // List<MtEoStepActual> eoActuals = new ArrayList<MtEoStepActual>();
            // List<String> eoStepActualIdList = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId,
            // eoId);
            // for (String eoStepActualId : eoStepActualIdList) {
            // MtEoStepActual eoStepPropertyGet =
            // mtEoStepActualRepository.eoStepPropertyGet(tenantId, eoStepActualId);
            // eoActuals.add(eoStepPropertyGet);
            // }
            // eoStepActualMap.put(eoId, eoActuals);
            // }

            for (MtRouterStep routerStep : stepGroups) {
//                String doneStepValidate =
//                        mtRouterDoneStepRepository.doneStepValidate(tenantId, routerStep.getRouterStepId());
                String doneStepValidate = doneStepValidateMap.get(routerStep.getRouterStepId());
                // 1?????????API{groupStepLimitStepQuery}??????????????????routerStepId?????????routerStepId??????routerStepId??????????????????
                // List<MtRouterStepGroupStep> childSteps = mtRouterStepGroupStepRepository
                // .groupStepLimitStepQuery(tenantId, routerStep.getRouterStepId());
                // ?????????????????????
                // BigDecimal completedQtySum = BigDecimal.ZERO;
                // BigDecimal queueQtySum = BigDecimal.ZERO;
                // BigDecimal workingQtySum = BigDecimal.ZERO;
                // BigDecimal completePendingQtySum = BigDecimal.ZERO;
                // ??????????????????List
                // if (CollectionUtils.isNotEmpty(childSteps)) {
                // List<String> childStepIds = childSteps.stream().map(MtRouterStepGroupStep::getRouterStepId)
                // .collect(Collectors.toList());
                // ??????routerStepId????????????API{routerStepBatchGet}???????????????step?????????????????????
                // List<MtRouterStep> childStepList =
                // mtRouterStepRepository.routerStepBatchGet(tenantId, childStepIds);
                // //// ??????routerStepId????????????API{routerOperationGet}??????????????????routerStepId?????????operationId????????????ID
                // List<MtRouterOperation> operationList =
                // mtRouterOperationRepository.routerOperationBatchGet(tenantId, childStepIds);

                // for (MtRouterStepGroupStep childStep : childSteps) {
                // Optional<MtRouterStep> routerStepOp = childStepList.stream()
                // .filter(t -> t.getRouterStepId().equals(childStep.getRouterStepId()))
                // .findFirst();
                // Optional<MtRouterOperation> operationOp = operationList.stream()
                // .filter(t -> t.getRouterStepId().equals(childStep.getRouterStepId()))
                // .findFirst();
                // if (routerStepOp.isPresent() && operationOp.isPresent()) {
                // MtRouterStep step = routerStepOp.get();
                // MtRouterOperation operation1 = operationOp.get();
                // List<MtEoStepActual> qtyList = new ArrayList<MtEoStepActual>();
                // 4?????????1??????????????????routerStepId???????????????????????????=???????????????????????????+??????ID=??????????????????ID???????????????????????????
                // ?????????????????????eoId?????????????????????4???????????????????????????????????????????????????EO???4??????????????????????????????EO???4??????????????????????????????4???????????????
                // for (Entry<String, List<MtEoStepActual>> actualEntry : eoStepActualMap.entrySet()) {
                // List<MtEoStepActual> actuals = actualEntry.getValue();
                // if (CollectionUtils.isNotEmpty(actuals)) {
                // List<MtEoStepActual> actualList = actuals.stream()
                // .filter(t -> t.getOperationId().equals(operation1.getOperationId())
                // && t.getStepName().equals(step.getStepName()))
                // .collect(Collectors.toList());
                // if (CollectionUtils.isNotEmpty(actualList)) {
                // DoubleSummaryStatistics completedQty = actualList.stream().collect(
                // Collectors.summarizingDouble(MtEoStepActual::getCompletedQty));
                // DoubleSummaryStatistics queueQty = actualList.stream().collect(
                // Collectors.summarizingDouble(MtEoStepActual::getQueueQty));
                // DoubleSummaryStatistics workingQty = actualList.stream().collect(
                // Collectors.summarizingDouble(MtEoStepActual::getWorkingQty));
                // DoubleSummaryStatistics completePendingQty =
                // actualList.stream().collect(Collectors.summarizingDouble(
                // MtEoStepActual::getCompletePendingQty));
                // // ??????????????????
                // MtEoStepActual tempActual = new MtEoStepActual();
                // tempActual.setCompletedQty(completedQty.getMin());
                // tempActual.setQueueQty(queueQty.getMin());
                // tempActual.setWorkingQty(workingQty.getMin());
                // tempActual.setCompletePendingQty(completePendingQty.getMin());
                // qtyList.add(tempActual);
                // }
                // }
                // }

                // ????????????????????????
                // BigDecimal completedQty = qtyList.stream()
                // .collect(CollectorsUtil.summingBigDecimal(
                // t -> t.getCompletedQty() == null ? BigDecimal.ZERO
                // : BigDecimal.valueOf(t.getCompletedQty())));
                // BigDecimal queueQty = qtyList.stream()
                // .collect(CollectorsUtil.summingBigDecimal(
                // t -> t.getQueueQty() == null ? BigDecimal.ZERO
                // : BigDecimal.valueOf(t.getQueueQty())));
                // BigDecimal workingQty = qtyList.stream().collect(CollectorsUtil.summingBigDecimal(
                //
                //
                // t -> t.getWorkingQty() == null ? BigDecimal.ZERO
                // : BigDecimal.valueOf(t.getWorkingQty())));
                // BigDecimal completePendingQty = qtyList.stream().collect(CollectorsUtil
                // .summingBigDecimal(t -> t.getCompletePendingQty() == null ? BigDecimal.ZERO
                // : BigDecimal.valueOf(t.getCompletePendingQty())));
                // completedQtySum =
                // completedQtySum.add(completedQty == null ? BigDecimal.ZERO : completedQty);
                // queueQtySum = queueQtySum.add(queueQty == null ? BigDecimal.ZERO : queueQty);
                // workingQtySum = workingQtySum.add(workingQty == null ? BigDecimal.ZERO : workingQty);
                // completePendingQtySum = completePendingQtySum
                // .add(completePendingQty == null ? BigDecimal.ZERO : completePendingQty);

                // }
                // }
                // }

                // ????????????
                MtWorkOrderVO41 resVO = new MtWorkOrderVO41();
                resVO.setRouterStepId(routerStep.getRouterStepId());
                resVO.setSequence(routerStep.getSequence());
                resVO.setStep(routerStep.getStepName());
                resVO.setDescription(routerStep.getDescription());
                resVO.setEntryStepFlag(routerStep.getEntryStepFlag());
                resVO.setKeyStepFlag(routerStep.getKeyStepFlag() == null ? "" : routerStep.getKeyStepFlag());
                resVO.setDoneStepFlag(doneStepValidate);
                resVO.setRouterStepType(routerStep.getRouterStepType());
                resVO.setRouterStepTypeDesc(stepTypeMap.get(routerStep.getRouterStepType()) == null ? ""
                        : stepTypeMap.get(routerStep.getRouterStepType()));
                resVO.setQueueDecisionType(routerStep.getQueueDecisionType());
                resVO.setQueueDecisionTypeDesc(queueDecisionTypeMap.get(routerStep.getQueueDecisionType()) == null ? ""
                        : queueDecisionTypeMap.get(routerStep.getQueueDecisionType()));
                resVO.setCompletedQty(null);
                resVO.setQueueQty(null);
                resVO.setWorkingQty(null);
                resVO.setCompletePendingQty(null);
                if ("Y".equals(subFlag)) {
                    resVO.setChildren(null);
                }
                resultList.add(resVO);
            }
        }


        // ?????????????????????
        if (CollectionUtils.isNotEmpty(stepRouters)) {
            // ??????????????????????????????????????????API{routerLinkBatchGet}???????????????routerStepId???????????????routerId
            List<MtRouterLink> routerLinkList = mtRouterLinkRepository.routerLinkBatchGet(tenantId, routerStepIdList);
            Map<String, String> routerMap = routerLinkList.stream()
                    .collect(Collectors.toMap(MtRouterLink::getRouterStepId, MtRouterLink::getRouterId));

            List<MtEoRouterActualVO20> dtoList = new ArrayList<>();
            for (MtRouterStep routerStep : stepRouters) {
                String routerId1 = routerMap.get(routerStep.getRouterStepId());
                if (StringUtils.isNotEmpty(routerId1)) {
                    for (String eoId : eoIdList
                         ) {
                        MtEoRouterActualVO20 eoRouterActualVO20 = new MtEoRouterActualVO20();
                        eoRouterActualVO20.setEoId(eoId);
                        eoRouterActualVO20.setRouterId(routerId1);
                        dtoList.add(eoRouterActualVO20);
                    }
                }
            }
            Map<String,List<String>> propertyLimitEoRouterActualBatchQueryMap =
                    mtEoRouterActualRepository.propertyLimitEoRouterActualBatchQuery(tenantId , dtoList);
            List<String> eoRouterActualIdList = new ArrayList<>();
            for (List<String> value : propertyLimitEoRouterActualBatchQueryMap.values()) {
                eoRouterActualIdList.addAll(value);
            }
            Map<String,MtEoRouterActualVO8> eoRouterActualVO8Map = new HashMap<>();
            if(CollectionUtils.isNotEmpty(eoRouterActualIdList)){
                eoRouterActualIdList = eoRouterActualIdList.stream().distinct().collect(Collectors.toList());
                eoRouterActualVO8Map =
                        mtEoRouterActualRepository.eoRouterProductionResultBatchGet(tenantId , eoRouterActualIdList);
            }

            for (MtRouterStep routerStep : stepRouters) {
//                String doneStepValidate =
//                        mtRouterDoneStepRepository.doneStepValidate(tenantId, routerStep.getRouterStepId());
                String doneStepValidate = doneStepValidateMap.get(routerStep.getRouterStepId());

                String routerId1 = routerMap.get(routerStep.getRouterStepId());
                List<MtEoRouterActualVO8> routerActualList = new ArrayList<MtEoRouterActualVO8>();
                // ????????????wo??????EO??????????????????EO????????????API{propertyLimitEoRouterActualQuery}
                if (StringUtils.isNotEmpty(routerId1)) {
                    for (String eoId : eoIdList) {
//                        MtEoRouterActualVO20 mtEoRouterActualVO20 = new MtEoRouterActualVO20();
//                        mtEoRouterActualVO20.setEoId(eoId);
//                        mtEoRouterActualVO20.setRouterId(routerId1);
//                        List<String> propertyLimitEoRouterActualQuery = mtEoRouterActualRepository
//                                .propertyLimitEoRouterActualQuery(tenantId, mtEoRouterActualVO20);
//                        for (String eoRouterActualId : propertyLimitEoRouterActualQuery) {
                            // ?????????eoRouterActualId????????????API{eoRouterProductionResultGet}
//                            routerActualList.add(mtEoRouterActualRepository.eoRouterProductionResultGet(tenantId,
//                                    eoRouterActualId));
//                        }

                        List<String> propertyLimitEoRouterActualBatchQueryList =
                                propertyLimitEoRouterActualBatchQueryMap.getOrDefault(eoId + "#" + routerId1 , new ArrayList<>());
                        for (String eoRouterActualId : propertyLimitEoRouterActualBatchQueryList) {
                            MtEoRouterActualVO8 eoRouterActualVO8 =
                                    eoRouterActualVO8Map.getOrDefault(eoRouterActualId , null);
                            if(Objects.nonNull(eoRouterActualVO8)){
                                routerActualList.add(eoRouterActualVO8);
                            }
                        }
                    }
                }

                BigDecimal completedQty = routerActualList.stream().collect(
                        CollectorsUtil.summingBigDecimal(t -> t.getCompletedQty() == null ? BigDecimal.ZERO
                                : BigDecimal.valueOf(t.getCompletedQty())));
                MtWorkOrderVO41 resVO = new MtWorkOrderVO41();
                resVO.setRouterStepId(routerStep.getRouterStepId());
                resVO.setSequence(routerStep.getSequence());
                resVO.setStep(routerStep.getStepName());
                resVO.setDescription(routerStep.getDescription());
                resVO.setEntryStepFlag(routerStep.getEntryStepFlag());
                resVO.setKeyStepFlag(routerStep.getKeyStepFlag() == null ? "" : routerStep.getKeyStepFlag());
                resVO.setRouterStepType(routerStep.getRouterStepType());
                resVO.setDoneStepFlag(doneStepValidate);
                resVO.setRouterStepType(routerStep.getRouterStepType());
                resVO.setRouterStepTypeDesc(stepTypeMap.get(routerStep.getRouterStepType()) == null ? ""
                        : stepTypeMap.get(routerStep.getRouterStepType()));
                resVO.setQueueDecisionType(routerStep.getQueueDecisionType());
                resVO.setQueueDecisionTypeDesc(queueDecisionTypeMap.get(routerStep.getQueueDecisionType()) == null ? ""
                        : queueDecisionTypeMap.get(routerStep.getQueueDecisionType()));
                resVO.setCompletedQty(completedQty == null ? null : completedQty.doubleValue());
                if ("Y".equals(subFlag)) {
                    resVO.setChildren(null);
                }
                resultList.add(resVO);
            }
        }
        return resultList;
    }

    @Override
    public Page<MtWorkOrderVO42> bomListForUi(Long tenantId, MtWorkOrderVO47 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        if (!("ASC".equals(dto.getSortDirection()) || "DESC".equals(dto.getSortDirection()))) {
            dto.setSortDirection("DESC");
        }

        //2020/7/23 update by sanfeng.zahng ???LINE_NUMBER ??????
        Page<MtWorkOrderVO42> base =
                PageHelper.doPage(pageRequest, () -> mtWorkOrderMapper.selectRKBomComponent(tenantId, dto));
        if (CollectionUtils.isEmpty(base)) {
            return new Page<MtWorkOrderVO42>();
        }

        ThreadPoolTaskExecutor poolExecutor = executorConfig.asyncServiceExecutor();
        List<String> locatorIds = base.stream().map(MtWorkOrderVO42::getIssuedLocatorId)
                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());

        List<MtModLocator> mtModLocators = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(locatorIds)) {
            try {
                // ??????????????????
                Future<List<MtModLocator>> modLocatorFuture =
                        mtThreadPoolRepository.getModLocatorFuture(poolExecutor, tenantId, locatorIds);

                mtModLocators = modLocatorFuture.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ????????????Map
        Map<String, MtModLocator> locatorMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtModLocators)) {
            locatorMap = mtModLocators.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, t -> t));
        }

        List<MtGenType> typeList = mtGenTypeRepository.getGenTypes(tenantId, "BOM", "BOM_COMPONENT_TYPE");
        List<MtGenType> methodList = mtGenTypeRepository.getGenTypes(tenantId, "MATERIAL", "ASSY_METHOD");

        Map<String, String> typeMap =
                typeList.stream().collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        Map<String, String> methodMap = methodList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));


        MtWorkOrder workOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        MtBomVO7 bom = workOrder == null || StringUtils.isEmpty(workOrder.getBomId()) ? null
                : mtBomRepository.bomBasicGet(tenantId, workOrder.getBomId());
        BigDecimal woQty = workOrder == null ? BigDecimal.ZERO : new BigDecimal(workOrder.getQty().toString());
        BigDecimal bomQty = bom == null ? BigDecimal.ZERO : new BigDecimal(bom.getPrimaryQty().toString());

        for (MtWorkOrderVO42 vo : base.getContent()) {// bomComponent
            if ("N".equals(vo.getAssembleExcessFlag())) {
                MtModLocator locator = locatorMap.get(vo.getIssuedLocatorId());
                BigDecimal qty = new BigDecimal(vo.getQty().toString());
                if(StringUtils.isNotEmpty(vo.getMaterialId())) {
                    MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, vo.getMaterialId());
                    if (!ObjectUtils.isEmpty(mtMaterialVO)) {
                        vo.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());
                    }
                }

                MtWoComponentActualVO5 mtWoComponentActualVO5 = new MtWoComponentActualVO5();
                mtWoComponentActualVO5.setBomComponentId(vo.getBomComponentId());
                mtWoComponentActualVO5.setWorkOrderId(dto.getWorkOrderId());
                mtWoComponentActualVO5.setSubstituteIncludedFlag("N");
                List<MtWoComponentActualVO4> actualList = mtWorkOrderComponentActualRepository
                        .componentLimitWoComponentAssembleActualQuery(tenantId, mtWoComponentActualVO5);

                vo.setAssembleQty(CollectionUtils.isEmpty(actualList) ? null : actualList.stream().mapToDouble(MtWoComponentActualVO4::getAssembleQty).sum());
                vo.setScrappedQty(CollectionUtils.isEmpty(actualList) ? null : actualList.stream().mapToDouble(MtWoComponentActualVO4::getScrappedQty).sum());
                vo.setComponentQty(qty.divide(bomQty).multiply(woQty).doubleValue());
                vo.setUom(qty.divide(bomQty).doubleValue());
                vo.setIssuedLocatorCode(locator == null ? "" : locator.getLocatorCode());
                vo.setBomComponentTypeDesc(typeMap.get(vo.getBomComponentType()));
                vo.setAssembleMethodDesc(methodMap.get(vo.getAssembleMethod()));
                vo.setLossQty(BigDecimal.valueOf(vo.getComponentQty()).multiply(vo.getAttritionChance()).divide(BigDecimal.valueOf(100)).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
            } else {// ??????????????????
                vo.setAssembleMethod("");
                vo.setAssembleMethodDesc("");
                vo.setBomComponentTypeDesc(typeMap.get(vo.getBomComponentType()));
            }
            vo.setBomSubstituteList(bomSubstituteForUi(tenantId, dto.getWorkOrderId(), vo));
        }

        return base;
    }



    private List<MtWorkOrderVO44> bomSubstituteForUi(Long tenantId, String workOrderId, MtWorkOrderVO42 dto) {
        MtBomSubstituteVO mtBomSubstituteVO = new MtBomSubstituteVO();
        mtBomSubstituteVO.setBomComponentId(dto.getBomComponentId());
        List<MtBomSubstituteVO2> bomSubstitutes =
                mtBomSubstituteRepository.propertyLimitBomSubstituteQuery(tenantId, mtBomSubstituteVO);
        if (CollectionUtils.isEmpty(bomSubstitutes)) {
            return Collections.emptyList();
        }
        List<String> groupIds = bomSubstitutes.stream().map(MtBomSubstituteVO2::getBomSubstituteGroupId)
                .collect(Collectors.toList());
        List<String> substituteIds = bomSubstitutes.stream().map(MtBomSubstituteVO2::getBomSubstituteId)
                .collect(Collectors.toList());
        List<MtBomSubstituteGroup> bomSubstituteGroupList =
                mtBomSubstituteGroupRepository.bomSubstituteGroupBasicBatchGet(tenantId, groupIds);
        List<MtBomSubstitute> bomSubstituteList =
                mtBomSubstituteRepository.bomSubstituteBasicBatchGet(tenantId, substituteIds);

        ThreadPoolTaskExecutor poolExecutor = executorConfig.asyncServiceExecutor();
        List<String> materialIds =
                bomSubstituteList.stream().map(MtBomSubstitute::getMaterialId).collect(Collectors.toList());

        List<MtMaterialVO> mtMaterialVOS = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(materialIds)) {
            try {
                // ?????????????????????????????????
                Future<List<MtMaterialVO>> materialFuture =
                        mtThreadPoolRepository.getMaterialFuture(poolExecutor, tenantId, materialIds);
                mtMaterialVOS = materialFuture.get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, MtMaterialVO> materialMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
            materialMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t));
        }

        Map<String, MtBomSubstituteGroup> groupByGroupId = bomSubstituteGroupList.stream()
                .collect(Collectors.toMap(MtBomSubstituteGroup::getBomSubstituteGroupId, t -> t));

        Map<String, MtBomSubstitute> groupByBomSubstituteId = bomSubstituteList.stream()
                .collect(Collectors.toMap(MtBomSubstitute::getBomSubstituteId, t -> t));

        List<MtGenType> genTypeList = mtGenTypeRepository.getGenTypes(tenantId, "BOM", "SUBSTITUTE_POLICY");
        Map<String, String> genTypeMap = genTypeList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        List<MtWorkOrderVO44> resultList = new ArrayList<MtWorkOrderVO44>(bomSubstitutes.size());

        // ?????????????????????
        for (MtBomSubstituteVO2 mtBomSubstituteVO2 : bomSubstitutes) {
            MtBomSubstituteGroup mtBomSubstituteGroup =
                    groupByGroupId.get(mtBomSubstituteVO2.getBomSubstituteGroupId());
            MtBomSubstitute mtBomSubstitute = groupByBomSubstituteId.get(mtBomSubstituteVO2.getBomSubstituteId());
            MtMaterialVO mtMaterialVO =
                    mtBomSubstitute == null ? null : materialMap.get(mtBomSubstitute.getMaterialId());

            MtWorkOrderVO44 resVO = new MtWorkOrderVO44();
            resVO.setBomSubstituteId(mtBomSubstituteVO2.getBomSubstituteId());
            resVO.setBomSubstituteGroupId(mtBomSubstituteVO2.getBomSubstituteGroupId());
            resVO.setSubstituteGroup(mtBomSubstituteGroup == null ? "" : mtBomSubstituteGroup.getSubstituteGroup());
            resVO.setSubstitutePolicy(mtBomSubstituteGroup == null ? "" : mtBomSubstituteGroup.getSubstitutePolicy());
            resVO.setSubstitutePolicyDesc(genTypeMap.get(resVO.getSubstitutePolicy()));
            resVO.setMaterialId(mtBomSubstitute == null ? "" : mtBomSubstitute.getMaterialId());
            resVO.setMaterialCode(mtMaterialVO == null ? "" : mtMaterialVO.getMaterialCode());
            resVO.setMaterialName(mtMaterialVO == null ? "" : mtMaterialVO.getMaterialName());
            resVO.setSubstituteValue(mtBomSubstitute == null ? null : mtBomSubstitute.getSubstituteValue());
            resVO.setSubstituteUsage(mtBomSubstitute == null ? null : mtBomSubstitute.getSubstituteUsage());

            // ??????????????????
            MtWoComponentActualVO23 mtWoComponentActualVO23 = new MtWoComponentActualVO23();
            mtWoComponentActualVO23.setBomComponentId(mtBomSubstituteVO2.getBomComponentId());
            mtWoComponentActualVO23.setWorkOrderId(workOrderId);
            List<MtWoComponentActualVO11> substituteMaterialList = mtWorkOrderComponentActualRepository
                    .woAssembledSubstituteMaterialQuery(tenantId, mtWoComponentActualVO23);
            if (mtBomSubstitute != null && CollectionUtils.isNotEmpty(substituteMaterialList)) {
                Optional<MtWoComponentActualVO11> materialOp = substituteMaterialList.stream()
                        .filter(t -> t.getMaterialId().equals(mtBomSubstitute.getMaterialId())).findFirst();
                if (materialOp.isPresent()) {
                    resVO.setAssembleQty(materialOp.get().getAssembleQty());
                }
            }
            resultList.add(resVO);
        }
        return resultList;
    }

    @Override
    @ProcessLovValue
    public List<MtWorkOrderVO51> eoListForUi(Long tenantId, MtWorkOrderVO46 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }
        Page<MtWorkOrderVO51> base =
                PageHelper.doPage(pageRequest, () -> mtEoMapper.selectByWorkOrder(tenantId, dto));
        if (CollectionUtils.isEmpty(base)) {
            return Collections.emptyList();
        }

        ThreadPoolTaskExecutor poolExecutor = executorConfig.asyncServiceExecutor();
        List<String> siteIds = base.stream().map(MtWorkOrderVO51::getSiteId).collect(Collectors.toList());
        List<String> prodlineIds = base.stream().map(MtWorkOrderVO51::getProductionLineId).collect(Collectors.toList());
        List<String> uomIds = base.stream().map(MtWorkOrderVO51::getUomId).collect(Collectors.toList());

        List<MtModSite> mtModSites = new ArrayList<>();
        List<MtModProductionLine> prodlines = new ArrayList<>();
        List<MtUomVO> uoms = new ArrayList<>();

        try {
            // ????????????????????????
            Future<List<MtModSite>> modSiteFuture =
                    mtThreadPoolRepository.getModSiteFuture(poolExecutor, tenantId, siteIds);
            // ???????????????????????????
            Future<List<MtModProductionLine>> modProductionLineFuture =
                    mtThreadPoolRepository.getModProductionLineFuture(poolExecutor, tenantId, prodlineIds);
            Future<List<MtUomVO>> uomFuture = mtThreadPoolRepository.getUomFuture(poolExecutor, tenantId, uomIds);
            mtModSites = modSiteFuture.get();
            prodlines = modProductionLineFuture.get();
            uoms = uomFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, MtModSite> siteMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtModSites)) {
            siteMap = mtModSites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
        }
        Map<String, MtModProductionLine> prodlineMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(prodlines)) {
            prodlineMap = prodlines.stream().collect(Collectors.toMap(MtModProductionLine::getProdLineId, t -> t));
        }
        Map<String, MtUomVO> uomMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(uoms)) {
            uomMap = uoms.stream().collect(Collectors.toMap(MtUomVO::getUomId, t -> t));
        }

        // ??????EO????????????
        List<MtGenStatus> eoStatusList = mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "EO_STATUS");
        Map<String, String> eoStatusMap = eoStatusList.stream()
                .collect(Collectors.toMap(MtGenStatus::getStatusCode, MtGenStatus::getDescription));

        // ????????????????????????
        List<MtGenType> assembleMethodList = mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "EO_TYPE");
        Map<String, String> assembleMethodMap = assembleMethodList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        //??????????????????
        List<String> eoList= base.stream().map(MtWorkOrderVO51::getEoId).collect(Collectors.toList());
        List<MtWorkOrderVO51> ncRecordList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(eoList)){
            ncRecordList = mtEoMapper.ncInfoFlagQuery(tenantId,eoList);
        }
        Map<String,Integer> ncRecordMap = ncRecordList.stream().collect(Collectors.toMap(MtWorkOrderVO51::getEoId,MtWorkOrderVO51::getNcRecordCount));

        // ????????????
        for (MtWorkOrderVO51 vo : base) {
            MtModSite mtModSite = siteMap.get(vo.getSiteId());
            MtModProductionLine mtModProductionLine = prodlineMap.get(vo.getProductionLineId());
            MtUomVO mtUomVO = uomMap.get(vo.getUomId());

            vo.setStatusDesc(eoStatusMap.get(vo.getStatus()));
            vo.setSiteCode(mtModSite == null ? "" : mtModSite.getSiteCode());
            vo.setSiteName(mtModSite == null ? "" : mtModSite.getSiteName());
            vo.setUomName(mtUomVO.getUomName());
            vo.setEoTypeDesc(assembleMethodMap.get(vo.getEoType()));
            vo.setProductionLineCode(mtModProductionLine == null ? "" : mtModProductionLine.getProdLineCode());
            vo.setProductionLineName(mtModProductionLine == null ? "" : mtModProductionLine.getProdLineName());

            //2020-07-09 add by sanfeng.zhang
            //EO????????????EO????????????????????????EO?????? EO?????????EO?????????sql?????????
            vo.setEoWorkcellIdDesc(hmeSnBindEoRepository.eoWorkcellIdDescQuery(tenantId,vo.getEoId()));

            // ??????bom
            String bomId = mtEoBomRepository.eoBomGet(tenantId, vo.getEoId());

            // ??????router
            String routerId = mtEoRouterRepository.eoRouterGet(tenantId, vo.getEoId());
            if (StringUtils.isNotEmpty(bomId)) {
                MtBomVO7 mtBomVO7 = mtBomRepository.bomBasicGet(tenantId, bomId);
                if (mtBomVO7 != null) {
                    vo.setEoBomName(mtBomVO7.getBomName());
                }

            }
            if (StringUtils.isNotEmpty(routerId)) {
                MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, routerId);
                if (mtRouter != null) {
                    vo.setEoRouterName(mtRouter.getRouterName());
                }
            }

            //????????????????????????
            Integer ncRecord = ncRecordMap.get(vo.getEoId());
            if (ncRecord != null && ncRecord.compareTo(0) > 0){
                vo.setNcFlag("Y");
            }else {
                vo.setNcFlag("N");
            }
        }
        return base;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String woSaveForUi(Long tenantId, MtWorkOrderVO48 dto) {
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_UPDATE");

        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventRequestId(eventRequestId);
        mtEventCreateVO.setEventTypeCode("WO_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        // ????????????API
        MtWorkOrderVO wo = new MtWorkOrderVO();
        BeanUtils.copyProperties(dto, wo);
        wo.setEventId(eventId);
        MtWorkOrderVO28 woUpdate = mtWorkOrderRepository.woUpdate(tenantId, wo, "N");
        return woUpdate.getWorkOrderId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woReleaseForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_RELEASE");

        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventRequestId(eventRequestId);
        mtEventCreateVO.setEventTypeCode("WO_VALIDATE_VERIFY_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        // ????????????????????????????????????
        mtWorkOrderRepository.woValidateVerifyUpdate(tenantId, workOrderId, eventId);
        // ????????????????????????
        mtWorkOrderRepository.woReleaseVerify(tenantId, workOrderId);
        // ??????????????????
        MtWorkOrderVO23 mtWorkOrderVO23 = new MtWorkOrderVO23();
        mtWorkOrderVO23.setEventRequestId(eventRequestId);
        mtWorkOrderVO23.setWorkOrderId(workOrderId);
        mtWorkOrderRepository.woRelease(tenantId, mtWorkOrderVO23);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woHoldForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        mtWorkOrderRepository.woHoldVerify(tenantId, workOrderId);

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_EO_HOLD");
        // ??????????????????
        MtWorkOrderVO2 mtWorkOrderVO2 = new MtWorkOrderVO2();
        mtWorkOrderVO2.setWorkOrderId(workOrderId);
        mtWorkOrderVO2.setEventRequestId(eventRequestId);
        mtWorkOrderRepository.woHold(tenantId, mtWorkOrderVO2);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woHoldCancelForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_HOLD_CANCEL");

        // ??????????????????
        MtWorkOrderVO15 mtWorkOrderVO15 = new MtWorkOrderVO15();
        mtWorkOrderVO15.setEventRequestId(eventRequestId);
        mtWorkOrderVO15.setWorkOrderId(workOrderId);
        mtWorkOrderRepository.woHoldCancel(tenantId, mtWorkOrderVO15);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woCompleteForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }
        // ??????
        mtWorkOrderRepository.woStatusCompleteVerify(tenantId, workOrderId);

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_STATUS_COMPLETE");
        // ??????????????????
        MtWorkOrderVO2 mtWorkOrderVO2 = new MtWorkOrderVO2();
        mtWorkOrderVO2.setWorkOrderId(workOrderId);
        mtWorkOrderVO2.setEventRequestId(eventRequestId);
        mtWorkOrderRepository.woStatusComplete(tenantId, mtWorkOrderVO2);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woCompleteCancelForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_STATUS_COMPLETE_CANCEL");

        // ????????????????????????
        MtWorkOrderVO5 mtWorkOrderVO5 = new MtWorkOrderVO5();
        mtWorkOrderVO5.setEventRequestId(eventRequestId);
        mtWorkOrderVO5.setWorkOrderId(workOrderId);
        mtWorkOrderRepository.woStatusCompleteCancel(tenantId, mtWorkOrderVO5);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woCloseForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }
        mtWorkOrderRepository.woCloseVerify(tenantId, workOrderId);

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_CLOSED");
        // ??????????????????
        MtWorkOrderVO2 mtWorkOrderVO2 = new MtWorkOrderVO2();
        mtWorkOrderVO2.setEventRequestId(eventRequestId);
        mtWorkOrderVO2.setWorkOrderId(workOrderId);
        mtWorkOrderRepository.woClose(tenantId, mtWorkOrderVO2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woCloseCancelForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_CLOSE_CANCEL");
        // ????????????????????????
        // 20210915 modiy by sanfeng.zhang ??????api??????
        MtWorkOrderVO2 mtWorkOrderVO2 = new MtWorkOrderVO2();
        mtWorkOrderVO2.setEventRequestId(eventRequestId);
        mtWorkOrderVO2.setWorkOrderId(workOrderId);
        mtWorkOrderRepository.woCloseCancel(tenantId, mtWorkOrderVO2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woAbandonForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        mtWorkOrderRepository.woStatusUpdateVerify(tenantId, workOrderId, "ABANDON");

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_ABANDON");

        // ????????????????????????
        MtWorkOrderVO5 mtWorkOrderVO5 = new MtWorkOrderVO5();
        mtWorkOrderVO5.setEventRequestId(eventRequestId);
        mtWorkOrderVO5.setWorkOrderId(workOrderId);
        mtWorkOrderRepository.woAbandon(tenantId, mtWorkOrderVO5);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> eoCreateForUi(Long tenantId, MtWorkOrderVO50 dto) {
        //2020-07-20 add by sanfeng.zhang ???????????????eo???Id
        List<String> eoIdList = new ArrayList<>();

        MtWorkOrderVO3 mtWorkOrderVO3 = new MtWorkOrderVO3();
        mtWorkOrderVO3.setTrxReleasedQty(dto.getTrxReleasedQty());
        mtWorkOrderVO3.setWorkOrderId(dto.getWorkOrderId());
        long startDate = System.currentTimeMillis();
        mtWorkOrderRepository.woLimitEoCreateVerify(tenantId, mtWorkOrderVO3);

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_LIMIT_EO_BATCH_CREATE");

        BigDecimal unitQty = new BigDecimal(dto.getUnitQty());
        BigDecimal trxReleasedQty = new BigDecimal(dto.getTrxReleasedQty());
        BigDecimal eoCount = new BigDecimal(dto.getEoCount());

        // ??????eoCount??????????????????
        BigDecimal eoCountRemainder = eoCount.divideAndRemainder(BigDecimal.ONE)[1];
        if (eoCountRemainder.compareTo(BigDecimal.ZERO) != 0) {
            throw new MtException("MT_ORDER_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0060", "ORDER", "eoCount", ""));
        }

        // ????????????
        BigDecimal remainder = trxReleasedQty.divideAndRemainder(unitQty)[1];

        if (remainder.compareTo(BigDecimal.ZERO) == 0 || eoCount.compareTo(BigDecimal.ONE) == 0) {// ??????????????????1
            MtEoVO14 mtEoVO14 = new MtEoVO14();
            mtEoVO14.setEoCount(String.valueOf(dto.getEoCount().intValue()));
            mtEoVO14.setEventRequestId(eventRequestId);
            mtEoVO14.setTotalQty(dto.getTrxReleasedQty());
            mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
            startDate = System.currentTimeMillis();
            List<String> eoIds = mtEoRepository.woLimitEoBatchCreate(tenantId, mtEoVO14);
            log.info("=================================>??????????????????-????????????-woReleaseForUi-woLimitEoBatchCreate????????????"+(System.currentTimeMillis() - startDate) + "??????");
            eoIdList.addAll(eoIds);
        } else {// ????????????
            // ????????????
            MtEoVO14 mtEoVO14 = new MtEoVO14();
            mtEoVO14.setEoCount(String.valueOf(dto.getEoCount().intValue()));
            mtEoVO14.setEventRequestId(eventRequestId);
            // ???EO???????????????1???*??????EO??????
            mtEoVO14.setTotalQty(eoCount.subtract(BigDecimal.ONE).multiply(unitQty).doubleValue());
            mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
            startDate = System.currentTimeMillis();
            List<String> eoIds = mtEoRepository.woLimitEoBatchCreate(tenantId, mtEoVO14);
            log.info("=================================>??????????????????-????????????-woReleaseForUi-????????????woLimitEoBatchCreate????????????"+(System.currentTimeMillis() - startDate) + "??????");
            eoIdList.addAll(eoIds);

            // ????????????
            MtEoVO6 mtEoVO6 = new MtEoVO6();
            mtEoVO6.setEventRequestId(eventRequestId);
            // ????????????-???EO???????????????1???*??????EO??????
            mtEoVO6.setQty(trxReleasedQty.subtract(eoCount.subtract(BigDecimal.ONE).multiply(unitQty)).doubleValue());
            mtEoVO6.setWorkOrderId(dto.getWorkOrderId());
            startDate = System.currentTimeMillis();
            String eoId = mtEoRepository.woLimitEoCreate(tenantId, mtEoVO6);
            log.info("=================================>??????????????????-????????????-woReleaseForUi-????????????woLimitEoCreate????????????"+(System.currentTimeMillis() - startDate) + "??????");
            eoIdList.add(eoId);
        }

        return eoIdList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woStatusUpdateForUi(Long tenantId, MtWorkOrderVO52 dto) {
        if (StringUtils.isEmpty(dto.getOperationType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "operationType", ""));
        }

        String operationType = dto.getOperationType();
        String workOrderId = dto.getWorkOrderId();
        switch (operationType) {
            case "RELEASE":// ??????
                woReleaseForUi(tenantId, workOrderId);
                break;
            case "HOLD":// ??????
                woHoldForUi(tenantId, workOrderId);
                break;
            case "HOLD_CANCEL":// ????????????
                woHoldCancelForUi(tenantId, workOrderId);
                break;
            case "COMPLETE":// ??????
                woCompleteForUi(tenantId, workOrderId);
                break;
            case "COMPLETE_CANCEL":// ????????????
                woCompleteCancelForUi(tenantId, workOrderId);
                break;
            case "CLOSE":// ??????
                woCloseForUi(tenantId, workOrderId);
                break;
            case "CLOSE_CANCEL":// ????????????
                woCloseCancelForUi(tenantId, workOrderId);
                break;
            case "ABANDON":// ??????
                woAbandonForUi(tenantId, workOrderId);
                break;
            case "EORELEASED":
                // ???????????? add by sanfeng.zhang for zhenyong.ban ??????????????????????????????HME.COC_PRODUCTION_LINE ?????????????????????
                hmeCosCommonService.woEoReleasedForUi(tenantId, workOrderId);
                break;
            default:
                break;
        }

    }

    @Override
    public Page<MtWorkOrderVO53> woRelListForUi(Long tenantId, MtWorkOrderVO54 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        // ????????????????????????
        Page<MtWorkOrderVO53> base =
                PageHelper.doPage(pageRequest, () -> mtWorkOrderRelMapper.selectByWorkOrderId(tenantId, dto));
        if (CollectionUtils.isEmpty(base)) {
            return base;
        }

        ThreadPoolTaskExecutor poolExecutor = executorConfig.asyncServiceExecutor();
        List<String> materialIds =
                base.parallelStream().map(MtWorkOrderVO53::getMaterialId).collect(Collectors.toList());
        List<MtMaterialVO> mtMaterialVOS = new ArrayList<>();
        try {
            // ?????????????????????????????????
            Future<List<MtMaterialVO>> materialFuture =
                    mtThreadPoolRepository.getMaterialFuture(poolExecutor, tenantId, materialIds);

            mtMaterialVOS = materialFuture.get();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, MtMaterialVO> materialMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
            materialMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t));
        }
        // ???????????????????????????
        List<MtGenType> orderTypeList = mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "WO_TYPE");
        List<MtGenStatus> statusList = mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "WO_STATUS");
        List<MtGenType> relList = mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "WO_REL_PARENT_CHILD");
        List<MtGenType> relTypeList = mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "WO_LEVEL_REL_TYPE");

        Map<String, String> orderTypeMap = orderTypeList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        Map<String, String> statusMap = statusList.stream()
                .collect(Collectors.toMap(MtGenStatus::getStatusCode, MtGenStatus::getDescription));
        Map<String, String> relMap =
                relList.stream().collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        Map<String, String> relTypeMap = relTypeList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        // ????????????
        for (MtWorkOrderVO53 vo : base) {
            MtMaterialVO materialVO = materialMap.get(vo.getMaterialId());
            vo.setMaterialCode(materialVO == null ? "" : materialVO.getMaterialCode());
            vo.setMaterialName(materialVO == null ? "" : materialVO.getMaterialName());
            vo.setWorkOrderTypeDesc(orderTypeMap.get(vo.getWorkOrderType()));
            vo.setStatusDesc(statusMap.get(vo.getStatus()));
            vo.setRelDesc(relMap.get(vo.getRel()));
            vo.setRelTypeDesc(relTypeMap.get(vo.getRelType()));
        }

        return base;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String woSplitForUi(Long tenantId, MtWorkOrderVO55 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }

        // ??????
        MtWorkOrderVO11 mtWorkOrderVO11 = new MtWorkOrderVO11();
        mtWorkOrderVO11.setSplitQty(dto.getSplitQty());
        mtWorkOrderVO11.setSourceWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderRepository.woSplitVerify(tenantId, mtWorkOrderVO11);

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_SPLIT");

        // ??????
        MtWorkOrderVO13 mtWorkOrderVO13 = new MtWorkOrderVO13();
        mtWorkOrderVO13.setSourceWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderVO13.setSplitQty(dto.getSplitQty());
        mtWorkOrderVO13.setEventRequestId(eventRequestId);

        return mtWorkOrderRepository.woSplit(tenantId, mtWorkOrderVO13);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String woMergeForUi(Long tenantId, MtWorkOrderVO56 dto) {
        if (StringUtils.isEmpty(dto.getPrimaryWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "primaryWorkOrderId", ""));
        }

        if (CollectionUtils.isEmpty(dto.getSecondaryWorkOrderIds())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "secondaryWorkOrderIds", ""));
        }

        // ??????
        MtWorkOrderVO10 mtWorkOrderVO10 = new MtWorkOrderVO10();
        mtWorkOrderVO10.setPrimaryWorkOrderId(dto.getPrimaryWorkOrderId());
        mtWorkOrderVO10.setSecondaryWorkOrderIds(dto.getSecondaryWorkOrderIds());
        mtWorkOrderRepository.woMergeVerify(tenantId, mtWorkOrderVO10);

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_MERGE");

        // ??????
        MtWorkOrderVO9 mtWorkOrderVO9 = new MtWorkOrderVO9();
        mtWorkOrderVO9.setPrimaryWorkOrderId(dto.getPrimaryWorkOrderId());
        mtWorkOrderVO9.setSecondaryWorkOrderIds(dto.getSecondaryWorkOrderIds());
        mtWorkOrderVO9.setEventRequestId(eventRequestId);

        return mtWorkOrderRepository.woMerge(tenantId, mtWorkOrderVO9);
    }

    @Override
    public MtWorkOrderVO57 woQtyForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            return null;
        }

        // ??????WO????????????
        MtWorkOrderActualVO mtWorkOrderActualVO = new MtWorkOrderActualVO();
        mtWorkOrderActualVO.setWorkOrderId(workOrderId);
        MtWorkOrderActual woActualGet = mtWorkOrderActualRepository.woActualGet(tenantId, mtWorkOrderActualVO);

        // ?????????????????????
        MtWorkOrderVO19 mtWorkOrderVO19 = mtWorkOrderRepository.woCompleteControlGet(tenantId, workOrderId);
        BigDecimal maxqty = BigDecimal.ZERO;
        Double completeControlQty = 0D;
        String controlType = null;
        if (mtWorkOrderVO19 != null) {
            controlType = mtWorkOrderVO19.getCompleteControlType();
            completeControlQty = mtWorkOrderVO19.getCompleteControlQty();

            // 1.???????????????completeControlType?????????
            if (null != controlType && !"FIX".equals(controlType) && !"PERCENT".equals(controlType)) {
                controlType = null;
            }

            // 2.????????????completeControlType??????????????????completeControlQty????????????????????????
            if (null == controlType || null == completeControlQty) {
                controlType = "FIX";
                completeControlQty = 0D;
            }

            // 3.???????????????completeControlType=FIX
            if ("FIX".equals(controlType)) {
                maxqty = BigDecimal.valueOf(completeControlQty).add(BigDecimal.valueOf(mtWorkOrder.getQty()));
            }

            // 4.???????????????completeControlType=PERCENT
            //2002-8-4 update by sanfeng.zhang ????????????????????????
            if ("PERCENT".equals(controlType)) {
                maxqty = BigDecimal.valueOf(completeControlQty)
                        .divide(BigDecimal.valueOf(100), 6, BigDecimal.ROUND_HALF_DOWN).add(BigDecimal.ONE)
                        .multiply(BigDecimal.valueOf(mtWorkOrder.getQty()));
            }

        }

        // ??????WO????????????
        MtWorkOrderVO57 resultVO = new MtWorkOrderVO57();
        resultVO.setWorkOrderId(workOrderId);
        resultVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
        resultVO.setQty(mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty());
        resultVO.setMaxQty(maxqty.doubleValue());
        resultVO.setReleasedQty(woActualGet == null ? Double.valueOf(0.0D) : woActualGet.getReleasedQty());
        resultVO.setCompletedQty(woActualGet == null ? Double.valueOf(0.0D) : woActualGet.getCompletedQty());
        resultVO.setCanQty(maxqty.subtract(BigDecimal.valueOf(resultVO.getReleasedQty())).doubleValue());
        resultVO.setCompleteControlQty(completeControlQty);
        resultVO.setCompleteControlType(controlType);
        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bomSplitForUi(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", ""));
        }


        mtWorkOrderRepository.bomLimitWoCreate(tenantId, workOrderId);

    }

}
