package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.time.*;

/**
 * @Classname HmeWoCalendarShiftVO3
 * @Description 工作日历视图
 * @Date 2020/8/27 10:05
 * @Author yuchao.wang
 */
@Data
public class HmeWoCalendarShiftVO3 implements Serializable {
    private static final long serialVersionUID = 4755688489910021758L;

    @ApiModelProperty(value = "日历ID,引用自MT_CALENDAR_B")
    private String calendarId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "班次开始时间")
    private LocalDate shiftDateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "班次结束时间")
    private LocalDate shiftDateTo;
}