package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 获取可制单数量
 * @author: han.zhang
 * @create: 2020/04/29 11:08
 */
@Getter
@Setter
@ToString
public class WmsAvailQuantityGetDTO implements Serializable {
    private static final long serialVersionUID = 898260381157375853L;

    @ApiModelProperty(value = "行Id")
    private List<String> instructionIdList;
}