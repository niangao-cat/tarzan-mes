package com.ruike.hme.api.dto.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/1 15:10
 */
@Data
public class HmeEquipmentStocktakeActualModifyCommand implements Serializable {
    private static final long serialVersionUID = 450921233057258308L;

    @ApiModelProperty(value = "盘点单行ID", required = true)
    @NotBlank
    private String stocktakeActualId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty("租户")
    private Long tenantId;
}
