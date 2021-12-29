package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsMaterialLotAddDTO implements Serializable {
    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "条码id")
    private String materialLotId;

    @ApiModelProperty(value = "条码code")
    private String materialLotCode;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "关联物料")
    private String materialId;

    @ApiModelProperty(value = "批次号")
    private String lot;

    @ApiModelProperty(value = "生产日期")
    private String ProductDate;

    @ApiModelProperty(value = "单位")
    private String primaryUomId;
    @ApiModelProperty(value = "物料数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "创建原因")
    private String createReason;

    @ApiModelProperty(value = "色温")
    private String colorBin;

    @ApiModelProperty(value = "亮度")
    private String lightBin;

    @ApiModelProperty(value = "电压")
    private String voltageBin;

    @ApiModelProperty(value = "创建数量")
    private Integer createQty;

    @ApiModelProperty(value = "性能等级")
    private String performanceLevel;

    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料版本")
    private String SiteId;

    @ApiModelProperty(value = "销售订单")
    private String soNum;

    @ApiModelProperty(value = "销售订单行")
    private String soLineNum;

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

}
