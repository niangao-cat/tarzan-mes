package com.ruike.reports.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * COS在制报表查询条件
 *
 * @author @author wenqiang.yin@hand-china.com 2021/01/27 11:26
 */
@Data
public class HmeCosInProductionDTO implements Serializable {

    private static final long serialVersionUID = -2576591625088391603L;

    @ApiModelProperty("工单")
    private List<String> workOrderNumList;

    @ApiModelProperty("工位")
    private String workcellId;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("WAFER")
    private List<String> waferList;

    @ApiModelProperty("产品编码")
    private List<String> materialCodeList;

    @ApiModelProperty("操作人")
    private Long operatorId;

    @ApiModelProperty("条码")
    private List<String> materialLotCodeList;

    @ApiModelProperty("开始时间")
    private String creationDateFrom;

    @ApiModelProperty("结束时间")
    private String creationDateTo;
}
