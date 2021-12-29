package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单组件头返回DTO
 *
 * @author chaonan.hu
 * @date 2020/6/10
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class QmsInvoiceAssemblyHeadReturnDTO implements Serializable {
    private static final long serialVersionUID = 7442050419192825698L;

    @ApiModelProperty(value = "采购订单号")
    private String instructionDocNum;

    @ApiModelProperty(value = "行号")
    private String lineNum;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "制单数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单位")
    private String uomCode;
}
