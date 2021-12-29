package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 送货单查询接收参数类
 * @author: han.zhang
 * @create: 2020/04/07 10:47
 */
@Getter
@Setter
@ToString
public class WmsPoDeliveryScanDTO implements Serializable {
    private static final long serialVersionUID = -610702322839915596L;
    @ApiModelProperty(value = "instructionDocNum")
    private String instructionDocNum;
    @ApiModelProperty(value = "")
    private String instructionDocType;
}