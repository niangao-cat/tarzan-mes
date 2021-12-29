package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 配送需求导出 数量
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/18 15:36
 */
@Data
public class WmsDistributionDemandExportQtyVO {
    @ApiModelProperty("班次日期")
    private Date shiftDate;
    @ApiModelProperty(value = "需求数量")
    private BigDecimal requirementQty;
}
