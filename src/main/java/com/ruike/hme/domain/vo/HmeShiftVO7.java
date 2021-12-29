package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeShiftVO7
 *
 * @author chaonan.hu@hand-china.com 2020/07/29 11:38:47
 */
@Data
public class HmeShiftVO7 implements Serializable {
    private static final long serialVersionUID = 8704360850679310067L;

    @ApiModelProperty(value = "x轴数据")
    private List<String> xDataList;

    @ApiModelProperty(value = "y轴数据")
    private List<BigDecimal> yDataList;

}
