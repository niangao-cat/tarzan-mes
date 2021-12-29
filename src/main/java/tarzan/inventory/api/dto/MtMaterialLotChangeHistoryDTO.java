package tarzan.inventory.api.dto;

import java.io.Serializable;

public class MtMaterialLotChangeHistoryDTO implements Serializable {
    private static final long serialVersionUID = -7983683092202233791L;


    private String materialLotId;

    private String sourceMaterialLotId;

    private String reason;

    private Long sequence;

    private String eventId;

    private Double trxQty;

    private Double sourceTrxQty;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getSourceMaterialLotId() {
        return sourceMaterialLotId;
    }

    public void setSourceMaterialLotId(String sourceMaterialLotId) {
        this.sourceMaterialLotId = sourceMaterialLotId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    @Override
    public String toString() {
        return "MtMaterialLotChangeHistoryDTO{" +
                "materialLotId='" + materialLotId + '\'' +
                ", sourceMaterialLotId='" + sourceMaterialLotId + '\'' +
                ", reason='" + reason + '\'' +
                ", sequence=" + sequence +
                ", eventId='" + eventId + '\'' +
                ", trxQty=" + trxQty +
                ", sourceTrxQty=" + sourceTrxQty +
                '}';
    }
}
