package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCenterKanbanDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/05/28 13:58:42
 **/
@Data
public class HmeCenterKanbanHeaderDTO implements Serializable {
    private static final long serialVersionUID = -4332872127278553948L;

    @ApiModelProperty(value = "头主键,传则更新,不传则新增")
    private String centerKanbanHeaderId;

    @ApiModelProperty(value = "站点ID",required = true)
    private String siteId;

    @ApiModelProperty(value = "看板区域",required = true)
    private String kanbanArea;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "部门ID")
    private String businessId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "产品组ID")
    private String productionGroupId;

    @ApiModelProperty(value = "工序ID")
    private String workcellId;

    @ApiModelProperty(value = "目标直通率")
    private BigDecimal throughRate;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;
}
