package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 12:00
 */
@Data
public class HmeMaterialTransferDTO4 {

    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "有效日期")
    private Date effectiveDate;
    @ApiModelProperty(value = "目标数量")
    private Double targetQty;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
}
