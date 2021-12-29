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
 * 泵浦源预筛选基础表
 *
 * @author chaonan.hu@hand-china.com 2021-08-30 10:59:48
 */
@ApiModel("泵浦源预筛选基础表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_pump_pre_selection")
public class HmePumpPreSelection extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PUMP_PRE_SELECTION_ID = "pumpPreSelectionId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_SELECTION_LOT = "selectionLot";
    public static final String FIELD_SETS_NUM = "setsNum";
    public static final String FIELD_COMB_MATERIAL_ID = "combMaterialId";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_RULE_HEAD_ID = "ruleHeadId";
    public static final String FIELD_PUMP_QTY = "pumpQty";
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
    private String pumpPreSelectionId;
    @ApiModelProperty(value = "工厂ID",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "工位ID",required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "筛选批次",required = true)
    @NotBlank
    private String selectionLot;
    @ApiModelProperty(value = "套数",required = true)
    @NotNull
    private Long setsNum;
    @ApiModelProperty(value = "组合物料ID",required = true)
    @NotBlank
    private String combMaterialId;
    @ApiModelProperty(value = "BOM_ID",required = true)
    @NotBlank
    private String bomId;
    @ApiModelProperty(value = "状态",required = true)
    @NotBlank
    private String status;
    @ApiModelProperty(value = "筛选规则ID",required = true)
    @NotBlank
    private String ruleHeadId;
    @ApiModelProperty(value = "泵浦源数量",required = true)
    @NotNull
    private Long pumpQty;
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
	public String getPumpPreSelectionId() {
		return pumpPreSelectionId;
	}

	public void setPumpPreSelectionId(String pumpPreSelectionId) {
		this.pumpPreSelectionId = pumpPreSelectionId;
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
     * @return 工位ID
     */
	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}
    /**
     * @return 筛选批次
     */
	public String getSelectionLot() {
		return selectionLot;
	}

	public void setSelectionLot(String selectionLot) {
		this.selectionLot = selectionLot;
	}
    /**
     * @return 套数
     */
	public Long getSetsNum() {
		return setsNum;
	}

	public void setSetsNum(Long setsNum) {
		this.setsNum = setsNum;
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
     * @return BOM_ID
     */
	public String getBomId() {
		return bomId;
	}

	public void setBomId(String bomId) {
		this.bomId = bomId;
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
     * @return 筛选规则ID
     */
	public String getRuleHeadId() {
		return ruleHeadId;
	}

	public void setRuleHeadId(String ruleHeadId) {
		this.ruleHeadId = ruleHeadId;
	}
    /**
     * @return 泵浦源数量
     */
	public Long getPumpQty() {
		return pumpQty;
	}

	public void setPumpQty(Long pumpQty) {
		this.pumpQty = pumpQty;
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
