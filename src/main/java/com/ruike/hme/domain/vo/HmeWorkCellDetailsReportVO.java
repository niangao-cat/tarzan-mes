package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeWorkCellDetailsReportVO - 工位产量明细报表请求VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 14:51
 */
@Data
public class HmeWorkCellDetailsReportVO implements Serializable {

    private static final long serialVersionUID = 3288408673413937143L;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "站点")
    private String siteId;

    @ApiModelProperty(value = "车间")
    private String workshop;

    @ApiModelProperty(value = "产线")
    private String productionLineId;

    @ApiModelProperty(value = "工段")
    private String lineWorkcellId;

    private String workcellId;

    @ApiModelProperty(value = "工单")
    private String workOrder;

    @ApiModelProperty(value = "产品料号")
    private String snMaterialId;

    @ApiModelProperty(value = "产品序列号")
    private String materialLotCode;

    @ApiModelProperty(value = "返修标识")
    private String reworkFlag;

    @ApiModelProperty(value = "进/出站标识 N-进站 Y-出站")
    private String siteFlag;

    @ApiModelProperty(value = "工序")
    private String processId;

    private List<String> workCellIdList;

}
