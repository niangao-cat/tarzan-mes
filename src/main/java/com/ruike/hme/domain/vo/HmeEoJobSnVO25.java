package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnVO25 implements Serializable {
    private static final long serialVersionUID = 4834661086917636009L;
    @ApiModelProperty("物料ID")
    String materialId;
    @ApiModelProperty("站点ID")
    String siteId;
    @ApiModelProperty("fac")
    String fac;
}
