package tarzan.calendar.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtCalendarShiftDTO3 implements Serializable {
    private static final long serialVersionUID = 3915683451395308856L;

    @ApiModelProperty(value = "日历Id", required = true)
    private String calendarId;
    @ApiModelProperty(value = "日历日期")
    private String calendarDate;

    @ApiModelProperty(hidden = true)
    private Date calendarDateStartTime;

    @ApiModelProperty(hidden = true)
    private Date calendarDateEndTime;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(String calendarDate) {
        this.calendarDate = calendarDate;
    }

    public Date getCalendarDateStartTime() {
        return calendarDateStartTime;
    }

    public void setCalendarDateStartTime(Date calendarDateStartTime) {
        this.calendarDateStartTime = calendarDateStartTime;
    }

    public Date getCalendarDateEndTime() {
        return calendarDateEndTime;
    }

    public void setCalendarDateEndTime(Date calendarDateEndTime) {
        this.calendarDateEndTime = calendarDateEndTime;
    }
}
