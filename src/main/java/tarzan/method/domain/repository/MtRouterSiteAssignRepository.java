package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtRouterSiteAssign;

/**
 * 工艺路线站点分配表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterSiteAssignRepository
                extends BaseRepository<MtRouterSiteAssign>, AopProxy<MtRouterSiteAssignRepository> {

    /**
     * propertyLimitRouterSiteAssignQuery-根据属性获取工艺路线站点分配及其属性
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtRouterSiteAssign> propertyLimitRouterSiteAssignQuery(Long tenantId, MtRouterSiteAssign dto);

    /**
     * 根据工艺路线Id批量获取工艺路线站点分配信息
     *
     * @author benjamin
     * @date 2019-07-31 10:03
     * @param tenantId
     * @param routerIdList 工艺路线Id集合
     * @return List
     */
    List<MtRouterSiteAssign> routerLimitRouterSiteAssignBatchQuery(Long tenantId, List<String> routerIdList);

    /**
     * routerSiteAssignAttrPropertyUpdate-工艺路线站点分配新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/12 15:01
     * @param tenantId, vo
     * @return void
     */
    void routerSiteAssignAttrPropertyUpdate(Long tenantId, MtExtendVO10 vo);
}
