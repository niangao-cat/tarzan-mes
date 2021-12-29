package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmePumpPreSelectionDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021/08/31 09:43:26
 **/
@Data
public class HmePumpPreSelectionDTO2 implements Serializable {
    private static final long serialVersionUID = 9134687020447319183L;

    @ApiModelProperty(value = "组合物料编码")
    private String combMaterialCode;

    @ApiModelProperty(value = "BOM版本号")
    private String revision;
}
