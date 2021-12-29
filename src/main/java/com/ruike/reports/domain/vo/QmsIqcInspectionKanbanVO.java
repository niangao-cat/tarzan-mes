package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * IQC检验看板
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 10:26
 */
@Data
public class QmsIqcInspectionKanbanVO {
    @ApiModelProperty(value = "检验员名称")
    private String inspectorName;
    @ApiModelProperty(value = "供应商")
    private String supplierName;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "检验批次")
    private BigDecimal totalNum;
    @ApiModelProperty(value = "合格批次")
    private BigDecimal okNum;
    @ApiModelProperty(value = "不合格批次")
    private BigDecimal ngNum;
    @ApiModelProperty(value = "合格率")
    private BigDecimal okRate;
    @ApiModelProperty(value = "不良率")
    private BigDecimal ngRate;
}
