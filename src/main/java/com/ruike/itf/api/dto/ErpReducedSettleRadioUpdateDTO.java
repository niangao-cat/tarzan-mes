package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ErpReducedSettleRadioUpdateDTO {

    @ApiModelProperty(value = "状态")
    private String ZFLAG;

    @ApiModelProperty(value = "比例")
    private String PROZS;

    @ApiModelProperty(value = "工单号")
    private String AUFNR;

    @ApiModelProperty(value = "返回信息")
    private String ZMESSAGE;

    @ApiModelProperty(value = "物料")
    private String MATNR;

}
