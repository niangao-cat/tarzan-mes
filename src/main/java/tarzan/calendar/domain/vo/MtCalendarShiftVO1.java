package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class MtCalendarShiftVO1 implements Serializable {
    private static final long serialVersionUID = -1819961145465054384L;

    @ApiModelProperty("日历ID")
    private String calendarId;
    @ApiModelProperty("目标日历ID")
    private String targetCalendarId;
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateFrom;
    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateTo;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Date getShiftDateFrom() {
        if (shiftDateFrom == null) {
            return null;
        }
        return (Date) shiftDateFrom.clone();
    }

    public void setShiftDateFrom(Date shiftDateFrom) {
        if (shiftDateFrom == null) {
            this.shiftDateFrom = null;
        } else {
            this.shiftDateFrom = (Date) shiftDateFrom.clone();
        }
    }

    public Date getShiftDateTo() {
        if (shiftDateTo == null) {
            return null;
        }
        return (Date) shiftDateTo.clone();
    }

    public void setShiftDateTo(Date shiftDateTo) {
        if (shiftDateTo == null) {
            this.shiftDateTo = null;
        } else {
            this.shiftDateTo = (Date) shiftDateTo.clone();
        }
    }

    public String getTargetCalendarId() {
        return targetCalendarId;
    }

    public void setTargetCalendarId(String targetCalendarId) {
        this.targetCalendarId = targetCalendarId;
    }
}
