package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * instructionCreate-指令创建时使用VO类
 * 
 * @author benjamin
 * @date 2019-06-19 13:58
 */
public class MtInstructionVO implements Serializable {
    private static final long serialVersionUID = 5998306390226307279L;

    /**
     * 指令ID
     */
    private String instructionId;
    /**
     * 指令编号
     */
    private String instructionNum;
    /**
     * 指令移动类型
     */
    private String instructionType;
    /**
     * 单据id
     */
    private String sourceDocId;
    /**
     * 来源指令ID
     */
    private String sourceInstructionId;


    /**
     * 指令状态
     */
    private String instructionStatus;

    /**
     * 站点id
     */
    private String siteId;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 在制品id
     */
    private String eoId;
    /**
     * 配送路线
     */
    private String disRouterId;
    /**
     * 订单类型（EO, NOTIFICATION,STOCKTAKE)
     */
    private String orderType;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * ERP订单类型（PO，SO）
     */
    private String sourceOrderType;
    /**
     * ERP订单ID
     */
    private String sourceOrderId;
    /**
     * 订单行id
     */
    private String sourceOrderLineId;
    /**
     * 订单分配行id
     */
    private String sourceOrderLineDistributionId;
    /**
     * 订单分配行id（oracle的分配行id）
     */
    private String sourceOrderLineLocationId;
    /**
     * 外协组件行id
     */
    private String sourceOutsideComponentLineId;
    /**
     * 来源站点id
     */
    private String fromSiteId;
    /**
     * 目标站点id
     */
    private String toSiteId;
    /**
     * 来源库位id
     */
    private String fromLocatorId;
    /**
     * 目标库位id
     */
    private String toLocatorId;
    /**
     * 成本中心
     */
    private String costCenterId;
    /**
     * 指令数量
     */
    private Double quantity;
    /**
     * 单位
     */
    private String uomId;
    /**
     * 供应商（指令指定供应商时）
     */
    private String supplierId;
    /**
     * 供应商地点id
     */
    private String supplierSiteId;
    /**
     * 客户（指令指定客户）
     */
    private String customerId;
    /**
     * 客户地点id
     */
    private String customerSiteId;
    /**
     * 需求日期
     */
    private Date demandTime;
    /**
     * 波次
     */
    private String waveSequence;
    /**
     * 班次日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    /**
     * 班次代码
     */
    private String shiftCode;
    /**
     * 覆盖数量
     */
    private Double coverQty;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 实际业务需要的单据编号
     */
    private String identification;
    /**
     * 库存形态
     */
    private String fromOwnerType;
    /**
     * 目标库存形
     */
    private String toOwnerType;
    /**
     * 父事件ID 创建事件使用
     */
    private String parentEventId;
    /**
     * 事件组ID 创建事件使用
     */
    private String eventRequestId;
    /**
     * 编码参数列表
     */
    private List<String> numIncomingValueList;

    /**
     * 事件ID
     */
    private String eventId;
    /**
     * 料废调换数量
     */
    private Double exchangeQty;
    /**
     * 已料废调换数量
     */
    private Double exchangegQty;
    /**
     * 料废调换标识
     */
    private String exchangeFlag;
    /**
     * 特采标识
     */
    private String uaiFlag;

    public Double getExchangegQty() {
        return exchangegQty;
    }

    public void setExchangegQty(Double exchangegQty) {
        this.exchangegQty = exchangegQty;
    }

    public Double getExchangeQty() {
        return exchangeQty;
    }

    public void setExchangeQty(Double exchangeQty) {
        this.exchangeQty = exchangeQty;
    }

    public String getExchangeFlag() {
        return exchangeFlag;
    }

    public void setExchangeFlag(String exchangeFlag) {
        this.exchangeFlag = exchangeFlag;
    }

    public String getUaiFlag() {
        return uaiFlag;
    }

