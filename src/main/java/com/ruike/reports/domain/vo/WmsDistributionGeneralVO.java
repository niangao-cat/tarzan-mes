package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 配送综合查询报表
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 16:06
 */
@Data
@ExcelSheet(title = "工单配送综合查询报表")
public class WmsDistributionGeneralVO {
    @ApiModelProperty("序号")
    @ExcelColumn(title = "序号")
    private Integer sequence;

    @ApiModelProperty("配送单ID")
    private String instructionDocId;

    @ApiModelProperty("配送单号")
    @ExcelColumn(title = "配送单号")
    private String instructionDocNum;

    @ApiModelProperty("状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("状态含义")
    @ExcelColumn(title = "状态")
    private String instructionDocStatusMeaning;

    @ApiModelProperty("是否备齐")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "suiteFlagMeaning")
    private String suiteFlag;

    @ApiModelProperty("是否备齐含义")
    @ExcelColumn(title = "是否备齐")
    private String suiteFlagMeaning;

    @ApiModelProperty("工厂")
    @ExcelColumn(title = "工厂")
    private String siteCode;

    @ApiModelProperty("产线")
    @ExcelColumn(title = "产线")
    private String productionLineCode;

    @ApiModelProperty("工段")
    @ExcelColumn(title = "工段")
    private String workcellCode;

    @ApiModelProperty(value = "目标仓库")
    @ExcelColumn(title = "目标仓库")
    private String toWarehouseCode;

    @ApiModelProperty("制单人")
    private String createdByName;

    @ApiModelProperty("制单时间")
    @ExcelColumn(title = "制单时间")
    private Date creationDate;

    @ApiModelProperty("备注")
    @ExcelColumn(title = "备注")
    private String remark;

    @ApiModelProperty("行号")
    @ExcelColumn(title = "行号")
    private String instructionLineNum;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(title = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    @ExcelColumn(title = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(title = "物料版本")
    private String materialVersion;

    @ApiModelProperty("行状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_LINE_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;

    @ApiModelProperty("行状态含义")
    @ExcelColumn(title = "行状态")
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "需求数量")
    @ExcelColumn(title = "需求数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "备料数量")
    @ExcelColumn(title = "备料数量")
    private BigDecimal actualQty;

    @ApiModelProperty(value = "签收数量")
    @ExcelColumn(title = "签收数量")
    private BigDecimal signedQty;

    @ApiModelProperty("单位编码")
    @ExcelColumn(title = "单位")
    private String uomCode;

    @ApiModelProperty("工单号")
    @ExcelColumn(title = "工单号")
    private String workOrderNum;

    @ApiModelProperty("工单分配数量")
    @ExcelColumn(title = "工单分配数量")
    private BigDecimal woDistQty;

    @ApiModelProperty("销售订单行-行号")
    @ExcelColumn(title = "销售订单行-行号")
    private String soLine;
}
