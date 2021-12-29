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

/**
 * 巡检物料与检验组关系表
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:12
 */
@ApiModel("巡检物料与检验组关系表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "qms_pqc_group_rel")
public class QmsPqcGroupRel extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PQC_GROUP_REL_ID = "pqcGroupRelId";
    public static final String FIELD_SCHEME_ID = "schemeId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
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
    @ApiModelProperty("主键id")
    @Id
    private String pqcGroupRelId;
    @ApiModelProperty(value = "物料检验计划主键ID",required = true)
    @NotBlank
    private String schemeId;
    @ApiModelProperty(value = "检验组ID",required = true)
    @NotBlank
    private String tagGroupId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "扩展字段1")
    private String attribute1;
    @ApiModelProperty(value = "扩展字段2")
    private String attribute2;
    @ApiModelProperty(value = "扩展字段3")
    private String attribute3;
    @ApiModelProperty(value = "cid",required = true)
    @NotNull
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
     * @return 主键id
     */
	public String getPqcGroupRelId() {
		return pqcGroupRelId;
	}

	public void setPqcGroupRelId(String pqcGroupRelId) {
		this.pqcGroupRelId = pqcGroupRelId;
	}
    /**
     * @return 物料检验计划主键ID
     */
	public String getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}
    /**
     * @return 检验组ID
     */
	public String getTagGroupId() {
		return tagGroupId;
	}

	public void setTagGroupId(String tagGroupId) {
		this.tagGroupId = tagGroupId;
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
     * @return 扩展字段1
     */
	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
    /**
     * @return 扩展字段2
     */
	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
    /**
     * @return 扩展字段3
     */
	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
    /**
     * @return cid
     */
	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

}
