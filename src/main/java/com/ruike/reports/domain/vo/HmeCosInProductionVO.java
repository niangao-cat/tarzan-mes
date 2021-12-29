package com.ruike.reports.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

/**
 * COS在制报表
 *
 * @author wenqiang.yin@hand-china.com 2021/01/27 11:11
 */
@Data
@ExcelSheet(title = "cos在制报表")
public class HmeCosInProductionVO implements Serializable {

    private static final long serialVersionUID = 3096427287631700534L;

    @ApiModelProperty("工单")
    @ExcelColumn(title = "工单")
    private String workOrderNum;

    @ApiModelProperty("物料编码")
    @ExcelColumn(title = "物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    @ExcelColumn(title = "物料描述")
    private String materialName;

    @ApiModelProperty("工单类型")
    @LovValue(value = "MT.WO_TYPE", meaningField = "workOrderTypeMeaning")
    private String workOrderType;

    @ApiModelProperty("工单类型")
    @ExcelColumn(title = "工单类型")
    private String workOrderTypeMeaning;

    @ApiModelProperty("生产线编码")
    @ExcelColumn(title = "生产线编码")
    private String prodLineCode;

    @ApiModelProperty("生产线描述")
    @ExcelColumn(title = "生产线描述")
    private String prodLineName;

    @ApiModelProperty("工单状态")
    @LovValue(value = "MT.WO_STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty("工单状态")
    @ExcelColumn(title = "工单状态")
    private String statusMeaning;

    @ApiModelProperty("工单数量")
    @ExcelColumn(title = "工单数量")
    private String workOrderQty;

    @ApiModelProperty("工单投料数量（来料）")
    @ExcelColumn(title = "工单投料数量（来料）")
    private String cosNum;

    @ApiModelProperty("完工数量")
    @ExcelColumn(title = "完工数量")
    private String completedQty;

    @ApiModelProperty("WAFER")
    @ExcelColumn(title = "WAFER")
    private String wafer;

    @ApiModelProperty("COS类型")
    @ExcelColumn(title = "COS类型")
    private String cosType;

    @ApiModelProperty("条码")
    @ExcelColumn(title = "条码")
    private String materialLotCode;

    @ApiModelProperty("数量")
    @ExcelColumn(title = "数量")
    private String qty;

    @ApiModelProperty("工位编码")
    @ExcelColumn(title = "工位编码")
    private String workcellCode;

    @ApiModelProperty("工位描述")
    @ExcelColumn(title = "工位描述")
    private String workcellName;

    @ApiModelProperty("工艺编码")
    @ExcelColumn(title = "工艺编码")
    private String operationName;

    @ApiModelProperty("加工开始时间")
    @ExcelColumn(title = "加工开始时间")
    private String siteInDate;

    @ApiModelProperty("加工结束时间")
    @ExcelColumn(title = "加工结束时间")
    private String siteOutDate;

    @ApiModelProperty("加工人员")
    private Long createdBy;

    @ApiModelProperty("加工人员")
    @ExcelColumn(title = "加工人员")
    private String createdByName;

}
