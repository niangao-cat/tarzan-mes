package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeEmployeeAttendanceDTO12
 *
 * @author chaonan.hu@hand-china.com 2020-08-24 14:34:29
 **/
@Data
public class HmeEmployeeAttendanceDTO12 implements Serializable {
    private static final long serialVersionUID = 2712207529509505557L;

    private int page;

    private int size;

    private List<String> idList;
}
