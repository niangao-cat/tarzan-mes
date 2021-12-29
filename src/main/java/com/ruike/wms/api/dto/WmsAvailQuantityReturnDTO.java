package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 获得可制单数量API返回值
 * @author: han.zhang
 * @create: 2020/04/28 21:37
 */
@Getter
@Setter
@ToString
public class WmsAvailQuantityReturnDTO implements Serializable {
    private static final long serialVersionUID = -358746358659313157L;

    @ApiModelProperty(value = "行id")
    private String instructionId;

    @ApiModelProperty(value = "可制单数量")
    private BigDecimal availableOrderQuantity;
}