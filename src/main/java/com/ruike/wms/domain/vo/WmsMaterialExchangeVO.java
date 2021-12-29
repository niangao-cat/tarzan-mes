package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 料废调换-库存调拨VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/9/2 9:58
 */
@Data
public class WmsMaterialExchangeVO implements Serializable {

    private static final long serialVersionUID = 4786695299708989641L;

    @ApiModelProperty("站点")
    private String siteId;

    @ApiModelProperty("货位id")
    private String locatorId;

    @ApiModelProperty("物料id")
    private String materialId;

    @ApiModelProperty("发出数量")
    private BigDecimal executeQty;

    @ApiModelProperty("增加数量")
    private BigDecimal addQty;

    @ApiModelProperty("行id")
    private String instructionId;
}
