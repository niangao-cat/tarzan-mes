package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeImportMaterialLotVO implements Serializable {

    private static final long serialVersionUID = -8439702390543059421L;

    // mt_material_lot
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "工厂Id")
    private String siteId;

    @ApiModelProperty(value = "工厂Code")
    private String siteCode;

    @ApiModelProperty(value = "条码号ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码号Code")
    private String materialLotCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "数量")
    private String primaryUomQty;

    @ApiModelProperty(value = "单位ID")
    private String primaryUomId;

    @ApiModelProperty(value = "单位Code")
    private String primaryUomCode;

    @ApiModelProperty(value = "仓库ID")
    private String parentLocatorId;

    @ApiModelProperty(value = "仓库Code")
    private String parentLocatorCode;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "货位Code")
    private String locatorCode;

    @ApiModelProperty(value = "批次号")
    private String lot;

    @ApiModelProperty(value = "物流器具ID")
    private String containerId;

    @ApiModelProperty(value = "物流器具")
    private String containerCode;

    @ApiModelProperty(value = "供应商")
    private String supplierId;

    @ApiModelProperty(value = "供应商ID")
    private String supplierCode;

    @ApiModelProperty(value = "当前质量状态")
    private String qualityStatus;


    // mt_material_lot_attr
    @ApiModelProperty(value = "条码号状态")
    private String status;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

}
