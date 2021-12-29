package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/24 15:04
 */
@Data
public class HmeMakeCenterProduceBoardVO20 implements Serializable {

    private static final long serialVersionUID = 9217386974897132603L;

    @ApiModelProperty("条码")
    private String materialLotId;

    @ApiModelProperty("复检合格数")
    private BigDecimal reInspectionOkQty;
}
