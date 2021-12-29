package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeWkcEquSwitchDTO
 * @author: chaonan.hu@hand-china.com 2020-06-23 10:11:13
 **/
@Data
public class HmeWkcEquSwitchDTO implements Serializable {
    private static final long serialVersionUID = -6391638706778400394L;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "工位编码", required = true)
    private String workcellCode;
}
