package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/19 14:09
 */
@Data
public class HmeCosRetestVO3 implements Serializable {

    private static final long serialVersionUID = -7313118107900674832L;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "工单")
    private String workOrderId;

    @ApiModelProperty(value = "班次日历")
    private String wkcShiftId;

    @ApiModelProperty("物料")
    private String materialId;

    @ApiModelProperty(value = "扫描的条码")
    private List<HmeCosRetestVO2> scanBarcodeList;
}
