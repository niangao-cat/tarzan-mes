package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 配送签收物料批
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 10:17
 */
@Data
public class WmsDistributionSignDetailVO {
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("配送单行ID")
    private String instructionId;
    @ApiModelProperty("配送单ID")
    private String instructionDocId;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("容器编码")
    private String containerCode;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("条码货位ID")
    private String locatorId;
    @ApiModelProperty("目标站点ID")
    private String toSiteId;
    @ApiModelProperty("目标仓库ID")
    private String toWarehouseId;
    @ApiModelProperty("目标货位ID")
    private String toLocatorId;
    @ApiModelProperty("物料批批次")
    private String lot;
    @ApiModelProperty("主单位数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty("单位ID")
    private String primaryUomId;
    @ApiModelProperty("单位编码")
    private String primaryUomCode;
    @ApiModelProperty("指令实际明细数量")
    private BigDecimal actualQty;
    @ApiModelProperty(value = "销售订单")
    private String soNum;
    @ApiModelProperty(value = "销售订单行")
    private String soLineNum;
}
