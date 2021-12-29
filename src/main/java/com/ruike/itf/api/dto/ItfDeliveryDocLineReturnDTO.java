package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 送货单接口返回参数
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
@Data
public class ItfDeliveryDocLineReturnDTO extends ItfCommonReturnDTO {

    @ApiModelProperty(value = "指令行号")
    private String instructionLineNum;
}
