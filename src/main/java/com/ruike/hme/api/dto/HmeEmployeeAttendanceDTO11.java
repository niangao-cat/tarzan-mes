package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/6 19:05
 */

@Data
public class HmeEmployeeAttendanceDTO11 implements Serializable {

    private static final long serialVersionUID = -5950361640841902846L;

    @ApiModelProperty(value = "事项")
    private String operation;

    @ApiModelProperty(value = "日期")
    private Date date;

    @ApiModelProperty(value = "班次")
    private String  shiftCode;

    @ApiModelProperty(value = "同一班次id")
    private String relId;

    @ApiModelProperty(value = "上下岗记录id")
    private String recordId;
}
