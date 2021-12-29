package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeEoJobPumpCombVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/08/23 19:51
 **/
@Data
public class HmeEoJobPumpCombVO2 implements Serializable {
    private static final long serialVersionUID = 2200968467573728847L;

    @ApiModelProperty(value = "组合物料ID")
    private String materialId;

    @ApiModelProperty(value = "泵浦源需求数")
    private BigDecimal qty;
}
