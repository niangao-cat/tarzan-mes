package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/2 0:23
 */
@Data
public class HmeMakeCenterProduceBoardVO11 implements Serializable {

    private static final long serialVersionUID = -1896273830799616324L;

    @ApiModelProperty("工单")
    private String workOrderId;
    @ApiModelProperty("工序")
    private String processId;
    @ApiModelProperty("工序名称")
    private String processName;
    @ApiModelProperty("COS标识")
    private String cosFlag;
    @ApiModelProperty("产线")
    private String prodLineId;
    @ApiModelProperty("取片标识")
    private String qpFlag;
}
