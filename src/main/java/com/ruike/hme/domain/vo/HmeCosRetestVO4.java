package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/20 13:58
 */
@Data
public class HmeCosRetestVO4 implements Serializable {

    private static final long serialVersionUID = -6691054203306192575L;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "工单")
    private String workOrderId;

    @ApiModelProperty("物料")
    private String materialId;

    @ApiModelProperty("剩余COS数据")
    private BigDecimal remainingQty;
}
