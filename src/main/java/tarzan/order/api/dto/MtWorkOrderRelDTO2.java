package tarzan.order.api.dto;


import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;



public class MtWorkOrderRelDTO2 implements Serializable {

    private static final long serialVersionUID = 1159655016005196442L;

    private String workOrderId; // 生产指令ID
    private String holdReasonCode; // 保留原因代码
    private String comment; // 保留备注
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiredReleaseTime; // 保留到期释放时间
    private String workcellId; // 工作单元ID
    private String locatorId; // 库位ID
    private String parentEventId; // 父事件ID
    private String eventRequestId; // 事件组ID
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate; // 日历日期
    private String shiftCode; // 班次编码

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getExpiredReleaseTime() {
        if (expiredReleaseTime == null) {
            return null;
        } else {
            return (Date) expiredReleaseTime.clone();
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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtWorkOrderRelDTO [workOrderId=");
        builder.append(workOrderId);
        builder.append(", holdReasonCode=");
        builder.append(holdReasonCode);
        builder.append(", comment=");
        builder.append(comment);
        builder.append(", expiredReleaseTime=");
        builder.append(expiredReleaseTime);
        builder.append(", workcellId=");
        builder.append(workcellId);
        builder.append(", locatorId=");
        builder.append(locatorId);
        builder.append(", parentEventId=");
        builder.append(parentEventId);
        builder.append(", eventRequestId=");
        builder.append(eventRequestId);
        builder.append(", shiftDate=");
        builder.append(shiftDate);
        builder.append(", shiftCode=");
        builder.append(shiftCode);
        builder.append("]");
        return builder.toString();
    }

}
