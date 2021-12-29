package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/13 11:21
 */
@Data
public class WmsProductionMaterialReturnVO implements Serializable {

    private static final long serialVersionUID = -2068135001147446971L;

    @ApiModelProperty("物料批Id")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("数量")
    private Double materialLotQty;
    @ApiModelProperty("对应的单据行信息")
    private WmsProductionReturnInstructionVO wmsProductionReturnInstructionVO;
    @ApiModelProperty("单据行信息")
    private List<WmsProductionReturnInstructionVO> instructionList;
    @ApiModelProperty("报错信息")
    private String msg;
    @ApiModelProperty("销售订单")
    private String soNum;
    @ApiModelProperty("行号")
    private String soLineNum;
}
