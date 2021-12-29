package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * WmsStockTransferDTO2
 *
 * @author: chaonan.hu@hand-china.com 2020-09-21 17:01
 **/
@Data
public class WmsStockTransferDTO2 implements Serializable {
    private static final long serialVersionUID = 1312098664085115288L;

    @ApiModelProperty(value = "收货单编号")
    private String instructionDocNum;

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "工厂")
    private String siteName;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    @ApiModelProperty(value = "单据类型")
    private String instructionDocTypeMeaning;
}
