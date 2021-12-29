package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工出勤报表
 *
 * @author jianfeng.xia01@hand-china.com 2020-08-05 09:29:36
 */
@Data
public class HmeEmployeeAttendanceDto8 implements Serializable {
    private static final long serialVersionUID = 8390638772895680112L;

    @ApiModelProperty(value = "产线Id")
    private String prodLineId;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;
}
