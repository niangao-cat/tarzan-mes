package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/10 15:28
 */
@Data
public class WmsReceiptLineVO implements Serializable {

    private static final long serialVersionUID = -6626918526697205087L;

    @ApiModelProperty(value = "指令行id")
    private String instructionId;

    @ApiModelProperty(value = "行号")
    private String lineNum;

    @ApiModelProperty(value = "行状态")
    @LovValue(lovCode = "WMS.RECEIPT_LINE_STATUS",meaningField = "lineStatusMeaning")
    private String lineStatus;

    @ApiModelProperty(value = "行状态含义")
    private String lineStatusMeaning;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "入库数")
    private BigDecimal receiptQty;

    @ApiModelProperty(value = "执行数")
    private BigDecimal executeQty;

    @ApiModelProperty(value = "单位")
    private String uom;

    @ApiModelProperty(value = "目标仓库")
    private String targetWarehouse;

    @ApiModelProperty(value = "备注")
    private String remark;

}
