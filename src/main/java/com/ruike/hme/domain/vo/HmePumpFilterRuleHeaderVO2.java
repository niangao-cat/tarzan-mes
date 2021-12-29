package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/20 14:48
 */
@Data
public class HmePumpFilterRuleHeaderVO2 implements Serializable {

    private static final long serialVersionUID = -1675610896237757461L;

    @ApiModelProperty("行ID")
    private String ruleLineId;
    @ApiModelProperty("头ID")
    private String ruleHeadId;
    @ApiModelProperty("参数代码")
    private String parameterCode;
    @ApiModelProperty("数据项")
    private String tagId;
    @ApiModelProperty("数据项编码")
    private String tagCode;
    @ApiModelProperty("数据项描述")
    private String tagDescription;
    @ApiModelProperty("计算类型")
    @LovValue(lovCode = "HME_PUMP_FILTER_RULE_LINE_TYPE", meaningField = "calculateTypeMeaning")
    private String calculateType;
    @ApiModelProperty("计算类型含义")
    private String calculateTypeMeaning;
    @ApiModelProperty("最小值")
    private BigDecimal minValue;
    @ApiModelProperty("最大值")
    private BigDecimal maxValue;
    @ApiModelProperty("有效性")
    @LovValue(lovCode = "Z.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty("有效性含义")
    private String enableFlagMeaning;
    @ApiModelProperty("公式")
    private String formula;
    @ApiModelProperty("公式组合")
    private String formulaAssemble;
    @ApiModelProperty("排序")
    private Long sequence;
    @ApiModelProperty("优先消耗")
    @LovValue(lovCode = "HME_PRIORITY_TYPE", meaningField = "priorityMeaning")
    private String priority;
    @ApiModelProperty("优先消耗含义")
    private String priorityMeaning;
}
