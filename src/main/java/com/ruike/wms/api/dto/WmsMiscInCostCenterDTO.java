package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Classname MiscInCostCenterDTO
 * @Description 杂收 成本中心查询数据返回类
 * @Date 2019/9/26 13:57
 * @Author zhihao.sang
 * @Moved by yifan.xiong@hand-china.com 2020-9-24 15:15:46
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMiscInCostCenterDTO {
    @ApiModelProperty("成本中心ID(主键，隐藏)")
    private String costCenterId;

    @ApiModelProperty("成本中心")
    private String costCenterCode;

    @ApiModelProperty("成本中心描述")
    private String costCenterName;

    @ApiModelProperty(value = "标识")
    private String mergeFlag;
}
