package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 工单派工齐套数量信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/14 11:25
 */
@Data
public class HmeWoDispatchSuiteVO {
    @ApiModelProperty(value = "路线ID")
    private String workcellId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "组件齐套数")
    private Long suiteQty;
}
