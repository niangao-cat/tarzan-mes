package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 示波器数据接口接收返回DTO
 * 防止被恶意攻击
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/17 3:48 下午
 */
@Data
public class OssmCollectItfDTO {
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;
    @ApiModelProperty(value = "产品SN")
    private String sn;
    @ApiModelProperty(value = "电压峰值")
    private BigDecimal voltageMax;
    @ApiModelProperty(value = "电压平均值")
    private BigDecimal voltageAve;
    @ApiModelProperty(value = "电流峰值")
    private BigDecimal currentMax;
    @ApiModelProperty(value = "电流平均值")
    private BigDecimal currentAve;
    @ApiModelProperty(value = "脉冲宽度")
    private BigDecimal pulseWidth;
    @ApiModelProperty(value = "频率")
    private BigDecimal frequency;
}
