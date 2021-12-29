package tarzan.inventory.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
@ApiModel("物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据")

@ModifyAudit

@Table(name = "mt_material_lot")
@CustomPrimary
public class MtMaterialLot extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_MATERIAL_LOT_CODE = "materialLotCode";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_QUALITY_STATUS = "qualityStatus";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_PRIMARY_UOM_ID = "primaryUomId";
    public static final String FIELD_PRIMARY_UOM_QTY = "primaryUomQty";
    public static final String FIELD_SECONDARY_UOM_ID = "secondaryUomId";
    public static final String FIELD_SECONDARY_UOM_QTY = "secondaryUomQty";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_ASSEMBLE_POINT_ID = "assemblePointId";
    public static final String FIELD_LOAD_TIME = "loadTime";
    public static final String FIELD_UNLOAD_TIME = "unloadTime";
    public static final String FIELD_OWNER_TYPE = "ownerType";
    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_LOT = "lot";
    public static final String FIELD_OVEN_NUMBER = "ovenNumber";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_SITE_ID = "customerSiteId";
    public static final String FIELD_RESERVED_FLAG = "reservedFlag";
    public static final String FIELD_RESERVED_OBJECT_TYPE = "reservedObjectType";
    public static final String FIELD_RESERVED_OBJECT_ID = "reservedObjectId";
    public static final String FIELD_CREATE_REASON = "createReason";
    public static final String FIELD_IDENTIFICATION = "identification";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_IN_LOCATOR_TIME = "inLocatorTime";
    public static final String FIELD_FREEZE_FLAG = "freezeFlag";
    public static final String FIELD_IN_SITE_TIME = "inSiteTime";
    public static final String FIELD_CURRENT_CONTAINER_ID = "currentContainerId";
    public static final String FIELD_TOP_CONTAINER_ID = "topContainerId";
    public static final String FIELD_LATEST_HIS_ID = "latestHisId";
    public static final String FIELD_INSTRUCTION_DOC_ID = "instructionDocId";

    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 3277246759713566947L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("作为物料批唯一标识，用于其他数据结构引用该物料批")
    @Id
    @Where
    private String materialLotId;
    @ApiModelProperty(value = "描述该物料批的唯一编码，用于方便识别", required = true)
    @NotBlank
    @Where
    private String materialLotCode;
    @ApiModelProperty(value = "该物料批所在生产站点的标识ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "描述该物料批的有效状态：描述该物料批的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "物料批所标识实物的质量状态：", required = true)
    @NotBlank
    @Where
    private String qualityStatus;
    @ApiModelProperty(value = "该物料批所表示的实物的物料标识ID", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "该物料批表示实物的主计量单位", required = true)
    @NotBlank
    @Where
    private String primaryUomId;
    @ApiModelProperty(value = "实物在主计量单位下的数量", required = true)
    @NotNull
    @Where
    private Double primaryUomQty;
    @ApiModelProperty(value = "该物料批表示实物的辅助计量单位")
    @Where
    private String secondaryUomId;
    @ApiModelProperty(value = "实物在辅助计量单位下的数量")
    @Where
    private Double secondaryUomQty;
    @ApiModelProperty(value = "物料批当前所在货位标识ID，表示物料批仓库下存储位置")
    @Where
    private String locatorId;
    @ApiModelProperty(value = "物料批所在装配点")
    @Where
    private String assemblePointId;
    @ApiModelProperty(value = "指示将物料装载到物料批的时间，在物料批装载时进行赋值")
    @Where
    private Date loadTime;
    @ApiModelProperty(value = "指示将物料批内物料消耗的时间，在物料批卸载时赋值，每次卸载时都更新")
    @Where
    private Date unloadTime;
    @ApiModelProperty(value = "描述物料批表示的实物所有权属于客户还是供应商，包括： C：客户，表示该物料批对应物料属于客户 S：供应商，表示该物料批对应物料属于供应商 空：表示物料批属于厂内自有")
    @Where
    private String ownerType;
    @ApiModelProperty(value = "配合所有者类型使用，描述物料批表示的实物具体的所有权对象，如所属于的具体供应商或具体客户，内容为供应商标识ID或客户标识ID")
    @Where
    private String ownerId;
    @ApiModelProperty(value = "指示物料批所表示实物的来源批次编号")
    @Where
    private String lot;
    @ApiModelProperty(value = "实物存在炉号管理时，指示物料批所表示实物对应的炉号")
    @Where
    private String ovenNumber;
    @ApiModelProperty(value = "物料批对应实物通过采购获取时，指示物料批采购来源供应商标识ID")
    @Where
    private String supplierId;
    @ApiModelProperty(value = "物料批对应实物通过采购获取时，指示物料批采购来源供应商地点标识ID")
    @Where
    private String supplierSiteId;
    @ApiModelProperty(value = "物料批对应实物通过客户退货获取时，指示物料批采购来源客户标识ID 物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID")
    @Where
    private String customerId;
    @ApiModelProperty(value = "物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID")
    @Where
    private String customerSiteId;
    @ApiModelProperty(value = "指示物料批标识实物是否被保留，默认值为N")
    @Where
    private String reservedFlag;
    @ApiModelProperty(value = "当物料批被保留时，指示物料批的保留对象类型，")
    @Where
    private String reservedObjectType;
    @ApiModelProperty(value = "当物料批被保留时，指示物料批的保留对象值，配合保留对象类型使用")
    @Where
    private String reservedObjectId;
    @ApiModelProperty(value = "该物料批被创建的原因，放入原因代码")
    @Where
    private String createReason;
    @ApiModelProperty(value = "表示该物料批使用的物料批编码外的可视化标识", required = true)
    @NotBlank
    @Where
    private String identification;
    @ApiModelProperty(value = "当物料批由于执行作业完工被创建时，指示物料批的来源EO")
    @Where
    private String eoId;
    @ApiModelProperty(value = "入库时间 ，表示物料批进入当前库位的时间（生效时更新，库位变化时更新）")
    @Where
    private Date inLocatorTime;
    @ApiModelProperty(value = "物料批冻结标识，用来盘点时冻结物料批的移动。为Y时不允许移动物料批。")
    @Where
    private String freezeFlag;
    @ApiModelProperty(value = "盘点停用标识")
    @Where
    private String stocktakeFlag;
    @ApiModelProperty(value = "业务类型")
    @Where
    private String businessType;
    @ApiModelProperty(value = "指令id")
    @Where
    private String instructionId;

    @ApiModelProperty(value = "入场时间")
    @Where
    private Date inSiteTime;

    @ApiModelProperty(value = "当前容器ID")
    @Where
    private String currentContainerId;

    @ApiModelProperty(value = "顶层容器ID")
    @Where
    private String topContainerId;

    @ApiModelProperty(value = "最新历史ID")
    @Where
    private String latestHisId;

    @ApiModelProperty(value = "指令单据ID")
    @Where
    private String instructionDocId;

    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 作为物料批唯一标识，用于其他数据结构引用该物料批
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    /**
     * @return 描述该物料批的唯一编码，用于方便识别
     */
    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
    }

    /**
     * @return 该物料批所在生产站点的标识ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 描述该物料批的有效状态：描述该物料批的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 物料批所标识实物的质量状态：
     */
    public String getQualityStatus() {
        return qualityStatus;
    }

    public void setQualityStatus(String qualityStatus) {
        this.qualityStatus = qualityStatus;
    }

    /**
     * @return 该物料批所表示的实物的物料标识ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 该物料批表示实物的主计量单位
     */
    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    /**
     * @return 实物在主计量单位下的数量
     */
    public Double getPrimaryUomQty() {
        return primaryUomQty;
    }

    public void setPrimaryUomQty(Double primaryUomQty) {
        this.primaryUomQty = primaryUomQty;
    }

    /**
     * @return 该物料批表示实物的辅助计量单位
     */
    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    /**
     * @return 实物在辅助计量单位下的数量
     */
    public Double getSecondaryUomQty() {
        return secondaryUomQty;
    }

    public void setSecondaryUomQty(Double secondaryUomQty) {
        this.secondaryUomQty = secondaryUomQty;
    }

    /**
     * @return 物料批当前所在货位标识ID，表示物料批仓库下存储位置
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 物料批所在装配点
     */
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

    /**
     * @return 描述物料批表示的实物所有权属于客户还是供应商，包括： C：客户，表示该物料批对应物料属于客户 S：供应商，表示该物料批对应物料属于供应商 空：表示物料批属于厂内自有
     */
    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * @return 配合所有者类型使用，描述物料批表示的实物具体的所有权对象，如所属于的具体供应商或具体客户，内容为供应商标识ID或客户标识ID
     */
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return 指示物料批所表示实物的来源批次编号
     */
    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    /**
     * @return 实物存在炉号管理时，指示物料批所表示实物对应的炉号
     */
    public String getOvenNumber() {
        return ovenNumber;
    }

    public void setOvenNumber(String ovenNumber) {
        this.ovenNumber = ovenNumber;
    }

    /**
     * @return 物料批对应实物通过采购获取时，指示物料批采购来源供应商标识ID
     */
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 物料批对应实物通过采购获取时，指示物料批采购来源供应商地点标识ID
     */
    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    /**
     * @return 物料批对应实物通过客户退货获取时，指示物料批采购来源客户标识ID 物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID
     */
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return 物料批对应实物通过客户退货获取时，指示物料批采购来源客户地点标识ID
     */
    public String getCustomerSiteId() {
        return customerSiteId;
    }

    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    /**
     * @return 指示物料批标识实物是否被保留，默认值为N
     */
    public String getReservedFlag() {
        return reservedFlag;
    }

    public void setReservedFlag(String reservedFlag) {
        this.reservedFlag = reservedFlag;
    }

    /**
     * @return 当物料批被保留时，指示物料批的保留对象类型，内容包括： WO：该物料批被某一生产指令保留 EO：该物料批被某一执行作业保留 DRIVING：该物料批被某一驱动指令保留
     *         CUSTOM：该物料批被某一客户保留 OO：该物料批被某一机会订单预留
     */
    public String getReservedObjectType() {
        return reservedObjectType;
    }

    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    /**
     * @return 当物料批被保留时，指示物料批的保留对象值，配合保留对象类型使用
     */
    public String getReservedObjectId() {
        return reservedObjectId;
    }

    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }

    /**
     * @return 该物料批被创建的原因，放入原因代码，包括： 1.INITIALIZE：初始化 2.INVENTORY：盘点 3.SPLIT：拆分 4.MERAGE：合并
     *         5.COMPLETE：完工 6.SUPPLY：外供
     */
    public String getCreateReason() {
        return createReason;
    }

    public void setCreateReason(String createReason) {
        this.createReason = createReason;
    }

    /**
     * @return 表示该物料批使用的物料批编码外的可视化标识
     */
    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * @return 当物料批由于执行作业完工被创建时，指示物料批的来源EO
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 物料批冻结标识，用来盘点时冻结物料批的移动。为Y时不允许移动物料批。
     */
    public String getFreezeFlag() {
        return freezeFlag;
    }

    public void setFreezeFlag(String freezeFlag) {
        this.freezeFlag = freezeFlag;
    }

    /**
     * @return 盘点停用标识
     */
    public String getStocktakeFlag() {
        return stocktakeFlag;
    }

    public void setStocktakeFlag(String stocktakeFlag) {
        this.stocktakeFlag = stocktakeFlag;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public Date getInSiteTime() {
        if (inSiteTime == null) {
            return null;
        } else {
            return (Date) inSiteTime.clone();
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
