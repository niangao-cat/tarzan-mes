package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeEoJobSnReworkDTO2 implements Serializable {
    private static final long serialVersionUID = 6650418884931329198L;

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "JobMaterialId")
    private String JobMaterialId;
}
