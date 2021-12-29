package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEoJobPumpCombDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021/08/23 15:23
 **/
@Data
public class HmeEoJobPumpCombDTO2 implements Serializable {
    private static final long serialVersionUID = -8403341288653961096L;

    @ApiModelProperty(value = "物料序列号", required = true)
    private String snNum;

    @ApiModelProperty(value = "工单ID", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "sap料号", required = true)
    private String sapMaterial;
}
