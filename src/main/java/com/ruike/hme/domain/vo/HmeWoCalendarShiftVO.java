package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 工单派工
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */

@Data
public class HmeWoCalendarShiftVO implements Serializable {

    private static final long serialVersionUID = -2451409199928730165L;

    @ApiModelProperty("班次ID")
    private String calendarShiftId;
    @ApiModelProperty(value = "班次代码")
    private String shiftCode;
    @ApiModelProperty(value = "班次所在日期，年月日")
    private Date shiftDate;
    @ApiModelProperty(value = "休息时长")
    private Double restTime;
    @ApiModelProperty(value = "班次开始时间")
    private Date shiftStartTime;
    @ApiModelProperty(value = "班次结束时间")
    private Date shiftEndTime;
    @ApiModelProperty(value = "派工数量")
    private BigDecimal dispatchQty;
    @ApiModelProperty(value = "合计数量")
    private String totalQty;
    @ApiModelProperty("顺序")
    private Long sequence;
    @ApiModelProperty("可编辑")
    private Integer editableFlag;
    @ApiModelProperty("配送单已生成标志")
    private Boolean docCreatedFlag;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    public HmeCalendarShiftVO convertCalendarShift() {
        HmeCalendarShiftVO calendarShift = new HmeCalendarShiftVO();
        BeanUtils.copyProperties(this, calendarShift);
        return calendarShift;
    }
}
