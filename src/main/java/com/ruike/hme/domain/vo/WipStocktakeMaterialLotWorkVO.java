package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 在制盘点物料批生产属性
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 16:52
 */
@Data
public class WipStocktakeMaterialLotWorkVO {
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料编码")
    private String materialName;
    @ApiModelProperty("物料编码")
    private String materialVersion;
    @ApiModelProperty("单位Id")
    private String uomId;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("账面数量")
    private BigDecimal quantity;
    @ApiModelProperty("工单Id")
    private String workOrderId;
    @ApiModelProperty("盘点产线Id")
    private String prodLineId;
    @ApiModelProperty("盘点产线")
    private String prodLineCode;
    @ApiModelProperty("盘点工序Id")
    private String workcellId;
    @ApiModelProperty("盘点工序")
    private String workcellCode;
}
