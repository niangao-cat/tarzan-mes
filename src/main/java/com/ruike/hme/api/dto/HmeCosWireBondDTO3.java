package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeCosWireBondDTO3 implements Serializable {

    private static final long serialVersionUID = 761373910554431688L;
    @ApiModelProperty(value = "条码Id", required = true)
    private String materialLotId;

    @ApiModelProperty(value = "条码号", required = true)
    private String materialLotCode;

    @ApiModelProperty(value = "物料Id", required = true)
    private String materialId;

    @ApiModelProperty(value = "物料编码", required = true)
    private String materialCode;

    @ApiModelProperty(value = "数量", required = true)
    private Double qty;
}
