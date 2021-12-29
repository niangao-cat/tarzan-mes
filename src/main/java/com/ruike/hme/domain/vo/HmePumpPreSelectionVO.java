package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmePumpPreSelectionVO
 *
 * @author: chaonan.hu@hand-china.com 2021/08/30 11:19:12
 **/
@Data
public class HmePumpPreSelectionVO implements Serializable {
    private static final long serialVersionUID = 2063921951568413227L;

    @ApiModelProperty(value = "筛选规则行主键")
    private String ruleLineId;

    @ApiModelProperty(value = "数据项ID")
    private String tagId;

    @ApiModelProperty(value = "数据项编码")
    private String tagCode;

    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;

    @ApiModelProperty(value = "计算类型")
    @LovValue(value = "HME_PUMP_FILTER_RULE_LINE_TYPE", meaningField = "calculateTypeMeaning")
    private String calculateType;

    @ApiModelProperty(value = "计算类型含义")
    private String calculateTypeMeaning;

    @ApiModelProperty(value = "最小值")
    private BigDecimal minValue;

    @ApiModelProperty(value = "最大值")
    private BigDecimal maxValue;

    @ApiModelProperty(value = "泵浦源个数")
    private BigDecimal qty;
}
