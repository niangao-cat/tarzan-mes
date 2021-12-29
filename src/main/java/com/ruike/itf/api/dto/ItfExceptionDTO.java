package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @auther:lkj
 * @Date:2020/8/4 12:37
 * @E-mail:kejin.liu@hand-china.com
 * @Description: 异常信息接口传参
 */
@Data
public class ItfExceptionDTO {

    @ApiModelProperty("反馈岗位")
    String respondPositionName;

    @ApiModelProperty("异常等级")
    Integer exceptionLevel;

    @ApiModelProperty("升级时长")
    Integer upgradeTime;

    @ApiModelProperty("审批人")
    List<ItfExceptionUserInfoDTO> approvedBy;

}
