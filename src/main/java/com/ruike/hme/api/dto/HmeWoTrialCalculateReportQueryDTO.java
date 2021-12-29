package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeTrialCalculateReportQueryDTO
 * @Description 工单试算报表查询参数
 * @Date 2020/8/26 16:29
 * @Author yuchao.wang
 */
@Data
public class HmeWoTrialCalculateReportQueryDTO implements Serializable {
    private static final long serialVersionUID = -544360445291287681L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "产线ID")
    private String productionLineId;

    @ApiModelProperty(value = "工单类型")
    private String workOrderType;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;
}