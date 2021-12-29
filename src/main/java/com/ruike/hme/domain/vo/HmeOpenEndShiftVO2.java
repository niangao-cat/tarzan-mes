package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeOpenEndShiftVO2
 * @author chaonan.hu@hand-china.com 2020-07-07 10:27:36
 **/
@Data
public class HmeOpenEndShiftVO2 implements Serializable {
    private static final long serialVersionUID = 7956994748447183338L;

    private String shiftCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shiftStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shiftEndTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;

    private String calendarShiftId;
}
