package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterSubstep;

/**
 * 工艺路线子步骤资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterSubstepRepository
                extends BaseRepository<MtRouterSubstep>, AopProxy<MtRouterSubstepRepository> {

    /**
     * routerSubstepQuery-获取步骤的子步骤清单
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    List<MtRouterSubstep> routerSubstepQuery(Long tenantId, String routerStepId);
}
