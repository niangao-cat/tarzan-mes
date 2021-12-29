package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;
import java.util.Date;

/**
 * QmsPqcReportVO5
 *
 * @author: chaonan.hu@hand-china.com 2020/12/12 13:54:45
 **/
@Data
@ExcelSheet(zh = "巡检报表导出")
public class QmsPqcReportVO5 implements Serializable {
    private static final long serialVersionUID = 1817844885497401416L;

    @ApiModelProperty(value = "车间")
    @ExcelColumn(zh = "车间",order = 1)
    private String areaName;

    @ApiModelProperty(value = "工序")
    @ExcelColumn(zh = "工序",order = 2)
    private String processName;

    @ApiModelProperty(value = "检验员")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "检验员")
    @ExcelColumn(zh = "检验员",order = 3)
    private String lastUpdatedByName;

    @ApiModelProperty(value = "问题点")
    @ExcelColumn(zh = "问题点",order = 4)
    private String attribute1;

    @ApiModelProperty(value = "检验时间")
    private Date inspectionFinishDate;

    @ApiModelProperty(value = "检验时间")
    @ExcelColumn(zh = "检验时间",order = 5)
    private String inspectionFinishDateStr;
}
