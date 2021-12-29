package com.ruike.wms.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author yifan.xiong@hand-china.com 2021/02/24 17:05
 */
@Data
public class WmsDistributionBasicDataDTO1 implements Serializable {

    @ApiModelProperty(value = "安全库存配送量")
    private BigDecimal everyQty;

    @ApiModelProperty(value = "工段ID")
    private String workcellId;

    @ApiModelProperty(value = "是否启用线边库存计算逻辑")
    private String backflushFlag;

    @ApiModelProperty(value = "产线ID", required = true)
    private String prodLineId;
}
