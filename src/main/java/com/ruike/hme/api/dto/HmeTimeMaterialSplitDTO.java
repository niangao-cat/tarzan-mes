package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeTimeMaterialSplitDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/09/12 15:00:34
 **/
@Data
public class HmeTimeMaterialSplitDTO implements Serializable {
    private static final long serialVersionUID = 6241220463372643291L;

    @ApiModelProperty(value = "条码Id",required = true)
    private String materialLotId;

    @ApiModelProperty(value = "时长",required = true)
    private Integer minute;

    @ApiModelProperty(value = "单位",required = true)
    private String timeUom;
}
