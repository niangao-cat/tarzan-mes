package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @description COS挑选查询接口返回列表2 - 盒子剩余芯片数
 * @author 田欣
 * @email xin.t@raycuslaser.com
 * @date 2021/9/24
 * @time 18:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosSelectReturnDTO2 implements Serializable {
    private static final long serialVersionUID = 3782101421713864023L;

    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "剩余芯片数")
    private Double surplusNum;
}
