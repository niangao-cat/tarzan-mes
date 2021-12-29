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

import java.util.Date;

/**
 * 设备工位关系表
 *
 * @author han.zhang03@hand-china.com 2020-06-09 11:32:08
 */
@ApiModel("设备工位关系表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_equipment_wkc_rel")
@CustomPrimary
public class HmeEquipmentWkcRel extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EQUIPMENT_WKC_REL_ID = "equipmentWkcRelId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_EQUIPMENT_ID = "equipmentId";
    public static final String FIELD_BUSINESS_ID = "businessId";
    public static final String FIELD_WORK_SHOP_ID = "workShopId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_LINE_ID = "lineId";
    public static final String FIELD_PROCESS_ID = "processId";
    public static final String FIELD_STATION_ID = "stationId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

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
    private String equipmentWkcRelId;
   @ApiModelProperty(value = "站点id")    
    private String siteId;
    @ApiModelProperty(value = "设备ID",required = true)
    @NotBlank
    private String equipmentId;
    @ApiModelProperty(value = "部门id",required = true)
    @NotBlank
    private String businessId;
    @ApiModelProperty(value = "车间id",required = true)
    @NotBlank
    private String workShopId;
    @ApiModelProperty(value = "产线id",required = true)
    @NotBlank
    private String prodLineId;
    @ApiModelProperty(value = "工段id",required = true)
    @NotBlank
    private String lineId;
   @ApiModelProperty(value = "工序id")    
    private String processId;
   @ApiModelProperty(value = "工位id")    
    private String stationId;
    @ApiModelProperty(value = "等级编码有效性",required = true)
    @NotBlank
    private String enableFlag;
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
    @ApiModelProperty(value = "CID",required = true)
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
	public String getEquipmentWkcRelId() {
		return equipmentWkcRelId;
	}

	public void setEquipmentWkcRelId(String equipmentWkcRelId) {
		this.equipmentWkcRelId = equipmentWkcRelId;
	}
    /**
     * @return 站点id
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 设备ID
     */
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
    /**
     * @return 部门id
     */
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    /**
     * @return 车间id
     */
	public String getWorkShopId() {
		return workShopId;
	}

	public void setWorkShopId(String workShopId) {
		this.workShopId = workShopId;
	}
    /**
     * @return 产线id
     */
	public String getProdLineId() {
		return prodLineId;
	}

	public void setProdLineId(String prodLineId) {
		this.prodLineId = prodLineId;
	}
    /**
     * @return 工段id
     */
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
    /**
     * @return 工序id
     */
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
    /**
     * @return 工位id
     */
	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
    /**
     * @return 等级编码有效性
     */
	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
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
     * @return CID
     */
	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}


}
