package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO15
 *
 * @author: chaonan.hu@hand-china.com 2021-03-10 18:35:12
 **/
@Data
public class HmeWipStocktakeDocDTO15 implements Serializable {
    private static final long serialVersionUID = -8642121184186458827L;

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
}
