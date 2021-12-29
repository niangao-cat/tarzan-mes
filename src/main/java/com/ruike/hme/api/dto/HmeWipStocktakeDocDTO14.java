package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO14
 *
 * @author: chaonan.hu@hand-china.com 2021-03-10 15:34:21
 **/
@Data
public class HmeWipStocktakeDocDTO14 implements Serializable {

    @ApiModelProperty(value = "产线ID集合", required = true)
    private List<String> prodLineIdList;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;

}
