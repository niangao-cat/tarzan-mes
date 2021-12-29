package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCosPatchPdaVO7
 *
 * @author: chaonan.hu@hand-china.com 2020/12/09 11:27:13
 **/
@Data
public class HmeCosPatchPdaVO7 implements Serializable {
    private static final long serialVersionUID = -1736989501650406096L;

    @ApiModelProperty(value = "true代表需要进行第二次计划外事务")
    private boolean secondTransaction;

    @ApiModelProperty(value = "第二次计划外事务的事务数量")
    private BigDecimal transactionQty;
}
