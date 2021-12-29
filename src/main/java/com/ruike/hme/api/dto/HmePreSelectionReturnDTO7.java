package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionReturnDTO7
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/11/2 17:17
 * @Version 1.0
 **/
@Data
public class HmePreSelectionReturnDTO7 implements Serializable {
    private static final long serialVersionUID = -8820163441151221982L;
    @ApiModelProperty(value = "物料批")
    private String materialLotCode;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "数量")
    private Double primaryUomQty;
}
