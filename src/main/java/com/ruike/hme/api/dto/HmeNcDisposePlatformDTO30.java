package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-09-27 21:39:28
 **/
@Data
public class HmeNcDisposePlatformDTO30 implements Serializable {
    private static final long serialVersionUID = -8738808805510585088L;

    @ApiModelProperty(value = "扫描SN", required = true)
    private String materialLotCode;

    @ApiModelProperty(value = "工序ID", required = true)
    private String processId;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;
}
