package com.ruike.wms.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 12:29
 */
@Data
public class WmsInvOnhandQtyRecordDTO3 implements Serializable {


    private static final long serialVersionUID = 6041193573842654621L;
    @ApiModelProperty(value = "显示日期")
    private String showDate;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "入库数量")
    private String sumInQty;
    @ApiModelProperty(value = "出库数量")
    private String sumOutQty;
    @ApiModelProperty(value = "当前库存")
    private String sumInvRecordQty;
}
