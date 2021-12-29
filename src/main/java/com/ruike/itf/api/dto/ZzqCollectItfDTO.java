package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName QqzCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/16 13:47
 * @Version 1.0
 **/
@Data
public class ZzqCollectItfDTO implements Serializable {
    private static final long serialVersionUID = -3049141362289257830L;
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "SN")
    private String sn;
    @ApiModelProperty(value = "质心X")
    private BigDecimal zzqCenterX;
    @ApiModelProperty(value = "质心Y")
    private BigDecimal zzqCenterY;
    @ApiModelProperty(value = "")
    private String zzqAttribute1;
    @ApiModelProperty(value = "")
    private String zzqAttribute2;
    @ApiModelProperty(value = "")
    private String zzqAttribute3;
    @ApiModelProperty(value = "")
    private String zzqAttribute4;
    @ApiModelProperty(value = "")
    private String zzqAttribute5;
    @ApiModelProperty(value = "")
    private String zzqAttribute6;
    @ApiModelProperty(value = "")
    private String zzqAttribute7;
    @ApiModelProperty(value = "")
    private String zzqAttribute8;
    @ApiModelProperty(value = "")
    private String zzqAttribute9;
    @ApiModelProperty(value = "")
    private String zzqAttribute10;
    @ApiModelProperty(value = "")
    private String zzqAttribute11;
    @ApiModelProperty(value = "")
    private String zzqAttribute12;
    @ApiModelProperty(value = "")
    private String zzqAttribute13;
    @ApiModelProperty(value = "")
    private String zzqAttribute14;
    @ApiModelProperty(value = "")
    private String zzqAttribute15;
}
