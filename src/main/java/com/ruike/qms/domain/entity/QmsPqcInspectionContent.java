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
import java.math.BigDecimal;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 巡检检验项目表
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:11
 */
@ApiModel("巡检检验项目表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_pqc_inspection_content")
@CustomPrimary
public class QmsPqcInspectionContent extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PQC_INSPECTION_CONTENT_ID = "pqcInspectionContentId";
    public static final String FIELD_SCHEME_ID = "schemeId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_ORDER_KEY = "orderKey";
    public static final String FIELD_INSPECTION = "inspection";
    public static final String FIELD_INSPECTION_DESC = "inspectionDesc";
    public static final String FIELD_INSPECTION_TYPE = "inspectionType";
    public static final String FIELD_FREQUENCY = "frequency";
    public static final String FIELD_STANDARD_TYPE = "standardType";
    public static final String FIELD_ACCURACY = "accuracy";
    public static final String FIELD_STANDARD_FROM = "standardFrom";
    public static final String FIELD_STANDARD_TO = "standardTo";
    public static final String FIELD_STANDARD_UOM = "standardUom";
    public static final String FIELD_STANDARD_TEXT = "standardText";
    public static final String FIELD_INSPECTIOM_TOOL = "inspectiomTool";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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
    private String pqcInspectionContentId;
    @ApiModelProperty(value = "物料检验计划ID",required = true)
    @NotBlank
    private String schemeId;
    @ApiModelProperty(value = "检验组ID",required = true)
    @NotBlank
    private String tagGroupId;
    @ApiModelProperty(value = "检验项ID",required = true)
    @NotBlank
    private String tagId;
    @ApiModelProperty(value = "排序码",required = true)
    @NotNull
    private Long orderKey;
    @ApiModelProperty(value = "检验项目",required = true)
    @NotBlank
    private String inspection;
    @ApiModelProperty(value = "检验项目描述")
    private String inspectionDesc;
    @ApiModelProperty(value = "检验项类型",required = true)
    @NotBlank
    private String inspectionType;
	@ApiModelProperty(value = "工序")
	private String processId;
    @ApiModelProperty(value = "检验频率")
    private String frequency;
    @ApiModelProperty(value = "规格类型",required = true)
    @NotBlank
    private String standardType;
    @ApiModelProperty(value = "精度")
    private BigDecimal accuracy;
    @ApiModelProperty(value = "规格值从")
    private BigDecimal standardFrom;
    @ApiModelProperty(value = "规格值至")
    private BigDecimal standardTo;
    @ApiModelProperty(value = "规格单位")
    private String standardUom;
    @ApiModelProperty(value = "文本规格值")
    private String standardText;
    @ApiModelProperty(value = "检验工具",required = true)
    @NotBlank
    private String inspectiomTool;
    @ApiModelProperty(value = "有效性",required = true)
    @NotBlank
	private String enableFlag;
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
	 * @return 工序id
	 */
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
     * @return 主键id
     */
	public String getPqcInspectionContentId() {
		return pqcInspectionContentId;
	}

	public void setPqcInspectionContentId(String pqcInspectionContentId) {
		this.pqcInspectionContentId = pqcInspectionContentId;
	}
    /**
     * @return 物料检验计划ID
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
     * @return 检验项ID
     */
	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
    /**
     * @return 排序码
     */
	public Long getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(Long orderKey) {
		this.orderKey = orderKey;
	}
    /**
     * @return 检验项目
     */
	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}
    /**
     * @return 检验项目描述
     */
	public String getInspectionDesc() {
		return inspectionDesc;
	}

	public void setInspectionDesc(String inspectionDesc) {
		this.inspectionDesc = inspectionDesc;
	}
    /**
     * @return 检验项类型
     */
	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
    /**
     * @return 检验频率
     */
	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
    /**
     * @return 规格类型
     */
	public String getStandardType() {
		return standardType;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}
    /**
     * @return 精度
     */
	public BigDecimal getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(BigDecimal accuracy) {
		this.accuracy = accuracy;
	}
    /**
     * @return 规格值从
     */
	public BigDecimal getStandardFrom() {
		return standardFrom;
	}

	public void setStandardFrom(BigDecimal standardFrom) {
		this.standardFrom = standardFrom;
	}
    /**
     * @return 规格值至
     */
	public BigDecimal getStandardTo() {
		return standardTo;
	}

	public void setStandardTo(BigDecimal standardTo) {
		this.standardTo = standardTo;
	}
    /**
     * @return 规格单位
     */
	public String getStandardUom() {
		return standardUom;
	}

	public void setStandardUom(String standardUom) {
		this.standardUom = standardUom;
	}
    /**
     * @return 文本规格值
     */
	public String getStandardText() {
		return standardText;
	}

	public void setStandardText(String standardText) {
		this.standardText = standardText;
	}
    /**
     * @return 检验工具
     */
	public String getInspectiomTool() {
		return inspectiomTool;
	}

	public void setInspectiomTool(String inspectiomTool) {
		this.inspectiomTool = inspectiomTool;
	}
    /**
     * @return 有效性
     */
	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
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
