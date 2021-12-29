package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 用户信息
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/14 21:02
 */
@ApiModel("用户信息")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeHzeroIamUserDTO  {

    @ApiModelProperty(value = "用户Id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String loginName;

    @ApiModelProperty(value = "租户id")
    private String organizationId;

    @ApiModelProperty(value = "用户类型")
    private String userType;

    @ApiModelProperty(value = "用户类型描述")
    private String userTypeMeaning;

    @ApiModelProperty(value = "租户名称")
    private String tenantName;

}
