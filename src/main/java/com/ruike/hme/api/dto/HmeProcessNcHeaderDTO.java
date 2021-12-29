package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工序不良判定标准查询条件
 *
 * @author li.zhang 2021/01/21 9:49
 */
@Data
public class HmeProcessNcHeaderDTO implements Serializable {

    private static final long serialVersionUID = -4335904868166239357L;
    
    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("产品代码")
    private String productCode;

    @ApiModelProperty("COS型号")
    private String cosModel;

    @ApiModelProperty("工艺ID")
    private String operationId;

    @ApiModelProperty("工序ID")
    private String workcellId;

    @ApiModelProperty("芯片组合")
    private String chipCombination;

    @ApiModelProperty("状态")
    private String status;

}
