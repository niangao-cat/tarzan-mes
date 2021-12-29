package tarzan.material.domain.entity;

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
 * 物料供应商采购属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@ApiModel("物料供应商采购属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_pfep_purchase_supplier")
@CustomPrimary
public class MtPfepPurchaseSupplier extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PFEP_PURCHASE_SUPPLIER_ID = "pfepPurchaseSupplierId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_RECEIVE_LOCATOR_ID = "receiveLocatorId";
    public static final String FIELD_VISIBLE_FLAG = "visibleFlag";
    public static final String FIELD_ECONOMIC_LOT_SIZE = "economicLotSize";
    public static final String FIELD_ECONOMIC_SPLIT_PARAMETER = "economicSplitParameter";
    public static final String FIELD_MIN_PACKAGE_QTY = "minPackageQty";
    public static final String FIELD_MIN_PURCHASE_QTY = "minPurchaseQty";
    public static final String FIELD_MAX_DAILY_SUPPLY_QTY = "maxDailySupplyQty";
    public static final String FIELD_SUPPLIER_CALENDAR_ID = "supplierCalendarId";
    public static final String FIELD_RELEASE_TIME_FENCE = "releaseTimeFence";
    public static final String FIELD_AUTO_LOCK_TIME = "autoLockTime";
    public static final String FIELD_LIMIT_HOUR = "limitHour";
    public static final String FIELD_PAST_DY_NOTIFY_CLOSE_TIME = "pastDyNotifyCloseTime";
    public static final String FIELD_PURCHASE_CYCLE = "purchaseCycle";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";

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
    @ApiModelProperty("主键ID,标识唯一一条记录")
    @Id
    @Where
    private String pfepPurchaseSupplierId;
    @ApiModelProperty(value = "物料站点主键，标识唯一物料站点对应关系，限定为采购站点", required = true)
    @NotBlank
    @Where
    private String materialSiteId;
    @ApiModelProperty(value = "组织类型，可选计划站点下区域、生产线、工作单元等类型")
    @Where
    private String organizationType;
    @ApiModelProperty(value = "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")
    @Where
    private String organizationId;
    @ApiModelProperty(value = "供应商，标识唯一供应商", required = true)
    @NotBlank
    @Where
    private String supplierId;
    @ApiModelProperty(value = "供应商地点，标识唯一供应商地点")
    @Where
    private String supplierSiteId;
    @ApiModelProperty(value = "默认接收库位，物料采购时默认接收的目标库位")
    @Where
    private String receiveLocatorId;
    @ApiModelProperty(value = "优先级，标识物料在多个供应商上的选择顺序，数值越小优先级越高")
    @Where
    private String visibleFlag;
    @ApiModelProperty(value = "经济批量")
    @Where
    private Double economicLotSize;
    @ApiModelProperty(value = "经济批量舍入值%")
    @Where
    private Double economicSplitParameter;
    @ApiModelProperty(value = "物料采购时最小包装数量")
    @Where
    private Double minPackageQty;
    @ApiModelProperty(value = "起订量，物料采购时最小数量")
    @Where
    private Double minPurchaseQty;
    @ApiModelProperty(value = "最大日供货量")
    @Where
    private Double maxDailySupplyQty;
    @ApiModelProperty(value = "供应商日历")
    @Where
    private String supplierCalendarId;
    @ApiModelProperty(value = "下达时间，制单提前时间")
    @Where
    private Double releaseTimeFence;
    @ApiModelProperty(value = "自动计算锁定时间")
    @Where
    private Double autoLockTime;
    @ApiModelProperty(value = "限制提前制单时间")
    @Where
    private Double limitHour;
    @ApiModelProperty(value = "过期送货通知关闭时间")
    @Where
    private Double pastDyNotifyCloseTime;
    @ApiModelProperty(value = "采购周期")
    @Where
    private Double purchaseCycle;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
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
     * @return 主键ID,标识唯一一条记录
     */
    public String getPfepPurchaseSupplierId() {
        return pfepPurchaseSupplierId;
    }

    public void setPfepPurchaseSupplierId(String pfepPurchaseSupplierId) {
        this.pfepPurchaseSupplierId = pfepPurchaseSupplierId;
    }

    /**
     * @return 物料站点主键，标识唯一物料站点对应关系，限定为采购站点
     */
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 组织类型，可选计划站点下区域、生产线、工作单元等类型
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return 供应商，标识唯一供应商
     */
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return 供应商地点，标识唯一供应商地点
     */
    public String getSupplierSiteId() {
        return supplierSiteId;
    }

    public void setSupplierSiteId(String supplierSiteId) {
        this.supplierSiteId = supplierSiteId;
    }

    /**
     * @return 默认接收库位，物料采购时默认接收的目标库位
     */
    public String getReceiveLocatorId() {
        return receiveLocatorId;
    }

    public void setReceiveLocatorId(String receiveLocatorId) {
        this.receiveLocatorId = receiveLocatorId;
    }

    /**
     * @return 优先级，标识物料在多个供应商上的选择顺序，数值越小优先级越高
     */
    public String getVisibleFlag() {
        return visibleFlag;
    }

    public void setVisibleFlag(String visibleFlag) {
        this.visibleFlag = visibleFlag;
    }

    /**
     * @return 经济批量
     */
    public Double getEconomicLotSize() {
        return economicLotSize;
    }

    public void setEconomicLotSize(Double economicLotSize) {
        this.economicLotSize = economicLotSize;
    }

    /**
     * @return 经济批量舍入值%
     */
    public Double getEconomicSplitParameter() {
        return economicSplitParameter;
    }

    public void setEconomicSplitParameter(Double economicSplitParameter) {
        this.economicSplitParameter = economicSplitParameter;
    }

    /**
     * @return 物料采购时最小包装数量
     */
    public Double getMinPackageQty() {
        return minPackageQty;
    }

    public void setMinPackageQty(Double minPackageQty) {
        this.minPackageQty = minPackageQty;
    }

    /**
     * @return 起订量，物料采购时最小数量
     */
    public Double getMinPurchaseQty() {
        return minPurchaseQty;
    }

    public void setMinPurchaseQty(Double minPurchaseQty) {
        this.minPurchaseQty = minPurchaseQty;
    }

    /**
     * @return 最大日供货量
     */
    public Double getMaxDailySupplyQty() {
        return maxDailySupplyQty;
    }

    public void setMaxDailySupplyQty(Double maxDailySupplyQty) {
        this.maxDailySupplyQty = maxDailySupplyQty;
    }

    /**
     * @return 供应商日历
     */
    public String getSupplierCalendarId() {
        return supplierCalendarId;
    }

    public void setSupplierCalendarId(String supplierCalendarId) {
        this.supplierCalendarId = supplierCalendarId;
    }

    /**
     * @return 下达时间，制单提前时间
     */
    public Double getReleaseTimeFence() {
        return releaseTimeFence;
    }

    public void setReleaseTimeFence(Double releaseTimeFence) {
        this.releaseTimeFence = releaseTimeFence;
    }

    /**
     * @return 自动计算锁定时间
     */
    public Double getAutoLockTime() {
        return autoLockTime;
    }

    public void setAutoLockTime(Double autoLockTime) {
        this.autoLockTime = autoLockTime;
    }

    /**
     * @return 限制提前制单时间
     */
    public Double getLimitHour() {
        return limitHour;
    }

    public void setLimitHour(Double limitHour) {
        this.limitHour = limitHour;
    }

    /**
     * @return 过期送货通知关闭时间
     */
    public Double getPastDyNotifyCloseTime() {
        return pastDyNotifyCloseTime;
    }

    public void setPastDyNotifyCloseTime(Double pastDyNotifyCloseTime) {
        this.pastDyNotifyCloseTime = pastDyNotifyCloseTime;
    }

    /**
     * @return 采购周期
     */
    public Double getPurchaseCycle() {
        return purchaseCycle;
    }

    public void setPurchaseCycle(Double purchaseCycle) {
        this.purchaseCycle = purchaseCycle;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
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
