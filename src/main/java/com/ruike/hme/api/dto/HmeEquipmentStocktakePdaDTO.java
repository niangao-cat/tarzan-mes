package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEquipmentStocktakePdaDTO
 * 扫描设备传参DTO
 * @author: chaonan.hu@hand-china.com 2021/4/1 16:42:02
 **/
@Data
public class HmeEquipmentStocktakePdaDTO implements Serializable {
    private static final long serialVersionUID = 5486270698365570373L;

    @ApiModelProperty(value = "单据ID", required = true)
    private String stocktakeId;

    @ApiModelProperty(value = "资产编码", required = true)
    private String assetEncoding;
}
