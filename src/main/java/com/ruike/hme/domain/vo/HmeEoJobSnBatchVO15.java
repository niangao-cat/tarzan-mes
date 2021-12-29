package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO15 implements Serializable {
    private static final long serialVersionUID = 6132524707443347175L;
    @ApiModelProperty(value = "EO ID")
    private String eoId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "已投数量")
    private BigDecimal releaseQty;
    @ApiModelProperty(value = "物料类型")
    private String materialType;
}
