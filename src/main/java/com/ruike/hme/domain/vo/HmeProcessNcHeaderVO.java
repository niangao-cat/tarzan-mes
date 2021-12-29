package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/01/21 9:52
 */
@Data
public class HmeProcessNcHeaderVO implements Serializable {

    private static final long serialVersionUID = 7189412758486101121L;

    @ApiModelProperty("头表ID")
    private String headerId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("产品代码")
    private String productCode;

    @ApiModelProperty("COS型号")
    private String cosModel;

    @ApiModelProperty("工艺ID")
    private String operationId;

    @ApiModelProperty("工艺编码")
    private String operationName;

    @ApiModelProperty("工艺描述")
    private String description;

    @ApiModelProperty("工序ID")
    private String workcellId;

    @ApiModelProperty("工序编码")
    private String workcellCode;

    @ApiModelProperty("工序描述")
    private String workcellName;

    @ApiModelProperty("芯片组合")
    private String chipCombination;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("状态含义")
    private String statusMeaning;
}
