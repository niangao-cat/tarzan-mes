package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterStepGroup;

/**
 * 工艺路线步骤组资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterStepGroupRepository
                extends BaseRepository<MtRouterStepGroup>, AopProxy<MtRouterStepGroupRepository> {

    /**
     * routerStepGroupGet-获取步骤组类型步骤的相关属性
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    MtRouterStepGroup routerStepGroupGet(Long tenantId, String routerStepId);


    /**
     * routerStepGroupBatchGet-批量获取步骤组类型步骤的相关属性
     *
     * @param tenantId
     * @param routerStepIds
     * @return
     */
    List<MtRouterStepGroup> routerStepGroupBatchGet(Long tenantId, List<String> routerStepIds);
}
