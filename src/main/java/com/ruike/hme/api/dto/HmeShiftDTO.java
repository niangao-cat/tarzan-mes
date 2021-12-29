package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * HmeShiftDTO
 * @author: chaonan.hu@hand-china.com 2020-06-15 20:06:52
 **/
@Data
public class HmeShiftDTO implements Serializable {
    private static final long serialVersionUID = 658020772224348362L;

    @ApiModelProperty(value = "用户默认站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工段Id", required = true)
    private String lineWorkcellId;

    @ApiModelProperty(value = "班次编码", required = true)
    private String shiftCode;

    @ApiModelProperty(value = "班次日期", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate shiftDate;

    @ApiModelProperty(value = "班次实际开班时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shiftActualStartTime;

    @ApiModelProperty(value = "班次实际结班时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shiftActualEndTime;

    @ApiModelProperty(value = "班次日历ID", required = true)
    private String wkcShiftId;

    @ApiModelProperty(value = "班次日历分配唯一标识", required = true)
    private String calendarShiftId;
}
