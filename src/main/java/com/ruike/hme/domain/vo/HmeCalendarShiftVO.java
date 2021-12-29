package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * 班次日历
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 09:29
 */
@Data
public class HmeCalendarShiftVO implements Comparable<HmeCalendarShiftVO> {
    @ApiModelProperty("班次ID")
    private String calendarShiftId;
    @ApiModelProperty(value = "班次代码")
    private String shiftCode;
    @ApiModelProperty(value = "班次所在日期，年月日")
    private Date shiftDate;
    @ApiModelProperty(value = "休息时长")
    private Double restTime;
    @ApiModelProperty(value = "班次开始时间")
    private Date shiftStartTime;
    @ApiModelProperty(value = "班次结束时间")
    private Date shiftEndTime;
    @ApiModelProperty("日历ID")
    private String calendarId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeCalendarShiftVO that = (HmeCalendarShiftVO) o;
        return Objects.equals(calendarShiftId, that.calendarShiftId) &&
                Objects.equals(shiftCode, that.shiftCode) &&
                Objects.equals(shiftDate, that.shiftDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calendarShiftId, shiftCode, shiftDate);
    }

    @Override
    public int compareTo(HmeCalendarShiftVO o) {
        int shiftDateCompare = this.shiftDate.compareTo(o.shiftDate);
        if (shiftDateCompare != 0) {
            return shiftDateCompare;
        }
        return this.shiftCode.compareTo(o.shiftCode);
    }
}
