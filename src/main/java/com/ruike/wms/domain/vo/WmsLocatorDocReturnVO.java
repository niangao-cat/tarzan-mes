package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/08 17:18
 */
@Data
public class WmsLocatorDocReturnVO implements Serializable {

    private static final long serialVersionUID = -1060105648976784487L;

    @ApiModelProperty(value = "货位Id")
    private String locatorId;
    @ApiModelProperty(value = "货位条码")
    private String locatorCode;
    @ApiModelProperty(value = "货位描述")
    private String locatorName;

}
