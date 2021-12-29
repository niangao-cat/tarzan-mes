package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelSheet;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName HmeFunctionReportDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/14 17:38
 * @Version 1.0
 **/
@Data
public class HmeFunctionReportDTO implements Serializable {

    private static final long serialVersionUID = -6904911312521744855L;

    @ApiModelProperty(value = "芯片料号")
    private String materialCode;
    @ApiModelProperty(value = "芯片类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;
    @ApiModelProperty(value = "芯片类型")
    private String cosTypeMeaning;
    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
    @ApiModelProperty(value = "条码号")
    private String materialLotCode;
    @ApiModelProperty(value = "盒子位置")
    private String rowCloumn;
    @ApiModelProperty(value = "热沉")
    private String hotSinkCode;
    @ApiModelProperty(value = "库位")
    private String locatorCode;
    @ApiModelProperty(value = "wafer")
    private String wafer;
    @ApiModelProperty(value = "电流")
    private String current;
    @ApiModelProperty(value = "功率等级")
    private String a01;
    @ApiModelProperty(value = "功率/w")
    private BigDecimal a02;
    @ApiModelProperty(value = "波长等级")
    private String a03;
    @ApiModelProperty(value = "波长/nm")
    private BigDecimal a04;
    @ApiModelProperty(value = "波长差/nm")
    private BigDecimal a05;
    @ApiModelProperty(value = "电压/V\"")
    private BigDecimal a06;
    @ApiModelProperty(value = "光谱宽度(单点)")
    private BigDecimal a07;
    @ApiModelProperty(value = "设备编码")
    private String a08;
    @ApiModelProperty(value = "测试模式")
    private String a09;
    @ApiModelProperty(value = "阈值电流")
    private BigDecimal a010;
    @ApiModelProperty(value = "阈值电压")
    private BigDecimal a011;
    @ApiModelProperty(value = "SE")
    private BigDecimal a012;
    @ApiModelProperty(value = "线宽")
    private BigDecimal a013;
    @ApiModelProperty(value = "光电转换效率")
    private BigDecimal a014;
    @ApiModelProperty(value = "偏振度数")
    private BigDecimal a15;
    @ApiModelProperty(value = "X半宽高")
    private BigDecimal a16;
    @ApiModelProperty(value = "Y半宽高")
    private BigDecimal a17;
    @ApiModelProperty(value = "X86能量宽度")
    private BigDecimal a18;
    @ApiModelProperty(value = "Y86能量宽度")
    private BigDecimal a19;
    @ApiModelProperty(value = "X95能量宽度")
    private BigDecimal a20;
    @ApiModelProperty(value = "Y95能量宽度")
    private BigDecimal a21;
    @ApiModelProperty(value = "透镜功率")
    private BigDecimal a22;
    @ApiModelProperty(value = "PBS功率")
    private BigDecimal a23;
    @ApiModelProperty(value = "不良代码")
    private String a24;
    @ApiModelProperty(value = "操作者")
    private String a25;
    @ApiModelProperty(value = "备注")
    private String a26;
    @ApiModelProperty(value = "设备描述")
    private String assetName;
    @ApiModelProperty(value = "不良描述")
    private String description;
    @ApiModelProperty(value = "姓名")
    private String userName;
}
