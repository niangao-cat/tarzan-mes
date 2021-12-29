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
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据采集项公式行表
 *
 * @author guiming.zhou@hand-china.com 2020-09-23 10:04:56
 */
@ApiModel("数据采集项公式行表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_tag_formula_line")
@CustomPrimary
public class HmeTagFormulaLine extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TAG_FORMULA_HEAD_ID = "tagFormulaHeadId";
    public static final String FIELD_TAG_FORMULA_LINE_ID = "tagFormulaLineId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_PARAMETER_CODE = "parameterCode";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "头表主键id",required = true)
    @NotBlank
    private String tagFormulaHeadId;
    @ApiModelProperty("行表主键id")
    @Id
    private String tagFormulaLineId;
   @ApiModelProperty(value = "工艺id")    
    private String operationId;
   @ApiModelProperty(value = "项目组id")    
    private String tagGroupId;
    @ApiModelProperty(value = "项目id",required = true)
    @NotBlank
    private String tagId;
    @ApiModelProperty(value = "参数代码",required = true)
    @NotBlank
    private String parameterCode;
    @ApiModelProperty(value = "CID",required = true)
	@Cid
    private Long cid;

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
     * @return 头表主键id
     */
	public String getTagFormulaHeadId() {
		return tagFormulaHeadId;
	}

	public void setTagFormulaHeadId(String tagFormulaHeadId) {
		this.tagFormulaHeadId = tagFormulaHeadId;
	}
    /**
     * @return 行表主键id
     */
	public String getTagFormulaLineId() {
		return tagFormulaLineId;
	}

	public void setTagFormulaLineId(String tagFormulaLineId) {
		this.tagFormulaLineId = tagFormulaLineId;
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
     * @return 项目组id
     */
	public String getTagGroupId() {
		return tagGroupId;
	}

	public void setTagGroupId(String tagGroupId) {
		this.tagGroupId = tagGroupId;
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
     * @return 参数代码
     */
	public String getParameterCode() {
		return parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
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

}
