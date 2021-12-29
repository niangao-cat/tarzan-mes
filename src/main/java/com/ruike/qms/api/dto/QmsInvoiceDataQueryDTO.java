package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 外协发货单表单数据查询DTO
 * @auther chaonan.hu
 * @date 2020/6/10
 **/
@Getter
@Setter
@ToString
public class QmsInvoiceDataQueryDTO implements Serializable {
    private static final long serialVersionUID = -8073647861409208342L;

    @ApiModelProperty(value = "采购订单行ID及其制单数量信息")
    List<QmsInvoiceDataQueryDTO1> qmsInvoiceDataQueryDTO1s;
}
