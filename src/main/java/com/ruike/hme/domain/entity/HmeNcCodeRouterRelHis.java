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
 * 不良代码工艺路线关系历史表
 *
 * @author penglin.sui@hand-china.com 2021-03-30 15:50:41
 */
@ApiModel("不良代码工艺路线关系历史表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_nc_code_router_rel_his")
@CustomPrimary
public class HmeNcCodeRouterRelHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NC_CODE_ROUTER_REL_HIS_ID = "ncCodeRouterRelHisId";
    public static final String FIELD_NC_CODE_ROUTER_REL_ID = "ncCodeRouterRelId";
    public static final String FIELD_NC_GROUP_ID = "ncGroupId";
    public static final String FIELD_NC_CODE_ID = "ncCodeId";
    public static final String FIELD_PROD_LINE_ID = "prodLineId";
    public static final String FIELD_DEVICE_TYPE = "deviceType";
    public static final String FIELD_CHIP_TYPE = "chipType";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_EVENT_ID = "eventId";
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
    private String ncCodeRouterRelHisId;
    @ApiModelProperty(value = "不良代码工艺路线关系id",required = true)
    @NotBlank
    private String ncCodeRouterRelId;
    @ApiModelProperty(value = "不良代码组id",required = true)
    @NotBlank
    private String ncGroupId;
    @ApiModelProperty(value = "不良代码id",required = true)
    @NotBlank
    private String ncCodeId;
    @ApiModelProperty(value = "产线id",required = true)
    @NotBlank
    private String prodLineId;
   @ApiModelProperty(value = "器件类型")    
    private String deviceType;
   @ApiModelProperty(value = "芯片类型")    
    private String chipType;
    @ApiModelProperty(value = "工艺路线id",required = true)
    @NotBlank
    private String routerId;
    @ApiModelProperty(value = "有效性",required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "事件id",required = true)
    @NotBlank
    private String eventId;
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
	public String getNcCodeRouterRelHisId() {
		return ncCodeRouterRelHisId;
	}

	public void setNcCodeRouterRelHisId(String ncCodeRouterRelHisId) {
		this.ncCodeRouterRelHisId = ncCodeRouterRelHisId;
	}
    /**
     * @return 不良代码工艺路线关系id
     */
	public String getNcCodeRouterRelId() {
		return ncCodeRouterRelId;
	}

	public void setNcCodeRouterRelId(String ncCodeRouterRelId) {
		this.ncCodeRouterRelId = ncCodeRouterRelId;
	}
    /**
     * @return 不良代码组id
     */
	public String getNcGroupId() {
		return ncGroupId;
	}

	public void setNcGroupId(String ncGroupId) {
		this.ncGroupId = ncGroupId;
	}
    /**
     * @return 不良代码id
     */
	public String getNcCodeId() {
		return ncCodeId;
	}

	public void setNcCodeId(String ncCodeId) {
		this.ncCodeId = ncCodeId;
	}
    /**
     * @return 产线id
     */
	public String getProdLineId() {
		return prodLineId;
	}

	public void setProdLineId(String prodLineId) {
		this.prodLineId = prodLineId;
	}
    /**
     * @return 器件类型
     */
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
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
     * @return 工艺路线id
     */
	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
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
     * @return 事件id
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
