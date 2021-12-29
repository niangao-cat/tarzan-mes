package com.ruike.itf.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 事务汇总接口表
 *
 * @author yonghui.zhu@hand-china.com 2020-08-11 09:44:09
 */
@ApiModel("事务汇总接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_object_transaction_iface")
@CustomPrimary
@Data
public class ItfObjectTransactionIface extends AuditDomain {

    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_MERGE_ID = "mergeId";
    public static final String FIELD_SYSTEM = "system";
    public static final String FIELD_TRANSACTION_TYPE_CODE = "transactionTypeCode";
    public static final String FIELD_MOVE_TYPE = "moveType";
    public static final String FIELD_MOVE_REASON = "moveReason";
    public static final String FIELD_ACCOUNT_DATE = "accountDate";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_TRANSACTION_TIME = "transactionTime";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_TRANSACTION_QTY = "transactionQty";
    public static final String FIELD_LOT_NUMBER = "lotNumber";
    public static final String FIELD_TRANSACTION_UOM = "transactionUom";
    public static final String FIELD_WAREHOUSE_CODE = "warehouseCode";
    public static final String FIELD_LOCATOR_CODE = "locatorCode";
    public static final String FIELD_TRANSFER_PLANT_CODE = "transferPlantCode";
    public static final String FIELD_TRANSFER_WAREHOUSE_CODE = "transferWarehouseCode";
    public static final String FIELD_TRANSFER_LOCATOR_CODE = "transferLocatorCode";
    public static final String FIELD_COSTCENTER_CODE = "costcenterCode";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_SUPPLIER_SITE_CODE = "supplierSiteCode";
    public static final String FIELD_CUSTOMER_CODE = "customerCode";
    public static final String FIELD_CUSTOMER_SITE_CODE = "customerSiteCode";
    public static final String FIELD_SOURCE_DOC_TYPE = "sourceDocType";
    public static final String FIELD_SOURCE_DOC_NUM = "sourceDocNum";
    public static final String FIELD_SOURCE_DOC_LINE_NUM = "sourceDocLineNum";
    public static final String FIELD_WORK_ORDER_NUM = "workOrderNum";
    public static final String FIELD_OPERATION_SEQUENCE = "operationSequence";
    public static final String FIELD_BOM_RESERVE_NUM = "bomReserveNum";
    public static final String FIELD_BOM_RESERVE_LINE_NUM = "bomReserveLineNum";
    public static final String FIELD_COMPLETE_FLAG = "completeFlag";
    public static final String FIELD_PROD_LINE_CODE = "prodLineCode";
    public static final String FIELD_DELIVERY_BATCH = "deliveryBatch";
    public static final String FIELD_CONTAINER_CODE = "containerCode";
    public static final String FIELD_CONTAINER_TYPE_CODE = "containerTypeCode";
    public static final String FIELD_SO_NUM = "soNum";
    public static final String FIELD_SO_LINE_NUM = "soLineNum";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_INSIDE_ORDER = "insideOrder";
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
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";
    public static final String FIELD_ATTRIBUTE16 = "attribute16";
    public static final String FIELD_ATTRIBUTE17 = "attribute17";
    public static final String FIELD_ATTRIBUTE18 = "attribute18";
    public static final String FIELD_ATTRIBUTE19 = "attribute19";
    public static final String FIELD_ATTRIBUTE20 = "attribute20";
    public static final String FIELD_ATTRIBUTE21 = "attribute21";
    public static final String FIELD_ATTRIBUTE22 = "attribute22";
    public static final String FIELD_ATTRIBUTE23 = "attribute23";
    public static final String FIELD_ATTRIBUTE24 = "attribute24";
    public static final String FIELD_ATTRIBUTE25 = "attribute25";
    public static final String FIELD_ATTRIBUTE26 = "attribute26";
    public static final String FIELD_ATTRIBUTE27 = "attribute27";
    public static final String FIELD_ATTRIBUTE28 = "attribute28";
    public static final String FIELD_ATTRIBUTE29 = "attribute29";
    public static final String FIELD_ATTRIBUTE30 = "attribute30";
    public static final String FIELD_TRANSFER_SO_LINE_NUM = "transferSoLineNum";
    public static final String FIELD_TRANSFER_LOT_NUMBER = "transferLotNumber";
    public static final String FIELD_SPEC_STOCK_FLAG = "specStockFlag";
    public static final String FIELD_PO_NUM = "poNum";
    public static final String FIELD_TRANSFER_SO_NUM = "transferSoNum";
    public static final String FIELD_GMCODE = "gmcode";
    public static final String FIELD_PO_LINE_NUM = "poLineNum";
    public static final String FIELD_BACK_FLAG = "backFlag";
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
    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("接口表ID，主键")
    @Id
    private String ifaceId;
    @ApiModelProperty(value = "接口汇总ID，此为调用ERP接口时传入的汇总ID")
    private String mergeId;
    @ApiModelProperty(value = "逻辑系统，")
    private String system;
    @ApiModelProperty(value = "事务类型编码")
    private String transactionTypeCode;
    @ApiModelProperty(value = "移动类型")
    private String moveType;
    @ApiModelProperty(value = "移动原因")
    private String moveReason;
    @ApiModelProperty(value = "记账时间")
    private Date accountDate;
    @ApiModelProperty(value = "工厂编码（来源工厂）")
    private String plantCode;
    @ApiModelProperty(value = "事务时间")
    private Date transactionTime;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "事务汇总数量")
    private BigDecimal transactionQty;
    @ApiModelProperty(value = "事务批次")
    private String lotNumber;
    @ApiModelProperty(value = "事务单位")
    private String transactionUom;
    @ApiModelProperty(value = "仓库（来源仓库）")
    private String warehouseCode;
    @ApiModelProperty(value = "货位（来源货位）")
    private String locatorCode;
    @ApiModelProperty(value = "目标工厂")
    private String transferPlantCode;
    @ApiModelProperty(value = "目标仓库")
    private String transferWarehouseCode;
    @ApiModelProperty(value = "目标货位")
    private String transferLocatorCode;
    @ApiModelProperty(value = "成本中心编码")
    private String costcenterCode;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商地点编码")
    private String supplierSiteCode;
    @ApiModelProperty(value = "客户编码")
    private String customerCode;
    @ApiModelProperty(value = "客户地点编码")
    private String customerSiteCode;
    @ApiModelProperty(value = "来源单据类型")
    private String sourceDocType;
    @ApiModelProperty(value = "来源单据号")
    private String sourceDocNum;
    @ApiModelProperty(value = "来源单据行号")
    private String sourceDocLineNum;
    @ApiModelProperty(value = "工单号/内部订单号")
    private String workOrderNum;
    @ApiModelProperty(value = "工序")
    private String operationSequence;
    @ApiModelProperty(value = "预留/相关需求的编号")
    private String bomReserveNum;
    @ApiModelProperty(value = "预留/相关需求的项目编号")
    private String bomReserveLineNum;
    @ApiModelProperty(value = "完成标识")
    private String completeFlag;
    @ApiModelProperty(value = "生产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "接收批（山蒲业务字段）")
    private String deliveryBatch;
    @ApiModelProperty(value = "容器编码")
    private String containerCode;
    @ApiModelProperty(value = "容器类型编码")
    private String containerTypeCode;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "SN号")
    private String snNum;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "处理时间", required = true)
    @NotNull
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)", required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty(value = "内部订单号")
    private String insideOrder;
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
    @ApiModelProperty(value = "")
    private String attribute16;
    @ApiModelProperty(value = "")
    private String attribute17;
    @ApiModelProperty(value = "")
    private String attribute18;
    @ApiModelProperty(value = "")
    private String attribute19;
    @ApiModelProperty(value = "")
    private String attribute20;
    @ApiModelProperty(value = "")
    private String attribute21;
    @ApiModelProperty(value = "")
    private String attribute22;
    @ApiModelProperty(value = "")
    private String attribute23;
    @ApiModelProperty(value = "")
    private String attribute24;
    @ApiModelProperty(value = "")
    private String attribute25;
    @ApiModelProperty(value = "")
    private String attribute26;
    @ApiModelProperty(value = "")
    private String attribute27;
    @ApiModelProperty(value = "")
    private String attribute28;
    @ApiModelProperty(value = "")
    private String attribute29;
    @ApiModelProperty(value = "")
    private String attribute30;
    @ApiModelProperty(value = "目标销售订单行")
    private String transferSoLineNum;
    @ApiModelProperty(value = "目标批次")
    private String transferLotNumber;
    @ApiModelProperty(value = "特殊库存，销售订单库存")
    private String specStockFlag;
    @ApiModelProperty(value = "采购订单头")
    private String poNum;
    @ApiModelProperty(value = "目标销售订单")
    private String transferSoNum;
    @ApiModelProperty(value = "SAP移动事物分配代码")
    private String gmcode;
    @ApiModelProperty(value = "采购订单行")
    private String poLineNum;
    @ApiModelProperty(value = "冲销标识")
    private String backFlag;
    @ApiModelProperty(value = "SAP字段-VGE01、准备时间单位")
    private String lineAttribute1;
    @ApiModelProperty(value = "SAP字段-VGW01：准备时间")
    private String lineAttribute2;
    @ApiModelProperty(value = "SAP字段-VGE02：排产时间单位")
    private String lineAttribute3;
    @ApiModelProperty(value = "SAP字段-VGW02：排产时间")
    private String lineAttribute4;
    @ApiModelProperty(value = "SAP字段-VGE03：定额时间单位")
    private String lineAttribute5;
    @ApiModelProperty(value = "SAP字段-VGW03：定额时间")
    private String lineAttribute6;
    @ApiModelProperty(value = "SAP字段-VGE04：动能时间单位")
    private String lineAttribute7;
    @ApiModelProperty(value = "SAP字段-VGW04：动能时间")
    private String lineAttribute8;
    @ApiModelProperty(value = "SAP字段-VGE05：人工时间单位")
    private String lineAttribute9;
    @ApiModelProperty(value = "SAP字段-VGW05：人工时间")
    private String lineAttribute10;
    @ApiModelProperty(value = "")
    private String lineAttribute11;
    @ApiModelProperty(value = "")
    private String lineAttribute12;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("工单物料编码")
    @Transient
    private String woMaterialCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItfObjectTransactionIface iface = (ItfObjectTransactionIface) o;
        return Objects.equals(system, iface.system) &&
                Objects.equals(transactionTypeCode, iface.transactionTypeCode) &&
                Objects.equals(moveType, iface.moveType) &&
                Objects.equals(moveReason, iface.moveReason) &&
                Objects.equals(accountDate, iface.accountDate) &&
                Objects.equals(plantCode, iface.plantCode) &&
                Objects.equals(transactionTime, iface.transactionTime) &&
                Objects.equals(materialCode, iface.materialCode) &&
                Objects.equals(transactionQty, iface.transactionQty) &&
                Objects.equals(lotNumber, iface.lotNumber) &&
                Objects.equals(transactionUom, iface.transactionUom) &&
                Objects.equals(warehouseCode, iface.warehouseCode) &&
                Objects.equals(locatorCode, iface.locatorCode) &&
                Objects.equals(transferPlantCode, iface.transferPlantCode) &&
                Objects.equals(transferWarehouseCode, iface.transferWarehouseCode) &&
                Objects.equals(transferLocatorCode, iface.transferLocatorCode) &&
                Objects.equals(costcenterCode, iface.costcenterCode) &&
                Objects.equals(supplierCode, iface.supplierCode) &&
                Objects.equals(supplierSiteCode, iface.supplierSiteCode) &&
                Objects.equals(customerCode, iface.customerCode) &&
                Objects.equals(customerSiteCode, iface.customerSiteCode) &&
                Objects.equals(sourceDocType, iface.sourceDocType) &&
                Objects.equals(sourceDocNum, iface.sourceDocNum) &&
                Objects.equals(sourceDocLineNum, iface.sourceDocLineNum) &&
                Objects.equals(workOrderNum, iface.workOrderNum) &&
                Objects.equals(operationSequence, iface.operationSequence) &&
                Objects.equals(bomReserveNum, iface.bomReserveNum) &&
                Objects.equals(bomReserveLineNum, iface.bomReserveLineNum) &&
                Objects.equals(completeFlag, iface.completeFlag) &&
                Objects.equals(prodLineCode, iface.prodLineCode) &&
                Objects.equals(deliveryBatch, iface.deliveryBatch) &&
                Objects.equals(containerCode, iface.containerCode) &&
                Objects.equals(containerTypeCode, iface.containerTypeCode) &&
                Objects.equals(soNum, iface.soNum) &&
                Objects.equals(soLineNum, iface.soLineNum) &&
                Objects.equals(snNum, iface.snNum) &&
                Objects.equals(remark, iface.remark) &&
                Objects.equals(tenantId, iface.tenantId) &&
                Objects.equals(processDate, iface.processDate) &&
                Objects.equals(processMessage, iface.processMessage) &&
                Objects.equals(processStatus, iface.processStatus) &&
                Objects.equals(transferSoLineNum, iface.transferSoLineNum) &&
                Objects.equals(transferLotNumber, iface.transferLotNumber) &&
                Objects.equals(specStockFlag, iface.specStockFlag) &&
                Objects.equals(poNum, iface.poNum) &&
                Objects.equals(transferSoNum, iface.transferSoNum) &&
                Objects.equals(gmcode, iface.gmcode) &&
                Objects.equals(poLineNum, iface.poLineNum) &&
                Objects.equals(backFlag, iface.backFlag) &&
                Objects.equals(woMaterialCode, iface.woMaterialCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(system, transactionTypeCode, moveType, moveReason, accountDate, plantCode, transactionTime,
                materialCode, transactionQty, lotNumber, transactionUom, warehouseCode, locatorCode, transferPlantCode,
                transferWarehouseCode, transferLocatorCode, costcenterCode, supplierCode, supplierSiteCode, customerCode,
                customerSiteCode, sourceDocType, sourceDocNum, sourceDocLineNum, workOrderNum, operationSequence,
                bomReserveNum, bomReserveLineNum, completeFlag, prodLineCode, deliveryBatch, containerCode, containerTypeCode,
                soNum, soLineNum, snNum, remark, processDate, processMessage, processStatus, tenantId, transferSoLineNum,
                transferLotNumber, specStockFlag, poNum, transferSoNum, gmcode, poLineNum, backFlag, woMaterialCode);
    }
}
