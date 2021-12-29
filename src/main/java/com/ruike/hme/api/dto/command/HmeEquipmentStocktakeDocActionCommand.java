package com.ruike.hme.api.dto.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * 设备盘点单 操作命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 17:07
 */
@Data
public class HmeEquipmentStocktakeDocActionCommand implements Serializable {
    private static final long serialVersionUID = -943255007411854351L;

    @ApiModelProperty(value = "盘点单ID", required = true)
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty(value = "操作", required = true)
    @NotBlank
    @Pattern(regexp = "^COMPLETE$|^CANCEL$", message = "操作无效")
    private String action;
    @ApiModelProperty("租户")
    private Long tenantId;
}
