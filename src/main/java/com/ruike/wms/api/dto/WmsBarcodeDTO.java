package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 条码
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 10:19
 */
@Data
public class WmsBarcodeDTO {
    @ApiModelProperty(value = "条码ID")
    private String loadObjectId;
    @ApiModelProperty(value = "条码类型")
    private String loadObjectType;
    @ApiModelProperty(value = "单位ID")
    private String primaryUomId;
    @ApiModelProperty(value = "主要数量")
    private BigDecimal primaryUomQty;
}
