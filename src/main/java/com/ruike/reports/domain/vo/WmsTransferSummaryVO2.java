package com.ruike.reports.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * WmsTransferSummaryVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/01/28 17:01:45
 **/
@Data
public class WmsTransferSummaryVO2 extends WmsTransferSummaryVO implements Serializable {
    private static final long serialVersionUID = -9122242164483552868L;

    @ApiModelProperty("签收条码号")
    private String materialLotCode;

    @ApiModelProperty("物料版本")
    private String detailMaterialVersion;

    @ApiModelProperty("物料批次")
    private String lot;

    @ApiModelProperty("供应商批次")
    private String supplierLot;

    @ApiModelProperty("容器编码")
    private String containerCode;

    @ApiModelProperty("调拨数量")
    private BigDecimal allocationQty;

    @ApiModelProperty("调拨操作人")
    private String allocationUser;

    @ApiModelProperty("调拨时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date allocationDate;
}
