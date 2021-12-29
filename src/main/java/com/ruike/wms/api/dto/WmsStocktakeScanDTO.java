package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 库存盘点扫描查询DTO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 10:38
 */
@Data
public class WmsStocktakeScanDTO {
    @ApiModelProperty("盘点单号")
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty("明盘标志")
    @NotBlank
    private String openFlag;
    @ApiModelProperty("扫描货位ID")
    @NotBlank
    private String locatorId;
    @ApiModelProperty("条码")
    @NotBlank
    private String barcode;
}
