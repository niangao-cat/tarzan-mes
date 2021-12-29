package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tarzan.instruction.domain.entity.MtInstruction;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 采购订单行DTO
 * @auther chaonan.hu
 * @date 2020/6/10
 **/
@Getter
@Setter
@ToString
public class QmsInvoiceInstructionDTO extends MtInstruction implements Serializable {
    private static final long serialVersionUID = -8649608803960468373L;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "前台传入的订单数量")
    private BigDecimal quantityUi;
}
