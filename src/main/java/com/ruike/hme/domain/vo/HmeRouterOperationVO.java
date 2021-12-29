package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 路线工艺数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/22 10:02
 */
@Data
public class HmeRouterOperationVO {
    @ApiModelProperty(value = "工艺路线步骤标识")
    private String routerStepId;
    @ApiModelProperty(value = "工艺路线名称")
    private String stepName;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
}
