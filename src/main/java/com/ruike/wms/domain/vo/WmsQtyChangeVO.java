package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/08 16:01
 */
@Data
public class WmsQtyChangeVO implements Serializable {

    private static final long serialVersionUID = -1928644532169324530L;

    @ApiModelProperty(value = "本次扫描数量")
    private Double lotMaterialQty;
    @ApiModelProperty(value = "行数据")
    private List<WmsProductReturnVO2> instructionList;

}
