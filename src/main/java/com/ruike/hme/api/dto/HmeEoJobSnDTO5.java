package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeEoJobSnDTO5
 *
 * @author: chaonan.hu@hand-china.com 2021/02/23 10:18:34
 **/
@Data
public class HmeEoJobSnDTO5 implements Serializable {
    private static final long serialVersionUID = 5294500496838516945L;

    @ApiModelProperty("主键")
    private String jobId;

    @ApiModelProperty("eoID")
    private String eoId;

    @ApiModelProperty("条码")
    private String identification;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("进站时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteInDate;

    @ApiModelProperty("工艺路线ID")
    private String routerId;

    @ApiModelProperty("工艺路线")
    private String routerName;

    @ApiModelProperty("装配清单ID")
    private String bomId;

    @ApiModelProperty("装配清单")
    private String bomName;
}
