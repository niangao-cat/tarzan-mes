package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderDTO2
 *
 * @author: chaonan.hu@hand-china.com 2020/8/17 19:47:15
 **/
@Data
public class QmsPqcHeaderDTO2 implements Serializable {
    private static final long serialVersionUID = -8728855721535920873L;

    @ApiModelProperty(value = "用户默认站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "事业部Id")
    private String departmentId;

    @ApiModelProperty(value = "车间id")
    private String workshopId;

    @ApiModelProperty(value = "产线id")
    private String prodLineId;
}
