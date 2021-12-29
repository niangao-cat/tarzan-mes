package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @Author peng.yuan
 * @Date 2019/10/18 16:36
 */
public class MtMaterialLotVO21 implements Serializable {

    private static final long serialVersionUID = 8696010172405320603L;
    @ApiModelProperty("物料批ID列表")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("所属站点")
    private String siteId;
    @ApiModelProperty("有效性")
    private String enableFlag;
    @ApiModelProperty("质量状态")
    private String qualityStatus;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("主单位ID")
    private String primaryUomId;
    @ApiModelProperty("辅助单位ID")
    private String secondaryUomId;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("装配点")
    private String assemblePointId;
    @ApiModelProperty("装载时间从")
    private Date loadTimeFrom;
    @ApiModelProperty("装载时间至")
    private Date loadTimeTo;
    @ApiModelProperty("卸载时间从")
    private Date unloadTimeFrom;
    @ApiModelProperty("卸载时间至")
    private Date unloadTimeTo;
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
    @ApiModelProperty("入库时间从")
    private Date inLocatorTimeFrom;
    @ApiModelProperty("入库时间至")
    private Date inLocatorTimeTo;
    @ApiModelProperty("冻结标识")
    private String freezeFlag;
    @ApiModelProperty("盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "入场时间从")
    private Date inSiteTimeFrom;
    @ApiModelProperty(value = "入场时间至")
    private Date inSiteTimeTo;
    @ApiModelProperty(value = "当前容器ID")
    private String currentContainerId;
    @ApiModelProperty(value = "顶层容器ID")
    private String topContainerId;
    @ApiModelProperty(value = "最新历史ID")
    private String latestHisId;
    @ApiModelProperty(value = "指令单据ID")
    private String instructionDocId;

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

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
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

    public Date getLoadTimeFrom() {
        if (loadTimeFrom != null) {
            return (Date) loadTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setLoadTimeFrom(Date loadTimeFrom) {
        if (loadTimeFrom == null) {
            this.loadTimeFrom = null;
        } else {
            this.loadTimeFrom = (Date) loadTimeFrom.clone();
        }
    }

    public Date getLoadTimeTo() {
        if (loadTimeTo != null) {
            return (Date) loadTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setLoadTimeTo(Date loadTimeTo) {
        if (loadTimeTo == null) {
            this.loadTimeTo = null;
        } else {
            this.loadTimeTo = (Date) loadTimeTo.clone();
        }
    }

    public Date getUnloadTimeFrom() {
        if (unloadTimeFrom != null) {
            return (Date) unloadTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setUnloadTimeFrom(Date unloadTimeFrom) {
        if (unloadTimeFrom == null) {
            this.unloadTimeFrom = null;
        } else {
            this.unloadTimeFrom = (Date) unloadTimeFrom.clone();
        }
    }

    public Date getUnloadTimeTo() {
        if (unloadTimeTo != null) {
            return (Date) unloadTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setUnloadTimeTo(Date unloadTimeTo) {
        if (unloadTimeTo == null) {
            this.unloadTimeTo = null;
        } else {
            this.unloadTimeTo = (Date) unloadTimeTo.clone();
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

    public Date getInLocatorTimeFrom() {
        if (inLocatorTimeFrom != null) {
            return (Date) inLocatorTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setInLocatorTimeFrom(Date inLocatorTimeFrom) {
        if (inLocatorTimeFrom == null) {
            this.inLocatorTimeFrom = null;
        } else {
            this.inLocatorTimeFrom = (Date) inLocatorTimeFrom.clone();
        }
    }

    public Date getInLocatorTimeTo() {
        if (inLocatorTimeTo != null) {
            return (Date) inLocatorTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setInLocatorTimeTo(Date inLocatorTimeTo) {
        if (inLocatorTimeTo == null) {
            this.inLocatorTimeTo = null;
        } else {
            this.inLocatorTimeTo = (Date) inLocatorTimeTo.clone();
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

    public Date getInSiteTimeFrom() {
        if (inSiteTimeFrom != null) {
            return (Date) inSiteTimeFrom.clone();
        } else {
            return null;
        }
    }

    public void setInSiteTimeFrom(Date inSiteTimeFrom) {
        if (inSiteTimeFrom == null) {
            this.inSiteTimeFrom = null;
        } else {
            this.inSiteTimeFrom = (Date) inSiteTimeFrom.clone();
        }
    }

    public Date getInSiteTimeTo() {
        if (inSiteTimeTo != null) {
            return (Date) inSiteTimeTo.clone();
        } else {
            return null;
        }
    }

    public void setInSiteTimeTo(Date inSiteTimeTo) {
        if (inSiteTimeTo == null) {
            this.inSiteTimeTo = null;
        } else {
            this.inSiteTimeTo = (Date) inSiteTimeTo.clone();
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

    public String getLatestHisId() {
        return latestHisId;
    }

    public void setLatestHisId(String latestHisId) {
        this.latestHisId = latestHisId;
    }

    public String getInstructionDocId() {
        return instructionDocId;
    }

    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }
}