    public void setUaiFlag(String uaiFlag) {
        this.uaiFlag = uaiFlag;
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

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getSourceDocId() {
        return sourceDocId;
    }

    public void setSourceDocId(String sourceDocId) {
        this.sourceDocId = sourceDocId;
    }

    public String getSourceInstructionId() {
        return sourceInstructionId;
    }

    public void setSourceInstructionId(String sourceInstructionId) {
        this.sourceInstructionId = sourceInstructionId;
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

    public String getSourceOrderLineDistributionId() {
        return sourceOrderLineDistributionId;
    }

    public String getSourceOrderLineLocationId() {
        return sourceOrderLineLocationId;
    }

    public void setSourceOrderLineLocationId(String sourceOrderLineLocationId) {
        this.sourceOrderLineLocationId = sourceOrderLineLocationId;
    }

    public void setSourceOrderLineDistributionId(String sourceOrderLineDistributionId) {
        this.sourceOrderLineDistributionId = sourceOrderLineDistributionId;
    }

    public String getSourceOutsideComponentLineId() {
        return sourceOutsideComponentLineId;
    }

    public void setSourceOutsideComponentLineId(String sourceOutsideComponentLineId) {
        this.sourceOutsideComponentLineId = sourceOutsideComponentLineId;
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

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
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
        if (demandTime == null) {
            return null;
        }
        return (Date) demandTime.clone();
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

        if (shiftDate == null) {
            return null;
        }
        return (Date) shiftDate.clone();
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

    public List<String> getNumIncomingValueList() {
        return numIncomingValueList;
    }

    public void setNumIncomingValueList(List<String> numIncomingValueList) {
        this.numIncomingValueList = numIncomingValueList;
    }

    public String getInstructionStatus() {
        return instructionStatus;
    }

    public void setInstructionStatus(String instructionStatus) {
        this.instructionStatus = instructionStatus;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "MtInstructionVO{" +
                "instructionId='" + instructionId + '\'' +
                ", instructionNum='" + instructionNum + '\'' +
                ", instructionType='" + instructionType + '\'' +
                ", sourceDocId='" + sourceDocId + '\'' +
                ", sourceInstructionId='" + sourceInstructionId + '\'' +
                ", instructionStatus='" + instructionStatus + '\'' +
                ", siteId='" + siteId + '\'' +
                ", materialId='" + materialId + '\'' +
                ", eoId='" + eoId + '\'' +
                ", disRouterId='" + disRouterId + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderId='" + orderId + '\'' +
                ", sourceOrderType='" + sourceOrderType + '\'' +
                ", sourceOrderId='" + sourceOrderId + '\'' +
                ", sourceOrderLineId='" + sourceOrderLineId + '\'' +
                ", sourceOrderLineDistributionId='" + sourceOrderLineDistributionId + '\'' +
                ", sourceOrderLineLocationId='" + sourceOrderLineLocationId + '\'' +
                ", sourceOutsideComponentLineId='" + sourceOutsideComponentLineId + '\'' +
                ", fromSiteId='" + fromSiteId + '\'' +
                ", toSiteId='" + toSiteId + '\'' +
                ", fromLocatorId='" + fromLocatorId + '\'' +
                ", toLocatorId='" + toLocatorId + '\'' +
                ", costCenterId='" + costCenterId + '\'' +
                ", quantity=" + quantity +
                ", uomId='" + uomId + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", supplierSiteId='" + supplierSiteId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerSiteId='" + customerSiteId + '\'' +
                ", demandTime=" + demandTime +
                ", waveSequence='" + waveSequence + '\'' +
                ", shiftDate=" + shiftDate +
                ", shiftCode='" + shiftCode + '\'' +
                ", coverQty=" + coverQty +
                ", businessType='" + businessType + '\'' +
                ", remark='" + remark + '\'' +
                ", identification='" + identification + '\'' +
                ", fromOwnerType='" + fromOwnerType + '\'' +
                ", toOwnerType='" + toOwnerType + '\'' +
                ", parentEventId='" + parentEventId + '\'' +
                ", eventRequestId='" + eventRequestId + '\'' +
                ", numIncomingValueList=" + numIncomingValueList +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
