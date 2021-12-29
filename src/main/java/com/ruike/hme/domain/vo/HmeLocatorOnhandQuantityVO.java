package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;

/**
 * 库位现有量
 *
 * @author penglin.sui@hand-china.com 2020/10/09 16:16
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeLocatorOnhandQuantityVO extends MtInvOnhandQuantity implements Serializable {
    private static final long serialVersionUID = -7924438704837258798L;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库编码")
    private String warehouseCode;
    @ApiModelProperty("仓库描述")
    private String warehouseName;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("库位编码")
    private String locatorCode;
    @ApiModelProperty("库位描述")
    private String locatorName;
    @ApiModelProperty("单位ID")
    private String primaryUomId;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("单位描述")
    private String uomName;
}