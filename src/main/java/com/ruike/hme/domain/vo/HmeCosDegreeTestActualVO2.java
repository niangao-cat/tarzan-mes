package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCosDegreeTestActualVO
 *
 * @author: chaonan.hu@hand-china.com 2021/9/14 15:11
 **/
@Data
public class HmeCosDegreeTestActualVO2 implements Serializable {
    private static final long serialVersionUID = 2254820479645461261L;

    @ApiModelProperty(value = "头ID")
    private String headerId;

    @ApiModelProperty(value = "行ID")
    private String lineId;

    @ApiModelProperty(value = "加测目标良率")
    private BigDecimal addPassRate;

    @ApiModelProperty(value = "优先级")
    private Long priority;

    @ApiModelProperty(value = "测试总量")
    private Long testSumQty;
}
