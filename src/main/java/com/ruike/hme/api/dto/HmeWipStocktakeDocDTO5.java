package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO5
 *
 * @author: chaonan.hu@hand-china.com 2021-03-04 14:48:42
 **/
@Data
public class HmeWipStocktakeDocDTO5 implements Serializable {
    private static final long serialVersionUID = -4742467955652674509L;

    @ApiModelProperty(value = "盘点单ID集合", required = true)
    private List<String> stocktakeIdList;

    @ApiModelProperty("工单")
    private String workOrderNum;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("工序ID")
    private String workcellId;

    @ApiModelProperty("初盘是否为空标志")
    private String firstcountNullFlag;

    @ApiModelProperty("复盘是否为空标志")
    private String recountcountNullFlag;

    @ApiModelProperty("初复盘是否一致标志")
    private String firstcountRecountFlag;

    @ApiModelProperty("工序是否一致标志")
    private String workcellFlag;

    @ApiModelProperty("账实是否一致标志")
    private String qtyFlag;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "返修条码")
    private String repairMaterialLotCode;
}
