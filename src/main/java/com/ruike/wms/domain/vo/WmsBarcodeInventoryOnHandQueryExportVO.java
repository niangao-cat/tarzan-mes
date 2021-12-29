package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

/**
 * @description 条码库存现有量查询 导出
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/13
 * @time 11:17
 * @version 0.0.1
 * @return
 */
@Data
@ExcelSheet(zh = "条码库存现有量查询")
public class WmsBarcodeInventoryOnHandQueryExportVO implements Serializable {
    private static final long serialVersionUID = -6496026128372419979L;

    @ApiModelProperty(value = "站点")
    @ExcelColumn(zh = "站点",order = 1)
    private String siteCode;

    @ApiModelProperty(value = "仓库")
    @ExcelColumn(zh = "仓库",order = 2)
    private String warehouseCode;

    @ApiModelProperty(value = "仓库描述")
    @ExcelColumn(zh = "仓库描述",order = 3)
    private String warehouseName;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 4)
    private String materialCode;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本",order = 5)
    private String materialVersion;

    @ApiModelProperty(value = "货位")
    @ExcelColumn(zh = "货位",order = 6)
    private String locatorCode;

    @ApiModelProperty(value = "货位描述")
    @ExcelColumn(zh = "货位描述",order = 7)
    private String locatorName;

    @ApiModelProperty(value = "销售订单号")
    @ExcelColumn(zh = "销售订单号",order = 8)
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    @ExcelColumn(zh = "销售订单行号",order = 9)
    private String soLineNum;

    @ApiModelProperty(value = "批次")
    @ExcelColumn(zh = "批次",order = 10)
    private String lot;

    @ApiModelProperty(value = "单位")
    @ExcelColumn(zh = "单位",order = 11)
    private String uomCode;

    @ApiModelProperty(value = "数量")
    @ExcelColumn(zh = "数量",order = 12)
    private Long qty;

}
