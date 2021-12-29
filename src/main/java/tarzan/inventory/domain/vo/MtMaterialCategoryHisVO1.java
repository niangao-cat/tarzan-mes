package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Leeloing
 * @date 2019/4/8 17:26
 */
public class MtMaterialCategoryHisVO1 implements Serializable {

    private static final long serialVersionUID = 8459644015816242254L;

    private String sourceMaterialLotId;
    private String targetMaterialLotId;
    private String reason;
    private Date eventTime;
    private String eventId;
    private Long sequence;
    private Double sourceTrxQty;
    private Double trxQty;

    public String getSourceMaterialLotId() {
        return sourceMaterialLotId;
    }

    public void setSourceMaterialLotId(String sourceMaterialLotId) {
        this.sourceMaterialLotId = sourceMaterialLotId;
    }

    public String getTargetMaterialLotId() {
        return targetMaterialLotId;
    }

    public void setTargetMaterialLotId(String targetMaterialLotId) {
        this.targetMaterialLotId = targetMaterialLotId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Double getSourceTrxQty() {
        return sourceTrxQty;
    }

    public void setSourceTrxQty(Double sourceTrxQty) {
        this.sourceTrxQty = sourceTrxQty;
    }

    public Double getTrxQty() {
        return trxQty;
    }

    public void setTrxQty(Double trxQty) {
        this.trxQty = trxQty;
    }
}
