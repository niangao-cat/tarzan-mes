package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@ApiModel("货位")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsLocatorDTO {
    @ApiModelProperty("仓库Id")
    private String warehouseId;
    @ApiModelProperty("库位Id")
    private String locatorId;
    @ApiModelProperty("库位Code")
    private String locatorCode;
    @ApiModelProperty("库位描述")
    private String locatorName;

}
