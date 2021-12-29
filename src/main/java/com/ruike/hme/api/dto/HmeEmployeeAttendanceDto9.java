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
public class HmeEmployeeAttendanceDto9 implements Serializable {
    private static final long serialVersionUID = -528300689154891315L;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "产线Id", required = true)
    private String prodLineId;

    @ApiModelProperty(value = "工段Id")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工段编码")
    private String lineWorkcellCode;

    @ApiModelProperty(value = "工段名称")
    private String lineWorkcellName;
}
