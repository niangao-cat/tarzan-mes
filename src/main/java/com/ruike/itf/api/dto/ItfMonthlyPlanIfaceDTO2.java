package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ItfMonthlyPlanIfaceDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021-06-01 16:42:12
 **/
@Data
public class ItfMonthlyPlanIfaceDTO2 extends ItfCommonReturnDTO {

    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "生产车间")
    private String prodSuper;
    @ApiModelProperty(value = "期间")
    private String month;
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
}
