package com.ruike.qms.domain.entity;

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
 * 检验水平转移规则表
 *
 * @author tong.li05@hand-china.com 2020-05-11 09:54:52
 */
@ApiModel("检验水平转移规则表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_transition_rule")
public class QmsTransitionRule extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TRANSITION_RULE_ID = "transitionRuleId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_TIGHTENED_BATCHES = "tightenedBatches";
    public static final String FIELD_NG_BATCHES = "ngBatches";
    public static final String FIELD_RELAXATION_BATCHES = "relaxationBatches";
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


    @ApiModelProperty(value = "租户id", required = true)
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    private String transitionRuleId;
	@ApiModelProperty(value = "组织", required = true)
	@NotBlank(message = "组织不能为空")
    private String siteId;
	@ApiModelProperty(value = "物料ID")
    private String materialId;
	@ApiModelProperty(value = "加严连续批", required = true)
	@NotNull(message = "加严连续批不能为空")
    private Long tightenedBatches;
	@ApiModelProperty(value = "加严不合格限", required = true)
	@NotNull(message = "加严不合格限不能为空")
    private Long ngBatches;
	@ApiModelProperty(value = "放宽连续批", required = true)
	@NotNull(message = "放宽连续批不能为空")
    private Long relaxationBatches;
	@ApiModelProperty(value = "有效性", required = true)
	@NotBlank(message = "有效性不能为空")
    private String enableFlag;
	@ApiModelProperty(value = "cid")
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
	 * @return 主键ID，标识唯一一条记录
	 */
	public String getTransitionRuleId() {
		return transitionRuleId;
	}

	public void setTransitionRuleId(String transitionRuleId) {
		this.transitionRuleId = transitionRuleId;
	}

	/**
	 * @return 组织
	 */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
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
	 * @return 加严连续批
	 */
	public Long getTightenedBatches() {
		return tightenedBatches;
	}

	public void setTightenedBatches(Long tightenedBatches) {
		this.tightenedBatches = tightenedBatches;
	}

	/**
	 * @return 加严不合格限
	 */
	public Long getNgBatches() {
		return ngBatches;
	}

	public void setNgBatches(Long ngBatches) {
		this.ngBatches = ngBatches;
	}

	/**
	 * @return 放宽连续批
	 */
	public Long getRelaxationBatches() {
		return relaxationBatches;
	}

	public void setRelaxationBatches(Long relaxationBatches) {
		this.relaxationBatches = relaxationBatches;
	}

	/**
	 * @return 有效性
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
