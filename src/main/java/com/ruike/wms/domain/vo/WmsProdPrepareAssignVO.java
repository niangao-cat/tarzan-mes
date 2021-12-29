package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 成品备料 预分配结果
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 13:57
 */
@Data
public class WmsProdPrepareAssignVO {
    @ApiModelProperty("行ID")
    private String instructionId;
    @ApiModelProperty("行号")
    private Integer instructionLineNum;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "发货仓库ID")
    private String fromLocatorId;
    @ApiModelProperty(value = "发货站点ID")
    private String fromSiteId;
    @ApiModelProperty("剩余需求数量")
    private BigDecimal remainingDemandQty;
    @ApiModelProperty("分配数量")
    private BigDecimal assignQty;
    @ApiModelProperty("允差上限")
    private BigDecimal toleranceUpperLimit;

    public static WmsProdPrepareAssignVO newInstance(String instructionId, Integer instructionLineNum, String materialId, String uomId, String fromLocatorId, String fromSiteId, BigDecimal remainingDemandQty, BigDecimal assignQty, BigDecimal toleranceUpperLimit) {
        WmsProdPrepareAssignVO obj = new WmsProdPrepareAssignVO();
        obj.instructionId = instructionId;
        obj.instructionLineNum = instructionLineNum;
        obj.materialId = materialId;
        obj.uomId = uomId;
        obj.fromLocatorId = fromLocatorId;
        obj.fromSiteId = fromSiteId;
        obj.remainingDemandQty = remainingDemandQty;
        obj.assignQty = assignQty;
        obj.toleranceUpperLimit = toleranceUpperLimit;
        return obj;
    }
}
