package com.ruike.hme.domain.entity;

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
 * 来料信息记录历史表
 *
 * @author sanfeng.zhang@hand-china.com 2021-11-24 18:46:25
 */
@ApiModel("来料信息记录历史表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_cos_operation_record_his")
@CustomPrimary
public class HmeCosOperationRecordHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COS_OPERATION_RECORD_HIS = "cosOperationRecordHisId";
    public static final String FIELD_OPERATION_RECORD_ID = "operationRecordId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_CONTAINER_TYPE_ID = "containerTypeId";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_AVERAGE_WAVELENGTH = "averageWavelength";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_LOT_NO = "lotNo";
    public static final String FIELD_WAFER = "wafer";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_JOB_BATCH = "jobBatch";
    public static final String FIELD_BAR_NUM = "barNum";
    public static final String FIELD_COS_NUM = "cosNum";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_EQUIPMENT_ID = "equipmentId";
    public static final String FIELD_SURPLUS_COS_NUM = "surplusCosNum";
    public static final String FIELD_MATERIAL_ID = "materialId";
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


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank
	@Id
    private String cosOperationRecordHisId;
    @ApiModelProperty("记录主键")
    private String operationRecordId;
    @ApiModelProperty(value = "工厂ID",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "工单id",required = true)
    @NotBlank
    private String workOrderId;
   @ApiModelProperty(value = "容器类型id")    
    private String containerTypeId;
   @ApiModelProperty(value = "COS类型")    
    private String cosType;
   @ApiModelProperty(value = "平均波长 Avg λ（nm）")    
    private BigDecimal averageWavelength;
   @ApiModelProperty(value = "类型")    
    private String type;
   @ApiModelProperty(value = "LOTNO")    
    private String lotNo;
   @ApiModelProperty(value = "wafer")    
    private String wafer;
   @ApiModelProperty(value = "备注")    
    private String remark;
   @ApiModelProperty(value = "作业批次")    
    private String jobBatch;
   @ApiModelProperty(value = "BAR条数")    
    private Long barNum;
   @ApiModelProperty(value = "芯片数")    
    private Long cosNum;
   @ApiModelProperty(value = "工艺id")    
    private String operationId;
   @ApiModelProperty(value = "wkcid")    
    private String workcellId;
   @ApiModelProperty(value = "设备id")    
    private String equipmentId;
   @ApiModelProperty(value = "剩余芯片数")    
    private Long surplusCosNum;
   @ApiModelProperty(value = "芯片物料id")    
    private String materialId;
    @ApiModelProperty(value = "",required = true)
    @NotNull
	@Cid
    private Long cid;
   @ApiModelProperty(value = "")    
    private String attributeCategory;
   @ApiModelProperty(value = "批次")    
    private String attribute1;
   @ApiModelProperty(value = "供应商id")    
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
     * @return 租户ID
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 主键ID
     */
	public String getCosOperationRecordHisId() {
		return cosOperationRecordHisId;
	}

	public void setCosOperationRecordHisId(String cosOperationRecordHisId) {
		this.cosOperationRecordHisId = cosOperationRecordHisId;
	}
    /**
     * @return 记录主键
     */
	public String getOperationRecordId() {
		return operationRecordId;
	}

	public void setOperationRecordId(String operationRecordId) {
		this.operationRecordId = operationRecordId;
	}
    /**
     * @return 工厂ID
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 工单id
     */
	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
    /**
     * @return 容器类型id
     */
	public String getContainerTypeId() {
		return containerTypeId;
	}

	public void setContainerTypeId(String containerTypeId) {
		this.containerTypeId = containerTypeId;
	}
    /**
     * @return COS类型
     */
	public String getCosType() {
		return cosType;
	}

	public void setCosType(String cosType) {
		this.cosType = cosType;
	}
    /**
     * @return 平均波长 Avg λ（nm）
     */
	public BigDecimal getAverageWavelength() {
		return averageWavelength;
	}

	public void setAverageWavelength(BigDecimal averageWavelength) {
		this.averageWavelength = averageWavelength;
	}
    /**
     * @return 类型
     */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    /**
     * @return LOTNO
     */
	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
    /**
     * @return wafer
     */
	public String getWafer() {
		return wafer;
	}

	public void setWafer(String wafer) {
		this.wafer = wafer;
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
     * @return 作业批次
     */
	public String getJobBatch() {
		return jobBatch;
	}

	public void setJobBatch(String jobBatch) {
		this.jobBatch = jobBatch;
	}
    /**
     * @return BAR条数
     */
	public Long getBarNum() {
		return barNum;
	}

	public void setBarNum(Long barNum) {
		this.barNum = barNum;
	}
    /**
     * @return 芯片数
     */
	public Long getCosNum() {
		return cosNum;
	}

	public void setCosNum(Long cosNum) {
		this.cosNum = cosNum;
	}
    /**
     * @return 工艺id
     */
	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
    /**
     * @return wkcid
     */
	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}
    /**
     * @return 设备id
     */
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
    /**
     * @return 剩余芯片数
     */
	public Long getSurplusCosNum() {
		return surplusCosNum;
	}

	public void setSurplusCosNum(Long surplusCosNum) {
		this.surplusCosNum = surplusCosNum;
	}
    /**
     * @return 芯片物料id
     */
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
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
     * @return 批次
     */
	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
    /**
     * @return 供应商id
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
