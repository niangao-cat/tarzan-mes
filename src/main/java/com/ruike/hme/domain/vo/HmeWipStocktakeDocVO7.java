package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeWipStocktakeDocVO7
 *
 * @author: chaonan.hu@hand-china.com 2021/3/10 09:19:23
 **/
@Data
public class HmeWipStocktakeDocVO7 implements Serializable {
    private static final long serialVersionUID = 6012614423323227092L;

    @ApiModelProperty("盘点单ID")
    private String stocktakeId;

    @ApiModelProperty("盘点单号")
    private String stocktakeNum;

    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("bomID")
    private String bomId;

    @ApiModelProperty("bom编码")
    private String bomName;

    @ApiModelProperty("bom描述")
    private String description;

    @ApiModelProperty("bom版本号")
    private String bomProductionVersion;

    @ApiModelProperty("bom版本描述")
    private String bomProductionVersionDesc;

    @ApiModelProperty("物料Id")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("物料组")
    private String itemGroup;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("产线编码")
    private String prodLineCode;

    @ApiModelProperty("产线名称")
    private String prodLineName;

    @ApiModelProperty("工序ID")
    private String workcellId;

    @ApiModelProperty("工序编码")
    private String workcellCode;

    @ApiModelProperty("工序名称")
    private String workcellName;

    @ApiModelProperty("在制数量")
    private BigDecimal currentQuantity;

    @ApiModelProperty("投料物料ID")
    private String releaseMaterial;

    @ApiModelProperty("投料物料编码")
    private String releaseMaterialCode;

    @ApiModelProperty("投料物料名称")
    private String releaseMaterialName;

    @ApiModelProperty("已投数量")
    private BigDecimal releaseQty;

    @ApiModelProperty("单位ID")
    private String uomId;

    @ApiModelProperty("单位编码")
    private String uomCode;

    @ApiModelProperty("已走报废数量")
    private BigDecimal scrapQty;

    @ApiModelProperty("单位用量")
    private BigDecimal qty;
}
