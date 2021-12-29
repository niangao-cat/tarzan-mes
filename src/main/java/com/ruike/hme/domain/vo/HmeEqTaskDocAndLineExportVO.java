package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yaqiong.zhou@raycus.com 2021/8/18 17:30
 */
@Data
@ExcelSheet(title = "设备点检任务&结果")
public class HmeEqTaskDocAndLineExportVO implements Serializable {
    private static final long serialVersionUID = -957899659436271223L;

    @ApiModelProperty(value = "任务单ID")
    private String taskDocId;

    @ApiModelProperty(value = "组织ID")
    private String siteId;

    @ApiModelProperty(value = "组织编码")
    private String siteCode;

    @ApiModelProperty(value = "组织名称")
    @ExcelColumn(zh = "组织", order = 1)
    private String siteName;

    @ExcelColumn(zh = "任务单号", order = 2)
    @ApiModelProperty(value = "单据编码")
    private String docNum;

    @ApiModelProperty(value = "单据类型")
    @LovValue(value = "HME.TASK_DOC_TYPE",meaningField ="docTypeMeaning")
    private String docType;
    @ApiModelProperty(value = "单据类型说明")
    @ExcelColumn(zh = "任务类型", order = 3)
    private String docTypeMeaning;

    @ApiModelProperty(value = "任务周期")
    @LovValue(value = "HME.EQUIPMENT_MANAGE_CYCLE",meaningField ="taskCycleMeaning")
    private String taskCycle;
    @ApiModelProperty(value = "任务周期含义")
    @ExcelColumn(zh = "检验周期", order = 4)
    private String taskCycleMeaning;

    @ApiModelProperty(value = "创建时间")
    @ExcelColumn(zh = "任务创建时间", order = 5)
    private String creationDate;

    @ApiModelProperty(value = "单据状态")
    @LovValue(value = "HME_MAINTENANCE_STATUS", meaningField = "docStatusMeaning")
    private String docStatus;
    @ApiModelProperty(value = "单据状态说明")
    @ExcelColumn(zh = "单据状态", order = 6)
    private String docStatusMeaning;

    @ApiModelProperty(value = "设备ID")
    private String equipmentId;
    @ApiModelProperty(value = "设备编码")
    @ExcelColumn(zh = "设备编码", order = 7)
    private String equipmentCode;

    @ApiModelProperty(value = "设备名称")
    @ExcelColumn(zh = "设备描述", order = 8)
    private String equipmentName;

    @ApiModelProperty(value = "型号")
    @ExcelColumn(zh = "型号", order = 9)
    private String model;

    @ApiModelProperty(value = "序列号")
    @ExcelColumn(zh = "序列号", order = 10)
    private String equipmentBodyNum;

    @ApiModelProperty(value = "部门id")
    private String departmentId;

    @ApiModelProperty(value = "部门")
    @ExcelColumn(zh = "保管部门", order = 11)
    private String description;

    @ApiModelProperty(value = "检验部门")
    @ExcelColumn(zh = "检验部门", order = 12)
    private String areaName;

    @ApiModelProperty(value = "车间")
    @ExcelColumn(zh = "车间", order = 13)
    private String workshopName;

    @ApiModelProperty(value = "产线")
    @ExcelColumn(zh = "产线", order = 14)
    private String prodLineName;

    @ApiModelProperty(value = "工序")
    @ExcelColumn(zh = "工序", order = 15)
    private String processName;

    @ApiModelProperty(value = "工位名称")
    @ExcelColumn(zh = "检验工位", order = 16)
    private String wkcName;

    @ApiModelProperty(value = "检验人")
    private Long checkBy;

    @ApiModelProperty(value = "检验人")
    @ExcelColumn(zh = "检验人", order = 17)
    private String checkByName;

    @ApiModelProperty(value = "检验时间")
    @ExcelColumn(zh = "检验时间", order = 18)
    private String checkDate;

    @ApiModelProperty(value = "检验结果")
    @LovValue(value = "HME.CHECK_RESLT",meaningField ="checkResultMeaning")
    private String checkResult;
    @ApiModelProperty(value = "检验结果说明")
    @ExcelColumn(zh = "检验结果", order = 19)
    private String checkResultMeaning;

    @ApiModelProperty(value = "班次日期")
    @ExcelColumn(zh = "班次日期", order = 20)
    private String shiftDate;

    @ApiModelProperty(value = "班次")
    @ExcelColumn(zh = "班次", order = 21)
    private String shiftCode;

    @ApiModelProperty(value = "确认人")
    private Long confirmBy;

    @ApiModelProperty(value = "确认人")
    private String confirmByName;

    @ApiModelProperty(value = "工位")
    private String wkcId;

    @ApiModelProperty(value = "工位编码")
    private String wkcCode;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注", order = 22)
    private String remark;

    @ApiModelProperty(value = "车间位置")
    private String location;

    @ApiModelProperty(value = "保管人")
    private String preserver;

    @ApiModelProperty(value = "完成率")
    private String completedRate;

    @ApiModelProperty(value = "完成率")
    @ExcelColumn(zh = "完成率", order = 23)
    private String rate;

    //设备管理任务单行表信息

    @ApiModelProperty(value = "序列号")
    private String serialNumber;

    @ApiModelProperty(value = "任务单行ID")
    private String taskDocLineId;

    @ApiModelProperty(value = "项目ID")
    private String manageTagId;

    @ApiModelProperty(value = "项目编码")
    private String tagCode;

    @ApiModelProperty(value = "项目说明")
    @ExcelColumn(zh = "项目描述", order = 24)
    private String tagDesc;

    @ApiModelProperty(value = "数据类型")
    private String valueType;

    @ApiModelProperty(value = "数据类型说明")
    @ExcelColumn(zh = "数据类型", order = 25)
    private String valueTypeDesc;

    @ApiModelProperty(value = "精度")
    @ExcelColumn(zh = "精度", order = 26)
    private BigDecimal accuracy;

    @ApiModelProperty(value = "最小值")
    @ExcelColumn(zh = "最小值", order = 27)
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "标准值")
    @ExcelColumn(zh = "标准值", order = 28)
    private BigDecimal standardValue;

    @ApiModelProperty(value = "最大值")
    @ExcelColumn(zh = "最大值", order = 29)
    private BigDecimal maximalValue;

    @ApiModelProperty(value = "单位ID")
    private String uomId;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "单位名称")
    @ExcelColumn(zh = "单位", order = 30)
    private String uomName;

    @ApiModelProperty(value = "检验值")
    @ExcelColumn(zh = "检验值", order = 31)
    private String checkValue;

    @ApiModelProperty(value = "结果")
    @ExcelColumn(zh = "结果", order = 32)
    private String result;

    @ApiModelProperty(value = "工具")
    private String tool;

    @ApiModelProperty(value = "责任人")
    private Long responsible;

    @ApiModelProperty(value = "责任人")
    private String responsibleName;

    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注", order = 33)
    private String itemRemark;

    @ApiModelProperty(value = "检验时间")
    private Date itemCheckDate;

    @ApiModelProperty(value = "检验人")
    private Long itemCheckBy;

    @ApiModelProperty(value = "检验人名称")
    private String itemCheckByName;

}
