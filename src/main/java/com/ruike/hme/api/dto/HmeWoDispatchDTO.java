package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 工单派工
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@Data
public class HmeWoDispatchDTO implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "工单ID", required = true)
    private String workOrderId;
    @ApiModelProperty(value = "产品线ID", required = true)
    private String prodLineId;
    @ApiModelProperty(value = "工段ID", required = true)
    private String workcellId;
    @ApiModelProperty(value = "日历班次ID", required = true)
    private String calendarShiftId;
    @ApiModelProperty(value = "班次编码", required = true)
    private String shiftCode;
    @ApiModelProperty(value = "班次日期", required = true)
    private Date shiftDate;
    @ApiModelProperty(value = "派工数量")
    private BigDecimal dispatchQty;
    @ApiModelProperty(value = "工单数量")
    private BigDecimal woQty;
}
