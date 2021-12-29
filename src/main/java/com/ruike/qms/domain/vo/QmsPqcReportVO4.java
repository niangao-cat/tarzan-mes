package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

/**
 * QmsPqcReportVO4
 *
 * @author: chaonan.hu@hand-china.com 2020/12/12 13:53:56
 **/
@Data
@ExcelSheet(zh = "巡检报表导出")
public class QmsPqcReportVO4 implements Serializable {
    private static final long serialVersionUID = -8749279529514966027L;

    @ApiModelProperty(value = "工序")
    @ExcelColumn(zh = "工序",order = 1)
    private String processName;

    @ApiModelProperty(value = "不合格数")
    @ExcelColumn(zh = "不合格数",order = 2)
    private long ncNum;
}
