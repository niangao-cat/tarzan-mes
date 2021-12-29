package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品追溯
 *
 * @author chaonan.hu@hand-china.com 2020-07-15 14:01:13
 */
@Data
@ExcelSheet(zh = "异常信息")
public class HmeEoTraceBackQueryDTO9 implements Serializable {
    private static final long serialVersionUID = 6254157599169978579L;

    @ApiModelProperty(value = "序号")
    @ExcelColumn(zh = "序号", order = 1)
    private Long number;

    @ApiModelProperty(value = "异常Id")
    private String exceptionId;

    @ApiModelProperty(value = "异常事项")
    @ExcelColumn(zh = "异常事项", order = 2)
    private String exceptionName;

    @ApiModelProperty(value = "异常描述")
    @ExcelColumn(zh = "异常描述", order = 3)
    private String exceptionRemark;

    @ApiModelProperty(value = "发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "发生时间", order = 4, pattern = BaseConstants.Pattern.DATETIME)
    private Date creationDate;

    @ApiModelProperty(value = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelColumn(zh = "完成时间", order = 5, pattern = BaseConstants.Pattern.DATETIME)
    private Date closeTime;

    @ApiModelProperty(value = "申报人")
    private String createdBy;

    @ApiModelProperty(value = "申报人姓名")
    @ExcelColumn(zh = "申报人", order = 6)
    private String createdByName;

    @ApiModelProperty(value = "处理人")
    private String respondedBy;

    @ApiModelProperty(value = "处理人姓名")
    @ExcelColumn(zh = "处理人", order = 7)
    private String respondedByName;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "EO")
    private String eoId;

    @ApiModelProperty(value = "工序流转顺序")
    @ExcelColumn(zh = "工序流转顺序", order = 8)
    private Long lineNum;
    @ApiModelProperty(value = "工序名称")
    @ExcelColumn(zh = "工艺步骤", order = 9)
    private String parentWorkcellName;
    @ApiModelProperty(value = "作业平台类型含义")
    @ExcelColumn(zh = "作业平台类型", order = 10)
    private String jobTypeMeaning;
    @ApiModelProperty(value = "工位名称")
    @ExcelColumn(zh = "工位", order = 11)
    private String workcellName;
    @ApiModelProperty(value = "加工开始时间")
    @ExcelColumn(zh = "加工开始时间", order = 12, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteInDate;
    @ApiModelProperty(value = "加工结束时间")
    @ExcelColumn(zh = "加工结束时间", order = 13, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteOutDate;
    @ApiModelProperty(value = "加工时长")
    @ExcelColumn(zh = "加工时长(分)", order = 14)
    private BigDecimal processTime;
    @ApiModelProperty(value = "进站人")
    @ExcelColumn(zh = "加工人", order = 15)
    private String createUserName;
    @ApiModelProperty(value = "不良信息标识")
    @ExcelColumn(zh = "不良", order = 16)
    private String ncInfoFlagMeaning;
    @ExcelColumn(zh = "是否返修", order = 17)
    private String isRework;
    @ApiModelProperty(value = "是否异常出站")
    @ExcelColumn(zh = "是否异常出站", order = 18)
    private String exceptionFlagMeaning;
}
