package com.ruike.hme.api.dto;

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
 * @author chaonan.hu@hand-china.com 2020-07-15 10:53:24
 */
@Data
@ExcelSheet(zh = "设备")
public class HmeEoTraceBackQueryDTO8 implements Serializable {
    private static final long serialVersionUID = -197372901450142087L;

    @ApiModelProperty(value = "序号")
    @ExcelColumn(zh = "序号", order = 1)
    private Long number;

    @ApiModelProperty(value = "设备Id")
    private String equipmentId;

    @ApiModelProperty(value = "设备代码")
    @ExcelColumn(zh = "设备代码", order = 3)
    private String equipmentCode;

    @ApiModelProperty(value = "设备名称")
    @ExcelColumn(zh = "设备名称", order = 2)
    private String equipmentnName;

    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME.STATION_EQ_STATUS", meaningField = "equipmentStatusMeaning")
    private String equipmentStatus;

    @ApiModelProperty(value = "状态含义")
    @ExcelColumn(zh = "状态", order = 4)
    private String equipmentStatusMeaning;

    @ApiModelProperty(value = "工序流转顺序")
    @ExcelColumn(zh = "工序流转顺序", order = 5)
    private Long lineNum;
    @ApiModelProperty(value = "工序名称")
    @ExcelColumn(zh = "工艺步骤", order = 6)
    private String parentWorkcellName;
    @ApiModelProperty(value = "作业平台类型含义")
    @ExcelColumn(zh = "作业平台类型", order = 7)
    private String jobTypeMeaning;
    @ApiModelProperty(value = "工位名称")
    @ExcelColumn(zh = "工位", order = 8)
    private String workcellName;
    @ApiModelProperty(value = "加工开始时间")
    @ExcelColumn(zh = "加工开始时间", order = 8, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteInDate;
    @ApiModelProperty(value = "加工结束时间")
    @ExcelColumn(zh = "加工结束时间", order = 9, pattern = BaseConstants.Pattern.DATETIME)
    private Date siteOutDate;
    @ApiModelProperty(value = "加工时长")
    @ExcelColumn(zh = "加工时长(分)", order = 10)
    private BigDecimal processTime;
    @ApiModelProperty(value = "进站人")
    @ExcelColumn(zh = "加工人", order = 11)
    private String createUserName;
    @ApiModelProperty(value = "不良信息标识")
    @ExcelColumn(zh = "不良", order = 12)
    private String ncInfoFlagMeaning;
    @ExcelColumn(zh = "是否返修", order = 13)
    private String isRework;
    @ApiModelProperty(value = "是否异常出站")
    @ExcelColumn(zh = "是否异常出站", order = 14)
    private String exceptionFlagMeaning;
    @ApiModelProperty(value = "工位")
    private String workcellId;
    @ApiModelProperty(value = "作业记录")
    private String jobId;
}
