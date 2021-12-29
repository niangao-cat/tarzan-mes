package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 数据收集统一返回DTO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com
 * @date 2020/7/13 7:23 下午
 */
@Data
public class DataCollectReturnDTO {

    @ApiModelProperty(value = "接口表ID")
    private String interfaceId;
    @ApiModelProperty(value = "处理时间")
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;
}
