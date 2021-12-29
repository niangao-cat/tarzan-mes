package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description
 *
 * @author li.zhang 2021/09/28 9:21
 */
@Data
@ExcelSheet(zh = "出入库动态报表")
public class WmsStockDynamicReportVO implements Serializable {

    private static final long serialVersionUID = -3997283926174367337L;

    @ApiModelProperty("工厂编码")
    @ExcelColumn(title = "工厂编码", order = 0)
    private String siteCode;
    @ApiModelProperty("物料组编码")
    @ExcelColumn(title = "物料组编码", order = 1)
    private String itemGroupCode;
    @ApiModelProperty("物料组描述")
    @ExcelColumn(title = "物料组描述", order = 2)
    private String itemGroupDescription;
    @ApiModelProperty("出入库类型")
    @ExcelColumn(title = "出入库类型", order = 3)
    private String stockTypeMeaning;
    @ApiModelProperty("出入库时间")
    @ExcelColumn(title = "出入库时间", order = 4)
    private String stockDate;
    @ApiModelProperty("数量")
    @ExcelColumn(title = "数量", order = 5)
    private String qty;
    @ApiModelProperty("仓库编码")
    @ExcelColumn(title = "仓库编码", order = 6)
    private String locatorCode;
}
