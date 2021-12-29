package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * 标准件检验结果
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/16 10:19
 */
@Data
@ExcelSheet(title = "标准检验结果")
public class HmeSsnInspectResultHeaderLinesVO implements Serializable {

    private static final long serialVersionUID = -9084816551947196474L;

    @ApiModelProperty("标准件检验结果头表Id")
    private String ssnInspectResultHeaderId;
    @ApiModelProperty("标准件编码")
    @ExcelColumn(title = "标准件编码", order = 6)
    private String standardSnCode;
    @ApiModelProperty("物料编码")
    @ExcelColumn(title = "物料编码", order = 7)
    private String materialCode;
    @ApiModelProperty("物料描述")
    @ExcelColumn(title = "物料描述", order = 8)
    private String materialName;
    @ApiModelProperty("芯片类型")
    @ExcelColumn(title = "芯片类型", order = 9)
    private String cosType;
    @ApiModelProperty("工作方式")
    @LovValue(value = "HME.SSN_WORK_WAY", meaningField = "workWayMeaning")
    private String workWay;
    @ApiModelProperty("工作方式含义")
    @ExcelColumn(title = "工作方式", order = 10)
    private String workWayMeaning;
    @ApiModelProperty("工位编码")
    @ExcelColumn(title = "工位编码", order = 4)
    private String workcellCode;
    @ApiModelProperty("工位描述")
    @ExcelColumn(title = "工位描述", order = 5)
    private String workcellName;
    @ApiModelProperty("日期")
    @ExcelColumn(title = "日期", order = 13)
    private String shiftDate;
    @ApiModelProperty("班次")
    @ExcelColumn(title = "班次", order = 14)
    private String shiftCode;
    @ApiModelProperty("检验人")
    @ExcelColumn(title = "操作人", order = 11)
    private String realName;
    @ApiModelProperty("标准件检验结果")
    @ExcelColumn(title = "标准件检验结果", order = 15)
    private String result;

    @ApiModelProperty("检验项目序号")
    @ExcelColumn(title = "检验项目序号", order = 16)
    private String sequence;
    @ApiModelProperty("检验项编码")
    @ExcelColumn(title = "检验项编码", order = 17)
    private String tagCode;
    @ApiModelProperty("检验项描述")
    @ExcelColumn(title = "检验项描述", order = 18)
    private String tagDescription;
    @ApiModelProperty("最小值")
    @ExcelColumn(title = "最小值", order = 19)
    private String minimumValue;
    @ApiModelProperty("最大值")
    @ExcelColumn(title = "最大值", order = 20)
    private String maximalValue;
    @ApiModelProperty("检验值")
    @ExcelColumn(title = "检验值", order = 21)
    private String inspectValue;
    @ApiModelProperty("项检验结果")
    @ExcelColumn(title = "项检验结果", order = 22)
    private String lineResult;
    @ApiModelProperty("创建日期")
    @ExcelColumn(title = "创建日期", order = 12)
    private String creationDate;

    @ApiModelProperty("产线")
    @ExcelColumn(title = "产线", order = 1)
    private String prodLineName;
    @ApiModelProperty("工艺")
    @ExcelColumn(title = "工艺", order = 2)
    private String description;
    @ApiModelProperty("工序")
    @ExcelColumn(title = "工序", order = 3)
    private String processName;
}
