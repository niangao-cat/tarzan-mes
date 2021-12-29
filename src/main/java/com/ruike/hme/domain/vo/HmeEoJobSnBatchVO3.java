package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO3 implements Serializable {
    private static final long serialVersionUID = 8325021028156342373L;
    @ApiModelProperty(value = "EOID")
    private String eoId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单类型")
    private String workOrderType;
}
