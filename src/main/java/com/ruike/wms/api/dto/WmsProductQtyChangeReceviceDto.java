package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsMaterialDocReturnVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/15 19:44
 */
@Data
public class WmsProductQtyChangeReceviceDto implements Serializable {

    private static final long serialVersionUID = -636028677611548772L;

    @ApiModelProperty(value = "数量更改")
    private Double changeQty;
    @ApiModelProperty(value = "扫描条码信息")
    private WmsMaterialDocReturnVO wmsMaterialDocReturnVO ;
}
