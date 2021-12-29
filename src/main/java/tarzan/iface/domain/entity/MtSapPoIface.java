package tarzan.iface.domain.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SAP采购订单接口表
 *
 * @author peng.yuan@hand-china.com 2019-10-08 19:40:53
 */
@ApiModel("SAP采购订单接口表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_sap_po_iface")
@CustomPrimary
public class MtSapPoIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_PO_NUMBER = "poNumber";
    public static final String FIELD_CONTRACT_NUM = "contractNum";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_SUPPLIER_SITE_CODE = "supplierSiteCode";
    public static final String FIELD_BUYER_CODE = "buyerCode";
    public static final String FIELD_PO_CATEGORY = "poCategory";
    public static final String FIELD_PO_ORDER_TYPE = "poOrderType";
    public static final String FIELD_APPROVED_FLAG = "approvedFlag";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CURRENCY_CODE = "currencyCode";
    public static final String FIELD_PO_ENABLE_FLAG = "poEnableFlag";
    public static final String FIELD_TRANSFER_PLANT_CODE = "transferPlantCode";
    public static final String FIELD_LINE_NUM = "lineNum";
    public static final String FIELD_LINE_TYPE = "lineType";
    public static final String FIELD_SHIPMENT_NUM = "shipmentNum";
    public static final String FIELD_ITEM_CODE = "itemCode";
    public static final String FIELD_UOM = "uom";
    public static final String FIELD_UNIT_PRICE = "unitPrice";
    public static final String FIELD_LINE_DESCRIPTION = "lineDescription";
    public static final String FIELD_CONTRACT_LINE_NUM = "contractLineNum";
    public static final String FIELD_QUANTITY_ORDERED = "quantityOrdered";
    public static final String FIELD_QUANTITY_DELIVERED = "quantityDelivered";
    public static final String FIELD_NEED_DATE = "needDate";
    public static final String FIELD_RETURN_FLAG = "returnFlag";
    public static final String FIELD_COMPLETE_FLAG = "completeFlag";
    public static final String FIELD_LINE_ENABLE_FLAG = "lineEnableFlag";
    public static final String FIELD_PO_ATTRIBUTE1 = "poAttribute1";
    public static final String FIELD_PO_ATTRIBUTE2 = "poAttribute2";
    public static final String FIELD_PO_ATTRIBUTE3 = "poAttribute3";
    public static final String FIELD_PO_ATTRIBUTE4 = "poAttribute4";
    public static final String FIELD_PO_ATTRIBUTE5 = "poAttribute5";
    public static final String FIELD_PO_ATTRIBUTE6 = "poAttribute6";
    public static final String FIELD_PO_ATTRIBUTE7 = "poAttribute7";
    public static final String FIELD_PO_ATTRIBUTE8 = "poAttribute8";
    public static final String FIELD_PO_ATTRIBUTE9 = "poAttribute9";
    public static final String FIELD_PO_ATTRIBUTE10 = "poAttribute10";
    public static final String FIELD_PO_ATTRIBUTE11 = "poAttribute11";
    public static final String FIELD_PO_ATTRIBUTE12 = "poAttribute12";
    public static final String FIELD_PO_ATTRIBUTE13 = "poAttribute13";
    public static final String FIELD_PO_ATTRIBUTE14 = "poAttribute14";
    public static final String FIELD_PO_ATTRIBUTE15 = "poAttribute15";
    public static final String FIELD_LINE_ATTRIBUTE1 = "lineAttribute1";
    public static final String FIELD_LINE_ATTRIBUTE2 = "lineAttribute2";
    public static final String FIELD_LINE_ATTRIBUTE3 = "lineAttribute3";
    public static final String FIELD_LINE_ATTRIBUTE4 = "lineAttribute4";
    public static final String FIELD_LINE_ATTRIBUTE5 = "lineAttribute5";
    public static final String FIELD_LINE_ATTRIBUTE6 = "lineAttribute6";
    public static final String FIELD_LINE_ATTRIBUTE7 = "lineAttribute7";
    public static final String FIELD_LINE_ATTRIBUTE8 = "lineAttribute8";
    public static final String FIELD_LINE_ATTRIBUTE9 = "lineAttribute9";
    public static final String FIELD_LINE_ATTRIBUTE10 = "lineAttribute10";
    public static final String FIELD_LINE_ATTRIBUTE11 = "lineAttribute11";
    public static final String FIELD_LINE_ATTRIBUTE12 = "lineAttribute12";
    public static final String FIELD_LINE_ATTRIBUTE13 = "lineAttribute13";
    public static final String FIELD_LINE_ATTRIBUTE14 = "lineAttribute14";
    public static final String FIELD_LINE_ATTRIBUTE15 = "lineAttribute15";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 6591292166417707971L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    @Where
    private String ifaceId;
    @ApiModelProperty(value = "工厂代码", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "采购订单号", required = true)
    @NotBlank
    @Where
    private String poNumber;
    @ApiModelProperty(value = "合同或协议号")
    @Where
    private String contractNum;
    @ApiModelProperty(value = "供应商代码", required = true)
    @NotBlank
    @Where
    private String supplierCode;
    @ApiModelProperty(value = "供应商地点编码", required = true)
    @NotBlank
    @Where
    private String supplierSiteCode;
    @ApiModelProperty(value = "采购员名称（SAP采购组）", required = true)
    @NotBlank
    @Where
    private String buyerCode;
    @ApiModelProperty(value = "凭证类别（SAP凭证类别.F：采购订单，K：合同，L：计划协议）", required = true)
    @NotBlank
    @Where
    private String poCategory;
    @ApiModelProperty(value = "凭证类型（NB：标准订单，UB：转储订单）", required = true)
    @NotBlank
    @Where
    private String poOrderType;
    @ApiModelProperty(value = "审批标志（SAP需要根据状态转换为Y/N写入，由于SAP的审批状态各项目可以自定义代码，因此产品中无法根据审批代码处理）", required = true)
    @NotBlank
    @Where
    private String approvedFlag;
    @ApiModelProperty(value = "订单说明（SAP 参考信息）")
    @Where
    private String description;
    @ApiModelProperty(value = "币种")
    @Where
    private String currencyCode;
    @ApiModelProperty(value = "删除标识")
    @Where
    private String poEnableFlag;
    @ApiModelProperty(value = "转储采购订单的发出工厂")
    @Where
    private String transferPlantCode;
    @ApiModelProperty(value = "采购订单行号（SAP 行项目）", required = true)
    @NotNull
    @Where
    private String lineNum;
    @ApiModelProperty(value = "行类型（SAP项目类别。标准：空 寄售：K 标准外协:L 调拨:U)", required = true)
    @NotBlank
    @Where
    private String lineType;
    @ApiModelProperty(value = "计划行项目（如果是合同类型则没有计划行）")
    @Where
    private String shipmentNum;
    @ApiModelProperty(value = "物料编码", required = true)
    @NotNull
    @Where
    private String itemCode;
    @ApiModelProperty(value = "物料单位", required = true)
    @NotBlank
    @Where
    private String uom;
    @ApiModelProperty(value = "单价")
    @Where
    private Double unitPrice;
    @ApiModelProperty(value = "采购订单行说明")
    @Where
    private String lineDescription;
    @ApiModelProperty(value = "合同或协议行号（根据合同产生的订单计划行关联的合同或协议行号）")
    @Where
    private String contractLineNum;
    @ApiModelProperty(value = "计划行订单数量（合同类型的则写入行的数量)")
    @Where
    private Double quantityOrdered;
    @ApiModelProperty(value = "计划行交货数量")
    @Where
    private Double quantityDelivered;
    @ApiModelProperty(value = "计划交货交期（计划行的交期 ，没有计划行合同或协议则取行日期写入）", required = true)
    @NotNull
    @Where
    private Date needDate;
    @ApiModelProperty(value = "退货行标识")
    @Where
    private String returnFlag;
    @ApiModelProperty(value = "交货完成标志（可以理解为关闭）")
    @Where
    private String completeFlag;
    @ApiModelProperty(value = "删除标志")
    @Where
    private String lineEnableFlag;
    @ApiModelProperty(value = "采购订单头扩展字段")
    @Where
    private String poAttribute1;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute2;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute3;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute4;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute5;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute6;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute7;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute8;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute9;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute10;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute11;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute12;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute13;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute14;
    @ApiModelProperty(value = "")
    @Where
    private String poAttribute15;
    @ApiModelProperty(value = "采购订单行扩展字段")
    @Where
    private String lineAttribute1;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute2;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute3;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute4;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute5;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute6;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute7;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute8;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute9;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute10;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute11;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute12;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute13;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute14;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute15;

    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    @Where
    private Double batchId;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    @Where
    private String message;
    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------


    public static String getFieldTenantId() {
        return FIELD_TENANT_ID;
    }

    public static String getFieldIfaceId() {
        return FIELD_IFACE_ID;
    }

    public static String getFieldPlantCode() {
        return FIELD_PLANT_CODE;
    }

    public static String getFieldPoNumber() {
        return FIELD_PO_NUMBER;
    }

    public static String getFieldContractNum() {
        return FIELD_CONTRACT_NUM;
    }

    public static String getFieldSupplierCode() {
        return FIELD_SUPPLIER_CODE;
    }

    public static String getFieldSupplierSiteCode() {
        return FIELD_SUPPLIER_SITE_CODE;
    }

    public static String getFieldBuyerCode() {
        return FIELD_BUYER_CODE;
    }

    public static String getFieldPoCategory() {
        return FIELD_PO_CATEGORY;
    }

    public static String getFieldPoOrderType() {
        return FIELD_PO_ORDER_TYPE;
    }

    public static String getFieldApprovedFlag() {
        return FIELD_APPROVED_FLAG;
    }

    public static String getFieldDescription() {
        return FIELD_DESCRIPTION;
    }

    public static String getFieldCurrencyCode() {
        return FIELD_CURRENCY_CODE;
    }

    public static String getFieldPoEnableFlag() {
        return FIELD_PO_ENABLE_FLAG;
    }

    public static String getFieldTransferPlantCode() {
        return FIELD_TRANSFER_PLANT_CODE;
    }

    public static String getFieldLineNum() {
        return FIELD_LINE_NUM;
    }

    public static String getFieldLineType() {
        return FIELD_LINE_TYPE;
    }

    public static String getFieldShipmentNum() {
        return FIELD_SHIPMENT_NUM;
    }

    public static String getFieldItemCode() {
        return FIELD_ITEM_CODE;
    }

    public static String getFieldUom() {
        return FIELD_UOM;
    }

    public static String getFieldUnitPrice() {
        return FIELD_UNIT_PRICE;
    }

    public static String getFieldLineDescription() {
        return FIELD_LINE_DESCRIPTION;
    }

    public static String getFieldContractLineNum() {
        return FIELD_CONTRACT_LINE_NUM;
    }

    public static String getFieldQuantityOrdered() {
        return FIELD_QUANTITY_ORDERED;
    }

    public static String getFieldQuantityDelivered() {
        return FIELD_QUANTITY_DELIVERED;
    }

    public static String getFieldNeedDate() {
        return FIELD_NEED_DATE;
    }

    public static String getFieldReturnFlag() {
        return FIELD_RETURN_FLAG;
    }

    public static String getFieldCompleteFlag() {
        return FIELD_COMPLETE_FLAG;
    }

    public static String getFieldLineEnableFlag() {
        return FIELD_LINE_ENABLE_FLAG;
    }

    public static String getFieldPoAttribute1() {
        return FIELD_PO_ATTRIBUTE1;
    }

    public static String getFieldPoAttribute2() {
        return FIELD_PO_ATTRIBUTE2;
    }

    public static String getFieldPoAttribute3() {
        return FIELD_PO_ATTRIBUTE3;
    }

    public static String getFieldPoAttribute4() {
        return FIELD_PO_ATTRIBUTE4;
    }

    public static String getFieldPoAttribute5() {
        return FIELD_PO_ATTRIBUTE5;
    }

    public static String getFieldPoAttribute6() {
        return FIELD_PO_ATTRIBUTE6;
    }

    public static String getFieldPoAttribute7() {
        return FIELD_PO_ATTRIBUTE7;
    }

    public static String getFieldPoAttribute8() {
        return FIELD_PO_ATTRIBUTE8;
    }

    public static String getFieldPoAttribute9() {
        return FIELD_PO_ATTRIBUTE9;
    }

    public static String getFieldPoAttribute10() {
        return FIELD_PO_ATTRIBUTE10;
    }

    public static String getFieldPoAttribute11() {
        return FIELD_PO_ATTRIBUTE11;
    }

    public static String getFieldPoAttribute12() {
        return FIELD_PO_ATTRIBUTE12;
    }

    public static String getFieldPoAttribute13() {
        return FIELD_PO_ATTRIBUTE13;
    }

    public static String getFieldPoAttribute14() {
        return FIELD_PO_ATTRIBUTE14;
    }

    public static String getFieldPoAttribute15() {
        return FIELD_PO_ATTRIBUTE15;
    }

    public static String getFieldLineAttribute1() {
        return FIELD_LINE_ATTRIBUTE1;
    }

    public static String getFieldLineAttribute2() {
        return FIELD_LINE_ATTRIBUTE2;
    }

    public static String getFieldLineAttribute3() {
        return FIELD_LINE_ATTRIBUTE3;
    }

    public static String getFieldLineAttribute4() {
        return FIELD_LINE_ATTRIBUTE4;
    }

    public static String getFieldLineAttribute5() {
        return FIELD_LINE_ATTRIBUTE5;
    }

    public static String getFieldLineAttribute6() {
        return FIELD_LINE_ATTRIBUTE6;
    }

    public static String getFieldLineAttribute7() {
        return FIELD_LINE_ATTRIBUTE7;
    }

    public static String getFieldLineAttribute8() {
        return FIELD_LINE_ATTRIBUTE8;
    }

    public static String getFieldLineAttribute9() {
        return FIELD_LINE_ATTRIBUTE9;
    }

    public static String getFieldLineAttribute10() {
        return FIELD_LINE_ATTRIBUTE10;
    }

    public static String getFieldLineAttribute11() {
        return FIELD_LINE_ATTRIBUTE11;
    }

    public static String getFieldLineAttribute12() {
        return FIELD_LINE_ATTRIBUTE12;
    }

    public static String getFieldLineAttribute13() {
        return FIELD_LINE_ATTRIBUTE13;
    }

    public static String getFieldLineAttribute14() {
        return FIELD_LINE_ATTRIBUTE14;
    }

    public static String getFieldLineAttribute15() {
        return FIELD_LINE_ATTRIBUTE15;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getIfaceId() {
        return ifaceId;
    }

    public void setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierSiteCode() {
        return supplierSiteCode;
    }

    public void setSupplierSiteCode(String supplierSiteCode) {
        this.supplierSiteCode = supplierSiteCode;
    }

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    public String getPoCategory() {
        return poCategory;
    }

    public void setPoCategory(String poCategory) {
        this.poCategory = poCategory;
    }

    public String getPoOrderType() {
        return poOrderType;
    }

    public void setPoOrderType(String poOrderType) {
        this.poOrderType = poOrderType;
    }

    public String getApprovedFlag() {
        return approvedFlag;
    }

    public void setApprovedFlag(String approvedFlag) {
        this.approvedFlag = approvedFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPoEnableFlag() {
        return poEnableFlag;
    }

    public void setPoEnableFlag(String poEnableFlag) {
        this.poEnableFlag = poEnableFlag;
    }

    public String getTransferPlantCode() {
        return transferPlantCode;
    }

    public void setTransferPlantCode(String transferPlantCode) {
        this.transferPlantCode = transferPlantCode;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getShipmentNum() {
        return shipmentNum;
    }

    public void setShipmentNum(String shipmentNum) {
        this.shipmentNum = shipmentNum;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getLineDescription() {
        return lineDescription;
    }

    public void setLineDescription(String lineDescription) {
        this.lineDescription = lineDescription;
    }

    public String getContractLineNum() {
        return contractLineNum;
    }

    public void setContractLineNum(String contractLineNum) {
        this.contractLineNum = contractLineNum;
    }

    public Double getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(Double quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public Double getQuantityDelivered() {
        return quantityDelivered;
    }

    public void setQuantityDelivered(Double quantityDelivered) {
        this.quantityDelivered = quantityDelivered;
    }

    public Date getNeedDate() {
        if (needDate != null) {
            return (Date) needDate.clone();
        } else {
            return null;
        }
    }

    public void setNeedDate(Date needDate) {
        if (needDate == null) {
            this.needDate = null;
        } else {
            this.needDate = (Date) needDate.clone();
        }
    }

    public String getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }

    public String getCompleteFlag() {
        return completeFlag;
    }

    public void setCompleteFlag(String completeFlag) {
        this.completeFlag = completeFlag;
    }

    public String getLineEnableFlag() {
        return lineEnableFlag;
    }

    public void setLineEnableFlag(String lineEnableFlag) {
        this.lineEnableFlag = lineEnableFlag;
    }

    public String getPoAttribute1() {
        return poAttribute1;
    }

    public void setPoAttribute1(String poAttribute1) {
        this.poAttribute1 = poAttribute1;
    }

    public String getPoAttribute2() {
        return poAttribute2;
    }

    public void setPoAttribute2(String poAttribute2) {
        this.poAttribute2 = poAttribute2;
    }

    public String getPoAttribute3() {
        return poAttribute3;
    }

    public void setPoAttribute3(String poAttribute3) {
        this.poAttribute3 = poAttribute3;
    }

    public String getPoAttribute4() {
        return poAttribute4;
    }

    public void setPoAttribute4(String poAttribute4) {
        this.poAttribute4 = poAttribute4;
    }

    public String getPoAttribute5() {
        return poAttribute5;
    }

    public void setPoAttribute5(String poAttribute5) {
        this.poAttribute5 = poAttribute5;
    }

    public String getPoAttribute6() {
        return poAttribute6;
    }

    public void setPoAttribute6(String poAttribute6) {
        this.poAttribute6 = poAttribute6;
    }

    public String getPoAttribute7() {
        return poAttribute7;
    }

    public void setPoAttribute7(String poAttribute7) {
        this.poAttribute7 = poAttribute7;
    }

    public String getPoAttribute8() {
        return poAttribute8;
    }

    public void setPoAttribute8(String poAttribute8) {
        this.poAttribute8 = poAttribute8;
    }

    public String getPoAttribute9() {
        return poAttribute9;
    }

    public void setPoAttribute9(String poAttribute9) {
        this.poAttribute9 = poAttribute9;
    }

    public String getPoAttribute10() {
        return poAttribute10;
    }

    public void setPoAttribute10(String poAttribute10) {
        this.poAttribute10 = poAttribute10;
    }

    public String getPoAttribute11() {
        return poAttribute11;
    }

    public void setPoAttribute11(String poAttribute11) {
        this.poAttribute11 = poAttribute11;
    }

    public String getPoAttribute12() {
        return poAttribute12;
    }

    public void setPoAttribute12(String poAttribute12) {
        this.poAttribute12 = poAttribute12;
    }

    public String getPoAttribute13() {
        return poAttribute13;
    }

    public void setPoAttribute13(String poAttribute13) {
        this.poAttribute13 = poAttribute13;
    }

    public String getPoAttribute14() {
        return poAttribute14;
    }

    public void setPoAttribute14(String poAttribute14) {
        this.poAttribute14 = poAttribute14;
    }

    public String getPoAttribute15() {
        return poAttribute15;
    }

    public void setPoAttribute15(String poAttribute15) {
        this.poAttribute15 = poAttribute15;
    }

    public String getLineAttribute1() {
        return lineAttribute1;
    }

    public void setLineAttribute1(String lineAttribute1) {
        this.lineAttribute1 = lineAttribute1;
    }

    public String getLineAttribute2() {
        return lineAttribute2;
    }

    public void setLineAttribute2(String lineAttribute2) {
        this.lineAttribute2 = lineAttribute2;
    }

    public String getLineAttribute3() {
        return lineAttribute3;
    }

    public void setLineAttribute3(String lineAttribute3) {
        this.lineAttribute3 = lineAttribute3;
    }

    public String getLineAttribute4() {
        return lineAttribute4;
    }

    public void setLineAttribute4(String lineAttribute4) {
        this.lineAttribute4 = lineAttribute4;
    }

    public String getLineAttribute5() {
        return lineAttribute5;
    }

    public void setLineAttribute5(String lineAttribute5) {
        this.lineAttribute5 = lineAttribute5;
    }

    public String getLineAttribute6() {
        return lineAttribute6;
    }

    public void setLineAttribute6(String lineAttribute6) {
        this.lineAttribute6 = lineAttribute6;
    }

    public String getLineAttribute7() {
        return lineAttribute7;
    }

    public void setLineAttribute7(String lineAttribute7) {
        this.lineAttribute7 = lineAttribute7;
    }

    public String getLineAttribute8() {
        return lineAttribute8;
    }

    public void setLineAttribute8(String lineAttribute8) {
        this.lineAttribute8 = lineAttribute8;
    }

    public String getLineAttribute9() {
        return lineAttribute9;
    }

    public void setLineAttribute9(String lineAttribute9) {
        this.lineAttribute9 = lineAttribute9;
    }

    public String getLineAttribute10() {
        return lineAttribute10;
    }

    public void setLineAttribute10(String lineAttribute10) {
        this.lineAttribute10 = lineAttribute10;
    }

    public String getLineAttribute11() {
        return lineAttribute11;
    }

    public void setLineAttribute11(String lineAttribute11) {
        this.lineAttribute11 = lineAttribute11;
    }

    public String getLineAttribute12() {
        return lineAttribute12;
    }

    public void setLineAttribute12(String lineAttribute12) {
        this.lineAttribute12 = lineAttribute12;
    }

    public String getLineAttribute13() {
        return lineAttribute13;
    }

    public void setLineAttribute13(String lineAttribute13) {
        this.lineAttribute13 = lineAttribute13;
    }

    public String getLineAttribute14() {
        return lineAttribute14;
    }

    public void setLineAttribute14(String lineAttribute14) {
        this.lineAttribute14 = lineAttribute14;
    }

    public String getLineAttribute15() {
        return lineAttribute15;
    }

    public void setLineAttribute15(String lineAttribute15) {
        this.lineAttribute15 = lineAttribute15;
    }

    public Double getBatchId() {
        return batchId;
    }

    public void setBatchId(Double batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtSapPoIface that = (MtSapPoIface) o;
        return Objects.equals(getTenantId(), that.getTenantId()) && Objects.equals(getIfaceId(), that.getIfaceId())
                        && Objects.equals(getPlantCode(), that.getPlantCode())
                        && Objects.equals(getPoNumber(), that.getPoNumber())
                        && Objects.equals(getContractNum(), that.getContractNum())
                        && Objects.equals(getSupplierCode(), that.getSupplierCode())
                        && Objects.equals(getSupplierSiteCode(), that.getSupplierSiteCode())
                        && Objects.equals(getBuyerCode(), that.getBuyerCode())
                        && Objects.equals(getPoCategory(), that.getPoCategory())
                        && Objects.equals(getPoOrderType(), that.getPoOrderType())
                        && Objects.equals(getApprovedFlag(), that.getApprovedFlag())
                        && Objects.equals(getDescription(), that.getDescription())
                        && Objects.equals(getCurrencyCode(), that.getCurrencyCode())
                        && Objects.equals(getPoEnableFlag(), that.getPoEnableFlag())
                        && Objects.equals(getTransferPlantCode(), that.getTransferPlantCode())
                        && Objects.equals(getLineNum(), that.getLineNum())
                        && Objects.equals(getLineType(), that.getLineType())
                        && Objects.equals(getShipmentNum(), that.getShipmentNum())
                        && Objects.equals(getItemCode(), that.getItemCode()) && Objects.equals(getUom(), that.getUom())
                        && Objects.equals(getUnitPrice(), that.getUnitPrice())
                        && Objects.equals(getLineDescription(), that.getLineDescription())
                        && Objects.equals(getContractLineNum(), that.getContractLineNum())
                        && Objects.equals(getQuantityOrdered(), that.getQuantityOrdered())
                        && Objects.equals(getQuantityDelivered(), that.getQuantityDelivered())
                        && Objects.equals(getNeedDate(), that.getNeedDate())
                        && Objects.equals(getReturnFlag(), that.getReturnFlag())
                        && Objects.equals(getCompleteFlag(), that.getCompleteFlag())
                        && Objects.equals(getLineEnableFlag(), that.getLineEnableFlag())
                        && Objects.equals(getPoAttribute1(), that.getPoAttribute1())
                        && Objects.equals(getPoAttribute2(), that.getPoAttribute2())
                        && Objects.equals(getPoAttribute3(), that.getPoAttribute3())
                        && Objects.equals(getPoAttribute4(), that.getPoAttribute4())
                        && Objects.equals(getPoAttribute5(), that.getPoAttribute5())
                        && Objects.equals(getPoAttribute6(), that.getPoAttribute6())
                        && Objects.equals(getPoAttribute7(), that.getPoAttribute7())
                        && Objects.equals(getPoAttribute8(), that.getPoAttribute8())
                        && Objects.equals(getPoAttribute9(), that.getPoAttribute9())
                        && Objects.equals(getPoAttribute10(), that.getPoAttribute10())
                        && Objects.equals(getPoAttribute11(), that.getPoAttribute11())
                        && Objects.equals(getPoAttribute12(), that.getPoAttribute12())
                        && Objects.equals(getPoAttribute13(), that.getPoAttribute13())
                        && Objects.equals(getPoAttribute14(), that.getPoAttribute14())
                        && Objects.equals(getPoAttribute15(), that.getPoAttribute15())
                        && Objects.equals(getLineAttribute1(), that.getLineAttribute1())
                        && Objects.equals(getLineAttribute2(), that.getLineAttribute2())
                        && Objects.equals(getLineAttribute3(), that.getLineAttribute3())
                        && Objects.equals(getLineAttribute4(), that.getLineAttribute4())
                        && Objects.equals(getLineAttribute5(), that.getLineAttribute5())
                        && Objects.equals(getLineAttribute6(), that.getLineAttribute6())
                        && Objects.equals(getLineAttribute7(), that.getLineAttribute7())
                        && Objects.equals(getLineAttribute8(), that.getLineAttribute8())
                        && Objects.equals(getLineAttribute9(), that.getLineAttribute9())
                        && Objects.equals(getLineAttribute10(), that.getLineAttribute10())
                        && Objects.equals(getLineAttribute11(), that.getLineAttribute11())
                        && Objects.equals(getLineAttribute12(), that.getLineAttribute12())
                        && Objects.equals(getLineAttribute13(), that.getLineAttribute13())
                        && Objects.equals(getLineAttribute14(), that.getLineAttribute14())
                        && Objects.equals(getLineAttribute15(), that.getLineAttribute15())
                        && Objects.equals(getBatchId(), that.getBatchId())
                        && Objects.equals(getStatus(), that.getStatus())
                        && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getCid(), that.getCid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenantId(), getIfaceId(), getPlantCode(), getPoNumber(), getContractNum(),
                        getSupplierCode(), getSupplierSiteCode(), getBuyerCode(), getPoCategory(), getPoOrderType(),
                        getApprovedFlag(), getDescription(), getCurrencyCode(), getPoEnableFlag(),
                        getTransferPlantCode(), getLineNum(), getLineType(), getShipmentNum(), getItemCode(), getUom(),
                        getUnitPrice(), getLineDescription(), getContractLineNum(), getQuantityOrdered(),
                        getQuantityDelivered(), getNeedDate(), getReturnFlag(), getCompleteFlag(), getLineEnableFlag(),
                        getPoAttribute1(), getPoAttribute2(), getPoAttribute3(), getPoAttribute4(), getPoAttribute5(),
                        getPoAttribute6(), getPoAttribute7(), getPoAttribute8(), getPoAttribute9(), getPoAttribute10(),
                        getPoAttribute11(), getPoAttribute12(), getPoAttribute13(), getPoAttribute14(),
                        getPoAttribute15(), getLineAttribute1(), getLineAttribute2(), getLineAttribute3(),
                        getLineAttribute4(), getLineAttribute5(), getLineAttribute6(), getLineAttribute7(),
                        getLineAttribute8(), getLineAttribute9(), getLineAttribute10(), getLineAttribute11(),
                        getLineAttribute12(), getLineAttribute13(), getLineAttribute14(), getLineAttribute15(),
                        getBatchId(), getStatus(), getMessage(), getCid());
    }
}
