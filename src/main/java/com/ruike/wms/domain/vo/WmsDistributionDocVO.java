package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ExcelSheet(zh = "配送单")
public class WmsDistributionDocVO implements Serializable {

    private static final long serialVersionUID = -7052090057288608887L;

    @ApiModelProperty(value = "配送单号")
    @ExcelColumn(zh = "配送单号", order = 1)
    private String instructionDocNum;

    @ApiModelProperty(value = "配送单id")
    private String instructionDocId;

    @ApiModelProperty("是否备齐")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "suiteFlagMeaning")
    private String suiteFlag;

    @ApiModelProperty("是否备齐")
    @ExcelColumn(zh = "是否备齐", order = 2)
    private String suiteFlagMeaning;

    @ApiModelProperty("行状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_DOC_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;

    @ApiModelProperty("行状态")
    @ExcelColumn(zh = "行状态", order = 3)
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "配送单状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty(value = "配送单状态")
    @ExcelColumn(zh = "配送单状态", order = 4)
    private String instructionDocStatusMeaning;

    @ApiModelProperty(value = "工厂")
    @ExcelColumn(zh = "工厂", order = 5)
    private String siteName;

    @ApiModelProperty(value = "物料")
    @ExcelColumn(zh = "物料", order = 6)
    private String materialCode;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本", order = 7)
    private String materialVersion;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述", order = 8)
    private String materialName;

    @ApiModelProperty("已签收数量")
    @ExcelColumn(zh = "已签收数量", order = 9)
    private BigDecimal signedQty;

    @ApiModelProperty("需求数量")
    @ExcelColumn(zh = "需求数量", order = 10)
    private BigDecimal quantity;

    @ApiModelProperty("备货数量")
    @ExcelColumn(zh = "备货数量", order = 11)
    private BigDecimal acutalQty;

    @ApiModelProperty("产线")
    @ExcelColumn(zh = "产线", order = 12)
    private String productionLineName;

    @ApiModelProperty(value = "需求时间")
    @ExcelColumn(zh = "需求时间", order = 13)
    private String demandTime;
}
