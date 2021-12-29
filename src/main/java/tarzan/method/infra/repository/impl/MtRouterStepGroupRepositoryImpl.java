package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtRouterStepGroup;
import tarzan.method.domain.repository.MtRouterStepGroupRepository;
import tarzan.method.infra.mapper.MtRouterStepGroupMapper;

/**
 * 工艺路线步骤组 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
public class MtRouterStepGroupRepositoryImpl extends BaseRepositoryImpl<MtRouterStepGroup>
                implements MtRouterStepGroupRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterStepGroupMapper mtRouterStepGroupMapper;

    @Override
    public MtRouterStepGroup routerStepGroupGet(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerStepGroupGet】"));
        }

        MtRouterStepGroup mtRouterStepGroup = new MtRouterStepGroup();
        mtRouterStepGroup.setRouterStepId(routerStepId);
        mtRouterStepGroup = mtRouterStepGroupMapper.selectOne(mtRouterStepGroup);
        if (mtRouterStepGroup == null) {
            throw new MtException("MT_ROUTER_0003", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0003", "ROUTER", "【API:routerStepGroupGet】"));
        }

        return mtRouterStepGroup;
    }

    @Override
    public List<MtRouterStepGroup> routerStepGroupBatchGet(Long tenantId, List<String> routerStepIds) {
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerStepGroupBatchGet】"));
        }
        return mtRouterStepGroupMapper.selectRouterStepGroupByIds(tenantId, routerStepIds);
    }
}
