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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 老化台数据采集接口表
 *
 * @author wenzhang.yu@hand-china.com 2020-08-25 19:15:00
 */
@ApiModel("老化台数据采集接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_ap_collect_iface")
@CustomPrimary
public class ItfApCollectIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_AP_DURATION = "apDuration";
    public static final String FIELD_AP_CHANNER = "apChanner";
    public static final String FIELD_AP_CURRENT = "apCurrent";
    public static final String FIELD_AP_POWER = "apPower";
    public static final String FIELD_AP_BASE_TEMP = "apBaseTemp";
    public static final String FIELD_AP_LASER_TEMP = "apLaserTemp";
    public static final String FIELD_AP_NOZZLE_TEMP = "apNozzleTemp";
    public static final String FIELD_AP_WATER_COOL_TEMP = "apWaterCoolTemp";
    public static final String FIELD_PRIMARY_KEY = "primaryKey";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_AP_ATTRIBUTE1 = "apAttribute1";
    public static final String FIELD_AP_ATTRIBUTE2 = "apAttribute2";
    public static final String FIELD_AP_ATTRIBUTE3 = "apAttribute3";
    public static final String FIELD_AP_ATTRIBUTE4 = "apAttribute4";
    public static final String FIELD_AP_ATTRIBUTE5 = "apAttribute5";
    public static final String FIELD_AP_ATTRIBUTE6 = "apAttribute6";
    public static final String FIELD_AP_ATTRIBUTE7 = "apAttribute7";
    public static final String FIELD_AP_ATTRIBUTE8 = "apAttribute8";
    public static final String FIELD_AP_ATTRIBUTE9 = "apAttribute9";
    public static final String FIELD_AP_ATTRIBUTE10 = "apAttribute10";
    public static final String FIELD_AP_ATTRIBUTE11 = "apAttribute11";
    public static final String FIELD_AP_ATTRIBUTE12 = "apAttribute12";
    public static final String FIELD_AP_ATTRIBUTE13 = "apAttribute13";
    public static final String FIELD_AP_ATTRIBUTE14 = "apAttribute14";
    public static final String FIELD_AP_ATTRIBUTE15 = "apAttribute15";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("接口表ID，主键")
    @Id
    private String interfaceId;
   @ApiModelProperty(value = "设备资产编码")    
    private String assetEncoding;
   @ApiModelProperty(value = "SN")    
    private String sn;
   @ApiModelProperty(value = "老化时长(以小时为单位)")    
    private BigDecimal apDuration;
   @ApiModelProperty(value = "通道号")    
    private String apChanner;
   @ApiModelProperty(value = "电流")    
    private BigDecimal apCurrent;
   @ApiModelProperty(value = "功率")    
    private BigDecimal apPower;
   @ApiModelProperty(value = "底座温度")    
    private BigDecimal apBaseTemp;
   @ApiModelProperty(value = "光纤温度")    
    private BigDecimal apLaserTemp;
   @ApiModelProperty(value = "管嘴温度")    
    private BigDecimal apNozzleTemp;
   @ApiModelProperty(value = "水冷板温度")    
    private BigDecimal apWaterCoolTemp;
   @ApiModelProperty(value = "外围系统唯一标识")    
    private String primaryKey;
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
    private String apAttribute1;
   @ApiModelProperty(value = "")    
    private String apAttribute2;
   @ApiModelProperty(value = "")    
    private String apAttribute3;
   @ApiModelProperty(value = "")    
    private String apAttribute4;
   @ApiModelProperty(value = "")    
    private String apAttribute5;
   @ApiModelProperty(value = "")    
    private String apAttribute6;
   @ApiModelProperty(value = "")    
    private String apAttribute7;
   @ApiModelProperty(value = "")    
    private String apAttribute8;
   @ApiModelProperty(value = "")    
    private String apAttribute9;
   @ApiModelProperty(value = "")    
    private String apAttribute10;
   @ApiModelProperty(value = "")    
    private String apAttribute11;
   @ApiModelProperty(value = "")    
    private String apAttribute12;
   @ApiModelProperty(value = "")    
    private String apAttribute13;
   @ApiModelProperty(value = "")    
    private String apAttribute14;
   @ApiModelProperty(value = "")    
    private String apAttribute15;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------
	@Transient
	@ApiModelProperty(value = "设备类")
	private String equipmentCategory;
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
     * @return 设备资产编码
     */
	public String getAssetEncoding() {
		return assetEncoding;
	}

	public void setAssetEncoding(String assetEncoding) {
		this.assetEncoding = assetEncoding;
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
     * @return 老化时长(以小时为单位)
     */
	public BigDecimal getApDuration() {
		return apDuration;
	}

	public void setApDuration(BigDecimal apDuration) {
		this.apDuration = apDuration;
	}
    /**
     * @return 通道号
     */
	public String getApChanner() {
		return apChanner;
	}

	public void setApChanner(String apChanner) {
		this.apChanner = apChanner;
	}
    /**
     * @return 电流
     */
	public BigDecimal getApCurrent() {
		return apCurrent;
	}

	public void setApCurrent(BigDecimal apCurrent) {
		this.apCurrent = apCurrent;
	}
    /**
     * @return 功率
     */
	public BigDecimal getApPower() {
		return apPower;
	}

	public void setApPower(BigDecimal apPower) {
		this.apPower = apPower;
	}
    /**
     * @return 底座温度
     */
	public BigDecimal getApBaseTemp() {
		return apBaseTemp;
	}

	public void setApBaseTemp(BigDecimal apBaseTemp) {
		this.apBaseTemp = apBaseTemp;
	}
    /**
     * @return 光纤温度
     */
	public BigDecimal getApLaserTemp() {
		return apLaserTemp;
	}

	public void setApLaserTemp(BigDecimal apLaserTemp) {
		this.apLaserTemp = apLaserTemp;
	}
    /**
     * @return 管嘴温度
     */
	public BigDecimal getApNozzleTemp() {
		return apNozzleTemp;
	}

	public void setApNozzleTemp(BigDecimal apNozzleTemp) {
		this.apNozzleTemp = apNozzleTemp;
	}
    /**
     * @return 水冷板温度
     */
	public BigDecimal getApWaterCoolTemp() {
		return apWaterCoolTemp;
	}

	public void setApWaterCoolTemp(BigDecimal apWaterCoolTemp) {
		this.apWaterCoolTemp = apWaterCoolTemp;
	}
    /**
     * @return 外围系统唯一标识
     */
	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
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
	public String getApAttribute1() {
		return apAttribute1;
	}

	public void setApAttribute1(String apAttribute1) {
		this.apAttribute1 = apAttribute1;
	}
    /**
     * @return 
     */
	public String getApAttribute2() {
		return apAttribute2;
	}

	public void setApAttribute2(String apAttribute2) {
		this.apAttribute2 = apAttribute2;
	}
    /**
     * @return 
     */
	public String getApAttribute3() {
		return apAttribute3;
	}

	public void setApAttribute3(String apAttribute3) {
		this.apAttribute3 = apAttribute3;
	}
    /**
     * @return 
     */
	public String getApAttribute4() {
		return apAttribute4;
	}

	public void setApAttribute4(String apAttribute4) {
		this.apAttribute4 = apAttribute4;
	}
    /**
     * @return 
     */
	public String getApAttribute5() {
		return apAttribute5;
	}

	public void setApAttribute5(String apAttribute5) {
		this.apAttribute5 = apAttribute5;
	}
    /**
     * @return 
     */
	public String getApAttribute6() {
		return apAttribute6;
	}

	public void setApAttribute6(String apAttribute6) {
		this.apAttribute6 = apAttribute6;
	}
    /**
     * @return 
     */
	public String getApAttribute7() {
		return apAttribute7;
	}

	public void setApAttribute7(String apAttribute7) {
		this.apAttribute7 = apAttribute7;
	}
    /**
     * @return 
     */
	public String getApAttribute8() {
		return apAttribute8;
	}

	public void setApAttribute8(String apAttribute8) {
		this.apAttribute8 = apAttribute8;
	}
    /**
     * @return 
     */
	public String getApAttribute9() {
		return apAttribute9;
	}

	public void setApAttribute9(String apAttribute9) {
		this.apAttribute9 = apAttribute9;
	}
    /**
     * @return 
     */
	public String getApAttribute10() {
		return apAttribute10;
	}

	public void setApAttribute10(String apAttribute10) {
		this.apAttribute10 = apAttribute10;
	}
    /**
     * @return 
     */
	public String getApAttribute11() {
		return apAttribute11;
	}

	public void setApAttribute11(String apAttribute11) {
		this.apAttribute11 = apAttribute11;
	}
    /**
     * @return 
     */
	public String getApAttribute12() {
		return apAttribute12;
	}

	public void setApAttribute12(String apAttribute12) {
		this.apAttribute12 = apAttribute12;
	}
    /**
     * @return 
     */
	public String getApAttribute13() {
		return apAttribute13;
	}

	public void setApAttribute13(String apAttribute13) {
		this.apAttribute13 = apAttribute13;
	}
    /**
     * @return 
     */
	public String getApAttribute14() {
		return apAttribute14;
	}

	public void setApAttribute14(String apAttribute14) {
		this.apAttribute14 = apAttribute14;
	}
    /**
     * @return 
     */
	public String getApAttribute15() {
		return apAttribute15;
	}

	public void setApAttribute15(String apAttribute15) {
		this.apAttribute15 = apAttribute15;
	}

	public String getEquipmentCategory() {
		return equipmentCategory;
	}

	public void setEquipmentCategory(String equipmentCategory) {
		this.equipmentCategory = equipmentCategory;
	}
}
