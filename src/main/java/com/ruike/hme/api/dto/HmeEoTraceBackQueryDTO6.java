package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 产品追溯
 *
 * @author chaonan.hu@hand-china.com 2020-07-15 09:38:09
 */
@Data
public class HmeEoTraceBackQueryDTO6 implements Serializable {
    private static final long serialVersionUID = -5733068526664668519L;

    @ApiModelProperty(value = "SN条码或当前层的序列号")
    private String materialLotCode;

    @ApiModelProperty(value = "内外层查询标识")
    private String parentType;
}
