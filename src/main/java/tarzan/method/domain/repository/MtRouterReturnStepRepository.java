package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterReturnStep;

/**
 * 返回步骤资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterReturnStepRepository
        extends BaseRepository<MtRouterReturnStep>, AopProxy<MtRouterReturnStepRepository> {

    /**
     * routerReturnStepGet-获取返回步骤的基础信息
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    MtRouterReturnStep routerReturnStepGet(Long tenantId, String routerStepId);


    /**
     * routerReturnStepBatchGet-批量获取返回步骤的基础信息
     *
     * @param tenantId
     * @param routerStepIds
     * @return
     */
    List<MtRouterReturnStep> routerReturnStepBatchGet(Long tenantId, List<String> routerStepIds);

    /**
     * returnStepValidate-校验步骤是否返回步骤
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    String returnStepValidate(Long tenantId, String routerStepId);
}
