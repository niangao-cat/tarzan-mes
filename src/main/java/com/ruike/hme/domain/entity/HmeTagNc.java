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
import lombok.Data;

/**
 * 数据项不良判定基础表
 *
 * @author guiming.zhou@hand-china.com 2020-09-24 16:00:30
 */
@ApiModel("数据项不良判定基础表")
@VersionAudit
@ModifyAudit
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_tag_nc")
@CustomPrimary
public class HmeTagNc extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TAG_NC_ID = "tagNcId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_TAG_TYPE = "tagType";
    public static final String FIELD_MIN_VALUE = "minValue";
    public static final String FIELD_STANDARD_VALUE = "standardValue";
    public static final String FIELD_MAX_VALUE = "maxValue";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_NC_CODE_GROUP_ID = "ncCodeGroupId";
    public static final String FIELD_NC_CODE_ID = "ncCodeId";
    public static final String FIELD_NC_DEAL_WAY = "ncDealWay";
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
    @ApiModelProperty("主键id")
	@GeneratedValue
    @Id
    private String tagNcId;
   @ApiModelProperty(value = "项目组id")    
    private String tagGroupId;
   @ApiModelProperty(value = "工艺id")    
    private String operationId;
   @ApiModelProperty(value = "项目id")    
    private String tagId;
   @ApiModelProperty(value = "数据类型")    
    private String tagType;
   @ApiModelProperty(value = "最小值")    
    private BigDecimal minValue;
   @ApiModelProperty(value = "标准值")    
    private BigDecimal standardValue;
   @ApiModelProperty(value = "最大值")    
    private BigDecimal maxValue;
    @ApiModelProperty(value = "优先级",required = true)
    @NotNull
    private Long priority;
   @ApiModelProperty(value = "不良代码组id")    
    private String ncCodeGroupId;
   @ApiModelProperty(value = "不良代码id")    
    private String ncCodeId;
   @ApiModelProperty(value = "不良处置方式")    
    private String ncDealWay;
   @ApiModelProperty(value = "CID")
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
     * @return 主键id
     */
	public String getTagNcId() {
		return tagNcId;
	}

	public void setTagNcId(String tagNcId) {
		this.tagNcId = tagNcId;
	}
    /**
     * @return 项目组id
     */
	public String getTagGroupId() {
		return tagGroupId;
	}

	public void setTagGroupId(String tagGroupId) {
		this.tagGroupId = tagGroupId;
	}
    /**
     * @return 工艺id
     */
	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
    /**
     * @return 项目id
     */
	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
    /**
     * @return 数据类型
     */
	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
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
     * @return 标准值
     */
	public BigDecimal getStandardValue() {
		return standardValue;
	}

	public void setStandardValue(BigDecimal standardValue) {
		this.standardValue = standardValue;
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
     * @return 优先级
     */
	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}
    /**
     * @return 不良代码组id
     */
	public String getNcCodeGroupId() {
		return ncCodeGroupId;
	}

	public void setNcCodeGroupId(String ncCodeGroupId) {
		this.ncCodeGroupId = ncCodeGroupId;
	}
    /**
     * @return 不良代码id
     */
	public String getNcCodeId() {
		return ncCodeId;
	}

	public void setNcCodeId(String ncCodeId) {
		this.ncCodeId = ncCodeId;
	}
    /**
     * @return 不良处置方式
     */
	public String getNcDealWay() {
		return ncDealWay;
	}

	public void setNcDealWay(String ncDealWay) {
		this.ncDealWay = ncDealWay;
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
