package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/7 15:51
 */
@Data
public class HmeEquipmentWorkingVO5 implements Serializable {

    private static final long serialVersionUID = 7697186384096926646L;

    @ApiModelProperty("日期")
    private String dateString;

    @ApiModelProperty("计划运行总时长")
    private BigDecimal totalPlanDate;

    @ApiModelProperty("实际运行总时长")
    private BigDecimal totalActualDate;

    @ApiModelProperty("停机总时长")
    private BigDecimal totalStopDate;

    @ApiModelProperty("平均开机率")
    private BigDecimal avgBoot;

    @ApiModelProperty("平均利用率")
    private BigDecimal avgUtilization;
}
