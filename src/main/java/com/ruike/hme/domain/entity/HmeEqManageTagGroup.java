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
 * 设备管理项目组表
 *
 * @author han.zhang03@hand-china.com 2020-06-10 14:58:51
 */
@ApiModel("设备管理项目组表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_eq_manage_tag_group")
@CustomPrimary
public class HmeEqManageTagGroup extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MANAGE_TAG_GROUP_ID = "manageTagGroupId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_EQUIPMENT_ID = "equipmentId";
    public static final String FIELD_BUSINESS_ID = "business_id";
    public static final String FIELD_OPERATION_ID = "operation_id";
    public static final String FIELD_SERVICE_LIFE = "service_life";
    public static final String FIELD_MANAGE_TYPED = "manage_typed";
    public static final String FIELD_EQUIPMENT_CATEGORY = "equipmentCategory";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
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


    @ApiModelProperty(value = "租户ID（企业ID）")
    @NotNull
    private Long tenantId;
    @ApiModelProperty("")
    @Id
    private String manageTagGroupId;
   @ApiModelProperty(value = "组织id")    
    private String siteId;
    @ApiModelProperty(value = "设备ID")
    @NotBlank
    private String equipmentId;
   @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;
    @ApiModelProperty(value = "项目组ID")
    @NotBlank
    private String tagGroupId;
	@ApiModelProperty(value = "部门ID")
	private String businessId;
	@ApiModelProperty(value = "工艺ID")
	private String operationId;
	@ApiModelProperty(value = "设备使用年限")
	private String serviceLife;
	@ApiModelProperty(value = "管理类型")
	private String manageType;
    @ApiModelProperty(value = "状态")
    @NotBlank
    private String status;
    @ApiModelProperty(value = "等级编码有效性")
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "CID")
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
     * @return 组织id
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
     * @return 设备ID
     */
	public String getEquipmentCategory() {
		return equipmentCategory;
	}

	public void setEquipmentCategory(String equipmentCategory) {
		this.equipmentCategory = equipmentCategory;
	}
    /**
     * @return 项目组ID
     */
	public String getTagGroupId() {
		return tagGroupId;
	}

	public void setTagGroupId(String tagGroupId) {
		this.tagGroupId = tagGroupId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getServiceLife() {
		return serviceLife;
	}

	public void setServiceLife(String serviceLife) {
		this.serviceLife = serviceLife;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	/**
     * @return 状态
     */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
     * @return CID
     */
	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public String getManageTagGroupId() {
		return manageTagGroupId;
	}

	public void setManageTagGroupId(String manageTagGroupId) {
		this.manageTagGroupId = manageTagGroupId;
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
