package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeWipStocktakeDocVO10
 *
 * @author: chaonan.hu@hand-china.com 2021/3/4 14:18:34
 **/
@Data
@ExcelSheet(zh = "在制盘点汇总")
public class HmeWipStocktakeDocVO10 implements Serializable {
    private static final long serialVersionUID = 4013211194012529181L;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 1)
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述",order = 2)
    private String materialName;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "产线编码")
    @ExcelColumn(zh = "产线编码",order = 3)
    private String prodLineCode;

    @ApiModelProperty(value = "产线名称")
    @ExcelColumn(zh = "产线名称",order = 4)
    private String prodLineName;

    @ApiModelProperty(value = "工序ID")
    private String workcellId;

    @ApiModelProperty(value = "工序编码")
    @ExcelColumn(zh = "工序编码",order = 5)
    private String workcellCode;

    @ApiModelProperty(value = "工序名称")
    @ExcelColumn(zh = "工序名称",order = 6)
    private String workcellName;

    @ApiModelProperty(value = "账面数量")
    @ExcelColumn(zh = "账面数量",order = 7)
    private BigDecimal currentQuantity;

    @ApiModelProperty(value = "初盘数量")
    @ExcelColumn(zh = "初盘数量",order = 8)
    private BigDecimal firstcountQuantity;

    @ApiModelProperty(value = "复盘数量")
    @ExcelColumn(zh = "复盘数量",order = 9)
    private BigDecimal recountQuantity;

    @ApiModelProperty(value = "初盘差异")
    @ExcelColumn(zh = "初盘差异",order = 10)
    private BigDecimal firstcountDiff;

    @ApiModelProperty(value = "复盘差异")
    @ExcelColumn(zh = "复盘差异",order = 11)
    private BigDecimal recountDiff;

    @ApiModelProperty(value = "单位ID")
    private String uomId;

    @ApiModelProperty(value = "单位")
    @ExcelColumn(zh = "单位",order = 12)
    private String uomCode;
}
