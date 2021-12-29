package tarzan.calendar.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.calendar.api.dto.*;

/**
 * 日历组织关系表应用服务
 *
 * @author peng.yuan@hand-china.com 2019-09-19 10:50:16
 */
public interface MtCalendarOrgRelService {

    /**
     * UI查询工作日历分配组织
     *
     * @author benjamin
     * @date 2019/9/27 10:19 AM
     * @param tenantId 租户Id
     * @param dto 查询条件
     * @param pageRequest PageRequest
     * @return Page
     */
    Page<MtCalendarOrgRelDTO> queryCalendarOrgRelForUi(Long tenantId, MtCalendarOrgRelDTO1 dto,
                    PageRequest pageRequest);

    /**
     * UI批量保存工作日历分配组织
     *
     * @author chuang.yang
     * @date 2019/12/10
     * @param tenantId
     * @param dto
     * @return void
     */
    List<MtCalendarOrgRelDTO> saveCalendarOrgRelBatchForUi(Long tenantId, MtCalendarOrgRelDTO2 dto);

    /**
     * UI批量删除工作日历分配组织
     * 
     * @author chuang.yang
     * @date 2019/12/10
     * @param tenantId
     * @param calendarOrgRelIdList
     * @return void
     */
    void removeCalendarOrgRelBatchForUi(Long tenantId, List<String> calendarOrgRelIdList);

    /**
     * UI日历组织分配，查询用户权限组织树
     *
     * @author chuang.yang
     * @date 2019/12/11
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.calendar.api.dto.MtCalendarOrgRelDTO4>
     */
    List<MtCalendarOrgRelDTO4> userLimitOrganizationTreeSingleForUi(Long tenantId, MtCalendarOrgRelDTO5 dto);
}
