package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeInspectorItemGroupRelDTO2
 * 检验员与物料组关系导入DTO
 * @author: chaonan.hu@hand-china.com 2021-03-30 15:50:23
 **/
@Data
public class HmeInspectorItemGroupRelDTO2 implements Serializable {

    @ApiModelProperty(value = "用户", required = true)
    private String loginName;

    @ApiModelProperty(value = "权限类型", required = true)
    private String inspectPowerType;

    @ApiModelProperty(value = "物料组", required = true)
    private String itemGroupCode;

    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;
}
