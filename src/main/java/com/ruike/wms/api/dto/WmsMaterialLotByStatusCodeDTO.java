package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname
 * @Description
 * @Date
 * @Author jiepeng.zhang
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 16:08:43
 */
@ApiModel("条码状态")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialLotByStatusCodeDTO {
    @ApiModelProperty(value = "物料批状态")
    String description;
    @ApiModelProperty(value = "物料批状态code")
    String statusCode;
}
