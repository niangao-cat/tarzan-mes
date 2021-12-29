package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/1 16:32
 */
@Data
public class HmeMakeCenterProduceBoardVO10 implements Serializable {

    private static final long serialVersionUID = 6241562591827368435L;

    @ApiModelProperty("看板信息主键")
    private String centerKanbanHeaderId;
    @ApiModelProperty("目标直通率")
    private BigDecimal targetThroughRate;
    @ApiModelProperty(value = "产品组")
    private String productionGroupCode;
    @ApiModelProperty(value = "产品组ID")
    private String productionGroupId;
    @ApiModelProperty(value = "产线")
    private String prodLineId;
    @ApiModelProperty(value = "产线描述")
    private String prodLineName;
    @ApiModelProperty("COS标识")
    private String cosFlag;
    @ApiModelProperty("产线顺序")
    private Long prodLineOrder;
    @ApiModelProperty(value = "物料")
    private String materialId;
    @ApiModelProperty(value = "产品组版本")
    private String productionVersion;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "工单")
    private List<String> workOrderIdList;
}
