package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeTimeMaterialSplitDTO2
 *
 * @author: chaonan.hu@hand-china.com 2020/09/12 15:33:28
 **/
@Data
public class HmeTimeMaterialSplitDTO2 implements Serializable {
    private static final long serialVersionUID = 7563395885536930994L;

    @ApiModelProperty(value = "来源条码ID",required = true)
    private String sourceMaterialLotId;

    @ApiModelProperty(value = "目标条码编码")
    private String targetMaterialLotCode;

    @ApiModelProperty(value = "时效时长",required = true)
    private int minute;

    @ApiModelProperty(value = "分装数量",required = true)
    private BigDecimal qty;

    @ApiModelProperty(value = "单位",required = true)
    private String timeUom;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "有效期起")
    private String dateTimeFrom;

    @ApiModelProperty(value = "有效期至")
    private String dateTimeTo;
}
