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
 * 质检单行表
 *
 * @author tong.li05@hand-china.com 2020-04-29 13:43:23
 */
@ApiModel("质检单行表")
@ModifyAudit
@CustomPrimary
@VersionAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_iqc_line")
public class QmsIqcLine extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IQC_HEADER_ID = "iqcHeaderId";
    public static final String FIELD_IQC_LINE_ID = "iqcLineId";
    public static final String FIELD_NUMBER = "number";
    public static final String FIELD_INSPECTION_TYPE = "inspectionType";
    public static final String FIELD_INSPECTION = "inspection";
    public static final String FIELD_INSPECTION_DESC = "inspectionDesc";
    public static final String FIELD_SAMPLE_TYPE = "sampleType";
    public static final String FIELD_INSPECTION_LEVELS = "inspectionLevels";
    public static final String FIELD_DEFECT_LEVELS = "defectLevels";
    public static final String FIELD_ACCEPTANCE_QUANTITY_LIMIT = "acceptanceQuantityLimit";
    public static final String FIELD_SAMPLE_SIZE = "sampleSize";
    public static final String FIELD_AC = "ac";
    public static final String FIELD_RE = "re";
    public static final String FIELD_STANDARD_TEXT = "standardText";
    public static final String FIELD_STANDARD_FROM = "standardFrom";
    public static final String FIELD_STANDARD_TO = "standardTo";
    public static final String FIELD_STANDARD_UOM = "standardUom";
	public static final String FIELD_STANDARD_TYPE = "standardType";
    public static final String FIELD_INSPECTION_TOOL = "inspectionTool";
    public static final String FIELD_NG_QTY = "ngQty";
    public static final String FIELD_OK_QTY = "okQty";
    public static final String FIELD_INSPECTION_RESULT = "inspectionResult";
    public static final String FIELD_ATTACHMENT_UUID = "attachmentUuid";
	public static final String FIELD_ENABLE_FLAG = "enableFlag";
	public static final String FIELD_ADDED_FLAG = "addedFlag";
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


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "检验单头表ID",required = true)
    @NotBlank
    private String iqcHeaderId;
    @ApiModelProperty("检验单行主键ID")
    @Id
    private String iqcLineId;
    @ApiModelProperty(value = "检验项序号",required = true)
    @NotNull
    private Long number;
	@LovValue(value = "QMS.INSPECTION_CONTENT_TYPE", meaningField = "inspectionTypeMeaning")
    @ApiModelProperty(value = "检验项类别",required = true)
    @NotBlank
    private String inspectionType;
    @ApiModelProperty(value = "检验项目",required = true)
    @NotBlank
    private String inspection;
    @ApiModelProperty(value = "检验项描述",required = true)
    @NotBlank
    private String inspectionDesc;
    @ApiModelProperty(value = "抽样方案类型",required = true)
    @NotBlank
    private String sampleType;
    @ApiModelProperty(value = "检验水平")
	@LovValue(value = "QMS.IQC_INSPECTION_LEVELS", meaningField = "inspectionLevelsMeaning")
	private String inspectionLevels;
    @ApiModelProperty(value = "缺陷等级",required = true)
    @NotBlank
	@LovValue(value = "QMS.DEFECT_LEVEL", meaningField = "defectLevelsMeaning")
    private String defectLevels;
    @ApiModelProperty(value = "AQL值",required = true)
    private String acceptanceQuantityLimit;
    @ApiModelProperty(value = "抽样数量",required = true)
    private BigDecimal sampleSize;
    @ApiModelProperty(value = "AC值",required = true)
    @NotNull
    private BigDecimal ac;
    @ApiModelProperty(value = "RE值",required = true)
    @NotNull
    private BigDecimal re;
   @ApiModelProperty(value = "文本规格值")    
    private String standardText;
   @ApiModelProperty(value = "规格值从")    
    private BigDecimal standardFrom;
   @ApiModelProperty(value = "规格值至")    
    private BigDecimal standardTo;
   @ApiModelProperty(value = "规格单位")    
    private String standardUom;
	@ApiModelProperty(value = "规格类型")
	@NotBlank
	private String standardType;
    @ApiModelProperty(value = "检验工具",required = true)
    @NotBlank
	@LovValue(value = "QMS.INSPECTION_TOOL", meaningField = "inspectionToolMeaning")
    private String inspectionTool;
   @ApiModelProperty(value = "不良数")    
    private Long ngQty;
   @ApiModelProperty(value = "合格数")    
    private Long okQty;
   @ApiModelProperty(value = "结论")
   @LovValue(value = "QMS.INSPECTION_RESULT", meaningField = "inspectionResultMeaning")
    private String inspectionResult;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
	@ApiModelProperty(value = "新增标识")
	private String addedFlag;
	@ApiModelProperty(value = "附件ID")
	private String attachmentUuid;
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
     * @return 租户ID
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 检验单头表ID
     */
	public String getIqcHeaderId() {
		return iqcHeaderId;
	}

	public void setIqcHeaderId(String iqcHeaderId) {
		this.iqcHeaderId = iqcHeaderId;
	}
    /**
     * @return 检验单行主键ID
     */
	public String getIqcLineId() {
		return iqcLineId;
	}

	public void setIqcLineId(String iqcLineId) {
		this.iqcLineId = iqcLineId;
	}
    /**
     * @return 检验项序号
     */
	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
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
     * @return 检验项目
     */
	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}
    /**
     * @return 检验项描述
     */
	public String getInspectionDesc() {
		return inspectionDesc;
	}

	public void setInspectionDesc(String inspectionDesc) {
		this.inspectionDesc = inspectionDesc;
	}
    /**
     * @return 抽样方案类型
     */
	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
    /**
     * @return 检验水平
     */
	public String getInspectionLevels() {
		return inspectionLevels;
	}

	public void setInspectionLevels(String inspectionLevels) {
		this.inspectionLevels = inspectionLevels;
	}
    /**
     * @return 缺陷等级
     */
	public String getDefectLevels() {
		return defectLevels;
	}

	public void setDefectLevels(String defectLevels) {
		this.defectLevels = defectLevels;
	}
    /**
     * @return AQL值
     */
	public String getAcceptanceQuantityLimit() {
		return acceptanceQuantityLimit;
	}

	public void setAcceptanceQuantityLimit(String acceptanceQuantityLimit) {
		this.acceptanceQuantityLimit = acceptanceQuantityLimit;
	}

	/**
     * @return 抽样数量
     */
	public BigDecimal getSampleSize() {
		return sampleSize;
	}

	public void setSampleSize(BigDecimal sampleSize) {
		this.sampleSize = sampleSize;
	}
    /**
     * @return AC值
     */
	public BigDecimal getAc() {
		return ac;
	}

	public void setAc(BigDecimal ac) {
		this.ac = ac;
	}
    /**
     * @return RE值
     */
	public BigDecimal getRe() {
		return re;
	}

	public void setRe(BigDecimal re) {
		this.re = re;
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
	 * @return 规格类型
	 */
	public String getStandardType() {
		return standardType;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
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
     * @return 不良数
     */
	public Long getNgQty() {
		return ngQty;
	}

	public void setNgQty(Long ngQty) {
		this.ngQty = ngQty;
	}

	/**
     * @return 合格数
     */
	public Long getOkQty() {
		return okQty;
	}

	public void setOkQty(Long okQty) {
		this.okQty = okQty;
	}

	/**
     * @return 结论
     */
	public String getInspectionResult() {
		return inspectionResult;
	}

	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getAddedFlag() {
		return addedFlag;
	}

	public void setAddedFlag(String addedFlag) {
		this.addedFlag = addedFlag;
	}

	/**
     * @return 附件ID
     */
	public String getAttachmentUuid() {
		return attachmentUuid;
	}

	public void setAttachmentUuid(String attachmentUuid) {
		this.attachmentUuid = attachmentUuid;
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
