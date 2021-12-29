package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 出库任务状态回传接口参数  API
 *
 * @author taowen.wang@hand-china.com 2021/7/2 16:23
 */
@Data
public class ItfWcsTaskIfaceDTO1 {
    @ApiModelProperty(value = "任务号")
    private String taskNum;
    @ApiModelProperty(value = "执行状态")
    private String taskStatus;
}
