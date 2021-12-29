package tarzan.calendar.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.calendar.api.dto.*;

/**
 * 工作日历班次应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
public interface MtCalendarShiftService {

    /**
     * UI查询日历班次列表
     * 
     * @author benjamin
     * @date 2019/9/27 3:09 PM
     * @param tenantId 租户Id
     * @param dto MtCalendarShiftDTO3
     * @return list
     */
    List<MtCalendarShiftDTO2> queryCalendarShiftGridForUi(Long tenantId, MtCalendarShiftDTO3 dto);

    /**
     * UI查询日历具体日期班次列表
     * 
     * @author benjamin
     * @date 2019/9/27 3:58 PM
     * @param tenantId 租户Id
     * @param vo MtCalendarShiftDTO4
     * @param pageRequest PageRequest
     * @return list
     */
    Page<MtCalendarShiftDTO4> queryCalendarShiftListForUi(Long tenantId, MtCalendarShiftDTO4 vo,
                    PageRequest pageRequest);

    /**
     *UI查询日历具体日期班次列表(不加分页)
     * @author chuang.yang
     * @date 2019/12/20
     * @param tenantId
     * @param vo
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<tarzan.calendar.api.dto.MtCalendarShiftDTO4>
     */
    List<MtCalendarShiftDTO4> queryCalendarShiftListNoPageForUi(Long tenantId, MtCalendarShiftDTO4 vo,
                                                          PageRequest pageRequest);

    /**
     * UI保存日历班次信息
     * 
     * @author benjamin
     * @date 2019/9/27 4:42 PM
     * @param tenantId 租户Id
     * @param dto MtCalendarShiftDTO4
     * @return String
     */
    String saveCalendarShiftForUi(Long tenantId, MtCalendarShiftDTO4 dto);

    /**
     * UI删除日历班次信息
     * 
     * @author benjamin
     * @date 2019/9/27 5:13 PM
     * @param tenantId 租户Id
     * @param calendarShiftList 日历班次Id
     */
    void removeCalendarShiftForUi(Long tenantId, List<String> calendarShiftList);

    /**
     * UI初始化日历班次信息
     * 
     * @author benjamin
     * @date 2019/9/27 6:14 PM
     * @param tenantId 租户Id
     * @param dto MtCalendarShiftDTO5
     */
    void initCalendarShiftForUi(Long tenantId, MtCalendarShiftDTO5 dto);

    /**
     * UI复制日历班次信息-前校验
     *
     * @author chuang.yang
     * @date 2019/12/23
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String copyCalendarShiftCheckForUi(Long tenantId, MtCalendarShiftDTO6 dto);

    /**
     * UI复制日历班次信息
     * 
     * @author benjamin
     * @date 2019/9/29 1:02 PM
     * @param tenantId 租户Id
     * @param dto MtCalendarShiftDTO6
     */
    void copyCalendarShiftForUi(Long tenantId, MtCalendarShiftDTO6 dto);


    /**
     * UI初始化日历班次信息(删除原来班次做判断)
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2020/11/3 14:07
     * @return void
     */
    void newInitCalendarShiftForUi(Long tenantId, MtCalendarShiftDTO5 dto);

    /**
     * UI删除日历班次信息（删除原来班次做判断）
     *
     * @param tenantId
     * @param calendarShiftList
     * @author sanfeng.zhang@hand-china.com 2020/11/3 14:09
     * @return void
     */
    void newRemoveCalendarShiftForUi(Long tenantId, List<String> calendarShiftList);
}
