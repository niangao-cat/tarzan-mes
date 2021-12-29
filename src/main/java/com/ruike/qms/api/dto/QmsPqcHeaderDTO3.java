package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderDTO3
 *
 * @author: chaonan.hu@hand-china.com 2020/8/17 20:41:13
 **/
@Data
public class QmsPqcHeaderDTO3 implements Serializable {
    private static final long serialVersionUID = -5195731900051224079L;

    @ApiModelProperty(value = "用户默认站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "产线Id", required = true)
    private String prodLineId;
}
