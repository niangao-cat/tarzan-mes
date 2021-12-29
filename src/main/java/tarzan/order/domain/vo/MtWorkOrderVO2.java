package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtWorkOrderVO2 implements Serializable {

    private static final long serialVersionUID = 9005118544427511323L;

    private String workOrderId;
    private String holdReasonCode;
    private String comments;
    private Date expiredReleaseTime;
    private String workcellId;
    private String locatorId;
    private String parentEventId;
    private String eventRequestId;
    private String targetStatus;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getHoldReasonCode() {
        return holdReasonCode;
    }

    public void setHoldReasonCode(String holdReasonCode) {
        this.holdReasonCode = holdReasonCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getExpiredReleaseTime() {
        if (expiredReleaseTime != null) {
            return (Date) expiredReleaseTime.clone();
        } else {
            return null;
        }
    }

    public void setExpiredReleaseTime(Date expiredReleaseTime) {
        if (expiredReleaseTime == null) {
            this.expiredReleaseTime = null;
        } else {
            this.expiredReleaseTime = (Date) expiredReleaseTime.clone();
        }
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
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

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }
}
