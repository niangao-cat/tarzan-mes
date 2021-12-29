package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/20 14:52
 */
@Data
public class HmePumpFilterRuleHeaderVO3 implements Serializable {

    private static final long serialVersionUID = -4103706137725440775L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "编码规则")
    private String ruleCode;
    @ApiModelProperty(value = "泵浦源个数")
    private BigDecimal qty;
    @ApiModelProperty(value = "头-有效性")
    @LovValue(lovCode = "Z.FLAG_YN", meaningField = "headEnableFlagMeaning")
    private String headEnableFlag;
    @ApiModelProperty(value = "头-有效性含义")
    private String headEnableFlagMeaning;
    @ApiModelProperty(value = "头-更新人")
    private String headRealName;
    @ApiModelProperty(value = "头-更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date headLastUpdateDate;
    @ApiModelProperty(value = "参数代码")
    private String parameterCode;
    @ApiModelProperty(value = "数据项编码")
    private String tagCode;
    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;
    @ApiModelProperty(value = "计算类型")
    @LovValue(lovCode = "HME_PUMP_FILTER_RULE_LINE_TYPE", meaningField = "calculateTypeMeaning")
    private String calculateType;
    @ApiModelProperty(value = "计算类型含义")
    private String calculateTypeMeaning;
    @ApiModelProperty(value = "最小值")
    private BigDecimal minValue;
    @ApiModelProperty(value = "最大值")
    private BigDecimal maxValue;
    @ApiModelProperty(value = "行-有效性")
    @LovValue(lovCode = "Z.FLAG_YN", meaningField = "lineEnableFlagMeaning")
    private String lineEnableFlag;
    @ApiModelProperty(value = "行-有效性含义")
    private String lineEnableFlagMeaning;
    @ApiModelProperty(value = "公式")
    private String formula;
    @ApiModelProperty(value = "排序")
    private Long sequence;
    @ApiModelProperty("优先消耗")
    @LovValue(lovCode = "HME_PRIORITY_TYPE", meaningField = "priorityMeaning")
    private String priority;
    @ApiModelProperty("优先消耗含义")
    private String priorityMeaning;
    @ApiModelProperty(value = "行-更新人")
    private String lineRealName;
    @ApiModelProperty(value = "行-更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lineLastUpdateDate;
}
