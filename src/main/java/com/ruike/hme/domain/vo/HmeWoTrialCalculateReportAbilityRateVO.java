package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;
import java.time.*;
import java.util.*;

/**
 * @Classname HmeWoTrialCalculateReportAbilityRateVO
 * @Description 工单试算报表产线负荷比例
 * @Date 2020/8/26 16:59
 * @Author yuchao.wang
 */
@Data
public class HmeWoTrialCalculateReportAbilityRateVO implements Serializable {
    private static final long serialVersionUID = 1186456052666524845L;

    public HmeWoTrialCalculateReportAbilityRateVO(){}

    public HmeWoTrialCalculateReportAbilityRateVO(LocalDate shiftDate){
        this.shiftDate = shiftDate;
        this.abilityRate = "0%";
    }

    public HmeWoTrialCalculateReportAbilityRateVO(LocalDate shiftDate, BigDecimal abilityRate){
        this.shiftDate = shiftDate;
        this.abilityRate = Objects.isNull(abilityRate) ? "0%" : String.format("%.2f",abilityRate) + "%";
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private LocalDate shiftDate;

    @ApiModelProperty(value = "产线负荷比例")
    private String abilityRate;
}