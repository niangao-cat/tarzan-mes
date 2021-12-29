package com.ruike.wms.domain.entity;

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
 * 配送基础数据表
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 21:05:25
 */
@ApiModel("配送基础数据表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_distribution_basic_data")
public class WmsDistributionBasicData extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_HEADER_ID = "headerId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_MATERIAL_GROUP_ID = "materialGroupId";
    public static final String FIELD_DISTRIBUTION_TYPE = "distributionType";
    public static final String FIELD_PROPORTION = "proportion";
    public static final String FIELD_INVENTORY_LEVEL = "inventoryLevel";
    public static final String FIELD_INVENTORY_LEVEL_UOM = "inventoryLevelUom";
    public static final String FIELD_EVERY_QTY = "everyQty";
    public static final String FIELD_ONE_QTY = "oneQty";
    public static final String FIELD_ONE_TIME = "oneTime";
    public static final String FIELD_ONE_UOM = "oneUom";
    public static final String FIELD_MINIMUM_PACKAGE_QTY = "minimumPackageQty";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
//    @GeneratedValue
    private String headerId;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "物料组ID")
    private String materialGroupId;
    @ApiModelProperty(value = "策略类型")
    private String distributionType;
    @ApiModelProperty(value = "比例")
    private BigDecimal proportion;
    @ApiModelProperty(value = "库存水位")
    private BigDecimal inventoryLevel;
    @ApiModelProperty(value = "单位")
    private String inventoryLevelUom;
    @ApiModelProperty(value = "安全库存配送量")
    private BigDecimal everyQty;
    @ApiModelProperty(value = "单次配送量")
    private BigDecimal oneQty;
    @ApiModelProperty(value = "单次配送时间")
    private Date oneTime;
    @ApiModelProperty(value = "单次配送单位")
    private String oneUom;
    @ApiModelProperty(value = "最小包装量")
    private BigDecimal minimumPackageQty;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
    @ApiModelProperty(value = "CID", required = true)
    @Cid
    private Long cid;
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
     * @return 主键
     */
    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
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
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 物料组ID
     */
    public String getMaterialGroupId() {
        return materialGroupId;
    }

    public void setMaterialGroupId(String materialGroupId) {
        this.materialGroupId = materialGroupId;
    }

    /**
     * @return 策略类型
     */
    public String getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType;
    }

    /**
     * @return 比例
     */
    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    /**
     * @return 库存水位
     */
    public BigDecimal getInventoryLevel() {
        return inventoryLevel;
    }

    public void setInventoryLevel(BigDecimal inventoryLevel) {
        this.inventoryLevel = inventoryLevel;
    }

    /**
     * @return 单位
     */
    public String getInventoryLevelUom() {
        return inventoryLevelUom;
    }

    public void setInventoryLevelUom(String inventoryLevelUom) {
        this.inventoryLevelUom = inventoryLevelUom;
    }

    /**
     * @return 安全库存配送量
     */
    public BigDecimal getEveryQty() {
        return everyQty;
    }

    public void setEveryQty(BigDecimal everyQty) {
        this.everyQty = everyQty;
    }

    /**
     * @return 单次配送量
     */
    public BigDecimal getOneQty() {
        return oneQty;
    }

    public void setOneQty(BigDecimal oneQty) {
        this.oneQty = oneQty;
    }

    /**
     * @return 单次配送时间
     */
    public Date getOneTime() {
        return oneTime;
    }

    public void setOneTime(Date oneTime) {
        this.oneTime = oneTime;
    }

    /**
     * @return 单次配送单位
     */
    public String getOneUom() {
        return oneUom;
    }

    public void setOneUom(String oneUom) {
        this.oneUom = oneUom;
    }

    /**
     * @return 最小包装量
     */
    public BigDecimal getMinimumPackageQty() {
        return minimumPackageQty;
    }

    public void setMinimumPackageQty(BigDecimal minimumPackageQty) {
        this.minimumPackageQty = minimumPackageQty;
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

    public String getMaterialVersion() {
        return materialVersion;
    }

    public void setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
    }
}
