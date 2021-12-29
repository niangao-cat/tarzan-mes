package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @auther:lkj
 * @Date:2020/8/3 17:47
 * @E-mail:kejin.liu@hand-china.com
 * @Description: 异常信息接口传参
 */
@Data
public class ItfExceptionWkcRecordDTO {

    @ApiModelProperty("异常信息主键")
    String exceptionWkcRecordId;

    @ApiModelProperty("异常信息类型")
    String exceptionType;

    @ApiModelProperty("异常信息编码")
    String exceptionCode;

    @ApiModelProperty("异常信息名称")
    String exceptionName;

    @ApiModelProperty("异常信息描述")
    String exceptionRemark;

    @ApiModelProperty("工位名称")
    String workcellName;

    @ApiModelProperty("当前时间")
    String currentTime;

    @ApiModelProperty("发起者")
    ItfExceptionUserInfoDTO initiator;

    @ApiModelProperty("岗位和岗位人员")
    List<ItfExceptionDTO> exceptions;

}
