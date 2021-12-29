package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/14 17:14
 */
@Data
public class HmeNonStandardReportVO implements Serializable {

    private static final long serialVersionUID = -2981355821764881692L;

    @ApiModelProperty(value = "站点id")
    private String siteId;

    @ApiModelProperty(value = "工单状态列表")
    private List<String> woStatus;

    @ApiModelProperty(value = "车间id")
    private String workshopId;

    @ApiModelProperty(value = "产线id")
    private String prodLineId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "产品id")
    private String materialId;

    @ApiModelProperty(value = "生产订单创建开始时间")
    private String creationStartDate;

    @ApiModelProperty(value = "生产订单创建结束时间")
    private String creationEndDate;

    @ApiModelProperty(value = "生产订单下达开始时间")
    private String releaseStartDate;

    @ApiModelProperty(value = "生产订单下达结束时间")
    private String releaseEndDate;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;
}
