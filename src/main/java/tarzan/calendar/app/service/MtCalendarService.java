package tarzan.calendar.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.calendar.api.dto.MtCalendarDTO;
import tarzan.calendar.api.dto.MtCalendarLovDTO;

/**
 * 工作日历应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
public interface MtCalendarService {

    /**
     * UI查询工作日历列表
     * 
     * @author benjamin
     * @date 2019/9/27 10:04 AM
     * @param tenantId 租户Id
     * @param dto MtCalendarDTO
     * @param pageRequest PageRequest
     * @return page
     */
    Page<MtCalendarDTO> queryCalendarListForUi(Long tenantId, MtCalendarDTO dto, PageRequest pageRequest);

    /**
     * UI查询工作日历LOV
     *
     * @author chuang.yang
     * @date 2019/12/6
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<tarzan.calendar.api.dto.MtCalendarLovDTO>
     */
    Page<MtCalendarLovDTO> queryCalendarLovForUi(Long tenantId, MtCalendarDTO dto, PageRequest pageRequest);

    /**
     * UI保存工作日历
     *
     * @author chuang.yang
     * @date 2019/12/4
     * @param tenantId
     * @param dto
     * @return tarzan.calendar.api.dto.MtCalendarDTO
     */
    MtCalendarDTO saveCalendarForUi(Long tenantId, MtCalendarDTO dto);

    /**
     * UI根据calendarId查询日历信息
     *
     * @author chuang.yang
     * @date 2019/12/30
     * @param tenantId
     * @param calendarId
     * @return tarzan.calendar.api.dto.MtCalendarDTO
     */
    MtCalendarDTO getCalendarForUi(Long tenantId, String calendarId);
}
