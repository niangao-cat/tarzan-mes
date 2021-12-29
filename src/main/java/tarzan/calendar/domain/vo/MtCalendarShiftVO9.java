package tarzan.calendar.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/10 14:35
 * @Author: ${yiyang.xie}
 */
public class MtCalendarShiftVO9 implements Serializable {
    private static final long serialVersionUID = 6458085237821189683L;

    @ApiModelProperty("日历班次ID")
    private String calendarShiftId;
    @ApiModelProperty("日历ID")
    private String calendarId;
    @ApiModelProperty("日历班次日期从")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date calendarShiftDateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("日历班次日期至")
    private Date calendarShiftDateTo;
    @ApiModelProperty("日历班次编码")
    private String calendarShiftCode;
    @ApiModelProperty("是否有效")
    private String enableFlag;
    @ApiModelProperty("班次开始时间从")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftStartTimeFrom;
    @ApiModelProperty("班次开始时间至")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftStartTimeTo;
    @ApiModelProperty("班次结束时间从")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftEndTimeFrom;
    @ApiModelProperty("班次结束时间至")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftEndTimeTo;

    public String getCalendarShiftId() {
        return calendarShiftId;
    }

    public void setCalendarShiftId(String calendarShiftId) {
        this.calendarShiftId = calendarShiftId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Date getCalendarShiftDateFrom() {
        if (calendarShiftDateFrom != null) {
            return (Date) calendarShiftDateFrom.clone();
        } else {
            return null;
        }
    }

    public void setCalendarShiftDateFrom(Date calendarShiftDateFrom) {
        if (calendarShiftDateFrom == null) {
            this.calendarShiftDateFrom = null;
        } else {
            this.calendarShiftDateFrom = (Date) calendarShiftDateFrom.clone();
        }
    }

    public Date getCalendarShiftDateTo() {
        if (calendarShiftDateTo != null) {
            return (Date) calendarShiftDateTo.clone();
        } else {
            return null;
        }
    }

    public void setCalendarShiftDateTo(Date calendarShiftDateTo) {
        if (calendarShiftDateTo == null) {
            this.calendarShiftDateTo = null;
        } else {
            this.calendarShiftDateTo = (Date) calendarShiftDateTo.clone();
        }
    }

    public String getCalendarShiftCode() {
        return calendarShiftCode;
    }

    public void setCalendarShiftCode(String calendarShiftCode) {
        this.calendarShiftCode = calendarShiftCode;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Date getShiftStartTimeFrom() {
        if (shiftStartTimeFrom != null) {
            return (Date) shiftStartTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setShiftStartTimeFrom(Date shiftStartTimeFrom) {
        if (shiftStartTimeFrom == null) {
            this.shiftStartTimeFrom = null;
        } else {
            this.shiftStartTimeFrom = (Date) shiftStartTimeFrom.clone();
        }
    }

    public Date getShiftStartTimeTo() {
        if (shiftStartTimeTo != null) {
            return (Date) shiftStartTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setShiftStartTimeTo(Date shiftStartTimeTo) {
        if (shiftStartTimeTo == null) {
            this.shiftStartTimeTo = null;
        } else {
            this.shiftStartTimeTo = (Date) shiftStartTimeTo.clone();
        }
    }

    public Date getShiftEndTimeFrom() {
        if (shiftEndTimeFrom != null) {
            return (Date) shiftEndTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setShiftEndTimeFrom(Date shiftEndTimeFrom) {
        if (shiftEndTimeFrom == null) {
            this.shiftEndTimeFrom = null;
        } else {
            this.shiftEndTimeFrom = (Date) shiftEndTimeFrom.clone();
        }
    }

    public Date getShiftEndTimeTo() {
        if (shiftEndTimeTo != null) {
            return (Date) shiftEndTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setShiftEndTimeTo(Date shiftEndTimeTo) {
        if (shiftEndTimeTo == null) {
            this.shiftEndTimeTo = null;
        } else {
            this.shiftEndTimeTo = (Date) shiftEndTimeTo.clone();
        }
    }
}
