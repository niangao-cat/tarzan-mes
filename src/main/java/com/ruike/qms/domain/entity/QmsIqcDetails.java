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
 * 质检单明细表
 *
 * @author tong.li05@hand-china.com 2020-04-29 13:50:51
 */
@ApiModel("质检单明细表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_iqc_details")
@CustomPrimary
public class QmsIqcDetails extends AuditDomain {

	public static final String FIELD_TENANT_ID = "tenantId";
	public static final String FIELD_IQC_DETAILS_ID = "iqcDetailsId";
	public static final String FIELD_IQC_HEADER_ID = "iqcHeaderId";
	public static final String FIELD_IQC_LINE_ID = "iqcLineId";
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_RESULT = "result";
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


	@ApiModelProperty(value = "")
	private Long tenantId;
    @ApiModelProperty("检验单明细主键ID")
    @Id
    private String iqcDetailsId;
	@ApiModelProperty(value = "检验单头主键", required = true)
	@NotBlank
    private String iqcHeaderId;
	@ApiModelProperty(value = "检验单行主键", required = true)
	@NotBlank
	private String iqcLineId;
	@ApiModelProperty(value = "序号", required = true)
	@NotNull
	private Long number;
	@ApiModelProperty(value = "结果值", required = true)
	@NotBlank
	private String result;
	@ApiModelProperty(value = "备注")
	private String remark;
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
	 * @return
	 */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return 检验单明细主键ID
	 */
	public String getIqcDetailsId() {
		return iqcDetailsId;
	}

	public void setIqcDetailsId(String iqcDetailsId) {
		this.iqcDetailsId = iqcDetailsId;
	}

	/**
	 * @return 检验单头主键
	 */
	public String getIqcHeaderId() {
		return iqcHeaderId;
	}

	public void setIqcHeaderId(String iqcHeaderId) {
		this.iqcHeaderId = iqcHeaderId;
	}

	/**
	 * @return 检验单行主键
	 */
	public String getIqcLineId() {
		return iqcLineId;
	}

	public void setIqcLineId(String iqcLineId) {
		this.iqcLineId = iqcLineId;
	}

	/**
	 * @return 序号
	 */
	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * @return 结果值
	 */
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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
