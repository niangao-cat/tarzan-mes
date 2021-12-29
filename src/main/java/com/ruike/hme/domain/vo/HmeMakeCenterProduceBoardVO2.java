package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/28 18:26
 */
@Data
public class HmeMakeCenterProduceBoardVO2 implements Serializable {

    private static final long serialVersionUID = 3870048695898909272L;

    @ApiModelProperty("排序")
    private Integer sortNum;
    @ApiModelProperty("工序")
    @JsonIgnore
    private String processId;
    @ApiModelProperty("工单")
    @JsonIgnore
    private String workOrderId;
    @ApiModelProperty("产线")
    @JsonIgnore
    private String prodLineId;
    @ApiModelProperty("物料id")
    private String snMaterialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("工段")
    private String lineWorkcellName;
    @ApiModelProperty("工段ID")
    private String lineWorkcellId;
    @ApiModelProperty("派工数量")
    private BigDecimal dispatchQty;
    @ApiModelProperty("实际投产")
    private BigDecimal actualQty;
    @ApiModelProperty("在制数")
    private BigDecimal wipQty;
    @ApiModelProperty("实际交付")
    private BigDecimal actualDeliverQty;
    @ApiModelProperty("计划达成率")
    private String planReachRate;
}
