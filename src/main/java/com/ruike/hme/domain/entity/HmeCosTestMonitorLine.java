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
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * COS测试良率监控行表
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:13
 */
@ApiModel("COS测试良率监控行表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "hme_cos_test_monitor_line")
public class HmeCosTestMonitorLine extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COS_MONITOR_LINE_ID = "cosMonitorLineId";
    public static final String FIELD_COS_MONITOR_HEADER_ID = "cosMonitorHeaderId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_LINE_CHECK_STATUS = "lineCheckStatus";
    public static final String FIELD_MATERIAL_LOT_STATUS = "materialLotStatus";
    public static final String FIELD_PASS_DATE = "passDate";
    public static final String FIELD_PASS_BY = "passBy";
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


    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    private String cosMonitorLineId;
    @ApiModelProperty(value = "头表主键", required = true)
    @NotBlank
    private String cosMonitorHeaderId;
    @ApiModelProperty(value = "盒子号", required = true)
    @NotBlank
    private String materialLotId;
    @ApiModelProperty(value = "盒子审核状态", required = true)
    @NotBlank
    private String lineCheckStatus;
    @ApiModelProperty(value = "盒子状态", required = true)
    @NotBlank
    private String materialLotStatus;
    @ApiModelProperty(value = "放行时间")
    private Date passDate;
    @ApiModelProperty(value = "放行人")
    private Long passBy;
    @ApiModelProperty(value = "CID", required = true)
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

//
// 非数据库字段
// ------------------------------------------------------------------------------

//
// getter/setter
// ------------------------------------------------------------------------------

    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public HmeCosTestMonitorLine setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 主键
     */
    public String getCosMonitorLineId() {
        return cosMonitorLineId;
    }

    public HmeCosTestMonitorLine setCosMonitorLineId(String cosMonitorLineId) {
        this.cosMonitorLineId = cosMonitorLineId;
        return this;
    }

    /**
     * @return 头表主键
     */
    public String getCosMonitorHeaderId() {
        return cosMonitorHeaderId;
    }

    public HmeCosTestMonitorLine setCosMonitorHeaderId(String cosMonitorHeaderId) {
        this.cosMonitorHeaderId = cosMonitorHeaderId;
        return this;
    }

    /**
     * @return 盒子号
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public HmeCosTestMonitorLine setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
        return this;
    }

    /**
     * @return 盒子审核状态
     */
    public String getLineCheckStatus() {
        return lineCheckStatus;
    }

    public HmeCosTestMonitorLine setLineCheckStatus(String lineCheckStatus) {
        this.lineCheckStatus = lineCheckStatus;
        return this;
    }

    /**
     * @return 盒子状态
     */
    public String getMaterialLotStatus() {
        return materialLotStatus;
    }

    public HmeCosTestMonitorLine setMaterialLotStatus(String materialLotStatus) {
        this.materialLotStatus = materialLotStatus;
        return this;
    }

    /**
     * @return 放行时间
     */
    public Date getPassDate() {
        return passDate;
    }

    public HmeCosTestMonitorLine setPassDate(Date passDate) {
        this.passDate = passDate;
        return this;
    }

    /**
     * @return 放行人
     */
    public Long getPassBy() {
        return passBy;
    }

    public HmeCosTestMonitorLine setPassBy(Long passBy) {
        this.passBy = passBy;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public HmeCosTestMonitorLine setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public HmeCosTestMonitorLine setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public HmeCosTestMonitorLine setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public HmeCosTestMonitorLine setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public HmeCosTestMonitorLine setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public HmeCosTestMonitorLine setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public HmeCosTestMonitorLine setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public HmeCosTestMonitorLine setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public HmeCosTestMonitorLine setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public HmeCosTestMonitorLine setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public HmeCosTestMonitorLine setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public HmeCosTestMonitorLine setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public HmeCosTestMonitorLine setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public HmeCosTestMonitorLine setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public HmeCosTestMonitorLine setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public HmeCosTestMonitorLine setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public HmeCosTestMonitorLine setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }
}
