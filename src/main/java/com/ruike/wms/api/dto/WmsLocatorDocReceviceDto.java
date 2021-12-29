package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsProductionMaterialReturnVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/13 16:20
 */
@Data
public class WmsLocatorDocReceviceDto implements Serializable {

    private static final long serialVersionUID = 6196048479117239467L;

    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "扫描条码信息")
    private WmsProductionMaterialReturnVO wmsProductionMaterialReturnVO;
}
