package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * WmsMaterialLotSplitDTO
 *
 * @author: chaonan.hu@hand-china.com 2020-09-07 16:42:34
 **/
@Data
public class WmsMaterialLotSplitDTO implements Serializable {
    private static final long serialVersionUID = 5313734511359705110L;

    @ApiModelProperty(value = "原始条码Id")
    private String sourceMaterialLotId;

    @ApiModelProperty(value = "拆出个数")
    private int total;

    @ApiModelProperty(value = "拆出数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "扫描条码编码")
    private List<String> materialLotCodeList;
}
