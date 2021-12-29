package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工出勤报表
 *
 * @author jianfeng.xia01@hand-china.com 2020-08-04 16:05:12
 */
@Data
public class HmeEmployeeAttendanceDto6 implements Serializable {
    private static final long serialVersionUID = -3172952474077903549L;

    @ApiModelProperty(value = "员工名称")
    private String name;

    @ApiModelProperty(value = "工号")
    private String employeeNum;

}
