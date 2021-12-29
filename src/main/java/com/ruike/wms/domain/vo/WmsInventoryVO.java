package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/26 12:26
 */
@Data
public class WmsInventoryVO implements Serializable {

    private static final long serialVersionUID = -5764346514656977328L;

    @ApiModelProperty(value = "行id")
    private String instructionId;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "库存数")
    private BigDecimal inventoryQty;

}
