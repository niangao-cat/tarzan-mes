package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/08/09 11:30
 */
@Data
public class ItfLightTaskIfaceDTO implements Serializable {

    private static final long serialVersionUID = -7797824334817116647L;

    @ApiModelProperty(value = "任务号")
    private String taskNum;
    @ApiModelProperty(value = "指令单据id")
    private String instructionDocId;
    @ApiModelProperty(value = "指令id")
    private String instructionId;
    @ApiModelProperty(value = "货位")
    private String locatorCode;
    @ApiModelProperty(value = "任务类型")
    private String taskType;
    @ApiModelProperty(value = "任务状态 ")
    private String taskStatus;

}
