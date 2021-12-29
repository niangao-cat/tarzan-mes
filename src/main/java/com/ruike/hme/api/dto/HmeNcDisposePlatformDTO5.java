package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 15:38:21
 **/
@Data
public class HmeNcDisposePlatformDTO5 implements Serializable {
    private static final long serialVersionUID = 7335607766653211209L;

    @ApiModelProperty(value = "工序Id", required = true)
    private String processId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位描述")
    private String workcellName;
}
