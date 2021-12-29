package tarzan.calendar.infra.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.calendar.api.dto.MtCalendarShiftDTO3;
import tarzan.calendar.api.dto.MtCalendarShiftDTO4;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.vo.*;

/**
 * 工作日历班次Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
public interface MtCalendarShiftMapper extends BaseMapper<MtCalendarShift> {

    List<String> calendarLimitShiftQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "vo") MtCalendarShiftVO2 vo);

    List<String> timeLimitCalendarShiftQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "vo") MtCalendarShiftVO7 vo);

    List<MtCalendarShift> limitShiftDateQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "vo") MtCalendarShiftVO7 vo);

    List<MtCalendarShift> calendarShiftBatchGet(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "calendarShiftIdList") List<String> calendarShiftIdList);

    List<String> timeLimitAvailableCalendarShiftQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "vo") MtCalendarShiftVO7 vo);

    List<String> limitShiftDateBatchQuery(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "ids") String ids, @Param(value = "shiftDateFrom") String shiftDateFrom,
                    @Param(value = "shiftDateTo") String shiftDateTo);

    void deleteByIdsCustom(@Param(value = "tenantId") Long tenantId, @Param(value = "ids") List<String> ids);

    List<MtCalendarShift> queryCalendarShiftGridForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtCalendarShiftDTO3 dto);

    List<MtCalendarShift> queryCalendarShiftListForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "vo") MtCalendarShiftDTO4 vo);

    List<MtCalendarShiftVO10> propertyLimitCalendarShiftPropertyQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "vo") MtCalendarShiftVO9 vo);

    List<MtCalendarShiftVO12> calendarShiftLimitTargetShiftQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "vo") MtCalendarShiftVO11 vo, @Param(value = "shiftStartTime") Date shiftStartTime,
                    @Param(value = "shiftEndTime") Date shiftEndTime);

    List<String> calendarLimitShiftBatchQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") List<MtCalendarShiftVO2> dto);

    /**
     * 查找工单派工大于0的班次
     * 
     * @param tenantId
     * @param calendarShiftList
     * @author sanfeng.zhang@hand-china.com 2020/11/3 14:39 
     * @return java.util.List<tarzan.calendar.domain.entity.MtCalendarShift>
     */
    List<MtCalendarShift> queryCalendarShiftDispatchQty(@Param("tenantId") Long tenantId, @Param("calendarShiftList") List<String> calendarShiftList);
}
