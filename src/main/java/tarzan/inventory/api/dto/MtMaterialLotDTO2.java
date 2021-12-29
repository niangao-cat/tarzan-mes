package tarzan.inventory.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtMaterialLotDTO2 implements Serializable {
    private static final long serialVersionUID = 7127811365905986959L;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("所属站点")
    private String siteId;
    @ApiModelProperty("有效性")
    private String enableFlag;
    @ApiModelProperty("质量状态")
    private String qualityStatus;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("主单位")
    private String primaryUomId;
    @ApiModelProperty("主单位数量")
    private Double primaryUomQty;
    @ApiModelProperty("辅助单位")
    private String secondaryUomId;
    @ApiModelProperty("辅助单位数量")
    private Double secondaryUomQty;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("装配点")
    private String assemblePointId;
    @ApiModelProperty("装载时间")
    private Date loadTime;
    @ApiModelProperty("卸载时间")
    private Date unloadTime;
    @ApiModelProperty("所有者类型")
    private String ownerType;
    @ApiModelProperty("所有者ID")
    private String ownerId;
    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("炉号")
    private String ovenNumber;
    @ApiModelProperty("供应商ID")
    private String supplierId;
    @ApiModelProperty("供应商地点ID")
    private String supplierSiteId;
    @ApiModelProperty("客户ID")
    private String customerId;
    @ApiModelProperty("客户地点ID")
    private String customerSiteId;
    @ApiModelProperty("预留标识")
    private String reservedFlag;
    @ApiModelProperty("预留对象类型")
    private String reservedObjectType;
    @ApiModelProperty("预留对象ID")
    private String reservedObjectId;
    @ApiModelProperty("创建原因")
    private String createReason;
    @ApiModelProperty("标识说明")
    private String identification;
    @ApiModelProperty("执行作业")
    private String eoId;
    @ApiModelProperty("入库时间")
    private Date inLocatorTime;
    @ApiModelProperty("盘点冻结标识")
    private String freezeFlag;
    @ApiModelProperty("盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("主单位变更数量")
    private Double trxPrimaryUomQty;
    @ApiModelProperty("辅助单位变更数量")
    private Double trxSecondaryUomQty;
    @ApiModelProperty("入场时间")
    private Date inSiteTime;
    @ApiModelProperty("当前容器ID")
    private String currentContainerId;
    @ApiModelProperty("顶层容器ID")
    private String topContainerId;
    @ApiModelProperty("指令单据ID")
    private String instructionDocId;
    @ApiModelProperty("外部输入编码")
    private String outsideNum;
    @ApiModelProperty("传入值参数列表")
    private List<String> incomingValueList;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getQualityStatus() {
        return qualityStatus;
    }

    public void setQualityStatus(String qualityStatus) {
        this.qualityStatus = qualityStatus;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public Double getPrimaryUomQty() {
        return primaryUomQty;
    }

    public void setPrimaryUomQty(Double primaryUomQty) {
        this.primaryUomQty = primaryUomQty;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    public Double getSecondaryUomQty() {
        return secondaryUomQty;
    }

    public void setSecondaryUomQty(Double secondaryUomQty) {
        this.secondaryUomQty = secondaryUomQty;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public Date getLoadTime() {
        if (loadTime == null) {
            return null;
        } else {
            return (Date) loadTime.clone();
        }
    }

    public void setLoadTime(Date loadTime) {
        if (loadTime == null) {
            this.loadTime = null;
        } else {
            this.loadTime = (Date) loadTime.clone();
        }
    }

    public Date getUnloadTime() {
        if (unloadTime == null) {
            return null;
        } else {
            return (Date) unloadTime.clone();
        }
    }

    public void setUnloadTime(Date unloadTime) {
        if (unloadTime == null) {
            this.unloadTime = null;
        } else {
            this.unloadTime = (Date) unloadTime.clone();
        }
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

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getOvenNumber() {
        return ovenNumber;
    }

    public void setOvenNumber(String ovenNumber) {
        this.ovenNumber = ovenNumber;
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

    public String getReservedFlag() {
        return reservedFlag;
    }

    public void setReservedFlag(String reservedFlag) {
        this.reservedFlag = reservedFlag;
    }

    public String getReservedObjectType() {
        return reservedObjectType;
    }

    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    public String getReservedObjectId() {
        return reservedObjectId;
    }

    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }

    public String getCreateReason() {
        return createReason;
    }

    public void setCreateReason(String createReason) {
        this.createReason = createReason;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public Date getInLocatorTime() {
        if (inLocatorTime == null) {
            return null;
        } else {
            return (Date) inLocatorTime.clone();
        }
    }

    public void setInLocatorTime(Date inLocatorTime) {
        if (inLocatorTime == null) {
            this.inLocatorTime = null;
        } else {
            this.inLocatorTime = (Date) inLocatorTime.clone();
        }
    }

    public String getFreezeFlag() {
        return freezeFlag;
    }

    public void setFreezeFlag(String freezeFlag) {
        this.freezeFlag = freezeFlag;
    }

    public String getStocktakeFlag() {
        return stocktakeFlag;
    }

    public void setStocktakeFlag(String stocktakeFlag) {
        this.stocktakeFlag = stocktakeFlag;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Double getTrxPrimaryUomQty() {
        return trxPrimaryUomQty;
    }

    public void setTrxPrimaryUomQty(Double trxPrimaryUomQty) {
        this.trxPrimaryUomQty = trxPrimaryUomQty;
    }

    public Double getTrxSecondaryUomQty() {
        return trxSecondaryUomQty;
    }

    public void setTrxSecondaryUomQty(Double trxSecondaryUomQty) {
        this.trxSecondaryUomQty = trxSecondaryUomQty;
    }

    public Date getInSiteTime() {
        if (inSiteTime != null) {
            return (Date) inSiteTime.clone();
        } else {
            return null;
        }
    }

    public void setInSiteTime(Date inSiteTime) {
        if (inSiteTime == null) {
            this.inSiteTime = null;
        } else {
            this.inSiteTime = (Date) inSiteTime.clone();
        }
    }

    public String getCurrentContainerId() {
        return currentContainerId;
    }

    public void setCurrentContainerId(String currentContainerId) {
        this.currentContainerId = currentContainerId;
    }

    public String getTopContainerId() {
        return topContainerId;
    }

    public void setTopContainerId(String topContainerId) {
        this.topContainerId = topContainerId;
    }

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }

    public String getOutsideNum() {
        return outsideNum;
    }

    public void setOutsideNum(String outsideNum) {
        this.outsideNum = outsideNum;
    }

    public List<String> getIncomingValueList() {
        return incomingValueList;
    }

    public void setIncomingValueList(List<String> incomingValueList) {
        this.incomingValueList = incomingValueList;
    }
}
