package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtEoBatchChangeHistoryVO5 implements Serializable {

    private static final long serialVersionUID = -8135076687732301376L;

    private String targetEoId;

    private String sourceEoId;

    private String reason;

    private Date creationDate;

    private String eventId;

    private Long sequence;

    private Double trxQty;

    private Double targetTrxQty;

    private String sourceEoStepActualId;

    private Date eventTime;

    private Long eventBy;

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

    public Date getCreationDate() {
        return creationDate == null ? null : (Date) creationDate.clone();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate == null ? null : (Date) creationDate.clone();
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }

    public Date getEventTime() {
        return eventTime == null ? null : (Date) eventTime.clone();
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime == null ? null : (Date) eventTime.clone();
    }

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

    public String getTargetEoId() {
        return targetEoId;
    }

    public void setTargetEoId(String targetEoId) {
        this.targetEoId = targetEoId;
    }

    public Double getTargetTrxQty() {
        return targetTrxQty;
    }

    public void setTargetTrxQty(Double targetTrxQty) {
        this.targetTrxQty = targetTrxQty;
    }

}

