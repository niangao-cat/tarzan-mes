package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ItfSendOutReturnDTO2 implements Serializable {
    private static final long serialVersionUID = 4327275920896887749L;
    @ApiModelProperty(value = "行号")
    private String lineNumber;

    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;

    @ApiModelProperty(value = "物料")
    private String materialId;

    @ApiModelProperty(value = "指令Id")
    private String instructionId;

    @ApiModelProperty(value = "销售订单行状态")
    private String instructionStatus;
}
