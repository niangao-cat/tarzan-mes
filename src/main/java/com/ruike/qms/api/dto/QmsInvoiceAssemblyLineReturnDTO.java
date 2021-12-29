package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 订单组件行返回DTO
 * @auther chaonan.hu
 * @date 2020/6/10
 **/
@Getter
@Setter
@ToString
public class QmsInvoiceAssemblyLineReturnDTO implements Serializable {
    private static final long serialVersionUID = 929768041771202619L;

    @ApiModelProperty(value = "行号")
    private String lineNum;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "BOM用量")
    private String bomUsuage;

    @ApiModelProperty(value = "已制单数量")
    private String quantityOrdered;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "制单数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单位")
    private String uomCode;
}
