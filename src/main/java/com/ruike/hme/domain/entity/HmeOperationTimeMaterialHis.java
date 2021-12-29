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
import java.math.BigDecimal;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 时效要求关联物料历史表
 *
 * @author chaonan.hu@hand-china.com 2020-08-11 11:44:07
 */
@ApiModel("时效要求关联物料历史表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_operation_time_material_his")
public class HmeOperationTimeMaterialHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OPERATION_TIME_MATERIAL_HIS_ID = "operationTimeMaterialHisId";
    public static final String FIELD_OPERATION_TIME_MATERIAL_ID = "operationTimeMaterialId";
    public static final String FIELD_OPERATION_TIME_ID = "operationTimeId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_PRODUCTION_VERSION_ID = "productionVersionId";
    public static final String FIELD_STANDARD_REQD_TIME_IN_PROCESS = "standardReqdTimeInProcess";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_EVENT_ID = "eventId";
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


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
//    @GeneratedValue
    private String operationTimeMaterialHisId;
    @ApiModelProperty(value = "",required = true)
    @NotBlank
    private String operationTimeMaterialId;
    @ApiModelProperty(value = "工艺时效要求主键",required = true)
    @NotBlank
    private String operationTimeId;
    @ApiModelProperty(value = "物料ID",required = true)
    @NotBlank
    private String materialId;
   @ApiModelProperty(value = "生产版本ID")    
    private String productionVersionId;
    @ApiModelProperty(value = "时效要求时长",required = true)
    @NotNull
    private BigDecimal standardReqdTimeInProcess;
	@ApiModelProperty(value = "站点",required = true)
	@NotNull
	private String siteId;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "事件ID",required = true)
    @NotBlank
    private String eventId;
    @ApiModelProperty(value = "CID")
	@Cid
    private Long cid;
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

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

	/**
	 * @return 站点ID
	 */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

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
     * @return 主键
     */
	public String getOperationTimeMaterialHisId() {
		return operationTimeMaterialHisId;
	}

	public void setOperationTimeMaterialHisId(String operationTimeMaterialHisId) {
		this.operationTimeMaterialHisId = operationTimeMaterialHisId;
	}
    /**
     * @return 
     */
	public String getOperationTimeMaterialId() {
		return operationTimeMaterialId;
	}

	public void setOperationTimeMaterialId(String operationTimeMaterialId) {
		this.operationTimeMaterialId = operationTimeMaterialId;
	}
    /**
     * @return 工艺时效要求主键
     */
	public String getOperationTimeId() {
		return operationTimeId;
	}

	public void setOperationTimeId(String operationTimeId) {
		this.operationTimeId = operationTimeId;
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
     * @return 生产版本ID
     */
	public String getProductionVersionId() {
		return productionVersionId;
	}

	public void setProductionVersionId(String productionVersionId) {
		this.productionVersionId = productionVersionId;
	}
    /**
     * @return 时效要求时长
     */
	public BigDecimal getStandardReqdTimeInProcess() {
		return standardReqdTimeInProcess;
	}

	public void setStandardReqdTimeInProcess(BigDecimal standardReqdTimeInProcess) {
		this.standardReqdTimeInProcess = standardReqdTimeInProcess;
	}
    /**
     * @return 是否有效
     */
	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
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

}
