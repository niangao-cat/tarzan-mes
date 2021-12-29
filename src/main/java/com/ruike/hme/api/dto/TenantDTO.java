package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 租户DTO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 17:24
 */
@Data
public class TenantDTO {
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("租户名称")
    private String tenantName;
    @ApiModelProperty("租户编号")
    private String tenantNum;
    @ApiModelProperty("是否启用")
    private Integer enabledFlag;
    @ApiModelProperty("限制用户数")
    private Integer limitUserQty;
}
