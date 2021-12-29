package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 工单派工
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */

@Data
public class HmeWoDispatchVO3 implements Serializable {

    private static final long serialVersionUID = -2451409199928730165L;

    @ApiModelProperty(value = "路线ID")
    private String workcellId;
    @ApiModelProperty(value = "路线编码")
    private String workcellCode;
    @ApiModelProperty(value = "路线名称")
    private String workcellName;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "产品线ID")
    private String prodLineId;
    @ApiModelProperty(value = "完成/工单数量")
    private String orderTotalQty;
    @ApiModelProperty(value = "工单数量")
    private BigDecimal woQty;
    @ApiModelProperty(value = "齐套数量")
    private BigDecimal setQty;
    @ApiModelProperty(value = "剩余数量")
    private BigDecimal remainQty;
    @ApiModelProperty(value = "工段完工数量")
    private BigDecimal workcellCompletionQty;
    @ApiModelProperty(value = "未派数量")
    private BigDecimal unDispatchQty;

    @ApiModelProperty(value = "日历班次派工列表")
    List<HmeWoCalendarShiftVO> hmeCalendarShiftList;
}
