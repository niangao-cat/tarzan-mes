package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/09/01 15:18
 */
@Data
public class WmsCostCtrMaterialVO implements Serializable {

    private static final long serialVersionUID = -1451531916704554168L;

    @ApiModelProperty(value = "任务号")
    private String taskNum;
    @ApiModelProperty(value = "任务状态")
    private String taskStatus;
    @ApiModelProperty("返回消息")
    private String message;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("行ID")
    private String instructionId;

}
