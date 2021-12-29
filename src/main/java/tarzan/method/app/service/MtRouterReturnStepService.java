package tarzan.method.app.service;

import tarzan.method.api.dto.MtRouterReturnStepDTO;

/**
 * 返回步骤应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterReturnStepService {

    /**
     * UI保存返回步骤
     *
     * @author benjamin
     * @date 2019/9/24 1:28 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     * @param dto MtRouterReturnStepDTO
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterReturnStepForUi(Long tenantId, String routerStepId, MtRouterReturnStepDTO dto, String eventId);

    /**
     * UI删除返回步骤
     *
     * @author benjamin
     * @date 2019/9/24 3:59 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     */
    void removeRouterReturnStepForUi(Long tenantId, String routerStepId);
}
