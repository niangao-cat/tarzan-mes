package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEoJobPumpCombDTO3
 *
 * @author: chaonan.hu@hand-china.com 2021/08/24 16:43
 **/
@Data
public class HmeEoJobPumpCombDTO3 implements Serializable {
    private static final long serialVersionUID = 7415975373224494153L;

    @ApiModelProperty(value = "子条码", required = true)
    private String subCode;

    @ApiModelProperty(value = "扫描条码", required = true)
    private String scanBarCode;

    @ApiModelProperty(value = "工单ID", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "sap料号", required = true)
    private String sapMaterial;
}
