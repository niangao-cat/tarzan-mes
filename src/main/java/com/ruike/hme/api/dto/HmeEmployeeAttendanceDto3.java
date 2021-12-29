package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工出勤报表
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-27 11:28:07
 */
@Data
public class HmeEmployeeAttendanceDto3 implements Serializable {
    @ApiModelProperty(value = "班组id",required = true)
    private String relId;
}
