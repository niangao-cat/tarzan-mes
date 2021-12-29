package tarzan.method.app.service;

import tarzan.method.api.dto.MtRouterStepGroupDTO;

/**
 * 工艺路线步骤组应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterStepGroupService {
    /**
     * UI保存工艺路线步骤组
     *
     * @author benjamin
     * @date 2019/9/24 11:04 AM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     * @param routerId 工艺路线Id
     * @param dto MtRouterStepGroupDTO
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterStepGroupForUi(Long tenantId, String routerStepId, String routerId, MtRouterStepGroupDTO dto,
                                  String eventId);

    /**
     * UI删除工艺路线步骤组
     *
     * @author benjamin
     * @date 2019/9/24 3:37 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     */
    void removeRouterStepGroupForUi(Long tenantId, String routerStepId);
}
