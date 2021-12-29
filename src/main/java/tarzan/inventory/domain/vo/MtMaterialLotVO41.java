package tarzan.inventory.domain.vo;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * Tangxiao
 */
public class MtMaterialLotVO41 implements Serializable {


    private static final long serialVersionUID = 5609207914825181490L;
    @ApiModelProperty("作为物料批唯一标识，用于其他数据结构引用该物料批")
    private String materialLotId;
    @ApiModelProperty("描述该物料批的唯一编码，用于方便识别")
    private String materialLotCode;
    @ApiModelProperty("该物料批所在生产站点的标识ID")
    private String siteId;
    @ApiModelProperty("描述该物料批的有效状态：描述该物料批的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效")
    private String enableFlag;
    @ApiModelProperty("物料批所标识实物的质量状态：")
    private String qualityStatus;
    @ApiModelProperty("该物料批所表示的实物的物料标识ID")
    private String materialId;
    @ApiModelProperty("物料版本")
    private String revisionCode;
    @ApiModelProperty("该物料批表示实物的主计量单位")
    private String primaryUomId;
    @ApiModelProperty("主单位事务数量")
    private Double trxPrimaryUomQty;
    @ApiModelProperty("该物料批表示实物的辅助计量单位")
    private String secondaryUomId;
    @ApiModelProperty("辅助单位事务数量")
    private Double trxSecondaryUomQty;
    @ApiModelProperty("物料批当前所在货位标识ID，表示物料批仓库下存储位置")
    private String locatorId;
    @ApiModelProperty("物料批所在装配点")
    private String assemblePointId;
    @ApiModelProperty("指示将物料装载到物料批的时间，在物料批装载时进行赋值")
    private Date loadTime;
    @ApiModelProperty("指示将物料批内物料消耗的时间，在物料批卸载时赋值，每次卸载时都更新")
    private Date unloadTime;
    @ApiModelProperty("描述物料批表示的实物所有权属于客户还是供应商，包括： C：客户，表示该物料批对应物料属于客户 S：供应商，表示该物料批对应物料属于供应商 空：表示物料批属于厂内自有")
    private String ownerType;
    @ApiModelProperty("配合所有者类型使用，描述物料批表示的实物具体的所有权对象，如所属于的具体供应商或具体客户，内容为供应商标识ID或客户标识ID")
    private String ownerId;
    @ApiModelProperty("指示物料批所表示实物的来源批次编号")
    private String lot;
    @ApiModelProperty("实物存在炉号管理时，指示物料批所表示实物对应的炉号")
    private String ovenNumber;
    @ApiModelProperty("物料批对应实物通过采购获取时，指示物料批采购来源供应商标识ID")
    private String supplierId;
    @ApiModelProperty("物料批对应实物通过采购获取时，指示物料批采购来源供应商地点标识ID")
    private String supplierSiteId;
    @ApiModelProperty("物料批对应实物通过客户退货获取时，指示物料批采购来源客户标识ID 物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID")
    private String customerId;
    @ApiModelProperty("物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID")
    private String customerSiteId;
    @ApiModelProperty("该物料批被创建的原因，放入原因代码")
    private String createReason;
    @ApiModelProperty("表示该物料批使用的物料批编码外的可视化标识")
    private String identification;
    @ApiModelProperty("当物料批由于执行作业完工被创建时，指示物料批的来源EO")
    private String eoId;
    @ApiModelProperty("入库时间 ，表示物料批进入当前库位的时间（生效时更新，库位变化时更新）")
    private Date inLocatorTime;
    @ApiModelProperty("物料批冻结标识，用来盘点时冻结物料批的移动。为Y时不允许移动物料批。")
    private String freezeFlag;
    @ApiModelProperty("入场时间")
    private Date inSiteTime;

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

    public String getRevisionCode() {
        return revisionCode;
    }

    public void setRevisionCode(String revisionCode) {
        this.revisionCode = revisionCode;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public Double getTrxPrimaryUomQty() {
        return trxPrimaryUomQty;
    }

    public void setTrxPrimaryUomQty(Double trxPrimaryUomQty) {
        this.trxPrimaryUomQty = trxPrimaryUomQty;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    public Double getTrxSecondaryUomQty() {
        return trxSecondaryUomQty;
    }

    public void setTrxSecondaryUomQty(Double trxSecondaryUomQty) {
        this.trxSecondaryUomQty = trxSecondaryUomQty;
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
}
