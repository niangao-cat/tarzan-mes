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
 * 物料存储属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@ApiModel("物料存储属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_pfep_inventory")
@CustomPrimary
public class MtPfepInventory extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PFEP_INVENTORY_ID = "pfepInventoryId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_IDENTIFY_TYPE = "identifyType";
    public static final String FIELD_IDENTIFY_ID = "identifyId";
    public static final String FIELD_STOCK_LOCATOR_ID = "stockLocatorId";
    public static final String FIELD_PACKAGE_LENGTH = "packageLength";
    public static final String FIELD_PACKAGE_WIDTH = "packageWidth";
    public static final String FIELD_PACKAGE_HEIGHT = "packageHeight";
    public static final String FIELD_PACKAGE_SIZE_UOM_ID = "packageSizeUomId";
    public static final String FIELD_PACKAGE_WEIGHT = "packageWeight";
    public static final String FIELD_WEIGHT_UOM_ID = "weightUomId";
    public static final String FIELD_MAX_STOCK_QTY = "maxStockQty";
    public static final String FIELD_MIN_STOCK_QTY = "minStockQty";
    public static final String FIELD_ISSUED_LOCATOR_ID = "issuedLocatorId";
    public static final String FIELD_COMPLETION_LOCATOR_ID = "completionLocatorId";
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
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    @Where
    private String pfepInventoryId;
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
    @ApiModelProperty(value = "存储标识类型，如无标识、容器标识、物料批标识等")
    @Where
    private String identifyType;
    @ApiModelProperty(value = "标识模板，关联物料默认使用的标识模板")
    @Where
    private String identifyId;
    @ApiModelProperty(value = "默认存储库位")
    @Where
    private String stockLocatorId;
    @ApiModelProperty(value = "存储包装长")
    @Where
    private Double packageLength;
    @ApiModelProperty(value = "存储包装宽")
    @Where
    private Double packageWidth;
    @ApiModelProperty(value = "存储包装高")
    @Where
    private Double packageHeight;
    @ApiModelProperty(value = "包装尺寸单位，如MMCMM等，与单位维护保持一致")
    @Where
    private String packageSizeUomId;
    @ApiModelProperty(value = "存储包装重量")
    @Where
    private Double packageWeight;
    @ApiModelProperty(value = "重量单位，如KG/G/T等")
    @Where
    private String weightUomId;
    @ApiModelProperty(value = "最大存储库存")
    @Where
    private Double maxStockQty;
    @ApiModelProperty(value = "最小存储库存")
    @Where
    private Double minStockQty;
    @ApiModelProperty(value = "默认发料库位")
    @Where
    private String issuedLocatorId;
    @ApiModelProperty(value = "默认完工库位")
    @Where
    private String completionLocatorId;
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
     * @return 主键ID，表示唯一一条记录
     */
    public String getPfepInventoryId() {
        return pfepInventoryId;
    }

    public void setPfepInventoryId(String pfepInventoryId) {
        this.pfepInventoryId = pfepInventoryId;
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
     * @return 存储标识类型，如无标识、容器标识、物料批标识等
     */
    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    /**
     * @return 标识模板，关联物料默认使用的标识模板
     */
    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    /**
     * @return 默认存储库位
     */
    public String getStockLocatorId() {
        return stockLocatorId;
    }

    public void setStockLocatorId(String stockLocatorId) {
        this.stockLocatorId = stockLocatorId;
    }

    /**
     * @return 存储包装长
     */
    public Double getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(Double packageLength) {
        this.packageLength = packageLength;
    }

    /**
     * @return 存储包装宽
     */
    public Double getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(Double packageWidth) {
        this.packageWidth = packageWidth;
    }

    /**
     * @return 存储包装高
     */
    public Double getPackageHeight() {
        return packageHeight;
    }

    public void setPackageHeight(Double packageHeight) {
        this.packageHeight = packageHeight;
    }

    /**
     * @return 包装尺寸单位，如MMCMM等，与单位维护保持一致
     */
    public String getPackageSizeUomId() {
        return packageSizeUomId;
    }

    public void setPackageSizeUomId(String packageSizeUomId) {
        this.packageSizeUomId = packageSizeUomId;
    }

    /**
     * @return 存储包装重量
     */
    public Double getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(Double packageWeight) {
        this.packageWeight = packageWeight;
    }

    /**
     * @return 重量单位，如KG/G/T等
     */
    public String getWeightUomId() {
        return weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }

    /**
     * @return 最大存储库存
     */
    public Double getMaxStockQty() {
        return maxStockQty;
    }

    public void setMaxStockQty(Double maxStockQty) {
        this.maxStockQty = maxStockQty;
    }

    /**
     * @return 最小存储库存
     */
    public Double getMinStockQty() {
        return minStockQty;
    }

    public void setMinStockQty(Double minStockQty) {
        this.minStockQty = minStockQty;
    }

    /**
     * @return 默认发料库位
     */
    public String getIssuedLocatorId() {
        return issuedLocatorId;
    }

    public void setIssuedLocatorId(String issuedLocatorId) {
        this.issuedLocatorId = issuedLocatorId;
    }

    /**
     * @return 默认完工库位
     */
    public String getCompletionLocatorId() {
        return completionLocatorId;
    }

    public void setCompletionLocatorId(String completionLocatorId) {
        this.completionLocatorId = completionLocatorId;
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
