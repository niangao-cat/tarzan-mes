package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsProductionMaterialReturnVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/13 15:21
 */
@Data
public class WmsProductionQtyChangeReceviceDto implements Serializable {

    private static final long serialVersionUID = 2489266797196882907L;

    @ApiModelProperty(value = "数量更改")
    private Double changeQty;
    @ApiModelProperty(value = "扫描条码信息")
    private WmsProductionMaterialReturnVO wmsProductionMaterialReturnVO;
}
