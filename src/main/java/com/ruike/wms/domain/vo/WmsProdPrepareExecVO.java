package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 成品备货执行数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 19:37
 */
@Data
public class WmsProdPrepareExecVO {
    @ApiModelProperty("单据行ID")
    private String instructionId;
    @ApiModelProperty("单据实际行ID")
    private String actualId;
    @ApiModelProperty("单据实际行明细ID")
    private String actualDetailId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("顶层容器ID")
    private String topContainerId;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("容器货位ID")
    private String containerLocatorId;
    @ApiModelProperty("顶层容器货位ID")
    private String topContainerLocatorId;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("实际发货仓库ID")
    private String actualFromLocatorId;
    @ApiModelProperty("目标仓库ID")
    private String targetWarehouseId;
    @ApiModelProperty("目标货位ID")
    private String targetLocatorId;
    @ApiModelProperty("目标站点ID")
    private String targetSiteId;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("明细实际数量")
    private BigDecimal detailActualQty;
}
