package tarzan.method.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.method.api.dto.MtRouterDTO;
import tarzan.method.api.dto.MtRouterDTO2;

/**
 * 工艺路线应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterService {
    /**
     * UI查询工艺路线列表
     *
     * @author benjamin
     * @date 2019/9/18 3:57 PM
     * @param tenantId 租户Id
     * @param dto MtRouterDTO
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtRouterDTO> queryRouterListForUi(Long tenantId, MtRouterDTO dto, PageRequest pageRequest);

    /**
     * UI查询工艺路线明细信息
     *
     * @author benjamin
     * @date 2019/9/18 3:57 PM
     * @param tenantId 租户Id
     * @param routerId String
     * @return MtRouterDTO
     */
    MtRouterDTO queryRouteretailForUi(Long tenantId, String routerId);

    /**
     * UI保存工艺路线
     *
     * @author benjamin
     * @date 2019/9/19 1:53 PM
     * @param tenantId 租户Id
     * @param dto MtRouterDTO
     * @return String
     */
    String saveRouterForUi(Long tenantId, MtRouterDTO dto);

    /**
     * UI弹窗提示后确认保存工艺路线
     *
     * @author chuang.yang
     * @date 2020/02/04 1:53 PM
     * @param tenantId 租户Id
     * @param dto MtRouterDTO
     * @return String
     */
    String confirmSaveRouterForUi(Long tenantId, MtRouterDTO dto);

    /**
     * UI删除工艺路线
     *
     * @author benjamin
     * @date 2019/9/19 1:53 PM
     * @param tenantId 租户Id
     * @param condition List
     * @return Integer
     */
    Integer deleteRouterForUi(Long tenantId, List<String> condition);

    /**
     * UI复制工艺路线
     *
     * @author benjamin
     * @date 2019/9/24 5:51 PM
     * @param tenantId 租户Id
     * @param dto MtRouterDTO2
     * @return String
     */
    String copyRouterForUi(Long tenantId, MtRouterDTO2 dto);
}
