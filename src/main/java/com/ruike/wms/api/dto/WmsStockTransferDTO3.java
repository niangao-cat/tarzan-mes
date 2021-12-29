package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * WmsStockTransferDTO3
 *
 * @author: chaonan.hu@hand-china.com 2020-09-21 17:01
 **/
@Data
public class WmsStockTransferDTO3 implements Serializable {
    private static final long serialVersionUID = 4157499127650003789L;

    @ApiModelProperty(value = "行号")
    private String lineNum;

    @ApiModelProperty(value = "物料编码")
    private String itemCode;

    @ApiModelProperty(value = "物料名称")
    private String itemDesc;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "单位")
    private String uom;

    @ApiModelProperty(value = "收货库位")
    private String locatorName;

    @ApiModelProperty(value = "旧物料号")
    private String oldItemCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "发货库位")
    private String fromLocatorName;
}
