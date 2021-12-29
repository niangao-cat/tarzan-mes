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

/**
 * SN进出站设备状态记录表
 *
 * @author chaonan.hu@hand-china.com 2020-06-28 16:52:11
 */
@ApiModel("SN进出站设备状态记录表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_eo_job_equipment")
public class HmeEoJobEquipment extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_JOB_EQUIPMENT_ID = "jobEquipmentId";
    public static final String FIELD_JOB_ID = "jobId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_EQUIPMENT_ID = "equipmentId";
    public static final String FIELD_EQUIPMENT_STATUS = "equipmentStatus";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户Id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    private String jobEquipmentId;
    @ApiModelProperty(value = "作业ID",required = true)
    @NotBlank
    private String jobId;
    @ApiModelProperty(value = "",required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "",required = true)
    @NotBlank
    private String equipmentId;
    @ApiModelProperty(value = "",required = true)
    @NotBlank
    private String equipmentStatus;
    @ApiModelProperty(value = "CID",required = true)
	@Cid
    private Long cid;
   @ApiModelProperty(value = "预留字段")    
    private String attribute1;
   @ApiModelProperty(value = "预留字段")    
    private String attribute2;
   @ApiModelProperty(value = "预留字段")    
    private String attribute3;
   @ApiModelProperty(value = "预留字段")    
    private String attribute4;
   @ApiModelProperty(value = "预留字段")    
    private String attribute5;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户Id
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
	public String getJobEquipmentId() {
		return jobEquipmentId;
	}

	public void setJobEquipmentId(String jobEquipmentId) {
		this.jobEquipmentId = jobEquipmentId;
	}
    /**
     * @return 
     */
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
    /**
     * @return 
     */
	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}
    /**
     * @return 
     */
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
    /**
     * @return 
     */
	public String getEquipmentStatus() {
		return equipmentStatus;
	}

	public void setEquipmentStatus(String equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
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
     * @return 预留字段
     */
	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
    /**
     * @return 预留字段
     */
	public String getAttribute5() {
		return attribute5;
	}

	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}

}
