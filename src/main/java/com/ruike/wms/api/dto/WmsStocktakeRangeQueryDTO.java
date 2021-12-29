package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 盘点范围查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/12 11:30
 */
@Data
public class WmsStocktakeRangeQueryDTO {
    @ApiModelProperty("盘点单据ID")
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty("范围对象类型")
    @NotBlank
    private String rangeObjectType;
    @ApiModelProperty("范围对象编码")
    private String rangeObjectCode;
    @ApiModelProperty("范围对象名称")
    private String rangeObjectName;
}
