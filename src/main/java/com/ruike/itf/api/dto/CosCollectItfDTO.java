package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName CosCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/28 11:29
 * @Version 1.0
 **/
@Data
public class CosCollectItfDTO implements Serializable {
    private static final long serialVersionUID = -3380959657404613407L;

    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "SN")
    private String sn;
    @ApiModelProperty(value = "位置")
    private String cosLocation;
    @ApiModelProperty(value = "测试模式")
    private String cosModel;
    @ApiModelProperty(value = "阈值电流")
    private BigDecimal cosThrescholdCurrent;
    @ApiModelProperty(value = "阈值电压")
    private BigDecimal cosThrescholdVoltage;
    @ApiModelProperty(value = "电流")
    private BigDecimal cosCurrent;
    @ApiModelProperty(value = "电压")
    private BigDecimal cosVoltage;
    @ApiModelProperty(value = "功率")
    private BigDecimal cosPower;
    @ApiModelProperty(value = "中心波长")
    private BigDecimal cosCenterWavelength;
    @ApiModelProperty(value = "SE")
    private BigDecimal cosSe;
    @ApiModelProperty(value = "线宽")
    private BigDecimal cosLinewidth;
    @ApiModelProperty(value = "光电转换效率")
    private BigDecimal cosIpce;
    @ApiModelProperty(value = "波长差")
    private BigDecimal cosWavelengthDiffer;
    @ApiModelProperty(value = "功率等级")
    private String cosPowerLevel;
    @ApiModelProperty(value = "波长分级")
    private String cosWavelengthLevel;
    @ApiModelProperty(value = "偏振度数")
    private BigDecimal cosPolarization;
    @ApiModelProperty(value = "X半宽高")
    private BigDecimal cosFwhmX;
    @ApiModelProperty(value = "Y半宽高")
    private BigDecimal cosFwhmY;
    @ApiModelProperty(value = "X86能量宽度")
    private BigDecimal cos86x;
    @ApiModelProperty(value = "Y86能量宽度")
    private BigDecimal cos86y;
    @ApiModelProperty(value = "X95能量宽度")
    private BigDecimal cos95x;
    @ApiModelProperty(value = "Y95能量宽度")
    private BigDecimal cos95y;
    @ApiModelProperty(value = "错误编码")
    private String cosNcCode;
    @ApiModelProperty(value = "透镜功率")
    private BigDecimal cosLensPower;
    @ApiModelProperty(value = "PBS功率")
    private BigDecimal cosPbsPower;
    @ApiModelProperty(value = "操作者")
    private String cosOperator;
    @ApiModelProperty(value = "备注")
    private String cosRemark;
    @ApiModelProperty(value = "备注")
    private String cosVoltageLevel;
    @ApiModelProperty(value = "")
    private String cosAttribute1;
    @ApiModelProperty(value = "")
    private String cosAttribute2;
    @ApiModelProperty(value = "")
    private String cosAttribute3;
    @ApiModelProperty(value = "")
    private String cosAttribute4;
    @ApiModelProperty(value = "")
    private String cosAttribute5;
    @ApiModelProperty(value = "")
    private String cosAttribute6;
    @ApiModelProperty(value = "")
    private String cosAttribute7;
    @ApiModelProperty(value = "")
    private String cosAttribute8;
    @ApiModelProperty(value = "")
    private String cosAttribute9;
    @ApiModelProperty(value = "")
    private String cosAttribute10;
}
