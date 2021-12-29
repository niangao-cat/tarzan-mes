package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 成品备料 物料批
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 13:57
 */
@Data
public class WmsProdPrepareMaterialLotVO {
    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "物料批数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "匹配组键，格式为：站点ID#物料ID#版本#单位ID#仓库ID#SO#SO行")
    private String matchGroupKey;

    @ApiModelProperty(value = "预分配行列表")
    List<WmsProdPrepareAssignVO> assignList;

    @ApiModelProperty(value = "预分配行列表")
    private int sort;

    public static WmsProdPrepareMaterialLotVO newInstance(String materialLotId, String materialId, String uomId, String warehouseId, String materialLotCode, BigDecimal quantity, String matchGroupKey, List<WmsProdPrepareAssignVO> assignList) {
        WmsProdPrepareMaterialLotVO obj = new WmsProdPrepareMaterialLotVO();
        obj.materialLotId = materialLotId;
        obj.materialId = materialId;
        obj.uomId = uomId;
        obj.warehouseId = warehouseId;
        obj.materialLotCode = materialLotCode;
        obj.quantity = quantity;
        obj.matchGroupKey = matchGroupKey;
        obj.assignList = assignList;
        return obj;
    }
}
