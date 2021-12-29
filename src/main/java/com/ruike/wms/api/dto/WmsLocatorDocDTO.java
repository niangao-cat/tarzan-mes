package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsMaterialDocReturnVO;
import com.ruike.wms.domain.vo.WmsProductionMaterialReturnVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/16 9:30
 */
@Data
public class WmsLocatorDocDTO implements Serializable {

    private static final long serialVersionUID = -1917571232592537240L;

    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "扫描条码信息")
    private WmsMaterialDocReturnVO wmsMaterialDocReturnVO;
}
