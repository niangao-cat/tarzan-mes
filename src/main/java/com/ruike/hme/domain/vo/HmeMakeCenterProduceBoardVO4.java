package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/30 22:59
 */
@Data
public class HmeMakeCenterProduceBoardVO4 implements Serializable {

    private static final long serialVersionUID = 7664872406616634843L;

    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("工段")
    private String lineWorkcellName;
    @ApiModelProperty("工段")
    private String lineWorkcellId;
    @ApiModelProperty("产线")
    private String prodLineId;
    @ApiModelProperty("首序")
    private String firstProcessId;
    @ApiModelProperty("末序")
    private String endProcessId;
    @ApiModelProperty("工单")
    private String workOrderId;
}
