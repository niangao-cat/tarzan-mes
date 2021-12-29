package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsMaterialLotDTO3 implements Serializable {
    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码号")
    private String materialLotCode;

    @ApiModelProperty(value = "工厂编码")
    private String factoryCode;

    @ApiModelProperty(value = "工厂名称")
    private String factoryName;

    @ApiModelProperty(value = "仓库编码")
    private String wareHouse;

    @ApiModelProperty(value = "仓库名称")
    private String wareHouseName;

    @ApiModelProperty(value = "货位编码")
    private String locatorCode;

    @ApiModelProperty(value = "货位名称")
    private String locatorName;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "单位")
    private String primaryUomCode;

    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "MT.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "批次号")
    private String lot;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "生产日期")
    private String productDate;

    @ApiModelProperty(value = "有效日期")
    private String effectiveDate;

    @ApiModelProperty(value = "当前容器")
    private String currentContainer;

    @ApiModelProperty(value = "顶层容器")
    private String topContainer;

    @ApiModelProperty(value = "当前容器编码")
    private String currentContainerCode;

    @ApiModelProperty(value = "顶层容器编码")
    private String topContainerCode;
}
