package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ItfMaterialLotConfirmIfaceVO4
 *
 * @author: chaonan.hu@hand-china.com 2021/07/14 17:35
 **/
@Data
public class ItfMaterialLotConfirmIfaceVO4 implements Serializable {
    private static final long serialVersionUID = 7239150917239818730L;

    @ApiModelProperty("条码号")
    private String barcode;

    @ApiModelProperty("报错消息")
    private String errorMessage;

    @ApiModelProperty("返回消息")
    private String message;

    @ApiModelProperty("标识 -成功-turn 失败-false")
    private Boolean success;
}
