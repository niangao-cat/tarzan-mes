package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmePumpPreSelectionVO7
 *
 * @author: chaonan.hu@hand-china.com 2021/09/01 14:36:32
 **/
@Data
public class HmePumpPreSelectionVO7 implements Serializable {
    private static final long serialVersionUID = -8004655856814601875L;

    @ApiModelProperty(value = "行主键")
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

    @ApiModelProperty(value = "排序")
    private Long sequence;

    @ApiModelProperty(value = "优先消耗")
    private String priority;

    @ApiModelProperty(value = "头主键")
    private String ruleHeadId;

    @ApiModelProperty(value = "泵浦源个数")
    private BigDecimal qty;

    @ApiModelProperty(value = "头物料")
    private String materialId;
}
