package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/16 18:13
 */
@Data
public class ItfFinishDeliveryInstructionIfaceDTO3 implements Serializable {
    private static final long serialVersionUID = -6084576392509833536L;

    @ApiModelProperty("消息状态")
    private String msgCode;
    @ApiModelProperty("消息")
    private String message;
    @ApiModelProperty("任务号")
    private String taskNum;
}
