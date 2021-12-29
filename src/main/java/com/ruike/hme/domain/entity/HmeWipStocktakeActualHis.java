package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 在制盘点实际历史
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@ApiModel("在制盘点实际历史")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_wip_stocktake_actual_his")
public class HmeWipStocktakeActualHis extends AuditDomain {

    public static final String FIELD_STOCKTAKE_ACTUAL_HIS_ID = "stocktakeActualHisId";
    public static final String FIELD_STOCKTAKE_ACTUAL_ID = "stocktakeActualId";
    public static final String FIELD_STOCKTAKE_ID = "stocktakeId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_LOT_CODE = "lotCode";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_CONTAINER_ID = "containerId";
    public static final String FIELD_OWNER_TYPE = "ownerType";
    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_RESERVED_OBJECT_TYPE = "reservedObjectType";
    public static final String FIELD_RESERVED_OBJECT_ID = "reservedObjectId";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_CURRENT_QUANTITY = "currentQuantity";
    public static final String FIELD_FIRSTCOUNT_MATERIAL_ID = "firstcountMaterialId";
    public static final String FIELD_FIRSTCOUNT_UOM_ID = "firstcountUomId";
    public static final String FIELD_FIRSTCOUNT_PROD_LINE_ID = "firstcountProdLineId";
    public static final String FIELD_FIRSTCOUNT_WORKCELL_ID = "firstcountWorkcellId";
    public static final String FIELD_FIRSTCOUNT_CONTAINER_ID = "firstcountContainerId";
    public static final String FIELD_FIRSTCOUNT_LOCATION_ROW = "firstcountLocationRow";
    public static final String FIELD_FIRSTCOUNT_LOCATION_COLUMN = "firstcountLocationColumn";
    public static final String FIELD_FIRSTCOUNT_OWNER_TYPE = "firstcountOwnerType";
    public static final String FIELD_FIRSTCOUNT_OWNER_ID = "firstcountOwnerId";
    public static final String FIELD_FIRSTCOUNT_RESERVED_OBJECT_TY = "firstcountReservedObjectTy";
    public static final String FIELD_FIRSTCOUNT_RESERVED_OBJECT_ID = "firstcountReservedObjectId";
    public static final String FIELD_FIRSTCOUNT_QUANTITY = "firstcountQuantity";
    public static final String FIELD_FIRSTCOUNT_BY = "firstcountBy";
    public static final String FIELD_FIRSTCOUNT_DATE = "firstcountDate";
    public static final String FIELD_FIRSTCOUNT_REMARK = "firstcountRemark";
    public static final String FIELD_RECOUNT_MATERIAL_ID = "recountMaterialId";
    public static final String FIELD_RECOUNT_UOM_ID = "recountUomId";
    public static final String FIELD_RECOUNT_PROD_LINE_ID = "recountProdLineId";
    public static final String FIELD_RECOUNT_WORKCELL_ID = "recountWorkcellId";
    public static final String FIELD_RECOUNT_CONTAINER_ID = "recountContainerId";
    public static final String FIELD_RECOUNT_LOCATION_ROW = "recountLocationRow";
    public static final String FIELD_RECOUNT_LOCATION_COLUMN = "recountLocationColumn";
    public static final String FIELD_RECOUNT_OWNER_TYPE = "recountOwnerType";
    public static final String FIELD_RECOUNT_OWNER_ID = "recountOwnerId";
    public static final String FIELD_RECOUNT_RESERVED_OBJECT_TYPE = "recountReservedObjectType";
    public static final String FIELD_RECOUNT_RESERVED_OBJECT_ID = "recountReservedObjectId";
    public static final String FIELD_RECOUNT_QUANTITY = "recountQuantity";
    public static final String FIELD_RECOUNT_BY = "recountBy";
    public static final String FIELD_RECOUNT_DATE = "recountDate";
    public static final String FIELD_RECOUNT_REMARK = "recountRemark";
    public static final String FIELD_ADJUST_FLAG = "adjustFlag";
    public static final String FIELD_LATEST_HIS_ID = "latestHisId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
//    @GeneratedValue
    private String stocktakeActualHisId;
    @ApiModelProperty(value = "盘点实际ID", required = true)
    @NotBlank
    private String stocktakeActualId;
    @ApiModelProperty(value = "盘点单ID", required = true)
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "物料批ID", required = true)
    @NotBlank
    private String materialLotId;
    @ApiModelProperty(value = "批次CODE")
    private String lotCode;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "在制品的工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "在制品的产线ID")
    private String prodLineId;
    @ApiModelProperty(value = "在制品的工序ID")
    private String workcellId;
    @ApiModelProperty(value = "物料批对应的容器ID（最上层）")
    private String containerId;
    @ApiModelProperty(value = "所有者类型")
    private String ownerType;
    @ApiModelProperty(value = "所有者类型ID")
    private String ownerId;
    @ApiModelProperty(value = "预留对象类型")
    private String reservedObjectType;
    @ApiModelProperty(value = "预留对象")
    private String reservedObjectId;
    @ApiModelProperty(value = "物料批主单位ID")
    private String uomId;
    @ApiModelProperty(value = "物料账上数量")
    private BigDecimal currentQuantity;
    @ApiModelProperty(value = "初盘物料ID")
    private String firstcountMaterialId;
    @ApiModelProperty(value = "初盘主单位ID")
    private String firstcountUomId;
    @ApiModelProperty(value = "初盘产线ID")
    private String firstcountProdLineId;
    @ApiModelProperty(value = "初盘工序ID")
    private String firstcountWorkcellId;
    @ApiModelProperty(value = "初盘容器ID")
    private String firstcountContainerId;
    @ApiModelProperty(value = "初盘容器装载物料对应行")
    private Long firstcountLocationRow;
    @ApiModelProperty(value = "初盘容器装载物料对应列")
    private Long firstcountLocationColumn;
    @ApiModelProperty(value = "初盘所有者类型")
    private String firstcountOwnerType;
    @ApiModelProperty(value = "初盘所有者ID")
    private String firstcountOwnerId;
    @ApiModelProperty(value = "初盘预留类型")
    private String firstcountReservedObjectTy;
    @ApiModelProperty(value = "初盘预留对象ID")
    private String firstcountReservedObjectId;
    @ApiModelProperty(value = "初盘数量，物料批主单位下的数量")
    private BigDecimal firstcountQuantity;
    @ApiModelProperty(value = "初盘当前用户id")
    private Long firstcountBy;
    @ApiModelProperty(value = "当前初盘时间")
    private Date firstcountDate;
    @ApiModelProperty(value = "初盘备注")
    private String firstcountRemark;
    @ApiModelProperty(value = "复盘物料ID")
    private String recountMaterialId;
    @ApiModelProperty(value = "复盘主单位ID")
    private String recountUomId;
    @ApiModelProperty(value = "复盘产线ID")
    private String recountProdLineId;
    @ApiModelProperty(value = "复盘工序ID")
    private String recountWorkcellId;
    @ApiModelProperty(value = "复盘容器ID")
    private String recountContainerId;
    @ApiModelProperty(value = "复盘容器装载物料对应行")
    private Long recountLocationRow;
    @ApiModelProperty(value = "复盘容器装载物料对应列")
    private Long recountLocationColumn;
    @ApiModelProperty(value = "复盘所有者类型")
    private String recountOwnerType;
    @ApiModelProperty(value = "复盘所有者ID")
    private String recountOwnerId;
    @ApiModelProperty(value = "复盘预留对象类型")
    private String recountReservedObjectType;
    @ApiModelProperty(value = "复盘预留对象")
    private String recountReservedObjectId;
    @ApiModelProperty(value = "复盘数量，物料批主单位下的数量")
    private BigDecimal recountQuantity;
    @ApiModelProperty(value = "复盘当前用户id")
    private Long recountBy;
    @ApiModelProperty(value = "当前复盘时间")
    private Date recountDate;
    @ApiModelProperty(value = "复盘备注")
    private String recountRemark;
    @ApiModelProperty(value = "调整标识")
    private String adjustFlag;
    @ApiModelProperty(value = "最近历史ID")
    private String latestHisId;
    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotNull
    private String eventId;
    @ApiModelProperty(value = "CID", required = true)
    @Cid
    private Long cid;
    @ApiModelProperty(value = "")
    private String attributeCategory;
    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
    @ApiModelProperty(value = "")
    private String attribute11;
    @ApiModelProperty(value = "")
    private String attribute12;
    @ApiModelProperty(value = "")
    private String attribute13;
    @ApiModelProperty(value = "")
    private String attribute14;
    @ApiModelProperty(value = "")
    private String attribute15;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 主键
     */
    public String getStocktakeActualHisId() {
        return stocktakeActualHisId;
    }

    public void setStocktakeActualHisId(String stocktakeActualHisId) {
        this.stocktakeActualHisId = stocktakeActualHisId;
    }

    /**
     * @return 盘点实际ID
     */
    public String getStocktakeActualId() {
        return stocktakeActualId;
    }

    public void setStocktakeActualId(String stocktakeActualId) {
        this.stocktakeActualId = stocktakeActualId;
    }

    /**
     * @return 盘点单ID
     */
    public String getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(String stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    /**
     * @return 站点ID
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 物料批ID
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    /**
     * @return 批次CODE
     */
    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 在制品的工单ID
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return 在制品的产线ID
     */
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    /**
     * @return 在制品的工序ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 物料批对应的容器ID（最上层）
     */
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * @return 所有者类型
     */
    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * @return 所有者类型ID
     */
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return 预留对象类型
     */
    public String getReservedObjectType() {
        return reservedObjectType;
    }

    public void setReservedObjectType(String reservedObjectType) {
        this.reservedObjectType = reservedObjectType;
    }

    /**
     * @return 预留对象
     */
    public String getReservedObjectId() {
        return reservedObjectId;
    }

    public void setReservedObjectId(String reservedObjectId) {
        this.reservedObjectId = reservedObjectId;
    }

    /**
     * @return 物料批主单位ID
     */
    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    /**
     * @return 物料账上数量
     */
    public BigDecimal getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(BigDecimal currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    /**
     * @return 初盘物料ID
     */
    public String getFirstcountMaterialId() {
        return firstcountMaterialId;
    }

    public void setFirstcountMaterialId(String firstcountMaterialId) {
        this.firstcountMaterialId = firstcountMaterialId;
    }

    /**
     * @return 初盘主单位ID
     */
    public String getFirstcountUomId() {
        return firstcountUomId;
    }

    public void setFirstcountUomId(String firstcountUomId) {
        this.firstcountUomId = firstcountUomId;
    }

    /**
     * @return 初盘产线ID
     */
    public String getFirstcountProdLineId() {
        return firstcountProdLineId;
    }

    public void setFirstcountProdLineId(String firstcountProdLineId) {
        this.firstcountProdLineId = firstcountProdLineId;
    }

    /**
     * @return 初盘工序ID
     */
    public String getFirstcountWorkcellId() {
        return firstcountWorkcellId;
    }

    public void setFirstcountWorkcellId(String firstcountWorkcellId) {
        this.firstcountWorkcellId = firstcountWorkcellId;
    }

    /**
     * @return 初盘容器ID
     */
    public String getFirstcountContainerId() {
        return firstcountContainerId;
    }

    public void setFirstcountContainerId(String firstcountContainerId) {
        this.firstcountContainerId = firstcountContainerId;
    }

    /**
     * @return 初盘容器装载物料对应行
     */
    public Long getFirstcountLocationRow() {
        return firstcountLocationRow;
    }

    public void setFirstcountLocationRow(Long firstcountLocationRow) {
        this.firstcountLocationRow = firstcountLocationRow;
    }

    /**
     * @return 初盘容器装载物料对应列
     */
    public Long getFirstcountLocationColumn() {
        return firstcountLocationColumn;
    }

    public void setFirstcountLocationColumn(Long firstcountLocationColumn) {
        this.firstcountLocationColumn = firstcountLocationColumn;
    }

    /**
     * @return 初盘所有者类型
     */
    public String getFirstcountOwnerType() {
        return firstcountOwnerType;
    }

    public void setFirstcountOwnerType(String firstcountOwnerType) {
        this.firstcountOwnerType = firstcountOwnerType;
    }

    /**
     * @return 初盘所有者ID
     */
    public String getFirstcountOwnerId() {
        return firstcountOwnerId;
    }

    public void setFirstcountOwnerId(String firstcountOwnerId) {
        this.firstcountOwnerId = firstcountOwnerId;
    }

    /**
     * @return 初盘预留类型
     */
    public String getFirstcountReservedObjectTy() {
        return firstcountReservedObjectTy;
    }

    public void setFirstcountReservedObjectTy(String firstcountReservedObjectTy) {
        this.firstcountReservedObjectTy = firstcountReservedObjectTy;
    }

    /**
     * @return 初盘预留对象ID
     */
    public String getFirstcountReservedObjectId() {
        return firstcountReservedObjectId;
    }

    public void setFirstcountReservedObjectId(String firstcountReservedObjectId) {
        this.firstcountReservedObjectId = firstcountReservedObjectId;
    }

    /**
     * @return 初盘数量，物料批主单位下的数量
     */
    public BigDecimal getFirstcountQuantity() {
        return firstcountQuantity;
    }

    public void setFirstcountQuantity(BigDecimal firstcountQuantity) {
        this.firstcountQuantity = firstcountQuantity;
    }

    /**
     * @return 初盘当前用户id
     */
    public Long getFirstcountBy() {
        return firstcountBy;
    }

    public void setFirstcountBy(Long firstcountBy) {
        this.firstcountBy = firstcountBy;
    }

    /**
     * @return 当前初盘时间
     */
    public Date getFirstcountDate() {
        return firstcountDate;
    }

    public void setFirstcountDate(Date firstcountDate) {
        this.firstcountDate = firstcountDate;
    }

    /**
     * @return 初盘备注
     */
    public String getFirstcountRemark() {
        return firstcountRemark;
    }

    public void setFirstcountRemark(String firstcountRemark) {
        this.firstcountRemark = firstcountRemark;
    }

    /**
     * @return 复盘物料ID
     */
    public String getRecountMaterialId() {
        return recountMaterialId;
    }

    public void setRecountMaterialId(String recountMaterialId) {
        this.recountMaterialId = recountMaterialId;
    }

    /**
     * @return 复盘主单位ID
     */
    public String getRecountUomId() {
        return recountUomId;
    }

    public void setRecountUomId(String recountUomId) {
        this.recountUomId = recountUomId;
    }

    /**
     * @return 复盘产线ID
     */
    public String getRecountProdLineId() {
        return recountProdLineId;
    }

    public void setRecountProdLineId(String recountProdLineId) {
        this.recountProdLineId = recountProdLineId;
    }

    /**
     * @return 复盘工序ID
     */
    public String getRecountWorkcellId() {
        return recountWorkcellId;
    }

    public void setRecountWorkcellId(String recountWorkcellId) {
        this.recountWorkcellId = recountWorkcellId;
    }

    /**
     * @return 复盘容器ID
     */
    public String getRecountContainerId() {
        return recountContainerId;
    }

    public void setRecountContainerId(String recountContainerId) {
        this.recountContainerId = recountContainerId;
    }

    /**
     * @return 复盘容器装载物料对应行
     */
    public Long getRecountLocationRow() {
        return recountLocationRow;
    }

    public void setRecountLocationRow(Long recountLocationRow) {
        this.recountLocationRow = recountLocationRow;
    }

    /**
     * @return 复盘容器装载物料对应列
     */
    public Long getRecountLocationColumn() {
        return recountLocationColumn;
    }

    public void setRecountLocationColumn(Long recountLocationColumn) {
        this.recountLocationColumn = recountLocationColumn;
    }

    /**
     * @return 复盘所有者类型
     */
    public String getRecountOwnerType() {
        return recountOwnerType;
    }

    public void setRecountOwnerType(String recountOwnerType) {
        this.recountOwnerType = recountOwnerType;
    }

    /**
     * @return 复盘所有者ID
     */
    public String getRecountOwnerId() {
        return recountOwnerId;
    }

    public void setRecountOwnerId(String recountOwnerId) {
        this.recountOwnerId = recountOwnerId;
    }

    /**
     * @return 复盘预留对象类型
     */
    public String getRecountReservedObjectType() {
        return recountReservedObjectType;
    }

    public void setRecountReservedObjectType(String recountReservedObjectType) {
        this.recountReservedObjectType = recountReservedObjectType;
    }

    /**
     * @return 复盘预留对象
     */
    public String getRecountReservedObjectId() {
        return recountReservedObjectId;
    }

    public void setRecountReservedObjectId(String recountReservedObjectId) {
        this.recountReservedObjectId = recountReservedObjectId;
    }

    /**
     * @return 复盘数量，物料批主单位下的数量
     */
    public BigDecimal getRecountQuantity() {
        return recountQuantity;
    }

    public void setRecountQuantity(BigDecimal recountQuantity) {
        this.recountQuantity = recountQuantity;
    }

    /**
     * @return 复盘当前用户id
     */
    public Long getRecountBy() {
        return recountBy;
    }

    public void setRecountBy(Long recountBy) {
        this.recountBy = recountBy;
    }

    /**
     * @return 当前复盘时间
     */
    public Date getRecountDate() {
        return recountDate;
    }

    public void setRecountDate(Date recountDate) {
        this.recountDate = recountDate;
    }

    /**
     * @return 复盘备注
     */
    public String getRecountRemark() {
        return recountRemark;
    }

    public void setRecountRemark(String recountRemark) {
        this.recountRemark = recountRemark;
    }

    /**
     * @return 调整标识
     */
    public String getAdjustFlag() {
        return adjustFlag;
    }

    public void setAdjustFlag(String adjustFlag) {
        this.adjustFlag = adjustFlag;
    }

    /**
     * @return 最近历史ID
     */
    public String getLatestHisId() {
        return latestHisId;
    }

    public void setLatestHisId(String latestHisId) {
        this.latestHisId = latestHisId;
    }

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
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
