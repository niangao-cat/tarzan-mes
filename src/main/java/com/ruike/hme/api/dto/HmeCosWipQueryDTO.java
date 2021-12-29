package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HmeCosWipQueryDTO implements Serializable {
    private static final long serialVersionUID = -7944769091813227503L;

    @ApiModelProperty("工厂")
    private String siteId;
    @ApiModelProperty(value = "工单编号")
    private String workOrderNum;
    @ApiModelProperty(value = "产品编码")
    private String productionCode;
    @ApiModelProperty(value = "产品名称")
    private String productionName;
    @ApiModelProperty(value = "芯片盒子")
    private String materialLotCode;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "工位")
    private String workCellId;
    @ApiModelProperty(value = "作业平台")
    private String jobType;
    @ApiModelProperty(value = "进站时间从")
    private String siteInDateFrom;
    @ApiModelProperty(value = "进站时间至")
    private String siteInDateTo;
    @ApiModelProperty(value = "出站时间从")
    private String siteOutDateFrom;
    @ApiModelProperty(value = "出站时间至")
    private String siteOutDateTo;
    @ApiModelProperty(value = "waferNum")
    private String waferNum;
    @ApiModelProperty(value = "equipmentId")
    private String equipmentId;
}
