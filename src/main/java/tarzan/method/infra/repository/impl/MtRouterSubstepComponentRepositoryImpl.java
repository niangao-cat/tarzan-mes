package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtRouterSubstepComponent;
import tarzan.method.domain.repository.MtRouterSubstepComponentRepository;
import tarzan.method.infra.mapper.MtRouterSubstepComponentMapper;

/**
 * 子步骤组件 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
public class MtRouterSubstepComponentRepositoryImpl extends BaseRepositoryImpl<MtRouterSubstepComponent>
                implements MtRouterSubstepComponentRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterSubstepComponentMapper mtRouterSubstepComponentMapper;

    @Override
    public List<MtRouterSubstepComponent> routerSubstepComponentQuery(Long tenantId, String routerSubstepId) {
        if (StringUtils.isEmpty(routerSubstepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerSubstepId", "【API:routerSubstepComponentQuery】"));
        }

        MtRouterSubstepComponent mtRouterSubstepComponent = new MtRouterSubstepComponent();
        mtRouterSubstepComponent.setTenantId(tenantId);
        mtRouterSubstepComponent.setRouterSubstepId(routerSubstepId);
        return this.mtRouterSubstepComponentMapper.select(mtRouterSubstepComponent);
    }
}
