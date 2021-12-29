package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.*;
import java.util.*;

/**
 * 工单入库日期试算表
 *
 * @author yuchao.wang@hand-china.com 2020-08-25 21:54:56
 */
@ApiModel("工单入库日期试算表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_wo_trial_calculate")
@CustomPrimary
public class HmeWoTrialCalculate extends AuditDomain {

	public static final String FIELD_TENANT_ID = "tenantId";
	public static final String FIELD_WO_TRIAL_CALCULATE_ID = "woTrialCalculateId";
	public static final String FIELD_WORK_ORDER_ID = "workOrderId";
	public static final String FIELD_DATE_FROM = "dateFrom";
	public static final String FIELD_DATE_TO = "dateTo";
	public static final String FIELD_QTY = "qty";
	public static final String FIELD_SHIFT_DATE = "shiftDate";
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


	@ApiModelProperty("表ID，主键")
	@Id
	@GeneratedValue
	private Long tenantId;
	@ApiModelProperty(value = "主键ID，标识唯一一条记录",required = true)
	@NotBlank
	private String woTrialCalculateId;
	@ApiModelProperty(value = "生产指令ID",required = true)
	@NotBlank
	private String workOrderId;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "计划开始时间")
	private LocalDate dateFrom;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "计划结束时间")
	private LocalDate dateTo;
	@ApiModelProperty(value = "预排数量",required = true)
	@NotNull
	private Long qty;
	@JsonFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "",required = true)
	@NotNull
	private LocalDate shiftDate;
	@ApiModelProperty(value = "",required = true)
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

	/**
	 * @return 表ID，主键
	 */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * @return 主键ID，标识唯一一条记录
	 */
	public String getWoTrialCalculateId() {
		return woTrialCalculateId;
	}

	public void setWoTrialCalculateId(String woTrialCalculateId) {
		this.woTrialCalculateId = woTrialCalculateId;
	}
	/**
	 * @return 生产指令ID
	 */
	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
	/**
	 * @return 计划开始时间
	 */
	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * @return 计划结束时间
	 */
	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	/**
	 * @return 预排数量
	 */
	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}
	/**
	 * @return
	 */
	public LocalDate getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(LocalDate shiftDate) {
		this.shiftDate = shiftDate;
	}

	/**
	 * @return
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
