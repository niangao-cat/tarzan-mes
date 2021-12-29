package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2021-01-04 19:40
 */
@Data
public class WmsMaterialOnShelfInsActualDTO implements Serializable {

    private static final long serialVersionUID = 901995748591588296L;


    @ApiModelProperty("指令ID")
    private String instructionId;
    @ApiModelProperty("执行总数")
    private BigDecimal executeQty;
    @ApiModelProperty("实际数量")
    private BigDecimal actualQty;
}
