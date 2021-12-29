package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeMaterialLotNcLoadVO4 implements Serializable {
    private static final long serialVersionUID = -3615711382938316219L;

    @ApiModelProperty(value = "虚拟号ID")
    private String virtualId;

    @ApiModelProperty(value = "id")
    private String ncLoadId;
}
