package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnVO19 implements Serializable {
    private static final long serialVersionUID = -7020929872169212596L;
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("工单")
    private String workOrderNum;
    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("产线有效性")
    private String enableFlag;
}
