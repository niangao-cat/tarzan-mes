package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/13 16:43
 */
@Data
public class WmsProductionDetailVO implements Serializable {

    private static final long serialVersionUID = 4950177074182616650L;

    @ApiModelProperty("单据行信息")
    private WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO;
    @ApiModelProperty("单据行明细信息")
    private List<WmsProductionReturnInstructionDetailVO> wmsProductionReturnInstructionDetailVOList;
}
