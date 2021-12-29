package tarzan.actual.api.dto;

import java.io.Serializable;

public class MtInstructionActualDTO2 implements Serializable {

    private static final long serialVersionUID = 4152759382001608393L;
    private String actualId;
    private String instructionId;
    private String instructionType;
    private String businessType;
    private String materialId;
    private String uomId;
    private String eoId;
    private String sourceOrderType;
    private String sourceOrderId;
    private String fromSiteId;
    private String toSiteId;
    private String fromLocatorId;
    private String toLocatorId;
    private String costCenterId;
    private String fromOwnerType;
    private String toOwnerType;
    private String supplierId;
    private String supplierSiteId;
    private String customerId;
    private String customerSiteId;
    private Double actualQty;

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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

    public Double getActualQty() {
        return actualQty;
    }

    public void setActualQty(Double actualQty) {
        this.actualQty = actualQty;
    }
}
