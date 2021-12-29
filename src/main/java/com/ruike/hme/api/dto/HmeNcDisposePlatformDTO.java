package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 09:53:12
 **/
@Data
public class HmeNcDisposePlatformDTO implements Serializable {
    private static final long serialVersionUID = -8105392827310798140L;

    @ApiModelProperty(value = "工位", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工序", required = true)
    private String processId;

    @ApiModelProperty(value = "序列号", required = true)
    private String materialLotCode;
}
