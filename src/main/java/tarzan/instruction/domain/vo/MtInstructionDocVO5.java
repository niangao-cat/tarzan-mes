package tarzan.instruction.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class MtInstructionDocVO5 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7055731090882179698L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;

    @ApiModelProperty(value = "单据编号")
    private String instructionDocNum;

    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    @ApiModelProperty(value = "单据状态")
    private String instructionDocStatus;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    @ApiModelProperty(value = "供应商地点ID")
    private String supplierSiteId;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "客户地点ID")
    private String customerSiteId;

    @ApiModelProperty(value = "来源ERP订单类型")
    private String sourceOrderType;

    @ApiModelProperty(value = "来源订单ID")
    private String sourceOrderId;

    @ApiModelProperty(value = "需求时间")
    private Date demandTime;

    @ApiModelProperty(value = "预计送达时间")
    private Date expectedArrivalTime;

    @ApiModelProperty(value = "成本中心或账户别名")
    private String costCenterId;

    @ApiModelProperty(value = "申请人/领料人")
    private Long personId;

    @ApiModelProperty(value = "实际业务需要的单据编号")
    private String identification;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "原因")
    private String reason;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "最后更新人")
    private Long lastUpdatedBy;

    private String fromParentLocatorId;

    private String toParentLocatorId;

    private List<MtInstructionVO2> instructionMessageList;

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

    public Date getExpectedArrivalTime() {
        if (expectedArrivalTime != null) {
            return (Date) expectedArrivalTime.clone();
        } else {
            return null;
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



    public Date getCreationDate() {
        if (creationDate != null) {
            return (Date) creationDate.clone();
        } else {
            return null;
        }
    }



    public void setCreationDate(Date creationDate) {
        if (creationDate == null) {
            this.creationDate = null;
        } else {
            this.creationDate = (Date) creationDate.clone();
        }
    }



    public Long getCreatedBy() {
        return createdBy;
    }



    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }



    public Date getLastUpdateDate() {
        if (lastUpdateDate != null) {
            return (Date) lastUpdateDate.clone();
        } else {
            return null;
        }
    }



    public void setLastUpdateDate(Date lastUpdateDate) {
        if (lastUpdateDate == null) {
            this.lastUpdateDate = null;
        } else {
            this.lastUpdateDate = (Date) lastUpdateDate.clone();
        }
    }



    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }



    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }



    public List<MtInstructionVO2> getInstructionMessageList() {
        return instructionMessageList;
    }



    public void setInstructionMessageList(List<MtInstructionVO2> instructionMessageList) {
        this.instructionMessageList = instructionMessageList;
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



}
