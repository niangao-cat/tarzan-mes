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
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 员工产量汇总表
 *
 * @author penglin.sui@hand-china.com 2021-07-28 11:19:48
 */
@ApiModel("员工产量汇总表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_employee_output_summary")
@CustomPrimary
public class HmeEmployeeOutputSummary extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OUTPUT_SUMMARY_ID = "outputSummaryId";
    public static final String FIELD_JOB_TIME = "jobTime";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_REAL_NAME = "realName";
    public static final String FIELD_LOGIN_NAME = "loginName";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_PROD_LINE_CODE = "prodLineCode";
    public static final String FIELD_PROD_LINE_NAME = "prodLineName";
    public static final String FIELD_LINE_ID = "lineId";
    public static final String FIELD_LINE_CODE = "lineCode";
    public static final String FIELD_LINE_NAME = "lineName";
    public static final String FIELD_PROCESS_ID = "processId";
    public static final String FIELD_PROCESS_CODE = "processCode";
    public static final String FIELD_PROCESS_NAME = "processName";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_MATERIAL_NAME = "materialName";
    public static final String FIELD_PRODUCTION_VERSION = "productionVersion";
    public static final String FIELD_ACTUAL_OUTPUT_QTY = "actualOutputQty";
    public static final String FIELD_OUTPUT_QTY = "outputQty";
    public static final String FIELD_NC_QTY = "ncQty";
    public static final String FIELD_REWORK_QTY = "reworkQty";
    public static final String FIELD_TOTAL_DURATION = "totalDuration";
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
    private String outputSummaryId;
    @ApiModelProperty(value = "时间",required = true)
    @NotNull
    private Date jobTime;
    @ApiModelProperty(value = "用户ID",required = true)
    @NotNull
    private Long userId;
    @ApiModelProperty(value = "姓名",required = true)
    @NotBlank
    private String realName;
    @ApiModelProperty(value = "工号",required = true)
    @NotBlank
    private String loginName;
    @ApiModelProperty(value = "产线ID",required = true)
    @NotBlank
    private String prodLineId;
    @ApiModelProperty(value = "产线编码",required = true)
    @NotBlank
    private String prodLineCode;
    @ApiModelProperty(value = "产线名称",required = true)
    @NotBlank
    private String prodLineName;
    @ApiModelProperty(value = "工段ID",required = true)
    @NotBlank
    private String lineId;
    @ApiModelProperty(value = "工段编码",required = true)
    @NotBlank
    private String lineCode;
    @ApiModelProperty(value = "工段描述",required = true)
    @NotBlank
    private String lineName;
    @ApiModelProperty(value = "工序ID",required = true)
    @NotBlank
    private String processId;
    @ApiModelProperty(value = "工序编码",required = true)
    @NotBlank
    private String processCode;
    @ApiModelProperty(value = "工序描述",required = true)
    @NotBlank
    private String processName;
    @ApiModelProperty(value = "物料ID",required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "物料编码",required = true)
    @NotBlank
    private String materialCode;
    @ApiModelProperty(value = "物料描述",required = true)
    @NotBlank
    private String materialName;
    @ApiModelProperty(value = "生产版本",required = true)
    @NotBlank
    private String productionVersion;
    @ApiModelProperty(value = "实际产出",required = true)
    @NotNull
    private BigDecimal actualOutputQty;
    @ApiModelProperty(value = "产量",required = true)
    @NotNull
    private BigDecimal outputQty;
    @ApiModelProperty(value = "不良数",required = true)
    @NotNull
    private BigDecimal ncQty;
    @ApiModelProperty(value = "返修数",required = true)
    @NotNull
    private BigDecimal reworkQty;
    @ApiModelProperty(value = "总时长",required = true)
    @NotNull
    private BigDecimal totalDuration;
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
	public String getOutputSummaryId() {
		return outputSummaryId;
	}

	public void setOutputSummaryId(String outputSummaryId) {
		this.outputSummaryId = outputSummaryId;
	}
    /**
     * @return 时间
     */
	public Date getJobTime() {
		return jobTime;
	}

	public void setJobTime(Date jobTime) {
		this.jobTime = jobTime;
	}
    /**
     * @return 用户ID
     */
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
    /**
     * @return 姓名
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
     * @return 产线ID
     */
	public String getProdLineId() {
		return prodLineId;
	}

	public void setProdLineId(String prodLineId) {
		this.prodLineId = prodLineId;
	}
    /**
     * @return 产线编码
     */
	public String getProdLineCode() {
		return prodLineCode;
	}

	public void setProdLineCode(String prodLineCode) {
		this.prodLineCode = prodLineCode;
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
     * @return 工段ID
     */
	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
    /**
     * @return 工段编码
     */
	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}
    /**
     * @return 工段描述
     */
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
    /**
     * @return 工序ID
     */
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
    /**
     * @return 工序编码
     */
	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
    /**
     * @return 工序描述
     */
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
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
     * @return 物料编码
     */
	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
    /**
     * @return 物料描述
     */
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
    /**
     * @return 生产版本
     */
	public String getProductionVersion() {
		return productionVersion;
	}

	public void setProductionVersion(String productionVersion) {
		this.productionVersion = productionVersion;
	}
    /**
     * @return 实际产出
     */
	public BigDecimal getActualOutputQty() {
		return actualOutputQty;
	}

	public void setActualOutputQty(BigDecimal actualOutputQty) {
		this.actualOutputQty = actualOutputQty;
	}
    /**
     * @return 产量
     */
	public BigDecimal getOutputQty() {
		return outputQty;
	}

	public void setOutputQty(BigDecimal outputQty) {
		this.outputQty = outputQty;
	}
    /**
     * @return 不良数
     */
	public BigDecimal getNcQty() {
		return ncQty;
	}

	public void setNcQty(BigDecimal ncQty) {
		this.ncQty = ncQty;
	}
    /**
     * @return 返修数
     */
	public BigDecimal getReworkQty() {
		return reworkQty;
	}

	public void setReworkQty(BigDecimal reworkQty) {
		this.reworkQty = reworkQty;
	}
    /**
     * @return 总时长
     */
	public BigDecimal getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(BigDecimal totalDuration) {
		this.totalDuration = totalDuration;
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
