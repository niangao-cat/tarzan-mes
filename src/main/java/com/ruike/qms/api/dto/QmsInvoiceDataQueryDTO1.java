package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 外协发货单表单数据查询DTO1
 * @auther chaonan.hu
 * @date 2020/6/13
 **/
@Getter
@Setter
@ToString
public class QmsInvoiceDataQueryDTO1 implements Serializable {
    private static final long serialVersionUID = 5422143369662636692L;

    @ApiModelProperty(value = "采购订单行ID")
    private String instructionId;

    @ApiModelProperty(value = "制单数量")
    private BigDecimal quantity;
}
