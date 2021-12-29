package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.instruction.domain.entity.MtInstruction;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/26 14:30
 */
@Data
public class WmsReplenishmentOrderLineVO extends MtInstruction implements Serializable {

    private static final long serialVersionUID = -7859559747489913843L;

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
    private Double quantity;

    @ApiModelProperty(value = "单位Id")
    private String uomId;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "实际制单数量")
    private BigDecimal actualQuantity;

    @ApiModelProperty(value = "发料仓库")
    private String deliveryWarehouseId;

    @ApiModelProperty(value = "发料仓库名称")
    private String deliveryWarehouseName;

    @ApiModelProperty(value = "发料仓库编码")
    private String deliveryWarehouseCode;

    @ApiModelProperty(value = "库存现有量")
    private BigDecimal inventoryQuantity;
}
