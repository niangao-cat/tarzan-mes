package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 组织信息
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/14 21:02
 */
@ApiModel("组织信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeHzeroPlatformUnitDTO {

    @ApiModelProperty(value = "组织Id")
    private String unitId;

    @ApiModelProperty(value = "组织编码")
    private String unitCode;

    @ApiModelProperty(value = "组织名称")
    private String unitName;

    @ApiModelProperty(value = "组织类型编码")
    private String unitTypeCode;

}
