package com.ruike.hme.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工出勤报表
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-27 11:28:07
 */
@Data
public class HmeEmployeeAttendanceDto4 implements Serializable {
    private static final long serialVersionUID = 8461182265235661265L;
    @ApiModelProperty(value = "组织id")
    private String organizationId;
    @ApiModelProperty(value = "组织名称")
    private String organizationName;
}
