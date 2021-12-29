package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tarzan.stocktake.domain.entity.MtStocktakeRange;

import java.io.Serializable;

/**
 * 库存盘点范围 查询VO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 10:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WmsStocktakeRangeVO extends MtStocktakeRange implements Serializable {

    private static final long serialVersionUID = 7772484720793385499L;

    @ApiModelProperty("范围对象编码")
    private String rangeObjectCode;
    @ApiModelProperty("范围对象描述")
    private String rangeObjectName;
}
