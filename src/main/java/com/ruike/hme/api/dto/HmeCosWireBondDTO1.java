package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeCosWireBondDTO1 implements Serializable {

    private static final long serialVersionUID = 1494560371857007527L;

    @ApiModelProperty(value = "默认站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工艺路线ID", required = true)
    private String operationId;

    @ApiModelProperty(value = "班次Id", required = true)
    private String wkcShiftId;

    @ApiModelProperty(value = "扫描条码Id", required = true)
    private String materialLotId;

    @ApiModelProperty(value = "来源条码作业记录Id", required = true)
    private String jobId;

}
