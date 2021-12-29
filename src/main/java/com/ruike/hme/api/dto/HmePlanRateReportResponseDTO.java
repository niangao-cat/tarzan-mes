package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * description 计划达成率报表响应DTO
 *
 * @author quan.luo@hand-china.com 2020/11/25 20:37
 */
@Data
public class HmePlanRateReportResponseDTO implements Serializable {

    private static final long serialVersionUID = -1767826825277120384L;

    @ApiModelProperty(value = "时间")
    private LocalDate dataTime;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    private List<HmePlanRateReportRateDTO> workcells;
}
