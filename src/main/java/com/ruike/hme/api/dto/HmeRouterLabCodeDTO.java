package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeRouterLabCodeDTO
 * 工艺实验代码导入DTO
 * @author: 胡超男
 **/
@Data
public class HmeRouterLabCodeDTO implements Serializable {
    private static final long serialVersionUID = 3755894610983273189L;

    @ApiModelProperty(value = "站点", required = true)
    private String siteCode;

    @ApiModelProperty(value = "工艺路线编码", required = true)
    private String routerName;

    @ApiModelProperty(value = "工艺路线版本", required = true)
    private String revision;

    @ApiModelProperty(value = "工艺路线类型", required = true)
    private String routerType;

    @ApiModelProperty(value = "步骤识别码", required = true)
    private String stepName;

    @ApiModelProperty(value = "实验代码", required = true)
    private String labCode;

    @ApiModelProperty(value = "备注")
    private String remark;
}
