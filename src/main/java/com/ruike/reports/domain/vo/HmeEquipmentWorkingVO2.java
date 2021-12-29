package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeEquipmentWorkingVO2
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/17 11:16
 */
@Data
public class HmeEquipmentWorkingVO2 implements Serializable {

    private static final long serialVersionUID = -5105181981471668225L;

    @ApiModelProperty("workcellId")
    private String workcellId;

    @ApiModelProperty("日期")
    @ExcelColumn(title = "日期")
    private String dateString;

    @ApiModelProperty("计划运行时间（h）")
    @ExcelColumn(title = "计划运行时间（h）")
    private BigDecimal planDate;

    @ApiModelProperty("实际运行时间（h）")
    @ExcelColumn(title = "实际运行时间（h）")
    private BigDecimal actualDate;

    @ApiModelProperty("停机时间（h）")
    @ExcelColumn(title = "停机时间（h）")
    private BigDecimal stopDate;

    @ApiModelProperty("开机率")
    @ExcelColumn(title = "开机率")
    private BigDecimal boot;

    @ApiModelProperty("利用率")
    @ExcelColumn(title = "利用率")
    private BigDecimal utilization;
}
