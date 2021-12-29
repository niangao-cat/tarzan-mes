package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

@Data
public class WmsStandingWarehouseOutboundPlatformReturnDTO3 implements Serializable {
    private static final long serialVersionUID = -7925948151835550238L;
    @ApiModelProperty(value = "日出库任务状态汇总")
    @LovValue(lovCode = "WX.WMS.TASK_STATUS", meaningField = "statusListMeaning")
    private String statusList;
    @ApiModelProperty(value = "日出库任务状态意义")
    private String statusListMeaning;
    @ApiModelProperty(value = "日出库任务汇总数量")
    private String counts;
}

