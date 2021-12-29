package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class HmeImportCosFunctionVO implements Serializable {

    private static final long serialVersionUID = -7394365654925500377L;

    @ApiModelProperty(value = "租户")
    private Long tenantId;

    // mt_material_lot
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

    @ApiModelProperty(value = "单位")
    private String primaryUomId;

    @ApiModelProperty(value = "数量")
    private String primaryUomQty;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "货位Code")
    private String locatorCode;

    @ApiModelProperty(value = "批次号")
    private String lot;

    // mt_material_attr
    @ApiModelProperty(value = "wafer")
    private String wafer;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    // hme_material_lot_load
    @ApiModelProperty(value = "芯片位置")
    private String loadRowCol;

    @ApiModelProperty(value = "芯片行")
    private String loadRow;

    @ApiModelProperty(value = "芯片列")
    private String loadColumn;

    @ApiModelProperty(value = "芯片序列号")
    private String hotSinkCode;

    // hme_cos_function
    @ApiModelProperty(value = "电流")
    private String current;

    @ApiModelProperty(value = "功率登记")
    private String A01;

    @ApiModelProperty(value = "功率")
    private BigDecimal A02;

    @ApiModelProperty(value = "波长登记")
    private String A03;

    @ApiModelProperty(value = "波长")
    private BigDecimal A04;

    @ApiModelProperty(value = "波长差")
    private BigDecimal A05;

    @ApiModelProperty(value = "电压")
    private BigDecimal A06;

    @ApiModelProperty(value = "光谱宽度（单点）")
    private BigDecimal A07;

}
