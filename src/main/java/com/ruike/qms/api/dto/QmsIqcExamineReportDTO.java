package com.ruike.qms.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * QmsIqcExamineReportDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/12/10 10:55
 **/
@Data
public class QmsIqcExamineReportDTO implements Serializable {
    private static final long serialVersionUID = 6114620199736817735L;

    @ApiModelProperty(value = "单据号")
    private String iqcNumber;

    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    @ApiModelProperty(value = "检验组ID")
    private String tagGroupId;

    @ApiModelProperty(value = "检验员ID")
    private Long lastUpdatedBy;

//    @ApiModelProperty(value = "检验开始时间从,前台传入")
//    private String inspectionStartDateFromStr;
//
//    @ApiModelProperty(value = "检验开始时间从")
//    private Date inspectionStartDateFrom;
//
//    @ApiModelProperty(value = "检验开始时间至，前台传入")
//    private String inspectionStartDateToStr;
//
//    @ApiModelProperty(value = "检验开始时间至")
//    private Date inspectionStartDateTo;

    @ApiModelProperty(value = "检验完成时间从，前台传入")
    private String inspectionFinishDateFromStr;

    @ApiModelProperty(value = "检验完成时间从")
    private Date inspectionFinishDateFrom;

    @ApiModelProperty(value = "检验完成时间至,前台传入")
    private String inspectionFinishDateToStr;

    @ApiModelProperty(value = "检验完成时间至")
    private Date inspectionFinishDateTo;

    @ApiModelProperty(value = "检验结果")
    private String inspectionResult;

}
