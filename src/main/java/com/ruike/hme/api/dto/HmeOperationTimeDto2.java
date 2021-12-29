package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeOperationTimeDto2
 * @author: chaonan.hu@hand-china.com 2020-08-12 10:13:25
 **/
@Data
public class HmeOperationTimeDto2 implements Serializable {
    private static final long serialVersionUID = -1537059520653627626L;

    @ApiModelProperty(value = "工艺时效Id", required = true)
    private String operationTimeId;

    @ApiModelProperty(value = "对象类型")
    private String objectType;

    @ApiModelProperty(value = "对象编码")
    private String objectCode;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
}
