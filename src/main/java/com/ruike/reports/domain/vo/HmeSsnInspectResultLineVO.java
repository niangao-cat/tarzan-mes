package com.ruike.reports.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标准件检验结果行数据
 *
 * @author wenqiang.yin@hand-china.com 2021/02/04 8:32
 */
@Data
public class HmeSsnInspectResultLineVO implements Serializable {

    private static final long serialVersionUID = -5986834232514026114L;

    @ApiModelProperty("序号")
    private String sequence;
    @ApiModelProperty("检验项编码")
    private String tagCode;
    @ApiModelProperty("检验项描述")
    private String tagDescription;
    @ApiModelProperty("最小值")
    private String minimumValue;
    @ApiModelProperty("最大值")
    private String maximalValue;
    @ApiModelProperty("检验值")
    private String inspectValue;
    @ApiModelProperty("检验结果")
    private String result;
}
