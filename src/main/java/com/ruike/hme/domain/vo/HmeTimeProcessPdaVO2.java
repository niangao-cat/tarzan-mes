package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeTimeProcessPdaVO2
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 14:45:54
 **/
@Data
public class HmeTimeProcessPdaVO2 implements Serializable {
    private static final long serialVersionUID = -1933226613950751680L;

    @ApiModelProperty(value = "条码总数")
    private Long barcodeCount;

    @ApiModelProperty(value = "物料总数")
    private BigDecimal materialCount;

    @ApiModelProperty(value = "主单位")
    private String uomCode;

    @ApiModelProperty(value = "进站点击标识")
    private int flag;

    private List<HmeTimeProcessPdaVO3> materialDataList;

}
