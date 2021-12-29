package tarzan.method.app.service;

import java.util.List;

import tarzan.method.api.dto.MtRouterStepGroupStepDTO;

/**
 * 工艺路线步骤组行步骤应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterStepGroupStepService {
    /**
     * UI保存工艺路线步骤组行步骤
     *
     * @author benjamin
     * @date 2019/9/24 11:15 AM
     * @param tenantId 租户Id
     * @param routerStepGroupId 工艺路线步骤组Id
     * @param routerId 工艺路线Id
     * @param dtoList 工艺路线步骤组行步骤集合
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterStepGroupStepForUi(Long tenantId, String routerStepGroupId, String routerId,
                                      List<MtRouterStepGroupStepDTO> dtoList, String eventId);

    /**
     * 保存步骤组下的步骤
     *
     * @param tenantId
     * @param routerId
     * @param dto
     * @param eventId
     * @return
     */
    String saveRouterStep(Long tenantId, String routerId, MtRouterStepGroupStepDTO dto, String eventId);

    /**
     * UI删除工艺路线步骤组行步骤
     *
     * @author benjamin
     * @date 2019/9/24 3:10 PM
     * @param tenantId 租户Id
     * @param routerStepGroupStepId 工艺路线步骤组行步骤Id
     * @param routerStepId 工艺路线步骤组步骤Id
     */
    void removeRouterStepGroupStepForUi(Long tenantId, String routerStepGroupStepId, String routerStepId);

    /**
     * UI删除工艺路线步骤组下步骤
     *
     * @author benjamin
     * @date 2019/9/24 4:22 PM
     * @param tenantId 租户Id
     * @param routerStepGroupId 工艺路线步骤组Id
     */
    void removeRouterStepGroupStepByRouterStepGroupIdForUi(Long tenantId, String routerStepGroupId);
}
