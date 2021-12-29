package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * 在制品盘点 物料明细查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/9 15:30
 */
@Data
public class WipStocktakeMaterialDetailQueryDTO implements Serializable {
    private static final long serialVersionUID = -7413869643454205592L;

    @ApiModelProperty(value = "盘点单ID", required = true)
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty("盘点类型，初盘=FIRST_COUNT，复盘=RECOUNT")
    @Pattern(regexp = "^FIRST_COUNT$|^RECOUNT$", message = "盘点类型必须为初盘=FIRST_COUNT或复盘=RECOUNT")
    @NotBlank
    private String stocktakeTypeCode;
    @ApiModelProperty("物料编码")
    private String materialCode;
}
