package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

/**
 * QmsPqcReportVO3
 *
 * @author: chaonan.hu@hand-china.com 2020/12/12 13:53:35
 **/
@Data
@ExcelSheet(zh = "巡检报表导出")
public class QmsPqcReportVO3 implements Serializable {
    private static final long serialVersionUID = -727367980182911284L;

    @ApiModelProperty(value = "车间")
    @ExcelColumn(zh = "车间",order = 1)
    private String areaName;

    @ApiModelProperty(value = "不合格数")
    @ExcelColumn(zh = "不合格数",order = 2)
    private long ncNum;
}
