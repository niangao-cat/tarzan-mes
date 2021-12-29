package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.util.List;

/**
 * cos目检条码
 *
 * @author li.zhang 2021/01/19 11:39
 */
@Data
@ExcelSheet(title = "cos目检条码表")
public class HmeCosCheckBarcodesVO {

    @ApiModelProperty("工单号")
    @ExcelColumn(title = "工单号")
    private String workOrderNum;

    @ApiModelProperty("产线")
    @ExcelColumn(title = "产线")
    private String prodLineCode;

    @ApiModelProperty("工单芯片数")
    @ExcelColumn(title = "工单芯片数")
    private String qty;

    @ApiModelProperty("目检工位")
    @ExcelColumn(title = "目检工位")
    private String workcellCode;

    @ApiModelProperty("测试机台")
    @ExcelColumn(title = "测试机台")
    private String bench;

    @ApiModelProperty("贴片设备")
    @ExcelColumn(title = "贴片设备")
    private String patch;

    @ApiModelProperty("热沉类型")
    @ExcelColumn(title = "热沉类型")
    private String hotType;

    @ApiModelProperty("热沉投料条码")
    @ExcelColumn(title = "热沉投料条码")
    private String barcode;

    @ApiModelProperty("金锡比")
    @ExcelColumn(title = "金锡比")
    private String ausnRatio;

    @ApiModelProperty("盒子号")
    @ExcelColumn(title = "盒子号")
    private String box;

    @ApiModelProperty("位置")
    @ExcelColumn(title = "位置")
    private String rowCloumn;

    @ApiModelProperty("热沉编号")
    @ExcelColumn(title = "热沉编号")
    private String hotSinkCode;

    @ApiModelProperty("不良编码")
    @ExcelColumn(title = "不良编码")
    private String ncCode;

    @ApiModelProperty("不良描述")
    @ExcelColumn(title = "不良描述")
    private String description;

    @ApiModelProperty("工序状态")
    @ExcelColumn(title = "工序状态")
    private String orderType;

    @ApiModelProperty("备注")
    @ExcelColumn(title = "备注")
    private String note;

    @ApiModelProperty("WAFER")
    @ExcelColumn(title = "WAFER")
    private String wafer;

    @ApiModelProperty("COS类型")
    @ExcelColumn(title = "COS类型")
    private String cosType;

    @ApiModelProperty("操作人")
    @ExcelColumn(title = "操作人")
    private String operatorName;

    @ApiModelProperty("时间")
    @ExcelColumn(title = "时间", pattern = BaseConstants.Pattern.DATE)
    private String creationDate;

    @ApiModelProperty("设备编码")
    @ExcelColumn(title = "设备编码")
    private String equipment;

    @ApiModelProperty("产品编码")
    @ExcelColumn(title = "产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(title = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "实验代码")
    @ExcelColumn(title = "实验代码")
    private String experimentCode;

}
