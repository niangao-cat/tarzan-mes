package com.ruike.qms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 查询订单组件返回DTO
 * @auther chaonan.hu
 * @date 2020/6/10
*/
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class QmsInvoiceAssemblyReturnDTO implements Serializable {
    private static final long serialVersionUID = -268689145888916751L;

    @ApiModelProperty(value = "订单组件头数据")
    private QmsInvoiceAssemblyHeadReturnDTO qmsInvoiceAssemblyHeadReturnDTO;

    @ApiModelProperty(value = "订单组件行数据")
    private List<QmsInvoiceAssemblyLineReturnDTO> qmsInvoiceAssemblyLineReturnDTOS;

}
