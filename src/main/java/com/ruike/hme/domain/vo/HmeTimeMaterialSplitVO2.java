package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeTimeMaterialSplitVO2
 *
 * @author: chaonan.hu@hand-china.com 2020/9/12 16:10:34
 **/
@Data
public class HmeTimeMaterialSplitVO2 implements Serializable {
    private static final long serialVersionUID = -1893976941873467724L;

    @ApiModelProperty(value = "目标条码Id")
    private String targetMaterialLotId;

    @ApiModelProperty(value = "目标条码编码")
    private String targetMaterialLotCode;

    @ApiModelProperty(value = "目标条码有效期从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date targetDateTimeFrom;

    @ApiModelProperty(value = "目标条码有效期至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date targetDateTimeTo;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
}
