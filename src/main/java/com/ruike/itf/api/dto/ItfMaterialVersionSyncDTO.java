package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 物料版本DTO
 */

@Data
public class ItfMaterialVersionSyncDTO {

    @ApiModelProperty(value = "物料")
    private String MATNR;

    @ApiModelProperty(value = "工厂")
    private String WERKS;

    @ApiModelProperty(value = "物料版本")
    private String VERID;

    @ApiModelProperty(value = "描述")
    private String TEXT1;

    @ApiModelProperty(value = "开始时间")
    private Date BDATU;

    @ApiModelProperty(value = "结束时间")
    private Date ADATU;

}
