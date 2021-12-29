package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/14 16:17
 */
@Data
public class HmeEquipmentUpadteDTO implements Serializable {

    private static final long serialVersionUID = -8949786091345635735L;

    @ApiModelProperty(value = "数据类型")
    private String valueType;

    @ApiModelProperty(value = "结果")
    private String result;

    @ApiModelProperty(value = "检验值")
    private String checkValue;

    @ApiModelProperty(value = "设备管理任务行ID")
    private String taskDocLineId;

    @ApiModelProperty(value = "员工工位ID")
    private String workcellId;

    @ApiModelProperty(value = "设备管理任务单表ID")
    private String taskDocId;

    @ApiModelProperty(value = "项目ID")
    private String manageTagId;

}
