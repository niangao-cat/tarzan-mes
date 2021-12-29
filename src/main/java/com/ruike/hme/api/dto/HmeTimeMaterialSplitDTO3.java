package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/30 14:44
 */
@Data
public class HmeTimeMaterialSplitDTO3 implements Serializable {

    private static final long serialVersionUID = -8083498778021888661L;

    @ApiModelProperty(value = "条码Id")
    private String materialLotId;

    @ApiModelProperty(value = "有效期起")
    private String dateTimeFrom;

    @ApiModelProperty(value = "有效期至")
    private String dateTimeTo;
}
