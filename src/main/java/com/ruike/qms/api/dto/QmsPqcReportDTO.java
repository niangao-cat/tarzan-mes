package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * QmsPqcReportDTO
 *
 * @author: chaonan.hu@hand-china.com 2020/12/11 15:38:11
 **/
@Data
public class QmsPqcReportDTO implements Serializable {
    private static final long serialVersionUID = -5926129833142773549L;

    @ApiModelProperty(value = "事业部ID")
    private String departmentId;

    @ApiModelProperty(value = "车间ID")
    private String workshopId;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "检验时间从，前台传入")
    private String inspectionFinishFromDateStr;

    @ApiModelProperty(value = "检验时间从")
    private Date inspectionFinishFromDate;

    @ApiModelProperty(value = "检验时间至，前台传入")
    private String inspectionFinishToDateStr;

    @ApiModelProperty(value = "检验时间至")
    private Date inspectionFinishToDate;
}
