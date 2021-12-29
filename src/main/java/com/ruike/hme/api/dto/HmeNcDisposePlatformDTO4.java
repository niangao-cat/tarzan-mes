package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 15:02:23
 **/
@Data
public class HmeNcDisposePlatformDTO4 implements Serializable {
    private static final long serialVersionUID = 5717981298189154434L;

    @ApiModelProperty(value = "产线Id", required = true)
    private String prodLineId;

    @ApiModelProperty(value = "工序编码")
    private String processCode;

    @ApiModelProperty(value = "工序描述")
    private String processName;
}
