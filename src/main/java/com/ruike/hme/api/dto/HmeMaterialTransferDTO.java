package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-09 12:00
 */
@Data
public class HmeMaterialTransferDTO {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "数量")
    private Double quantity;
    @ApiModelProperty(value = "转移数量")
    private Double transferQuantity;
    @ApiModelProperty(value = "输入次数")
    private Long inputTimes;
    @ApiModelProperty(value = "扣减数量")
    private BigDecimal deductionQty;
    @ApiModelProperty("当前容器ID")
    @JsonIgnore
    private String currentContainerId;
}
