package tarzan.method.app.service;

import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.method.api.dto.MtRouterSiteAssignDTO2;

/**
 * 工艺路线站点分配表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterSiteAssignService {

    /**
     * UI查询工艺路线分配站点列表
     *
     * @author benjamin
     * @date 2019/9/20 1:35 PM
     * @param tenantId 租户Id
     * @param routerId 工艺路线Id
     * @param pageRequest PageRequest
     * @return page
     */
    List<MtRouterSiteAssignDTO2> queryRouterSiteAssignListForUi(Long tenantId, String routerId,
                                                                PageRequest pageRequest);

    /**
     * UI保存工艺路线分配站点
     *
     * @author benjamin
     * @date 2019/9/24 5:04 PM
     * @param tenantId 租户Id
     * @param dto MtRouterSiteAssignDTO2
     * @return String
     */
    String saveRouterSiteAssignForUi(Long tenantId, MtRouterSiteAssignDTO2 dto);

    /**
     * UI删除工艺路线分配站点
     *
     * @author benjamin
     * @date 2019/9/24 5:36 PM
     * @param tenantId 租户Id
     * @param routerSiteAssignId 工艺路线站点分配Id
     */
    void removeRouterSiteAssignForUi(Long tenantId, String routerSiteAssignId);
}
