package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * WmsLibraryAgeReportDTO2
 *
 * @author: chaonan.hu@hand-china.com 2020/11/18 19:37:56
 **/
@Data
public class WmsLibraryAgeReportDTO3 implements Serializable {
    private static final long serialVersionUID = -5434866916196737983L;

    @ApiModelProperty(value = "库龄区间小值")
    private long minLibraryAge;

    @ApiModelProperty(value = "库龄区间大值")
    private long maxLibraryAge;
}
