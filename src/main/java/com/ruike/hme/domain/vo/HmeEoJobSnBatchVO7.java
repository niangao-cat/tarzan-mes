package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO7 implements Serializable {
    private static final long serialVersionUID = 702785468483090872L;

    @ApiModelProperty(value = "EO ID")
    private String eoId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "装配数量")
    private BigDecimal assembleQty;
}
