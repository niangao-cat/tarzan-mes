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
 * 偏振度和发散角良率维护行历史表
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:38
 */
@ApiModel("偏振度和发散角良率维护行历史表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "hme_tag_pass_rate_line_his")
public class HmeTagPassRateLineHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_LINE_HIS_ID = "lineHisId";
    public static final String FIELD_HEADER_HIS_ID = "headerHisId";
    public static final String FIELD_HEADER_ID = "headerId";
    public static final String FIELD_LINE_ID = "lineId";
    public static final String FIELD_ADD_PASS_RATE = "addPassRate";
    public static final String FIELD_TEST_SUM_QTY = "testSumQty";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_REMARK = "remark";
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
    private String lineHisId;
    @ApiModelProperty(value = "头表历史主键", required = true)
    private String headerHisId;
    @ApiModelProperty(value = "偏振度和发散角良率维护头表ID", required = true)
    @NotBlank
    private String headerId;
    @ApiModelProperty(value = "偏振度和发散角良率维护行表ID", required = true)
    @NotBlank
    private String lineId;
    @ApiModelProperty(value = "加测目标良率")
    private BigDecimal addPassRate;
    @ApiModelProperty(value = "测试总量", required = true)
    @NotNull
    private Long testSumQty;
    @ApiModelProperty(value = "优先级", required = true)
    @NotNull
    private Long priority;
    @ApiModelProperty(value = "备注")
    private String remark;
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

    public HmeTagPassRateLineHis setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 主键
     */
    public String getLineHisId() {
        return lineHisId;
    }

    public HmeTagPassRateLineHis setLineHisId(String lineHisId) {
        this.lineHisId = lineHisId;
        return this;
    }

    /**
     * @return 偏振度和发散角良率维护头表ID
     */
    public String getHeaderId() {
        return headerId;
    }

    public HmeTagPassRateLineHis setHeaderId(String headerId) {
        this.headerId = headerId;
        return this;
    }

    /**
     * @return 偏振度和发散角良率维护行表ID
     */
    public String getLineId() {
        return lineId;
    }

    public HmeTagPassRateLineHis setLineId(String lineId) {
        this.lineId = lineId;
        return this;
    }

    /**
     * @return 加测目标良率
     */
    public BigDecimal getAddPassRate() {
        return addPassRate;
    }

    public HmeTagPassRateLineHis setAddPassRate(BigDecimal addPassRate) {
        this.addPassRate = addPassRate;
        return this;
    }

    /**
     * @return 测试总量
     */
    public Long getTestSumQty() {
        return testSumQty;
    }

    public HmeTagPassRateLineHis setTestSumQty(Long testSumQty) {
        this.testSumQty = testSumQty;
        return this;
    }

    /**
     * @return 优先级
     */
    public Long getPriority() {
        return priority;
    }

    public HmeTagPassRateLineHis setPriority(Long priority) {
        this.priority = priority;
        return this;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public HmeTagPassRateLineHis setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public HmeTagPassRateLineHis setCid(Long cid) {
        this.cid = cid;
        return this;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public HmeTagPassRateLineHis setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public HmeTagPassRateLineHis setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public HmeTagPassRateLineHis setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public HmeTagPassRateLineHis setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public HmeTagPassRateLineHis setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public HmeTagPassRateLineHis setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public HmeTagPassRateLineHis setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public HmeTagPassRateLineHis setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public HmeTagPassRateLineHis setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public HmeTagPassRateLineHis setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public HmeTagPassRateLineHis setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public HmeTagPassRateLineHis setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public HmeTagPassRateLineHis setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public HmeTagPassRateLineHis setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public HmeTagPassRateLineHis setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
        return this;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public HmeTagPassRateLineHis setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
        return this;
    }

    public String getHeaderHisId() {
        return headerHisId;
    }

    public void setHeaderHisId(String headerHisId) {
        this.headerHisId = headerHisId;
    }
}
