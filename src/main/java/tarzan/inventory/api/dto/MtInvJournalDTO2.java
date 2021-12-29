package tarzan.inventory.api.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 库存日记账-查询 返回参数DTO
 * 
 * @author benjamin
 */
public class MtInvJournalDTO2 implements Serializable {
    private static final long serialVersionUID = 6868169726473379434L;

    @ApiModelProperty("日记账ID")
    private String journalId;
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
    @ApiModelProperty(value = "库存现有量值")
    private Double onhandQuantity;
    @ApiModelProperty(value = "批次CODE")
    private String lotCode;
    @ApiModelProperty(value = "库存变化数量")
    private Double changeQuantity;
    @ApiModelProperty(value = "所有者类型")
    private String ownerType;
    @ApiModelProperty(value = "所有者ID（客户ID或供应商ID）")
    private String ownerId;
    @ApiModelProperty(value = "所有者编码")
    private String ownerCode;
    @ApiModelProperty(value = "所有者类型描述")
    private String ownerTypeDesc;
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
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
    @ApiModelProperty(value = "仓库描述")
    private String warehouseDesc;
    @ApiModelProperty(value = "仓库id")
    private String warehouseId;
    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    public String getJournalId() {
        return journalId;
    }

    public void setJournalId(String journalId) {
        this.journalId = journalId;
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

    public Double getOnhandQuantity() {
        return onhandQuantity;
    }

    public void setOnhandQuantity(Double onhandQuantity) {
        this.onhandQuantity = onhandQuantity;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
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

    public String getOwnerTypeDesc() {
        return ownerTypeDesc;
    }

    public void setOwnerTypeDesc(String ownerTypeDesc) {
        this.ownerTypeDesc = ownerTypeDesc;
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

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseDesc() {
        return warehouseDesc;
    }

    public void setWarehouseDesc(String warehouseDesc) {
        this.warehouseDesc = warehouseDesc;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
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
}
