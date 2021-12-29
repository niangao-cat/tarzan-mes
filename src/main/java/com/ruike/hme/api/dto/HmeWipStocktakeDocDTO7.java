package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO7
 *
 * @author: chaonan.hu@hand-china.com 2021-03-04 18:59:23
 **/
@Data
public class HmeWipStocktakeDocDTO7 implements Serializable {
    private static final long serialVersionUID = -2616518057210070664L;

    @ApiModelProperty(value = "盘点单ID集合", required = true)
    private List<String> stocktakeIdList;

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

    @ApiModelProperty("初盘一致标志")
    private String firstcountConsistentFlag;

    @ApiModelProperty("复盘一致标志")
    private String recountConsistentFlag;
}
