package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工出勤报表
 *
 * @author jianfeng.xia01@hand-china.com 2020-08-05 10:04:29
 */
@Data
public class HmeEmployeeAttendanceDto10 implements Serializable {
    private static final long serialVersionUID = 1753584758473187858L;

    @ApiModelProperty(value = "工段Id")
    private String processId;

    @ApiModelProperty(value = "工段编码")
    private String processCode;

    @ApiModelProperty(value = "工段名称")
    private String processName;
}
