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

import java.util.Date;

/**
 * 营销服务部接收拆箱登记表
 *
 * @author chaonan.hu@hand-china.com 2020-09-01 14:14:21
 */
@ApiModel("营销服务部接收拆箱登记表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_service_receive")
public class HmeServiceReceive extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SERVICE_RECEIVE_ID = "serviceReceiveId";
    public static final String FIELD_LOGISTICS_INFO_ID = "logisticsInfoId";
    public static final String FIELD_AREA_CODE = "areaCode";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_PRODUCTION_VERSION = "productionVersion";
    public static final String FIELD_RECEIVE_BY = "receiveBy";
    public static final String FIELD_RECEIVE_DATE = "receiveDate";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_RECEIVE_STATUS = "receiveStatus";
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


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "站点ID",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty("主键")
    @Id
    private String serviceReceiveId;
    @ApiModelProperty(value = "物流信息表ID",required = true)
    @NotBlank
    private String logisticsInfoId;
   @ApiModelProperty(value = "区域编码（制造部）")    
    private String areaCode;
   @ApiModelProperty(value = "物料批ID")    
    private String materialLotId;
    @ApiModelProperty(value = "sn",required = true)
    @NotBlank
    private String snNum;
   @ApiModelProperty(value = "物料ID")    
    private String materialId;
   @ApiModelProperty(value = "物料版本")    
    private String productionVersion;
   @ApiModelProperty(value = "接收人")    
    private Long receiveBy;
   @ApiModelProperty(value = "接收时间")    
    private Date receiveDate;
   @ApiModelProperty(value = "备注")    
    private String remark;
    @ApiModelProperty(value = "接收状态",required = true)
    @NotBlank
    private String receiveStatus;
   @ApiModelProperty(value = "有效标志")    
    private String enableFlag;
    @ApiModelProperty(value = "",required = true)
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
     * @return 租户ID
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 站点ID
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 主键
     */
	public String getServiceReceiveId() {
		return serviceReceiveId;
	}

	public void setServiceReceiveId(String serviceReceiveId) {
		this.serviceReceiveId = serviceReceiveId;
	}
    /**
     * @return 物流信息表ID
     */
	public String getLogisticsInfoId() {
		return logisticsInfoId;
	}

	public void setLogisticsInfoId(String logisticsInfoId) {
		this.logisticsInfoId = logisticsInfoId;
	}
    /**
     * @return 区域编码（制造部）
     */
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
    /**
     * @return 物料批ID
     */
	public String getMaterialLotId() {
		return materialLotId;
	}

	public void setMaterialLotId(String materialLotId) {
		this.materialLotId = materialLotId;
	}
    /**
     * @return sn
     */
	public String getSnNum() {
		return snNum;
	}

	public void setSnNum(String snNum) {
		this.snNum = snNum;
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
     * @return 物料版本
     */
	public String getProductionVersion() {
		return productionVersion;
	}

	public void setProductionVersion(String productionVersion) {
		this.productionVersion = productionVersion;
	}
    /**
     * @return 接收人
     */
	public Long getReceiveBy() {
		return receiveBy;
	}

	public void setReceiveBy(Long receiveBy) {
		this.receiveBy = receiveBy;
	}
    /**
     * @return 接收时间
     */
	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
    /**
     * @return 备注
     */
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    /**
     * @return 接收状态
     */
	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
    /**
     * @return 有效标志
     */
	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
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

}
