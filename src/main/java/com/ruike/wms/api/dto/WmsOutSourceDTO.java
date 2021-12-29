package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * WmsOutSourceDTO
 * @description: 条码拆分输入参数类
 * @author: chaonan.hu@hand-china.com 2020-10-21
 **/
@Data
public class WmsOutSourceDTO implements Serializable {
    private static final long serialVersionUID = 7701069719851067465L;

    @ApiModelProperty(value = "单据头id", required = true)
    private String instructionDocId;

    @ApiModelProperty(value = "指令id")
    private String instructionId;

    @ApiModelProperty(value = "来源条码", required = true)
    private String sourceMaterialLotCode;

    @ApiModelProperty(value = "目标条码Id")
    private String targetMaterialLotId;

    @ApiModelProperty(value = "目标条码编码")
    private String targetMaterialLotCode;

    @ApiModelProperty(value = "拆分数量", required = true)
    private BigDecimal splitQty;
}
