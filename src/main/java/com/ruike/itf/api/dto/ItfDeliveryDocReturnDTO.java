package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 送货单接口返回参数
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
@Data
public class ItfDeliveryDocReturnDTO extends ItfCommonReturnDTO {

    @ApiModelProperty(value = "送货单编号")
    private String instructionDocNum;
    @ApiModelProperty(value = "送货单行数据")
    private List<ItfDeliveryDocLineReturnDTO> lineReturnDTOList;
}
