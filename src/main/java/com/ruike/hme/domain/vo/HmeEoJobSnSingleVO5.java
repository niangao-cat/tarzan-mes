package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;

@Data
public class HmeEoJobSnSingleVO5 implements Serializable {
    private static final long serialVersionUID = 1696248116839653381L;
    @ApiModelProperty(value = "主键")
    private String jobMaterialId;
    @ApiModelProperty(value = "物料类型")
    private String productionType;
    @ApiModelProperty(value = "反冲标识")
    private String backflushFlag;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位描述")
    private String uomName;
    @ApiModelProperty(value = "条码ID")
    private String materialLotId;
    @ApiModelProperty(value = "条码ID")
    private String materialLotCode;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
    @ApiModelProperty(value = "条码数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "倒计时")
    private String deadLineDate;
    @ApiModelProperty(value = "有效性")
    private String enableFlag;
    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;

}
