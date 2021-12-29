package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * QmsIqcExamineReportVO2
 *
 * @author: chaonan.hu@hand-china.com 2020/12/10 14:35:56
 **/
@Data
public class QmsIqcExamineReportVO2 implements Serializable {
    private static final long serialVersionUID = -529608022063177655L;

    @ApiModelProperty(value = "检验总数")
    private BigDecimal totalNum;

    @ApiModelProperty(value = "合格数")
    private BigDecimal okNum;

    @ApiModelProperty(value = "不合格数")
    private BigDecimal ngNum;

    @ApiModelProperty(value = "合格率")
    private BigDecimal okProbability;

    @ApiModelProperty(value = "不合格率")
    private BigDecimal ngProbability;
}
