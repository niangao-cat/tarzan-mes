package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtContainerVO16 implements Serializable {
    private static final long serialVersionUID = 681123089545623389L;

    private String containerId; // 容器ID
    private String releaseComment; // 保留释放的备注
    private String releaseReasonCode; // 保留释放的原因代码
    private String targetStatus; // 目标状态
    private String workcellId; // 工作单元
    private String parentEventId; // 父事件ID
    private String eventRequestId; // 事件组ID
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate; // 日历日期
    private String shiftCode; // 班次编码

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getReleaseComment() {
        return releaseComment;
    }

    public void setReleaseComment(String releaseComment) {
        this.releaseComment = releaseComment;
    }

    public String getReleaseReasonCode() {
        return releaseReasonCode;
    }

    public void setReleaseReasonCode(String releaseReasonCode) {
        this.releaseReasonCode = releaseReasonCode;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(String targetStatus) {
        this.targetStatus = targetStatus;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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
        if (shiftDate != null) {
            return (Date) shiftDate.clone();
        } else {
            return null;
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
}
