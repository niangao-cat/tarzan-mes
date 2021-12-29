package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtCalendarShiftVO7 implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;
    private String calendarId; // 日历ID

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateFrom;

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
}
