package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ItfInstructionAddrIfaceDTO {

    @ApiModelProperty(value = "送货单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "送货单行号")
    private String srmLineNum;
    @ApiModelProperty(value = "地址")
    private String attribute1;

    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "信息")
    private String message;
}
