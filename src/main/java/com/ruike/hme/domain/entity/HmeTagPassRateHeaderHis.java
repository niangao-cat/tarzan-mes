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

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 偏振度和发散角良率维护头历史表
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:36
 */
@ApiModel("偏振度和发散角良率维护头历史表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "hme_tag_pass_rate_header_his")
public class HmeTagPassRateHeaderHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_HEADER_HIS_ID = "headerHisId";
    public static final String FIELD_HEADER_ID = "headerId";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_TEST_OBJECT = "testObject";
    public static final String FIELD_TEST_TYPE = "testType";
    public static final String FIELD_TEST_QTY = "testQty";
    public static final String FIELD_PASS_RATE = "passRate";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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
    private String headerHisId;
    @ApiModelProperty(value = "偏振度和发散角良率维护头表ID", required = true)
    @NotBlank
    private String headerId;
    @ApiModelProperty(value = "cos类型", required = true)
    @NotBlank
    private String cosType;
    @ApiModelProperty(value = "测试对象", required = true)
    @NotBlank
    private String testObject;
    @ApiModelProperty(value = "测试类型", required = true)
    @NotBlank
    private String testType;
    @ApiModelProperty(value = "测试数量")
    private Long testQty;
    @ApiModelProperty(value = "良率", required = true)
    @NotNull
    private BigDecimal passRate;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "有效性", required = true)
    @NotBlank
    private String enableFlag;
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

    public HmeTagPassRateHeaderHis setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 主键
     */
    public String getHeaderHisId() {
        return headerHisId;
    }

    public HmeTagPassRateHeaderHis setHeaderHisId(String headerHisId) {
        this.headerHisId = headerHisId;
        return this;
    }

    /**
     * @return 偏振度和发散角良率维护头表ID
     */
    public String getHeaderId() {
        return headerId;
    }

    public HmeTagPassRateHeaderHis setHeaderId(String headerId) {
        this.headerId = headerId;
        return this;
    }

    /**
     * @return cos类型
     */
    public String getCosType() {
        return cosType;
    }

    public HmeTagPassRateHeaderHis setCosType(String cosType) {
        this.cosType = cosType;
        return this;
    }

    /**
     * @return 测试对象
     */
    public String getTestObject() {
        return testObject;
    }

    public HmeTagPassRateHeaderHis setTestObject(String testObject) {
        this.testObject = testObject;
        return this;
    }

    /**
     * @return 测试类型
     */
    public String getTestType() {
        return testType;
    }

    public HmeTagPassRateHeaderHis setTestType(String testType) {
        this.testType = testType;
        return this;
    }

    /**
     * @return 测试数量
     */
    public Long getTestQty() {
        return testQty;
    }

    public HmeTagPassRateHeaderHis setTestQty(Long testQty) {
        this.testQty = testQty;
        return this;
    }

    /**
     * @return 良率
     */
    public BigDecimal getPassRate() {
        return passRate;
    }

    public HmeTagPassRateHeaderHis setPassRate(BigDecimal passRate) {
        this.passRate = passRate;
        return this;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public HmeTagPassRateHeaderHis setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    /**
     * @return 有效性
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public HmeTagPassRateHeaderHis setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public HmeTagPassRateHeaderHis setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public HmeTagPassRateHeaderHis setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public HmeTagPassRateHeaderHis setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public HmeTagPassRateHeaderHis setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public HmeTagPassRateHeaderHis setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public HmeTagPassRateHeaderHis setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public HmeTagPassRateHeaderHis setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public HmeTagPassRateHeaderHis setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public HmeTagPassRateHeaderHis setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public HmeTagPassRateHeaderHis setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public HmeTagPassRateHeaderHis setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public HmeTagPassRateHeaderHis setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public HmeTagPassRateHeaderHis setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public HmeTagPassRateHeaderHis setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public HmeTagPassRateHeaderHis setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public HmeTagPassRateHeaderHis setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public HmeTagPassRateHeaderHis setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }
}
