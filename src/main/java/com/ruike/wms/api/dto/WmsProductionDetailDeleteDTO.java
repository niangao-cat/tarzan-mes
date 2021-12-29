package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsProductionReturnInstructionDetailVO;
import com.ruike.wms.domain.vo.WmsProductionReturnInstructionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/14 9:07
 */
@Data
public class WmsProductionDetailDeleteDTO implements Serializable {

    private static final long serialVersionUID = 4686771530350021265L;

    @ApiModelProperty("单据行信息")
    private WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO;
    @ApiModelProperty("被选中的单据行明细信息")
    private List<WmsProductionReturnInstructionDetailVO> wmsProductionReturnInstructionDetailVOList;
}
