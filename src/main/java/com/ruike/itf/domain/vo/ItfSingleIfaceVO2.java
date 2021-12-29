package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ItfSingleIfaceVO
 *
 * @author: chaonan.hu@hand-china.com 2021-09-27 10:08:12
 **/
@Data
public class ItfSingleIfaceVO2 implements Serializable {
    private static final long serialVersionUID = 7894973231677188211L;

    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;
}
