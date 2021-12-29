package com.ruike.wms.domain.entity;

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

/**
 * 发货箱号表
 *
 * @author yonghui.zhu@hand-china.com 2020-12-09 13:44:15
 */
@ApiModel("发货箱号表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_so_delivery_container_num")
@CustomPrimary
public class WmsSoDeliveryContainerNum extends AuditDomain {

	public static final String FIELD_TENANT_ID = "tenantId";
	public static final String FIELD_DELIVERY_CONTAINER_ID = "deliveryContainerId";
	public static final String FIELD_INSTRUCTION_DOC_ID = "instructionDocId";
	public static final String FIELD_INSTRUCTION_ID = "instructionId";
	public static final String FIELD_CONTAINER_NUM = "containerNum";
	public static final String FIELD_SEAL_NUM = "sealNum";
	public static final String FIELD_CAR_NUM = "carNum";
	public static final String FIELD_LICENCE_NUM = "licenceNum";
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


	@ApiModelProperty("租户id")
	@Id
	private Long tenantId;
	@ApiModelProperty(value = "主键", required = true)
	@NotBlank
	private String deliveryContainerId;
	@ApiModelProperty(value = "单据ID", required = true)
	@NotBlank
	private String instructionDocId;
	@ApiModelProperty(value = "单据行ID", required = true)
	@NotBlank
	private String instructionId;
	@ApiModelProperty(value = "箱号")
	private String containerNum;
	@ApiModelProperty(value = "封号")
	private String sealNum;
	@ApiModelProperty(value = "车号")
	private String carNum;
	@ApiModelProperty(value = "车牌号")
	private String licenceNum;
	@ApiModelProperty(value = "CID", required = true)
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
	public String getDeliveryContainerId() {
		return deliveryContainerId;
	}

	public void setDeliveryContainerId(String deliveryContainerId) {
		this.deliveryContainerId = deliveryContainerId;
	}

	/**
	 * @return 单据ID
	 */
	public String getInstructionDocId() {
		return instructionDocId;
	}

	public void setInstructionDocId(String instructionDocId) {
		this.instructionDocId = instructionDocId;
	}

	/**
	 * @return 单据行ID
	 */
	public String getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(String instructionId) {
		this.instructionId = instructionId;
	}

	/**
	 * @return 箱号
	 */
	public String getContainerNum() {
		return containerNum;
	}

	public void setContainerNum(String containerNum) {
		this.containerNum = containerNum;
	}

	/**
	 * @return 封号
	 */
	public String getSealNum() {
		return sealNum;
	}

	public void setSealNum(String sealNum) {
		this.sealNum = sealNum;
	}

	/**
	 * @return 车号
	 */
	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	/**
	 * @return 车牌号
	 */
	public String getLicenceNum() {
		return licenceNum;
	}

	public void setLicenceNum(String licenceNum) {
		this.licenceNum = licenceNum;
	}

	/**
	 * @return CID
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
