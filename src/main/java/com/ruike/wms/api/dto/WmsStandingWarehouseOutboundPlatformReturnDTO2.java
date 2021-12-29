package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

@Data
public class WmsStandingWarehouseOutboundPlatformReturnDTO2 implements Serializable {
    private static final long serialVersionUID = -8459321193497036218L;
    @ApiModelProperty(value = "任务号 ")
    private String taskNum;
    @ApiModelProperty(value = "单据号")
    private String instructionLineNum;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "WX.WMS.TASK_STATUS", meaningField = "taskStatusMeaning")
    private String taskStatus;
    @ApiModelProperty(value = "状态含义")
    private String taskStatusMeaning;
    @ApiModelProperty(value = "出口")
    private String exitNum;
    @ApiModelProperty(value = "消息")
    private String message;

}
