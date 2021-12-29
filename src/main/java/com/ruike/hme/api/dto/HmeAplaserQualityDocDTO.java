package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeAplaserQualityDocDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-01-20 09:16:53
 **/
@Data
public class HmeAplaserQualityDocDTO implements Serializable {
    private static final long serialVersionUID = 7993924140723337433L;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("SN号")
    private String materialLotCode;

    @ApiModelProperty("数量")
    private BigDecimal quantity;

    @ApiModelProperty("纤芯")
    private String fiberCore;

    @ApiModelProperty("包层")
    private String cladding;

    @ApiModelProperty("包层泵浦吸收")
    private String cladAbsorb;

    @ApiModelProperty("纤芯光损耗")
    private String fiberCoreLoss;

    @ApiModelProperty("包层光损耗")
    private String coatingLoss;

    @ApiModelProperty("纤芯直径")
    private String fiberCoreDiam;

    @ApiModelProperty("包层直径")
    private String cladDiam;

    @ApiModelProperty("涂覆层直径")
    private String coatingDiam;

    @ApiModelProperty("纤芯包层同心度")
    private String fiberCoreConcentricity;

    @ApiModelProperty("筛选测试")
    private String screenTest;

    @ApiModelProperty("斜效率")
    private String obliqueEfficiency;

    @ApiModelProperty("双向11A功率")
    private String twoWayPower;

}
