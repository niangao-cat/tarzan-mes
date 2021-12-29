package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeNcDisposePlatformDTO34
 * 不良计划内退料事务DTO
 * @author: chaonan.hu@hand-china.com 2020-12-14 16:13:45
 **/
@Data
public class HmeNcDisposePlatformDTO34 implements Serializable {
    private static final long serialVersionUID = -8368898290182270681L;

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
