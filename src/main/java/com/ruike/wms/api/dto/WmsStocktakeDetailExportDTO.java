package com.ruike.wms.api.dto;

import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;

/**
 * <p>
 * 盘点明细导出
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/19 20:12
 */
@Data
@ExcelSheet(title = "盘点明细")
public class WmsStocktakeDetailExportDTO {
    @ExcelColumn(title = "物料编码")
    private String materialCode;
    @ExcelColumn(title = "物料名称")
    private String materialName;
    @ExcelColumn(title = "批次")
    private String lotCode;
    @ExcelColumn(title = "货位")
    private String locatorCode;
    @ExcelColumn(title = "账面数量")
    private BigDecimal currentQuantity;
    @ExcelColumn(title = "初盘数量")
    private BigDecimal firstcountQuantity;
    @ExcelColumn(title = "复盘数量")
    private BigDecimal recountQuantity;
    @ExcelColumn(title = "初盘差异数量")
    private BigDecimal firstcountDifferentQuantity;
    @ExcelColumn(title = "复盘差异数量")
    private BigDecimal recountDifferentQuantity;
    @ExcelColumn(title = "单位编码")
    private String uomCode;
}
