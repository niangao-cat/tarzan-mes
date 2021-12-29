package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class HmeCosWireBondVO2 implements Serializable {
    private static final long serialVersionUID = -3161146301971715821L;

    private String materialId;

    private String materialCode;

    private String materialName;
    @ApiModelProperty(value = "需求数量")
    private Double demandQty;
    @ApiModelProperty(value = "条码数量")
    private Double materialLotQty;
    @ApiModelProperty(value = "已投入数量")
    private Double InvestedQty;

    private String uomCode;

    @ApiModelProperty(value = "1代表解绑，2代表成功")
    private Long flag;

    private String materialLotId;

    private String materialLotCode;

}
