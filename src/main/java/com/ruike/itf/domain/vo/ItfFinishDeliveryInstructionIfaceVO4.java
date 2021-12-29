package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/19 1:18
 */
@Data
public class ItfFinishDeliveryInstructionIfaceVO4 implements Serializable {

    private static final long serialVersionUID = -8686915524928911087L;

    @ApiModelProperty("msgCode")
    private String msgCode;

    @ApiModelProperty("message")
    private String message;

    @ApiModelProperty("taskNum")
    private String taskNum;
}
