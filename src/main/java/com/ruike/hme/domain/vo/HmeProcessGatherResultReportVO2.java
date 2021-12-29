package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeProcessGatherResultReportVO2
 * 工序采集结果报表导出VO
 * @author: chaonan.hu@hand-china.com 2021/03/22 13:42:15
 **/
@Data
@ExcelSheet(zh = "工序采集结果")
public class HmeProcessGatherResultReportVO2 implements Serializable {
    private static final long serialVersionUID = 54323840485347674L;

    @ApiModelProperty(value = "SN")
    @ExcelColumn(zh = "SN",order = 1)
    private String sn;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 2)
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    @ExcelColumn(zh = "物料名称",order = 3)
    private String materialName;

    @ApiModelProperty(value = "机型")
    @ExcelColumn(zh = "机型",order = 4)
    private String model;

    @ApiModelProperty(value = "工单号")
    @ExcelColumn(zh = "工单号",order = 5)
    private String workOrderNum;

    @ApiModelProperty(value = "数据收集组编码")
    @ExcelColumn(zh = "数据收集组编码",order = 6)
    private String tagGroupCode;

    @ApiModelProperty(value = "数据收集组描述")
    @ExcelColumn(zh = "数据收集组描述",order = 7)
    private String tagGroupDescription;

    @ApiModelProperty(value = "数据收集项编码")
    @ExcelColumn(zh = "数据收集项编码",order = 8)
    private String tagCode;

    @ApiModelProperty(value = "数据收集项描述")
    @ExcelColumn(zh = "数据收集项描述",order = 9)
    private String tagDescription;

    @ApiModelProperty(value = "上限")
    @ExcelColumn(zh = "上限",order = 10)
    private BigDecimal maximalValue;

    @ApiModelProperty(value = "下限")
    @ExcelColumn(zh = "下限",order = 11)
    private BigDecimal minimumValue;

    @ApiModelProperty(value = "采集结果")
    @ExcelColumn(zh = "采集结果",order = 12)
    private String result;

    @ApiModelProperty(value = "采集工序编码")
    @ExcelColumn(zh = "采集工序编码",order = 13)
    private String processCode;

    @ApiModelProperty(value = "采集工序描述")
    @ExcelColumn(zh = "采集工序描述",order = 14)
    private String processName;

    @ApiModelProperty(value = "采集工位编码")
    @ExcelColumn(zh = "采集工位编码",order = 15)
    private String workcellCode;

    @ApiModelProperty(value = "采集工位描述")
    @ExcelColumn(zh = "采集工位描述",order = 16)
    private String workcellName;

    @ApiModelProperty(value = "采集人")
    @ExcelColumn(zh = "采集人",order = 17)
    private String realName;

    @ApiModelProperty(value = "采集时间")
    @ExcelColumn(zh = "采集时间",order = 18)
    private String gatherDateStr;

    @ApiModelProperty(value = "进站时间")
    @ExcelColumn(zh = "进站时间",order = 19)
    private String siteInDateStr;

    @ApiModelProperty(value = "出站时间")
    @ExcelColumn(zh = "出站时间",order = 20)
    private String siteOutDateStr;
}
