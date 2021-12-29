package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeProductionPrintVO8
 *
 * @author chaonan.hu@hand-china.com 2021/10/19 20:20
 */
@Data
public class HmeProductionPrintVO8 implements Serializable {
    private static final long serialVersionUID = 733478210489983663L;

    @ApiModelProperty(value = "执行作业ID")
    private String eoId;

    @ApiModelProperty(value = "机型")
    private String modelName;

    @ApiModelProperty(value = "serialNumber")
    private String serialNumber;

    @ApiModelProperty(value = "powerSupply")
    private String powerSupply;

    @ApiModelProperty(value = "内部识别码")
    private String internalCode;

    @ApiModelProperty(value = "netWeight")
    private String netWeight;

    @ApiModelProperty(value = "size")
    private String size;
}
