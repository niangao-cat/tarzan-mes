package com.ruike.reports.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * WmsTransferSummaryQueryDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021/01/28 17:01:45
 **/
@Data
public class WmsTransferSummaryQueryDTO2 extends WmsTransferSummaryQueryDTO implements Serializable {
    private static final long serialVersionUID = 9102072917828973173L;

    @ApiModelProperty("签收条码号")
    private String materialLotCode;

    @ApiModelProperty("物料批次")
    private String lot;

    @ApiModelProperty("供应商批次")
    private String supplierLot;

    @ApiModelProperty("调拨操作人")
    private String allocationUserId;

    @ApiModelProperty("调拨操作从")
    private Date allocationDateFrom;

    @ApiModelProperty("调拨操作至")
    private Date allocationDateTo;
}
