package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeWipStocktakeDocDTO9
 *
 * @author: chaonan.hu@hand-china.com 2021-03-07 17:43:34
 **/
@Data
public class HmeWipStocktakeDocDTO9 implements Serializable {
    private static final long serialVersionUID = 8051720676489112854L;

    @ApiModelProperty(value = "盘点单ID", required = true)
    private String stocktakeId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
