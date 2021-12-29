package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeRasterQualityDocDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-01-19 17:06:26
 **/
@Data
public class HmeRasterQualityDocDTO implements Serializable {
    private static final long serialVersionUID = 2638219745616263221L;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("SN号")
    private String materialLotCode;

    @ApiModelProperty("数量")
    private BigDecimal quantity;

    @ApiModelProperty("光栅类型")
    private String rasterType;

    @ApiModelProperty("中心波长")
    private String centerWavelength;

    @ApiModelProperty("带宽")
    private String bandWidth;

    @ApiModelProperty("反射率")
    private String reflectivity;

    @ApiModelProperty("SLSR")
    private String slsr;

    @ApiModelProperty("通泵浦光光纤温度")
    private String apLaserTemp;

    @ApiModelProperty("光纤类型")
    private String apLaserType;

    @ApiModelProperty("光纤lot号")
    private String apLaserLot;

    @ApiModelProperty("纤芯直径")
    private String fiberCoreDiam;

    @ApiModelProperty("包层直径")
    private String cladDiam;

    @ApiModelProperty("NA")
    private String na;

    @ApiModelProperty("芯包同心度")
    private String corePackageConcentricity;
}
