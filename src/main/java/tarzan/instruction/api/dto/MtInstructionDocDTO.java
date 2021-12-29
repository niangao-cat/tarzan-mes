package tarzan.instruction.api.dto;

import java.io.Serializable;
import java.util.Date;

public class MtInstructionDocDTO implements Serializable {
    private static final long serialVersionUID = -1286031916759380868L;

    private String instructionDocId;
    private String instructionDocNum;
    private String instructionDocType;
    private String instructionDocStatus;
    private String siteId;
    private String supplierId;
    private String supplierSiteId;
    private String customerId;
    private String customerSiteId;
    private String sourceOrderType;
    private String sourceOrderId;
    private Date demandTimeFrom;
    private Date demandTimeTo;
    private Date expectedArrivalTimeFrom;
    private Date expectedArrivalTimeTo;
    private String costCenterId;
    private Long personId;
    private String identification;
    private String remark;
    private String reason;

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }

    public String getInstructionDocNum() {
        return instructionDocNum;
    }

    public void setInstructionDocNum(String instructionDocNum) {
        this.instructionDocNum = instructionDocNum;
    }

    public String getInstructionDocType() {
        return instructionDocType;
    }

    public void setInstructionDocType(String instructionDocType) {
        this.instructionDocType = instructionDocType;
    }

    public String getInstructionDocStatus() {
        return instructionDocStatus;
    }

    public void setInstructionDocStatus(String instructionDocStatus) {
        this.instructionDocStatus = instructionDocStatus;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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

    public Date getExpectedArrivalTimeFrom() {
        if (expectedArrivalTimeFrom == null) {
            return null;
        } else {
            return (Date) expectedArrivalTimeFrom.clone();
        }
    }

    public void setExpectedArrivalTimeFrom(Date expectedArrivalTimeFrom) {
        if (expectedArrivalTimeFrom == null) {
            this.expectedArrivalTimeFrom = null;
        } else {
            this.expectedArrivalTimeFrom = (Date) expectedArrivalTimeFrom.clone();
        }
    }

    public Date getExpectedArrivalTimeTo() {
        if (expectedArrivalTimeTo == null) {
            return null;
        } else {
            return (Date) expectedArrivalTimeTo.clone();
        }
    }

    public void setExpectedArrivalTimeTo(Date expectedArrivalTimeTo) {
        if (expectedArrivalTimeTo == null) {
            this.expectedArrivalTimeTo = null;
        } else {
            this.expectedArrivalTimeTo = (Date) expectedArrivalTimeTo.clone();
        }
    }

    public String getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(String costCenterId) {
        this.costCenterId = costCenterId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "MtInstructionDocDTO{" +
                "instructionDocId='" + instructionDocId + '\'' +
                ", instructionDocNum='" + instructionDocNum + '\'' +
                ", instructionDocType='" + instructionDocType + '\'' +
                ", instructionDocStatus='" + instructionDocStatus + '\'' +
                ", siteId='" + siteId + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", supplierSiteId='" + supplierSiteId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerSiteId='" + customerSiteId + '\'' +
                ", sourceOrderType='" + sourceOrderType + '\'' +
                ", sourceOrderId='" + sourceOrderId + '\'' +
                ", demandTimeFrom=" + demandTimeFrom +
                ", demandTimeTo=" + demandTimeTo +
                ", expectedArrivalTimeFrom=" + expectedArrivalTimeFrom +
                ", expectedArrivalTimeTo=" + expectedArrivalTimeTo +
                ", costCenterId='" + costCenterId + '\'' +
                ", personId=" + personId +
                ", identification='" + identification + '\'' +
                ", remark='" + remark + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
