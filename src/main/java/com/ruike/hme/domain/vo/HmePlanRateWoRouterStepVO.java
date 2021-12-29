package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 计划达成率 工单路线步骤关系
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/17 15:17
 */
@Data
public class HmePlanRateWoRouterStepVO {
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("EO ID")
    private String eoId;
    @ApiModelProperty("路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("步骤顺序")
    private Long sequence;
    @ApiModelProperty("数量")
    private Long qty;
}
