package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class HmeCosFunctionMaterialVO implements Serializable {
    private static final long serialVersionUID = -868923462582383215L;

    @ApiModelProperty("工艺ID")
    private String operationId;

    @ApiModelProperty("alias")
    private String alias;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("数据采集项编码")
    private String tagCode;
}
