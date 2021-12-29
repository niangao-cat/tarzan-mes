package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备点检查询
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@Data
@ExcelSheet(zh = "设备点检行")
public class HmeEqTaskDocLineQueryDTO implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "任务单号")
    @ExcelColumn(zh = "任务单号", order = 1)
    private String docNum;
    @ApiModelProperty(value = "设备编码")
    @ExcelColumn(zh = "设备编码", order = 2)
    private String assetEncoding;
    @ApiModelProperty(value = "任务单ID")
    private String taskDocId;
    @ApiModelProperty(value = "任务单行ID")
    private String taskDocLineId;
    @ApiModelProperty(value = "项目ID")
    private String manageTagId;
    @ApiModelProperty(value = "序列号")
    @ExcelColumn(zh = "序列号", order = 0)
    private String serialNumber;
    @ApiModelProperty(value = "项目编码")
    @ExcelColumn(zh = "项目编码", order = 3)
    private String tagCode;
    @ApiModelProperty(value = "项目说明")
    @ExcelColumn(zh = "项目描述", order = 4)
    private String tagDesc;
    @ApiModelProperty(value = "数据类型")
    private String valueType;
    @ApiModelProperty(value = "数据类型说明")
    @ExcelColumn(zh = "数据类型", order = 5)
    private String valueTypeDesc;
    @ApiModelProperty(value = "精度")
    @ExcelColumn(zh = "精度", order = 6)
    private BigDecimal accuracy;
    @ApiModelProperty(value = "最小值")
    @ExcelColumn(zh = "最小值", order = 7)
    private BigDecimal minimumValue;
    @ApiModelProperty(value = "标准值")
    @ExcelColumn(zh = "标准值", order = 8)
    private BigDecimal standardValue;
    @ApiModelProperty(value = "最大值")
    @ExcelColumn(zh = "最大值", order = 9)
    private BigDecimal maximalValue;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    @ExcelColumn(zh = "单位", order = 10)
    private String uomName;
    @ApiModelProperty(value = "检验值")
    @ExcelColumn(zh = "检验值", order = 11)
    private String checkValue;
    @ApiModelProperty(value = "结果")
    @ExcelColumn(zh = "结果", order = 12)
    private String result;
    @ApiModelProperty(value = "工具")
    private String tool;
    @ApiModelProperty(value = "责任人")
    private Long responsible;
    @ApiModelProperty(value = "责任人")
    private String responsibleName;
    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注", order = 13)
    private String remark;
    @ApiModelProperty(value = "检验时间")
    private Date checkDate;
    @ApiModelProperty(value = "检验人")
    private Long checkBy;
    @ApiModelProperty(value = "检验人名称")
    private String checkByName;
    @ApiModelProperty(value = "工位")
    private String wkcId;
    @ApiModelProperty(value = "工位编码")
    private String wkcCode;
    @ApiModelProperty(value = "工位名称")
    private String wkcName;
}
