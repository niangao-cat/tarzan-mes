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
 * 质检单明细历史表
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
@ApiModel("质检单明细历史表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_iqc_details_his")
public class QmsIqcDetailsHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IQC_DETAILS_HIS_ID = "iqcDetailsHisId";
    public static final String FIELD_IQC_DETAILS_ID = "iqcDetailsId";
    public static final String FIELD_IQC_HEADER_ID = "iqcHeaderId";
    public static final String FIELD_IQC_LINE_ID = "iqcLineId";
    public static final String FIELD_NUMBER = "number";
    public static final String FIELD_RESULT = "result";
    public static final String FIELD_REMARK = "remark";
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
    @ApiModelProperty("检验单明细历史主键")
    @Id
    private String iqcDetailsHisId;
    @ApiModelProperty(value = "检验单明细主键ID")
    private String iqcDetailsId;
    @ApiModelProperty(value = "检验单头主键")
    private String iqcHeaderId;
    @ApiModelProperty(value = "检验单行主键")
    private String iqcLineId;
    @ApiModelProperty(value = "序号",required = true)
    @NotNull
    private Long number;
    @ApiModelProperty(value = "结果值",required = true)
    @NotBlank
    private String result;
    @ApiModelProperty(value = "备注",required = true)
    @NotBlank
    private String remark;
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
     * @return 检验单明细历史主键
     */
	public String getIqcDetailsHisId() {
		return iqcDetailsHisId;
	}

	public void setIqcDetailsHisId(String iqcDetailsHisId) {
		this.iqcDetailsHisId = iqcDetailsHisId;
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
