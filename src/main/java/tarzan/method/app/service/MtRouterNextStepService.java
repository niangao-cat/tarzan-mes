package tarzan.method.app.service;

import java.util.List;

import tarzan.method.api.dto.MtRouterNextStepDTO;

/**
 * 下一步骤应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterNextStepService {
    /**
     * UI保存下一步骤
     *
     * @author benjamin
     * @date 2019/9/24 12:37 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     * @param dtoList 下一步骤集合
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterNextStepForUi(Long tenantId, String routerStepId, List<MtRouterNextStepDTO> dtoList, String eventId);

    /**
     * UI删除下一步骤
     *
     * @author benjamin
     * @date 2019/9/24 2:56 PM
     * @param tenantId 租户Id
     * @param routerNextStepId 工艺路线下一步骤Id
     */
    void removeRouterNextStepForUi(Long tenantId, String routerNextStepId);

    /**
     * UI删除工艺路线步骤对应下一步骤
     *
     * @author benjamin
     * @date 2019/9/24 3:54 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     */
    void removeRouterNextStepByRouterStepIdForUi(Long tenantId, String routerStepId);
}
