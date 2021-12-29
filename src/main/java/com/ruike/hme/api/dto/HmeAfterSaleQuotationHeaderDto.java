package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeAfterSaleQuotationHeaderDto
 *
 * @author: chaonan.hu@hand-china.com 2021/09/29 11:20
 **/
@Data
public class HmeAfterSaleQuotationHeaderDto implements Serializable {
    private static final long serialVersionUID = -6962599820297704358L;

    @ApiModelProperty(value = "SN编码对应的物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "扫描SN")
    private String snNum;

    @ApiModelProperty(value = "物料ID", required = true)
    private String materialId;

    @ApiModelProperty(value = "送达方ID", required = true)
    private String sendTo;
}
