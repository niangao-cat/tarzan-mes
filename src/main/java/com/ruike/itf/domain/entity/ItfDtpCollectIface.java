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
 * 器件测试台数据采集接口表
 *
 * @author wenzhang.yu@hand-china.com 2020-08-25 19:15:00
 */
@ApiModel("器件测试台数据采集接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_dtp_collect_iface")
@CustomPrimary
public class ItfDtpCollectIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_DTP_CURRENT = "dtpCurrent";
    public static final String FIELD_DTP_THRESCHOLD_CURRENT = "dtpThrescholdCurrent";
    public static final String FIELD_DTP_VOLTAGE = "dtpVoltage";
    public static final String FIELD_DTP_THRESCHOLD_VOLTAGE = "dtpThrescholdVoltage";
    public static final String FIELD_DTP_CENTER_WAVELENGTH = "dtpCenterWavelength";
    public static final String FIELD_DTP_LINEWIDTH = "dtpLinewidth";
    public static final String FIELD_DTP_IPCE = "dtpIpce";
    public static final String FIELD_DTP_SE = "dtpSe";
    public static final String FIELD_DTP_WAVELENGTH_DIFFER = "dtpWavelengthDiffer";
    public static final String FIELD_DTP_LINEWIDTH_DIFFER = "dtpLinewidthDiffer";
    public static final String FIELD_DTP_CHANGE_RATE = "dtpChangeRate";
    public static final String FIELD_DTP_MODE = "dtpMode";
	public static final String FIELD_DTP_POWER = "dtpPower";
	public static final String FIELD_DTP_OPERATION_NAME = "dtpOperationName";
    public static final String FIELD_PRIMARY_KEY = "primaryKey";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_DTP_ATTRIBUTE1 = "dtpAttribute1";
    public static final String FIELD_DTP_ATTRIBUTE2 = "dtpAttribute2";
    public static final String FIELD_DTP_ATTRIBUTE3 = "dtpAttribute3";
    public static final String FIELD_DTP_ATTRIBUTE4 = "dtpAttribute4";
    public static final String FIELD_DTP_ATTRIBUTE5 = "dtpAttribute5";
    public static final String FIELD_DTP_ATTRIBUTE6 = "dtpAttribute6";
    public static final String FIELD_DTP_ATTRIBUTE7 = "dtpAttribute7";
    public static final String FIELD_DTP_ATTRIBUTE8 = "dtpAttribute8";
    public static final String FIELD_DTP_ATTRIBUTE9 = "dtpAttribute9";
    public static final String FIELD_DTP_ATTRIBUTE10 = "dtpAttribute10";
    public static final String FIELD_DTP_ATTRIBUTE11 = "dtpAttribute11";
    public static final String FIELD_DTP_ATTRIBUTE12 = "dtpAttribute12";
    public static final String FIELD_DTP_ATTRIBUTE13 = "dtpAttribute13";
    public static final String FIELD_DTP_ATTRIBUTE14 = "dtpAttribute14";
    public static final String FIELD_DTP_ATTRIBUTE15 = "dtpAttribute15";

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
   @ApiModelProperty(value = "产品SN")    
    private String sn;
   @ApiModelProperty(value = "电流")    
    private BigDecimal dtpCurrent;
   @ApiModelProperty(value = "阈值电流")    
    private BigDecimal dtpThrescholdCurrent;
   @ApiModelProperty(value = "电压")    
    private BigDecimal dtpVoltage;
   @ApiModelProperty(value = "阈值电压")    
    private BigDecimal dtpThrescholdVoltage;
   @ApiModelProperty(value = "中心波长")    
    private BigDecimal dtpCenterWavelength;
   @ApiModelProperty(value = "线宽")    
    private BigDecimal dtpLinewidth;
   @ApiModelProperty(value = "光电转换效率")    
    private BigDecimal dtpIpce;
   @ApiModelProperty(value = "SE")    
    private BigDecimal dtpSe;
   @ApiModelProperty(value = "波长差")    
    private BigDecimal dtpWavelengthDiffer;
   @ApiModelProperty(value = "线宽差")    
    private BigDecimal dtpLinewidthDiffer;
   @ApiModelProperty(value = "变化率")    
    private BigDecimal dtpChangeRate;
   @ApiModelProperty(value = "测试模式")    
    private String dtpMode;
	@ApiModelProperty(value = "功率")
	private BigDecimal dtpPower;
	@ApiModelProperty(value = "工序名")
	private String dtpOperationName;
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
    private String dtpAttribute1;
   @ApiModelProperty(value = "")    
    private String dtpAttribute2;
   @ApiModelProperty(value = "")    
    private String dtpAttribute3;
   @ApiModelProperty(value = "")    
    private String dtpAttribute4;
   @ApiModelProperty(value = "")    
    private String dtpAttribute5;
   @ApiModelProperty(value = "")    
    private String dtpAttribute6;
   @ApiModelProperty(value = "")    
    private String dtpAttribute7;
   @ApiModelProperty(value = "")    
    private String dtpAttribute8;
   @ApiModelProperty(value = "")    
    private String dtpAttribute9;
   @ApiModelProperty(value = "")    
    private String dtpAttribute10;
   @ApiModelProperty(value = "")    
    private String dtpAttribute11;
   @ApiModelProperty(value = "")    
    private String dtpAttribute12;
   @ApiModelProperty(value = "")    
    private String dtpAttribute13;
   @ApiModelProperty(value = "")    
    private String dtpAttribute14;
   @ApiModelProperty(value = "")    
    private String dtpAttribute15;

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
     * @return 产品SN
     */
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
    /**
     * @return 电流
     */
	public BigDecimal getDtpCurrent() {
		return dtpCurrent;
	}

	public void setDtpCurrent(BigDecimal dtpCurrent) {
		this.dtpCurrent = dtpCurrent;
	}
    /**
     * @return 阈值电流
     */
	public BigDecimal getDtpThrescholdCurrent() {
		return dtpThrescholdCurrent;
	}

	public void setDtpThrescholdCurrent(BigDecimal dtpThrescholdCurrent) {
		this.dtpThrescholdCurrent = dtpThrescholdCurrent;
	}
    /**
     * @return 电压
     */
	public BigDecimal getDtpVoltage() {
		return dtpVoltage;
	}

	public void setDtpVoltage(BigDecimal dtpVoltage) {
		this.dtpVoltage = dtpVoltage;
	}
    /**
     * @return 阈值电压
     */
	public BigDecimal getDtpThrescholdVoltage() {
		return dtpThrescholdVoltage;
	}

	public void setDtpThrescholdVoltage(BigDecimal dtpThrescholdVoltage) {
		this.dtpThrescholdVoltage = dtpThrescholdVoltage;
	}
    /**
     * @return 中心波长
     */
	public BigDecimal getDtpCenterWavelength() {
		return dtpCenterWavelength;
	}

	public void setDtpCenterWavelength(BigDecimal dtpCenterWavelength) {
		this.dtpCenterWavelength = dtpCenterWavelength;
	}
    /**
     * @return 线宽
     */
	public BigDecimal getDtpLinewidth() {
		return dtpLinewidth;
	}

	public void setDtpLinewidth(BigDecimal dtpLinewidth) {
		this.dtpLinewidth = dtpLinewidth;
	}
    /**
     * @return 光电转换效率
     */
	public BigDecimal getDtpIpce() {
		return dtpIpce;
	}

	public void setDtpIpce(BigDecimal dtpIpce) {
		this.dtpIpce = dtpIpce;
	}
    /**
     * @return SE
     */
	public BigDecimal getDtpSe() {
		return dtpSe;
	}

	public void setDtpSe(BigDecimal dtpSe) {
		this.dtpSe = dtpSe;
	}
    /**
     * @return 波长差
     */
	public BigDecimal getDtpWavelengthDiffer() {
		return dtpWavelengthDiffer;
	}

	public void setDtpWavelengthDiffer(BigDecimal dtpWavelengthDiffer) {
		this.dtpWavelengthDiffer = dtpWavelengthDiffer;
	}
    /**
     * @return 线宽差
     */
	public BigDecimal getDtpLinewidthDiffer() {
		return dtpLinewidthDiffer;
	}

	public void setDtpLinewidthDiffer(BigDecimal dtpLinewidthDiffer) {
		this.dtpLinewidthDiffer = dtpLinewidthDiffer;
	}
    /**
     * @return 变化率
     */
	public BigDecimal getDtpChangeRate() {
		return dtpChangeRate;
	}

	public void setDtpChangeRate(BigDecimal dtpChangeRate) {
		this.dtpChangeRate = dtpChangeRate;
	}
    /**
     * @return 测试模式
     */
	public String getDtpMode() {
		return dtpMode;
	}

	public void setDtpMode(String dtpMode) {
		this.dtpMode = dtpMode;
	}
	/**
	 * @return 功率
	 */
	public BigDecimal getDtpPower() {
		return dtpPower;
	}

	public void setDtpPower(BigDecimal dtpPower) {
		this.dtpPower = dtpPower;
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
	public String getDtpAttribute1() {
		return dtpAttribute1;
	}

	public void setDtpAttribute1(String dtpAttribute1) {
		this.dtpAttribute1 = dtpAttribute1;
	}
    /**
     * @return 
     */
	public String getDtpAttribute2() {
		return dtpAttribute2;
	}

	public void setDtpAttribute2(String dtpAttribute2) {
		this.dtpAttribute2 = dtpAttribute2;
	}
    /**
     * @return 
     */
	public String getDtpAttribute3() {
		return dtpAttribute3;
	}

	public void setDtpAttribute3(String dtpAttribute3) {
		this.dtpAttribute3 = dtpAttribute3;
	}
    /**
     * @return 
     */
	public String getDtpAttribute4() {
		return dtpAttribute4;
	}

	public void setDtpAttribute4(String dtpAttribute4) {
		this.dtpAttribute4 = dtpAttribute4;
	}
    /**
     * @return 
     */
	public String getDtpAttribute5() {
		return dtpAttribute5;
	}

	public void setDtpAttribute5(String dtpAttribute5) {
		this.dtpAttribute5 = dtpAttribute5;
	}
    /**
     * @return 
     */
	public String getDtpAttribute6() {
		return dtpAttribute6;
	}

	public void setDtpAttribute6(String dtpAttribute6) {
		this.dtpAttribute6 = dtpAttribute6;
	}
    /**
     * @return 
     */
	public String getDtpAttribute7() {
		return dtpAttribute7;
	}

	public void setDtpAttribute7(String dtpAttribute7) {
		this.dtpAttribute7 = dtpAttribute7;
	}
    /**
     * @return 
     */
	public String getDtpAttribute8() {
		return dtpAttribute8;
	}

	public void setDtpAttribute8(String dtpAttribute8) {
		this.dtpAttribute8 = dtpAttribute8;
	}
    /**
     * @return 
     */
	public String getDtpAttribute9() {
		return dtpAttribute9;
	}

	public void setDtpAttribute9(String dtpAttribute9) {
		this.dtpAttribute9 = dtpAttribute9;
	}
    /**
     * @return 
     */
	public String getDtpAttribute10() {
		return dtpAttribute10;
	}

	public void setDtpAttribute10(String dtpAttribute10) {
		this.dtpAttribute10 = dtpAttribute10;
	}
    /**
     * @return 
     */
	public String getDtpAttribute11() {
		return dtpAttribute11;
	}

	public void setDtpAttribute11(String dtpAttribute11) {
		this.dtpAttribute11 = dtpAttribute11;
	}
    /**
     * @return 
     */
	public String getDtpAttribute12() {
		return dtpAttribute12;
	}

	public void setDtpAttribute12(String dtpAttribute12) {
		this.dtpAttribute12 = dtpAttribute12;
	}
    /**
     * @return 
     */
	public String getDtpAttribute13() {
		return dtpAttribute13;
	}

	public void setDtpAttribute13(String dtpAttribute13) {
		this.dtpAttribute13 = dtpAttribute13;
	}
    /**
     * @return 
     */
	public String getDtpAttribute14() {
		return dtpAttribute14;
	}

	public void setDtpAttribute14(String dtpAttribute14) {
		this.dtpAttribute14 = dtpAttribute14;
	}
    /**
     * @return 
     */
	public String getDtpAttribute15() {
		return dtpAttribute15;
	}

	public void setDtpAttribute15(String dtpAttribute15) {
		this.dtpAttribute15 = dtpAttribute15;
	}

	public String getEquipmentCategory() {
		return equipmentCategory;
	}

	public void setEquipmentCategory(String equipmentCategory) {
		this.equipmentCategory = equipmentCategory;
	}


	public String getDtpOperationName() {
		return dtpOperationName;
	}

	public void setDtpOperationName(String dtpOperationName) {
		this.dtpOperationName = dtpOperationName;
	}
}
