package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/7 11:29
 */
@Data
public class HmeFacYkImportVO implements Serializable {

    private static final long serialVersionUID = 1517950399532136629L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "FAC物料编码")
    private String facMaterialCode;

    @ApiModelProperty(value = "FAC物料ID")
    private String facMaterialId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "标准值")
    private BigDecimal standardValue;

    @ApiModelProperty(value = "允差")
    private BigDecimal allowDiffer;

    @ApiModelProperty(value = "导入方式")
    private String importType;
}
