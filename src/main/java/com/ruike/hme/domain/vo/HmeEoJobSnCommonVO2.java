package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/9 17:36
 */
@Data
public class HmeEoJobSnCommonVO2 implements Serializable {

    private static final long serialVersionUID = -3844663124107870216L;

    @ApiModelProperty("条码Id")
    private String materialLotId;
    @ApiModelProperty("条码")
    private String materialLotCode;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("供应商批次")
    private String supplierLot;
}
