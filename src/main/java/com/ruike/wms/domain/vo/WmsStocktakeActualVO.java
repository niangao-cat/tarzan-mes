package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.actual.domain.entity.MtStocktakeActual;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存盘点实绩
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 16:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("库存盘点实绩")
public class WmsStocktakeActualVO extends MtStocktakeActual implements Serializable {
    private static final long serialVersionUID = -6268708677153114181L;

    @ApiModelProperty(value = "盘点单据编号")
    private String stocktakeNum;
    @ApiModelProperty("实物条码")
    private String materialLotCode;
    @ApiModelProperty("条码状态")
    @LovValue(lovCode = "WMS.MTLOT.STATUS", meaningField = "materialLotStatusMeaning")
    private String materialLotStatus;
    @ApiModelProperty("条码状态含义")
    private String materialLotStatusMeaning;
    @ApiModelProperty("物料批是否有效")
    private String materialLotEnableFlag;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库编码")
    private String warehouseCode;
    @ApiModelProperty("差异数量")
    private BigDecimal differentQuantity;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("货位")
    private String locatorCode;
    @ApiModelProperty("容器条码")
    private String containerCode;
    @ApiModelProperty("质量状态")
    @LovValue(lovCode = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;
    @ApiModelProperty("质量状态含义")
    private String qualityStatusMeaning;
    @ApiModelProperty("初盘人")
    private String firstcountBy;
    @ApiModelProperty("初盘人用户名")
    private String firstcountByName;
    @ApiModelProperty("初盘时间")
    private String firstcountDate;
    @ApiModelProperty("复盘人")
    private String recountBy;
    @ApiModelProperty("复盘人用户名")
    private String recountByName;
    @ApiModelProperty("复盘时间")
    private String recountDate;
    @ApiModelProperty("调整人")
    private String adjustBy;
    @ApiModelProperty("调整人用户名")
    private String adjustByName;
    @ApiModelProperty("调整时间")
    private String adjustDate;
    @ApiModelProperty(value = "初盘货位编码")
    private String firstcountLocatorCode;
    @ApiModelProperty(value = "复盘货位编码")
    private String recountLocatorCode;
    @ApiModelProperty(value = "批次")
    private String lotCode;
    @ApiModelProperty(value = "重复标志")
    private String duplicateFlag;
}




