package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * WmsMaterialOnShelfBarCodeDTO2
 *
 * @author liyuan.lv@hand-china.com 2020/04/03 18:25
 */

@Data
public class WmsMaterialOnShelfBarCodeDTO2 implements Serializable {

    private static final long serialVersionUID = 7398014942584934750L;

    @ApiModelProperty("单据Num")
    private String instructionDocNum;
    @ApiModelProperty("是否启用")
    private String enableDocFlag;
    @ApiModelProperty("条码")
    private String barCode;
    @ApiModelProperty("货位code")
    private String locatorCode;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("单据行")
    List<WmsMaterialOnShelfDocLineDTO2> orderLineList;
}
