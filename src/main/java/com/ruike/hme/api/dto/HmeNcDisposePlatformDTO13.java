package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-01 10:27:11
 **/
@Data
public class HmeNcDisposePlatformDTO13 implements Serializable {
    private static final long serialVersionUID = 1750917009606553426L;

    private Long number;

    private String materialId;

    private String materialCode;

    private String materialName;

    private String materialLotId;

    private String materialLotCode;

    private String ncComponentTempId;

    @ApiModelProperty(value = "报废数量")
    private String scrapQtyStr;

    @ApiModelProperty(value = "报废数量")
    private BigDecimal scrapQty;

}
