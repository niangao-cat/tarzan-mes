package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/13 19:12
 */
@Data
public class HmeEquipmentLocatingDTO implements Serializable {

    private static final long serialVersionUID = -2738717570364239478L;

    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate shiftDate;
    @ApiModelProperty(value = "班次")
    private String shiftCode;
    @ApiModelProperty(value = "工位")
    private String workcellId;
    @ApiModelProperty(value = "设备ID")
    private String equipmentId;
    @ApiModelProperty(value = "站点ID")
    private String topSiteId;
    @ApiModelProperty(value = "任务单ID")
    private String taskDocId;
    @ApiModelProperty(value = "时间类型")
    private String timeType;
}
