package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeOperationTimeDto
 * @author: chaonan.hu@hand-china.com 2020-08-11 20:17:38
 **/
@Data
public class HmeOperationTimeDto implements Serializable {
    private static final long serialVersionUID = -2172450437219392590L;

    @ApiModelProperty(value = "工艺时效Id", required = true)
    private String operationTimeId;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "版本名称")
    private String description;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "是否启用")
    private String enableFlag;

}
