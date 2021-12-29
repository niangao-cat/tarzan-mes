package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmePumpPreSelectionDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/08/30 14:11:35
 **/
@Data
public class HmePumpPreSelectionDTO implements Serializable {
    private static final long serialVersionUID = -3543197466244130371L;

    @ApiModelProperty(value = "扫描的容器编码或泵浦源编码", required = true)
    private String scanCode;

    @ApiModelProperty(value = "容器数", required = true)
    private Long containerQty;

    @ApiModelProperty(value = "泵浦源数", required = true)
    private Long pumpQty;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;

}
