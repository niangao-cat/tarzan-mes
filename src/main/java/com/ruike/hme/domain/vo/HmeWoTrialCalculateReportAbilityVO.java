package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.time.*;
import java.util.*;

/**
 * @Classname HmeWoTrialCalculateReportAbilityVO
 * @Description 工单试算报表产线负荷
 * @Date 2020/8/26 16:59
 * @Author yuchao.wang
 */
@Data
public class HmeWoTrialCalculateReportAbilityVO implements Serializable {
    private static final long serialVersionUID = 5452450873955400886L;

    public HmeWoTrialCalculateReportAbilityVO(){}

    public HmeWoTrialCalculateReportAbilityVO(LocalDate shiftDate){
        this.shiftDate = shiftDate;
        this.ability = "0";
    }

    public HmeWoTrialCalculateReportAbilityVO(LocalDate shiftDate, Long ability){
        this.shiftDate = shiftDate;
        this.ability = Objects.isNull(ability) ? "0" : ability.toString();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private LocalDate shiftDate;

    @ApiModelProperty(value = "产线负荷")
    private String ability;
}