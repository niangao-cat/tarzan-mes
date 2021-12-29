package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 自动化测试接口表
 *
 * @author wenzhang.yu@hand-china 2021-01-06 11:37:08
 */
@ApiModel("自动化测试接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_atp_collect_iface")
@CustomPrimary
public class ItfAtpCollectIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_ATP_HR_YB_RDT = "atpHrYbRdt";
    public static final String FIELD_ATP_YB_OC_RDT = "atpYbOcRdt";
    public static final String FIELD_ATP_OC_61_RDT = "atpOc61Rdt";
    public static final String FIELD_ATP_HR_R61_RDT = "atpHrR61Rdt";
    public static final String FIELD_ATP_61_CMS1_RDT = "atp61Cms1Rdt";
    public static final String FIELD_ATP_CMS1_CMS2_RDT = "atpCms1Cms2Rdt";
    public static final String FIELD_ATP_OTHER_RDT = "atpOtherRdt";
    public static final String FIELD_ATP_HR_SRGXT = "atpHrSrgxt";
    public static final String FIELD_ATP_HR_SQT = "atpHrSqt";
    public static final String FIELD_ATP_HR_SCGXT = "atpHrScgxt";
    public static final String FIELD_ATP_OC_SRGXT = "atpOcSrgxt";
    public static final String FIELD_ATP_OC_SQT = "atpOcSqt";
    public static final String FIELD_ATP_OC_SCGXT = "atpOcScgxt";
    public static final String FIELD_ATP_R61_SRGXT = "atpR61Srgxt";
    public static final String FIELD_ATP_R61_SQT = "atpR61Sqt";
    public static final String FIELD_ATP_R61_SCGXT = "atpR61Scgxt";
    public static final String FIELD_ATP_61_SRGXT = "atp61Srgxt";
    public static final String FIELD_ATP_61_SQT = "atp61Sqt";
    public static final String FIELD_ATP_61_SCGXT = "atp61Scgxt";
    public static final String FIELD_ATP_CMS1_SRGXT = "atpCms1Srgxt";
    public static final String FIELD_ATP_CMS1_SQT = "atpCms1Sqt";
    public static final String FIELD_ATP_CMS1_SCGXT = "atpCms1Scgxt";
    public static final String FIELD_ATP_YB_GXPT = "atpYbGxpt";
    public static final String FIELD_ATP_OTHER_QJT = "atpOtherQjt";
    public static final String FIELD_ATP_RED_POWER = "atpRedPower";
    public static final String FIELD_ATP_RED_VOLTAGE = "atpRedVoltage";
    public static final String FIELD_ATP_LEAKAGE = "atpLeakage";
    public static final String FIELD_ATP_SPECTRAL_IMAGE = "atpSpectralImage";
    public static final String FIELD_ATP_POWER = "atpPower";
    public static final String FIELD_ATP_CURRENT = "atpCurrent";
    public static final String FIELD_ATP_SLT = "atpSlt";
    public static final String FIELD_ATP_OPERATOR = "atpOperator";
    public static final String FIELD_ATP_OPERATION_TIME = "atpOperationTime";
    public static final String FIELD_PRIMARY_KEY = "primaryKey";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ATP_ATTRIBUTE1 = "atpAttribute1";
    public static final String FIELD_ATP_ATTRIBUTE2 = "atpAttribute2";
    public static final String FIELD_ATP_ATTRIBUTE3 = "atpAttribute3";
    public static final String FIELD_ATP_ATTRIBUTE4 = "atpAttribute4";
    public static final String FIELD_ATP_ATTRIBUTE5 = "atpAttribute5";
    public static final String FIELD_ATP_ATTRIBUTE6 = "atpAttribute6";
    public static final String FIELD_ATP_ATTRIBUTE7 = "atpAttribute7";
    public static final String FIELD_ATP_ATTRIBUTE8 = "atpAttribute8";
    public static final String FIELD_ATP_ATTRIBUTE9 = "atpAttribute9";
    public static final String FIELD_ATP_ATTRIBUTE10 = "atpAttribute10";
	public static final String FIELD_ATP_ATTRIBUTE11 = "atpAttribute11";
	public static final String FIELD_ATP_ATTRIBUTE12 = "atpAttribute12";
	public static final String FIELD_ATP_ATTRIBUTE13 = "atpAttribute13";
	public static final String FIELD_ATP_ATTRIBUTE14 = "atpAttribute14";
	public static final String FIELD_ATP_ATTRIBUTE15 = "atpAttribute15";

	public static final String FIELD_ATP_HR_MOYB_RDT = "atpHrMoybRdt";
	public static final String FIELD_ATP_MOYB_OC_RDT = "atpMoybOcRdt";
	public static final String FIELD_ATP_OC_PAYB_RDT = "atpOcPaybRdt";
	public static final String FIELD_ATP_PAYB_61RDT = "atpPayb61rdt";
	public static final String FIELD_ATP_BLOCK_U_T = "atpBlockUT";
	public static final String FIELD_ATP_BLOCK_D_T = "atpBlockDT";
	public static final String FIELD_ATP_NON_LINEAR = "atpNonLinear";
	public static final String FIELD_ATP_CMS_JUMPER_RDT = "atpCmsJumperRdt";
	public static final String FIELD_ATP_FIBER1_T = "atpFiber1T";
	public static final String FIELD_ATP_FIBER2_T = "atpFiber2T";
	public static final String FIELD_ATP_CMS2_SRGXT = "atpCms2Srgxt";
	public static final String FIELD_ATP_CMS2_SQT = "atpCms2Sqt";
	public static final String FIELD_ATP_CMS2_SCGXT = "atpCms2Scgxt";
	public static final String FIELD_ATP_TEMPERATURE = "atpTemperature";
	public static final String FIELD_ATP_HUMIDITY = "atpHumidity";
	public static final String FIELD_ATP_CASE_T = "atpCaseT";

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
   @ApiModelProperty(value = "HR-YB熔点温度")    
    private BigDecimal atpHrYbRdt;
   @ApiModelProperty(value = "YB-OC熔点温度")    
    private BigDecimal atpYbOcRdt;
   @ApiModelProperty(value = "OC-6+1熔点温度")    
    private BigDecimal atpOc61Rdt;
   @ApiModelProperty(value = "HR-R6+1熔点温度")    
    private BigDecimal atpHrR61Rdt;
   @ApiModelProperty(value = "6+1-CMS1熔点温度")    
    private BigDecimal atp61Cms1Rdt;
   @ApiModelProperty(value = "CMS1-CMS2熔点温度")    
    private BigDecimal atpCms1Cms2Rdt;
   @ApiModelProperty(value = "其他熔点熔点温度")    
    private BigDecimal atpOtherRdt;
   @ApiModelProperty(value = "HR输入光纤温度")    
    private BigDecimal atpHrSrgxt;
   @ApiModelProperty(value = "HR栅区温度")    
    private BigDecimal atpHrSqt;
   @ApiModelProperty(value = "HR输出光纤温度")    
    private BigDecimal atpHrScgxt;
   @ApiModelProperty(value = "OC输入光纤温度")    
    private BigDecimal atpOcSrgxt;
   @ApiModelProperty(value = "OC栅区温度")    
    private BigDecimal atpOcSqt;
   @ApiModelProperty(value = "OC输出光纤温度")    
    private BigDecimal atpOcScgxt;
   @ApiModelProperty(value = "R6+1输入光纤温度")    
    private BigDecimal atpR61Srgxt;
   @ApiModelProperty(value = "R6+1栅区温度")    
    private BigDecimal atpR61Sqt;
   @ApiModelProperty(value = "R6+1输出光纤温度")    
    private BigDecimal atpR61Scgxt;
   @ApiModelProperty(value = "6+1输入光纤温度")    
    private BigDecimal atp61Srgxt;
   @ApiModelProperty(value = "6+1栅区温度")    
    private BigDecimal atp61Sqt;
   @ApiModelProperty(value = "6+1输出光纤温度")    
    private BigDecimal atp61Scgxt;
   @ApiModelProperty(value = "CMS1输入光纤温度")    
    private BigDecimal atpCms1Srgxt;
   @ApiModelProperty(value = "CMS1栅区温度")    
    private BigDecimal atpCms1Sqt;
   @ApiModelProperty(value = "CMS1输出光纤温度")    
    private BigDecimal atpCms1Scgxt;
   @ApiModelProperty(value = "YB光纤盘温度")    
    private BigDecimal atpYbGxpt;
   @ApiModelProperty(value = "其他器件温度")    
    private BigDecimal atpOtherQjt;
   @ApiModelProperty(value = "红光功率")    
    private BigDecimal atpRedPower;
   @ApiModelProperty(value = "红光电压")    
    private BigDecimal atpRedVoltage;
   @ApiModelProperty(value = "监控点漏光值")    
    private BigDecimal atpLeakage;
   @ApiModelProperty(value = "光谱图像")    
    private String atpSpectralImage;
   @ApiModelProperty(value = "整机功率")    
    private BigDecimal atpPower;
   @ApiModelProperty(value = "整机电流")    
    private BigDecimal atpCurrent;
   @ApiModelProperty(value = "水冷机温度")    
    private BigDecimal atpSlt;
   @ApiModelProperty(value = "操作者")    
    private String atpOperator;
   @ApiModelProperty(value = "操作时间")    
    private Date atpOperationTime;
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
    private String atpAttribute1;
   @ApiModelProperty(value = "")    
    private String atpAttribute2;
   @ApiModelProperty(value = "")    
    private String atpAttribute3;
   @ApiModelProperty(value = "")    
    private String atpAttribute4;
   @ApiModelProperty(value = "")    
    private String atpAttribute5;
   @ApiModelProperty(value = "")    
    private String atpAttribute6;
   @ApiModelProperty(value = "")    
    private String atpAttribute7;
   @ApiModelProperty(value = "")    
    private String atpAttribute8;
   @ApiModelProperty(value = "")    
    private String atpAttribute9;
   @ApiModelProperty(value = "")    
    private String atpAttribute10;


	@ApiModelProperty(value = "")
	private String atpAttribute11;
	@ApiModelProperty(value = "")
	private String atpAttribute12;
	@ApiModelProperty(value = "")
	private String atpAttribute13;
	@ApiModelProperty(value = "")
	private String atpAttribute14;
	@ApiModelProperty(value = "")
	private String atpAttribute15;

	@ApiModelProperty(value = "HR-MOYB熔点温度")
	private BigDecimal atpHrMoybRdt;
	@ApiModelProperty(value = "MOYB-OC熔点温度")
	private BigDecimal atpMoybOcRdt;
	@ApiModelProperty(value = "OC-PAYB熔点温度")
	private BigDecimal atpOcPaybRdt;
	@ApiModelProperty(value = "PAYB-6+1熔点温度")
	private BigDecimal atpPayb61Rdt;
	@ApiModelProperty(value = "高反吸收块上温度")
	private BigDecimal atpBlockUT;
	@ApiModelProperty(value = "高反吸收块下温度")
	private BigDecimal atpBlockDT;
	@ApiModelProperty(value = "非线性数值")
	private BigDecimal atpNonLinear;
	@ApiModelProperty(value = "CMS-跳线熔点温度")
	private BigDecimal atpCmsJumperRdt;
	@ApiModelProperty(value = "监控点光纤1温度")
	private BigDecimal atpFiber1T;
	@ApiModelProperty(value = "监控点光纤2温度")
	private BigDecimal atpFiber2T;
	@ApiModelProperty(value = "CMS2输入光纤温度")
	private BigDecimal atpCms2Srgxt;
	@ApiModelProperty(value = "CMS2栅区温度")
	private BigDecimal atpCms2Sqt;
	@ApiModelProperty(value = "CMS2输出光纤温度")
	private BigDecimal atpCms2Scgxt;
	@ApiModelProperty(value = "环境温度")
	private BigDecimal atpTemperature;
	@ApiModelProperty(value = "环境湿度")
	private BigDecimal atpHumidity;
	@ApiModelProperty(value = "光学壳体温度")
	private BigDecimal atpCaseT;

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
     * @return HR-YB熔点温度
     */
	public BigDecimal getAtpHrYbRdt() {
		return atpHrYbRdt;
	}

	public void setAtpHrYbRdt(BigDecimal atpHrYbRdt) {
		this.atpHrYbRdt = atpHrYbRdt;
	}
    /**
     * @return YB-OC熔点温度
     */
	public BigDecimal getAtpYbOcRdt() {
		return atpYbOcRdt;
	}

	public void setAtpYbOcRdt(BigDecimal atpYbOcRdt) {
		this.atpYbOcRdt = atpYbOcRdt;
	}
    /**
     * @return OC-6+1熔点温度
     */
	public BigDecimal getAtpOc61Rdt() {
		return atpOc61Rdt;
	}

	public void setAtpOc61Rdt(BigDecimal atpOc61Rdt) {
		this.atpOc61Rdt = atpOc61Rdt;
	}
    /**
     * @return HR-R6+1熔点温度
     */
	public BigDecimal getAtpHrR61Rdt() {
		return atpHrR61Rdt;
	}

	public void setAtpHrR61Rdt(BigDecimal atpHrR61Rdt) {
		this.atpHrR61Rdt = atpHrR61Rdt;
	}
    /**
     * @return 6+1-CMS1熔点温度
     */
	public BigDecimal getAtp61Cms1Rdt() {
		return atp61Cms1Rdt;
	}

	public void setAtp61Cms1Rdt(BigDecimal atp61Cms1Rdt) {
		this.atp61Cms1Rdt = atp61Cms1Rdt;
	}
    /**
     * @return CMS1-CMS2熔点温度
     */
	public BigDecimal getAtpCms1Cms2Rdt() {
		return atpCms1Cms2Rdt;
	}

	public void setAtpCms1Cms2Rdt(BigDecimal atpCms1Cms2Rdt) {
		this.atpCms1Cms2Rdt = atpCms1Cms2Rdt;
	}
    /**
     * @return 其他熔点熔点温度
     */
	public BigDecimal getAtpOtherRdt() {
		return atpOtherRdt;
	}

	public void setAtpOtherRdt(BigDecimal atpOtherRdt) {
		this.atpOtherRdt = atpOtherRdt;
	}
    /**
     * @return HR输入光纤温度
     */
	public BigDecimal getAtpHrSrgxt() {
		return atpHrSrgxt;
	}

	public void setAtpHrSrgxt(BigDecimal atpHrSrgxt) {
		this.atpHrSrgxt = atpHrSrgxt;
	}
    /**
     * @return HR栅区温度
     */
	public BigDecimal getAtpHrSqt() {
		return atpHrSqt;
	}

	public void setAtpHrSqt(BigDecimal atpHrSqt) {
		this.atpHrSqt = atpHrSqt;
	}
    /**
     * @return HR输出光纤温度
     */
	public BigDecimal getAtpHrScgxt() {
		return atpHrScgxt;
	}

	public void setAtpHrScgxt(BigDecimal atpHrScgxt) {
		this.atpHrScgxt = atpHrScgxt;
	}
    /**
     * @return OC输入光纤温度
     */
	public BigDecimal getAtpOcSrgxt() {
		return atpOcSrgxt;
	}

	public void setAtpOcSrgxt(BigDecimal atpOcSrgxt) {
		this.atpOcSrgxt = atpOcSrgxt;
	}
    /**
     * @return OC栅区温度
     */
	public BigDecimal getAtpOcSqt() {
		return atpOcSqt;
	}

	public void setAtpOcSqt(BigDecimal atpOcSqt) {
		this.atpOcSqt = atpOcSqt;
	}
    /**
     * @return OC输出光纤温度
     */
	public BigDecimal getAtpOcScgxt() {
		return atpOcScgxt;
	}

	public void setAtpOcScgxt(BigDecimal atpOcScgxt) {
		this.atpOcScgxt = atpOcScgxt;
	}
    /**
     * @return R6+1输入光纤温度
     */
	public BigDecimal getAtpR61Srgxt() {
		return atpR61Srgxt;
	}

	public void setAtpR61Srgxt(BigDecimal atpR61Srgxt) {
		this.atpR61Srgxt = atpR61Srgxt;
	}
    /**
     * @return R6+1栅区温度
     */
	public BigDecimal getAtpR61Sqt() {
		return atpR61Sqt;
	}

	public void setAtpR61Sqt(BigDecimal atpR61Sqt) {
		this.atpR61Sqt = atpR61Sqt;
	}
    /**
     * @return R6+1输出光纤温度
     */
	public BigDecimal getAtpR61Scgxt() {
		return atpR61Scgxt;
	}

	public void setAtpR61Scgxt(BigDecimal atpR61Scgxt) {
		this.atpR61Scgxt = atpR61Scgxt;
	}
    /**
     * @return 6+1输入光纤温度
     */
	public BigDecimal getAtp61Srgxt() {
		return atp61Srgxt;
	}

	public void setAtp61Srgxt(BigDecimal atp61Srgxt) {
		this.atp61Srgxt = atp61Srgxt;
	}
    /**
     * @return 6+1栅区温度
     */
	public BigDecimal getAtp61Sqt() {
		return atp61Sqt;
	}

	public void setAtp61Sqt(BigDecimal atp61Sqt) {
		this.atp61Sqt = atp61Sqt;
	}
    /**
     * @return 6+1输出光纤温度
     */
	public BigDecimal getAtp61Scgxt() {
		return atp61Scgxt;
	}

	public void setAtp61Scgxt(BigDecimal atp61Scgxt) {
		this.atp61Scgxt = atp61Scgxt;
	}
    /**
     * @return CMS1输入光纤温度
     */
	public BigDecimal getAtpCms1Srgxt() {
		return atpCms1Srgxt;
	}

	public void setAtpCms1Srgxt(BigDecimal atpCms1Srgxt) {
		this.atpCms1Srgxt = atpCms1Srgxt;
	}
    /**
     * @return CMS1栅区温度
     */
	public BigDecimal getAtpCms1Sqt() {
		return atpCms1Sqt;
	}

	public void setAtpCms1Sqt(BigDecimal atpCms1Sqt) {
		this.atpCms1Sqt = atpCms1Sqt;
	}
    /**
     * @return CMS1输出光纤温度
     */
	public BigDecimal getAtpCms1Scgxt() {
		return atpCms1Scgxt;
	}

	public void setAtpCms1Scgxt(BigDecimal atpCms1Scgxt) {
		this.atpCms1Scgxt = atpCms1Scgxt;
	}
    /**
     * @return YB光纤盘温度
     */
	public BigDecimal getAtpYbGxpt() {
		return atpYbGxpt;
	}

	public void setAtpYbGxpt(BigDecimal atpYbGxpt) {
		this.atpYbGxpt = atpYbGxpt;
	}
    /**
     * @return 其他器件温度
     */
	public BigDecimal getAtpOtherQjt() {
		return atpOtherQjt;
	}

	public void setAtpOtherQjt(BigDecimal atpOtherQjt) {
		this.atpOtherQjt = atpOtherQjt;
	}
    /**
     * @return 红光功率
     */
	public BigDecimal getAtpRedPower() {
		return atpRedPower;
	}

	public void setAtpRedPower(BigDecimal atpRedPower) {
		this.atpRedPower = atpRedPower;
	}
    /**
     * @return 红光电压
     */
	public BigDecimal getAtpRedVoltage() {
		return atpRedVoltage;
	}

	public void setAtpRedVoltage(BigDecimal atpRedVoltage) {
		this.atpRedVoltage = atpRedVoltage;
	}
    /**
     * @return 监控点漏光值
     */
	public BigDecimal getAtpLeakage() {
		return atpLeakage;
	}

	public void setAtpLeakage(BigDecimal atpLeakage) {
		this.atpLeakage = atpLeakage;
	}
    /**
     * @return 光谱图像
     */
	public String getAtpSpectralImage() {
		return atpSpectralImage;
	}

	public void setAtpSpectralImage(String atpSpectralImage) {
		this.atpSpectralImage = atpSpectralImage;
	}
    /**
     * @return 整机功率
     */
	public BigDecimal getAtpPower() {
		return atpPower;
	}

	public void setAtpPower(BigDecimal atpPower) {
		this.atpPower = atpPower;
	}
    /**
     * @return 整机电流
     */
	public BigDecimal getAtpCurrent() {
		return atpCurrent;
	}

	public void setAtpCurrent(BigDecimal atpCurrent) {
		this.atpCurrent = atpCurrent;
	}
    /**
     * @return 水冷机温度
     */
	public BigDecimal getAtpSlt() {
		return atpSlt;
	}

	public void setAtpSlt(BigDecimal atpSlt) {
		this.atpSlt = atpSlt;
	}
    /**
     * @return 操作者
     */
	public String getAtpOperator() {
		return atpOperator;
	}

	public void setAtpOperator(String atpOperator) {
		this.atpOperator = atpOperator;
	}
    /**
     * @return 操作时间
     */
	public Date getAtpOperationTime() {
		return atpOperationTime;
	}

	public void setAtpOperationTime(Date atpOperationTime) {
		this.atpOperationTime = atpOperationTime;
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
	public String getAtpAttribute1() {
		return atpAttribute1;
	}

	public void setAtpAttribute1(String atpAttribute1) {
		this.atpAttribute1 = atpAttribute1;
	}
    /**
     * @return 
     */
	public String getAtpAttribute2() {
		return atpAttribute2;
	}

	public void setAtpAttribute2(String atpAttribute2) {
		this.atpAttribute2 = atpAttribute2;
	}
    /**
     * @return 
     */
	public String getAtpAttribute3() {
		return atpAttribute3;
	}

	public void setAtpAttribute3(String atpAttribute3) {
		this.atpAttribute3 = atpAttribute3;
	}
    /**
     * @return 
     */
	public String getAtpAttribute4() {
		return atpAttribute4;
	}

	public void setAtpAttribute4(String atpAttribute4) {
		this.atpAttribute4 = atpAttribute4;
	}
    /**
     * @return 
     */
	public String getAtpAttribute5() {
		return atpAttribute5;
	}

	public void setAtpAttribute5(String atpAttribute5) {
		this.atpAttribute5 = atpAttribute5;
	}
    /**
     * @return 
     */
	public String getAtpAttribute6() {
		return atpAttribute6;
	}

	public void setAtpAttribute6(String atpAttribute6) {
		this.atpAttribute6 = atpAttribute6;
	}
    /**
     * @return 
     */
	public String getAtpAttribute7() {
		return atpAttribute7;
	}

	public void setAtpAttribute7(String atpAttribute7) {
		this.atpAttribute7 = atpAttribute7;
	}
    /**
     * @return 
     */
	public String getAtpAttribute8() {
		return atpAttribute8;
	}

	public void setAtpAttribute8(String atpAttribute8) {
		this.atpAttribute8 = atpAttribute8;
	}
    /**
     * @return 
     */
	public String getAtpAttribute9() {
		return atpAttribute9;
	}

	public void setAtpAttribute9(String atpAttribute9) {
		this.atpAttribute9 = atpAttribute9;
	}
    /**
     * @return 
     */
	public String getAtpAttribute10() {
		return atpAttribute10;
	}


	public String getAtpAttribute11() {
		return atpAttribute11;
	}

	public void setAtpAttribute11(String atpAttribute11) {
		this.atpAttribute11 = atpAttribute11;
	}

	public String getAtpAttribute12() {
		return atpAttribute12;
	}

	public void setAtpAttribute12(String atpAttribute12) {
		this.atpAttribute12 = atpAttribute12;
	}

	public String getAtpAttribute13() {
		return atpAttribute13;
	}

	public void setAtpAttribute13(String atpAttribute13) {
		this.atpAttribute13 = atpAttribute13;
	}

	public String getAtpAttribute14() {
		return atpAttribute14;
	}

	public void setAtpAttribute14(String atpAttribute14) {
		this.atpAttribute14 = atpAttribute14;
	}

	public String getAtpAttribute15() {
		return atpAttribute15;
	}

	public void setAtpAttribute15(String atpAttribute15) {
		this.atpAttribute15 = atpAttribute15;
	}

	public void setAtpAttribute10(String atpAttribute10) {
		this.atpAttribute10 = atpAttribute10;
	}

	public BigDecimal getAtpHrMoybRdt() {
		return atpHrMoybRdt;
	}

	public void setAtpHrMoybRdt(BigDecimal atpHrMoybRdt) {
		this.atpHrMoybRdt = atpHrMoybRdt;
	}

	public BigDecimal getAtpMoybOcRdt() {
		return atpMoybOcRdt;
	}

	public void setAtpMoybOcRdt(BigDecimal atpMoybOcRdt) {
		this.atpMoybOcRdt = atpMoybOcRdt;
	}

	public BigDecimal getAtpOcPaybRdt() {
		return atpOcPaybRdt;
	}

	public void setAtpOcPaybRdt(BigDecimal atpOcPaybRdt) {
		this.atpOcPaybRdt = atpOcPaybRdt;
	}

	public BigDecimal getAtpPayb61Rdt() {
		return atpPayb61Rdt;
	}

	public void setAtpPayb61Rdt(BigDecimal atpPayb61Rdt) {
		this.atpPayb61Rdt = atpPayb61Rdt;
	}

	public BigDecimal getAtpBlockUT() {
		return atpBlockUT;
	}

	public void setAtpBlockUT(BigDecimal atpBlockUT) {
		this.atpBlockUT = atpBlockUT;
	}

	public BigDecimal getAtpBlockDT() {
		return atpBlockDT;
	}

	public void setAtpBlockDT(BigDecimal atpBlockDT) {
		this.atpBlockDT = atpBlockDT;
	}

	public BigDecimal getAtpNonLinear() {
		return atpNonLinear;
	}

	public void setAtpNonLinear(BigDecimal atpNonLinear) {
		this.atpNonLinear = atpNonLinear;
	}

	public BigDecimal getAtpCmsJumperRdt() {
		return atpCmsJumperRdt;
	}

	public void setAtpCmsJumperRdt(BigDecimal atpCmsJumperRdt) {
		this.atpCmsJumperRdt = atpCmsJumperRdt;
	}

	public BigDecimal getAtpFiber1T() {
		return atpFiber1T;
	}

	public void setAtpFiber1T(BigDecimal atpFiber1T) {
		this.atpFiber1T = atpFiber1T;
	}

	public BigDecimal getAtpFiber2T() {
		return atpFiber2T;
	}

	public void setAtpFiber2T(BigDecimal atpFiber2T) {
		this.atpFiber2T = atpFiber2T;
	}

	public BigDecimal getAtpCms2Srgxt() {
		return atpCms2Srgxt;
	}

	public void setAtpCms2Srgxt(BigDecimal atpCms2Srgxt) {
		this.atpCms2Srgxt = atpCms2Srgxt;
	}

	public BigDecimal getAtpCms2Sqt() {
		return atpCms2Sqt;
	}

	public void setAtpCms2Sqt(BigDecimal atpCms2Sqt) {
		this.atpCms2Sqt = atpCms2Sqt;
	}

	public BigDecimal getAtpCms2Scgxt() {
		return atpCms2Scgxt;
	}

	public void setAtpCms2Scgxt(BigDecimal atpCms2Scgxt) {
		this.atpCms2Scgxt = atpCms2Scgxt;
	}

	public BigDecimal getAtpTemperature() {
		return atpTemperature;
	}

	public void setAtpTemperature(BigDecimal atpTemperature) {
		this.atpTemperature = atpTemperature;
	}

	public BigDecimal getAtpHumidity() {
		return atpHumidity;
	}

	public void setAtpHumidity(BigDecimal atpHumidity) {
		this.atpHumidity = atpHumidity;
	}

	public BigDecimal getAtpCaseT() {
		return atpCaseT;
	}

	public void setAtpCaseT(BigDecimal atpCaseT) {
		this.atpCaseT = atpCaseT;
	}
}
