package tarzan.method.app.service;

import tarzan.method.api.dto.MtRouterOperationDTO;

/**
 * 工艺路线步骤对应工序应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterOperationService {
    /**
     * UI保存工艺路线步骤对应工序
     *
     * @author benjamin
     * @date 2019/9/24 9:58 AM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     * @param dto MtRouterOperationDTO
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterOperationForUi(Long tenantId, String routerStepId, MtRouterOperationDTO dto, String eventId);

    /**
     * UI删除工艺路线步骤对应工序
     *
     * @author benjamin
     * @date 2019/9/24 3:35 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     */
    void removeRouterOperationForUi(Long tenantId, String routerStepId);
}
