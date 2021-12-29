package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * COS芯片退料扫描参数
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/12 15:14
 */
@Data
public class HmeCosReturnScanDTO {
    @ApiModelProperty("条码")
    @NotBlank
    private String materialLotCode;
    @ApiModelProperty("工单id")
    @NotBlank
    private String workOrderId;
    @ApiModelProperty("物料")
    @NotBlank
    private String materialId;
    @ApiModelProperty("退料类型")
    @NotBlank
    @Pattern(regexp = "(CHIP|HOT_SINK|WIRE_BOND)")
    private String returnType;

}
