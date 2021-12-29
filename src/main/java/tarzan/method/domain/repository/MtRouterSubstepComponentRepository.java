package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterSubstepComponent;

/**
 * 子步骤组件资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterSubstepComponentRepository
                extends BaseRepository<MtRouterSubstepComponent>, AopProxy<MtRouterSubstepComponentRepository> {

    /**
     * routerSubstepComponentQuery-获取子步骤的组件清单
     *
     * @param tenantId
     * @param routerSubstepId
     * @return
     */
    List<MtRouterSubstepComponent> routerSubstepComponentQuery(Long tenantId, String routerSubstepId);
}
