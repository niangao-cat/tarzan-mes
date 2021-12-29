package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeEoJobSnDTO4
 *
 * @author: chaonan.hu@hand-china.com 2021/02/23 10:07:23
 **/
@Data
public class HmeEoJobSnDTO4 implements Serializable {
    private static final long serialVersionUID = 4661355346869167255L;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty("条码")
    private String identification;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("工艺路线ID")
    private String routerId;

    @ApiModelProperty("装配清单")
    private String bomName;
}
