package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/08 18:22
 */
@Data
public class WmsMaterialLineReturnVO implements Serializable {

    private static final long serialVersionUID = 7712577200084837524L;

    @ApiModelProperty(value = "物料批ID")
    private String actdetmaterialLotId;
    @ApiModelProperty(value = "实物编码")
    private String actdetmaterialLotCode;
    @ApiModelProperty(value = "物料Id")
    private String actdetMaterialId;
    @ApiModelProperty(value = "物料名称")
    private String actdetMaterialName;
    @ApiModelProperty(value = "物料编码")
    private String actdetMaterialCode;
    @ApiModelProperty(value = "版本")
    private String actdetMaterialVersion;
    @ApiModelProperty(value = "单位Id")
    private String actdetUomId;
    @ApiModelProperty(value = "单位")
    private String actdetMaterialUom;
    @ApiModelProperty(value = "批次")
    private String actdetMaterialLot;
    @ApiModelProperty(value = "货位Id")
    private String actdetLocatorId;
    @ApiModelProperty(value = "货位")
    private String actdetLocatorCode;
    @ApiModelProperty(value = "质量状态")
    private String actdetQualityStatus;
    @ApiModelProperty(value = "质量状态意义")
    private String actdetQualityStatusMeaning;
    @ApiModelProperty(value = "有效性")
    private String actdetEnableFlag;
    @ApiModelProperty(value = "有效性意义")
    private String actdetEnableFlagMeaning;
    @ApiModelProperty(value = "数量")
    private Double actdetQty;
    @ApiModelProperty(value = "可选")
    private String selectFlag;
}
