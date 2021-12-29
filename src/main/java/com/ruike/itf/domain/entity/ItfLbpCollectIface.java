package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * lbp数据采集接口
 *
 * @author wenzhang.yu@hand-china.com 2020-09-04 16:35:53
 */
@ApiModel("lbp数据采集接口")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_lbp_collect_iface")
@CustomPrimary
public class ItfLbpCollectIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_SN = "sn";
	public static final String FIELD_ASSET_ENCODING = "assetEncoding";
	public static final String FIELD_M2 = "m2";
    public static final String FIELD_M2_Y = "m2Y";
    public static final String FIELD_BPP = "bpp";
    public static final String FIELD_BPP_Y = "bppY";
    public static final String FIELD_DIVERGENCE = "divergence";
    public static final String FIELD_DIVERGENCE_Y = "divergenceY";
    public static final String FIELD_WAIST_WIDTH = "waistWidth";
    public static final String FIELD_WAIST_WIDTH_Y = "waistWidthY";
    public static final String FIELD_WAIST_LOCATION = "waistLocation";
    public static final String FIELD_WAIST_LOCATION_Y = "waistLocationY";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
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


    @ApiModelProperty("接口表ID，主键")
    @Id
    private String interfaceId;
	@ApiModelProperty(value = "SN")
	private String sn;
   @ApiModelProperty(value = "设备编码")
    private String assetEncoding;
   @ApiModelProperty(value = "")    
    private String m2;
   @ApiModelProperty(value = "")    
    private String m2Y;
   @ApiModelProperty(value = "")    
    private String bpp;
   @ApiModelProperty(value = "")    
    private String bppY;
   @ApiModelProperty(value = "")    
    private String divergence;
   @ApiModelProperty(value = "")    
    private String divergenceY;
   @ApiModelProperty(value = "")    
    private String waistWidth;
   @ApiModelProperty(value = "")    
    private String waistWidthY;
   @ApiModelProperty(value = "")    
    private String waistLocation;
   @ApiModelProperty(value = "")    
    private String waistLocationY;
    @ApiModelProperty(value = "处理时间",required = true)
    @NotNull
    private Date processDate;
   @ApiModelProperty(value = "处理消息")    
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)",required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "租户id",required = true)
    @NotNull
    private Long tenantId;
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
     * @return 接口表ID，主键
     */
	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
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
     * @return 
     */
	public String getM2() {
		return m2;
	}

	public void setM2(String m2) {
		this.m2 = m2;
	}
    /**
     * @return 
     */
	public String getM2Y() {
		return m2Y;
	}

	public void setM2Y(String m2Y) {
		this.m2Y = m2Y;
	}
    /**
     * @return 
     */
	public String getBpp() {
		return bpp;
	}

	public void setBpp(String bpp) {
		this.bpp = bpp;
	}
    /**
     * @return 
     */
	public String getBppY() {
		return bppY;
	}

	public void setBppY(String bppY) {
		this.bppY = bppY;
	}
    /**
     * @return 
     */
	public String getDivergence() {
		return divergence;
	}

	public void setDivergence(String divergence) {
		this.divergence = divergence;
	}
    /**
     * @return 
     */
	public String getDivergenceY() {
		return divergenceY;
	}

	public void setDivergenceY(String divergenceY) {
		this.divergenceY = divergenceY;
	}
    /**
     * @return 
     */
	public String getWaistWidth() {
		return waistWidth;
	}

	public void setWaistWidth(String waistWidth) {
		this.waistWidth = waistWidth;
	}
    /**
     * @return 
     */
	public String getWaistWidthY() {
		return waistWidthY;
	}

	public void setWaistWidthY(String waistWidthY) {
		this.waistWidthY = waistWidthY;
	}
    /**
     * @return 
     */
	public String getWaistLocation() {
		return waistLocation;
	}

	public void setWaistLocation(String waistLocation) {
		this.waistLocation = waistLocation;
	}
    /**
     * @return 
     */
	public String getWaistLocationY() {
		return waistLocationY;
	}

	public void setWaistLocationY(String waistLocationY) {
		this.waistLocationY = waistLocationY;
	}
    /**
     * @return 处理时间
     */
	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}
    /**
     * @return 处理消息
     */
	public String getProcessMessage() {
		return processMessage;
	}

	public void setProcessMessage(String processMessage) {
		this.processMessage = processMessage;
	}
    /**
     * @return 处理状态(N/P/E/S:正常/处理中/错误/成功)
     */
	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}
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

	public String getAssetEncoding() {
		return assetEncoding;
	}

	public void setAssetEncoding(String assetEncoding) {
		this.assetEncoding = assetEncoding;
	}
}
