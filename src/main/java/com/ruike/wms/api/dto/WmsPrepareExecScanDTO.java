package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 备料执行扫描参数DTO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 09:16
 */
@Data
public class WmsPrepareExecScanDTO {

    public static final String SCAN_TYPE_MAIN = "MAIN";
    public static final String SCAN_TYPE_SPLIT = "SPLIT";

    @ApiModelProperty(value = "单据行ID")
    private String instructionId;
    @ApiModelProperty(value = "条码")
    private String barcode;
    @ApiModelProperty(value = "扫描类型")
    private String scanType;
}
