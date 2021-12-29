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
 * 设备管理项目表
 *
 * @author han.zhang03@hand-china.com 2020-06-10 14:58:50
 */
@ApiModel("设备管理项目表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_eq_manage_tag")
@CustomPrimary
public class HmeEqManageTag extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MANAGE_TAG_ID = "manageTagId";
    public static final String FIELD_MANAGE_TAG_GROUP_ID = "manageTagGroupId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SERIAL_NUMBER = "serialNumber";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_TAG_CODE = "tagCode";
    public static final String FIELD_TAG_DESCRIPTIONS = "tagDescriptions";
    public static final String FIELD_VALUE_TYPE = "valueType";
    public static final String FIELD_COLLECTION_METHOD = "collectionMethod";
    public static final String FIELD_MANAGE_CYCLE = "manage_cycle";
    public static final String FIELD_ACCURACY = "accuracy";
    public static final String FIELD_MINIMUM_VALUE = "minimumValue";
    public static final String FIELD_MAXIMAL_VALUE = "maximalValue";
    public static final String FIELD_STANDARD_VALUE = "standardValue";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_TRUE_VALUE = "true_value";
    public static final String FIELD_FALSE_VALUE = "false_value";
    public static final String FIELD_MAINTAIN_LEADTIME = "maintainLeadtime";
    public static final String FIELD_TOOL = "tool";
    public static final String FIELD_RESPONSIBLE = "responsible";
    public static final String FIELD_REMARK = "remark";
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


    @ApiModelProperty(value = "租户ID（企业ID）",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("")
    @Id
    private String manageTagId;
    @ApiModelProperty(value = "设备管理项目组ID",required = true)
    @NotBlank
    private String manageTagGroupId;
   @ApiModelProperty(value = "组织id")    
    private String siteId;
   @ApiModelProperty(value = "排序码")    
    private Long serialNumber;
   @ApiModelProperty(value = "项目ID")    
    private String tagId;
    @ApiModelProperty(value = "项目编码",required = true)
    @NotBlank
    private String tagCode;
    @ApiModelProperty(value = "项目描述",required = true)
    @NotBlank
    private String tagDescriptions;
    @ApiModelProperty(value = "数据类型",required = true)
    @NotBlank
    private String valueType;
   @ApiModelProperty(value = "收集方式")    
    private String collectionMethod;
   @ApiModelProperty(value = "管理周期")
    private String manageCycle;
   @ApiModelProperty(value = "精度")    
    private BigDecimal accuracy;
   @ApiModelProperty(value = "最小值")    
    private BigDecimal minimumValue;
   @ApiModelProperty(value = "最大值")    
    private BigDecimal maximalValue;
   @ApiModelProperty(value = "标准值")    
    private BigDecimal standardValue;
   @ApiModelProperty(value = "计量单位")    
    private String uomId;
   @ApiModelProperty(value = "符合值")
    private String trueValue;
   @ApiModelProperty(value = "不符合值")
    private String falseValue;
   @ApiModelProperty(value = "保养提前期")    
    private String maintainLeadtime;
   @ApiModelProperty(value = "工具")    
    private String tool;
   @ApiModelProperty(value = "责任人")    
    private String responsible;
   @ApiModelProperty(value = "备注")    
    private String remark;
    @ApiModelProperty(value = "有效性",required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "CID",required = true)
    @NotNull
	@Cid
    private Long cid;
   @ApiModelProperty(value = "预留字段")
    private String attribute1;
   @ApiModelProperty(value = "预留字段")    
    private String attribute2;
   @ApiModelProperty(value = "预留字段")    
    private String attribute3;
   @ApiModelProperty(value = "预留字段")    
    private String attribute4;
   @ApiModelProperty(value = "预留字段")    
    private String attribute5;
   @ApiModelProperty(value = "预留字段")    
    private String attribute6;
   @ApiModelProperty(value = "预留字段")    
    private String attribute7;
   @ApiModelProperty(value = "预留字段")    
    private String attribute8;
   @ApiModelProperty(value = "预留字段")    
    private String attribute9;
   @ApiModelProperty(value = "预留字段")    
    private String attribute10;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID（企业ID）
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 
     */
	public String getManageTagId() {
		return manageTagId;
	}

	public void setManageTagId(String manageTagId) {
		this.manageTagId = manageTagId;
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
     * @return 排序码
     */
	public Long getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}

    /**
     * @return 项目ID
     */
	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
    /**
     * @return 项目编码
     */
	public String getTagCode() {
		return tagCode;
	}

	public void setTagCode(String tagCode) {
		this.tagCode = tagCode;
	}
    /**
     * @return 项目描述
     */
	public String getTagDescriptions() {
		return tagDescriptions;
	}

	public void setTagDescriptions(String tagDescriptions) {
		this.tagDescriptions = tagDescriptions;
	}
    /**
     * @return 数据类型
     */
	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
    /**
     * @return 收集方式
     */
	public String getCollectionMethod() {
		return collectionMethod;
	}

	public void setCollectionMethod(String collectionMethod) {
		this.collectionMethod = collectionMethod;
	}

	public String getManageCycle() {
		return manageCycle;
	}

	public void setManageCycle(String manageCycle) {
		this.manageCycle = manageCycle;
	}

	public String getTrueValue() {
		return trueValue;
	}

	public void setTrueValue(String trueValue) {
		this.trueValue = trueValue;
	}

	public String getFalseValue() {
		return falseValue;
	}

	public void setFalseValue(String falseValue) {
		this.falseValue = falseValue;
	}

	/**
     * @return 精度
     */
	public BigDecimal getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(BigDecimal accuracy) {
		this.accuracy = accuracy;
	}
    /**
     * @return 最小值
     */
	public BigDecimal getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(BigDecimal minimumValue) {
		this.minimumValue = minimumValue;
	}
    /**
     * @return 最大值
     */
	public BigDecimal getMaximalValue() {
		return maximalValue;
	}

	public String getManageTagGroupId() {
		return manageTagGroupId;
	}

	public void setManageTagGroupId(String manageTagGroupId) {
		this.manageTagGroupId = manageTagGroupId;
	}

	public BigDecimal getStandardValue() {
		return standardValue;
	}

	public void setStandardValue(BigDecimal standardValue) {
		this.standardValue = standardValue;
	}

	public String getUomId() {
		return uomId;
	}

	public void setUomId(String uomId) {
		this.uomId = uomId;
	}

	public void setMaximalValue(BigDecimal maximalValue) {
		this.maximalValue = maximalValue;
	}
    /**
     * @return 保养提前期
     */
	public String getMaintainLeadtime() {
		return maintainLeadtime;
	}

	public void setMaintainLeadtime(String maintainLeadtime) {
		this.maintainLeadtime = maintainLeadtime;
	}
    /**
     * @return 工具
     */
	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}
    /**
     * @return 责任人
     */
	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
    /**
     * @return 备注
     */
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    /**
     * @return 有效性
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
     * @return 预留字段
     */
	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute6() {
		return attribute6;
	}

	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute7() {
		return attribute7;
	}

	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute8() {
		return attribute8;
	}

	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute9() {
		return attribute9;
	}

	public void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute10() {
		return attribute10;
	}

	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}

}
