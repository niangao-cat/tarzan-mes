package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterLink;

/**
 * 嵌套工艺路线步骤资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterLinkRepository extends BaseRepository<MtRouterLink>, AopProxy<MtRouterLinkRepository> {

    /**
     * routerLinkGet-获取嵌套工艺路线类型步骤的相关属性
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    MtRouterLink routerLinkGet(Long tenantId, String routerStepId);

    /**
     * routerLinkBatchGet-批量获取嵌套工艺路线类型步骤的相关属性
     *
     * @param tenantId
     * @param routerStepIds
     * @return
     */
    List<MtRouterLink> routerLinkBatchGet(Long tenantId, List<String> routerStepIds);
}
