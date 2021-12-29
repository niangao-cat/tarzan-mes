package tarzan.method.app.service;

import java.util.List;

import tarzan.method.api.dto.MtRouterOperationComponentDTO;

/**
 * 工艺路线步骤对应工序组件应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
public interface MtRouterOperationComponentService {
    /**
     * UI保存工艺路线步骤对应工序组件
     *
     * @author benjamin
     * @date 2019/9/24 10:54 AM
     * @param tenantId 租户Id
     * @param routerOperationId 工艺路线步骤对应工序Id
     * @param dtoList 工艺路线步骤对应工序组件集合
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterOperationComponentForUi(Long tenantId, String routerOperationId,
                                           List<MtRouterOperationComponentDTO> dtoList, String eventId);

    /**
     * UI删除工艺路线步骤对应工序组件
     *
     * @author benjamin
     * @date 2019/9/24 3:01 PM
     * @param tenantId 租户Id
     * @param routerOperationComponentId 工艺路线步骤对应工序组件Id
     */
    void removeRouterOperationComponentForUi(Long tenantId, String routerOperationComponentId);

    /**
     * UI删除工艺路线步骤对应工序下组件
     *
     * @author benjamin
     * @date 2019/9/24 4:06 PM
     * @param tenantId 租户Id
     * @param routerOperationId 工艺路线步骤对应工序Id
     */
    void removeRouterOperationComponentByRouterOperationIdForUi(Long tenantId, String routerOperationId);
}
