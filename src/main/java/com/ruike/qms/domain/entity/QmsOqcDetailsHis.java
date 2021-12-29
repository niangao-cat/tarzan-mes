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
 * 出库检明细历史表
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:07
 */
@ApiModel("出库检明细历史表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_oqc_details_his")
public class QmsOqcDetailsHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_OQC_DETAILS_HIS_ID = "oqcDetailsHisId";
    public static final String FIELD_OQC_DETAILS_ID = "oqcDetailsId";
    public static final String FIELD_OQC_HEADER_ID = "oqcHeaderId";
    public static final String FIELD_OQC_LINE_ID = "oqcLineId";
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


	@ApiModelProperty(value = "租户ID",required = true)
	@NotNull
	private Long tenantId;
    @ApiModelProperty(value = "事件ID",required = true)
    @NotBlank
    private String eventId;
    @ApiModelProperty(value = "检验单明细历史表主键ID")
	@Id
	@GeneratedValue
    private String oqcDetailsHisId;
    @ApiModelProperty(value = "检验单明细主键ID",required = true)
    @NotBlank
    private String oqcDetailsId;
    @ApiModelProperty(value = "检验单头表ID",required = true)
    @NotBlank
    private String oqcHeaderId;
    @ApiModelProperty(value = "检验单行主键ID",required = true)
    @NotBlank
    private String oqcLineId;
    @ApiModelProperty(value = "序号",required = true)
    @NotNull
    private Long number;
    @ApiModelProperty(value = "结果值",required = true)
    private String result;
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
     * @return 检验单明细历史表主键ID
     */
	public String getOqcDetailsHisId() {
		return oqcDetailsHisId;
	}

	public void setOqcDetailsHisId(String oqcDetailsHisId) {
		this.oqcDetailsHisId = oqcDetailsHisId;
	}
    /**
     * @return 检验单明细主键ID
     */
	public String getOqcDetailsId() {
		return oqcDetailsId;
	}

	public void setOqcDetailsId(String oqcDetailsId) {
		this.oqcDetailsId = oqcDetailsId;
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
