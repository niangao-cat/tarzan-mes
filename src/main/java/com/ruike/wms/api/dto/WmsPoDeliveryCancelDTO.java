package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: tarzan-mes
 * @description: 取消送货单API参数
 * @author: han.zhang
 * @create: 2020/04/08 21:14
 */
@Getter
@Setter
@ToString
public class WmsPoDeliveryCancelDTO {
    @ApiModelProperty(value = "送货单id")
    private String instructionDocId;
}