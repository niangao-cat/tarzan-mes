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
 * 物料采购属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@ApiModel("物料采购属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_pfep_purchase")
@CustomPrimary
public class MtPfepPurchase extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PFEP_PURCHASE_ID = "pfepPurchaseId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_RECEIVE_LOCATOR_ID = "receiveLocatorId";
    public static final String FIELD_ECONOMIC_LOT_SIZE = "economicLotSize";
    public static final String FIELD_ECONOMIC_SPLIT_PARAMETER = "economicSplitParameter";
    public static final String FIELD_AUTO_LOCK_TIME = "autoLockTime";
    public static final String FIELD_LIMIT_HOUR = "limitHour";
    public static final String FIELD_PAST_DY_NOTIFY_CLOSE_TIME = "pastDyNotifyCloseTime";
    public static final String FIELD_PURCHASE_CYCLE = "purchaseCycle";
    public static final String FIELD_MIN_PACKAGE_QTY = "minPackageQty";
    public static final String FIELD_MIN_PURCHASE_QTY = "minPurchaseQty";
    public static final String FIELD_RELEASE_TIME_FENCE = "releaseTimeFence";
    public static final String FIELD_COLLECT_TYPE = "collectType";
    public static final String FIELD_REQUIREMENT_TYPE = "requirementType";
    public static final String FIELD_DLY_CANLENDER_TYPE = "dlyCanlenderType";
    public static final String FIELD_NOTIFY_AUTO_CAL_TYPE = "notifyAutoCalType";
    public static final String FIELD_REQUIREMENT_LEAD_TIME = "requirementLeadTime";
    public static final String FIELD_PAST_REQUIREMENT_TIME = "pastRequirementTime";
    public static final String FIELD_PAST_DELIVERY_NOTIFY_TIME = "pastDeliveryNotifyTime";
    public static final String FIELD_PAST_DELIVERY_TICKET_TIME = "pastDeliveryTicketTime";
    public static final String FIELD_ONHAND_QTY_MODEL = "onhandQtyModel";
    public static final String FIELD_ISSUE_TYPE = "issueType";
    public static final String FIELD_AUTO_PERCENT = "autoPercent";
    public static final String FIELD_BUYER = "buyer";
    public static final String FIELD_VMI_FLAG = "vmiFlag";
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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String pfepPurchaseId;
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
    @ApiModelProperty(value = "默认接收库位，物料采购时默认接收的目标库位")
    @Where
    private String receiveLocatorId;
    @ApiModelProperty(value = "经济批量")
    @Where
    private Double economicLotSize;
    @ApiModelProperty(value = "经济批量舍入值%")
    @Where
    private Double economicSplitParameter;
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
    @ApiModelProperty(value = "物料采购时最小包装数量")
    @Where
    private Double minPackageQty;
    @ApiModelProperty(value = "起订量，物料采购时最小数量")
    @Where
    private Double minPurchaseQty;
    @ApiModelProperty(value = "下达时间，制单提前时间")
    @Where
    private Double releaseTimeFence;
    @ApiModelProperty(value = "汇总模型，标识物料的采购维度，如按区域采购，按站点采购等")
    @Where
    private String collectType;
    @ApiModelProperty(value = "需求模型，标识需求计算方法")
    @Where
    private String requirementType;
    @ApiModelProperty(value = "班次模型")
    @Where
    private String dlyCanlenderType;
    @ApiModelProperty(value = "自动计算模型")
    @Where
    private String notifyAutoCalType;
    @ApiModelProperty(value = "需求提前期")
    @Where
    private Double requirementLeadTime;
    @ApiModelProperty(value = "过期需求，需求过期时间")
    @Where
    private Double pastRequirementTime;
    @ApiModelProperty(value = "过期送货通知，送货通知过期时间")
    @Where
    private Double pastDeliveryNotifyTime;
    @ApiModelProperty(value = "过期送货单，送货单过期时间")
    @Where
    private Double pastDeliveryTicketTime;
    @ApiModelProperty(value = "推演现有量模型")
    @Where
    private String onhandQtyModel;
    @ApiModelProperty(value = "下达类型")
    @Where
    private String issueType;
    @ApiModelProperty(value = "自动计算允差百分比，标识相对供货比例允许的差异范围")
    @Where
    private Double autoPercent;
    @ApiModelProperty(value = "采购员")
    @Where
    private String buyer;
    @ApiModelProperty(value = "寄售标识")
    @Where
    private String vmiFlag;
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getPfepPurchaseId() {
        return pfepPurchaseId;
    }

    public void setPfepPurchaseId(String pfepPurchaseId) {
        this.pfepPurchaseId = pfepPurchaseId;
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
     * @return 默认接收库位，物料采购时默认接收的目标库位
     */
    public String getReceiveLocatorId() {
        return receiveLocatorId;
    }

    public void setReceiveLocatorId(String receiveLocatorId) {
        this.receiveLocatorId = receiveLocatorId;
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
     * @return 下达时间，制单提前时间
     */
    public Double getReleaseTimeFence() {
        return releaseTimeFence;
    }

    public void setReleaseTimeFence(Double releaseTimeFence) {
        this.releaseTimeFence = releaseTimeFence;
    }

    /**
     * @return 汇总模型，标识物料的采购维度，如按区域采购，按站点采购等
     */
    public String getCollectType() {
        return collectType;
    }

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    /**
     * @return 需求模型，标识需求计算方法
     */
    public String getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType;
    }

    /**
     * @return 班次模型
     */
    public String getDlyCanlenderType() {
        return dlyCanlenderType;
    }

    public void setDlyCanlenderType(String dlyCanlenderType) {
        this.dlyCanlenderType = dlyCanlenderType;
    }

    /**
     * @return 自动计算模型
     */
    public String getNotifyAutoCalType() {
        return notifyAutoCalType;
    }

    public void setNotifyAutoCalType(String notifyAutoCalType) {
        this.notifyAutoCalType = notifyAutoCalType;
    }

    /**
     * @return 需求提前期
     */
    public Double getRequirementLeadTime() {
        return requirementLeadTime;
    }

    public void setRequirementLeadTime(Double requirementLeadTime) {
        this.requirementLeadTime = requirementLeadTime;
    }

    /**
     * @return 过期需求，需求过期时间
     */
    public Double getPastRequirementTime() {
        return pastRequirementTime;
    }

    public void setPastRequirementTime(Double pastRequirementTime) {
        this.pastRequirementTime = pastRequirementTime;
    }

    /**
     * @return 过期送货通知，送货通知过期时间
     */
    public Double getPastDeliveryNotifyTime() {
        return pastDeliveryNotifyTime;
    }

    public void setPastDeliveryNotifyTime(Double pastDeliveryNotifyTime) {
        this.pastDeliveryNotifyTime = pastDeliveryNotifyTime;
    }

    /**
     * @return 过期送货单，送货单过期时间
     */
    public Double getPastDeliveryTicketTime() {
        return pastDeliveryTicketTime;
    }

    public void setPastDeliveryTicketTime(Double pastDeliveryTicketTime) {
        this.pastDeliveryTicketTime = pastDeliveryTicketTime;
    }

    /**
     * @return 推演现有量模型
     */
    public String getOnhandQtyModel() {
        return onhandQtyModel;
    }

    public void setOnhandQtyModel(String onhandQtyModel) {
        this.onhandQtyModel = onhandQtyModel;
    }

    /**
     * @return 下达类型
     */
    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    /**
     * @return 自动计算允差百分比，标识相对供货比例允许的差异范围
     */
    public Double getAutoPercent() {
        return autoPercent;
    }

    public void setAutoPercent(Double autoPercent) {
        this.autoPercent = autoPercent;
    }

    /**
     * @return 采购员
     */
    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    /**
     * @return 寄售标识
     */
    public String getVmiFlag() {
        return vmiFlag;
    }

    public void setVmiFlag(String vmiFlag) {
        this.vmiFlag = vmiFlag;
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
