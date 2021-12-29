package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * HmeEmployeeAttendanceExportVO5
 *
 * @author: chaonan.hu@hand-china.com 2021/03/15 09:57:21
 **/
@Data
@ExcelSheet(zh = "员工产量汇总")
public class HmeEmployeeAttendanceExportVO5 implements Serializable {
    private static final long serialVersionUID = 7373063310813245771L;

    @ApiModelProperty(value = "员工ID")
    private String userId;

    @ApiModelProperty(value = "员工")
    @ExcelColumn(zh = "员工",order = 1)
    private String userName;

    @ApiModelProperty(value = "工号")
    @ExcelColumn(zh = "工号",order = 2)
    private String userNum;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "产线名称")
    @ExcelColumn(zh = "产线",order = 3)
    private String prodLineName;

    @ApiModelProperty(value = "工段ID")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工段名称")
    @ExcelColumn(zh = "工段",order = 4)
    private String lineWorkcerllName;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序名称")
    @ExcelColumn(zh = "工序",order = 4)
    private String processName;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    @ExcelColumn(zh = "物料编码",order = 5)
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    @ExcelColumn(zh = "物料描述",order = 6)
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    @ExcelColumn(zh = "物料版本",order = 7)
    private String materialVersion;

    @ApiModelProperty(value = "实际产出")
    @ExcelColumn(zh = "实际产出",order = 8)
    private BigDecimal actualOutputNumber;

    @ApiModelProperty(value = "产量")
    @ExcelColumn(zh = "产量",order = 9)
    private BigDecimal countNumber;

    @ApiModelProperty(value = "在制数")
    @ExcelColumn(zh = "在制数",order = 10)
    private BigDecimal inMakeNum;

    @ApiModelProperty(value = "不良数")
    @ExcelColumn(zh = "不良数",order = 11)
    private BigDecimal defectsNumber;

    @ApiModelProperty(value = "返修数")
    @ExcelColumn(zh = "返修数",order = 12)
    private BigDecimal repairNum;

    @ApiModelProperty(value = "一次合格率")
    @ExcelColumn(zh = "一次合格率",order = 13)
    private String firstPassRate;

    @ApiModelProperty(value = "生产总时长")
    @ExcelColumn(zh = "生产总时长",order = 14)
    private String totalProductionTime;

    @ApiModelProperty(value = "时间从", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateFrom;

    @ApiModelProperty(value = "时间至", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTo;
}
