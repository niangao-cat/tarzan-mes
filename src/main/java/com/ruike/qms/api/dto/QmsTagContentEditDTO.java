package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: tarzan-mes
 * @description: 检验项编辑参数
 * @author: han.zhang
 * @create: 2020/04/24 14:48
 */
@Getter
@Setter
@ToString
public class QmsTagContentEditDTO implements Serializable {
    @ApiModelProperty(value = "检验项id",required = true)
    private String materialInspectionContentId;

    @ApiModelProperty(value = "检验项id",required = true)
    private String pqcInspectionContentId;

    @ApiModelProperty(value = "检验频率")
    private String frequency;

    @ApiModelProperty(value = "检验计划Id",required = true)
    private String schemeId;

    @ApiModelProperty(value = "规格值从",required = true)
    private BigDecimal standardFrom;

    @ApiModelProperty(value = "规格值至",required = true)
    private BigDecimal standardTo;

    @ApiModelProperty(value = "文本规格值",required = true)
    private String standardText;

    @ApiModelProperty(value = "缺陷等级",required = true)
    private String defectLevel;

    @ApiModelProperty(value = "版本号",required = true)
    private Long objectVersionNumber;

    @ApiModelProperty(value = "规格类型")
    private String standardType;

    @ApiModelProperty(value = "检验工具")
    private String inspectionTool;

    @ApiModelProperty(value = "检验方法")
    private String inspectionMethod;

    @ApiModelProperty(value = "抽样类型")
    private String sampleType;

    @ApiModelProperty(value = "工序id")
    private String processId;
}