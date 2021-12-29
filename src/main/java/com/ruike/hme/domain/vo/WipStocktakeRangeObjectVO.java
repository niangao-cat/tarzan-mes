package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 09:09
 */
@Data
public class WipStocktakeRangeObjectVO {
    @ApiModelProperty("范围对象类型")
    @NotBlank
    private String rangeObjectType;
    @ApiModelProperty("范围对象ID")
    @NotBlank
    private String rangeObjectId;
}
