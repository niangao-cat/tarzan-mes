package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.vo.MtRouterNextStepVO3;

/**
 * 下一步骤资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterNextStepRepository
                extends BaseRepository<MtRouterNextStep>, AopProxy<MtRouterNextStepRepository> {


    /**
     * routerNextStepQuery-获取步骤的下一个步骤清单
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    List<MtRouterNextStep> routerNextStepQuery(Long tenantId, String routerStepId);

    /**
     * routerPreviousStepQuery-获取步骤的上一个步骤清单
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    List<MtRouterNextStep> routerPreviousStepQuery(Long tenantId, String routerStepId);

    /**
     * decisionTypeLimitRouterNextStepQuery-获取特定策略的下一个步骤清单
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtRouterNextStep> decisionTypeLimitRouterNextStepQuery(Long tenantId, MtRouterNextStepVO3 dto);
}
