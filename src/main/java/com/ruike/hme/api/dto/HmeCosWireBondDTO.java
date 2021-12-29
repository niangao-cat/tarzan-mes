package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HmeCosWireBondDTO implements Serializable {

    private static final long serialVersionUID = -7845128682523095582L;

    @ApiModelProperty(value = "默认站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "设备编码")
    private String equipmentCode;

    @ApiModelProperty(value = "扫描条码")
    private String materialLotCode;

    @ApiModelProperty(value = "工艺路线ID", required = true)
    private String operationId;

    @ApiModelProperty(value = "班次Id", required = true)
    private String wkcShiftId;
}
