package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosGetChipMaterialLotConfirmDTO
 *
 * @author: chaonan.hu@hand-china.com 2020-11-03 16:00
 **/
@Data
public class HmeCosGetChipMaterialLotConfirmDTO implements Serializable {
    private static final long serialVersionUID = 265601510534001886L;

    @ApiModelProperty("物料批条码ID")
    private String materialLotId;

    @ApiModelProperty("物料批条码")
    private String materialLotCode;

    @ApiModelProperty("来料数量")
    private Long primaryUomQty;

    @ApiModelProperty("EoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("状态")
    private String status;
}
