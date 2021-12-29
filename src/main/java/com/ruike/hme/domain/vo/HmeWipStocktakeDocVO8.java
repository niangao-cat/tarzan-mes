package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/16 14:58
 */
@Data
@ExcelSheet(zh = "COS盘点")
public class HmeWipStocktakeDocVO8 implements Serializable {

    private static final long serialVersionUID = -7504680765438468536L;

    @ApiModelProperty(value = "盘点单号")
    @ExcelColumn(zh = "盘点单号",order = 0)
    private String stocktakeNum;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号",order = 1)
    private String workOrderNum;

    @ApiModelProperty(value = "BOM编码")
    @ExcelColumn(zh = "BOM编码",order = 2)
    private String productionVersion;

    @ApiModelProperty(value = "产品编码")
    @ExcelColumn(zh = "产品编码",order = 3)
    private String materialCode;

    @ApiModelProperty(value = "产品名称")
    @ExcelColumn(zh = "产品名称",order = 4)
    private String materialName;

    @ApiModelProperty(value = "物料组")
    @ExcelColumn(zh = "物料组",order = 5)
    private String itemGroup;

    @ApiModelProperty(value = "产线编码")
    @ExcelColumn(zh = "产线编码",order = 6)
    private String prodLineCode;

    @ApiModelProperty(value = "产线名称")
    @ExcelColumn(zh = "产线名称",order = 7)
    private String prodLineName;

    @ApiModelProperty(value = "仓库")
    @ExcelColumn(zh = "仓库",order = 8)
    private String warehouseCode;

    @ApiModelProperty(value = "货位")
    @ExcelColumn(zh = "货位",order = 9)
    private String locatorCode;

    @ApiModelProperty(value = "条码")
    @ExcelColumn(zh = "条码",order = 10)
    private String materialLotCode;

    @ApiModelProperty(value = "位置行")
    private Long loadRow;

    @ApiModelProperty(value = "位置列")
    private Long loadColumn;

    @ApiModelProperty(value = "位置")
    @ExcelColumn(zh = "位置",order = 11)
    private String position;

    @ApiModelProperty(value = "WAFER")
    @ExcelColumn(zh = "WAFER",order = 12)
    private String wafer;

    @ApiModelProperty(value = "热沉编码")
    @ExcelColumn(zh = "热沉编码",order = 13)
    private String hotSinkCode;

    @ApiModelProperty(value = "账面数量")
    @ExcelColumn(zh = "账面数量",order = 14)
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "单位")
    @ExcelColumn(zh = "单位",order = 15)
    private String uomName;

    @ApiModelProperty(value = "初盘工序")
    @ExcelColumn(zh = "初盘工序",order = 16)
    private String firstWorkcellCode;

    @ApiModelProperty(value = "初盘数量")
    @ExcelColumn(zh = "初盘数量",order = 17)
    private BigDecimal firstcountQuantity;

    @ApiModelProperty(value = "复盘工序")
    @ExcelColumn(zh = "复盘工序",order = 18)
    private String reworkcellCode;

    @ApiModelProperty(value = "复盘数量")
    @ExcelColumn(zh = "复盘数量",order = 19)
    private BigDecimal recountQuantity;

    @ApiModelProperty(value = "复盘差异")
    @ExcelColumn(zh = "复盘差异",order = 20)
    private BigDecimal diffQty;
}
