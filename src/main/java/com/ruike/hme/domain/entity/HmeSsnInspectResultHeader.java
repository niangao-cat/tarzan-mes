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
 * 标准件检验结果头
 *
 * @author sanfeng.zhang@hand-china 2021-02-04 14:51:27
 */
@ApiModel("标准件检验结果头")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_ssn_inspect_result_header")
public class HmeSsnInspectResultHeader extends AuditDomain {

    public static final String FIELD_SSN_INSPECT_RESULT_HEADER_ID = "ssnInspectResultHeaderId";
    public static final String FIELD_STANDARD_SN_CODE = "standardSnCode";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_WORK_WAY = "workWay";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_RESULT = "result";
    public static final String FIELD_SHIFT_DATE = "shiftDate";
    public static final String FIELD_SHIFT_CODE = "shiftCode";
    public static final String FIELD_TENANT_ID = "tenantId";
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


    @ApiModelProperty("主键")
	@Id
    private String ssnInspectResultHeaderId;
    @ApiModelProperty(value = "标准件编码",required = true)
    @NotBlank
    private String standardSnCode;
   	@ApiModelProperty(value = "物料id")
    private String materialId;
   	@ApiModelProperty(value = "芯片类型")
    private String cosType;
   	@ApiModelProperty(value = "工作方式")
    private String workWay;
   	@ApiModelProperty(value = "工位id")
    private String workcellId;
   	@ApiModelProperty(value = "检验结果")
    private String result;
   	@ApiModelProperty(value = "日期")
    private Date shiftDate;
   	@ApiModelProperty(value = "班次")
    private String shiftCode;
    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
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
     * @return 主键
     */
	public String getSsnInspectResultHeaderId() {
		return ssnInspectResultHeaderId;
	}

	public void setSsnInspectResultHeaderId(String ssnInspectResultHeaderId) {
		this.ssnInspectResultHeaderId = ssnInspectResultHeaderId;
	}
    /**
     * @return 标准件编码
     */
	public String getStandardSnCode() {
		return standardSnCode;
	}

	public void setStandardSnCode(String standardSnCode) {
		this.standardSnCode = standardSnCode;
	}
    /**
     * @return 物料id
     */
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
    /**
     * @return 芯片类型
     */
	public String getCosType() {
		return cosType;
	}

	public void setCosType(String cosType) {
		this.cosType = cosType;
	}
    /**
     * @return 工作方式
     */
	public String getWorkWay() {
		return workWay;
	}

	public void setWorkWay(String workWay) {
		this.workWay = workWay;
	}
    /**
     * @return 工位id
     */
	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}
    /**
     * @return 检验结果
     */
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
    /**
     * @return 日期
     */
	public Date getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(Date shiftDate) {
		this.shiftDate = shiftDate;
	}
    /**
     * @return 班次
     */
	public String getShiftCode() {
		return shiftCode;
	}

	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
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
