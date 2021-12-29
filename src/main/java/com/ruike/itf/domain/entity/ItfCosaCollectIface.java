package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;


/**
 * 芯片转移接口表
 *
 * @author wenzhang.yu@hand-china.com 2021-01-21 14:53:19
 */
@ApiModel("芯片转移接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_cosa_collect_iface")
public class ItfCosaCollectIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
	public static final String FIELD_VIRTUAL_NUM = "virtualNum";
	public static final String FIELD_SOURCE_MATERIAL_LOT_CODE = "sourceMaterialLotCode";
    public static final String FIELD_SOURCE_LOAD = "sourceLoad";
    public static final String FIELD_TARGET_MATERIAL_LOT_CODE = "targetMaterialLotCode";
    public static final String FIELD_TARGET_LOAD = "targetLoad";
	public static final String FIELD_TARGET_COS_POS = "targetCosPos";
    public static final String FIELD_PRIMARY_KEY = "primaryKey";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_COSA_ATTRIBUTE1 = "cosaAttribute1";
    public static final String FIELD_COSA_ATTRIBUTE2 = "cosaAttribute2";
    public static final String FIELD_COSA_ATTRIBUTE3 = "cosaAttribute3";
    public static final String FIELD_COSA_ATTRIBUTE4 = "cosaAttribute4";
    public static final String FIELD_COSA_ATTRIBUTE5 = "cosaAttribute5";
    public static final String FIELD_COSA_ATTRIBUTE6 = "cosaAttribute6";
    public static final String FIELD_COSA_ATTRIBUTE7 = "cosaAttribute7";
    public static final String FIELD_COSA_ATTRIBUTE8 = "cosaAttribute8";
    public static final String FIELD_COSA_ATTRIBUTE9 = "cosaAttribute9";
    public static final String FIELD_COSA_ATTRIBUTE10 = "cosaAttribute10";
    public static final String FIELD_COSA_ATTRIBUTE11 = "cosaAttribute11";
    public static final String FIELD_COSA_ATTRIBUTE12 = "cosaAttribute12";
    public static final String FIELD_COSA_ATTRIBUTE13 = "cosaAttribute13";
    public static final String FIELD_COSA_ATTRIBUTE14 = "cosaAttribute14";
    public static final String FIELD_COSA_ATTRIBUTE15 = "cosaAttribute15";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("接口表ID，主键")
    @Id
    @GeneratedValue
    private String interfaceId;
	@ApiModelProperty(value = "虚拟号")
	private String virtualNum;
	@ApiModelProperty(value = "来源盒子号")
    private String sourceMaterialLotCode;
   @ApiModelProperty(value = "来源位置")    
    private String sourceLoad;
   @ApiModelProperty(value = "目标盒子号")    
    private String targetMaterialLotCode;
   @ApiModelProperty(value = "目标位置")    
    private String targetLoad;
	@ApiModelProperty(value = "目标COS位置")
	private String targetCosPos;
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
    private String cosaAttribute1;
   @ApiModelProperty(value = "")    
    private String cosaAttribute2;
   @ApiModelProperty(value = "")    
    private String cosaAttribute3;
   @ApiModelProperty(value = "")    
    private String cosaAttribute4;
   @ApiModelProperty(value = "")    
    private String cosaAttribute5;
   @ApiModelProperty(value = "")    
    private String cosaAttribute6;
   @ApiModelProperty(value = "")    
    private String cosaAttribute7;
   @ApiModelProperty(value = "")    
    private String cosaAttribute8;
   @ApiModelProperty(value = "")    
    private String cosaAttribute9;
   @ApiModelProperty(value = "")    
    private String cosaAttribute10;
   @ApiModelProperty(value = "")    
    private String cosaAttribute11;
   @ApiModelProperty(value = "")    
    private String cosaAttribute12;
   @ApiModelProperty(value = "")    
    private String cosaAttribute13;
   @ApiModelProperty(value = "")    
    private String cosaAttribute14;
   @ApiModelProperty(value = "")    
    private String cosaAttribute15;

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
     * @return 来源盒子号
     */
	public String getSourceMaterialLotCode() {
		return sourceMaterialLotCode;
	}

	public void setSourceMaterialLotCode(String sourceMaterialLotCode) {
		this.sourceMaterialLotCode = sourceMaterialLotCode;
	}
    /**
     * @return 来源位置
     */
	public String getSourceLoad() {
		return sourceLoad;
	}

	public void setSourceLoad(String sourceLoad) {
		this.sourceLoad = sourceLoad;
	}
    /**
     * @return 目标盒子号
     */
	public String getTargetMaterialLotCode() {
		return targetMaterialLotCode;
	}

	public void setTargetMaterialLotCode(String targetMaterialLotCode) {
		this.targetMaterialLotCode = targetMaterialLotCode;
	}
    /**
     * @return 目标位置
     */
	public String getTargetLoad() {
		return targetLoad;
	}

	public void setTargetLoad(String targetLoad) {
		this.targetLoad = targetLoad;
	}
	/**
	 * @return 目标cos位置
	 */
	public String getTargetCosPos() {
		return targetCosPos;
	}

	public void setTargetCosPos(String targetCosPos) {
		this.targetCosPos = targetCosPos;
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
	public String getCosaAttribute1() {
		return cosaAttribute1;
	}

	public void setCosaAttribute1(String cosaAttribute1) {
		this.cosaAttribute1 = cosaAttribute1;
	}
    /**
     * @return 
     */
	public String getCosaAttribute2() {
		return cosaAttribute2;
	}

	public void setCosaAttribute2(String cosaAttribute2) {
		this.cosaAttribute2 = cosaAttribute2;
	}
    /**
     * @return 
     */
	public String getCosaAttribute3() {
		return cosaAttribute3;
	}

	public void setCosaAttribute3(String cosaAttribute3) {
		this.cosaAttribute3 = cosaAttribute3;
	}
    /**
     * @return 
     */
	public String getCosaAttribute4() {
		return cosaAttribute4;
	}

	public void setCosaAttribute4(String cosaAttribute4) {
		this.cosaAttribute4 = cosaAttribute4;
	}
    /**
     * @return 
     */
	public String getCosaAttribute5() {
		return cosaAttribute5;
	}

	public void setCosaAttribute5(String cosaAttribute5) {
		this.cosaAttribute5 = cosaAttribute5;
	}
    /**
     * @return 
     */
	public String getCosaAttribute6() {
		return cosaAttribute6;
	}

	public void setCosaAttribute6(String cosaAttribute6) {
		this.cosaAttribute6 = cosaAttribute6;
	}
    /**
     * @return 
     */
	public String getCosaAttribute7() {
		return cosaAttribute7;
	}

	public void setCosaAttribute7(String cosaAttribute7) {
		this.cosaAttribute7 = cosaAttribute7;
	}
    /**
     * @return 
     */
	public String getCosaAttribute8() {
		return cosaAttribute8;
	}

	public void setCosaAttribute8(String cosaAttribute8) {
		this.cosaAttribute8 = cosaAttribute8;
	}
    /**
     * @return 
     */
	public String getCosaAttribute9() {
		return cosaAttribute9;
	}

	public void setCosaAttribute9(String cosaAttribute9) {
		this.cosaAttribute9 = cosaAttribute9;
	}
    /**
     * @return 
     */
	public String getCosaAttribute10() {
		return cosaAttribute10;
	}

	public void setCosaAttribute10(String cosaAttribute10) {
		this.cosaAttribute10 = cosaAttribute10;
	}
    /**
     * @return 
     */
	public String getCosaAttribute11() {
		return cosaAttribute11;
	}

	public void setCosaAttribute11(String cosaAttribute11) {
		this.cosaAttribute11 = cosaAttribute11;
	}
    /**
     * @return 
     */
	public String getCosaAttribute12() {
		return cosaAttribute12;
	}

	public void setCosaAttribute12(String cosaAttribute12) {
		this.cosaAttribute12 = cosaAttribute12;
	}
    /**
     * @return 
     */
	public String getCosaAttribute13() {
		return cosaAttribute13;
	}

	public void setCosaAttribute13(String cosaAttribute13) {
		this.cosaAttribute13 = cosaAttribute13;
	}
    /**
     * @return 
     */
	public String getCosaAttribute14() {
		return cosaAttribute14;
	}

	public void setCosaAttribute14(String cosaAttribute14) {
		this.cosaAttribute14 = cosaAttribute14;
	}
    /**
     * @return 
     */
	public String getCosaAttribute15() {
		return cosaAttribute15;
	}

	public void setCosaAttribute15(String cosaAttribute15) {
		this.cosaAttribute15 = cosaAttribute15;
	}

	public String getVirtualNum() {
		return virtualNum;
	}

	public void setVirtualNum(String virtualNum) {
		this.virtualNum = virtualNum;
	}
}
