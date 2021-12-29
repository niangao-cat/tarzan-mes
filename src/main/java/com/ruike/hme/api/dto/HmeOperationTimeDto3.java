package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeOperationTimeDto3
 * @author: chaonan.hu@hand-china.com 2020-08-12 13:50:47
 **/
@Data
public class HmeOperationTimeDto3 implements Serializable {
    private static final long serialVersionUID = -7762885100204251448L;

    @ApiModelProperty(value = "工艺时效Id", required = true)
    private String operationTimeId;

    @ApiModelProperty(value = "起始日期")
    private Date dateFrom;

    @ApiModelProperty(value = "截止日期")
    private Date dateTo;
}
