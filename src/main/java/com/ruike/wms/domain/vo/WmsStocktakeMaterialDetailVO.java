package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 盘点物料明细
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/10 15:19
 */
@Data
public class WmsStocktakeMaterialDetailVO {
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("批次")
    private String lotCode;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("账面数量")
    private BigDecimal currentQuantity;
    @ApiModelProperty("初盘数量")
    private BigDecimal firstcountQuantity;
    @ApiModelProperty("复盘数量")
    private BigDecimal recountQuantity;
    @ApiModelProperty("初盘差异数量")
    private BigDecimal firstcountDifferentQuantity;
    @ApiModelProperty("复盘差异数量")
    private BigDecimal recountDifferentQuantity;
}
