package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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

import java.util.Date;

/**
 * 作业指导
 *
 * @author jiangling.zheng@hand-china.com 2020-10-20 16:07:50
 */
@ApiModel("作业指导")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_operation_instruction")
@CustomPrimary
@Data
public class HmeOperationInstruction extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OPERATION_INS_ID = "operationInsId";
    public static final String FIELD_INS_HEADER_ID = "insHeaderId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_VERSION_ID = "materialVersionId";
    public static final String FIELD_MATERIAL_CATEGORY_ID = "materialCategoryId";
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
    @ApiModelProperty("主键id")
    @Id
    @GeneratedValue
    private String operationInsId;
    @ApiModelProperty(value = "头表ID",required = true)
    @NotBlank
    private String insHeaderId;
    @ApiModelProperty(value = "工艺ID",required = true)
    @NotBlank
    private String operationId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料版本ID")
    private String materialVersionId;
    @ApiModelProperty(value = "物料类别ID")
    private String materialCategoryId;
    @ApiModelProperty(value = "有效性标识",required = true)
    @NotBlank
    private String enableFlag;
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

	@ApiModelProperty(value = "物料编码")
    @Transient
	private String materialCode;
	@ApiModelProperty(value = "物料名称")
    @Transient
	private String materialName;
	@ApiModelProperty(value = "物料版本")
    @Transient
	private String materialVersion;
	@ApiModelProperty(value = "物料类别编码")
    @Transient
	private String materialCategoryCode;
	@ApiModelProperty(value = "物料类别说明")
    @Transient
	private String materialCategoryDesc;
	@ApiModelProperty(value = "创建人名称")
    @Transient
	private String createdByName;
    @ApiModelProperty(value = "创建人名称")
    @Transient
    private String lastUpdatedByName;
    @ApiModelProperty(value = "工艺编码")
    @Transient
    private String operationName;
    @ApiModelProperty(value = "工艺描述")
    @Transient
    private String operationDes;

}
