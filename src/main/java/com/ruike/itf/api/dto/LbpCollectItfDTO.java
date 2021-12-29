package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName LbpCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/4 17:23
 * @Version 1.0
 **/
@Data
public class LbpCollectItfDTO implements Serializable {

    @ApiModelProperty(value = "SN")
    private String sn;
    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;
    @ApiModelProperty(value = "")
    private String m2;
    @ApiModelProperty(value = "")
    private String m2Y;
    @ApiModelProperty(value = "")
    private String bpp;
    @ApiModelProperty(value = "")
    private String bppY;
    @ApiModelProperty(value = "")
    private String divergence;
    @ApiModelProperty(value = "")
    private String divergenceY;
    @ApiModelProperty(value = "")
    private String waistWidth;
    @ApiModelProperty(value = "")
    private String waistWidthY;
    @ApiModelProperty(value = "")
    private String waistLocation;
    @ApiModelProperty(value = "")
    private String waistLocationY;
}
