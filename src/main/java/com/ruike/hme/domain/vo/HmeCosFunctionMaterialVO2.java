package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeCosFunctionMaterialVO2 implements Serializable {
    private static final long serialVersionUID = -2616374679939411135L;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("电流")
    private String current;

    @ApiModelProperty("芯片序列号")
    private String loadSequence;
}
