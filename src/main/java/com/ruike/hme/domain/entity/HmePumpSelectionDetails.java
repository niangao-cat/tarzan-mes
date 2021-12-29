package com.ruike.hme.domain.entity;

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

import java.util.Date;

/**
 * 泵浦源预筛选明细表
 *
 * @author chaonan.hu@hand-china.com 2021-08-30 10:59:48
 */
@ApiModel("泵浦源预筛选明细表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_pump_selection_details")
public class HmePumpSelectionDetails extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PUMP_SELECTION_DETAILS_ID = "pumpSelectionDetailsId";
    public static final String FIELD_PUMP_PRE_SELECTION_ID = "pumpPreSelectionId";
    public static final String FIELD_SELECTION_ORDER = "selectionOrder";
    public static final String FIELD_NEW_CONTAINER_ID = "newContainerId";
    public static final String FIELD_OLD_CONTAINER_ID = "oldContainerId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_PACKED_DATE = "packedDate";
    public static final String FIELD_PACKED_BY = "packedBy";
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
    private String pumpSelectionDetailsId;
    @ApiModelProperty(value = "预筛选基础ID",required = true)
    @NotBlank
    private String pumpPreSelectionId;
    @ApiModelProperty(value = "筛选顺序",required = true)
    @NotNull
    private Long selectionOrder;
    @ApiModelProperty(value = "新容器ID")
    private String newContainerId;
    @ApiModelProperty(value = "旧容器ID")
    private String oldContainerId;
    @ApiModelProperty(value = "泵浦源物料批ID",required = true)
    @NotBlank
    private String materialLotId;
    @ApiModelProperty(value = "泵浦源物料ID")
    private String materialId;
    @ApiModelProperty(value = "状态",required = true)
    @NotBlank
    private String status;
	@ApiModelProperty(value = "装箱时间")
	private Date packedDate;
	@ApiModelProperty(value = "装箱操作人")
	private Long packedBy;
    @ApiModelProperty(value = "CID",required = true)
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
	public String getPumpSelectionDetailsId() {
		return pumpSelectionDetailsId;
	}

	public void setPumpSelectionDetailsId(String pumpSelectionDetailsId) {
		this.pumpSelectionDetailsId = pumpSelectionDetailsId;
	}
    /**
     * @return 预筛选基础ID
     */
	public String getPumpPreSelectionId() {
		return pumpPreSelectionId;
	}

	public void setPumpPreSelectionId(String pumpPreSelectionId) {
		this.pumpPreSelectionId = pumpPreSelectionId;
	}
    /**
     * @return 筛选顺序
     */
	public Long getSelectionOrder() {
		return selectionOrder;
	}

	public void setSelectionOrder(Long selectionOrder) {
		this.selectionOrder = selectionOrder;
	}
    /**
     * @return 新容器ID
     */
	public String getNewContainerId() {
		return newContainerId;
	}

	public void setNewContainerId(String newContainerId) {
		this.newContainerId = newContainerId;
	}
    /**
     * @return 旧容器ID
     */
	public String getOldContainerId() {
		return oldContainerId;
	}

	public void setOldContainerId(String oldContainerId) {
		this.oldContainerId = oldContainerId;
	}
    /**
     * @return 泵浦源物料批ID
     */
	public String getMaterialLotId() {
		return materialLotId;
	}

	public void setMaterialLotId(String materialLotId) {
		this.materialLotId = materialLotId;
	}
    /**
     * @return 泵浦源物料ID
     */
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
    /**
     * @return 状态
     */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Date getPackedDate() {
		return packedDate;
	}

	public void setPackedDate(Date packedDate) {
		this.packedDate = packedDate;
	}

	public Long getPackedBy() {
		return packedBy;
	}

	public void setPackedBy(Long packedBy) {
		this.packedBy = packedBy;
	}
}
