package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * description 计划达成率报表请求DTO
 *
 * @author quan.luo@hand-china.com 2020/11/25 20:33
 */
@Data
public class HmePlanRateReportRequestDTO implements Serializable {

    private static final long serialVersionUID = 7169427339354285907L;

    @ApiModelProperty(value = "工段id")
    private String lineId;

    @ApiModelProperty(value = "生产线id", required = true)
    private String prodLineId;

    @ApiModelProperty(value = "开始时间", required = true)
    private LocalDate dateTimeFrom;

    @ApiModelProperty(value = "结束时间", required = true)
    private LocalDate dateTimeTo;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "站点")
    private String siteId;

}
