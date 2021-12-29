package tarzan.method.app.service;

import java.util.List;

import tarzan.method.api.dto.MtRouterSubstepDTO;

/**
 * 工艺路线子步骤应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterSubstepService {

    /**
     * UI保存工艺路线子步骤
     *
     * @author benjamin
     * @date 2019/9/24 10:13 AM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     * @param dtoList 工艺路线子步骤集合
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterSubstepForUi(Long tenantId, String routerStepId, List<MtRouterSubstepDTO> dtoList, String eventId);

    /**
     * UI删除工艺路线子步骤
     *
     * @author benjamin
     * @date 2019/9/24 3:03 PM
     * @param tenantId 租户Id
     * @param routerSubstepId 工艺路线子步骤Id
     */
    void removeRouterSubstepForUi(Long tenantId, String routerSubstepId);

    /**
     * UI删除工艺路线步骤下子步骤
     *
     * @author benjamin
     * @date 2019/9/24 4:16 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     */
    void removeRouterSubstepByRouterStepIdForUi(Long tenantId, String routerStepId);
}
