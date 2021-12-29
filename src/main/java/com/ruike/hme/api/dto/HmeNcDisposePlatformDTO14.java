package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-01 15:35:19
 **/
@Data
public class HmeNcDisposePlatformDTO14 implements Serializable {
    private static final long serialVersionUID = -3451908758476623732L;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "工位Id")
    private String workcrellId;

    @ApiModelProperty(value = "扫描条码")
    private String scanMaterialLotCode;

    @ApiModelProperty(value = "不良材料清单临时表ID")
    private String ncComponentTempId;
}
