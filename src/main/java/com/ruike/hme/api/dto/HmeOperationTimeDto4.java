package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeOperationTimeDto4
 * @author: chaonan.hu@hand-china.com 2020-08-12 14:40:27
 **/
@Data
public class HmeOperationTimeDto4 implements Serializable {
    private static final long serialVersionUID = 4953959292940141279L;

    @ApiModelProperty(value = "时效编码")
    private String timeCode;

    @ApiModelProperty(value = "时效名称")
    private String timeName;

    @ApiModelProperty(value = "时效要求时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "工艺名称")
    private String description;

    @ApiModelProperty(value = "工位名称")
    private String workcellName;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
}
