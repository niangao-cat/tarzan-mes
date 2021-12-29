package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * 在制盘点范围 扫描查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 11:18
 */
@Data
public class WipStocktakeRangeScanQueryDTO implements Serializable {
    private static final long serialVersionUID = 3114955708117583409L;

    @ApiModelProperty("盘点单ID")
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty(value = "范围对象类型", notes = "PL:产线，工序:WP")
    @NotBlank
    @Pattern(regexp = "^PL$|^WP$", message = "范围对象类型必须为PL或者WP")
    private String rangeObjectType;
    @ApiModelProperty("范围对象编码")
    @NotBlank
    private String rangeObjectCode;
}
