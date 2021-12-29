package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.time.*;
import java.util.*;

/**
 * @Classname HmeWoTrialCalculateReportDetailVO
 * @Description 工单试算报表工单日期数据
 * @Date 2020/8/26 16:59
 * @Author yuchao.wang
 */
@Data
public class HmeWoTrialCalculateReportWoDetailVO implements Serializable {
    private static final long serialVersionUID = -1205437604630608683L;

    public HmeWoTrialCalculateReportWoDetailVO(){}

    public HmeWoTrialCalculateReportWoDetailVO(LocalDate shiftDate){
        this.shiftDate = shiftDate;
        this.trialQty = 0L;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    private LocalDate shiftDate;

    @ApiModelProperty(value = "预排数量")
    private Long trialQty;
}