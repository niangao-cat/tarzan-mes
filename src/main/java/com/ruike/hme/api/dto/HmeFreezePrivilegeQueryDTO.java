package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 条码冻结权限 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 08:55
 */
@Data
public class HmeFreezePrivilegeQueryDTO implements Serializable {
    private static final long serialVersionUID = -6590500105440039560L;

    @ApiModelProperty(value = "权限表ID")
    private String privilegeId;
    @ApiModelProperty(value = "用户名称")
    private String userName;
    @ApiModelProperty(value = "权限类型")
    private String privilegeType;
    @ApiModelProperty(value = "COS权限类型")
    private String cosPrivilegeType;
    @ApiModelProperty(value = "有效性")
    private String enableFlag;
}
