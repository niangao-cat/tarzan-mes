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
 * 物料发运属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@ApiModel("物料发运属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_pfep_shipping")
@CustomPrimary
public class MtPfepShipping extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PFEP_SHIPPING_ID = "pfepShippingId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_CUSTOMER_SITE_ID = "customerSiteId";
    public static final String FIELD_SHIPPING_CHANNEL = "shippingChannel";
    public static final String FIELD_CUSTOMER_GOODS_CODE = "customerGoodsCode";
    public static final String FIELD_PACKAGE_LIST_ID = "packageListId";
    public static final String FIELD_ATTACHMENT_LIST_ID = "attachmentListId";
    public static final String FIELD_TRANSPORT_MODE = "transportMode";
    public static final String FIELD_TRANSPORT_COMPANY = "transportCompany";
    public static final String FIELD_SHIPPING_CALENDAR_ID = "shippingCalendarId";
    public static final String FIELD_PACKAGE_STRATEGY = "packageStrategy";
    public static final String FIELD_PACKAGE_IDENTIFY_ID = "packageIdentifyId";
    public static final String FIELD_PRODUCT_IDENTIFY_ID = "productIdentifyId";
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
    private String pfepShippingId;
    @ApiModelProperty(value = "物料站点主键，标识唯一物料站点对应关系，限定为生产站点", required = true)
    @NotBlank
    @Where
    private String materialSiteId;
    @ApiModelProperty(value = "组织类型，可选计划站点下区域、生产线、工作单元等类型")
    @Where
    private String organizationType;
    @ApiModelProperty(value = "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")
    @Where
    private String organizationId;
    @ApiModelProperty(value = "客户")
    @Where
    private String customerId;
    @ApiModelProperty(value = "客户地点")
    @Where
    private String customerSiteId;
    @ApiModelProperty(value = "分销渠道")
    @Where
    private String shippingChannel;
    @ApiModelProperty(value = "客户物料号")
    @Where
    private String customerGoodsCode;
    @ApiModelProperty(value = "包装清单，关联包装类型装配清单")
    @Where
    private String packageListId;
    @ApiModelProperty(value = "附件清单，关联附件类型装配清单")
    @Where
    private String attachmentListId;
    @ApiModelProperty(value = "运输方式")
    @Where
    private String transportMode;
    @ApiModelProperty(value = "运输公司")
    @Where
    private String transportCompany;
    @ApiModelProperty(value = "发货日历")
    @Where
    private String shippingCalendarId;
    @ApiModelProperty(value = "包装策略")
    @Where
    private String packageStrategy;
    @ApiModelProperty(value = "包装标识，关联标识模板")
    @Where
    private String packageIdentifyId;
    @ApiModelProperty(value = "产品标识，关联产品模板")
    @Where
    private String productIdentifyId;
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
    public String getPfepShippingId() {
        return pfepShippingId;
    }

    public void setPfepShippingId(String pfepShippingId) {
        this.pfepShippingId = pfepShippingId;
    }

    /**
     * @return 物料站点主键，标识唯一物料站点对应关系，限定为生产站点
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
     * @return 客户
     */
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return 客户地点
     */
    public String getCustomerSiteId() {
        return customerSiteId;
    }

    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    /**
     * @return 分销渠道
     */
    public String getShippingChannel() {
        return shippingChannel;
    }

    public void setShippingChannel(String shippingChannel) {
        this.shippingChannel = shippingChannel;
    }

    /**
     * @return 客户物料号
     */
    public String getCustomerGoodsCode() {
        return customerGoodsCode;
    }

    public void setCustomerGoodsCode(String customerGoodsCode) {
        this.customerGoodsCode = customerGoodsCode;
    }

    /**
     * @return 包装清单，关联包装类型装配清单
     */
    public String getPackageListId() {
        return packageListId;
    }

    public void setPackageListId(String packageListId) {
        this.packageListId = packageListId;
    }

    /**
     * @return 附件清单，关联附件类型装配清单
     */
    public String getAttachmentListId() {
        return attachmentListId;
    }

    public void setAttachmentListId(String attachmentListId) {
        this.attachmentListId = attachmentListId;
    }

    /**
     * @return 运输方式
     */
    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * @return 运输公司
     */
    public String getTransportCompany() {
        return transportCompany;
    }

    public void setTransportCompany(String transportCompany) {
        this.transportCompany = transportCompany;
    }

    /**
     * @return 发货日历
     */
    public String getShippingCalendarId() {
        return shippingCalendarId;
    }

    public void setShippingCalendarId(String shippingCalendarId) {
        this.shippingCalendarId = shippingCalendarId;
    }

    /**
     * @return 包装策略
     */
    public String getPackageStrategy() {
        return packageStrategy;
    }

    public void setPackageStrategy(String packageStrategy) {
        this.packageStrategy = packageStrategy;
    }

    /**
     * @return 包装标识，关联标识模板
     */
    public String getPackageIdentifyId() {
        return packageIdentifyId;
    }

    public void setPackageIdentifyId(String packageIdentifyId) {
        this.packageIdentifyId = packageIdentifyId;
    }

    /**
     * @return 产品标识，关联产品模板
     */
    public String getProductIdentifyId() {
        return productIdentifyId;
    }

    public void setProductIdentifyId(String productIdentifyId) {
        this.productIdentifyId = productIdentifyId;
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
