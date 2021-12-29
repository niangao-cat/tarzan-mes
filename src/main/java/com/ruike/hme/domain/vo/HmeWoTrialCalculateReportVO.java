package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;
import java.time.*;
import java.util.*;

/**
 * @Classname HmeWoTrialCalculateReportVO
 * @Description 工单试算报表返回数据
 * @Date 2020/8/26 17:25
 * @Author yuchao.wang
 */
@Data
public class HmeWoTrialCalculateReportVO implements Serializable {
    private static final long serialVersionUID = -2616131019600205959L;

    @ApiModelProperty(value = "工单数据")
    List<HmeWoTrialCalculateReportWoVO> woList;

    @ApiModelProperty(value = "产线负荷")
    List<HmeWoTrialCalculateReportAbilityVO> prodLineAbilityList;

    @ApiModelProperty(value = "产线负荷比例")
    List<HmeWoTrialCalculateReportAbilityRateVO> prodLineAbilityRateList;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "报表时间区间")
    List<LocalDate> reportTimeSpanList;
}