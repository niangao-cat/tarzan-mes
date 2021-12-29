package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName WmsDistributionRevokeReturnDTO2
 * @Description 配送单行信息
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/8 17:28
 * @Version 1.0
 **/
@Data
public class WmsDistributionRevokeReturnDTO2 implements Serializable {
    private static final long serialVersionUID = -2852548141160819434L;

    @ApiModelProperty("配送单行Id")
    private String instructionId;

    @ApiModelProperty("行号")
    private String instructionLineNum;

    @ApiModelProperty("物料Id")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("行状态")
    private String instructionStatus;

    @ApiModelProperty("行需求数量")
    private String quantity;

    @ApiModelProperty("已执行数量")
    private String qty;

    @ApiModelProperty("条码个数")
    private String barcodeNum;

    @ApiModelProperty("单位Id")
    private String UomId;

    @ApiModelProperty("单位编码")
    private String UomCode;

    @ApiModelProperty("预售订单")
    private String soNum;
}
