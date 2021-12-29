package com.ruike.wms.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WmsDistributionBasicDataDTO
 * @author: penglin.sui@hand-china.com 2020/7/29 20:27
 **/
@Data
public class WmsMaterialLotPrintVO  implements Serializable {

    private static final long serialVersionUID = 7773139231045849952L;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "条码号")
    private String materialLotCode;

    @ApiModelProperty(value = "物料")
    private String materialCode;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "数量")
    private String qty;

    @ApiModelProperty(value = "单位")
    private String uomName;

    @ApiModelProperty(value = "接收批次")
    private String lot;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "创建时间")
    private String creationDate;

    @ApiModelProperty(value = "外箱条码")
    private String outMaterialLotCode;
}
