package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工出勤报表
 *
 * @author jianfeng.xia01@hand-china.com 2020-08-05 09:19:17
 */
@Data
public class HmeEmployeeAttendanceDto7 implements Serializable {
    private static final long serialVersionUID = -4995282317536173572L;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "车间ID", required = true)
    private String workshopId;
}
