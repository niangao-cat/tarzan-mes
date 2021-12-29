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
 * 出库检行历史表
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:08
 */
@ApiModel("出库检行历史表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_oqc_line_his")
public class QmsOqcLineHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_OQC_LINE_HIS_ID = "oqcLineHisId";
    public static final String FIELD_OQC_HEADER_ID = "oqcHeaderId";
    public static final String FIELD_OQC_LINE_ID = "oqcLineId";
    public static final String FIELD_NUMBER = "number";
    public static final String FIELD_INSPECTION_TYPE = "inspectionType";
    public static final String FIELD_INSPECTION = "inspection";
    public static final String FIELD_INSPECTION_DESC = "inspectionDesc";
    public static final String FIELD_STANDARD_TYPE = "standardType";
    public static final String FIELD_STANDARD_TEXT = "standardText";
    public static final String FIELD_STANDARD_FROM = "standardFrom";
    public static final String FIELD_STANDARD_TO = "standardTo";
    public static final String FIELD_ACCURACY = "accuracy";
    public static final String FIELD_STANDARD_UOM = "standardUom";
    public static final String FIELD_INSPECTION_TOOL = "inspectionTool";
    public static final String FIELD_INSPECTION_RESULT = "inspectionResult";
    public static final String FIELD_ATTACHMENT_UUID = "attachmentUuid";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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
    @ApiModelProperty(value = "事件ID",required = true)
    @NotBlank
    private String eventId;
    @ApiModelProperty(value = "检验单行历史表主键ID")
	@Id
	@GeneratedValue
    private String oqcLineHisId;
    @ApiModelProperty(value = "检验单头表ID",required = true)
    @NotBlank
    private String oqcHeaderId;
    @ApiModelProperty(value = "检验单行主键ID",required = true)
    @NotBlank
    private String oqcLineId;
    @ApiModelProperty(value = "检验项序号",required = true)
    @NotNull
    private BigDecimal number;
    @ApiModelProperty(value = "检验项类别",required = true)
    @NotBlank
    private String inspectionType;
    @ApiModelProperty(value = "检验项目",required = true)
    @NotBlank
    private String inspection;
    @ApiModelProperty(value = "检验项描述",required = true)
    @NotBlank
    private String inspectionDesc;
    @ApiModelProperty(value = "规格类型",required = true)
    @NotBlank
    private String standardType;
    @ApiModelProperty(value = "文本规格值")
    private String standardText;
    @ApiModelProperty(value = "规格值从")
    private BigDecimal standardFrom;
    @ApiModelProperty(value = "规格值至")
    private BigDecimal standardTo;
    @ApiModelProperty(value = "精度",required = true)
    private BigDecimal accuracy;
    @ApiModelProperty(value = "规格单位")
    private String standardUom;
    @ApiModelProperty(value = "检验工具")
    private String inspectionTool;
    @ApiModelProperty(value = "结论")
    private String inspectionResult;
    @ApiModelProperty(value = "附件ID")
    private String attachmentUuid;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
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
     * @return 事件ID
     */
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
    /**
     * @return 检验单行历史表主键ID
     */
	public String getOqcLineHisId() {
		return oqcLineHisId;
	}

	public void setOqcLineHisId(String oqcLineHisId) {
		this.oqcLineHisId = oqcLineHisId;
	}
    /**
     * @return 检验单头表ID
     */
	public String getOqcHeaderId() {
		return oqcHeaderId;
	}

	public void setOqcHeaderId(String oqcHeaderId) {
		this.oqcHeaderId = oqcHeaderId;
	}
    /**
     * @return 检验单行主键ID
     */
	public String getOqcLineId() {
		return oqcLineId;
	}

	public void setOqcLineId(String oqcLineId) {
		this.oqcLineId = oqcLineId;
	}
    /**
     * @return 检验项序号
     */
	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
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
     * @return 规格类型
     */
	public String getStandardType() {
		return standardType;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
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
     * @return 精度
     */
	public BigDecimal getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(BigDecimal accuracy) {
		this.accuracy = accuracy;
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
     * @return 检验工具
     */
	public String getInspectionTool() {
		return inspectionTool;
	}

	public void setInspectionTool(String inspectionTool) {
		this.inspectionTool = inspectionTool;
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
