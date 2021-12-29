package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/11 22:09
 */
@Data
@ExcelSheet(zh = "外协管理平台")
public class WmsOutsourceExportVO implements Serializable {

    private static final long serialVersionUID = 13920108524667995L;

    @ApiModelProperty("工厂ID")
    private String siteId;

    @ApiModelProperty("工厂")
    @ExcelColumn(zh = "工厂",order = 1)
    private String siteCode;

    @ApiModelProperty("单号")
    @ExcelColumn(zh = "单号",order = 2)
    private String instructionDocNum;

    @ApiModelProperty("供应商ID")
    private String supplierId;

    @ApiModelProperty("供应商编码")
    @ExcelColumn(zh = "供应商编码",order = 3)
    private String supplierCode;

    @ApiModelProperty("供应商")
    @ExcelColumn(zh = "供应商",order = 4)
    private String supplierName;

    @ApiModelProperty("单据类型")
    @LovValue(value = "WMS.OUTSOURCING_DOC_TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;

    @ApiModelProperty("单据类型描述")
    @ExcelColumn(zh = "单据类型",order = 5)
    private String instructionDocTypeMeaning;

    @ApiModelProperty("单据状态")
    @LovValue(value = "WMS.OUTSOURCING_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("单据状态描述")
    @ExcelColumn(zh = "单据状态",order = 6)
    private String instructionDocStatusMeaning;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    @ExcelColumn(zh = "物料编码",order = 7)
    private String materialCode;

    @ApiModelProperty("物料描述")
    @ExcelColumn(zh = "物料描述",order = 8)
    private String materialName;

    @ApiModelProperty("物料版本")
    @ExcelColumn(zh = "物料版本",order = 9)
    private String materialVersion;

    @ApiModelProperty("制单数量")
    @ExcelColumn(zh = "制单数量",order = 10)
    private BigDecimal quantity;

    @ApiModelProperty("实际制单数量")
    @ExcelColumn(zh = "实际制单数量",order = 11)
    private BigDecimal actualOrderedQty;

    @ApiModelProperty("单位ID")
    private String uomId;

    @ApiModelProperty("单位编码")
    @ExcelColumn(zh = "单位",order = 13)
    private String uomCode;

    @ApiModelProperty("单位名称")
    private String uomName;

    @ApiModelProperty("已接收数量")
    @ExcelColumn(zh = "执行数量",order = 12)
    private BigDecimal actualQty;

    @ApiModelProperty("行状态")
    @LovValue(value = "WMS.OUTSOURCING_LINE_STATUS",meaningField ="instructionStatusMeaning" )
    private String instructionStatus;

    @ApiModelProperty("行状态描述")
    @ExcelColumn(zh = "行状态",order = 14)
    private String instructionStatusMeaning;
}
