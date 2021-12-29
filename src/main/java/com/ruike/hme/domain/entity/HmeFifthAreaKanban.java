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
 * 五部看板
 *
 * @author penglin.sui@hand-china.com 2021-06-08 07:53:30
 */
@ApiModel("五部看板")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_fifth_area_kanban")
@CustomPrimary
public class HmeFifthAreaKanban extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_FIFTH_AREA_KANBAN_ID = "fifthAreaKanbanId";
	public static final String FIELD_JOB_ID = "jobId";
	public static final String FIELD_EO_ID = "eoId";
	public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_WORKCELL_NAME = "workcellName";
    public static final String FIELD_PROD_LINE_NAME = "prodLineName";
    public static final String FIELD_OPERATION_NAME = "operationName";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_CHIP_TYPE = "chipType";
    public static final String FIELD_WO_MATERIAL_CODE = "woMaterialCode";
    public static final String FIELD_SN_MATERIAL_CODE = "snMaterialCode";
    public static final String FIELD_LAB_CODE = "labCode";
    public static final String FIELD_SITE_OUT_DATE = "siteOutDate";
    public static final String FIELD_REWORK_FLAG = "reworkFlag";
    public static final String FIELD_NC_PROCESS_METHOD = "ncProcessMethod";
    public static final String FIELD_REAL_NAME = "realName";
    public static final String FIELD_LOGIN_NAME = "loginName";
    public static final String FIELD_RELEASE_SN = "releaseSn";
    public static final String FIELD_CIRCUIT_NUMBER = "circuitNumber";
    public static final String FIELD_WAFER = "wafer";
    public static final String FIELD_HOT_SUPPLIER_LOT = "hotSupplierLot";
    public static final String FIELD_VIRTUAL_NUMBER = "virtualNumber";
    public static final String FIELD_HOT_SINK_CODE = "hotSinkCode";
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
    @ApiModelProperty("主键ID")
    @Id
    private String fifthAreaKanbanId;
	@ApiModelProperty(value = "工序作业ID",required = true)
	@NotBlank
	private String jobId;
	@ApiModelProperty(value = "EOID",required = true)
	@NotBlank
	private String eoId;
	@ApiModelProperty(value = "工位ID",required = true)
	@NotBlank
	private String workcellId;
   @ApiModelProperty(value = "工位")    
    private String workcellName;
   @ApiModelProperty(value = "产线名称")    
    private String prodLineName;
   @ApiModelProperty(value = "工艺名称")    
    private String operationName;
    @ApiModelProperty(value = "SN",required = true)
    @NotBlank
    private String sn;
    @ApiModelProperty(value = "芯片类型",required = true)
    @NotBlank
    private String chipType;
   @ApiModelProperty(value = "工单物料编码")    
    private String woMaterialCode;
   @ApiModelProperty(value = "SN物料编码")    
    private String snMaterialCode;
   @ApiModelProperty(value = "实验代码")    
    private String labCode;
   @ApiModelProperty(value = "出站时间")    
    private Date siteOutDate;
   @ApiModelProperty(value = "返修标识")    
    private String reworkFlag;
   @ApiModelProperty(value = "不良处理方式")    
    private String ncProcessMethod;
   @ApiModelProperty(value = "出站人员")    
    private String realName;
   @ApiModelProperty(value = "工号")    
    private String loginName;
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
	public String getFifthAreaKanbanId() {
		return fifthAreaKanbanId;
	}

	public void setFifthAreaKanbanId(String fifthAreaKanbanId) {
		this.fifthAreaKanbanId = fifthAreaKanbanId;
	}
	/**
	 * @return 工序作业ID
	 */
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	/**
	 * @return EOID
	 */
	public String getEoId() {
		return eoId;
	}

	public void setEoId(String eoId) {
		this.eoId = eoId;
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
     * @return 工位
     */
	public String getWorkcellName() {
		return workcellName;
	}

	public void setWorkcellName(String workcellName) {
		this.workcellName = workcellName;
	}
    /**
     * @return 产线名称
     */
	public String getProdLineName() {
		return prodLineName;
	}

	public void setProdLineName(String prodLineName) {
		this.prodLineName = prodLineName;
	}
    /**
     * @return 工艺名称
     */
	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
    /**
     * @return SN
     */
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
    /**
     * @return 芯片类型
     */
	public String getChipType() {
		return chipType;
	}

	public void setChipType(String chipType) {
		this.chipType = chipType;
	}
    /**
     * @return 工单物料编码
     */
	public String getWoMaterialCode() {
		return woMaterialCode;
	}

	public void setWoMaterialCode(String woMaterialCode) {
		this.woMaterialCode = woMaterialCode;
	}
    /**
     * @return SN物料编码
     */
	public String getSnMaterialCode() {
		return snMaterialCode;
	}

	public void setSnMaterialCode(String snMaterialCode) {
		this.snMaterialCode = snMaterialCode;
	}
    /**
     * @return 实验代码
     */
	public String getLabCode() {
		return labCode;
	}

	public void setLabCode(String labCode) {
		this.labCode = labCode;
	}
    /**
     * @return 出站时间
     */
	public Date getSiteOutDate() {
		return siteOutDate;
	}

	public void setSiteOutDate(Date siteOutDate) {
		this.siteOutDate = siteOutDate;
	}
    /**
     * @return 返修标识
     */
	public String getReworkFlag() {
		return reworkFlag;
	}

	public void setReworkFlag(String reworkFlag) {
		this.reworkFlag = reworkFlag;
	}
    /**
     * @return 不良处理方式
     */
	public String getNcProcessMethod() {
		return ncProcessMethod;
	}

	public void setNcProcessMethod(String ncProcessMethod) {
		this.ncProcessMethod = ncProcessMethod;
	}
    /**
     * @return 出站人员
     */
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
    /**
     * @return 工号
     */
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
