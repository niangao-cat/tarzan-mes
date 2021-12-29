package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class MtCalendarShiftVO2 implements Serializable {

    private static final long serialVersionUID = -7250087812520958065L;

    @ApiModelProperty(value = "日历ID", required = true)
    private String calendarId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "班次所在日期，年月日")
    private Date shiftDate;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        }
        return (Date) shiftDate.clone();
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }
}
