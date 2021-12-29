package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 拆分参数
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 16:29
 */
@Data
public class WmsPrepareExecSplitDTO {
    @ApiModelProperty(value = "配送单行ID")
    private String instructionId;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "拆分数量")
    private BigDecimal splitQty;
    @ApiModelProperty(value = "自定义条码编号")
    private String customMaterialLotCode;
}
