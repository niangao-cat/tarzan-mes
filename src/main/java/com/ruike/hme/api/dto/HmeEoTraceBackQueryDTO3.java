package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品追溯
 *
 * @author jiangling.zheng@hand-china.com 2020-04-21 13:18
 */
@Data
@ExcelSheet(zh = "工艺质量")
public class HmeEoTraceBackQueryDTO3 implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "序号")
    @ExcelColumn(zh = "序号", order = 1)
    private long lineNum;
    @ApiModelProperty(value = "工序ID")
    private String parentWorkcellId;
    @ApiModelProperty(value = "工序编码")
    private String parentWorkcellCode;
    @ApiModelProperty(value = "工序名称")
    private String parentWorkcellName;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工位编码")
    private String workcellCode;
    @ApiModelProperty(value = "工位名称")
    private String workcellName;
    @ApiModelProperty(value = "位置")
    @ExcelColumn(zh = "位置", order = 3)
    private String position;
    @ApiModelProperty(value = "设备")
    @ExcelColumn(zh = "设备", order = 4)
    private String equipment;
    @ApiModelProperty(value = "项目编码")
    private String tagCode;
    @ApiModelProperty(value = "项目说明")
    @ExcelColumn(zh = "项目", order = 2)
    private String tagDescription;
    @ApiModelProperty(value = "下限")
    @ExcelColumn(zh = "下限", order = 5)
    private String minimumValue;
    @ApiModelProperty(value = "标准值")
    @ExcelColumn(zh = "标准值", order = 6)
    private String standardValue;
    @ApiModelProperty(value = "上限")
    @ExcelColumn(zh = "上限", order = 7)
    private String maximalValue;
    @ApiModelProperty(value = "结果")
    @ExcelColumn(zh = "结果", order = 8)
    private String result;
    @ApiModelProperty(value = "单路状态")
    @LovValue(lovCode = "HME.COUPLING_SINGLE_STATUS", meaningField = "cosStatusMeaning")
    private String cosStatus;
    @ApiModelProperty(value = "单路状态含义")
    @ExcelColumn(zh = "单路状态", order = 9)
    private String cosStatusMeaning;
    @ApiModelProperty(value = "备注")
    @ExcelColumn(zh = "备注", order = 10)
    private String remark;
    @ApiModelProperty(value = "作业记录ID")
    private String jobId;
    @ApiModelProperty(value = "收集头ID")
    private String collectHeaderId;

    @ApiModelProperty(value = "工序流转顺序")
    @ExcelColumn(zh = "工序流转顺序", order = 11)
    private Long headerLineNum;
    @ApiModelProperty(value = "工序名称")
    @ExcelColumn(zh = "工艺步骤", order = 12)
    private String headerProcessName;
    @ApiModelProperty(value = "作业平台类型含义")
    @ExcelColumn(zh = "作业平台类型", order = 13)
    private String jobTypeMeaning;
    @ApiModelProperty(value = "工位名称")
    @ExcelColumn(zh = "工位", order = 14)
    private String headerWorkcellName;
    @ApiModelProperty(value = "加工开始时间")
    @ExcelColumn(zh = "加工开始时间", order = 15, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteInDate;
    @ApiModelProperty(value = "加工结束时间")
    @ExcelColumn(zh = "加工结束时间", order = 16, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteOutDate;
    @ApiModelProperty(value = "加工时长")
    @ExcelColumn(zh = "加工时长(分)", order = 17)
    private BigDecimal processTime;
    @ApiModelProperty(value = "进站人")
    @ExcelColumn(zh = "加工人", order = 18)
    private String createUserName;
    @ApiModelProperty(value = "不良信息标识")
    @ExcelColumn(zh = "不良", order = 19)
    private String ncInfoFlagMeaning;
    @ExcelColumn(zh = "是否返修", order = 20)
    private String isRework;
    @ApiModelProperty(value = "是否异常出站")
    @ExcelColumn(zh = "是否异常出站", order = 21)
    private String exceptionFlagMeaning;
}
