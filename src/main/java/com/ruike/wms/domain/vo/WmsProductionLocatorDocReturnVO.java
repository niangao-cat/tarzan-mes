package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/13 16:18
 */
@Data
public class WmsProductionLocatorDocReturnVO implements Serializable {

    private static final long serialVersionUID = -4390450585506255152L;

    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("货位描述")
    private String locatorName;
}
