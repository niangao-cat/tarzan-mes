package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class MtEoBatchChangeHistoryVO implements Serializable {

    private static final long serialVersionUID = -649560894730714963L;
    @ApiModelProperty("目标执行作业")
    private String targetEoId;
    @ApiModelProperty("变更原因")
    private String reason;
    @ApiModelProperty("变更时间")
    private String creationDate;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("顺序")
    private Long sequence;
    @ApiModelProperty("执行作业变更数量")
    private Double trxQty;
    @ApiModelProperty("目标执行作业变更数量")
    private Double targetTrxQty;
    @ApiModelProperty("来源EO步骤实际")
    private String sourceEoStepActualId;
    @ApiModelProperty("事件记录创建时间")
    private Date eventTime;
    @ApiModelProperty("事件记录创建人")
    private Long eventBy;

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

    public Double getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(Double trxQty) {
        this.trxQty = trxQty;
    }

    public Double getTargetTrxQty() {
        return targetTrxQty;
    }

    public void setTargetTrxQty(Double targetTrxQty) {
        this.targetTrxQty = targetTrxQty;
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
