package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 工单派工
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
public class HmeWoCalendarShiftVO2 implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;
    /**
     * 日历ID
     */
    private String calendarId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateTo;
    private String workOrderId;
    private String prodLineId;
    private String workcellId;

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

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}
