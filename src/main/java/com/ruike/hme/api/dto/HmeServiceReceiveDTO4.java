package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeServiceReceiveDTO4
 *
 * @author: chaonan.hu@hand-china.com 2020/9/23 11:13:23
 **/
@Data
public class HmeServiceReceiveDTO4 implements Serializable {
    private static final long serialVersionUID = -6547534687450354625L;

    @ApiModelProperty(value = "SN", required = true)
    private String sn;

    @ApiModelProperty(value = "物流单ID", required = true)
    private String logisticsInfoId;

    @ApiModelProperty(value = "站点ID")
    private String siteId;
}
