package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class MtEoBatchChangeHistoryVO2 implements Serializable {

    private static final long serialVersionUID = 6878823735208374932L;

    @ApiModelProperty(value = "来源执行作业")
    private String sourceEoId;
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
    @ApiModelProperty(value = "执行作业变更数量")
    private Double trxQty;
    @ApiModelProperty(value = "来源执行作业变更数量")
    private Double sourceTrxQty;

    public String getSourceEoId() {
        return sourceEoId;
    }

    public void setSourceEoId(String sourceEoId) {
        this.sourceEoId = sourceEoId;
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

    public Double getSourceTrxQty() {
        return sourceTrxQty;
    }

    public void setSourceTrxQty(Double sourceTrxQty) {
        this.sourceTrxQty = sourceTrxQty;
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
