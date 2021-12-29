package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/25 17:20
 */
@Data
public class HmeCosRetestVO11 implements Serializable {

    private static final long serialVersionUID = 1581266919975866390L;

    @ApiModelProperty(value = "条码Id")
    private String materialLotId;

    @ApiModelProperty(value = "条码编码")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private Double primaryUomQty;

    @ApiModelProperty(value = "转移数量")
    private Double trxPrimaryUomQty;
}
