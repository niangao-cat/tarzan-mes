package tarzan.general.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class MtEventDTO implements Serializable {
    private static final long serialVersionUID = 3440856879010746666L;

    @ApiModelProperty(name = "事件请求类型Id")
    private String requestTypeId;
    @ApiModelProperty(name = "事件请求类型")
    private String requestTypeCode;
    @ApiModelProperty(name = "事件类型Id")
    private String eventTypeId;
    @ApiModelProperty(name = "事件类型")
    private String eventTypeCode;
    @ApiModelProperty(name = "开始时间", required = true)
    private Date startTime;
    @ApiModelProperty(name = "结束时间", required = true)
    private Date endTime;
    @ApiModelProperty(name = "事件请求主键")
    private String requestIdList;
    @ApiModelProperty(name = "事件主键")
    private String eventIdList;
    @ApiModelProperty(name = "操作人")
    private Long operationBy;
    @ApiModelProperty(name = "库位")
    private String locatorId;
    @ApiModelProperty(name = "班次日期，格式yyyy-MM-dd")
    private String shiftDate;
    @ApiModelProperty(name = "班次编码")
    private String shiftCode;
    @ApiModelProperty(name = "工作单元")
    private String workcellId;

    public String getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public Date getStartTime() {
        if (startTime != null) {
            return (Date) startTime.clone();
        } else {
            return null;
        }
    }

    public void setStartTime(Date startTime) {
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = (Date) startTime.clone();
        }
    }

    public Date getEndTime() {
        if (endTime != null) {
            return (Date) endTime.clone();
        } else {
            return null;
        }
    }

    public void setEndTime(Date endTime) {
        if (endTime == null) {
            this.endTime = null;
        } else {
            this.endTime = (Date) endTime.clone();
        }
    }

    public String getRequestIdList() {
        return requestIdList;
    }

    public void setRequestIdList(String requestIdList) {
        this.requestIdList = requestIdList;
    }

    public String getEventIdList() {
        return eventIdList;
    }

    public void setEventIdList(String eventIdList) {
        this.eventIdList = eventIdList;
    }

    public Long getOperationBy() {
        return operationBy;
    }

    public void setOperationBy(Long operationBy) {
        this.operationBy = operationBy;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}
