package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeVisualInspectionDTO2
 *
 * @author: chaonan.hu@hand-china.com 2020/01/20 16:07:28
 **/
@Data
public class HmeVisualInspectionDTO2 implements Serializable {
    private static final long serialVersionUID = -2451340677886960231L;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工艺Id", required = true)
    private String operationId;

    @ApiModelProperty(value = "物料批编码", required = true)
    private String materialLotCode;

    @ApiModelProperty(value = "班次Id", required = true)
    private String wkcShiftId;
}
