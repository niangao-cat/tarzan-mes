package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName ApCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/25 19:06
 * @Version 1.0
 **/
@Data
public class ApCollectItfDTO implements Serializable {
    private static final long serialVersionUID = -5278935344282495656L;
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "SN")
    private String sn;
    @ApiModelProperty(value = "老化时长(以小时为单位)")
    private BigDecimal apDuration;
    @ApiModelProperty(value = "通道号")
    private String apChanner;
    @ApiModelProperty(value = "电流")
    private BigDecimal apCurrent;
    @ApiModelProperty(value = "功率")
    private BigDecimal apPower;
    @ApiModelProperty(value = "底座温度")
    private BigDecimal apBaseTemp;
    @ApiModelProperty(value = "光纤温度")
    private BigDecimal apLaserTemp;
    @ApiModelProperty(value = "管嘴温度")
    private BigDecimal apNozzleTemp;
    @ApiModelProperty(value = "水冷板温度")
    private BigDecimal apWaterCoolTemp;

    @ApiModelProperty(value = "")
    private String apAttribute1;
    @ApiModelProperty(value = "")
    private String apAttribute2;
    @ApiModelProperty(value = "")
    private String apAttribute3;
    @ApiModelProperty(value = "")
    private String apAttribute4;
    @ApiModelProperty(value = "")
    private String apAttribute5;
    @ApiModelProperty(value = "")
    private String apAttribute6;
    @ApiModelProperty(value = "")
    private String apAttribute7;
    @ApiModelProperty(value = "")
    private String apAttribute8;
    @ApiModelProperty(value = "")
    private String apAttribute9;
    @ApiModelProperty(value = "")
    private String apAttribute10;
    @ApiModelProperty(value = "")
    private String apAttribute11;
    @ApiModelProperty(value = "")
    private String apAttribute12;
    @ApiModelProperty(value = "")
    private String apAttribute13;
    @ApiModelProperty(value = "")
    private String apAttribute14;
    @ApiModelProperty(value = "")
    private String apAttribute15;
}
