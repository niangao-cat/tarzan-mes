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
 * 样本量字码表
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:11
 */
@ApiModel("样本量字码表")
@VersionAudit
@ModifyAudit
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_sample_size_code_letter")
@CustomPrimary
public class QmsSampleSizeCodeLetter extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_LETTER_ID = "letterId";
    public static final String FIELD_SAMPLE_STANDARD_TYPE = "sampleStandardType";
    public static final String FIELD_LOT_SIZE_FROM = "lotSizeFrom";
    public static final String FIELD_LOT_SIZE_TO = "lotSizeTo";
    public static final String FIELD_SIZE_CODE_LETTER1 = "sizeCodeLetter1";
    public static final String FIELD_SIZE_CODE_LETTER2 = "sizeCodeLetter2";
    public static final String FIELD_SIZE_CODE_LETTER3 = "sizeCodeLetter3";
    public static final String FIELD_SIZE_CODE_LETTER4 = "sizeCodeLetter4";
    public static final String FIELD_SIZE_CODE_LETTER5 = "sizeCodeLetter5";
    public static final String FIELD_SIZE_CODE_LETTER6 = "sizeCodeLetter6";
    public static final String FIELD_SIZE_CODE_LETTER7 = "sizeCodeLetter7";
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
    private String letterId;
    @ApiModelProperty(value = "抽样标准类型",required = true)
    @NotBlank
    private String sampleStandardType;
    @ApiModelProperty(value = "批次范围从(大于等于)",required = true)
    @NotNull
    private Long lotSizeFrom;
    @ApiModelProperty(value = "批次范围至(小于等于)",required = true)
    @NotNull
    private Long lotSizeTo;
    @ApiModelProperty(value = "样本量字码1(特殊检验水平S-1)",required = true)
    @NotBlank
    private String sizeCodeLetter1;
    @ApiModelProperty(value = "样本量字码2(特殊检验水平S-2)",required = true)
    @NotBlank
    private String sizeCodeLetter2;
    @ApiModelProperty(value = "样本量字码3(特殊检验水平S-3)",required = true)
    @NotBlank
    private String sizeCodeLetter3;
    @ApiModelProperty(value = "样本量字码4(特殊检验水平S-4)",required = true)
    @NotBlank
    private String sizeCodeLetter4;
    @ApiModelProperty(value = "样本量字码5(一般检验水平I)",required = true)
    @NotBlank
    private String sizeCodeLetter5;
    @ApiModelProperty(value = "样本量字码6(一般检验水平II)",required = true)
    @NotBlank
    private String sizeCodeLetter6;
    @ApiModelProperty(value = "样本量字码7(一般检验水平III)",required = true)
    @NotBlank
    private String sizeCodeLetter7;
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
