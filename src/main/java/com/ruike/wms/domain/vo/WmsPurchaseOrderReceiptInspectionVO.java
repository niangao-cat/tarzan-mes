package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description
 *
 * @author li.zhang 2021/09/09 13:45
 */
@Data
@ExcelSheet(title = "采购订单接收检验统计报表")
public class WmsPurchaseOrderReceiptInspectionVO implements Serializable {

    private static final long serialVersionUID = 5454474758547250395L;

    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("工厂编码")
    @ExcelColumn(title = "工厂编码", order = 0)
    private String siteCode;
    @ApiModelProperty("采购订单Id")
    private String instructionDocId;
    @ApiModelProperty("采购订单编号")
    @ExcelColumn(title = "采购订单编号", order = 1)
    private String instructionDocNum;
    @ApiModelProperty("采购订单供应商Id")
    private String supplierId;
    @ApiModelProperty("采购订单供应商编码")
    @ExcelColumn(title = "供应商编码", order = 2)
    private String supplierCode;
    @ApiModelProperty("采购订单供应商描述")
    @ExcelColumn(title = "供应商描述", order = 3)
    private String supplierName;
    @ApiModelProperty("采购订单行Id")
    private String instructionId;
    @ApiModelProperty("采购订单行号")
    @ExcelColumn(title = "采购订单行号", order = 4)
    private String instructionLineNum;
    @ApiModelProperty("采购订单行物料Id")
    private String materialId;
    @ApiModelProperty("采购订单行物料编码")
    @ExcelColumn(title = "物料编码", order = 5)
    private String materialCode;
    @ApiModelProperty("采购订单行物料描述")
    @ExcelColumn(title = "物料描述", order = 6)
    private String materialName;
    @ApiModelProperty("计划到货时间")
    @ExcelColumn(title = "计划到货时间", order = 7)
    private String demandTime;
    @ApiModelProperty("送货单Id")
    private String deliveryInstructionDocId;
    @ApiModelProperty("送货单")
    @ExcelColumn(title = "送货单", order = 8)
    private String deliveryInstructionDocNum;
    @ApiModelProperty("送货单状态")
    @LovValue(value = "WMS.DELIVERY_DOC.STATUS", meaningField = "deliveryInstructionDocStatusMeaning")
    private String deliveryInstructionDocStatus;
    @ApiModelProperty("送货单状态意义")
    @ExcelColumn(title = "送货单状态", order = 9)
    private String deliveryInstructionDocStatusMeaning;
    @ApiModelProperty("送货单行")
    @ExcelColumn(title = "送货单行", order = 10)
    private String deliveryInstructionLineNum;
    @ApiModelProperty("送货单行状态")
    private String deliveryInstructionLineStatus;
    @ApiModelProperty("送货单行状态意义")
    @ExcelColumn(title = "送货单行状态", order = 11)
    private String deliveryInstructionLineStatusMeaning;
    @ApiModelProperty("到货数量")
    @ExcelColumn(title = "到货数量", order = 12)
    private BigDecimal receivedQty;
    @ApiModelProperty("接收时间")
    @ExcelColumn(title = "接收时间", order = 13)
    private String actualReceivedDate;
    @ApiModelProperty("是否及时到货")
    @ExcelColumn(title = "是否及时到货", order = 14)
    private String receivedFlag ;
    @ApiModelProperty("检验合格数量")
    @ExcelColumn(title = "检验合格数量", order = 15)
    private BigDecimal poStockInQty;
    @ApiModelProperty("来料合格率")
    @ExcelColumn(title = "来料合格率", order = 16)
    private String qualificationRate;

}
