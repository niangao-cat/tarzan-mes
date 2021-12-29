package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/25 0:38
 */
@Data
public class HmeCosRetestVO10 implements Serializable {

    private static final long serialVersionUID = -8144340476303032847L;

    @ApiModelProperty(value = "条码Id")
    private String materialLotId;

    @ApiModelProperty(value = "条码编码")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private Double primaryUomQty;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;
}
