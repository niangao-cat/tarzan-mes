package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-07 11:28:41
 **/
@Data
public class HmeOpenEndShiftDTO2 implements Serializable {
    private static final long serialVersionUID = -5019796311384151663L;

    @ApiModelProperty(value = "工段")
    private String lineWorkcellId;

    @ApiModelProperty(value = "班次时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate shiftDate;

    @ApiModelProperty(value = "班次编码")
    private String shiftCode;

    @ApiModelProperty(value = "班次开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shiftStartTime;

    @ApiModelProperty(value = "班次结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shiftEndTime;

    @ApiModelProperty(value = "班次日历ID")
    private String wkcShiftId;
}
