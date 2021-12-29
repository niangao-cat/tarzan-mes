package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料条码接口返回参数
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
@Data
public class ItfMaterialLotReturnDTO extends ItfCommonReturnDTO {

    @ApiModelProperty(value = "条码号")
    private String materialLotCode;
}
