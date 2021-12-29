package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description
 *
 * @author li.zhang 2021/09/27 17:08
 */
@Data
@ExcelSheet(title = "物料周转率报表")
public class WmsMaterialTurnoverRateVO implements Serializable {

    private static final long serialVersionUID = -5680755060441820885L;

    @ApiModelProperty("工厂编码")
    @ExcelColumn(title = "工厂编码", order = 0)
    private String siteCode;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料编码")
    @ExcelColumn(title = "物料编码", order = 1)
    private String materialCode;
    @ApiModelProperty("物料描述")
    @ExcelColumn(title = "物料描述", order = 2)
    private String materialName;
    @ApiModelProperty("仓库编码")
    @ExcelColumn(title = "仓库编码", order = 3)
    private String warehouseCode;
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("货位编码")
    @ExcelColumn(title = "货位编码", order = 4)
    private String locatorCode;
    @ApiModelProperty("期初库存")
    @ExcelColumn(title = "期初库存", order = 5)
    private BigDecimal startInventory;
    @ApiModelProperty("总收货数量")
    @ExcelColumn(title = "总收货数量", order = 6)
    private BigDecimal receiptQty;
    @ApiModelProperty("总发货数量")
    @ExcelColumn(title = "总发货数量", order = 7)
    private BigDecimal sendQty;
    @ApiModelProperty("期末库存")
    @ExcelColumn(title = "期末库存", order = 8)
    private BigDecimal endInventory;
    @ApiModelProperty("周转率")
    @ExcelColumn(title = "周转率", order = 9)
    private String turnoverRate;
    @ApiModelProperty("计量单位")
    @ExcelColumn(title = "计量单位", order = 10)
    private String uomCode;
    @ApiModelProperty("开始时间")
    @ExcelColumn(title = "开始时间", order = 11)
    private String startDate;
    @ApiModelProperty("结束时间")
    @ExcelColumn(title = "结束时间", order = 12)
    private String endDate;

}
