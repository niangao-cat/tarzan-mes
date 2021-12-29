package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 库存盘点 盘点执行
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/17 10:05
 */
@Data
public class WmsStocktakeCountExecuteDTO {
    @ApiModelProperty("物料批")
    private String materialLotId;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("货位")
    private String locatorId;
    @ApiModelProperty("容器")
    private String containerId;
    @ApiModelProperty("数量")
    private BigDecimal quantity;
}
