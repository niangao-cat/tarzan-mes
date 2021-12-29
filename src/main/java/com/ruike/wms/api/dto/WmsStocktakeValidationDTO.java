package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 盘点执行 条码物料验证
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/17 16:36
 */
@Data
public class WmsStocktakeValidationDTO {
    @ApiModelProperty("盘点单")
    @NotBlank
    String stocktakeId;
    @ApiModelProperty("装载类型")
    @NotBlank
    String loadObjectType;
    @ApiModelProperty("装载对象ID")
    @NotBlank
    String loadObjectId;
    @ApiModelProperty("物料ID")
    String materialId;
}
