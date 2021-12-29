package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/13 15:19
 */
@Data
public class WmsProductionQtyChangeVO implements Serializable {

    private static final long serialVersionUID = -4731004487872310363L;

    @ApiModelProperty("数量")
    private Double materialLotQty;
    @ApiModelProperty("单据行信息")
    private List<WmsProductionReturnInstructionVO> instructionList;
}
