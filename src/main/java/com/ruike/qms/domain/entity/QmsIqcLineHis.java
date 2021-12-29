package com.ruike.qms.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 质检单行历史表
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
@ApiModel("质检单行历史表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_iqc_line_his")
public class QmsIqcLineHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IQC_LINE_HIS_ID = "iqcLineHisId";
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
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("检验单行历史表主键")
    @Id
    private String iqcLineHisId;
    @ApiModelProperty(value = "检验单头表ID",required = true)
    @NotBlank
    private String iqcHeaderId;
    @ApiModelProperty(value = "检验单行主键ID",required = true)
    @NotBlank
    private String iqcLineId;
    @ApiModelProperty(value = "检验项序号",required = true)
    @NotNull
    private Long number;
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
    @ApiModelProperty(value = "检验水平",required = true)
    @NotBlank
    private String inspectionLevels;
    @ApiModelProperty(value = "缺陷等级",required = true)
    @NotBlank
    private String defectLevels;
    @ApiModelProperty(value = "AQL值")
    private BigDecimal acceptanceQuantityLimit;
    @ApiModelProperty(value = "抽样数量",required = true)
    @NotNull
    private BigDecimal sampleSize;
    @ApiModelProperty(value = "AC值",required = true)
    @NotNull
    private BigDecimal ac;
    @ApiModelProperty(value = "RE值",required = true)
    @NotNull
    private BigDecimal re;
    @ApiModelProperty(value = "文本规格值")
    private String standardText;
    @ApiModelProperty(value = "规格值从",required = true)
    @NotNull
    private BigDecimal standardFrom;
    @ApiModelProperty(value = "规格值至",required = true)
    @NotNull
    private BigDecimal standardTo;
    @ApiModelProperty(value = "规格单位",required = true)
    @NotNull
    private BigDecimal standardUom;
    @ApiModelProperty(value = "规格类型",required = true)
    @NotBlank
    private String standardType;
    @ApiModelProperty(value = "检验工具",required = true)
    @NotBlank
    private String inspectionTool;
    @ApiModelProperty(value = "不良数")
    private BigDecimal ngQty;
    @ApiModelProperty(value = "合格数")
    private BigDecimal okQty;
    @ApiModelProperty(value = "结论")
    private String inspectionResult;
    @ApiModelProperty(value = "附件ID")
    private String attachmentUuid;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "新增标识")
    private String addedFlag;
    @ApiModelProperty(value = "事件id",required = true)
    @NotBlank
    private String eventId;
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
     * @return 租户ID
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 检验单行历史表主键
     */
	public String getIqcLineHisId() {
		return iqcLineHisId;
	}

	public void setIqcLineHisId(String iqcLineHisId) {
		this.iqcLineHisId = iqcLineHisId;
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
	public BigDecimal getAcceptanceQuantityLimit() {
		return acceptanceQuantityLimit;
	}

	public void setAcceptanceQuantityLimit(BigDecimal acceptanceQuantityLimit) {
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
	public BigDecimal getStandardUom() {
		return standardUom;
	}

	public void setStandardUom(BigDecimal standardUom) {
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
	public BigDecimal getNgQty() {
		return ngQty;
	}

	public void setNgQty(BigDecimal ngQty) {
		this.ngQty = ngQty;
	}
    /**
     * @return 合格数
     */
	public BigDecimal getOkQty() {
		return okQty;
	}

	public void setOkQty(BigDecimal okQty) {
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
     * @return 是否有效
     */
	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}
    /**
     * @return 新增标识
     */
	public String getAddedFlag() {
		return addedFlag;
	}

	public void setAddedFlag(String addedFlag) {
		this.addedFlag = addedFlag;
	}
    /**
     * @return 事件id
     */
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
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
