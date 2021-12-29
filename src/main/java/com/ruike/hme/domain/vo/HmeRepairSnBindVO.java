package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/26 12:54
 */
@Data
@ExcelSheet(zh = "返修SN")
public class HmeRepairSnBindVO implements Serializable {

    private static final long serialVersionUID = -7823411248739666313L;

    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("执行作业编码")
    @ExcelColumn(zh = "执行作业编码", order = 1)
    private String eoNum;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    @ExcelColumn(zh = "物料编码", order = 4)
    private String materialCode;
    @ApiModelProperty("物料名称")
    @ExcelColumn(zh = "物料名称", order = 5)
    private String materialName;
    @ApiModelProperty("生产线ID")
    private String productionLineId;
    @ApiModelProperty("生产线编码")
    @ExcelColumn(zh = "生产线编码", order = 6)
    private String productionLineCode;
    @ApiModelProperty("生产线描述")
    @ExcelColumn(zh = "生产线短描述", order = 7)
    private String productionLineName;
    @ApiModelProperty("生产指令ID")
    private String workOrderId;
    @ApiModelProperty("生产指令编码")
    @ExcelColumn(zh = "工单", order = 8)
    private String workOrderNum;
    @ApiModelProperty(value = "EO标识")
    @ExcelColumn(zh = "EO标识", order = 2)
    private String eoIdentification;
    @ApiModelProperty(value = "返修SN")
    @ExcelColumn(zh = "返修SN", order = 3)
    private String repairSn;
}
