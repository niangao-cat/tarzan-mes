package tarzan.method.infra.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.MtRouterOperationMapper;
import tarzan.method.infra.mapper.MtRouterStepMapper;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工艺路线步骤 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
public class MtRouterStepRepositoryImpl extends BaseRepositoryImpl<MtRouterStep> implements MtRouterStepRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private MtRouterNextStepRepository mtRouterNextStepRepo;
    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepo;
    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepo;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterStepGroupStepRepository mtRouterStepGroupStepRepository;

    @Autowired
    private MtRouterLinkRepository mtRouterLinkRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtRouterStepMapper mtRouterStepMapper;

    @Autowired
    private MtRouterOperationMapper mtRouterOperationMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public MtRouterStep routerStepGet(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerStepGet】"));
        }

        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setTenantId(tenantId);
        mtRouterStep.setRouterStepId(routerStepId);
        mtRouterStep = mtRouterStepMapper.selectOne(mtRouterStep);

        return mtRouterStep;
    }

    @Override
    public List<MtRouterStep> routerStepBatchGet(Long tenantId, List<String> routerStepIds) {
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerStepBatchGet】"));
        }
        return mtRouterStepMapper.selectByCondition(Condition.builder(MtRouterStep.class).andWhere(Sqls.custom()
                .andEqualTo(MtRouterStep.FIELD_TENANT_ID, tenantId).andIn(MtRouterStep.FIELD_ROUTER_STEP_ID, routerStepIds))
                .build());
    }

    @Override
    public List<String> operationStepQuery(Long tenantId, String operationId, String routerId) {
        if (StringUtils.isEmpty(operationId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "operationId", "【API:operationStepQuery】"));
        }

        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:operationStepQuery】"));
        }

        return this.mtRouterStepMapper.selectOperationStep(tenantId, operationId, routerId);
    }

    @Override
    public MtRouterStepVO6 parentStepQuery(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:parentStepQuery】"));
        }

        MtRouterStepVO6 mtRouterStepVO = new MtRouterStepVO6();
        List<MtRouterStepVO7> parentSteps = null;
        List<Map<String, String>> maps = new ArrayList<>();

        String doneFlag = this.mtRouterDoneStepRepo.doneStepValidate(tenantId, routerStepId);
        if ("Y".equals(doneFlag)) {
            // 【第二步】判断完成步骤是否存在上层步骤
            parentSteps = this.mtRouterStepMapper.selectComParentStep(tenantId, routerStepId);
            if (CollectionUtils.isEmpty(parentSteps)) {
                mtRouterStepVO.setParentExistFlag(false);
            } else {
                mtRouterStepVO.setParentExistFlag(true);
                parentSteps.forEach(t -> {
                    Map<String, String> map = new HashMap<>(2);
                    map.put("parentStepType", t.getParentType());
                    map.put("parentStepId", t.getParentStepId());
                    maps.add(map);
                });
                mtRouterStepVO.setParentSteps(maps);
            }
        } else if ("N".equals(doneFlag)) {
            // 【第三步】判断非完成步骤是否存在上层步骤
            parentSteps = this.mtRouterStepMapper.selectNonComParentStep(tenantId, routerStepId);
            if (CollectionUtils.isEmpty(parentSteps)) {
                mtRouterStepVO.setParentExistFlag(false);
            } else {
                mtRouterStepVO.setParentExistFlag(true);
                mtRouterStepVO.setParentStepType("GROUP");
                parentSteps.forEach(t -> {
                    Map<String, String> map = new HashMap<>(2);
                    map.put("groupType", t.getParentType());
                    map.put("parentStepId", t.getParentStepId());
                    maps.add(map);
                });
                mtRouterStepVO.setParentSteps(maps);
            }
        }

        return mtRouterStepVO;
    }

    @Override
    public MtRouterStepVO10 subStepQuery(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:childStepQuery】"));
        }

        MtRouterStepVO10 childRouterStepVO = new MtRouterStepVO10();

        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setTenantId(tenantId);
        mtRouterStep.setRouterStepId(routerStepId);
        mtRouterStep = mtRouterStepMapper.selectOne(mtRouterStep);
        if (mtRouterStep == null) {
            throw new MtException("MT_ROUTER_0007", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0007", "ROUTER", "【API:childStepQuery】"));
        }

        switch (mtRouterStep.getRouterStepType()) {
            case "OPERATION":
                childRouterStepVO.setChildExistFlag(false);
                childRouterStepVO.setRouterStepType(mtRouterStep.getRouterStepType());
                break;
            case "ROUTER":
                List<String> childStepIds = this.mtRouterStepMapper.selectChildEntry(tenantId, routerStepId);
                if (CollectionUtils.isEmpty(childStepIds)) {
                    throw new MtException("MT_ROUTER_0008", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                    "MT_ROUTER_0008", "ROUTER", "【API:childStepQuery】"));
                } else {
                    childRouterStepVO.setChildExistFlag(true);
                    childRouterStepVO.setRouterStepType(mtRouterStep.getRouterStepType());
                    childRouterStepVO.setChildStepId(childStepIds);
                }
                break;
            case "GROUP":
                List<MtRouterStepVO9> childSteps = this.mtRouterStepMapper.selectChild(tenantId, routerStepId);
                if (CollectionUtils.isEmpty(childSteps)) {
                    throw new MtException("MT_ROUTER_0008", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                    "MT_ROUTER_0008", "ROUTER", "【API:childStepQuery】"));
                } else {
                    childRouterStepVO.setChildExistFlag(true);
                    childRouterStepVO.setRouterStepType(mtRouterStep.getRouterStepType());
                    List<Map<String, String>> maps = new ArrayList<>();
                    childSteps.forEach(t -> {
                        Map<String, String> map = new HashMap<>(2);
                        map.put("groupType", t.getChildType());
                        map.put("childStepId", t.getChildStepId());
                        maps.add(map);
                    });
                    childRouterStepVO.setChildSteps(maps);
                }
                break;
            default:
                break;
        }

        return childRouterStepVO;
    }

    @Override
    public String stepNameLimitRouterStepGet(Long tenantId, String routerId, String stepName) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:stepNameLimitRouterStepGet】"));
        }
        if (StringUtils.isEmpty(stepName)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "stepName", "【API:stepNameLimitRouterStepGet】"));
        }

        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setRouterId(routerId);
        mtRouterStep.setStepName(stepName);
        mtRouterStep = this.mtRouterStepMapper.selectOne(mtRouterStep);

        return mtRouterStep == null ? null : mtRouterStep.getRouterStepId();
    }

    @Override
    public String routerStepQueueDecisionTypeGet(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerStepQueueDecisionTypeGet】"));
        }

        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setTenantId(tenantId);
        mtRouterStep.setRouterStepId(routerStepId);
        mtRouterStep = mtRouterStepMapper.selectOne(mtRouterStep);

        return mtRouterStep == null ? "" : mtRouterStep.getQueueDecisionType();
    }

    @Override
    public List<MtRouterStepVO5> routerStepListQuery(Long tenantId, String routerId) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerStepListQuery】"));
        }

        return mtRouterStepMapper.selectRouterStepOp(tenantId, routerId);
    }

    @Override
    public List<String> routerEntryRouterStepGet(Long tenantId, String routerId) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerEntryRouterStepGet】"));
        }

        String profileValue = profileClient.getProfileValueByOptions(tenantId,
                DetailsHelper.getUserDetails().getUserId(),
                DetailsHelper.getUserDetails().getRoleId(), "KEY_STEP_TO_GET_NEXT_STEP");

        List<String> entryRouterStepList = new ArrayList<>();
        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setRouterId(routerId);
        mtRouterStep.setEntryStepFlag("Y");
        if (!"Y".equals(profileValue)) {
            entryRouterStepList = mtRouterStepMapper.select(mtRouterStep).stream().map(MtRouterStep::getRouterStepId)
                            .collect(Collectors.toList());
        } else {
            mtRouterStep.setKeyStepFlag("Y");
            List<MtRouterStep> routerStepList = mtRouterStepMapper.select(mtRouterStep);
            if (CollectionUtils.isEmpty(routerStepList)) {
                mtRouterStep.setKeyStepFlag(null);
                routerStepList = mtRouterStepMapper.select(mtRouterStep);
                if (CollectionUtils.isNotEmpty(routerStepList)) {
                    List<MtRouterNextStep> routerNextStepList = mtRouterNextStepRepo.routerNextStepQuery(tenantId,
                                    routerStepList.get(0).getRouterStepId());
                    entryRouterStepList = routerNextStepList.stream().map(MtRouterNextStep::getNextStepId)
                                    .collect(Collectors.toList());
                }
            } else {
                entryRouterStepList =
                                routerStepList.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList());
            }
        }

        return entryRouterStepList;
    }

    @Override
    public List<MtRouterStepVO17> routerEntryRouterStepBatchGet(Long tenantId, List<String> routerIds) {
        final String apiName = "【API:routerEntryRouterStepBatchGet】";
        if (CollectionUtils.isEmpty(routerIds)) {
            throw new MtException("MT_ROUTER_0001",
                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER", "routerId", apiName));
        }

        SecurityTokenHelper.close();
        List<MtRouterStep> routerStepList = mtRouterStepMapper.selectByCondition(Condition.builder(MtRouterStep.class)
                .select(MtRouterStep.FIELD_ROUTER_ID, MtRouterStep.FIELD_ROUTER_STEP_ID)
                .andWhere(Sqls.custom().andEqualTo(MtRouterStep.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtRouterStep.FIELD_ENTRY_STEP_FLAG, MtBaseConstants.YES)
                        //modify by penglin.sui for fang.pan 锐科未用到此字段
//                        .andEqualTo(MtRouterStep.FIELD_KEY_STEP_FLAG, MtBaseConstants.YES)
                )
                .andWhere(Sqls.custom().andIn(MtRouterStep.FIELD_ROUTER_ID, routerIds))
                .build());
        if (CollectionUtils.isEmpty(routerStepList)) {
            return Lists.newArrayList();
        }

        List<MtRouterStepVO17> resultList = new ArrayList<>(routerStepList.size());
        routerStepList.forEach(t -> {
            MtRouterStepVO17 result = new MtRouterStepVO17();
            result.setRouterId(t.getRouterId());
            result.setRouterStepId(t.getRouterStepId());
            resultList.add(result);
        });
        return resultList;
    }

    @Override
    public List<MtRouterVO> bottomStepOperationQuery(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:bottomStepOperationQuery】"));
        }

        List<MtRouterVO> result = new ArrayList<MtRouterVO>();
        MtRouterStepVO10 childRouterStepVO = subStepQuery(tenantId, routerStepId);

        if (!childRouterStepVO.isChildExistFlag()) {
            MtRouterVO routerVO = new MtRouterVO();
            routerVO.setChildStepId(routerStepId);
            MtRouterOperation mtRouterOperation = this.mtRouterOperationRepo.routerOperationGet(tenantId, routerStepId);
            if (mtRouterOperation != null) {
                routerVO.setOperationId(mtRouterOperation.getOperationId());
            }
            result.add(routerVO);
        } else {
            List<String> childStepIds = new ArrayList<String>();

            if (CollectionUtils.isNotEmpty(childRouterStepVO.getChildStepId())) {
                childStepIds.addAll(childRouterStepVO.getChildStepId());
            }

            if (CollectionUtils.isNotEmpty(childRouterStepVO.getChildSteps())) {
                childRouterStepVO.getChildSteps().forEach(t -> {
                    if (t.containsKey("childStepId")) {
                        childStepIds.add(t.get("childStepId"));
                    }
                });
            }

            if (CollectionUtils.isNotEmpty(childStepIds)) {
                for (String t : childStepIds) {
                    result.addAll(bottomStepOperationQuery(tenantId, t));
                }
            }
        }
        return result;
    }

    @Override
    public List<MtRouterStepVO1> routerComponentQtyCalculate(Long tenantId, MtRouterStepVO2 dto) {
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerComponentQtyCalculate】"));
        }

        List<MtRouterStepVO1> resultList = new ArrayList<>();
        List<MtRouterStepVO3> routerStepO3s = this.mtRouterStepMapper.routerComponentQuery(tenantId, dto);

        for (MtRouterStepVO3 routerStepO3 : routerStepO3s) {
            MtBomComponentVO8 bomComponentVO8 =
                            mtBomComponentRepository.bomComponentBasicGet(tenantId, routerStepO3.getBomComponentId());

            MtRouterStepVO1 result = new MtRouterStepVO1();
            result.setBomComponentId(bomComponentVO8.getBomComponentId());
            result.setMaterialId(bomComponentVO8.getMaterialId());
            result.setComponentQty(bomComponentVO8.getQty());
            result.setOperationId(routerStepO3.getOperationId());
            result.setRouterStepId(routerStepO3.getRouterStepId());
            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public List<MtRouterStepVO12> propertyLimitRouterStepPropertyQuery(Long tenantId, MtRouterStepVO11 dto) {
        return mtRouterStepMapper.selectCondition(tenantId, dto);
    }

    @Override
    public List<MtRouterStepVO14> eoLimitUnStartRouterStepQuery(Long tenantId, MtRouterStepVO13 dto) {
        List<MtRouterStepVO14> result = Collections.synchronizedList(new ArrayList<>());
        // 第一步，校验参数的合规性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "eoId", "【API:eoLimitUnStartRouterStepQuery】"));
        }
        if (StringUtils.isEmpty(dto.getUnStartFlag())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "unStartFlag", "【API:eoLimitUnStartRouterStepQuery】"));
        }
        // 第二步，获取eo需求数量
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (null == mtEo || null == mtEo.getQty()) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_MOVING_0015", "MOVING", "eoId", "【API:eoLimitUnStartRouterStepQuery】"));
        }
        // 第三步，获取工艺路线
        String routerGet = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
        if (StringUtils.isEmpty(routerGet)) {
            throw new MtException("MT_MOVING_0054", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_MOVING_0054", "MOVING", "【API:eoLimitUnStartRouterStepQuery】"));
        }
        // 第四步，获取工艺路线下所有步骤
        List<String> routerStepIdP5 = new ArrayList<>();
        List<String> routerStepIdP7 = new ArrayList<>();
        List<String> routerStepIdP10 = new ArrayList<>();

        // 记录步骤组和嵌套步骤的map
        Map<String, List<String>> idMap = new HashMap<>();


        List<MtRouterStepVO14> routerStepList = new ArrayList<>();
        List<MtRouterStepVO5> routerStepVO5s = mtRouterStepRepository.routerStepListQuery(tenantId, routerGet);
        // 将所有步骤按类型划分
        if (CollectionUtils.isNotEmpty(routerStepVO5s)) {

            // OPERATION step
            List<MtRouterStepVO5> operationSteps = routerStepVO5s.stream()
                    .filter(t -> "OPERATION".equals(t.getRouterStepType())).collect(Collectors.toList());
            routerStepIdP5.addAll(
                    operationSteps.stream().map(MtRouterStepVO5::getRouterStepId).collect(Collectors.toList()));
            // Group step
            List<MtRouterStepVO5> groupSteps = routerStepVO5s.stream()
                    .filter(t -> "GROUP".equals(t.getRouterStepType())).collect(Collectors.toList());
            // Router step
            List<MtRouterStepVO5> routerSteps = routerStepVO5s.stream()
                    .filter(t -> "ROUTER".equals(t.getRouterStepType())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(operationSteps)) {
                operationSteps.parallelStream().forEach(s -> {
                    MtRouterStepVO14 mtRouterStepVO14 = new MtRouterStepVO14();
                    mtRouterStepVO14.setRouterStepId(s.getRouterStepId());
                    mtRouterStepVO14.setRouterStepType(s.getRouterStepType());
                    result.add(mtRouterStepVO14);
                });
            }

            if (CollectionUtils.isNotEmpty(groupSteps)) {
                groupSteps.parallelStream().forEach(g -> {
                    MtRouterStepVO14 mtRouterStepVO14 = new MtRouterStepVO14();
                    mtRouterStepVO14.setRouterStepId(g.getRouterStepId());
                    mtRouterStepVO14.setRouterStepType(g.getRouterStepType());
                    List<MtRouterStepGroupStep> groups = mtRouterStepGroupStepRepository
                            .groupStepLimitStepQuery(tenantId, g.getRouterStepId());
                    if (CollectionUtils.isNotEmpty(groups)) {
                        List<String> list = groups.stream().map(MtRouterStepGroupStep::getRouterStepId)
                                .collect(Collectors.toList());
                        routerStepIdP7.addAll(list);
                        idMap.put(g.getRouterStepId(), list);
                        mtRouterStepVO14.setRouterGroupStepId(constructRouterStepVO(tenantId, routerStepVO5s, list,
                                routerStepIdP7, routerStepIdP10, null));
                    }
                    result.add(mtRouterStepVO14);
                });
            }

            if (CollectionUtils.isNotEmpty(routerSteps)) {
                routerSteps.parallelStream().forEach(r -> {
                    MtRouterStepVO14 mtRouterStepVO14 = new MtRouterStepVO14();
                    mtRouterStepVO14.setRouterStepId(r.getRouterStepId());
                    mtRouterStepVO14.setRouterStepType(r.getRouterStepType());
                    MtRouterLink mtRouterLink = mtRouterLinkRepository.routerLinkGet(tenantId, r.getRouterStepId());
                    if (null != mtRouterLink) {
                        List<MtRouterStepVO5> vo5s = mtRouterStepRepository.routerStepListQuery(tenantId,
                                mtRouterLink.getRouterId());
                        if (CollectionUtils.isNotEmpty(vo5s)) {
                            List<String> list = vo5s.stream().map(MtRouterStepVO5::getRouterStepId)
                                    .collect(Collectors.toList());
                            routerStepIdP10.addAll(list);
                            idMap.put(r.getRouterStepId(), list);
                            mtRouterStepVO14.setRouterLinkStepId(constructRouterStepVO(tenantId, vo5s, list,
                                    routerStepIdP7, routerStepIdP10, null));
                            routerStepList.addAll(mtRouterStepVO14.getRouterLinkStepId());
                        }
                    }
                    result.add(mtRouterStepVO14);
                });
            }
        }

        // 第五步，获取工艺路线步骤实绩
        List<String> eoStepActualIds = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getEoId());
        // 获取为空，则结束此API并输出
        if (CollectionUtils.isEmpty(eoStepActualIds)) {
            List<MtRouterStepVO14> collect = result.stream().filter(t -> routerStepIdP7.contains(t.getRouterStepId()))
                    .collect(Collectors.toList());
            result.removeAll(collect);
            return result;
        }

        // 若不为空，则限制条件在表MT_EO_STEP_ACTUAL中查询数据
        List<MtEoStepActual> mtEoStepActuals =
                mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, eoStepActualIds);

        List<String> actulStepList =
                mtEoStepActuals.stream().map(MtEoStepActual::getRouterStepId).collect(Collectors.toList());

        // 第六步，获取执行作业未经过的工艺路线列表
        // [P5]，[P7]和[P10]非交集的部分
        List<String> disList = new ArrayList<>();
        disList.addAll(routerStepIdP5);
        disList.addAll(routerStepIdP7);
        disList.addAll(routerStepIdP10);
        // 非交集部分
        disList.removeAll(actulStepList);

        // 返回的StepActualIds集合
        List<MtRouterStepVO14> resultList = new ArrayList<>();
        Map<String, Double> newRouterStepActualMap = new HashMap<>();
        List<String> addListP7 = new ArrayList<>();


        if (StringUtils.isEmpty(dto.getUnStartFlag()) || "Y".equals(dto.getUnStartFlag())) {
            resultList = constructRouterStepVO2(tenantId, routerStepVO5s, disList, addListP7, null, null);
        }


        if ("NS".equals(dto.getUnStartFlag())) {
            for (MtEoStepActual actual : mtEoStepActuals) {
                if (BigDecimal.valueOf(actual.getCompletedQty()).compareTo(BigDecimal.valueOf(mtEo.getQty())) < 0) {
                    disList.add(actual.getRouterStepId());
                    double qty = BigDecimal.valueOf(mtEo.getQty())
                            .subtract(BigDecimal.valueOf(actual.getCompletedQty())).doubleValue();
                    newRouterStepActualMap.put(actual.getRouterStepId(), qty);
                }
            }

            idMap.forEach((key, value) -> {
                List<String> newDisList = new ArrayList<>(disList);
                newDisList.retainAll(value);
                if (newDisList.size() > 0) {
                    disList.add(key);
                }
            });
            resultList = constructRouterStepVO2(tenantId, routerStepVO5s, disList, addListP7, null,
                    newRouterStepActualMap);
        }

        if ("NQ".equals(dto.getUnStartFlag())) {
            for (MtEoStepActual actual : mtEoStepActuals) {
                if (BigDecimal.valueOf(actual.getCompletedQty()).compareTo(BigDecimal.valueOf(mtEo.getQty())) < 0
                        && BigDecimal.valueOf(actual.getQueueQty()).compareTo(BigDecimal.ZERO) > 0) {
                    disList.add(actual.getRouterStepId());
                    double qty = BigDecimal.valueOf(mtEo.getQty())
                            .subtract(BigDecimal.valueOf(actual.getCompletedQty())).doubleValue();
                    newRouterStepActualMap.put(actual.getRouterStepId(), qty);
                }
            }

            idMap.forEach((key, value) -> {
                List<String> newDisList = new ArrayList<>(disList);
                newDisList.retainAll(value);
                if (newDisList.size() > 0) {
                    disList.add(key);
                }
            });
            resultList = constructRouterStepVO2(tenantId, routerStepVO5s, disList, addListP7, null,
                    newRouterStepActualMap);
        }

        if ("NW".equals(dto.getUnStartFlag())) {
            for (MtEoStepActual actual : mtEoStepActuals) {
                if (BigDecimal.valueOf(actual.getCompletedQty()).compareTo(BigDecimal.valueOf(mtEo.getQty())) < 0
                        && BigDecimal.valueOf(actual.getQueueQty()).compareTo(BigDecimal.ZERO) > 0
                        && BigDecimal.valueOf(actual.getWorkingQty()).compareTo(BigDecimal.ZERO) > 0) {
                    disList.add(actual.getRouterStepId());
                    double qty = BigDecimal.valueOf(mtEo.getQty())
                            .subtract(BigDecimal.valueOf(actual.getCompletedQty())).doubleValue();
                    newRouterStepActualMap.put(actual.getRouterStepId(), qty);
                }
            }

            idMap.forEach((key, value) -> {
                List<String> newDisList = new ArrayList<>(disList);
                newDisList.retainAll(value);
                if (newDisList.size() > 0) {
                    disList.add(key);
                }
            });
            resultList = constructRouterStepVO2(tenantId, routerStepVO5s, disList, addListP7, null,
                    newRouterStepActualMap);
        }
        List<MtRouterStepVO14> collect = resultList.stream().filter(t -> addListP7.contains(t.getRouterStepId()))
                .collect(Collectors.toList());
        resultList.removeAll(collect);
        return resultList;
    }

    /**
     * 递归构造返回步骤集结果
     *
     * @param
     * @return java.util.List<tarzan.method.domain.vo.MtRouterStepVO14>
     * @Author Xie.yiyang
     * @Date 2019/11/13 15:16
     */
    private List<MtRouterStepVO14> constructRouterStepVO(Long tenantId, List<MtRouterStepVO5> forEachList,
                                                         List<String> ids, List<String> addListP7, List<String> addListP10, Map<String, Double> qtyMap) {
        List<MtRouterStepVO14> result = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(forEachList)) {
            for (MtRouterStepVO5 stepVO5 : forEachList) {
                if (ids.contains(stepVO5.getRouterStepId())) {

                    MtRouterStepVO14 mtRouterStepVO14 = new MtRouterStepVO14();
                    mtRouterStepVO14.setRouterStepId(stepVO5.getRouterStepId());
                    mtRouterStepVO14.setRouterStepType(stepVO5.getRouterStepType());
                    if (MapUtils.isNotEmpty(qtyMap)) {
                        mtRouterStepVO14.setUnCompletedQty(qtyMap.get(stepVO5.getRouterStepId()));
                    }

                    switch (stepVO5.getRouterStepType()) {
                        case "OPERATION":
                            mtRouterStepVO14.setRouterStepId(stepVO5.getRouterStepId());
                            break;
                        case "GROUP":
                            List<MtRouterStepGroupStep> groupSteps = mtRouterStepGroupStepRepository
                                    .groupStepLimitStepQuery(tenantId, stepVO5.getRouterStepId());
                            if (CollectionUtils.isNotEmpty(groupSteps)) {
                                List<String> list = groupSteps.stream().map(MtRouterStepGroupStep::getRouterStepId)
                                        .collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(addListP7)) {
                                    addListP7.addAll(list);
                                }
                                mtRouterStepVO14.setRouterGroupStepId(constructRouterStepVO(tenantId, forEachList, list,
                                        addListP7, addListP10, qtyMap));
                            }
                            break;
                        case "ROUTER":
                            MtRouterLink mtRouterLink =
                                    mtRouterLinkRepository.routerLinkGet(tenantId, stepVO5.getRouterStepId());
                            if (null != mtRouterLink) {
                                List<MtRouterStepVO5> vo5s = mtRouterStepRepository.routerStepListQuery(tenantId,
                                        mtRouterLink.getRouterId());
                                if (CollectionUtils.isNotEmpty(vo5s)) {
                                    List<String> list = vo5s.stream().map(MtRouterStepVO5::getRouterStepId)
                                            .collect(Collectors.toList());
                                    if (CollectionUtils.isNotEmpty(addListP10)) {
                                        addListP10.addAll(list);
                                    }
                                    mtRouterStepVO14.setRouterLinkStepId(constructRouterStepVO(tenantId, vo5s, list,
                                            addListP7, addListP10, qtyMap));
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    result.add(mtRouterStepVO14);
                }
            }
        }
        return result;
    }

    private List<MtRouterStepVO14> constructRouterStepVO2(Long tenantId, List<MtRouterStepVO5> forEachList,
                                                          List<String> ids, List<String> addListP7, List<String> addListP10, Map<String, Double> qtyMap) {
        List<MtRouterStepVO14> result = new ArrayList<>();
        List<String> groupList = new ArrayList<>(ids);
        List<String> routerList = new ArrayList<>(ids);
        if (CollectionUtils.isNotEmpty(forEachList)) {
            for (MtRouterStepVO5 stepVO5 : forEachList) {
                if (ids.contains(stepVO5.getRouterStepId())) {
                    MtRouterStepVO14 mtRouterStepVO14 = new MtRouterStepVO14();
                    mtRouterStepVO14.setRouterStepId(stepVO5.getRouterStepId());
                    mtRouterStepVO14.setRouterStepType(stepVO5.getRouterStepType());
                    if (MapUtils.isNotEmpty(qtyMap)) {
                        mtRouterStepVO14.setUnCompletedQty(qtyMap.get(stepVO5.getRouterStepId()));
                    }

                    switch (stepVO5.getRouterStepType()) {
                        case "OPERATION":
                            mtRouterStepVO14.setRouterStepId(stepVO5.getRouterStepId());
                            break;
                        case "GROUP":
                            List<MtRouterStepGroupStep> groupSteps = mtRouterStepGroupStepRepository
                                    .groupStepLimitStepQuery(tenantId, stepVO5.getRouterStepId());
                            if (CollectionUtils.isNotEmpty(groupSteps)) {
                                List<String> list = groupSteps.stream().map(MtRouterStepGroupStep::getRouterStepId)
                                        .collect(Collectors.toList());
                                if (addListP7 != null) {
                                    addListP7.addAll(list);
                                }
                                groupList.retainAll(list);
                                mtRouterStepVO14.setRouterGroupStepId(constructRouterStepVO2(tenantId, forEachList,
                                        groupList, addListP7, addListP10, qtyMap));
                            }
                            break;
                        case "ROUTER":
                            MtRouterLink mtRouterLink =
                                    mtRouterLinkRepository.routerLinkGet(tenantId, stepVO5.getRouterStepId());
                            if (null != mtRouterLink) {
                                List<MtRouterStepVO5> vo5s = mtRouterStepRepository.routerStepListQuery(tenantId,
                                        mtRouterLink.getRouterId());
                                if (CollectionUtils.isNotEmpty(vo5s)) {
                                    List<String> list = vo5s.stream().map(MtRouterStepVO5::getRouterStepId)
                                            .collect(Collectors.toList());
                                    if (addListP10 != null) {
                                        addListP10.addAll(list);
                                    }
                                    routerList.retainAll(list);
                                    mtRouterStepVO14.setRouterLinkStepId(constructRouterStepVO2(tenantId, vo5s,
                                            routerList, addListP7, addListP10, qtyMap));

                                }
                            }
                            break;
                        default:
                            break;
                    }
                    result.add(mtRouterStepVO14);
                }
            }
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void routerStepAttrPropertyUpdate(Long tenantId, MtExtendVO10 vo) {

        if (StringUtils.isEmpty(vo.getKeyId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "keyId", "【API:routerStepAttrPropertyUpdate】"));
        }
        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setTenantId(tenantId);
        mtRouterStep.setRouterStepId(vo.getKeyId());
        mtRouterStep = mtRouterStepMapper.selectOne(mtRouterStep);
        if (null == mtRouterStep) {
            throw new MtException("MT_ROUTER_0071",
                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_ROUTER_0071", "ROUTER",
                            "keyId:" + vo.getKeyId(), "mt_router_step",
                            "【API:routerStepAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_router_step_attr", vo.getKeyId(), vo.getEventId(),
                vo.getAttrs());
    }

    @Override
    public List<MtRouterStep> stepNameLimitRouterStepQuery(Long tenantId, List<String> routerId) {
        if (CollectionUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerId", "【API:stepNameLimitRouterStepQuery】"));
        }
        return this.mtRouterStepMapper.selectRouterStepByRouter(tenantId, routerId);
    }

    @Override
    public List<MtRouterStepVO15> routerStepListBatchQuery(Long tenantId, List<String> routerIds) {
        if (CollectionUtils.isEmpty(routerIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerIds", "【routerStepListBatchQuery】"));
        }

        //去重
        routerIds = routerIds.stream().distinct().collect(Collectors.toList());

        SecurityTokenHelper.close();
        List<MtRouterStepVO5> stepVO5s = mtRouterStepMapper.selectStepByRouterIds(tenantId, routerIds);
        if (CollectionUtils.isEmpty(stepVO5s)) {
            return Collections.emptyList();
        }
        List<String> routerStepIds = stepVO5s.stream()
                .filter(t -> "OPERATION".equals(t.getRouterStepType()) || "".equals(t.getRouterStepType()))
                .map(MtRouterStepVO5::getRouterStepId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(routerStepIds)) {
            SecurityTokenHelper.close();
            List<MtRouterStepVO5> routerOperationBatch =
                    mtRouterOperationMapper.routerOperationBatch(tenantId, routerStepIds);

            Map<String, MtRouterStepVO5> routerStepOpMap = new HashMap<String, MtRouterStepVO5>();
            if (CollectionUtils.isNotEmpty(routerOperationBatch)) {
                routerStepOpMap = routerOperationBatch.stream()
                        .collect(Collectors.toMap(MtRouterStepVO5::getRouterStepId, t -> t));
            }

            for (MtRouterStepVO5 stepVO5 : stepVO5s) {
                if (MapUtils.isNotEmpty(routerStepOpMap)) {
                    MtRouterStepVO5 routerStepOpVO = routerStepOpMap.get(stepVO5.getRouterStepId());
                    if (routerStepOpVO != null) {
                        stepVO5.setOperationId(routerStepOpVO.getOperationId());
                        stepVO5.setOperationName(routerStepOpVO.getOperationName());
                        stepVO5.setDescription(routerStepOpVO.getDescription());
                    }
                }
            }
        }
        List<MtRouterStepVO15> resultList = Collections.synchronizedList(new ArrayList<MtRouterStepVO15>(routerIds.size()));
        Map<String, List<MtRouterStepVO5>> map = stepVO5s.stream().collect(Collectors.groupingBy(MtRouterStepVO5::getRouterId));
        for (String routerId : routerIds) {
            MtRouterStepVO15 mtRouterStepVO15 = new MtRouterStepVO15();
            mtRouterStepVO15.setRouterId(routerId);
            mtRouterStepVO15.setRouterStepList(map.get(routerId) == null ? Collections.emptyList() : map.get(routerId));
            resultList.add(mtRouterStepVO15);
        }
        return resultList;
    }

    @Override
    public List<String> operationStepBatchQuery(Long tenantId, List<String> operationIds, List<String> routerIds) {
        String apiName = "【API:operationStepBatchQuery】";

        if (CollectionUtils.isEmpty(routerIds)) {
            throw new MtException("MT_ROUTER_0001",
                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER","routerId", apiName));
        }

        if (CollectionUtils.isEmpty(operationIds)) {
            throw new MtException("MT_ROUTER_0001",
                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001","ROUTER", "operationId", apiName));
        }

        SecurityTokenHelper.close();
        List<MtRouterStep> routerSteps = mtRouterStepMapper.selectByCondition(Condition.builder(MtRouterStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterStep.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterStep.FIELD_ROUTER_ID, routerIds))

                .build());

        if (CollectionUtils.isEmpty(routerSteps)) {
            return Collections.emptyList();
        }

        List<String> routerStepIds =
                routerSteps.stream().map(MtRouterStep::getRouterStepId).distinct().collect(Collectors.toList());

        SecurityTokenHelper.close();
        List<MtRouterOperation> mtRouterOperations = mtRouterOperationMapper.selectByCondition(Condition
                .builder(MtRouterOperation.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterOperation.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterOperation.FIELD_OPERATION_ID, operationIds))
                .andWhere(Sqls.custom().andIn(MtRouterStep.FIELD_ROUTER_STEP_ID, routerStepIds)
                        )
                .build());

        return mtRouterOperations.stream().map(MtRouterOperation::getRouterStepId).collect(Collectors.toList());
    }
}
