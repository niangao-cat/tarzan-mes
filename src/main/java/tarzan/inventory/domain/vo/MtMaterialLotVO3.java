package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * @author Leeloing
 * @date 2019/3/26 9:42
 */
public class MtMaterialLotVO3 extends MtMaterialLot implements Serializable {

    private static final long serialVersionUID = 6092620453352288600L;

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
    @ApiModelProperty(value = "该物料批表示实物的辅助计量单位")
    private String secondaryUomId;
    @ApiModelProperty(value = "实物在辅助计量单位下的数量")
    private Double secondaryUomQty;
    @ApiModelProperty(value = "物料批当前所在货位标识ID，表示物料批仓库下存储位置")
    private String locatorId;
    @ApiModelProperty(value = "物料批所在装配点")
    private String assemblePointId;
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
    @ApiModelProperty(value = "物料批冻结标识，用来盘点时冻结物料批的移动。为Y时不允许移动物料批。")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    @ApiModelProperty(value = "指令id")
    private String instructionId;
    @ApiModelProperty(value = "装载时间从")
    private Date loadTimeFrom;
    @ApiModelProperty(value = "装载时间到")
    private Date loadTimeTo;
    @ApiModelProperty(value = "卸载时间从")
    private Date unloadTimeFrom;
    @ApiModelProperty(value = "卸载时间到")
    private Date unloadTimeTo;
    @ApiModelProperty(value = "入库时间从")
    private Date inLocatorTimeFrom;
    @ApiModelProperty(value = "入库时间至")
    private Date inLocatorTimeTo;
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

    @Override
    public String getMaterialLotId() {
        return materialLotId;
    }

    @Override
    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    @Override
    public String getMaterialLotCode() {
        return materialLotCode;
    }

    @Override
    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
    }

    @Override
    public String getSiteId() {
        return siteId;
    }

    @Override
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    @Override
    public String getEnableFlag() {
        return enableFlag;
    }

    @Override
    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    @Override
    public String getQualityStatus() {
        return qualityStatus;
    }

    @Override
    public void setQualityStatus(String qualityStatus) {
        this.qualityStatus = qualityStatus;
    }

    @Override
    public String getMaterialId() {
        return materialId;
    }

    @Override
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    @Override
    public String getPrimaryUomId() {
        return primaryUomId;
    }

    @Override
    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    @Override
    public Double getPrimaryUomQty() {
        return primaryUomQty;
    }

    @Override
    public void setPrimaryUomQty(Double primaryUomQty) {
        this.primaryUomQty = primaryUomQty;
    }

    @Override
    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    @Override
    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    @Override
    public Double getSecondaryUomQty() {
        return secondaryUomQty;
    }

    @Override
    public void setSecondaryUomQty(Double secondaryUomQty) {
        this.secondaryUomQty = secondaryUomQty;
    }

    @Override
    public String getLocatorId() {
        return locatorId;
    }

    @Override
    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    @Override
    public String getAssemblePointId() {
        return assemblePointId;
    }

    @Override
    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    @Override
    public String getOwnerType() {
        return ownerType;
    }

    @Override
    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String getLot() {
        return lot;
    }

    @Override
    public void setLot(String lot) {
        this.lot = lot;
    }

    @Override
    public String getOvenNumber() {
        return ovenNumber;
    }

    @Override
    public void setOvenNumber(String ovenNumber) {
        this.ovenNumber = ovenNumber;
    }

    @Override
    public String getSupplierId() {
        return supplierId;
    }

    @Override
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    @Override
    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    @Override
    public String getCustomerId() {
        return customerId;
    }

    @Override
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String getCustomerSiteId() {
        return customerSiteId;
    }

    @Override
    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    @Override
    public String getReservedFlag() {
        return reservedFlag;
    }

    @Override
    public void setReservedFlag(String reservedFlag) {
        this.reservedFlag = reservedFlag;
    }

    @Override
    public String getReservedObjectType() {
        return reservedObjectType;
    }

    @Override
    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    @Override
    public String getReservedObjectId() {
        return reservedObjectId;
    }

    @Override
    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }

    @Override
    public String getCreateReason() {
        return createReason;
    }

    @Override
    public void setCreateReason(String createReason) {
        this.createReason = createReason;
    }

    @Override
    public String getIdentification() {
        return identification;
    }

    @Override
    public void setIdentification(String identification) {
        this.identification = identification;
    }

    @Override
    public String getEoId() {
        return eoId;
    }

    @Override
    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    @Override
    public String getFreezeFlag() {
        return freezeFlag;
    }

    @Override
    public void setFreezeFlag(String freezeFlag) {
        this.freezeFlag = freezeFlag;
    }

    @Override
    public String getStocktakeFlag() {
        return stocktakeFlag;
    }

    @Override
    public void setStocktakeFlag(String stocktakeFlag) {
        this.stocktakeFlag = stocktakeFlag;
    }

    @Override
    public String getBusinessType() {
        return businessType;
    }

    @Override
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Override
    public String getInstructionId() {
        return instructionId;
    }

    @Override
    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
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

    @Override
    public String getCurrentContainerId() {
        return currentContainerId;
    }

    @Override
    public void setCurrentContainerId(String currentContainerId) {
        this.currentContainerId = currentContainerId;
    }

    @Override
    public String getTopContainerId() {
        return topContainerId;
    }

    @Override
    public void setTopContainerId(String topContainerId) {
        this.topContainerId = topContainerId;
    }

    @Override
    public String getLatestHisId() {
        return latestHisId;
    }

    @Override
    public void setLatestHisId(String latestHisId) {
        this.latestHisId = latestHisId;
    }

    @Override
    public String getInstructionDocId() {
        return instructionDocId;
    }

    @Override
    public void setInstructionDocId(String instructionDocId) {
        this.instructionDocId = instructionDocId;
    }
}
