package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtCalendarShiftVO6 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4083739819815348526L;

    private String calendarId; // 业务组织实体类型

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateFrom; // 开始时间

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateTo; // 结束时间

    private List<String> targetCalendarIds;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Date getDateFrom() {
        if (dateFrom == null) {
            return null;
        }
        return (Date) dateFrom.clone();
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo == null) {
            return null;
        }
        return (Date) dateTo.clone();
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    public List<String> getTargetCalendarIds() {
        return targetCalendarIds;
    }

    public void setTargetCalendarIds(List<String> targetCalendarIds) {
        this.targetCalendarIds = targetCalendarIds;
    }

}
