package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-01 16:20:26
 **/
@Data
public class HmeNcDisposePlatformDTO15 implements Serializable {
    private static final long serialVersionUID = 5263230121617903870L;

    @ApiModelProperty(value = "工位Id")
    private String workcellId;

    @ApiModelProperty(value = "序列号")
    private String snNumber;

    @ApiModelProperty(value = "扫描条码")
    private String scanCode;
}
