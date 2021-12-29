package com.ruike.qms.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * 质检单头表
 *
 * @author tong.li05@hand-china.com 2020-04-28 19:47:39
 */
@ApiModel("质检单头表")
@ModifyAudit
@CustomPrimary
@VersionAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_iqc_header")
public class QmsIqcHeader extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
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
	@ApiModelProperty(value = "组织")
    private String siteId;
    @ApiModelProperty("检验单头表主键")
    @Id
    private String iqcHeaderId;
	@ApiModelProperty(value = "检验单号", required = true)
	@NotBlank
    private String iqcNumber;
	@ApiModelProperty(value = "接收批次", required = true)
	@NotBlank
    private String receiptLot;
	@ApiModelProperty(value = "接收人", required = true)
	@NotBlank
	private String receiptBy;
	@ApiModelProperty(value = "供应商ID", required = true)
	@NotBlank
	private String supplierId;
	@ApiModelProperty(value = "物料ID", required = true)
	@NotBlank
	private String materialId;
	@ApiModelProperty(value = "物料版本")
	private String materialVersion;

	@LovValue(value = "QMS.DOC_INSPECTION_TYPE", meaningField = "inspectionTypeMeaning")
	@ApiModelProperty(value = "检验类型", required = true)
	@NotBlank
	private String inspectionType;
	@ApiModelProperty(value = "特采标识")
	private String uaiFlag;
	@LovValue(value = "QMS.INSPECTION_DOC_STATUS", meaningField = "inspectionStatusMeaning")
	@ApiModelProperty(value = "检验状态", required = true)
	@NotBlank
	private String inspectionStatus;
	@ApiModelProperty(value = "检验来源", required = true)
	@NotBlank
	@LovValue(value = "QMS.DOC_TYPE", meaningField = "docTypeMeaning")
	private String docType;
	@ApiModelProperty(value = "来源单号", required = true)
	@NotBlank
	private String docHeaderId;
	@ApiModelProperty(value = "来源单行号")
    private String docLineId;
	@ApiModelProperty(value = "货位", required = true)
	@NotBlank
	private String locatorId;
	@ApiModelProperty(value = "建单日期", required = true)
	@NotNull
	private Date createdDate;
	@ApiModelProperty(value = "物料数量", required = true)
	@NotNull
	private BigDecimal quantity;
	@ApiModelProperty(value = "单位", required = true)
	@NotBlank
	private String uomId;
	@LovValue(value = "QMS.INSPECTION_METHOD", meaningField = "inspectionMethodMeaning")
	@ApiModelProperty(value = "检验方式", required = true)
	@NotBlank
	private String inspectionMethod;
	@ApiModelProperty(value = "检验开始时间")
	private Date inspectionStartDate;
	@LovValue(value = "QMS.IDENTIFICATION", meaningField = "identificationMeaning")
	@ApiModelProperty(value = "检验单标识（如加急）")
	private String identification;
	@ApiModelProperty(value = "完成时间")
	private Date inspectionFinishDate;
	@ApiModelProperty(value = "检验时长（单位：小时）")
	private BigDecimal inspectionTime;
	@LovValue(value = "QMS.INSPECTION_RESULT", meaningField = "inspectionResultMeaning")
	@ApiModelProperty(value = "检验结果")
	private String inspectionResult;
	@ApiModelProperty(value = "检验员")
	private Long qcBy;
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
	@ApiModelProperty(value = "报废数量")
	private BigDecimal scrapQty;
	@ApiModelProperty(value = "cid", required = true)
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
	 * @return 组织
	 */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return 检验单头表主键
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
	 * @return 建单日期
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

	/**
	 * @return 检验员
	 */
	public Long getQcBy() {
		return qcBy;
	}

	public void setQcBy(Long qcBy) {
		this.qcBy = qcBy;
	}

	public BigDecimal getScrapQty() {
		return scrapQty;
	}

	public void setScrapQty(BigDecimal scrapQty) {
		this.scrapQty = scrapQty;
	}
}
