package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCosPatchPdaDTO10
 *
 * @author: chaonan.hu@hand-china.com 2021-01-08 17:39:45
 **/
@Data
public class HmeCosPatchPdaDTO10 implements Serializable {
    private static final long serialVersionUID = -8368898790182270681L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "事件ID")
    private String eventId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "事务数量")
    private BigDecimal transactionQty;

    @ApiModelProperty(value = "工序ID")
    private String processId;
}
