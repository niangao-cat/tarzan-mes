package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * description 计划达成率报表达成率DTO
 *
 * @author quan.luo@hand-china.com 2020/11/25 20:41
 */
@Data
public class HmePlanRateReportRateDTO implements Serializable {

    private static final long serialVersionUID = 264585147564410216L;

    @ApiModelProperty(value = "名称")
    private String description;

    @ApiModelProperty(value = "计划投产")
    private BigDecimal plannedProduction;

    @ApiModelProperty(value = "实际投产")
    private BigDecimal actualProduction;

    @ApiModelProperty(value = "实际投产比例")
    private String actualProductionRatio;

    @ApiModelProperty(value = "计划交付")
    private BigDecimal plannedDelivery;

    @ApiModelProperty(value = "实际交付")
    private BigDecimal actualAelivery;

    @ApiModelProperty(value = "实际交付比例")
    private String actualAeliveryRatio;

    @ApiModelProperty(value = "在制数量")
    private BigDecimal quantityUnderProduction;

    @ApiModelProperty(value = "在制标准")
    private BigDecimal inProcessStandards;

    @ApiModelProperty(value = "在制百分比")
    private String percentageInProduction;

    @ApiModelProperty(value = "wkcId")
    private String workcellId;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "原始班次")
    private String originalShiftCode;

    @ApiModelProperty(value = "时间")
    private LocalDate shiftDate;

}
