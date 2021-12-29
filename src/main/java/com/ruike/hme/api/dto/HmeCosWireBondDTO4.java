package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeCosWireBondDTO4
 *
 * @author: chaonan.hu@hand-china.com 2020/10/3 16:43
 **/
@Data
public class HmeCosWireBondDTO4 implements Serializable {
    private static final long serialVersionUID = 6397388250254893843L;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工艺路线ID", required = true)
    private String operationId;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工单ID", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "物料批ID集合", required = true)
    private List<String> materialLotIdList;
}
