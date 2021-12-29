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
 * 售后登记平台表
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 09:59:30
 */
@ApiModel("售后登记平台表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_after_sales_repair")
@CustomPrimary
public class HmeAfterSalesRepair extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_AFTER_SALES_REPAIR_ID = "afterSalesRepairId";
    public static final String FIELD_LOGISTICS_INFO_ID = "logisticsInfoId";
    public static final String FIELD_SERVICE_RECEIVE_ID = "serviceReceiveId";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_AREA_CODE = "areaCode";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_VIRTUAL_MATERIAL_CODE = "virtualMaterialCode";
    public static final String FIELD_LOGISTICS_NUMBER = "logisticsNumber";
    public static final String FIELD_LOGISTICS_COMPANY = "logisticsCompany";
    public static final String FIELD_BACK_TYPE = "backType";
    public static final String FIELD_STOCK_IN_NUMBER = "stockInNumber";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_STOCK_OUT_NUMBER = "stockOutNumber";
    public static final String FIELD_VIRTUAL_MATERIAL_DESC = "virtualMaterialDesc";
    public static final String FIELD_ITEM_GROUP = "itemGroup";
    public static final String FIELD_CUSTOMER_ID = "customerId";
    public static final String FIELD_STOCK_IN_STATUS = "stockInStatus";
    public static final String FIELD_STOCK_OUT_STATUS = "stockOutStatus";
    public static final String FIELD_STOCK_IN_ORG = "stockInOrg";
    public static final String FIELD_STOCK_OUT_ORG = "stockOutOrg";
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


    @ApiModelProperty("租户ID")
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "主键",required = true)
    @Id
    private String afterSalesRepairId;
    @ApiModelProperty(value = "物流单号ID",required = true)
    @NotBlank
    private String logisticsInfoId;
    @ApiModelProperty(value = "售后接收ID",required = true)
    @NotBlank
    private String serviceReceiveId;
    @ApiModelProperty(value = "机器编码",required = true)
    @NotBlank
    private String snNum;
    @ApiModelProperty(value = "部门",required = true)
    @NotBlank
    private String areaCode;
    @ApiModelProperty(value = "工厂",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "返品物料编码",required = true)
    @NotBlank
    private String materialId;
   @ApiModelProperty(value = "虚拟物料号")    
    private String virtualMaterialCode;
   @ApiModelProperty(value = "提单")    
    private String logisticsNumber;
   @ApiModelProperty(value = "物流公司")    
    private String logisticsCompany;
   @ApiModelProperty(value = "实物返回属性")    
    private String backType;
   @ApiModelProperty(value = "交货")    
    private String stockInNumber;
   @ApiModelProperty(value = "订单号")    
    private String workOrderId;
   @ApiModelProperty(value = "库存地点")    
    private String locatorId;
   @ApiModelProperty(value = "出库单号")    
    private String stockOutNumber;
   @ApiModelProperty(value = "虚拟物料号描述")    
    private String virtualMaterialDesc;
   @ApiModelProperty(value = "物料组")    
    private String itemGroup;
   @ApiModelProperty(value = "客户编号")    
    private String customerId;
   @ApiModelProperty(value = "入库单处理状态")    
    private String stockInStatus;
   @ApiModelProperty(value = "出库单处理状态")    
    private String stockOutStatus;
   @ApiModelProperty(value = "入库销售组织")    
    private String stockInOrg;
   @ApiModelProperty(value = "出库销售组织")    
    private String stockOutOrg;
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
     * @return 主键
     */
	public String getAfterSalesRepairId() {
		return afterSalesRepairId;
	}

	public void setAfterSalesRepairId(String afterSalesRepairId) {
		this.afterSalesRepairId = afterSalesRepairId;
	}
    /**
     * @return 物流单号ID
     */
	public String getLogisticsInfoId() {
		return logisticsInfoId;
	}

	public void setLogisticsInfoId(String logisticsInfoId) {
		this.logisticsInfoId = logisticsInfoId;
	}
    /**
     * @return 售后接收ID
     */
	public String getServiceReceiveId() {
		return serviceReceiveId;
	}

	public void setServiceReceiveId(String serviceReceiveId) {
		this.serviceReceiveId = serviceReceiveId;
	}
    /**
     * @return 机器编码
     */
	public String getSnNum() {
		return snNum;
	}

	public void setSnNum(String snNum) {
		this.snNum = snNum;
	}
    /**
     * @return 部门
     */
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
    /**
     * @return 工厂
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 返品物料编码
     */
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
    /**
     * @return 虚拟物料号
     */
	public String getVirtualMaterialCode() {
		return virtualMaterialCode;
	}

	public void setVirtualMaterialCode(String virtualMaterialCode) {
		this.virtualMaterialCode = virtualMaterialCode;
	}
    /**
     * @return 提单
     */
	public String getLogisticsNumber() {
		return logisticsNumber;
	}

	public void setLogisticsNumber(String logisticsNumber) {
		this.logisticsNumber = logisticsNumber;
	}
    /**
     * @return 物流公司
     */
	public String getLogisticsCompany() {
		return logisticsCompany;
	}

	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}
    /**
     * @return 实物返回属性
     */
	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}
    /**
     * @return 交货
     */
	public String getStockInNumber() {
		return stockInNumber;
	}

	public void setStockInNumber(String stockInNumber) {
		this.stockInNumber = stockInNumber;
	}
    /**
     * @return 订单号
     */
	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
    /**
     * @return 库存地点
     */
	public String getLocatorId() {
		return locatorId;
	}

	public void setLocatorId(String locatorId) {
		this.locatorId = locatorId;
	}
    /**
     * @return 出库单号
     */
	public String getStockOutNumber() {
		return stockOutNumber;
	}

	public void setStockOutNumber(String stockOutNumber) {
		this.stockOutNumber = stockOutNumber;
	}
    /**
     * @return 虚拟物料号描述
     */
	public String getVirtualMaterialDesc() {
		return virtualMaterialDesc;
	}

	public void setVirtualMaterialDesc(String virtualMaterialDesc) {
		this.virtualMaterialDesc = virtualMaterialDesc;
	}
    /**
     * @return 物料组
     */
	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}
    /**
     * @return 客户编号
     */
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
    /**
     * @return 入库单处理状态
     */
	public String getStockInStatus() {
		return stockInStatus;
	}

	public void setStockInStatus(String stockInStatus) {
		this.stockInStatus = stockInStatus;
	}
    /**
     * @return 出库单处理状态
     */
	public String getStockOutStatus() {
		return stockOutStatus;
	}

	public void setStockOutStatus(String stockOutStatus) {
		this.stockOutStatus = stockOutStatus;
	}
    /**
     * @return 入库销售组织
     */
	public String getStockInOrg() {
		return stockInOrg;
	}

	public void setStockInOrg(String stockInOrg) {
		this.stockInOrg = stockInOrg;
	}
    /**
     * @return 出库销售组织
     */
	public String getStockOutOrg() {
		return stockOutOrg;
	}

	public void setStockOutOrg(String stockOutOrg) {
		this.stockOutOrg = stockOutOrg;
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
