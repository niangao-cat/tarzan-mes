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
 * @Description 工单试算报表工单数据
 * @Date 2020/8/26 16:59
 * @Author yuchao.wang
 */
@Data
public class HmeWoTrialCalculateReportWoVO implements Serializable {
    private static final long serialVersionUID = -2616131019600205959L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "工单类型")
    private String workOrderType;

    @ApiModelProperty(value = "工单类型描述")
    private String workOrderTypeDesc;

    @ApiModelProperty(value = "数量")
    private Long qty;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "交付日期")
    private LocalDate dueDate;

    @ApiModelProperty(value = "最大产量")
    private BigDecimal maxAbility;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private LocalDate dateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private LocalDate dateTo;

    @ApiModelProperty(value = "试算标识，判断该工单是否做过交期试算")
    private String trialFlag;

    @ApiModelProperty(value = "每天的明细数据")
    private List<HmeWoTrialCalculateReportWoDetailVO> detailList;
}