package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @description COS挑选查询接口返回列表1 - 虚拟号列表
 * @author 田欣
 * @email xin.t@raycuslaser.com
 * @date 2021/9/24
 * @time 18:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosSelectReturnDTO1 implements Serializable {

    private static final long serialVersionUID = -143557852136424902L;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "虚拟号")
    private List<String> virtualNum;

    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String status;

    @ApiModelProperty(value = "列表")
    private List<ItfPreselectedCosSelectShowDTO> list;
}
