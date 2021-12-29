package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeProductionPrintVO10
 *
 * @author chaonan.hu@hand-china.com 2021/11/15 10:26
 */
@Data
public class HmeProductionPrintVO10 implements Serializable {
    private static final long serialVersionUID = 5365851941840187670L;

    @ApiModelProperty(value = "物料序列号")
    private String snNum;

    @ApiModelProperty(value = "泵浦源条码")
    private String sn2;

    @ApiModelProperty(value = "子条码")
    private String sn1;

    @ApiModelProperty(value = "打印张数")
    private Long printNumber;
}
