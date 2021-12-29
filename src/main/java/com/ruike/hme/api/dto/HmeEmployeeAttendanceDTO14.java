package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeEmployeeAttendanceDTO14
 *
 * @author chaonan.hu@hand-china.com 2021-03-19 09:37:12
 **/
@Data
public class HmeEmployeeAttendanceDTO14 implements Serializable {
    private static final long serialVersionUID = -676278985356862868L;

    @ApiModelProperty(value = "员工ID")
    private String userId;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "时间从")
    private Date dateFrom;

    @ApiModelProperty(value = "时间至")
    private Date dateTo;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "WO编码")
    private String workOrderNum;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "EO标识")
    private String identification;
}
