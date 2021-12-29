package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/31 9:57
 */
@Data
public class HmePumpCombVO implements Serializable {

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
