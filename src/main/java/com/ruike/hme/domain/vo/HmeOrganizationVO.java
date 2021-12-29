package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/8 11:38
 */
@Data
public class HmeOrganizationVO implements Serializable {

    private static final long serialVersionUID = 7648391424781050531L;

    @ApiModelProperty("工位")
    private String workcellId;
    @ApiModelProperty("工序")
    private String processId;
    @ApiModelProperty("工序名称")
    private String processName;
    @ApiModelProperty("工段")
    private String lineWorkcellId;
    @ApiModelProperty("工段编码")
    private String lineWorkcellCode;
    @ApiModelProperty("工段名称")
    private String lineWorkcellName;
    @ApiModelProperty("产线Id")
    private String prodLineId;
    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("产线名称")
    private String prodLineName;
    @ApiModelProperty("车间名称")
    private String workshopName;
    @ApiModelProperty("部门名称")
    private String areaName;
}
