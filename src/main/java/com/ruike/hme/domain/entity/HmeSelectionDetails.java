package com.ruike.hme.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 预挑选明细表
 *
 * @author wenzhang.yu@hand-china.com 2020-08-19 15:44:45
 */
@ApiModel("预挑选明细表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_selection_details")
@CustomPrimary
public class HmeSelectionDetails extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SELECTION_DETAILS_ID = "selectionDetailsId";
    public static final String FIELD_PRE_SELECTION_ID = "preSelectionId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_VIRTUAL_NUM = "virtual Num";
    public static final String FIELD_NEW_MATERIAL_LOT_ID = "newMaterialLotId";
    public static final String FIELD_NEW_LOAD = "newLoad";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_OLD_MATERIAL_LOT_ID = "oldMaterialLotId";
    public static final String FIELD_OLD_LOAD = "oldLoad";
    public static final String FIELD_LOAD_SEQUENCE = "loadSequence";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_POWER = "power";
    public static final String FIELD_RULE1 = "rule1";
    public static final String FIELD_RULE2 = "rule2";
    public static final String FIELD_RULE3 = "rule3";
    public static final String FIELD_RULE4 = "rule4";
    public static final String FIELD_RULE5 = "rule5";
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
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    private String selectionDetailsId;
    @ApiModelProperty(value = "预挑选基础表Id",required = true)
    @NotBlank
    private String preSelectionId;
    @ApiModelProperty(value = "工厂ID",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "虚拟号",required = true)
    @NotBlank
    private String virtualNum;
   @ApiModelProperty(value = "新盒子号")    
    private String newMaterialLotId;
   @ApiModelProperty(value = "新盒位置")    
    private String newLoad;
    @ApiModelProperty(value = "芯片料号",required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "旧盒号",required = true)
    @NotBlank
    private String oldMaterialLotId;
    @ApiModelProperty(value = "旧盒位置",required = true)
    @NotBlank
    private String oldLoad;
    @ApiModelProperty(value = "芯片序列号",required = true)
    @NotNull
    private String loadSequence;
    @ApiModelProperty(value = "芯片类型",required = true)
    @NotBlank
    private String cosType;
   @ApiModelProperty(value = "功率")    
    private String power;
   @ApiModelProperty(value = "规则1")    
    private String rule1;
   @ApiModelProperty(value = "规则2")    
    private String rule2;
   @ApiModelProperty(value = "规则3")    
    private String rule3;
   @ApiModelProperty(value = "规则4")    
    private String rule4;
   @ApiModelProperty(value = "规则5")    
    private String rule5;
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
     * @return 主键ID，表示唯一一条记录
     */
	public String getSelectionDetailsId() {
		return selectionDetailsId;
	}

	public void setSelectionDetailsId(String selectionDetailsId) {
		this.selectionDetailsId = selectionDetailsId;
	}
    /**
     * @return 预挑选基础表Id
     */
	public String getPreSelectionId() {
		return preSelectionId;
	}

	public void setPreSelectionId(String preSelectionId) {
		this.preSelectionId = preSelectionId;
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
     * @return 虚拟号
     */
	public String getVirtualNum() {
		return virtualNum;
	}

	public void setVirtualNum(String virtualNum) {
		this.virtualNum = virtualNum;
	}
    /**
     * @return 新盒子号
     */
	public String getNewMaterialLotId() {
		return newMaterialLotId;
	}

	public void setNewMaterialLotId(String newMaterialLotId) {
		this.newMaterialLotId = newMaterialLotId;
	}
    /**
     * @return 新盒位置
     */
	public String getNewLoad() {
		return newLoad;
	}

	public void setNewLoad(String newLoad) {
		this.newLoad = newLoad;
	}
    /**
     * @return 芯片料号
     */
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
    /**
     * @return 旧盒号
     */
	public String getOldMaterialLotId() {
		return oldMaterialLotId;
	}

	public void setOldMaterialLotId(String oldMaterialLotId) {
		this.oldMaterialLotId = oldMaterialLotId;
	}
    /**
     * @return 旧盒位置
     */
	public String getOldLoad() {
		return oldLoad;
	}

	public void setOldLoad(String oldLoad) {
		this.oldLoad = oldLoad;
	}
    /**
     * @return 芯片序列号
     */
	public String getLoadSequence() {
		return loadSequence;
	}

	public void setLoadSequence(String loadSequence) {
		this.loadSequence = loadSequence;
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
     * @return 功率
     */
	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}
    /**
     * @return 规则1
     */
	public String getRule1() {
		return rule1;
	}

	public void setRule1(String rule1) {
		this.rule1 = rule1;
	}
    /**
     * @return 规则2
     */
	public String getRule2() {
		return rule2;
	}

	public void setRule2(String rule2) {
		this.rule2 = rule2;
	}
    /**
     * @return 规则3
     */
	public String getRule3() {
		return rule3;
	}

	public void setRule3(String rule3) {
		this.rule3 = rule3;
	}
    /**
     * @return 规则4
     */
	public String getRule4() {
		return rule4;
	}

	public void setRule4(String rule4) {
		this.rule4 = rule4;
	}
    /**
     * @return 规则5
     */
	public String getRule5() {
		return rule5;
	}

	public void setRule5(String rule5) {
		this.rule5 = rule5;
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

	@ApiModelProperty("虚拟号的序号，用于COS预筛选时记录筛选明细数据与生成的第几个虚拟号的对应关系")
	@Transient
	private Integer virtualNumOrder;

	public Integer getVirtualNumOrder() {
		return virtualNumOrder;
	}

	public void setVirtualNumOrder(Integer virtualNumOrder) {
		this.virtualNumOrder = virtualNumOrder;
	}
}
