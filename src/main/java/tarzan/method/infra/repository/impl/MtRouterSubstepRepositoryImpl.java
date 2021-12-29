package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtRouterSubstep;
import tarzan.method.domain.repository.MtRouterSubstepRepository;
import tarzan.method.infra.mapper.MtRouterSubstepMapper;

/**
 * 工艺路线子步骤 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
public class MtRouterSubstepRepositoryImpl extends BaseRepositoryImpl<MtRouterSubstep>
                implements MtRouterSubstepRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterSubstepMapper mtRouterSubstepMapper;

    @Override
    public List<MtRouterSubstep> routerSubstepQuery(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerSubstepQuery】"));
        }

        MtRouterSubstep mtRouterSubstep = new MtRouterSubstep();
        mtRouterSubstep.setTenantId(tenantId);
        mtRouterSubstep.setRouterStepId(routerStepId);
        return this.mtRouterSubstepMapper.select(mtRouterSubstep);
    }
}
