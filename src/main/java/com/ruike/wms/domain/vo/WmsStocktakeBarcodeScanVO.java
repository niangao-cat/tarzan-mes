package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存盘点实绩 条码扫描结果
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 17:12
 */
@Data
public class WmsStocktakeBarcodeScanVO {
    @ApiModelProperty("盘点单ID")
    private String stocktakeId;
    @ApiModelProperty("条码类型")
    private String loadObjectType;
    @ApiModelProperty("条码ID")
    private String loadObjectId;
    @ApiModelProperty("条码编码")
    private String loadObjectCode;
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("数量")
    private BigDecimal quantity;
    @ApiModelProperty("条码个数")
    private Integer lotCount;
    @ApiModelProperty("货位是否一致")
    private Boolean locatorFlag;
}
