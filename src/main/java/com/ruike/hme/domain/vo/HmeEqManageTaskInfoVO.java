package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/14 13:44
 */
@Data
public class HmeEqManageTaskInfoVO implements Serializable {

    private static final long serialVersionUID = -931712343898502888L;

    @ApiModelProperty(value = "设备管理任务行ID")
    private String taskDocLineId;

    @ApiModelProperty(value = "项目ID")
    private String manageTagId;

    @ApiModelProperty(value = "检验值")
    private String checkValue;

    @ApiModelProperty(value = "结果")
    private String result;

    @ApiModelProperty(value = "单据头id")
    private String taskDocId;

    @ApiModelProperty(value = "")
    private String attribute1;

    @ApiModelProperty(value = "设备管理项目组ID")
    private String manageTagGroupId;

    @ApiModelProperty(value = "排序码")
    private Long serialNumber;

    @ApiModelProperty(value = "项目描述")
    private String tagDescriptions;

    @ApiModelProperty(value = "数据类型")
    private String valueType;

    @ApiModelProperty(value = "最小值")
    private Double minimumValue;

    @ApiModelProperty(value = "最大值")
    private Double maximalValue;

    @ApiModelProperty(value = "计量单位Id")
    private String uomId;

    @ApiModelProperty(value = "计量单位编码")
    private String uomCode;

    @ApiModelProperty(value = "计量单位编码")
    private String tagCode;

    @ApiModelProperty(value = "标准值")
    private Double standardValue;

    @ApiModelProperty(value = "符合值")
    private String trueValue;

    @ApiModelProperty(value = "不符合值")
    private String falseValue;

    @ApiModelProperty(value = "单据头号")
    private String docNum;

    @ApiModelProperty(value = "单据状态")
    private String docStatus;

    @ApiModelProperty(value = "备注")
    private String remark;
}
