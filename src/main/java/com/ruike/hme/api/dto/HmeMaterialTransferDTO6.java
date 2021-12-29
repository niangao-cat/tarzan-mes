package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 12:00
 */
@Data
public class HmeMaterialTransferDTO6 implements Serializable {

    private static final long serialVersionUID = 2527237833385752484L;

    @ApiModelProperty(value = "目标物料批Id")
    private String targetMaterialLotId;
    @ApiModelProperty(value = "目标物料批编码")
    private String targetMaterialLotCode;
    @ApiModelProperty(value = "目标数量")
    private Double targetQty;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
}
