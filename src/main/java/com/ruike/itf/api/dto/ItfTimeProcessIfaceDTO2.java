package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/28 16:28
 */
@Data
public class ItfTimeProcessIfaceDTO2 implements Serializable {

    private static final long serialVersionUID = -8058354006908087666L;

    @ApiModelProperty("工位")
    private String workcellCode;

    @ApiModelProperty("SN")
    private String snNum;

    @ApiModelProperty("用户")
    private String user;

    @ApiModelProperty("实验代码")
    private String labCode;

    @ApiModelProperty("出站操作")
    private String outSiteAction;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户ID")
    private String defaultSiteId;
}
