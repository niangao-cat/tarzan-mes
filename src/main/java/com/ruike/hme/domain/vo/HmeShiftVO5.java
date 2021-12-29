package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeShiftVO5
 *
 * @author chaonan.hu@hand-china.com 2020/07/29 09:22:32
 */
@Data
public class HmeShiftVO5 implements Serializable {
    private static final long serialVersionUID = -2646972018875107520L;

    @ApiModelProperty(value = "产品ID")
    private String materialId;

    @ApiModelProperty(value = "产品名称")
    private String materialName;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单")
    private String workOrderNumber;

    @ApiModelProperty(value = "派工数量")
    private BigDecimal dispatchNumber;

    @ApiModelProperty(value = "本班投产")
    private BigDecimal shiftProduction;

    @ApiModelProperty(value = "本班完成")
    private BigDecimal shiftComplete;

    @ApiModelProperty(value = "不良数")
    private BigDecimal ncNumber;
}
