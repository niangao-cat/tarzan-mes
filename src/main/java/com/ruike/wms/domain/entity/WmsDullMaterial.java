package com.ruike.wms.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

/**
 * @author yubin.huang@hand-china.com 2019-10-31 13:21:57
 */
@ApiModel("")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_dull_material")
public class WmsDullMaterial extends AuditDomain {

	public static final String FIELD_DULL_MATERIAL_ID = "dullMaterialId";
	public static final String FIELD_MATERIAL_LOT_CODE = "materialLotCode";
	public static final String FIELD_SITE_ID = "siteId";
	public static final String FIELD_CID = "cid";

	//
	// 业务方法(按public protected private顺序排列)
	// ------------------------------------------------------------------------------

	//
	// 数据库字段
	// ------------------------------------------------------------------------------


	@ApiModelProperty("id")
	@Id
	@GeneratedValue
	private String dullMaterialId;
	@ApiModelProperty(value = "条码", required = true)
	@NotBlank
	private String materialLotCode;
	@ApiModelProperty(value = "工厂", required = true)
	@NotBlank
	private String siteId;
	@ApiModelProperty(value = "cid", required = true)
	@NotNull
	private Long cid;
	//
	// 非数据库字段
	// ------------------------------------------------------------------------------

	//
	// getter/setter
	// ------------------------------------------------------------------------------

	/**
	 * @return
	 */
	public String getDullMaterialId() {
		return dullMaterialId;
	}

	public void setDullMaterialId(String dullMaterialId) {
		this.dullMaterialId = dullMaterialId;
	}

	/**
	 * @return
	 */
	public String getMaterialLotCode() {
		return materialLotCode;
	}

	public void setMaterialLotCode(String materialLotCode) {
		this.materialLotCode = materialLotCode;
	}

	/**
	 * @return
	 */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
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

}
