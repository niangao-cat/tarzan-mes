package com.ruike.hme.api.dto.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 设备盘点单 修改命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 11:06
 */
@Data
public class HmeEquipmentStocktakeDocModifyCommand implements Serializable {
    private static final long serialVersionUID = 6852793710414669918L;

    @ApiModelProperty(value = "盘点单ID", required = true)
    @NotBlank
    private String stocktakeId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty("租户")
    private Long tenantId;
}
