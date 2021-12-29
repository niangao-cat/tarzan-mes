package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MtInstructionVO11 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8082630548877479483L;
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
    private String fromParentLocatorId;
    private String fromLocatorId;
    private String toParentLocatorId;
    private String toLocatorId;
    private String costCenterId;
    private Double quantity;
    private String supplierId;
    private String supplierSiteId;
    private String customerId;
    private String customerSiteId;
    private Date demandTime;
    private String waveSequence;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    private String shiftCode;
    private Double coverQty;
    private String businessType;
    private String remark;
    private String identification;
    private String fromOwnerType;
    private String toOwnerType;
    private Double executableQty;
    private List<InstructionActualLine> instructionActualLines;

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

    public Date getDemandTime() {
        if (demandTime != null) {
            return (Date) demandTime.clone();
        } else {
            return null;
        }
    }

    public void setDemandTime(Date demandTime) {
        if (demandTime == null) {
            this.demandTime = null;
        } else {
            this.demandTime = (Date) demandTime.clone();
        }
    }

    public String getWaveSequence() {
        return waveSequence;
    }

    public void setWaveSequence(String waveSequence) {
        this.waveSequence = waveSequence;
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

    public Double getExecutableQty() {
        return executableQty;
    }

    public void setExecutableQty(Double executableQty) {
        this.executableQty = executableQty;
    }

    public List<InstructionActualLine> getInstructionActualLines() {
        return instructionActualLines;
    }

    public void setInstructionActualLines(List<InstructionActualLine> instructionActualLines) {
        this.instructionActualLines = instructionActualLines;
    }

    public String getFromParentLocatorId() {
        return fromParentLocatorId;
    }

    public void setFromParentLocatorId(String fromParentLocatorId) {
        this.fromParentLocatorId = fromParentLocatorId;
    }

    public String getToParentLocatorId() {
        return toParentLocatorId;
    }

    public void setToParentLocatorId(String toParentLocatorId) {
        this.toParentLocatorId = toParentLocatorId;
    }


    public static class InstructionActualLine implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 7727700190440170338L;
        private String actualId;
        private String sourceOrderType;
        private String sourceOrderId;
        private Double actualQty;

        public String getActualId() {
            return actualId;
        }

        public void setActualId(String actualId) {
            this.actualId = actualId;
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

        public Double getActualQty() {
            return actualQty;
        }

        public void setActualQty(Double actualQty) {
            this.actualQty = actualQty;
        }


    }
}
