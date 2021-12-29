package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 12:29
 */
@Data
public class WmsInvOnhandQtyRecordDTO implements Serializable {

    private static final long serialVersionUID = 3827200270590030662L;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "租户ID")
    private String materialId;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "出库数量")
    private BigDecimal outQty;
    @ApiModelProperty(value = "入库数量")
    private BigDecimal inQty;
    @ApiModelProperty(value = "保留数量")
    private BigDecimal invRecordQty;
}
