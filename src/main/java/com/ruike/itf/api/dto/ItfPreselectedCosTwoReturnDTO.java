package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @description COS接口1返回参数
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/1/19
 * @time 11:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosTwoReturnDTO implements Serializable {


    private static final long serialVersionUID = 351116452241328017L;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String status;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;

    @ApiModelProperty(value = "列表")
    private List<ItfPreselectedCosTwoShowDTO> list;

}
