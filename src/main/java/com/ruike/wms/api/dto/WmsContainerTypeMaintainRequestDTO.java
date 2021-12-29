package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname ContainerTypeMaintainRequestDTO
 * @Description TODO
 * @Date 2019/12/27 8:44
 * @author  by 许博思
 */
@ApiModel("")
@Getter
@Data
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsContainerTypeMaintainRequestDTO {
    @ApiModelProperty(value = "容器类型编码")
    private String containerType;
    @ApiModelProperty(value = "容器类型描述")
    private String description;
    @ApiModelProperty(value = "是否启用AGV")
    private String agvFlag;
}
