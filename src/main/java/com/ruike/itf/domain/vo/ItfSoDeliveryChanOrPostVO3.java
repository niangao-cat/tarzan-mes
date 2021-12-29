package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/13 7:02
 */
@Data
public class ItfSoDeliveryChanOrPostVO3 implements Serializable {

    private static final long serialVersionUID = 6011705551769462352L;

    @ApiModelProperty("指令行")
    private String instructionId;
    @ApiModelProperty("实绩数量")
    private BigDecimal actualQty;
    @ApiModelProperty("条码")
    private String materialLotCode;
    @ApiModelProperty("条码ID")
    private String materialLotId;
    @ApiModelProperty("批次")
    private String lot;

}
