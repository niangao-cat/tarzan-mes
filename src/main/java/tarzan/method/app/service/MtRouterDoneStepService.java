package tarzan.method.app.service;

/**
 * 完成步骤应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterDoneStepService {
    /**
     * UI保存工艺路线完成步骤
     *
     * @author benjamin
     * @date 2019/9/24 1:07 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     * @param routerDoneStepId 完成步骤Id
     * @param routerDoneStepFlag 是否完成步骤
     * @param eventId 工艺路线更新事件Id
     */
    void saveRouterDoneStepForUi(Long tenantId, String routerStepId, String routerDoneStepId, String routerDoneStepFlag,
                                 String eventId);

    /**
     * UI删除工艺路线完成步骤
     *
     * @author benjamin
     * @date 2019/9/24 3:57 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     */
    void removeRouterDoneStepForUi(Long tenantId, String routerStepId);
}
