package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @description COS接口2传入参数
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/1/19
 * @time 11:06
 * @version 0.0.1
 * @return
 */
@Data
public class ItfPreselectedCosTwoDTO implements Serializable {

    private static final long serialVersionUID = -4782695457007734020L;

    @ApiModelProperty(value = "虚拟号", required = true)
    private String virtualNum;

}
