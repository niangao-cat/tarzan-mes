package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 12:00
 */
@Data
public class HmeMaterialTransferDTO7 implements Serializable {

    private static final long serialVersionUID = 5772408852559970170L;

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
    @ApiModelProperty(value = "条码数量")
    private Double totalQty;
    @ApiModelProperty(value = "待转移总数量")
    private Double totalTransferQty;
    @ApiModelProperty(value = "目标条码Id")
    private String targetMaterialLotId;
    @ApiModelProperty(value = "目标条码编码")
    private String targetMaterialLotCode;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
}
