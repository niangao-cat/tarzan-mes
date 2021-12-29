package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;

/**
 * @ClassName HmeFunctionExportDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/14 19:18
 * @Version 1.0
 **/
@Data
@ExcelSheet(title = "芯片性能")
public class HmeFunctionExportDTO {
    @ExcelColumn(title = "芯片料号")
    private String materialCode;
    @ApiModelProperty(value = "芯片类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;
    @ExcelColumn(title = "芯片类型")
    private String cosTypeMeaning;
    @ExcelColumn(title = "芯片序列号")
    private String loadSequence;
    @ExcelColumn(title = "条码号")
    private String materialLotCode;
    @ExcelColumn(title = "盒子位置")
    private String rowCloumn;
    @ExcelColumn(title = "热沉")
    private String hotSinkCode;
    @ExcelColumn(title = "库位")
    private String locatorCode;
    @ExcelColumn(title = "wafer")
    private String wafer;
    @ExcelColumn(title = "电流")
    private String current;
    @ExcelColumn(title = "功率等级")
    private String a01;
    @ExcelColumn(title = "功率/w")
    private BigDecimal a02;
    @ExcelColumn(title = "波长等级")
    private String a03;
    @ExcelColumn(title = "波长/nm")
    private BigDecimal a04;
    @ExcelColumn(title = "波长差/nm")
    private BigDecimal a05;
    @ExcelColumn(title = "电压/V\"")
    private BigDecimal a06;
    @ExcelColumn(title = "光谱宽度(单点)")
    private BigDecimal a07;
    @ApiModelProperty(value = "")
    @ExcelColumn(title = "设备编码")
    private String a08;
    @ExcelColumn(title = "测试模式")
    private String a09;
    @ExcelColumn(title = "阈值电流")
    private BigDecimal a010;
    @ExcelColumn(title = "阈值电压")
    private BigDecimal a011;
    @ExcelColumn(title = "SE")
    private BigDecimal a012;
    @ExcelColumn(title = "线宽")
    private BigDecimal a013;
    @ExcelColumn(title = "光电转换效率")
    private BigDecimal a014;
    @ExcelColumn(title = "偏振度数")
    private BigDecimal a15;
    @ExcelColumn(title = "X半宽高")
    private BigDecimal a16;
    @ExcelColumn(title = "Y半宽高")
    private BigDecimal a17;
    @ExcelColumn(title = "X86能量宽度")
    private BigDecimal a18;
    @ExcelColumn(title = "Y86能量宽度")
    private BigDecimal a19;
    @ExcelColumn(title = "X95能量宽度")
    private BigDecimal a20;
    @ExcelColumn(title = "Y95能量宽度")
    private BigDecimal a21;
    @ExcelColumn(title = "透镜功率")
    private BigDecimal a22;
    @ExcelColumn(title = "PBS功率")
    private BigDecimal a23;
    @ExcelColumn(title = "不良代码")
    private String a24;
    @ExcelColumn(title = "工号")
    private String a25;
    @ExcelColumn(title = "备注")
    private String a26;
    @ExcelColumn(title = "设备描述")
    private String assetName;
    @ExcelColumn(title = "不良描述")
    private String description;
    @ExcelColumn(title = "姓名")
    private String userName;
}
