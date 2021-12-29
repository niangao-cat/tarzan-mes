package tarzan.calendar.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.calendar.domain.entity.MtCalendar;
import tarzan.calendar.domain.vo.MtCalendarVO3;
import tarzan.calendar.domain.vo.MtCalendarVO4;
import tarzan.calendar.domain.vo.MtCalendarVO5;

/**
 * 工作日历Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
public interface MtCalendarMapper extends BaseMapper<MtCalendar> {

    List<String> typeLimitCalendarQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "calendarType") String calendarType);

    List<MtCalendar> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param("calendarIds") List<String> calendarIds);

    /**
     * 批量获取日历属性
     * 
     * @Author peng.yuan
     * @Date 2019/9/23 9:54
     * @param tenantId :
     * @param calendarIds :
     * @return java.util.List<tarzan.calendar.domain.vo.MtCalendarVO3>
     */
    List<MtCalendarVO3> calendarBatchGet(@Param(value = "tenantId") Long tenantId,
                    @Param("calendarIds") List<String> calendarIds);

    /**
     * 根据属性获取日历信息
     * 
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtCalendarVO5> propertyLimitCalendarPropertyQuery(@Param(value = "tenantId") Long tenantId,
                    @Param("vo") MtCalendarVO4 vo);

    List<MtCalendar> propertyOrgLimitQuery(@Param(value = "tenantId") Long tenantId, @Param("dto") MtCalendar dto);


    List<MtCalendar> selectByIdsAndType(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "calendarType") String calendarType, @Param("calendarIds") List<String> calendarIds);
}
