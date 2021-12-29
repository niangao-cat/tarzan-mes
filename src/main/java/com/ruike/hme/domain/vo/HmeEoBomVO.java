package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoBomVO implements Serializable {

    private static final long serialVersionUID = 1362349169001831577L;

    @ApiModelProperty("EOID")
    private String eoId;
    @ApiModelProperty("装配清单ID")
    private String bomId;
    @ApiModelProperty("装配清单名称")
    private String bomName;
}
