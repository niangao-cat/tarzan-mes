package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeWipStocktakeDocVO11
 *
 * @author: chaonan.hu@hand-china.com 2021/4/12 10:58:12
 **/
@Data
@ExcelSheet(zh = "在制盘点投料汇总")
public class HmeWipStocktakeDocVO11 implements Serializable {
    private static final long serialVersionUID = -984438683047453084L;

    @ApiModelProperty("盘点单ID")
    private String stocktakeId;

    @ApiModelProperty("盘点单号")
    @ExcelColumn(zh = "盘点单号",order = 1)
    private String stocktakeNum;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单号")
    @ExcelColumn(zh = "工单编号",order = 2)
    private String workOrderNum;

    @ApiModelProperty("bomID")
    private String bomId;

    @ApiModelProperty("bom编码")
    private String bomName;

    @ApiModelProperty("bom描述")
    private String description;

    @ApiModelProperty("bom版本号")
    @ExcelColumn(zh = "BOM版本号",order = 3)
    private String bomProductionVersion;

    @ApiModelProperty("bom版本描述")
    @ExcelColumn(zh = "BOM版本描述",order = 4)
    private String bomProductionVersionDesc;

    @ApiModelProperty("物料Id")
    private String materialId;

    @ApiModelProperty("产品编码")
    @ExcelColumn(zh = "产品编码",order = 5)
    private String materialCode;

    @ApiModelProperty("产品名称")
    @ExcelColumn(zh = "产品描述",order = 6)
    private String materialName;

    @ApiModelProperty("物料组")
    @ExcelColumn(zh = "物料组",order = 7)
    private String itemGroup;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("产线编码")
    @ExcelColumn(zh = "产线编码",order = 8)
    private String prodLineCode;

    @ApiModelProperty("产线名称")
    @ExcelColumn(zh = "产线名称",order = 9)
    private String prodLineName;

    @ApiModelProperty("工序ID")
    private String workcellId;

    @ApiModelProperty("工序编码")
    @ExcelColumn(zh = "工序编码",order = 10)
    private String workcellCode;

    @ApiModelProperty("工序名称")
    @ExcelColumn(zh = "工序名称",order = 11)
    private String workcellName;

    @ApiModelProperty("在制数量")
    @ExcelColumn(zh = "在制数量",order = 12)
    private BigDecimal currentQuantity;

    @ApiModelProperty("投料物料ID")
    private String releaseMaterial;

    @ApiModelProperty("投料物料编码")
    @ExcelColumn(zh = "在制账面已投物料",order = 13)
    private String releaseMaterialCode;

    @ApiModelProperty("投料物料名称")
    @ExcelColumn(zh = "在制账面已投物料描述",order = 14)
    private String releaseMaterialName;

    @ApiModelProperty("单位用量")
    @ExcelColumn(zh = "单位用量",order = 15)
    private BigDecimal qty;

    @ApiModelProperty("已投数量")
    @ExcelColumn(zh = "在制账面已投物料数量",order = 16)
    private BigDecimal releaseQty;

    @ApiModelProperty("已走报废数量")
    @ExcelColumn(zh = "已走报废数量",order = 17)
    private BigDecimal scrapQty;

    @ApiModelProperty("单位ID")
    private String uomId;

    @ApiModelProperty("单位编码")
    @ExcelColumn(zh = "单位",order = 18)
    private String uomCode;
}
