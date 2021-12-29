package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HmeCosWireBondDTO2 implements Serializable {

    private static final long serialVersionUID = 145205839788268563L;
    @ApiModelProperty(value = "默认站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工艺路线ID", required = true)
    private String operationId;

    @ApiModelProperty(value = "班次Id", required = true)
    private String wkcShiftId;

    @ApiModelProperty(value = "来源条码作业记录Id", required = true)
    private String jobId;

    @ApiModelProperty(value = "工单id")
    private String workOrderId;

    @ApiModelProperty(value = "投料条码", required = true)
    private List<HmeCosWireBondDTO3> materialLotList;

}
