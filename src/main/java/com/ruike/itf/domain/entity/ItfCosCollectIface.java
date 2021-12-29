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
 * cos测试数据采集接口表
 *
 * @author wenzhang.yu@hand-china.com 2020-08-28 11:18:23
 */
@ApiModel("cos测试数据采集接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_cos_collect_iface")
@CustomPrimary
public class ItfCosCollectIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_COS_LOCATION = "cosLocation";
    public static final String FIELD_COS_MODEL = "cosModel";
    public static final String FIELD_COS_THRESCHOLD_CURRENT = "cosThrescholdCurrent";
    public static final String FIELD_COS_THRESCHOLD_VOLTAGE = "cosThrescholdVoltage";
    public static final String FIELD_COS_CURRENT = "cosCurrent";
    public static final String FIELD_COS_VOLTAGE = "cosVoltage";
    public static final String FIELD_COS_POWER = "cosPower";
    public static final String FIELD_COS_CENTER_WAVELENGTH = "cosCenterWavelength";
    public static final String FIELD_COS_SE = "cosSe";
    public static final String FIELD_COS_LINEWIDTH = "cosLinewidth";
    public static final String FIELD_COS_IPCE = "cosIpce";
    public static final String FIELD_COS_WAVELENGTH_DIFFER = "cosWavelengthDiffer";
    public static final String FIELD_COS_POWER_LEVEL = "cosPowerLevel";
    public static final String FIELD_COS_WAVELENGTH_LEVEL = "cosWavelengthLevel";
    public static final String FIELD_COS_POLARIZATION = "cosPolarization";
    public static final String FIELD_COS_FWHM_X = "cosFwhmX";
    public static final String FIELD_COS_FWHM_Y = "cosFwhmY";
    public static final String FIELD_COS_86X = "cos86x";
    public static final String FIELD_COS_86Y = "cos86y";
    public static final String FIELD_COS_95X = "cos95x";
    public static final String FIELD_COS_95Y = "cos95y";
	public static final String FIELD_COS_NC_CODE = "cosNcCode";
	public static final String FIELD_COS_LENS_POWER = "cosLensPower";
	public static final String FIELD_COS_PBS_POWER = "cosPbsPower";
	public static final String FIELD_COS_OPERATOR = "cosOperator";
	public static final String FIELD_COS_REMARK = "cosRemark";
	public static final String FIELD_COS_VOLTAGE_LEVEL = "cosVoltageLevel";
	public static final String FIELD_PRIMARY_KEY = "primaryKey";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_COS_ATTRIBUTE1 = "cosAttribute1";
    public static final String FIELD_COS_ATTRIBUTE2 = "cosAttribute2";
    public static final String FIELD_COS_ATTRIBUTE3 = "cosAttribute3";
    public static final String FIELD_COS_ATTRIBUTE4 = "cosAttribute4";
    public static final String FIELD_COS_ATTRIBUTE5 = "cosAttribute5";
    public static final String FIELD_COS_ATTRIBUTE6 = "cosAttribute6";
    public static final String FIELD_COS_ATTRIBUTE7 = "cosAttribute7";
    public static final String FIELD_COS_ATTRIBUTE8 = "cosAttribute8";
    public static final String FIELD_COS_ATTRIBUTE9 = "cosAttribute9";
    public static final String FIELD_COS_ATTRIBUTE10 = "cosAttribute10";

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
   @ApiModelProperty(value = "位置")    
    private String cosLocation;
   @ApiModelProperty(value = "测试模式")    
    private String cosModel;
   @ApiModelProperty(value = "阈值电流")    
    private BigDecimal cosThrescholdCurrent;
   @ApiModelProperty(value = "阈值电压")    
    private BigDecimal cosThrescholdVoltage;
   @ApiModelProperty(value = "电流")    
    private BigDecimal cosCurrent;
   @ApiModelProperty(value = "电压")    
    private BigDecimal cosVoltage;
   @ApiModelProperty(value = "功率")    
    private BigDecimal cosPower;
   @ApiModelProperty(value = "中心波长")    
    private BigDecimal cosCenterWavelength;
   @ApiModelProperty(value = "SE")    
    private BigDecimal cosSe;
   @ApiModelProperty(value = "线宽")    
    private BigDecimal cosLinewidth;
   @ApiModelProperty(value = "光电转换效率")    
    private BigDecimal cosIpce;
   @ApiModelProperty(value = "波长差")    
    private BigDecimal cosWavelengthDiffer;
   @ApiModelProperty(value = "功率等级")    
    private String cosPowerLevel;
   @ApiModelProperty(value = "波长分级")    
    private String cosWavelengthLevel;
   @ApiModelProperty(value = "偏振度数")    
    private BigDecimal cosPolarization;
   @ApiModelProperty(value = "X半宽高")    
    private BigDecimal cosFwhmX;
   @ApiModelProperty(value = "Y半宽高")    
    private BigDecimal cosFwhmY;
   @ApiModelProperty(value = "X86能量宽度")    
    private BigDecimal cos86x;
   @ApiModelProperty(value = "Y86能量宽度")    
    private BigDecimal cos86y;
   @ApiModelProperty(value = "X95能量宽度")    
    private BigDecimal cos95x;
   @ApiModelProperty(value = "Y95能量宽度")    
    private BigDecimal cos95y;
	@ApiModelProperty(value = "错误编码")
	private String cosNcCode;
	@ApiModelProperty(value = "透镜功率")
	private BigDecimal cosLensPower;
	@ApiModelProperty(value = "PBS功率")
	private BigDecimal cosPbsPower;
	@ApiModelProperty(value = "操作者")
	private String cosOperator;
	@ApiModelProperty(value = "备注")
	private String cosRemark;
	@ApiModelProperty(value = "备注")
	private String cosVoltageLevel;
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
    private String cosAttribute1;
   @ApiModelProperty(value = "")    
    private String cosAttribute2;
   @ApiModelProperty(value = "")    
    private String cosAttribute3;
   @ApiModelProperty(value = "")    
    private String cosAttribute4;
   @ApiModelProperty(value = "")    
    private String cosAttribute5;
   @ApiModelProperty(value = "")    
    private String cosAttribute6;
   @ApiModelProperty(value = "")    
    private String cosAttribute7;
   @ApiModelProperty(value = "")    
    private String cosAttribute8;
   @ApiModelProperty(value = "")    
    private String cosAttribute9;
   @ApiModelProperty(value = "")    
    private String cosAttribute10;

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
     * @return 位置
     */
	public String getCosLocation() {
		return cosLocation;
	}

	public void setCosLocation(String cosLocation) {
		this.cosLocation = cosLocation;
	}
    /**
     * @return 测试模式
     */
	public String getCosModel() {
		return cosModel;
	}

	public void setCosModel(String cosModel) {
		this.cosModel = cosModel;
	}
    /**
     * @return 阈值电流
     */
	public BigDecimal getCosThrescholdCurrent() {
		return cosThrescholdCurrent;
	}

	public void setCosThrescholdCurrent(BigDecimal cosThrescholdCurrent) {
		this.cosThrescholdCurrent = cosThrescholdCurrent;
	}
    /**
     * @return 阈值电压
     */
	public BigDecimal getCosThrescholdVoltage() {
		return cosThrescholdVoltage;
	}

	public void setCosThrescholdVoltage(BigDecimal cosThrescholdVoltage) {
		this.cosThrescholdVoltage = cosThrescholdVoltage;
	}
    /**
     * @return 电流
     */
	public BigDecimal getCosCurrent() {
		return cosCurrent;
	}

	public void setCosCurrent(BigDecimal cosCurrent) {
		this.cosCurrent = cosCurrent;
	}
    /**
     * @return 电压
     */
	public BigDecimal getCosVoltage() {
		return cosVoltage;
	}

	public void setCosVoltage(BigDecimal cosVoltage) {
		this.cosVoltage = cosVoltage;
	}
    /**
     * @return 功率
     */
	public BigDecimal getCosPower() {
		return cosPower;
	}

	public void setCosPower(BigDecimal cosPower) {
		this.cosPower = cosPower;
	}
    /**
     * @return 中心波长
     */
	public BigDecimal getCosCenterWavelength() {
		return cosCenterWavelength;
	}

	public void setCosCenterWavelength(BigDecimal cosCenterWavelength) {
		this.cosCenterWavelength = cosCenterWavelength;
	}
    /**
     * @return SE
     */
	public BigDecimal getCosSe() {
		return cosSe;
	}

	public void setCosSe(BigDecimal cosSe) {
		this.cosSe = cosSe;
	}
    /**
     * @return 线宽
     */
	public BigDecimal getCosLinewidth() {
		return cosLinewidth;
	}

	public void setCosLinewidth(BigDecimal cosLinewidth) {
		this.cosLinewidth = cosLinewidth;
	}
    /**
     * @return 光电转换效率
     */
	public BigDecimal getCosIpce() {
		return cosIpce;
	}

	public void setCosIpce(BigDecimal cosIpce) {
		this.cosIpce = cosIpce;
	}
    /**
     * @return 波长差
     */
	public BigDecimal getCosWavelengthDiffer() {
		return cosWavelengthDiffer;
	}

	public void setCosWavelengthDiffer(BigDecimal cosWavelengthDiffer) {
		this.cosWavelengthDiffer = cosWavelengthDiffer;
	}
    /**
     * @return 功率等级
     */
	public String getCosPowerLevel() {
		return cosPowerLevel;
	}

	public void setCosPowerLevel(String cosPowerLevel) {
		this.cosPowerLevel = cosPowerLevel;
	}
    /**
     * @return 波长分级
     */
	public String getCosWavelengthLevel() {
		return cosWavelengthLevel;
	}

	public void setCosWavelengthLevel(String cosWavelengthLevel) {
		this.cosWavelengthLevel = cosWavelengthLevel;
	}
    /**
     * @return 偏振度数
     */
	public BigDecimal getCosPolarization() {
		return cosPolarization;
	}

	public void setCosPolarization(BigDecimal cosPolarization) {
		this.cosPolarization = cosPolarization;
	}
    /**
     * @return X半宽高
     */
	public BigDecimal getCosFwhmX() {
		return cosFwhmX;
	}

	public void setCosFwhmX(BigDecimal cosFwhmX) {
		this.cosFwhmX = cosFwhmX;
	}
    /**
     * @return Y半宽高
     */
	public BigDecimal getCosFwhmY() {
		return cosFwhmY;
	}

	public void setCosFwhmY(BigDecimal cosFwhmY) {
		this.cosFwhmY = cosFwhmY;
	}
    /**
     * @return X86能量宽度
     */
	public BigDecimal getCos86x() {
		return cos86x;
	}

	public void setCos86x(BigDecimal cos86x) {
		this.cos86x = cos86x;
	}
    /**
     * @return Y86能量宽度
     */
	public BigDecimal getCos86y() {
		return cos86y;
	}

	public void setCos86y(BigDecimal cos86y) {
		this.cos86y = cos86y;
	}
    /**
     * @return X95能量宽度
     */
	public BigDecimal getCos95x() {
		return cos95x;
	}

	public void setCos95x(BigDecimal cos95x) {
		this.cos95x = cos95x;
	}
    /**
     * @return Y95能量宽度
     */
	public BigDecimal getCos95y() {
		return cos95y;
	}

	public void setCos95y(BigDecimal cos95y) {
		this.cos95y = cos95y;
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
	public String getCosAttribute1() {
		return cosAttribute1;
	}

	public void setCosAttribute1(String cosAttribute1) {
		this.cosAttribute1 = cosAttribute1;
	}
    /**
     * @return 
     */
	public String getCosAttribute2() {
		return cosAttribute2;
	}

	public void setCosAttribute2(String cosAttribute2) {
		this.cosAttribute2 = cosAttribute2;
	}
    /**
     * @return 
     */
	public String getCosAttribute3() {
		return cosAttribute3;
	}

	public void setCosAttribute3(String cosAttribute3) {
		this.cosAttribute3 = cosAttribute3;
	}
    /**
     * @return 
     */
	public String getCosAttribute4() {
		return cosAttribute4;
	}

	public void setCosAttribute4(String cosAttribute4) {
		this.cosAttribute4 = cosAttribute4;
	}
    /**
     * @return 
     */
	public String getCosAttribute5() {
		return cosAttribute5;
	}

	public void setCosAttribute5(String cosAttribute5) {
		this.cosAttribute5 = cosAttribute5;
	}
    /**
     * @return 
     */
	public String getCosAttribute6() {
		return cosAttribute6;
	}

	public void setCosAttribute6(String cosAttribute6) {
		this.cosAttribute6 = cosAttribute6;
	}
    /**
     * @return 
     */
	public String getCosAttribute7() {
		return cosAttribute7;
	}

	public void setCosAttribute7(String cosAttribute7) {
		this.cosAttribute7 = cosAttribute7;
	}
    /**
     * @return 
     */
	public String getCosAttribute8() {
		return cosAttribute8;
	}

	public void setCosAttribute8(String cosAttribute8) {
		this.cosAttribute8 = cosAttribute8;
	}
    /**
     * @return 
     */
	public String getCosAttribute9() {
		return cosAttribute9;
	}

	public void setCosAttribute9(String cosAttribute9) {
		this.cosAttribute9 = cosAttribute9;
	}
    /**
     * @return 
     */
	public String getCosAttribute10() {
		return cosAttribute10;
	}

	public void setCosAttribute10(String cosAttribute10) {
		this.cosAttribute10 = cosAttribute10;
	}

	public String getCosNcCode() {
		return cosNcCode;
	}

	public void setCosNcCode(String cosNcCode) {
		this.cosNcCode = cosNcCode;
	}

	public BigDecimal getCosLensPower() {
		return cosLensPower;
	}

	public void setCosLensPower(BigDecimal cosLensPower) {
		this.cosLensPower = cosLensPower;
	}

	public BigDecimal getCosPbsPower() {
		return cosPbsPower;
	}

	public void setCosPbsPower(BigDecimal cosPbsPower) {
		this.cosPbsPower = cosPbsPower;
	}

	public String getCosOperator() {
		return cosOperator;
	}

	public void setCosOperator(String cosOperator) {
		this.cosOperator = cosOperator;
	}

	public String getCosRemark() {
		return cosRemark;
	}

	public void setCosRemark(String cosRemark) {
		this.cosRemark = cosRemark;
	}

	public String getCosVoltageLevel() {
		return cosVoltageLevel;
	}

	public void setCosVoltageLevel(String cosVoltageLevel) {
		this.cosVoltageLevel = cosVoltageLevel;
	}
}
