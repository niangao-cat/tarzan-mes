package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeEmployeeAttendanceExportVO2
 *
 * @author: chaonan.hu@hand-china.com 2020/8/21 14:47:13
 **/
@Data
public class HmeEmployeeAttendanceExportVO2 implements Serializable {
    private static final long serialVersionUID = -4073964205604813545L;

    private String ncRecordId;

    private String materialLotId;
}
