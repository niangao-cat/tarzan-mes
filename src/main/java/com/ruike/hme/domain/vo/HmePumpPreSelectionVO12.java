package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmePumpPreSelectionVO12
 *
 * @author: chaonan.hu@hand-china.com 2021/09/02 16:06:21
 **/
@Data
public class HmePumpPreSelectionVO12 implements Serializable {
    private static final long serialVersionUID = 4688797154092819863L;

    @ApiModelProperty(value = "筛选池中的条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "SUM类型sequence最小行的tagId,条码所对应的结果")
    private BigDecimal result;

    @ApiModelProperty(value = "结果与SUM类型sequence最小行的最小值的差值,即result-最小值,可正可负")
    private BigDecimal diffMinAverageValue;

    @ApiModelProperty(value = "结果与SUM类型sequence最小行的最大值的差值,即result-最大值,可正可负")
    private BigDecimal diffMaxAverageValue;

    @ApiModelProperty(value = "p/v的结果值")
    private BigDecimal pvResult;

}
