package tarzan.method.infra.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtRouterDoneStep;
import tarzan.method.domain.repository.MtRouterDoneStepRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.method.domain.vo.MtRouterDoneStepVO;
import tarzan.method.domain.vo.MtRouterStepVO5;
import tarzan.method.infra.mapper.MtRouterDoneStepMapper;
import tarzan.order.domain.repository.MtEoRouterRepository;

/**
 * 完成步骤 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
public class MtRouterDoneStepRepositoryImpl extends BaseRepositoryImpl<MtRouterDoneStep>
                implements MtRouterDoneStepRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtRouterDoneStepMapper mtRouterDoneStepMapper;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Override
    public MtRouterDoneStep routerDoneStepGet(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerDoneStepGet】"));
        }

        MtRouterDoneStep mtRouterDoneStep = new MtRouterDoneStep();
        mtRouterDoneStep.setTenantId(tenantId);
        mtRouterDoneStep.setRouterStepId(routerStepId);
        return this.mtRouterDoneStepMapper.selectOne(mtRouterDoneStep);
    }

    @Override
    public String doneStepValidate(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:doneStepValidate】"));
        }

        MtRouterDoneStep mtRouterDoneStep = new MtRouterDoneStep();
        mtRouterDoneStep.setTenantId(tenantId);
        mtRouterDoneStep.setRouterStepId(routerStepId);
        mtRouterDoneStep = this.mtRouterDoneStepMapper.selectOne(mtRouterDoneStep);
        if (mtRouterDoneStep == null) {
            return "N";
        }
        return "Y";
    }

    @Override
    public List<MtRouterDoneStep> routerDoneStepBatchGet(Long tenantId, List<String> routerStepIds) {
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerDoneStepBatchGet】"));
        }

        return this.mtRouterDoneStepMapper.selectRouterDoneSteByIds(tenantId, routerStepIds);
    }

    /**
     * routerLimitDoneStepQuery-获取工艺路线完成步骤
     *
     * @author chuang.yang
     * @date 2019/9/25
     * @param tenantId
     * @param routerId
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> routerLimitDoneStepQuery(Long tenantId, String routerId) {
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerLimitDoneStepQuery】"));
        }

        List<MtRouterStepVO5> mtRouterStepVo5List = mtRouterStepRepository.routerStepListQuery(tenantId, routerId);
        if (CollectionUtils.isEmpty(mtRouterStepVo5List)) {
            return Collections.emptyList();
        }

        List<String> routerStepIds =
                mtRouterStepVo5List.stream().map(MtRouterStepVO5::getRouterStepId).collect(Collectors.toList());

        List<MtRouterDoneStep> mtRouterDoneSteps = this.routerDoneStepBatchGet(tenantId, routerStepIds);
        if (CollectionUtils.isEmpty(mtRouterDoneSteps)) {
            return Collections.emptyList();
        }

        return mtRouterDoneSteps.stream().map(MtRouterDoneStep::getRouterStepId).collect(Collectors.toList());
    }

    /**
     * eoLimitDoneStepQuery-根据执行作业获取工艺路线完成步骤
     *
     * @author chuang.yang
     * @date 2019/9/25
     * @param tenantId
     * @param eoId
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> eoLimitDoneStepQuery(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "eoId", "【API:routerLimitDoneStepQuery】"));
        }

        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, eoId);
        if (StringUtils.isEmpty(routerId)) {
            return Collections.emptyList();
        }

        return this.routerLimitDoneStepQuery(tenantId, routerId);
    }

    @Override
    public List<MtRouterDoneStepVO> doneStepBatchValidate(Long tenantId, List<String> routerStepIds) {
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER",
                    "routerStepId", "【API:doneStepValidate】"));
        }

        Set<String> doneStepIdSet = mtRouterDoneStepMapper.selectByCondition(Condition.builder(MtRouterDoneStep.class)
                .andWhere(Sqls.custom().andEqualTo(MtRouterDoneStep.FIELD_TENANT_ID, tenantId)
                        .andIn(MtRouterDoneStep.FIELD_ROUTER_STEP_ID, routerStepIds))
                .build()).stream().map(MtRouterDoneStep::getRouterStepId).collect(Collectors.toSet());

        return routerStepIds.stream().map(t -> new MtRouterDoneStepVO(t, doneStepIdSet.contains(t) ? "Y" : "N"))
                .collect(Collectors.toList());

    }
}
