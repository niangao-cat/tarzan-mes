package tarzan.method.app.service;

import tarzan.method.api.dto.MtRouterLinkDTO;
import tarzan.method.api.dto.MtRouterLinkDTO2;

/**
 * 嵌套工艺路线步骤应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterLinkService {

    /**
     * UI保存嵌套工艺路线
     *
     * @author benjamin
     * @date 2019/9/24 9:44 AM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     * @param dto MtRouterLinkDTO
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterLinkForUi(Long tenantId, String routerStepId, MtRouterLinkDTO dto, String eventId);

    /**
     * UI删除嵌套工艺路线
     *
     * @author benjamin
     * @date 2019/9/24 3:32 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     */
    void removeRouterLinkForUi(Long tenantId, String routerStepId);

    /**
     * UI获取嵌套工艺路线详细信息
     *
     * @author benjamin
     * @date 2019/9/24 3:32 PM
     * @param tenantId 租户Id
     * @param routerId 工艺路线Id
     */
    MtRouterLinkDTO2 queryRouterLinkDetailForUi(Long tenantId, String routerId);

}
