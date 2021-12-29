package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeEmployeeAttendanceExportVO
 * @description:
 * @author: chaonan.hu@hand-china.com 2020/8/21 14:29:34
 **/
@Data
public class HmeEmployeeAttendanceExportVO implements Serializable {
    private static final long serialVersionUID = 4854192389494566174L;

    private String jobId;

    private String materialLotId;

}
