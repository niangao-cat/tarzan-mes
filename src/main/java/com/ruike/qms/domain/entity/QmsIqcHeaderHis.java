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
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 质检单头历史表
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
@ApiModel("质检单头历史表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_iqc_header_his")
public class QmsIqcHeaderHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IQC_HEADER_HIS_ID = "iqcHeaderHisId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_IQC_HEADER_ID = "iqcHeaderId";
    public static final String FIELD_IQC_NUMBER = "iqcNumber";
    public static final String FIELD_RECEIPT_LOT = "receiptLot";
    public static final String FIELD_RECEIPT_BY = "receiptBy";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_INSPECTION_TYPE = "inspectionType";
    public static final String FIELD_UAI_FLAG = "uaiFlag";
    public static final String FIELD_INSPECTION_STATUS = "inspectionStatus";
    public static final String FIELD_DOC_TYPE = "docType";
    public static final String FIELD_DOC_HEADER_ID = "docHeaderId";
    public static final String FIELD_DOC_LINE_ID = "docLineId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_INSPECTION_METHOD = "inspectionMethod";
    public static final String FIELD_INSPECTION_START_DATE = "inspectionStartDate";
    public static final String FIELD_IDENTIFICATION = "identification";
    public static final String FIELD_INSPECTION_FINISH_DATE = "inspectionFinishDate";
    public static final String FIELD_INSPECTION_TIME = "inspectionTime";
    public static final String FIELD_INSPECTION_RESULT = "inspectionResult";
    public static final String FIELD_QC_BY = "qcBy";
    public static final String FIELD_OK_QTY = "okQty";
    public static final String FIELD_NG_QTY = "ngQty";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_FINAL_DECISION = "finalDecision";
    public static final String FIELD_AUDIT_OPINION = "auditOpinion";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty("质检单头历史表主键")
    @Id
    private String iqcHeaderHisId;
    @ApiModelProperty(value = "组织",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "检验单头表id",required = true)
    @NotBlank
    private String iqcHeaderId;
    @ApiModelProperty(value = "检验单号",required = true)
    @NotBlank
    private String iqcNumber;
    @ApiModelProperty(value = "接收批次",required = true)
    @NotBlank
    private String receiptLot;
    @ApiModelProperty(value = "接收人",required = true)
    @NotBlank
    private String receiptBy;
    @ApiModelProperty(value = "供应商ID",required = true)
    @NotBlank
    private String supplierId;
    @ApiModelProperty(value = "物料ID",required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "检验类型",required = true)
    @NotBlank
    private String inspectionType;
    @ApiModelProperty(value = "特采标识")
    private String uaiFlag;
    @ApiModelProperty(value = "检验状态",required = true)
    @NotBlank
    private String inspectionStatus;
    @ApiModelProperty(value = "检验来源",required = true)
    @NotBlank
    private String docType;
    @ApiModelProperty(value = "来源单号",required = true)
    @NotBlank
    private String docHeaderId;
    @ApiModelProperty(value = "来源单行号")
    private String docLineId;
    @ApiModelProperty(value = "货位",required = true)
    @NotBlank
    private String locatorId;
    @ApiModelProperty(value = "到货日期",required = true)
    @NotNull
    private Date createdDate;
    @ApiModelProperty(value = "物料数量",required = true)
    @NotNull
    private BigDecimal quantity;
    @ApiModelProperty(value = "单位",required = true)
    @NotBlank
    private String uomId;
    @ApiModelProperty(value = "检验方式",required = true)
    @NotBlank
    private String inspectionMethod;
    @ApiModelProperty(value = "检验开始时间")
    private Date inspectionStartDate;
    @ApiModelProperty(value = "检验单标识（如加急）")
    private String identification;
    @ApiModelProperty(value = "完成时间")
    private Date inspectionFinishDate;
    @ApiModelProperty(value = "检验时长（单位：小时）")
    private BigDecimal inspectionTime;
    @ApiModelProperty(value = "检验结果")
    private String inspectionResult;
    @ApiModelProperty(value = "检验员")
    private String qcBy;
    @ApiModelProperty(value = "合格项数")
    private Long okQty;
    @ApiModelProperty(value = "不合格项数")
    private Long ngQty;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "审核结果")
    private String finalDecision;
    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;
    @ApiModelProperty(value = "事件id",required = true)
    @NotBlank
    private String eventId;
	@ApiModelProperty(value = "报废数量")
	private BigDecimal scrapQty;
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
     * @return 质检单头历史表主键
     */
	public String getIqcHeaderHisId() {
		return iqcHeaderHisId;
	}

	public void setIqcHeaderHisId(String iqcHeaderHisId) {
		this.iqcHeaderHisId = iqcHeaderHisId;
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
     * @return 检验单头表id
     */
	public String getIqcHeaderId() {
		return iqcHeaderId;
	}

	public void setIqcHeaderId(String iqcHeaderId) {
		this.iqcHeaderId = iqcHeaderId;
	}
    /**
     * @return 检验单号
     */
	public String getIqcNumber() {
		return iqcNumber;
	}

	public void setIqcNumber(String iqcNumber) {
		this.iqcNumber = iqcNumber;
	}
    /**
     * @return 接收批次
     */
	public String getReceiptLot() {
		return receiptLot;
	}

	public void setReceiptLot(String receiptLot) {
		this.receiptLot = receiptLot;
	}
    /**
     * @return 接收人
     */
	public String getReceiptBy() {
		return receiptBy;
	}

	public void setReceiptBy(String receiptBy) {
		this.receiptBy = receiptBy;
	}
    /**
     * @return 供应商ID
     */
	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
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
     * @return 物料版本
     */
	public String getMaterialVersion() {
		return materialVersion;
	}

	public void setMaterialVersion(String materialVersion) {
		this.materialVersion = materialVersion;
	}
    /**
     * @return 检验类型
     */
	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
    /**
     * @return 特采标识
     */
	public String getUaiFlag() {
		return uaiFlag;
	}

	public void setUaiFlag(String uaiFlag) {
		this.uaiFlag = uaiFlag;
	}
    /**
     * @return 检验状态
     */
	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}
    /**
     * @return 检验来源
     */
	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}
    /**
     * @return 来源单号
     */
	public String getDocHeaderId() {
		return docHeaderId;
	}

	public void setDocHeaderId(String docHeaderId) {
		this.docHeaderId = docHeaderId;
	}
    /**
     * @return 来源单行号
     */
	public String getDocLineId() {
		return docLineId;
	}

	public void setDocLineId(String docLineId) {
		this.docLineId = docLineId;
	}
    /**
     * @return 货位
     */
	public String getLocatorId() {
		return locatorId;
	}

	public void setLocatorId(String locatorId) {
		this.locatorId = locatorId;
	}
    /**
     * @return 到货日期
     */
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
    /**
     * @return 物料数量
     */
	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
    /**
     * @return 单位
     */
	public String getUomId() {
		return uomId;
	}

	public void setUomId(String uomId) {
		this.uomId = uomId;
	}
    /**
     * @return 检验方式
     */
	public String getInspectionMethod() {
		return inspectionMethod;
	}

	public void setInspectionMethod(String inspectionMethod) {
		this.inspectionMethod = inspectionMethod;
	}
    /**
     * @return 检验开始时间
     */
	public Date getInspectionStartDate() {
		return inspectionStartDate;
	}

	public void setInspectionStartDate(Date inspectionStartDate) {
		this.inspectionStartDate = inspectionStartDate;
	}
    /**
     * @return 检验单标识（如加急）
     */
	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}
    /**
     * @return 完成时间
     */
	public Date getInspectionFinishDate() {
		return inspectionFinishDate;
	}

	public void setInspectionFinishDate(Date inspectionFinishDate) {
		this.inspectionFinishDate = inspectionFinishDate;
	}
    /**
     * @return 检验时长（单位：小时）
     */
	public BigDecimal getInspectionTime() {
		return inspectionTime;
	}

	public void setInspectionTime(BigDecimal inspectionTime) {
		this.inspectionTime = inspectionTime;
	}
    /**
     * @return 检验结果
     */
	public String getInspectionResult() {
		return inspectionResult;
	}

	public void setInspectionResult(String inspectionResult) {
		this.inspectionResult = inspectionResult;
	}
    /**
     * @return 检验员
     */
	public String getQcBy() {
		return qcBy;
	}

	public void setQcBy(String qcBy) {
		this.qcBy = qcBy;
	}
    /**
     * @return 合格项数
     */
	public Long getOkQty() {
		return okQty;
	}

	public void setOkQty(Long okQty) {
		this.okQty = okQty;
	}
    /**
     * @return 不合格项数
     */
	public Long getNgQty() {
		return ngQty;
	}

	public void setNgQty(Long ngQty) {
		this.ngQty = ngQty;
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
     * @return 审核结果
     */
	public String getFinalDecision() {
		return finalDecision;
	}

	public void setFinalDecision(String finalDecision) {
		this.finalDecision = finalDecision;
	}
    /**
     * @return 审核意见
     */
	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
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

	public BigDecimal getScrapQty() {
		return scrapQty;
	}

	public void setScrapQty(BigDecimal scrapQty) {
		this.scrapQty = scrapQty;
	}
}
