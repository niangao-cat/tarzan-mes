package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/14 9:06
 */
@Data
public class WmsProductionDetailDeleteVO implements Serializable {

    private static final long serialVersionUID = 1440151668074658547L;

    @ApiModelProperty("单据行信息")
    private WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO;
}
