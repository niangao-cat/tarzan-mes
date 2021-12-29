package com.ruike.qms.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.common.query.Where;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 抽样类型管理
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:44
 */
@ApiModel("抽样类型管理")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_sample_type")
@CustomPrimary
public class QmsSampleType extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SAMPLE_TYPE_ID = "sampleTypeId";
    public static final String FIELD_SAMPLE_TYPE_CODE = "sampleTypeCode";
    public static final String FIELD_SAMPLE_TYPE_DESC = "sampleTypeDesc";
    public static final String FIELD_SAMPLE_TYPE = "sampleType";
    public static final String FIELD_PARAMETERS = "parameters";
    public static final String FIELD_SAMPLE_STANDARD = "sampleStandard";
    public static final String FIELD_ACCEPTANCE_QUANTITY_LIMIT = "acceptanceQuantityLimit";
    public static final String FIELD_INSPECTION_LEVELS = "inspectionLevels";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


   @ApiModelProperty(value = "租户id")    
    private Long tenantId;
    @ApiModelProperty("主键id，标识唯一一条记录")
    @Id
    private String sampleTypeId;
    @ApiModelProperty(value = "抽样方式编码",required = true)
    @NotBlank
	@Where
    private String sampleTypeCode;
    @ApiModelProperty(value = "抽样方式描述",required = true)
    @NotBlank
    private String sampleTypeDesc;
    @ApiModelProperty(value = "抽样类型",required = true)
    @NotBlank
	@Where
	@LovValue(value = "QMS.IQC_SAMPLE_TYPE", meaningField = "sampleTypeMeaning")
    private String sampleType;
    @ApiModelProperty(value = "参数值",required = true)
    @NotNull
    private Long parameters;
    @ApiModelProperty(value = "抽样标准",required = true)
    @NotBlank
	@LovValue(value = "QMS.IQC_SAMPLE_STANDARD_TYPE", meaningField = "sampleStandardMeaning")
    private String sampleStandard;
    @ApiModelProperty(value = "aql值",required = true)
	@LovValue(value = "QMS.IQC_AQL", meaningField = "acceptanceQuantityLimitMeaning")
	private String acceptanceQuantityLimit;
    @ApiModelProperty(value = "检验水平",required = true)
	@LovValue(value = "QMS.IQC_INSPECTION_LEVELS", meaningField = "inspectionLevelsMeaning")
    private String inspectionLevels;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
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
     * @return 主键id，标识唯一一条记录
     */
	public String getSampleTypeId() {
		return sampleTypeId;
	}

	public void setSampleTypeId(String sampleTypeId) {
		this.sampleTypeId = sampleTypeId;
	}
    /**
     * @return 抽样方式编码
     */
	public String getSampleTypeCode() {
		return sampleTypeCode;
	}

	public void setSampleTypeCode(String sampleTypeCode) {
		this.sampleTypeCode = sampleTypeCode;
	}
    /**
     * @return 抽样方式描述
     */
	public String getSampleTypeDesc() {
		return sampleTypeDesc;
	}

	public void setSampleTypeDesc(String sampleTypeDesc) {
		this.sampleTypeDesc = sampleTypeDesc;
	}
    /**
     * @return 抽样类型
     */
	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
    /**
     * @return 参数值
     */
	public Long getParameters() {
		return parameters;
	}

	public void setParameters(Long parameters) {
		this.parameters = parameters;
	}
    /**
     * @return 抽样标准
     */
	public String getSampleStandard() {
		return sampleStandard;
	}

	public void setSampleStandard(String sampleStandard) {
		this.sampleStandard = sampleStandard;
	}

	public String getAcceptanceQuantityLimit() {
		return acceptanceQuantityLimit;
	}

	public void setAcceptanceQuantityLimit(String acceptanceQuantityLimit) {
		this.acceptanceQuantityLimit = acceptanceQuantityLimit;
	}

	/**
     * @return 检验水平
     */
	public String getInspectionLevels() {
		return inspectionLevels;
	}

	public void setInspectionLevels(String inspectionLevels) {
		this.inspectionLevels = inspectionLevels;
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

}
