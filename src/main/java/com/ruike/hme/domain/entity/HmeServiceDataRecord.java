package com.ruike.hme.domain.entity;

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
 * 售后返品信息采集确认表
 *
 * @author sanfeng.zhang@hand-china.com 2020-09-03 15:20:59
 */
@ApiModel("售后返品信息采集确认表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_service_data_record")
@CustomPrimary
public class HmeServiceDataRecord extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SERVICE_DATA_RECORD_ID = "serviceDataRecordId";
    public static final String FIELD_SERVICE_RECEIVE_ID = "serviceReceiveId";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_LOGISTICS_NUMBER = "logisticsNumber";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_WKC_SHIFT_ID = "wkcShiftId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_BUSINESS_TYPE = "businessType";
    public static final String FIELD_RESULT = "result";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_ATTACHMENT_UUID = "attachmentUuid";
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
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    private String serviceDataRecordId;
    @ApiModelProperty(value = "售后接收信息主键",required = true)
    @NotBlank
    private String serviceReceiveId;
    @ApiModelProperty(value = "售后接收机器SN",required = true)
    @NotBlank
    private String snNum;
    @ApiModelProperty(value = "物流单号",required = true)
    @NotBlank
    private String logisticsNumber;
    @ApiModelProperty(value = "工位ID",required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "班次ID")
    private String wkcShiftId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "收集组ID",required = true)
    @NotBlank
    private String tagGroupId;
    @ApiModelProperty(value = "收集组类型",required = true)
    @NotBlank
    private String businessType;
    @ApiModelProperty(value = "采集结果")
    private String result;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "附件组ID")
    private String attachmentUuid;
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
    @ApiModelProperty(value = "")
    private String attribute11;
    @ApiModelProperty(value = "")
    private String attribute12;
    @ApiModelProperty(value = "")
    private String attribute13;
    @ApiModelProperty(value = "")
    private String attribute14;
    @ApiModelProperty(value = "")
    private String attribute15;

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
     * @return 主键
     */
	public String getServiceDataRecordId() {
		return serviceDataRecordId;
	}

	public void setServiceDataRecordId(String serviceDataRecordId) {
		this.serviceDataRecordId = serviceDataRecordId;
	}
    /**
     * @return 售后接收信息主键
     */
	public String getServiceReceiveId() {
		return serviceReceiveId;
	}

	public void setServiceReceiveId(String serviceReceiveId) {
		this.serviceReceiveId = serviceReceiveId;
	}
    /**
     * @return 售后接收机器SN
     */
	public String getSnNum() {
		return snNum;
	}

	public void setSnNum(String snNum) {
		this.snNum = snNum;
	}
    /**
     * @return 物流单号
     */
	public String getLogisticsNumber() {
		return logisticsNumber;
	}

	public void setLogisticsNumber(String logisticsNumber) {
		this.logisticsNumber = logisticsNumber;
	}
    /**
     * @return 工位ID
     */
	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}
    /**
     * @return 班次ID
     */
	public String getWkcShiftId() {
		return wkcShiftId;
	}

	public void setWkcShiftId(String wkcShiftId) {
		this.wkcShiftId = wkcShiftId;
	}
    /**
     * @return 工艺ID
     */
	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
    /**
     * @return 收集组ID
     */
	public String getTagGroupId() {
		return tagGroupId;
	}

	public void setTagGroupId(String tagGroupId) {
		this.tagGroupId = tagGroupId;
	}
    /**
     * @return 收集组类型
     */
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
    /**
     * @return 采集结果
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
     * @return 附件组ID
     */
	public String getAttachmentUuid() {
		return attachmentUuid;
	}

	public void setAttachmentUuid(String attachmentUuid) {
		this.attachmentUuid = attachmentUuid;
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
    /**
     * @return 
     */
	public String getAttribute11() {
		return attribute11;
	}

	public void setAttribute11(String attribute11) {
		this.attribute11 = attribute11;
	}
    /**
     * @return 
     */
	public String getAttribute12() {
		return attribute12;
	}

	public void setAttribute12(String attribute12) {
		this.attribute12 = attribute12;
	}
    /**
     * @return 
     */
	public String getAttribute13() {
		return attribute13;
	}

	public void setAttribute13(String attribute13) {
		this.attribute13 = attribute13;
	}
    /**
     * @return 
     */
	public String getAttribute14() {
		return attribute14;
	}

	public void setAttribute14(String attribute14) {
		this.attribute14 = attribute14;
	}
    /**
     * @return 
     */
	public String getAttribute15() {
		return attribute15;
	}

	public void setAttribute15(String attribute15) {
		this.attribute15 = attribute15;
	}

}
