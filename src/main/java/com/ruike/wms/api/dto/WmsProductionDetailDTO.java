package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsProductionReturnInstructionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/13 16:45
 */
@Data
public class WmsProductionDetailDTO implements Serializable {

    private static final long serialVersionUID = -1834424715078453780L;

    @ApiModelProperty("单据行信息")
    private WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO;
}
