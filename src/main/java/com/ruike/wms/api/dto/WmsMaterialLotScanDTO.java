package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 条码扫描参数
 * @author: han.zhang
 * @create: 2020/05/08 12:00
 */
@Getter
@Setter
@ToString
public class WmsMaterialLotScanDTO implements Serializable {
    private static final long serialVersionUID = 1247771334146471379L;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
}