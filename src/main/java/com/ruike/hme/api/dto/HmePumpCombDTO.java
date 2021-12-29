package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/22 16:13
 */
@Data
public class HmePumpCombDTO implements Serializable {

    private static final long serialVersionUID = -7853543730879990202L;

    @ApiModelProperty("作业Id")
    private String jobId;
    @ApiModelProperty("泵浦源数量")
    private BigDecimal pumpReqQty;
    @ApiModelProperty("子条码")
    private String subBarcode;
    @ApiModelProperty("子条码顺序")
    private Long subBarcodeSeq;
    @ApiModelProperty("泵浦源物料批ID")
    private String materialLotId;
}
