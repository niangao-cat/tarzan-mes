package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 工单派工明细信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/2 11:37
 */
@Data
public class HmeWoDispatchWkcVO {
    @ApiModelProperty(value = "产品ID")
    private String productId;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "交付时间")
    private String deliveryDate;
    @ApiModelProperty(value = "完成/总数")
    private String completeTotalQty;
    @ApiModelProperty(value = "路线ID")
    private String workcellId;
    @ApiModelProperty(value = "路线编码")
    private String workcellCode;
    @ApiModelProperty(value = "路线名称")
    private String workcellName;
    @ApiModelProperty(value = "完成/工单数量")
    private String orderTotalQty;
    @ApiModelProperty(value = "齐套数量")
    private BigDecimal setQty;
    @ApiModelProperty(value = "剩余数量")
    private BigDecimal remainQty;
    @ApiModelProperty(value = "工段完工数量")
    private BigDecimal workcellCompletionQty;
    @ApiModelProperty(value = "未派数量")
    private BigDecimal unDispatchQty;
    @ApiModelProperty(value = "工单数量")
    private BigDecimal woQty;
    @ApiModelProperty(value = "工单完工数量")
    private BigDecimal completedQty;
    @ApiModelProperty(value = "派工日期列表")
    List<HmeWoCalendarShiftVO> calendarShiftList;

    public HmeWoDispatchVO summaryProduct() {
        HmeWoDispatchVO product = new HmeWoDispatchVO();
        product.setProductId(this.getProductId());
        product.setProductName(this.getProductName());
        product.setCompleteTotalQty(this.getCompleteTotalQty());
        return product;
    }

    public HmeWoDispatchWorkOrderVO summaryWorkOrder() {
        HmeWoDispatchWorkOrderVO workOrder = new HmeWoDispatchWorkOrderVO();
        workOrder.setWorkOrderId(this.getWorkOrderId());
        workOrder.setWorkOrderNum(this.getWorkOrderNum());
        workOrder.setDeliveryDate(this.getDeliveryDate());
        return workOrder;
    }
}
