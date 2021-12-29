package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnVO21 implements Serializable {
    private static final long serialVersionUID = 5826035077849405901L;
    @ApiModelProperty("投料条码编码")
    private String materialLotCode;
    @ApiModelProperty("投料物料ID")
    private String materialId;
    @ApiModelProperty(value = "升级标识")
    private String upgradeFlag;
}
