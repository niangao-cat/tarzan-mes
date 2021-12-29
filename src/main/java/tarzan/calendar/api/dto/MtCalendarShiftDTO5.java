package tarzan.calendar.api.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtCalendarShiftDTO5 implements Serializable {
    private static final long serialVersionUID = -5830039487152729381L;

    @ApiModelProperty(value = "日历ID")
    private String calendarId;
    @ApiModelProperty("班次开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shiftStartDate;
    @ApiModelProperty("班次结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shiftEndDate;
    @ApiModelProperty("排班策略")
    private String shiftType;
    @ApiModelProperty("组织Id")
    private String organizationId;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Date getShiftStartDate() {
        if (shiftStartDate == null) {
            return null;
        } else {
            return (Date) shiftStartDate.clone();
        }
    }

    public void setShiftStartDate(Date shiftStartDate) {
        if (shiftStartDate == null) {
            this.shiftStartDate = null;
        } else {
            this.shiftStartDate = (Date) shiftStartDate.clone();
        }
    }

    public Date getShiftEndDate() {
        if (shiftEndDate == null) {
            return null;
        } else {
            return (Date) shiftEndDate.clone();
        }
    }

    public void setShiftEndDate(Date shiftEndDate) {
        if (shiftEndDate == null) {
            this.shiftEndDate = null;
        } else {
            this.shiftEndDate = (Date) shiftEndDate.clone();
        }
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
