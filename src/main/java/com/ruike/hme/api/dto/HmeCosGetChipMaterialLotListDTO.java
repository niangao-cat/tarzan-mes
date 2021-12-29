package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosGetChipMaterialLotListDTO
 *
 * @author: chaonan.hu@hand-china.com 2020-11-03 10:25
 **/
@Data
public class HmeCosGetChipMaterialLotListDTO implements Serializable {
    private static final long serialVersionUID = 5310008699692500725L;

    @ApiModelProperty(value = "工艺路线ID", required = true)
    private String operationId;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工单ID", required = true)
    private String workOrderId;
}
