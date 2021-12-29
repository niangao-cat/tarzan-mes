package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/9 18:05
 */
@Data
public class HmeMakeCenterProduceBoardVO19 implements Serializable {

    private static final long serialVersionUID = -1602345151169069345L;

    @ApiModelProperty("EO")
    private String eoId;
    @ApiModelProperty("工单")
    private String workOrderId;
    @ApiModelProperty("工序")
    private String processId;
    @ApiModelProperty("SN数")
    private BigDecimal snQty;
    @ApiModelProperty("出站数")
    private BigDecimal siteOutQty;
    @ApiModelProperty("条码Id")
    private String materialLotId;
}
