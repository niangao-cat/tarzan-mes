package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 19:03:29
 **/
@Data
public class HmeNcDisposePlatformDTO10 implements Serializable {
    private static final long serialVersionUID = -7151206912150077211L;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "序列号", required = true)
    private String materialLotCode;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位描述")
    private String workcellName;
}
