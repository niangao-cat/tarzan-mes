package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WmsDeliveryPrintVO implements Serializable {

    private static final long serialVersionUID = -5196421009418338263L;

    @ApiModelProperty("收货单编号")
    private String instructionDocNum;

    @ApiModelProperty("供应商")
    private String supplierName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("公司")
    private String customerName;

    @ApiModelProperty("收货单ID")
    private String instructionDocId;

    @ApiModelProperty("制单人")
    private String createdBy;
}

