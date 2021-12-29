package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/12 17:05
 */
@Data
public class ItfSoDeliveryChanOrPostDTO3 implements Serializable {

    private static final long serialVersionUID = 3820936653520683223L;

    @ApiModelProperty("单据id")
    private String instructionId;
    @ApiModelProperty("行号")
    private String instructionLineNum;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("批次标识")
    private String lotFlag;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("指令数量")
    private BigDecimal quantity;
    @ApiModelProperty("仓库")
    private String locatorCode;
    @ApiModelProperty("仓库1")
    private String toLocatorCode;
}
