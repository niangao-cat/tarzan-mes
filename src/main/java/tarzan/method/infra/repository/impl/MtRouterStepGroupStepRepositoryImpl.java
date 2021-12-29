package tarzan.method.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtRouterStepGroup;
import tarzan.method.domain.entity.MtRouterStepGroupStep;
import tarzan.method.domain.repository.MtRouterStepGroupStepRepository;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO1;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO2;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO3;
import tarzan.method.infra.mapper.MtRouterStepGroupMapper;
import tarzan.method.infra.mapper.MtRouterStepGroupStepMapper;

/**
 * 工艺路线步骤组行步骤 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
public class MtRouterStepGroupStepRepositoryImpl extends BaseRepositoryImpl<MtRouterStepGroupStep>
        implements MtRouterStepGroupStepRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterStepGroupStepMapper mtRouterStepGroupStepMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtRouterStepGroupMapper mtRouterStepGroupMapper;

    @Override
    public List<MtRouterStepGroupStep> groupStepLimitStepQuery(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:groupStepLimitStepQuery】"));
        }

        return this.mtRouterStepGroupStepMapper.selectRouterStepByGroup(tenantId, routerStepId);
    }

    @Override
    public MtRouterStepGroupStepVO stepLimitStepGroupGet(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerStepGroupStepGet】"));
        }

        return this.mtRouterStepGroupStepMapper.selectRouterStepGroupStep(tenantId, routerStepId);
    }

    @Override
    public List<MtRouterStepGroupStepVO> routerStepGroupStepBatchGet(Long tenantId, List<String> routerStepIds) {
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER",
                    "routerStepGroupId", "【API:routerStepGroupStepBatchGet】"));
        }

        // get step group by step id
        List<MtRouterStepGroupStep> mtRouterStepGroupSteps = mtRouterStepGroupStepMapper.selectByCondition(Condition
                .builder(MtRouterStepGroupStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterStepGroupStep.FIELD_TENANT_ID, tenantId))
                .andWhere(Sqls.custom().andIn(MtRouterStepGroupStep.FIELD_ROUTER_STEP_ID, routerStepIds))
                .build());
        if (CollectionUtils.isEmpty(mtRouterStepGroupSteps)) {
            return Collections.emptyList();
        }

        Map<String, MtRouterStepGroupStep> mtRouterStepGroupStepMap = mtRouterStepGroupSteps.stream()
                .collect(Collectors.toMap(MtRouterStepGroupStep::getRouterStepId, c -> c, (o, n) -> o));

        // get step group id
        List<String> routerStepGroupIds = mtRouterStepGroupStepMap.values().stream()
                .map(MtRouterStepGroupStep::getRouterStepGroupId).collect(Collectors.toList());

        // get step group
        List<MtRouterStepGroup> mtRouterStepGroups = mtRouterStepGroupMapper.selectByCondition(Condition
                .builder(MtRouterStepGroup.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterStepGroup.FIELD_TENANT_ID, tenantId))
                .andWhere(Sqls.custom()
                        .andIn(MtRouterStepGroup.FIELD_ROUTER_STEP_GROUP_ID, routerStepGroupIds))
                .build());
        if (CollectionUtils.isEmpty(mtRouterStepGroups)) {
            return Collections.emptyList();
        }

        Map<String, MtRouterStepGroup> mtRouterStepGroupMap = mtRouterStepGroups.stream().collect(
                Collectors.toMap(MtRouterStepGroup::getRouterStepGroupId, t -> t));

        List<MtRouterStepGroupStepVO> result = new ArrayList<>(mtRouterStepGroupStepMap.entrySet().size());
        MtRouterStepGroupStepVO mtRouterStepGroupStepVO;
        for (Map.Entry<String, MtRouterStepGroupStep> entry : mtRouterStepGroupStepMap.entrySet()) {
            // group step self step id
            MtRouterStepGroup stepGroup = mtRouterStepGroupMap.get(entry.getValue().getRouterStepGroupId());
            if (null != stepGroup) {
                mtRouterStepGroupStepVO = new MtRouterStepGroupStepVO();
                mtRouterStepGroupStepVO.setRouterStepGroupStepId(entry.getValue().getRouterStepGroupStepId());
                mtRouterStepGroupStepVO.setRouterStepGroupId(entry.getValue().getRouterStepGroupId());
                mtRouterStepGroupStepVO.setGroupRouterStepId(stepGroup.getRouterStepId());
                mtRouterStepGroupStepVO.setRouterStepGroupType(stepGroup.getRouterStepGroupType());
                mtRouterStepGroupStepVO.setRouterStepId(entry.getKey());
                result.add(mtRouterStepGroupStepVO);
            }
        }
        return result;
    }


    @Override
    public List<String> routerStepLimitGroupStepQuery(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerStepLimitGroupStepQuery】"));
        }
        MtRouterStepGroupStepVO mtRouterStepGroupStepVO = stepLimitStepGroupGet(tenantId, routerStepId);
        if (null == mtRouterStepGroupStepVO) {
            throw new MtException("MT_ROUTER_0068", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0068", "ROUTER", "routerStepId", "【API:routerStepLimitGroupStepQuery】"));
        }
        MtRouterStepGroup groupStep =
                mtRouterStepGroupMapper.selectByPrimaryKey(mtRouterStepGroupStepVO.getRouterStepGroupId());
        if (null == groupStep) {
            return Collections.emptyList();
        }
        routerStepId = groupStep.getRouterStepId();
        List<MtRouterStepGroupStep> mtRouterStepGroupSteps = null;
        if (StringUtils.isNotEmpty(routerStepId)) {
            mtRouterStepGroupSteps = groupStepLimitStepQuery(tenantId, routerStepId);
        }

        if (CollectionUtils.isEmpty(mtRouterStepGroupSteps)) {
            return Collections.emptyList();
        }
        return mtRouterStepGroupSteps.stream().map(MtRouterStepGroupStep::getRouterStepId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void routerStepGroupStepAttrPropertyUpdate(Long tenantId, MtRouterStepGroupStepVO1 vo) {
        if (StringUtils.isEmpty(vo.getKeyId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "keyId", "【API:routerStepGroupStepAttrPropertyUpdate】"));
        }

        MtRouterStepGroupStep mtRouterStepGroupStep = new MtRouterStepGroupStep();
        mtRouterStepGroupStep.setTenantId(tenantId);
        mtRouterStepGroupStep.setRouterStepGroupStepId(vo.getKeyId());
        mtRouterStepGroupStep = mtRouterStepGroupStepMapper.selectOne(mtRouterStepGroupStep);

        if (null == mtRouterStepGroupStep) {
            throw new MtException("MT_ROUTER_0071",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0071", "ROUTER",
                            "keyId:" + vo.getKeyId(), "mt_router_step_group_step",
                            "【API:routerStepGroupStepAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_router_st_gr_st_attr", vo.getKeyId(),
                vo.getEventId(), vo.getAttrs());
    }

    @Override
    public List<MtRouterStepGroupStepVO2> groupSteplimitStepBatchQuery(Long tenantId, List<String> routerStepIds) {
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerStepIds", "【API:groupSteplimitStepBatchQuery】"));
        }
        List<MtRouterStepGroup> groupList =
                mtRouterStepGroupMapper.selectByCondition(Condition.builder(MtRouterStepGroup.class)
                        .andWhere(Sqls.custom().andEqualTo(MtRouterStepGroup.FIELD_TENANT_ID, tenantId)
                                .andIn(MtRouterStepGroup.FIELD_ROUTER_STEP_ID, routerStepIds))
                        .build());
        if (CollectionUtils.isEmpty(groupList)) {
            return Collections.emptyList();
        }
        List<String> groupIds =
                groupList.stream().map(MtRouterStepGroup::getRouterStepGroupId).collect(Collectors.toList());

        List<MtRouterStepGroupStep> groupStepList = mtRouterStepGroupStepMapper.selectByCondition(Condition
                .builder(MtRouterStepGroupStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterStepGroupStep.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterStepGroupStep.FIELD_ROUTER_STEP_GROUP_ID, groupIds))
                .build());
        Map<String, List<MtRouterStepGroupStep>> groupStepMap = groupStepList.stream()
                .collect(Collectors.groupingBy(MtRouterStepGroupStep::getRouterStepGroupId));

        List<MtRouterStepGroupStepVO2> resultList =
                Collections.synchronizedList(new ArrayList<MtRouterStepGroupStepVO2>(groupList.size()));
        for (MtRouterStepGroup group : groupList) {
            MtRouterStepGroupStepVO2 vo = new MtRouterStepGroupStepVO2();
            vo.setRouterStepId(group.getRouterStepId());

            List<MtRouterStepGroupStep> mtRouterStepGroupStepList = groupStepMap.get(group.getRouterStepGroupId());
            if (CollectionUtils.isEmpty(mtRouterStepGroupStepList)) {
                vo.setGroupStepList(Collections.emptyList());
                continue;
            }
            List<MtRouterStepGroupStepVO3> stepList = Collections
                    .synchronizedList(new ArrayList<>(new ArrayList<>(mtRouterStepGroupStepList.size())));

            for (MtRouterStepGroupStep step : mtRouterStepGroupStepList) {
                MtRouterStepGroupStepVO3 mtRouterStepGroupStepVO3 = new MtRouterStepGroupStepVO3();
                mtRouterStepGroupStepVO3.setRouterStepGroupId(step.getRouterStepGroupId());
                mtRouterStepGroupStepVO3.setRouterStepGroupStepId(step.getRouterStepGroupStepId());
                mtRouterStepGroupStepVO3.setRouterStepId(step.getRouterStepId());
                mtRouterStepGroupStepVO3.setSequence(step.getSequence());
                stepList.add(mtRouterStepGroupStepVO3);
            }
            vo.setGroupStepList(stepList);
            resultList.add(vo);
        }
        return resultList;
    }
}
