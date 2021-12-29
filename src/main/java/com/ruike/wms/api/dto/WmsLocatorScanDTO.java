package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 库位转移之货位扫描
 * @author: han.zhang
 * @create: 2020/05/08 10:54
 */
@Getter
@Setter
@ToString
public class WmsLocatorScanDTO implements Serializable {
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
}