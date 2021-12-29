package com.ruike.hme.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 芯片规则逻辑
 *
 * @author wenzhang.yu@hand-china.com 2020-08-10 16:16:47
 */
@ApiModel("芯片规则逻辑")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_cos_rule_logic")
@CustomPrimary
public class HmeCosRuleLogic extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COS_RULE_LOGIC_ID = "cosRuleLogicId";
    public static final String FIELD_COS_RULE_ID = "cosRuleId";
	public static final String FIELD_SITE_ID = "siteId";
	public static final String FIELD_RULE_NUMBER = "ruleNumber";
    public static final String FIELD_CURRENT = "current";
    public static final String FIELD_COLLECTION_ITEM = "collectionItem";
    public static final String FIELD_COUNT_TYPE = "countType";
    public static final String FIELD_RANGE_TYPE = "rangeType";
    public static final String FIELD_RULE_VALUE = "ruleValue";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


   @ApiModelProperty(value = "租户id")    
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    private String cosRuleLogicId;
    @ApiModelProperty(value = "头表id",required = true)
    @NotBlank
    private String cosRuleId;
	@ApiModelProperty(value = "组织id",required = true)
	@NotBlank
	private String siteId;
    @ApiModelProperty(value = "规则",required = true)
    @NotBlank
    private String ruleNumber;
   @ApiModelProperty(value = "电流")    
    private String current;
    @ApiModelProperty(value = "采集项",required = true)
    @NotBlank
    private String collectionItem;
    @ApiModelProperty(value = "计算类型",required = true)
    @NotBlank
    private String countType;
    @ApiModelProperty(value = "范围类型",required = true)
    @NotNull
    private String rangeType;
    @ApiModelProperty(value = "值",required = true)
    @NotNull
    private String ruleValue;
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
     * @return 主键ID，标识唯一一条记录
     */
	public String getCosRuleLogicId() {
		return cosRuleLogicId;
	}

	public void setCosRuleLogicId(String cosRuleLogicId) {
		this.cosRuleLogicId = cosRuleLogicId;
	}
    /**
     * @return 头表id
     */
	public String getCosRuleId() {
		return cosRuleId;
	}

	public void setCosRuleId(String cosRuleId) {
		this.cosRuleId = cosRuleId;
	}
	/**
	 * @return 组织id
	 */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	/**
     * @return 规则
     */
	public String getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(String ruleNumber) {
		this.ruleNumber = ruleNumber;
	}
    /**
     * @return 电流
     */
	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}
    /**
     * @return 采集项
     */
	public String getCollectionItem() {
		return collectionItem;
	}

	public void setCollectionItem(String collectionItem) {
		this.collectionItem = collectionItem;
	}
    /**
     * @return 计算类型
     */
	public String getCountType() {
		return countType;
	}

	public void setCountType(String countType) {
		this.countType = countType;
	}
    /**
     * @return 范围类型
     */
	public String getRangeType() {
		return rangeType;
	}

	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}
    /**
     * @return 值
     */
	public String getRuleValue() {
		return ruleValue;
	}

	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
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

}
