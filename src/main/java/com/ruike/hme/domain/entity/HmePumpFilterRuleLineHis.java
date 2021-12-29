package com.ruike.hme.domain.entity;

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
 * 泵浦源筛选规则行表历史表
 *
 * @author sanfeng.zhang@hand-china.com 2021-08-20 14:28:34
 */
@ApiModel("泵浦源筛选规则行表历史表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_pump_filter_rule_line_his")
@CustomPrimary
public class HmePumpFilterRuleLineHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_RULE_LINE_HIS_ID = "ruleLineHisId";
    public static final String FIELD_RULE_HEADER_HIS_ID = "ruleHeadHisId";
    public static final String FIELD_RULE_LINE_ID = "ruleLineId";
    public static final String FIELD_RULE_HEAD_ID = "ruleHeadId";
    public static final String FIELD_PARAMETER_CODE = "parameterCode";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_CALCULATE_TYPE = "calculateType";
    public static final String FIELD_MIN_VALUE = "minValue";
    public static final String FIELD_MAX_VALUE = "maxValue";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_FORMULA = "formula";
	public static final String FIELD_SEQUENCE = "sequence";
	public static final String FIELD_PRIORITY = "priority";
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
    @ApiModelProperty("行表历史主键ID")
    @Id
    private String ruleLineHisId;
    @ApiModelProperty(value = "头表历史主键ID",required = true)
    @NotBlank
    private String ruleHeadHisId;
    @ApiModelProperty(value = "行表主键",required = true)
    @NotBlank
    private String ruleLineId;
    @ApiModelProperty(value = "头表主键",required = true)
    @NotBlank
    private String ruleHeadId;
    @ApiModelProperty(value = "参数代码",required = true)
    @NotBlank
    private String parameterCode;
    @ApiModelProperty(value = "数据项ID",required = true)
    @NotBlank
    private String tagId;
    @ApiModelProperty(value = "计算类型",required = true)
    @NotBlank
    private String calculateType;
    @ApiModelProperty(value = "最小值",required = true)
    @NotNull
    private BigDecimal minValue;
    @ApiModelProperty(value = "最大值",required = true)
    @NotNull
    private BigDecimal maxValue;
    @ApiModelProperty(value = "启用标识。Y启用，N未启用",required = true)
    @NotBlank
    private String enableFlag;
   @ApiModelProperty(value = "公式")    
    private String formula;
	@ApiModelProperty(value = "排序")
	private Long sequence;
	@ApiModelProperty(value = "优先消耗")
	private String priority;
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
     * @return 行表历史主键ID
     */
	public String getRuleLineHisId() {
		return ruleLineHisId;
	}

	public void setRuleLineHisId(String ruleLineHisId) {
		this.ruleLineHisId = ruleLineHisId;
	}
    /**
     * @return 头表历史主键ID
     */
	public String getRuleHeadHisId() {
		return ruleHeadHisId;
	}

	public void setRuleHeadHisId(String ruleHeadHisId) {
		this.ruleHeadHisId = ruleHeadHisId;
	}
    /**
     * @return 行表主键
     */
	public String getRuleLineId() {
		return ruleLineId;
	}

	public void setRuleLineId(String ruleLineId) {
		this.ruleLineId = ruleLineId;
	}
    /**
     * @return 头表主键
     */
	public String getRuleHeadId() {
		return ruleHeadId;
	}

	public void setRuleHeadId(String ruleHeadId) {
		this.ruleHeadId = ruleHeadId;
	}
    /**
     * @return 参数代码
     */
	public String getParameterCode() {
		return parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}
    /**
     * @return 数据项ID
     */
	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
    /**
     * @return 计算类型
     */
	public String getCalculateType() {
		return calculateType;
	}

	public void setCalculateType(String calculateType) {
		this.calculateType = calculateType;
	}
    /**
     * @return 最小值
     */
	public BigDecimal getMinValue() {
		return minValue;
	}

	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}
    /**
     * @return 最大值
     */
	public BigDecimal getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}
    /**
     * @return 启用标识。Y启用，N未启用
     */
	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}
    /**
     * @return 公式
     */
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
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

}
