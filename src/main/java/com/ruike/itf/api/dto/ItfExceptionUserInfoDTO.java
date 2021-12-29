package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @auther:lkj
 * @Date:2020/8/4 12:38
 * @E-mail:kejin.liu@hand-china.com
 * @Description: 异常信息接口传参
 */
@Data
public class ItfExceptionUserInfoDTO {


    @ApiModelProperty("员工编码")
    String employeeCode;

    @ApiModelProperty("用户名称")
    String realName;

    @ApiModelProperty("用户电话")
    String mobile;

    @ApiModelProperty("用户邮箱")
    String email;

}
