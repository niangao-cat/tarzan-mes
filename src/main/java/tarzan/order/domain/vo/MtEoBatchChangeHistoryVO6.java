package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.Date;

public class MtEoBatchChangeHistoryVO6 implements Serializable {

    private static final long serialVersionUID = -4622378113995989661L;

    private String eoId;

    private String sourceEoId;

    private String reason;

    private Date creationDate;

    private String eventId;

    private Long sequence;

    private Double trxQty;

    private Double sourceTrxQty;

    private String sourceEoStepActualId;

    private Date eventTime;

    private Long eventBy;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

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

    public Date getCreationDate() {
        return creationDate == null ? null : (Date) creationDate.clone();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate == null ? null : (Date) creationDate.clone();
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


}
