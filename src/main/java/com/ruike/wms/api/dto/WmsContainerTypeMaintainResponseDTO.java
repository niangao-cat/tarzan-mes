package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author by 许博思
 * @Classname ContainerTypeMaintainResponseDTO
 * @Description TODO
 * @Date 2019/12/27 8:45
 */
@ApiModel("")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class WmsContainerTypeMaintainResponseDTO {

    @ApiModelProperty(value = "容器类型ID")
    private String containerTypeId;
    @ApiModelProperty(value = "容器类型编码")
    private String containerTypeCode;
    @ApiModelProperty(value = "容器类型描述")
    private String containerTypeDescription;
    @ApiModelProperty(value = "是否启用AGV")
    private String agvFlag;
    @ApiModelProperty(value = "长度")
    private Double length;
    @ApiModelProperty(value = "宽度")
    private Double width;
    @ApiModelProperty(value = "高度")
    private Double height;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "操作")
    private String operation;
}
