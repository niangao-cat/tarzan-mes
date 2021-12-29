package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeCenterKanbanDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021/05/28 15:29:16
 **/
@Data
public class HmeCenterKanbanHeaderDTO2 implements Serializable {
    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "看板区域")
    private String kanbanArea;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "产品组ID")
    private String productionGroupId;

    @ApiModelProperty(value = "工序ID")
    private String workcellId;
}
