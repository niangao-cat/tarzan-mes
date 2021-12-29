package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/29 16:06
 */
@Data
public class ItfReworkTimeIfaceDTO2 implements Serializable {

    private static final long serialVersionUID = 6399694582492052488L;

    @ApiModelProperty("工位")
    private String workcellCode;

    @ApiModelProperty("SN")
    private String snNum;

    @ApiModelProperty("用户")
    private String user;

    @ApiModelProperty("出站操作")
    private String outSiteAction;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户ID")
    private String defaultSiteId;
}
