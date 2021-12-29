package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeQuantityAnalyzeDocDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-01-19 11:28:34
 **/
@Data
public class HmeQuantityAnalyzeDocDTO implements Serializable {
    private static final long serialVersionUID = -8784366951435046147L;

    @ApiModelProperty("物料Id")
    private String materialId;

    @ApiModelProperty("SN条码")
    private String materialLotCode;

    @ApiModelProperty("工单")
    private String workOrderNum;

    @ApiModelProperty("SNId")
    private String materialLotId;

}
