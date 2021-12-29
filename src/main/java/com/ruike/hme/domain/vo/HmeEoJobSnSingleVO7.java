package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnSingleVO7 implements Serializable {
    private static final long serialVersionUID = -1222951369351890615L;
    @ApiModelProperty(value = "虚拟号ID")
    private String virtualId;
    @ApiModelProperty(value = "装载表主键")
    private String materialLotLoadId;
}
