package com.ruike.qms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 外协发货单头数据返回DTO
 * @auther chaonan.hu
 * @date 2020/6/10
 **/
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class QmsInvoiceHeadReturnDTO implements Serializable {
    private static final long serialVersionUID = 2559355959219007031L;

    @ApiModelProperty(value = "工厂")
    private String siteName;

    @ApiModelProperty(value = "发货单号")
    private String number;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商描述")
    private String supplierName;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WMS.INSTRUCTION_STATUS2", meaningField = "stateMeaning")
    private String state;

    @ApiModelProperty(value = "单据状态含义")
    private String stateMeaning;

    @ApiModelProperty(value = "单据类型")
    @LovValue(lovCode = "WMS.INSTRUCTION_DOC_TYPE", meaningField = "typeMeaning")
    private String type;

    @ApiModelProperty(value = "单据类型含义")
    private String typeMeaning;

    @ApiModelProperty(value = "到货时间")
    private Date earilyDemandTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String userName;

    @ApiModelProperty(value = "收货地址")
    private String receivingAddress;

}
