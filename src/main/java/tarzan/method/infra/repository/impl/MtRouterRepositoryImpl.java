package tarzan.method.infra.repository.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.lang.Console;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendAttrVO3;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtEoRouterActual;
import tarzan.actual.domain.repository.MtEoRouterActualRepository;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.vo.MtEoRouterActualVO11;
import tarzan.actual.domain.vo.MtEoRouterActualVO9;
import tarzan.actual.domain.vo.MtEoStepActualVO1;
import tarzan.actual.domain.vo.MtEoStepActualVO10;
import tarzan.actual.infra.mapper.MtEoRouterActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.util.Constant;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.*;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.repository.MtEoRouterRepository;

import static io.tarzan.common.domain.util.MtBaseConstants.NO;
import static io.tarzan.common.domain.util.MtBaseConstants.YES;

/**
 * 工艺路线 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
@Slf4j
public class MtRouterRepositoryImpl extends BaseRepositoryImpl<MtRouter> implements MtRouterRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private MtEventRepository iMtEventRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private MtRouterHisRepository iMtRouterHisRepository;

    @Autowired
    private MtRouterSiteAssignRepository mtRouterSiteAssignRepository;

    @Autowired
    private MtRouterStepRepository iMtRouterStepRepository;

    @Autowired
    private MtRouterStepGroupStepRepository mtRouterStepGroupStepRepository;

    @Autowired
    private MtRouterNextStepRepository iMtRouterNextStepRepository;

    @Autowired
    private MtRouterReturnStepRepository mtRouterReturnStepRepository;

    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;

    @Autowired
    private MtBomComponentRepository iMtBomComponentRepository;

    @Autowired
    private MtRouterMapper mtRouterMapper;

    @Autowired
    private MtRouterSiteAssignMapper mtRouterSiteAssignMapper;

    @Autowired
    private MtRouterStepMapper mtRouterStepMapper;

    @Autowired
    private MtRouterStepGroupMapper mtRouterStepGroupMapper;

    @Autowired
    private MtRouterOperationMapper mtRouterOperationMapper;

    @Autowired
    private MtRouterOperationComponentMapper mtRouterOperationComponentMapper;

    @Autowired
    private MtRouterLinkMapper mtRouterLinkMapper;

    @Autowired
    private MtRouterDoneStepMapper mtRouterDoneStepMapper;

    @Autowired
    private MtRouterReturnStepMapper mtRouterReturnStepMapper;

    @Autowired
    private MtRouterNextStepMapper mtRouterNextStepMapper;

    @Autowired
    private MtRouterSubstepMapper mtRouterSubstepMapper;

    @Autowired
    private MtRouterSubstepComponentMapper mtRouterSubstepComponentMapper;

    @Autowired
    private MtRouterStepGroupStepMapper mtRouterStepGroupStepMapper;

    @Autowired
    private MtSubstepMapper mtSubstepMapper;

    @Autowired
    private MtEoRouterActualMapper mtEoRouterActualMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Override
    public MtRouter routerGet(Long tenantId, String routerId) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerGet】"));
        }

        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterId(routerId);
        mtRouter = mtRouterMapper.selectOne(mtRouter);

        return mtRouter;
    }

    @Override
    public List<MtRouter> routerBatchGet(Long tenantId, List<String> routerIds) {
        if (CollectionUtils.isEmpty(routerIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerBatchGet】"));
        }
        return this.mtRouterMapper.selectRouterByIds(tenantId, routerIds);
    }

    @Override
    public String routerCurrentVersionGet(Long tenantId, String router) {
        if (StringUtils.isEmpty(router)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "router", "【API:routerCurrentVersionGet】"));
        }

        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterName(router);
        mtRouter.setCurrentFlag("Y");
        mtRouter = this.mtRouterMapper.selectOne(mtRouter);

        return mtRouter == null ? "" : mtRouter.getRouterId();
    }

    @Override
    public List<MtRouterVO5> routerAllVersionQuery(Long tenantId, String router) {
        if (StringUtils.isEmpty(router)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "router", "【API:routerAllVersionQuery】"));
        }

        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterName(router);
        List<MtRouter> mtRouters = this.mtRouterMapper.select(mtRouter);
        if (CollectionUtils.isEmpty(mtRouters)) {
            return Collections.emptyList();
        }

        List<MtRouterVO5> resultList = new ArrayList<MtRouterVO5>(mtRouters.size());
        mtRouters.forEach(t -> {
            MtRouterVO5 result = new MtRouterVO5();
            result.setRouterId(t.getRouterId());
            result.setRevision(t.getRevision());
            result.setRouterType(t.getRouterType());
            resultList.add(result);
        });
        return resultList;
    }

    @Override
    public String routerReleasedFlagValidate(Long tenantId, String routerId) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerReleasedFlagValidate】"));
        }

        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterId(routerId);
        mtRouter = mtRouterMapper.selectOne(mtRouter);

        return mtRouter == null || StringUtils.isEmpty(mtRouter.getHasBeenReleasedFlag()) ? "N"
                        : mtRouter.getHasBeenReleasedFlag();
    }

    @Override
    public void routerAvailabilityValidate(Long tenantId, String routerId) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerAvailabilityValidate】"));
        }

        if (0 == this.mtRouterMapper.selectRouterAvailability(tenantId, routerId)) {
            throw new MtException("MT_ROUTER_0028", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0028", "ROUTER", "routerId", "【API:routerAvailabilityValidate】"));
        }
    }

    @Override
    public List<String> routerTypeQuery(Long tenantId, String routerType) {
        if (StringUtils.isEmpty(routerType)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerType", "【API:routerTypeQuery】"));
        }

        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterType(routerType);
        List<MtRouter> mtRouters = this.mtRouterMapper.select(mtRouter);
        if (CollectionUtils.isEmpty(mtRouters)) {
            return Collections.emptyList();
        } else {
            return mtRouters.stream().map(MtRouter::getRouterId).collect(Collectors.toList());
        }
    }

    @Override
    public String routerBomGet(Long tenantId, String routerId) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerBomGet】"));
        }

        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterId(routerId);
        mtRouter = mtRouterMapper.selectOne(mtRouter);

        return mtRouter == null ? "" : mtRouter.getBomId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> routerCurrentVersionUpdate(Long tenantId, String routerId) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerCurrentVersionUpdate】"));
        }

        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterId(routerId);
        mtRouter = mtRouterMapper.selectOne(mtRouter);
        if (mtRouter == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:routerCurrentVersionUpdate】"));
        }

        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date currentDate = new Date(System.currentTimeMillis());
        MtRouter updateRouter = new MtRouter();
        updateRouter.setTenantId(tenantId);
        updateRouter.setRouterId(routerId);
        updateRouter.setCurrentFlag("Y");
        updateRouter.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_cid_s")));
        updateRouter.setLastUpdateDate(currentDate);
        updateRouter.setLastUpdatedBy(userId);

        List<String> sqlList = new ArrayList<String>(customDbRepository.getUpdateSql(updateRouter));

        List<MtRouterVO5> routerVersionList = routerAllVersionQuery(tenantId, mtRouter.getRouterName());

        List<String> routerIds = routerVersionList.stream().map(MtRouterVO5::getRouterId).distinct()
                        .collect(Collectors.toList());

        routerIds.remove(routerId);

        for (String c : routerIds) {
            MtRouter updateMtRouter = new MtRouter();
            updateMtRouter.setTenantId(tenantId);
            updateMtRouter.setRouterId(c);
            updateMtRouter.setCurrentFlag("N");
            updateMtRouter.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_cid_s")));
            updateMtRouter.setLastUpdateDate(currentDate);
            updateMtRouter.setLastUpdatedBy(userId);
            sqlList.addAll(customDbRepository.getUpdateSql(updateMtRouter));
        }

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return routerIds;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String routerReleasedFlagUpdate(Long tenantId, String routerId) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerReleasedFlagUpdate】"));
        }

        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterId(routerId);
        mtRouter = mtRouterMapper.selectOne(mtRouter);
        if (mtRouter == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:routerReleasedFlagUpdate】"));
        }

        mtRouter.setHasBeenReleasedFlag("Y");

        self().updateByPrimaryKey(mtRouter);
        return mtRouter.getRouterId();
    }

    @Override
    public void routerDataValidate(Long tenantId, MtRouterVO10 routerData) {
        if (routerData == null || routerData.getRouter() == null) {
            throw new MtException("MT_ROUTER_0011", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0011", "ROUTER", "【API:routerDataValidate】"));
        }

        List<String> enableFlags = Arrays.asList("Y", "N");
        // Router的必输项校验
        MtRouter router = routerData.getRouter();
        if (StringUtils.isEmpty(router.getRouterName())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "route:stepName", "【API:routerDataValidate】"));
        }
        if (StringUtils.isEmpty(router.getRouterType())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "route:stepType", "【API:routerDataValidate】"));
        }
        if (StringUtils.isEmpty(router.getRevision())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "route:revision", "【API:routerDataValidate】"));
        }
        if (StringUtils.isEmpty(router.getRouterStatus())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "route:status", "【API:routerDataValidate】"));
        }
        if (router.getDateFrom() == null) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "route:dateFrom", "【API:routerDataValidate】"));
        }

        // 如果沒有RouterStep数据则返回
        List<MtRouterStepVO> routerStepVOs = routerData.getRouterSteps();
        if (CollectionUtils.isEmpty(routerStepVOs)) {
            return;
        }

        Map<String, Long> routerStepGroupBy = routerStepVOs.stream()
                        .filter(r -> r != null && r.getRouterStep() != null
                                        && StringUtils.isNotEmpty(r.getRouterStep().getRouterStepId()))
                        .map(MtRouterStepVO::getRouterStep)
                        .collect(Collectors.groupingBy(MtRouterStep::getRouterStepId, Collectors.counting()));
        if (routerStepGroupBy.values().stream().anyMatch(t -> t > 1L)) {
            throw new MtException("MT_ROUTER_0024", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0024", "ROUTER", "routerStepId", "【API:routerDataValidate】"));
        }

        List<MtRouterStepGroupStep> allRouterStepGroupSteps = new ArrayList<MtRouterStepGroupStep>();
        Map<String, MtRouterStepVO> allRouterSteps = new HashMap<String, MtRouterStepVO>();
        for (MtRouterStepVO routerStepVO : routerStepVOs) {
            if (routerStepVO == null) {
                continue;
            }

            MtRouterStep routerStep = routerStepVO.getRouterStep();
            if (routerStep == null) {
                continue;
            }

            if (StringUtils.isEmpty(routerStep.getRouterStepId())) {
                throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0001", "ROUTER", "routerStep:routerStepId", "【API:routerDataValidate】"));
            }
            if (StringUtils.isEmpty(routerStep.getStepName())) {
                throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0001", "ROUTER", "routerStep:stepName", "【API:routerDataValidate】"));
            }
            if (StringUtils.isEmpty(routerStep.getRouterStepType())) {
                throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0001", "ROUTER", "routerStep:stepType", "【API:routerDataValidate】"));
            }
            if (routerStep.getSequence() == null) {
                throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0001", "ROUTER", "routerStep:sequence", "【API:routerDataValidate】"));
            }

            /*
             * mtRouter中routerId不等于mtRouterStep中routerId若结果不为空则输出[O1]=false,[O2] =“ 传入ROUTER_STEP与ROUTER不一致，
             * routerStepId/stepName:xxx/xxxx”(MT_ROUTER_0009); 新加逻辑，只有是update模式下，才执行这步校验
             */
            if ("update".equalsIgnoreCase(routerStepVO.getProcessFlag())) {
                if (StringUtils.isNotEmpty(router.getRouterId())
                                && !router.getRouterId().equals(routerStep.getRouterId())) {
                    throw new MtException("MT_ROUTER_0009",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0009", "ROUTER",
                                                    routerStep.getRouterStepId(), routerStep.getStepName(),
                                                    "【API:routerDataValidate】"));
                }
            }

            if ("OPERATION".equals(routerStep.getRouterStepType())) {
                // 当mtRouterStep中routerStepType=“OPERATION”时，routerOperation中必须且仅能有一行数据，routerLink以及routerStepGroup中不可有数据
                MtRouterOperationVO routerOperationVO = routerStepVO.getRouterOperation();
                if (routerOperationVO == null || routerOperationVO.getRouterOperation() == null) {
                    throw new MtException("MT_ROUTER_0010",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0010", "ROUTER",
                                                    routerStep.getRouterStepId(), routerStep.getStepName(),
                                                    "【API:routerDataValidate】"));
                }

                if (routerStepVO.getRouterLink() != null || routerStepVO.getRouterStepGroup() != null) {
                    throw new MtException("MT_ROUTER_0010",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0010", "ROUTER",
                                                    routerStep.getRouterStepId(), routerStep.getStepName(),
                                                    "【API:routerDataValidate】"));
                }

                MtRouterOperation tmpMtRouterOperation = routerOperationVO.getRouterOperation();
                if (StringUtils.isEmpty(tmpMtRouterOperation.getOperationId())) {
                    throw new MtException("MT_ROUTER_0001",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER",
                                                    "routerOperation: operationId", "【API:routerDataValidate】"));
                }
                List<MtRouterOperationComponent> tmpRouterOperationComponents =
                                routerOperationVO.getRouterOperationComponents();
                if (CollectionUtils.isNotEmpty(tmpRouterOperationComponents)) {
                    for (MtRouterOperationComponent t : tmpRouterOperationComponents) {
                        if (StringUtils.isEmpty(t.getBomComponentId())) {
                            throw new MtException("MT_ROUTER_0001",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                            "ROUTER", "routerOperationComponent: bomComponentId",
                                                            "【API:routerDataValidate】"));
                        }
                        if (t.getSequence() == null) {
                            throw new MtException("MT_ROUTER_0001",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                            "ROUTER", "routerOperationComponent: sequence",
                                                            "【API:routerDataValidate】"));
                        }
                        if (null != t.getEnableFlag() && !enableFlags.contains(t.getEnableFlag())) {
                            throw new MtException("MT_ROUTER_0033", mtErrorMessageRepo.getErrorMessageWithModule(
                                            tenantId, "MT_ROUTER_0033", "ROUTER", "【API:routerDataValidate】"));
                        }
                    }
                }
            } else if ("ROUTER".equals(routerStep.getRouterStepType())) {
                // 当mtRouterStep中routerStepType=“ROUTER”时，routerLink中必须且仅能有一行数据,routerOperation以及routerStepGroup中不可有数据
                MtRouterLink mtRouterLink = routerStepVO.getRouterLink();
                if (mtRouterLink == null) {
                    throw new MtException("MT_ROUTER_0010",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0010", "ROUTER",
                                                    routerStep.getRouterStepId(), routerStep.getStepName(),
                                                    "【API:routerDataValidate】"));
                }

                if (routerStepVO.getRouterOperation() != null || routerStepVO.getRouterStepGroup() != null) {
                    throw new MtException("MT_ROUTER_0010",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0010", "ROUTER",
                                                    routerStep.getRouterStepId(), routerStep.getStepName(),
                                                    "【API:routerDataValidate】"));
                }

                if (StringUtils.isEmpty(mtRouterLink.getRouterId())) {
                    throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_ROUTER_0001", "ROUTER", "routerLink: routerId", "【API:routerDataValidate】"));
                }

                // 当步骤为嵌套工艺路线时，此嵌套工艺路线不能是需要验证的工艺路线本身
                if (StringUtils.isNotEmpty(router.getRouterId())
                                && router.getRouterId().equals(mtRouterLink.getRouterId())) {
                    throw new MtException("MT_ROUTER_0014",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0014", "ROUTER",
                                                    routerStep.getRouterStepId(), routerStep.getStepName(),
                                                    "【API:routerDataValidate】"));
                }
            } else if ("GROUP".equals(routerStep.getRouterStepType())) {
                // 当mtRouterStep中routerStepType=“GROUP”时，routerStepGroup中必须且仅能有一行数据,routerLink以及routerOperation中不可有数据
                MtRouterStepGroupVO routerStepGroupVO = routerStepVO.getRouterStepGroup();
                if (routerStepGroupVO == null || routerStepGroupVO.getRouterStepGroup() == null) {
                    throw new MtException("MT_ROUTER_0010",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0010", "ROUTER",
                                                    routerStep.getRouterStepId(), routerStep.getStepName(),
                                                    "【API:routerDataValidate】"));
                }
                if (routerStepVO.getRouterLink() != null || routerStepVO.getRouterOperation() != null) {
                    throw new MtException("MT_ROUTER_0010",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0010", "ROUTER",
                                                    routerStep.getRouterStepId(), routerStep.getStepName(),
                                                    "【API:routerDataValidate】"));
                }

                MtRouterStepGroup tmpMtRouterStepGroup = routerStepGroupVO.getRouterStepGroup();
                if (StringUtils.isEmpty(tmpMtRouterStepGroup.getRouterStepGroupType())) {
                    throw new MtException("MT_ROUTER_0001",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER",
                                                    "routerStepGroup: routerStepGroupType",
                                                    "【API:routerDataValidate】"));
                }

                // 步骤组内必须有两个或两个以上步骤
                List<MtRouterStepGroupStep> eachRouterStepGroupSteps = routerStepGroupVO.getRouterStepGroupSteps();
                if (CollectionUtils.isEmpty(eachRouterStepGroupSteps) || eachRouterStepGroupSteps.size() == 1) {
                    throw new MtException("MT_ROUTER_0016",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0016", "ROUTER",
                                                    routerStep.getRouterStepId(), routerStep.getStepName(),
                                                    "【API:routerDataValidate】"));
                }

                for (MtRouterStepGroupStep eachRouterStepGroupStep : eachRouterStepGroupSteps) {
                    if (StringUtils.isEmpty(eachRouterStepGroupStep.getRouterStepId())) {
                        throw new MtException("MT_ROUTER_0001",
                                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                        "ROUTER", "routerStepGroupStep:routerStepId",
                                                        "【API:routerDataValidate】"));
                    }
                }
                allRouterStepGroupSteps.addAll(eachRouterStepGroupSteps);
            } else {
                // 步骤只能是工艺、步骤组以及嵌套工艺路线中的其中一种
                throw new MtException("MT_ROUTER_0010",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0010", "ROUTER",
                                                routerStep.getRouterId(), routerStep.getStepName(),
                                                "【API:routerDataValidate】"));
            }

            List<MtRouterSubstepVO> routerSubstepVOs = routerStepVO.getRouterSubsteps();
            if (CollectionUtils.isNotEmpty(routerSubstepVOs)) {
                for (MtRouterSubstepVO t : routerSubstepVOs) {
                    MtRouterSubstep tmpRouterSubstep = t.getRouterSubstep();
                    if (tmpRouterSubstep != null) {
                        if (StringUtils.isEmpty(tmpRouterSubstep.getSubstepId())) {
                            throw new MtException("MT_ROUTER_0001",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                            "ROUTER", "routerSubstep:substepId",
                                                            "【API:routerDataValidate】"));
                        }
                        if (tmpRouterSubstep.getSequence() == null) {
                            throw new MtException("MT_ROUTER_0001",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                            "ROUTER", "routerSubstep:sequence",
                                                            "【API:routerDataValidate】"));
                        }
                    }
                    List<MtRouterSubstepComponent> tmpMtRouterSubstepComponent = t.getRouterSubstepComponents();
                    if (CollectionUtils.isNotEmpty(tmpMtRouterSubstepComponent)) {
                        tmpMtRouterSubstepComponent.forEach(c -> {
                            if (StringUtils.isEmpty(c.getBomComponentId())) {
                                throw new MtException("MT_ROUTER_0001",
                                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                                "ROUTER", "routerSubstepComponent:bomComponentId",
                                                                "【API:routerDataValidate】"));
                            }
                            if (c.getSequence() == null) {
                                throw new MtException("MT_ROUTER_0001",
                                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                                "ROUTER", "routerSubstepComponent:sequence",
                                                                "【API:routerDataValidate】"));
                            }
                        });
                    }
                }
            }

            List<MtRouterNextStep> mtRouterNextSteps = routerStepVO.getRouterNextSteps();
            if (CollectionUtils.isNotEmpty(mtRouterNextSteps)) {
                for (MtRouterNextStep t : mtRouterNextSteps) {
                    if (t != null) {
                        if (StringUtils.isEmpty(t.getNextStepId())) {
                            throw new MtException("MT_ROUTER_0001",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                            "ROUTER", "routerNextstep:nextStepId",
                                                            "【API:routerDataValidate】"));
                        }
                        if (StringUtils.isEmpty(t.getNextDecisionType())) {
                            throw new MtException("MT_ROUTER_0001",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                            "ROUTER", "routerNextstep:nextDecisionType",
                                                            "【API:routerDataValidate】"));
                        }
                    }
                }
            }

            allRouterSteps.put(routerStep.getRouterStepId(), routerStepVO);
        }

        // 当步骤属于步骤组时，有且只能属于一个步骤组
        Map<String, List<MtRouterStepGroupStep>> tmpGroupMapClone = new HashMap<String, List<MtRouterStepGroupStep>>();
        final StringBuilder tmpRouterStepId = new StringBuilder();
        final StringBuilder tmpRouterStep = new StringBuilder();
        if (CollectionUtils.isNotEmpty(allRouterStepGroupSteps)) {
            final Map<String, List<MtRouterStepGroupStep>> tmpGroupMap = allRouterStepGroupSteps.stream()
                            .collect(Collectors.groupingBy(MtRouterStepGroupStep::getRouterStepId));

            for (Map.Entry<String, List<MtRouterStepGroupStep>> entry : tmpGroupMap.entrySet()) {
                if (entry.getValue().size() > 1) {
                    tmpRouterStepId.append(entry.getKey()).append(",");
                    Optional<String> optional =
                                    allRouterSteps.keySet().stream().filter(c -> c.equals(entry.getKey())).findFirst();
                    if (optional.isPresent()) {
                        tmpRouterStep.append(allRouterSteps.get(optional.get()).getRouterStep().getStepName())
                                        .append(",");
                    } else {
                        tmpRouterStep.append(",");
                    }
                    throw new MtException("MT_ROUTER_0012",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0012", "ROUTER",
                                                    tmpRouterStepId.toString().substring(0,
                                                                    tmpRouterStepId.length() - 1),
                                                    tmpRouterStep.toString().substring(0, tmpRouterStep.length() - 1),
                                                    "【API:routerDataValidate】"));
                }
            }

            // 校验：当步骤属于步骤组时，只能为工艺类型且不能为完成步骤或返回步骤
            for (String t : tmpGroupMap.keySet()) {
                Optional<String> optional = allRouterSteps.keySet().stream().filter(c -> c.equals(t)).findFirst();
                if (!optional.isPresent()) {
                    throw new MtException("MT_ROUTER_0013", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_ROUTER_0013", "ROUTER", t, "", "【API:routerDataValidate】"));
                } else {
                    MtRouterStepVO tmpRouterStepVO = allRouterSteps.get(optional.get());
                    MtRouterOperationVO tmpRouterOperationVO = tmpRouterStepVO.getRouterOperation();
                    if (!(tmpRouterOperationVO != null && tmpRouterOperationVO.getRouterOperation() != null
                                    && tmpRouterStepVO.getRouterDoneStep() == null
                                    && tmpRouterStepVO.getRouterReturnStep() == null)) {
                        throw new MtException("MT_ROUTER_0013",
                                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0013",
                                                        "ROUTER", t, tmpRouterStepVO.getRouterStep().getStepName(),
                                                        "【API:routerDataValidate】"));
                    }
                }
            }
            tmpGroupMapClone.putAll(tmpGroupMap);
        }

        // 当工艺路线类型为NC或特殊时，最后一个步骤为返回步骤, 当工艺路线类型不为NC或特殊时，最后一个步骤为完成步骤
        final Map<String, MtRouterStepVO> tmpRouterSteps = new HashMap<String, MtRouterStepVO>();
        allRouterSteps.keySet().stream().forEach(t -> {
            if (!tmpGroupMapClone.keySet().contains(t)) {
                tmpRouterSteps.put(t, allRouterSteps.get(t));
            }
        });

        /* 校验如果步骤不是任何步骤的下一步骤并且不是不是入口步骤 */

        /**
         * 2019-07-25 add by zijin.liang start
         */
        log.info("=========================DetailsHelper.getUserDetails().getUserId:"
                        + DetailsHelper.getUserDetails().getUserId());
        log.info("=========================DetailsHelper.getUserDetails().getRoleId:"
                        + DetailsHelper.getUserDetails().getRoleId());
        log.info("=========================profileClient:" + profileClient);
        String keyStepToGetNextStep =
                        profileClient.getProfileValueByOptions(tenantId, DetailsHelper.getUserDetails().getUserId(),
                                        DetailsHelper.getUserDetails().getRoleId(), "KEY_STEP_TO_GET_NEXT_STEP");
        if (StringUtils.isEmpty(keyStepToGetNextStep)) {
            keyStepToGetNextStep = "N";
        }

        if ("N".equals(keyStepToGetNextStep)) {
            for (MtRouterStepVO s : tmpRouterSteps.values()) {
                if (!"Y".equals(s.getRouterStep().getEntryStepFlag())) {
                    boolean hasdata = false;
                    for (MtRouterStepVO s2 : tmpRouterSteps.values()) {
                        if (!s.getRouterStep().getRouterStepId().equals(s2.getRouterStep().getRouterStepId())
                                        && s2.getRouterNextSteps() != null) {
                            for (int i = 0; i < s2.getRouterNextSteps().size(); i++) {
                                if (s2.getRouterNextSteps().get(i).getNextStepId()
                                                .equals(s.getRouterStep().getRouterStepId())) {
                                    hasdata = true;
                                    break;
                                }
                            }
                        }
                        if (hasdata) {
                            break;
                        }
                    }
                    if (!hasdata) {
                        throw new MtException("MT_ROUTER_0027",
                                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0027",
                                                        "ROUTER", "stepName:" + s.getRouterStep().getStepName(),
                                                        "【API:routerDataValidate】"));
                    }
                }
            }

            if ("NC".equals(router.getRouterType()) || "SPECIAL".equals(router.getRouterType())) {
                tmpRouterSteps.values().forEach(t -> {
                    if (CollectionUtils.isEmpty(t.getRouterNextSteps())) {
                        if (t.getRouterReturnStep() == null || t.getRouterDoneStep() != null) {
                            throw new MtException("MT_ROUTER_0017", mtErrorMessageRepo.getErrorMessageWithModule(
                                            tenantId, "MT_ROUTER_0017", "ROUTER", t.getRouterStep().getRouterStepId(),
                                            t.getRouterStep().getStepName(), "【API:routerDataValidate】"));
                        }

                        MtRouterReturnStep tmpMtRouterReturnStep = t.getRouterReturnStep();
                        if (StringUtils.isEmpty(tmpMtRouterReturnStep.getReturnType())) {
                            throw new MtException("MT_ROUTER_0001",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                            "ROUTER", "routerReturnStep:returnType",
                                                            "【API:routerDataValidate】"));
                        }

                        // 返回步骤不能有下一个步骤
                        if (t.getRouterNextSteps() != null) {
                            throw new MtException("MT_ROUTER_0020", mtErrorMessageRepo.getErrorMessageWithModule(
                                            tenantId, "MT_ROUTER_0020", "ROUTER", t.getRouterStep().getRouterStepId(),
                                            t.getRouterStep().getStepName(), "【API:routerDataValidate】"));
                        }
                    } else {
                        // 下一个步骤不能有返回或完成步骤
                        if (t.getRouterReturnStep() != null || t.getRouterDoneStep() != null) {
                            throw new MtException("MT_ROUTER_0025", mtErrorMessageRepo.getErrorMessageWithModule(
                                            tenantId, "MT_ROUTER_0025", "ROUTER", t.getRouterStep().getRouterStepId(),
                                            t.getRouterStep().getStepName(), "【API:routerDataValidate】"));
                        }
                    }
                });
            } else {
                tmpRouterSteps.values().forEach(t -> {
                    if (CollectionUtils.isEmpty(t.getRouterNextSteps())) {
                        if (t.getRouterDoneStep() == null || t.getRouterReturnStep() != null) {
                            throw new MtException("MT_ROUTER_0018", mtErrorMessageRepo.getErrorMessageWithModule(
                                            tenantId, "MT_ROUTER_0018", "ROUTER", t.getRouterStep().getRouterStepId(),
                                            t.getRouterStep().getStepName(), "【API:routerDataValidate】"));
                        }

                        // 完成步骤不能有下一个步骤
                        if (t.getRouterNextSteps() != null) {
                            throw new MtException("MT_ROUTER_0019", mtErrorMessageRepo.getErrorMessageWithModule(
                                            tenantId, "MT_ROUTER_0019", "ROUTER", t.getRouterStep().getRouterStepId(),
                                            t.getRouterStep().getStepName(), "【API:routerDataValidate】"));
                        }
                    } else {
                        // 下一个步骤不能有返回或完成步骤
                        if (t.getRouterDoneStep() != null || t.getRouterReturnStep() != null) {
                            throw new MtException("MT_ROUTER_0025", mtErrorMessageRepo.getErrorMessageWithModule(
                                            tenantId, "MT_ROUTER_0025", "ROUTER", t.getRouterStep().getRouterStepId(),
                                            t.getRouterStep().getStepName(), "【API:routerDataValidate】"));
                        }
                    }
                });
            }
        }

        // 下一个步骤中的步骤在当前工艺路线下
        Set<String> allRouterStepIds = allRouterSteps.keySet();
        allRouterSteps.values().forEach(t -> {
            if (CollectionUtils.isNotEmpty(t.getRouterNextSteps())) {
                for (MtRouterNextStep mtRouterNextStep : t.getRouterNextSteps()) {
                    if (!allRouterStepIds.contains(mtRouterNextStep.getNextStepId())) {
                        throw new MtException("MT_ROUTER_0015",
                                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0015",
                                                        "ROUTER", t.getRouterStep().getRouterStepId(),
                                                        t.getRouterStep().getStepName(), "【API:routerDataValidate】"));
                    }
                }
            }
        });

        List<MtRouterStepVO> entrySteps = allRouterSteps.values().stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getRouterStep().getEntryStepFlag())
                                        && "Y".equals(t.getRouterStep().getEntryStepFlag()))
                        .collect(Collectors.toList());
        if (entrySteps.size() == 0) {
            throw new MtException("MT_ROUTER_0021", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0021", "ROUTER", "【API:routerDataValidate】"));
        }
        if (entrySteps.size() > 1) {
            tmpRouterStepId.append(entrySteps.stream().map(MtRouterStepVO::getRouterStep)
                            .map(MtRouterStep::getRouterStepId).collect(Collectors.joining(",")));
            tmpRouterStep.append(entrySteps.stream().map(MtRouterStepVO::getRouterStep).map(MtRouterStep::getStepName)
                            .collect(Collectors.joining(",")));
            throw new MtException("MT_ROUTER_0022",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0022", "ROUTER",
                                            tmpRouterStepId.toString(), tmpRouterStep.toString(),
                                            "【API:routerDataValidate】"));
        }

        /**
         * 2019-07-26 add by zijin.liang
         */
        if ("Y".equals(keyStepToGetNextStep)) {
            MtRouterStepVO entryRouterStepVO = entrySteps.get(0);
            if (!"Y".equals(entryRouterStepVO.getRouterStep().getKeyStepFlag())) {
                throw new MtException("MT_ROUTER_0035", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0035", "ROUTER", entryRouterStepVO.getRouterStep().getRouterStepId(),
                                entryRouterStepVO.getRouterStep().getStepName(), "【API:routerDataValidate】"));
            }

            List<MtRouterStepVO> allSteps = new ArrayList<>(allRouterSteps.values());
            for (MtRouterStepVO vo : allSteps) {
                if (null != vo.getRouterDoneStep() && !"Y".equals(vo.getRouterStep().getKeyStepFlag())) {
                    throw new MtException("MT_ROUTER_0035",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0035", "ROUTER",
                                                    vo.getRouterStep().getRouterStepId(),
                                                    vo.getRouterStep().getStepName(), "【API:routerDataValidate】"));
                }
                if (null != vo.getRouterReturnStep() && !"Y".equals(vo.getRouterStep().getKeyStepFlag())) {
                    throw new MtException("MT_ROUTER_0035",
                                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0035", "ROUTER",
                                                    vo.getRouterStep().getRouterStepId(),
                                                    vo.getRouterStep().getStepName(), "【API:routerDataValidate】"));
                }
            }

        }

        // 12. 松散模式下不允许设置步骤组
        if ("Y".equals(router.getRelaxedFlowFlag())) {
            List<MtRouterStepVO> groupRouterSteps = allRouterSteps.values().stream()
                            .filter(t -> "GROUP".equals(t.getRouterStep().getRouterStepType()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(groupRouterSteps)) {
                tmpRouterStepId.append(entrySteps.stream().map(MtRouterStepVO::getRouterStep)
                                .map(MtRouterStep::getRouterStepId).collect(Collectors.joining(",")));
                tmpRouterStep.append(entrySteps.stream().map(MtRouterStepVO::getRouterStep)
                                .map(MtRouterStep::getStepName).collect(Collectors.joining(",")));
                throw new MtException("MT_ROUTER_0030",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0030", "ROUTER",
                                                tmpRouterStepId.toString(), tmpRouterStep.toString(),
                                                "【API:routerDataValidate】"));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String routerCopy(Long tenantId, MtRouterVO1 condition) {
        if (StringUtils.isEmpty(condition.getRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerCopy】"));
        }
        if (StringUtils.isEmpty(condition.getRouterName())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerName", "【API:routerCopy】"));
        }
        if (StringUtils.isEmpty(condition.getRouterType())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerType", "【API:routerCopy】"));
        }
        if (CollectionUtils.isEmpty(condition.getSiteIds())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "siteId", "【API:routerCopy】"));
        }
        if (StringUtils.isEmpty(condition.getRevision())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "revision", "【API:routerCopy】"));
        }
        // 第一步：准备步骤数据[P1]
        MtRouter oldRouter = new MtRouter();
        oldRouter.setTenantId(tenantId);
        oldRouter.setRouterId(condition.getRouterId());
        oldRouter = mtRouterMapper.selectOne(oldRouter);
        if (oldRouter == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:routerCopy】"));
        }

        Map<String, Map<String, String>> routerTls = null;
        List<MtRouterVO2> routertl = this.mtRouterMapper.selectRouterTL(tenantId, condition.getRouterId());
        if (CollectionUtils.isNotEmpty(routertl)) {
            routerTls = new HashMap<String, Map<String, String>>();
            Map<String, String> map = new HashMap<String, String>();
            for (MtRouterVO2 tl : routertl) {
                map.put(tl.getLang(), tl.getDescription());
            }
            routerTls.put("description", map);
        }
        /**
         * add by peng.yuan 2019/11/11 【第2.5步：校验BOM】
         */
        // a,若[I5]为空，则直接进到第三步
        // b,若[I5]不为空，但[P1]中的bomId为空，则进到第三步，
        // c,若[I5]不为空，且[P1]中的bomId不为空，则比较[I5]和[P1]中的bomId若相同，则进入第三步
        // d,若不同则调用API{ BomLimitSouceBomGet }输入参数bomId=[P1]，获取输出参数sourceBomId赋予给过程参数[P24]，
        // 并比较[I5]和[P24]，若相同则进入第三步，e,若不同则报错：“输入装配清单不是由原工艺路线所属装配清单复制而来，请检查！”（MT_ROUTER_0070）
        // 这里根据逻辑只需要判断c,d,e步骤即可
        if (StringUtils.isNotEmpty(condition.getBomId()) && StringUtils.isNotEmpty(oldRouter.getBomId())
                        && !condition.getBomId().equals(oldRouter.getBomId())) {
            String sourceBomId = mtBomRepository.bomLimitSourceBomGet(tenantId, condition.getBomId());
            if (!oldRouter.getBomId().equals(sourceBomId)) {
                throw new MtException("MT_ROUTER_0070", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0070", "ROUTER", "【API:routerCopy】"));
            }
        }



        List<String> sqlList = new ArrayList<String>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());

        String newRouterId = this.customDbRepository.getNextKey("mt_router_s");
        oldRouter.setRouterId(newRouterId);
        oldRouter.setRouterName(condition.getRouterName());
        oldRouter.setRouterType(condition.getRouterType());
        oldRouter.setRevision(condition.getRevision());
        oldRouter.setCopiedFromRouterId(condition.getRouterId());
        if (StringUtils.isNotEmpty(condition.getBomId())) {
            oldRouter.setBomId(condition.getBomId());
        }
        oldRouter.setCreatedBy(userId);
        oldRouter.setCreationDate(now);
        oldRouter.setLastUpdateDate(now);
        oldRouter.setLastUpdatedBy(userId);
        oldRouter.set_tls(routerTls);
        oldRouter.setObjectVersionNumber(1L);
        oldRouter.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_cid_s")));
        oldRouter.setTenantId(tenantId);
        sqlList.addAll(customDbRepository.getInsertSql(oldRouter));

        /**
         * 新增逻辑：ROUTER_ATTR复制 add by zijin.liang 2019-08-21
         */
        List<MtExtendAttrVO3> routerAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId, "mt_router_attr",
                        "ROUTER_ID", Arrays.asList(condition.getRouterId()));
        if (CollectionUtils.isNotEmpty(routerAttrs)) {
            for (MtExtendAttrVO3 routerAttr : routerAttrs) {
                sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_attr", "ROUTER_ID",
                                newRouterId, routerAttr.getAttrName(), routerAttr.getAttrValue(), routerAttr.getLang(),
                                now, userId));
            }
        }

        // router site assign
        MtRouterSiteAssign mMtRouterSiteAssign = null;
        for (String siteId : condition.getSiteIds()) {
            mMtRouterSiteAssign = new MtRouterSiteAssign();
            mMtRouterSiteAssign.setRouterSiteAssignId(this.customDbRepository.getNextKey("mt_router_site_assign_s"));
            mMtRouterSiteAssign.setRouterId(newRouterId);
            mMtRouterSiteAssign.setSiteId(siteId);
            mMtRouterSiteAssign.setEnableFlag("Y");
            mMtRouterSiteAssign.setCreatedBy(userId);
            mMtRouterSiteAssign.setCreationDate(now);
            mMtRouterSiteAssign.setLastUpdateDate(now);
            mMtRouterSiteAssign.setLastUpdatedBy(userId);
            mMtRouterSiteAssign.setObjectVersionNumber(1L);
            mMtRouterSiteAssign.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_site_assign_cid_s")));
            mMtRouterSiteAssign.setTenantId(tenantId);
            sqlList.addAll(customDbRepository.getInsertSql(mMtRouterSiteAssign));
        }

        // [P2]
        MtRouterStep searchMtRouterStep = new MtRouterStep();
        searchMtRouterStep.setTenantId(tenantId);
        searchMtRouterStep.setRouterId(condition.getRouterId());
        List<MtRouterStep> mtRouterSteps = this.mtRouterStepMapper.select(searchMtRouterStep);

        if (CollectionUtils.isNotEmpty(mtRouterSteps)) {

            // add 2019/05/10
            Map<String, List<MtBomComponent>> bomComponentMaps = new HashMap<String, List<MtBomComponent>>();

            // 如果输入了I5 获取相关数据
            if (StringUtils.isNotEmpty(condition.getBomId())) {
                MtBomComponentVO bomComponentVO = new MtBomComponentVO();
                bomComponentVO.setBomId(condition.getBomId());

                List<MtBomComponentVO16> bomBomComponentMaps =
                                iMtBomComponentRepository.propertyLimitBomComponentQuery(tenantId, bomComponentVO);
                List<String> bomComponentIds = null;
                if (CollectionUtils.isNotEmpty(bomBomComponentMaps)) {
                    bomComponentIds = bomBomComponentMaps.get(0).getBomComponentId();
                }

                // [P19]
                List<MtBomComponentVO13> bomComponents =
                                iMtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);

                bomComponentMaps = bomComponents.stream()
                                .filter(t -> StringUtils.isNotEmpty(t.getCopiedFromComponentId()))
                                .collect(Collectors.groupingBy(MtBomComponent::getCopiedFromComponentId));
            }

            Map<String, String> routerMap = new HashMap<String, String>();
            Map<String, String> routerStepGroupMap = new HashMap<String, String>();
            Map<String, String> routerOperCompMap = new HashMap<String, String>();
            Map<String, String> routerSubStepMap = new HashMap<String, String>();
            Map<String, String> routerSubStepCompMap = new HashMap<String, String>();
            Map<String, String> routerNextStepMap = new HashMap<String, String>();
            Map<String, String> routerStepGroupStepMap = new HashMap<String, String>();

            for (MtRouterStep mtRouterStep : mtRouterSteps) {
                String oldRouterStepId = null;

                Map<String, Map<String, String>> routerStepTls = null;
                List<MtRouterStepVO4> routerSteptl =
                                this.mtRouterStepMapper.selectRouterStepTL(tenantId, mtRouterStep.getRouterStepId());
                if (CollectionUtils.isNotEmpty(routerSteptl)) {
                    routerStepTls = new HashMap<String, Map<String, String>>();
                    Map<String, String> map = new HashMap<String, String>();
                    for (MtRouterStepVO4 tl : routerSteptl) {
                        map.put(tl.getLang(), tl.getDescription());
                    }
                    routerStepTls.put("description", map);
                }

                oldRouterStepId = mtRouterStep.getRouterStepId();
                final String newRouterStepId = this.customDbRepository.getNextKey("mt_router_step_s");
                mtRouterStep.setRouterStepId(newRouterStepId);
                mtRouterStep.setRouterId(newRouterId);
                mtRouterStep.setCreatedBy(userId);
                mtRouterStep.setCreationDate(now);
                mtRouterStep.setLastUpdateDate(now);
                mtRouterStep.setLastUpdatedBy(userId);
                mtRouterStep.setObjectVersionNumber(1L);
                // 新加.19.4.16
                mtRouterStep.setCopiedFromRouterStepId(oldRouterStepId);
                mtRouterStep.set_tls(routerStepTls);
                mtRouterStep.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_step_cid_s")));
                mtRouterStep.setTenantId(tenantId);
                sqlList.addAll(customDbRepository.getInsertSql(mtRouterStep));

                routerMap.put(oldRouterStepId, newRouterStepId);

                // 第四步：复制MT_ROUTER_STEP_GROUP [P4]
                MtRouterStepGroup mtRouterStepGroup = new MtRouterStepGroup();
                mtRouterStepGroup.setTenantId(tenantId);
                mtRouterStepGroup.setRouterStepId(oldRouterStepId);
                List<MtRouterStepGroup> mtRouterStepGroups = this.mtRouterStepGroupMapper.select(mtRouterStepGroup);
                if (CollectionUtils.isNotEmpty(mtRouterStepGroups)) {
                    for (MtRouterStepGroup tmpMtRouterStepGroup : mtRouterStepGroups) {
                        String oldRouterStepGroupId = tmpMtRouterStepGroup.getRouterStepGroupId();
                        String newRouterStepGroupId = this.customDbRepository.getNextKey("mt_router_step_group_s");
                        tmpMtRouterStepGroup.setRouterStepGroupId(newRouterStepGroupId);
                        tmpMtRouterStepGroup.setRouterStepId(newRouterStepId);
                        tmpMtRouterStepGroup.setObjectVersionNumber(1L);
                        tmpMtRouterStepGroup.setCid(
                                        Long.valueOf(this.customDbRepository.getNextKey("mt_router_step_group_cid_s")));
                        tmpMtRouterStepGroup.setTenantId(tenantId);
                        sqlList.addAll(customDbRepository.getInsertSql(tmpMtRouterStepGroup));

                        routerStepGroupMap.put(oldRouterStepGroupId, newRouterStepGroupId);
                    }
                }
                // 第六步：复制MT_ROUTER_OPERATION
                MtRouterOperation oldMtRouterOperation = new MtRouterOperation();
                oldMtRouterOperation.setTenantId(tenantId);
                oldMtRouterOperation.setRouterStepId(oldRouterStepId);
                oldMtRouterOperation = this.mtRouterOperationMapper.selectOne(oldMtRouterOperation);
                if (oldMtRouterOperation != null) {
                    String oldRouterOperationId = null;

                    Map<String, Map<String, String>> routerOperationTls = null;
                    List<MtRouterOperationVO2> routerOperationtl = this.mtRouterOperationMapper
                                    .selectRouterOperationTL(tenantId, oldMtRouterOperation.getRouterOperationId());
                    if (CollectionUtils.isNotEmpty(routerOperationtl)) {
                        routerOperationTls = new HashMap<String, Map<String, String>>();
                        Map<String, String> map = new HashMap<String, String>();
                        for (MtRouterOperationVO2 tl : routerOperationtl) {
                            map.put(tl.getLang(), tl.getSpecialIntruction());
                        }
                        routerOperationTls.put("specialIntruction", map);
                    }

                    oldRouterOperationId = oldMtRouterOperation.getRouterOperationId();
                    final String newRouterOperationId = this.customDbRepository.getNextKey("mt_router_operation_s");
                    oldMtRouterOperation.setRouterOperationId(newRouterOperationId);
                    oldMtRouterOperation.setRouterStepId(newRouterStepId);
                    oldMtRouterOperation.setCreatedBy(userId);
                    oldMtRouterOperation.setCreationDate(now);
                    oldMtRouterOperation.setLastUpdateDate(now);
                    oldMtRouterOperation.setLastUpdatedBy(userId);
                    oldMtRouterOperation.setObjectVersionNumber(1L);
                    oldMtRouterOperation.set_tls(routerOperationTls);
                    oldMtRouterOperation.setCid(
                                    Long.valueOf(this.customDbRepository.getNextKey("mt_router_operation_cid_s")));
                    oldMtRouterOperation.setTenantId(tenantId);
                    sqlList.addAll(customDbRepository.getInsertSql(oldMtRouterOperation));

                    // 第十一步：复制MT_ROUTER_OPERATION_COMPONENT [P13]
                    MtRouterOperationComponent searchRouterOperationComponent = new MtRouterOperationComponent();
                    searchRouterOperationComponent.setTenantId(tenantId);
                    searchRouterOperationComponent.setRouterOperationId(oldRouterOperationId);
                    List<MtRouterOperationComponent> mtRouterOperationComponents =
                                    this.mtRouterOperationComponentMapper.select(searchRouterOperationComponent);
                    if (CollectionUtils.isNotEmpty(mtRouterOperationComponents)) {
                        if (StringUtils.isEmpty(condition.getBomId())) {
                            mtRouterOperationComponents.stream().forEach(c -> {
                                MtRouterOperationComponent mtRouterOperationComponent =
                                                new MtRouterOperationComponent();
                                mtRouterOperationComponent.setRouterOperationComponentId(
                                                this.customDbRepository.getNextKey("mt_router_operation_component_s"));
                                mtRouterOperationComponent.setRouterOperationId(newRouterOperationId);
                                mtRouterOperationComponent.setBomComponentId(c.getBomComponentId());
                                mtRouterOperationComponent.setSequence(c.getSequence());
                                mtRouterOperationComponent.setEnableFlag(c.getEnableFlag());
                                // mtRouterOperationComponent.setQty(c.getQty());2019-07-15
                                mtRouterOperationComponent.setCid(Long.valueOf(this.customDbRepository
                                                .getNextKey("mt_router_operation_component_cid_s")));
                                mtRouterOperationComponent.setObjectVersionNumber(1L);
                                mtRouterOperationComponent.setCreatedBy(userId);
                                mtRouterOperationComponent.setCreationDate(now);
                                mtRouterOperationComponent.setLastUpdatedBy(userId);
                                mtRouterOperationComponent.setLastUpdateDate(now);
                                mtRouterOperationComponent.setTenantId(tenantId);
                                sqlList.addAll(customDbRepository.getInsertSql(mtRouterOperationComponent));

                                /**
                                 * 添加old router oper comp id<->new router oper comp id关系 add by zijin.liang 2019-08-21
                                 */
                                routerOperCompMap.put(c.getRouterOperationComponentId(),
                                                mtRouterOperationComponent.getRouterOperationComponentId());
                            });
                        } else {
                            for (MtRouterOperationComponent c : mtRouterOperationComponents) {
                                String bomComponentId;
                                List<MtBomComponent> currentBomComponents = bomComponentMaps.get(c.getBomComponentId());
                                if (CollectionUtils.isNotEmpty(currentBomComponents)) {
                                    // 匹配的上的话，就只有一个
                                    bomComponentId = currentBomComponents.get(0).getBomComponentId();
                                } else {
                                    // 匹配不上，则不更新
                                    bomComponentId = c.getBomComponentId();
                                }

                                MtRouterOperationComponent mtRouterOperationComponent =
                                                new MtRouterOperationComponent();
                                mtRouterOperationComponent.setRouterOperationComponentId(
                                                this.customDbRepository.getNextKey("mt_router_operation_component_s"));

                                mtRouterOperationComponent.setRouterOperationId(newRouterOperationId);
                                mtRouterOperationComponent.setBomComponentId(bomComponentId);
                                mtRouterOperationComponent.setSequence(c.getSequence());
                                mtRouterOperationComponent.setEnableFlag(c.getEnableFlag());
                                // mtRouterOperationComponent.setQty(c.getQty());2019-07-15
                                mtRouterOperationComponent.setCid(Long.valueOf(this.customDbRepository
                                                .getNextKey("mt_router_operation_component_cid_s")));
                                mtRouterOperationComponent.setObjectVersionNumber(1L);
                                mtRouterOperationComponent.setCreatedBy(userId);
                                mtRouterOperationComponent.setCreationDate(now);
                                mtRouterOperationComponent.setLastUpdatedBy(userId);
                                mtRouterOperationComponent.setLastUpdateDate(now);
                                mtRouterOperationComponent.setTenantId(tenantId);
                                sqlList.addAll(customDbRepository.getInsertSql(mtRouterOperationComponent));

                                /**
                                 * 添加old router oper comp id<->new router oper comp id关系 add by zijin.liang 2019-08-21
                                 */
                                routerOperCompMap.put(c.getRouterOperationComponentId(),
                                                mtRouterOperationComponent.getRouterOperationComponentId());

                            }
                        }
                    }
                }
                // 第七步：复制MT_ROUTER_LINK
                MtRouterLink oldMtRouterLink = new MtRouterLink();
                oldMtRouterLink.setTenantId(tenantId);
                oldMtRouterLink.setRouterStepId(oldRouterStepId);
                oldMtRouterLink = this.mtRouterLinkMapper.selectOne(oldMtRouterLink);
                if (oldMtRouterLink != null) {
                    oldMtRouterLink.setRouterLinkId(this.customDbRepository.getNextKey("mt_router_link_s"));
                    oldMtRouterLink.setRouterStepId(newRouterStepId);
                    oldMtRouterLink.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_link_cid_s")));
                    oldMtRouterLink.setCreatedBy(userId);
                    oldMtRouterLink.setCreationDate(now);
                    oldMtRouterLink.setLastUpdateDate(now);
                    oldMtRouterLink.setLastUpdatedBy(userId);
                    oldMtRouterLink.setObjectVersionNumber(1L);
                    oldMtRouterLink.setTenantId(tenantId);
                    sqlList.addAll(customDbRepository.getInsertSql(oldMtRouterLink));
                }

                // 第八步：复制MT_ROUTER_DONE_STEP
                MtRouterDoneStep searchRouterDoneStep = new MtRouterDoneStep();
                searchRouterDoneStep.setTenantId(tenantId);
                searchRouterDoneStep.setRouterStepId(oldRouterStepId);
                List<MtRouterDoneStep> mtRouterDoneSteps = this.mtRouterDoneStepMapper.select(searchRouterDoneStep);
                if (CollectionUtils.isNotEmpty(mtRouterDoneSteps)) {
                    mtRouterDoneSteps.stream().forEach(c -> {
                        MtRouterDoneStep mtRouterDoneStep = new MtRouterDoneStep();
                        mtRouterDoneStep.setRouterDoneStepId(
                                        this.customDbRepository.getNextKey("mt_router_done_step_s"));
                        mtRouterDoneStep.setRouterStepId(newRouterStepId);
                        mtRouterDoneStep.setCid(
                                        Long.valueOf(this.customDbRepository.getNextKey("mt_router_done_step_cid_s")));
                        mtRouterDoneStep.setObjectVersionNumber(1L);
                        mtRouterDoneStep.setCreatedBy(userId);
                        mtRouterDoneStep.setCreationDate(now);
                        mtRouterDoneStep.setLastUpdateDate(now);
                        mtRouterDoneStep.setLastUpdatedBy(userId);
                        mtRouterDoneStep.setTenantId(tenantId);
                        sqlList.addAll(customDbRepository.getInsertSql(mtRouterDoneStep));
                    });
                }

                // 第九步：复制MT_ROUTER_RETURN_STEP [P10]
                MtRouterReturnStep searchRouterReturnStep = new MtRouterReturnStep();
                searchRouterReturnStep.setTenantId(tenantId);
                searchRouterReturnStep.setRouterStepId(oldRouterStepId);
                List<MtRouterReturnStep> mtRouterReturnSteps =
                                this.mtRouterReturnStepMapper.select(searchRouterReturnStep);
                if (CollectionUtils.isNotEmpty(mtRouterReturnSteps)) {
                    mtRouterReturnSteps.stream().forEach(c -> {
                        MtRouterReturnStep mtRouterReturnStep = new MtRouterReturnStep();
                        mtRouterReturnStep.setRouterReturnStepId(
                                        this.customDbRepository.getNextKey("mt_router_return_step_s"));
                        mtRouterReturnStep.setRouterStepId(newRouterStepId);
                        mtRouterReturnStep.setReturnType(c.getReturnType());
                        mtRouterReturnStep.setOperationId(c.getOperationId());
                        mtRouterReturnStep.setCompleteOriginalFlag(c.getCompleteOriginalFlag());
                        mtRouterReturnStep.setStepName(c.getStepName());
                        mtRouterReturnStep.setCid(Long
                                        .valueOf(this.customDbRepository.getNextKey("mt_router_return_step_cid_s")));
                        mtRouterReturnStep.setObjectVersionNumber(1L);
                        mtRouterReturnStep.setCreatedBy(userId);
                        mtRouterReturnStep.setCreationDate(now);
                        mtRouterReturnStep.setLastUpdateDate(now);
                        mtRouterReturnStep.setLastUpdatedBy(userId);
                        mtRouterReturnStep.setTenantId(tenantId);
                        sqlList.addAll(customDbRepository.getInsertSql(mtRouterReturnStep));
                    });
                }

                MtRouterSubstep oldMtRouterSubstep = new MtRouterSubstep();
                oldMtRouterSubstep.setTenantId(tenantId);
                oldMtRouterSubstep.setRouterStepId(oldRouterStepId);
                List<MtRouterSubstep> oldMtRouterSubsteps = this.mtRouterSubstepMapper.select(oldMtRouterSubstep);
                if (CollectionUtils.isNotEmpty(oldMtRouterSubsteps)) {
                    for (MtRouterSubstep mtRouterSubstep : oldMtRouterSubsteps) {
                        String oldRouterSubstepId = null;

                        oldRouterSubstepId = mtRouterSubstep.getRouterSubstepId();
                        final String newRouterSubstepId = this.customDbRepository.getNextKey("mt_router_substep_s");
                        mtRouterSubstep.setRouterSubstepId(newRouterSubstepId);
                        mtRouterSubstep.setRouterStepId(newRouterStepId);
                        mtRouterSubstep.setCreatedBy(userId);
                        mtRouterSubstep.setCreationDate(now);
                        mtRouterSubstep.setLastUpdateDate(now);
                        mtRouterSubstep.setLastUpdatedBy(userId);
                        mtRouterSubstep.setObjectVersionNumber(1L);
                        mtRouterSubstep.setCid(
                                        Long.valueOf(this.customDbRepository.getNextKey("mt_router_substep_cid_s")));
                        mtRouterSubstep.setTenantId(tenantId);
                        sqlList.addAll(customDbRepository.getInsertSql(mtRouterSubstep));

                        /**
                         * 增加old router substep id<->new router substep id关系 add by zijin.liang 2019-08-21
                         */
                        routerSubStepMap.put(oldRouterSubstepId, newRouterSubstepId);

                        MtRouterSubstepComponent searchRouterSubstepComponent = new MtRouterSubstepComponent();
                        searchRouterSubstepComponent.setTenantId(tenantId);
                        searchRouterSubstepComponent.setRouterSubstepId(oldRouterSubstepId);
                        List<MtRouterSubstepComponent> mtRouterSubstepComponents =
                                        this.mtRouterSubstepComponentMapper.select(searchRouterSubstepComponent);
                        if (CollectionUtils.isNotEmpty(mtRouterSubstepComponents)) {
                            if (StringUtils.isEmpty(condition.getBomId())) {
                                mtRouterSubstepComponents.stream().forEach(c -> {
                                    MtRouterSubstepComponent mtRouterSubstepComponent = new MtRouterSubstepComponent();
                                    mtRouterSubstepComponent.setRouterSubstepComponentId(this.customDbRepository
                                                    .getNextKey("mt_router_substep_component_s"));
                                    mtRouterSubstepComponent.setRouterSubstepId(newRouterSubstepId);
                                    mtRouterSubstepComponent.setBomComponentId(c.getBomComponentId());
                                    mtRouterSubstepComponent.setSequence(c.getSequence());
                                    mtRouterSubstepComponent.setQty(c.getQty());
                                    mtRouterSubstepComponent.setCreatedBy(userId);
                                    mtRouterSubstepComponent.setCreationDate(now);
                                    mtRouterSubstepComponent.setLastUpdateDate(now);
                                    mtRouterSubstepComponent.setLastUpdatedBy(userId);
                                    mtRouterSubstepComponent.setObjectVersionNumber(1L);
                                    mtRouterSubstepComponent.setCid(Long.valueOf(this.customDbRepository
                                                    .getNextKey("mt_router_substep_component_cid_s")));
                                    mtRouterSubstepComponent.setTenantId(tenantId);
                                    sqlList.addAll(customDbRepository.getInsertSql(mtRouterSubstepComponent));

                                    /**
                                     * 增加old router substep comp id<->new router substep comp id关系 add by zijin.liang
                                     * 2019-08-21
                                     */
                                    routerSubStepCompMap.put(c.getRouterSubstepComponentId(),
                                                    mtRouterSubstepComponent.getRouterSubstepComponentId());
                                });
                            } else {
                                for (MtRouterSubstepComponent c : mtRouterSubstepComponents) {
                                    String bomComponentId;
                                    List<MtBomComponent> currentBomComponents =
                                                    bomComponentMaps.get(c.getBomComponentId());
                                    if (CollectionUtils.isNotEmpty(currentBomComponents)) {
                                        // 匹配的上的话，就只有一个
                                        bomComponentId = currentBomComponents.get(0).getBomComponentId();
                                    } else {
                                        // 匹配不上，则不更新
                                        bomComponentId = c.getBomComponentId();
                                    }

                                    MtRouterSubstepComponent mtRouterSubstepComponent = new MtRouterSubstepComponent();
                                    mtRouterSubstepComponent.setRouterSubstepComponentId(this.customDbRepository
                                                    .getNextKey("mt_router_substep_component_s"));
                                    mtRouterSubstepComponent.setRouterSubstepId(newRouterSubstepId);
                                    mtRouterSubstepComponent.setBomComponentId(bomComponentId);
                                    mtRouterSubstepComponent.setSequence(c.getSequence());
                                    mtRouterSubstepComponent.setQty(c.getQty());
                                    mtRouterSubstepComponent.setCreatedBy(userId);
                                    mtRouterSubstepComponent.setCreationDate(now);
                                    mtRouterSubstepComponent.setLastUpdateDate(now);
                                    mtRouterSubstepComponent.setLastUpdatedBy(userId);
                                    mtRouterSubstepComponent.setObjectVersionNumber(1L);
                                    mtRouterSubstepComponent.setCid(Long.valueOf(this.customDbRepository
                                                    .getNextKey("mt_router_substep_component_cid_s")));
                                    mtRouterSubstepComponent.setTenantId(tenantId);
                                    sqlList.addAll(customDbRepository.getInsertSql(mtRouterSubstepComponent));

                                    /**
                                     * 增加old router substep comp id<->new router substep comp id关系 add by zijin.liang
                                     * 2019-08-21
                                     */
                                    routerSubStepCompMap.put(c.getRouterSubstepComponentId(),
                                                    mtRouterSubstepComponent.getRouterSubstepComponentId());
                                }
                            }
                        }
                    }
                }
            }

            for (Map.Entry<String, String> entry : routerMap.entrySet()) {
                // 第十步：复制MT_ROUTER_NEXT_STEP [P11]
                MtRouterNextStep mtRouterNextStep = new MtRouterNextStep();
                mtRouterNextStep.setTenantId(tenantId);
                mtRouterNextStep.setRouterStepId(entry.getKey());
                List<MtRouterNextStep> mtRouterNextSteps = this.mtRouterNextStepMapper.select(mtRouterNextStep);
                if (CollectionUtils.isNotEmpty(mtRouterNextSteps)) {
                    mtRouterNextSteps.stream().forEach(t -> {
                        if (t != null && routerMap.containsKey(t.getNextStepId())) {
                            String oldRouterNextStepId = t.getRouterNextStepId();
                            String newRouterNextStepId = this.customDbRepository.getNextKey("mt_router_next_step_s");

                            t.setRouterNextStepId(newRouterNextStepId);
                            t.setRouterStepId(entry.getValue());
                            t.setNextStepId(routerMap.get(t.getNextStepId()));
                            t.setCreatedBy(userId);
                            t.setCreationDate(now);
                            t.setLastUpdateDate(now);
                            t.setLastUpdatedBy(userId);
                            t.setObjectVersionNumber(1L);
                            t.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_next_step_cid_s")));
                            t.setTenantId(tenantId);
                            sqlList.addAll(customDbRepository.getInsertSql(t));

                            /**
                             * 增加old router next step id<->new router next step id关系 add by zijin.liang 2019-08-21
                             */
                            routerNextStepMap.put(oldRouterNextStepId, newRouterNextStepId);
                        }
                    });
                }
            }

            // routerStepGroupMap
            for (Map.Entry<String, String> entry : routerStepGroupMap.entrySet()) {
                // 第五步：复制MT_ROUTER_STEP_GROUP_STEP [P6]
                MtRouterStepGroupStep mtRouterStepGroupStep = new MtRouterStepGroupStep();
                mtRouterStepGroupStep.setTenantId(tenantId);
                mtRouterStepGroupStep.setRouterStepGroupId(entry.getKey());
                List<MtRouterStepGroupStep> mtRouterStepGroupSteps =
                                this.mtRouterStepGroupStepMapper.select(mtRouterStepGroupStep);
                if (CollectionUtils.isNotEmpty(mtRouterStepGroupSteps)) {
                    mtRouterStepGroupSteps.stream().forEach(t -> {
                        if (t != null && routerMap.containsKey(t.getRouterStepId())) {
                            String oldRouterStepGroupStepId = t.getRouterStepGroupStepId();
                            String newRouterStepGroupStepId =
                                            this.customDbRepository.getNextKey("mt_router_step_group_step_s");

                            t.setRouterStepGroupStepId(newRouterStepGroupStepId);
                            t.setRouterStepGroupId(entry.getValue());
                            t.setRouterStepId(routerMap.get(t.getRouterStepId()));
                            t.setCreatedBy(userId);
                            t.setCreationDate(now);
                            t.setLastUpdateDate(now);
                            t.setLastUpdatedBy(userId);
                            t.setObjectVersionNumber(1L);
                            t.setCid(Long.valueOf(
                                            this.customDbRepository.getNextKey("mt_router_step_group_step_cid_s")));
                            t.setTenantId(tenantId);
                            sqlList.addAll(customDbRepository.getInsertSql(t));

                            /**
                             * 增加old router step group step id<->new router step group step id关系 add by zijin.liang
                             * 2019-08-21
                             */
                            routerStepGroupStepMap.put(oldRouterStepGroupStepId, newRouterStepGroupStepId);
                        }
                    });
                }
            }

            /**
             * 新增逻辑：复制ROUTER STEP ATTR
             */
            if (MapUtils.isNotEmpty(routerMap)) {
                List<String> oldRouterStepIds =
                                routerMap.entrySet().stream().map(t -> t.getKey()).collect(Collectors.toList());
                List<MtExtendAttrVO3> routerStepAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                                "mt_router_step_attr", "ROUTER_STEP_ID", oldRouterStepIds);
                if (CollectionUtils.isNotEmpty(routerStepAttrs)) {
                    for (MtExtendAttrVO3 routerStepAttr : routerStepAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_step_attr",
                                        "ROUTER_STEP_ID", routerMap.get(routerStepAttr.getMainTableKeyValue()),
                                        routerStepAttr.getAttrName(), routerStepAttr.getAttrValue(),
                                        routerStepAttr.getLang(), now, userId));
                    }
                }
            }

            /**
             * 新增逻辑：复制ROUTER OPERATION COMP ATTR
             */
            if (MapUtils.isNotEmpty(routerOperCompMap)) {
                List<String> oldRouterOperCompIds =
                                routerOperCompMap.entrySet().stream().map(t -> t.getKey()).collect(Collectors.toList());
                List<MtExtendAttrVO3> routerOperCompAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                                "mt_router_operation_c_attr", "ROUTER_OPERATION_COMPONENT_ID", oldRouterOperCompIds);
                if (CollectionUtils.isNotEmpty(routerOperCompAttrs)) {
                    for (MtExtendAttrVO3 routerOperCompAttr : routerOperCompAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_operation_c_attr",
                                        "ROUTER_OPERATION_COMPONENT_ID",
                                        routerOperCompMap.get(routerOperCompAttr.getMainTableKeyValue()),
                                        routerOperCompAttr.getAttrName(), routerOperCompAttr.getAttrValue(),
                                        routerOperCompAttr.getLang(), now, userId));
                    }
                }
            }

            /**
             * 新增逻辑：复制ROUTER SUBSTEP ATTR
             */
            if (MapUtils.isNotEmpty(routerSubStepMap)) {
                List<String> oldRouterSubStepIds =
                                routerSubStepMap.entrySet().stream().map(t -> t.getKey()).collect(Collectors.toList());
                List<MtExtendAttrVO3> routerSubStepAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                                "mt_router_substep_attr", "ROUTER_SUBSTEP_ID", oldRouterSubStepIds);

                if (CollectionUtils.isNotEmpty(routerSubStepAttrs)) {
                    for (MtExtendAttrVO3 routerSubStepAttr : routerSubStepAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_substep_attr",
                                        "ROUTER_SUBSTEP_ID",
                                        routerSubStepMap.get(routerSubStepAttr.getMainTableKeyValue()),
                                        routerSubStepAttr.getAttrName(), routerSubStepAttr.getAttrValue(),
                                        routerSubStepAttr.getLang(), now, userId));
                    }
                }
            }

            /**
             * 新增逻辑：复制ROUTER SUBSTEP COMP ATTR
             */
            if (MapUtils.isNotEmpty(routerSubStepCompMap)) {
                List<String> oldRouterSubStepCompIds = routerSubStepCompMap.entrySet().stream().map(t -> t.getKey())
                                .collect(Collectors.toList());
                List<MtExtendAttrVO3> routerSubStepCompAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                                "mt_router_substep_c_attr", "ROUTER_SUBSTEP_COMPONENT_ID", oldRouterSubStepCompIds);

                if (CollectionUtils.isNotEmpty(routerSubStepCompAttrs)) {
                    for (MtExtendAttrVO3 routerSubStepCompAttr : routerSubStepCompAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_substep_c_attr",
                                        "ROUTER_SUBSTEP_COMPONENT_ID",
                                        routerSubStepCompMap.get(routerSubStepCompAttr.getMainTableKeyValue()),
                                        routerSubStepCompAttr.getAttrName(), routerSubStepCompAttr.getAttrValue(),
                                        routerSubStepCompAttr.getLang(), now, userId));
                    }
                }
            }

            /**
             * 新增逻辑：复制ROUTER NEXT STEP ATTR
             */
            if (MapUtils.isNotEmpty(routerNextStepMap)) {
                List<String> oldRouterNextStepIds =
                                routerNextStepMap.entrySet().stream().map(t -> t.getKey()).collect(Collectors.toList());
                List<MtExtendAttrVO3> routerNextStepAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                                "mt_router_next_step_attr", "ROUTER_NEXT_STEP_ID", oldRouterNextStepIds);

                if (CollectionUtils.isNotEmpty(routerNextStepAttrs)) {
                    for (MtExtendAttrVO3 routerNextStepAttr : routerNextStepAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_next_step_attr",
                                        "ROUTER_NEXT_STEP_ID",
                                        routerNextStepMap.get(routerNextStepAttr.getMainTableKeyValue()),
                                        routerNextStepAttr.getAttrName(), routerNextStepAttr.getAttrValue(),
                                        routerNextStepAttr.getLang(), now, userId));
                    }
                }
            }

            /**
             * 新增逻辑：复制ROUTER STEP GROUP STEP
             */
            if (MapUtils.isNotEmpty(routerStepGroupStepMap)) {
                List<String> oldRouterStepGroupStepIds = routerStepGroupStepMap.entrySet().stream().map(t -> t.getKey())
                                .collect(Collectors.toList());
                List<MtExtendAttrVO3> routerStepGroupStepAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                                "mt_router_st_gr_st_attr", "ROUTER_STEP_GROUP_STEP_ID", oldRouterStepGroupStepIds);

                if (CollectionUtils.isNotEmpty(routerStepGroupStepAttrs)) {
                    for (MtExtendAttrVO3 routerStepGroupStepAttr : routerStepGroupStepAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_st_gr_st_attr",
                                        "ROUTER_STEP_GROUP_STEP_ID",
                                        routerStepGroupStepMap.get(routerStepGroupStepAttr.getMainTableKeyValue()),
                                        routerStepGroupStepAttr.getAttrName(), routerStepGroupStepAttr.getAttrValue(),
                                        routerStepGroupStepAttr.getLang(), now, userId));
                    }
                }
            }
        }

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return newRouterId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String routerAllUpdate(Long tenantId, MtRouterVO10 routerData, String copyFlag) {
        if (routerData == null || routerData.getRouter() == null) {
            return null;
        }
        log.info("<===========routerAllUpdate begin===========>");
        MtRouter mtRouter = routerData.getRouter();

        List<MtRouterStepVO> routerStepVOs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(routerData.getRouterSteps())) {
            routerStepVOs = routerData.getRouterSteps();
        }

        if (CollectionUtils.isEmpty(routerStepVOs)) {
            // 新增逻辑： 如果只针对router头的新增则报错，更新只允许更新个别字段
            if (StringUtils.isEmpty(mtRouter.getRouterId())) {
                throw new MtException("MT_ROUTER_0075", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0075", "ROUTER", "【API:routerAllUpdate】"));
            } else {
                MtRouter oldRouter = self().selectByPrimaryKey(mtRouter.getRouterId());
                if (StringUtils.isNotEmpty(mtRouter.getRouterType())) {
                    if (!oldRouter.getRouterType().equals(mtRouter.getRouterType())) {
                        throw new MtException("MT_ROUTER_0074", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                        "MT_ROUTER_0074", "ROUTER", "【API:routerAllUpdate】"));
                    }
                }
                if (StringUtils.isNotEmpty(mtRouter.getRelaxedFlowFlag())) {
                    if (!oldRouter.getRelaxedFlowFlag().equals(mtRouter.getRelaxedFlowFlag())) {
                        throw new MtException("MT_ROUTER_0074", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                        "MT_ROUTER_0074", "ROUTER", "【API:routerAllUpdate】"));
                    }
                }
                if (StringUtils.isNotEmpty(mtRouter.getBomId())) {
                    if (!oldRouter.getBomId().equals(mtRouter.getBomId())) {
                        throw new MtException("MT_ROUTER_0074", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                        "MT_ROUTER_0074", "ROUTER", "【API:routerAllUpdate】"));
                    }
                }
                if (StringUtils.isNotEmpty(mtRouter.getCopiedFromRouterId())) {
                    if (!oldRouter.getCopiedFromRouterId().equals(mtRouter.getCopiedFromRouterId())) {
                        throw new MtException("MT_ROUTER_0074", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                        "MT_ROUTER_0074", "ROUTER", "【API:routerAllUpdate】"));
                    }
                }
            }
        }

        // 排除processFlag=Delete
        final List<String> chkRouterStepGroupStepIds = new ArrayList<String>();
        List<String> delRouterStepGroupStepIds = new ArrayList<String>();
        MtRouterVO10 chkRouterData = new MtRouterVO10();
        chkRouterData.setRouter(routerData.getRouter());
        chkRouterData.setRouterSteps(new ArrayList<MtRouterStepVO>());

        /*
         * 是否需要判断自动升版本 1. 有processFlag 为 add 或者 delete 的数据，需要判断自动升级版本 2. processFlag 为
         * update，以下字段发生变更的时候，需要判断自动升级版本 MT_ROUTER_STEP_B entryStepFlag MT_ROUTER_DONE_STEP routerStepId
         * MT_ROUTER_RETURN_STEP routerStepId MT_ROUTER_NEXT_STEP nextStepId
         */
        List<String> deleteStepIds = new ArrayList<>();
        // 剔除delete类型的routerStep后的结果
        List<MtRouterStepVO> noDeleteRouterStepVO = new ArrayList<>();
        boolean isNeedAdjustAutoRevision = false;

        for (MtRouterStepVO routerStepVO : routerStepVOs) {
            if(Objects.nonNull(routerStepVO.getRouterStep())) {
                log.info("<===============循环routerStepVOs,routerStepVO.getRouterStep()============>:" + routerStepVO.getRouterStep());
            }else{
                log.info("<===============循环routerStepVOs,routerStepVO.getRouterStep() is null============>");
            }
            // 1. 输入参数有效性校验
            if (StringUtils.isEmpty(routerStepVO.getProcessFlag())) {
                throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0001", "ROUTER", "processFlag", "【API:routerAllUpdate】"));
            }
            if (!"add".equals(routerStepVO.getProcessFlag()) && !"update".equals(routerStepVO.getProcessFlag())
                            && !"delete".equals(routerStepVO.getProcessFlag())) {
                throw new MtException("MT_ROUTER_0026", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0026", "ROUTER", "【API:routerAllUpdate】"));
            }

            MtRouterStepGroupVO routerStepGroupVO = routerStepVO.getRouterStepGroup();
            if (routerStepGroupVO != null) {
                List<MtRouterStepGroupStep> eachRouterStepGroupSteps = routerStepGroupVO.getRouterStepGroupSteps();
                if (CollectionUtils.isNotEmpty(eachRouterStepGroupSteps)) {
                    for (MtRouterStepGroupStep eachRouterStepGroupStep : eachRouterStepGroupSteps) {
                        if (StringUtils.isEmpty(eachRouterStepGroupStep.getRouterStepId())) {
                            throw new MtException("MT_ROUTER_0001",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001",
                                                            "ROUTER", "routerStepGroupStep:routerStepId",
                                                            "【API:routerAllUpdate】"));
                        }
                    }
                }
            }

            if (routerStepVO != null) {
                if ("delete".equals(routerStepVO.getProcessFlag())) {
                    List<String> ls = mtRouterStepMapper.selectStepByGroupStep(tenantId,
                                    routerStepVO.getRouterStep().getRouterStepId());
                    if (ls.size() != 0) {
                        chkRouterStepGroupStepIds.addAll(ls);
                        delRouterStepGroupStepIds.addAll(ls);
                    }

                    deleteStepIds.add(routerStepVO.getRouterStep().getRouterStepId());
                    isNeedAdjustAutoRevision = true;
                } else {
                    List<String> ls = mtRouterStepMapper.selectStepByGroupStep(tenantId,
                                    routerStepVO.getRouterStep().getRouterStepId());
                    if (ls.size() != 0) {
                        chkRouterStepGroupStepIds.addAll(ls);
                        if (chkRouterStepGroupStepIds.size() != 0) {
                            routerStepVO.getRouterStepGroup().getRouterStepGroupSteps()
                                            .forEach(c -> chkRouterStepGroupStepIds.remove(c.getRouterStepId()));
                        }
                    }
                    chkRouterData.getRouterSteps().add(routerStepVO);

                    // 记录去除掉delete类型的step数据
                    noDeleteRouterStepVO.add(routerStepVO);

                    if ("add".equalsIgnoreCase(routerStepVO.getProcessFlag())) {
                        isNeedAdjustAutoRevision = true;
                    } else if ("update".equalsIgnoreCase(routerStepVO.getProcessFlag())) {

                        // 如果之前已经判断了：需要自动升级版本，则后面的数据就无需判断了
                        if (!isNeedAdjustAutoRevision) {
                            MtRouterStep routerStep = routerStepVO.getRouterStep();
                            if (routerStep != null) {
                                String routerStepId = routerStep.getRouterStepId();

                                MtRouterStep oldRouterStep = new MtRouterStep();
                                oldRouterStep.setTenantId(tenantId);
                                oldRouterStep.setRouterStepId(routerStepId);
                                oldRouterStep = mtRouterStepMapper.selectOne(oldRouterStep);
                                if (null == oldRouterStep || !oldRouterStep.getEntryStepFlag()
                                                .equals(routerStep.getEntryStepFlag())) {
                                    // 不相等，说明数据变更，则需要判断自动升级版本
                                    isNeedAdjustAutoRevision = true;
                                    continue;
                                }

                                // MT_ROUTER_DONE_STEP 判断依据：新数据有，旧数据没有 或者
                                // 旧数据有，新数据没有，则需要判断自动升级版本
                                MtRouterDoneStep routerDoneStep = routerStepVO.getRouterDoneStep();

                                MtRouterDoneStep oldRouterDoneStep = new MtRouterDoneStep();
                                oldRouterDoneStep.setTenantId(tenantId);
                                oldRouterDoneStep.setRouterStepId(routerStepId);
                                oldRouterDoneStep = mtRouterDoneStepMapper.selectOne(oldRouterDoneStep);
                                if ((routerDoneStep != null && oldRouterDoneStep == null)
                                                || (routerDoneStep == null && oldRouterDoneStep != null)) {
                                    isNeedAdjustAutoRevision = true;
                                    continue;
                                }

                                // MT_ROUTER_RETURN_STEP 判断依据：新数据有，旧数据没有 或者
                                // 旧数据有，新数据没有，则需要判断自动升级版本
                                MtRouterReturnStep routerReturnStep = routerStepVO.getRouterReturnStep();

                                MtRouterReturnStep oldRouterReturnStep = new MtRouterReturnStep();
                                oldRouterReturnStep.setTenantId(tenantId);
                                oldRouterReturnStep.setRouterStepId(routerStepId);
                                oldRouterReturnStep = mtRouterReturnStepMapper.selectOne(oldRouterReturnStep);
                                if ((routerReturnStep != null && oldRouterReturnStep == null)
                                                || (routerReturnStep == null && oldRouterReturnStep != null)) {
                                    isNeedAdjustAutoRevision = true;
                                    continue;
                                }

                                // MT_ROUTER_NEXT_STEP
                                // 判断依据：数量上不一致或者数量上一致但是内容不一致，则需要判断自动升级版本
                                List<MtRouterNextStep> routerNextSteps = routerStepVO.getRouterNextSteps();

                                MtRouterNextStep routerNextStep = new MtRouterNextStep();
                                routerNextStep.setTenantId(tenantId);
                                routerNextStep.setRouterStepId(routerStepId);
                                List<MtRouterNextStep> oldRouterNextSteps =
                                                mtRouterNextStepMapper.select(routerNextStep);

                                if ((CollectionUtils.isEmpty(routerNextSteps)
                                                && CollectionUtils.isNotEmpty(oldRouterNextSteps))
                                                || (CollectionUtils.isNotEmpty(routerNextSteps)
                                                                && CollectionUtils.isEmpty(oldRouterNextSteps))) {
                                    // 一个为空另一个不为空，则认为不一致
                                    isNeedAdjustAutoRevision = true;
                                    continue;
                                }

                                if (CollectionUtils.isNotEmpty(routerNextSteps)
                                                && CollectionUtils.isNotEmpty(oldRouterNextSteps)) {
                                    // 都为空，则判断数量和内容上的一致性
                                    if (routerNextSteps.size() != oldRouterNextSteps.size()) {
                                        isNeedAdjustAutoRevision = true;
                                    } else {
                                        List<String> routerNextStepIds =
                                                        routerNextSteps.stream().map(MtRouterNextStep::getNextStepId)
                                                                        .collect(Collectors.toList());
                                        List<String> oldRouterNextStepIds =
                                                        oldRouterNextSteps.stream().map(MtRouterNextStep::getNextStepId)
                                                                        .collect(Collectors.toList());

                                        // 如果 nextStepId 有任一一个不一致，则认为需要判断自动升级版本
                                        if (routerNextStepIds.stream()
                                                        .anyMatch(t -> !oldRouterNextStepIds.contains(t))) {
                                            isNeedAdjustAutoRevision = true;
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        final List<MtRouterStepVO> chkRouterSteps = new ArrayList<MtRouterStepVO>();
        chkRouterData.getRouterSteps().stream().forEach(t -> {
            if (t.getRouterStep() == null) {
                chkRouterSteps.add(t);
            } else {
                if (!chkRouterStepGroupStepIds.contains(t.getRouterStep().getRouterStepId())) {

                    chkRouterSteps.add(t);
                }
            }
        });
        chkRouterData.setRouterSteps(chkRouterSteps);

        routerDataValidate(tenantId, chkRouterData);

        boolean allAddFlag = false;
        boolean isAutoRevisionFlag = false;

        List<String> sqlList = new ArrayList<String>();

        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date date = new Date(System.currentTimeMillis());

        mtRouter.setLastUpdateDate(date);
        mtRouter.setLastUpdatedBy(userId);
        mtRouter.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_cid_s")));

        String routerId = mtRouter.getRouterId();

        // Router
        if (StringUtils.isEmpty(mtRouter.getRouterId())) {
            routerId = this.customDbRepository.getNextKey("mt_router_s");
            mtRouter.setTenantId(tenantId);
            mtRouter.setRouterId(routerId);
            mtRouter.setCreatedBy(userId);
            mtRouter.setCreationDate(date);
            sqlList.addAll(customDbRepository.getInsertSql(mtRouter));
            allAddFlag = true;

            // router site的分配关系新建
            if (CollectionUtils.isNotEmpty(routerData.getRouterSiteAssigns())) {
                List<MtRouterSiteAssign> mtRouterSiteAssigns = routerData.getRouterSiteAssigns();
                for (MtRouterSiteAssign mtRouterSiteAssign : mtRouterSiteAssigns) {
                    if (StringUtils.isNotEmpty(mtRouterSiteAssign.getSiteId())) {
                        mtRouterSiteAssign.setRouterSiteAssignId(
                                        this.customDbRepository.getNextKey("mt_router_site_assign_s"));
                        mtRouterSiteAssign.setRouterId(routerId);
                        mtRouterSiteAssign.setCreatedBy(userId);
                        mtRouterSiteAssign.setCreationDate(date);
                        mtRouterSiteAssign.setLastUpdateDate(date);
                        mtRouterSiteAssign.setLastUpdatedBy(userId);
                        mtRouterSiteAssign.setObjectVersionNumber(1L);
                        mtRouterSiteAssign.setCid(Long
                                        .valueOf(this.customDbRepository.getNextKey("mt_router_site_assign_cid_s")));
                        mtRouterSiteAssign.setTenantId(tenantId);
                        sqlList.addAll(customDbRepository.getInsertSql(mtRouterSiteAssign));
                    }
                }
            }
        } else {
            /*
             * 逻辑变更 2019/04/26 新增自动升级版本的校验
             */
            if (isNeedAdjustAutoRevision) {
                String routerAutoRevision = routerAutoRevisionGet(tenantId, routerId);
                isAutoRevisionFlag = "Y".equals(routerAutoRevision);

                /*
                 * 如果是自动升级版本的，需要处理类型为 delete 的 routerStep 1. 将这部分 routerStep 存在于其他 routerStepGroupStep 中剔除。 2. 将这部分
                 * routerStep 存在于其他 nextStep 中剔除 3. 将这部分 routerStep 从数据中剔除(前面已经剔除掉了)
                 */
                if (isAutoRevisionFlag) {
                    if (CollectionUtils.isNotEmpty(deleteStepIds)) {
                        noDeleteRouterStepVO.stream().forEach(currentStep -> {
                            // 处理 routerStepGroupStep
                            if (currentStep.getRouterStepGroup() != null) {
                                List<MtRouterStepGroupStep> stepGroupSteps =
                                                currentStep.getRouterStepGroup().getRouterStepGroupSteps();
                                List<MtRouterStepGroupStep> newStepGroupSteps = new ArrayList<>();
                                if (CollectionUtils.isNotEmpty(stepGroupSteps)) {
                                    stepGroupSteps.forEach(t -> {
                                        if (!deleteStepIds.contains(t.getRouterStepId())) {
                                            newStepGroupSteps.add(t);
                                        }
                                    });
                                }

                                currentStep.getRouterStepGroup().setRouterStepGroupSteps(newStepGroupSteps);
                            }

                            // 处理 nextStep
                            List<MtRouterNextStep> nextSteps = currentStep.getRouterNextSteps();
                            List<MtRouterNextStep> newNextSteps = new ArrayList<>();
                            if (CollectionUtils.isNotEmpty(nextSteps)) {
                                nextSteps.forEach(t -> {
                                    if (!deleteStepIds.contains(t.getRouterStepId())) {
                                        newNextSteps.add(t);
                                    }
                                });
                            }

                            currentStep.setRouterNextSteps(newNextSteps);
                        });
                    }

                    // 自动升级版本的模式下，剔除delete后的所有数据均为新增
                    // 首先获取自动升级版本
                    String maxRevision = mtRouterMapper.selectMaxRevision(tenantId, mtRouter.getRouterName(),
                                    mtRouter.getRouterType());

                    String newRevision = "1";
                    try {
                        if (StringUtils.isNotEmpty(maxRevision)) {
                            // 转为数字加1
                            int maxRevisionI = Integer.parseInt(maxRevision);
                            newRevision = String.valueOf(maxRevisionI + 1);
                        }
                    } catch (Exception ex) {
                        newRevision = "1";
                    }

                    routerId = this.customDbRepository.getNextKey("mt_router_s");
                    mtRouter.setRouterId(routerId);
                    mtRouter.setCreatedBy(userId);
                    mtRouter.setCreationDate(date);
                    mtRouter.setRevision(newRevision);
                    mtRouter.setTenantId(tenantId);
                    sqlList.addAll(customDbRepository.getInsertSql(mtRouter));

                    // router site的分配关系新建
                    if (CollectionUtils.isNotEmpty(routerData.getRouterSiteAssigns())) {
                        List<MtRouterSiteAssign> mtRouterSiteAssigns = routerData.getRouterSiteAssigns();
                        for (MtRouterSiteAssign mtRouterSiteAssign : mtRouterSiteAssigns) {
                            if (StringUtils.isNotEmpty(mtRouterSiteAssign.getSiteId())) {
                                mtRouterSiteAssign.setRouterSiteAssignId(
                                                this.customDbRepository.getNextKey("mt_router_site_assign_s"));
                                mtRouterSiteAssign.setRouterId(routerId);
                                mtRouterSiteAssign.setCreatedBy(userId);
                                mtRouterSiteAssign.setCreationDate(date);
                                mtRouterSiteAssign.setLastUpdateDate(date);
                                mtRouterSiteAssign.setLastUpdatedBy(userId);
                                mtRouterSiteAssign.setObjectVersionNumber(1L);
                                mtRouterSiteAssign.setCid(Long.valueOf(
                                                this.customDbRepository.getNextKey("mt_router_site_assign_cid_s")));
                                mtRouterSiteAssign.setTenantId(tenantId);
                                sqlList.addAll(customDbRepository.getInsertSql(mtRouterSiteAssign));
                            }
                        }
                    }

                    allAddFlag = true;
                    chkRouterStepGroupStepIds.clear();
                    delRouterStepGroupStepIds.clear();
                    routerStepVOs = noDeleteRouterStepVO;
                } else {
                    mtRouter.setTenantId(tenantId);
                    // string null to ""
                    mtRouter = (MtRouter) ObjectFieldsHelper.setStringFieldsEmpty(mtRouter);
                    sqlList.addAll(customDbRepository.getFullUpdateSql(mtRouter));

                    // router site的分配关系更新
                    if (CollectionUtils.isNotEmpty(routerData.getRouterSiteAssigns())) {
                        List<MtRouterSiteAssign> mtRouterSiteAssigns = routerData.getRouterSiteAssigns();
                        for (MtRouterSiteAssign mtRouterSiteAssign : mtRouterSiteAssigns) {
                            if (StringUtils.isNotEmpty(mtRouterSiteAssign.getRouterSiteAssignId())) {
                                if (StringUtils.isNotEmpty(mtRouterSiteAssign.getRouterId())
                                                && StringUtils.isNotEmpty(mtRouterSiteAssign.getSiteId())) {
                                    mtRouterSiteAssign.setLastUpdateDate(date);
                                    mtRouterSiteAssign.setLastUpdatedBy(userId);
                                    mtRouterSiteAssign.setCid(Long.valueOf(
                                                    this.customDbRepository.getNextKey("mt_router_site_assign_cid_s")));
                                    mtRouterSiteAssign.setTenantId(tenantId);
                                    mtRouterSiteAssign = (MtRouterSiteAssign) ObjectFieldsHelper
                                                    .setStringFieldsEmpty(mtRouterSiteAssign);
                                    sqlList.addAll(customDbRepository.getFullUpdateSql(mtRouterSiteAssign));
                                }
                            } else {
                                if (StringUtils.isNotEmpty(mtRouterSiteAssign.getRouterId())
                                                && StringUtils.isNotEmpty(mtRouterSiteAssign.getSiteId())) {
                                    mtRouterSiteAssign.setRouterSiteAssignId(
                                                    this.customDbRepository.getNextKey("mt_router_site_assign_s"));
                                    mtRouterSiteAssign.setRouterId(routerId);
                                    mtRouterSiteAssign.setCreatedBy(userId);
                                    mtRouterSiteAssign.setCreationDate(date);
                                    mtRouterSiteAssign.setLastUpdateDate(date);
                                    mtRouterSiteAssign.setLastUpdatedBy(userId);
                                    mtRouterSiteAssign.setObjectVersionNumber(1L);
                                    mtRouterSiteAssign.setCid(Long.valueOf(
                                                    this.customDbRepository.getNextKey("mt_router_site_assign_cid_s")));
                                    mtRouterSiteAssign.setTenantId(tenantId);
                                    sqlList.addAll(customDbRepository.getInsertSql(mtRouterSiteAssign));
                                }
                            }
                        }
                    }

                    allAddFlag = false;
                }
            } else {
                mtRouter.setTenantId(tenantId);
                mtRouter = (MtRouter) ObjectFieldsHelper.setStringFieldsEmpty(mtRouter);
                sqlList.addAll(customDbRepository.getFullUpdateSql(mtRouter));

                // router site的分配关系更新
                if (CollectionUtils.isNotEmpty(routerData.getRouterSiteAssigns())) {
                    List<MtRouterSiteAssign> mtRouterSiteAssigns = routerData.getRouterSiteAssigns();
                    for (MtRouterSiteAssign mtRouterSiteAssign : mtRouterSiteAssigns) {
                        if (StringUtils.isNotEmpty(mtRouterSiteAssign.getRouterSiteAssignId())) {
                            if (StringUtils.isNotEmpty(mtRouterSiteAssign.getRouterId())
                                            && StringUtils.isNotEmpty(mtRouterSiteAssign.getSiteId())) {
                                mtRouterSiteAssign.setLastUpdateDate(date);
                                mtRouterSiteAssign.setLastUpdatedBy(userId);
                                mtRouterSiteAssign.setCid(Long.valueOf(
                                                this.customDbRepository.getNextKey("mt_router_site_assign_cid_s")));
                                mtRouterSiteAssign.setTenantId(tenantId);
                                mtRouterSiteAssign = (MtRouterSiteAssign) ObjectFieldsHelper
                                                .setStringFieldsEmpty(mtRouterSiteAssign);
                                sqlList.addAll(customDbRepository.getFullUpdateSql(mtRouterSiteAssign));
                            }
                        } else {
                            if (StringUtils.isNotEmpty(mtRouterSiteAssign.getRouterId())
                                            && StringUtils.isNotEmpty(mtRouterSiteAssign.getSiteId())) {
                                mtRouterSiteAssign.setRouterSiteAssignId(
                                                this.customDbRepository.getNextKey("mt_router_site_assign_s"));
                                mtRouterSiteAssign.setRouterId(routerId);
                                mtRouterSiteAssign.setCreatedBy(userId);
                                mtRouterSiteAssign.setCreationDate(date);
                                mtRouterSiteAssign.setLastUpdateDate(date);
                                mtRouterSiteAssign.setLastUpdatedBy(userId);
                                mtRouterSiteAssign.setObjectVersionNumber(1L);
                                mtRouterSiteAssign.setCid(Long.valueOf(
                                                this.customDbRepository.getNextKey("mt_router_site_assign_cid_s")));
                                mtRouterSiteAssign.setTenantId(tenantId);
                                sqlList.addAll(customDbRepository.getInsertSql(mtRouterSiteAssign));
                            }
                        }
                    }
                }

                allAddFlag = false;
            }
        }

        if (CollectionUtils.isNotEmpty(routerStepVOs)) {
            Map<String, String> routerStepMap = new HashMap<String, String>();
            List<MtRouterNextStep> mtRouterNextSteps = new ArrayList<MtRouterNextStep>();
            List<MtRouterStepGroupStep> mtRouterStepGroupSteps = new ArrayList<MtRouterStepGroupStep>();

            // if (CollectionUtils.isNotEmpty(chkRouterStepGroupStepIds)) {
            // chkRouterStepGroupStepIds.stream().forEach(t -> {
            // sqlList.add(Constant.DELETE_ROUTER_DONE_STEP.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_LINK.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_NEXT_STEP.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_OPERATION_COMPONENT.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_OPERATION_TL.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_OPERATION.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_RETURN_STEP.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_STEP_GROUP.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_STEP_GROUP_STEP.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_SUBSTEP_COMPONENT.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_SUBSTEP.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_STEP_TL.replace("?", "'" + t + "'"));
            // sqlList.add(Constant.DELETE_ROUTER_STEP.replace("?", "'" + t + "'"));
            // });
            // }

            if (CollectionUtils.isNotEmpty(delRouterStepGroupStepIds)) {
                routerStepVOs.stream().forEach(t -> {
                    if (delRouterStepGroupStepIds.contains(t.getRouterStep().getRouterStepId())) {
                        t.setProcessFlag("delete");
                    }
                });
            }

            // 效率优化：批量获取数据Id集合
            int routerStepIdCount = 0;
            int routerStepCidCount = 0;
            int routerLinkIdCount = 0;
            int routerOperationIdCount = 0;
            int routerOperationComponentIdCount = 0;
            int routerSubstepIdCount = 0;
            int routerSubstepComponentIdCount = 0;
            int routerDoneStepIdCount = 0;
            int routerReturnStepIdCount = 0;
            int routerStepGroupIdCount = 0;
            int routerStepGroupStepIdCount = 0;
            for (MtRouterStepVO routerStepVO : routerStepVOs) {
                routerStepCidCount++;
                if (allAddFlag || "add".equalsIgnoreCase(routerStepVO.getProcessFlag())) {
                    routerStepIdCount++;
                }

                if (routerStepVO.getRouterLink() != null) {
                    routerLinkIdCount++;
                }

                if (routerStepVO.getRouterOperation() != null
                                && routerStepVO.getRouterOperation().getRouterOperation() != null) {
                    routerOperationIdCount++;
                    if (CollectionUtils.isNotEmpty(routerStepVO.getRouterOperation().getRouterOperationComponents())) {
                        routerOperationComponentIdCount +=
                                        routerStepVO.getRouterOperation().getRouterOperationComponents().size();
                    }
                }

                if (CollectionUtils.isNotEmpty(routerStepVO.getRouterSubsteps())) {
                    routerSubstepIdCount += routerStepVO.getRouterSubsteps().size();
                    for (MtRouterSubstepVO routerSubstep : routerStepVO.getRouterSubsteps()) {
                        if (CollectionUtils.isNotEmpty(routerSubstep.getRouterSubstepComponents())) {
                            routerSubstepComponentIdCount += routerSubstep.getRouterSubstepComponents().size();
                        }
                    }
                }

                if (routerStepVO.getRouterDoneStep() != null) {
                    routerDoneStepIdCount++;
                }

                if (routerStepVO.getRouterReturnStep() != null) {
                    routerReturnStepIdCount++;
                }

                if (routerStepVO.getRouterStepGroup() != null
                                && routerStepVO.getRouterStepGroup().getRouterStepGroup() != null) {
                    routerStepGroupIdCount++;
                    if (CollectionUtils.isNotEmpty(routerStepVO.getRouterStepGroup().getRouterStepGroupSteps())) {
                        routerStepGroupStepIdCount +=
                                        routerStepVO.getRouterStepGroup().getRouterStepGroupSteps().size();
                    }
                }
            }

            // 批量获取 routerLinkId、cid
            List<String> routerStepIdS = this.customDbRepository.getNextKeys("mt_router_step_s", routerStepIdCount);
            List<String> routerStepCidS =
                            this.customDbRepository.getNextKeys("mt_router_step_cid_s", routerStepCidCount);

            // 批量获取 routerLinkId、cid
            List<String> routerLinkIdS = this.customDbRepository.getNextKeys("mt_router_link_s", routerLinkIdCount);
            List<String> routerLinkCidS =
                            this.customDbRepository.getNextKeys("mt_router_link_cid_s", routerLinkIdCount);

            // 批量获取 routerOperationId、cid
            List<String> routerOperationIdS =
                            this.customDbRepository.getNextKeys("mt_router_operation_s", routerOperationIdCount);
            List<String> routerOperationCidS =
                            this.customDbRepository.getNextKeys("mt_router_operation_cid_s", routerOperationIdCount);

            // 批量获取 routerOperationComponentId、cid
            List<String> routerOperationComponentIdS = this.customDbRepository
                            .getNextKeys("mt_router_operation_component_s", routerOperationComponentIdCount);
            List<String> routerOperationComponentCidS = this.customDbRepository
                            .getNextKeys("mt_router_operation_component_cid_s", routerOperationComponentIdCount);

            // 批量获取 routerSubstepId、cid
            List<String> routerSubstepIdS =
                            this.customDbRepository.getNextKeys("mt_router_substep_s", routerSubstepIdCount);
            List<String> routerSubstepCidS =
                            this.customDbRepository.getNextKeys("mt_router_substep_cid_s", routerSubstepIdCount);

            // 批量获取 routerSubstepId、cid
            List<String> routerSubstepComponentIdS = this.customDbRepository
                            .getNextKeys("mt_router_substep_component_s", routerSubstepComponentIdCount);
            List<String> routerSubstepComponentCidS = this.customDbRepository
                            .getNextKeys("mt_router_substep_component_cid_s", routerSubstepComponentIdCount);

            // 批量获取 routerDoneStepId、cid
            List<String> routerDoneStepIdS =
                            this.customDbRepository.getNextKeys("mt_router_done_step_s", routerDoneStepIdCount);
            List<String> routerDoneStepCidS =
                            this.customDbRepository.getNextKeys("mt_router_done_step_cid_s", routerDoneStepIdCount);

            // 批量获取 routerReturnStepId、cid
            List<String> routerReturnStepIdS =
                            this.customDbRepository.getNextKeys("mt_router_return_step_s", routerReturnStepIdCount);
            List<String> routerReturnStepCidS =
                            this.customDbRepository.getNextKeys("mt_router_return_step_cid_s", routerReturnStepIdCount);

            // 批量获取 routerStepGroupId、cid
            List<String> routerStepGroupIdS =
                            this.customDbRepository.getNextKeys("mt_router_step_group_s", routerStepGroupIdCount);
            List<String> routerStepGroupCidS =
                            this.customDbRepository.getNextKeys("mt_router_step_group_cid_s", routerStepGroupIdCount);

            // 批量获取 routerStepGroupStepId、cid
            List<String> routerStepGroupStepIdS = this.customDbRepository.getNextKeys("mt_router_step_group_step_s",
                            routerStepGroupStepIdCount);
            List<String> routerStepGroupStepCidS = this.customDbRepository
                            .getNextKeys("mt_router_step_group_step_cid_s", routerStepGroupStepIdCount);

            // 记录 oldId和 newId关系
            Map<String, String> routerStepIdRel = new HashMap<>();
            Map<String, String> routerOperationCompIdRel = new HashMap<>();
            Map<String, String> routerSubstepIdRel = new HashMap<>();
            Map<String, String> routerSubstepCompIdRel = new HashMap<>();
            Map<String, String> routerNextStepIdRel = new HashMap<>();
            Map<String, String> routerStepGroupStepIdRel = new HashMap<>();

            for (MtRouterStepVO routerStepVO : routerStepVOs) {
                String processFlag =
                                StringUtils.isEmpty(routerStepVO.getProcessFlag()) ? "" : routerStepVO.getProcessFlag();

                // Router Step
                MtRouterStep mtRouterStep = routerStepVO.getRouterStep();
                mtRouterStep.setLastUpdateDate(date);
                mtRouterStep.setLastUpdatedBy(userId);
                mtRouterStep.setCid(Long.valueOf(routerStepCidS.remove(0)));
                String routerStepId = null;
                String oldRouterStepId = null;

                if (allAddFlag) {
                    oldRouterStepId = mtRouterStep.getRouterStepId();
                    routerStepId = routerStepIdS.remove(0);
                    log.info("<==================序列获取routerStepIdS========>" + routerStepId);
                    routerStepMap.put(mtRouterStep.getRouterStepId(), routerStepId);
                    mtRouterStep.setRouterId(routerId);
                    mtRouterStep.setRouterStepId(routerStepId);
                    mtRouterStep.setCreatedBy(userId);
                    mtRouterStep.setCreationDate(date);
                    mtRouterStep.setObjectVersionNumber(1L);
                    mtRouterStep.setTenantId(tenantId);
                    sqlList.addAll(customDbRepository.getInsertSql(mtRouterStep));

                    if (StringUtils.isNotEmpty(oldRouterStepId) && !oldRouterStepId.contains("-")) {
                        routerStepIdRel.put(oldRouterStepId, routerStepId);
                    }
                } else {
                    if ("add".equalsIgnoreCase(processFlag)) {
                        oldRouterStepId = mtRouterStep.getRouterStepId();
                        routerStepId = routerStepIdS.remove(0);
                        log.info("<==================序列获取routerStepIdS========>" + routerStepId);
                        routerStepMap.put(mtRouterStep.getRouterStepId(), routerStepId);
                        mtRouterStep.setRouterId(routerId);
                        mtRouterStep.setRouterStepId(routerStepId);
                        mtRouterStep.setCreatedBy(userId);
                        mtRouterStep.setCreationDate(date);
                        mtRouterStep.setObjectVersionNumber(1L);
                        mtRouterStep.setTenantId(tenantId);
                        sqlList.addAll(customDbRepository.getInsertSql(mtRouterStep));

                        if (StringUtils.isNotEmpty(oldRouterStepId) && !oldRouterStepId.contains("-")) {
                            routerStepIdRel.put(oldRouterStepId, routerStepId);
                        }
                    } else if ("update".equalsIgnoreCase(processFlag)) {
                        routerStepId = mtRouterStep.getRouterStepId();
                        log.info("<==================mtRouterStep.getRouterStepId()routerStepIdS========>" + routerStepId);
                        routerStepMap.put(mtRouterStep.getRouterStepId(), routerStepId);
                        mtRouterStep.setRouterId(routerId);
                        mtRouterStep.setTenantId(tenantId);
                        mtRouterStep = (MtRouterStep) ObjectFieldsHelper.setStringFieldsEmpty(mtRouterStep);
                        sqlList.addAll(customDbRepository.getFullUpdateSql(mtRouterStep));
                        // sqlList.add(Constant.DELETE_ROUTER_DONE_STEP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_LINK.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_NEXT_STEP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_OPERATION_COMPONENT.replace("?", "'" + routerStepId +
                        // "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_OPERATION_TL.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_OPERATION.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_RETURN_STEP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_STEP_GROUP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_STEP_GROUP_STEP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_SUBSTEP_COMPONENT.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_SUBSTEP.replace("?", "'" + routerStepId + "'"));
                    } else {
                        routerStepId = mtRouterStep.getRouterStepId();
                        log.info("<==================mtRouterStep.getRouterStepId()routerStepIdS========>" + routerStepId);
                        // sqlList.add(Constant.DELETE_ROUTER_DONE_STEP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_LINK.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_NEXT_STEP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_OPERATION_COMPONENT.replace("?", "'" + routerStepId +
                        // "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_OPERATION_TL.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_OPERATION.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_RETURN_STEP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_STEP_GROUP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_STEP_GROUP_STEP.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_SUBSTEP_COMPONENT.replace("?", "'" + routerStepId + "'"));
                        // sqlList.add(Constant.DELETE_ROUTER_SUBSTEP.replace("?", "'" + routerStepId + "'"));
                        mtRouterStep.setTenantId(tenantId);
                        sqlList.addAll(customDbRepository.getDeleteSql(mtRouterStep));
                    }
                }

                if (!allAddFlag && "delete".equalsIgnoreCase(processFlag)) {
                    continue;
                }

                final String tmpRouterStepId = routerStepId;
                // Router Link
                MtRouterLink mtRouterLink = routerStepVO.getRouterLink();
                if (mtRouterLink != null) {
                    mtRouterLink.setCreatedBy(userId);
                    mtRouterLink.setLastUpdateDate(date);
                    mtRouterLink.setCreationDate(date);
                    mtRouterLink.setLastUpdatedBy(userId);
                    mtRouterLink.setObjectVersionNumber(1L);
                    mtRouterLink.setCid(Long.valueOf(routerLinkCidS.remove(0)));

                    String routerLinkId = routerLinkIdS.remove(0);
                    mtRouterLink.setRouterLinkId(routerLinkId);
                    mtRouterLink.setRouterStepId(routerStepId);
                    mtRouterLink.setTenantId(tenantId);
                    sqlList.addAll(customDbRepository.getInsertSql(mtRouterLink));
                }

                // routerOperation
                MtRouterOperationVO routerOperationVO = routerStepVO.getRouterOperation();
                if (routerOperationVO != null) {
                    MtRouterOperation mtRouterOperation = routerOperationVO.getRouterOperation();
                    if (mtRouterOperation != null) {
                        mtRouterOperation.setCreatedBy(userId);
                        mtRouterOperation.setLastUpdateDate(date);
                        mtRouterOperation.setCreationDate(date);
                        mtRouterOperation.setLastUpdatedBy(userId);
                        mtRouterOperation.setObjectVersionNumber(1L);
                        mtRouterOperation.setCid(Long.valueOf(routerOperationCidS.remove(0)));

                        String routerOperationId = routerOperationIdS.remove(0);
                        mtRouterOperation.setRouterOperationId(routerOperationId);
                        mtRouterOperation.setRouterStepId(routerStepId);
                        mtRouterOperation.setTenantId(tenantId);

                        sqlList.addAll(customDbRepository.getInsertSql(mtRouterOperation));

                        List<MtRouterOperationComponent> routerOperationComponents =
                                        routerOperationVO.getRouterOperationComponents();
                        if (CollectionUtils.isNotEmpty(routerOperationComponents)) {
                            for (MtRouterOperationComponent t : routerOperationComponents) {
                                if (t != null) {
                                    t.setCreatedBy(userId);
                                    t.setLastUpdateDate(date);
                                    t.setCreationDate(date);
                                    t.setLastUpdatedBy(userId);
                                    t.setObjectVersionNumber(1L);
                                    t.setCid(Long.valueOf(routerOperationComponentCidS.remove(0)));

                                    String oldRouterOperationComponentId = t.getRouterOperationComponentId();
                                    String routerOperationComponentId = routerOperationComponentIdS.remove(0);
                                    t.setRouterOperationComponentId(routerOperationComponentId);
                                    t.setRouterOperationId(routerOperationId);
                                    t.setTenantId(tenantId);
                                    sqlList.addAll(customDbRepository.getInsertSql(t));

                                    // 不为空并且不是负数
                                    if (StringUtils.isNotEmpty(oldRouterOperationComponentId)
                                                    && !oldRouterOperationComponentId.contains("-")) {
                                        routerOperationCompIdRel.put(oldRouterOperationComponentId,
                                                        routerOperationComponentId);
                                    }
                                }
                            }
                        }
                    }
                }

                // Router SubStep
                List<MtRouterSubstepVO> routerSubstepVOs = routerStepVO.getRouterSubsteps();
                if (CollectionUtils.isNotEmpty(routerSubstepVOs)) {
                    for (MtRouterSubstepVO t : routerSubstepVOs) {
                        MtRouterSubstep mtRouterSubstep = t.getRouterSubstep();
                        if (mtRouterSubstep != null) {
                            mtRouterSubstep.setCreatedBy(userId);
                            mtRouterSubstep.setLastUpdateDate(date);
                            mtRouterSubstep.setCreationDate(date);
                            mtRouterSubstep.setLastUpdatedBy(userId);
                            mtRouterSubstep.setObjectVersionNumber(1L);
                            mtRouterSubstep.setCid(Long.valueOf(routerSubstepCidS.remove(0)));

                            String oldRouterSubstepId = mtRouterSubstep.getRouterSubstepId();
                            String routerSubstepId = routerSubstepIdS.remove(0);
                            mtRouterSubstep.setRouterStepId(tmpRouterStepId);
                            mtRouterSubstep.setRouterSubstepId(routerSubstepId);
                            mtRouterSubstep.setTenantId(tenantId);
                            sqlList.addAll(customDbRepository.getInsertSql(mtRouterSubstep));

                            // 不为空并且不是负数
                            if (StringUtils.isNotEmpty(oldRouterSubstepId) && !oldRouterSubstepId.contains("-")) {
                                routerSubstepIdRel.put(oldRouterSubstepId, routerSubstepId);
                            }

                            List<MtRouterSubstepComponent> mtRouterSubstepComponents = t.getRouterSubstepComponents();
                            if (CollectionUtils.isNotEmpty(mtRouterSubstepComponents)) {
                                for (MtRouterSubstepComponent c : mtRouterSubstepComponents) {
                                    if (c != null) {
                                        c.setCreatedBy(userId);
                                        c.setLastUpdateDate(date);
                                        c.setCreationDate(date);
                                        c.setLastUpdatedBy(userId);
                                        c.setObjectVersionNumber(1L);
                                        c.setCid(Long.valueOf(routerSubstepComponentCidS.remove(0)));

                                        String oldrouterSubstepComponentId = c.getRouterSubstepComponentId();
                                        String routerSubstepComponentId = routerSubstepComponentIdS.remove(0);
                                        c.setRouterSubstepComponentId(routerSubstepComponentId);
                                        c.setRouterSubstepId(routerSubstepId);
                                        c.setTenantId(tenantId);
                                        sqlList.addAll(customDbRepository.getInsertSql(c));

                                        // 不为空并且不是负数
                                        if (StringUtils.isNotEmpty(oldrouterSubstepComponentId)
                                                        && !oldrouterSubstepComponentId.contains("-")) {
                                            routerSubstepCompIdRel.put(oldrouterSubstepComponentId,
                                                            routerSubstepComponentId);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                List<MtRouterNextStep> tmpRouterNextSteps = routerStepVO.getRouterNextSteps();
                if (CollectionUtils.isNotEmpty(tmpRouterNextSteps)) {
                    tmpRouterNextSteps.stream().forEach(t -> t.setRouterStepId(tmpRouterStepId));
                    mtRouterNextSteps.addAll(tmpRouterNextSteps);
                }

                // Done Step
                MtRouterDoneStep mtRouterDoneStep = routerStepVO.getRouterDoneStep();
                if (mtRouterDoneStep != null) {
                    mtRouterDoneStep.setCreatedBy(userId);
                    mtRouterDoneStep.setLastUpdateDate(date);
                    mtRouterDoneStep.setCreationDate(date);
                    mtRouterDoneStep.setLastUpdatedBy(userId);
                    mtRouterDoneStep.setObjectVersionNumber(1L);
                    mtRouterDoneStep.setCid(Long.valueOf(routerDoneStepCidS.remove(0)));

                    String routerDoneStepId = routerDoneStepIdS.remove(0);
                    mtRouterDoneStep.setRouterDoneStepId(routerDoneStepId);
                    mtRouterDoneStep.setRouterStepId(routerStepId);
                    mtRouterDoneStep.setTenantId(tenantId);
                    sqlList.addAll(customDbRepository.getInsertSql(mtRouterDoneStep));
                }

                // Return Step
                MtRouterReturnStep mtRouterReturnStep = routerStepVO.getRouterReturnStep();
                if (mtRouterReturnStep != null) {
                    mtRouterReturnStep.setCreatedBy(userId);
                    mtRouterReturnStep.setLastUpdateDate(date);
                    mtRouterReturnStep.setCreationDate(date);
                    mtRouterReturnStep.setLastUpdatedBy(userId);
                    mtRouterReturnStep.setObjectVersionNumber(1L);
                    mtRouterReturnStep.setCid(Long.valueOf(routerReturnStepCidS.remove(0)));

                    String routerReturnStepId = routerReturnStepIdS.remove(0);
                    mtRouterReturnStep.setRouterReturnStepId(routerReturnStepId);
                    mtRouterReturnStep.setRouterStepId(routerStepId);
                    mtRouterReturnStep.setTenantId(tenantId);
                    sqlList.addAll(customDbRepository.getInsertSql(mtRouterReturnStep));
                }

                // Step Group
                MtRouterStepGroupVO routerStepGroupVO = routerStepVO.getRouterStepGroup();
                if (routerStepGroupVO != null) {
                    MtRouterStepGroup mtRouterStepGroup = routerStepGroupVO.getRouterStepGroup();
                    if (mtRouterStepGroup != null) {
                        mtRouterStepGroup.setCreatedBy(userId);
                        mtRouterStepGroup.setLastUpdateDate(date);
                        mtRouterStepGroup.setCreationDate(date);
                        mtRouterStepGroup.setLastUpdatedBy(userId);
                        mtRouterStepGroup.setObjectVersionNumber(1L);
                        mtRouterStepGroup.setCid(Long.valueOf(routerStepGroupCidS.remove(0)));

                        String routerStepGroupId = routerStepGroupIdS.remove(0);
                        mtRouterStepGroup.setRouterStepGroupId(routerStepGroupId);
                        mtRouterStepGroup.setRouterStepId(routerStepId);
                        mtRouterStepGroup.setTenantId(tenantId);
                        sqlList.addAll(customDbRepository.getInsertSql(mtRouterStepGroup));

                        List<MtRouterStepGroupStep> tmpRouterStepGroupSteps =
                                        routerStepGroupVO.getRouterStepGroupSteps();
                        if (CollectionUtils.isNotEmpty(tmpRouterStepGroupSteps)) {
                            for (MtRouterStepGroupStep routerStepGroupStep : tmpRouterStepGroupSteps) {
                                if (routerStepGroupStep != null) {
                                    routerStepGroupStep.setRouterStepGroupId(routerStepGroupId);
                                }
                            }
                            mtRouterStepGroupSteps.addAll(tmpRouterStepGroupSteps);
                        }
                    }
                }
            }

            // Next Step
            List<String> routerNextStepIdS =
                            this.customDbRepository.getNextKeys("mt_router_next_step_s", mtRouterNextSteps.size());
            List<String> routerNextStepCidS =
                            this.customDbRepository.getNextKeys("mt_router_next_step_cid_s", mtRouterNextSteps.size());
            String oldRouterNextStepId;
            String routerNextStepId;
            for (MtRouterNextStep routerNextStep : mtRouterNextSteps) {
                if (routerNextStep != null) {
                    oldRouterNextStepId = routerNextStep.getRouterNextStepId();
                    routerNextStepId = routerNextStepIdS.remove(0);

                    routerNextStep.setCreatedBy(userId);
                    routerNextStep.setLastUpdateDate(date);
                    routerNextStep.setCreationDate(date);
                    routerNextStep.setLastUpdatedBy(userId);
                    routerNextStep.setObjectVersionNumber(1L);
                    routerNextStep.setCid(Long.valueOf(routerNextStepCidS.remove(0)));
                    routerNextStep.setRouterNextStepId(routerNextStepId);
                    routerNextStep.setNextStepId(routerStepMap.get(routerNextStep.getNextStepId()));
                    routerNextStep.setTenantId(tenantId);
                    sqlList.addAll(customDbRepository.getInsertSql(routerNextStep));

                    // 不为空并且不是负数
                    if (StringUtils.isNotEmpty(oldRouterNextStepId) && !oldRouterNextStepId.contains("-")) {
                        routerNextStepIdRel.put(oldRouterNextStepId, routerNextStepId);
                    }
                }
            }

            // 只有所有步骤都处理完，才能处理步骤组内步骤的关系
            for (MtRouterStepGroupStep routerStepGroupStep : mtRouterStepGroupSteps) {
                if (routerStepGroupStep != null) {
                    String oldRouterStepGroupStepId = routerStepGroupStep.getRouterStepGroupStepId();
                    String routerStepGroupStepId = routerStepGroupStepIdS.remove(0);

                    routerStepGroupStep.setTenantId(tenantId);
                    routerStepGroupStep.setCreatedBy(userId);
                    routerStepGroupStep.setLastUpdateDate(date);
                    routerStepGroupStep.setCreationDate(date);
                    routerStepGroupStep.setLastUpdatedBy(userId);
                    routerStepGroupStep.setCid(Long.valueOf(routerStepGroupStepCidS.remove(0)));
                    routerStepGroupStep.setRouterStepGroupStepId(routerStepGroupStepId);
                    routerStepGroupStep.setRouterStepId(routerStepMap.get(routerStepGroupStep.getRouterStepId()));

                    sqlList.addAll(customDbRepository.getInsertSql(routerStepGroupStep));

                    // 不为空并且不是负数
                    if (StringUtils.isNotEmpty(oldRouterStepGroupStepId) && !oldRouterStepGroupStepId.contains("-")) {
                        routerStepGroupStepIdRel.put(oldRouterStepGroupStepId, routerStepGroupStepId);
                    }
                }
            }

            // update by yc 2019.12.31
            // 增加扩展属性的更新逻辑、涉及8张表,step相关的6张
            // mt_router_step_attr、mt_router_operation_c_attr、mt_router_substep_attr
            // mt_router_substep_c_attr、mt_router_next_step_attr、mt_router_st_gr_st_attr
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            MtExtendVO1 mtExtendVO1 = null;
            // 1. mt_router_step_attr
            if (MapUtils.isNotEmpty(routerStepIdRel)) {
                if (YES.equals(copyFlag)) {
                    // 复制新的扩展属性出来
                    mtExtendVO1 = new MtExtendVO1();
                    mtExtendVO1.setTableName("mt_router_step_attr");
                    mtExtendVO1.setKeyIdList(new ArrayList<String>(routerStepIdRel.keySet()));
                    List<MtExtendAttrVO1> routerStepAttrList =
                                    mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                    if (CollectionUtils.isNotEmpty(routerStepAttrList)) {
                        // 批量获取 routerStepAttrId、cid
                        List<String> routerStepAttrIdS = this.customDbRepository.getNextKeys("mt_router_step_attr_s",
                                        routerStepAttrList.size());
                        List<String> routerStepAttrCidS = this.customDbRepository
                                        .getNextKeys("mt_router_step_attr_cid_s", routerStepAttrList.size());

                        for (MtExtendAttrVO1 routerStepAttr : routerStepAttrList) {
                            String newId = routerStepIdRel.get(routerStepAttr.getKeyId());
                            if (StringUtils.isNotEmpty(newId)) {
                                String copyAttrSql = mtExtendSettingsRepository.getCopyAttrSql("mt_router_step_attr",
                                                routerStepAttrIdS.remove(0), routerStepAttr.getAttrId(),
                                                "ROUTER_STEP_ID", newId, routerStepAttrCidS.remove(0), userId, now);
                                sqlList.add(copyAttrSql);
                            }
                        }
                    }
                } else {
                    // 更新扩展属性为新的主表ID
                    for (Map.Entry<String, String> entry : routerStepIdRel.entrySet()) {
                        String updateAttrSql = mtExtendSettingsRepository.getRepleaceMainKeyIdSql("mt_router_step_attr",
                                        "ROUTER_STEP_ID", entry.getValue(), entry.getKey());
                        sqlList.add(updateAttrSql);
                    }
                }
            }

            // 2. mt_router_operation_c_attr
            if (MapUtils.isNotEmpty(routerOperationCompIdRel)) {
                if (YES.equals(copyFlag)) {
                    mtExtendVO1 = new MtExtendVO1();
                    mtExtendVO1.setTableName("mt_router_operation_c_attr");
                    mtExtendVO1.setKeyIdList(new ArrayList<String>(routerOperationCompIdRel.keySet()));
                    List<MtExtendAttrVO1> routerOperationCompAttrList =
                                    mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                    if (CollectionUtils.isNotEmpty(routerOperationCompAttrList)) {
                        // 批量获取 routerOperationCompAttrId、cid
                        List<String> routerOperationCompAttrIdS = this.customDbRepository.getNextKeys(
                                        "mt_router_operation_c_attr_s", routerOperationCompAttrList.size());
                        List<String> routerOperationCompAttrCidS = this.customDbRepository.getNextKeys(
                                        "mt_router_operation_c_attr_cid_s", routerOperationCompAttrList.size());

                        for (MtExtendAttrVO1 routerOperationCompAttr : routerOperationCompAttrList) {
                            String newId = routerOperationCompIdRel.get(routerOperationCompAttr.getKeyId());
                            if (StringUtils.isNotEmpty(newId)) {
                                String copyAttrSql = mtExtendSettingsRepository.getCopyAttrSql(
                                                "mt_router_operation_c_attr", routerOperationCompAttrIdS.remove(0),
                                                routerOperationCompAttr.getAttrId(), "ROUTER_OPERATION_COMPONENT_ID",
                                                newId, routerOperationCompAttrCidS.remove(0), userId, now);
                                sqlList.add(copyAttrSql);
                            }
                        }
                    }
                } else {
                    // 更新扩展属性为新的主表ID
                    for (Map.Entry<String, String> entry : routerOperationCompIdRel.entrySet()) {
                        String updateAttrSql = mtExtendSettingsRepository.getRepleaceMainKeyIdSql(
                                        "mt_router_operation_c_attr", "ROUTER_OPERATION_COMPONENT_ID", entry.getValue(),
                                        entry.getKey());
                        sqlList.add(updateAttrSql);
                    }
                }
            }

            // 3. mt_router_substep_attr
            if (MapUtils.isNotEmpty(routerSubstepIdRel)) {
                if (YES.equals(copyFlag)) {
                    mtExtendVO1 = new MtExtendVO1();
                    mtExtendVO1.setTableName("mt_router_substep_attr");
                    mtExtendVO1.setKeyIdList(new ArrayList<String>(routerSubstepIdRel.keySet()));
                    List<MtExtendAttrVO1> routerSubstepAttrList =
                                    mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                    if (CollectionUtils.isNotEmpty(routerSubstepAttrList)) {
                        // 批量获取 routerSubstepAttrId、cid
                        List<String> routerSubstepAttrIdS = this.customDbRepository
                                        .getNextKeys("mt_router_substep_attr_s", routerSubstepAttrList.size());
                        List<String> routerSubstepAttrCidS = this.customDbRepository
                                        .getNextKeys("mt_router_substep_attr_cid_s", routerSubstepAttrList.size());

                        for (MtExtendAttrVO1 routerSubstepAttr : routerSubstepAttrList) {
                            String newId = routerSubstepIdRel.get(routerSubstepAttr.getKeyId());
                            if (StringUtils.isNotEmpty(newId)) {
                                String copyAttrSql = mtExtendSettingsRepository.getCopyAttrSql("mt_router_substep_attr",
                                                routerSubstepAttrIdS.remove(0), routerSubstepAttr.getAttrId(),
                                                "ROUTER_SUBSTEP_ID", newId, routerSubstepAttrCidS.remove(0), userId,
                                                now);
                                sqlList.add(copyAttrSql);
                            }
                        }
                    }
                } else {
                    // 更新扩展属性为新的主表ID
                    for (Map.Entry<String, String> entry : routerSubstepIdRel.entrySet()) {
                        String updateAttrSql =
                                        mtExtendSettingsRepository.getRepleaceMainKeyIdSql("mt_router_substep_attr",
                                                        "ROUTER_SUBSTEP_ID", entry.getValue(), entry.getKey());
                        sqlList.add(updateAttrSql);
                    }
                }
            }

            // 4. mt_router_substep_c_attr
            if (MapUtils.isNotEmpty(routerSubstepCompIdRel)) {
                if (YES.equals(copyFlag)) {
                    mtExtendVO1 = new MtExtendVO1();
                    mtExtendVO1.setTableName("mt_router_substep_c_attr");
                    mtExtendVO1.setKeyIdList(new ArrayList<String>(routerSubstepCompIdRel.keySet()));
                    List<MtExtendAttrVO1> routerSubstepCompAttrList =
                                    mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                    if (CollectionUtils.isNotEmpty(routerSubstepCompAttrList)) {
                        // 批量获取 routerSubstepAttrId、cid
                        List<String> routerSubstepCompAttrIdS = this.customDbRepository
                                        .getNextKeys("mt_router_substep_c_attr_s", routerSubstepCompAttrList.size());
                        List<String> routerSubstepCompAttrCidS = this.customDbRepository.getNextKeys(
                                        "mt_router_substep_c_attr_cid_s", routerSubstepCompAttrList.size());

                        for (MtExtendAttrVO1 routerSubstepCompAttr : routerSubstepCompAttrList) {
                            String newId = routerSubstepCompIdRel.get(routerSubstepCompAttr.getKeyId());
                            if (StringUtils.isNotEmpty(newId)) {
                                String copyAttrSql = mtExtendSettingsRepository.getCopyAttrSql(
                                                "mt_router_substep_c_attr", routerSubstepCompAttrIdS.remove(0),
                                                routerSubstepCompAttr.getAttrId(), "ROUTER_SUBSTEP_COMPONENT_ID", newId,
                                                routerSubstepCompAttrCidS.remove(0), userId, now);
                                sqlList.add(copyAttrSql);
                            }
                        }
                    }
                } else {
                    // 更新扩展属性为新的主表ID
                    for (Map.Entry<String, String> entry : routerSubstepCompIdRel.entrySet()) {
                        String updateAttrSql = mtExtendSettingsRepository.getRepleaceMainKeyIdSql(
                                        "mt_router_substep_c_attr", "ROUTER_SUBSTEP_COMPONENT_ID", entry.getValue(),
                                        entry.getKey());
                        sqlList.add(updateAttrSql);
                    }
                }
            }

            // 5. mt_router_next_step_attr
            if (MapUtils.isNotEmpty(routerNextStepIdRel)) {
                if (YES.equals(copyFlag)) {
                    mtExtendVO1 = new MtExtendVO1();
                    mtExtendVO1.setTableName("mt_router_next_step_attr");
                    mtExtendVO1.setKeyIdList(new ArrayList<String>(routerNextStepIdRel.keySet()));
                    List<MtExtendAttrVO1> routerNextStepAttrList =
                                    mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                    if (CollectionUtils.isNotEmpty(routerNextStepAttrList)) {
                        // 批量获取 routerNextStepAttrId、cid
                        List<String> routerNextStepAttrIdS = this.customDbRepository
                                        .getNextKeys("mt_router_next_step_attr_s", routerNextStepAttrList.size());
                        List<String> routerNextStepAttrCidS = this.customDbRepository
                                        .getNextKeys("mt_router_next_step_attr_cid_s", routerNextStepAttrList.size());

                        for (MtExtendAttrVO1 routerNextStepAttr : routerNextStepAttrList) {
                            String newId = routerNextStepIdRel.get(routerNextStepAttr.getKeyId());
                            if (StringUtils.isNotEmpty(newId)) {
                                String copyAttrSql = mtExtendSettingsRepository.getCopyAttrSql(
                                                "mt_router_next_step_attr", routerNextStepAttrIdS.remove(0),
                                                routerNextStepAttr.getAttrId(), "ROUTER_NEXT_STEP_ID", newId,
                                                routerNextStepAttrCidS.remove(0), userId, now);
                                sqlList.add(copyAttrSql);
                            }
                        }
                    }
                } else {
                    // 更新扩展属性为新的主表ID
                    for (Map.Entry<String, String> entry : routerNextStepIdRel.entrySet()) {
                        String updateAttrSql =
                                        mtExtendSettingsRepository.getRepleaceMainKeyIdSql("mt_router_next_step_attr",
                                                        "ROUTER_NEXT_STEP_ID", entry.getValue(), entry.getKey());
                        sqlList.add(updateAttrSql);
                    }
                }
            }

            // 6. mt_router_st_gr_st_attr
            if (MapUtils.isNotEmpty(routerStepGroupStepIdRel)) {
                if (YES.equals(copyFlag)) {
                    mtExtendVO1 = new MtExtendVO1();
                    mtExtendVO1.setTableName("mt_router_st_gr_st_attr");
                    mtExtendVO1.setKeyIdList(new ArrayList<String>(routerStepGroupStepIdRel.keySet()));
                    List<MtExtendAttrVO1> routerStepGroupStepAttrList =
                                    mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                    if (CollectionUtils.isNotEmpty(routerStepGroupStepAttrList)) {
                        // 批量获取 routerNextStepAttrId、cid
                        List<String> routerStepGroupStepAttrIdS = this.customDbRepository
                                        .getNextKeys("mt_router_st_gr_st_attr_s", routerStepGroupStepAttrList.size());
                        List<String> routerStepGroupStepAttrCidS = this.customDbRepository.getNextKeys(
                                        "mt_router_st_gr_st_attr_cid_s", routerStepGroupStepAttrList.size());

                        for (MtExtendAttrVO1 routerStepGroupStepAttr : routerStepGroupStepAttrList) {
                            String newId = routerStepGroupStepIdRel.get(routerStepGroupStepAttr.getKeyId());
                            if (StringUtils.isNotEmpty(newId)) {
                                String copyAttrSql = mtExtendSettingsRepository.getCopyAttrSql(
                                                "mt_router_st_gr_st_attr", routerStepGroupStepAttrIdS.remove(0),
                                                routerStepGroupStepAttr.getAttrId(), "ROUTER_STEP_GROUP_STEP_ID", newId,
                                                routerStepGroupStepAttrCidS.remove(0), userId, now);
                                sqlList.add(copyAttrSql);
                            }
                        }
                    }
                } else {
                    // 更新扩展属性为新的主表ID
                    for (Map.Entry<String, String> entry : routerStepGroupStepIdRel.entrySet()) {
                        String updateAttrSql =
                                        mtExtendSettingsRepository.getRepleaceMainKeyIdSql("mt_router_st_gr_st_attr",
                                                        "ROUTER_STEP_GROUP_STEP_ID", entry.getValue(), entry.getKey());
                        sqlList.add(updateAttrSql);
                    }
                }
            }
        }

        if (sqlList.size() > 0) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        // 非自动升级版本的情况，需要记录历史
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("ROUTER_UPDATE");
        String eventId = iMtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtRouterHis routerHis = new MtRouterHis();
        routerHis.setEventId(eventId);
        routerHis.setRouterId(routerId);
        iMtRouterHisRepository.routerHisCreate(tenantId, routerHis);
        log.info("<===========routerAllUpdate end===========>");
        return routerId;
    }

    @Override
    public List<MtRouterNextStepVO2> primaryRouterLimitNextStepQuery(Long tenantId, MtRouterStep dto) {
        // MtRouterNextStep t = new MtRouterNextStep();
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:primaryRouterLimitNextStepQuery】"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:primaryRouterLimitNextStepQuery】"));
        }

        List<MtRouterStepVO8> ls = primaryRouterLimitRouterStepGet(tenantId, dto);
        String descisionStepId = null;
        for (MtRouterStepVO8 l : ls) {
            if (l.getDecisionStep() != null) {
                descisionStepId = l.getDecisionStep();
                break;
            }
        }

        List<MtRouterNextStep> nrs = iMtRouterNextStepRepository.routerNextStepQuery(tenantId, descisionStepId);
        List<MtRouterNextStepVO2> nrs2 = new ArrayList<MtRouterNextStepVO2>();

        if (CollectionUtils.isNotEmpty(nrs)) {

            List<String> nextStepIds = nrs.stream().map(MtRouterNextStep::getNextStepId).collect(Collectors.toList());
            nrs.stream().forEach(c -> {
                MtRouterNextStepVO2 v1 = new MtRouterNextStepVO2();
                BeanUtils.copyProperties(c, v1);
                nrs2.add(v1);
            });

            List<MtRouterStep> steps = this.iMtRouterStepRepository.routerStepBatchGet(tenantId, nextStepIds);
            if (CollectionUtils.isNotEmpty(steps)) {
                nrs2.stream().forEach(c -> {
                    Optional<MtRouterStep> v3 = steps.stream()
                                    .filter(t1 -> t1.getRouterStepId().equals(c.getNextStepId())).findFirst();
                    v3.ifPresent(mtRouterStep -> c.setRouterStepType(mtRouterStep.getRouterStepType()));
                });
            }

        }
        return nrs2;
    }

    @Override
    public List<MtRouterStepVO8> primaryRouterLimitRouterStepGet(Long tenantId, MtRouterStep dto) {
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:primaryRouterLimitRouterStepGet】"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:primaryRouterLimitRouterStepGet】"));
        }
        // 将[I2]赋值给过程参数[P1]
        String routerId = dto.getRouterId();
        String routerStepId = dto.getRouterStepId();

        // 获取步骤当前工艺路线
        MtRouterStep mtRouterStep = iMtRouterStepRepository.routerStepGet(tenantId, dto.getRouterStepId());
        if (mtRouterStep == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:primaryRouterLimitRouterStepGet】"));

        }
        String routerId2 = mtRouterStep.getRouterId();

        // Step2 判断routerStepId是否步骤组内步骤
        MtRouterStepVO6 rv = iMtRouterStepRepository.parentStepQuery(tenantId, routerStepId);
        if (rv != null && rv.isParentExistFlag() && "GROUP".equals(rv.getParentStepType())
                        && rv.getParentSteps().size() > 0) {
            routerStepId = rv.getParentSteps().get(0).get("parentStepId");
        }

        // Step 3 依据routerId往下展（展开三层）获取嵌套工艺路线清单
        List<MtRouterStepVO8> list = getRouterIdAndRouterStePId(tenantId, routerId, 1, null);

        // 添加主工艺路线,默认层级为0
        MtRouterStepVO8 vo3 = new MtRouterStepVO8();
        vo3.setRouterId(routerId);
        vo3.setRouterStepId(dto.getRouterStepId());
        vo3.setLevel("0");
        list.add(0, vo3);

        // Step 4判断当前步骤所在工艺路线[P2]在哪个层级上
        if (list.stream().noneMatch(t -> routerId2.equals(t.getRouterId()))) {
            throw new MtException("MT_ROUTER_0029", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0029", "ROUTER", routerId, "【API:primaryRouterLimitRouterStepGet】"));

        }

        // Step 5 判断是否首道步骤，首道步骤需要向上寻找决策步骤
        String jRouterStepId = findDecisionStep(tenantId, routerStepId,
                        list.stream().map(MtRouterStepVO8::getRouterId).collect(Collectors.toList()));
        MtRouterStep r = new MtRouterStep();
        r.setTenantId(tenantId);
        r.setRouterStepId(jRouterStepId);
        String jRouterId = mtRouterStepMapper.selectOne(r).getRouterId();
        // 设置对应参数
        for (MtRouterStepVO8 t : list) {
            if (t.getRouterId().equals(routerId2)) {
                t.setCurrentLevelFlag("Y");
            } else {
                t.setCurrentLevelFlag("N");
            }
            if (jRouterId.equals(t.getRouterId())) {
                t.setDecisionRouterFlag("Y");
                t.setDecisionStep(jRouterStepId);
            } else {
                t.setDecisionRouterFlag("N");
            }
        }
        return list;
    }

    private List<MtRouterStepVO8> getRouterIdAndRouterStePId(Long tenantId, String routerId, int level,
                    String levelValue) {
        List<MtRouterStepVO8> list = this.mtRouterMapper.selectRouterStep(tenantId, routerId);
        List<MtRouterStepVO8> vo3s = new ArrayList<>();
        if (level > 3) {
            return vo3s;
        }
        for (int i = 0; i < list.size(); i++) {
            StringBuffer str;
            if (levelValue != null) {
                str = new StringBuffer(levelValue);
            } else {
                str = new StringBuffer(String.valueOf(i + 1));
            }
            if (level != 1) {
                str.append(".");
                str.append(String.valueOf(i + 1));
            }

            MtRouterStepVO8 vo3 = new MtRouterStepVO8();
            vo3.setLevel(str.toString());
            vo3.setRouterId(list.get(i).getRouterId());
            vo3.setRouterStepId(list.get(i).getRouterStepId());
            vo3s.add(vo3);
            vo3s.addAll(getRouterIdAndRouterStePId(tenantId, list.get(i).getRouterId(), level + 1, vo3.getLevel()));

        }
        return vo3s;
    }

    private String findDecisionStep(Long tenantId, String routerStepId, List<String> ls) {
        MtRouterStep tmp = new MtRouterStep();
        tmp.setTenantId(tenantId);
        tmp.setRouterStepId(routerStepId);
        tmp = mtRouterStepMapper.selectOne(tmp);

        if ("N".equals(tmp.getEntryStepFlag())) {
            return routerStepId;
        } else {

            MtRouterLink t = new MtRouterLink();
            t.setTenantId(tenantId);
            t.setRouterId(tmp.getRouterId());

            List<MtRouterLink> ts = mtRouterLinkMapper.select(t);
            for (MtRouterLink mtRouterLink : ts) {
                MtRouterStep rs = new MtRouterStep();
                rs.setTenantId(tenantId);
                rs.setRouterStepId(mtRouterLink.getRouterStepId());
                rs = mtRouterStepMapper.selectOne(rs);
                if (rs != null && ls.contains(rs.getRouterId())) {
                    if ("N".equals(rs.getEntryStepFlag())) {
                        return rs.getRouterStepId();
                    } else {
                        return findDecisionStep(tenantId, routerStepId, ls);
                    }
                }
            }
            return routerStepId;
        }
    }

    @Override
    public List<MtRouterNextStep> eoNextStepQuery(Long tenantId, MtRouterVO4 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoNextStepQuery】"));
        }

        // 2. 获取需要的全局参数
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());

        if (mtEoStepActualVO1 == null || StringUtils.isEmpty(mtEoStepActualVO1.getEoRouterActualId())
                        || StringUtils.isEmpty(mtEoStepActualVO1.getRouterId())
                        || StringUtils.isEmpty(mtEoStepActualVO1.getRouterStepId())) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", dto.getEoStepActualId(), "【API:eoNextStepQuery】"));
        }
        return eoNextStepGetRecursion(tenantId, mtEoStepActualVO1);
    }

    private List<MtRouterNextStep> eoNextStepGetRecursion(Long tenantId, MtEoStepActualVO1 dto) {

        List<MtRouterNextStep> result = new ArrayList<>();
        // 第二步.获取工艺路线类型、分支标识、末步骤标识
        MtRouterStepGroupStepVO mtRouterStepGroupStepVO =
                        mtRouterStepGroupStepRepository.stepLimitStepGroupGet(tenantId, dto.getRouterStepId());
        // 当groupRouterStepId不为空
        if (mtRouterStepGroupStepVO != null && StringUtils.isNotEmpty(mtRouterStepGroupStepVO.getGroupRouterStepId())) {
            dto.setRouterStepId(mtRouterStepGroupStepVO.getGroupRouterStepId());
            MtEoStepActualVO10 mtEoStepActualVO10 = new MtEoStepActualVO10();
            mtEoStepActualVO10.setRouterStepId(dto.getRouterStepId());
            mtEoStepActualVO10.setEoRouterActualId(dto.getEoRouterActualId());
            String flag = mtEoStepActualRepository.eoStepGroupCompletedValidate(tenantId, mtEoStepActualVO10);
            if (!"Y".equals(flag)) {
                throw new MtException("MT_MOVING_0045",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_MOVING_0045", "MOVING",
                                                "routerStepId:" + dto.getRouterStepId(), "【API:eoNextStepQuery】"));
            }
        }

        // 当groupRouterStepId为空
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);
        if (mtEoRouterActual == null) {
            return Collections.emptyList();
        }

        MtRouter mtRouter = routerGet(tenantId, dto.getRouterId());
        if (mtRouter == null) {
            return Collections.emptyList();
        }
        String returnStepFlag = null;
        if ("NC".equals(mtRouter.getRouterType()) || "SPECIAL".equals(mtRouter.getRouterType())) {
            returnStepFlag = mtRouterReturnStepRepository.returnStepValidate(tenantId, dto.getRouterStepId());
        } else {
            returnStepFlag = mtRouterDoneStepRepository.doneStepValidate(tenantId, dto.getRouterStepId());
        }

        // 第三步.根据类型获取下一步骤
        // 当[P4] !=“Y”，[P6] !=“Y”
        if (!"Y".equals(mtEoRouterActual.getSubRouterFlag()) && !"Y".equals(returnStepFlag)) {
            return iMtRouterNextStepRepository.routerNextStepQuery(tenantId, dto.getRouterStepId());
        } else if (!"Y".equals(mtEoRouterActual.getSubRouterFlag()) && "Y".equals(returnStepFlag)) {
            return Collections.emptyList();
        } else if ("Y".equals(mtEoRouterActual.getSubRouterFlag()) && !"Y".equals(returnStepFlag)) {

            return iMtRouterNextStepRepository.routerNextStepQuery(tenantId, dto.getRouterStepId());

        } else if ("Y".equals(mtEoRouterActual.getSubRouterFlag()) && "Y".equals(returnStepFlag)) {
            if ("NC".equals(mtRouter.getRouterType()) || "SPECIAL".equals(mtRouter.getRouterType())) {
                MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
                mtEoRouterActualVO9.setEoRouterActualId(dto.getEoRouterActualId());
                mtEoRouterActualVO9.setRouterStepId(dto.getRouterStepId());
                MtEoRouterActualVO11 mtEoRouterActualVO11 =
                                mtEoRouterActualRepository.eoReturnStepGet(tenantId, mtEoRouterActualVO9);
                if (mtEoRouterActualVO11 != null && CollectionUtils.isNotEmpty(mtEoRouterActualVO11.getReturnStep())) {
                    MtRouterNextStep mtRouterNextStep;
                    for (String t : mtEoRouterActualVO11.getReturnStep()) {
                        mtRouterNextStep = new MtRouterNextStep();
                        mtRouterNextStep.setNextStepId(t);
                        result.add(mtRouterNextStep);
                    }
                    return result;
                }
            } else {
                MtEoRouterActual mtEoRouterActual1 = mtEoRouterActualMapper.selectEoRouterActualByCondition(tenantId,
                                dto.getEoRouterActualId());
                if (mtEoRouterActual1 != null) {
                    dto.setEoRouterActualId(mtEoRouterActual1.getEoRouterActualId());
                    dto.setRouterId(mtEoRouterActual1.getRouterId());
                    dto.setRouterStepId(mtEoRouterActual1.getSourceEoStepActualId());
                    result.addAll(eoNextStepGetRecursion(tenantId, dto));
                }
            }
        }
        return result;
    }

    @Override
    public MtRouterVO10 routerAllQuery(Long tenantId, String routerId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerAllQuery】"));
        }

        MtRouterVO10 result = new MtRouterVO10();

        // 2. 获取router信息
        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterId(routerId);
        mtRouter = mtRouterMapper.selectOne(mtRouter);
        if (null == mtRouter) {
            return null;
        }
        result.setRouter(mtRouter);

        // 获取router site关系
        MtRouterSiteAssign mMtRouterSiteAssign = new MtRouterSiteAssign();
        mMtRouterSiteAssign.setTenantId(tenantId);
        mMtRouterSiteAssign.setRouterId(routerId);
        List<MtRouterSiteAssign> mtRouterSiteAssigns = this.mtRouterSiteAssignMapper.select(mMtRouterSiteAssign);
        result.setRouterSiteAssigns(mtRouterSiteAssigns);

        // 3. 获取 router 下的 MtRouterStep
        MtRouterStep routerStep = new MtRouterStep();
        routerStep.setTenantId(tenantId);
        routerStep.setRouterId(routerId);
        List<MtRouterStep> routerSteps = mtRouterStepMapper.select(routerStep);

        if (CollectionUtils.isNotEmpty(routerSteps)) {
            // 汇总 所有routerStepId
            List<String> routerStepIds =
                            routerSteps.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList());

            // 3.1. 获取 MtRouterLink
            Map<String, MtRouterLink> routerLinkMap = new HashMap<>();
            List<MtRouterLink> routerLinks = mtRouterLinkMapper.selectRouterLinkByIds(tenantId, routerStepIds);
            if (CollectionUtils.isNotEmpty(routerLinks)) {
                routerLinkMap = routerLinks.stream().collect(Collectors.toMap(MtRouterLink::getRouterStepId, m -> m));
            }

            // 3.2. 获取 MtRouterDoneStep
            Map<String, MtRouterDoneStep> routerDoneStepMap = new HashMap<>();
            List<MtRouterDoneStep> routerDoneSteps =
                            mtRouterDoneStepMapper.selectRouterDoneSteByIds(tenantId, routerStepIds);
            if (CollectionUtils.isNotEmpty(routerDoneSteps)) {
                routerDoneStepMap = routerDoneSteps.stream()
                                .collect(Collectors.toMap(MtRouterDoneStep::getRouterStepId, m -> m));
            }

            // 3.3. 获取 MtRouterReturnStep
            Map<String, MtRouterReturnStep> routerReturnStepMap = new HashMap<>();
            List<MtRouterReturnStep> routerReturnSteps =
                            mtRouterReturnStepMapper.selectRouterReturnStepByIds(tenantId, routerStepIds);
            if (CollectionUtils.isNotEmpty(routerReturnSteps)) {
                routerReturnStepMap = routerReturnSteps.stream()
                                .collect(Collectors.toMap(MtRouterReturnStep::getRouterStepId, m -> m));
            }

            // 3.5. 获取 MtRouterOperation
            Map<String, MtRouterOperation> routerOperationMap = new HashMap<>();
            List<MtRouterOperation> routerOperations =
                            mtRouterOperationMapper.selectRouterOperationByIds(tenantId, routerStepIds);

            // 3.5.1 获取 MtRouterOperationComponent
            List<MtRouterOperationComponent> operationComponents = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(routerOperations)) {
                routerOperationMap = routerOperations.stream()
                                .collect(Collectors.toMap(MtRouterOperation::getRouterStepId, m -> m));

                List<String> operationIds = routerOperations.stream().map(MtRouterOperation::getRouterOperationId)
                                .collect(Collectors.toList());
                operationComponents = mtRouterOperationComponentMapper.selectByOperationIds(tenantId, operationIds);
            }

            Map<String, List<MtRouterOperationComponent>> operationComponentMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(operationComponents)) {
                operationComponentMap = operationComponents.stream()
                                .collect(Collectors.groupingBy(MtRouterOperationComponent::getRouterOperationId));
            }

            // 3.6. 获取 MtRouterSubstep
            List<MtRouterSubstep> routerSubsteps = mtRouterSubstepMapper.selectByStepIds(tenantId, routerStepIds);
            Map<String, List<MtRouterSubstep>> routerSubStepMap =
                            routerSubsteps.stream().collect(Collectors.groupingBy(MtRouterSubstep::getRouterStepId));

            // 3.6.2. 获取 MtSubStep
            List<MtSubstep> subSteps = new ArrayList<>();

            // 3.6.1. 获取 MtRouterSubstepComponent
            List<MtRouterSubstepComponent> substepComponents = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(routerSubsteps)) {
                // MtRouterSubstepComponent
                List<String> routerSubStepIds = routerSubsteps.stream().map(MtRouterSubstep::getRouterSubstepId)
                                .collect(Collectors.toList());
                substepComponents = mtRouterSubstepComponentMapper.selectBySubStepIds(tenantId, routerSubStepIds);

                // MtSubStep
                List<String> subStepIds = routerSubsteps.stream().map(MtRouterSubstep::getSubstepId).distinct()
                                .collect(Collectors.toList());
                subSteps = mtSubstepMapper.selectSubstepByIds(tenantId, subStepIds);
            }

            Map<String, List<MtRouterSubstepComponent>> subStepComponentMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(substepComponents)) {
                subStepComponentMap = substepComponents.stream()
                                .collect(Collectors.groupingBy(MtRouterSubstepComponent::getRouterSubstepId));
            }

            Map<String, MtSubstep> subStepMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(subSteps)) {
                subStepMap = subSteps.stream().collect(Collectors.toMap(MtSubstep::getSubstepId, m -> m));
            }

            // 3.7. 获取 MtRouterNextStep
            Map<String, List<MtRouterNextStep>> routerNextStepMap = new HashMap<>();
            List<MtRouterNextStep> routerNextSteps =
                            mtRouterNextStepMapper.selectRouterNextStepByStepIds(tenantId, routerStepIds);
            if (CollectionUtils.isNotEmpty(routerNextSteps)) {
                routerNextStepMap = routerNextSteps.stream()
                                .collect(Collectors.groupingBy(MtRouterNextStep::getRouterStepId));
            }

            // 3.8. 获取 MtRouterStepGroup
            Map<String, MtRouterStepGroup> routerStepGroupMap = new HashMap<>();
            List<MtRouterStepGroup> routerStepGroups =
                            mtRouterStepGroupMapper.selectRouterStepGroupByIds(tenantId, routerStepIds);

            // 3.8.1. 获取 MtRouterStepGroupStep
            List<MtRouterStepGroupStep> routerStepGroupSteps = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(routerStepGroups)) {
                routerStepGroupMap = routerStepGroups.stream()
                                .collect(Collectors.toMap(MtRouterStepGroup::getRouterStepId, m -> m));

                List<String> stepGroupIds = routerStepGroups.stream().map(MtRouterStepGroup::getRouterStepGroupId)
                                .collect(Collectors.toList());
                routerStepGroupSteps = mtRouterStepGroupStepMapper.selectByGroupIds(tenantId, stepGroupIds);
            }

            Map<String, List<MtRouterStepGroupStep>> routerStepGroupStepMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(routerStepGroupSteps)) {
                routerStepGroupStepMap = routerStepGroupSteps.stream()
                                .collect(Collectors.groupingBy(MtRouterStepGroupStep::getRouterStepGroupId));
            }

            // 循环所有步骤，填充数据
            List<MtRouterStepVO> routerStepVOS = new ArrayList<>();
            for (MtRouterStep mtRouterStep : routerSteps) {
                MtRouterStepVO routerStepVO = new MtRouterStepVO();
                String routerStepId = mtRouterStep.getRouterStepId();

                // routerStep
                routerStepVO.setRouterStep(mtRouterStep);

                // routerLink
                MtRouterLink routerLink = routerLinkMap.get(routerStepId);
                routerStepVO.setRouterLink(routerLink);

                // routerDoneStep
                MtRouterDoneStep routerDoneStep = routerDoneStepMap.get(routerStepId);
                routerStepVO.setRouterDoneStep(routerDoneStep);

                // routerReturnStep
                MtRouterReturnStep routerReturnStep = routerReturnStepMap.get(routerStepId);
                routerStepVO.setRouterReturnStep(routerReturnStep);

                // routerStepGroup
                MtRouterStepGroupVO routerStepGroupVO = new MtRouterStepGroupVO();
                MtRouterStepGroup routerStepGroup = routerStepGroupMap.get(routerStepId);

                if (routerStepGroup != null) {
                    List<MtRouterStepGroupStep> groupSteps =
                                    routerStepGroupStepMap.get(routerStepGroup.getRouterStepGroupId());
                    routerStepGroupVO.setRouterStepGroup(routerStepGroup);
                    routerStepGroupVO.setRouterStepGroupSteps(groupSteps);
                    routerStepVO.setRouterStepGroup(routerStepGroupVO);
                }

                // MtRouterOperationVO
                MtRouterOperationVO routerOperationVO = new MtRouterOperationVO();
                MtRouterOperation routerOperation = routerOperationMap.get(routerStepId);

                if (routerOperation != null) {
                    List<MtRouterOperationComponent> operationComponentList =
                                    operationComponentMap.get(routerOperation.getRouterOperationId());
                    routerOperationVO.setRouterOperationComponents(operationComponentList);
                    routerOperationVO.setRouterOperation(routerOperation);
                    routerStepVO.setRouterOperation(routerOperationVO);
                }

                // routerSubsteps
                List<MtRouterSubstepVO> substepVOList = new ArrayList<>();
                List<MtRouterSubstep> routerSubstepList = routerSubStepMap.get(routerStepId);
                if (CollectionUtils.isNotEmpty(routerSubstepList)) {
                    for (MtRouterSubstep tempSubstep : routerSubstepList) {
                        MtRouterSubstepVO routerSubstepVO = new MtRouterSubstepVO();
                        routerSubstepVO.setRouterSubstep(tempSubstep);

                        // 设置名称和描述
                        MtSubstep substep = subStepMap.get(tempSubstep.getSubstepId());
                        if (substep != null) {
                            routerSubstepVO.setSubstepName(substep.getSubstepName());
                            routerSubstepVO.setDescription(substep.getDescription());
                        }

                        List<MtRouterSubstepComponent> substepComponentList =
                                        subStepComponentMap.get(tempSubstep.getRouterSubstepId());
                        routerSubstepVO.setRouterSubstepComponents(substepComponentList);

                        substepVOList.add(routerSubstepVO);
                    }

                    routerStepVO.setRouterSubsteps(substepVOList);
                }

                // MtRouterNextStep
                List<MtRouterNextStep> nextStepList = routerNextStepMap.get(routerStepId);

                routerStepVO.setRouterNextSteps(nextStepList);

                routerStepVOS.add(routerStepVO);
            }

            result.setRouterSteps(routerStepVOS);
        }

        return result;
    }

    @Override
    public String routerAutoRevisionGet(Long tenantId, String routerId) {
        // Step1校验参数的有效性
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerAutoRevisionGet】"));
        }

        // Step2获取Router数据
        MtRouter mtRouter = new MtRouter();
        mtRouter.setTenantId(tenantId);
        mtRouter.setRouterId(routerId);
        mtRouter = mtRouterMapper.selectOne(mtRouter);
        if (mtRouter == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:routerAutoRevisionGet】"));
        }
        if (StringUtils.isNotEmpty(mtRouter.getAutoRevisionFlag())) {
            return mtRouter.getAutoRevisionFlag();
        }

        // Step3获取系统参数ROUTER_AUTO_REVISION_FLAG中的“系统”层的值
        String systemAutoRevisionFlag =
                        profileClient.getProfileValueByOptions(tenantId, DetailsHelper.getUserDetails().getUserId(),
                                        DetailsHelper.getUserDetails().getRoleId(), "ROUTER_AUTO_REVISION_FLAG");
        if (StringUtils.isEmpty(systemAutoRevisionFlag)) {
            systemAutoRevisionFlag = "N";
        }

        return systemAutoRevisionFlag;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String sourceRouterLimitRouterAllUpdate(Long tenantId, MtRouterVO6 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getSourceRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "sourceRouterId", "【API:sourceRouterLimitRouterAllUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getTargetRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "targetRouterId", "【API:sourceRouterLimitRouterAllUpdate】"));
        }

        // 2. 获取来源工艺路线所有信息
        MtRouterVO10 sourceRouterDataVO = routerAllQuery(tenantId, dto.getSourceRouterId());
        if (sourceRouterDataVO == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:sourceRouterLimitRouterAllUpdate】"));
        }

        // 3. 获取目标工艺路线信息
        MtRouter targetRouter = routerGet(tenantId, dto.getTargetRouterId());
        if (targetRouter == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:sourceRouterLimitRouterAllUpdate】"));
        }

        MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
        mtRouterSiteAssign.setRouterId(dto.getTargetRouterId());
        mtRouterSiteAssign.setEnableFlag("Y");
        List<MtRouterSiteAssign> mtRouterSiteAssigns =
                        mtRouterSiteAssignRepository.propertyLimitRouterSiteAssignQuery(tenantId, mtRouterSiteAssign);

        // 将目标router信息覆盖来源router信息
        MtRouter sourceRouter = sourceRouterDataVO.getRouter();
        sourceRouter.setRouterId(targetRouter.getRouterId());
        sourceRouter.setRouterName(targetRouter.getRouterName());
        sourceRouter.setRouterType(targetRouter.getRouterType());
        sourceRouter.setRevision(targetRouter.getRevision());
        sourceRouterDataVO.setRouter(sourceRouter);
        sourceRouterDataVO.setRouterSiteAssigns(mtRouterSiteAssigns);

        MtRouterVO10 targetRouterDataVO = routerAllQuery(tenantId, dto.getTargetRouterId());
        if (targetRouterDataVO == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:sourceRouterLimitRouterAllUpdate】"));
        }

        // 新增所有已经更新router的来源数据
        if (CollectionUtils.isNotEmpty(sourceRouterDataVO.getRouterSteps())) {
            sourceRouterDataVO.getRouterSteps().forEach(t -> t.setProcessFlag("add"));
        } else {
            sourceRouterDataVO.setRouterSteps(new ArrayList<>());
        }

        // 删除目标router下，所有相关step信息 (每一个步骤均设置为delete), 将step放到上面的数据结构下面
        if (CollectionUtils.isNotEmpty(targetRouterDataVO.getRouterSteps())) {
            targetRouterDataVO.getRouterSteps().forEach(t -> t.setProcessFlag("delete"));
            targetRouterDataVO.getRouterSteps().addAll(sourceRouterDataVO.getRouterSteps());
        } else {
            targetRouterDataVO.setRouterSteps(sourceRouterDataVO.getRouterSteps());
        }
        return routerAllUpdate(tenantId, targetRouterDataVO, "Y");
    }

    @Override
    public String eoRelaxedFlowVerify(Long tenantId, String eoId) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoRelaxedFlowVerify】"));
        }

        // 2. 获取routerId
        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, eoId);
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "routerId", "【API:eoRelaxedFlowVerify】"));
        }

        // 获取router中的relaxedFlowFlag
        MtRouter mtRouter = this.routerGet(tenantId, routerId);
        return mtRouter == null ? "" : mtRouter.getRelaxedFlowFlag();
    }

    @Override
    public String eoPrimaryRouterValidate(Long tenantId, MtRouterVO8 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoPrimaryRouterValidate】"));
        }
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerId", "【API:eoPrimaryRouterValidate】"));
        }

        // 2. 获取主routerId
        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoId", "【API:eoPrimaryRouterValidate】"));
        }

        return dto.getRouterId().equals(routerId) ? "Y" : "N";
    }

    @Override
    public Map<String, String> eoBatchPrimaryRouterValidate(Long tenantId, List<MtRouterVO8> dtoList) {
        // 1. 校验参数有效性
        if (CollectionUtils.isEmpty(dtoList)) {
            // 传入参数${1}为空,请检查！${2}
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "dtoList", "【API:eoBatchPrimaryRouterValidate】"));
        }
        List<String> eoIdList = new ArrayList<>();
        for (MtRouterVO8 dto : dtoList) {
            if (StringUtils.isEmpty(dto.getEoId())) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "eoId", "【API:eoBatchPrimaryRouterValidate】"));
            }
            if (StringUtils.isEmpty(dto.getRouterId())) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "routerId", "【API:eoBatchPrimaryRouterValidate】"));
            }

            if (CollectionUtils.isEmpty(eoIdList)) {
                eoIdList.add(dto.getEoId());
            } else {
                if (!eoIdList.contains(dto.getEoId())) {
                    eoIdList.add(dto.getEoId());
                }
            }
        }
        // 2. 获取主routerId
        List<MtEoRouter> mtEoRouterList = mtEoRouterRepository.eoRouterBatchGet(tenantId, eoIdList);
        if (CollectionUtils.isEmpty(mtEoRouterList) || mtEoRouterList.size() != eoIdList.size()) {
            // 传入参数${1}不存在,请检查！${2}
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoId", "【API:eoBatchPrimaryRouterValidate】"));
        }
        Map<String, String> primaryRouterMap = new HashMap<>();
        List<MtRouterVO8> mtRouterVO8List = new ArrayList<>();
        String primaryRouterFlag = "N";
        for (MtEoRouter mtEoRouter : mtEoRouterList) {
            primaryRouterFlag = "N";
            mtRouterVO8List = dtoList.stream().filter(item -> item.getEoId().equals(mtEoRouter.getEoId()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(mtRouterVO8List)) {
                primaryRouterFlag = mtRouterVO8List.get(0).getRouterId().equals(mtEoRouter.getRouterId()) ? "Y" : "N";
            }
            primaryRouterMap.put(mtEoRouter.getEoId(), primaryRouterFlag);
        }
        return primaryRouterMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String routerCut(Long tenantId, MtRouterVO9 cutVO) {
        // verify before cutting
        routerCutVerify(tenantId, cutVO);

        // query source router
        MtRouterVO10 routerDataVO = routerAllQuery(tenantId, cutVO.getSourceRouterId());
        if (routerDataVO == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0005", "ROUTER", "【API:routerCut】"));
        }

        // get reserved router step
        List<String> reservedRouterStepList = new ArrayList<>();
        reservedRouterStepList.add(cutVO.getCutFromRouterStepId());
        reservedRouterStepList.addAll(
                        getReservedRouterStep(tenantId, cutVO.getCutFromRouterStepId(), cutVO.getCutToRouterStepId()));

        // construct router data vo
        routerDataVO.getRouter().setRouterId(null);
        routerDataVO.getRouter().setRouterName(cutVO.getRouterName());
        routerDataVO.getRouter().setRouterType(cutVO.getRouterType());
        routerDataVO.getRouter().setRevision(cutVO.getRevision());

        List<MtRouterSiteAssign> mtRouterSiteAssigns = new ArrayList<MtRouterSiteAssign>(cutVO.getSiteIds().size());
        cutVO.getSiteIds().stream().forEach(t -> {
            MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
            mtRouterSiteAssign.setRouterId(null);
            mtRouterSiteAssign.setSiteId(t);
            mtRouterSiteAssign.setEnableFlag("Y");
            mtRouterSiteAssigns.add(mtRouterSiteAssign);
        });

        routerDataVO.setRouterSiteAssigns(mtRouterSiteAssigns);

        for (MtRouterStepVO routerStepVO : routerDataVO.getRouterSteps()) {
            if (reservedRouterStepList.contains(routerStepVO.getRouterStep().getRouterStepId())) {
                routerStepVO.setProcessFlag("update");
            } else {
                routerStepVO.setProcessFlag("delete");
            }

            if (cutVO.getCutFromRouterStepId().equals(routerStepVO.getRouterStep().getRouterStepId())) {
                routerStepVO.getRouterStep().setEntryStepFlag("Y");
            }

            if (cutVO.getCutToRouterStepId().equals(routerStepVO.getRouterStep().getRouterStepId())) {
                if ("NC".equals(cutVO.getRouterType()) || "SPECIAL".equals(cutVO.getRouterType())) {
                    MtRouterReturnStep mtRouterReturnStep = new MtRouterReturnStep();
                    mtRouterReturnStep.setRouterStepId(cutVO.getCutToRouterStepId());
                    mtRouterReturnStep.setReturnType(cutVO.getReturnType());
                    mtRouterReturnStep.setOperationId(cutVO.getReturnOperationId());
                    mtRouterReturnStep.setStepName(cutVO.getReturnStepName());
                    routerStepVO.setRouterReturnStep(mtRouterReturnStep);
                } else {
                    MtRouterDoneStep mtRouterDoneStep = new MtRouterDoneStep();
                    routerStepVO.setRouterDoneStep(mtRouterDoneStep);
                    // if done step then clear return step
                    if (routerStepVO.getRouterReturnStep() != null) {
                        routerStepVO.setRouterReturnStep(null);
                    }
                }
            }
        }

        routerDataVO.getRouterSteps().removeIf(s -> "delete".equals(s.getProcessFlag()));

        return routerAllUpdate(tenantId, routerDataVO, "Y");
    }

    @Override
    public List<MtRouterVO12> propertyLimitRouterPropertyQuery(Long tenantId, MtRouterVO11 dto) {
        List<MtRouterVO12> voList = mtRouterMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        // 根据第一步获取到的工艺路线 bomId 列表，调用API{ bomBasicGet }获取BOM名称和BOM描述
        List<String> bomIds = voList.stream().map(MtRouterVO12::getBomId).filter(StringUtils::isNotEmpty).distinct()
                        .collect(Collectors.toList());

        Map<String, MtBomVO7> bomVO7Map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomIds)) {
            List<MtBomVO7> mtBomVO7s = mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
            // 获取装配清单描述 bomName
            if (CollectionUtils.isNotEmpty(mtBomVO7s)) {
                bomVO7Map = mtBomVO7s.stream().collect(Collectors.toMap(MtBom::getBomId, t -> t));
            }
        }


        MtBomVO7 mtBomVO7;
        for (MtRouterVO12 vo12 : voList) {
            mtBomVO7 = bomVO7Map.get(vo12.getBomId());
            vo12.setBomName(null != mtBomVO7 ? mtBomVO7.getBomName() : null);
            vo12.setBomDescription(null != mtBomVO7 ? mtBomVO7.getDescription() : null);
        }
        voList.sort(Comparator
                        .comparingDouble((MtRouterVO12 t) -> Double
                                        .valueOf(StringUtils.isEmpty(t.getRouterId()) ? "0" : t.getRouterId()))
                        .thenComparing(MtRouterVO12::getRouterType).thenComparingDouble((MtRouterVO12 t) -> Double
                                        .valueOf(StringUtils.isEmpty(t.getRevision()) ? "0" : t.getRevision())));
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void routerAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10) {
        if (StringUtils.isEmpty(mtExtendVO10.getKeyId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "keyId", "【API:routerAttrPropertyUpdate】"));
        }
        MtRouter router = new MtRouter();
        router.setTenantId(tenantId);
        router.setRouterId(mtExtendVO10.getKeyId());
        MtRouter mtRouter = mtRouterMapper.selectOne(router);
        if (null == mtRouter) {
            throw new MtException("MT_ROUTER_0071",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0071", "ROUTER",
                                            mtExtendVO10.getKeyId(), "mt_router", "【API:routerAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_router_attr", mtExtendVO10.getKeyId(),
                        mtExtendVO10.getEventId(), mtExtendVO10.getAttrs());
    }

    /**
     * 工艺路线裁剪前校验
     *
     * @author benjamin
     * @date 2019-07-03 11:37
     * @param tenantId IRequest
     * @param cutVO 工艺路线裁剪传入参数VO
     */
    private void routerCutVerify(Long tenantId, MtRouterVO9 cutVO) {
        if (StringUtils.isEmpty(cutVO.getSourceRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "sourceRouterId", "【API:routerCut】"));
        }
        if (StringUtils.isEmpty(cutVO.getRouterName())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerName", "【API:routerCut】"));
        }
        if (CollectionUtils.isEmpty(cutVO.getSiteIds())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "siteId", "【API:routerCut】"));
        }
        if (StringUtils.isEmpty(cutVO.getRouterType())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerType", "【API:routerCut】"));
        }
        if (StringUtils.isEmpty(cutVO.getRevision())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "revision", "【API:routerCut】"));
        }
        if (StringUtils.isEmpty(cutVO.getCutFromRouterStepId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "cutFromRouterStepId", "【API:routerCut】"));
        }
        if (StringUtils.isEmpty(cutVO.getCutToRouterStepId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "cutToRouterStepId", "【API:routerCut】"));
        }

        if ("NC".equals(cutVO.getRouterType()) || "SPECIAL".equals(cutVO.getRouterType())) {
            if (StringUtils.isEmpty(cutVO.getReturnType())) {
                throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0001", "ROUTER", "returnType", "【API:routerCut】"));
            }
        }

        if ("DESIGNATED_OPERATION".equals(cutVO.getReturnType())) {
            if (StringUtils.isEmpty(cutVO.getReturnOperationId())) {
                throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0001", "ROUTER", "returnOperationId", "【API:routerCut】"));
            }
        }

        MtRouterStep fromRouterStep = iMtRouterStepRepository.routerStepGet(tenantId, cutVO.getCutFromRouterStepId());
        if (fromRouterStep == null || !cutVO.getSourceRouterId().equals(fromRouterStep.getRouterId())) {
            String routerStepId = fromRouterStep == null ? null : fromRouterStep.getRouterStepId();
            String stepName = fromRouterStep == null ? null : fromRouterStep.getStepName();
            throw new MtException("MT_ROUTER_0009", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0009", "ROUTER", routerStepId, stepName, "【API:routerCut】"));
        }

        MtRouterStep toRouterStep = iMtRouterStepRepository.routerStepGet(tenantId, cutVO.getCutToRouterStepId());
        if (toRouterStep == null || !cutVO.getSourceRouterId().equals(toRouterStep.getRouterId())) {
            String routerStepId = toRouterStep == null ? null : toRouterStep.getRouterStepId();
            String stepName = toRouterStep == null ? null : toRouterStep.getStepName();
            throw new MtException("MT_ROUTER_0009", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0009", "ROUTER", routerStepId, stepName, "【API:routerCut】"));
        }
    }

    /**
     * 从起始步骤开始至终止步骤，获取应当保留的步骤清单
     *
     * @author benjamin
     * @date 2019-07-03 11:38
     * @param tenantId IRequest
     * @param cutFromRouterStepId 裁剪起始步骤
     * @param cutToRouterStepId 裁剪终止步骤
     * @return List
     */
    private List<String> getReservedRouterStep(Long tenantId, String cutFromRouterStepId, String cutToRouterStepId) {
        List<String> reservedRouterStepList = new ArrayList<>();

        List<MtRouterNextStep> cutFromRouterNextStepList =
                        iMtRouterNextStepRepository.routerNextStepQuery(tenantId, cutFromRouterStepId);
        if (CollectionUtils.isEmpty(cutFromRouterNextStepList)) {
            throw new MtException("MT_ROUTER_0008", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0008", "ROUTER", "【API:routerCut】"));
        } else if (cutFromRouterNextStepList.size() > 1) {
            throw new MtException("MT_ROUTER_0032", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0032", "ROUTER", "【API:routerCut】"));
        } else if (cutToRouterStepId.equals(cutFromRouterNextStepList.get(0).getNextStepId())) {
            reservedRouterStepList.add(cutFromRouterNextStepList.get(0).getNextStepId());
        } else {
            reservedRouterStepList.add(cutFromRouterNextStepList.get(0).getNextStepId());
            reservedRouterStepList.addAll(getReservedRouterStep(tenantId,
                            cutFromRouterNextStepList.get(0).getNextStepId(), cutToRouterStepId));
        }

        return reservedRouterStepList;
    }

    @Override
    public List<MtRouter> routerCurrentVersionQuery(Long tenantId, List<String> routerNme) {
        if (CollectionUtils.isEmpty(routerNme)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "router", "【API:routerCurrentVersionQuery】"));
        }
        return this.mtRouterMapper.selectRouterByNames(tenantId, routerNme);
    }

    @Override
    public List<MtRouterVO21> eoPrimaryRouterBatchValidate(Long tenantId, List<MtRouterVO8> dtoList) {
        final String apiName = "【API:eoPrimaryRouterBatchValidate】";
        if (CollectionUtils.isEmpty(dtoList) || dtoList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "ROUTER", "eoId", apiName));
        }
        if (dtoList.stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "ROUTER", "routerId", apiName));
        }

        List<MtEoRouter> eoRouterList = mtEoRouterRepository.eoRouterBatchGet(tenantId,
                        dtoList.stream().map(MtRouterVO8::getEoId).collect(Collectors.toList()));
        List<String> paramEoIdList = dtoList.stream().map(MtRouterVO8::getEoId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eoRouterList)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "ROUTER", "eoId:" + paramEoIdList.toString(), apiName));
        }

        List<String> eoIdList = eoRouterList.stream().map(MtEoRouter::getEoId).collect(Collectors.toList());
        List<String> errEoIdList =
                        paramEoIdList.stream().filter(t -> !eoIdList.contains(t)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(errEoIdList)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "ROUTER", "eoId:" + errEoIdList.toString(), apiName));
        }
        Map<String, String> eoRouterMap =
                        eoRouterList.stream().collect(Collectors.toMap(MtEoRouter::getEoId, MtEoRouter::getRouterId));

        List<MtRouterVO21> resultList = new ArrayList<>();
        dtoList.forEach(t -> {
            MtRouterVO21 result = new MtRouterVO21();
            result.setEoId(t.getEoId());
            result.setRouterId(t.getRouterId());
            result.setValidateResult(t.getRouterId().equals(eoRouterMap.get(t.getEoId())) ? YES : NO);
            resultList.add(result);
        });

        return resultList;
    }

}
