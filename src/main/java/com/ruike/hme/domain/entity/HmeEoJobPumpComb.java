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
 * 泵浦源组合关系表
 *
 * @author chaonan.hu@hand-china.com 2021-08-23 10:34:03
 */
@ApiModel("泵浦源组合关系表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_eo_job_pump_comb")
public class HmeEoJobPumpComb extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_JOB_PUMP_COMB_ID = "jobPumpCombId";
    public static final String FIELD_JOB_ID = "jobId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_COMB_MATERIAL_LOT_ID = "combMaterialLotId";
    public static final String FIELD_COMB_MATERIAL_ID = "combMaterialId";
    public static final String FIELD_PUMP_REQ_QTY = "pumpReqQty";
    public static final String FIELD_SUB_BARCODE = "subBarcode";
    public static final String FIELD_SUB_BARCODE_SEQ = "subBarcodeSeq";
    public static final String FIELD_PRINT_TIME = "printTime";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
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


    @ApiModelProperty("租户id")
	@NotNull
    private Long tenantId;
    @ApiModelProperty(value = "主键",required = true)
	@Id
    private String jobPumpCombId;
    @ApiModelProperty(value = "作业ID",required = true)
    @NotBlank
    private String jobId;
    @ApiModelProperty(value = "工位ID",required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "EO_ID")
    private String eoId;
    @ApiModelProperty(value = "组合物料批ID",required = true)
    @NotBlank
    private String combMaterialLotId;
    @ApiModelProperty(value = "组合物料ID")
    private String combMaterialId;
    @ApiModelProperty(value = "泵浦源需求数",required = true)
    @NotNull
    private Long pumpReqQty;
    @ApiModelProperty(value = "子条码",required = true)
    @NotBlank
    private String subBarcode;
    @ApiModelProperty(value = "子条码顺序",required = true)
    @NotNull
    private Long subBarcodeSeq;
    @ApiModelProperty(value = "打印时间")
    private Date printTime;
    @ApiModelProperty(value = "泵浦源物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "泵浦源物料ID")
    private String materialId;
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
	public String getJobPumpCombId() {
		return jobPumpCombId;
	}

	public void setJobPumpCombId(String jobPumpCombId) {
		this.jobPumpCombId = jobPumpCombId;
	}
    /**
     * @return 作业ID
     */
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
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
     * @return EO_ID
     */
	public String getEoId() {
		return eoId;
	}

	public void setEoId(String eoId) {
		this.eoId = eoId;
	}
    /**
     * @return 组合物料批ID
     */
	public String getCombMaterialLotId() {
		return combMaterialLotId;
	}

	public void setCombMaterialLotId(String combMaterialLotId) {
		this.combMaterialLotId = combMaterialLotId;
	}
    /**
     * @return 组合物料ID
     */
	public String getCombMaterialId() {
		return combMaterialId;
	}

	public void setCombMaterialId(String combMaterialId) {
		this.combMaterialId = combMaterialId;
	}
    /**
     * @return 泵浦源需求数
     */
	public Long getPumpReqQty() {
		return pumpReqQty;
	}

	public void setPumpReqQty(Long pumpReqQty) {
		this.pumpReqQty = pumpReqQty;
	}
    /**
     * @return 子条码
     */
	public String getSubBarcode() {
		return subBarcode;
	}

	public void setSubBarcode(String subBarcode) {
		this.subBarcode = subBarcode;
	}
    /**
     * @return 子条码顺序
     */
	public Long getSubBarcodeSeq() {
		return subBarcodeSeq;
	}

	public void setSubBarcodeSeq(Long subBarcodeSeq) {
		this.subBarcodeSeq = subBarcodeSeq;
	}
    /**
     * @return 打印时间
     */
	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
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
