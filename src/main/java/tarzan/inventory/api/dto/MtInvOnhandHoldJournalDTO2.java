package tarzan.inventory.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 库存预留日记帐查询 返回结果DTO
 * 
 * @author benjamin
 */
public class MtInvOnhandHoldJournalDTO2 implements Serializable {
    private static final long serialVersionUID = -1038091462618525501L;

    @ApiModelProperty("主键ID")
    private String onhandHoldJournalId;
    @ApiModelProperty(value = "库存保留主键ID")
    private String onhandHoldId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialDesc;
    @ApiModelProperty(value = "库位ID")
    private String locatorId;
    @ApiModelProperty(value = "库位编码")
    private String locatorCode;
    @ApiModelProperty(value = "库位描述")
    private String locatorDesc;
    @ApiModelProperty(value = "批次CODE")
    private String lotCode;
    @ApiModelProperty(value = "保留数量")
    private Double holdQuantity;
    @ApiModelProperty(value = "预留类型（手工/指令）")
    private String holdType;
    @ApiModelProperty(value = "预留类型描述")
    private String holdTypeDesc;
    @ApiModelProperty(value = "指令类型")
    private String orderType;
    @ApiModelProperty(value = "指令类型描述")
    private String orderTypeDesc;
    @ApiModelProperty(value = "指令ID")
    private String orderId;
    @ApiModelProperty(value = "库存变化数量")
    private Double changeQuantity;
    @ApiModelProperty(value = "所有者类型")
    private String ownerType;
    @ApiModelProperty(value = "所有者类型描述")
    private String ownerTypeDesc;
    @ApiModelProperty(value = "所有者ID（客户ID或供应商ID）")
    private String ownerId;
    @ApiModelProperty(value = "所有者编码")
    private String ownerCode;
    @ApiModelProperty(value = "所有者描述")
    private String ownerDesc;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "事件类型")
    private String eventType;
    @ApiModelProperty(value = "事件类型描述")
    private String eventTypeDesc;
    @ApiModelProperty(value = "事件时间")
    private Date eventTime;
    @ApiModelProperty(value = "创建人")
    private Long eventBy;
    @ApiModelProperty(value = "创建人名称")
    private String eventByUserName;

    public String getOnhandHoldJournalId() {
        return onhandHoldJournalId;
    }

    public void setOnhandHoldJournalId(String onhandHoldJournalId) {
        this.onhandHoldJournalId = onhandHoldJournalId;
    }

    public String getOnhandHoldId() {
        return onhandHoldId;
    }

    public void setOnhandHoldId(String onhandHoldId) {
        this.onhandHoldId = onhandHoldId;
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

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorDesc() {
        return locatorDesc;
    }

    public void setLocatorDesc(String locatorDesc) {
        this.locatorDesc = locatorDesc;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public Double getHoldQuantity() {
        return holdQuantity;
    }

    public void setHoldQuantity(Double holdQuantity) {
        this.holdQuantity = holdQuantity;
    }

    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    public String getHoldTypeDesc() {
        return holdTypeDesc;
    }

    public void setHoldTypeDesc(String holdTypeDesc) {
        this.holdTypeDesc = holdTypeDesc;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeDesc() {
        return orderTypeDesc;
    }

    public void setOrderTypeDesc(String orderTypeDesc) {
        this.orderTypeDesc = orderTypeDesc;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getChangeQuantity() {
        return changeQuantity;
    }

    public void setChangeQuantity(Double changeQuantity) {
        this.changeQuantity = changeQuantity;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerTypeDesc() {
        return ownerTypeDesc;
    }

    public void setOwnerTypeDesc(String ownerTypeDesc) {
        this.ownerTypeDesc = ownerTypeDesc;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerDesc() {
        return ownerDesc;
    }

    public void setOwnerDesc(String ownerDesc) {
        this.ownerDesc = ownerDesc;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTypeDesc() {
        return eventTypeDesc;
    }

    public void setEventTypeDesc(String eventTypeDesc) {
        this.eventTypeDesc = eventTypeDesc;
    }

    public Date getEventTime() {
        if (eventTime == null) {
            return null;
        } else {
            return (Date) eventTime.clone();
        }
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

    public String getEventByUserName() {
        return eventByUserName;
    }

    public void setEventByUserName(String eventByUserName) {
        this.eventByUserName = eventByUserName;
    }
}
