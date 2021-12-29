package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeCosPatchPdaDTO5
 *
 * @author: chaonan.hu@hand-china.com 2020/8/27 10:48:23
 **/
@Data
public class HmeCosPatchPdaDTO5 implements Serializable {
    private static final long serialVersionUID = 6993224221402495351L;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

    @ApiModelProperty(value = "工单Id", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "wafer", required = true)
    private String wafer;

    @ApiModelProperty(value = "设备ID")
    private String equipmentId;

    @ApiModelProperty(value = "班次日历ID", required = true)
    private String wkcShiftId;

    @ApiModelProperty(value = "作业记录ID集合", required = true)
    private List<String> jobIdList;

    @ApiModelProperty(value = "工段ID", required = true)
    private String wkcLineId;
}
