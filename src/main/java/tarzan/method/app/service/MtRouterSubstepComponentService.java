package tarzan.method.app.service;

import java.util.List;

import tarzan.method.api.dto.MtRouterSubstepComponentDTO;

/**
 * 子步骤组件应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterSubstepComponentService {

    /**
     * UI保存工艺路线子步骤组件
     *
     * @author benjamin
     * @date 2019/9/24 10:40 AM
     * @param tenantId 租户Id
     * @param routerSubstepId 工艺路线子步骤Id
     * @param dtoList 工艺路线子步骤组件集合
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterSubstepComponentForUi(Long tenantId, String routerSubstepId,
                                         List<MtRouterSubstepComponentDTO> dtoList, String eventId);

    /**
     * UI删除工艺路线子步骤组件
     *
     * @author benjamin
     * @date 2019/9/24 3:06 PM
     * @param tenantId 租户Id
     * @param routerSubstepComponentId 工艺路线子步骤组件Id
     */
    void removeRouterSubstepComponentForUi(Long tenantId, String routerSubstepComponentId);

    /**
     * UI删除工艺路线子步骤下组件
     *
     * @author benjamin
     * @date 2019/9/24 4:13 PM
     * @param tenantId 租户Id
     * @param routerSubstepId 工艺路线子步骤Id
     */
    void removeRouterSubstepComponentBySubstepIdForUi(Long tenantId, String routerSubstepId);
}
