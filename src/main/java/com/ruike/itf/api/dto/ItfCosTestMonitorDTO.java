package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/21 12:08
 */
@Data
public class ItfCosTestMonitorDTO implements Serializable {

    private static final long serialVersionUID = -3851509601611242554L;

    @ApiModelProperty(value = "接口表主键id")
    private String cosMonitorIfaceId;

    @ApiModelProperty(value = "状态")
    private String docStatus;

    @ApiModelProperty(value = "状态")
    private String checkStatus;
}
