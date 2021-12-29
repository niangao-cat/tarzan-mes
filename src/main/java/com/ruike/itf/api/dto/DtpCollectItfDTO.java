package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName DtpCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/25 19:22
 * @Version 1.0
 **/
@Data
public class DtpCollectItfDTO implements Serializable {
    private static final long serialVersionUID = -1566334472776490293L;
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "产品SN")
    private String sn;
    @ApiModelProperty(value = "电流")
    private BigDecimal dtpCurrent;
    @ApiModelProperty(value = "阈值电流")
    private BigDecimal dtpThrescholdCurrent;
    @ApiModelProperty(value = "电压")
    private BigDecimal dtpVoltage;
    @ApiModelProperty(value = "阈值电压")
    private BigDecimal dtpThrescholdVoltage;
    @ApiModelProperty(value = "中心波长")
    private BigDecimal dtpCenterWavelength;
    @ApiModelProperty(value = "线宽")
    private BigDecimal dtpLinewidth;
    @ApiModelProperty(value = "光电转换效率")
    private BigDecimal dtpIpce;
    @ApiModelProperty(value = "SE")
    private BigDecimal dtpSe;
    @ApiModelProperty(value = "波长差")
    private BigDecimal dtpWavelengthDiffer;
    @ApiModelProperty(value = "线宽差")
    private BigDecimal dtpLinewidthDiffer;
    @ApiModelProperty(value = "变化率")
    private BigDecimal dtpChangeRate;
    @ApiModelProperty(value = "测试模式")
    private String dtpMode;
    @ApiModelProperty(value = "功率")
    private BigDecimal dtpPower;
    @ApiModelProperty(value = "工序名")
    private String dtpOperationName;

    @ApiModelProperty(value = "")
    private String dtpAttribute1;
    @ApiModelProperty(value = "")
    private String dtpAttribute2;
    @ApiModelProperty(value = "")
    private String dtpAttribute3;
    @ApiModelProperty(value = "")
    private String dtpAttribute4;
    @ApiModelProperty(value = "")
    private String dtpAttribute5;
    @ApiModelProperty(value = "")
    private String dtpAttribute6;
    @ApiModelProperty(value = "")
    private String dtpAttribute7;
    @ApiModelProperty(value = "")
    private String dtpAttribute8;
    @ApiModelProperty(value = "")
    private String dtpAttribute9;
    @ApiModelProperty(value = "")
    private String dtpAttribute10;
    @ApiModelProperty(value = "")
    private String dtpAttribute11;
    @ApiModelProperty(value = "")
    private String dtpAttribute12;
    @ApiModelProperty(value = "")
    private String dtpAttribute13;
    @ApiModelProperty(value = "")
    private String dtpAttribute14;
    @ApiModelProperty(value = "")
    private String dtpAttribute15;
}
