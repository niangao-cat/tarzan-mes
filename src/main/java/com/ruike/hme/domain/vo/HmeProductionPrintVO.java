package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author wengang.qiang@hand-china.com 2021/09/23 9:48
 */
@Data
public class HmeProductionPrintVO implements Serializable {

    private static final long serialVersionUID = 736903842749346901L;

    @ApiModelProperty(value = "物料序列号", required = true)
    private String snNum;

    @ApiModelProperty(value = "工单ID", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "sap料号", required = true)
    private String sapMaterial;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

}
