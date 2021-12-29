package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 回传ERP消息信息返回报文
 *
 * @author kejin.liu01@hand-china.com 2020/9/2 13:29
 */
@Data
public class ItfLogisticsServiceDTO {

    @ApiModelProperty(value = "")
    private String PRCTR;

    @ApiModelProperty(value = "")
    private String DATUM;

    @ApiModelProperty(value = "")
    private String ZFLAG;

    @ApiModelProperty(value = "")
    private String BOLNR;

    @ApiModelProperty(value = "")
    private String WERKS;

    @ApiModelProperty(value = "")
    private String SERNR1;

    @ApiModelProperty(value = "")
    private String ZSJR;

    @ApiModelProperty(value = "")
    private String MATNR1;

    @ApiModelProperty(value = "")
    private String ZMESSAGE;

    @ApiModelProperty(value = "")
    private String ZWL;

    @ApiModelProperty(value = "")
    private String ZDELETE;

}
