package tarzan.actual.domain.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 盘点实绩历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("盘点实绩历史表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_stocktake_actual_his")
@CustomPrimary
public class MtStocktakeActualHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_STOCKTAKE_ACTUAL_HIS_ID = "stocktakeActualHisId";
    public static final String FIELD_STOCKTAKE_ACTUAL_ID = "stocktakeActualId";
    public static final String FIELD_STOCKTAKE_ID = "stocktakeId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_LOT_CODE = "lotCode";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_CONTAINER_ID = "containerId";
    public static final String FIELD_OWNER_TYPE = "ownerType";
    public static final String FIELD_OWNER_ID = "ownerId";
    public static final String FIELD_RESERVED_OBJECT_TYPE = "reservedObjectType";
    public static final String FIELD_RESERVED_OBJECT_ID = "reservedObjectId";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_CURRENT_QUANTITY = "currentQuantity";
    public static final String FIELD_FIRSTCOUNT_MATERIAL_ID = "firstcountMaterialId";
    public static final String FIELD_FIRSTCOUNT_UOM_ID = "firstcountUomId";
    public static final String FIELD_FIRSTCOUNT_LOCATOR_ID = "firstcountLocatorId";
    public static final String FIELD_FIRSTCOUNT_CONTAINER_ID = "firstcountContainerId";
    public static final String FIELD_FIRSTCOUNT_LOCATION_ROW = "firstcountLocationRow";
    public static final String FIELD_FIRSTCOUNT_LOCATION_COLUMN = "firstcountLocationColumn";
    public static final String FIELD_FIRSTCOUNT_OWNER_TYPE = "firstcountOwnerType";
    public static final String FIELD_FIRSTCOUNT_OWNER_ID = "firstcountOwnerId";
    public static final String FIELD_FIRSTCOUNT_RESERVED_OBJECT_TY = "firstcountReservedObjectTy";
    public static final String FIELD_FIRSTCOUNT_RESERVED_OBJECT_ID = "firstcountReservedObjectId";
    public static final String FIELD_FIRSTCOUNT_QUANTITY = "firstcountQuantity";
    public static final String FIELD_FIRSTCOUNT_REMARK = "firstcountRemark";
    public static final String FIELD_RECOUNT_MATERIAL_ID = "recountMaterialId";
    public static final String FIELD_RECOUNT_UOM_ID = "recountUomId";
    public static final String FIELD_RECOUNT_LOCATOR_ID = "recountLocatorId";
    public static final String FIELD_RECOUNT_CONTAINER_ID = "recountContainerId";
    public static final String FIELD_RECOUNT_LOCATION_ROW = "recountLocationRow";
    public static final String FIELD_RECOUNT_LOCATION_COLUMN = "recountLocationColumn";
    public static final String FIELD_RECOUNT_OWNER_TYPE = "recountOwnerType";
    public static final String FIELD_RECOUNT_OWNER_ID = "recountOwnerId";
    public static final String FIELD_RECOUNT_RESERVED_OBJECT_TYPE = "recountReservedObjectType";
    public static final String FIELD_RECOUNT_RESERVED_OBJECT_ID = "recountReservedObjectId";
    public static final String FIELD_RECOUNT_QUANTITY = "recountQuantity";
    public static final String FIELD_RECOUNT_REMARK = "recountRemark";
    public static final String FIELD_ADJUST_FLAG = "adjustFlag";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -4993544327971970589L;

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
    @ApiModelProperty("盘点指令实历史ID")
    @Id
    @Where
    private String stocktakeActualHisId;
    @ApiModelProperty(value = "盘点指令实绩ID", required = true)
    @NotBlank
    @Where
    private String stocktakeActualId;
    @ApiModelProperty(value = "单据ID", required = true)
    @NotBlank
    @Where
    private String stocktakeId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "物料批ID", required = true)
    @NotBlank
    @Where
    private String materialLotId;
    @ApiModelProperty(value = "批次CODE")
    @Where
    private String lotCode;
    @ApiModelProperty(value = "物料ID")
    @Where
    private String materialId;
    @ApiModelProperty(value = "存储类型的货位ID")
    @Where
    private String locatorId;
    @ApiModelProperty(value = "物料批对应的容器ID（最上层）")
    @Where
    private String containerId;
    @ApiModelProperty(value = "所有者类型")
    @Where
    private String ownerType;
    @ApiModelProperty(value = "所有者类型ID")
    @Where
    private String ownerId;
    @ApiModelProperty(value = "预留对象类型")
    @Where
    private String reservedObjectType;
    @ApiModelProperty(value = "预留对象")
    @Where
    private String reservedObjectId;
    @ApiModelProperty(value = "物料批主单位ID")
    @Where
    private String uomId;
    @ApiModelProperty(value = "物料账上数量")
    @Where
    private Double currentQuantity;
    @ApiModelProperty(value = "初盘物料ID")
    @Where
    private String firstcountMaterialId;
    @ApiModelProperty(value = "初盘主单位ID")
    @Where
    private String firstcountUomId;
    @ApiModelProperty(value = "初盘货位ID")
    @Where
    private String firstcountLocatorId;
    @ApiModelProperty(value = "初盘容器ID")
    @Where
    private String firstcountContainerId;
    @ApiModelProperty(value = "初盘容器装载物料对应行")
    @Where
    private Long firstcountLocationRow;
    @ApiModelProperty(value = "初盘容器装载物料对应列")
    @Where
    private Long firstcountLocationColumn;
    @ApiModelProperty(value = "初盘所有者类型")
    @Where
    private String firstcountOwnerType;
    @ApiModelProperty(value = "初盘所有者ID")
    @Where
    private String firstcountOwnerId;
    @ApiModelProperty(value = "初盘预留类型")
    @Where
    private String firstcountReservedObjectTy;
    @ApiModelProperty(value = "初盘预留对象ID")
    @Where
    private String firstcountReservedObjectId;
    @ApiModelProperty(value = "初盘数量，物料批主单位下的数量")
    @Where
    private Double firstcountQuantity;
    @ApiModelProperty(value = "初盘备注")
    @Where
    private String firstcountRemark;
    @ApiModelProperty(value = "复盘物料ID")
    @Where
    private String recountMaterialId;
    @ApiModelProperty(value = "复盘主单位ID")
    @Where
    private String recountUomId;
    @ApiModelProperty(value = "复盘货位ID")
    @Where
    private String recountLocatorId;
    @ApiModelProperty(value = "复盘容器ID")
    @Where
    private String recountContainerId;
    @ApiModelProperty(value = "复盘容器装载物料对应行")
    @Where
    private Long recountLocationRow;
    @ApiModelProperty(value = "复盘容器装载物料对应列")
    @Where
    private Long recountLocationColumn;
    @ApiModelProperty(value = "复盘所有者类型")
    @Where
    private String recountOwnerType;
    @ApiModelProperty(value = "复盘所有者ID")
    @Where
    private String recountOwnerId;
    @ApiModelProperty(value = "复盘预留对象类型")
    @Where
    private String recountReservedObjectType;
    @ApiModelProperty(value = "复盘预留对象")
    @Where
    private String recountReservedObjectId;
    @ApiModelProperty(value = "复盘数量，物料批主单位下的数量")
    @Where
    private Double recountQuantity;
    @ApiModelProperty(value = "复盘备注")
    @Where
    private String recountRemark;
    @ApiModelProperty(value = "调整标识")
    @Where
    private String adjustFlag;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
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
     * @return 盘点指令实历史ID
     */
    public String getStocktakeActualHisId() {
        return stocktakeActualHisId;
    }

    public void setStocktakeActualHisId(String stocktakeActualHisId) {
        this.stocktakeActualHisId = stocktakeActualHisId;
    }

    /**
     * @return 盘点指令实绩ID
     */
    public String getStocktakeActualId() {
        return stocktakeActualId;
    }

    public void setStocktakeActualId(String stocktakeActualId) {
        this.stocktakeActualId = stocktakeActualId;
    }

    /**
     * @return 单据ID
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
     * @return 存储类型的货位ID
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
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
    public Double getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Double currentQuantity) {
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
     * @return 初盘货位ID
     */
    public String getFirstcountLocatorId() {
        return firstcountLocatorId;
    }

    public void setFirstcountLocatorId(String firstcountLocatorId) {
        this.firstcountLocatorId = firstcountLocatorId;
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
    public Double getFirstcountQuantity() {
        return firstcountQuantity;
    }

    public void setFirstcountQuantity(Double firstcountQuantity) {
        this.firstcountQuantity = firstcountQuantity;
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
     * @return 复盘货位ID
     */
    public String getRecountLocatorId() {
        return recountLocatorId;
    }

    public void setRecountLocatorId(String recountLocatorId) {
        this.recountLocatorId = recountLocatorId;
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
    public Double getRecountQuantity() {
        return recountQuantity;
    }

    public void setRecountQuantity(Double recountQuantity) {
        this.recountQuantity = recountQuantity;
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
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

}
