package com.ruike.wms.api.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel("仓库")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsWarehouseDTO {

    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("仓库Id")
    private String warehouseId;
    @ApiModelProperty("仓库Code")
    private String warehouseCode;
    @ApiModelProperty("仓库描述")
    private String warehouseDesc;
}
