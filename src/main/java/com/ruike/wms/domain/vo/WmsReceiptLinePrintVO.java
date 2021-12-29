package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WmsReceiptLinePrintVO implements Serializable {

    private static final long serialVersionUID = -8227296793770212446L;
    @ApiModelProperty(value = "指令行id")
    private String instructionId;

    @ApiModelProperty(value = "行号")
    private String lineNum;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "旧物料号")
    private String oldItemCode;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "入库数")
    private BigDecimal receiptQty;

    @ApiModelProperty(value = "执行数")
    private BigDecimal executeQty;

    @ApiModelProperty(value = "物流器具总数")
    private BigDecimal containerQty;

    @ApiModelProperty(value = "单位")
    private String uom;

    @ApiModelProperty(value = "目标仓库")
    private String targetWarehouse;

    @ApiModelProperty(value = "来源仓库")
    private String fromWarehouse;

    @ApiModelProperty(value = "备注")
    private String remark;
}
