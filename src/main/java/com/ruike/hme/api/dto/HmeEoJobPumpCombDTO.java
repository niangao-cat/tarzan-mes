package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEoJobPumpCombDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/08/23 14:01
 **/
@Data
public class HmeEoJobPumpCombDTO implements Serializable {
    private static final long serialVersionUID = 641148532483133056L;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工单ID", required = true)
    private String workOrderId;
}
