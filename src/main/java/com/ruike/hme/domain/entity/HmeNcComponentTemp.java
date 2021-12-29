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
 * 不良材料清单临时表
 *
 * @author chaonan.hu@hand-china.com 2020-07-01 20:13:11
 */
@ApiModel("不良材料清单临时表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_nc_component_temp")
public class HmeNcComponentTemp extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NC_COMPONENT_TEMP_ID = "ncComponentTempId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户Id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    private String ncComponentTempId;
    @ApiModelProperty(value = "组织Id",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "序列号",required = true)
    @NotBlank
    private String sn;
    @ApiModelProperty(value = "工位",required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "用户id",required = true)
    @NotBlank
    private String userId;
    @ApiModelProperty(value = "物料ID",required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "",required = true)
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

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户Id
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 主键
     */
	public String getNcComponentTempId() {
		return ncComponentTempId;
	}

	public void setNcComponentTempId(String ncComponentTempId) {
		this.ncComponentTempId = ncComponentTempId;
	}
    /**
     * @return 组织Id
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 序列号
     */
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
    /**
     * @return 工位
     */
	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}
    /**
     * @return 用户id
     */
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    /**
     * @return 物料ID
     */
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
    /**
     * @return 
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

}
