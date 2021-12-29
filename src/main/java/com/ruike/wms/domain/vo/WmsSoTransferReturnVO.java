package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/08/06 11:34
 */
@Data
public class WmsSoTransferReturnVO implements Serializable {

    private static final long serialVersionUID = 5220427477469119097L;
    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批")
    private String materialLotCode;

    @ApiModelProperty(value = "库位Code")
    private String locatorCode;

    @ApiModelProperty(value = "库位类型")
    private String locatorType;
}
