package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * 盘点执行 条码明细
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 17:23
 */
@Data
public class WmsStocktakeMaterialLotVO {
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("盘点单ID")
    private String stocktakeId;
    @ApiModelProperty("盘点实际ID")
    private String stocktakeActualId;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("批次号")
    private String lotCode;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("账面数量")
    private BigDecimal currentQuantity;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("次序")
    private Integer sequence;
    @ApiModelProperty("初盘数量")
    private BigDecimal firstcountQuantity;
    @ApiModelProperty("初盘货位ID")
    private String firstcountLocatorId;
    @ApiModelProperty("初盘货位编码")
    private String firstcountLocatorCode;
    @ApiModelProperty("初盘容器ID")
    private String firstcountContainerId;
    @ApiModelProperty("初盘容器编码")
    private String firstcountContainerCode;
    @ApiModelProperty("复盘数量")
    private BigDecimal recountQuantity;
    @ApiModelProperty("复盘货位ID")
    private String recountLocatorId;
    @ApiModelProperty("复盘货位编码")
    private String recountLocatorCode;
    @ApiModelProperty("复盘容器ID")
    private String recountContainerId;
    @ApiModelProperty("复盘容器编码")
    private String recountContainerCode;
}
