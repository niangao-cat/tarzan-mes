package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 采购订单行查询
 * @author: han.zhang
 * @create: 2020/04/28 19:34
 */
@Getter
@Setter
@ToString
public class WmsAvailQuantityQueryDTO implements Serializable {
    private static final long serialVersionUID = -8997630607692211478L;

    @ApiModelProperty(value = "行id")
    private List<String> instructionIdList;
}