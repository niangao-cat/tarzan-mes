package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 库存盘点条码扫描结果
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/16 19:28
 */
@Data
public class WmsStocktakeBarcodeVO {
    @ApiModelProperty("条码类型")
    private String loadObjectType;
    @ApiModelProperty("条码ID")
    private String loadObjectId;
    @ApiModelProperty("条码编码")
    private String loadObjectCode;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("盘点实际ID")
    private String stocktakeActualId;
}
