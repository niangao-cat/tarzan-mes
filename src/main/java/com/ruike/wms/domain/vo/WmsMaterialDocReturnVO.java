package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/07 16:58
 */
@Data
public class WmsMaterialDocReturnVO implements Serializable {

    private static final long serialVersionUID = 7763334776931542380L;

    @ApiModelProperty("物料批Id")
    private String materialLotId;
    @ApiModelProperty("物料批条码")
    private String materialLotCode;
    @ApiModelProperty("物料Id")
    private String lotMaterialId;
    @ApiModelProperty("物料编码")
    private String lotMaterialCode;
    @ApiModelProperty("物料版本")
    private String lotMaterialVersion;
    @ApiModelProperty("本次扫描数量")
    private Double lotMaterialQty;
    @ApiModelProperty(value = "对应的行数据")
    private WmsProductReturnVO2 wmsProductReturnVO2;
    @ApiModelProperty(value = "行数据")
    private List<WmsProductReturnVO2> instructionList;
    @ApiModelProperty(value = "行Id集合")
    private List<String> idList;
    @ApiModelProperty(value = "报错信息")
    private String msg;
}
