package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeCosPatchPdaDTO3
 *
 * @author: chaonan.hu@hand-china.com 2020/8/25 15:52:28
 **/
@Data
public class HmeCosPatchPdaDTO3 implements Serializable {
    private static final long serialVersionUID = -3371388495111304059L;

    @ApiModelProperty(value = "扫描条码Id", required = true)
    private String materialLotId;

    @ApiModelProperty(value = "来源条码作业记录Id", required = true)
    private String jobId;

    @ApiModelProperty(value = "工单Id", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

    @ApiModelProperty(value = "班次日历ID", required = true)
    private String wkcShiftId;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;
}
