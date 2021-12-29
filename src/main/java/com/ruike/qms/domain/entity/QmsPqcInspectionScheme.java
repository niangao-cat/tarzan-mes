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
 * 巡检检验计划
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:12
 */
@ApiModel("巡检检验计划")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_pqc_inspection_scheme")
@CustomPrimary
public class QmsPqcInspectionScheme extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSPECTION_SCHEME_ID = "inspectionSchemeId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_CATEGORY_ID = "materialCategoryId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_INSPECTION_TYPE = "inspectionType";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_INSPECTION_FILE = "inspectionFile";
    public static final String FIELD_FILE_VERSION = "fileVersion";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_PUBLISH_FLAG = "publishFlag";
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
    private String inspectionSchemeId;
    @ApiModelProperty(value = "组织",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "物料类别")
    private String materialCategoryId;
    @ApiModelProperty(value = "物料")
    private String materialId;
    @ApiModelProperty(value = "物料版本",required = true)
    @NotBlank
    private String materialVersion;
    @ApiModelProperty(value = "物料版本",required = true)
    @NotBlank
    private String inspectionType;
    @ApiModelProperty(value = "状态",required = true)
    @NotBlank
    private String status;
    @ApiModelProperty(value = "检验文件号",required = true)
    @NotBlank
    private String inspectionFile;
    @ApiModelProperty(value = "文件版本号",required = true)
    @NotBlank
    private String fileVersion;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "发布标识",required = true)
    @NotBlank
    private String publishFlag;
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
	public String getInspectionSchemeId() {
		return inspectionSchemeId;
	}

	public void setInspectionSchemeId(String inspectionSchemeId) {
		this.inspectionSchemeId = inspectionSchemeId;
	}
    /**
     * @return 组织
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 物料类别
     */
	public String getMaterialCategoryId() {
		return materialCategoryId;
	}

	public void setMaterialCategoryId(String materialCategoryId) {
		this.materialCategoryId = materialCategoryId;
	}
    /**
     * @return 物料
     */
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
    /**
     * @return 物料版本
     */
	public String getMaterialVersion() {
		return materialVersion;
	}

	public void setMaterialVersion(String materialVersion) {
		this.materialVersion = materialVersion;
	}
    /**
     * @return 物料版本
     */
	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
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
     * @return 检验文件号
     */
	public String getInspectionFile() {
		return inspectionFile;
	}

	public void setInspectionFile(String inspectionFile) {
		this.inspectionFile = inspectionFile;
	}
    /**
     * @return 文件版本号
     */
	public String getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}
    /**
     * @return 是否有效
     */
	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}
    /**
     * @return 发布标识
     */
	public String getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
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
