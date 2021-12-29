package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tarzan.stocktake.domain.vo.MtStocktakeDocVO2;

import java.io.Serializable;
import java.util.List;

/**
 * 库存盘点单据 更新DTO
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 20:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("库存盘点单据更新")
public class WmsStocktakeDocUpdateDTO extends MtStocktakeDocVO2 implements Serializable {
    private static final long serialVersionUID = 2543033353894599947L;

    @ApiModelProperty("物料范围列表")
    private List<WmsStocktakeRangeVO> materialRangeList;
    @ApiModelProperty("货位范围列表")
    private List<WmsStocktakeRangeVO> locatorRangeList;
}
