package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

@Data
public class HmeEoJobSnBatchVO21 extends MtMaterialLot implements Serializable {
    private static final long serialVersionUID = -5878024185649790854L;
    @ApiModelProperty(value = "站点编码")
    String siteCode;
    @ApiModelProperty(value = "库位编码")
    String locatorCode;
    @ApiModelProperty(value = "区域库位ID")
    String areaLocatorId;
    @ApiModelProperty(value = "区域库位编码")
    String areaLocatorCode;
}
