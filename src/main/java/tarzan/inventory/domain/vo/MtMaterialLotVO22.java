package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @Author peng.yuan
 * @Date 2019/10/18 16:36
 */
public class MtMaterialLotVO22 implements Serializable {

    private static final long serialVersionUID = -8507825758322588540L;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("所属站点ID")
    private String siteId;
    @ApiModelProperty("所属站点编码")
    private String siteCode;
    @ApiModelProperty("所属站点名称")
    private String siteName;
    @ApiModelProperty("有效性")
    private String enableFlag;
    @ApiModelProperty("质量状态")
    private String qualityStatus;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("主单位ID")
    private String primaryUomId;
    @ApiModelProperty("主单位编码")
    private String primaryUomCode;
    @ApiModelProperty("主单位名称")
    private String primaryUomName;
    @ApiModelProperty("主单位数量")
    private Double primaryUomQty;
    @ApiModelProperty("辅助单位ID")
    private String secondaryUomId;
    @ApiModelProperty("辅助单位编码")
    private String secondaryUomCode;
    @ApiModelProperty("辅助单位名称")
    private String secondaryUomName;
    @ApiModelProperty("辅助单位数量")
    private Double secondaryUomQty;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("库位编码")
    private String locatorCode;
    @ApiModelProperty("库位名称")
    private String locatorName;
    @ApiModelProperty("装配点ID")
    private String assemblePointId;
    @ApiModelProperty("装配点编码")
    private String assemblePointCode;
    @ApiModelProperty("装配点描述")
    private String assemblePointDescription;
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
    @ApiModelProperty("冻结标识")
    private String freezeFlag;
    @ApiModelProperty("盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "入场时间")
    private Date inSiteTime;
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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
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

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public String getPrimaryUomCode() {
        return primaryUomCode;
    }

    public void setPrimaryUomCode(String primaryUomCode) {
        this.primaryUomCode = primaryUomCode;
    }

    public String getPrimaryUomName() {
        return primaryUomName;
    }

    public void setPrimaryUomName(String primaryUomName) {
        this.primaryUomName = primaryUomName;
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

    public String getSecondaryUomCode() {
        return secondaryUomCode;
    }

    public void setSecondaryUomCode(String secondaryUomCode) {
        this.secondaryUomCode = secondaryUomCode;
    }

    public String getSecondaryUomName() {
        return secondaryUomName;
    }

    public void setSecondaryUomName(String secondaryUomName) {
        this.secondaryUomName = secondaryUomName;
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

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getAssemblePointCode() {
        return assemblePointCode;
    }

    public void setAssemblePointCode(String assemblePointCode) {
        this.assemblePointCode = assemblePointCode;
    }

    public String getAssemblePointDescription() {
        return assemblePointDescription;
    }

    public void setAssemblePointDescription(String assemblePointDescription) {
        this.assemblePointDescription = assemblePointDescription;
    }

    public Date getLoadTime() {
        if (loadTime != null) {
            return (Date) loadTime.clone();
        } else {
            return null;
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
        if (unloadTime != null) {
            return (Date) unloadTime.clone();
        } else {
            return null;
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
        if (inLocatorTime != null) {
            return (Date) inLocatorTime.clone();
        } else {
            return null;
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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
