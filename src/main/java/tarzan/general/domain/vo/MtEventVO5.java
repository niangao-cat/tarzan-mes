package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class MtEventVO5 implements Serializable {

    private static final long serialVersionUID = -1543023751390479692L;

    @ApiModelProperty(value = "事件Id")
    private String eventId;
    @ApiModelProperty(value = "事件类型编码")
    private String eventTypeCode;
    @ApiModelProperty(value = "事件类型描述")
    private String eventTypeDescription;
    @ApiModelProperty(value = "事件事件")
    private Date eventTime;
    @ApiModelProperty(value = "事件人")
    private Long eventBy;
    @ApiModelProperty(value = "事件人名称")
    private String eventTypeUserName;

    @ApiModelProperty(value = "工位Id")
    private String workcellId;
    @ApiModelProperty(value = "工位编码")
    private String workcellCode;
    @ApiModelProperty(value = "库位Id")
    private String locatorId;
    @ApiModelProperty(value = "库位编码")
    private String locatorCode;
    @ApiModelProperty(value = "班组编码")
    private String shiftCode;
    @ApiModelProperty(value = "编组日期")
    private Date shiftDate;

    @ApiModelProperty(value = "父事件Id")
    private String parentEventId;
    @ApiModelProperty(value = "事件请求Id")
    private String eventRequestId;
    @ApiModelProperty(value = "事件请求类型编码")
    private String requestTypeCode;
    @ApiModelProperty(value = "事件请求类型描述")
    private String requestTypeDescription;
    @ApiModelProperty(value = "事件请求事件")
    private Date requestTime;
    @ApiModelProperty(value = "事件请求人")
    private Long requestBy;
    @ApiModelProperty(value = "事件请求人名称")
    private String requestTypeUserName;

    @ApiModelProperty(value = "是否父事件标识")
    private Boolean parentEventFlag;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getEventTypeDescription() {
        return eventTypeDescription;
    }

    public void setEventTypeDescription(String eventTypeDescription) {
        this.eventTypeDescription = eventTypeDescription;
    }

    public Date getEventTime() {
        if (eventTime == null) {
            return null;
        } else {
            return (Date) eventTime.clone();
        }
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

    public String getEventTypeUserName() {
        return eventTypeUserName;
    }

    public void setEventTypeUserName(String eventTypeUserName) {
        this.eventTypeUserName = eventTypeUserName;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Date getShiftDate() {
        if (shiftDate == null) {
            return null;
        } else {
            return (Date) shiftDate.clone();
        }
    }

    public void setShiftDate(Date shiftDate) {
        if (shiftDate == null) {
            this.shiftDate = null;
        } else {
            this.shiftDate = (Date) shiftDate.clone();
        }
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getRequestTypeCode() {
        return requestTypeCode;
    }

    public void setRequestTypeCode(String requestTypeCode) {
        this.requestTypeCode = requestTypeCode;
    }

    public String getRequestTypeDescription() {
        return requestTypeDescription;
    }

    public void setRequestTypeDescription(String requestTypeDescription) {
        this.requestTypeDescription = requestTypeDescription;
    }

    public Date getRequestTime() {
        if (requestTime == null) {
            return null;
        } else {
            return (Date) requestTime.clone();
        }
    }

    public void setRequestTime(Date requestTime) {
        if (requestTime == null) {
            this.requestTime = null;
        } else {
            this.requestTime = (Date) requestTime.clone();
        }
    }

    public Long getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(Long requestBy) {
        this.requestBy = requestBy;
    }

    public String getRequestTypeUserName() {
        return requestTypeUserName;
    }

    public void setRequestTypeUserName(String requestTypeUserName) {
        this.requestTypeUserName = requestTypeUserName;
    }

    public Boolean getParentEventFlag() {
        return parentEventFlag;
    }

    public void setParentEventFlag(Boolean parentEventFlag) {
        this.parentEventFlag = parentEventFlag;
    }
}
