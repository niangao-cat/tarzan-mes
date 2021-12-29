package tarzan.method.app.service;

import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.method.api.dto.MtRouterStepDTO4;

/**
 * 工艺路线步骤应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterStepService {
    /**
     * UI查询工艺路线下步骤列表
     *
     * @author benjamin
     * @date 2019/9/19 3:25 PM
     * @param tenantId 租户Id
     * @param routerId 工艺路线Id
     * @param pageRequest PageRequest
     * @return page
     */
    List<MtRouterStepDTO4> queryRouterStepListForUi(Long tenantId, String routerId, PageRequest pageRequest);

    /**
     * UI保存工艺路线步骤
     *
     * @author benjamin
     * @date 2019/9/20 11:06 AM
     * @param tenantId 租户Id
     * @param dto MtRouterStepDTO4
     * @return String
     */
    String saveRouterStepForUi(Long tenantId, MtRouterStepDTO4 dto);

    /**
     * UI删除工艺路线步骤
     *
     * @author benjamin
     * @date 2019/9/24 3:27 PM
     * @param tenantId 租户Id
     * @param routerStepId 工艺路线步骤Id
     */
    void removeRouterStepForUi(Long tenantId, String routerStepId);
}
