package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-14 19:54:23
 **/
@Data
public class HmeNcDisposePlatformDTO24 implements Serializable {
    private static final long serialVersionUID = 2325578614791877525L;

    private String materialLotId;

    private String materialLotCode;

    @ApiModelProperty(value = "报废数量")
    private BigDecimal scrapQty;
}
