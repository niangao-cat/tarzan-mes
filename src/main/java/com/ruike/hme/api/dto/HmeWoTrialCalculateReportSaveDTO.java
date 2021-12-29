package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.time.*;

/**
 * @Classname HmeWoTrialCalculateReportSaveDTO
 * @Description 工单试算报表保存参数
 * @Date 2020/8/26 16:29
 * @Author yuchao.wang
 */
@Data
public class HmeWoTrialCalculateReportSaveDTO implements Serializable {
    private static final long serialVersionUID = 7575872835362573162L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "产线ID")
    private String productionLineId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private LocalDate dateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private LocalDate dateTo;
}