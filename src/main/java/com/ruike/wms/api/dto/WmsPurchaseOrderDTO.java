package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 采购订单接收参数dto
 * @author: han.zhang
 * @create: 2020/03/19 10:56
 */
@Getter
@Setter
@ToString
public class WmsPurchaseOrderDTO implements Serializable {
    private static final long serialVersionUID = 2838198922802824011L;

    @ApiModelProperty(value = "采购订单号")
    private String instructionNum;

    @ApiModelProperty(value = "单据状态")
    private String instructionStatus;

    @ApiModelProperty(value = "工厂")
    private String siteId;

    @ApiModelProperty(value = "供应商")
    private String supplierId;

    @ApiModelProperty(value = "到货时间从")
    private String demandTimeFrom;

    @ApiModelProperty(value = "到货时间至")
    private String demandTimeTo;

    @ApiModelProperty(value = "物料")
    private String materialId;

    @ApiModelProperty(value = "送货单号")
    private String deliveryDocNum;

    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    @ApiModelProperty(value = "订单类型")
    private String poType;
}