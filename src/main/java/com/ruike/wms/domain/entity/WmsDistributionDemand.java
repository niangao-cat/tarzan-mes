package com.ruike.wms.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;
import java.util.Objects;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * 配送需求表
 *
 * @author yonghui.zhu@hand-china.com 2020-08-31 11:09:51
 */
@ApiModel("配送需求表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_distribution_demand")
@CustomPrimary
public class WmsDistributionDemand extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DIST_DEMAND_ID = "distDemandId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_DISTRIBUTION_BASIC_ID = "distributionBasicId";
    public static final String FIELD_DISTRIBUTION_TYPE = "distributionType";
    public static final String FIELD_REQUIREMENT_QTY = "requirementQty";
    public static final String FIELD_CALENDAR_SHIFT_ID = "calendarShiftId";
    public static final String FIELD_SO_NUM = "soNum";
    public static final String FIELD_SO_LINE_NUM = "soLineNum";
    public static final String FIELD_STATUS = "status";
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
    public static final String FIELD_ATTRIBUTE16 = "attribute16";
    public static final String FIELD_ATTRIBUTE17 = "attribute17";
    public static final String FIELD_ATTRIBUTE18 = "attribute18";
    public static final String FIELD_ATTRIBUTE19 = "attribute19";
    public static final String FIELD_ATTRIBUTE20 = "attribute20";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID")
    @Id
    private String distDemandId;
    @ApiModelProperty(value = "组件物料ID")
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "单位ID")
    @NotBlank
    private String uomId;
    @NotBlank
    @ApiModelProperty(value = "工厂")
    private String siteId;
    @ApiModelProperty(value = "产品版本")
    private String materialVersion;
    @ApiModelProperty(value = "生产线ID")
    @NotBlank
    private String prodLineId;
    @ApiModelProperty(value = "工段（工作单元）ID")
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "配送基础属性ID")
    private String distributionBasicId;
    @ApiModelProperty(value = "策略类型")
    private String distributionType;
    @ApiModelProperty(value = "总需求数量")
    @NotNull
    private BigDecimal requirementQty;
    @ApiModelProperty(value = "班次")
    @NotBlank
    private String calendarShiftId;
    @ApiModelProperty(value = "班次编码", required = true)
    private String shiftCode;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "状态(新建/下达/完成/关闭)", required = true)
    @NotBlank
    @LovValue(lovCode = "WMS.DISTRIBUTION_DEMAND", meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "", required = true)
    @NotNull
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
    @ApiModelProperty(value = "")
    private String attribute16;
    @ApiModelProperty(value = "")
    private String attribute17;
    @ApiModelProperty(value = "")
    private String attribute18;
    @ApiModelProperty(value = "")
    private String attribute19;
    @ApiModelProperty(value = "")
    private String attribute20;

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
     * @return 主键ID
     */
    public String getDistDemandId() {
        return distDemandId;
    }

    public void setDistDemandId(String distDemandId) {
        this.distDemandId = distDemandId;
    }

    /**
     * @return 组件物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 工厂
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 产品版本
     */
    public String getMaterialVersion() {
        return materialVersion;
    }

    public void setMaterialVersion(String materialVersion) {
        this.materialVersion = materialVersion;
    }

    /**
     * @return 生产线ID
     */
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    /**
     * @return 工段（工作单元）ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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
     * @return 总需求数量
     */
    public BigDecimal getRequirementQty() {
        return requirementQty;
    }

    public void setRequirementQty(BigDecimal requirementQty) {
        this.requirementQty = requirementQty;
    }

    /**
     * @return 班次
     */
    public String getCalendarShiftId() {
        return calendarShiftId;
    }

    public void setCalendarShiftId(String calendarShiftId) {
        this.calendarShiftId = calendarShiftId;
    }

    /**
     * @return 销售订单号
     */
    public String getSoNum() {
        return soNum;
    }

    public void setSoNum(String soNum) {
        this.soNum = soNum;
    }

    /**
     * @return 销售订单行号
     */
    public String getSoLineNum() {
        return soLineNum;
    }

    public void setSoLineNum(String soLineNum) {
        this.soLineNum = soLineNum;
    }

    /**
     * @return 状态(新建 / 下达 / 完成 / 关闭)
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    /**
     * @return
     */
    public String getAttribute16() {
        return attribute16;
    }

    public void setAttribute16(String attribute16) {
        this.attribute16 = attribute16;
    }

    /**
     * @return
     */
    public String getAttribute17() {
        return attribute17;
    }

    public void setAttribute17(String attribute17) {
        this.attribute17 = attribute17;
    }

    /**
     * @return
     */
    public String getAttribute18() {
        return attribute18;
    }

    public void setAttribute18(String attribute18) {
        this.attribute18 = attribute18;
    }

    /**
     * @return
     */
    public String getAttribute19() {
        return attribute19;
    }

    public void setAttribute19(String attribute19) {
        this.attribute19 = attribute19;
    }

    /**
     * @return
     */
    public String getAttribute20() {
        return attribute20;
    }

    public void setAttribute20(String attribute20) {
        this.attribute20 = attribute20;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getDistributionBasicId() {
        return distributionBasicId;
    }

    public void setDistributionBasicId(String distributionBasicId) {
        this.distributionBasicId = distributionBasicId;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public WmsDistributionDemand setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WmsDistributionDemand that = (WmsDistributionDemand) o;
        return Objects.equals(tenantId, that.tenantId) &&
                Objects.equals(distDemandId, that.distDemandId) &&
                Objects.equals(materialId, that.materialId) &&
                Objects.equals(uomId, that.uomId) &&
                Objects.equals(siteId, that.siteId) &&
                Objects.equals(materialVersion, that.materialVersion) &&
                Objects.equals(prodLineId, that.prodLineId) &&
                Objects.equals(workcellId, that.workcellId) &&
                Objects.equals(distributionType, that.distributionType) &&
                Objects.equals(calendarShiftId, that.calendarShiftId) &&
                Objects.equals(soNum, that.soNum) &&
                Objects.equals(soLineNum, that.soLineNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, distDemandId, materialId, uomId, siteId, materialVersion, prodLineId, workcellId, distributionType, calendarShiftId, soNum, soLineNum);
    }
}
