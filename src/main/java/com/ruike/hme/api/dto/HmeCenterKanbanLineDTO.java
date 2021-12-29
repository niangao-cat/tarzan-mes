package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCenterKanbanLineDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/05/28 15:02:12
 **/
@Data
public class HmeCenterKanbanLineDTO implements Serializable {
    private static final long serialVersionUID = -5881655438278292093L;

    @ApiModelProperty(value = "头主键", required = true)
    private String centerKanbanHeaderId;

    @ApiModelProperty(value = "行主键,传则更新,不传则新增")
    private String centerKanbanLineId;

    @ApiModelProperty(value = "工序ID", required = true)
    private String workcellId;

    @ApiModelProperty(value = "直通率", required = true)
    private BigDecimal throughRate;

    @ApiModelProperty(value = "有效性", required = true)
    private String enableFlag;
}
