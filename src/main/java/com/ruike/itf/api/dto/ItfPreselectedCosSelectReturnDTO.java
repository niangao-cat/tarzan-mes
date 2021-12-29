package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @description COS挑选查询接口返回参数
 * @author 田欣
 * @email xin.t@raycuslaser.com
 * @date 2021/9/24
 * @time 18:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosSelectReturnDTO implements Serializable {
    private static final long serialVersionUID = 1774898227750825081L;

    @ApiModelProperty(value = "返回的芯片信息列表")
    private List<ItfPreselectedCosSelectReturnDTO1> returnSn;

    @ApiModelProperty(value = "返回的盒子剩余芯片数")
    private List<ItfPreselectedCosSelectReturnDTO2> returnSurplusNum;
}
