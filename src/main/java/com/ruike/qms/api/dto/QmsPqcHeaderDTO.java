package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/8/17 18:37:26
 **/
@Data
public class QmsPqcHeaderDTO implements Serializable {
    private static final long serialVersionUID = -2736823299530339185L;

    @ApiModelProperty(value = "SN",required = true)
    private String materialLotCode;

    @ApiModelProperty(value = "工序id",required = true)
    private String processId;

    @ApiModelProperty(value = "产线Id", required = true)
    private String prodLineId;

}
