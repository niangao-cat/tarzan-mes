package com.ruike.qms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 外协发货单行数据返回DTO
 * @auther chaonan.hu
 * @date 2020/6/10
 **/
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class QmsInvoiceLineReturnDTO implements Serializable {
    private static final long serialVersionUID = -5558611333442663752L;

    @ApiModelProperty(value = "采购订单组件Id")
    private String assemblyId;

    @ApiModelProperty(value = "发货单行号")
    private Long lineNum;

    @ApiModelProperty(value = "物料Id")
    private String MaterialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "制单数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单位Id")
    private String uomId;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "采购订单行号")
    private String orderLineNum;

    @ApiModelProperty(value = "实际制单数量")
    private BigDecimal actualQuantity;

    @ApiModelProperty(value = "超发库存")
    private BigDecimal overQuantity;

    @ApiModelProperty(value = "发料仓库")
    private String deliveryWarehouseId;

    @ApiModelProperty(value = "发料仓库名称")
    private String deliveryWarehouseName;

    @ApiModelProperty(value = "发料仓库编码")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "库存现有量")
    private BigDecimal inventoryQuantity;

    @ApiModelProperty(value = "差异数量")
    private BigDecimal subQty;

}
