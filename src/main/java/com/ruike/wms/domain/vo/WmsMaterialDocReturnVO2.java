package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/08 18:13
 */
@Data
public class WmsMaterialDocReturnVO2 implements Serializable {

    private static final long serialVersionUID = -7385102846794132976L;
    @ApiModelProperty("单据行信息")
    private WmsProductReturnVO2 wmsProductReturnVO2;
    @ApiModelProperty("单据行明细信息")
    private List<WmsMaterialLineReturnVO> wmsMaterialLineReturnVOList;

}
