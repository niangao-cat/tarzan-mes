package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description mes接口返回参数
 *
 * @author wengang.qiang@hand-chian.com 2021/09/29 17:23
 */
@Data
public class HmeCosTestMonitorResponseVO implements Serializable {

    private static final long serialVersionUID = -8505979547685948812L;

    @ApiModelProperty(value = "处理结果")
    private String success;

    @ApiModelProperty(value = "错误编码")
    private String code;

    @ApiModelProperty(value = "返回消息")
    private String message;

}
