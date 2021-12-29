package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 工单派工齐套信息查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/14 11:28
 */
@Data
public class HmeWoDispatchSuiteQueryDTO {
    @ApiModelProperty(value = "路线ID")
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "工单ID")
    @NotBlank
    private String workOrderId;
}
