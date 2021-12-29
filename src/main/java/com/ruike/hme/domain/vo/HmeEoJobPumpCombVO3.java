package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeEoJobPumpCombVO3
 *
 * @author: chaonan.hu@hand-china.com 2021/08/25 14:32
 **/
@Data
public class HmeEoJobPumpCombVO3 implements Serializable {
    private static final long serialVersionUID = -8974060214364686578L;

    @ApiModelProperty(value = "行表主键")
    private String ruleLineId;

    @ApiModelProperty(value = "参数代码")
    private String parameterCode;

    @ApiModelProperty(value = "数据项ID")
    private String tagId;

    @ApiModelProperty(value = "计算类型")
    private String calculateType;

    @ApiModelProperty(value = "最小值")
    private BigDecimal minValue;

    @ApiModelProperty(value = "最大值")
    private BigDecimal maxValue;

    @ApiModelProperty(value = "公式")
    private String formula;
}
