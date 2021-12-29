package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class WmsInstructionVO3 implements Serializable {

    private static final long serialVersionUID = 4316952251248280275L;
    @ApiModelProperty(value = "是否启用单据扫描")
    private String enableDocFlag;

    @ApiModelProperty("明细行")
    List<WmsMaterialLotLineVO> detailLineList;

}
