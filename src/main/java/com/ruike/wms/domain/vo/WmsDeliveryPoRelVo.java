package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 送货单与采购订单关联数据
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 09:51
 */
@Data
public class WmsDeliveryPoRelVo {
    @ApiModelProperty("送货单ID")
    private String deliveryDocId;
    @ApiModelProperty("送货单行ID")
    private String deliveryId;
    @ApiModelProperty("采购订单ID")
    private String poId;
    @ApiModelProperty("采购订单行ID")
    private String poLineId;
    @ApiModelProperty("采购订单号")
    private String poNumber;
    @ApiModelProperty("采购订单行号")
    private String poLineNumber;
    @ApiModelProperty("分配数量")
    private BigDecimal distributeQty;
    @ApiModelProperty("执行数量")
    private BigDecimal stockInQty;
}
