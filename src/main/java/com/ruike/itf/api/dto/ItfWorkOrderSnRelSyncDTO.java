package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 生产订单和SN关系
 *
 * @author kejin.liu01@hand-china.com 2020/9/16 15:31
 */
@Data
public class ItfWorkOrderSnRelSyncDTO {

    @ApiModelProperty(value = "生产订单")
    private String AUFNR;

    @ApiModelProperty(value = "生产订单类型")
    private String AUART;

    @ApiModelProperty(value = "SN号")
    private String SERNR;

    @ApiModelProperty(value = "描述")
    private String STTXT;


}
