package com.ruike.reports.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ReportDispatchDetailsDTO
 * @Description 派工明细报表
 * @Author lkj
 * @Date 2020/12/15
 */
@Data
public class ReportDispatchDetailsDTO {

    @ApiModelProperty("默认站点ID")
    private String siteId;

    @ApiModelProperty("生产线ID")
    private List<String> prodLineIdList;

    @ApiModelProperty("工单号")
    private List<String> workOrderNum;

    @ApiModelProperty("物料ID")
    private List<String> materialId;

    @ApiModelProperty("工段编码")
    private List<String> lineWorkcellIdList;

    @ApiModelProperty("日期")
    private String shiftDate;

    @ApiModelProperty("班次")
    private String shiftCode;

    @ApiModelProperty("开始时间")
    private String startDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("派工时段起")
    private String startWocellDate;

    @ApiModelProperty("派工时段至")
    private String endWocellDate;

    @ApiModelProperty("派工人")
    private String userId;



}
