package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeProcessGatherResultReportDto
 * 工序采集结果报表DTO
 * @author: chaonan.hu@hand-china.com 2021/03/22 10:12:14
 **/
@Data
public class HmeProcessGatherResultReportDto implements Serializable {
    private static final long serialVersionUID = 2494843892639452704L;

    @ApiModelProperty(value = "工厂ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "SN")
    private List<String> snList;

    @ApiModelProperty(value = "物料编码")
    private List<String> materialCodeList;

    @ApiModelProperty(value = "机型")
    private String model;

    @ApiModelProperty(value = "工单号")
    private List<String> workOrderNumList;

    @ApiModelProperty(value = "数据收集组编码")
    private List<String> tagGroupCodeList;

    @ApiModelProperty(value = "数据收集项编码")
    private List<String> tagCodeList;

    @ApiModelProperty(value = "采集工序编码")
    private List<String> processCodeList;

    @ApiModelProperty(value = "采集工位编码")
    private List<String> workcellCodeList;

    @ApiModelProperty(value = "采集时间起", required = true)
    private Date gatherDateFrom;

    @ApiModelProperty(value = "采集时间至", required = true)
    private Date gatherDateTo;

    @ApiModelProperty(value = "进站时间起")
    private Date siteInDateFrom;

    @ApiModelProperty(value = "进站时间至")
    private Date siteInDateTo;

    @ApiModelProperty(value = "出站时间起")
    private Date siteOutDateFrom;

    @ApiModelProperty(value = "出站时间至")
    private Date siteOutDateTo;
}
