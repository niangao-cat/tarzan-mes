package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 12:00
 */
@Data
public class HmeMaterialTransferDTO5 {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "目标物料批编码")
    private String targetMaterialLotCode;
    @ApiModelProperty(value = "目标数量")
    private Double targetQty;

    private List<HmeMaterialTransferDTO> dtoList;
}
