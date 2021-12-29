package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtMaterialLotVO1 implements Serializable {

    private static final long serialVersionUID = 7003617403810787987L;

    private String materialLotId;
    private String primaryUomId;
    private Double trxPrimaryUomQty;
    private String secondaryUomId;
    private Double trxSecondaryUomQty;
    private String instructionDocId;
    private String workcellId;
    private String parentEventId;
    private String eventRequestId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public Double getTrxPrimaryUomQty() {
        return trxPrimaryUomQty;
    }

    public void setTrxPrimaryUomQty(Double trxPrimaryUomQty) {
        this.trxPrimaryUomQty = trxPrimaryUomQty;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    public Double getTrxSecondaryUomQty() {
        return trxSecondaryUomQty;
    }

    public void setTrxSecondaryUomQty(Double trxSecondaryUomQty) {
        this.trxSecondaryUomQty = trxSecondaryUomQty;
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

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
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
