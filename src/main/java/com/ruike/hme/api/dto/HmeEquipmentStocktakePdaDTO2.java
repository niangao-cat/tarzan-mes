package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEquipmentStocktakePdaDTO2
 * 提交传参DTO
 * @author: chaonan.hu@hand-china.com 2021/4/1 16:55:42
 **/
@Data
public class HmeEquipmentStocktakePdaDTO2 implements Serializable {
    private static final long serialVersionUID = -3625869570952933259L;

    @ApiModelProperty(value = "盘点实绩ID", required = true)
    private String stocktakeActualId;

    @ApiModelProperty(value = "设备ID", required = true)
    private String equipmentId;

    @ApiModelProperty(value = "盘点时间", required = true)
    private String stocktakeDate;

    @ApiModelProperty(value = "设备状态", required = true)
    private String equipmentStatus;

    @ApiModelProperty(value = "备注")
    private String remark;
}
