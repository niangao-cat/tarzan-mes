package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: chaonan.hu@hand-china.com 2020-09-10 15:10:34
 **/
@Data
public class HmeNcDisposePlatformDTO28 implements Serializable {
    private static final long serialVersionUID = 4832710910435182288L;

    @ApiModelProperty(value = "扫描条码")
    private String materialLotCode;

    @ApiModelProperty(value = "报废数量")
    private BigDecimal scrapQty;
}
