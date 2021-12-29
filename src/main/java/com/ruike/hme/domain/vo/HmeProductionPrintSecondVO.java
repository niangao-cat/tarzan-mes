package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description 条码打印二
 *
 * @author wengang.qiang@hand-china 2021/09/26 13:47
 */
@Data
public class HmeProductionPrintSecondVO implements Serializable {


    private static final long serialVersionUID = -9184283118868102148L;

    @ApiModelProperty(value = "子条码")
    private String subCode;

    @ApiModelProperty(value = "扫描条码")
    private String scanBarCode;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "sap料号")
    private String sapMaterial;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "工位id")
    private String workcellId;
}
