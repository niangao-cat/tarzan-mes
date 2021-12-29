package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

@Data
public class HmeCosWipQueryVO implements Serializable {
    private static final long serialVersionUID = -1389233793307102766L;

    @ApiModelProperty("工厂")
    private String siteName;
    @ApiModelProperty(value = "工单编号")
    private String workOrderNum;
    @ApiModelProperty(value = "产品编码")
    private String productionCode;
    @ApiModelProperty(value = "产品名称")
    private String productionName;
    @ApiModelProperty(value = "芯片盒子")
    private String materialLotCode;
    @ApiModelProperty(value = "wafer")
    private String waferNum;
    @ApiModelProperty(value = "芯片类型编码")
    private String cosType;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "数量")
    private String primaryUomQty;
    @ApiModelProperty(value = "仓库")
    private String parentLocatorCode;
    @ApiModelProperty(value = "货位")
    private String locatorCode;
    @ApiModelProperty(value = "工艺步骤加工次数")
    private String eoStepNum;
    @ApiModelProperty(value = "当前工位")
    private String workcellCode;
    @ApiModelProperty(value = "工位名称")
    private String workcellName;

    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;
    @ApiModelProperty(value = "设备名称")
    private String assetName;

    @LovValue(value = "HME.JOB_TYPE", meaningField = "jobTypeMeaning")
    @ApiModelProperty(value = "作业平台")
    private String jobType;

    @ApiModelProperty(value = "作业平台含义")
    private String jobTypeMeaning;

    @ApiModelProperty(value = "进站时间")
    private Date siteInDate;
    @ApiModelProperty(value = "进站人员")
    private String siteInBy;
    @ApiModelProperty(value = "出站时间")
    private Date siteOutDate;
    @ApiModelProperty(value = "出站人员")
    private String siteOutBy;
    @ApiModelProperty(value = "是否返修标识")
    private String reworkflag ;
}
