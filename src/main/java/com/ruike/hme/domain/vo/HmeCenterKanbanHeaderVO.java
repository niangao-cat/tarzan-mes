package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCenterKanbanHeaderVO
 *
 * @author: chaonan.hu@hand-china.com 2022/05/28 15:21:11
 **/
@Data
public class HmeCenterKanbanHeaderVO implements Serializable {
    private static final long serialVersionUID = -5281097400765493369L;

    @ApiModelProperty(value = "头主键")
    private String centerKanbanHeaderId;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "看板区域")
    @LovValue(value = "HME.CENTER_KANBAN_AREA", meaningField = "kanbanAreaMeaning")
    private String kanbanArea;

    @ApiModelProperty(value = "看板区域含义")
    private String kanbanAreaMeaning;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;

    @ApiModelProperty(value = "部门ID")
    private String businessId;

    @ApiModelProperty(value = "部门编码")
    private String businessCode;

    @ApiModelProperty(value = "部门名称")
    private String businessName;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "产品组ID")
    private String productionGroupId;

    @ApiModelProperty(value = "产品组编码")
    private String productionGroupCode;

    @ApiModelProperty(value = "产品组名称")
    private String productionGroupName;

    @ApiModelProperty(value = "工序ID")
    private String workcellId;

    @ApiModelProperty(value = "工序编码")
    private String workcellCode;

    @ApiModelProperty(value = "工序名称")
    private String workcellName;

    @ApiModelProperty(value = "目标直通率")
    private BigDecimal throughRate;

    @ApiModelProperty(value = "有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性")
    private String enableFlagMeaning;
}
