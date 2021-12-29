package com.ruike.qms.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 物料检验项目表
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:42
 */
@ApiModel("物料检验项目表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_material_insp_content")
@CustomPrimary
public class QmsMaterialInspContent extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_INSPECTION_CONTENT_ID = "materialInspectionContentId";
    public static final String FIELD_SCHEME_ID = "schemeId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_ORDER_KEY = "orderKey";
    public static final String FIELD_INSPECTION = "inspection";
    public static final String FIELD_INSPECTION_DESC = "inspectionDesc";
    public static final String FIELD_INSPECTION_TYPE = "inspectionType";
    public static final String FIELD_STANDARD_TYPE = "standardType";
    public static final String FIELD_ACCURACY = "accuracy";
    public static final String FIELD_STANDARD_FROM = "standardFrom";
    public static final String FIELD_STANDARD_TO = "standardTo";
    public static final String FIELD_STANDARD_UOM = "standardUom";
    public static final String FIELD_DEFECT_LEVEL = "defectLevel";
    public static final String FIELD_STANDARD_TEXT = "standardText";
    public static final String FIELD_INSPECTION_TOOL = "inspectionTool";
    public static final String FIELD_INSPECTION_METHOD = "inspectionMethod";
    public static final String FIELD_SAMPLE_TYPE = "sampleType";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_REMARK = "remark";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


   @ApiModelProperty(value = "租户id")    
    private Long tenantId;
    @ApiModelProperty("主键id，标识唯一一条记录")
    @Id
    private String materialInspectionContentId;
    @ApiModelProperty(value = "物料检验计划主键id",required = true)
    @NotBlank
    private String schemeId;
    @ApiModelProperty(value = "检验组id",required = true)
    @NotBlank
    private String tagGroupId;
    @ApiModelProperty(value = "检验项id",required = true)
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
    @ApiModelProperty(value = "检验项类别",required = true)
    @NotBlank
	@LovValue(value = "QMS.INSPECTION_CONTENT_TYPE",meaningField = "inspectionTypeMeaning")
    private String inspectionType;
    @ApiModelProperty(value = "规格类型",required = true)
    @NotBlank
    private String standardType;
    @ApiModelProperty(value = "精度",required = true)
    @NotNull
    private BigDecimal accuracy;
   @ApiModelProperty(value = "规格值从")    
    private BigDecimal standardFrom;
   @ApiModelProperty(value = "规格值至")    
    private BigDecimal standardTo;
   @ApiModelProperty(value = "规格单位")    
    private String standardUom;
	@LovValue(value = "QMS.DEFECT_LEVEL",meaningField = "defectLevelMeaning")
   @ApiModelProperty(value = "缺陷等级")    
    private String defectLevel;
   @ApiModelProperty(value = "文本规格值")
    private String standardText;
    @ApiModelProperty(value = "检验工具",required = true)
    @NotBlank
	@LovValue(value = "QMS.INSPECTION_TOOL",meaningField = "inspectionToolMeaning")
    private String inspectionTool;
   @ApiModelProperty(value = "检验方法")
    private String inspectionMethod;
    @ApiModelProperty(value = "抽样类型",required = true)
    @NotBlank
    private String sampleType;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
   @ApiModelProperty(value = "备注")    
    private String remark;
    @ApiModelProperty(value = "cid",required = true)
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
     * @return 主键id，标识唯一一条记录
     */
	public String getMaterialInspectionContentId() {
		return materialInspectionContentId;
	}

	public void setMaterialInspectionContentId(String materialInspectionContentId) {
		this.materialInspectionContentId = materialInspectionContentId;
	}
    /**
     * @return 物料检验计划主键id
     */
	public String getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}
    /**
     * @return 检验组id
     */
	public String getTagGroupId() {
		return tagGroupId;
	}

	public void setTagGroupId(String tagGroupId) {
		this.tagGroupId = tagGroupId;
	}
    /**
     * @return 检验项id
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
     * @return 检验项类别
     */
	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
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

	public BigDecimal getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(BigDecimal accuracy) {
		this.accuracy = accuracy;
	}

	public BigDecimal getStandardFrom() {
		return standardFrom;
	}

	public void setStandardFrom(BigDecimal standardFrom) {
		this.standardFrom = standardFrom;
	}

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
     * @return 缺陷等级
     */
	public String getDefectLevel() {
		return defectLevel;
	}

	public void setDefectLevel(String defectLevel) {
		this.defectLevel = defectLevel;
	}
    /**
     * @return 检验工具
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
	public String getInspectionTool() {
		return inspectionTool;
	}

	public void setInspectionTool(String inspectionTool) {
		this.inspectionTool = inspectionTool;
	}

	/**
     * @return 检验方法
     */
	public String getInspectionMethod() {
		return inspectionMethod;
	}

	public void setInspectionMethod(String inspectionMethod) {
		this.inspectionMethod = inspectionMethod;
	}
    /**
     * @return 抽样类型
     */
	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
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
     * @return 备注
     */
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
    /**
     * @return 
     */
	public String getAttributeCategory() {
		return attributeCategory;
	}

	public void setAttributeCategory(String attributeCategory) {
		this.attributeCategory = attributeCategory;
	}
    /**
     * @return 
     */
	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
    /**
     * @return 
     */
	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
    /**
     * @return 
     */
	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
    /**
     * @return 
     */
	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
    /**
     * @return 
     */
	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}
    /**
     * @return 
     */
	public String getAttribute6() {
		return attribute6;
	}

	public void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}
    /**
     * @return 
     */
	public String getAttribute7() {
		return attribute7;
	}

	public void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}
    /**
     * @return 
     */
	public String getAttribute8() {
		return attribute8;
	}

	public void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}
    /**
     * @return 
     */
	public String getAttribute9() {
		return attribute9;
	}

	public void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}
    /**
     * @return 
     */
	public String getAttribute10() {
		return attribute10;
	}

	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}

}
