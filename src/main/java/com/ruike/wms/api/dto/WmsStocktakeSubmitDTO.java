package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * <p>
 * 盘点执行 提交参数
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/16 20:02
 */
@Data
public class WmsStocktakeSubmitDTO {

    @ApiModelProperty("盘点单ID")
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty("盘点类型，初盘=FIRST_COUNT，复盘=RECOUNT")
    @Pattern(regexp = "^FIRST_COUNT$|^RECOUNT$", message = "盘点类型必须为初盘=FIRST_COUNT或复盘=RECOUNT")
    @NotBlank
    private String stocktakeTypeCode;
    @ApiModelProperty("条码类型")
    @NotBlank
    private String loadObjectType;
    @ApiModelProperty("条码ID")
    @NotBlank
    private String loadObjectId;
    @ApiModelProperty("条码编码")
    @NotBlank
    private String loadObjectCode;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("货位ID")
    @NotBlank
    private String locatorId;
    @ApiModelProperty("数量")
    private BigDecimal quantity;
    @ApiModelProperty("备注")
    private String remark;
}
