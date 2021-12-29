package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname CostCenterLovResponseDTO
 * @Description 成本中心LOV输出类型
 * @Date 2019/9/25 19:08
 * @Author by {HuangYuBin}
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:15:46
 */
@ApiModel("成本中心LOV输出类型")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsCostCenterLovResponseDTO {
    @ApiModelProperty(value = "成本中心ID")
    private String costcenterId;
    @ApiModelProperty(value = "成本中心(账户别名)")
    private String costcenterCode;
    @ApiModelProperty(value = "成本中心描述")
    private String costcenterDescription;
    @ApiModelProperty(value = "标识")
    private String mergeFlag;
}
