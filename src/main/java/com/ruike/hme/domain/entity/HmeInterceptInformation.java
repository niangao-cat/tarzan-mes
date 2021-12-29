package com.ruike.hme.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 拦截单信息表
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:07
 */
@ApiModel("拦截单信息表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "hme_intercept_information")
public class HmeInterceptInformation extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INTERCEPT_ID = "interceptId";
    public static final String FIELD_INTERCEPT_NUM = "interceptNum";
    public static final String FIELD_DIMENSION = "dimension";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_INTERCEPT_BY = "interceptBy";
    public static final String FIELD_INTERCEPT_DATE = "interceptDate";
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
    @ApiModelProperty("信息表主键ID")
    @Id
    private String interceptId;
    @ApiModelProperty(value = "拦截单号", required = true)
    @NotBlank
    private String interceptNum;
    @ApiModelProperty(value = "拦截维度", required = true)
    @NotBlank
    private String dimension;
    @ApiModelProperty(value = "状态", required = true)
    @NotBlank
    private String status;
    @ApiModelProperty(value = "拦截消息", required = true)
    @NotBlank
    private String remark;
    @ApiModelProperty(value = "拦截人", required = true)
    @NotNull
    private Long interceptBy;
    @ApiModelProperty(value = "拦截时间", required = true)
    @NotNull
    private Date interceptDate;
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

    public HmeInterceptInformation setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 信息表主键ID
     */
    public String getInterceptId() {
        return interceptId;
    }

    public HmeInterceptInformation setInterceptId(String interceptId) {
        this.interceptId = interceptId;
        return this;
    }

    /**
     * @return 拦截单号
     */
    public String getInterceptNum() {
        return interceptNum;
    }

    public HmeInterceptInformation setInterceptNum(String interceptNum) {
        this.interceptNum = interceptNum;
        return this;
    }

    /**
     * @return 拦截维度
     */
    public String getDimension() {
        return dimension;
    }

    public HmeInterceptInformation setDimension(String dimension) {
        this.dimension = dimension;
        return this;
    }

    /**
     * @return 状态
     */
    public String getStatus() {
        return status;
    }

    public HmeInterceptInformation setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * @return 拦截消息
     */
    public String getRemark() {
        return remark;
    }

    public HmeInterceptInformation setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    /**
     * @return 拦截人
     */
    public Long getInterceptBy() {
        return interceptBy;
    }

    public HmeInterceptInformation setInterceptBy(Long interceptBy) {
        this.interceptBy = interceptBy;
        return this;
    }

    /**
     * @return 拦截时间
     */
    public Date getInterceptDate() {
        return interceptDate;
    }

    public HmeInterceptInformation setInterceptDate(Date interceptDate) {
        this.interceptDate = interceptDate;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public HmeInterceptInformation setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public HmeInterceptInformation setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public HmeInterceptInformation setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public HmeInterceptInformation setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public HmeInterceptInformation setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public HmeInterceptInformation setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public HmeInterceptInformation setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public HmeInterceptInformation setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public HmeInterceptInformation setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public HmeInterceptInformation setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public HmeInterceptInformation setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public HmeInterceptInformation setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public HmeInterceptInformation setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public HmeInterceptInformation setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public HmeInterceptInformation setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public HmeInterceptInformation setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public HmeInterceptInformation setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }
}
