package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 组件日需求数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/19 16:45
 */
@Data
public class WmsComponentDemandDateVO {
    @ApiModelProperty("需求日期")
    private Date requirementDate;
    @ApiModelProperty("需求数量")
    private BigDecimal requirementQty;
    @ApiModelProperty("损耗数量")
    private BigDecimal attritionQty;
}
