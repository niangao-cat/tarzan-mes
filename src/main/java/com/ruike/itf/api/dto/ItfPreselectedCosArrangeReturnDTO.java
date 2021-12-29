package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @description COS芯片归集接口返回参数
 * @author 田欣
 * @email xin.t@raycuslaser.com
 * @date 2021/9/26
 * @time 18:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosArrangeReturnDTO implements Serializable {
    private static final long serialVersionUID = -7691192346879697926L;

    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String status;

    @ApiModelProperty(value = "列表")
    private List<ItfPreselectedCosArrangeShowDTO> list;
}
