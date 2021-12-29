package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 备料执行提交执行
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 10:53
 */
@Data
public class WmsPrepareExecScannedDTO {
    @ApiModelProperty(value = "单据行ID")
    private String instructionId;
    @ApiModelProperty(value = "条码ID")
    private String loadObjectId;
    @ApiModelProperty(value = "条码类型")
    private String loadObjectType;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "实际数量")
    private BigDecimal actualQty;
}
