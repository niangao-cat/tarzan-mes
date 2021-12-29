package com.ruike.qms.domain.vo;

import com.ruike.qms.domain.entity.QmsPqcInspectionContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 巡检检查项VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/12 19:53
 */
@Data
public class QmsPqcInspectionContentVO extends QmsPqcInspectionContent implements Serializable {

    @ApiModelProperty(value = "检验频率名称")
    private String frequencyMeaning;

    @ApiModelProperty(value = "规格单位")
    private String standardUom;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "检验工具含义")
    private String inspectionToolMeaning;

    @ApiModelProperty(value = "检验项类别含义")
    private String inspectionTypeMeaning;

    @ApiModelProperty(value = "规格类型含义")
    private String standardTypeMeaning;

    @ApiModelProperty(value = "检验方法含义")
    private String inspectionMethodMeaning;

    @ApiModelProperty(value = "抽样方式编码")
    private String sampleTypeCode;

    @ApiModelProperty(value = "检验值从字符型")
    private String standardFromStr;

    @ApiModelProperty(value = "检验值至字符型")
    private String standardFromTo;
}
