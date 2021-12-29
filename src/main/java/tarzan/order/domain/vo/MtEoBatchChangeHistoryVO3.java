package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class MtEoBatchChangeHistoryVO3 implements Serializable {

    private static final long serialVersionUID = -2375469204269632303L;

    @ApiModelProperty(value = "来源执行作业")
    private String sourceEoId;
    @ApiModelProperty(value = "目标指定作业")
    private String targetEoId;
    @ApiModelProperty(value = "变更原因")
    private String reason;
    @ApiModelProperty(value = "变更时间")
    private String creationDate;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "顺序")
    private Long sequence;
    @ApiModelProperty(value = "来源EO步骤实际")
    private String sourceEoStepActualId;
    @ApiModelProperty(value = "事件记录创建时间")
    private Date eventTime;
    @ApiModelProperty(value = "事件记录创建人")
    private Long eventBy;

    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
    }

    public String getTargetEoId() {
        return targetEoId;
    }

    public void setTargetEoId(String targetEoId) {
        this.targetEoId = targetEoId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public Date getEventTime() {
        if (eventTime != null) {
            return (Date) eventTime.clone();
        } else {
            return null;
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
}
