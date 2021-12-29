package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 外协发货单据扫描行vo
 * @author: han.zhang
 * @create: 2020/06/19 17:16
 */
@Getter
@Setter
@ToString
public class WmsOutSourceLineVO implements Serializable {
    @ApiModelProperty(value = "单据行Id")
    private String instructionId;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "单位id")
    private String uomId;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "单位名称")
    private String uomName;

    @ApiModelProperty(value = "数量")
    private Double quantity;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WMS.OUTSOURCING_LINE_STATUS",meaningField="instructionStatusMeaning")
    private String instructionStatus;

    @ApiModelProperty(value = "单据状态含义")
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "来源换位id")
    private String fromLocatorId;

    @ApiModelProperty(value = "来源货位编码")
    private String fromLocatorCode;

    @ApiModelProperty(value = "来源货位名称")
    private String fromLocatorName;

    @ApiModelProperty(value = "目标货位id")
    private String toLocatorId;

    @ApiModelProperty(value = "目标货位编码")
    private String toLocatorCode;

    @ApiModelProperty(value = "目标货位名称")
    private String toLocatorName;

    @ApiModelProperty(value = "条码数量")
    private Long codeQty;

    @ApiModelProperty(value = "指令实绩id")
    private String actualId;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "指令实绩数量")
    private Double actualQty;

    @ApiModelProperty(value = "取料货位Id")
    private String getMaterialLocatorId;

    @ApiModelProperty(value = "取料货位编码")
    private String getMaterialLocatorCode;

    @ApiModelProperty(value = "排序标识")
    private int sortInt;

    @ApiModelProperty(value = "任务号")
    private String taskNum;

    @ApiModelProperty(value = "亮灯关灯按钮标识")
    private String lightFlag;

    @ApiModelProperty(value = "亮灯接口成功/失败状态")
    private String status;

    @ApiModelProperty(value = "标识符")
    private String lightStatus;

    @ApiModelProperty(value = "亮灯接口返回状态")
    private String taskStatus;
}