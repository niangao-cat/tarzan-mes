package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ItfMaterialLotConfirmIfaceVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/07/14 15:10
 **/
@Data
public class ItfMaterialLotConfirmIfaceVO2 implements Serializable {
    private static final long serialVersionUID = 8574532957935863091L;

    @ApiModelProperty("msgCode")
    private String msgCode;

    @ApiModelProperty("message")
    private String message;

    @ApiModelProperty("materialLotCode")
    private String materialLotCode;
}
