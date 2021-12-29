package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 指令行带拓展属性
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 17:59
 */
@Data
public class WmsInstructionAttrVO {
    @ApiModelProperty("指令行ID")
    private String instructionId;
    @ApiModelProperty("指令行号")
    private String instructionNum;
    @ApiModelProperty("指令行序号")
    private String instructionLineNum;
    @ApiModelProperty(value = "来源指令ID")
    private String sourceInstructionId;
    @ApiModelProperty(value = "单据id")
    private String sourceDocId;
    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;
    @ApiModelProperty(value = "指令移动类型")
    private String instructionType;
    @ApiModelProperty(value = "指令状态")
    private String instructionStatus;
    @ApiModelProperty(value = "站点id")
    private String siteId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "单位")
    private String uomId;
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "销售订单号")
    private String soNum;
    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value = "签收数量")
    private BigDecimal signedQty;
    @ApiModelProperty(value = "是否签收标志")
    private String signFlag;
    @ApiModelProperty(value = "实际数量")
    private BigDecimal actualQty;
    @ApiModelProperty(value = "存在批次标志")
    private Boolean lotFlag;
    @ApiModelProperty(value = "已执行数量")
    private BigDecimal exchangedQty;
    @ApiModelProperty(value = "执行数量")
    private BigDecimal exchangeQty;
    @ApiModelProperty("来源货位")
    private String fromLocatorId;
    @ApiModelProperty("目标货位")
    private String toLocatorId;
    @ApiModelProperty("目标站点")
    private String toSiteId;
    @ApiModelProperty("供应商")
    private String supplierId;
    @ApiModelProperty(value = "执行标志")
    private String exchangeFlag;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "发货数量")
    private BigDecimal deliveredQty;
}
