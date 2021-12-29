package com.ruike.qms.domain.entity;

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
import lombok.Data;

/**
 * 抽样方案表
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:10
 */
@ApiModel("抽样方案表")
@VersionAudit
@ModifyAudit
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_sample_scheme")
@CustomPrimary
public class QmsSampleScheme extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SCHEME_ID = "schemeId";
    public static final String FIELD_SAMPLE_PLAN_TYPE = "samplePlanType";
    public static final String FIELD_SAMPLE_STANDARD_TYPE = "sampleStandardType";
    public static final String FIELD_SAMPLE_SIZE_CODE_LETTER = "sampleSizeCodeLetter";
    public static final String FIELD_LOT_UPPER_LIMIT = "lotUpperLimit";
    public static final String FIELD_LOT_LOWER_LIMIT = "lotLowerLimit";
    public static final String FIELD_ACCEPTANCE_QUANTITY_LIMIT = "acceptanceQuantityLimit";
    public static final String FIELD_SAMPLE_SIZE = "sampleSize";
    public static final String FIELD_AC = "ac";
    public static final String FIELD_RE = "re";
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


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    @GeneratedValue
    private String schemeId;
    @ApiModelProperty(value = "抽样计划类型",required = true)
    @NotBlank
    private String samplePlanType;
    @ApiModelProperty(value = "抽样标准类型",required = true)
    @NotBlank
    private String sampleStandardType;
   @ApiModelProperty(value = "样本量字码")    
    private String sampleSizeCodeLetter;
   @ApiModelProperty(value = "批量上限（零抽样方案用,非负整数）")    
    private Long lotUpperLimit;
   @ApiModelProperty(value = "批量下限（零抽样方案用,非负整数）")    
    private Long lotLowerLimit;
    @ApiModelProperty(value = "aql值",required = true)
    @NotNull
    private String acceptanceQuantityLimit;
   @ApiModelProperty(value = "抽样数量（非负整数，除-1，-1代表全检）")    
    private Long sampleSize;
   @ApiModelProperty(value = "ac值（非负整数）")    
    private Long ac;
   @ApiModelProperty(value = "re值（非负整数）")    
    private Long re;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
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

}
