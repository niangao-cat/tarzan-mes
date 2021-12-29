package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *  接口通用返回DTO
 * @author jiangling.zheng@hand-china.com 2020/7/22 21:44
 */
@Data
public class ItfCommonReturnDTO {

    @ApiModelProperty(value = "处理时间")
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;
}
