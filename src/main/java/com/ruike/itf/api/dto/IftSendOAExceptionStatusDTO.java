package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 异常工单回传状态
 *
 * @author kejin.liu01@hand-china.com 2020/9/1 11:04
 */

@Data
public class IftSendOAExceptionStatusDTO {
    @ApiModelProperty("异常信息主键")
    private String exceptionWkcRecordId;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("响应时间，当前时间")
    private String respondTime;
    @ApiModelProperty("响应人")
    private String respondedBy;
    @ApiModelProperty("响应备注")
    private String respondRemark;
    @ApiModelProperty("返回消息")
    private String message;

}
