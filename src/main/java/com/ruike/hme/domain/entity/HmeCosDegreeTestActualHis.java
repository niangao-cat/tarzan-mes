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
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 偏振度和发散角测试结果历史表
 *
 * @author chaonan.hu@hand-china.com 2021-09-14 17:06:57
 */
@ApiModel("偏振度和发散角测试结果历史表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_cos_degree_test_actual_his")
public class HmeCosDegreeTestActualHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DEGREE_TEST_HIS_ID = "degreeTestHisId";
    public static final String FIELD_DEGREE_TEST_ID = "degreeTestId";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_WAFER = "wafer";
    public static final String FIELD_TEST_OBJECT = "testObject";
    public static final String FIELD_TEST_QTY = "testQty";
    public static final String FIELD_TARGET_QTY = "targetQty";
    public static final String FIELD_TEST_RATE = "testRate";
    public static final String FIELD_PRIORITY = "priority";
	public static final String FIELD_TEST_STATUS = "testStatus";
	public static final String FIELD_RELEASE_BY = "releaseBy";
	public static final String FIELD_RELEASE_DATE = "releaseDate";
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


    @ApiModelProperty(value = "租户id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("历史表主键")
    @Id
    private String degreeTestHisId;
    @ApiModelProperty(value = "主表主键",required = true)
    @NotBlank
    private String degreeTestId;
    @ApiModelProperty(value = "cos类型",required = true)
    @NotBlank
    private String cosType;
    @ApiModelProperty(value = "wafer",required = true)
    @NotBlank
    private String wafer;
    @ApiModelProperty(value = "测试对象",required = true)
    @NotBlank
    private String testObject;
    @ApiModelProperty(value = "测试数量",required = true)
    @NotNull
    private Long testQty;
    @ApiModelProperty(value = "目标数量")
    private Long targetQty;
    @ApiModelProperty(value = "测试良率")
    private BigDecimal testRate;
    @ApiModelProperty(value = "优先级")
    private Long priority;
	@ApiModelProperty(value = "测试状态",required = true)
	@NotNull
	private String testStatus;
	@ApiModelProperty(value = "放行人")
	private Long releaseBy;
	@ApiModelProperty(value = "放行时间")
	private Date releaseDate;
    @ApiModelProperty(value = "CID",required = true)
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

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 历史表主键
     */
	public String getDegreeTestHisId() {
		return degreeTestHisId;
	}

	public void setDegreeTestHisId(String degreeTestHisId) {
		this.degreeTestHisId = degreeTestHisId;
	}
    /**
     * @return 主表主键
     */
	public String getDegreeTestId() {
		return degreeTestId;
	}

	public void setDegreeTestId(String degreeTestId) {
		this.degreeTestId = degreeTestId;
	}
    /**
     * @return cos类型
     */
	public String getCosType() {
		return cosType;
	}

	public void setCosType(String cosType) {
		this.cosType = cosType;
	}
    /**
     * @return wafer
     */
	public String getWafer() {
		return wafer;
	}

	public void setWafer(String wafer) {
		this.wafer = wafer;
	}
    /**
     * @return 测试对象
     */
	public String getTestObject() {
		return testObject;
	}

	public void setTestObject(String testObject) {
		this.testObject = testObject;
	}
    /**
     * @return 测试数量
     */
	public Long getTestQty() {
		return testQty;
	}

	public void setTestQty(Long testQty) {
		this.testQty = testQty;
	}
    /**
     * @return 目标数量
     */
	public Long getTargetQty() {
		return targetQty;
	}

	public void setTargetQty(Long targetQty) {
		this.targetQty = targetQty;
	}
    /**
     * @return 测试良率
     */
	public BigDecimal getTestRate() {
		return testRate;
	}

	public void setTestRate(BigDecimal testRate) {
		this.testRate = testRate;
	}
    /**
     * @return 优先级
     */
	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
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

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}

	public Long getReleaseBy() {
		return releaseBy;
	}

	public void setReleaseBy(Long releaseBy) {
		this.releaseBy = releaseBy;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
}