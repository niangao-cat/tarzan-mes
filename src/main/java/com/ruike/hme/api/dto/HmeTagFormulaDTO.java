package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeTagFormulaDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/01/14 17:51:09
 **/
@Data
public class HmeTagFormulaDTO implements Serializable {
    private static final long serialVersionUID = -3380422697817060358L;

    @ApiModelProperty(value = "数据项", required = true)
    private String tagCodeHead;

    @ApiModelProperty(value = "数据组")
    private String tagGroupCodeHead;

    @ApiModelProperty(value = "工艺")
    private String operationNameHead;

    @ApiModelProperty(value = "公式类型")
    private String formulaType;

    @ApiModelProperty(value = "公式", required = true)
    private String formula;

    @ApiModelProperty(value = "参数代码", required = true)
    private String parameterCode;

    @ApiModelProperty(value = "参数数据项", required = true)
    private String tagCodeLine;

    @ApiModelProperty(value = "数据组")
    private String tagGroupCodeLine;

    @ApiModelProperty(value = "工艺")
    private String operationNameLine;

    @ApiModelProperty(value = "导入方式", required = true)
    private String importWay;
}
