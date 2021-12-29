package tarzan.calendar.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.vo.*;

/**
 * 工作日历班次资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
public interface MtCalendarShiftRepository
                extends BaseRepository<MtCalendarShift>, AopProxy<MtCalendarShiftRepository> {

    /**
     * calendarShiftGet-获取日历班次基础属性
     *
     * @param tenantId
     * @param calendarShiftId
     * @return
     */
    MtCalendarShift calendarShiftGet(Long tenantId, String calendarShiftId);

    /**
     * calendarShiftBatchGet-批量获取日历班次基础属性
     *
     * @param tenantId
     * @return
     */
    List<MtCalendarShift> calendarShiftBatchGet(Long tenantId, List<String> calendarShiftIdList);

    /**
     * calendarLimitShiftQuery-获取特定日历班次
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<String> calendarLimitShiftQuery(Long tenantId, MtCalendarShiftVO2 vo);

    /**
     * calendarShiftAvailableValidate-验证班次有效性
     *
     * @param tenantId
     * @param calendarShiftId
     * @return
     */
    String calendarShiftAvailableValidate(Long tenantId, String calendarShiftId);

    /**
     * timeLimitCalendarShiftQuery-获取特定时间段日历班次
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<String> timeLimitCalendarShiftQuery(Long tenantId, MtCalendarShiftVO7 vo);

    /**
     * timeLimitAvailableCalendarShiftQuery-获取特定时间段有效日历班次
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<String> timeLimitAvailableCalendarShiftQuery(Long tenantId, MtCalendarShiftVO7 vo);

    /**
     * calendarShiftCopy-复制日历班次
     *
     * @param tenantId
     * @param vo
     */
    void calendarShiftCopy(Long tenantId, MtCalendarShiftVO1 vo);

    /**
     * calendarShiftBatchCopy-批量复制日历班次
     *
     * @param tenantId
     * @param dto
     */
    void calendarShiftBatchCopy(Long tenantId, MtCalendarShiftVO6 dto);

    /**
     * calendarPreviousShiftGet-获取日历上一班次
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtCalendarShift calendarPreviousShiftGet(Long tenantId, MtCalendarShiftVO dto);

    /**
     * calendarNextShiftGet-获取日历下一班次
     *
     * @param tenantId
     * @param dto
     */
    MtCalendarShift calendarNextShiftGet(Long tenantId, MtCalendarShiftVO dto);

    /**
     * calendarShiftLimitNearShiftQuery-获取指定班次前后班次
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtCalendarShiftVO4> calendarShiftLimitNearShiftQuery(Long tenantId, MtCalendarShiftVO3 dto);

    /**
     * organizationAndShiftLimitCalendarShiftGet-获取特定组织及班次下日历班次
     *
     * @param tenantId
     * @param dto
     * @return
     */
    String organizationAndShiftLimitCalendarShiftGet(Long tenantId, MtCalendarShiftVO5 dto);

    /**
     * currentShiftLimitNextCalendarShiftGet-基于当前班次获取工作日历下一班次
     *
     * @param tenantId
     * @param workcellId
     * @author guichuan.li
     * @date 2019/9/27
     */
    MtCalendarShiftVO8 currentShiftLimitNextCalendarShiftGet(Long tenantId, String workcellId);

    /**
     * propertyLimitCalendarShiftPropertyQuery-根据属性获取日历班次信息
     * 
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtCalendarShiftVO10> propertyLimitCalendarShiftPropertyQuery(Long tenantId, MtCalendarShiftVO9 vo);

    /**
     * calendarShiftLimitTargetShiftQuery-根据日历班次获取特定班次信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtCalendarShiftVO12> calendarShiftLimitTargetShiftQuery(Long tenantId, MtCalendarShiftVO11 vo);

    /**
     * calendarLimitShiftBatchQuery-批量获取特定日历班次
     *
     * @author chuang.yang
     * @date 2019/12/12
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    List<String> calendarLimitShiftBatchQuery(Long tenantId, List<MtCalendarShiftVO2> dto);

    /**
     * dateLimitCalendarShiftGet-获取指定时间指定组织下的计划班次
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtCalendarShiftVO14> dateLimitCalendarShiftGet(Long tenantId, MtCalendarShiftVO13 dto);
}
