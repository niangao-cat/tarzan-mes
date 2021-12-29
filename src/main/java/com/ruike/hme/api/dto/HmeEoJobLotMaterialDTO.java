package com.ruike.hme.api.dto;


import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobLotMaterialDTO implements Serializable {

    private static final long serialVersionUID = 9063224936499115826L;

    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("条码ID")
    private String materialLotId;
    @ApiModelProperty("是否投料标识")
    private Integer isReleased;
}
