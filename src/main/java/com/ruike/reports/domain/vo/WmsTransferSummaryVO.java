package com.ruike.reports.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 调拨汇总报表
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/23 14:30
 */
@Data
@ExcelSheet(title = "调拨汇总报表")
public class WmsTransferSummaryVO {
    @ApiModelProperty("序号")
    @ExcelColumn(title = "序号")
    private Integer sequence;

    @ApiModelProperty("站点编码")
    @ExcelColumn(title = "站点编码")
    private String siteCode;

    @ApiModelProperty("调拨单号")
    @ExcelColumn(title = "调拨单号")
    private String instructionDocNum;

    @ApiModelProperty("状态")
    @LovValue(lovCode = "WMS.STOCK_ALLOCATION_DOC.STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("状态含义")
    @ExcelColumn(title = "状态")
    private String instructionDocStatusMeaning;

    @ApiModelProperty("类型")
    @LovValue(lovCode = "WMS.STOCK_ALLOCATION_DOC.TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;

    @ApiModelProperty("类型含义")
    @ExcelColumn(title = "类型")
    private String instructionDocTypeMeaning;

    @ApiModelProperty("备注")
    @ExcelColumn(title = "备注")
    private String remark;

    @ApiModelProperty("制单人")
    private String createdByName;

    @ApiModelProperty("制单时间")
    @ExcelColumn(title = "制单时间")
    private LocalDateTime creationDate;

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
    @LovValue(lovCode = "WMS.STOCK_ALLOCATION_DOC_LINE.STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;

    @ApiModelProperty("行状态含义")
    @ExcelColumn(title = "行状态")
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "制单数量")
    @ExcelColumn(title = "制单数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "执行数量")
    @ExcelColumn(title = "执行数量")
    private BigDecimal actualQuantity;

    @ApiModelProperty(value = "来源仓库")
    @ExcelColumn(title = "来源仓库")
    private String fromWarehouseCode;

    @ApiModelProperty(value = "来源货位")
    @ExcelColumn(title = "来源货位")
    private String fromLocatorCode;

    @ApiModelProperty(value = "目标仓库")
    @ExcelColumn(title = "目标仓库")
    private String toWarehouseCode;

    @ApiModelProperty(value = "目标货位")
    @ExcelColumn(title = "目标货位")
    private String toLocatorCode;

    @ApiModelProperty("超发设置")
    @LovValue(lovCode = "WMS.EXCESS_SETTING", meaningField = "excessSettingMeaning")
    private String excessSetting;

    @ApiModelProperty("超发设置含义")
    @ExcelColumn(title = "超发设置")
    private String excessSettingMeaning;

    @ApiModelProperty("超发值")
    @ExcelColumn(title = "超发值")
    private String excessValue;

    @ApiModelProperty("待调拨数量")
    private BigDecimal waitAllocationQty;

    @ApiModelProperty("单位")
    private String uomCode;

    @ApiModelProperty("最新执行人")
    private String executorUser;

    @ApiModelProperty("最新执行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date executorDate;
}
