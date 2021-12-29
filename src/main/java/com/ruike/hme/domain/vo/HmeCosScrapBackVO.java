package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * COS报废撤回-查询VO
 *
 * @author sanfeng.zhang@hand-china.com 2021/1/26 9:43
 */
@Data
public class HmeCosScrapBackVO implements Serializable {

    private static final long serialVersionUID = -8585472932045835776L;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "热沉")
    private String hotSinkCode;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "不良代码")
    private String ncCodeId;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "报废开始时间")
    private String startScrapTime;

    @ApiModelProperty(value = "报废结束时间")
    private String endScrapTime;
}
