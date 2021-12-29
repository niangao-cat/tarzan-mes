package tarzan.instruction.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MtInstructionDocDTO2 implements Serializable {
    private static final long serialVersionUID = -4891266741876391054L;

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
    private Date demandTime;
    private Date expectedArrivalTime;
    private String costCenterId;
    private Long personId;
    private String identification;
    private String remark;
    private String reason;
    private String eventId;
    private String eventRequestId;
    private String mark;
    private List<String> numIncomingValueList;

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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

    public Date getDemandTime() {
        if (demandTime == null) {
            return null;
        } else {
            return (Date) demandTime.clone();
        }
    }

    public void setDemandTime(Date demandTime) {
        if (demandTime == null) {
            this.demandTime = null;
        } else {
            this.demandTime = (Date) demandTime.clone();
        }
    }

    public Date getExpectedArrivalTime() {
        if (expectedArrivalTime == null) {
            return null;
        } else {
            return (Date) expectedArrivalTime.clone();
        }
    }

    public void setExpectedArrivalTime(Date expectedArrivalTime) {
        if (expectedArrivalTime == null) {
            this.expectedArrivalTime = null;
        } else {
            this.expectedArrivalTime = (Date) expectedArrivalTime.clone();
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public List<String> getNumIncomingValueList() {
        return numIncomingValueList;
    }

    public void setNumIncomingValueList(List<String> numIncomingValueList) {
        this.numIncomingValueList = numIncomingValueList;
    }
}
