package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 异常信息查看报表请求VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/14 15:02
 */
@Data
public class HmeExceptionReportVO implements Serializable {

    private static final long serialVersionUID = -8241732789520760536L;

    @ApiModelProperty(value = "发起开始时间")
    private String startTime;

    @ApiModelProperty(value = "发起结束时间")
    private String endTime;

    @ApiModelProperty(value = "制造部ID")
    private String areaId;

    @ApiModelProperty(value = "产线ID")
    private String productionLineId;

    @ApiModelProperty(value = "班组ID")
    private String shiftId;

    @ApiModelProperty(value = "班次")
    private String shiftCode;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "异常产品序列号")
    private String identification;

    @ApiModelProperty(value = "异常类型")
    private String exceptionType;

    @ApiModelProperty(value = "异常描述")
    private String exceptionName;

    @ApiModelProperty(value = "异常描述Id")
    private String exceptionId;

    @ApiModelProperty(value = "异常状态")
    private String exceptionStatus;
}
