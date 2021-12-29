package tarzan.instruction.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class MtInstructionHisDTO implements Serializable {
    private static final long serialVersionUID = -6048570007693795487L;

    private String instructionHisId;
    private String eventId;
    private String instructionId;
    private String instructionNum;
    private String sourceInstructionId;
    private String sourceDocId;
    private String instructionType;
    private String instructionStatus;
    private String siteId;
    private String materialId;
    private String uomId;
    private String eoId;
    private String disRouterId;
    private String orderType;
    private String orderId;
    private String sourceOrderType;
    private String sourceOrderId;
    private String sourceOrderLineId;
    private String sourceOrderLineLocationId;
    private String sourceOrderLineDistId;
    private String sourceOutsideCompLineId;
    private String fromSiteId;
    private String toSiteId;
    private String fromLocatorId;
    private String toLocatorId;
    private String costCenterId;
    private Double quantity;
    private String supplierId;
    private String supplierSiteId;
    private String customerId;
    private String customerSiteId;
    private Date demandTimeFrom;
    private Date demandTimeTo;
    private String waveSequence;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateFrom;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDateTo;
    private String shiftCode;
    private Double coverQty;
    private String businessType;
    private String remark;
    private String identification;
    private String fromOwnerType;
    private String toOwnerType;

    public String getInstructionHisId() {
        return instructionHisId;
    }

    public void setInstructionHisId(String instructionHisId) {
        this.instructionHisId = instructionHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getInstructionNum() {
        return instructionNum;
    }

    public void setInstructionNum(String instructionNum) {
        this.instructionNum = instructionNum;
    }

    public String getSourceInstructionId() {
        return sourceInstructionId;
    }

    public void setSourceInstructionId(String sourceInstructionId) {
        this.sourceInstructionId = sourceInstructionId;
    }

    public String getSourceDocId() {
        return sourceDocId;
    }

    public void setSourceDocId(String sourceDocId) {
        this.sourceDocId = sourceDocId;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getInstructionStatus() {
        return instructionStatus;
    }

    public void setInstructionStatus(String instructionStatus) {
        this.instructionStatus = instructionStatus;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getDisRouterId() {
        return disRouterId;
    }

    public void setDisRouterId(String disRouterId) {
        this.disRouterId = disRouterId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSourceOrderType() {
        return sourceOrderType;
    }

    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }

    public String getSourceOrderId() {
        return sourceOrderId;
    }

    public void setSourceOrderId(String sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }

    public String getSourceOrderLineId() {
        return sourceOrderLineId;
    }

    public void setSourceOrderLineId(String sourceOrderLineId) {
        this.sourceOrderLineId = sourceOrderLineId;
    }

    public String getSourceOrderLineLocationId() {
        return sourceOrderLineLocationId;
    }

    public void setSourceOrderLineLocationId(String sourceOrderLineLocationId) {
        this.sourceOrderLineLocationId = sourceOrderLineLocationId;
    }

    public String getSourceOrderLineDistId() {
        return sourceOrderLineDistId;
    }

    public void setSourceOrderLineDistId(String sourceOrderLineDistId) {
        this.sourceOrderLineDistId = sourceOrderLineDistId;
    }

    public String getSourceOutsideCompLineId() {
        return sourceOutsideCompLineId;
    }

    public void setSourceOutsideCompLineId(String sourceOutsideCompLineId) {
        this.sourceOutsideCompLineId = sourceOutsideCompLineId;
    }

    public String getFromSiteId() {
        return fromSiteId;
    }

    public void setFromSiteId(String fromSiteId) {
        this.fromSiteId = fromSiteId;
    }

    public String getToSiteId() {
        return toSiteId;
    }

    public void setToSiteId(String toSiteId) {
        this.toSiteId = toSiteId;
    }

    public String getFromLocatorId() {
        return fromLocatorId;
    }

    public void setFromLocatorId(String fromLocatorId) {
        this.fromLocatorId = fromLocatorId;
    }

    public String getToLocatorId() {
        return toLocatorId;
    }

    public void setToLocatorId(String toLocatorId) {
        this.toLocatorId = toLocatorId;
    }

    public String getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        this.costCenterId = costCenterId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerSiteId() {
        return customerSiteId;
    }

    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    public Date getDemandTimeFrom() {
        if (demandTimeFrom == null) {
            return null;
        } else {
            return (Date) demandTimeFrom.clone();
        }
    }

    public void setDemandTimeFrom(Date demandTimeFrom) {
        if (demandTimeFrom == null) {
            this.demandTimeFrom = null;
        } else {
            this.demandTimeFrom = (Date) demandTimeFrom.clone();
        }
    }

    public Date getDemandTimeTo() {
        if (demandTimeTo == null) {
            return null;
        } else {
            return (Date) demandTimeTo.clone();
        }
    }

    public void setDemandTimeTo(Date demandTimeTo) {
        if (demandTimeTo == null) {
            this.demandTimeTo = null;
        } else {
            this.demandTimeTo = (Date) demandTimeTo.clone();
        }
    }

    public String getWaveSequence() {
        return waveSequence;
    }

    public void setWaveSequence(String waveSequence) {
        this.waveSequence = waveSequence;
    }

    public Date getShiftDateFrom() {
        if (shiftDateFrom == null) {
            return null;
        } else {
            return (Date) shiftDateFrom.clone();
        }
    }

    public void setShiftDateFrom(Date shiftDateFrom) {
        if (shiftDateFrom == null) {
            this.shiftDateFrom = null;
        } else {
            this.shiftDateFrom = (Date) shiftDateFrom.clone();
        }
    }

    public Date getShiftDateTo() {
        if (shiftDateTo == null) {
            return null;
        } else {
            return (Date) shiftDateTo.clone();
        }
    }

    public void setShiftDateTo(Date shiftDateTo) {
        if (shiftDateTo == null) {
            this.shiftDateTo = null;
        } else {
            this.shiftDateTo = (Date) shiftDateTo.clone();
        }
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Double getCoverQty() {
        return coverQty;
    }

    public void setCoverQty(Double coverQty) {
        this.coverQty = coverQty;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getFromOwnerType() {
        return fromOwnerType;
    }

    public void setFromOwnerType(String fromOwnerType) {
        this.fromOwnerType = fromOwnerType;
    }

    public String getToOwnerType() {
        return toOwnerType;
    }

    public void setToOwnerType(String toOwnerType) {
        this.toOwnerType = toOwnerType;
    }
}
