package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;

/**
 * 配送签收单据行
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 19:35
 */
@Data
public class WmsDistributionSignLineVO {
    @ApiModelProperty("配送单行ID")
    private String instructionId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty("配送单行状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_LINE_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty("配送单行状态含义")
    private String instructionStatusMeaning;
    @ApiModelProperty(value = "行需求数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "可签收数量")
    private BigDecimal signableQty;
    @ApiModelProperty(value = "条码个数")
    private Integer barcodeNum;
    @ApiModelProperty(value = "可签收条码个数")
    private Integer signableBarcodeNum;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("销售订单")
    private String soNum;
    @ApiModelProperty("销售订单行")
    private String soLineNum;
}
