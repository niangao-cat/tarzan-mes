package com.ruike.qms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * QmsPqcReportVO2
 *
 * @author: chaonan.hu@hand-china.com 2020/12/12 10:11:11
 **/
@Data
public class QmsPqcReportVO2 implements Serializable {
    private static final long serialVersionUID = 2680946755159095759L;

    @ApiModelProperty(value = "巡检单头ID")
    private String pqcHeaderId;

    @ApiModelProperty(value = "巡检单行ID")
    private String pqcLineId;

    @ApiModelProperty(value = "巡检单明细ID")
    private String pqcDetailsId;

    @ApiModelProperty(value = "车间ID")
    private String areaId;

    @ApiModelProperty(value = "车间名称")
    private String areaName;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "检验员ID")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "检验员名称")
    private String lastUpdatedByName;

    @ApiModelProperty(value = "检验时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionFinishDate;

    @ApiModelProperty(value = "问题点")
    private String attribute1;
}
