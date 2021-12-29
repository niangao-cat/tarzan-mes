package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeInspectorItemGroupRelDTO
 * @description: 分页查询条件DTO
 * @author: chaonan.hu@hand-china.com 2021/03/29 13:59:37
 **/
@Data
public class HmeInspectorItemGroupRelDTO implements Serializable {
    private static final long serialVersionUID = -380722975445137220L;

    @ApiModelProperty(value = "用户")
    private String loginName;

    @ApiModelProperty(value = "权限类型")
    private String inspectPowerType;

    @ApiModelProperty(value = "物料组")
    private String itemGroup;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;
}
