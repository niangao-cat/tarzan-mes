package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 配送需求替代料
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 13:53
 */
@Data
public class WmsDistributeSubstitutionVO {
    @ApiModelProperty("配送需求ID")
    private String distDemandId;
    @ApiModelProperty("替代物料ID")
    private String materialId;
    @ApiModelProperty("替代料编码")
    private String materialCode;
    @ApiModelProperty("替代料数量")
    private BigDecimal substituteQty;
    @ApiModelProperty("班次日期")
    private String calendarShiftId;
}
