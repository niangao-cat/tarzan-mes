package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName WmsMaterialReturnConfirmDTO2
 * @Deacription
 * @Author ywz
 * @Date 2020/4/22 17:52
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsMaterialReturnConfirmDTO2 extends MtMaterialLot implements Serializable {

    private static final long serialVersionUID = -6493175405623655244L;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料单位编码")
    private String primaryUomCode;

    @ApiModelProperty(value = "接收货位ID")
    private String toLocatorId;

    @ApiModelProperty(value = "接收货位ID")
    private String locatorId;

    @ApiModelProperty(value = "接收货位编码")
    private String toLocatorCode;

    @ApiModelProperty(value = "执行数量")
    private BigDecimal executeQty;
}
