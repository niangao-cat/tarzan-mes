package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsProductReturnVO2;
import com.ruike.wms.domain.vo.WmsProductionReturnInstructionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/16 9:44
 */
@Data
public class WmsDetailReceviceDto implements Serializable {

    private static final long serialVersionUID = 688423873236467085L;

    @ApiModelProperty("单据行信息")
    private WmsProductReturnVO2 wmsProductReturnVO2;
}
