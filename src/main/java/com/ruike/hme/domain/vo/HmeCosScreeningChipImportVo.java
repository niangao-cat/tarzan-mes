package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCosScreeningChipImportVo
 * @description: COS筛选后芯片导入-VO
 * @author: chaonan.hu@hand-china.com 2020-10-12 09:12:23
 **/
@Data
public class HmeCosScreeningChipImportVo implements Serializable {
    private static final long serialVersionUID = 4785539179755756560L;

    @ApiModelProperty("公司编码")
    private String siteCode;

    @ApiModelProperty("条码号")
    private String materialLotCode;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("数量")
    private String qtyStr;

//    @ApiModelProperty("货位")
//    private String locatorCode;

    @ApiModelProperty("虚拟号")
    private String virtualNum;

//    @ApiModelProperty("批次号")
//    private String lot;

    @ApiModelProperty("芯片位置")
    private String chipLocation;

    @ApiModelProperty("wafer")
    private String wafer;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("芯片序列号")
    private String chipSequence;

    @ApiModelProperty("电流")
    private BigDecimal current;

    @ApiModelProperty(value = "功率等级")
    private String cosPowerLevel;

    @ApiModelProperty(value = "功率")
    private BigDecimal cosPower;

    @ApiModelProperty(value = "波长等级")
    private String cosWavelengthLevel;

    @ApiModelProperty(value = "波长差")
    private BigDecimal cosWavelengthDiffer;

    @ApiModelProperty("电压")
    private BigDecimal cosVoltage;

    @ApiModelProperty("设备")
    private String assetEncoding;

    @ApiModelProperty("测试模式")
    private String cosModel;

    @ApiModelProperty("阈值电流")
    private BigDecimal cosThrescholdCurrent;

    @ApiModelProperty("阈值电压")
    private BigDecimal cosThrescholdVoltage;

    @ApiModelProperty("中心波长")
    private BigDecimal cosCenterWavelength;

    @ApiModelProperty("SE")
    private BigDecimal cosSe;

    @ApiModelProperty("线宽")
    private BigDecimal cosLinewidth;

    @ApiModelProperty("光电能转换效率")
    private BigDecimal cosIpce;

    @ApiModelProperty("偏振度")
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
}
