package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 库存调拨平台关闭按钮:返回消息
 *
 * @author taowen.wang@hand-china.com
 * @version 1.0
 * @date 2021/7/16 15:20
 */
@Data
public class WmsInstructionReturnDTO {
    @ApiModelProperty(value = "处理时间")
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;
}
