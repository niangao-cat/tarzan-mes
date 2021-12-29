package tarzan.calendar.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.calendar.domain.entity.MtCalendar;
import tarzan.calendar.domain.vo.*;

/**
 * 工作日历资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
public interface MtCalendarRepository extends BaseRepository<MtCalendar>, AopProxy<MtCalendarRepository> {
    /**
     * 工作日历前台查询数据方法
     *
     * @author chuang.yang
     * @date 2019/12/3
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.calendar.domain.entity.MtCalendar>
     */
    List<MtCalendar> calendarQueryForUi(Long tenantId, MtCalendar dto);

    /**
     * calendarGet-获取日历基础属性
     *
     * @param tenantId
     * @param calendarId
     * @return
     */
    MtCalendarVO3 calendarGet(Long tenantId, String calendarId);

    /**
     * calendarBatchGet-批量获取日历基础属性
     *
     * @author chuang.yang
     * @date 2019/6/24
     * @param tenantId
     * @param calendarIds
     * @return java.util.List<hmes.calendar.dto.MtCalendar>
     */
    List<MtCalendarVO3> calendarBatchGet(Long tenantId, List<String> calendarIds);

    /**
     * typeLimitCalendarQuery-获取特定类型日历
     *
     * @param tenantId
     * @param calendarType
     * @return
     */
    List<String> typeLimitCalendarQuery(Long tenantId, String calendarType);

    /**
     * organizationLimitCalendarQuery-获取特定组织结构日历
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<String> organizationLimitCalendarQuery(Long tenantId, MtCalendarVO vo);

    /**
     * calendarAvailableValidate-验证日历有效性
     *
     * @param tenantId
     * @param calendarId
     * @return
     */
    String calendarAvailableValidate(Long tenantId, String calendarId);

    /**
     * organizationLimitOnlyCalendarGet-根据组织结构获取特定日历
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String organizationLimitOnlyCalendarGet(Long tenantId, MtCalendarVO2 dto);

    /**
     * propertyLimitCalendarPropertyQuery-根据属性获取日历信息
     * 
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtCalendarVO5> propertyLimitCalendarPropertyQuery(Long tenantId, MtCalendarVO4 vo);

    /**
     * organizationLimitOnlyCalendarBatchGet-根据组织结构批量获取特定日历
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/20
     */
    List<MtCalendarVO7> organizationLimitOnlyCalendarBatchGet(Long tenantId, MtCalendarVO6 dto);

}
