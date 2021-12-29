package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import com.ruike.itf.api.dto.ItfAfterSalesRepairSyncDTO;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 售后登记平台表
 *
 * @author kejin.liu01@hand-china.com 2020-09-02 11:26:48
 */
@ApiModel("售后登记平台表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_after_sales_repair_ifaces")
@Data
public class ItfAfterSalesRepairIfaces extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_AREA_CODE = "areaCode";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_VIRTUAL_MATERIAL_CODE = "virtualMaterialCode";
    public static final String FIELD_LOGISTICS_NUMBER = "logisticsNumber";
    public static final String FIELD_LOGISTICS_COMPANY = "logisticsCompany";
    public static final String FIELD_BACK_TYPE = "backType";
    public static final String FIELD_STOCK_IN_NUMBER = "stockInNumber";
    public static final String FIELD_WORK_ORDER_NUM = "workOrderNum";
    public static final String FIELD_LOCATOR_CODE = "locatorCode";
    public static final String FIELD_STOCK_OUT_NUMBER = "stockOutNumber";
    public static final String FIELD_VIRTUAL_MATERIAL_DESC = "virtualMaterialDesc";
    public static final String FIELD_ITEM_GROUP = "itemGroup";
    public static final String FIELD_CUSTOMER_CODE = "customerCode";
    public static final String FIELD_STOCK_IN_STATUS = "stockInStatus";
    public static final String FIELD_STOCK_OUT_STATUS = "stockOutStatus";
    public static final String FIELD_STOCK_IN_ORG = "stockInOrg";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_IS_FLAG = "isFlag";
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
    @Id
    @GeneratedValue
    private Long tenantId;
    @ApiModelProperty(value = "机器编码")
    private String snNum;
    @ApiModelProperty(value = "部门")
    private String areaCode;
    @ApiModelProperty(value = "工厂")
    private String plantCode;
    @ApiModelProperty(value = "返品物料编码")
    private String materialCode;
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
    private String workOrderNum;
    @ApiModelProperty(value = "库存地点")
    private String locatorCode;
    @ApiModelProperty(value = "出库单号")
    private String stockOutNumber;
    @ApiModelProperty(value = "虚拟物料号描述")
    private String virtualMaterialDesc;
    @ApiModelProperty(value = "物料组")
    private String itemGroup;
    @ApiModelProperty(value = "客户编号")
    private String customerCode;
    @ApiModelProperty(value = "入库单处理状态")
    private String stockInStatus;
    @ApiModelProperty(value = "出库单处理状态")
    private String stockOutStatus;
    @ApiModelProperty(value = "入库销售组织")
    private String stockInOrg;
    @ApiModelProperty(value = "记录错误信息")
    private String message;
    @ApiModelProperty(value = "是否发送成功")
    private String isFlag;
    @ApiModelProperty(value = "出库销售组织")
    private String stockOutOrg;
    @ApiModelProperty(value = "", required = true)
    @NotNull
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

    public ItfAfterSalesRepairIfaces(ItfAfterSalesRepairSyncDTO dto, Long tenantId) {
        this.tenantId = tenantId;
        this.snNum = dto.getSERNR1();
        this.areaCode = dto.getPRCTR();
        this.plantCode = dto.getWERKS();
        this.materialCode = dto.getMATNR1().replaceAll("^(0+)", "");
        this.virtualMaterialCode = dto.getMATNR2().replaceAll("^(0+)", "");
        this.logisticsNumber = dto.getBOLNR();
        this.logisticsCompany = dto.getZWL();
        this.backType = dto.getZSTATU();
        this.stockInNumber = dto.getVBELN();
        this.workOrderNum = dto.getAUFNR().replaceAll("^(0+)", "");
        this.locatorCode = dto.getLGORT();
        this.stockOutNumber = dto.getVBELN1();
        this.virtualMaterialDesc = dto.getMAKTX2();
        this.itemGroup = dto.getMATKL();
        this.customerCode = dto.getKUNNR().replaceAll("^(0+)", "");
        this.stockInStatus = dto.getWBSTK1();
        this.stockOutStatus = dto.getWBSTK2();
        this.stockInOrg = dto.getVKORG();
        this.stockOutOrg = dto.getVKORG1();
        this.isFlag = dto.getSTATUS();
        this.message = dto.getMESSAGE();
        this.cid = 1L;
    }
}