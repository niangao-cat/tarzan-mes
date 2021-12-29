package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/20 15:29
 */
@Data
public class HmeCosRetestVO7 implements Serializable {

    private static final long serialVersionUID = 2134349564383091021L;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "物料批")
    private String materialLotCode;

    @ApiModelProperty(value = "数量")
    private Double cosNum;
}
