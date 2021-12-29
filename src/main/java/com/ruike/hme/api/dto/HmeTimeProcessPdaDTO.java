package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeTimeProcessPdaDTO
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 14:09:37
 **/
@Data
public class HmeTimeProcessPdaDTO implements Serializable {
    private static final long serialVersionUID = 3324935722384309401L;

    @ApiModelProperty(value = "设备编码", required = true)
    private String equipmentCode;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "用户默认站点Id", required = true)
    private String siteId;
}
