package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/14 11:07
 */
@Data
public class WmsStocktakeCostCenterVO {
    @ApiModelProperty("盘点单ID")
    private String stocktakeId;
    @ApiModelProperty("盘点单号")
    private String stocktakeNum;
    @ApiModelProperty("成本中心ID")
    private String costcenterId;
    @ApiModelProperty("成本中心编码")
    private String costcenterCode;
}
