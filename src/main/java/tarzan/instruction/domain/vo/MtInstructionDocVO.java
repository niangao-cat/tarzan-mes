package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * instructionDocUpdate-更新时使用VO类
 * 
 * @author benjamin
 * @date 2019-06-19 09:34
 */
public class MtInstructionDocVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2276376643087338546L;
    /**
     * 主键Id
     */
    private String instructionDocId;
    /**
     * 指令单据状态
     */
    private String instructionDocStatus;

    private String instructionDocType;
    private String siteId;
    private String supplierId;
    private String supplierSiteId;
    private String customerId;
    private String customerSiteId;
    private String sourceOrderType;
    private String sourceOrderId;
    private Date expectedArrivalTime;
    private Date demandTime;
    private String costCenterId;
    private Long personId;

    /**
     * 备注
     */
    private String remark;
    /**
     * 原因
     */
    private String reason;
    /**
     * 事件Id
     */
    private String eventId;
    /**
     * 事件时间
     */
    private Date eventTime;
    /**
     * 事件记录创建人
     */
    private Long eventBy;

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }

    public String getInstructionDocStatus() {
        return instructionDocStatus;
    }

    public void setInstructionDocStatus(String instructionDocStatus) {
        this.instructionDocStatus = instructionDocStatus;
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

    public Date getEventTime() {
        if (eventTime == null) {
            return null;
        }
        return (Date) eventTime.clone();
    }

    public void setEventTime(Date eventTime) {
        if (eventTime == null) {
            this.eventTime = null;
        } else {
            this.eventTime = (Date) eventTime.clone();
        }
    }

    public Long getEventBy() {
        return eventBy;
    }

    public void setEventBy(Long eventBy) {
        this.eventBy = eventBy;
    }

    public String getInstructionDocType() {
        return instructionDocType;
    }

    public void setInstructionDocType(String instructionDocType) {
        this.instructionDocType = instructionDocType;
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

    public void setDemandTime(Date demandTime) {
        if (demandTime == null) {
            this.demandTime = null;
        } else {
            this.demandTime = (Date) demandTime.clone();
        }

    }

    public Date getDemandTime() {
        if (demandTime == null) {
            return null;
        }
        return (Date) demandTime.clone();
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

    @Override
    public String toString() {
        return "MtInstructionDocVO{" +
                "instructionDocId='" + instructionDocId + '\'' +
                ", instructionDocStatus='" + instructionDocStatus + '\'' +
                ", instructionDocType='" + instructionDocType + '\'' +
                ", siteId='" + siteId + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", supplierSiteId='" + supplierSiteId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerSiteId='" + customerSiteId + '\'' +
                ", sourceOrderType='" + sourceOrderType + '\'' +
                ", sourceOrderId='" + sourceOrderId + '\'' +
                ", expectedArrivalTime=" + expectedArrivalTime +
                ", demandTime=" + demandTime +
                ", costCenterId='" + costCenterId + '\'' +
                ", personId=" + personId +
                ", remark='" + remark + '\'' +
                ", reason='" + reason + '\'' +
                ", eventId='" + eventId + '\'' +
                ", eventTime=" + eventTime +
                ", eventBy=" + eventBy +
                '}';
    }
}
