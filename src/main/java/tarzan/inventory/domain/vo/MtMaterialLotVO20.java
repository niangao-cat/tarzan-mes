package tarzan.inventory.domain.vo;


import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/10/15 15:43
 * @Description:
 */
public class MtMaterialLotVO20 implements Serializable {

    private static final long serialVersionUID = 8971036257158097355L;
    @ApiModelProperty("作为物料批唯一标识，用于其他数据结构引用该物料批")
    private String materialLotId;
    @ApiModelProperty(value = "描述该物料批的唯一编码，用于方便识别", required = true)
    private String materialLotCode;
    @ApiModelProperty(value = "该物料批所在生产站点的标识ID", required = true)
    private String siteId;
    @ApiModelProperty(value = "描述该物料批的有效状态：描述该物料批的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效", required = true)
    private String enableFlag;
    @ApiModelProperty(value = "物料批所标识实物的质量状态：", required = true)
    private String qualityStatus;
    @ApiModelProperty(value = "该物料批所表示的实物的物料标识ID", required = true)
    private String materialId;
    @ApiModelProperty(value = "该物料批表示实物的主计量单位", required = true)
    private String primaryUomId;
    @ApiModelProperty(value = "实物在主计量单位下的数量", required = true)
    private Double primaryUomQty;
    @ApiModelProperty("主单位事务数量")
    private Double trxPrimaryUomQty;
    @ApiModelProperty(value = "该物料批表示实物的辅助计量单位")
    private String secondaryUomId;
    @ApiModelProperty(value = "实物在辅助计量单位下的数量")
    private Double secondaryUomQty;
    @ApiModelProperty("辅助单位事务数量")
    private Double trxSecondaryUomQty;
    @ApiModelProperty(value = "物料批当前所在货位标识ID，表示物料批仓库下存储位置")
    private String locatorId;
    @ApiModelProperty(value = "物料批所在装配点")
    private String assemblePointId;
    @ApiModelProperty(value = "指示将物料装载到物料批的时间，在物料批装载时进行赋值")
    private Date loadTime;
    @ApiModelProperty(value = "指示将物料批内物料消耗的时间，在物料批卸载时赋值，每次卸载时都更新")
    private Date unloadTime;
    @ApiModelProperty(value = "描述物料批表示的实物所有权属于客户还是供应商，包括： C：客户，表示该物料批对应物料属于客户 S：供应商，表示该物料批对应物料属于供应商 空：表示物料批属于厂内自有")
    private String ownerType;
    @ApiModelProperty(value = "配合所有者类型使用，描述物料批表示的实物具体的所有权对象，如所属于的具体供应商或具体客户，内容为供应商标识ID或客户标识ID")
    private String ownerId;
    @ApiModelProperty(value = "指示物料批所表示实物的来源批次编号")
    private String lot;
    @ApiModelProperty(value = "实物存在炉号管理时，指示物料批所表示实物对应的炉号")
    private String ovenNumber;
    @ApiModelProperty(value = "物料批对应实物通过采购获取时，指示物料批采购来源供应商标识ID")
    private String supplierId;
    @ApiModelProperty(value = "物料批对应实物通过采购获取时，指示物料批采购来源供应商地点标识ID")
    private String supplierSiteId;
    @ApiModelProperty(value = "物料批对应实物通过客户退货获取时，指示物料批采购来源客户标识ID 物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID")
    private String customerId;
    @ApiModelProperty(value = "物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID")
    private String customerSiteId;
    @ApiModelProperty(value = "指示物料批标识实物是否被保留，默认值为N")
    private String reservedFlag;
    @ApiModelProperty(value = "当物料批被保留时，指示物料批的保留对象类型，")
    private String reservedObjectType;
    @ApiModelProperty(value = "当物料批被保留时，指示物料批的保留对象值，配合保留对象类型使用")
    private String reservedObjectId;
    @ApiModelProperty(value = "该物料批被创建的原因，放入原因代码")
    private String createReason;
    @ApiModelProperty(value = "表示该物料批使用的物料批编码外的可视化标识", required = true)
    private String identification;
    @ApiModelProperty(value = "当物料批由于执行作业完工被创建时，指示物料批的来源EO")
    private String eoId;
    @ApiModelProperty(value = "入库时间 ，表示物料批进入当前库位的时间（生效时更新，库位变化时更新）")
    private Date inLocatorTime;
    @ApiModelProperty(value = "物料批冻结标识，用来盘点时冻结物料批的移动。为Y时不允许移动物料批。")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "入场时间")
    private Date inSiteTime;
    @ApiModelProperty(value = "当前容器ID")
    private String currentContainerId;
    @ApiModelProperty(value = "顶层容器ID")
    private String topContainerId;
    @ApiModelProperty(value = "指令单据ID")
    private String instructionDocId;

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
}
