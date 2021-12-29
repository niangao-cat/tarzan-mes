package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/18 23:53
 */
@Data
public class ItfFinishDeliveryInstructionIfaceVO2 implements Serializable {

    private static final long serialVersionUID = -8487674236260479130L;

    @ApiModelProperty("指令行ID")
    private String instructionId;
    @ApiModelProperty("指令行编码")
    private String instructionNum;
    @ApiModelProperty("行号")
    private String instructionLineNum;
}
