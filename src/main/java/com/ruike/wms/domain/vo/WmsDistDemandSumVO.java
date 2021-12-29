package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 物料配送需求汇总数据
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/19 20:04
 */
@Data
@AllArgsConstructor
public class WmsDistDemandSumVO implements Serializable {
    private static final long serialVersionUID = -9181488716061328873L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "工段ID")
    private String workcellId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "销售订单号")
    private String saleOrderNumber;
    @ApiModelProperty(value = "销售订单行号")
    private String saleOrderLineNumber;
}
