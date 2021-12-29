package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeCosGetChipDeleteDTO
 *
 * @author: chaonan.hu@hand-china.com 2020-11-03 10:25
 **/
@Data
public class HmeCosGetChipDeleteDTO implements Serializable {
    private static final long serialVersionUID = 1015535051791712597L;

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
