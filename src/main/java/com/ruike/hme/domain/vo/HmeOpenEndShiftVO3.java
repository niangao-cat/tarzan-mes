package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeOpenEndShiftVO3
 * @author chaonan.hu@hand-china.com 2020-07-07 10:27:36
 **/
@Data
public class HmeOpenEndShiftVO3 implements Serializable {
    private static final long serialVersionUID = -2701826736864278340L;

    private Date shiftStartTime;

    private Date shiftEndTime;

    private Date shiftActualStartTime;

    private Date shiftActualEndTime;

    private String wkcShiftId;
}
