package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/***
 * @description COS接口1传入参数
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/1/19
 * @time 11:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosOneDTO implements Serializable {

    private static final long serialVersionUID = 4649258161052396432L;

    @ApiModelProperty(value = "盒子号" , required = true)
    private String materialLotCode;


}
