package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeWorkCellDetailsReportVO - 工位产量明细报表返回VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 15:16
 */
@Data
public class HmeWorkCellDetailsReportVO2 implements Serializable {

    private static final long serialVersionUID = -4640821094339438627L;

    @ApiModelProperty(value = "SN作业ID")
    private String jobId;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "生产线")
    private String productionLineName;

    private String productionLineId;

    @ApiModelProperty(value = "工段")
    private String lineWorkcellName;

    private String lineWorkcellId;

    @ApiModelProperty(value = "工序")
    private String processWorkcellName;

    private String processId;

    @ApiModelProperty(value = "工位")
    private String stationWorkcellName;

    @ApiModelProperty(value = "产品料号")
    private String snMaterialName;

    @ApiModelProperty(value = "产品描述")
    private String materialDesc;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "产品序列号")
    private String materialLotCode;

    @ApiModelProperty(value = "作业类型")
    private String jobTypeName;

    @ApiModelProperty(value = "作业编码")
    @JsonIgnore
    private String jobPlatformCode;

    @ApiModelProperty(value = "进出站标识")
    @JsonIgnore
    private String flag;

    @ApiModelProperty(value = "作业平台")
    private String jobPlatform;

    @ApiModelProperty(value = "数量")
    private String primaryUomQty;

    @ApiModelProperty(value = "返修标识")
    private String reworkFlag;

    @ApiModelProperty(value = "进站创建人")
    @JsonIgnore
    private String siteInBy;

    @ApiModelProperty(value = "出站创建人")
    @JsonIgnore
    private String siteOutBy;

    @ApiModelProperty(value = "作业人")
    private String workerName;

    @ApiModelProperty(value = "作业时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date workerTime;


}
